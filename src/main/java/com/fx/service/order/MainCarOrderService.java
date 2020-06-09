package com.fx.service.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.Customer;
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
	 * @param operUname				当前账号
	 * @param gathType 0师傅团上现收 1业务员交车款 2客户自己付尾款
	 * @param ids 订单id,多个逗号拼接
	 * @param gathMoney 收款金额
	 * @param gathRemark 摘要
	 * @param gathTime 收款时间
	 * @param routeDriver 师傅信息
	 * @param cusId 下账客户id
	 * @param preMoney 客户本次预存款
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> serviceGath(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum,String operUname,String gathType, String ids,
		String gathMoney, String gathRemark,String routeDriver,String cusId,String preMoney);
	
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
			HttpServletRequest request, JSONObject jsonObject,Customer customer);
}
