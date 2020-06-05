package com.fx.commons.utils.enums;


/**
 * 数据-请求来源
 */
public enum ReqSrc {
	/** 移动端-微信 */
	WX,
	/** 电脑端-单位 */
	PC_COMPANY,
	/** 电脑端-个人 */
	PC_PERSONAL,
	/** 电脑端-后台 */
	PC_BACK,
	/** 公用 */
	COMMON;
	
	/** 文本描述 */
	public String getText(){
		switch(this){
			case WX:
				return "移动端-微信";
			case PC_COMPANY:
				return "电脑端-单位";
			case PC_PERSONAL:
				return "电脑端-个人";
			case PC_BACK:
				return "电脑端-后台";
				
			default:
				return "公用";
		}
	}
	
}
