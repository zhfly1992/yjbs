package com.fx.service.company;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CustomType;

public interface CustomTypeService extends BaseService<CustomType, Long> {
	/**
	 *  查询单位客户类型设置列表
	 * @author xx
	 * @date 20200708
	 * @param pageData 分页数据
	 * @param unitNum 单位编号
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findCustomType(ReqSrc reqsrc, String page, String rows,String unitNum, String typeName,
			String sTime, String eTime);
	/**
	 *	客户类型列表，用于下拉框
	 * @author xx
	 * @version 20200708
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, cusTypes:客户类型列表 }
	 */
	public Map<String, Object> findCusTypes(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	/**
	 * 添加/修改客户类型
	 * @author xx
	 * @date 20200708
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param updId 修改记录id
	 * @param typeName 类型名称
	 * @param isSupplier 供应商 0否 1是
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> adupCusType(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String typeName,String isSupplier);
	/**
	 * 删除客户类型
	 * @author xx
	 * @date 20200708
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delCusType(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
	
	/**
	 * @Description:根据id查询客户类型信息
	 * @author xx
	 * @date 20200708
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data:客户类型数据}
	 */
	public Map<String, Object> cusTypeFindById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	
}
