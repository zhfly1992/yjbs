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
import com.fx.commons.annotation.Log;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.entity.cus.CompanyUser;
import com.fx.service.company.TouristCharterPriceService;
import com.fx.service.company.TouristCharterService;
import com.fx.service.company.TouristTempPriceService;
import com.fx.service.order.CarOrderService;
import com.fx.service.order.CompanyOrderTempService;
import com.fx.service.order.MainCarOrderService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 单位订单-控制器
 */
@Api(tags="电脑端-单位管理-订单模块")
@Controller
@RequestMapping("/company/order")
public class CompanyOrderController extends BaseController {
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	/** 包车价格区域-服务 */
	@Autowired
	private TouristCharterService			tcSer;
	/** 包车价格阶梯设置-服务 */
	@Autowired
	private TouristCharterPriceService			tcpSer;
	/** 包车价格阶梯临时设置-服务 */
	@Autowired
	private TouristTempPriceService			ttpSer;
	
	/** 单位-往返订单临时参数-服务 */
	@Autowired
	private CompanyOrderTempService cotSer;
	
	/** 行程订单-服务 */
	@Autowired
	private CarOrderService carOrderService;
	/**主订单-服务*/
	@Autowired
	private MainCarOrderService mainCarOrderService;
	
	
	/**
	 * 获取-包车价格区域-分页列表 API（post）/company/order/tourList
	 * @author xx
	 * @date 20200427
	 */
	@ApiOperation(value="获取包车价格区域-分页", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String", 
			value="页码 eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页显示行数 eg：20"
		),
		@ApiImplicitParam(
			name="carSeats", 
			dataType="String",
			value="座位数 eg：10"
		),
		@ApiImplicitParam(
			name="areaType", 
			dataType="String",
			value="地区类型类型 eg：0城市 1景点"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="开始时间"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="结束时间"
		),
		@ApiImplicitParam(
			name="areaName", 
			dataType="String",
			value="地区名称 eg：成都市"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="/tourList", method=RequestMethod.POST)
	public void tourList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String carSeats = jsonObject.getString("carSeats");
		String areaType = jsonObject.getString("areaType");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		String areaName = jsonObject.getString("areaName");
		map = tcSer.findTcList(ReqSrc.PC_COMPANY, page, rows, "18982208376", carSeats, areaType, sTime, eTime, areaName);

		Message.print(response, map);
	}
	
	/**
	 * 获取-包车价格阶梯-分页列表 API（post）/company/order/tcpList
	 * @author xx
	 * @date 20200427
	 */
	@ApiOperation(value="获取包车价格区域-分页", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String", 
			value="页码 eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页显示行数 eg：20"
		),
		@ApiImplicitParam(
			required=true, 
			name="toucId", 
			dataType="String",
			value="包车设置id eg：1"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="开始时间"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="结束时间"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="/tcpList", method=RequestMethod.POST)
	public void tcpList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String toucId = jsonObject.getString("toucId");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = tcpSer.findTcpList(ReqSrc.PC_COMPANY, page, rows, toucId, sTime, eTime);

		Message.print(response, map);
	}
	
	/**
 	 * 添加/修改价格阶梯设置提交API（post）/company/order/adupdeTcpr
 	 * @author xx
 	 * @date 20200427
 	 */
	@ApiOperation(value="添加/修改价格阶梯设置", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="updId", 
			dataType="String", 
			value="修改记录id,添加时为空 eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="dayPrice", 
			dataType="String",
			value="每日价格,周一到周日 eg：600=600=600=600=600=600=600"
		),
		@ApiImplicitParam(
			required=true, 
			name="toucId", 
			dataType="String",
			value="包车设置id eg：1"
		),
		@ApiImplicitParam(
			name="dayKm", 
			dataType="String",
			value="每日公里数 eg:20"
		),
		@ApiImplicitParam(
			required=true, 
			name="fourHourRebate", 
			dataType="String",
			value="4小时内折扣 eg:0.8"
		),
		@ApiImplicitParam(
			required=true, 
			name="fiveHourRebate", 
			dataType="String",
			value="5小时内折扣 eg:0.8"
		),
		@ApiImplicitParam(
			required=true, 
			name="sixHourRebate", 
			dataType="String",
			value="6小时内折扣 eg:0.8"
		),
		@ApiImplicitParam(
			required=true, 
			name="sevenHourRebate", 
			dataType="String",
			value="7小时内折扣 eg:0.8"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="adupdeTcpr", method=RequestMethod.POST)
 	public void adupdeTcpr(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String updId = jsonObject.getString("updId");
		String dayPrice = jsonObject.getString("dayPrice");
		String toucId = jsonObject.getString("toucId");
		String dayKm = jsonObject.getString("dayKm");
		String fourHourRebate = jsonObject.getString("fourHourRebate");
		String fiveHourRebate = jsonObject.getString("fiveHourRebate");
		String sixHourRebate = jsonObject.getString("sixHourRebate");
		String sevenHourRebate = jsonObject.getString("sevenHourRebate");
		map = tcpSer.adupdeTcpr(ReqSrc.PC_COMPANY, updId,LU.getLUName(request, redis), dayPrice, toucId, dayKm, 
				fourHourRebate, fiveHourRebate, sixHourRebate, sevenHourRebate);

		Message.print(response, map);
 	}
 	
 	/**
	 * 通过包车区域id和座位数获取已有价格临时价格设置（post）/company/order/hasTempById
	 * @author xx
 	 * @date 20200427
	 */
	@ApiOperation(value="通过包车区域id和座位数获取已有价格临时价格设置", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="toucId", 
			dataType="String",
			value="包车设置id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="seats", 
			dataType="String",
			value="座位数"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "hasTempById", method = RequestMethod.POST)
	public void getCompanyCusById(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String toucId = jsonObject.getString("toucId");
		String seats = jsonObject.getString("seats");
		map = ttpSer.hasTempById(ReqSrc.PC_COMPANY, LU.getLUName(request, redis), toucId, seats);
		Message.print(response, map);
		
	} 	
 	/**
 	 * 添加临时价格提交API（post）/company/order/tempPrice
 	 * @author xx
 	 * @date 20200427
 	 */
	@ApiOperation(value="添加临时价格", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="toucId", 
			dataType="String",
			value="包车设置id eg：1"
		),
		@ApiImplicitParam(
			required=true,
			name="expireTime", 
			dataType="String",
			value="过期时间 eg:2020-05-31 23:59:59"
		),
		@ApiImplicitParam(
			required=true, 
			name="tempPrice", 
			dataType="String",
			value="公里数/每日价格(周一到周日) eg：50/600=600=600=600=600=600=600"
		),
		@ApiImplicitParam(
			required=true, 
			name="carSeats", 
			dataType="String",
			value="座位数 eg:10"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="tempPrice", method=RequestMethod.POST)
 	public void tempPrice(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
		String toucId = jsonObject.getString("toucId");
		String expireTime = jsonObject.getString("expireTime");
		String tempPrice = jsonObject.getString("tempPrice");
		String carSeats = jsonObject.getString("carSeats");
		map=ttpSer.addTempPirice(ReqSrc.PC_COMPANY, toucId,LU.getLUName(request, redis),carSeats, expireTime, tempPrice);
		Message.print(response, map);
 	}
 	
 	/**
 	 *获取订单列表API（post）/company/order/orderList
 	 * @author zh
 	 * @date 20200429
 	 */
	@ApiOperation(value="获取订单列表", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 订单列表, count:总数, statics: 统计}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="int",
			value="页码 eg：1"
		),
		@ApiImplicitParam(
			required=true,
			name="row", 
			dataType="int",
			value="每页数量 eg:10"
		),
		@ApiImplicitParam(
			required=false, 
			name="find", 
			dataType="String",
			value="业务员或者订单号"
		),
		@ApiImplicitParam(
			required=false, 
			name="orderPayStatus", 
			dataType="String",
			value="订单支付状态 "
		),
		@ApiImplicitParam(
			required=false, 
			name="orderSource", 
			dataType="String",
			value="订单来源 "
		),
		@ApiImplicitParam(
			required=false, 
			name="orderStatus", 
			dataType="String",
			value="订单状态 "
		),
		@ApiImplicitParam(
			required=false, 
			name="startTime", 
			dataType="String",
			value="开始时间"
		),
		@ApiImplicitParam(
			required=false, 
			name="endTime", 
			dataType="String",
			value="结束时间"
		),
		@ApiImplicitParam(
			required=true, 
			name="compositor", 
			dataType="String",
			value="顺序 eg:ASC,DESC"
		),
		@ApiImplicitParam(
			required=false, 
			name="timeType", 
			dataType="String",
			value="时间类型 eg:1.用车时间,2.下单时间"
		),
		@ApiImplicitParam(
			required=false, 
			name="driver", 
			dataType="String",
			value="驾驶员"
		),
		@ApiImplicitParam(
			required=false, 
			name="seats", 
			dataType="String",
			value="座位数"
		),
		@ApiImplicitParam(
			required=false, 
			name="dutyMan", 
			dataType="String",
			value="用车方负责人"
		),
		@ApiImplicitParam(
			required=false, 
			name="suppMan", 
			dataType="String",
			value="供车方负责人"
		),
		@ApiImplicitParam(
			required=false, 
			name="plateNum", 
			dataType="String",
			value="车牌号"
		),
		@ApiImplicitParam(
			required=false, 
			name="routeType", 
			dataType="String",
			value="行程类型"
		),
		@ApiImplicitParam(
			required=false, 
			name="serviceType", 
			dataType="String",
			value="订单业务类型"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="orderList",method=RequestMethod.POST)
 	public void getCarOrderList(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = mainCarOrderService.findMainCarOrderList(ReqSrc.PC_COMPANY, response, request, jsonObject,LU.getLUnitNum(request, redis));
 		Message.print(response, map);
 	}
 	
 	/**
 	 *设置订单外调API（post）/company/order/setExternal
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="设置订单外调", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="idList", 
			dataType="String",
			value="子订单id数组  eg：[1,2]"
		),
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="setExternal",method=RequestMethod.POST)
 	public void setOrderExternal(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.setExternal(ReqSrc.PC_COMPANY, response, request,jsonObject);
 		Message.print(response, map);
 	}
 	
 	/**
 	 *取消订单外调API（post）/company/order/cancelExternal
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="取消订单外调", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="idList", 
			dataType="String",
			value="子订单id数组  eg：[1,2]"
		),
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="cancelExternal",method=RequestMethod.POST)
 	public void cancelOrderExternal(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.cancelExternal(ReqSrc.PC_COMPANY, response, request,jsonObject);
 		Message.print(response, map);
 	}
	
 	/**
 	 *锁定 订单外调API（post）/company/order/lockExternal
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="锁定 订单外调", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="idList", 
			dataType="String",
			value="子订单id数组  eg：[1,2]"
		),
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="lockExternal",method=RequestMethod.POST)
 	public void lockOrderExternal(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.lockExternal(ReqSrc.PC_COMPANY, response, request,jsonObject,LU.getLRealName(request, redis));
 		Message.print(response, map);
 	}
	
 	/**
 	 *解锁订单外调API（post）/company/order/unlockExternal
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="解锁订单外调", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="idList", 
			dataType="String",
			value="子订单id数组  eg：[1,2]"
		),
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="unlockExternal",method=RequestMethod.POST)
 	public void unlockOrderExternal(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.unlockExternal(ReqSrc.PC_COMPANY, response, request,jsonObject,LU.getLRealName(request, redis));
 		Message.print(response, map);
 	}
 	
 	/**
 	 *删除订单API（post）/company/order/deleteOrder
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="删除订单API", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="idList", 
			dataType="String",
			value="子订单id数组  eg：[1,2]"
		),
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="deleteOrder",method=RequestMethod.POST)
 	public void deleteOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.deleteOrder(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
 	/**
 	 *根据id获取子订单API（post）/company/order/getCarOrderById
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="根据id获取子订单", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 子订单数据}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="子订单id  eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="getOrderById",method=RequestMethod.POST)
 	public void getCarOrderById(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.getCarOrderById(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
 	/**
 	 *根据id获取主订单（post）/company/order/getMainCarOrderById
 	 * @author zh
 	 * @date 20200529
 	 */
	@ApiOperation(value="根据id获取主订单", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 主订单数据}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="主订单id  eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="getMainCarOrderById",method=RequestMethod.POST)
 	public void getMainCarOrderById(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = mainCarOrderService.getMainCarOrderById(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
 	
 	/**
 	 * 取消派车API（post）/company/order/cancelDisCar
 	 * @author zh
 	 * @date 20200502
 	 */
	@ApiOperation(value="取消派车", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="orderId", 
			dataType="String",
			value="子订单id  eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="cancelDisCar",method=RequestMethod.POST)
 	public void cancelDisCar(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.cancelDisCar(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 				
 	}
 	
	/**
 	 * 修改订单（post）/company/order/updateOrder
 	 * @author zh
 	 * @date 20200503
 	 */
 	@RequestMapping(value="updateOrder",method=RequestMethod.POST)
 	public void updateCarOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.updateOrder(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
	/**
 	 * 取消主订单（post）/company/order/cancelMainCarOrder
 	 * @author zh
 	 * @date 20200503
 	 */
	@ApiOperation(value="取消主订单", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="mainOrderId", 
			dataType="String",
			value="主订单id  eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="cancelMainCarOrder",method=RequestMethod.POST)
 	public void cancelOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = mainCarOrderService.cancelMainCarOrder(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
	
	/**
 	 * 查询主订单下的子订单（post）/company/order/getAllOrderByMainOrderNum
 	 * @author zh
 	 * @date 20200503
 	 */
	@ApiOperation(value="查询主订单下的子订单", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 子订单数据}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="mainOrderNum", 
			dataType="String",
			value="主订单编号  eg：xxxxx"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="getAllOrderByMainOrderNum",method=RequestMethod.POST)
 	public void getAllOrderByMainOrderNum(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.getAllCarOrderByMainOrderNum(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
 	/**
 	 * 添加-旅游包车-天数行程临时数据（post）/company/order/addCompanyDayRouteTemp
 	 * @author xx
 	 * @date 20200513
 	 */
 	@Log("添加-旅游包车-天数行程临时数据")
	@ApiOperation(
		value="添加-旅游包车-天数行程临时数据", 
		notes="返回map集合中有mainOrderNum字段，如果要继续添加下一程或者修改当前行程需要传入该字段"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true,
			name="curDay",  
			paramType="query", 
			dataType="String",
			value="当前行程数 eg：1"
		),
		@ApiImplicitParam(
			required=true,
			name="ywType",  
			paramType="query", 
			dataType="String",
			value="游玩类型 1-周边游；2-市内一日游；3-周边一日游；"
		),
		@ApiImplicitParam(
			required=true,
			name="stime",  
			paramType="query", 
			dataType="String",
			value="出发时间"
		),
		@ApiImplicitParam(
			required=true,
			name="etime",  
			paramType="query", 
			dataType="String",
			value="结束时间"
		),
		@ApiImplicitParam(
			required=true,
			name="spoint",  
			paramType="query", 
			dataType="String",
			value="出发地点 eg：成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区"
		),
		@ApiImplicitParam(
			required=true,
			name="epoint",  
			paramType="query", 
			dataType="String",
			value="到达地点 eg：成都市 双流机场T1=103.666666,30.666666=四川省-成都市-武侯区"
		),
		@ApiImplicitParam(
			required=true,
			name="wpoints", 
			paramType="query", 
			dataType="String",
			value="途径地点 eg：成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区;成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区;"
		),
		@ApiImplicitParam(
			required=true,
			name="isHighSpeed",  
			paramType="query", 
			dataType="String",
			value="是否走高速 0否 1是"
		),
		@ApiImplicitParam(
			name="otherPrice",  
			paramType="query", 
			dataType="String",
			value="其他费用，可为空"
		),
		@ApiImplicitParam(
			name="otherPriceNote",  
			paramType="query", 
			dataType="String",
			value="其他费用备注,可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="routePrice",  
			paramType="query", 
			dataType="String",
			value="行程价格"
		),
		@ApiImplicitParam(
			name="remindRouteCash",  
			paramType="query", 
			dataType="String",
			value="提醒师傅现收, 可为空"
		),
		@ApiImplicitParam(
			name="limitNum",  
			paramType="query", 
			dataType="String",
			value="限号 eg:1,6 可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="seats",  
			paramType="query", 
			dataType="String",
			value="座位数"
		),
		@ApiImplicitParam(
			required=true,
			name="cars",  
			paramType="query", 
			dataType="String",
			value="车辆数"
		),
		@ApiImplicitParam(
				required=true,
				name="routeDetail",  
				paramType="query", 
				dataType="String",
				value="行程详情"
			),
		@ApiImplicitParam(
			required=true,
			name="routeLink",  
			paramType="query", 
			dataType="String",
			value="乘车联系人  eg:姓名-15999999999"
		),
		@ApiImplicitParam(
				name="note",  
				paramType="query", 
				dataType="String",
				value="备注 可为空"
			),
		@ApiImplicitParam(
			name="mainOrderNum",  
			paramType="query", 
			dataType="String",
			value="主订单号 可为空"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addCompanyDayRouteTemp", method=RequestMethod.POST)
  	public void addCompanyDayRouteTemp(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		map = cotSer.addDayRouteTemp(ReqSrc.PC_COMPANY, request, response, jsonObject, LU.getLUnitNum(request, redis));
  		
  		Message.print(response, map);
  	}
 	
 	
	/**
 	 * 添加-旅游包车-行程订单（post）/company/order/addCompanyLybcOrder
 	 * @author xx
 	 * @date 20200514
 	 */
 	@Log("添加-旅游包车-行程订单")
	@ApiOperation(
		value="添加-旅游包车-行程订单", 
		notes="map集合"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true,
			name="mainOrderNum",  
			paramType="query", 
			dataType="String",
			value="主订单编号"
		),
		@ApiImplicitParam(
			required=true,
			name="serviceMan",  
			paramType="query", 
			dataType="String",
			value="下单客户的业务员"
		),
		@ApiImplicitParam(
			required=true,
			name="dutyService",  
			paramType="query", 
			dataType="String",
			value="下单客户的业务负责人"
		),
		@ApiImplicitParam(
			required=true,
			name="companyCusId",  
			paramType="query", 
			dataType="String",
			value="下单客户的id"
		),
		@ApiImplicitParam(
				required=true,
				name="routeLink",  
				paramType="query", 
				dataType="String",
				value="乘车联系人  eg:姓名-15999999999"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addCompanyLybcOrder", method=RequestMethod.POST)
	public void addCompanyLybcOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String serviceMan = jsonObject.getString("serviceMan");
		String dutyService = jsonObject.getString("dutyService");
		String companyCusId = jsonObject.getString("companyCusId");
		String routeLink = jsonObject.getString("routeLink");
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		CompanyUser company=LU.getLCompany(request, redis);
		map = carOrderService.addCompanyLybcOrder(ReqSrc.PC_COMPANY, request, response,company.getCompanyName() ,company.getUnitNum(), 
			  companyCusId,serviceMan,dutyService,routeLink,mainOrderNum);
		
		Message.print(response, map);
	}
 	
 	
 	/**
 	 * 添加-单程接送-订单（post）/company/order/addCompanyOnewayTransferOrder
 	 * @author xx
 	 * @date 20200513
 	 */
 	@Log("添加-单程接送-订单")
	@ApiOperation(
		value="添加-单程接送-订单", 
		notes="返回map集合中有mainOrderNum字段，如果要添加返程需要传入该字段"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true,
			name="companyCusId",  
			paramType="query", 
			dataType="String",
			value="下单客户id"
		),
		@ApiImplicitParam(
			required=true,
			name="serviceMan",  
			paramType="query", 
			dataType="String",
			value="下单业务员姓名"
		),
		@ApiImplicitParam(
			required=true,
			name="dutyService",  
			paramType="query", 
			dataType="String",
			value="下单客户业务负责人姓名"
		),
		@ApiImplicitParam(
			required=true,
			name="routeNo",  
			paramType="query", 
			dataType="String",
			value="当前行程数 eg：去程1 返程2"
		),
		@ApiImplicitParam(
			required=true,
			name="stime",  
			paramType="query", 
			dataType="String",
			value="出发时间"
		),
		@ApiImplicitParam(
			required=true,
			name="spoint",  
			paramType="query", 
			dataType="String",
			value="出发地点 eg：成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区"
		),
		@ApiImplicitParam(
			required=true,
			name="epoint",  
			paramType="query", 
			dataType="String",
			value="到达地点 eg：成都市 双流机场T1=103.666666,30.666666=四川省-成都市-武侯区"
		),
		@ApiImplicitParam(
			required=true,
			name="wpoints", 
			paramType="query", 
			dataType="String",
			value="途径地点 eg：成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区;成都市 天府广场=103.666666,30.666666=四川省-成都市-武侯区;"
		),
		@ApiImplicitParam(
			required=true,
			name="isHighSpeed",  
			paramType="query", 
			dataType="String",
			value="是否走高速 0否 1是"
		),
		@ApiImplicitParam(
			name="otherPrice",  
			paramType="query", 
			dataType="String",
			value="其他费用，可为空"
		),
		@ApiImplicitParam(
			name="otherPriceNote",  
			paramType="query", 
			dataType="String",
			value="其他费用备注,可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="routePrice",  
			paramType="query", 
			dataType="String",
			value="行程价格"
		),
		@ApiImplicitParam(
			name="remindRouteCash",  
			paramType="query", 
			dataType="String",
			value="提醒师傅现收, 可为空"
		),
		@ApiImplicitParam(
			name="limitNum",  
			paramType="query", 
			dataType="String",
			value="限号 eg:1,6 可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="seats",  
			paramType="query", 
			dataType="String",
			value="座位数"
		),
		@ApiImplicitParam(
			required=true,
			name="cars",  
			paramType="query", 
			dataType="String",
			value="车辆数"
		),
		@ApiImplicitParam(
			name="reasonTime",  
			paramType="query", 
			dataType="String",
			value="其他原因延长时间 eg:2.5 单位（小时）可为空"
		),
		@ApiImplicitParam(
			name="note",  
			paramType="query", 
			dataType="String",
			value="备注 可为空"
		),
		@ApiImplicitParam(
			name="routeDetail",  
			paramType="query", 
			dataType="String",
			value="行程详情 可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="routeLink",  
			paramType="query", 
			dataType="String",
			value="乘车联系人  eg:姓名-15999999999"
		),
		@ApiImplicitParam(
			required=true,
			name="isShuttle",  
			paramType="query", 
			dataType="String",
			value="0接 1送"
		),
		@ApiImplicitParam(
			name="wayNum",  
			paramType="query", 
			dataType="String",
			value="航班号,车次号,可为空"
		),
		@ApiImplicitParam(
			name="fdTime",  
			paramType="query", 
			dataType="String",
			value="起飞降落时间，wayNum不为空时此参数必有值"
		),
		@ApiImplicitParam(
			name="mainOrderNum",  
			paramType="query", 
			dataType="String",
			value="主订单号 可为空"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addCompanyOnewayTransferOrder", method=RequestMethod.POST)
  	public void addCompanyOnewayTransferOrder(HttpServletRequest request, HttpServletResponse response,  
  			@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
		CompanyUser company=LU.getLCompany(request, redis);
  		map = carOrderService.addCompanyOnewayTransferOrder(ReqSrc.PC_COMPANY, request, response, jsonObject, company.getCompanyName(), 
  				company.getUnitNum());
  		Message.print(response, map);
  	}
 	
 	
 	/**
 	 * 确认收款价格（post）/company/order/confirmCollection
 	 * @author zh
 	 * @date 202005013
 	 */
 	
 	@ApiOperation(value="确认收款价格", notes="确认收款价格，在订单表中新增确认收款价格人的姓名")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="订单id eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="confirmCollection",method=RequestMethod.POST)
 	public void confirmCollection(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = mainCarOrderService.confirmCollection(ReqSrc.PC_COMPANY, response, request, jsonObject,LU.getLStaff(request, redis));
 		Message.print(response, map);
 	}
 	
 	
 	/**
 	 * 确认付款价格（post）/company/order/confirmPayment
 	 * @author zh
 	 * @date 202005013
 	 */
 	
 	@ApiOperation(value="确认付款价格", notes="确认付款价格，在订单表中新增确认付款价格人的姓名")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="订单id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="confirmPayMentName", 
			dataType="String",
			value="确认付款人姓名 ge：xx"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="confirmPayment",method=RequestMethod.POST)
 	public void confirmPayment(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.confirmPayment(ReqSrc.PC_COMPANY, response, request, jsonObject,LU.getLStaff(request, redis));
 		Message.print(response, map);
 	}
 	
 	
	/**
 	 * 确认用车（post）/company/order/confirmUseCar
 	 * @author zh
 	 * @date 20200518
 	 */
 	@ApiOperation(value="确认用车", notes="确认用车")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="订单id eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="confirmUseCar",method=RequestMethod.POST)
 	public void comfirmUseCar(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		map = carOrderService.confirmUseCar(ReqSrc.PC_COMPANY, response, request, jsonObject);
 		Message.print(response, map);
 	}
 	
	/**
 	 *  人工派单获取车辆（post）/company/order/handleSendCar
 	 * @author xx
 	 * @date 20200529
 	 */
 	@ApiOperation(value="人工派单获取车辆", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data：数据列表}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="sendOrderNum", 
			dataType="String", 
			value="订单号，多个逗号拼接"
		),
		@ApiImplicitParam(
			required=true, 
			name="seats", 
			dataType="String", 
			value="派单座位数，默认为订单的座位数，可以筛选所有车辆的座位数"
		),
		@ApiImplicitParam(
			name="force", 
			dataType="String", 
			value="1强制查询 0非强制查询"
		),
		@ApiImplicitParam(
			name="runArea", 
			dataType="String", 
			value="运营区域：0-不限区域(默认) 1-省际包车 2-市级包车 3-县级包车"
		),
		@ApiImplicitParam(
			name="plateNum", 
			dataType="String", 
			value="查询车牌号，多个逗号拼接，可为空"
		),
		@ApiImplicitParam(
			name="selfOwned", 
			dataType="String", 
			value="自营：MYSELF 非自营：NOTSELF，可为空"
		),
		@ApiImplicitParam(
			name="notContainPn", 
			dataType="String", 
			value="不包含的车牌号，用于点击【下一辆车】的功能，多个逗号拼接，可为空"
		),
		@ApiImplicitParam(
			required=true, 
			name="avgSpeed", 
			dataType="String", 
			value="平均速度(km/h)，默认0"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="handleSendCar",method=RequestMethod.POST)
 	public void handleSendCar(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String sendOrderNum = jsonObject.getString("sendOrderNum");
 		String seats = jsonObject.getString("seats");
 		String force = jsonObject.getString("force");
 		String runArea = jsonObject.getString("runArea");
 		String plateNum = jsonObject.getString("plateNum");
 		String selfOwned = jsonObject.getString("selfOwned");
 		String notContainPn = jsonObject.getString("notContainPn");
 		String avgSpeed = jsonObject.getString("avgSpeed");
 		map=carOrderService.handleSendCar(ReqSrc.PC_COMPANY, sendOrderNum, seats, force, runArea, plateNum, selfOwned, avgSpeed, notContainPn);
 		Message.print(response, map);
 	}
 	
 	
 	/**
 	 *    智能派单获取车辆（post）/company/order/smartSendOrder
 	 * @author xx
 	 * @date 20200519
 	 */
 	@ApiOperation(value="智能派单获取车辆", 
 			notes="返回map中code=1：如果有cancelNum字段，前端需提示是否再次通过智能派单调起该接口并传入该字段；"
 		+ "code=0：提示手动派单，"	
 		+ "code=-1：会返回currPlateNum,nextSeats,cancelNum[0或者订单号]，前端选择是继续派车还是下一辆车，继续派车则调起[confirmSendOrder]并传入cancelNum[非0才传值]，"
 		+ "否则再次调起该接口，并在notContainPn里面加入currPlateNum,通过逗号拼接,seats值为nextSeats；"
 		/*+ "code=-2：会返回currPlateNum,cancelNum[],nextSeats，目标车辆座位数大于目标订单座位数，前端选择是继续派车还是下一辆车，"
 		+ "继续派车则调起[confirmSendOrder]并传入cancelNum[非0才传值]，否则传入notContainPn里面加入currPlateNum,通过逗号拼接,seats值为nextSeats"*/
 		+ "code=-2：会返回carOne[plateNum,cancelNum],carTwo[plateNum,cancelNum]两辆车，选择其中一辆调起[confirmSendOrder]接口，cancelNum[非0才传值]；"
 		+ "code=-3：会返回nextSeats，前端提示是否选择更大车型，确定则再次调起该接口,seats值为nextSeats")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="sendOrderNum", 
			dataType="String", 
			value="订单号，多个逗号拼接"
		),
		@ApiImplicitParam(
			required=true, 
			name="sendModel", 
			dataType="String", 
			value="获取模式，需要用户自己选择：0淡季模式 1旺季模式"
		),
		@ApiImplicitParam(
			name="firstCar", 
			dataType="String", 
			value="车辆优先顺序 eg：1 自营优先 2挂靠优先"
		),
		@ApiImplicitParam(
			name="seats", 
			dataType="String", 
			value="座位数 eg：此参数在请看接口说明"
		),
		@ApiImplicitParam(
			name="notContainPn", 
			dataType="String", 
			value="不包含的车牌号 eg：此参数是接口返回车牌号页面选择下一辆车，就要将之前不要的车牌号都提交到后台"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="smartSendOrder",method=RequestMethod.POST)
 	public void smartSendOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String sendOrderNum = jsonObject.getString("sendOrderNum");
 		String firstCar = jsonObject.getString("firstCar");
 		String seats = jsonObject.getString("seats");
		String notContainPn = jsonObject.getString("notContainPn");
		String sendPlate = jsonObject.getString("sendPlate");
		String sendModel = jsonObject.getString("sendModel");
 		map = carOrderService.smartSendOrder(ReqSrc.PC_COMPANY, response, request, sendOrderNum, firstCar, seats, notContainPn, sendPlate,sendModel);
 		Message.print(response, map);
 	}
 	/**
 	 *  确认派单(非线下车辆)（post）/company/order/confirmSendOrder
 	 * @author xx
 	 * @date 20200519
 	 */
 	@ApiOperation(value="确认派单(非线下车辆)", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="sendOrderNum", 
			dataType="String", 
			value="订单号"
		),
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String", 
			value="车牌号"
		),
		@ApiImplicitParam(
			name="cancelNum", 
			dataType="String", 
			value="取消订单号，可为空"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="confirmSendOrder",method=RequestMethod.POST)
 	public void confirmSendOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String sendOrderNum = jsonObject.getString("sendOrderNum");
 		String cancelNum = jsonObject.getString("cancelNum");
 		String plateNum = jsonObject.getString("plateNum");
 		map = carOrderService.confirmSendSmart(ReqSrc.PC_COMPANY, map, cancelNum, sendOrderNum, plateNum,null,null);
 		Message.print(response, map);
 	}
 	/**
 	 *  确认派单(线下车辆)（post）/company/order/confirmSendUnder
 	 * @author xx
 	 * @date 20200529
 	 */
 	@ApiOperation(value="确认派单(线下车辆)", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="sendOrderNum", 
			dataType="String", 
			value="订单号"
		),
		@ApiImplicitParam(
			required=true, 
			name="suppCar", 
			dataType="String", 
			value="供车方单位名称"
		),
		@ApiImplicitParam(
			required=true, 
			name="suppCarHead", 
			dataType="String", 
			value="供车方业务负责人 eg:15982808024,姓名"
		),
		@ApiImplicitParam(
			required=true, 
			name="sendPlateNum", 
			dataType="String", 
			value="车牌号"
		),
		@ApiImplicitParam(
			required=true, 
			name="driverInfo", 
			dataType="String", 
			value="师傅信息 eg:15982808024,姓名"
		),
		@ApiImplicitParam(
			required=true, 
			name="sendPrice", 
			dataType="String", 
			value="派单价格 eg:500"
		),
		@ApiImplicitParam(
			name="driverGath", 
			dataType="String", 
			value="师傅现收金额，默认0 eg:500"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="confirmSendUnder",method=RequestMethod.POST)
 	public void confirmSendUnder(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String sendOrderNum = jsonObject.getString("sendOrderNum");
 		String suppCar = jsonObject.getString("suppCar");
 		String suppCarHead = jsonObject.getString("suppCarHead");
 		String sendPlateNum = jsonObject.getString("sendPlateNum");
 		String driverInfo = jsonObject.getString("driverInfo");
 		String sendPrice = jsonObject.getString("sendPrice");
 		String driverGath = jsonObject.getString("driverGath");
 		map = carOrderService.confirmSendUnder(ReqSrc.PC_COMPANY, sendOrderNum, suppCar, suppCarHead,
 				sendPlateNum, driverInfo, sendPrice, driverGath);
 		Message.print(response, map);
 	}
 	
 	
 	
	/**
 	 * 经理确认派单（post）/company/order/JLConfirmSend
 	 * @author zh
 	 * @date 20200603
 	 */
 	@ApiOperation(value="经理确认派单", notes="经理确认派单")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="carOrderId", 
			dataType="String", 
			value="子订单id eg：1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="JLConfirmSend",method=RequestMethod.POST)
 	public void JLConfirmSend(HttpServletRequest request, HttpServletResponse response,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
 		String carOrderId = jsonObject.getString("carOrderId");
 		map = carOrderService.JLComfirm(ReqSrc.PC_COMPANY, response, request, carOrderId);
 		Message.print(response, map);
 	}
}
