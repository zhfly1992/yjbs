package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.CarOilList;

public interface CarOilListService extends BaseService<CarOilList, Long> {
	/**
	 * 
	 * @Description:获取车辆加油记录列表-分页
	 * @param reqsrc
	 * @param unitNum
	 * @param plateNum
	 * @param oilStation
	 * @param sTime
	 * @param eTime
	 * @param isCheck
	 * @param driver
	 * @param oilWay
	 * @param timeType
	 * @param page
	 * @param rows
	 * @return
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Map<String, Object> findCoiList(ReqSrc reqsrc, String unitNum, String plateNum, String oilStation, String sTime,
			String eTime, String isCheck, String driver, String oilWay, String timeType, String page, String rows);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20180427
	 * @param updId 修改/删除对象id
	 * @param coi 添加/修改/删除对象[为空，表示根据updId删除设置；不为空，updId为空，则保存，否则修改；]
	 * @return 1成功，-1异常
	 */
	public int operCoi(String updId, CarOilList coi);
	/**
	 * 车队驾驶员-获取-车队车牌号/站点/充值卡
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param teamNo 	驾驶员所属车队编号
	 * @param luname 	驾驶员账号（用于判断是否是驾驶员和获取充值卡） 
	 * @param type 		类型：0-加油方式；1-充值卡；2-加油站；3-维修站；
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findCarOilRepair(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String teamNo, String luname, String type);
	/**
	 * 获取-加油记账-分页列表
	 * @param reqsrc 	数据来源
	 * @param request 	request
	 * @param response 	response
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> findJyjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String page, String rows, String stime, String etime);

	/**
	 * 删除-加油记录
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lunitNum		登录单位编号
	 * @param lname			登录用户名
	 * @param did			加油记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delJyjz(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String lunitNum, String lname, String did);
	/**
	 * 获取-添加加油记账-上一次车辆的公里数和最大续航里程
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param teamNo		车队编号
	 * @param lname			驾驶员用户名
	 * @param plateNum		车牌号
	 * @param uid 			修改加油记账id
	 * @param type 查询类型：1-加油记账；2-维修记账；
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], prevkm[上一次车辆的公里数], maxkm[车辆最大续航里数]}
	 */
	public Map<String, Object> findPrevkmAndMaxkm(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String teamNo, String lname, String plateNum, String uid, String type);
	/**
	 * 审核/复核/核销加油记录
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
	public int checkCoi(String checkId, int state,String note,String operator,String remark,double currMoney,int isCash);
	
	/**
	 * 获取-加油记账-详情
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param luname 	登录用户名
	 * @param id 		加油记账对象id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findJyjzDetail(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String luname, String id);
	/**
	 * 
	 * 撤销油票或油罐车加油记录（已核销）
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param cId			记录id
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> cancelOil(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String cId);
	
	/**
	 * 
	 * @Description:添加车辆加油记录
	 * @param reqSrc
	 * @param request
	 * @param response
	 * @param unitNum
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Map<String, Object> addCarOil(ReqSrc reqSrc,HttpServletRequest request, 
			HttpServletResponse response,String unitNum,JSONObject jsonObject);
	/**
	 * 获取-车辆记账数据
	 * @param reqSrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param lteamNo 	登录车队编号
	 * @param luname 	登录用户名
	 * @param type 		类型：0-加油方式；1-充值卡；2-加油站；3-维修站；
	 */
	public Map<String, Object> findCarJzDat(ReqSrc reqSrc, HttpServletRequest request, HttpServletResponse response,
		String teamNo, String luname, String type);
	
	/**
	 * 添加-加油记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param flen 			上传文件个数
	 * @param lunitNum		登录单位编号
	 * @param luname		登录用户名
	 * @param plateNum 		车牌号
	 * @param currKm 		当前公里数
	 * @param jyWay 		加油方式 
	 * @param jyStation 	加油站名称
	 * @param jyCount 		加油数量
	 * @param jyMoney 		加油金额
	 * @param jyCard 		加油卡号
	 * @param jyDate 		加油日期
	 * @param jyRemark 		加油备注
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> addJyjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String flen, String plateNum, String currKm, String jyWay, String jyStation, 
		String jyCount, String jyMoney, String jyCard, String jyDate, String jyRemark);
	
	/**
	 * 修改-加油记账
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param uid 			加油记账对象id
	 * @param flen 			上传文件的个数
	 * @param lunitNum		登录单位编号
	 * @param luname		登录用户名
	 * @param currKm 		当前公里数
	 * @param jyWay 		加油方式 
	 * @param jyStation 	加油站名称
	 * @param jyCount 		加油数量
	 * @param jyMoney 		加油金额
	 * @param jyCard 		加油卡号
	 * @param jyDate 		加油日期
	 * @param jyRemark 		加油备注
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> updJyjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String uid, String flen, String currKm, String jyWay, String jyStation, 
		String jyCount, String jyMoney, String jyCard, String jyDate, String jyRemark);
}
