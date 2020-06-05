package com.fx.service.wxdat;

import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.order.CarOrder;
import com.fx.entity.wxdat.WxTplmsgData;

public interface WxTplmsgDataService extends BaseService<WxTplmsgData, Long> {

	/**
	 * 获取-指定模板编号的微信模板消息对象数据
	 * @param teamNo 	车队编号
	 * @param tplNo 	自定义模板编号
	 * @return 数据对象
	 */
	public WxTplmsgData getWxTplmsgData(String teamNo, String tplNo);
	
	
	/*************微信-模板消息--begin*******************/
	
	/**
	 * [登录成功通知]-登录成功通知-登录用户
	 * @param wtd 	微信模板消息数据对象
	 * @param wbu 	登录微信信息
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> loginSuccessOfWxMsg(WxTplmsgData wtd, WxBaseUser wbu);
	
	/**
	 * [订单确认通知]-通知车队计调（车队后台/乘客添加订单成功后）
	 * @param wtd 		微信模板消息数据对象
	 * @param order 	车辆订单
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> jdOfWxmsg(WxTplmsgData wtd, CarOrder order, String mbName);
	
	/**
	 * 变更驾驶员-通知旧驾驶员
	 * @param wtd 		微信模板消息数据对象
	 * @param order 	车辆订单
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> changeOfWxmsg(WxTplmsgData wtd, CarOrder order, String mbName);
	
	/**
	 * [新订单通知]-通知驾驶员（计调派单成功后/计调为派车订单添加驾驶员）
	 * @param wtd 		微信模板消息数据对象
	 * @param order 	车辆订单
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> driverOfWxmsg(WxTplmsgData wtd, CarOrder order, String mbName);
	
	/**
	 * [车辆安排提醒]-通知业务员和旅行社（师傅确认订单成功后）
	 * @param wtd 		微信模板消息数据对象
	 * @param order 	车辆订单
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> useCarOfWxmsg(WxTplmsgData wtd, CarOrder order, String mbName);
	
	/**
	 * [订单取消通知]-取消订单通知-驾驶员（计调取消订单）
	 * @param wtd 		微信模板消息数据对象
	 * @param order 	车辆订单
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> cancelOrderOfWxmsg(WxTplmsgData wtd, CarOrder order, String mbName);
	
	/**
	 * [订单待审核通知]-添加记账报销-通知有权限的车队审核人（移动端驾驶员添加记账报销等）
	 * @param wtd 		微信模板消息数据对象
	 * @param obj 		报销-记录
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> orderWaitforCheckOfWxmsg(WxTplmsgData wtd, ReimburseList obj, String mbName);
	
	/**
	 * [车队财务下账通知]-车队财务下账-通知
	 * @param wtd 		微信模板消息数据对象
	 * @param obj 		报销-记录
	 * @param mbName 	欲通知的用户名
	 * @return map{微信模板消息所需数据}
	 */
	public Map<String, Object> financeReimburseOfWxmsg(WxTplmsgData wtd, ReimburseList obj, String mbName);
	
	
	
	/*************微信-模板消息--end*******************/
	
}
