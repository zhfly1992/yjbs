/*
 * File name: CompanyVehicleController.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 Administrator 2020年4月20日
 * ... ... ...
 *
 ***************************************************/

package com.fx.web.controller.pc.company;

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
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.service.company.CompanyVehicleService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @Description:
 * @author: zh
 * @version: 1.0, 2020年4月20日
 */
@Api(tags="电脑端-单位管理-车辆模块")
@Controller
@RequestMapping("/company/vehicle")
public class CompanyVehicleController extends BaseController {

	@Autowired
	private CompanyVehicleService companyVehicleService;
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;



	/**
	 * 单位-添加车辆 /company/vehicle/vehicleAdd
	 * 
	 * @param response
	 * 
	 * @param request
	 * 
	 * @param jsonObject
	 * 
	 * @param
	 */
	@RequestMapping(value = "vehicleAdd", method = RequestMethod.POST)
	public void companyVehicleAdd(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.companyVehicleAdd(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 单位-修改车辆 /company/vehicle/vehicleUpdate
	 * 
	 * @param response
	 * 
	 * @param request
	 * 
	 * @param jsonObject
	 * 
	 * @param
	 */
	@RequestMapping(value = "vehicleUpdate", method = RequestMethod.POST)
	public void companyVehicleUpdate(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = companyVehicleService.companyVehicleUpdate(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description:单位-删除车辆 /company/vehicle/vehicleDelete
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月21日
	 */
	@RequestMapping(value = "vehicleDelete", method = RequestMethod.POST)
	public void companyVehicleDelete(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = companyVehicleService.companyVehicleDelete(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description: 单位 -查询车辆列表 /company/vehicle/vehicleListFind
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月21日
	 */
	@RequestMapping(value = "vehicleListFind", method = RequestMethod.POST)
	public void companyVehicleFindList(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = companyVehicleService.companyVehicleFindLists(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description:单位-根据车辆id查询车辆信息  /company/vehicle/vehicleFindById
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
	@RequestMapping(value = "vehicleFindById", method = RequestMethod.POST)
	public void companyVehicleFindById(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.companyVehicleFindById(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);

	}
	
	
	/**
	 * 
	 * @Description:车辆-设置主驾驶员/company/vehicle/setDriver
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
 	@ApiOperation(value="设置主驾驶员", notes="车辆设置驾驶员")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="车辆id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="uname", 
			dataType="String",
			value="驾驶员uname eg：U1588836285726"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "setDriver", method = RequestMethod.POST)
	public void setDriverToVehicle(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.setDriverToVehicle(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
 	
 	
 	
	/**
	 * 
	 * @Description:车辆-校验车牌号是否重复 /company/vehicle/checkPlateNum
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年5月15日
	 */
	@ApiOperation(value="校验车牌号是否重复", notes="校验车牌号是否重复")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String", 
			value="车牌号  eg：川A2999"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "checkPlateNum", method = RequestMethod.POST)
	public void checkPlateNumExists(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String plateNum = jsonObject.getString("plateNum");
		map = companyVehicleService.checkPlateNumExists(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis), plateNum);
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取单位下的所有车辆/company/vehicle/getAllVehicle
	 * @param response
	 * @param request
	 * @author :zh
	 * @version 2020年5月23日
	 */
	@ApiOperation(value="获取单位下的所有车辆", notes="获取单位下的车辆")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getAllVehicle", method = RequestMethod.POST)
	public void getAllVehicle(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.getAllVehicle(ReqSrc.COMMON, response, request, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	/**
	 * 
	 * @Description:获取单位下的所有车牌号列表，用于下拉框 /company/vehicle/getAllPlateNum
	 * @param response
	 * @param request
	 * @author :xx
	 * @version 20200529
	 */
	@ApiOperation(value="获取单位下的车牌号", notes="获取单位下的车牌号")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getAllPlateNum", method = RequestMethod.POST)
	public void getAllPlateNum(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.getAllPlateSeats(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis),1);
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取单位下的所有车辆座位数列表，用于下拉框 /company/vehicle/getAllSeats
	 * @param response
	 * @param request
	 * @author :xx
	 * @version 20200529
	 */
	@ApiOperation(value="获取单位下的车辆座位数", notes="获取单位下的车辆座位数")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getAllSeats", method = RequestMethod.POST)
	public void getAllPlateSeats(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map = companyVehicleService.getAllPlateSeats(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis),0);
		Message.print(response, map);
	}
	
	
	
	/**
	 * 
	 * @Description:校验是否能被设置为驾驶员，用于设置驾驶员前的检验 /company/vehicle/checkBeforeSetDriver
	 * @param response
	 * @param request
	 * @author :zh
	 * @version 20200601
	 */
	@ApiOperation(value="校验是否能被设置为驾驶员", notes="一个人只能成为一辆车的主驾")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="uname", 
			dataType="String", 
			value="驾驶员uname  eg：U1586856470308"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "checkBeforeSetDriver", method = RequestMethod.POST)
	public void checkBeforeSetDriver(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String uname = jsonObject.getString("uname");
		map = companyVehicleService.checkBeforeSetDriver(ReqSrc.PC_COMPANY, response, request, uname, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
}
