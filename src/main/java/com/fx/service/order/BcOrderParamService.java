package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.order.BcOrderParam;

public interface BcOrderParamService extends BaseService<BcOrderParam, Long> {

	/**
	 * 添加-旅游包车-天数行程临时数据
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param unitNum 		单位编号
	 * @param baseUser		下单用户
	 * @param curDay 		当前行程数
	 * @param ywType 		游玩类型
	 * @param stime 		出发时间
	 * @param etime 		到达时间
	 * @param spoint 		出发地点
	 * @param epoint 		到达地点
	 * @param wpoint 		途径地点
	 * @param isHighSpeed 	是否走高速
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> addDayRouteDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String unitNum, BaseUser bu, String curDay, String ywType, String stime, String etime, String spoint,
		String epoint, String wpoint, String isHighSpeed);
	
}
