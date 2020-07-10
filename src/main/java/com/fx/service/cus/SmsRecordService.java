package com.fx.service.cus;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.SmsRecord;

public interface SmsRecordService extends BaseService<SmsRecord, Long> {

	/**
	 * 发送短信
	 * @param content  	短信内容（超过40个字，可能按照两条短信费用计算）
	 * @param phones 	1个或多个手机号，多个用英文逗号分隔
	 * @param linkPhone 联系电话（一般指咨询详情等电话）
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> sendSms(String content, String phones, String linkPhone);
	
	/**
	 * 验证-短信发送
	 * @param request 
	 * @param response 
	 * @param phone 验证的手机号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> valSms(HttpServletRequest request, HttpServletResponse response, 
		String phone);
	
	/**
	 * 发送-短信
	 * @param request 
	 * @param response 
	 * @param phone 手机号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> sendSmsCode(HttpServletRequest request, HttpServletResponse response, 
		String phone);
	
	/**
	 * 添加-短信发送记录
	 * @param request 
	 * @param response 
	 * @param phone 手机号
	 * @param context 短信内容
	 * @param sState 结果状态
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addSmsRecord(HttpServletRequest request, HttpServletResponse response, 
		String phone, String context, int sState);
	
}
