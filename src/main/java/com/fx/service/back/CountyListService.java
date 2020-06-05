package com.fx.service.back;


import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.CountyList;

public interface CountyListService extends BaseService<CountyList, Long> {

	/**
	 * 更新区县景点字符串	
	 * @author qfc
	 * @date 20200426
	 * @param c 欲更新的区县对象
	 * @return 成功 1 失败 0
	 */
	public int updCountViews(CountyList c);
	/**
	 * 重置区县排序
	 * @author qfc
	 * @date 20200426
	 * @param cityId 城市编号
	 * @return 成功 1 失败 0
	 */
	public int resetCountyOrder(String cityId);
	/**
	 * 处理城市列表的序号移动
	 * @author qfc
	 * @date 20200426
	 * @param currCity 区县所属城市编号
	 * @param currOrder 欲移动的当前区县序号
	 * @param targetOrder 目标区县序号
	 * @param sta 1为[向上、向下移动] 2为移动到
	 * @return 成功 1 失败0
	 */
	public int moveCountyOrder(String currCity, int currOrder, int targetOrder, int sta);
	/**
	 * 获取-城市区/县-列表
	 * @author qfc
	 * @date 20200426
	 * @param city 城市名称或编号
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findCountyList(String city);
	
}
