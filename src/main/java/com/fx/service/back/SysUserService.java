package com.fx.service.back;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.SysUser;

public interface SysUserService extends BaseService<SysUser, Long> {
	
	/**
	 * 管理员-用户密码登录
	 * @param response 	response
	 * @param request 	request
	 * @param lphone 	登录手机号
	 * @param lpass 	登录密码
	 * @param imgCode 	图片验证码
	 * @param remberMe 	记住账号
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, uuid: 登录uuid }
	 */
	public Map<String, Object> passLogin(HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe);

	/**
	 * 获取-管理员登录用户信息
	 * @param response response
	 * @param request request
	 * @return map{ code: 结果状态码, msg: 结果状态码说明, token: 登录token }
	 */
	public Map<String, Object> findLSysUser(HttpServletResponse response, HttpServletRequest request);
	
	/**
	 * 获取-管理员用户
	 * @param uname 用户名
	 * @return 用户对象
	 */
	public SysUser findByUname(String uname);

	
	
}
