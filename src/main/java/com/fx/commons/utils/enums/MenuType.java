package com.fx.commons.utils.enums;

/** 菜单类型 */
public enum MenuType {
	/** 侧边栏菜单 */
	SIDEBAR_MENU(1, "侧边栏菜单"),
	/** 筛选条件 */
	FILTER(2, "筛选条件"),
	/** 按钮 */
	BUTTON(3, "按钮");
   
	
	private int value;
    private String key;
	
	MenuType(int value, String key) {
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
