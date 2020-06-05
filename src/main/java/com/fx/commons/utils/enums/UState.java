package com.fx.commons.utils.enums;

/**
 * 用户-状态
 */
public enum UState {

	/** 正常 */
	NORMAL,
	/** 已删除 */
	DELETED,
	/** 已锁定 */
	LOCKED;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case NORMAL:
				return "正常";
			case DELETED:
				return "已删除";
			case LOCKED:
				return "已锁定";
			default:
				return "其他";
		}
	}
	
}
