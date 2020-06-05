package com.fx.commons.utils.enums;

public enum VehiclePowerSourceType {
	/**柴油*/
    DIESEL,
    
    /**汽油*/
    GASOLINE,
    /**CNG*/
    CNG,
    /**新能源*/
    NEWENERGY;
    
    

    ;

	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case DIESEL:
				return "柴油";
			case GASOLINE:
				return "汽油";
			case CNG:
				return "CNG";
			case NEWENERGY:
				return "新能源";
			default:
				return "";
		}
	}
}
