package com.fx.web.controller.pc.customer.cus;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fx.commons.annotation.Log;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.service.cus.CustomerService;
import com.fx.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="电脑端-个人管理-用户模块")
@Controller
@RequestMapping("/cus")
public class CusController extends BaseController {
	
	/** 用户信息-服务 */
	@Autowired
	private CustomerService customerSer;
	
	
	@ApiOperation(value="[不登录]个人用户-注册")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="lphone", 
			paramType="query", 
			dataType="String", 
			value="登录手机号"
		),
		@ApiImplicitParam(
			required=true, 
			name="lpass", 
			paramType="query", 
			dataType="String",
			value="登录密码"
		),
		@ApiImplicitParam(
			required=true, 
			name="imgCode", 
			paramType="query", 
			dataType="String",
			value="图片验证码"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="subRegister", method=RequestMethod.POST)
	public void subRegister(HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.subCompanyRegister(ReqSrc.PC_PERSONAL, response, request, 
			lphone, lpass, imgCode);
		
		Message.print(response, map);
	}
	
	@ApiOperation(value="[不登录]个人用户-登录")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="lphone", 
			paramType="query", 
			dataType="String", 
			value="登录手机号"
		),
		@ApiImplicitParam(
			required=true, 
			name="lpass", 
			paramType="query", 
			dataType="String",
			value="登录密码"
		),
		@ApiImplicitParam(
			required=true, 
			name="imgCode", 
			paramType="query", 
			dataType="String",
			value="图片验证码"
		),
		@ApiImplicitParam(
			required=true, 
			name="remberMe", 
			paramType="query", 
			dataType="String",
			value="是否记住账号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="subLogin", method=RequestMethod.POST)
	public void subLogin(HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.subCompanyLogin(ReqSrc.PC_PERSONAL, response, request, 
			lphone, lpass, imgCode, remberMe);
		
		Message.print(response, map);
	}
	
	@Log("获取登录用户信息")
	@ApiOperation(value="获取登录用户信息", notes="data：数据")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getLCompanyUser", method=RequestMethod.POST)
	public void getLCompanyUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.findLCus(ReqSrc.PC_PERSONAL, response, request);
		
		Message.print(response, map);
	}

	@Log("单位退出系统")
	@ApiOperation(value="个人用户-退出")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="logout", method=RequestMethod.POST)
    public void logout(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
        getSubject().logout();
        U.setPut(map, 1, "登出系统成功");
        
        Message.print(response, map);
    }
	
}
