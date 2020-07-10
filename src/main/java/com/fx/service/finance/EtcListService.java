package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.EtcList;

public interface EtcListService extends BaseService<EtcList, Long> {
	/**
	 *   查询ETC列表
	 * @author xx
	 * @date 20200525
	 * @param reqsrc 请求来源
	 * @param page 当前页号
	 * @param rows 查询行数
	 * @param unitNum 单位编号
	 * @param findOrderNum 订单号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param plateNum 车牌号
	 * @param driverUname 驾驶员账号
	 * @param cardNo 卡号
	 * @param operMark 操作编号
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findEtcList(ReqSrc reqsrc,String page, String rows,String unitNum,String findOrderNum, String sTime, String eTime,
			String plateNum,String driverUname,String cardNo,String operMark);
	/**
	 * 导入etc记录
	 * @author xx
	 * @date 20200525
	 * @param request
	 * @param reqsrc 请求来源
	 * @param file 文件对象
	 * @param cardNo 卡号
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> importFeeEtc(ReqSrc reqsrc,HttpServletRequest request,MultipartFile file,String cardNo);
	/**
	 * 删除ETC记录
	 * @author xx
	 * @date 20200515
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param delId 删除记录id
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> delEtc(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request,String delId);
}
