package com.fx.commons.exception;

/**
 * 验证码图片异常处理
 */
public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 122692112200026949L;
	
	/** 异常消息 */
    private String msg;
    
	/**  
	 * 获取 异常消息  
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}
	/**  
	 * 设置 异常消息  
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GlobalException(String message) {
        this.msg = message;
    }
}
