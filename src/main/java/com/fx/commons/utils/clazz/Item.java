package com.fx.commons.utils.clazz;

import java.io.Serializable;

/**
 * 选项：值-文本工具类
 */
public class Item implements Serializable {
	private static final long serialVersionUID = -8771575220970467708L;
	/** key */
	private String id;
	/** 值 */
	private Object val;
	/** 对应值文本 */
	private String text;
	/** 其他字段 */
	private Object other;
	
	
	public Item() {}
	public Item(String id, String text) {
		this.id = id;
		this.text = text;
	}
	public Item(String id, String text, Object other) {
		this.id = id;
		this.text = text;
		this.other = other;
	}
	public Item(String id, Object val, String text, Object other) {
		this.id = id;
		this.val = val;
		this.text = text;
		this.other = other;
	}
	/**  
	 * 获取值  
	 * @return id 值  
	 */
	public String getId() {
		return id;
	}
	/**  
	 * 设置值  
	 * @param id 值  
	 */
	public void setId(String id) {
		this.id = id;
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
	/**  
	 * 获取对应值文本  
	 * @return text 对应值文本  
	 */
	public String getText() {
		return text;
	}
	/**  
	 * 设置对应值文本  
	 * @param text 对应值文本  
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**  
	 * 获取其他字段  
	 * @return other 其他字段  
	 */
	public Object getOther() {
		return other;
	}
	/**  
	 * 设置其他字段  
	 * @param other 其他字段  
	 */
	public void setOther(Object other) {
		this.other = other;
	}
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"text\":\"" + text + "\", \"other\":\"" + other + "\"}";
	}
	
}
