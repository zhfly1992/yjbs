package com.fx.service.order;

import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.order.OrderParam;

public interface OrderParamService extends BaseService<OrderParam, Long> {
	/**
	 * 添加-单程接送-临时订单参数对象
	 * @author xx
	 * @date 20200513
	 * @param reqsrc        请求来源
	 * @param uname 		添加用户名
	 * @param linkPhone 	添加联系电话
	 * @param companyNum 	单位编号
	 * @param backRelNum 	返程关联编号
	 * @param num 			航班编号/车次号
	 * @param isShuttle 	接送类型
	 * @param spoint 		出发地点
	 * @param epoint 		到达地点
	 * @param wpoints 		途径地点
	 * @param gotime 		行程距离
	 * @param flyOrDownTime 行程耗时
	 * @return map{code: 结果状态码, msg: 结果状态码说明, id: 临时订单id}
	 */
	public Map<String, Object> addOrderParam(ReqSrc reqsrc, String uname,String phone, String companyNum, String backRelNum,
			String num, String isShuttle, String spoint, String epoint, String wpoints, String gotime, String flyOrDownTime);
}
