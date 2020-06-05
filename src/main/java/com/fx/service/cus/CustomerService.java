package com.fx.service.cus;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.Customer;

public interface CustomerService extends BaseService<Customer, Long> {
	
	/**
	 * 微信授权-回调方法
	 * @param reqsrc 请求来源
	 * @param request request
	 * @param response response
	 * @return map{ code: 结果状态码, msg: 结果状态码说明，redirectUrl: 跳转地址 }
	 */
	public Map<String, Object> wxAuthCallBack(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 单位单位-手机号密码注册
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @param lphone 	登录手机号
	 * @param lpass 	登录密码
	 * @param imgCode 	图片验证码
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */
	public Map<String, Object> subCompanyRegister(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode);
	
	/**
	 * 单位单位-手机号密码登录
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @param lphone 	登录手机号
	 * @param lpass 	登录密码
	 * @param imgCode 	图片验证码
	 * @param remberMe 	是否记住账号 true-记住；false-不记住；
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, token: 登录token }
	 */
	public Map<String, Object> subCompanyLogin(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe);

	/**
	 * 获取-用户
	 * @param uname 用户名
	 * @return 用户对象
	 */
	public Customer findByName(String uname);
	
	/**
	 * 获取-登录用户信息
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, data: 数据 }
	 */
	public Map<String, Object> findLCus(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);

	/**
	 * 获取-用户分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find 		查询关键字 手机号/姓名/账号
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findCusList(ReqSrc reqsrc, String page, String rows, String find);
	
	/**
	 * 获取-用户-详情
	 * @param reqsrc 			请求来源
	 * @param idOrUnameOrPhone 	用户id/用户名/手机号
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findCusDetail(ReqSrc reqsrc, String idOrUnameOrPhone);

	/**
	 * 驾驶员用户-手机号密码登录
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @param role 		登录用户角色
	 * @param wxid 		登录用户微信id
	 * @param teamNo 	登录用户所在车队编号
	 * @param lphone 	登录手机号
	 * @param lpass 	登录密码
	 * @param remberMe 	是否记住账号 true-记住；false-不记住；
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, token: 登录token }
	 */
	public Map<String, Object> subDriverPassLogin(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
		CusRole role, String wxid, String teamNo, String lphone, String lpass, String remberMe);
	
	/**
	 * 获取-登录驾驶员用户信息
	 * @param teamNo 登录车队编号
	 * @param luname 登录用户名
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, lbuser: 登录用户基类，lwxuser: 登录微信用户基类 }
	 */
	public Map<String, Object> findLDriverUser(String teamNo, String luname);
	
}
