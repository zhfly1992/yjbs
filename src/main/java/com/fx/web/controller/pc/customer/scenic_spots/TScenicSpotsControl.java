package com.fx.web.controller.pc.customer.scenic_spots;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.entity.cus.Customer;
import com.fx.service.back.scenic_spots_dat.ScenicSpotsExplainService;
import com.fx.service.back.scenic_spots_dat.ScenicSpotsPointService;
import com.fx.web.util.RedisUtil;

/**
 * 景点数据-控制器
 */
@Controller
@RequestMapping("/travel/scespo")
public class TScenicSpotsControl{

	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 景点说明-服务 */
	@Autowired
	private ScenicSpotsExplainService sseSer;
	/** 景点地点-服务 */
	@Autowired
	private ScenicSpotsPointService sspSer;
	
	/**
     * 获取-景点数据-分页列表
     * 请求接口（post）：/travel/scespo/getScenicSpotsList
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param cityName	城市查询
	 * @param countyName城市区/县查询
	 * @param find		查询关键字
     */
	@RequestMapping(value="getScenicSpotsList", method=RequestMethod.POST)
  	public void getScenicSpotsList(HttpServletRequest request, HttpServletResponse response, 
  		String page, String rows, String cityName, String countyName, String find){
		
		Map<String, Object> map = sspSer.findScenicSpotsList(ReqSrc.PC_PERSONAL, page, rows, 
			cityName, countyName, find);
		
		Message.print(response, map);
  	}
	
	/**
	 * 更新-景点排序
	 * 请求接口（post）：/travel/scespo/updScenicSpotsSort
	 * @param id 		景点id
	 * @param sortType 	排序类型
	 * @param id2 		交换序号景点2
	 */
	@RequestMapping(value="updScenicSpotsSort", method=RequestMethod.POST)
  	public synchronized void updScenicSpotsSort(HttpServletRequest request, HttpServletResponse response, 
  		String id, String sortType, String id2){
		
		Map<String, Object> map = sspSer.updScenicSpotsSort(ReqSrc.PC_PERSONAL, id, sortType, id2);
		
		Message.print(response, map);
  	}
	
	/**
	 * 获取-景点数据
	 * 请求API（post）/travel/scespo/getScenicSpotsDat
	 * @param id 景点地点对象id
	 */
	@RequestMapping(value="getScenicSpotsDat", method=RequestMethod.POST)
	public void getScenicSpotsDat(HttpServletRequest request, HttpServletResponse response, 
		String id){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = sspSer.findScenicSpotsDat(ReqSrc.PC_PERSONAL, request, response, id);
		
		Message.print(response, map);
	}
	
	/**
	 * 上传-景点图片
	 * 请求API（post）/travel/scespo/uploadImg
	 * @param filedName 所属字段名称
	 */
	@RequestMapping(value="uploadImg", method=RequestMethod.POST)
	public void uploadImg(HttpServletRequest request, HttpServletResponse response, 
		MultipartHttpServletRequest freq, String filedName){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = sseSer.uploadScenicSpotsImg(ReqSrc.PC_PERSONAL, request, response, freq, 
			lcus, filedName);
		
		Message.print(response, map);
	}
	
	/**
	 * 添加-景点数据
	 * 请求API（post）/travel/scespo/addScenicSpotsDat
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
	 */
	@RequestMapping(value="addScenicSpotsDat", method=RequestMethod.POST)
	public void addScenicSpotsDat(HttpServletRequest request, HttpServletResponse response, 
		String city, String county, String lngLat, String mapAddr, String addrShort, 
		String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = sspSer.addScenicSpotsDat(ReqSrc.PC_PERSONAL, request, response, lcus, city, county, 
			lngLat, mapAddr, addrShort, openTimeExp, ticketExp, notice, otherExp, detailExp);
		
		Message.print(response, map);
	}
	
	/**
	 * 修改-景点数据
	 * 请求API（post）/travel/scespo/updScenicSpotsDat
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
	 */
	@RequestMapping(value="updScenicSpotsDat", method=RequestMethod.POST)
	public synchronized void updScenicSpotsDat(HttpServletRequest request, HttpServletResponse response, 
		String id, String city, String county, String lngLat, String mapAddr, String addrShort, 
		String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Customer lcus = LU.getLUSER(request, redis);
		map = sspSer.updScenicSpotsDat(ReqSrc.PC_PERSONAL, request, response, lcus, id, city, county, 
			lngLat, mapAddr, addrShort, openTimeExp, ticketExp, notice, otherExp, detailExp);
		
		Message.print(response, map);
	}
	
	/**
	 * 删除-景点数据
	 * 请求API（post）/travel/scespo/delScenicSpotsDat
	 * @param id 景点地点对象id
	 */
	@RequestMapping(value="delScenicSpotsDat", method=RequestMethod.POST)
	public void delScenicSpotsDat(HttpServletRequest request, HttpServletResponse response, 
		String id){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = sspSer.delScenicSpotsDat(ReqSrc.PC_PERSONAL, request, response, id);
		
		Message.print(response, map);
	}
	
}
