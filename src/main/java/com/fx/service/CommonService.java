package com.fx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.ReqSrc;

public interface CommonService {

	/**
	 * 获取-图片验证码
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 * @param type 图片类型 1-png图片验证码；2-gif图片验证码；
	 */
	public void findImgCode(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, int type);
	
	/**
	 *   移动端获取-图片验证码
	 * @param reqsrc 	请求来源
	 * @param response 	response
	 * @param request 	request
	 */
	public Map<String, Object> findImgCodeMobile(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	
	/**
	 * 获取-地图地点列表（地图api）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param keywords 	搜索关键字
	 * @param city 		城市名称
	 * @return map{code: 结果状态码, msg: 结果状态信息, data: 数据}
	 */
	public Map<String, Object> findMapPointList(ReqSrc reqsrc, HttpServletRequest request, String keywords, 
		String city);

	/**
	 * 获取-站点列表（机场站点、火车站等）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param travelWay 出行类型 1-飞机；2-火车；3-汽车；
	 * @param city		城市名称
	 * @return map{code: 结果状态码, msg: 结果状态信息, data: 数据}
	 */
	public Map<String, Object> findStationList(ReqSrc reqsrc, HttpServletRequest request, String travelWay,
		String city);
	
	/**
	 * 查询-站点信息（航班号、车次号）
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param num		航班号/车次号
	 * @param date		查询日期 格式为：yyyy-MM-dd eg:2017-10-10
	 * @param travelWay 出行类型 1-飞机；2-火车；3-汽车；
	 * @return
	 */
	public Map<String, Object> findStationInfo(ReqSrc reqsrc, HttpServletRequest request, String num,
		String date, String travelWay);

	/**
	 * 验证-指定城市站点（机场/火车站/车站）平台是否有开通服务
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param travelWay 出行类型 1-飞机；2-火车；3-汽车；
	 * @param city 		城市名称
	 * @param terminal	T1、T2等
	 * @return map{code: 结果状态码, msg: 结果状态信息, terminal}
	 */
	public Map<String, Object> isOpenCityService(ReqSrc reqsrc, HttpServletRequest request, String travelWay,
		String city, String terminal);
	
	/**
	 * 查询-两个坐标点之间的驾车行程规划结果（高德地图）
	 * @param spoint 	起点[lng, lat] （经纬度小数点不超过6位）
	 * @param epoint 	终点[lng, lat] （经纬度小数点不超过6位）
	 * @param waypoints 途经点[lng, lat;lng, lat;] （经纬度小数点不超过6位）
	 * @param stg		查询线路策略
	 * 2-距离最短；10-路程较短（默认）；19-优先选择高速路（与高德地图的“高速优先”策略一致）；
	 * @return 车辆行程结果
	 */
	public CarRouteRes queryCarRouteRes(String spoint, String epoint, String waypoints, String stg);

	/**
	 * 添加-记账报销-文件
	 * @param reqsrc 	请求来源
	 * @param ftype 	文件类型
	 * @param lteamNo 	登录车队编号
	 * @param luname 	登录用户名
	 * @param files 	文件数组
	 * @param uid 		更新记账记录
	 * @return map{code: 结果状态码, msg: 结果状态码说明, ids: 文件上传成功后数据id数组}
	 */
	public Map<String, Object> addJzbxFile(ReqSrc reqsrc, String ftype, String lteamNo, String luname, 
		MultipartFile[] files, String uid);
	
	/**
	 * 修改-记账报销-文件
	 * @param reqsrc 	请求来源
	 * @param fid 		文件数据对象自定义id
	 * @param files 	文件数组
	 * @return map{code: 结果状态码, msg: 结果状态码说明, ids: 文件上传成功后数据id数组}
	 */
	public Map<String, Object> updJzbxFile(ReqSrc reqsrc, String fid, MultipartFile[] files);
	
}
