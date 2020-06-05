package com.fx.commons.hiberantedao.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.fx.commons.hiberantedao.pagingcom.Page;


/**
 * 数据接口基类 实现类
 * @param <T> 类名
 * @param <PK> 类序列号
 */
@SuppressWarnings("unchecked")
public class ZBaseDaoImpl<T, PK extends Serializable> extends HibernateDaoSupport implements ZBaseDao<T, Serializable> {
	
	@Autowired
    private EntityManagerFactory entityManagerFactory;

	@Override
    public Session getCurrentSession() {
        return entityManagerFactory.unwrap(SessionFactory.class).getCurrentSession();
    }
    
    @Override
    public Session openSession() {
        return entityManagerFactory.unwrap(SessionFactory.class).openSession();
    }
    
//	@Autowired
//    private SessionFactory sessionFactory;
//
//	@Override
//    public Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//	
//	@Override
//	public Session openSession() {
//		return sessionFactory.openSession();
//	}
	
	@Override
	public void closeSession(Session session){
		session.flush();// 同步缓存数据到数据库
		session.clear();// 清楚其中的缓存
		session.close();// 关闭该session
	}
	
	/**
	 * 泛型类的class
	 */
	private final Class<T> entityClass;

	/**
	 * 构造时，告诉baseDao要查询表的类型，并且初始化查询和统计语句
	 */
	public ZBaseDaoImpl() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Autowired
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void save(T entity) {
		getCurrentSession().save(entity);
	}
	
	@Override
	public void update(T entity) {
		getCurrentSession().update(entity);
	}
	

	@Override
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}
	
	@Override
	public void delete(Serializable id) {
		getCurrentSession().delete(getHibernateTemplate().get(entityClass, id));
	}
	
	@Override
	public T get(Serializable id) {
		return (T) getCurrentSession().get(entityClass, id);
	}
	
	@Override
	public T find(Serializable id) {
		return (T) getCurrentSession().load(entityClass, id);
	}
	
	@Override
	public T findByField(String fieldName, Object fieldValue) {
		Criterion criterion = Restrictions.eq(fieldName, fieldValue);
		return (T) HibernateUtils.createCriteria(this.getCurrentSession(), entityClass, criterion).uniqueResult();
	}
	
	@Override
	public List<T> findListByField(String fieldName, Object fieldValue) {
		Criterion criterion = Restrictions.eq(fieldName, fieldValue);
		return HibernateUtils.createCriteria(getCurrentSession(), entityClass, criterion).list();
	}
	
	@Override
	public List<T> findListByField(String fieldName, List<String> values) {
		if (values != null && values.size() >= 1) {
			Criterion criterion = Restrictions.in(fieldName, values);
			return HibernateUtils.createCriteria(getCurrentSession(), entityClass, criterion).list();
		} else {
			return null;
		}
	}
	
	@Override
	public <X> X findObj(String hql, Object... values) {
		return (X) HibernateUtils.createQuery(this.getCurrentSession(), hql, values).uniqueResult();
	}
	
	@Override
	public <X> X find(String hql, Map<String, ?> values) {
		return (X) HibernateUtils.createQuery(this.getCurrentSession(), hql, values).uniqueResult();
	}
	@Override
	public <X> List<X> findhqlList(String hql, Object... values) {
		return HibernateUtils.createQuery(this.getCurrentSession(), hql, values).list();
	}
	
	@Override
	public <X> List<X> findList(String hql, Map<String, ?> values) {
		return HibernateUtils.createQuery(this.getCurrentSession(), hql, values).list();
	}
	
	@Override
	public int batchExecute(String hql, Object... values) {
		return HibernateUtils.createQuery(this.getCurrentSession(), hql, values).executeUpdate();
	}
	
	@Override
	public int batchExecute(String hql, Map<String, ?> values) {
		return HibernateUtils.createQuery(this.getCurrentSession(), hql, values).executeUpdate();
	}
	
	@Override
	public void flush() {
		this.getCurrentSession().flush();
		this.getCurrentSession().clear();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<T> hqlListFirstMax(String hql, Object... values) {
		List<T> objlist = null;
		try {
			Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
			for (int i = 2; i < values.length; i++) {
				q.setParameter(i-2, values[i]);
			}
			q.setFirstResult(Integer.parseInt(values[0].toString()));	// 从0开始 算第一条
			q.setMaxResults(Integer.parseInt(values[1].toString()));
			objlist = q.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objlist;
	}
	
	@Override
	public Page<T> findPage(Page<T> pageData) {
		Criteria criteria=HibernateUtils.createCriteria(this.getCurrentSession(), entityClass);
		HibernateUtils.setParameter(criteria, pageData);
		pageData.setResult(criteria.list());
		return pageData;
	}
	
	@Override
	public Page<T> findPageByOrders(Page<T> pageData) {
		Criteria criteria = HibernateUtils.createCriteria(this.getCurrentSession(), entityClass);
		HibernateUtils.setParameters(criteria, pageData);
		pageData.setResult(criteria.list());
		return pageData;
	}
	
	@Override
	public long findPageCount(Page<T> pageData) {
		long totalCount = 0;
		Criteria criteria = HibernateUtils.createCriteria(this.getCurrentSession(), entityClass);
		HibernateUtils.setParameters(criteria, pageData);
		Object obj = (Object)criteria.setProjection(Projections.rowCount()).uniqueResult();
		if(obj != null && Long.parseLong(obj.toString()) > 0){
			totalCount = Long.parseLong(obj.toString());
		}
		return totalCount;
	}
	
	@Override
	public List<T> findListIns(String hql, Object... values) {
		return HibernateUtils.createQueryIns(this.getCurrentSession(), hql, values).list();
	}
	
	@Override
	public int batchExecuteIns(String hql, Object... values) {
		return HibernateUtils.createQueryIns(this.getCurrentSession(), hql, values).executeUpdate();
	}
	
}