package com.fx.service.company;


import java.util.Date;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.company.TouristDayPrice;

public interface TouristDayPriceService extends BaseService<TouristDayPrice, Long> {
	/**
	 * 添加每日价格记录 根据城市，座位数，公里数，时间
	 * @author xx
	 * @date 20200426
	 * @param tcId
	 * @param uname
	 * @param areaName
	 * @param carSeats
	 * @param dayKm
	 * @param sTime
	 * @param eTime
	 * @param dayPrice
	 * @param feastRatio
	 * @return
	 */
	public int addTdPirice(String tcId,String uname,String areaName,int carSeats,String dayKm,
			Date sTime,Date eTime, String dayPrice,double feastRatio);
	/**
	 * 删除每日价格记录 根据tcid，公里数，时间
	 * @author xx
	 * @date 20200426
	 * @param tcId
	 * @param dayKm
	 * @param sTime
	 * @param eTime
	 * @return
	 */
	public int delTdPirice(String tcId,double dayKm,Date sTime,Date eTime);
}
