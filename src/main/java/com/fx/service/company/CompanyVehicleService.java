/*
 * File name: CompanyVehicleService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 Administrator 2020年4月20日
 * ... ... ...
 *
 ***************************************************/

package com.fx.service.company;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.clazz.OrderTemp;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CompanyVehicle;
/**
 * @Description:
 * @author: zh
 * @version: 1.0, 2020年4月20日
 */

public interface CompanyVehicleService extends BaseService<CompanyVehicle, Long> {

	/**
	 * 
	 * @Description:新增车辆接口
	 * @param reqsrc	请求来源
	 * @param response
	 * @param request
	 * @param jsonObject	参数封装
	 * @return	map{ code: 结果状态码, msg: 结果状态码说明 }
	 * @author :zh
	 * @version 2020年4月20日
	 */
	public Map<String, Object> companyVehicleAdd(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);



	/**
	 * 单位-编辑车辆
	 * 
	 * @param response	
	 *            response
	 * @param request
	 *            request
	 * @param reqsrc
	 *            请求来源
	 * @param jsonObject
	 *            封装数据
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */

	public Map<String, Object> companyVehicleUpdate(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:删除车辆
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 目前只用传 id
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 * @author :zh
	 * @version 2020年4月21日
	 */
	public Map<String, Object> companyVehicleDelete(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);
	
	/**
	 * @Description:
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 * @author :zh
	 * @version 2020年4月21日
	 */
	public Map<String, Object> companyVehicleFindLists(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);
	/**
	 * 
	 * @Description:根据id查询车辆信息
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年4月22日
	 */
	public Map<String, Object> companyVehicleFindById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:设置车辆主驾驶员
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject   {
							"id":1,
							"uname":""
							}
	 * @return
	 * @author :zh
	 * @version 2020年4月22日
	 */
	public Map<String, Object> setDriverToVehicle(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:添加车辆时检验车牌号是否重复
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum
	 * @param id
	 * @return
	 * @author :zh
	 * @version 2020年5月15日
	 */
	public Map<String, Object> checkPlateNumExists(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum,String plateNum) ;
	
	/**
	  *  派单-获取最符合条件的车辆
	 * @author xx
	 * @date 20200518
	 * @param reqsrc
	 * @param ot 		派单订单
	 * @param firstCar 	优先顺序
	 * @param seats 	座位数
	 * @param force 	强制查询
     * @param runArea 	运营区域
     * @param plateNum 	车牌号
     * @param selfOwned 0自营 1挂靠
     * @param avgSpeed 平均速度
     * @param notContainPn 不包含的车牌号
     * @param isSmart 0手动  1智能
     * @param sendModel 0淡季模式  1旺季模式
	 * @return list
	 */
	public List<Map<String, Object>> lastSmartCar(ReqSrc reqsrc,OrderTemp ot,String firstCar,String seats,String force,
  			String runArea,String plateNum,String selfOwned,double avgSpeed,String notContainPn,int isSmart,int sendModel) throws Exception;
	/**
	 * @author xx
	 * @date 20200529
	 * @Description:获取单位的所有车牌号，用于下拉框
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum 单位编号
	 * @param status 车辆状态
	 * @return
	 */
	public Map<String, Object> getAllPlates(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum,String status);
	
	/**
	 * @author xx
	 * @date 20200529
	 * @Description:获取单位的所有座位数，用于下拉框
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum 单位编号
	 * @return
	 */
	public Map<String, Object> getAllSeats(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum);
	
	/**
	 * 
	 * @Description:获取单位的所有车辆
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Map<String, Object> getAllVehicle(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum);
	
	/**
	 * 
	 * @Description:设置驾驶员时判断该人员是否已被设置为驾驶员,一个人只能成为一辆车的主驾
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param uname
	 * @return
	 * @author :zh
	 * @version 2020年6月1日
	 */
	public Map<String, Object> checkBeforeSetDriver(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String uname,String unitNum);



	/**
	 * 获取-车队所有车辆
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param teamNo 	车队编号
	 * @param lname 	指定用户名
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findTeamAllCar(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String teamNo, String lname);
	
}
