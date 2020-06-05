package com.fx.commons.utils.enums;

public enum CarNature {
	/**公路客运*/
    HIGHWAYTRANS,
    /**公路客运上网*/
    HIGHWAYTRANSNET,
    /**旅游客运*/
    TRAVELTRANS,
    /**旅游客运上网*/
    TRAVELTRANSNET,
    /**租赁*/
    LEASE;
    
    


    ;

	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case HIGHWAYTRANS:
				return "公路客运";
			case HIGHWAYTRANSNET:
				return "公路客运上网";
			case TRAVELTRANS:
				return "旅游客运";
			case TRAVELTRANSNET:
				return "旅游客运上网";
			case LEASE:
				return "租赁";
			default:
				return "";
		}
	}
}
