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
 * 旅游包车平均值数据
 * @author xx
 * @date 20200426
 */
@Entity
public class TouristAverage{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 地区名称
	 */
	@Column(name="area_name",columnDefinition="varchar(50) COMMENT '地区名称'")
	private String areaName;
	/**
	 * 座位数
	 */
	@Column(name="car_seats", columnDefinition="int(11) default '0' COMMENT '座位数'")
	private int carSeats;
	/**
	 * 每日公里数
	 */
	@Column(name="day_km", columnDefinition="double(20,2) default 0 COMMENT '每日公里数'")
	private double dayKm;
	/**
	 * 有效开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time", columnDefinition="datetime COMMENT '有效开始时间'")
	private Date startTime;
	/**
	 * 有效结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time", columnDefinition="datetime COMMENT '有效结束时间'")
	private Date endTime;
	/**
	 * 平均油耗
	 */
	@Column(name="average_oil", columnDefinition="varchar(20) COMMENT '平均油耗'")
	private double averageOil;
	/**
	 * 每日均价
	 */
	@Column(name="average_price", columnDefinition="int(11) default '0' COMMENT '每日均价'")
	private double averagePrice;
	/**
	 * 平均免费公里数
	 */
	@Column(name="average_free_km", columnDefinition="int(11) default '0' COMMENT '平均免费公里数'")
	private double averageFreeKm;
	/**
	 * 超范围公里数平均单价
	 */
	@Column(name="average_free_km_price",columnDefinition="varchar(20) COMMENT '超范围公里数平均单价'")
	private double averageFreeKmPrice;
	/**
	 * 节假日平均浮动比例
	 */
	@Column(name="average_feast_ratio", columnDefinition="varchar(20) COMMENT '节假日平均浮动比例'")
	private double averageFeastRatio;
	/**
	 * 最后一次修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time",columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public double getDayKm() {
		return dayKm;
	}
	public void setDayKm(double dayKm) {
		this.dayKm = dayKm;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public double getAverageOil() {
		return averageOil;
	}
	public void setAverageOil(double averageOil) {
		this.averageOil = averageOil;
	}
	public double getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}
	public double getAverageFreeKm() {
		return averageFreeKm;
	}
	public void setAverageFreeKm(double averageFreeKm) {
		this.averageFreeKm = averageFreeKm;
	}
	public double getAverageFreeKmPrice() {
		return averageFreeKmPrice;
	}
	public void setAverageFreeKmPrice(double averageFreeKmPrice) {
		this.averageFreeKmPrice = averageFreeKmPrice;
	}
	public double getAverageFeastRatio() {
		return averageFeastRatio;
	}
	public void setAverageFeastRatio(double averageFeastRatio) {
		this.averageFeastRatio = averageFeastRatio;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}