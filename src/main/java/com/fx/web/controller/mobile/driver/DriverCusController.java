package com.fx.web.controller.mobile.driver;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.annotation.Log;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.service.cus.CustomerService;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 驾驶员用户-控制器
 */
@Controller
@RequestMapping("/mb/driver")
public class DriverCusController {
	
	/** 用户信息-服务 */
	@Autowired
	private CustomerService customerSer;
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	
	@ApiOperation(
		value="驾驶员用户-手机号密码登录", 
		notes="返回map{code: 结果状态码, msg: 结果状态码说明}"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="wxid", 
			dataType="String",
			value="登录用户微信id"
		),
		@ApiImplicitParam(
			required=true, 
			name="teamNo", 
			dataType="String",
			value="登录用户车队编号"
		),
		@ApiImplicitParam(
			required=true, 
			name="lphone", 
			dataType="String",
			value="登录手机号"
		),
		@ApiImplicitParam(
			required=true, 
			name="lpass", 
			dataType="String",
			value="登录密码"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "passLogin", method = RequestMethod.POST)
	public void passLogin(HttpServletResponse response, HttpServletRequest request,
		@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String wxid = jsonObject.getString("wxid");
		String teamNo = jsonObject.getString("teamNo");
		String lphone = jsonObject.getString("lphone");
		String lpass = jsonObject.getString("lpass");
		
		map = customerSer.subDriverPassLogin(ReqSrc.WX, response, request, CusRole.TEAM_DRIVER, 
			wxid, teamNo, lphone, lpass, "false");

		Message.print(response, map);
	}
	
	@ApiOperation(
		value="获取-驾驶员登录用户信息",
		notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 数据}"
	)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@Log("获取-驾驶员登录用户信息")
	@RequestMapping(value="getLDriverUser", method=RequestMethod.POST)
	public void getLDriverUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String teamNo = LU.getLUnitNum(request, redis);
		String luname = LU.getLUName(request, redis);
		map = customerSer.findLDriverUser(teamNo, luname);
		
		Message.print(response, map);
	}
	
}
