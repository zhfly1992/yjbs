package com.fx.service.company;


import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.TouristCharter;

public interface TouristCharterService extends BaseService<TouristCharter, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200426
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find 		查询条件
	 * @param carSeats 	座位数
	 * @param areaType 	地区类型
	 * @param sTime 	开始时间
	 * @param eTime 	结束时间
	 * @param areaName 	地区名称
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findTcList(ReqSrc reqsrc,String page, String rows,String find,
			String carSeats,String areaType,String sTime,String eTime,String areaName);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20200426
	 * @param tcId 修改/删除对象id
	 * @param tc 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operTc(String tcId, TouristCharter tc);
	/**
	 * 查询最接近每日公里数的设置
	 * @author xx
	 * @date 20200426
	 * @param hql
	 * @param account
	 * @param areaName
	 * @param carSeats
	 * @param dayKilo
	 * @return
	 */
	public double closeToDayKilo(String hql,String tcId,double dayKilo);
}
