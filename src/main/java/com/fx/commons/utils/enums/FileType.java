package com.fx.commons.utils.enums;

/** 上传文件类型 */
public enum FileType {
	/** 身份证正面 */
	ID_CARD_FRONT(0, "身份证正面"),
	/** 身份证背面 */
	ID_CARD_BACK(1, "身份证背面"),
	/** 加油记账凭证 */
	JYJZ_IMG(2, "加油记账凭证"),
	/** 维修记账凭证 */
	WXJZ_IMG(3, "维修记账凭证"),
	/** 其他记账凭证 */
	QTJZ_IMG(4, "其他记账凭证"),
	/** 行程收支凭证 */
	XCSZ_IMG(5, "行程收支凭证");
   
	
	private int value;
    private String key;
	
	FileType(int value, String key) {
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
