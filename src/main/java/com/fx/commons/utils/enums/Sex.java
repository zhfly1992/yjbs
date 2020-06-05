package com.fx.commons.utils.enums;

/**
 * 用户性别
 */
public enum Sex {
	/** 男 */
	MALE,
	/** 女 */
	FEMALE,
	/** 其他 */
	OTHER;
	
	/**
	 * 获取对应文本说明
	 */
	public String getText(){
		switch(this){
			case MALE:
				return "男";
			case FEMALE:
				return "女";
			default:
				return "未知";
		}
	}
	
}
