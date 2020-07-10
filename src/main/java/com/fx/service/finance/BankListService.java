package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.BankList;

public interface BankListService extends BaseService<BankList, Long> {
	/**
	 *   查询单位银行设置列表
	 * @author xx
	 * @date 20200508
	 * @param pageData 分页数据
	 * @param unitNum 单位编号
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param isOpen 1已启用 0未启用
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findBankList(ReqSrc reqsrc, String page, String rows,String unitNum, String find,
			String sTime, String eTime,String isOpen);
	/**
	 * @author xx
	 * @version 20200603
	 * @Description:银行账添加时获取银行列表，用于下拉框（post）/company/finance/findBanks
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param isOpen 0未启用  1已启用
	 * @return map{code: 结果状态码, msg: 结果状态说明, banks:银行列表 }
	 */
	public Map<String, Object> findBanks(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String isOpen);
	/**
	 * 添加/修改银行
	 * @author xx
	 * @date 20200515
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param updId 修改记录id
	 * @param bankName 账户名称
	 * @param cardNo 卡号
	 * @param cardName 开户行
	 * @param course 科目id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> adupBank(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String bankName,String cardNo, String cardName,String courseId);
	/**
	 * 删除银行
	 * @author xx
	 * @date 20200515
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delBank(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
	
	/**
	 * @Description:根据id查询银行信息
	 * @author xx
	 * @date 20200515
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data:银行数据}
	 */
	public Map<String, Object> bankFindById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	
	/**
	 * @Description:查询银行是否能修改
	 * @author xx
	 * @date 20200701
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 查询id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> isAllowModify(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id);
	
	
	/**
	 * 启用银行账本
	 * @author xx
	 * @date 20200515
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param openAccount   启用银行 eg：银行id,银行id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> openAccount(ReqSrc reqsrc, HttpServletRequest request,
			String openAccount);
	
}
