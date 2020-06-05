package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.order.CompanyOrderTemp;

public interface CompanyOrderTempService extends BaseService<CompanyOrderTemp, Long> {

	/**
	 * 添加-旅游包车-天数行程临时数据
	 * @author xx
	 * @date 20200514
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param jsonObject 	前端请求参数
	 * @param unitNum		单位编号
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> addDayRouteTemp(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		JSONObject jsonObject,String unitNum);
	
}
