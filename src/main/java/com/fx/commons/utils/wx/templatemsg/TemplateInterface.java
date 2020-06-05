package com.fx.commons.utils.wx.templatemsg;

/**
 * 微信发送模板消息接口
 * @author qfc
 * @2018-5-16
 */
public interface TemplateInterface {
	/**
	 * 发送微信模板消息结果
	 * @author qfc
	 * 2018-5-16
	 * @param accessToken 微信令牌
	 * @param params 请求参数
	 */
	public TemplateMsgResult sendTemplate(String accessToken, String params);
}
