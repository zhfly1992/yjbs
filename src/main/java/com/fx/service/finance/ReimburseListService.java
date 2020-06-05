package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.pagingcom.Page;
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
	 * @author xx
	 * @version 20200521
	 * @Description:下拉框获取凭证摘要列表
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findReimRemarks(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
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
	 * 	单位-审核凭证记录
	 * @author xx
	 * @date 20200520
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param ids 			待审核记录Id
	 * @param note 			备注
	 * @param alNoticeChoice 通报人
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> checkReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			String ids,  String note,String alNoticeChoice);
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
	
	/**
	 * 拒绝报销凭证
	 * @author xx
	 * @date 20200520
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param rId			记录id
	 * @param reason		拒绝原因
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> checkRefuse(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String rId, String reason);
	/**
	 * 
	 * 	撤销已审核凭证
	 * @author xx
	 * @date 20200520
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param cId			记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> cancelReim(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String cId);
	
	/**
	 * 导入财务记账报销记录
	 * @author xx
	 * @date 2020521
	 * @param request
	 * @param reqsrc 请求来源
	 * @param file 文件对象
	 */
	public Map<String, Object> reimExport(MultipartFile file,HttpServletRequest request,ReqSrc reqsrc);
	
	///////////////其他记账--begin////////////////////
	/**
	 * 
	 * 获取-其他记账-分页列表
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @return pd 		分页后的数据
	 */
	public Page<ReimburseList> findOtherjzList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime);
	/**
	 * 
	 * 获取-其他记账-详情
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param id		其他记账id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String id);
	/**
	 * 
	 * 车队驾驶员-添加/修改-其他记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param multReq		文件上传request
	 * @param uid  			其他记账id
	 * @param lname  		记账账号
	 * @param plateNum 		车牌号
	 * @param jzDate		记账日期
	 * @param jzType		记账类型
	 * @param jzStatus		收支状态
	 * @param jzMoney		记账金额
	 * @param jzRemark		记账备注
	 * @param updPicId 		修改后图片id
	 * @param isCn 			是否是出纳：0/空-不是；1-是；
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addUpdOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String uid, 
		String lname, String plateNum, String jzDate, String jzType, String jzStatus, 
		String jzMoney, String jzRemark, String updPicId, String isCn);
	/**
	 * 删除-其他记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param unitNum		登录车队编号
	 * @param lname			登录用户名
	 * @param did			其他记账id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String lname, String did);
	
	/**
	 * 获取-全部记账-分页列表
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @param dtype    	数据类型[0-未报账；1-已报账；]
	 * @return pd 		分页后的数据
	 */
	public Page<ReimburseList> findAlljzList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime, 
		String dtype);
	
	/**
	 * 修改-其他记账记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param multReq 		文件request
	 * @param unitNum 		车队编号
	 * @param luname 		登录用户名
	 * @param uid 			记账记录id
	 * @param jzDate 		记账日期
	 * @param jzType 		记账类型
	 * @param jzStatus 		记账状态
	 * @param jzMoney 		记账金额
	 * @param jzRemark 		记账记录
	 * @param imgIds		图片id数组字符串
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> updOtherJz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String unitNum, 
		String luname, String uid, String jzDate, String jzType, String jzStatus, 
		String jzMoney, String jzRemark, String imgIds);
	
	/**
	 * 修改-记账记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param multReq 		文件request
	 * @param unitNum 		车队编号
	 * @param luname 		登录用户名
	 * @param uid 			记账记录id
	 * @param jzDate 		记账日期
	 * @param jzType 		记账类型
	 * @param jzStatus 		记账状态
	 * @param jzMoney 		记账金额
	 * @param jzRemark 		记账记录
	 * @param imgIds		图片id数组字符串
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> updReimburse(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String unitNum, 
		String luname, String uid, String jzDate, String jzType, String jzStatus, 
		String jzMoney, String jzRemark, String imgIds);
	
	/**
	 * 获取-记账对象详情
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param uid 		记账记录对象id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findReimburseDetail(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String luname, String uid);
	
	/**
	 * 删除-记账报销记录id
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param unitNum		登录车队编号
	 * @param lname			登录用户名
	 * @param did			记账报销记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delReimburse(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String lname, String did);
	
	/**
	 * 设置-记账报销通知操作员
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param unitNum 	登录车队编号
	 * @param luname 	登录用户名
	 * @param selOper 	选择的操作员数组字符串
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> updReimburseTipOper(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String luname, String selOper);
	/**
	 * 获取-记账报销通知操作员-数据
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param unitNum 	登录车队编号
	 * @param luname 	登录用户名
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findSetReimburseTipOper(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String unitNum, String luname);
}
