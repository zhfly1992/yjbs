package com.fx.web.controller.pc.customer.wallet;

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
import com.fx.commons.utils.tools.LU;
import com.fx.entity.cus.Customer;
import com.fx.service.order.CarPriceService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="电脑端-个人管理-钱包模块")
@Controller
@RequestMapping("/cus/wallet")
public class CusWalletController extends BaseController {
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 单程接送-临时车辆价格-服务 */
	@Autowired
	private CarPriceService carPriceSer;
	
	
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
	
}
