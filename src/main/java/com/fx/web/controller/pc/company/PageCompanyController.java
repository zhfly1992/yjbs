package com.fx.web.controller.pc.company;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.LU;
import com.fx.entity.cus.Customer;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

/**
 * 单位-页面跳转-控制器
 */
@Controller
@RequestMapping("/page/company")
public class PageCompanyController extends BaseController{
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 配置文件常量-服务 */
	@Autowired
	private ConfigPs cps;
	
	/**
	 * 导航-单位-用户登录
	 * API（get）/page/company/login
	 */
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		mv.addObject("title", "单位-用户登录");
		
		mv.setViewName("company/login");
		
		return mv;
	}
	
	/**
	 * 导航-单位-用户注册
	 * API（get）/page/company/register
	 */
	@RequestMapping("/register")
	public ModelAndView register(ModelAndView mv) {
		mv.addObject("title", "单位-用户注册");
		
		mv.setViewName("company/register");
		
		return mv;
	}
	
	/**
	 * 导航-单位-用户首页
	 * API（get）/page/company/index
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, ModelAndView mv) {
		mv.addObject("title", "单位-用户首页");
		
		Customer lcus = LU.getLUSER(request, redis);
		mv.addObject("lcus", lcus);
		
		String ip = cps.getJdbcIp();
		System.out.println("jdbcIp="+ip);
		
		mv.setViewName("company/index");
		
		return mv;
	}
	
	/**
	 * 导航-单位-主页
	 * API（get）/page/company/main
	 */
	@RequestMapping("/main")
	public ModelAndView main(ModelAndView mv) {
		mv.addObject("title", "单位-用户主页");
		
		mv.setViewName("company/main");
		
		return mv;
	}
	
	/**
	 * 导航-用户列表
	 * API（get）/page/company/cusList
	 */
	@RequestMapping("/cusList")
	public ModelAndView cusList(ModelAndView mv) {
		mv.addObject("title", "单位-用户列表");
		
		mv.setViewName("company/cus/cus_list");
		
		return mv;
	}
	
}
