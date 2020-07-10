package com.fx.web.controller.pc.back.permi;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.service.cus.permi.MenuService;

/**
 * 菜单资源-控制器
 */
@Controller
@RequestMapping("/back/menu")
public class BMenuController {
	
	/** 菜单资源-服务 */
	@Autowired
	private MenuService menuSer;
	
	
	/**
	 * 添加-菜单资源
	 * API（post）/back/menu/addMenu
	 */
	@RequestMapping(value="addMenu", method=RequestMethod.POST)
	public void addMenu(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject ps) {
//		String systype = U.P(ps, "systype");
		String menuName = U.P(ps, "menuName");
		String pid = U.P(ps, "pid");
		String url = U.P(ps, "url");
		String perms = U.P(ps, "perms");
		String mtype = U.P(ps, "mtype");
		String num = U.P(ps, "num");
		
		Map<String, Object> map = menuSer.addMenu(ReqSrc.PC_COMPANY.name(), menuName, pid, url, perms, mtype, num);
		
		Message.print(response, map);
	}
	
}
