package com.fx.commons.utils.enums;

/**
 * 行程订单状态
 */
public enum OrderStatus {
	/** 未派车 */
	NOT_DIS_CAR,
	/** 经理未确认派车 */
	JL_NOT_CONFIRM,
	/** 师傅未确认 */
	DRIVER_NOT_CONFIRM,
	/** 师傅已确认 */
	DRIVER_CONFIRMED,
	/** 已出行 */
	AL_TRAVEL,
	/** 已完成 */
	COMPLETED,
	/** 已取消 */
	CANCELED,
	/** 已拒绝 */
	REFUSED;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case NOT_DIS_CAR:
				return "未派车";
			case JL_NOT_CONFIRM:
				return "经理未确认";
			case DRIVER_NOT_CONFIRM:
				return "师傅未确认";
			case DRIVER_CONFIRMED:
				return "师傅已确认";
			case AL_TRAVEL:
				return "已出行";
			case COMPLETED:
				return "已完成";
			case CANCELED:
				return "已取消";
			case REFUSED:
				return "已拒绝";
			default:
				return "其他";
		}
	}
	
}
