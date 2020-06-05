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
 * 旅游包车临时价格表
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="tourist_temp_price")
public class TouristTempPrice implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 * 座位数
	 */
	@Column(name="car_seats", columnDefinition="int(11) default '0' COMMENT '座位数'")
	private int carSeats;
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


	public int getCarSeats() {
		return carSeats;
	}


	public void setCarSeats(int carSeats) {
		this.carSeats = carSeats;
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


	public Date getAddTime() {
		return addTime;
	}


	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
