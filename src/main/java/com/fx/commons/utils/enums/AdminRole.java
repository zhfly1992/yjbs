package com.fx.commons.utils.enums;

/**
 * 管理员用户-状态
 */
public enum AdminRole {

	/** 管理员 */
	ADMIN,
	/** 超级管理员 */
	SUP_ADMIN;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case ADMIN:
				return "管理员";
			case SUP_ADMIN:
				return "超级管理员";
			default:
				return "其他";
		}
	}
	
}
