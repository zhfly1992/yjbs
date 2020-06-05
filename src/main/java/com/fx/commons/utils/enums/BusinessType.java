package com.fx.commons.utils.enums;

public enum BusinessType {

	/**自营**/
    MYSELF(0),
    /**挂靠**/ 
    NOTSELF(1),


    ;

	private int type;

	BusinessType(int type) {
        this.type = type;
    }
    
    public int getCode(){
    	return type;
    }
    public static void main(String[] args) {
		System.out.println(BusinessType.valueOf("MYSELF"));
	}
}
