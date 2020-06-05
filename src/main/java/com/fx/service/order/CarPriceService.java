package com.fx.service.order;

import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.CarPrice;

public interface CarPriceService extends BaseService<CarPrice, Long> {

	/**
	 * 获取-单程接送-临时车辆价格列表数据
	 * @param reqsrc 		请求来源
	 * @param lcus			登录用户
	 * @param teamNo		发布订单所属单位编号
	 * @param backRelNum	返程关联编号
	 * @param isShuttle 	是否接送：0-接；1-送；
	 * @param num			航班号/车次号
	 * @param spoint		出发地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区
	 * @param epoint		到达地点 eg：成都市 双流机场T1=103.666666,30.666666=四川省=成都市=武侯区
	 * @param wpoints		途径地点 eg：成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;成都市 天府广场=103.666666,30.666666=四川省=成都市=武侯区;
	 * @param gotime		出发时间
	 * @param flyOrDownTime	起飞或者降落时间
	 * @return map{code: 结果状态码, msg: 结果状态信息, data: 数据}
	 */
	Map<String, Object> findCarPriceList(ReqSrc reqsrc, Customer lcus, String teamNo, String backRelNum,
		String num, String isShuttle, String spoint, String epoint, String wpoints, String gotime, 
		String flyOrDownTime);

	/**
	 * 获取-单程接送-所选择的临时车辆价格信息
	 * @param reqsrc 		请求来源
	 * @param lcus			登录用户
	 * @param companyNum	单位编号
	 * @param selCarId		所选临时车辆价格对象id
	 * @return map{code: 结果状态码, msg: 结果状态信息, op：所选临时订单参数； cp：所选临时车辆价格对象； uselist：可使用的优惠券列表； nouselist：不可使用的优惠券列表；}
	 */
	Map<String, Object> findSelCarPrice(ReqSrc reqsrc, Customer lcus, String companyNum, String selCarId);
	
}
