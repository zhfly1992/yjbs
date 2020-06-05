package com.fx.commons.utils.enums;

/** 订单业务类型 */
public enum ServiceType {
	/** 县际业务 */
	COUNTY_SER(1, "县际业务"),
	/** 市际业务 */
	CITY_SER(2, "市际业务"),
	/** 省际业务 */
	PROVINCE_SER(3, "省际业务");
   
	
	private int value;
    private String key;
	
	ServiceType(int value, String key) {
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
