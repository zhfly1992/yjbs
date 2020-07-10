package com.fx.web.controller.pc.back.permi;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.annotation.Log;
import com.fx.commons.annotation.NoRepeatSubmit;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.service.back.SysUserService;
import com.fx.service.cus.CustomerService;

/**
 * 用户-控制器
 */
@Controller
@RequestMapping("/back/cus")
public class BSysCusController{
	
	/** 用户信息-服务 */
	@Autowired
	private CustomerService customerSer;
	/** 管理员用户-服务 */
	@Autowired
	private SysUserService sysUserSer;
	
	
	/**
	 * 管理员用户登录
	 * API（post）/back/cus/passLogin
	 * @param lphone 	登录手机号
	 * @param lpass 	登录密码
	 * @param imgCode 	图片验证码
	 * @param remberMe 	记住账号
	 */
	@RequestMapping(value="passLogin", method=RequestMethod.POST)
	@NoRepeatSubmit
	public void passLogin(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject ps) {
		String lphone = U.P(ps, "lphone");
		String lpass = U.P(ps, "lpass");
		String imgCode = U.P(ps, "imgCode");
		String remberMe = U.P(ps, "remberMe");
		
		Map<String, Object> map = sysUserSer.passLogin(response, request, lphone, lpass, imgCode, remberMe);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-管理员登录用户信息
	 * API（post）/back/cus/getLSysUser
	 */
	@Log("获取登录用户信息")
	@RequestMapping(value="getLSysUser", method=RequestMethod.POST)
	public void getLSysUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = sysUserSer.findLSysUser(response, request);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-用户-分页列表
	 * API（post）/back/cus/getCusList
	 * @param find 	查询关键字
	 */
	@RequestMapping(value="getCusList", method=RequestMethod.POST)
	public void getCusList(HttpServletResponse response, HttpServletRequest request, 
		String page, String rows, String find) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.findCusList(ReqSrc.PC_BACK, page, rows, find);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-用户-详情
	 * API（post）/back/cus/getCusDetail
	 * @param id 	用户id
	 */
	@RequestMapping(value="getCusDetail", method=RequestMethod.POST)
	public void getCusDetail(HttpServletResponse response, HttpServletRequest request, 
		String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.findCusDetail(ReqSrc.PC_BACK, id);
		
		Message.print(response, map);
	}
	
	/**
	 * 登出-系统
	 * API（post）/back/cus/logout
	 */
	@Log("管理员退出系统")
	@RequestMapping(value="logout", method=RequestMethod.POST)
    public void logout(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		SecurityUtils.getSubject().logout();
        U.setPut(map, 1, "登出管理员-系统成功");
        
        Message.print(response, map);
    }
	
}
