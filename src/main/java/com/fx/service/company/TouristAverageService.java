package com.fx.service.company;


import java.util.Date;
import java.util.List;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.company.TouristAverage;

public interface TouristAverageService extends BaseService<TouristAverage, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据
	 */
	public Page<TouristAverage> findTAList(Page<TouristAverage> pageData);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20200426
	 * @param agtId 修改/删除对象id
	 * @param agt 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operTA(String updId, TouristAverage ta);
	/**
	 * 计算平均值
	 * @author xx
	 * @date 20200426
	 * @param state
	 * @param areaName
	 * @param carSeats
	 * @param averageOil
	 * @param averageFreeKmPrice
	 * @return
	 */
	public int operToucAverage(int state,String areaName,int carSeats,double averageOil, double averageFreeKmPrice);
	/**
	 * 计算平均值
	 * @author xx
	 * @date 20200426
	 * @param state
	 * @param tcId
	 * @param dayKilo
	 * @param sTime
	 * @param eTime
	 * @param averagePrice
	 * @param averageFeastRatio
	 * @return
	 */
	public int operTcprAverage(int state,String tcId,Date sTime,Date eTime, String averagePrice,
			double averageFeastRatio);
	/**
	 * 获取平均数据
	 * @author xx
	 * @date 20200426
	 * @param areaName
	 * @param account 账号用于获取该账号临时价格设置-20191205
	 * @param dayKilo
	 * @param sTime
	 * @param eTime
	 * @param count
	 * @return
	 */
	public List<String> getAverage(String areaName,String account,double dayKilo,Date sTime,Date eTime,List<String> tour);
}
