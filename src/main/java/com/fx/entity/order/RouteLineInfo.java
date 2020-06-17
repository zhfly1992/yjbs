package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 行程线路信息表
 */
@Entity
@Table(name="route_line_info")
public class RouteLineInfo implements Serializable,Cloneable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 行程天数 默认=到达日期-出发日期+1 */
	@Column(name="day_num", nullable=false, columnDefinition="int(11) default 0 COMMENT '行程天数'")
	private int dayNum;
	
	/** 起止行程距离（公里） */
	@Column(name="distance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '起止行程距离（公里）'")
	private double distance;
	
	/** 起止行程距离耗时（分钟） */
	@Column(name="route_time", nullable=false, columnDefinition="int(11) default 0 COMMENT '起止行程距离耗时（分钟）'")
	private int routeTime;

	/** 过路费（元） */
	@Column(name="tolls", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '过路费（元）'")
	private double tolls;
	
	/** 是否走高速 1-走高速；0-不走高速； */
	@Column(name="is_high_speed", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否走高速'")
	private int isHighSpeed;

	
	/**  
	 * 获取 id  
	 * @return id
	 */
	public long getId() {
		return id;
	}
	

	/**  
	 * 设置 id  
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	

	/**  
	 * 获取 行程天数默认=到达日期-出发日期+1  
	 * @return dayNum
	 */
	public int getDayNum() {
		return dayNum;
	}
	

	/**  
	 * 设置 行程天数默认=到达日期-出发日期+1  
	 * @param dayNum
	 */
	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}
	

	/**  
	 * 获取 起止行程距离（公里）  
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}
	

	/**  
	 * 设置 起止行程距离（公里）  
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	

	/**  
	 * 获取 起止行程距离耗时（分钟）  
	 * @return routeTime
	 */
	public int getRouteTime() {
		return routeTime;
	}
	

	/**  
	 * 设置 起止行程距离耗时（分钟）  
	 * @param routeTime
	 */
	public void setRouteTime(int routeTime) {
		this.routeTime = routeTime;
	}
	

	/**  
	 * 获取 过路费（元）  
	 * @return tolls
	 */
	public double getTolls() {
		return tolls;
	}
	

	/**  
	 * 设置 过路费（元）  
	 * @param tolls
	 */
	public void setTolls(double tolls) {
		this.tolls = tolls;
	}
	

	/**  
	 * 获取 是否走高速1-走高速；0-不走高速；  
	 * @return isHighSpeed
	 */
	public int getIsHighSpeed() {
		return isHighSpeed;
	}
	

	/**  
	 * 设置 是否走高速1-走高速；0-不走高速；  
	 * @param isHighSpeed
	 */
	public void setIsHighSpeed(int isHighSpeed) {
		this.isHighSpeed = isHighSpeed;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	@Override  
    public Object clone() {  
		RouteLineInfo stu = null;  
        try{  
            stu = (RouteLineInfo)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return stu;  
    } 
	
}
