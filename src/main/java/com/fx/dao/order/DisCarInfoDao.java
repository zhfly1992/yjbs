package com.fx.dao.order;

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

import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.BaseUserDao;
import com.fx.entity.order.DisCarInfo;

@Repository
public class DisCarInfoDao extends ZBaseDaoImpl<DisCarInfo, Long> {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	@Autowired
	private BaseUserDao	baseUserDao;



	/**
	 * 
	 * @Description:根据驾驶员信息（姓名，手机号获得主订单id）
	 * @author :zh
	 * @version 2020年5月2日
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getMainCarOrderIdByDriverInfo(ReqSrc reqsrc, String driver) {
		String logtxt = U.log(log, "获取-后台订单-根据驾驶员信息获得订单号", reqsrc);
		List<Long> resultList = new ArrayList<Long>();
		try {
			// 先查询线上驾驶员
			List<String> findUnameByUnameOrPhone = baseUserDao.findUnameByUnameOrPhone(driver);

			String sql = "select main_order_id from dis_car_info where main_driver in ?0 or main_driver in ?1";

			NativeQuery createSQLQuery = currentSession().createSQLQuery(sql).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			createSQLQuery.setParameter(0, findUnameByUnameOrPhone);
			createSQLQuery.setParameter(1, findUnameByUnameOrPhone);

			List list = createSQLQuery.list();
			for (Object object : list) {
				resultList.add((Long) object);
			}

			// 查询线下驾驶员
			String outdrvier = "select main_order_id from dis_car_info where out_driver_info like ?0";
			NativeQuery outdriverQuery = currentSession().createSQLQuery(outdrvier).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			outdriverQuery.setParameter(0, "%" + driver + "%");
			List list1 = outdriverQuery.list();
			for (Object object : list1) {
				resultList.add((Long) object);
			}

		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}



	/**
	 * 
	 * @Description:根据供车方负责人姓名获取主订单号
	 * @author :zh
	 * @version 2020年5月2日
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getMainCarOrderIdBySuppCarHead(ReqSrc reqsrc, String suppMan) {
		String logtxt = U.log(log, "获取-后台订单-根据供车方负责人姓名获得订单号", reqsrc);
		List<Long> resultList = new ArrayList<Long>();
		try {
			String sql = "select main_order_id from dis_car_info where supp_car_head = ?0";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			sqlQuery.setParameter(0, suppMan);

			List list = sqlQuery.list();
			for (Object object : list) {
				resultList.add((Long) object);
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}



	/**
	 * 
	 * @Description:根据单个车牌号获取主订单id
	 * @author :zh
	 * @version 2020年5月2日
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getMainCarOrderIdBySinglePlateNum(ReqSrc reqsrc, String plateNum) {
		String logtxt = U.log(log, "获取-派车信息-根据单个车牌号获得主订单id", reqsrc);
		List<Long> resultList = new ArrayList<Long>();
		try {
			String sql = "select main_order_id from dis_car_info where plate_num like ?0";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			sqlQuery.setParameter(0, plateNum);

			List list = sqlQuery.list();
			for (Object object : list) {
				resultList.add((Long) object);
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}



	/**
	 * 
	 * @Description:根据id删除派车
	 * @author :zh
	 * @version 2020年5月2日
	 */
	@SuppressWarnings("rawtypes")
	public boolean cancelDisCar(String orderId) {
		try {
			String sql = "delete from dis_car_info where car_order_id = ?0";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql);
			sqlQuery.setParameter(0, orderId);
			int executeUpdate = sqlQuery.executeUpdate();
			if (executeUpdate != 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			U.logEx(log, "取消派车dao层异常");
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * 
	 * @Description:查询在开始时间和结束时间段内已派的车辆,为简化查询，分别查询可以使用的车辆和所有车辆，再得到已派车辆
	 * @author :zh
	 * @version 2020年5月2日
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getPlateNumByStimeAndEtime(String startTime, String endTime) {
		if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
			U.log(log, "车辆查询-时间段内已派车辆-开始时间和结束时间为空");
			return null;
		}
		try {
			// //查询未派车辆
			// List<Filtration> filts = new ArrayList<Filtration>();
			// if (StringUtils.isBlank(endTime)) {
			// U.log(log, "车辆查询-时间段内已派车辆-结束时间为空");
			// //只有开始时间，则查询主驾驶结束时间小于开始时间的车辆
			// filts.add(new Filtration(MatchType.LE,
			// DateUtils.strToDate(startTime), "mainDriverEtime"));
			// }
			// else if (StringUtils.isBlank(startTime)) {
			// U.log(log, "车辆查询-时间段内已派车辆-开始时间为空");
			// //只有结束时间，则查询主驾驶开始时间大于结束时间的车辆
			// filts.add(new Filtration(MatchType.GE,
			// DateUtils.strToDate(endTime), "mainDriverStime"));
			// }
			// else {
			// U.log(log, "车辆查询-时间段内已派车辆-开始时间结束时间都不为空");
			// //查询主驾驶开始时间大于结束时间或者主驾驶结束时间小于开始时间
			// List<Filtration> timeFilt = new ArrayList<Filtration>();
			// timeFilt.add(new
			// Filtration(MatchType.GE,DateUtils.strToDate(endTime),"mainDriverStime"));
			// timeFilt.add(new
			// Filtration(MatchType.LE,DateUtils.strToDate(startTime),"mainDriverEtime"));
			// filts.add(new Filtration(MatchType.OR,timeFilt,""));
			// }
			List<DisCarInfo> disCarInfos = null;
			String hql = null;
			if (StringUtils.isBlank(endTime)) {
				U.log(log, "车辆查询-时间段内已派车辆-结束时间为空");
				hql = "from DisCarInfo where mainDriverEtime <?0";
				// 得到可以使用的车辆
				disCarInfos = findhqlList(hql, DateUtils.strToDate(startTime));
			} else if (StringUtils.isBlank(startTime)) {
				U.log(log, "车辆查询-时间段内已派车辆-开始时间为空");
				hql = "from DisCarInfo where mainDriverStime >?0";
				// 得到可以使用的车辆
				disCarInfos = findhqlList(hql, DateUtils.strToDate(endTime));
			} else {
				hql = "from DisCarInfo where mainDriverStime >?0 or mainDriverEtime <?1";
				// 得到可以使用的车辆
				disCarInfos = findhqlList(hql, DateUtils.strToDate(endTime), DateUtils.strToDate(startTime));
			}

			List<String> canUse = new ArrayList<>();
			for (DisCarInfo car : disCarInfos) {
				canUse.add(car.getPlateNum());
			}

			String sql = "select DISTINCT plate_num from dis_car_info";
			NativeQuery query = currentSession().createSQLQuery(sql).addScalar("plate_num", StandardBasicTypes.STRING);
			// 得到所有车辆
			List list = query.list();

			// 得到时间段内无法使用的车辆
			List<String> cantUse = new ArrayList<>();
			for (Object object : list) {
				if (!canUse.contains((String) object)) {
					cantUse.add((String) object);
				}
			}
			return cantUse;

		} catch (Exception e) {
			U.logEx(log, e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 
	 * @Description:根据驾驶员uname查询订单号
	 * @author :zh
	 * @version 2020年6月2日
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getMainCarOrderIdByDriverUname(ReqSrc reqsrc, String uname){
		
		String logtxt = U.log(log, "获取-派车信息-根据驾驶员uname获得主订单号", reqsrc);
		List<Long> resultList = new ArrayList<Long>();
		try {
			String sql = "select main_order_id from dis_car_info where main_driver = ?0 or vice_driver = ?1";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			sqlQuery.setParameter(0, uname);
			sqlQuery.setParameter(1, uname);

			List list = sqlQuery.list();
			for (Object object : list) {
				resultList.add((Long) object);
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}
	
	
	/**
	 * 
	 * @Description:根据多个车牌号获取主订单id
	 * @author :zh
	 * @version 2020年6月3日
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getMainCarOrderIdByPlateNums(ReqSrc reqsrc, List<String> plateNums) {
		String logtxt = U.log(log, "获取-派车信息-根据多个车牌号获得主订单id", reqsrc);
		List<Long> resultList = new ArrayList<Long>();
		try {
			String sql = "select main_order_id from dis_car_info where plate_num in ?0";
			NativeQuery sqlQuery = currentSession().createSQLQuery(sql).addScalar("main_order_id",
					StandardBasicTypes.LONG);
			sqlQuery.setParameter(0, plateNums);

			List list = sqlQuery.list();
			for (Object object : list) {
				resultList.add((Long) object);
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		return resultList;
	}
	
}
