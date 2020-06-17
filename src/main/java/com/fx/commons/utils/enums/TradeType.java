package com.fx.commons.utils.enums;

/**
 * 交易-状态
 */
public enum TradeType {

	PAY(1,"支出"),
	
	GATH(0,"收入");
	
	private Integer code;

    private String msg;

    TradeType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case PAY:
				return "支出";
			case GATH:
				return "收入";
			default:
				return "其他";
		}
	}

	/**  
	 * 获取 code  
	 * @return code
	 */
	public Integer getCode() {
		return code;
	}
	

	/**  
	 * 设置 code  
	 * @param code 
	 */
	public void setCode(Integer code) {
		this.code = code;
	}
	

	/**  
	 * 获取 msg  
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}
	

	/**  
	 * 设置 msg  
	 * @param msg 
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
