package com.fx.commons.utils.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;

/** 
 * 工具-类
 */
public class Util {
	private static Logger log = LogManager.getLogger(Util.class.getName());//日志记录
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("四川省-成都市-金牛区");
		list.add("四川省-成都市-金牛区");
		list.add("四川省-武汉市-金牛区");
		list.add("四川省-成都市-金牛区");
		System.out.println(getRouteServiceType(list));
	}
	
	/**
	 * 获取-旅游包车-行程业务类型文本
	 * @param list 	地点省市县列表
	 * @return 行程业务类型
	 */
	public static String getRouteServiceTypeText(List<String> list) {
		String serviceType = null;
		
		try {
			if(list.size() > 0) {
				String[] s = list.get(0).split("-");
				String prov = s[0];
				String city = s[1];
				String county = s[2];
						
				// 遍历第一遍 确定是否是=省际业务
				for (String str : list) {
					if(!prov.equals(str.split("-")[0])) {
						serviceType = "省际业务";
						break;
					}
				}
				
				// 遍历第一遍 确定是否是=市际业务
				if(serviceType == null) {
					for (String str : list) {
						if(!city.equals(str.split("-")[1])) {
							serviceType = "市际业务";
							break;
						}
					}
				}
				
				// 遍历第一遍 确定是否是=县际业务
				if(serviceType == null) {
					for (String str : list) {
						if(!county.equals(str.split("-")[2])) {
							serviceType = "县际业务";
							break;
						}
					}
				}
				
				// 省市都一样，则是县际业务
				if(serviceType == null) {
					serviceType = "县际业务";
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return serviceType;
	}
	
	/**
	 * 获取-旅游包车-行程业务类型
	 * @param list 	地点省市县列表
	 * @return 行程业务类型枚举
	 */
	public static ServiceType getRouteServiceType(List<String> list) {
		ServiceType serviceType = null;
		
		try {
			if(list.size() > 0) {
				String[] s = list.get(0).split("-");
				String prov = s[0];
				String city = s[1];
				String county = s[2];
						
				// 遍历第一遍 确定是否是=省际业务
				for (String str : list) {
					if(!prov.equals(str.split("-")[0])) {
						serviceType = ServiceType.PROVINCE_SER;
						break;
					}
				}
				
				// 遍历第一遍 确定是否是=市际业务
				if(serviceType == null) {
					for (String str : list) {
						if(!city.equals(str.split("-")[1])) {
							serviceType = ServiceType.CITY_SER;
							break;
						}
					}
				}
				
				// 遍历第一遍 确定是否是=县际业务
				if(serviceType == null) {
					for (String str : list) {
						if(!county.equals(str.split("-")[2])) {
							serviceType = ServiceType.COUNTY_SER;
							break;
						}
					}
				}
				
				// 省市都一样，则是县际业务
				if(serviceType == null) {
					serviceType = ServiceType.COUNTY_SER;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return serviceType;
	}
	
	/**
	 * 获取-旅游包车-行程详情
	 * @param stime 	出发时间
	 * @param etime 	到达时间
	 * @param saddr 	出发地址
	 * @param eaddr 	到达地址
	 * @param wpoint 	途径地址
	 * @param note 		行程备注
	 * @return 行程详情
	 */
	public static String getBcRouteDetail(Date stime, Date etime, String saddr, String eaddr, 
		String wpoint, String note) {
		String rd = "";
		
		try {
			String st = DateUtils.DateToStr("yyyy年MM月dd日 HH点mm分", stime);
			
			String waddr = "";
			String[] waddrs = wpoint.split(",");
			for(int i = 0; i < waddrs.length; i++) {
				waddr += waddrs[i].split("-")[0]+"、";
			}
			if(waddr != "") {
				waddr = waddr.substring(0, waddr.length()-1);
			}
			
			rd = st+"出发从"+saddr+"到达"+eaddr+"途径"+waddr+"；"+(StringUtils.isBlank(note) ? "" : note+"；");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rd;
	}
	
	/**
	 * 正则-获取富文本框中所有img标签的src值
	 * @param htmlStr 含有html标签的字符串内容
	 * @return img标签src值的列表
	 */
	public static List<String> getImgStr(String htmlStr) {
        List<String> list = new ArrayList<String>();
        try {
			String img = "";
			Pattern p_image;
			Matcher m_image;
			String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
			p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
			m_image = p_image.matcher(htmlStr);
			while (m_image.find()) {
			    // 得到<img />数据
			    img = m_image.group();
			    // 匹配<img>中的src数据
			    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			    while (m.find()) {
			        list.add(m.group(1));
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return list;
    }
	
	/**
	 * 获取操作用户操作信息
	 * @param oper 操作用户
	 * @param step 操作功能
	 * @return 操作信息
	 */
	public static String getOperInfo(String oper, String step) {
		String info = DateUtils.getOperTime(new Date())+"&nbsp;";
		
		if(StringUtils.isNotBlank(oper)) {
			info += ",操作员:"+oper;
		}
		
		if(StringUtils.isNotBlank(step)) {
			info += "["+step+"]";
		}
		
		return "("+info+")";
	}
	
	/**
	 * 根据报销类型获取报销文本
	 * @param rtype 报销类型
	 * @return 报销类型文本
	 */
	public static String getReimburseTxt(int rtype) {
		U.log(log, "根据报销类型获取报销类型对应文本");
		
		String reimburseTxt = "";
		
		if(rtype == 1) {
			reimburseTxt = "内部账报销";
		}else if(rtype == 2) {
			reimburseTxt = "其他报销";
		}else if(rtype == 3) {
			reimburseTxt = "加油报销";
		}else if(rtype == 4) {
			reimburseTxt = "维修报销";
		}else if(rtype == 5) {
			reimburseTxt = "团上开支";
		}else if(rtype == 6) {
			reimburseTxt = "无客户银行收入";
		}else {
			reimburseTxt = "正常报销";
		}
		
		return reimburseTxt;
	}
	
	/**
	 * 处理-航班升降/火车车次查询结果数据
	 * @param json 		json对象
	 * @param travelWay 出行方式：1-飞机；2-火车；
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public static Map<String, Object> getNumInfo(JSONObject json, int travelWay) {
		String logtxt = U.log(log, "处理-航班查询结果数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			if(fg) {
				if(json == null) {
					fg = U.setPutFalse(map, "获取错误");
				}else {
					U.log(log, "json结果："+json.toString());
				}
			}
			
			// 飞机升降查询结果
			if(fg && travelWay == 1) {
				if(fg) {
					String error_code = json.getString("code");
					if(!StringUtils.equals("10000", error_code)){
						fg = U.setPutFalse(map, "获取数据失败");
					}
				}
				
				JSONObject result = json.getJSONObject("result");
				if(fg) {
					if(result == null) {
						fg = U.setPutFalse(map, "获取数据结果为空");
					}else if(result.get("topic") != null && "device error".equals(result.get("topic").toString())) {
						U.log(log, result.getString("message"));
						
						fg = U.setPutFalse(map, "请检查[航班号是否正确]后再次请求");
					}
				}
				
				JSONArray ja = new JSONArray();
				if(fg) {
					ja = result.getJSONObject("output").getJSONArray("result");
					if(ja.size() == 0) {
						fg = U.setPutFalse(map, "数据列表为空");
					}
				}
				
				if(fg) {
					res.put("travelway", travelWay); 					// 出行方式
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> jo = new HashMap<String, Object>();
						
						JSONObject jb = ja.getJSONObject(i);
						
						jo.put("no", i+1); 								// 编号
						jo.put("flightNo", jb.get("flightNo")); 		// 航班号
						jo.put("rate", jb.get("rate")); 				// 准点率
						jo.put("depCity", jb.get("depCity")); 			// 出发城市
						jo.put("depPort", jb.get("depPort")); 			// 出发机场
						jo.put("depTerminal", jb.get("depTerminal")); 	// 出发航站楼
						String depScheduled = jb.get("depScheduled").toString().replace("T", " ").replace(":00Z", "");
						jo.put("depScheduled", depScheduled); 			// 出发时间
						
						jo.put("arrCity", jb.get("arrCity")); 			// 到达城市
						jo.put("arrPort", jb.get("arrPort")); 			// 到达机场
						jo.put("arrTerminal", jb.get("arrTerminal")); 	// 到达航站楼
						String arrScheduled = jb.get("arrScheduled").toString().replace("T", " ").replace(":00Z", "");
						jo.put("arrScheduled", arrScheduled); 			// 到达时间
						
						list.add(jo);
					}
					
					res.put("list", list);
					
					U.setPut(map, 1, "获取数据列表成功");
				}
				
			}
			
			if(fg && travelWay == 2) {// 火车车次查询结果
				if(fg) {
					String error_code = json.getString("code");
					if(!StringUtils.equals("10000", error_code)){
						fg = U.setPutFalse(map, "获取数据失败");
					}
				}
				
				JSONObject result = json.getJSONObject("result");
				if(fg) {
					if(result == null) {
						fg = U.setPutFalse(map, "获取数据结果为空");
					}else if(result.get("status") != null && "203".equals(result.get("status").toString())) {
						U.log(log, result.getString("msg"));
						
						fg = U.setPutFalse(map, "请检查[车次号是否正确]后再次请求");
					}
				}
				
				JSONObject jop = null;
				if(fg) {
					String jopStr = result.getString("result");
					if(StringUtils.isEmpty(jopStr)) {
						fg = U.setPutFalse(map, "获取[车次号]基本数据为空");
					}else {
						jop = result.getJSONObject("result");
						if(jop == null) {
							fg = U.setPutFalse(map, "获取[车次号]基本数据为空");
						}
					}
				}
				
				JSONArray ja = new JSONArray();
				if(fg) {
					ja = jop.getJSONArray("list");
					if(ja.size() == 0) {
						fg = U.setPutFalse(map, "站点数据列表为空");
					}
				}
				
				if(fg) {
					res.put("travelway", travelWay); 					// 出行方式
					res.put("trainno", jop.get("trainno")); 			// 车次号
					res.put("date", jop.get("date")); 					// 发车日期
					res.put("typename", jop.get("typename")); 			// 车辆类型
					res.put("startstation", jop.get("startstation")); 	// 起点站
					res.put("endstation", jop.get("endstation")); 		// 终点站
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> jo = new HashMap<String, Object>();
						
						JSONObject jb = ja.getJSONObject(i);
						
						jo.put("sequenceno", jb.get("sequenceno")); 		// 停靠站编号
						jo.put("station", jb.get("station")); 				// 站名
						String arrivaltime = jb.get("arrivaltime").toString();
						if(arrivaltime.indexOf(":") == -1) arrivaltime = "";
						jo.put("arrivaltime", arrivaltime); 				// 进站时间
						jo.put("departuretime", jb.get("departuretime")); 	// 发车时间
						jo.put("stoptime", jb.get("stoptime")); 			// 停留时间（分钟）
						jo.put("costtime", jb.get("costtime")); 			// 用时（分钟）
						jo.put("distance", jb.get("distance")); 			// 距离（公里）
						jo.put("day", jb.get("day")); 						// 天数
						
						list.add(jo);
						
						if(i == ja.size() - 1) {// 最后一个，保存总行程耗时
							res.put("costtimetxt", jb.get("costtimetxt"));
						}
					}
					
					res.put("list", list);
					
					U.setPut(map, 1, "获取数据列表成功");
				}
				
			}
			
			map.put("data", res);
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return map;
	}
	
	/**
	 * 根据编号获取-出行方式：1-飞机；2-火车；0-不知；
	 * @param num 航班号/车次号
	 * @return 订单类型
	 */
	public static int getTravelWayByNum(String num) {
		String logtxt = "验证传入的字符串是航班/车次号";
		
		boolean fg = true;
		int res = 0;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(num)) {
					fg = U.logFalse(log, "[航班/车次号]不能为空");
				}else {
					num = num.trim();
					if(num.length() < 2) {
						fg = U.logFalse(log, "[航班/车次号]组成至少2个字符");
					}
					
					U.log(log, "[航班/车次号] num="+num);
				}
			}
			
			if(fg) {
				// 编号必须包含字母
				boolean is = FV.isConLetter(num);
				if(!is) {
					fg = U.logFalse(log, "[航班/车次号]全是数字");
				}else {
					// 获取前2位字符
					String str2 = num.substring(0, 2);
					if(!FV.isConNumber(str2)) {
						U.log(log, "前2位字符全为字母，一定是航班号");
						
						res = 1;
					}else {
						U.log(log, "前2位字符至少有一个数字，则再验证第1位字符");
						
						String str1 = str2.substring(0, 1);
						if(FV.isConNumber(str1)) {
							U.log(log, "前1位字符为数字，一定是航班号");
							
							res = 1;
						}else {
							U.log(log, "前1位字符为字母，一定是车次号");
							
							res = 2;
						}
					}
				}
			}
			
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return res;
	}
	
	/**
	 * 验证-传入字符串是否为空
	 * @param obj 传入字符串
	 * @return 为空-true；不为空-false；
	 */
	public static boolean isEmpty(String obj){
		if(StringUtils.isEmpty(obj) || "null".equals(obj)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 通过日期获取限号
	 * @param date 日期
	 * @return 限号数据
	 */
	public static String limitNumByDate(Date date){
		String res = "";
		
		// 如果时间是7：30-20：00之间，才判断限行
		boolean fg = U.isIncludeDate("07:00=20:00", DateUtils.DateToStr(date));
		if(fg){
			// 获取日期是星期几
			int week = DateUtils.getDayOfWeek(date);
			
			if(week == 1){// 周一
				res = "1,6";
			}else if(week == 2){// 周二
				res = "2,7";
			}else if(week == 3){// 周三
				res = "3,8";
			}else if(week == 4){// 周四
				res = "4,9";
			}else if(week == 5){// 周五
				res = "5,0";
			}
		}
		
		return res;
	}
	
	/**
	 * 根据用户角色-跳转不同的路由地址
	 * @param wrole 用户角色
	 * @param type 地址类型[0-登录地址; 否则-首页地址;]
	 * @return 路由地址
	 */
	/*public static String mobileRoute(CusRole wrole, int type){
		String url = "";
		
		try {
			if(CusRole.TEAM_JD == wrole){
				if(type == 0){
					url = "jd-pass-login";
				}else{
					url = "carteam/jd-main/4/0";
				}
			}else if(CusRole.TEAM_YW == wrole){
				if(type == 0){
					url = "yw-pass-login";
				}else{
					url = "carteam/yw-main/4/0";
				}
			}else if(CusRole.PT_CUS == wrole){
				if(type == 0){
					url = "cus-pass-login";
				}else{
					url = "cus/cus-main/4/0";
				}
			}else if(CusRole.TEAM_DRIVER == wrole){
				if(type == 0){
					url = "driver-pass-login";
				}else{
					url = "cus/driver-main/1/0";
				}
			}
		} catch (Exception e) {
			url = "error";
		}
		
		return url;
	}*/
	
	/**
	 * 过滤-字符串中的\s、\t、\n
	 * @param str 过滤的字符串
	 * @return 新的字符串
	 */
	public static String fitStr(String str){
		if(StringUtils.isNotEmpty(str)){
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			
			str = m.replaceAll("");
		}
		
		return str;
	}
	
	/**
	 * 获取 银行卡后四位数字
	 * @param no 银行卡卡号
	 * @return 后四位数字字符串
	 */
	public static String ftBankCardNo(String no){
		if(StringUtils.isNotBlank(no)){
			return no.substring(no.length() - 4);
		}else{
			return "";
		}
	}
	
	/**
	 * 获取-request传入的所有参数
	 * @param request request
	 * @return 字符串
	 */
	public static String getRequestParams(HttpServletRequest request){
		String pms = "";
		
		@SuppressWarnings("rawtypes")
		Enumeration enu = request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName = (String)enu.nextElement();
			pms += "&"+paraName+"="+request.getParameter(paraName);
		}
		
		if(!"".equals(pms)){
			pms = pms.substring(1);
		}
		
		return pms;
	}
	
	/**
	 * 向数组字符串中设置值，不存在设置，存在修改
	 * @param item 18982121994=up=2019-05-06 11:20:20
	 * @param index 使用item的第几项作为比较，eg：0，即使用手机号作为标识
	 * @param strArrs 数组字符串，格式按照item的格式
	 * @return 新的数组字符串
	 */
	public static String setStrToArr(String item, int index, String strArrs){
		String logtxt = U.log(log, "向数组字符串中设置值");
		List<String> list = new ArrayList<String>();
		
		try {
			if(StringUtils.isEmpty(strArrs)){
				list.add(item);
			}else{
				String[] sarrs = strArrs.split(",");
				
				String[] ns = item.split("=");
				boolean fg = false;
				for(int i = 0; i < sarrs.length; i++){
					String[] sa = sarrs[i].split("=");
					if(ns[index].equals(sa[index])){// 存在，修改
						fg = true;
						list.add(item);
					}else{
						list.add(sarrs[i]);
					}
				}
				
				if(!fg){// 不存在，添加到最后
					list.add(item);
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return StringUtils.join(list.toArray(), ",");
	}
	
	/**
	 * 获取-行车距离
	 * @param lnglat 起点/终点坐标 103.124562|30.123456/103.124562|30.123456
	 * @param wps 途径点 成都市 天府五街地铁A口-103.124562|30.123456,...
	 * @return 行程距离-公里数 eg: 55.23
	 */
	public static double getOrderRouteDis(String lnglat, String wps){
		double res = 0d;
		
		try {
			String[] lnglatArr = lnglat.split("/");
			
			String dis = lnglatArr[0].replace("|", ",");// 起点坐标
			String des = lnglatArr[1].replace("|", ",");// 终点坐标
			
			String wsp = "";// 途径点坐标
			if(StringUtils.isNotBlank(wps)){
				if(wps.indexOf(",") != -1){// 说明不止1个途径点
					String[] wpss = wps.split(",");
					for(int i = 0; i < wpss.length; i++){
						wsp += wpss[i].split("-")[1].replace("|", ",")+";";
					}
				}else{
					wsp = wps.split("-")[1].replace("|", ",");
				}
			}else{
				wsp = null;
			}
			
			String disArr = HttpReqMeth.getRoutTimeAndDis_amap(dis, des, wsp, null);
			res = Double.parseDouble(disArr.split("-")[0])/1000;
		} catch (Exception e) {
			res = -1d;
		}
		
		return res;
	}
	
	/** 
     * 计算地球上任意两点(经纬度)距离   
     * 与百度地图测距最相近，差距小于1米
     * @param long1 	起点-经度[103.123456]
     * @param lat1 		起点-纬度[30.123456]
     * @param long2 	终点-经度[103.123456]
     * @param lat2 		终点-纬度[30.123456]
     * @return 返回距离 单位：米 
     */  
    public static double getDistance(double long1, double lat1, double long2, double lat2) {  
        double a, b, R;  
        R = 6371004; // 地球半径  6371.004 6378137
        lat1 = lat1 * Math.PI / 180.0;  
        lat2 = lat2 * Math.PI / 180.0;  
        a = lat1 - lat2;  
        b = (long1 - long2) * Math.PI / 180.0;  
        double d;  
        double sa2, sb2;  
        sa2 = Math.sin(a / 2.0);  
        sb2 = Math.sin(b / 2.0);  
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
                        * Math.cos(lat2) * sb2 * sb2));  
        U.log(log, "两点距离是："+d);
        
        return d;  
    }
    
    /**
	 * 通过HashSet去重复
	 * @param list 字符串列表
	 * @return 新的字符串列表
	 */
	public static List<Object> filtRept(List<Object> list) {   
	    HashSet<Object> h = new HashSet<Object>(list);
	    
	    list.clear();   
	    list.addAll(h); 
	    
	    return list;   
	}
	
	/**
	 * 根据用户角色获取前端路由导航地址
	 * @param role 用户角色
	 * @param t 地址类型：1-主页；否则-密码登录页；
	 */
	public static String getRouteUrlByCusRole(CusRole role, int t) {
		String logtxt = U.log(log, "根据用户角色-获取导航的前端路由地址");
		String url = QC.PRO_URL;
		
		try {
			if(role == CusRole.PT_CUS) {
				url += "/yjbs-cus";
				if(t == 1) {// 主页
					url += "/main/1/0";
				}else {// 登录页面
					url += "/pass-login";
				}
			}else if(role == CusRole.TEAM_DRIVER) {
				url += "/yjbs-driver";
				if(t == 1) {// 主页
					url += "/main/1/0";
				}else {// 登录页面
					url += "/pass-login";
				}
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
		}
		
		return url;
	}
	
	/**
	 * 获取-驾车行程数据
	 * @param list 派车成功订单列表
	 * @param p 中心点（终点）
	 * @param sort 排序[1-按照时间最短升序排序；2-按照距离最短升序排序；]
	 * @param timeType 时间查询类型
	 * @return 点对象
	 */
	/*public static List<MapPoint> getRData(List<JuniorCarOrder> list, MapPoint p, int sort, String timeType){
		String logtxt = U.log(log, "获取-派车成功订单距离中心点的驾车数据");
		
		List<MapPoint> mps = new ArrayList<MapPoint>();
		boolean fg = true;
		
		try {
			String dpoint = null;
			if(fg){
				if(p == null){
					fg = U.logFalse(log, "[中心点坐标]为空");
				}else{
					dpoint = p.getLng() + "," + p.getLat();
				}
				
				U.log(log, "[中心点] dpoint="+dpoint);
			}
			
			int failCount = 0; // 请求失败条数
			if(fg){
				if(list.size() == 0){
					fg = U.logFalse(log, "没有初始列表数据");
				}else{
					for(int i = 0; i < list.size(); i++){
						JuniorCarOrder jco = list.get(i);
						
						U.log(log, "~~~~~订单编号=["+jco.getOrderNum()+"]~~~~~");
						
						// 获取地点坐标
						String upoint = jco.getLonAndLat().split("/")[1].replace("|", ",");
						if("st".equals(timeType)){
							upoint = jco.getLonAndLat().split("/")[0].replace("|", ",");
						}else{
							upoint = jco.getLonAndLat().split("/")[1].replace("|", ",");
						}
						
						MapPoint mp = HttpReqMeth.getCarRoute(upoint, dpoint, null);
						if(mp != null){
							// 设置所需数据
							mp.setId(jco.getOrderNum());			// 订单编号
							mp.setPlateNum(jco.getPlateNum()); 		// 车牌号
							if("st".equals(timeType)){
								mp.setAddr(jco.getDepartPlace()); 	// 出发地点地址		
							}else{
								mp.setAddr(jco.getDestination()); 	// 完团地点地址	
							}
							
							mp.setLnglat(upoint); 					// 坐标
							double lng = Double.parseDouble(mp.getLnglat().split(",")[0]);
							mp.setLng(lng);							// 坐标-经度
							double lat = Double.parseDouble(mp.getLnglat().split(",")[1]);
							mp.setLat(lat);
							
							*//*************处理额外数据--begin**********************//*
							Map<String, Object> dm = new HashMap<String, Object>();
							dm.put("useDayStart", DateUtils.DateToStr(jco.getUseDayStart()));
							dm.put("useDayEnd", DateUtils.DateToStr(jco.getUseDayEnd()));
							dm.put("driver", jco.getDriver());
							mp.setData(dm);
							*//*************处理额外数据--end**********************//*
							
							mps.add(mp);
						}else{
							failCount++;
							
							U.log(log, "[失败："+jco.getOrderNum()+"]");
						}
					}
					
					U.log(log, "[请求地图失败条数] failCount="+failCount);
				}
			}
			
			// 将成功的数据进行排序
			if(fg && sort == 1){// 按照时间-升序-排序
				
				Collections.sort(mps, new Comparator<MapPoint>(){
		            public int compare(MapPoint o1, MapPoint o2) {
		                // 按照MapPoint的timeCons字段进行升序排列
		                if(o1.getTimeCons() > o2.getTimeCons()){
		                    return 1;
		                }
		                
		                if(o1.getTimeCons() == o2.getTimeCons()){
		                    return 0;
		                }
		                
		                return -1;
		            }
		        });
			}
			
			if(fg && sort == 2){// 按照距离-升序-排序
				Collections.sort(mps, new Comparator<MapPoint>(){
		            public int compare(MapPoint o1, MapPoint o2) {
		                // 按照MapPoint的timeCons字段进行升序排列
		                if(o1.getDistance() > o2.getDistance()){
		                    return 1;
		                }
		                
		                if(o1.getDistance() == o2.getDistance()){
		                    return 0;
		                }
		                
		                return -1;
		            }
		        });
			}
			
//			if(fg){
//				for(int i = 0; i < mps.size(); i++){
//					System.out.println("时间："+mps.get(i).getTimeCons()+"***********距离："+mps.get(i).getDistance());
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			U.log(log, logtxt);
		}
		
		return mps;
	}*/
	
	/**
	 * 获取订单起点/终点距离指定地点的直线范围距离
	 * @param lonAndLat 订单起止点坐标
	 * @param lnglat 指定点坐标
	 * @param type 地点类型[1-起点；2-终点；]
	 * @return 直线范围距离
	 */
	public static int getDist(String lonAndLat, String lnglat, int type){
		int distance = 0;
		
		try {
			double slng = 0d, slat = 0d, elng = 0d, elat = 0d;
			
			if(type == 1){// 起点
				// 确认出行地点与订单起点的直线距离
				slng = Double.parseDouble(lonAndLat.split("/")[0].split("\\|")[0]);
				slat = Double.parseDouble(lonAndLat.split("/")[0].split("\\|")[1]);
			}else{// 终点
				// 确认出行地点与订单起点的直线距离
				slng = Double.parseDouble(lonAndLat.split("/")[1].split("\\|")[0]);
				slat = Double.parseDouble(lonAndLat.split("/")[1].split("\\|")[1]);
			}
			
			elng = Double.parseDouble(lnglat.split("\\|")[0]);
			elat = Double.parseDouble(lnglat.split("\\|")[1]);
			
			distance = (int)Util.getDistance(slng, slat, elng, elat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return distance;
	}
	
	/**
	 * 获取-角色列表中, 指定角色及其下级角色id列表
	 * @param objs 		保存角色id结果的集合
	 * @param list 		总角色列表（在这个列表中筛选）
	 * @param id 		角色id
	 */
	/*public static void getURoleSelfAndChildId(List<Object> objs, List<URole> list, String id) {
		try {
			for (URole r : list) {
				if(!objs.contains(r.getId())){// 不能重复
					if(id.equals(r.getPid()+"")){
						objs.add(r.getId());
						// 重复调用
						getURoleSelfAndChildId(objs, list, r.getId()+"");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 获取-资源列表中, 指定资源及其下级资源列表
	 * @param objs 		保存资源结果的集合
	 * @param list 		总资源列表（在这个列表中筛选）
	 * @param id 		资源id
	 */
	/*public static void getAllChildList(List<CarteamRes> objs, List<CarteamRes> list, long id) {
		try {
			for (CarteamRes o : list) {
				if(!objs.contains(o)){// 不能重复
					if(id == o.getId()){
						objs.add(o);// 自身也添加到列表
					}
					
					if(id == o.getPid()){
						objs.add(o);
						// 重复调用
						getAllChildList(objs, list, o.getId());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	
	/**
	 * 获取-角色列表中, 指定角色及其下级角色
	 * @param objs 		保存角色结果的集合
	 * @param list 		总角色列表（在这个列表中筛选）
	 * @param id 		角色id
	 */
	/*public static void getURoleSelfAndChild(List<URole> objs, List<URole> list, String id) {
		try {
			for (URole r : list) {
				// 自身也加入
				if(id.equals(r.getId()+"")){
					// 不包含才加入
					if(!objs.contains(r)){
						URole nur = (URole)r.clone();
						nur.setPid(0);
						
						objs.add(nur);
					}
				}
				
				if(id.equals(r.getPid()+"")){
					// 不包含才加入
					if(!objs.contains(r)) objs.add(r);
					// 重复调用
					getURoleSelfAndChild(objs, list, r.getId()+"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 获取-操作员列表中, 指定操作员及其下级
	 * @param objs 		保存结果的集合
	 * @param list 		总操作员列表（在这个列表中筛选）
	 * @param id 		操作员id
	 */
	/*public static void getOperSelfAndChild(List<OperatorList> objs, List<OperatorList> list, Long id) {
		try {
			for (OperatorList r : list) {
				// 自身也加入
				if(id == r.getId()){
					// 不包含才加入
					if(!objs.contains(r)){
						OperatorList o = (OperatorList)r.clone();
						o.setPid(0);
						
						objs.add(o);
					}
				}
				
				if(id == r.getPid()){
					// 不包含才加入
					if(!objs.contains(r)) objs.add(r);
					// 重复调用
					getOperSelfAndChild(objs, list, r.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/*@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> listToTreeMap(List<CarteamRes> list) {
		List<Map<String, Object>> reslist = new ArrayList<Map<String, Object>>();
		
		try {
			// 创建一个对象命名为map
			Map<String, Object> map = new HashMap<String, Object>();
			
			// 通过遍历，把list中的对象放到map中
			for (CarteamRes cr : list) {
				if(!map.containsKey(cr.getId()+"")){
					Map<String, Object> atr = new HashMap<String, Object>();
					atr.put("id", cr.getId());							// 车队资源id
					atr.put("pid", cr.getPid()); 						// 车队资源父级id
					atr.put("num", cr.getResource() == null ? null : cr.getResource().getNum()); 			// 车队资源对应原始资源编号
					atr.put("url", cr.getResource() == null ? null : cr.getResource().getUrl()); 			// 车队资源对应原始资源url
					atr.put("remark", cr.getResNote() != null ? cr.getResNote().getRemark() : (cr.getResource() == null ? null : cr.getResource().getTitle())); 	// 车队资源标题备注
					atr.put("sortNo", cr.getSortNo()); 	// 车队资源排序编号
					
					// 核心步骤1：map中的'item.id'属性指向list数组中的对象元素
					map.put(cr.getId()+"", atr);
				}
			}
			
			// 遍历map，为响应对象添加children健
			for(Map.Entry<String, Object> m : map.entrySet()){
				Map<String, Object> mc = (Map<String, Object>)m.getValue();
				
				// 只处理父级id不是0的元素
				if(!"0".equals(mc.get("pid").toString())){
					// 获取父级
					Map<String, Object> pmc = (Map<String, Object>)map.get(mc.get("pid").toString());
					// 判断父级是否有children健
					if(pmc != null){
						if(pmc.containsKey("children")){// 存在子集
							// 取出子集map列表
							List<Map<String, Object>> clist = (List<Map<String, Object>>)pmc.get("children");
							
							clist.add(mc);										// 将当前遍历的车队资源加入列表
							
							// 按照排序序号升序排序
							Collections.sort(clist, new Comparator<Map<String, Object>>(){
					            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					                // 按照Map<String, Object>的sortNo字段进行升序排列
					                if(Integer.parseInt(o1.get("sortNo").toString()) > Integer.parseInt(o2.get("sortNo").toString())){
					                    return 1;
					                }
					                
					                if(Integer.parseInt(o1.get("sortNo").toString()) == Integer.parseInt(o2.get("sortNo").toString())){
					                    return 0;
					                }
					                
					                return -1;
					            }
					        });
							
							pmc.put("children", clist);							// 更新子集
						}else{
							// 不存在子集
							List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
							
							clist.add(mc);										// 将当前遍历的车队资源加入列表
							pmc.put("children", clist);							// 添加key为children的子集
						}
					}
				}
		    }
			
			// 过滤后，仅剩下根节点
			for (CarteamRes cr : list) {
				if(cr.getPid() == 0){
					reslist.add((Map<String, Object>)map.get(cr.getId()+""));
				}
			}
			
			// 按照排序序号升序排序
			Collections.sort(reslist, new Comparator<Map<String, Object>>(){
	            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	                // 按照Map<String, Object>的sortNo字段进行升序排列
	                if(Integer.parseInt(o1.get("sortNo").toString()) > Integer.parseInt(o2.get("sortNo").toString())){
	                    return 1;
	                }
	                
	                if(Integer.parseInt(o1.get("sortNo").toString()) == Integer.parseInt(o2.get("sortNo").toString())){
	                    return 0;
	                }
	                
	                return -1;
	            }
	        });
		} catch (Exception e) {
			U.log(log, "将车队资源线性列表，转换成树形map列表数据");
			e.printStackTrace();
		}
		
		return reslist;
	}*/

	/**
	 * 获取-乘客订单行程详情
	 * @param col 	乘客订单
	 * @param plms 	乘客订单联系人列表
	 * @return 行程详情
	 */
	/*public static String getRouteView(CarOrderList col, List<PointLinkman> plms) {
		String lotxt = U.log(log, "生成行程详情");
		String view = "";
		
		try {
			// 12.3早上09：30温江接9人送机场T1,刘玉明，联系电话：13981709333,温江哪里联系客人,12.12晚上23:15机场T1接9人送温江，刘玉明，联系电话：13981709333，CA472，温江哪里联系客人
			
			// 出发时间 eg：12月3号
			view += DateUtils.get_MDM_str(col.getUseDayStart());
			
			// 接/送
			String isShuttle = col.getIsShuttle() == 0 ? "接" : "送";
			if(col.getCustomers() > 0){
				isShuttle += col.getCustomers()+"人到";
			}else{
				isShuttle += "人到";
			}
			
			// 起止地点
			view += " "+col.getDepartPlace()+isShuttle+col.getDestination();
					
			// 联系人
			if(plms.size() > 0){
				view += "，联系人：";
				
				for (int i = 0; i < plms.size(); i++) {
					PointLinkman plm = plms.get(i);
					if(i == 0){
						view += plm.getLinkPhone()+"-"+plm.getLinkName();
					}else{
						view += "、"+plm.getLinkPhone()+"-"+plm.getLinkName();
					}
				}
			}
			
			// 航班号
			if(StringUtils.isNotEmpty(col.getFlightNum())){
				String tt = "";
				if(col.getType() == 1){
					tt = "航班号：";
				}else if(col.getType() == 2){
					tt = "车次号：";
				}else{
					tt = "车次号：";
				}
				view += "，"+tt+col.getFlightNum();
			}
			
			// 订单备注
			if(StringUtils.isNotBlank(col.getNote())){
				view += "，乘客备注："+col.getNote();
			}
		} catch (Exception e) {
			U.log(log, lotxt, e);
			e.printStackTrace();
		}
		
		return view;
	}*/
	
}
