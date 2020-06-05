package com.fx.service.company;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CompanyCustom;

public interface CompanyCustomService extends BaseService<CompanyCustom, Long> {

	/**
	 * 单位-添加客户
	 * 
	 * @param reqsrc
	 *            请求来源
	 * @param response
	 *            response
	 * @param request
	 *            request
	 * @param unitNum
	 *            单位编号
	 * @param companyCus
	 *            封装对象
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */
	public Map<String, Object> subCompanyCusAdup(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum, JSONObject jsonObject);



	/**
	 * 获取-单位客户分页列表
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
	 * @param unitName
	 *            单位全称/简称
	 * @param serviceMan
	 *            业务员
	 * @param recomMan
	 *            recomMan
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findCompanyCusList(ReqSrc reqsrc, String page, String rows, String unitNum, String find,
			String unitName, String serviceMan, String recomMan);



	/**
	 * 
	 * @Description:
	 * @param reqsrc
	 *            请求来源
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            请求数据， id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> deleteCompanyCus(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);



	/**
	 * 
	 * @Description:根据id获取客户信息
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            请求数据，目前只有id
	 * @return
	 * @author :zh
	 * @version 2020年4月21日
	 */
	public Map<String, Object> findCompanyCusById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);



	/**
	 * 
	 * @Description:获取挂靠公司为是的客户部分下拉框，用于添加车辆下拉框
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return { "msg": "", "code": , "data": [ { "unitName": "入职公司",
	 *         "unitSimple": "简称", "id": 1 } ] }
	 * @author :zh
	 * @version 2020年4月22日
	 */
	public Map<String, Object> getCompanyCusIsDepend(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);



	/**
	 * 
	 * @Description:添加客户时检验手机号对应的客户是否已经存在
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param phone
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年5月13日
	 */
	public Map<String, Object> checkPhoneExists(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String phone, String unitNum);



	/**
	 * 
	 * @Description:添加业务负责人
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id
	 * @param personInCharge
	 * @return
	 * @author :zh
	 * @version 2020年5月13日
	 */
	public Map<String, Object> addPersonInCharge(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id, String personInCharge);
	
	/**
	 * 
	 * @Description:获取所有客户的基本信息,用于填写报销人账号
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> getCustomBaseInfo(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum);
	
	/**
	 * 
	 * @Description:获取指定类型的客户信息，暂用于下拉框（如加油记录查询页面的加油站下拉框）
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum
	 * @param cusType
	 * @return
	 * @author :zh
	 * @version 2020年5月24日
	 */
	public Map<String, Object> getCustomInfoByType(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String unitNum,String cusType);
}
