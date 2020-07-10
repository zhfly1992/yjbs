package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.FeeCourseTradeFirst;

public interface FeeCourseTradeFirstService extends BaseService<FeeCourseTradeFirst, Long> {
	
	/**
	 *	 期初余额设置
	 *@author xx
	 *@date 20200709
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param courseId  科目id
	 * @param gathMoney 收入金额
	 * @param payMoney  支出金额
	 * @param balance   余额
	 * @param setId     期初记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> firstBalanceSet(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String courseId,String gathMoney, String payMoney,String balance,String setId);
}
