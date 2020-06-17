package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.ReimburseList;

public interface ReimburseListService extends BaseService<ReimburseList, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200520
	 * @param reqsrc 请求来源
	 * @param page 页数
	 * @param rows 行数
	 * @param unitNum 车队编号
	 * @param uname  报销人账号
	 * @param vouNum 凭证编号
	 * @param status 收支情况 0收入 1支出
	 * @param isCheck 审核状态
	 * @param courseName 科目名称，逗号拼接
	 * @param myBank 银行名称
	 * @param reimIsCar 0公司开支 1车辆开支
	 * @param reimPlateNum 车牌号
	 * @param reimZy 摘要
	 * @param reimMoney 金额
	 * @param operMark 操作编号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findReimList(ReqSrc reqsrc, String page, String rows,String unitNum, String uname,String vouNum,String status,
			String isCheck,String courseName,String myBank,String reimIsCar,String reimPlateNum,
			String reimZy,String reimMoney,String operMark,String sTime,String eTime);
	/**
	 * @Description:根据id查询凭证信息
	 * @author xx
	 * @date 20200521
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 */
	public Map<String, Object> findReimById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	/**
	 * @author xx
	 * @date 20200520
	 * 单位-添加/修改-凭证记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param unitNum  		车队编号
	 * @param updId  		修改记录id
	 * @param uname			报销人账号
	 * @param feeCourseId 	科目id
	 * @param totalMoney 	总价
	 * @param remark 		备注
	 * @param gainTime	 	记账时间
	 * @param voucherUrl    图片URL
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> adupReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response, String updId, 
			String unitNum,String uname, String feeCourseId,  String totalMoney, String remark,String gainTime,String voucherUrl);
	
	/**
	 * 	单位-删除凭证记录
	 * @author xx
	 * @date 20200520
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delReim(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
	
	/**
	 * 
	 * 单位-核销凭证记录
	 * @author xx
	 * @date 20200520
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param ids 核销记录id
	 * @param money 付款金额
	 * @param note 备注
	 * @param myBankInfo 我的银行格式：银行名称/银行账号
	 * @param transName  对方银行格式：对方户名/对方账号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> confirmReim(ReqSrc reqsrc, HttpServletRequest request, 
			String ids, String money, String note,String myBankInfo, String transInfo);
	
}
