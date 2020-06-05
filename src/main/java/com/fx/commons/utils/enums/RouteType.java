package com.fx.commons.utils.enums;

/** 订单行程类型 */
public enum RouteType {
	/** 单程接送 */
	ONE_WAY(1, "单程接送"),
	/** 往返包车 */
	TRAVEL_BC(2, "往返包车");
   
	
	private int value;
    private String key;
	
	RouteType(int value, String key) {
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
