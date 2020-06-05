package com.fx.commons.utils.enums;

/**
 * 用户角色
 */
public enum CusRole {
	/** 普通-会员 */
	PT_CUS(1, "普通-会员"),
	/** 车队-计调 */
	TEAM_JD(2, "车队-计调"),
	/** 车队-业务员 */
	TEAM_YW(3, "车队-业务员"),
	/** 车队-驾驶员 */
	TEAM_DRIVER(4, "车队-驾驶员"),
	/** 后台-管理员 */
	BACK_ADMIN(5, "后台-管理员"),
	/** 单位 */
	COMPANY(6, "单位");
	
	private int value;
    private String key;
	
    CusRole(int value, String key) {
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
