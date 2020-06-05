package com.fx.service.company;

import java.util.Map;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.TouristTempPrice;

public interface TouristTempPriceService  extends BaseService<TouristTempPrice,Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据
	 * @param teamNo 车队编号
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 */
	public Page<TouristTempPrice> findTouristTempPriceList(Page<TouristTempPrice> pageData,String teamNo,String find,
			String sTime,String eTime);
	/**
	 * 供车单位添加临时价格设置
	 * @author xx
	 * @date 20200426
 	 * @param tcId 包车价格记录id
 	 * @param account 添加账号
 	 * @param carSeats 座位数
 	 * @param expireTime 过期时间
 	 * @param tempPrice 公里数/价格,...
 	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addTempPirice(ReqSrc reqsrc,String tcId,String uname,String carSeats,String expireTime,String tempPrice);
	/**
	 * 通过包车区域id和座位数获取已有价格临时价格设置
	 * @author xx
	 * @date 20200427
 	 * @param tcId 包车价格记录id
 	 * @param uname 当前登录账号
 	 * @param seats 座位数
 	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> hasTempById(ReqSrc reqsrc,String uname,String tcId,String seats);
}
