package com.fx.commons.utils.clazz;

import java.io.Serializable;

/**
 * 键值-工具类
 */
public class KeyVal implements Serializable {
	private static final long serialVersionUID = -3255210567707491900L;
	
	/** key */
	private String key;
	/** 值 */
	private Object val;
	
	public KeyVal() {}
	public KeyVal(String key, String val) {
		this.key = key;
		this.val = val;
	}
	
	/**
	 * 获取 键
	 * @return key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 设置 键
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * 获取 值
	 * @return val
	 */
	public Object getVal() {
		return val;
	}
	/**
	 * 设置 值
	 * @param val
	 */
	public void setVal(Object val) {
		this.val = val;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
