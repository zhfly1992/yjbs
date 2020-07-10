package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.MoneyType;

public interface MoneyTypeService extends BaseService<MoneyType, Long> {
	/**
	 *  查询单位金额类型设置列表
	 * @author xx
	 * @date 20200701
	 * @param pageData 分页数据
	 * @param unitNum 单位编号
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param isOpen 1已启用 0未启用
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findMoneyType(ReqSrc reqsrc, String page, String rows,String unitNum, String typeName,
			String sTime, String eTime);
	/**
	 * 员工列表设置可查看的金额类型，用于下拉框
	 * @author xx
	 * @version 20200701
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, mTypes:金额类型列表 }
	 */
	public Map<String, Object> findMtypes(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	/**
	 * 添加/修改金额类型
	 * @author xx
	 * @date 20200701
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param updId 修改记录id
	 * @param typeName 类型名称
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> adupMtype(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String typeName);
	/**
	 * 删除金额类型
	 * @author xx
	 * @date 20200701
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delMtype(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
	
	/**
	 * @Description:根据id查询金额类型信息
	 * @author xx
	 * @date 20200701
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data:金额类型数据}
	 */
	public Map<String, Object> mtypeFindById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	
}
