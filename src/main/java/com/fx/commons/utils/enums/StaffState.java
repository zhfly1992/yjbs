package com.fx.commons.utils.enums;

/**
 * 员工入职-状态
 */
public enum StaffState {

	/** 正式 */
	NORMAL,
	/** 试用 */
	TRY,
	/** 离职 */
	LEAVE;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case NORMAL:
				return "正式";
			case TRY:
				return "试用";
			case LEAVE:
				return "离职";
			default:
				return "其他";
		}
	}
	
}
