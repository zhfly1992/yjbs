package com.fx.commons.utils.tools;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.CookieUtil;
import com.fx.commons.utils.other.DateUtils;

/**
 * 工具处理类
 */
public class U {
	/** 日志记录 */
	public static Logger log = LogManager.getLogger(U.class.getName());
	
	/**
	 * 打印-普通日志
	 * @param log 日志对象
	 */
	public static String log(Logger log, String con) {
		String txt = "<"+con+">"; 
		log.info(txt);
		return txt;
	}
	/**
	 * 打印-表示异常日志
	 * @param log 日志对象
	 */
	public static String logEx(Logger log, String con) {
		String txt = "异常：【"+con+"】"; 
		log.info(txt);
		return txt;
	}
	/**
	 * 打印-普通日志
	 * @param log 日志对象
	 */
	public static boolean logFalse(Logger log, String con) {
		String txt = "<"+con+">"; 
		log.info(txt);
		return false;
	}
	
	/**
	 * 打印-日志，并返回日志文本
	 * @param log 日志对象
	 * @param con 日志内容
	 * @param reqsrc 请求来源
	 * @param role 用户角色
	 * @return 日志文本
	 */
//	public static String log(Logger log, String con, ReqSrc reqsrc, CusRole role) {
//		String txt = "<begin--"+"["+reqsrc.getReqSrcText()+"-"+role.getCusRoleText()+"]-"+con+">";
//		log.info(txt);
//		return txt;
//	}
	
	/**
	 * 打印-日志，并返回日志文本
	 * @param log 日志对象
	 * @param con 日志内容
	 * @param reqsrc 请求来源
	 * @return 日志文本
	 */
	public static String log(Logger log, String con, ReqSrc reqsrc) {
		String txt = "["+reqsrc.getText()+"]-"+con;
		log.info(txt);
		return txt;
	}
	/**
	 * 打印-异常日志
	 * @param log 日志对象
	 * @param e 异常对象
	 */
	public static void log(Logger log, String con, Exception e) {
		String tip = "";
		if(StringUtils.isNotBlank(con) && !con.contains("异常")) {
			tip = "异常：";
		}
		log.info(tip + con, e);
	}
	
	/**
	 * 生成一个UUID
	 */
	public static String getUUID(){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		return UUID.randomUUID().toString();
	}
	/**
	 * 从request中获取UUID
	 */
	public static String getUUID(HttpServletRequest request){
		return request.getParameter(QC.UUID);
	}
	
	/**
	 * 从request中获取UUID，为空则从cookie中获取
	 */
	public static String getUuidOfCookie(HttpServletRequest request){
		String uuid = request.getParameter(QC.UUID);
		if(StringUtils.isEmpty(uuid)){
			 uuid = CookieUtil.getUUID(request, null); 
		 }
		
		return uuid;
	}
	
//	/**
//	 * 将对象保存到缓存中，指定时间长度
//	 * @param obj 保存的对象
//	 * @param key 保存的key
//	 * @param min 保存的时间（分钟）
//	 */
//	public static void setMemcach(Object obj, String key, int min){
//		Item i = new Item();
//		i.setVal(obj);
//		i.setOther(new Date());
//		if(StringUtils.isBlank(key)){
//			// 未传入保存key，系统生成指定格式key
//			key = "k-"+new Date().getTime();
//		}
//		i.setId(key);
//		
//		BeanComponent.put(key, i, min);
//		log.info("====>为【"+key+"（"+min+"分钟）】保存缓存成功");
//	}
	
//	/**
//	 * 获取缓存中指定key的值
//	 * @param key 健
//	 * @return 值
//	 */
//	public static String getMemch(String key){
//		Object obj = BeanComponent.get(key);
//		if(obj != null){
//			return obj.toString();
//		}else{
//			return null;
//		}
//	}
	
	/**
	 * 获取类上面：注解JsonFilter的值
	 * @param clazz 类
	 * @return 字符串值
	 */
	public static String getAtJsonFilter(Class<?> clazz){
		JsonFilter jf = clazz.getAnnotation(JsonFilter.class);
		if(jf != null){
			return jf.value();
		}else{
			return "";
		}
	}
	
	/**
	 * 对象转换成json字符串
	 * @param obj 转换对象
	 */
	public static String toJsonStr(Object obj){
		String sw = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			String json = mapper.writeValueAsString(obj);
	        return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return sw;
	}
	
	/**
	 * 获取-json对象中的参数值
	 * @param json json对象
	 * @param name 参数名
	 */
	public static String P(JSONObject json, String name) {
		String str = "";
		
		try {
			str = json.getString(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	/**
	 * 将JsonNode转字符串并去掉字符中的"引号
	 * @param str 字符串
	 */
	public static String Cq(JsonNode node, String parName){
		if(node != null && node.get(parName) != null) {
			return node.get(parName).toString().replaceAll("\"", "");
		}else {
			return "";
		}
	}
	
	/**
	 * josn字符串转换成对象
	 * @param jsonStr 	json字符串
	 * @param clazz 	转换的类
	 * @return 对象
	 */
	public static Object toJsonBean(String jsonStr, Class<?> clazz){
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			Class<?> bean  = mapper.readValue(jsonStr, clazz.getClass());
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return null;
	}
	
	/***********分页--begin****************/
	/**
	 * 设置导出分页列表设置(多排序)
	 * @param pd 当前页分页数据设置
	 * @param pdEx 导出数据设置
	 * @return 新的导出数据设置pdEx
	 */
	public static void setPdEx(Page<?> pd, Page<?> pdEx){
		pdEx.setCompositors(pd.getCompositors());// 设置排序
		pdEx.setFiltrations(pd.getFiltrations());// 设置过滤条件
		pdEx.getPagination().setPageSize((int)pd.getPagination().getTotalCount());// 设置总数量
		pdEx.getPagination().setPageNo(1);// 设置页码
	}
	/**
	 * 设置分页数据count, data(一般列表数据所需参数)
	 * @param map map对象
	 * @param pageData 分页设置数据
	 */
	public static void setPageData(Map<String, Object> map, Page<?> pageData){
		map.put("count", (int)pageData.getPagination().getTotalCount());
		map.put("data", pageData.getResult());
	}
	/**
	 * 自定义列表-设置分页数据count, data(一般列表数据所需参数)
	 * @param map map对象
	 * @param pageData 分页设置数据
	 */
	public static void setPageData(Map<String, Object> map, List<Object> list){
		map.put("count", list.size());
		map.put("data", list);
	}
	
	/**
	 * 自定义列表-设置分页数据count, data(一般列表数据所需参数)
	 * @param map map对象
	 * @param maps 分页设置数据
	 */
	public static void setPageDataMaps(Map<String, Object> map, List<Map<String, Object>> maps){
		map.put("count", maps.size());
		map.put("data", maps);
	}
	/**
	 * 设置分页数据total, rows(easyui前端UI框架列表数据所需参数)
	 * @param map map对象
	 * @param pageDate 分页设置数据
	 */
	public static void setPageDataEasyUi(Map<String, Object> map, Page<?> pageData){
		if(pageData != null && pageData.getPagination().getTotalCount() > 0){
			map.put("total", (int)pageData.getPagination().getTotalCount());
			map.put("rows", pageData.getResult());
		}else{
			map.put("total", 0);
			map.put("rows", new ArrayList<String>());
		}
	}
	/**
	 * 设置列表数据total, rows(easyui前端UI框架列表数据所需参数)
	 * @param map map对象
	 * @param total 数据总条数
	 * @param rows 数据列表
	 */
	public static void setDataEasyUi(Map<String, Object> map, int total, List<?> rows){
		map.put("total", total);
		map.put("rows", rows);
	}
	/**
	 * 验证-分页-页码&页大小（适用列表参数验证）
	 * @param map 结果map
	 * @param page 页码
	 * @param rows 页大小
	 * @param tip 参数描述
	 * @return true-通过；false-不通过；
	 */
	public static boolean valPageNo(Map<String, Object> map, 
		String page, String rows, String tip){
		boolean fg = true;
		
		if(StringUtils.isEmpty(tip)) tip = "";
		
		if(fg){
			if(StringUtils.isEmpty(page)){
				fg = U.setPutFalse(map, "["+tip+"]数据分页页码不能为空");
			}else{
				page = page.trim();
				if(!FV.isPosInteger(page)){
					fg = U.setPutFalse(map, "["+tip+"]数据分页页码应为正整数");
				}
				
				U.log(log, "["+tip+"]数据分页页码：page="+page);
			}
		}
		
		if(fg){
			if(StringUtils.isEmpty(rows)){
				fg = U.setPutFalse(map, "["+tip+"]数据分页页大小不能为空");
			}else{
				rows = rows.trim();
				if(!FV.isPosInteger(rows)){
					fg = U.setPutFalse(map, "["+tip+"]数据分页页大小应为正整数");
				}
				
				U.log(log, "["+tip+"]数据分页页大小：rows="+rows);
			}
		}
		
		return fg;
	}
	/**
	 * 验证-开始&结束时间（适用列表参数验证）
	 * @param map 结果map
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param tip 参数描述
	 * @return true-通过；false-不通过；
	 */
	public static boolean valSEtime(Map<String, Object> map, 
		String stime, String etime, String tip){
		boolean fg = true;
		
		if(StringUtils.isEmpty(tip)) tip = "";
		
		if(fg){
			if(StringUtils.isEmpty(stime)){
				U.log(log, "["+tip+"]开始时间：stime为空");
			}else{
				stime = stime.trim();
				if(!FV.isDate(stime)){
					fg = U.setPutFalse(map, "["+tip+"]开始时间格式不正确");
				}
				
				U.log(log, "["+tip+"]开始时间：stime="+stime);
			}
		}
		
		if(fg){
			if(StringUtils.isEmpty(etime)){
				U.log(log, "["+tip+"]结束时间：etime为空");
			}else{
				etime = etime.trim();
				if(!FV.isDate(etime)){
					fg = U.setPutFalse(map, "["+tip+"]结束时间格式不正确");
				}
				
				U.log(log, "["+tip+"]结束时间：etime="+etime);
			}
		}
		
		if(fg){
			if(StringUtils.isNotBlank(stime) && StringUtils.isBlank(etime)){
				// 开始时间不为空，结束时间为空
				fg = U.setPutFalse(map, "["+tip+"]结束时间不能为空");
			}else if(StringUtils.isBlank(stime) && StringUtils.isNotBlank(etime)){
				// 开始时间为空，结束时间不为空
				fg = U.setPutFalse(map, "["+tip+"]开始时间不能为空");
			}else if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
				// 开始时间必须小于或登录结束时间
				Date st = DateUtils.strToDate(stime);
				Date et = DateUtils.strToDate(etime);
				
				if(st.getTime() > et.getTime()){
					fg = U.setPutFalse(map, "["+tip+"]开始时间不能大于["+tip+"]结束时间");
				}
			}
		}
		
		return fg;
	}
	/***********分页--end****************/
	
	/**********结果处理--begin***********/
	/**
	 * 设置返回状态码及对应信息内容（异常时）
	 * @param map map对象
	 * @param log 日志对象
	 * @param e 异常对象
	 * @param msg 信息内容
	 */
	public static void setPutEx(Map<String, Object> map, Logger log, Exception e, String msg){
		U.setPut(map, -1, QC.ERRORS_MSG);
		U.log(log, "异常："+msg, e);
	}
	/**
	 * 设置返回状态码及对应信息内容（code=0, fg=false）
	 * @param map map对象
	 * @param fg 结果
	 * @param msg 信息内容
	 */
	public static boolean setPutFalse(Map<String, Object> map, String msg){
		map.put("code", 0);
		map.put("msg", msg);
		U.log(log, U.getMC(map));
		return false;
	}
	/**
	 * 设置返回状态码及对应信息内容
	 * @param map map对象
	 * @param code 状态码
	 * @param msg 信息内容
	 */
	public static void setPut(Map<String, Object> map, int code, String msg){
		map.put("code", code);
		map.put("msg", msg);
		U.log(log, U.getMC(map));
	}
	/**
	 * 设置返回状态码及对应信息内容（fg=false）
	 * 一般用在最后成功结果，不想执行之后操作
	 * @param map map对象
	 * @param code 状态码
	 * @param msg 信息内容
	 */
	public static boolean setPutFalse(Map<String, Object> map, int code, String msg){
		map.put("code", code);
		map.put("msg", msg);
		U.log(log, U.getMC(map));
		return false;
	}
	/**
	 * 设置返回结果true/false
	 * @param map map对象
	 */
	public static boolean setPutFg(Map<String, Object> map){
		if(map != null){
			Object obj = map.get("code");
			if(obj != null){
				int code = Integer.parseInt(obj.toString());
				if(code == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	/**
	 * 设置返回状态码及对应信息内容，并返回json结果
	 * @author qfc
	 * 2017-12-6
	 * @param map map对象
	 * @param code 状态码
	 * @param msg 信息内容
	 */
	public static void setPut(Map<String, Object> map, int code, String msg, 
		HttpServletResponse response){
		map.put("code", code);
		map.put("msg", msg);
		Message.print(response, map);
	}
	/**
	 * 获取返回结果内容
	 * @author qfc
	 * 2018-8-29
	 * @param map 返回的map结果
	 * eg：[状态：1 ==> 消息：成功]
	 */
	public static String getMC(Map<String, Object> map){
		return "[状态："+map.get("code")+" ==> 消息："+map.get("msg")+"]";
	}
	/**********结果处理--end***********/
	
	/**
	 * 获取指定类中，指定字段的数组
	 * @param clazz 指定类
	 * @param fields 需要[包含/显示]的字段数组
	 * @return 排除的字段字符串数组
	 */
	public static String[] includeFields(Class<?> clazz, String[] fields){
		List<String> fds = new ArrayList<String>();
		log(log, "此方法为[包含]，除去传入数组中的字段，类中的其他字段全部过滤");
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				boolean fg = false;
				
				// 便利传入的数组字段
				for (int j = 0; j < fields.length; j++) {
					if(fields[j].equals(fs[i].getName())){
						fg = true;
						break;
					}
				}
				
				if(!fg){
					fds.add(fs[i].getName());
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return fds.toArray(new String[fds.size()]);
	}
	
	/**
	 * 获取map中指定key的值，并转字符串
	 * @param map map对象
	 * @param key key名称
	 * @return 不为空-字符串值；为空-空字符串；
	 */
	public static String getInMap(Map<String, Object> map, String key){
		String res = "";
		
		if(map != null){
			Object it = map.get(key);
			if(it != null){
				res = it.toString();
			}
		}
		
		return res;
	}
	
	/**
	 * 获取指定列表中的指定项
	 * @author qfc
	 * 2017-9-12
	 * @param items 指定列表项
	 * @param str 指定标识
	 */
	public static Item getItem(List<Item> items, String str){
		Item it = new Item();
		
		for(int i = 0; i < items.size(); i++){
			if(StringUtils.equals(str, items.get(i).getId())){
				it = items.get(i);
				break;
			}
		}
		
		return it;
	}
	
	/**
	 * 判断数组中是否有重复的值
	 * @param arr 传入的数组
	 * @return [true, hashset]-不重复，[false, ""]-重复
	 */
    public static Object[] cheakIsRepeat(Object[] arr) {
    	Object[] res = new Object[]{};
    	
        HashSet<Object> hashSet = new HashSet<Object>();
        for (int i = 0; i < arr.length; i++) {
            hashSet.add(arr[i]);
        }
        if (hashSet.size() == arr.length) {
            res[0] = true;
            res[1] = hashSet;
        } else {
        	 res[0] = false;
             res[1] = "";
        }
        return res;
    }
    
    /**
	 * 判断预约时间是否在指定时间之内
	 * @param overTime 加班起止时间段，如：12:30=23:30
	 * @param reserveOrderTime 订单预约时间：如：2017-10-30 12:30:20
	 * @return true-包括，false-不包括
	 */
	public static boolean isIncludeDate(String overTime, String reserveOrderTime){
		boolean isInclude = false;//默认不包括
		
		try {
			if(reserveOrderTime.split(" ")[1].split(":").length != 3){// 表示预约时间规则不是时分秒
				reserveOrderTime += ":00";// 则给预约时间字符串拼接一个秒
			}
			
			long rot = DateUtils.strToDate(reserveOrderTime).getTime();// 预约时间毫秒数
			
			String ydd = reserveOrderTime.split(" ")[0];// 预约时间年月日
			
			long ot_s = DateUtils.strToDate(ydd+" "+overTime.split("=")[0]+":00").getTime();// 加班开始时间毫秒数
			long ot_e = DateUtils.strToDate(ydd+" "+overTime.split("=")[1]+":00").getTime();// 加班结束时间毫秒数
			
			if(ot_s > ot_e){// 如果开始时间大于结束时间，说明结束时间加一天，表示是第二天的时间
				ot_e += 1000*60*60*24;  
				
				if(rot >= ot_s && rot <= ot_e){// 如果预约时间在加班时间之内
					isInclude = true;
				}else{
					rot += 1000*60*60*24;
					
					if(rot <= ot_e){
						isInclude = true;
					}
				}
			}else{
				if(rot >= ot_s && rot <= ot_e){// 如果预约时间在加班时间之内
					isInclude = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isInclude;
	}
	/**
	 * 取出一个指定长度大小的随机正整数.
	 * @param length
	 * int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	/**
	 * 判断文件夹是否存在，不存在创建
	 * @param path 文件夹路径 D:/a/b/c/d
	 * @return 返回false（true）表示文件夹不存在（存在）；如果不存在，文件夹会被创建
	 */
	public static boolean creatFolder(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 获取指定时间年月文件夹
	 * @return eg:/2018/10
	 */
	public static String getYearMonthFolder(Date date){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);// 设置指定时间
		int _year = cl.get(Calendar.YEAR);
		int _month = cl.get(Calendar.MONTH)+1;
		
		return "/"+_year+"/"+_month;
	}
	
	/**
     * 得到类的路径，例如E:/workspace/JavaGUI/bin/com/util
     * @return 所在盘符，eg：E
     */
    public String getClassPath() {
        try {
            String strClassName = getClass().getName();
            String strPackageName = "";
            if (getClass().getPackage() != null) {
                strPackageName = getClass().getPackage().getName();
            }
            String strClassFileName = "";
            if (!"".equals(strPackageName)) {
                strClassFileName = strClassName.substring(strPackageName.length() + 1, strClassName.length());
            } else {
                strClassFileName = strClassName;
            }
            URL url = null;
            url = getClass().getResource(strClassFileName + ".class");
            String strURL = url.toString();
            strURL = strURL.substring(strURL.indexOf('/') + 1, strURL.lastIndexOf('/'));
            //返回当前类的路径，并且处理路径中的空格，因为在路径中出现的空格如果不处理的话，
            //在访问时就会从空格处断开，那么也就取不到完整的信息了，这个问题在web开发中尤其要注意
            return strURL.replaceAll("%20", " ").split(":")[0];
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
	
	
	/**
	 * 根据座位数获取是几类车
	 * @param i 座位数
	 * @return 几类车
	 */
	public static int getTypeBySeats(int i){
		if(i <= 7){
			return 1;
		}else if(i >= 8 && i <= 19){
			return 2;
		}else if(i >= 20 && i <= 39){
			return 3;
		}else if(i >= 40){
			return 4;
		}
		return 0;
	}
	
	/**
	 * 获取-map结果数据
	 * @param map map对象
	 * @return 结果对象
	 */
	public static MapRes mapRes(Map<String, Object> map){
		MapRes mr = null;
		
		try {
			if(map != null) {
				mr = new MapRes();
				mr.setCode(Integer.parseInt(map.get("code").toString()));
				mr.setMsg(map.get("msg").toString());
				mr.setData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mr;
	}
	
	public static void main(String[] args) {
		U u = new U();
		String pf = u.getClassPath();
		System.out.println(pf);
		
	}
	
}
