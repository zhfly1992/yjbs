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
 * 价格阶梯列表
 */
@Entity
@Table(name="price_stair")
public class PriceStair implements Serializable {
	private static final long serialVersionUID = 6580024299870737671L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 行程ID
	 */
	@Column(name="route_id", columnDefinition="bigint(20) COMMENT '对应行程ID'")
	private long routeId;
	/**
	 * 价格类型
	 * -1人数  大车（包含0大巴车、1中巴车、2商务车） 4小车
	 */
	@Column(name="price_type", columnDefinition="int default 0 COMMENT '价格类型  -1人数  大车（包含0大巴车、1中巴车、2商务车） 4小车'")
	private int priceType;
	/**
	 * 车辆配置(仅价格类型为1/2时需要使用此字段)
	 */
	@Column(name="car_config", columnDefinition="varchar(25) COMMENT '车辆配置(仅价格类型为1/时需要使用此字段)'")
	private String carConfig;
	/**
	 * 车辆品牌(仅价格类型为2时需要使用此字段)
	 */
	@Column(name="car_brand", columnDefinition="varchar(25) COMMENT '车辆品牌(仅价格类型为2时需要使用此字段)'")
	private String carBrand;
	/**
	 * 车辆型号(仅价格类型为2时需要使用此字段)
	 */
	@Column(name="car_model", columnDefinition="varchar(25) COMMENT '车辆型号(仅价格类型为2时需要使用此字段)'")
	private String carModel;
	/**
	 * 价格类型为0代表人数
	 * 价格类型不为0代表座位数
	 */
	@Column(name="type_count", columnDefinition="int default 0 COMMENT '价格类型为0：人数  价格类型不为0：座位数'")
	private int typeCount;
	/**
	 * 价格类型为0代表每人优惠后价格
	 * 价格类型不为0代表正常价格
	 */
	@Column(name="price", nullable=false, columnDefinition="double default 0 COMMENT '价格类型为0：优惠价格 价格类型不为0：正常价格'")
	private double price;
	/**
	 * 超出范围每车每公里加价
	 */
	@Column(name="beyond_price", columnDefinition="double default 0 COMMENT '每车每公里需加价'")
	private double beyondPrice;
	/**
	 * 超范围最低加收金额
	 */
	@Column(name="beyond_minadd_money", columnDefinition="double default 0 COMMENT '超范围最低加收金额'")
	private double beyondMinaddMoney;
	/**
	 * 加班时段格式：8:00-23:00
	 * 开始时间大于结束时间表示第二天，反之当天
	 */
	@Column(name="over_time", columnDefinition="varchar(25) default '0' COMMENT '加班时间段格式：8:00-23:00 开始时间大于结束时间表示第二,反之当天'")
	private String overTime;
	/**
	 * 行程类型为2：加班时段每车增加价格
	 */
	@Column(name="over_time_price", columnDefinition="double default 0 COMMENT '行程类型为2：每车加价'")
	private double overTimePrice;
	/**
	 * 行程类型为2：同行加班时段每车增加价格
	 */
	@Column(name="peer_over_time_price", columnDefinition="double default 0 COMMENT '行程类型为2：同行每车加价'")
	private double peerOverTimePrice;
	/**
	 * 行程类型为2：增加途经点加价（元/个）
	 */
	@Column(name="add_waypoint_price", columnDefinition="double default 0 COMMENT '行程类型为2：增加途经点加价（元/个）'")
	private double addWaypointPrice;
	/**
	 * 行程类型为2：途经点超公里数单价（元/公里）
	 */
	@Column(name="waypoint_overkm_price", columnDefinition="double default 0 COMMENT '行程类型为2：途经点超公里数单价（元/公里）'")
	private double waypointOverkmPrice;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
	public PriceStair() {}
	public PriceStair(int typeCount) {
		super();
		this.typeCount = typeCount;
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
	 * 获取行程ID  
	 * @return routeId 行程ID  
	 */
	public long getRouteId() {
		return routeId;
	}
	/**  
	 * 设置行程ID  
	 * @param routeId 行程ID  
	 */
	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}
	
	/**  
	 * 获取价格类型-1人数大车（包含0大巴车、1中巴车、2商务车）4小车  
	 * @return priceType 价格类型-1人数大车（包含0大巴车、1中巴车、2商务车）4小车  
	 */
	public int getPriceType() {
		return priceType;
	}
	/**  
	 * 设置价格类型-1人数大车（包含0大巴车、1中巴车、2商务车）4小车  
	 * @param priceType 价格类型-1人数大车（包含0大巴车、1中巴车、2商务车）4小车  
	 */
	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}
	/**  
	 * 获取车辆配置(仅价格类型为1/2时需要使用此字段)  
	 * @return carConfig 车辆配置(仅价格类型为1/2时需要使用此字段)  
	 */
	public String getCarConfig() {
		return carConfig;
	}
	/**  
	 * 设置车辆配置(仅价格类型为12时需要使用此字段)  
	 * @param carConfig 车辆配置(仅价格类型为12时需要使用此字段)  
	 */
	public void setCarConfig(String carConfig) {
		this.carConfig = carConfig;
	}
	/**  
	 * 获取车辆品牌(仅价格类型为2时需要使用此字段)  
	 * @return carBrand 车辆品牌(仅价格类型为2时需要使用此字段)  
	 */
	public String getCarBrand() {
		return carBrand;
	}
	/**  
	 * 设置车辆品牌(仅价格类型为2时需要使用此字段)  
	 * @param carBrand 车辆品牌(仅价格类型为2时需要使用此字段)  
	 */
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	/**  
	 * 获取车辆型号(仅价格类型为2时需要使用此字段)  
	 * @return carModel 车辆型号(仅价格类型为2时需要使用此字段)  
	 */
	public String getCarModel() {
		return carModel;
	}
	/**  
	 * 设置车辆型号(仅价格类型为2时需要使用此字段)  
	 * @param carModel 车辆型号(仅价格类型为2时需要使用此字段)  
	 */
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	/**  
	 * 获取价格类型为0代表人数价格类型不为0代表座位数  
	 * @return typeCount 价格类型为0代表人数价格类型不为0代表座位数  
	 */
	public int getTypeCount() {
		return typeCount;
	}
	/**  
	 * 设置价格类型为0代表人数价格类型不为0代表座位数  
	 * @param typeCount 价格类型为0代表人数价格类型不为0代表座位数  
	 */
	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
	/**  
	 * 获取价格类型为0代表每人优惠价格价格类型不为0代表正常价格  
	 * @return price 价格类型为0代表每人优惠价格价格类型不为0代表正常价格  
	 */
	public double getPrice() {
		return price;
	}
	/**  
	 * 设置价格类型为0代表每人优惠价格价格类型不为0代表正常价格  
	 * @param price 价格类型为0代表每人优惠价格价格类型不为0代表正常价格  
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**  
	 * 获取超出范围每车每公里加价  
	 * @return beyondPrice 超出范围每车每公里加价  
	 */
	public double getBeyondPrice() {
		return beyondPrice;
	}
	/**  
	 * 设置超出范围每车每公里加价  
	 * @param beyondPrice 超出范围每车每公里加价  
	 */
	public void setBeyondPrice(double beyondPrice) {
		this.beyondPrice = beyondPrice;
	}
	/**  
	 * 获取超范围最低加收金额  
	 * @return beyondMinaddMoney 超范围最低加收金额  
	 */
	public double getBeyondMinaddMoney() {
		return beyondMinaddMoney;
	}
	/**  
	 * 设置超范围最低加收金额  
	 * @param beyondMinaddMoney 超范围最低加收金额  
	 */
	public void setBeyondMinaddMoney(double beyondMinaddMoney) {
		this.beyondMinaddMoney = beyondMinaddMoney;
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
	 * 获取行程类型为2：加班时段每车增加价格  
	 * @return overTimePrice 行程类型为2：加班时段每车增加价格  
	 */
	public double getOverTimePrice() {
		return overTimePrice;
	}
	/**  
	 * 设置行程类型为2：加班时段每车增加价格  
	 * @param overTimePrice 行程类型为2：加班时段每车增加价格  
	 */
	public void setOverTimePrice(double overTimePrice) {
		this.overTimePrice = overTimePrice;
	}
	/**  
	 * 获取行程类型为2：同行加班时段每车增加价格  
	 * @return peerOverTimePrice 行程类型为2：同行加班时段每车增加价格  
	 */
	public double getPeerOverTimePrice() {
		return peerOverTimePrice;
	}
	/**  
	 * 设置行程类型为2：同行加班时段每车增加价格  
	 * @param peerOverTimePrice 行程类型为2：同行加班时段每车增加价格  
	 */
	public void setPeerOverTimePrice(double peerOverTimePrice) {
		this.peerOverTimePrice = peerOverTimePrice;
	}
	/**  
	 * 获取serialVersionUID  
	 * @return serialVersionUID serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**  
	 * 获取 行程类型为2：增加途经点加价（元个）  
	 * @return addWaypointPrice  
	 */
	public double getAddWaypointPrice() {
		return addWaypointPrice;
	}
	/**  
	 * 设置 行程类型为2：增加途经点加价（元个）  
	 * @param addWaypointPrice
	 */
	public void setAddWaypointPrice(double addWaypointPrice) {
		this.addWaypointPrice = addWaypointPrice;
	}
	/**  
	 * 获取 行程类型为2：途经点超的公里数单价（元公里）  
	 * @return waypointOverkmPrice  
	 */
	public double getWaypointOverkmPrice() {
		return waypointOverkmPrice;
	}
	/**  
	 * 设置 行程类型为2：途经点超的公里数单价（元公里）  
	 * @param waypointOverkmPrice
	 */
	public void setWaypointOverkmPrice(double waypointOverkmPrice) {
		this.waypointOverkmPrice = waypointOverkmPrice;
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
} 