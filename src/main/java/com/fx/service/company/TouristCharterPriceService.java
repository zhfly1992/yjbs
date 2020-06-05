package com.fx.service.company;


import java.util.Date;
import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.TouristCharterPrice;

public interface TouristCharterPriceService extends BaseService<TouristCharterPrice, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> findTcpList(ReqSrc reqsrc,String page, String rows,String find,String sTime,String eTime);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20200426
	 * @param updId 修改/删除记录id
 	 * @param uname 添加账号
 	 * @param dayKm 每日公里数(20200317取消)
 	 * @param dayPrice 开始时间/结束时间/价格/比例,...
 	 * @param tcId 包车价格记录id
 	 * @param fourHourRebate 4小时内折扣
 	 * @param fiveHourRebate 5小时内折扣
 	 * @param sixHourRebate 6小时内折扣
 	 * @param sevenHourRebate 7小时内折扣
 	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */
	public Map<String, Object> adupdeTcpr(ReqSrc reqsrc,String updId,String uname,String dayPrice,String tcId,String dayKm,
 			String fourHourRebate,String fiveHourRebate,String sixHourRebate,String sevenHourRebate);
	/**
	 * 获取折扣
	 * @author xx
	 * @date 20200426
	 * @param account 账号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param areaName 地区名称
	 * @param carSeats 座位数
	 * @param dayKilo 每日公里数
	 * @return discount
	 */
	public double getDiscount(String account,Date sTime, Date eTime,String areaName, int carSeats, double dayKilo);
	/**
	 * 查询合适的旅游包车价格设置
	 * @author xx
	 * @date 20200426
	 * @param account 
	 * @param areaName 起点城市名称
	 * @param endArea 终点城市名称
	 * @param carSeats 座位数20190108取消根据车型全部返回供用户选择
	 * @param sTime 用车开始时间
	 * @param eTime 用车结束时间
	 * @param km 总驾车行程公里
	 * @param waypointCitys 途经点城市数组字符串
	 * @param teamNo 车队编号
	 * @param lon 起点坐标
	 * @param lat 终点坐标
	 * @param tolls 过路费
	 * @return map{}
	 */
	public Map<String, Object> findTour(String account, String areaName, String endArea,String sTime, String eTime,
		 String km, String waypointCitys, String teamNo,String lon,String lat,String tolls,String mainOrderNum);
}
