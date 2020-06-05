package com.fx.commons.hiberantedao.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;

/**
 * hibernate工具类 Criteria方式获取数据
 */
public class HibernateUtils {
	/**
	 * 根据Criterion条件创建Criteria.
	 */
	@SuppressWarnings("deprecation")
	public static Criteria createCriteria(Session session, Class<?> entityClass, Criterion... criterions) {
		Criteria criteria = session.createCriteria(entityClass);
		for (Criterion criterion : criterions) {
			criteria.add(criterion);
		}
		return criteria;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象
	 * @param session
	 * @param hql hql查询字符串
	 * @param values 参数列表
	 * @return query
	 */
	@SuppressWarnings("rawtypes")
	public static Query createQuery(Session session, String hql, Object... values) {
		Assert.hasText(hql, "hql不能为空");
		
		Query query = session.createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			if(values[i].toString().contains("LIMIT")){//返回条数限制
				query.setMaxResults(Integer.parseInt(values[i].toString().split(" ")[1]));//设置获取的数量
			}else{
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象
	 * @param session
	 * @param hql hql查询字符串
	 * @param values Map键值类型的参数列表
	 * @return query
	 */
	@SuppressWarnings("rawtypes")
	public static Query createQuery(Session session, String hql, Map<String, ?> values) {
		Assert.hasText(hql, "hql不能为空");
		
		Query query = session.createQuery(hql);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}
	
	/**
	 * 设置分页参数到Criteria对象
	 */
	public static Criteria setParameter(Criteria criteria, Page<?> pageData){
		//第一步：设置查询条件
		setFiltrationParameter(criteria, pageData.getFiltrations());
		//第二步：读取记录总数
		if (pageData.getPagination().isReadTotalCount()){
			long totalCount = countCriteriaResult(criteria);
			pageData.getPagination().setTotalCount(totalCount);
		}
		//第三步：设置查询范围
		criteria.setFirstResult(pageData.getPagination().getCurrentlyPageFirstResoultIndex());
		criteria.setMaxResults(pageData.getPagination().getPageSize());
		//排序条件
		setCompositorParameter(criteria, pageData.getCompositor());
		//设置分组条件
		//setProjectionGroup(criteria,pageData.getGroupName());
		return criteria;
	}
	
	/**
	 * 设置分页参数到Criteria对象(可多条件排序)
	 * @param criteria
	 * @param pageData
	 * @return
	 */
	public static Criteria setParameters(Criteria criteria, Page<?> pageData){
		//第一步：设置查询条件
		setFiltrationParameter(criteria, pageData.getFiltrations());
		//第二步：读取记录总数
		if (pageData.getPagination().isReadTotalCount()){
			long totalCount = countCriteriaResult(criteria);
			pageData.getPagination().setTotalCount(totalCount);
		}
		//第三步：设置查询范围
		criteria.setFirstResult(pageData.getPagination().getCurrentlyPageFirstResoultIndex());
		criteria.setMaxResults(pageData.getPagination().getPageSize());
		//排序条件(可多条件排序)
		setCompositorParameters(criteria, pageData.getCompositors());
		return criteria;
	}
	
	/**
	 * 设置过滤条件到Criteria对象
	 */
	public static Criteria setFiltrationParameter(Criteria criteria, 
		Filtration... filtrations){
		if (filtrations.length > 0){
			List<Criterion> criterions = new ArrayList<Criterion>();
			for (Filtration filtration : filtrations){
				Criterion criterion = null;
				if (!filtration.isMultiFilter()){
					if(filtration.getMatchType() == MatchType.AND){// 多个and条件
						// 传入的值是List<Filtration>泛型类型，进行or处理
						@SuppressWarnings("unchecked")
						List<Filtration> fils = (List<Filtration>)filtration.getFieldValue();
						
						Conjunction cj = Restrictions.conjunction();
						for (Filtration fil : fils){
							criterion = createCriterion(fil.getFieldName(), fil.getFieldValue(), fil.getMatchType());
							cj.add(criterion);
						}
						criterions.add(cj);
					}else if(filtration.getMatchType() == MatchType.OR){// 多个or条件
						// 传入的值是List<Filtration>泛型类型，进行or处理
						@SuppressWarnings("unchecked")
						List<Filtration> fils = (List<Filtration>)filtration.getFieldValue();
						
						Disjunction dj = Restrictions.disjunction();
						for (Filtration fil : fils){
							if(fil.getMatchType() == MatchType.AND){// 多个and条件
								@SuppressWarnings("unchecked")
								List<Filtration> ftand = (List<Filtration>)fil.getFieldValue();
								
								Conjunction ands = Restrictions.conjunction();
								for (Filtration fa : ftand){
									criterion = createCriterion(fa.getFieldName(), fa.getFieldValue(), fa.getMatchType());
									ands.add(criterion);
								}
								
								dj.add(ands);
							}else if(fil.getMatchType() == MatchType.OR){// 多个or条件
								@SuppressWarnings("unchecked")
								List<Filtration> ftor = (List<Filtration>)fil.getFieldValue();
								
								Disjunction ors = Restrictions.disjunction();
								for (Filtration fo : ftor){
									criterion = createCriterion(fo.getFieldName(), fo.getFieldValue(), fo.getMatchType());
									ors.add(criterion);
								}
								
								dj.add(ors);
							}else{
								criterion = createCriterion(fil.getFieldName(), fil.getFieldValue(), fil.getMatchType());
								dj.add(criterion);
							}
						}
						criterions.add(dj);
					}else{// 单个条件
						criterion = createCriterion(filtration.getFieldName(), filtration.getFieldValue(), filtration.getMatchType());
						criterions.add(criterion);
					}
				} else {
					//包含多个属性需要比较的情况,进行or处理.
					Disjunction disjunction = Restrictions.disjunction();
					for (String filedName : filtration.getFieldNames()){
						criterion = createCriterion(filedName, filtration.getFieldValue(), filtration.getMatchType());
						disjunction.add(criterion);
					}
					criterions.add(disjunction);
				}
			}
			for (Criterion criterion : criterions){
				criteria.add(criterion);
			}
		}
		return criteria;
	}
	
	/**
	 * 设置过滤条件到Criteria对象
	 */
	public static Criteria setProjectionGroup(Criteria criteria, String GroupName){
		if (StringUtils.isNotEmpty(GroupName)){
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty(GroupName));//需要分组的字段
			projectionList.add(Projections.rowCount());//每组的记录条数
			criteria.setProjection(projectionList);
			return criteria;
		} else {
			return criteria;
		}
	}
	
	/**
	 * 设置过滤条件到Criteria对象
	 */
	public static Criteria setFiltrationParameter(Criteria criteria, 
			List<Filtration> filtrationList){
		if (filtrationList != null){
			Filtration[] filtrations = new Filtration[filtrationList.size()];
			for(int i =0;i<filtrationList.size();i++){
				filtrations[i] = filtrationList.get(i);
			}
			return setFiltrationParameter(criteria, filtrations);
		} else {
			return criteria;
		}
	}
	
	/**
	 * 创建Criterion
	 */
	@SuppressWarnings("incomplete-switch")
	private static Criterion createCriterion(String fieldName, 
			Object fieldValue, MatchType matchType){
		Criterion criterion = null;
		Assert.hasText(fieldName, "fieldName不能为空");
		switch (matchType){
			case ISNULL: // is null
				criterion = Restrictions.isNull(fieldName);
				break;
			case ISNOTNULL: // is not null
				criterion = Restrictions.isNotNull(fieldName);
				break;
			case EQ: // =
				criterion = Restrictions.eq(fieldName, fieldValue);
				break;
			case NE: // <>
				criterion = Restrictions.ne(fieldName, fieldValue);
				break;
			case LIKE: // like “%”
				criterion = Restrictions.like(fieldName, (String) fieldValue, MatchMode.ANYWHERE);
				break;
			case NOTLIKE: // not like
				criterion = Restrictions.sqlRestriction(fieldName+" not like "+fieldValue+"");
				break;
			case LIKE_: // like “_”
				criterion = Restrictions.like(fieldName, (String) fieldValue);
				break;
			case IN: // IN
				criterion = Restrictions.in(fieldName, (Object[])fieldValue);
				break;
			case NOTIN: // NOTIN
				criterion = Restrictions.not(Restrictions.in(fieldName, (Object[])fieldValue));
				break;
			case LT: // <
				criterion = Restrictions.lt(fieldName, fieldValue);
				break;
			case LE: // <=
				criterion = Restrictions.le(fieldName, fieldValue);
				break;
			case GT: // >
				criterion = Restrictions.gt(fieldName, fieldValue);
				break;
			case GE: // >=
				criterion = Restrictions.ge(fieldName, fieldValue);
				break;
			case SQL: // sql
				criterion = Restrictions.sqlRestriction(fieldName);
				break;
		}
		return criterion;
	}
	
	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings("unchecked")
	private static long countCriteriaResult(Criteria criteria){
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer resultTransformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries = null;
		orderEntries = (List<CriteriaImpl.OrderEntry>) ReflectionUtils.getFieldValue(impl, "orderEntries");
		ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList<CriteriaImpl.OrderEntry>());
		// 执行Count查询
		long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null){
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (resultTransformer != null){
			criteria.setResultTransformer(resultTransformer);
		}
		ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		return totalCount;
	}
	
	/**
	 * 设置排序参数到Criteria对象
	 */
	public static Criteria setCompositorParameter(Criteria criteria, 
			Compositor compositor){
		if (compositor != null){
			String fieldName = compositor.getFieldName();
			CompositorType compositorType = compositor.getCompositorType();
			switch (compositorType){
			case ASC:
				criteria.addOrder(Order.asc(fieldName));
				break;
			case DESC:
				criteria.addOrder(Order.desc(fieldName));
				break;
			}
		}
		return criteria;
	}
	
	/**
	 * 设置排序参数到Criteria对象(可多条件排序)
	 * @param criteria
	 * @param compositors 排序条件列表
	 * @return
	 */
	public static Criteria setCompositorParameters(Criteria criteria, List<Compositor> compositors){
		if (compositors.size() > 0){
			for(int i = 0; i < compositors.size(); i++){
				String fieldName = compositors.get(i).getFieldName();
				CompositorType compositorType = compositors.get(i).getCompositorType();
				switch (compositorType)
				{
				case ASC:
					criteria.addOrder(Order.asc(fieldName));
					break;
				case DESC:
					criteria.addOrder(Order.desc(fieldName));
					break;
				}
			}
		}
		return criteria;
	}
	
	@SuppressWarnings("rawtypes")
	public static Query createQueryIns(Session session, String hql, Object... values){
		Assert.hasText(hql, "hql不能为空");
		
		Query query = session.createQuery(hql);
		//处理只有一个参数值且是数组，该查询是in查询
		//（原因：当单独一个in查询时，传入的参数是Object[]类型，但是values也是Object[]类型，而只定义了一个:v0，所以会出现找不到:v1参数名[could not locate named parameter [v1]]错误）
		int v_len = hql.split(":v").length;
		if(v_len == 2){
			query.setParameterList("v0", (Object[])values);
		}else{
			for (int j = 0; j < values.length; j++){
				if(values[j] instanceof Object[]){
					query.setParameterList("v"+j, (Object[])values[j]);
				}else{
					query.setParameter("v"+j, values[j]);
				}
			}
		}
		return query;
	}
	
}