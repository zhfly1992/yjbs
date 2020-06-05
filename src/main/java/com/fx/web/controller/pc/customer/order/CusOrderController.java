package com.fx.web.controller.pc.customer.order;

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
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.entity.cus.Customer;
import com.fx.service.order.BcCarPriceService;
import com.fx.service.order.BcOrderParamService;
import com.fx.service.order.CarOrderService;
import com.fx.service.order.CarPriceService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="电脑端-个人管理-订单模块")
@Controller
@RequestMapping("/cus/order")
public class CusOrderController extends BaseController {
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 单程接送-临时车辆价格-服务 */
	@Autowired
	private CarPriceService carPriceSer;
	/** 车辆订单-服务 */
	@Autowired
	private CarOrderService carOrderSer;
	/** 旅游包车-临时订单参数-服务 */
	@Autowired
	private BcOrderParamService bcOrderParamSer;
	/** 旅游包车-临时车辆价格-服务 */
	@Autowired
	private BcCarPriceService bcCarPriceSer;
	
	
	@Log("获取-单程接送-临时车辆价格列表数据")
	@ApiOperation(
		value="获取-单程接送-临时车辆价格列表数据", 
		notes="routeKm：行程距离（公里） routeTime：行程耗时（分钟） data：临时车辆价格列表"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="companyNum",  
			paramType="query", 
			dataType="String",
			value="单位编号"
		),
		@ApiImplicitParam(
			name="backRelNum",  
			paramType="query", 
			dataType="String",
			value="返程关联编号"
		),
		@ApiImplicitParam(
			name="isShuttle",  
			paramType="query", 
			dataType="String",
			value="接送类型：0-接；1-送；"
		),
		@ApiImplicitParam(
			name="num",  
			paramType="query", 
			dataType="String",
			value="航班号/车次号"
		),
		@ApiImplicitParam(
			name="spoint",  
			paramType="query", 
			dataType="String",
			value="出发地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区"
		),
		@ApiImplicitParam(
			name="epoint",  
			paramType="query", 
			dataType="String",
			value="到达地点 eg：成都市 双流机场T1=103.666666,30.666666=四川省=成都市=武侯区"
		),
		@ApiImplicitParam(
			name="wpoints", 
			paramType="query", 
			dataType="String",
			value="途径地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;"
		),
		@ApiImplicitParam(
			name="gotime",  
			paramType="query", 
			dataType="String",
			value="出发时间"
		),
		@ApiImplicitParam(
			name="flyOrDownTime",  
			paramType="query", 
			dataType="String",
			value="起飞或者降落时间"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="queryCarPriceList", method=RequestMethod.POST)
	public void queryCarPriceList(HttpServletResponse response, HttpServletRequest request, 
		String companyNum, String backRelNum, String isShuttle, String num, String spoint, 
		String epoint, String wpoints, String gotime, String flyOrDownTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = carPriceSer.findCarPriceList(ReqSrc.PC_PERSONAL, lcus, companyNum, backRelNum, 
			isShuttle, num, spoint, epoint, wpoints, gotime, flyOrDownTime);
		
		Message.print(response, map);
	}
	
	@Log("获取-单程接送-所选择的临时车辆价格信息")
	@ApiOperation(
		value="获取-单程接送-所选择的临时车辆价格信息", 
		notes="op：所选临时订单参数； cp：所选临时车辆价格对象； uselist：可使用的优惠券列表； nouselist：不可使用的优惠券列表；"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="selCarId",  
			paramType="query", 
			dataType="String",
			value="所选临时车辆价格对象id"
		),
		@ApiImplicitParam(
			name="companyNum",  
			paramType="query", 
			dataType="String",
			value="单位编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getSelCarPrice", method=RequestMethod.POST)
	public void getSelCarPrice(HttpServletResponse response, HttpServletRequest request, 
		String selCarId, String companyNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = carPriceSer.findSelCarPrice(ReqSrc.PC_PERSONAL, lcus, companyNum, selCarId);
		
		Message.print(response, map);
	}
	
	@Log("添加-单程接送-订单")
	@ApiOperation(
		value="添加-单程接送-订单", 
		notes="id: 订单id, orderNum: 订单编号"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="selCarId",  
			paramType="query", 
			dataType="String",
			value="所选临时车辆价格对象id"
		),
		@ApiImplicitParam(
			name="linkPhone",  
			paramType="query", 
			dataType="String",
			value="联系人电话"
		),
		@ApiImplicitParam(
			name="upCarCount",  
			paramType="query", 
			dataType="String",
			value="上车人数"
		),
		@ApiImplicitParam(
			name="note",  
			paramType="query", 
			dataType="String",
			value="备注 eg：有一个大拉箱"
		),
		@ApiImplicitParam(
			name="couponId",  
			paramType="query", 
			dataType="String",
			value="选择的优惠券id"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addOnewayTransferOrder", method=RequestMethod.POST)
  	public void addOnewayTransferOrder(HttpServletRequest request, HttpServletResponse response,  
  		String selCarId, String linkPhone, String upCarCount, String note, String couponId){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		Customer lcus = LU.getLUSER(request, redis);
  		map = carOrderSer.addOnewayTransferOrder(ReqSrc.PC_PERSONAL, request, response, lcus, 
  			OrderSource.PC_PERSONAL, selCarId, linkPhone, upCarCount, note, couponId);
  		
  		Message.print(response, map);
  	}
	
	
	
	
	
	
	@Log("添加-旅游包车-天数行程临时数据")
	@ApiOperation(
		value="添加-旅游包车-天数行程临时数据", 
		notes="id: 订单id"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="teamNo",  
			paramType="query", 
			dataType="String",
			value="车队编号"
		),
		@ApiImplicitParam(
			name="curDay",  
			paramType="query", 
			dataType="String",
			value="当前行程数 eg：1"
		),
		@ApiImplicitParam(
			name="ywType",  
			paramType="query", 
			dataType="String",
			value="游玩类型 1-周边游；2-市内一日游；3-周边一日游；"
		),
		@ApiImplicitParam(
			name="stime", 
			paramType="query", 
			dataType="Date",
			value="出发时间"
		),
		@ApiImplicitParam(
			name="etime", 
			paramType="query", 
			dataType="Date",
			value="到达时间"
		),
		@ApiImplicitParam(
			name="spoint",  
			paramType="query", 
			dataType="String",
			value="出发地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区"
		),
		@ApiImplicitParam(
			name="epoint",  
			paramType="query", 
			dataType="String",
			value="到达地点 eg：成都市 双流机场T1=103.666666,30.666666=四川省=成都市=武侯区"
		),
		@ApiImplicitParam(
			name="wpoints", 
			paramType="query", 
			dataType="String",
			value="途径地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;"
		),
		@ApiImplicitParam(
			name="isHighSpeed", 
			paramType="query", 
			dataType="String",
			value="是否走高速 0-不走高速；1-走高速；"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addDayRouteDat", method=RequestMethod.POST)
  	public void addDayRouteDat(HttpServletRequest request, HttpServletResponse response, 
  		String unitNum, String curDay, String ywType, String stime, String etime, String spoint, 
  		String epoint, String wpoint, String isHighSpeed){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		Customer lcus = LU.getLUSER(request, redis);
  		map = bcOrderParamSer.addDayRouteDat(ReqSrc.PC_PERSONAL, request, response, unitNum, lcus.getBaseUserId(),
  				 curDay, ywType, stime, etime, spoint, epoint, wpoint, isHighSpeed);
  		
  		Message.print(response, map);
  	}
	
	@Log("获取-旅游包车-临时车辆价格数据列表")
	@ApiOperation(
		value="获取-旅游包车-临时车辆价格数据列表", 
		notes="list: 临时车辆价格数据列表"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="teamNo",  
			paramType="query", 
			dataType="String",
			value="车队编号"
		),
		@ApiImplicitParam(
			name="dayNum",  
			paramType="query", 
			dataType="String",
			value="天数行程数量"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getBcCarPriceList", method=RequestMethod.POST)
	public void getBcCarPriceList(HttpServletRequest request, HttpServletResponse response, 
		String teamNo, String dayNum){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = bcCarPriceSer.findBcCarPriceList(ReqSrc.PC_PERSONAL, request, response, teamNo, 
			lcus, dayNum);
		
		Message.print(response, map);
	}
	
	@Log("添加-旅游包车-行程订单")
	@ApiOperation(
		value="添加-旅游包车-行程订单", 
		notes="mainOrderNum: 主订单编号"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="teamNo",  
			paramType="query", 
			dataType="String",
			value="车队编号"
		),
		@ApiImplicitParam(
			name="id",  
			paramType="query", 
			dataType="String",
			value="临时车辆价格对象id"
		),
		@ApiImplicitParam(
			name="isBill",  
			paramType="query", 
			dataType="String",
			value="是否开发票 0-不开发票；1-开发票；"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addLybcOrder", method=RequestMethod.POST)
	public void addLybcOrder(HttpServletRequest request, HttpServletResponse response, 
		String teamNo, String id, String isBill){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = carOrderSer.addLybcOrder(ReqSrc.PC_PERSONAL, request, response, teamNo, 
			lcus, OrderSource.PC_PERSONAL, id, isBill);
		
		Message.print(response, map);
	}


//	/**
//  	 * 添加-旅游包车订单-支付订单
//	 * API（post）：/travel/transfer/addLybcPayOrder
//	 * @param mainOrderNum 	主订单编号
//	 * @param payway 		支付方式
//	 */
//	@RequestMapping(value="addLybcPayOrder", method=RequestMethod.POST)
//  	public void addLybcPayOrder(HttpServletRequest request, HttpServletResponse response, 
//  		String mainOrderNum, String payway){
//  		Map<String, Object> map = new HashMap<String, Object>();
//  		
//  		String teamNo = LU.getTeamNo(request);
//  		String luname = LU.getLName(request);
//  		map = payOrderSer.addLybcPayOrder(ReqSrc.PC_TRAVEL, request, response, teamNo, luname, 
//  			null, mainOrderNum, payway);
//  		
//  		Message.print(response, map);
//  	}
	
}
