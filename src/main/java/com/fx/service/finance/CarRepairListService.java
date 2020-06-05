package com.fx.service.finance;


import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.CarRepairList;

public interface CarRepairListService extends BaseService<CarRepairList, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20180424
	 * @param pageData 分页数据
	 * @param teamNo 车队编号
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param isCheck 审核状态
	 * @param cpaiStation 维修站
	 * @param driver 驾驶员
	 * @param repeat 重复价格检查
	 */
	public Page<CarRepairList> findRepairList(Page<CarRepairList> pageData,String teamNo,
			String find,String cpaiStation,String sTime,String eTime,String isCheck,String driver,String repeat);

	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20180424
	 * @param updId 修改/删除对象id
	 * @param rel 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operRepair(String updId, CarRepairList rel);
	/**
	 * 获取-维修记账-分页列表
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @return pd 		分页后的数据
	 */
	public Page<CarRepairList> findRepairList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime);
	/**
	 * 获取-维修记录-详情
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param id		维修记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findRepair(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String id);
	/**
	 * 删除-维修记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lname			登录用户名
	 * @param did			维修记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delRepair(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String lname, String did);
	/**
	 * 车队驾驶员-添加/修改-维修记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param multReq		文件上传request
	 * @param uid  			加油记录id
	 * @param lname  		登录账号
	 * @param plateNum 		车牌号
	 * @param currKm 		当前公里数
	 * @param wxPayWay 		支付方式：CASH_PAY-现金；JZ_PAY-记账；
	 * @param wxStation 	维修站名称
	 * @param wxMoney 		维修金额
	 * @param wxDate 		维修日期
	 * @param wxRemark 		维修备注
	 * @param updPicId 		修改后图片id
	 * @param isCn 			是否是出纳：0/空-不是；1-是；
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addUpdRepair(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String uid, 
		String lname, String plateNum, String currKm, String wxPayWay, String wxStation,
		String wxMoney, String wxDate, String wxRemark, String updPicId, String isCn);
	/**
	 * 审核/复核/核销维修记录
	 * @author xx
	 * @date 20190524
	 * @param checkId
	 * @param state 1审核 2复核 3核销
	 * @param note 备注
	 * @param operator 操作员
	 * @param remark 标识
	 * @param currMoney 核销金额
	 * @param isCash 1钱包 
	 * @return 1成功，-1异常
	 */
	public int checkCpai(String checkId, int state,String note,String operator,String remark,double currMoney,int isCash);
	
	/**
	 * 获取-维修记账-详情
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param id 		维修记账对象id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findRepairDetail(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String id);
	/**
	 * 修改-维修记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param multReq		文件上传request
	 * @param teamNo  		登录车队账号
	 * @param luname  		登录账号
	 * @param uid  			加油记录id
	 * @param plateNum 		车牌号
	 * @param currKm 		当前公里数
	 * @param wxPayWay 		支付方式：CASH_PAY-现金；JZ_PAY-记账；
	 * @param wxStation 	维修站名称
	 * @param wxMoney 		维修金额
	 * @param wxDate 		维修日期
	 * @param wxRemark 		维修备注
	 * @param imgIds		图片id数组字符串
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> updRepair(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		MultipartHttpServletRequest multReq, String teamNo, String luname, String uid, String currKm, String wxPayWay,
		String wxStation, String wxMoney, String wxDate, String wxRemark, String imgIds);
	
	
	/*********************************新旧代码分割*******************end**************/
	
	/**
	 * 
	 * @Description: 获取维修记录列表，改成现有代码模式
	 * @param reqsrc
	 * @param request
	 * @param response
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月24日
	 */
	public Map<String,Object> getCarRepairList(ReqSrc reqsrc,HttpServletRequest request, HttpServletResponse response,JSONObject jsonObject,String unitNum);
	
	/**
	 * 
	 * @Description:新增维修记录
	 * @param reqSrc
	 * @param request
	 * @param response
	 * @param unitNum
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> addCarRepair(ReqSrc reqSrc, HttpServletRequest request, HttpServletResponse response,
			String unitNum, JSONObject jsonObject);
}
