package com.fx.commons.utils.wx.templatemsg;

/**
 * 微信API返回状态
 * @author qfc
 * @2018-5-16
 */
public class TemplateMsgResult {
	/**
	 * 状态
	 */
	private int errcode;
	/**
	 * 信息
	 */
	private String errmsg;
	/**
	 * 信息id
	 */
	private String msgid;
	
	/**  
	 * 获取 状态  
	 * @return errcode  
	 */
	public int getErrcode() {
		return errcode;
	}
	/**  
	 * 设置 状态  
	 * @param errcode
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	/**  
	 * 获取 信息  
	 * @return errmsg  
	 */
	public String getErrmsg() {
		return errmsg;
	}
	/**  
	 * 设置 信息  
	 * @param errmsg
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	/**  
	 * 获取 信息id  
	 * @return msgid  
	 */
	public String getMsgid() {
		return msgid;
	}
	/**  
	 * 设置 信息id  
	 * @param msgid
	 */
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
}
