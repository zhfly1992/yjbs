package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.Staff;
import com.fx.entity.order.MainCarOrder;

public interface MainCarOrderService extends BaseService<MainCarOrder, Long> {
	
	/**
	 *	 业务收款
	 * @author xx
	 * @date 20200602
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param response 			response
	 * @param unitNum			单位编号
	 * @param gathType 0师傅团上现收 1业务员交车款 2客户自己付尾款
	 * @param routeDriver 师傅信息
	 * @param cusId 下账客户id
	 * @param gathMoney 收款金额
	 * @param createInfo	订单id=科目id@订单id=科目id
	 * @param faceCourseInfo    对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额
	 * @param operName 操作账号
	 * @param operRealName 操作姓名
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> serviceGath(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String gathType,String routeDriver, String cusId,
		String gathMoney, String createInfo,String faceCourseInfo,String operName,String operRealName);
	
	/**
	 * 
	 * @Description:获取订单列表
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            搜索参数
	 * @return
	 * @author :zh
	 * @version 2020年5月21日
	 */
	public Map<String, Object> findMainCarOrderList(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,String companyNum);
	
	/**
	 * 
	 * @Description:取消主订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> cancelMainCarOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:根据id获取主订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> getMainCarOrderById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:取消车价确认
	 * @Description:订单清除确认收款人的姓名
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年6月2日
	 */
	public Map<String, Object> cancelConfirmCollection(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:业务付款列表(已确认用车，已经确认收款价格的主订单)
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年6月3日
	 */
	public Map<String, Object> getMainCarOrderForCollection(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,String unitNum);
	
	
	/**
	 * 
	 * @Description:确认收款价格
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月13日
	 */
	public Map<String, Object> confirmCollection(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject,Staff staff);

	/**
	 * 获取-行程记账-主订单列表（未出行、已出行的行程）
	 * @param reqsrc  	请求来源
	 * @param request 	request
	 * @param response 	respons
	 * @param lunitNum 	登录车队编号
	 * @param luname 	登录用户名
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	出发开始时间
	 * @param etime 	出发结束时间
	 * @param datType 	查询数据类型 0-未完团订单；1-已完团订单；2-行程收支订单；
	 * @return map{code: 结果状态码, msg: 结果状态码说明, count: 数据总条数, data: 数据列表}
	 */
	public Map<String, Object> findXcjzMainOrderList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String page, String rows, String stime, String etime, String datType);
}
