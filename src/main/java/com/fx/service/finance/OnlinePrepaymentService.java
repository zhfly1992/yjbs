package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.OnlinePrepayment;

public interface OnlinePrepaymentService extends BaseService<OnlinePrepayment, Long> {
	/**
	 * 查询预付费列表
	 * @author xx
	 * 20190327
	 * @param pageData 分页数据
	 * @param teamNo 车队编号
	 * @param find 查询条件
	 * @param moneyType 1应收款 2已收款
	 * @param timeType 时间类型
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param plateNum 车牌号
	 */
	public Page<OnlinePrepayment> findOpmList(Page<OnlinePrepayment> pageData,String teamNo,String find,
			String moneyType,String timeType,String sTime,String eTime,String plateNum);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * 20190327
	 * @param opmId 修改/删除对象id
	 * @param agt 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operOpm(String opmId, OnlinePrepayment opm);
	/**
	 * 预付费提交
	 * @author xx
	 * @date 20190530
	 * @param teamNo 车队编号
	 * @param orderNum 主订单号
	 * @param payMoney 付款金额
	 * @param payNote 备注
	 * @param payTime 付款时间
	 * @param payAccount 付款人
	 * @param isCash 0车方垫支 1用车方付费
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> adupOnline(ReqSrc reqsrc,HttpServletRequest request,HttpServletResponse response,
			MultipartHttpServletRequest multipartRequest, String teamNo, String orderNum,
			String payMoney, String payNote, String payTime,String payAccount, String isCash);
	/**
	 * 删除上网费
	 * @author xx
	 * @date 20190603
	 * @param did 删除id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delPrepay(ReqSrc reqsrc,HttpServletRequest request,HttpServletResponse response, String did);
	/**
	 * 收上网预付费提交
	 * @author xx
	 * @date 20191220
	 * @param ids 记录id，逗号拼接
	 * @param money 收款金额
	 * @param rootIn 金额来源
	 * @param gathTime 收款时间
	 * @param voucherNo 凭证号
	 * @param isBzy 1是报账员
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> gathPrepaySub(ReqSrc reqsrc,HttpServletRequest request,HttpServletResponse response,
			String ids, double money,String rootIn, String gathTime, String voucherNo,String isBzy);
}
