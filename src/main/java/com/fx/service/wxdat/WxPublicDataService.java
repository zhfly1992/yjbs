package com.fx.service.wxdat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.wxdat.WxPublicData;

public interface WxPublicDataService extends BaseService<WxPublicData, Long> {
	
	
	/**
	 * 微信-授权
	 * @param request 		request
	 * @param response 		response
	 * @param state 		状态（通过状态判断不同的授权）
	 * @param lteamNo 		登录车队编号
	 * @param lrole 		登录用户角色
	 */
	public void wxAuthority(HttpServletRequest request, HttpServletResponse response, 
		String state, String lteamNo, String lrole);
	
	/**
	 * 获取-指定车队微信公众号数据
	 * @param teamNo 车队编号
	 * @return 数据对象
	 */
	public WxPublicData getWxPublicData(String teamNo);
	
}
