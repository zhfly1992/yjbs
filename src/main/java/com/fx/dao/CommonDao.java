package com.fx.dao;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.clazz.OrderTemp;
import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.entity.order.CarOrder;

@Repository
public class CommonDao extends ZBaseDaoImpl<Object, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 远程请求-服务 */
	@Autowired
	private RestTemplate restTemplate;
	
	
	/**
	 * 查询-两个坐标点之间的驾车行程规划结果（高德地图）
	 * @param spoint 	起点[lng, lat] （经纬度小数点不超过6位）
	 * @param epoint 	终点[lng, lat] （经纬度小数点不超过6位）
	 * @param waypoints 途经点[lng, lat;lng, lat;] （经纬度小数点不超过6位）
	 * @param stg		查询线路策略
	 * 2-距离最短；10-路程较短（默认）；19-优先选择高速路（与高德地图的“高速优先”策略一致）；
	 * @return 车辆行程结果
	 */
	public CarRouteRes queryCarRouteRes(String spoint, String epoint, String waypoints, String stg) {
		String logtxt = U.log(log, "查询-两个坐标点之间的驾车行程规划结果（高德地图）");
		
		CarRouteRes mp = new CarRouteRes();
		boolean fg = true;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			if(fg){
				if(StringUtils.isEmpty(spoint)){
					fg = U.logFalse(log, "[起点坐标]不能为空");
				}else{
					spoint = spoint.trim();
					if(spoint.indexOf(",") == -1){
						fg = U.logFalse(log, "[起点坐标]格式不正确");
					}else {
						mp.setSlnglat(spoint);
					}
					
					U.log(log, "[起点坐标] spoint="+spoint);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(epoint)){
					fg = U.logFalse(log, "[终点坐标]不能为空");
				}else{
					epoint = epoint.trim();
					if(epoint.indexOf(",") == -1){
						fg = U.logFalse(log, "[终点坐标]格式不正确");
					}else {
						mp.setElnglat(epoint);
					}
					
					U.log(log, "[终点坐标] epoint="+epoint);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(waypoints)){
					U.log(log, "[途径点]为空");
					
					waypoints = "";
				}else{
					waypoints = waypoints.trim();
					if(waypoints.indexOf(",") == -1){
						fg = U.logFalse(log, "[途径点坐标]格式不正确");
					}else {
						mp.setWlnglat(waypoints);
					}
					
					U.log(log, "[途径点坐标] waypoints="+waypoints);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(stg)) {
					U.log(log, "[计算策略]为空");
					stg = "10";
				}else {
					stg = stg.trim();
					
					U.log(log, "[计算策略] stg="+stg);
				}
				
				mp.setStg(stg);
			}
			
			JsonNode js = null;
			if(fg){
				// 发送 GET请求
				String params = "origin="+spoint+"&destination="+epoint+"&waypoints="+waypoints+"&output=json&strategy="+stg+"&key="+QC.AMAP_WEB_SER_AK;
				String res = restTemplate.getForObject("http://restapi.amap.com/v3/direction/driving?"+params, String.class);
				if(StringUtils.isEmpty(res)){
					fg = U.logFalse(log, "[http请求结果]为空");
				}else{
					js = mapper.readTree(res);
					
					//U.log(log, "[请求结果] json="+js);
				}
			}
			
			JsonNode path = null;
			if(fg){
				String sta = U.Cq(js, "status");
				if(!StringUtils.equals("1", sta)){
					fg = U.logFalse(log, "[请求结果异常], 状态="+sta);
				}else{
					JsonNode route = mapper.readTree(js.get("route").toString());
					JsonNode jarr = route.get("paths");
					
		            path = jarr.get(0);// 获取数组中的第一个对象
		            
		            //U.log(log, "第一个路径规划："+path.toString());
				}
			}
			
			// 获取：距离
			if(fg){
				String distance = U.Cq(path, "distance");	// 行驶距离 单位：米
				if(StringUtils.isEmpty(distance)){
					fg = U.logFalse(log, "[行车距离]为空");
				}else{
					distance = distance.trim();
					if(!FV.isInteger(distance)){
						fg = U.logFalse(log, "[行车距离]格式错误，不是整数");
					}else{
						mp.setDistance(Integer.parseInt(distance));
					}
					
					U.log(log, "[行车距离] distance="+distance);
				}
			}
			
			// 获取：耗时
			if(fg){
				String duration =  U.Cq(path, "duration");	// 预计行驶时间 单位：秒
				if(StringUtils.isEmpty(duration)){
					fg = U.logFalse(log, "[行车耗时]为空");
				}else{
					duration = duration.trim();
					if(!FV.isInteger(duration)){
						fg = U.logFalse(log, "[行车耗时]格式错误，不是整数");
					}else{
						mp.setTimeCons(Integer.parseInt(duration));
					}
					
					U.log(log, "[行车耗时] duration="+duration);
				}
			}
			
			// 获取：过路费
			if(fg){
				String tolls =  U.Cq(path, "tolls");			// 过路费 单位：元
				if(StringUtils.isEmpty(tolls)){
					fg = U.logFalse(log, "[行车过路费]为空");
				}else{
					tolls = tolls.trim();
					if(!FV.isDouble(tolls)){
						fg = U.logFalse(log, "[行车过路费]格式错误，不是浮点型");
					}else{
						mp.setTolls(Integer.parseInt(tolls));
					}
					
					U.log(log, "[行车过路费] tolls="+tolls);
				}
			}
			if(fg) {
				String citys = "";// 途经城市数组字符串
				List<String> citylist = new ArrayList<String>();
				JsonNode steps=path.get("steps");
				if(steps.isArray()) {
					for (JsonNode step : steps) {
						if(step.get("cities")!=null){// 存在城市信息
							JsonNode cities = step.get("cities");// 获取城市信息数组
							if(cities.isArray()){
								for(JsonNode citie : cities){// 获取城市信息（一般每一步只有一个城市信息）
									if(StringUtils.join(citylist, ",").lastIndexOf(U.Cq(citie, "name")) == -1){// 不存在城市，则添加
										citylist.add(U.Cq(citie, "name"));
									}
								}
							}
						}
				    }
				}
				if(citylist.size() > 0){
					citys = StringUtils.join(citylist, "=");
				}
				
				mp.setWayCity(citys);
			}
			if(!fg) mp = null;
		} catch (Exception e) {
			e.printStackTrace();
			U.log(log, logtxt);
		}
        
		return mp;
	}
	
	/**
	 * 派单获取车辆时返回临时订单
	 * @author xx
	 * @date 20200601
	 * @param sendOrderNum 派单订单号
	 * @param colist 派单订单
	 * @return
	 */
	public Map<String, Object> sendTempParam(List<CarOrder> colist,int isSmart) {
		String logtxt = U.log(log, "派单-根据选择的派单订单重新生成一个临时派单订单（主要用于多个派单）");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if(colist==null) {
				fg=U.setPutFalse(map, "订单不存在，请检查数据");
			}
			if(fg) {
				for (CarOrder eachco : colist) {
					if(eachco.getDisCar()!=null) {
						fg=U.setPutFalse(map, "订单【"+eachco.getOrderNum()+"】已派单，不能参与此次派单");
						break;
					}else if(isSmart==1 && eachco.getIsExternal()==1) {
						fg=U.setPutFalse(map, "订单【"+eachco.getOrderNum()+"】已外调，请人工线下派单");
						break;
					}
				}
			}
			RouteType routeType=colist.get(0).getCarOrderBase().getRouteType();//行程类型
			if(fg) {
				if(routeType.equals(RouteType.ONE_WAY) && colist.size()!=1) {//单程接送只能单个派单
					fg=U.setPutFalse(map, "单程接送订单只能单个派单");
				}
			}
			if(fg) {
				OrderTemp ot=new OrderTemp();
				String unitNum=colist.get(0).getCarOrderBase().getUnitNum();//单位编号
				int needSeats=colist.get(0).getNeedSeats();//所需座位数
				Date sTime=colist.get(0).getStime();//开始时间
				Date eTime=colist.get(0).getEtime();//结束时间
				String limitNum="";//限号
				String sLonLat=colist.get(0).getRouteMps().get(0).getMapPoint().getLngLat();//起点经纬度
				String eLonLat=colist.get(0).getRouteMps().get(1).getMapPoint().getLngLat();//终点经纬度
				boolean isHaveCitySer=false;
				boolean isHaveProSer=false;
				for (CarOrder eachco:colist) {
					if(StringUtils.isNotBlank(eachco.getLimitNum())) {
						limitNum+=eachco.getLimitNum()+",";
					}
					if(eachco.getNeedSeats()>needSeats) {
						needSeats=eachco.getNeedSeats();
					}
					if(eachco.getStime().before(sTime)) {
						sTime=eachco.getStime();
						sLonLat=eachco.getRouteMps().get(0).getMapPoint().getLngLat();
					}
					if(eachco.getEtime().after(eTime)) {
						eTime=eachco.getEtime();
						eLonLat=eachco.getRouteMps().get(1).getMapPoint().getLngLat();
					}
					if(eachco.getServiceType().equals(ServiceType.PROVINCE_SER) && !isHaveProSer) {
						isHaveProSer=true;
					}
					if(eachco.getServiceType().equals(ServiceType.CITY_SER) && !isHaveCitySer) {
						isHaveCitySer=true;
					}
				}
				ot.setUnitNum(unitNum);
				ot.setRouteType(routeType);
				ot.setStime(sTime);
				ot.setEtime(eTime);
				ot.setsLonLat(sLonLat);
				ot.seteLonLat(eLonLat);
				ot.setNeedSeats(needSeats);
				if(StringUtils.isNotEmpty(limitNum))ot.setLimitNum(limitNum.substring(0,limitNum.length()-1));
				if(isHaveProSer) {
					ot.setServiceType(ServiceType.PROVINCE_SER);
				}else if(isHaveCitySer){
					ot.setServiceType(ServiceType.CITY_SER);
				}else {
					ot.setServiceType(ServiceType.COUNTY_SER);
				}
				map.put("ot", ot);
				U.setPut(map, 1, "本次派单生成临时参数成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
        
		return map;
	}
}
