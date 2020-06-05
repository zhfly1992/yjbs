package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.BcCarPrice;

public interface BcCarPriceService extends BaseService<BcCarPrice, Long> {

	/**
	 * 获取-旅游包车-临时车辆价格数据列表
	 * @param reqsrc	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param teamNo 	车队编号
	 * @param lcus 		登录用户
	 * @param dayNum 	天数行程数量
	 * @return map{code: 结果状态码, msg: 结果状态信息, data: 数据}
	 */
	public Map<String, Object> findBcCarPriceList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String teamNo, Customer lcus, String dayNum);
	
}
