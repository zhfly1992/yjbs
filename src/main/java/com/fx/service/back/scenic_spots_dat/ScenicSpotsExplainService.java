package com.fx.service.back.scenic_spots_dat;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsExplain;
import com.fx.entity.cus.Customer;

public interface ScenicSpotsExplainService extends BaseService<ScenicSpotsExplain, Long> {
	
	/**
	 * 上传-景点图片
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param freq			文件request
	 * @param lcus 			登录用户
	 * @param filedName 	所属字段名称
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> uploadScenicSpotsImg(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		MultipartHttpServletRequest freq, Customer lcus, String filedName);
	
	/**
	 * 添加-景点说明【暂未使用】
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lcus 			登录用户
	 * @param sspId 		景点地址对象id
	 * @param openTimeExp	开放时间说明
	 * @param ticketExp		门票说明
	 * @param notice		公告
	 * @param otherExp		其他说明
	 * @param detailExp		景点详情
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> addScenicSpotsExplain(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String sspId, String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp);
	
	/**
	 * 修改-景点说明【暂未使用】
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lcus 			登录用户
	 * @param sspId 		景点地址对象id
	 * @param openTimeExp	开放时间说明
	 * @param ticketExp		门票说明
	 * @param notice		公告
	 * @param otherExp		其他说明
	 * @param detailExp		景点详情
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updScenicSpotsExplain(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String sspId, String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp);
	
}
