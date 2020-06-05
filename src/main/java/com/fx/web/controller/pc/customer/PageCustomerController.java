package com.fx.web.controller.pc.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

/**
 * 个人-页面跳转-控制器
 */
@Controller
@RequestMapping("/page/cus")
public class PageCustomerController extends BaseController{
	
	@Resource
	RedisUtil redis;
	
	/**
	 * 导航-单位-用户注册
	 * API（get）/page/cus/register
	 */
	@RequestMapping("/register")
	public ModelAndView register(ModelAndView mv) {
		mv.addObject("title", "单位-用户注册");
		
		mv.setViewName("cus/register");
		
		return mv;
	}
	
	/**
	 * 导航-单位-用户登录
	 * API（get）/page/cus/login
	 */
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		mv.addObject("title", "单位-用户登录");
		
		mv.setViewName("cus/login");
		
		return mv;
	}
	
	/**
	 * 导航-单位-用户主页
	 * API（get）/page/cus/goMain
	 */
	@RequestMapping("/goMain")
	public ModelAndView goMain(ModelAndView mv) {
		mv.addObject("title", "单位-用户主页");
		
		mv.setViewName("cus/goMain");
		
		return mv;
	}
	
}
