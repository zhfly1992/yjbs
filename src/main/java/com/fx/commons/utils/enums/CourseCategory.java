package com.fx.commons.utils.enums;

/** 科目类别 */
public enum CourseCategory {
	/** 资产 */
	PROPERTY(0, "资产类"),
	/** 负债 */
	DEBT(1, "负债类"),
	/** 权益 */
	LEGAL(2, "权益类"),
	/** 权益 */
	COST(3, "成本类"),
	/** 损益 */
	LOSSES(4, "损益类");
   
	
	private int value;
    private String key;
	
	CourseCategory(int value, String key) {
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
