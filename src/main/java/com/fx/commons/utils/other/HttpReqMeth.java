package com.fx.commons.utils.other;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;

/**
 * 发送http请求-调用工具方法类
 */
public class HttpReqMeth {
	/** 日志记录 */
	public final static Logger log = LogManager.getLogger(HttpReqMeth.class);
	
	
	/**
	 * 获取一个/多个起点到终点的驾车行程距离，可以设置途经点（高德地图）
	 * @param origin 起点（支持100个坐标对，坐标对之间用“| ”分隔；经度和纬度用","分隔）
	 * @param destination 终点（规则： lon，lat（经度，纬度）， “,”分割，如117.500244,40.417801     经纬度小数点不超过6位）
	 * @param waypoints 途经点：经度和纬度用","分割，经度在前，纬度在后，小数点后不超过6位，坐标点之间用";"分隔
	 * @param strategy 驾车策略：默认10
	 * @return [距离（米）-时间（秒）-过路费（元）-途径城市（中文逗号（，）分割）]
	 */
	public static String getRoutTimeAndDis_amap(String origin, String destination, String waypoints, String strategy){
		U.log(log, "高德地图-计算行车-距离/时间/过路费/途径城市");
		
		U.log(log, "起点 origin="+origin);
		U.log(log, "终点 destination="+destination);
		U.log(log, "途径点 waypoints="+waypoints);
		origin=origin.replace("|", ",");
		destination=destination.replace("|", ",");
		if(StringUtils.isNotEmpty(waypoints))waypoints=destination.replace("|", ",");
		//origins = "103.770690,30.544077|103.679351,30.646140";//区域中心点|乘客起点
		//destination = "104.073341,30.696769";//乘客目的地
		
		// 2:返回结果距离最短（不考虑路况，仅走距离最短的路线，但是可能存在穿越小路/小区的情况）
		// 10:返回结果会躲避拥堵，路程较短，尽量缩短时间，与高德地图的默认策略也就是不进行任何勾选一致（存在多条线路情况）
		// 19:返回的结果会优先选择高速路，与高德地图的“高速优先”策略一致（存在多条线路情况）
		if(StringUtils.isEmpty(strategy)) strategy = "10";
		if(StringUtils.isEmpty(waypoints)) waypoints = "";// 没有途经点
		
		String url = "http://restapi.amap.com/v3/direction/driving";
		String params = "origin="+origin+"&destination="+destination+"&waypoints="+waypoints+
				"&output=json&strategy="+Integer.parseInt(strategy)+"&key="+QC.AMAP_WEB_SER_AK;
		//发送 GET 请求
		String str = HttpRequest.sendGet(url, params, null);
		
		JSONObject json = JSONObject.parseObject(str);//将json对象字符串转换成json对象
        
        String sta = json.getString("status");
        
        String res = "";
        if("1".equals(sta)){
        	JSONObject route = JSONObject.parseObject(json.getString("route"));
//        	System.out.println(json.getString("route"));
            JSONArray jarr = route.getJSONArray("paths");
            
            JSONObject path = jarr.getJSONObject(0);//获取数组中的第一个对象
			
			String distance = path.getString("distance");	// 行驶距离 单位：米
			String duration = path.getString("duration");	// 预计行驶时间 单位：秒
			String tolls = path.getString("tolls");			// 过路费 单位：元
			
			String citys = "";// 途经城市数组字符串
			List<String> citylist = new ArrayList<String>();
			JSONArray steps = JSONArray.parseArray(path.getString("steps"));// 路线步骤
			if(steps.size() > 0){
				for(int b = 0; b < steps.size(); b++){
					JSONObject step = steps.getJSONObject(b);// 获取每一步
					
					if(StringUtils.isNotBlank(step.get("cities").toString())){// 存在城市信息
						JSONArray cities = JSONArray.parseArray(step.get("cities").toString());// 获取城市信息数组
						
						if(cities.size() > 0){
							for(int c = 0; c < cities.size(); c++){
								JSONObject citie = cities.getJSONObject(c);// 获取城市信息（一般每一步只有一个城市信息）
								
								if(StringUtils.join(citylist, ",").lastIndexOf(citie.getString("name")) == -1){// 不存在城市，则添加
									citylist.add(citie.getString("name"));
								}
							}
						}
					}
				}
			}
			
			if(citylist.size() > 0){
				citys = StringUtils.join(citylist, "，");
			}
			
			res = distance + "-" + duration + "-" + tolls + "-" + citys;
			
			log.info("距离:" + MathUtils.div(Double.parseDouble(distance), 1000, 2) + "公里, 耗时:" 
					+MathUtils.div(Double.parseDouble(duration), 60, 2)+"分钟, 过路费:"
					+tolls+"元, 途经城市：["+citys+"]");
        }
        
		return res;
	}
	
	
	/**
	 * 获取-两个坐标点之间的驾车行程规划（高德地图）
	 * @param upoint 	起点[lng, lat] （经纬度小数点不超过6位）
	 * @param dpoint 	终点[lng, lat] （经纬度小数点不超过6位）
	 * @param waypoints 途经点[lng, lat;lng, lat;] （经纬度小数点不超过6位）
	 * @return 点对象
	 */
	public static CarRouteRes getCarRoute(String upoint, String dpoint, String waypoints, String stg){
		String logtxt = U.log(log, "高德地图-驾车规划-距离/时间");
		
		CarRouteRes mp = new CarRouteRes();
		boolean fg = true;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			if(fg){
				if(StringUtils.isEmpty(upoint)){
					fg = U.logFalse(log, "[起点坐标]不能为空");
				}else{
					upoint = upoint.trim();
					if(upoint.indexOf(",") == -1){
						fg = U.logFalse(log, "[起点坐标]格式不正确");
					}
					
					U.log(log, "[起点坐标] upoint="+upoint);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(dpoint)){
					fg = U.logFalse(log, "[终点坐标]不能为空");
				}else{
					dpoint = dpoint.trim();
					if(dpoint.indexOf(",") == -1){
						fg = U.logFalse(log, "[终点坐标]格式不正确");
					}
					
					U.log(log, "[终点坐标] dpoint="+dpoint);
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
			}
			
			JsonNode js = null;
			if(fg){
				// 发送 GET请求
				String url = "http://restapi.amap.com/v3/direction/driving";
				String params = "origin="+upoint+"&destination="+dpoint+"&waypoints="+waypoints+"&output=json&strategy="+stg+"&key="+QC.AMAP_WEB_SER_AK;
				String str = HttpRequest.sendGet(url, params, null);
				if(StringUtils.isEmpty(str)){
					fg = U.logFalse(log, "[http请求结果]为空");
				}else{
					js = mapper.readTree(str);
					
					U.log(log, "[请求结果] json="+js);
				}
			}
			
			JsonNode path = null;
			if(fg){
				String sta = js.get("status").toString();
				if(!"1".equals(sta)){
					fg = U.logFalse(log, "[请求结果异常], 状态="+sta);
				}else{
					JsonNode route = mapper.readTree(js.get("route").toString());
					JsonNode jarr = route.get("paths");
					
		            path = jarr.get(0);// 获取数组中的第一个对象
		            
		            U.log(log, "第一个路径规划："+path.toString());
				}
			}
			
			// 获取：距离
			if(fg){
				String distance = path.get("distance").toString();	// 行驶距离 单位：米
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
				String duration = path.get("duration").toString();	// 预计行驶时间 单位：秒
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
				String tolls = path.get("tolls").toString();			// 过路费 单位：元
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
			
			if(!fg) mp = null;
		} catch (Exception e) {
			U.log(log, logtxt);
		}
        
		return mp;
	}
	
	/**
	 * 根据ip获取地址详情（高德地图）
	 * @param ip 查询的ip
	 * @return 详情地址
	 */
	public static String getAddressByIp(String ip){
		ObjectMapper mapper = new ObjectMapper();
		
		String res = "";
		try {
			String url = "https://restapi.amap.com/v3/ip";
			String params = "ip="+ip+"&output=json&key="+QC.AMAP_WEB_SER_AK;
			// 发送 GET 请求
			String str = HttpRequest.sendGet(url, params, null);
			
			//将json对象字符串转换成json对象
			JsonNode root = mapper.readTree(str);
			log.info("根据ip获取地址详情（高德地图）:"+str);
			
			String status = root.get("status").toString();
			
			if("1".equals(status)){// 成功
				String province = root.get("province").toString();
				if(StringUtils.isNotEmpty(province)){
					res += province;// 省份
					
					String city = root.get("city").toString();
					if(StringUtils.isNotEmpty(city)){
						res += "-"+city;// 城市
					}
				}
			}else{
				res = root.get("info").toString();// status=0时，info=错误信息
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return res;
	}
		
}