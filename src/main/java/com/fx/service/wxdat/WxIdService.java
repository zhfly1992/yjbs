package com.fx.service.wxdat;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.Customer;
import com.fx.entity.wxdat.WxId;

public interface WxIdService extends BaseService<WxId,Long> {

	/****移动端--begin*************************/
	
	/**
	 * 添加-微信绑定信息
	 * @param cus 		添加用户
	 * @param teamNo 	车队编号
	 * @param openid 	用户openid
	 * @return 不为空-添加成功；为空-添加失败；
	 */
	public WxId addWxId(Customer lcus, String teamNo, String openid);
	
	/**
	 * 获取-用户的微信id列表
	 * @param uid 用户id
	 * @return 微信id集合
	 */
	public List<WxId> getWxIdByUid(String uid);
	
	/**
	 * 判断-用户绑定的微信id列表中，是否存在指定微信id/车队编号
	 * @param uid 用户id
	 * @param field 判断的微信id/车队编号
	 * @return 微信id对象：不为空-存在；为空-不存在；
	 */
	public WxId isExist(String uid, String field);
	
	/**
	 * 获取-登录用户微信绑定信息
	 * @param reqsrc 请求来源
	 * @param request
	 * @param response
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findWxId(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response);
	
	/**
	 * 更新-用户的地理位置坐标
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lname			用户账号
	 * @param teamNo		用户所属车队编号
	 * @param lnglat		坐标：103.123456|30.123456
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> updLuserLnglat(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String lname, String teamNo, String lnglat);
	
	/**
	 * 设置-当前微信为当前账号的主微信
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param teamNo 		车队编号
	 * @param phone 		手机号
	 * @param smsCode 		短信验证码
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> updSetMainWx(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String teamNo, String lphone, String smsCode);
	/****移动端--end*************************/

	
	
	
}
