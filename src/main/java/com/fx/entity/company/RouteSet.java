package com.fx.entity.company;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 行程设置表
 */
@Entity
@Table(name="route_set")
public class RouteSet implements Serializable {
	private static final long serialVersionUID = 7580649255854911523L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 对应接送区域ID
	 */
	@Column(name="area_id", columnDefinition="bigint(20) COMMENT '对应接送区域ID'")
	private long areaId;
	/**
	 * 行程类型
	 * 1：单程接送
	 * 2：旅游包车
	 */
	@Column(name="route_type", columnDefinition="int default 1 COMMENT '行程类型'")
	private int routeType;
	/**
	 * 目的地
	 */
	@Column(name="destination", columnDefinition="varchar(25) COMMENT '目的地'")
	private String destination;
	/**
	 * 行程类型为1：?元/人
	 * 行程类型为2：?元/车
	 */
	@Column(name="route_price", columnDefinition="double default 0 COMMENT '行程类型为1：?元/人  行程类型为2：?元/车'")
	private double routePrice;
	/**
	 * 途经点，eg：大邑-20-30-1,邛崃-30-40-2
	 */
	@Column(name="by_way_points", columnDefinition="varchar(200) COMMENT '途经点，eg：大邑-20-30-1,邛崃-30-40-2'")
	private String byWayPoints;
	/**
	 * 同行优惠比例
	 */
	@Column(name="peer_pref_ratio", columnDefinition="double default 0 COMMENT '同行优惠比例'")
	private double peerPrefRatio;
	/**
	 * 行程耗时：单位（分钟）
	 */
	@Column(name="route_minute", nullable=false, columnDefinition="int default 0 COMMENT '行程耗时：单位分钟'")
	private int routeMinute;
	/**
	 * 几人起送(仅超出范围的拼车需要用此字段)
	 */
	@Column(name="min_people", nullable=false, columnDefinition="int default 0 COMMENT '几人起送(仅超出范围的拼车需要用此字段)'")
	private int minPeople;
	/**
	 * 最远距离(仅超出范围的拼车需要用此字段)
	 */
	@Column(name="max_distance", columnDefinition="int default 0 COMMENT '最远距离(仅超出范围的拼车需要用此字段)'")
	private int maxDistance;
	/**
	 * 行程类型为1：超出范围每人每公里需加价
	 */
	@Column(name="beyond_price", columnDefinition="double default 0 COMMENT '车辆类型1：每人每公里需加价;行程类型2：每车每公里需加价'")
	private double beyondPrice;
	/**
	 * 行程类型为2：超范围最低加收金额
	 */
	@Column(name="beyond_minadd_money", columnDefinition="double default 0 COMMENT '行程类型为2：超范围最低加收金额'")
	private double beyondMinaddMoney;
	/**
	 * 加班时段格式：8:00-23:00
	 * 开始时间大于结束时间表示第二天，反之当天
	 */
	@Column(name="over_time", columnDefinition="varchar(25) COMMENT '加班时间段格式：8:00-23:00 开始时间大于结束时间表示第二,反之当天'")
	private String overTime;
	/**
	 * 同行加班时段格式：8:00-23:00
	 * 开始时间大于结束时间表示第二天，反之当天
	 */
	@Column(name="peer_over_time", columnDefinition="varchar(25) COMMENT '同行加班时间段格式：8:00-23:00 开始时间大于结束时间表示第二,反之当天'")
	private String peerOverTime;
	/**
	 * 行程类型为1：加班时段每人增加价格（阶梯价格，人数-加价金额：1-5,2-3,4-1）
	 */
	@Column(name="over_time_price", columnDefinition="varchar(100) COMMENT '行程类型为1：每人加价（阶梯价格，人数-加价金额：1-5,2-3,4-1）;行程类型为2：每车加价'")
	private String overTimePrice;
	/**
	 * 行程类型为1：同行加班时段每人增加价格
	 */
	@Column(name="peer_over_time_price", columnDefinition="double default 0 COMMENT '行程类型为1：每人加价;行程类型为2：每车加价'")
	private double peerOverTimePrice;
	/**
	 * 车辆类型(仅不拼车需要用到此字段)
	 * 0：大巴车 1：中巴车 2：商务车 4：小车
	 */
	@Column(name="car_type", nullable=false, columnDefinition="int default 0 COMMENT '车辆类型 0：大巴车 1：中巴车 2：商务车 4：小车'")
	private int carType;
	/**
	 * 免费等待时间:单位分钟(仅不拼车需要用到此字段)
	 */
	@Column(name="free_time", nullable=false, columnDefinition="int default 0 COMMENT '免费等待时间:单位分钟(仅不拼车需要用到此字段)'")
	private int freeTime;
	/**
	 * 超出免费等待时间加价(仅不拼车需要用到此字段)
	 */
	@Column(name="over_free_time", nullable=false, columnDefinition="double default 0 COMMENT '超出免费等待时间加价(仅不拼车需要用到此字段)'")
	private double overFreeTime;
	/**
	 * 顺风车优惠比例(仅不拼车需要用到此字段)
	 */
	@Column(name="ride_sharing", nullable=false, columnDefinition="double default 0 COMMENT '顺风车优惠比例(仅不拼车需要用到此字段)'")
	private double rideSharing;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	public RouteSet() {}
	public RouteSet(long id) {
		super();
		this.id = id;
	}

	/**  
	 * 获取id  
	 * @return id id  
	 */
	public long getId() {
		return id;
	}
	/**  
	 * 设置id  
	 * @param id id  
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**  
	 * 获取对应接送区域ID  
	 * @return areaId 对应接送区域ID  
	 */
	public long getAreaId() {
		return areaId;
	}
	/**  
	 * 设置对应接送区域ID  
	 * @param areaId 对应接送区域ID  
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	
	/**  
	 * 获取 行程类型1：单程接送2：旅游包车  
	 * @return routeType
	 */
	public int getRouteType() {
		return routeType;
	}
	
	/**  
	 * 设置 行程类型1：单程接送2：旅游包车  
	 * @param routeType
	 */
	public void setRouteType(int routeType) {
		this.routeType = routeType;
	}
	
	/**  
	 * 获取目的地  
	 * @return destination 目的地  
	 */
	public String getDestination() {
		return destination;
	}
	/**  
	 * 设置目的地  
	 * @param destination 目的地  
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**  
	 * 获取行程类型为1：?元人行程类型为2：?元车  
	 * @return routePrice 行程类型为1：?元人行程类型为2：?元车  
	 */
	public double getRoutePrice() {
		return routePrice;
	}
	/**  
	 * 设置行程类型为1：?元人行程类型为2：?元车  
	 * @param routePrice 行程类型为1：?元人行程类型为2：?元车  
	 */
	public void setRoutePrice(double routePrice) {
		this.routePrice = routePrice;
	}
	/**  
	 * 获取途经点，eg：大邑-20-30,邛崃-30-40  
	 * @return byWayPoints 途经点，eg：大邑-20-30,邛崃-30-40  
	 */
	public String getByWayPoints() {
		return byWayPoints;
	}
	/**  
	 * 设置途经点，eg：大邑-20-30-1,邛崃-30-40-2  
	 * @param byWayPoints 途经点，eg：大邑-20-30-1,邛崃-30-40-2  
	 */
	public void setByWayPoints(String byWayPoints) {
		this.byWayPoints = byWayPoints;
	}
	/**  
	 * 获取行程耗时：单位分钟  
	 * @return routeMinute 行程耗时：单位分钟  
	 */
	public int getRouteMinute() {
		return routeMinute;
	}
	/**  
	 * 设置行程耗时：单位分钟  
	 * @param routeMinute 行程耗时：单位分钟  
	 */
	public void setRouteMinute(int routeMinute) {
		this.routeMinute = routeMinute;
	}
	/**  
	 * 获取几人起送(仅超出范围的拼车需要用此字段)  
	 * @return minPeople 几人起送(仅超出范围的拼车需要用此字段)  
	 */
	public int getMinPeople() {
		return minPeople;
	}
	/**  
	 * 设置几人起送(仅超出范围的拼车需要用此字段)  
	 * @param minPeople 几人起送(仅超出范围的拼车需要用此字段)  
	 */
	public void setMinPeople(int minPeople) {
		this.minPeople = minPeople;
	}
	/**  
	 * 获取最远距离(仅超出范围的拼车需要用此字段)  
	 * @return maxDistance 最远距离(仅超出范围的拼车需要用此字段)  
	 */
	public int getMaxDistance() {
		return maxDistance;
	}
	/**  
	 * 设置最远距离(仅超出范围的拼车需要用此字段)  
	 * @param maxDistance 最远距离(仅超出范围的拼车需要用此字段)  
	 */
	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}
	/**  
	 * 获取行程类型为1：超出范围每人每公里需加价行程类型为2：超出范围每车每公里加价  
	 * @return beyondPrice 行程类型为0：超出范围每人每公里需加价行程类型为1：超出范围每车每公里加价  
	 */
	public double getBeyondPrice() {
		return beyondPrice;
	}
	/**  
	 * 设置行程类型为1：超出范围每人每公里需加价行程类型为2：超出范围每车每公里加价  
	 * @param beyondPrice 行程类型为0：超出范围每人每公里需加价行程类型为1：超出范围每车每公里加价  
	 */
	public void setBeyondPrice(double beyondPrice) {
		this.beyondPrice = beyondPrice;
	}
	/**  
	 * 获取加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 * @return overTime 加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 */
	public String getOverTime() {
		return overTime;
	}
	/**  
	 * 设置加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 * @param overTime 加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 */
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	
	/**  
	 * 获取行程类型为1：加班时段每人增加价格（阶梯价格，人数-加价金额：1-5,2-3,4-1）行程类型为2：加班时段每车增加价格  
	 * @return overTimePrice 行程类型为0：加班时段每人增加价格（阶梯价格，人数-加价金额：1-5,2-3,4-1）行程类型为1：加班时段每车增加价格  
	 */
	public String getOverTimePrice() {
		return overTimePrice;
	}
	/**  
	 * 设置行程类型为1：加班时段每人增加价格（阶梯价格，人数-加价金额：1-5,2-3,4-1）行程类型为2：加班时段每车增加价格  
	 * @param overTimePrice 行程类型为0：加班时段每人增加价格（阶梯价格，人数-加价金额：1-5,2-3,4-1）行程类型为1：加班时段每车增加价格  
	 */
	public void setOverTimePrice(String overTimePrice) {
		this.overTimePrice = overTimePrice;
	}
	/**  
	 * 获取serialVersionUID  
	 * @return serialVersionUID serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**  
	 * 获取车辆类型1大车2小车  
	 * @return carType 车辆类型1大车2小车  
	 */
	public int getCarType() {
		return carType;
	}
	/**  
	 * 设置车辆类型1大车2小车  
	 * @param carType 车辆类型1大车2小车  
	 */
	public void setCarType(int carType) {
		this.carType = carType;
	}
	/**  
	 * 获取免费等待时间:单位分钟(仅不拼车需要用到此字段)  
	 * @return freeTime 免费等待时间:单位分钟(仅不拼车需要用到此字段)  
	 */
	public int getFreeTime() {
		return freeTime;
	}
	/**  
	 * 设置免费等待时间:单位分钟(仅不拼车需要用到此字段)  
	 * @param freeTime 免费等待时间:单位分钟(仅不拼车需要用到此字段)  
	 */
	public void setFreeTime(int freeTime) {
		this.freeTime = freeTime;
	}
	
	/**  
	 * 获取超出免费等待时间加价(仅不拼车需要用到此字段)  
	 * @return overFreeTime 超出免费等待时间加价(仅不拼车需要用到此字段)  
	 */
	public double getOverFreeTime() {
		return overFreeTime;
	}
	/**  
	 * 设置超出免费等待时间加价(仅不拼车需要用到此字段)  
	 * @param overFreeTime 超出免费等待时间加价(仅不拼车需要用到此字段)  
	 */
	public void setOverFreeTime(double overFreeTime) {
		this.overFreeTime = overFreeTime;
	}
	/**  
	 * 获取顺风车优惠比例(仅不拼车需要用到此字段)  
	 * @return rideSharing 顺风车优惠比例(仅不拼车需要用到此字段)  
	 */
	public double getRideSharing() {
		return rideSharing;
	}
	/**  
	 * 设置顺风车优惠比例(仅不拼车需要用到此字段)  
	 * @param rideSharing 顺风车优惠比例(仅不拼车需要用到此字段)  
	 */
	public void setRideSharing(double rideSharing) {
		this.rideSharing = rideSharing;
	}
	/**  
	 * 获取添加时间  
	 * @return addTime 添加时间  
	 */
	public Date getAddTime() {
		return addTime;
	}
	/**  
	 * 设置添加时间  
	 * @param addTime 添加时间  
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	/**  
	 * 获取同行优惠比例  
	 * @return peerPrefRatio 同行优惠比例  
	 */
	public double getPeerPrefRatio() {
		return peerPrefRatio;
	}
	/**  
	 * 设置同行优惠比例  
	 * @param peerPrefRatio 同行优惠比例  
	 */
	public void setPeerPrefRatio(double peerPrefRatio) {
		this.peerPrefRatio = peerPrefRatio;
	}
	/**  
	 * 获取同行加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 * @return peerOverTime 同行加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 */
	public String getPeerOverTime() {
		return peerOverTime;
	}
	/**  
	 * 设置同行加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 * @param peerOverTime 同行加班时段格式：8:00-23:00开始时间大于结束时间表示第二天，反之当天  
	 */
	public void setPeerOverTime(String peerOverTime) {
		this.peerOverTime = peerOverTime;
	}
	/**  
	 * 获取行程类型为1：同行加班时段每人增加价格行程类型为2：同行加班时段每车增加价格  
	 * @return peerOverTimePrice 行程类型为1：同行加班时段每人增加价格行程类型为2：同行加班时段每车增加价格  
	 */
	public double getPeerOverTimePrice() {
		return peerOverTimePrice;
	}
	/**  
	 * 设置行程类型为1：同行加班时段每人增加价格行程类型为2：同行加班时段每车增加价格  
	 * @param peerOverTimePrice 行程类型为1：同行加班时段每人增加价格行程类型为2：同行加班时段每车增加价格  
	 */
	public void setPeerOverTimePrice(double peerOverTimePrice) {
		this.peerOverTimePrice = peerOverTimePrice;
	}
	/**  
	 * 获取行程类型为2：超范围最低加收金额  
	 * @return beyondMinaddMoney 行程类型为2：超范围最低加收金额  
	 */
	public double getBeyondMinaddMoney() {
		return beyondMinaddMoney;
	}
	/**  
	 * 设置行程类型为2：超范围最低加收金额  
	 * @param beyondMinaddMoney 行程类型为2：超范围最低加收金额  
	 */
	public void setBeyondMinaddMoney(double beyondMinaddMoney) {
		this.beyondMinaddMoney = beyondMinaddMoney;
	}
} 
