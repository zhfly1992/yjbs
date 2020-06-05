package com.fx.entity.company;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 旅游包车价格设置
 * @author xx
 * @date 20200426
 */
@Entity
public class TouristCharter{
	public TouristCharter() {}
	public TouristCharter(int carSeats) {
		this.carSeats = carSeats;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 账号
	 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(50) COMMENT '账号'")
	private String uname;
	/**
	 * 地区类型
	 * 0城市 1景点
	 */
	@Column(name="area_type", nullable=false, columnDefinition="int(11) default '0' COMMENT '地区类型:0城市 1景点'")
	private int areaType;
	/**
	 * 地区名称
	 */
	@Column(name="area_name", nullable=false, columnDefinition="varchar(50) COMMENT '地区名称'")
	private String areaName;
	/**
	 * 座位数
	 */
	@Column(name="car_seats", nullable=false, columnDefinition="int(11) default '0' COMMENT '省份名称'")
	private int carSeats;
	/**
	 * 车型油耗:元/公里
	 */
	@Column(name="oil_wear", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '车型油耗:元/公里'")
	private double oilWear;
	/**
	 * 每日服务时间
	 */
	@Column(name="work_hour", nullable=false, columnDefinition="int(11) default '0' COMMENT '省份名称'")
	private int workHour;
	/**
	 * 超时加收:元/小时
	 */
	@Column(name="more_hour_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超出免费公里单价:元/公里'")
	private double moreHourPrice;
	/**
	 * 免费接送公里数
	 */
	@Column(name="free_km", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '免费接送公里数'")
	private double freeKm;
	/**
	 * 超出免费接送公里单价:元/公里
	 */
	@Column(name="more_free_km_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超出免费接送公里单价:元/公里'")
	private double moreFreeKmPrice;
	/**
	 * 超出免费接送公里最少加收（即不足1公里）
	 */
	@Column(name="more_free_km_low_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超出免费接送公里最少加收（即不足1公里）'")
	private double moreFreeKmLowPrice;
	/**
	 * 免费行程公里数
	 */
	@Column(name="free_route_km", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '免费行程公里数'")
	private double freeRouteKm;
	/**
	 * 超出免费行程公里单价:元/公里
	 */
	@Column(name="more_route_km_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超出免费行程公里单价:元/公里'")
	private double moreRouteKmPrice;
	/**
	 * 超出免费行程公里最少加收（即不足1公里）
	 */
	@Column(name="more_route_km_low_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超出免费行程公里最少加收（即不足1公里）'")
	private double moreRouteKmLowPrice;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	/**
	 * 车辆类型 0：大巴车 1：中巴车 2：商务车 4：小车
	 */
	@Column(name="car_type", nullable=false, columnDefinition="int default 0 COMMENT '车辆类型 0：大巴车 1：中巴车 2：商务车 4：小车'")
	private int carType;
	/**
	 * 车辆品牌
	 */
	@Column(name="car_brand", nullable=false, columnDefinition="varchar(50) COMMENT '车辆品牌'")
	private String carBrand;
	/**
	 * 车辆配置
	 */
	@Column(name="car_config", nullable=false, columnDefinition="varchar(50) COMMENT '车辆配置'")
	private String carConfig;
	/**
	 * 车辆型号
	 */
	@Column(name="car_model", nullable=false, columnDefinition="varchar(50) COMMENT '车辆型号'")
	private String carModel;
	/**
	 * 每日最低价格
	 */
	@Column(name="day_low_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '每日最低价格'")
	private double dayLowPrice;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAreaType() {
		return areaType;
	}
	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getCarSeats() {
		return carSeats;
	}
	public void setCarSeats(int carSeats) {
		this.carSeats = carSeats;
	}
	public double getOilWear() {
		return oilWear;
	}
	public void setOilWear(double oilWear) {
		this.oilWear = oilWear;
	}
	public int getWorkHour() {
		return workHour;
	}
	public void setWorkHour(int workHour) {
		this.workHour = workHour;
	}
	public double getMoreHourPrice() {
		return moreHourPrice;
	}
	public void setMoreHourPrice(double moreHourPrice) {
		this.moreHourPrice = moreHourPrice;
	}
	public double getFreeKm() {
		return freeKm;
	}
	public void setFreeKm(double freeKm) {
		this.freeKm = freeKm;
	}
	public double getMoreFreeKmPrice() {
		return moreFreeKmPrice;
	}
	public void setMoreFreeKmPrice(double moreFreeKmPrice) {
		this.moreFreeKmPrice = moreFreeKmPrice;
	}
	public double getMoreFreeKmLowPrice() {
		return moreFreeKmLowPrice;
	}
	public void setMoreFreeKmLowPrice(double moreFreeKmLowPrice) {
		this.moreFreeKmLowPrice = moreFreeKmLowPrice;
	}
	public double getFreeRouteKm() {
		return freeRouteKm;
	}
	public void setFreeRouteKm(double freeRouteKm) {
		this.freeRouteKm = freeRouteKm;
	}
	public double getMoreRouteKmPrice() {
		return moreRouteKmPrice;
	}
	public void setMoreRouteKmPrice(double moreRouteKmPrice) {
		this.moreRouteKmPrice = moreRouteKmPrice;
	}
	public double getMoreRouteKmLowPrice() {
		return moreRouteKmLowPrice;
	}
	public void setMoreRouteKmLowPrice(double moreRouteKmLowPrice) {
		this.moreRouteKmLowPrice = moreRouteKmLowPrice;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getCarType() {
		return carType;
	}
	public void setCarType(int carType) {
		this.carType = carType;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCarConfig() {
		return carConfig;
	}
	public void setCarConfig(String carConfig) {
		this.carConfig = carConfig;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public double getDayLowPrice() {
		return dayLowPrice;
	}
	public void setDayLowPrice(double dayLowPrice) {
		this.dayLowPrice = dayLowPrice;
	}
	/**  
	 * 获取 账号  
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}
	
	/**  
	 * 设置 账号  
	 * @param uname 
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	
}