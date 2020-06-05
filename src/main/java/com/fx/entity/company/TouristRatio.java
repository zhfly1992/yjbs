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
 * 旅游包车利润比幅度表
 * @author xx
 * @date 20200426
 */
@Entity
public class TouristRatio{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 公里数
	 */
	@Column(name="kilometre", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '公里数'")
	private double kilometre;
	/**
	 * 座位数
	 */
	@Column(name="car_seats", nullable=false, columnDefinition="int(11) default '0' COMMENT '省份名称'")
	private int carSeats;
	/**
	 * 公里数增加幅度
	 */
	@Column(name="markup_ratio",nullable=false, columnDefinition="double(20,2) default 0 COMMENT '公里数增加幅度'")
	private double markupRatio;
	/**
	 * 天数增加幅度
	 */
	@Column(name="day_ratio",nullable=false, columnDefinition="double(20,2) default 0 COMMENT '天数增加幅度'")
	private double dayRatio;
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
	public double getKilometre() {
		return kilometre;
	}
	public void setKilometre(double kilometre) {
		this.kilometre = kilometre;
	}
	public double getMarkupRatio() {
		return markupRatio;
	}
	public void setMarkupRatio(double markupRatio) {
		this.markupRatio = markupRatio;
	}
	/**  
	 * 获取 carSeats  
	 * @return carSeats 
	 */
	public int getCarSeats() {
		return carSeats;
	}
	/**  
	 * 设置 carSeats  
	 * @param carSeats  
	 */
	public void setCarSeats(int carSeats) {
		this.carSeats = carSeats;
	}
	/**  
	 * 获取 dayRatio  
	 * @return dayRatio 
	 */
	public double getDayRatio() {
		return dayRatio;
	}
	/**  
	 * 设置 dayRatio  
	 * @param dayRatio  
	 */
	public void setDayRatio(double dayRatio) {
		this.dayRatio = dayRatio;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}