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
 * 旅游包车每日价格数据
 * @author xx
 * @date 20200426
 */
@Entity
public class TouristDayPrice{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 包车设置id
	 */
	@Column(name="touc_id", nullable=false, columnDefinition="varchar(20) COMMENT '包车设置id'")
	private String toucId;
	/**
	 * 账号
	 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(50) COMMENT '账号'")
	private String uname;
	/**
	 * 地区名称
	 */
	@Column(name="area_name",columnDefinition="varchar(50) COMMENT '地区名称'")
	private String areaName;
	/**
	 * 座位数
	 */
	@Column(name="car_seats", nullable=false, columnDefinition="int(11) default '0' COMMENT '座位数'")
	private int carSeats;
	/**
	 * 每日公里数
	 */
	@Column(name="day_km", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '每日公里数'")
	private double dayKm;
	/**
	 * 价格
	 */
	@Column(name="day_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '价格'")
	private double dayPrice;
	/**
	 * 有效开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time", nullable=false, columnDefinition="datetime COMMENT '有效开始时间'")
	private Date startTime;
	/**
	 * 有效结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time", nullable=false, columnDefinition="datetime COMMENT '有效结束时间'")
	private Date endTime;
	/**
	 * 节假日上浮/下调比例
	 */
	@Column(name="feast_ratio", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '节假日上浮/下调比例'")
	private double feastRatio;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getToucId() {
		return toucId;
	}
	public void setToucId(String toucId) {
		this.toucId = toucId;
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
	public double getDayPrice() {
		return dayPrice;
	}
	public void setDayPrice(double dayPrice) {
		this.dayPrice = dayPrice;
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
	public double getFeastRatio() {
		return feastRatio;
	}
	public void setFeastRatio(double feastRatio) {
		this.feastRatio = feastRatio;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}