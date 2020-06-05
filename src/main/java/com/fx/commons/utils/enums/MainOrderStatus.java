package com.fx.commons.utils.enums;

/**
 *  主行程订单状态
 */
public enum MainOrderStatus {
	/** 未确认用车 */
	NOT_CONFIRM,
	/** 未派车 */
	NOT_DIS_CAR,
	/** 已完成派车 */
	FINISHED_DIS_CAR,
	/** 已取消 */
	CANCELED;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case NOT_CONFIRM:
				return "未确认用车";
			case NOT_DIS_CAR:
				return "未派车";
			case FINISHED_DIS_CAR:
				return "已完成派车";
			case CANCELED:
				return "已取消";
			default:
				return "其他";
		}
	}
	
}
