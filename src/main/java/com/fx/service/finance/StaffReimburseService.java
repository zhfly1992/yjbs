package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.StaffReimburse;



public interface StaffReimburseService extends BaseService<StaffReimburse, Long> {
	
	/**
	 *  查询员工报账分页列表
	 * @author xx
	 * @date 20200611
	 *@param reqsrc 请求来源
	 * @param page 页码
	 * @param rows  页大小
	 * @param unitNum  单位编号
	 * @param uname    报销人账号
	 * @param deptId   业务部门id
	 * @param remark   摘要
	 * @param money  金额
	 * @param isCheck 状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findStaffReimburse(HttpServletRequest request,ReqSrc reqsrc, String page, String rows, String uname,
			String deptId,String remark,String money,String isCheck,String addMark,String sTime,String eTime);
	/**
	 * @author xx
	 * @version 2020611
	 * @Description:下拉框获取报账摘要列表
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findStaffReimRemarks(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	/**
	 * @Description:根据id查询员工报账信息
	 * @author xx
	 * @date 20200611
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 */
	public Map<String, Object> findStaffReimById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	/**
	 * 单位-添加-员工报账记录
	 * @author xx
	 * @date 20200611
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param uname			报销人账号
	 * @param staffReimInfo 	收入金额=支出金额=摘要=图片url,多张图片逗号拼接/@收入金额=支出金额=摘要=图片url,多张图片逗号拼接
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addStaffReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response,String uname, String staffReimInfo);
	
	/**
	 * 单位-修改-员工报账记录
	 * @author xx
	 * @date 20200611
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param updId  		修改记录id
	 * @param uname			报销人账号
	 * @param gathMoney 	收入金额
	 * @param payMoney 		支出金额
	 * @param remark 		摘要
	 * @param voucherUrl    报账图片URL
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> modifyStaffReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response, String updId, String uname, String gathMoney,
			String payMoney, String remark,String voucherUrl);
	
	/**
	 * 	单位-删除员工报账记录
	 * @author xx
	 * @date 20200611
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delStaffReim(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
	
	/**
	 * 	单位-审核员工报账记录
	 * @author xx
	 * @date 20200611
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param ids 			待审核记录Id
	 * @param note 			备注
	 * @param alNoticeChoice 通报人
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> checkStaffReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			String ids,  String note,String alNoticeChoice);
	
	/**
	 * 拒绝员工报账
	 * @author xx
	 * @date 20200611
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
	 *  生成凭证
	 * @author xx
	 * @date 20200611
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param createInfo	报账id=科目id@报账id=科目id
	 * @param faceCourseInfo    对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额
	 * @param gainTime`		记账时间
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> createReim(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String createInfo, String faceCourseInfo,String gainTime);
	
	/**
	 * 添加-其他记账
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param flen 		上传文件的个数
	 * @param lunitNum	登录单位编号
	 * @param luname	登录用户名
	 * @param plateNum 	车牌号
	 * @param jzDate	记账日期
	 * @param jzType	记账类型
	 * @param jzMoney	记账金额
	 * @param jzRemark	记账备注
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> addQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String flen, String plateNum, String jzDate, String jzType, String jzMoney, 
		String jzRemark);
	
	/**
	 * 修改-其他记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param uid 			其他记账id
	 * @param flen 			上传文件个数
	 * @param lunitNum		登录单位编号
	 * @param luname		登录用户名
	 * @param jzDate		记账日期
	 * @param jzFeeCourseId	记账科目id（记账类型）
	 * @param jzMoney		记账金额
	 * @param jzRemark		记账备注
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> updQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String uid, String flen, String jzDate, String jzFeeCourseId, 
		String jzMoney, String jzRemark);
	
	/**
	 * 获取-其他记账-详情
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param id 		其他记账id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findQtjzDetail(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String id);
	
	/**
	 * 删除-其他记账（即员工记账）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param did 		其他记账id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> delQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String did);
	/**
	 * 获取-其他记账-分页列表
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> findQtjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String page, String rows, String stime, String etime);
	
	/**
	 * 删除-员工记账（包含行程收支）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param did 		其他记账id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> delXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String did);
	
	
	/**
	 * 获取-行程记账-列表
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param orderNum 	子订单编号
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> findXcjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String orderNum, String page, String rows, String stime, String etime);
	
}



