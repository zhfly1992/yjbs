package com.fx.entity.back;

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
 * 车辆生产厂家表
 * @author xx
 * @Date 20200507
 */
@Entity
@Table(name="car_company")
public class CarCompany {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 车辆生产厂家 */
	@Column(name="car_company",nullable=false, columnDefinition="varchar(20) COMMENT '车辆生产厂家'")
	private String carCompany;
	
	/** 编号 */
	@Column(name="company_no",nullable=false, columnDefinition="int default 0 COMMENT '编号'")
	private int companyNo;
	
	/** 厂家生产的车辆类型  通过逗号链接 */
	@Column(name="car_type",nullable=false, columnDefinition="varchar(20) COMMENT '厂家生产的车辆类型'")
	private String carType;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time",nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
	public CarCompany() {}
	public CarCompany(String carCompany, int companyNo) {
		this.carCompany = carCompany;
		this.companyNo = companyNo;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCarCompany() {
		return carCompany;
	}
	public void setCarCompany(String carCompany) {
		this.carCompany = carCompany;
	}
	public int getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(int companyNo) {
		this.companyNo = companyNo;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
} 
