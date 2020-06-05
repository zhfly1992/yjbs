package com.fx.dao.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.tools.U;
import com.fx.entity.order.OrderParam;

@Repository
public class OrderParamDao extends ZBaseDaoImpl<OrderParam, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	
	/**
	 * 添加-单程接送-临时订单参数对象
	 * @param backRelNum 	返程关联编号
	 * @param useCase 		优惠券使用场景
	 * @param companyNum 	单位编号
	 * @param serviceType 	业务类型
	 * @param uname 		添加用户名
	 * @param linkPhone 	添加联系电话
	 * @param otype 		订单类型
	 * @param isShuttle 	接送类型
	 * @param spoint 		出发地点
	 * @param epoint 		到达地点
	 * @param num 			航班编号/车次号
	 * @param wpoints 		途径地点
	 * @param distance 		行程距离
	 * @param routeTime 	行程耗时
	 * @param stime 		出发时间
	 * @param etime 		到达时间
	 * @param fdtime 		起飞/降落/出站/进站时间
	 * @param routeDetail 	行程详情
	 */
	public OrderParam addOrderParam(String backRelNum, int useCase, String companyNum, ServiceType serviceType, 
		String uname, String linkPhone, int otype, int isShuttle, String spoint, String epoint, 
		String num, String wpoints, double distance, int routeTime, Date stime, Date etime, 
		Date fdtime, String routeDetail) {
		String logtxt = U.log(log, "添加-发布订单参数");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		OrderParam op = null;
		
		try {
			if(fg){
				// 先删除之前的订单参数
				hql = "delete from OrderParam where uname = ? and companyNum = ?";
				int c = batchExecute(hql, uname, companyNum);
				U.log(log, "删除之前的订单参数"+c+"条");
			}
			
			if(fg){
				op = new OrderParam();
				op.setUname(uname);
				op.setLinkPhone(linkPhone);
				op.setCompanyNum(companyNum);
				op.setServiceType(serviceType);
				op.setStime(stime);
				op.setEtime(etime);
				op.setSpoint(spoint);
				op.setEpoint(epoint);
				op.setWpoints(wpoints);
				op.setOtype(otype);
				op.setIsShuttle(isShuttle);
				op.setFlightNum(num);
				op.setFdtime(fdtime);
				op.setDistance(distance);
				op.setRouteTime(routeTime);
				op.setUseCase(useCase);
				op.setBackRelNum(backRelNum);
				op.setRouteDetail(routeDetail);
				save(op);
				
				U.setPut(map, 1, "添加成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return op;
	}
	
}
