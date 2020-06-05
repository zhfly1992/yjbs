package com.fx.commons.utils.enums;

/**
 * 行程订单来源
 */
public enum OrderSource {
	/** 电脑端-单位 */
	PC_COMPANY,
	/** 电脑端-个人 */
	PC_PERSONAL,
	/** 移动端-单位 */
	MOBILE_COMPANY,
	/** 移动端-个人 */
	MOBILE_PERSONAL;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case PC_COMPANY:
				return "电脑端-单位";
			case PC_PERSONAL:
				return "电脑端-个人";
			case MOBILE_COMPANY:
				return "移动端-单位";
			case MOBILE_PERSONAL:
				return "移动端-个人";
			default:
				return "其他";
		}
	}
	
}
