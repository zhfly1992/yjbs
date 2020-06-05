package com.fx.commons.utils.clazz;

import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fx.commons.utils.tools.QC;

/**
 * json-结果
 * @author yigou
 *
 */
public class Message implements Serializable{
	public final static Logger log = LogManager.getLogger(Message.class);// 日志记录
	
	private static final long serialVersionUID = 5270534335530464884L;
	
	/** 结果状态码 */
	private int code;
	
	/** 结果状态码说明 */
	private String msg;
	
	/** 其他数据 */
	private Object obj;

	
	public Message() {}
	
	public Message(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public Message(int code, String msg, Object obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}


	/** 
	 * 将对象、列表等转化为json数据 
	 * @param object 对象、列表 
	 * @return json数据
	 */  
	public static void print(HttpServletResponse response, Object obj){  
	    ObjectMapper mapper = new ObjectMapper(); 
	    
	    try {  
	    	// 配置指定时间格式
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    mapper.setDateFormat(df);
		    
	    	String sw = mapper.writeValueAsString(obj);
	        
	        response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(sw);
			out.close();
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}
	
	/** 
	 * 将对象、列表等转化为json数据 
	 * @param object 对象、列表 
	 * @return json数据
	 */  
	@SuppressWarnings("unchecked")
	public static void print(HttpServletResponse response, Map<String, Object> map){
//		String logtxt = U.log(log, "序列化map中的对象为json格式");
		
	    ObjectMapper mapper = new ObjectMapper();
	    try {  
		    /****字段过滤*************************/
	    	Object ffs = map.get(QC.FIT_FIELDS);
	    	if(ffs != null){// 设置了过滤字段数组
	    		Map<String, Object> fitFields = (Map<String, Object>)ffs;
	    		map.remove(QC.FIT_FIELDS);
	    		
	    		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    		
	    		Iterator<Map.Entry<String, Object>> entries = fitFields.entrySet().iterator(); 
	    		while (entries.hasNext()) {
	    		    Map.Entry<String, Object> entry = entries.next(); 
	    		    if(entry.getValue() != null){
	    		    	filterProvider.addFilter(entry.getKey(), SimpleBeanPropertyFilter.serializeAllExcept((String[])entry.getValue()));
	    		    }else{
	    		    	filterProvider.addFilter(entry.getKey(), SimpleBeanPropertyFilter.serializeAllExcept(""));
	    		    }
	    		}
	    		
	    		mapper.setFilterProvider(filterProvider);
	    	}
	    	
	    	// 配置指定时间格式
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    mapper.setDateFormat(df);
	    	
	    	String sw = mapper.writeValueAsString(map);
	        
	        response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(sw);
			out.close();
	    }catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}
	
	/** 
	 * 将对象、列表等转化为json数据 
	 * @param object 对象、列表 
	 * @return json数据字符串
	 */  
	public static String toJson(Object obj){  
	    ObjectMapper mapper = new ObjectMapper(); 
	    
	    try {  
	    	// 配置指定时间格式
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    mapper.setDateFormat(df);
		    
	    	return mapper.writeValueAsString(obj);
	    	
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    
	    return "[解析json对象失败]";
	}
	
	
	/**
	 * 获取 结果状态码
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置 结果状态码
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 获取 结果状态码说明
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 设置 结果状态码说明
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 获取 其他数据
	 * @return obj
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * 设置 其他数据
	 * @param obj
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
