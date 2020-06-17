package com.fx.commons.utils.enums;

/** 记账类型 */
public enum JzType {
	/** 加油记账 */
	JYJZ(1, "加油记账"),
	/** 维修记账 */
	WXJZ(2, "维修记账"),
	/** 其他记账凭证 */
	QTJZ(3, "其他记账"),
	/** 行程收支 */
	XCSZ(4, "行程收支");
   
	
	private int value;
    private String key;
	
	JzType(int value, String key) {
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
