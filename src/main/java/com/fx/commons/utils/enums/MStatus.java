package com.fx.commons.utils.enums;

/**
 * 消息状态
 */
public enum MStatus {

	/** 操作成功 */
    SUCCESS(200, "操作成功"),
    
    /** 操作成功 */
    ACCOUNT_UNKNOWN(500, "账户不存在"),
    
    /** 用户名或密码错误 */
    ACCOUNT_ERROR(500, "用户名或密码错误"),
    
    /** 验证码错误 */
    CODE_ERROR(400, "验证码错误"),
    
    /** 系统错误 */
    SYSTEM_ERROR(500, "系统错误"),
    
    /** 参数错误 */
    PARAM_ERROR(400, "参数错误"),
    
    /** 参数已存在 */
    PARAM_REPEAT(400, "参数已存在"),
    
    /** 没有操作权限 */
    PERMISSION_ERROR(403, "没有操作权限"),
    
    /** 其他错误 */
    OTHER(-100, "其他错误");

    private int code;
    private String info;
    

    /**  
	 * 获取 code  
	 * @return code
	 */
	public int getCode() {
		return code;
	}
	/**  
	 * 设置 code  
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**  
	 * 获取 info  
	 * @return info
	 */
	public String getInfo() {
		return info;
	}
	/**  
	 * 设置 info  
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	MStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }
}
