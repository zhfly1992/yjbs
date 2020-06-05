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
 * @author XX
 * @date 20200507
 * 车辆品牌列表
 */
@Entity
@Table(name="car_brand")
public class CarBrand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 车辆所属公司id */
	@Column(name="car_company",nullable=false, columnDefinition="int default 0 COMMENT '车辆所属公司id'")
	private int carCompany;
	
	/** 车辆品牌 */
	@Column(name="car_brand",nullable=false, columnDefinition="varchar(20) COMMENT '车辆品牌'")
	private String carBrand;
	
	/** 车辆配置 */
	@Column(name="car_config",nullable=false, columnDefinition="varchar(20) COMMENT '车辆配置'")
	private String carConfig;
	
	/** 所属车辆类型 */
	@Column(name="car_type",nullable=false, columnDefinition="int default 0 COMMENT '所属车辆类型'")
	private int carType;
	
	/** 热门排序 */
	@Column(name="hot_order", nullable=false, columnDefinition="int default 0 COMMENT '热门排序'")
	private int hotOrder;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
	public CarBrand() {}
	public CarBrand(long id, String carBrand) {
		this.id = id;
		this.carBrand = carBrand;
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
	 * 获取车辆品牌  
	 * @return carCompany 车辆品牌  
	 */
	public int getCarCompany() {
		return carCompany;
	}
	/**  
	 * 设置车辆品牌  
	 * @param carCompany 车辆品牌  
	 */
	public void setCarCompany(int carCompany) {
		this.carCompany = carCompany;
	}
	/**  
	 * 获取车辆型号  
	 * @return carBrand 车辆型号  
	 */
	public String getCarBrand() {
		return carBrand;
	}
	/**  
	 * 设置车辆型号  
	 * @param carBrand 车辆型号  
	 */
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	/**  
	 * 获取车辆配置  
	 * @return carConfig 车辆配置  
	 */
	public String getCarConfig() {
		return carConfig;
	}
	/**  
	 * 设置车辆配置  
	 * @param carConfig 车辆配置  
	 */
	public void setCarConfig(String carConfig) {
		this.carConfig = carConfig;
	}
	/**  
	 * 获取所属车辆类型  
	 * @return carType 所属车辆类型  
	 */
	public int getCarType() {
		return carType;
	}
	/**  
	 * 设置所属车辆类型  
	 * @param carType 所属车辆类型  
	 */
	public void setCarType(int carType) {
		this.carType = carType;
	}
	/**  
	 * 获取热门排序  
	 * @return hotOrder 热门排序  
	 */
	public int getHotOrder() {
		return hotOrder;
	}
	/**  
	 * 设置热门排序  
	 * @param hotOrder 热门排序  
	 */
	public void setHotOrder(int hotOrder) {
		this.hotOrder = hotOrder;
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
