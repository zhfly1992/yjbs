package com.fx.web.controller.pc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转-控制器
 */
@Controller
@RequestMapping("/page")
public class PageController {
	
	/**
	 * 导航-遇见巴士-首页
	 * API（get）/page/index
	 */
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView mv) {
		mv.addObject("title", "遇见巴士-首页");
		
		mv.setViewName("index");
		
		return mv;
	}
	
	/**
	 * 导航-遇见巴士-错误页面
	 * API（get）/page/error
	 */
	@RequestMapping("/error")
	public ModelAndView error(ModelAndView mv) {
		mv.addObject("title", "遇见巴士-错误页面");
		
		mv.setViewName("error");
		
		return mv;
	}
	
	/**
	 * 导航-遇见巴士-404页面
	 * API（get）/page/notfound
	 */
	@RequestMapping("/notfound")
	public ModelAndView notfound(ModelAndView mv) {
		mv.addObject("title", "遇见巴士-404页面");
		
		mv.setViewName("404");
		
		return mv;
	}
	
}
