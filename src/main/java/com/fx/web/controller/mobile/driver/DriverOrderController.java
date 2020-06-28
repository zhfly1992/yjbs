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
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.U;
import com.fx.service.order.CarOrderService;
import com.fx.service.order.MainCarOrderService;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 驾驶员-订单-控制器
 */
@Controller
@RequestMapping("/mb/driver/order")
public class DriverOrderController {
	
	/** 车辆订单-服务 */
	@Autowired
	private CarOrderService carOrderSer;
	/** 主订单服务 */
	@Autowired
	private MainCarOrderService mainCarOrderSer;
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	
	@ApiOperation(
		value="获取-车队驾驶员-订单列表", 
		notes="返回map{code: 结果状态码, msg: 结果状态码说明,data: 数据列表，count: 数据总条数}"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String",
			value="页码"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="页大小"
		),
		@ApiImplicitParam(
			required=true, 
			name="stime", 
			dataType="String",
			value="用车时间-开始"
		),
		@ApiImplicitParam(
			required=true, 
			name="etime", 
			dataType="String",
			value="用车时间-结束"
		),
		@ApiImplicitParam(
			required=true, 
			name="isTrip", 
			dataType="String",
			value="出行类型：1-未出行；0-已出行；"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getDriverOrderList", method=RequestMethod.POST)
  	public void getDriverOrderList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String stime = jsonObject.getString("stime");
		String etime = jsonObject.getString("etime");
		String isTrip = jsonObject.getString("isTrip");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String lunitNum = LU.getLUnitNum(request, redis);
		String luname = LU.getLUName(request, redis);
		map = mainCarOrderSer.findXcjzMainOrderList(ReqSrc.WX, request, response, lunitNum, luname, page, rows, stime, etime, isTrip);
		
		Message.print(response, map);
  	}
	
	/**
	 * 验证-驾驶员位置是否是订单出发点/完团地点
	 * 请求API（post）/mb/driver/order/valCofmGoOrDownCar
	 * @param orderNum 	派车-订单编号
	 * @param lnglat 	驾驶员确认出行-坐标：103.123456|30.123456
	 */
	@RequestMapping(value="valCofmGoOrDownCar", method=RequestMethod.POST)
	public void valCofmGoOrDownCar(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String orderNum = U.P(json, "orderNum");
		String lnglat = U.P(json, "lnglat");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = carOrderSer.valCofmGoOrDownCar(ReqSrc.WX, request, response, orderNum, lnglat);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-确认订单
	 * 请求API（post）/mb/driver/order/driverCofmOrder
	 * @param orderNum 	订单编号
	 * @param isAgree 	1-同意; 2-拒绝;
	 * @param reason 	拒绝理由，100中文字符
	 */
	@RequestMapping(value="driverCofmOrder", method=RequestMethod.POST)
	public void driverCofmOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String orderNum = U.P(json, "orderNum");
		String isAgree = U.P(json, "isAgree");
		String reason = U.P(json, "reason");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = carOrderSer.driverCofmOrder(ReqSrc.WX, CusRole.TEAM_DRIVER, request, response, orderNum, isAgree, reason);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-确认订单出行
	 * 请求API（post）/mb/driver/order/driverCofmOrderGo
	 * @param orderNum  派车-订单编号
	 * @param lnglat 	驾驶员确认出行-坐标：103.123456|30.123456
	 * @param isArr		是否到达[1-已到达出行地点；2-已到达完团地点；3-未到达；]
	 * @param isUpCar	乘客是否上车：1-已上车；0-未上车；（点击时使用）
	 * @param isToDP	是否到下车点：1-已到下车点；0-还在走行程；（点击时使用）
	 * @param okBack	确认直接回程完团：1-确认；否则-点错了；（点击时使用）
	 */
	@RequestMapping(value="driverCofmOrderGo", method=RequestMethod.POST)
	public void driverCofmOrderGo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String orderNum = U.P(json, "orderNum");
		String lnglat = U.P(json, "lnglat");
		String isArr = U.P(json, "isArr");
		String isUpCar = U.P(json, "isUpCar");
		String isToDP = U.P(json, "isToDP");
		String okBack = U.P(json, "okBack");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = carOrderSer.updCofmOrderGo(ReqSrc.WX, request, response, orderNum, lnglat, isArr, isUpCar, isToDP, okBack);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-确认完团
	 * 请求API（post）/mb/driver/order/driverCofmDownCar
	 * @param orderNum 派车-订单编号
	 * @param dayId		天数行程id
	 * @param lnglat 	驾驶员确认出行-坐标：103.123456|30.123456
	 * @param isArr		是否到达出行地点[1-已到达；3-未到达；]
	 */
	@RequestMapping(value="driverCofmDownCar", method=RequestMethod.POST)
	public void driverCofmDownCar(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String orderNum = U.P(json, "orderNum");
		String dayId = U.P(json, "dayId");
		String lnglat = U.P(json, "lnglat");
		String isArr = U.P(json, "isArr");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = carOrderSer.driverCofmDownCar(ReqSrc.WX, request, response, orderNum, dayId, lnglat, isArr);
		
		Message.print(response, map);
	}
	
	
}
