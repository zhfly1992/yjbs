package com.fx.commons.utils.enums;

/**
 * 注册方式
 */
public enum RegWay {

	/** 微信 */
	WX,
	/** 微信小程序 */
	WX_APP,
	/** 单位单位 */
	PC_COMPANY,
	/** 车队 */
	PC_TEAM,
	/** 后台 */
	PC_BACK;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case WX:
				return "微信";
			case WX_APP:
				return "微信小程序";
			case PC_COMPANY:
				return "单位";
			case PC_TEAM:
				return "车队";
			case PC_BACK:
				return "后台";
			default:
				return "其他";
		}
	}
	
}
