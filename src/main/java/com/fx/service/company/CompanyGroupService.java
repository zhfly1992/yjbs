package com.fx.service.company;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CompanyGroup;

public interface CompanyGroupService extends BaseService<CompanyGroup, Long> {
	
	/**
	 * 单位-添加/修改小组
	 * @author xx
	 * @version 20200505
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @param unitNum   单位编号
	 * @param groupName 小组名称
	 * @param linkPhone 组长电话
	 * @param linkName 	组长姓名
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */
	public Map<String, Object> subGroupAdUp(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, String id,
		String unitNum, String groupName, String linkPhone,String linkName);
	/**
	 * @author xx
	 * @version 20200505
	 * @Description:根据ID查找小组
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject 包含主键id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findGroupById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	/**
	 * @author xx
	 * @version 20200505
	 * @Description:单位删除小组
	 * @param reqsrc 请求来源
	 * @param response
	 * @param request
	 * @param jsonObject 请求数据， id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> groupDelete(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,JSONObject jsonObject);
	
	/**
	 * @author xx
	 * @version 20200505
	 * 获取-小组分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param unitNum   单位编号
	 * @param find 		查询关键字 手机号/姓名
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findCompanyGroupList(ReqSrc reqsrc, String page, String rows,String unitNum, String find);
	/**
	 * @author xx
	 * @version 20200505
	 * @Description:获取小组信息，用于下拉框 ,不分页
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum 单位编号
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> getGroupList(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String unitNum);
	/**
	 * @author xx
	 * @version 20200515
	 * @Description:根据小组名称查找小组
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param groupName 小组名称
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表}
	 */
	public Map<String, Object> findGroupByName(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String groupName,String groupId);
}
