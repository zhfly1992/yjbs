package com.fx.dao.company;



import java.util.ArrayList;

import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.utils.enums.CarNature;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.order.DisCarInfoDao;
import com.fx.entity.company.CompanyVehicle;



@Repository
public class CompanyVehicleDao extends ZBaseDaoImpl<CompanyVehicle, Long> {

	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());
	@Autowired
	private BaseUserDao	baseUserDao;
	@Autowired
	private DisCarInfoDao discarInfoDao;


	/**
	 * 获取-单位车辆
	 * @param unitNum 	单位编号
	 * @param plateNum 	车辆车牌号
	 */
	public CompanyVehicle findCompanyCar(String unitNum, String plateNum) {
		String logtxt = U.log(log, "获取-单位车辆");
		
		String hql = "";
		CompanyVehicle car = null;
		
		try {
			hql = "from CompanyVehicle where unitNum = ?0 and plateNumber = ?1";
			car = findObj(hql, unitNum, plateNum);
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return car;
	}

	/**
	 * 
	 * @Description:
	 * @param companyVehicle
	 * @param reqsrc
	 * @return
	 * @author :zh
	 * @version 2020年4月21日
	 */
//	public boolean update(CompanyVehicle companyVehicle, ReqSrc reqsrc) {
//
//		String logtxt = U.log(log, "修改-车辆-dao层", reqsrc);
//		try {
//			Session currentSession = getCurrentSession();
//			Transaction transaction = currentSession.getTransaction();
//			transaction.begin();
//			currentSession.update(companyVehicle);
//			transaction.commit();
//			currentSession.close();
//			return true;
//		} catch (Exception e) {
//			U.log(log, logtxt, e);
//			e.printStackTrace();
//			return false;
//		}
//
//	}



	/**
	 * 
	 * @Description:
	 * @param reqsrc
	 * @param id
	 * @return
	 * @author :zh
	 * @version 2020年4月21日
	 */
/*	public boolean deleteById(ReqSrc reqsrc, long id) {
		String logtxt = U.log(log, "删除-车辆-dao层", reqsrc);
		try {
	
			Session session = getCurrentSession();
			Query createQuery = HibernateUtils.createQuery(session, "delete from CompanyVehicle where id = ?0", id);
			Transaction transaction = session.getTransaction();
			transaction.begin();
			int res = createQuery.executeUpdate();
			transaction.commit();
			session.close();
			if (res != 0) {
				U.log(log, "删除-车辆-dao层成功");
				return true;
			}
			U.logFalse(log, "删除-车辆-dao层失败");
			return false;
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return false;
		}
	}*/



	/**
	 * 
	 * @Description:
	 * @param reqsrc
	 *            请求来源
	 * @param page
	 *            页码
	 * @param rows
	 *            页大小
	 * @param phone
	 *            驾驶员手机号
	 * @param plateNumber
	 *            车牌
	 * @param seats
	 *            座位数
	 * @param vehicleType
	 *            车辆类型
	 * @param unitNum       
	 *       	      创建公司编号     
	 * @param belongCampany           
	 *            所属公司
	 * @param carNature    
	 *          车辆性质
	 * @return Page<T> 分页数据
	 * @author :zh
	 * @version 2020年4月21日
	 */
	@SuppressWarnings("rawtypes")
	public Page<CompanyVehicle> findCompanyVehicleList(ReqSrc reqsrc, String page, String rows, String phone,
			String plateNumber, String seats,String unitNum,String belongCampany,CarNature carNature,String groupName,String startTime,
			String endTime) {

		String logtxt = U.log(log, "获取-车辆-分页列表", reqsrc);

		Page<CompanyVehicle> pd = new Page<CompanyVehicle>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			} else {
				comps.add(new Compositor("purchaseDate", CompositorType.DESC));
				if (StringUtils.isNotBlank(phone)) {
					String uName = baseUserDao.findUname(phone);
					if (uName != null) {// 匹配用户名
						filts.add(new Filtration(MatchType.EQ, uName, "baseUserId.uname"));
					}
				}

				if (StringUtils.isNotBlank(plateNumber)) {
					filts.add(new Filtration(MatchType.EQ, plateNumber, "plateNumber"));
				}
				if (StringUtils.isNotBlank(seats)) {
					filts.add(new Filtration(MatchType.EQ,Integer.valueOf(seats), "seats"));
				}
				if (StringUtils.isNotBlank(unitNum)) {
					filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
				}
				if (StringUtils.isNotBlank(belongCampany)) {
					List<Filtration> companyFilt = new ArrayList<Filtration>();
					companyFilt.add(new Filtration((MatchType.LIKE),belongCampany,"belongCompanyName"));
					companyFilt.add(new Filtration((MatchType.LIKE),belongCampany,"belongComapnySimName"));
					filts.add(new Filtration(MatchType.OR,companyFilt,""));
				}
				if (carNature != null) {
					filts.add(new Filtration(MatchType.EQ,carNature,"carUsage"));
				}
				if (StringUtils.isNotBlank(groupName)) {
					//根据groupName拿到驾驶员
					String sql = "select base_user_id from staff where group_id = ?0 and is_del = 0";
					NativeQuery query = currentSession().createSQLQuery(sql).addScalar("base_user_id", StandardBasicTypes.STRING);
					query.setParameter(0, groupName);
					List list = query.list();
					ArrayList<String> baseList = new ArrayList<>();
					for(Object object:list){
						baseList.add((String)object);
					}
					filts.add(new Filtration(MatchType.IN,baseList.toArray(),"baseUserId.uname"));
				}
				List<String> plateNumByStimeAndEtime = discarInfoDao.getPlateNumByStimeAndEtime(startTime, endTime);
				if (plateNumByStimeAndEtime !=null && plateNumByStimeAndEtime.size() >0) {
					filts.add(new Filtration(MatchType.NOTIN,plateNumByStimeAndEtime.toArray(),"plateNumber"));
				}
				
			}
			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = findPageByOrders(pd); // 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}
	
	
	/*public boolean setDriver(ReqSrc reqsrc, long id ,String uname){
		String logtxt = U.log(log, "设置-车辆-主驾驶员", reqsrc);
		try {
			
			Session session = getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery("update CompanyVehicle set baseUserId.uname = :uname where id = :id");
			query.setParameter("uname", uname);
			query.setParameter("id", id);
			int res = query.executeUpdate();
			session.getTransaction().commit();
			session.close();
			if (res != 0) {
				U.log(log, "设置-车辆-主驾驶员-dao层成功");
				return true;
			}
			U.logFalse(log, "设置-车辆-主驾驶员-dao层失败");
			return false;

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return false;
		}
		
	}*/
	
	
	/**
	 * 
	 * @Description:根据车辆是否自营获取车牌号
	 * @author :zh
	 * @version 2020年6月3日
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getPlateNumsByBusinessType(ReqSrc reqsrc, String businessType,String unitNum){
		String logtxt = U.log(log, "获取-车牌号-根据是否自营", reqsrc);
		List<String> resultList = new ArrayList<String>();
		try {
			String sql = "select plate_number from company_vehicle where business_type = ?0 and unit_num = ?1";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql).addScalar("plate_number",
					StandardBasicTypes.STRING);
			sqlQuery.setParameter(0, businessType);
			sqlQuery.setParameter(1, unitNum);
			List list = sqlQuery.list();
			
			for (Object object : list) {
				resultList.add((String) object);
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}
		
	
}
