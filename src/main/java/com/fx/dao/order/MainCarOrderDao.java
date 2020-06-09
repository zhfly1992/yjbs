package com.fx.dao.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.HibernateUtils;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.enums.MainOrderStatus;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;

@Repository
public class MainCarOrderDao extends ZBaseDaoImpl<MainCarOrder, Long> {

	/** 日志记录 */
	private Logger			log	= LogManager.getLogger(this.getClass());

	@Autowired
	private DisCarInfoDao	disCarInfoDao;

	/** 派车订单-数据源 */
	@Autowired
	@Lazy(value = true)
	private CarOrderDao		coDao;
	/** 公司车辆-数据源 */
	@Autowired
	private CompanyVehicleDao cvdao;



	/**
	 * 
	 * @Description:
	 * @param reqsrc
	 * @param page
	 * @param rows
	 * @param find
	 *            订单号，业务员姓名
	 * @param orderPayStatus
	 *            付款状态
	 * @param orderSource
	 *            订单来源
	 * @param orderStatus
	 *            订单状态
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param compositorType
	 *            升序降序
	 * @param timeType
	 *            时间类型
	 * @param driver
	 *            驾驶员
	 * @param seat
	 *            座位数
	 * @param dutyMan
	 *            用车方负责人
	 * @param suppMan
	 *            公车方负责人
	 * @param plateNum
	 *            车牌号
	 * @return
	 * @author :zh
	 * @version 2020年5月21日
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> findMainCarOrderList(ReqSrc reqsrc, String page, String rows, String find,
			OrderPayStatus orderPayStatus, OrderSource orderSource, MainOrderStatus orderStatus, String startTime,
			String endTime, CompositorType compositorType, String timeType, String driver, Integer seat, String dutyMan,
			String suppMan, String plateNum, RouteType routeType, ServiceType serviceType, String companyNum) {

		String logtxt = U.log(log, "获取-后台订单-分页列表", reqsrc);
		Page<MainCarOrder> pd = new Page<MainCarOrder>();
		Map<String, Object> result = new HashMap<String, Object>();
		// Page<CarOrder> pd = new Page<CarOrder>();
		// List<Compositor> comps = new ArrayList<Compositor>();
		// List<Filtration> filts = new ArrayList<Filtration>();

		Criteria criteria = this.getCurrentSession().createCriteria(MainCarOrder.class);

		criteria.createAlias("mainOrderBase", "mainOrderBase", JoinType.INNER_JOIN);

		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据

				criteria.add(Restrictions.eq("id", null));
			} else {
				criteria.add(Restrictions.eq("mainOrderBase.unitNum", companyNum));
				criteria.add(Restrictions.eq("isDel", 0));


				// 搜索时间为用车时间
				if (timeType.equals("1")) {
					if (compositorType.equals("ASC")) {
						criteria.addOrder(Order.asc("stime"));
					} else {
						criteria.addOrder(Order.desc("stime"));
					}

					// comps.add(new Compositor("stime", compositorType));
					criteria.add(Restrictions.ge("stime", DateUtils.strToDate(startTime)));
					// filts.add(new Filtration(MatchType.GE,
					// DateUtils.strToDate(startTime), "stime"));
					criteria.add(Restrictions.le("etime", DateUtils.strToDate(endTime)));
					// filts.add(new Filtration(MatchType.LE,
					// DateUtils.strToDate(endTime), "etime"));
				}
				// 搜索时间为下单时间
				else if (timeType.equals("2")) {
					if (compositorType.equals("ASC")) {
						criteria.addOrder(Order.asc("addTime"));
					} else {
						criteria.addOrder(Order.desc("addTime"));
					}
					// comps.add(new Compositor("atime", compositorType));
					criteria.add(Restrictions.ge("addTime", DateUtils.strToDate(startTime)));
					// filts.add(new Filtration(MatchType.GE,
					// DateUtils.strToDate(startTime), "atime"));
					criteria.add(Restrictions.le("addTime", DateUtils.strToDate(endTime)));
					// filts.add(new Filtration(MatchType.LE,
					// DateUtils.strToDate(endTime), "atime"));
				}
				if (orderStatus != null) {
					// 添加订单状态搜索
					criteria.add(Restrictions.eq("mainOrderBase.status", orderStatus));
					// filts.add(new Filtration(MatchType.EQ, orderStatus,
					// "status"));
				}
				else{
					//默认不查出已取消的
					criteria.add(Restrictions.ne("mainOrderBase.status", MainOrderStatus.CANCELED));
				}
				if (orderPayStatus != null) {
					// 添加付款状态
					criteria.add(Restrictions.eq("payStatus", orderPayStatus));
					// filts.add(new Filtration(MatchType.EQ, orderPayStatus,
					// "payStatus"));
				}
				if (orderSource != null) {
					// 添加订单来源
					criteria.add(Restrictions.eq("mainOrderBase.orderSource", orderSource));
					// filts.add(new Filtration(MatchType.EQ, orderSource,
					// "orderSource"));
				}
				if (routeType != null) {
					// 添加订单行程类型
					criteria.add(Restrictions.eq("mainOrderBase.routeType", routeType));
					// filts.add(new
					// Filtration(MatchType.EQ,routeType,"routeType"));
				}
				if (serviceType != null) {
					// 添加订单业务类型
					criteria.add(Restrictions.eq("serviceType", serviceType));
					// filts.add(new
					// Filtration(MatchType.EQ,serviceType,"serviceType"));
				}
				// 通过驾驶员手机号和姓名查询
				if (!StringUtils.isBlank(driver)) {
					List<Long> mainCarOrderIdList = disCarInfoDao.getMainCarOrderIdByDriverInfo(reqsrc, driver);
					// 通过驾驶员手机号和姓名查询到的子订单id集合为空
					if (mainCarOrderIdList.size() == 0) {
						criteria.add(Restrictions.eq("id", null));
						// filts.add(new Filtration(MatchType.EQ, null, "id"));
					} else {
						criteria.add(Restrictions.in("id", mainCarOrderIdList.toArray()));
						// filts.add(new Filtration(MatchType.IN,
						// carOrderIdList.toArray(), "id"));
					}
				}
				// 关键字搜索，业务员，订单号
				if (!StringUtils.isBlank(find)) {

					criteria.add(Restrictions.or(Restrictions.like("orderNum", "%" + find + "%"),
							Restrictions.like("mainOrderBase.serviceMan", "%" + find + "%")));
					// filts.add(new Filtration(MatchType.LIKE, find,
					// "orderNum", "serviceMan"));
				}
				// 座位搜索
				if (seat != null) {
					criteria.add(Restrictions.eq("needCars", seat.intValue()));
					// filts.add(new Filtration(MatchType.EQ, seat.intValue(),
					// "needSeats", "realSeats"));
				}
				// 用车方负责人搜索
				if (!StringUtils.isBlank(dutyMan)) {
					criteria.add(Restrictions.eq("mainOrderBase.dutyService", dutyMan));
					// filts.add(new Filtration(MatchType.EQ, dutyMan,
					// "dutyService"));
				}
				// 供车方负责人搜索
				if (!StringUtils.isBlank(suppMan)) {
					List<Long> mainCarOrderIdBySuppCarHead = disCarInfoDao.getMainCarOrderIdBySuppCarHead(reqsrc,
							suppMan);
					if (mainCarOrderIdBySuppCarHead.size() == 0) {
						criteria.add(Restrictions.eq("id", null));
						// filts.add(new Filtration(MatchType.EQ, null, "id"));
					} else {
						criteria.add(Restrictions.in("id", mainCarOrderIdBySuppCarHead.toArray()));
						// filts.add(new Filtration(MatchType.IN,
						// carOrderIdBySuppCarHead.toArray(), "id"));
					}
				}
				// 车牌号搜索
				if (!StringUtils.isBlank(plateNum)) {
					List<Long> mainCarOrderIdByPlateNum = disCarInfoDao.getMainCarOrderIdBySinglePlateNum(reqsrc, plateNum);
					if (mainCarOrderIdByPlateNum.size() == 0) {
						criteria.add(Restrictions.eq("id", null));
						// filts.add(new Filtration(MatchType.EQ, null, "id"));
					} else {
						criteria.add(Restrictions.in("id", mainCarOrderIdByPlateNum.toArray()));
						// filts.add(new Filtration(MatchType.IN,
						// carOrderIdByPlateNum.toArray(), "id"));
					}
				}

				criteria.addOrder(Order.asc("orderNum"));
				// 取得总数
				Long uniqueResult = (Long) criteria.setProjection(Projections.countDistinct("id")).uniqueResult();
				// 分页设置
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				// criteria.setFirstResult((Integer.parseInt(page)-1)*
				// Integer.parseInt(rows));
				// criteria.setMaxResults(Integer.parseInt(rows));
				List<MainCarOrder> list = criteria.list();

				// 手动分页,在criteria中分页会出错
				// int pageint = Integer.parseInt(page);
				// int rowint = Integer.parseInt(rows);
				// int start = (pageint -1) * rowint;
				//
				//
				// List<MainCarOrder> resultList = new ArrayList<>();
				// //start+m<list.size()防止越界
				// for(int m = 0; m< rowint && start+m<list.size();m++){
				// resultList.add(list.get(start + m));
				// }

				System.out.println("123");

				/////////////////// --分页设置--////////////////////////////
				result.put("count", uniqueResult);
				result.put("data", list);
				// pd.setPageNo(Integer.parseInt(page)); // 页码
				// pd.getPagination().setPageSize(Integer.parseInt(rows)); //
				// 页大小
				// pd.setResult(list);
				// System.out.println("123");

			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return result;
	}



	/**
	 * @author xx
	 * @date 20200529
	 * @Description:更新主订单
	 * @param colist
	 *            子订单列表
	 * @param mco
	 *            主订单
	 * @param mainOrderNum
	 *            主订单编号
	 * @param type
	 *            1添加 2修改 3删除
	 * @param reqsrc
	 */
	public Map<String, Object> modifyMainOrder(ReqSrc reqsrc, List<CarOrder> colist, MainCarOrder mco,
			String mainOrderNum, int type) {
		String logtxt = U.log(log, "单位订单-添加或修改或删除子订单后更新主订单信息", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (colist == null) {
				colist = coDao.findListByField("mainOrderNum", mainOrderNum);
			}
			if (mco == null) {
				mco = findByField("orderNum", mainOrderNum);
			}
			int needCars = 0;// 所需车辆数
			String needSeats = "";// 所需座位数
			double remDriverCharge = 0;// 提醒师傅现收
			Date sTime = colist.get(0).getStime();// 开始时间
			Date eTime = colist.get(0).getEtime();// 结束时间
			String routeDetails = "";// 行程详情
			double orderPrice = 0;// 订单价格
			boolean isHaveCitySer = false;
			boolean isHaveProSer = false;
			for (CarOrder eachco : colist) {
				needCars += eachco.getNeedCars();
				needSeats += eachco.getNeedSeats() + ",";
				remDriverCharge += eachco.getRemDriverCharge();
				if (eachco.getStime().before(sTime)) {
					sTime = eachco.getStime();
				}
				if (eachco.getEtime().after(eTime)) {
					eTime = eachco.getEtime();
				}
				routeDetails += eachco.getRouteDetail() + ";";
				orderPrice += eachco.getPrice();
				if (eachco.getServiceType().equals(ServiceType.PROVINCE_SER) && !isHaveProSer) {
					isHaveProSer = true;
				}
				if (eachco.getServiceType().equals(ServiceType.CITY_SER) && !isHaveCitySer) {
					isHaveCitySer = true;
				}
			}
			mco.setNeedCars(needCars);
			mco.setNeedSeats(needSeats.substring(0, needSeats.length() - 1));
			mco.setRemDriverCharge(remDriverCharge);
			mco.setStime(sTime);
			mco.setEtime(eTime);
			mco.setRouteDetail(routeDetails.substring(0, routeDetails.length() - 1));
			mco.setPrice(orderPrice);
			if (isHaveProSer) {
				mco.setServiceType(ServiceType.PROVINCE_SER);
			} else if (isHaveCitySer) {
				mco.setServiceType(ServiceType.CITY_SER);
			} else {
				mco.setServiceType(ServiceType.COUNTY_SER);
			}
			if (type == 1) {
				mco.setAddTime(new Date());
				save(mco);
			} else {
				update(mco);
			}
			U.setPut(map, 1, "更新主订单【" + mco.getOrderNum() + "】成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;

	}



	public Page<MainCarOrder> getMainCarOrderForCollection(ReqSrc reqsrc, String page, String rows,
			OrderPayStatus orderPayStatus, String startTime, String endTime, CompositorType compositorType,
			String timeType, String driver, String dutyService, String plateNum, String orderNum, String routeDetail,
			String serviceMan, String unitNum, String customer,String businessType) {

		String logtxt = U.log(log, "获取-业务收款-分页列表", reqsrc);

		Page<MainCarOrder> pd = new Page<MainCarOrder>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "id"));
			} else {

				// 未删除
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));

				// 已经确认付款价格
				filts.add(new Filtration(MatchType.ISNOTNULL, "", "mainOrderBase.confirmCollectionName"));
				// 单位编号
				filts.add(new Filtration(MatchType.EQ, unitNum, "mainOrderBase.unitNum"));
				// 搜索时间为用车时间
				if (timeType.equals("1")) {
					comps.add(new Compositor("stime", compositorType));
					filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "stime"));
					filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "etime"));
				}
				// 搜索时间为下单时间
				else if (timeType.equals("2")) {
					comps.add(new Compositor("atime", compositorType));
					filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "addTime"));
					filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "addTime"));
				}

				if (orderPayStatus != null) {
					// 添加付款状态
					filts.add(new Filtration(MatchType.EQ, orderPayStatus, "payStatus"));
				}

				// 通过驾驶员uname搜索
				if (!StringUtils.isBlank(driver)) {
					List<Long> mainCarOrderIdList = disCarInfoDao.getMainCarOrderIdByDriverUname(reqsrc, driver);
					// 通过驾驶员手机号和姓名查询到的订单id集合为空
					if (mainCarOrderIdList.size() == 0) {
						filts.add(new Filtration(MatchType.EQ, null, "id"));
					} else {
						filts.add(new Filtration(MatchType.IN, mainCarOrderIdList.toArray(), "id"));
					}
				}

				// 用车方负责人搜索
				if (!StringUtils.isBlank(dutyService)) {
					filts.add(new Filtration(MatchType.EQ, dutyService, "mainOrderBase.dutyService"));
				}

				// 车牌号搜索
				if (!StringUtils.isBlank(plateNum)) {
					List<Long> mainCarOrderIdByPlateNum = disCarInfoDao.getMainCarOrderIdBySinglePlateNum(reqsrc, plateNum);
					filts.add(new Filtration(MatchType.IN,mainCarOrderIdByPlateNum.toArray(),"id"));
					
				}
				// 业务员搜索
				if (!StringUtils.isBlank(serviceMan)) {
					filts.add(new Filtration(MatchType.EQ, serviceMan, "mainOrderBase.serviceMan"));
				}
				// 订单号搜索
				if (!StringUtils.isBlank(orderNum)) {
					filts.add(new Filtration(MatchType.EQ, orderNum, "orderNum"));
				}
				// 行程详情搜索
				if (!StringUtils.isBlank(routeDetail)) {
					filts.add(new Filtration(MatchType.LIKE, routeDetail, "routeDetail"));
				}
				// 客户（用车方）搜索
				if (!StringUtils.isBlank(customer)) {
					filts.add(new Filtration(MatchType.EQ, customer, "mainOrderBase.baseUserId.uname"));
				}
				// 车辆是否自营搜索
				if (!StringUtils.isBlank(businessType)) {
					List<String> plateNums = cvdao.getPlateNumsByBusinessType(reqsrc, businessType, unitNum);
					if (plateNums.size() == 0) {
						//无该类型车辆
						filts.add(new Filtration(MatchType.EQ, null, "id"));
					}
					else{
						List<Long> mainCarOrderIdByPlateNums = disCarInfoDao.getMainCarOrderIdByPlateNums(reqsrc, plateNums);
						filts.add(new Filtration(MatchType.IN,mainCarOrderIdByPlateNums.toArray(),"id"));
					}
				}

				/////////////////// --分页设置--////////////////////////////
				pd.setPageNo(Integer.parseInt(page)); // 页码
				pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
				pd.setCompositors(comps); // 排序条件
				pd.setFiltrations(filts); // 查询条件

				Criteria criteria = HibernateUtils.createCriteria(this.getCurrentSession(), MainCarOrder.class);
				// 设置别名，否则无法匹配属性名
				criteria.createAlias("mainOrderBase", "mainOrderBase", JoinType.INNER_JOIN);
				HibernateUtils.setParameters(criteria, pd);

				pd.setResult(criteria.list());
				System.out.println("123");

			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}
	
	
	public Map<String, Object> countMainCarOrderForCollection(ReqSrc reqsrc, String page, String rows,
			OrderPayStatus orderPayStatus, String startTime, String endTime, CompositorType compositorType,
			String timeType, String driver, String dutyService, String plateNum, String orderNum, String routeDetail,
			String serviceMan, String unitNum, String customer,String businessType) {

		String logtxt = U.log(log, "获取-业务收款-统计", reqsrc);
		Map<String, Object> statics = new HashMap<String, Object>();
		Page<MainCarOrder> pd = new Page<MainCarOrder>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "id"));
			} else {

				// 未删除
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));

				// 已经确认付款价格
				filts.add(new Filtration(MatchType.ISNOTNULL, "", "mainOrderBase.confirmCollectionName"));
				// 单位编号
				filts.add(new Filtration(MatchType.EQ, unitNum, "mainOrderBase.unitNum"));
				// 搜索时间为用车时间
				if (timeType.equals("1")) {
					comps.add(new Compositor("stime", compositorType));
					filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "stime"));
					filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "etime"));
				}
				// 搜索时间为下单时间
				else if (timeType.equals("2")) {
					comps.add(new Compositor("atime", compositorType));
					filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "addTime"));
					filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "addTime"));
				}

				if (orderPayStatus != null) {
					// 添加付款状态
					filts.add(new Filtration(MatchType.EQ, orderPayStatus, "payStatus"));
				}

				// 通过驾驶员uname搜索
				if (!StringUtils.isBlank(driver)) {
					List<Long> mainCarOrderIdList = disCarInfoDao.getMainCarOrderIdByDriverUname(reqsrc, driver);
					// 通过驾驶员手机号和姓名查询到的订单id集合为空
					if (mainCarOrderIdList.size() == 0) {
						filts.add(new Filtration(MatchType.EQ, null, "id"));
					} else {
						filts.add(new Filtration(MatchType.IN, mainCarOrderIdList.toArray(), "id"));
					}
				}

				// 用车方负责人搜索
				if (!StringUtils.isBlank(dutyService)) {
					filts.add(new Filtration(MatchType.EQ, dutyService, "mainOrderBase.dutyService"));
				}

				// 车牌号搜索
				if (!StringUtils.isBlank(plateNum)) {
					List<Long> mainCarOrderIdByPlateNum = disCarInfoDao.getMainCarOrderIdBySinglePlateNum(reqsrc, plateNum);
					filts.add(new Filtration(MatchType.IN,mainCarOrderIdByPlateNum.toArray(),"id"));
					
				}
				// 业务员搜索
				if (!StringUtils.isBlank(serviceMan)) {
					filts.add(new Filtration(MatchType.EQ, serviceMan, "mainOrderBase.serviceMan"));
				}
				// 订单号搜索
				if (!StringUtils.isBlank(orderNum)) {
					filts.add(new Filtration(MatchType.EQ, orderNum, "orderNum"));
				}
				// 行程详情搜索
				if (!StringUtils.isBlank(routeDetail)) {
					filts.add(new Filtration(MatchType.LIKE, routeDetail, "routeDetail"));
				}
				// 客户（用车方）搜索
				if (!StringUtils.isBlank(customer)) {
					filts.add(new Filtration(MatchType.EQ, customer, "mainOrderBase.baseUserId.uname"));
				}
				// 车辆是否自营搜索
				if (!StringUtils.isBlank(businessType)) {
					List<String> plateNums = cvdao.getPlateNumsByBusinessType(reqsrc, businessType, unitNum);
					if (plateNums.size() == 0) {
						//无该类型车辆
						filts.add(new Filtration(MatchType.EQ, null, "id"));
					}
					else{
						List<Long> mainCarOrderIdByPlateNums = disCarInfoDao.getMainCarOrderIdByPlateNums(reqsrc, plateNums);
						filts.add(new Filtration(MatchType.IN,mainCarOrderIdByPlateNums.toArray(),"id"));
					}
				}

				/////////////////// --分页设置--////////////////////////////
				pd.setPageNo(Integer.parseInt(page)); // 页码
				pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
				pd.setCompositors(comps); // 排序条件
				pd.setFiltrations(filts); // 查询条件

				Criteria criteria = HibernateUtils.createCriteria(this.getCurrentSession(), MainCarOrder.class);
				// 设置别名，否则无法匹配属性名
				criteria.createAlias("mainOrderBase", "mainOrderBase", JoinType.INNER_JOIN);
				HibernateUtils.setParameters(criteria, pd);

				pd.setResult(criteria.list());
				double totalPrice = 0;
				double totalAlGathPrice = 0;
				for(MainCarOrder mainCarOrder:pd.getResult()){
					totalPrice += mainCarOrder.getPrice();
					totalAlGathPrice += mainCarOrder.getAlGathPrice();
				}
				statics.put("totalAlGathPrice", totalAlGathPrice);
				statics.put("totalPrice", totalPrice);
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return statics;
	}
}
