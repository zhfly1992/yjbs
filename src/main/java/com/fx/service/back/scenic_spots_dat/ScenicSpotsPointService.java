package com.fx.service.back.scenic_spots_dat;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsPoint;
import com.fx.entity.cus.Customer;

public interface ScenicSpotsPointService extends BaseService<ScenicSpotsPoint, Long> {
	
	/**
	 * 添加-景点地点数据【暂未使用】
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param lcus 		登录用户
	 * @param city 		景点所属城市 eg:四川省-成都市
	 * @param county 	景点所属区/县  eg:双流区
	 * @param lngLat 	景点所在地图坐标 格式（gcj02）：104.147788,30.635141
	 * @param mapAddr 	景点所在地图地址  eg:成都市 华府大道地铁C2口
	 * @param addrShort 景点简称 eg:华府大道
	 * @return map{code: 结果状态码, msg: 结果状态码说明, id: 景点地点对象id}
	 */
	public Map<String, Object> addScenicSpotsPoint(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String city, String county, String lngLat, String mapAddr, String addrShort);
	
	/**
	 * 修改-景点地点数据【暂未使用】
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param lcus 		登录用户
	 * @param id 		修改对象id
	 * @param city 		景点所属城市 eg:四川省-成都市
	 * @param county 	景点所属区/县  eg:双流区
	 * @param lngLat 	景点所在地图坐标 格式（gcj02）：104.147788,30.635141
	 * @param mapAddr 	景点所在地图地址  eg:成都市 华府大道地铁C2口
	 * @param addrShort 景点简称 eg:华府大道
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updScenicSpotsPoint(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String id, String city, String county, String lngLat, String mapAddr, String addrShort);

	/**
	 * 添加-景点数据
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lcus 			登录用户
	 * @param city			景点所属城市
	 * @param county		景点所属区/县
	 * @param lngLat		景点坐标
	 * @param mapAddr		景点地图地址
	 * @param addrShort		景点简称
	 * @param openTimeExp	景点开放时间说明
	 * @param ticketExp		景点门票说明
	 * @param notice		景点公告
	 * @param otherExp		景点其他说明
	 * @param detailExp		景点简介
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> addScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String city, String county, String lngLat, String mapAddr, String addrShort, String openTimeExp, 
		String ticketExp, String notice, String otherExp, String detailExp);
	
	/**
	  *   修改-景点数据
	 * @param reqsrc 		请求来源
	 * @param request 		request
	 * @param response 		response
	 * @param lcus 			登录用户
	 * @param id			景点地点对象id
	 * @param city			景点所属城市
	 * @param county		景点所属区/县
	 * @param lngLat		景点坐标
	 * @param mapAddr		景点地图地址
	 * @param addrShort		景点简称
	 * @param openTimeExp	景点开放时间说明
	 * @param ticketExp		景点门票说明
	 * @param notice		景点公告
	 * @param otherExp		景点其他说明
	 * @param detailExp		景点简介
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String id, String city, String county, String lngLat, String mapAddr, String addrShort, 
		String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp);

	/**
	 * 获取-景点数据-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param cityName	城市名称
	 * @param countyName城市区/县名称
	 * @param find		查询关键字
	 * @return map{code: 结果状态码, msg: 结果状态码说明, count: 数据总条数, data: 数据列表}
	 */
	public Map<String, Object> findScenicSpotsList(ReqSrc reqsrc, String page, String rows, String cityName, String countyName, 
		String find);

	/**
	 * 获取-景点数据
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param id		景点地点对象id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String id);

	/**
	 * 删除-景点数据
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param id		景点地点对象id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> delScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String id);

	/**
	 * 更新-景点排序
	 * @param reqsrc 	请求来源
	 * @param id 		景点id
	 * @param sortType 	排序类型
	 * @param id2 		交换序号景点2
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updScenicSpotsSort(ReqSrc reqsrc, String id, String sortType, String id2);

	/**
	 * 获取-景点城市区/县-列表
	 * @param cityName 城市名称
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findJdCityOfCountyList(String cityName);

	/**
	 * 获取-区/县景点-列表
	 * @param cityName 城市名称
	 * @param countyName 区/县名称
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findCountyJdList(String cityName, String countyName);
	
}
