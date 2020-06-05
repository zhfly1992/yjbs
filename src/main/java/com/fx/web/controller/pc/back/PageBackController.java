package com.fx.web.controller.pc.back;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fx.commons.utils.enums.AdminRole;
import com.fx.commons.utils.enums.RegWay;
import com.fx.commons.utils.enums.UState;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.entity.back.SysUser;
import com.fx.entity.cus.BaseUser;
import com.fx.service.back.SysUserService;
import com.fx.service.cus.BaseUserService;

/**
 * 数据后台-页面跳转-控制器
 */
@Controller
@RequestMapping("/page/back")
public class PageBackController {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SysUserService sysUserSer;
	@Autowired
	private BaseUserService baseUserSer;
	
	/**
	 * 导航-数据后台-用户登录
	 * API（get）/page/back/login
	 */
	@RequestMapping(value="login", method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mv) {
		// 检查是否有管理员用户，不存在，则添加
		String phone = "18980887490";
		
		BaseUser buser = baseUserSer.findByPhone(phone);
		if(buser == null) {
			buser = new BaseUser();
			buser.setUname(UT.createSysUname());
			buser.setPhone(phone);
			buser.setRealName("超级管理员");
			buser.setSalt(buser.getUname());
			// 重新生成加密登录
            PasswordHelper passHelper = new PasswordHelper();
			buser.setLpass(passHelper.encryptPassword(QC.DEF_LOGIN_PASS, buser.getSalt()));
			buser.setRegWay(RegWay.PC_BACK);
			buser.setUstate(UState.NORMAL);
			buser.setAtime(new Date());
			baseUserSer.save(buser);
            U.log(log, "初始化-管理员-用户基类-完成");
			
			SysUser sysUser = new SysUser();
			sysUser.setBaseUserId(buser);
			sysUser.setRole(AdminRole.SUP_ADMIN);
			sysUserSer.save(sysUser);
			U.log(log, "初始化-管理员-用户-完成");
		}
		
		mv.setViewName("back/login");
		
		return mv;
	}
	
	/**
	 * 导航-数据后台-首页
	 * API（get）/page/back/index
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mv) {
		mv.setViewName("back/index");
		
		return mv;
	}
	
	/**
	 * 导航-数据后台-主页
	 * API（get）/page/back/main
	 */
	@RequestMapping(value="main", method=RequestMethod.GET)
	public ModelAndView main(ModelAndView mv) {
		mv.setViewName("back/main");
		
		return mv;
	}
	
	/**
	 * 导航-数据后台-用户列表
	 * API（get）/page/back/cusList
	 */
	@RequestMapping(value="cusList", method=RequestMethod.GET)
	public ModelAndView cusList(ModelAndView mv) {
		mv.setViewName("back/cus_list");
		
		return mv;
	}
	
	/**
	 * 导航-数据后台-用户详情
	 * API（get）/page/back/cusDetail
	 */
	@RequestMapping(value="cusDetail", method=RequestMethod.GET)
	public ModelAndView cusDetail(ModelAndView mv) {
		mv.setViewName("back/cus_detail");
		
		return mv;
	}
	
}
