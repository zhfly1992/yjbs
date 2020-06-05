package com.fx.service.back;


import java.util.List;
import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.CityList;

public interface CityListService extends BaseService<CityList, Long> {

	/**
	 * 重置所有城市排序
	 * @param proCode 所属省份编号
	 * @author qfc
	 * @date 20200426
	 * @return
	 */
	public int resetCityOrder(String proCode);
	/**
	 * 处理城市列表的序号移动
	 * @author qfc
	 * @date 20200426
	 * @param currPro 城市所属省份编号
	 * @param currOrder 欲移动的当前城市序号
	 * @param targetOrder 目标城市序号
	 * @param sta 1为[向上、向下移动] 2为移动到
	 * @return 成功 1 失败0
	 */
	public int moveCityOrder(String currPro, int currOrder, int targetOrder, int sta);
	/**
	 * 获取当前城市列表（默认第一个是当前ip城市，其他的是后台设置顺序的城市）
	 * @author qfc
	 * @date 20200426
	 * @param response 
	 * @param request 
	 * @return 处理后的城市列表
	 */
	public List<CityList> getCurrList(String currCity);
	/**
	 * 根据城市热度排序，获取城市列表
	 * @author qfc
	 * @date 20200426
	 * @return 热度排序、英文顺序排序
	 */
	public List<CityList> getCityListByHot();
	/**
	 * 根据车牌号获取所属城市
	 * @author qfc
	 * @date 20200426
	 * @param plateNum 车牌号
	 */
	public String getCityNameByPlateNum(String plateNum);
	/**
	 * 获取-城市列表
	 * @author qfc
	 * @date 20200426
	 * @param province 省份名称/编号
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findCityList(String province);
	
	/**
	 * 获取-城市对应的车牌前缀
	 * @author zh
	 * @date 20200508
	 * @param id 城市id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> getPlateNumShortById(String id);
}
