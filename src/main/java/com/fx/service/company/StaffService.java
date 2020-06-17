package com.fx.service.company;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.CompanyUser;



public interface StaffService extends BaseService<Staff, Long> {

	/**
	 * 单位-添加员工
	 * 
	 * @param reqsrc
	 *            请求来源
	 * @param response
	 *            response
	 * @param request
	 *            request
	 * @param unitNum
	 *            单位编号
	 * @param staff
	 *            封装对象：前台要用JSON.stringify(paramdata)，而且要带上请求头，后台才会自动封装
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */
	public Map<String, Object> subStaffAdup(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum, Staff staff);



	/**
	 * 获取-员工分页列表
	 * 
	 * @param reqsrc
	 *            请求来源
	 * @param page
	 *            页码
	 * @param rows
	 *            页大小
	 * @param unitNum
	 *            单位编号
	 * @param find
	 *            查询关键字 手机号/姓名/账号
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findStaffList(ReqSrc reqsrc, String page, String rows, String unitNum, String find);



	/**
	 * 
	 * @Description：添加员工
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param staff
	 * @param companyUser 当前账号信息
	 * @return
	 */
	public Map<String, Object> subStaffAdd(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,CompanyUser companyUser);



	/**
	 * 
	 * @Description:修改员工
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> subStaffUpdate(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);



	/**
	 * 
	 * @Description:删除员工，根据id
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject	包含主键id
	 * @return
	 */

	public Map<String, Object> subStaffDelete(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:根据ID查找员工
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 包含主键id
	 * @return
	 * @author :zh
	 * @version 2020年4月21日
	 */
	public Map<String, Object> findStaffById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:获取公司员工信息，用于下拉框 
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 包含uninNum
	 * @return
	 * @author :zh
	 * @version 2020年4月22日
	 */
	public Map<String, Object> getAllStaff(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:获取公司驾驶员信息，用于下拉框 ,为了后续好改动，该接口和获取员工信息接口分开
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum 
	 * @return
	 * @author :zh
	 * @version 2020年4月22日
	 */
	public Map<String, Object> getDriverList(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum);

}
