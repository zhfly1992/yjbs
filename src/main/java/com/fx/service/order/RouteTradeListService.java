package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.order.RouteTradeList;

public interface RouteTradeListService extends BaseService<RouteTradeList, Long> {

	
	/**
	 * 添加-行程记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lunitNum 		登录用户单位编号
	 * @param luname 		登录用户名
	 * @param flen 			上传文件个数
	 * @param orderNum		订单编号
	 * @param groupCash		团上现收
	 * @param groupRebate   团上返点
	 * @param routeRebate	行程加点
	 * @param singleFee		打单费
	 * @param washingFee	洗车费
	 * @param parkingFee	停车费
	 * @param roadFee		过路费
	 * @param livingFee 	生活费
	 * @param otherFee 		其他费
	 * @param waterFee      买水费
	 * @param stayFee       住宿费
	 * @param remark 		备注
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> addXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String flen, String orderNum, String groupCash, String groupRebate, 
		String routeRebate, String singleFee, String washingFee, String parkingFee, String roadFee, String livingFee, 
		String otherFee, String waterFee, String stayFee, String remark);
	
	/**
	 * 修改-行程收支
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lunitNum 		登录用户单位编号
	 * @param luname 		登录用户名
	 * @param uid 			行程收支id
	 * @param flen 			上传文件个数
	 * @param groupCash		团上现收
	 * @param groupRebate   团上返点
	 * @param routeRebate	行程加点
	 * @param singleFee		打单费
	 * @param washingFee	洗车费
	 * @param parkingFee	停车费
	 * @param roadFee		过路费
	 * @param livingFee 	生活费
	 * @param otherFee 		其他费
	 * @param waterFee      买水费
	 * @param stayFee       住宿费
	 * @param remark 		备注
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String uid, String flen, String groupCash, String groupRebate, 
		String routeRebate, String singleFee, String washingFee, String parkingFee, String roadFee, String livingFee, 
		String otherFee, String waterFee, String stayFee, String remark);

	/**
	 * 获取-行程记账-详情
	 * @param reqsrc 	请求来源
	 * @param lunitNum 	登录单位编号
	 * @param luname 	登录用户名
	 * @param id 		员工记账id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 员工记账数据}
	 */
	public Map<String, Object> findXcjzDetail(ReqSrc reqsrc, String lunitNum, String luname, String id);
	
}
