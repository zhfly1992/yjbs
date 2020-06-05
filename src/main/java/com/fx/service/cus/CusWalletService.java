package com.fx.service.cus;


import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.CusWallet;

public interface CusWalletService extends BaseService<CusWallet, Long> {

	/**
	 * 计算实际扣除余额、支付宝、微信金额
	 * @author qfc
	 * @date 20200426
	 * @param type 类型 5车主方领取定金后扣除手续费 6 租车方支付尾款
	 * @param payMoney 支付金额
	 * @param cusWallet 付款方[租车方/车主方]钱包对象
	 * @param subject:{2[订单Id]} {5[收款人用户名,总价,订单Id,车牌号,司机手机号,收定金时间]}
 	 * @return 实际支付金额
	 */
	//public double getRealPayMoney(int type, double payMoney, CusWallet cusWallet,
	//		String subject);
	/**
	 * 车队服务费、租车付定金、尾款、置顶共用钱包扣钱方法
	 * @author xx
	 * @date 20200426
	 * @param type 支付类型
	 * @param payMoney 支付金额
	 * @param loginMeName 支付用户名
	 * @param count 拼接字段
	 * @param note 备注
	 * @param payWallet 支付钱包
	 * @param orderNum 订单号
	 * @return
	 */
	//public int payWallet(int type,double payMoney,String loginMeName,String[] count,String note,
	//        CusWallet payWallet,String orderNum);
	/**
	 * 钱包退款
	 * @author xx
	 * @date 20200426
	 * @param refundMoney 退款金额
	 * @param refundName 退款用户名
	 * @param agent 代理优惠
	 * @param orderNum 订单号
	 * @return
	 */
	//public String refundWallet(double refundMoney,String refundName,String agent,String orderNum);
	/**
	 * 车主领取定金后扣手续费，双方提成，加车辆行程，用车方发单奖励
	 * @author xx
	 * @date 20200426
	 * @param loginMeName 支付用户名
	 * @param handSel 定金
	 * @param plateNum 车牌号
	 * @param carOrder 操作订单
	 * @param driverWallet 操作钱包
	 */
	//public void payHandSel(String loginMeName,double handSel,String plateNum,CarOrderList carOrder,CusWallet driverWallet);
	/**
	 * 电话查询扣费，后期扩展为红包余额分开扣，现在为一起扣
	 * @author xx
	 * @date 20200426
	 * @param i 返回结果
	 * @param payWallet 支付钱包
	 * @param payMoney 支付金额
	 * @param subject 开通时间
	 * @param orderNum 订单号
	 */
	//public String VIPSee(String i,CusWallet payWallet,double payMoney,int subject,String orderNum);
	/**
	 * 开通VIP
	 * @author xx
	 * @date 20200426
	 * @param wallet 支付钱包
	 * @param openDay 开通天数
	 * @param type 1旅行社 2车主
	 */
	//public void openVip(int type,long openDay,CusWallet wallet);
	/**
	 * 判断用户是否是VIP
	 * @author xx
	 * @date 20200426
	 * @param isVip 返回结果 0不是 1是
	 * @param VIPType 会员类型：1用车方 2车主方
	 * @param cName 用户名
	 * @param wallet 钱包
	 */
	//public int isVIP(int isVip,int VIPType,String cName,CusWallet wallet);
	/**
	 * 车队付尾款
	 * @author xx
	 * @date 20200426
	 * @param type 支付类型
	 * @param payMoney 支付金额
	 * @param payName 支付用户名
	 * @param driverName 收款用户名
	 * @param note 备注
	 * @param payWallet 支付钱包
	 * @param orderNum 订单号
	 * @param messageType 0不通知 1通知收款方 2通知付款方
	 * @return
	 */
	//public int teamPayFinal(int type,double payMoney,String payName,String driverName,String note,
	//        CusWallet payWallet,String orderNum,int messageType);
	/**
	 * 车队重新派单退款
	 * @author xx
	 * @date 20200426
	 * @param type 支付类型
	 * @param payMoney 支付金额
	 * @param payName 支付用户名
	 * @param oldDriver 原师傅
	 * @param newDriver 现师傅
	 * @param note 备注
	 * @param orderNum 订单号
	 * @return
	 */
	//public void teamSendRefund(int type,double payMoney,String payName,String oldDriver,
	//		String newDriver,String note,String orderNum);
	/**
	 * 获取有分红权的用户
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据
	 * @param find 查找用户
	 * @param loginName 登录用户
	 */
	//public Page<CusWallet> haveDividend(Page<CusWallet> pageData,String find,String loginName);
	/**
	 * 被分享人领取后给该推荐人加推荐奖（不可使用）
	 * @author xx
	 * @date 20200426
	 * @param type 1领取 2消费
	 * @param sharePhone
	 * @param note 备注
	 * @param floor 奖励层数
	 * @param rewardMoeny 奖励金额
	 */
	//public void recomMoney(int type,String sharePhone,String note,int floor,double rewardMoney);
	
	
	
	/****移动端====begin**************************/
	
	/**
	 * 获取-登录用户钱包信息
	 * @param reqsrc 请求来源
	 * @param request request
	 * @param response response
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据]}
	 */
	//public Map<String, Object> findLWallet(ReqSrc reqsrc,
	//	HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 隐藏/显示-登录用户-钱包余额
	 * @param reqsrc 请求来源
	 * @param request request
	 * @param response response
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据]}
	 */
	//public Map<String, Object> updShBalance(ReqSrc reqsrc, HttpServletRequest request, 
	//	HttpServletResponse response);
	
	/**
	 * 用户-充值-现金余额
	 * @param request 	request
	 * @param response 	response
	 * @param role		用户角色
	 * @param lname		登录用户名
	 * @param openid	登录用户openid
	 * @param count		充值数量
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	//public Map<String, Object> addCusRecharge(HttpServletRequest request, HttpServletResponse response, 
	//	CusRole role, String lname, String openid, String count);
	
	/**
	 * [威富通]支付成功-回调方法（移动端/PC端）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 */
	//public void updPayCallBack(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response);
	
	
	/****移动端====end**************************/
}
