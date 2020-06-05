package com.fx.commons.hiberantedao.pagingcom;

/**
 * 排序工具类
 */
public class Compositor{
	/** 属性名称 */
	private String fieldName;
	private CompositorType compositorType;
	
	/**
	 * 排序类型
	 */
	public enum CompositorType{
		/** 顺序 */
		ASC,
		/** 倒序 */
		DESC;
	}
	
	/**
	 * 构造方法
	 * @param fieldName 字段名称
	 * @param compositorType 排序类型
	 */
	public Compositor(String fieldName, CompositorType compositorType){
		this.fieldName = fieldName;
		this.compositorType = compositorType;
	}
	
	/**
	 * 获取字段名称
	 */
	public String getFieldName(){
		return fieldName;
	}
	
	/**
	 * 获取排序类型
	 */
	public CompositorType getCompositorType(){
		return compositorType;
	}
}