package com.fx.commons.utils.enums;

/** 支付方式 */
public enum PayWay {
	/** 记账 */
	JZ_PAY(0, "记账"),
	/** 现金 */
	CASH_PAY(1, "现金"),
	/** 微信 */
	WX_PAY(1, "微信"),
	/** 支付宝 */
	ALI_PAY(1, "支付宝"),
	/** 余额 */
	BALANCE_PAY(1, "余额"),
	/** 月结 */
	MONTH_PAY(1, "月结");
   
	
	private int value;
    private String key;
	
	PayWay(int value, String key) {
        this.value = value;
        this.key = key;
    }
 
    public int getValue() {
        return value;
    }

	public String getKey() {
		return key;
	}
	
}
