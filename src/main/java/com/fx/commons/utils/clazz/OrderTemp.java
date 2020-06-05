package com.fx.commons.utils.clazz;

import java.io.Serializable;
import java.util.Date;

import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;

/**
 * 派单时选择多个行程临时派单表
 * @author xx
 * @date 20200601
 */
public class OrderTemp implements Serializable {
	private static final long serialVersionUID = -2316103457639994875L;
	
	private String id;
	
	/** 业务类型 */
	private ServiceType serviceType;
	
	/** 单位编号 */
	private String unitNum;
	
	/** 开始时间 */
	private Date stime;
	
	/** 结束时间 */
	private Date etime;
	
	/** 行程类型 */
	private RouteType routeType;
	
	/** 限号 */
	private String limitNum;
	
	/** 起点经纬度 */
	private String sLonLat;
	
	/** 终点经纬度 */
	private String eLonLat;
	
	/** 需求座位数 */
	private int needSeats;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取 id  
	 * @return id
	 */
	public String getId() {
		return id;
	}
	

	/**  
	 * 设置 id  
	 * @param id 
	 */
	public void setId(String id) {
		this.id = id;
	}
	

	/**  
	 * 获取 业务类型  
	 * @return serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	

	/**  
	 * 设置 业务类型  
	 * @param serviceType 
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	

	/**  
	 * 获取 单位编号  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	

	/**  
	 * 设置 单位编号  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	

	/**  
	 * 获取 开始时间  
	 * @return stime
	 */
	public Date getStime() {
		return stime;
	}
	

	/**  
	 * 设置 开始时间  
	 * @param stime 
	 */
	public void setStime(Date stime) {
		this.stime = stime;
	}
	

	/**  
	 * 获取 结束时间  
	 * @return etime
	 */
	public Date getEtime() {
		return etime;
	}
	

	/**  
	 * 设置 结束时间  
	 * @param etime 
	 */
	public void setEtime(Date etime) {
		this.etime = etime;
	}
	

	/**  
	 * 获取 行程类型  
	 * @return routeType
	 */
	public RouteType getRouteType() {
		return routeType;
	}
	

	/**  
	 * 设置 行程类型  
	 * @param routeType 
	 */
	public void setRouteType(RouteType routeType) {
		this.routeType = routeType;
	}
	

	/**  
	 * 获取 限号  
	 * @return limitNum
	 */
	public String getLimitNum() {
		return limitNum;
	}
	

	/**  
	 * 设置 限号  
	 * @param limitNum 
	 */
	public void setLimitNum(String limitNum) {
		this.limitNum = limitNum;
	}
	

	/**  
	 * 获取 起点经纬度  
	 * @return sLonLat
	 */
	public String getsLonLat() {
		return sLonLat;
	}
	

	/**  
	 * 设置 起点经纬度  
	 * @param sLonLat 
	 */
	public void setsLonLat(String sLonLat) {
		this.sLonLat = sLonLat;
	}

	/**  
	 * 获取 终点经纬度  
	 * @return eLonLat
	 */
	public String geteLonLat() {
		return eLonLat;
	}
	

	/**  
	 * 设置 终点经纬度  
	 * @param eLonLat 
	 */
	public void seteLonLat(String eLonLat) {
		this.eLonLat = eLonLat;
	}

	/**  
	 * 获取 需求座位数  
	 * @return needSeats
	 */
	public int getNeedSeats() {
		return needSeats;
	}
	

	/**  
	 * 设置 需求座位数  
	 * @param needSeats 
	 */
	public void setNeedSeats(int needSeats) {
		this.needSeats = needSeats;
	}
	
	

	
	
	
}
