package com.fx.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.CommonDao;
import com.fx.dao.company.CompanyDiscountDao;
import com.fx.dao.company.PickupAreaSetDao;
import com.fx.dao.company.PickupPartnerDao;
import com.fx.dao.company.PriceStairDao;
import com.fx.dao.company.RouteSetDao;
import com.fx.dao.order.CarPriceDao;
import com.fx.dao.order.OrderParamDao;
import com.fx.entity.company.PickupAreaSet;
import com.fx.entity.company.PickupPartner;
import com.fx.entity.company.PriceStair;
import com.fx.entity.company.RouteSet;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.CarPrice;
import com.fx.entity.order.OrderParam;
import com.fx.service.order.CarPriceService;

@Service
@Transactional
public class CarPriceServiceImpl extends BaseServiceImpl<CarPrice, Long> implements CarPriceService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private CarPriceDao carPriceDao;
	@Override
	public ZBaseDaoImpl<CarPrice, Long> getDao() {
		return carPriceDao;
	}
	
	/** 公共-服务 */
	@Autowired
	private CommonDao commonDao;
	/** 单程接送-临时订单参数-服务 */
	@Autowired
	private OrderParamDao orderParamDao;
	/** 车队接送区域设置-服务 */
	@Autowired
	private PickupAreaSetDao pickupAreaSetDao;
	/** 价格阶梯列表 */
	@Autowired
	private PriceStairDao priceStairDao;
	/** 行程设置 */
	@Autowired
	private RouteSetDao routeSetDao;
	/** 车队合作账号-服务 */
	@Autowired
	private PickupPartnerDao pickupPartnerDao;
	/** 单位优惠券-服务 */
	@Autowired
	private CompanyDiscountDao companyDiscountDao;
	
	
	
	@Override
	public Map<String, Object> findCarPriceList(ReqSrc reqsrc, Customer lcus, String companyNum, String backRelNum,
		String num, String isShuttle, String spoint, String epoint, String wpoints, String gotime, String flyOrDownTime) {
		String logtxt = U.log(log, "获取-单程接送-临时车辆价格列表数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			String lname = "", lphone = "";// 默认发送短信手机号
			if(fg){
				if(lcus == null){
					fg = U.setPutFalse(map, "[登录用户]为空");
				}else{
					lname = lcus.getBaseUserId().getUname();
					lphone = lcus.getBaseUserId().getPhone();
					
					U.log(log, "[登录用户名] lname="+lname+", [登录用户手机号] lphone="+lphone);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(companyNum)){
//					fg = U.setPutFalse(map, "[用户所属车队编号]不能为空");
					U.log(log, "[下单所属单位编号]为空，则默认：飞翔车队单位编号");
					companyNum = QC.DEF_COMPANY_NUM;
					
				}else{
					companyNum = companyNum.trim();
					
					U.log(log, "[下单所属单位编号] companyNum="+companyNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(backRelNum)){
					U.log(log, "[返程关联编号]为空");
				}else{
					backRelNum = backRelNum.trim();
					
					U.log(log, "[返程关联编号] backRelNum="+backRelNum);
				}
			}
			
			int _isShuttle = -1;
			if(fg){
				if(StringUtils.isEmpty(isShuttle)){
					fg = U.setPutFalse(map, "[是否接送]不能为空");
				}else{
					isShuttle = isShuttle.trim();
					if(!FV.isInteger(isShuttle)){
						fg = U.setPutFalse(map, "[是否接送]格式错误");
					}else{
						_isShuttle = Integer.parseInt(isShuttle);
					}
					
					U.log(log, "[是否接送] isShuttle="+isShuttle);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(num)){
					U.log(log, "[航班号/车次号]为空");
				}else{
					num = num.trim();
					
					U.log(log, "[航班号/车次号] num="+num);
				}
			}
			
			// 保存地点 省市县
			List<String> cs = new ArrayList<String>();
			
			String slnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(spoint)) {
					fg = U.setPutFalse(map, "[出发地点]不能为空");
				}else {
					spoint = spoint.trim();
					if(spoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[出发地点]格式错误");
					}else {
						slnglat = spoint.split("=")[1];
						cs.add(spoint.split("=")[2]);
						
						U.log(log, "[出发地点坐标] slnglat="+slnglat);
					}
					
					U.log(log, "[出发地点] spoint="+spoint);
				}
			}
			
			String elnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(epoint)) {
					fg = U.setPutFalse(map, "[到达地点]不能为空");
				}else {
					epoint = epoint.trim();
					if(epoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[到达地点]格式错误");
					}else {
						elnglat = epoint.split("=")[1];
						cs.add(epoint.split("=")[2]);
						
						U.log(log, "[到达地点坐标] elnglat="+elnglat);
					}
					
					U.log(log, "[到达地点] epoint="+epoint);
				}
			}
			
			String wlnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(wpoints)) {
					U.log(log, "[途径地点]为空");
				}else {
					wpoints = wpoints.trim();
					if(wpoints.indexOf(";") == -1) {
						fg = U.setPutFalse(map, "[途径地点]格式错误");
					}else {
						String[] wps = wpoints.split(";");
						for(int i = 0; i < wps.length; i++) {
							wlnglat += wps[i].split("=")[1]+";";
							cs.add(wps[i].split("=")[2]);
						}
						
						U.log(log, "[途径地点坐标] wlnglat="+wlnglat);
					}
					
					U.log(log, "[途径地点] wpoints="+wpoints);
				}
			}
			
			ServiceType serviceType = null;
			if(fg) {
				serviceType = Util.getRouteServiceType(cs);
				
				U.log(log, "[行程业务类型] serviceType="+serviceType);
			}
			
			// 选择地点所在城市：接-到达地点所在城市；送-出发地点所在城市；
			String selCity = "";
			// 选择地点所在区/县：接-到达地点所在区/县；送-出发地点所在区/县；
			String selCounty = "";
			// 飞机/火车站点（双流机场T1/火车东站）：接-出发地点所在站点；送-到达地点所在站点；
			String station = "";
			if(fg) {
				if(_isShuttle == 0) {// 接
					selCity = epoint.split("=")[0].split(" ")[0];
					selCounty = cs.get(1);
					station = spoint.split("=")[0].split(" ")[1];
				}else if(_isShuttle == 1) {// 送
					selCity = spoint.split("=")[0].split(" ")[0];
					selCounty = cs.get(0);
					station = epoint.split("=")[0].split(" ")[1];
				}
			}
			
			Date stime = null, etime = null, fdtime = null;
			if(fg){
				if(fg){
					if(StringUtils.isEmpty(gotime)){
						fg = U.setPutFalse(map, "[出发时间]不能为空");
					}else{
						gotime = gotime.trim();
						if(!FV.isDate(gotime)){
							fg = U.setPutFalse(map, "[出发时间]格式错误");
						}else{
							stime = DateUtils.strToDate(gotime);
						}
						
						U.log(log, "[出发时间] gotime="+gotime);
					}
				}
				
				if(fg){
					// 存在航班号（一般存在航班号就存在起飞/降落时间）
					if(StringUtils.isNotBlank(num)){
						if(StringUtils.isEmpty(flyOrDownTime)){
							fg = U.setPutFalse(map, "[起飞或降落时间]不能为空");
						}else{
							flyOrDownTime = flyOrDownTime.trim();
							if(!FV.isDate(flyOrDownTime)){
								fg = U.setPutFalse(map, "[起飞或降落时间]格式错误");
							}else{
								fdtime = DateUtils.strToDate(flyOrDownTime);
							}
							
							U.log(log, "[起飞或降落时间] flyOrDownTime="+flyOrDownTime);
						}
					}
					
					// 下单时间
					Date today = DateUtils.strToDate(DateUtils.DateToStr("yyyy-MM-dd HH:mm", new Date()));
					long putTime = 1000*60*60;// 时间基数
					if(fdtime != null){
						U.log(log, "[存在]起飞/降落时间");
						
						if(_isShuttle == 0){// 接
							// 有航班号，下单时间(即当前时间)相对降落时间需提前3小时，出发相对降落时间需延长半小时
			              	if(today.getTime() > fdtime.getTime() - putTime*3){
			              		fg = U.setPutFalse(map, "准备时间不足，需提前3小时下单");
			              	}else if(stime.getTime() < fdtime.getTime() + putTime*0.5){
			              		fg = U.setPutFalse(map, "出发时间需推迟30分钟出发");
			              	}
						}else{// 送
							// 有航班号，相对起飞时间需提前6小时下单，出发时间需提前3.5小时
							if(today.getTime() > fdtime.getTime() - putTime*6){
								fg = U.setPutFalse(map, "准备时间不足，需提前6小时下单");
							}else if(stime.getTime() < fdtime.getTime() - putTime*3.5){
								fg = U.setPutFalse(map, "[出发时间]需提前3.5小时");
							}
						}
					}else{
						U.log(log, "[不存在]起飞/降落时间");
						
						if(stime.getTime() < today.getTime() + putTime*3){
							if(_isShuttle == 0){// 接
								fg = U.setPutFalse(map, "[接机时间]需提前3小时");
							}else{// 送
								fg = U.setPutFalse(map, "[送机时间]需提前3小时");
							}
						}
					}
					
				}
				
				// 预约时间不能是今天晚上9点与明天早上9点之间的时间
				if(fg){
					Date jt_21 = DateUtils.strToDate(DateUtils.DateToStr(new Date()).split(" ")[0]+" 21:00:00");						// 今天的晚上21点
					Date mt_9 = DateUtils.strToDate(DateUtils.getPlusDays("yyyy-MM-dd HH:mm:ss", jt_21, 1).split(" ")[0]+" 09:00:00");	// 明天的上午9点
					
					// 当前时间超过晚上9点时，才判断预约时间在：今天晚上21点到明天早上9点之间
					if(new Date().getTime() > jt_21.getTime() && (stime.getTime() >= jt_21.getTime() && stime.getTime() <= mt_9.getTime())){
						fg = U.setPutFalse(map, 0, "抱歉，今晚9点到明早9点之间是师傅休息时间，请选择其他时间出发");
					}
				}
			}
			
			CarRouteRes ucp2ucp = null;
			if(fg){
				ucp2ucp = commonDao.queryCarRouteRes(slnglat, elnglat, null, null);
				if(ucp2ucp == null){
					fg = U.setPutFalse(map, "获取行程距离失败，请重新获取");
				}
			}
			
			if(fg){
				List<PickupAreaSet> areas = new ArrayList<PickupAreaSet>();
				if(fg){
					// 根据乘客选择地点（接机-目的地；送机-出发地）所在城市名称，匹配后台设置数据，获取该点可能在的区域（区域状态为：开启）列表
					hql = "from PickupAreaSet where areaAddress like ? and isOpen = ? order by addTime asc";
					areas = pickupAreaSetDao.findhqlList(hql, "%省-"+selCity+"%", 1);
					if(areas.size() == 0){
						fg = U.setPutFalse(map, "抱歉，该区域暂未开通服务");
					}
				}
				
				List<RouteSet> routeSetsList = new ArrayList<RouteSet>();
				if(fg){
					List<Object> areas_arr = new ArrayList<Object>();
					for (PickupAreaSet pas : areas) {
						areas_arr.add(pas.getId());// 保存符合条件的【区域id】
					}
					
					// 获取区域列表中所有不拼车行程终点是指定地址名称，如：双流机场T1
					hql = "from RouteSet where areaId in(:v0) and routeType = :v1 and destination = :v2";
					routeSetsList = routeSetDao.findListIns(hql, areas_arr.toArray(), 1, station);
					
					if(routeSetsList.size() == 0){
						fg = U.setPutFalse(map, "抱歉，暂未开通该行程的服务");
					}
				}
			
				// 符合条件的区域列表
				List<PickupAreaSet> areaSetList = new ArrayList<PickupAreaSet>();
				if(fg){
					List<Long> areaIdsList = new ArrayList<Long>();
					for (RouteSet rs : routeSetsList) {
						// 保存符合条件的区域id，sql已去重复
						areaIdsList.add(rs.getAreaId());
					}
					
					if(areaIdsList.size() == 0){
						fg = U.setPutFalse(map, "抱歉，未匹配到合适的行程，请选择其他地点");
					}else{
						U.log(log, "符合条件的区域id数组："+StringUtils.join(areaIdsList.toArray(), ","));
						
						// 再次筛选出符合条件的数据
						for (PickupAreaSet pas : areas) {
							if(areaIdsList.contains(pas.getId())){
								areaSetList.add(pas);
							}
						}
						
						if(areaSetList.size() == 0){
							fg = U.setPutFalse(map, "抱歉，未匹配到合适的区域，请选择其他地点");
						}
					}
				}
				
				PickupAreaSet finalArea = null;		// 最终的一条区域数据
				double lng = 0d, lat = 0d;			// 乘客上车点-经度, 纬度
				double minDistance = -1;			// 区域中最近区域的直线距离
				if(fg){
					// 与区域中心点作比较，接机-终点，送机-起点，即地图选择点；
					String mtPoint = _isShuttle == 0 ? epoint : spoint;
					// 用作与区域中心点比较的上车点经度、纬度
					lng = Double.parseDouble(mtPoint.split(",")[0]);// 乘客上车点-经度 
					lat = Double.parseDouble(mtPoint.split(",")[1]);// 乘客上车点-纬度
					
					// 1.找出上车点所在的所有区域列表
					for (PickupAreaSet pas : areaSetList) {
						double area_center_lng = pas.getMapPoint().getLng();// 区域中心点的-经度
						double area_center_lat = pas.getMapPoint().getLat();// 区域中心点的-纬度
						
						// 获取区域中心点-指定点之间的直线距离
						double range = UT.getDistance(area_center_lng, area_center_lat, lng, lat);
						U.log(log, "第一次筛选，两点经纬度直线距离："+range);
						
						// 距离小于区域半径，则乘客上车点在区域内
						if(range <= pas.getDefAreaRound()){
							U.log(log, "存在地点在区域内的区域");
						}
						
						// 无论上车点是否在区域内，都要找出最短的一条区域对象
						if(minDistance == -1 || minDistance > range){// 说明新的直线距离小于已保存的直线距离
							minDistance = range;		// 则更新距离变量值
							finalArea = pas;			// 保存最短距离区域对象
						}
					}
					
					U.log(log, "唯一区域中心点地址："+finalArea.getMapPoint().getAddress()+"，经纬度："+finalArea.getMapPoint().getLngLat());
				}
				
				// 最终的一条行程数据
				RouteSet route = null;									
				if(fg){
					// 遍历第二次筛选的行程列表，获取最终的一条行程
					for (RouteSet rs : routeSetsList) {
						// 该行程中一定存在该区域id
						if(rs.getAreaId() == finalArea.getId()){
							route = rs;// 保存唯一的行程
							break;
						}
					}
					
					U.log(log, "匹配行程："+route.getDestination());
				}
			
				// 筛选出最终行程价格阶梯列表
				List<PriceStair> pslist = new ArrayList<PriceStair>();	
				if(fg){
					// 获取唯一行程种符合条件的价格阶梯列表
					hql = "from PriceStair where routeId = ? and priceType in(?,?,?,?) and carConfig is not null order by typeCount asc";
					pslist = priceStairDao.findhqlList(hql, route.getId(), 0, 1, 2, 4);
					if(pslist.size() == 0){
						fg = U.setPutFalse(map, "抱歉，未找到符合条件的车辆价格阶梯");
					}
				}
				
				
				// 接机：超范围距离AC-B = (起点->终点->中心点) - (中心点->起点)
				// 送机：超范围距离AC-B = (中心点->起点->终点) - (中心点->终点)
				
				CarRouteRes mpAC = null;	// 行程距离AC
				CarRouteRes mpB = null;	// 行程距离B
				if(fg){
					U.log(log, "订单行程[不存在]途经点");
					
					String acpoint = finalArea.getMapPoint().getLngLat();
					
					if(_isShuttle == 0) {// 接机：超范围距离 = (起点->终点->中心点) - (中心点->起点)
						mpAC = commonDao.queryCarRouteRes(spoint, acpoint, epoint, null);
						mpB = commonDao.queryCarRouteRes(acpoint, spoint, null, null);
					}else {// 送机：超范围距离 = (中心点->起点->终点) - (中心点->终点)
						mpAC = commonDao.queryCarRouteRes(acpoint, epoint, spoint, null);
						mpB = commonDao.queryCarRouteRes(acpoint, epoint, null, null);
					}
					
					if(mpAC == null || mpB == null){
						fg = U.setPutFalse(map, "获取行程距离失败，请重新获取");
						
						U.log(log, "请求高德地图计算【区域中心点-下车点-上车点距离、区域中心点-上车点】失败！！！");
					}else{
						// 到达时间 = 出发时间 + 行程距离耗时（增减20%的耗时，防止路上堵车）
						etime = DateUtils.getPlusSecondsD(stime, -Long.parseLong((int)(ucp2ucp.getTimeCons()*1.2)+""));
					}
				}
				
				// 保存最终结果数据
				if(fg){
					long opid = 0;
					if(fg){
						// 保存发布订单参数
						OrderParam op = orderParamDao.addOrderParam(backRelNum, 1, companyNum, serviceType, lname, lphone, 
							1, _isShuttle, spoint, epoint, num, wpoints, MathUtils.div(ucp2ucp.getDistance(), 1000, 1), 
							ucp2ucp.getTimeCons()/60, stime, etime, fdtime, null);
						if(op != null) opid = op.getId();
						
						// 查询前，批量删除查询用户之前的车辆查询数据
						hql = "delete from CarPrice where uname = ?";
						int c = carPriceDao.batchExecute(hql, lname);
						U.log(log, "批量删除之前车辆查询数据"+c+"条");
					}
					
					// 计算车队合作优惠
					PickupPartner pp = null;
					if(fg){
						pp = pickupPartnerDao.findObj("from PickupPartner where partner = ? and teamNo = ?", lname, companyNum);
						if(pp == null){
							U.log(log, "未设置车队合作优惠");
						}else{// 是车队的合作账户
							String endTimeStr = DateUtils.getPlusDays(DateUtils.yyyy_MM_dd_HH_mm_ss, pp.getAddTime(), (long)pp.getValidDay());
							if(new Date().getTime() > DateUtils.strToDate(endTimeStr).getTime()){// 优惠已过期
								pp = null;
								
								U.log(log, "优惠券已经过期");
							}
						}
					}
					
					if(fg){
						List<CarPrice> its = new ArrayList<CarPrice>();
						for (PriceStair ps : pslist) {
							// 调用方法，获取单个车辆价格信息
							CarPrice cp = carPriceDao.findCarPrice(lname, companyNum, _isShuttle, selCity, selCounty, 
								slnglat, elnglat, station, stime, ps.getPriceType(), ps.getCarConfig(), ps.getTypeCount(), 
								minDistance, ucp2ucp, mpAC, mpB, finalArea, route, ps, opid, pp);
									
							its.add(cp);
						}
						
						map.put("routeKm", (int)ucp2ucp.getDistance()/1000); 	// 行程距离（公里）
						map.put("routeTime", (int)ucp2ucp.getTimeCons()/60); 	// 行程耗时（分钟）
						map.put("data", its);									// 临时车辆价格列表
						
						U.setPut(map, 1, "获取成功");
					}
				}
				
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findSelCarPrice(ReqSrc reqsrc, Customer lcus, String companyNum, String selCarId) {
		String logtxt = U.log(log, "获取-单程接送-所选择的临时车辆价格信息", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			CarPrice cp = null;
			if(fg) {
				if(StringUtils.isEmpty(selCarId)) {
					fg = U.setPutFalse(map, "[临时车辆价格对象id]不能为空");
				}else {
					selCarId = selCarId.trim();
					if(!FV.isLong(selCarId)) {
						fg = U.setPutFalse(map, "[临时车辆价格对象id]格式错误");
					}else {
						cp = carPriceDao.findByField("id", Long.parseLong(selCarId));
						if(cp == null) {
							fg = U.setPutFalse(map, "[临时车辆价格对象]不存在");
						}else {
							map.put("cp", cp);
						}
					}
					
					U.log(log, "[临时车辆价格对象id] selCarId="+selCarId);
				}
			}
			
			OrderParam op = null;
			if(fg) {
				op = orderParamDao.findByField("id", cp.getOpid());
				if(op == null) {
					fg = U.setPutFalse(map, "[临时订单参数对象]不存在");
				}else {
					map.put("op", op);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(companyNum)) {
					fg = U.setPutFalse(map, "[单位编号]不能为空");
				}else {
					companyNum = companyNum.trim();
					
					U.log(log, "[单位编号] companyNum="+companyNum);
				}
			}
			
			if(fg) {
		  		Map<String, Object> m = companyDiscountDao.findUseCouponList(reqsrc, 
		  			lcus, companyNum, selCarId);
				if("1".equals(m.get("code").toString())){
					map.put("uselist", m.get("uselist"));
					map.put("nouselist", m.get("nouselist"));
				}
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
