package com.fx.dao.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.OrderStatus;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.HttpReqMeth;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.U;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.DisCarInfo;
import com.fx.entity.order.RouteLineInfo;


@Repository
public class CarOrderDao extends ZBaseDaoImpl<CarOrder, Long> {

	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());


	/** 派单车辆信息-服务 */
	@Autowired
	private DisCarInfoDao disCarInfoDao;
	
	/** 行程线路信息-服务 */
	@Autowired
	private RouteLineInfoDao routeLineInfoDao;

	/**
	 * 
	 * @Description:业务付款列表(外调的已经确认付款价格的子订单)
	 * @param reqsrc
	 * @param page
	 * @param rows
	 * @param orderPayStatus
	 *            订单支付状态
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
	 * @param orderNum
	 *            订单号
	 * @param serviceMan
	 *            业务员
	 * @param routeDetail
	 *            行程详情
	 * @param dutyService
	 *            用车方负责人
	 * @param suppCar
	 *            供车方（收款方）搜索
	 * @param plateNum
	 *            车牌号
	 * @param unitNum
	 * 			      单位编号
	 * @param customer
	 * 			      客户（用车方）uname
	 * @return 
	 * @author :zh
	 * @version 2020年5月1日
	 */
	public Page<CarOrder> getCarOrderForPayment(ReqSrc reqsrc, String page, String rows, String orderPayStatus,
			String startTime, String endTime, CompositorType compositorType, String timeType, String driver,
			String dutyService, String suppCar, String plateNum, String orderNum, String routeDetail, String serviceMan,
			String unitNum, String customer) {

		String logtxt = U.log(log, "获取-业务付款-分页列表", reqsrc);

		Page<CarOrder> pd = new Page<CarOrder>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "id"));
			} else {
				//设置别名
				List<String> alias = new ArrayList<String>();
				alias.add("carOrderBase");
				alias.add("disCar");
				pd.setAlias(alias);
				// 未删除
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));
				// 添加外调条件
				filts.add(new Filtration(MatchType.NE, 0, "isExternal"));
				// 已经确认付款价格
				filts.add(new Filtration(MatchType.ISNOTNULL, "", "confirmPaymentName"));
				// 单位编号
				filts.add(new Filtration(MatchType.EQ, unitNum, "carOrderBase.unitNum"));
				// 搜索时间为用车时间
				if (timeType.equals("1")) {
					comps.add(new Compositor("stime", compositorType));
					if (StringUtils.isNotBlank(startTime)) {
						filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "stime"));
					}
					if (StringUtils.isNotBlank(endTime)) {
						filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "etime"));
					}				
				}
				// 搜索时间为下单时间
				else if (timeType.equals("2")) {
					comps.add(new Compositor("atime", compositorType));
					if (StringUtils.isNotBlank(startTime)) {
						filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "addTime"));
					}
					if (StringUtils.isNotBlank(endTime)) {
						filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "addTime"));
					}				
				}
				
				if (StringUtils.isNotBlank(orderPayStatus)) {
					if("0".equals(orderPayStatus)) {//应付款
						filts.add(new Filtration(MatchType.NE, OrderPayStatus.FULL_PAID, "payStatus"));//非全款已付
					}else {//已付款
						filts.add(new Filtration(MatchType.NE, OrderPayStatus.UNPAID, "payStatus"));//非未收款
					}
				}

				// 通过驾驶员uname搜索
				if (!StringUtils.isBlank(driver)) {
					List<Filtration> unameFilt = new ArrayList<Filtration>();
					// 主驾驶搜索
					unameFilt.add(new Filtration(MatchType.EQ, driver, "disCar.main_driver.uname"));
					// 副驾驶搜索
					unameFilt.add(new Filtration(MatchType.EQ, driver, "disCar.vice_driver.uname"));
					filts.add(new Filtration(MatchType.OR, unameFilt, ""));
				}

				// 用车方负责人搜索
				if (!StringUtils.isBlank(dutyService)) {
					filts.add(new Filtration(MatchType.EQ, dutyService, "carOrderBase.dutyService"));
				}
				// 供车方搜索
				if (!StringUtils.isBlank(suppCar)) {

					filts.add(new Filtration(MatchType.EQ, suppCar, "disCar.suppCar"));
				}
				// 车牌号搜索
				if (!StringUtils.isBlank(plateNum)) {

					filts.add(new Filtration(MatchType.EQ, plateNum, "disCar.plateNum"));
				}
				// 业务员搜索
				if (!StringUtils.isBlank(serviceMan)) {
					filts.add(new Filtration(MatchType.EQ, serviceMan, "carOrderBase.serviceMan"));
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
					filts.add(new Filtration(MatchType.EQ,customer,"carOrderBase.baseUserId.uname"));
				}

				comps.add(new Compositor("mainOrderNum", CompositorType.ASC));
				

				/////////////////// --分页设置--////////////////////////////
				pd.setPageNo(Integer.parseInt(page)); // 页码
				pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
				pd.setCompositors(comps); // 排序条件
				pd.setFiltrations(filts); // 查询条件
				pd = findPageByOrders(pd);

			/*	Criteria criteria = HibernateUtils.createCriteria(this.getCurrentSession(), CarOrder.class);
				// 设置别名，否则无法匹配属性名
				criteria.createAlias("carOrderBase", "carOrderBase", JoinType.INNER_JOIN);
				criteria.createAlias("disCar", "disCar", JoinType.INNER_JOIN);
				HibernateUtils.setParameters(criteria, pd);
				pd.setResult(criteria.list());*/
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}

	/**
	 * 获取-车队驾驶员-分页订单列表
	 * @param buser 	登录用户基类
	 * @param comUser 	登录单位对象
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	订单出行开始时间
	 * @param etime 	订单出行结束时间
	 * @param isTrip 	出行类型：1-未出行；0-已出行；
	 * @return 分页订单列表
	 */
	public Page<CarOrder> findPageDriverOrderList(BaseUser buser, CompanyUser comUser, String page,
		String rows, String stime, String etime, String isTrip) {
		String logtxt = U.log(log, "获取-车队驾驶员-分页订单列表");
		
		Page<CarOrder> pd = new Page<CarOrder>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			////////////////////--默认排序--//////////////////////////
			
			///////////////////--条件--begin//////////////////////////
			// 1.通过派单车辆的主驾驶员查询到id数组；
			// 2.再通过id数组查询到的数据列表，排除指定单位编号的列表；
			// 3.排除时间
			
			String hql = "";
			boolean fg = true;
			
			List<Object> ids = new ArrayList<Object>();
			if(fg) {
				hql = "select new DisCarInfo(id) from DisCarInfo where main_driver.uname = ?0 or vice_driver.uname = ?1";
				List<DisCarInfo> dciList = disCarInfoDao.findhqlList(hql, buser.getUname(), buser.getUname());
				for (DisCarInfo dci : dciList) {
					ids.add(dci.getId());
				}
				
				if(ids.size() == 0) {
					fg = U.logFalse(log, "未从派单车辆列表中查询到符合条件的数据");
				}
			}
			
			if(fg) {
				hql = "select new CarOrder(CarOrderBase) from CarOrder where id in(:v0)";
				List<CarOrder> colist = findListIns(hql, ids.toArray());
				ids.clear();
				for (CarOrder co : colist) {
					if(co.getCarOrderBase().getUnitNum().equals(comUser.getUnitNum())) {
						ids.add(co.getId());
					}
				}
				
				if(ids.size() == 0) {
					fg = U.logFalse(log, "未从车辆订单基类中查询到符合条件的数据");
				}
			}
			
			if(!fg) {
				// 此条件不可能查询到数据
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}else {
				// 查询[未软删除]
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));
				
				// 查询-符合条件id的车辆订单
				filts.add(new Filtration(MatchType.IN, ids.toArray(), "id"));
				
				if("1".equals(isTrip)){
					U.log(log, "未完团=出行时间未过当前时间");
					
					// 按[出发时间]顺序-排序
					comps.add(new Compositor("stime", CompositorType.ASC));
					
					// 未抢待师傅确认
					Object[] status = new Object[]{OrderStatus.DRIVER_NOT_CONFIRM, OrderStatus.DRIVER_CONFIRMED, OrderStatus.AL_TRAVEL};
					filts.add(new Filtration(MatchType.IN, status, "status"));
				}else{
					U.log(log, "已完团=出行时间已过当前时间");
					
					// 按[出发时间]倒序-排序
					comps.add(new Compositor("stime", CompositorType.DESC));
					
					// 交易完成
					Object[] status = new Object[]{OrderStatus.COMPLETED};
					filts.add(new Filtration(MatchType.IN, status, "status"));
					
					// 结束时间小于当前时间
					filts.add(new Filtration(MatchType.LE, new Date(), "etime"));
				}
				
				// 查询-指定[出发]时间段
				if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
					List<Filtration> fland = new ArrayList<Filtration>();
					fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "stime"));
					fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "stime"));
					filts.add(new Filtration(MatchType.AND, fland, ""));
				}
				
			}
			///////////////////--条件--end////////////////////////////
			
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = findPageByOrders(pd);								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}

	
	
	
	
	/**
	 * 
	 * @Description:统计业务付款列表(外调的已经确认付款价格的子订单)
	 * @param reqsrc
	 * @param page
	 * @param rows
	 * @param orderPayStatus
	 *            订单支付状态
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
	 * @param orderNum
	 *            订单号
	 * @param serviceMan
	 *            业务员
	 * @param routeDetail
	 *            行程详情
	 * @param dutyService
	 *            用车方负责人
	 * @param suppCar
	 *            供车方（收款方）搜索
	 * @param plateNum
	 *            车牌号
	 * @param unitNum
	 * 			      单位编号
	 * @param customer
	 * 			      客户（用车方）uname
	 * @return 
	 * @author :zh
	 * @version 2020年6月8日
	 */
	public Map<String, Object> countCarOrderForPayment(ReqSrc reqsrc, String page, String rows, String orderPayStatus,
			String startTime, String endTime, CompositorType compositorType, String timeType, String driver,
			String dutyService, String suppCar, String plateNum, String orderNum, String routeDetail, String serviceMan,
			String unitNum, String customer) {

		String logtxt = U.log(log, "获取-业务付款-统计", reqsrc);

		Page<CarOrder> pd = new Page<CarOrder>();
		Map<String, Object> statics = new HashMap<String, Object>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "id"));
			} else {
				//设置别名
				List<String> alias = new ArrayList<String>();
				alias.add("carOrderBase");
				alias.add("disCar");
				pd.setAlias(alias);
				// 未删除
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));
				// 添加外调条件
				filts.add(new Filtration(MatchType.NE, 0, "isExternal"));
				// 已经确认付款价格
				filts.add(new Filtration(MatchType.ISNOTNULL, "", "confirmPaymentName"));
				// 单位编号
				filts.add(new Filtration(MatchType.EQ, unitNum, "carOrderBase.unitNum"));
				// 搜索时间为用车时间
				if (timeType.equals("1")) {
					comps.add(new Compositor("stime", compositorType));
					if (StringUtils.isNotBlank(startTime)) {
						filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "stime"));
					}
					if (StringUtils.isNotBlank(endTime)) {
						filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "etime"));
					}				
				}
				// 搜索时间为下单时间
				else if (timeType.equals("2")) {
					comps.add(new Compositor("atime", compositorType));
					if (StringUtils.isNotBlank(startTime)) {
						filts.add(new Filtration(MatchType.GE, DateUtils.strToDate(startTime), "addTime"));
					}
					if (StringUtils.isNotBlank(endTime)) {
						filts.add(new Filtration(MatchType.LE, DateUtils.strToDate(endTime), "addTime"));
					}				
				}
				
				if (StringUtils.isNotBlank(orderPayStatus)) {
					if("0".equals(orderPayStatus)) {//应付款
						filts.add(new Filtration(MatchType.NE, OrderPayStatus.FULL_PAID, "payStatus"));//非全款已付
					}else {//已付款
						filts.add(new Filtration(MatchType.NE, OrderPayStatus.UNPAID, "payStatus"));//非未收款
					}
				}

				// 通过驾驶员uname搜索
				if (!StringUtils.isBlank(driver)) {
					List<Filtration> unameFilt = new ArrayList<Filtration>();
					// 主驾驶搜索
					unameFilt.add(new Filtration(MatchType.EQ, driver, "disCar.main_driver.uname"));
					// 副驾驶搜索
					unameFilt.add(new Filtration(MatchType.EQ, driver, "disCar.vice_driver.uname"));
					filts.add(new Filtration(MatchType.OR, unameFilt, ""));
				}

				// 用车方负责人搜索
				if (!StringUtils.isBlank(dutyService)) {
					filts.add(new Filtration(MatchType.EQ, dutyService, "carOrderBase.dutyService"));
				}
				// 供车方搜索
				if (!StringUtils.isBlank(suppCar)) {

					filts.add(new Filtration(MatchType.EQ, suppCar, "disCar.suppCar"));
				}
				// 车牌号搜索
				if (!StringUtils.isBlank(plateNum)) {

					filts.add(new Filtration(MatchType.EQ, plateNum, "disCar.plateNum"));
				}
				// 业务员搜索
				if (!StringUtils.isBlank(serviceMan)) {
					filts.add(new Filtration(MatchType.EQ, serviceMan, "carOrderBase.serviceMan"));
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
					filts.add(new Filtration(MatchType.EQ,customer,"carOrderBase.baseUserId.uname"));
				}

				comps.add(new Compositor("mainOrderNum", CompositorType.ASC));
				

				/////////////////// --分页设置--////////////////////////////
				pd.setPageNo(Integer.parseInt(page)); // 页码
				pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
				pd.setCompositors(comps); // 排序条件
				pd.setFiltrations(filts); // 查询条件
				pd = findPageByOrders(pd);
				double totalDisPrice = 0;
				double totalAlPayPrice = 0;
				for(CarOrder carOrder:pd.getResult()){
					totalAlPayPrice += carOrder.getAlPayPrice();
					totalDisPrice += carOrder.getDisPrice();
				}
				statics.put("totalAlPayPrice", totalAlPayPrice);
				statics.put("totalDisPrice", totalDisPrice);

			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return statics;
	}


	public Map<String, Object> updAgainCalcEtime(CarOrder jco, int type) {
		String logtxt = U.log(log, "重新计算-派车成功订单-行程距离和完团时间");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try{
			if(fg){
				if(jco == null){
					fg = U.setPutFalse(map, "[派车成功订单]不存在");
				}else{
					
					U.log(log, "[派车成功订单] orderNum="+jco.getOrderNum());
				}
			}
			
			String spoint = null;// 起点：103.123456,30.123456
			if(fg){
				if(type == 1){
					U.log(log, "根据[行程起点坐标]重新计算");
					
					spoint = Util.getRSP(jco.getRouteMps()).getMapPoint().getLngLat();
				}else if(type == 2){
					U.log(log, "根据[确认出行地点坐标]重新计算");
					
					if(StringUtils.isBlank(jco.getConfmSlnglat())){
						fg = U.setPutFalse(map, "[确认出行地点坐标]不能为空");
					}else{
						spoint = jco.getConfmSlnglat();
					}
				}else{
					fg = U.setPutFalse(map, "[计算起点类型]不存在");
				}
				
				U.log(log, "[计算起点坐标] spoint="+spoint);
			}
			
			String epoint = null;// 终点：103.123456,30.123456
			if(fg){
				epoint = Util.getREP(jco.getRouteMps()).getMapPoint().getLngLat();
				
				U.log(log, "[计算终点坐标] epoint="+epoint);
			}
			
			String wspoint = null;// 途径点：103.123456,30.123456;103.123456,30.123456;
			if(fg){
				if(Util.getRWP(jco.getRouteMps()).size() == 0){
					U.log(log, "该行程无途径点");
				}else{
					wspoint = Util.getRWPLnglats(jco.getRouteMps());
					
					U.log(log, "[计算途径点坐标] wspoint="+wspoint);
				}
			}
			
			double distance = 0d;// 行程距离（米）
			int takeupTime = 0;// 行程耗时（秒）
			if(fg){
				String arr = HttpReqMeth.getRoutTimeAndDis_amap(spoint, epoint, wspoint, null);
				distance = MathUtils.div(Integer.parseInt(arr.split("-")[0]), 1000, 2);// 米转换成公里
				takeupTime = Integer.parseInt(arr.split("-")[1]);
				
				if(distance <= 0 || takeupTime <= 0){
					fg = U.setPutFalse(map, "[重新计算距离和耗时]失败");
				}
				
				U.log(log, "[行程耗时] takeupTime="+takeupTime);
			}
			
			Date etime = null;
			if(fg){
				if(type == 1){
					etime = DateUtils.getPlusSecondsD(jco.getStime(), -takeupTime);
				}else if(type == 2) {
					etime = DateUtils.getPlusSecondsD(new Date(), -takeupTime);
				}
				
				if(etime == null){
					fg = U.setPutFalse(map, "[计算耗时] 错误，请刷新再试");
				}
				
				U.log(log, "[新到达时间] useDayEnd="+DateUtils.DateToStr(etime));
			}
			
			if(fg){
				RouteLineInfo rli = jco.getRouteLineInfo();
				rli.setDistance(distance);
				routeLineInfoDao.update(rli);
				U.log(log, "更新-行程线路-距离");
				
				jco.setEtime(etime);
				update(jco);
				U.log(log, "更新-行程距离和完团时间-完成");
				
				U.setPut(map, 1, "重新计算成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}

}
