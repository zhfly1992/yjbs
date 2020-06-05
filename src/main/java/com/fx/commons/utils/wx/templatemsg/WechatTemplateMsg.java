package com.fx.commons.utils.wx.templatemsg;

import java.util.TreeMap;

/**
 * 模板消息类
 * @author qfc
 * @2018-5-16
 */
public class WechatTemplateMsg {
	/**
	 * 接收者openid
	 */
	private String touser;
	/**
	 * 模板ID
	 */
    private String template_id;
    /**
     * 模板跳转链接
     */
    private String url;
    /**
     * data数据
     */
    private TreeMap<String, TreeMap<String, String>> data;
    
    
    /** 
     * data参数内项 
     * @param value 值
     * @param color 颜色（可不填） 
     */  
    public static TreeMap<String, String> item(String value, String color) {  
        TreeMap<String, String> params = new TreeMap<String, String>();  
        params.put("value", value);  
        params.put("color", color);  
        return params;  
    }


	/**  
	 * 获取 接收者openid  
	 * @return touser  
	 */
	public String getTouser() {
		return touser;
	}
	/**  
	 * 设置 接收者openid  
	 * @param touser
	 */
	public void setTouser(String touser) {
		this.touser = touser;
	}
	/**  
	 * 获取 模板ID  
	 * @return template_id  
	 */
	public String getTemplate_id() {
		return template_id;
	}
	/**  
	 * 设置 模板ID  
	 * @param template_id
	 */
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	/**  
	 * 获取 模板跳转链接  
	 * @return url  
	 */
	public String getUrl() {
		return url;
	}
	/**  
	 * 设置 模板跳转链接  
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**  
	 * 获取 data数据  
	 * @return data数据  
	 */
	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}
	/**  
	 * 设置 data数据  
	 * @param data数据
	 */
	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}
}
