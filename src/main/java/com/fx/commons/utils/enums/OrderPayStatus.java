package com.fx.commons.utils.enums;

/**
 * 行程订单支付状态
 */
public enum OrderPayStatus {
	/** 未付款 */
	UNPAID,
	/** 已付定金 */
	DEPOSIT_PAID,
	/** 全款已付 */
	FULL_PAID;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case UNPAID:
				return "未付款";
			case DEPOSIT_PAID:
				return "已付定金";
			case FULL_PAID:
				return "全款已付";
			default:
				return "其他";
		}
	}
	
}
