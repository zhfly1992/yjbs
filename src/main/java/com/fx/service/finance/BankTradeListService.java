package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.BankTradeList;

public interface BankTradeListService extends BaseService<BankTradeList, Long> {
	/**
	 * 查询银行账列表
	 * @author xx
	 * @date 20200508
	 * @param pageData 	分页数据
	 * @param unitNum 	车队编号
	 * @param bankName 	银行名称
	 * @param transName 对方户名
	 * @param remark    摘要
	 * @param timeType  0添加时间 1交易时间
	 * @param sTime 	开始时间
	 * @param eTime 	结束时间
	 * @param status    0收入 1支出
	 * @param isReim	报销状态
	 * @param findMonay 金额
	 * @param openRole  开放角色
	 * @param voucherNum 凭证号
	 * @param operMark   操作编号
	 * @param openSel   0未开放查询 1已开放查询
	 * @param moneyType      金额类型
	 * @param cusName   客户名称
	 * @param serviceName 业务员名称
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> findBankTradeList(ReqSrc reqsrc, String page, String rows,String unitNum,String bankName,String transName,
			String remark,String timeType,String sTime,String eTime,String status,String isReim,String findMoney,
			String openRole,String voucherNum,String operMark,String openSel,String moneyType,String cusName,String serviceName);
	/**
	 * 锁定银行账
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param lockId 锁定id，逗号拼接
	 * @param isLock 0解锁 1锁定
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> lockBankTrade(HttpServletRequest request,ReqSrc reqsrc,String lockId,String isLock);
	/**
	 * 银行账关联财务记账报销（结账）
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param reimId  凭证id
	 * @param btlId 银行账id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> linkReim(HttpServletRequest request,ReqSrc reqsrc,String reimId,String btlId);
	/**
	 * @Description：银行账下账
	 * @author xx
	 * @date 20200523
	 * @param request
	 * @param reqsrc 请求来源
	 * @param btlId  下账记录id
	 * @param money 下账金额
	 * @param companyCusId 客户id
	 * @param remak 摘要
	 * @param notice_uname 通报人id
	 * @param notice_note 通报内容
	 * @param orderNum 下账订单号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> downBtlMoney(ReqSrc reqsrc,HttpServletResponse response,HttpServletRequest request,String btlId,
			String money,String companyCusId,String remark,String notice_uname,String notice_note,String orderNum);
	
	/**
	 * @Description：审核下账记录：通过
	 * @author xx
	 * @date 20200523
	 * @param request
	 * @param reqsrc 请求来源
	 * @param btlId  审核记录id
	 * @param feeCourseId 科目id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> checkYesBtl(ReqSrc reqsrc,HttpServletResponse response,HttpServletRequest request,
			String btlId,String feeCourseId);
	/**
	 * @Description：审核下账记录：不通过
	 * @author xx
	 * @date 20200615
	 * @param request
	 * @param reqsrc 请求来源
	 * @param btlId  审核记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> checkNoBtl(ReqSrc reqsrc,HttpServletResponse response,HttpServletRequest request,
			String btlId);
	/**
	 * 银行互转
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param transId  互转id
	 * @param voucherNumber 凭证号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> transBtl(HttpServletRequest request,ReqSrc reqsrc,String transId,String voucherNumber);
	/**
	 * 根据类型计算本次添加记录的余额
	 * @author xx
	 * @date 20200508
	 * @param unitNum 车队编号
	 * @param accountMoney 本次借方
	 * @param creditMoney 本次贷方
	 * @param bankName 收付款方式
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public double bankBalance(String unitNum,double accountMoney,double creditMoney,String bankName);
	/**
	 * 人工添加银行账
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param myBank  我的银行信息
	 * @param transName 对方户名
	 * @param transNum 对方账号
	 * @param tradeTime 交易时间
	 * @param tradeStatus 0收入 1支出
	 * @param tradeMoney 交易金额
	 * @param balance 余额
	 * @param remark 备注
	 * @param moneyType 金额类型
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addBtl(HttpServletRequest request,ReqSrc reqsrc,String myBank,String transName,String transNum,
			String tradeTime,String tradeStatus,String tradeMoney,String balance,String remark,String moneyType);
	/**
	 * 修改银行账
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param updId  修改id
	 * @param moneyType 金额类型
	 * @param remark 摘要
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> modifyBtl(HttpServletRequest request,ReqSrc reqsrc,String updId,String moneyType,String remark);
	/**
	 * 删除银行账
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param delId 删除id
	 * @param myBankNum 卡号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delBtl(HttpServletRequest request,ReqSrc reqsrc,String delId,String myBankNum);
	/**
	 * 开放银行账查询
	 * @author xx
	 * @date 20200508
	 * @param request
	 * @param reqsrc 请求来源
	 * @param openBtlId 开放id
	 * @param openRole 开放角色
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> openSelBtl(HttpServletRequest request,ReqSrc reqsrc,String openBtlId,String openRole);
	/**
	 * 撤销银行账
	 * @author xx
	 * @date 20200508
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param response 			response
	 * @param cancelId 			撤销id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> cancelBtl(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String cancelId);
	/**
	 * 根据条件获取订单号
	 * @author xx
	 * @param 20200508
	 * @param unitNum 车队编号
	 * @param isCheck 状态
	 */
	public Object [] getOrderNum(String unitNum,String isCheck);
	/**
	 * @author xx
	 * @version 20200517
	 * @Description:根据ID查找银行账
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 包含主键id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表 }
	 */
	public Map<String, Object> findBtlById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	/**
	 * @author xx
	 * @version 20200517
	 * @Description:根据银行账户和银行卡号查询银行账余额
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 包含银行卡号和名称
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表 }
	 */
	public Map<String, Object> findBalanceByBankInfo(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	/**
	 * @author xx
	 * @version 20200517
	 * @Description:添加时获取银行账对方户名列表和摘要列表，下拉框
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, transNames:对方户名列表, remarks:摘要列表 }
	 */
	public Map<String, Object> findTransNamesAndRemarks(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	
	/**
	 * @Description：导入银行账记录
	 * @author xx
	 * @date 20200520
	 * @param request
	 * @param reqsrc 请求来源
	 * @param file 文件对象
	 * @param tradeBank 导入银行
	 * @return map
	 */
	public Map<String, Object> importfeeBtl(MultipartFile file,HttpServletRequest request,ReqSrc reqsrc,String tradeBank);
	
	/**
	 * @Description:导出银行账记录
	 * @author xx
	 * @date 20200520
	 * @param request
	 * @param reqsrc
	 * @param jsonObject
	 * @return  map{code: 结果状态码, msg: 结果状态说明 }
	 */
	public Map<String, Object> btlExport(HttpServletRequest request,HttpServletResponse response,ReqSrc reqsrc,JSONObject jsonObject);
}
