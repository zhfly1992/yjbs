package com.fx.commons.utils.enums;

public enum VehicleType {
	/** 大巴车 */
    BUS(0),
    
    /** 中巴车*/
    MINIBUS(1),
    
    /** 商务车*/
    MPV(2),
    
    /** 越野车*/
    SUV(3),
    
    /** 轿车*/
    CAR(4)
    ;

    private int type;

    VehicleType(int type) {
        this.type = type;
    }
    
    public int getType(){
    	return type;
    }
}
