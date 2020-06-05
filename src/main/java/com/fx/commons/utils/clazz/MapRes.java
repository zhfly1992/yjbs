package com.fx.commons.utils.clazz;

import java.io.Serializable;

/**
 * map结果类
 */
public class MapRes implements Serializable {
	private static final long serialVersionUID = 2217349239067396985L;
	
	/** 结果状态码 */
	private int code;
	/** 结果状态说明 */
	private String msg;
	/** 数据 */
	private Object data;
	
	
	public MapRes() {}
	public MapRes(int code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public MapRes(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
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
	 * 获取 结果状态说明  
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}
	
	/**  
	 * 设置 结果状态说明  
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**  
	 * 获取 数据  
	 * @return data
	 */
	public Object getData() {
		return data;
	}
	
	/**  
	 * 设置 数据  
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
