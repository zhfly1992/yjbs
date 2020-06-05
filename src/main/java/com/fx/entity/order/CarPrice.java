package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单程接送-用户选车价格-临时表
 */
@Entity
@Table(name="car_price")
public class CarPrice implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 订单参数对象id */
	@Column(name="opid", nullable=false, columnDefinition="bigint COMMENT '订单参数对象id'")
	private long opid;
	
	/** 优惠详情对象json字符串 */
	@Column(name="dis_json", columnDefinition="text COMMENT '优惠详情对象json字符串'")
	private String disJson;
	
	/** 操作账户 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(50) COMMENT '操作账户'")
	private String uname;
	
	/** 价格阶梯id */
	@Column(name="psid", nullable=false, columnDefinition="bigint COMMENT '价格阶梯id'")
	private long psid;
	
	/** 车辆类型[0-大巴, 1-中巴, 2-商务, 4-小车] */
	@Column(name="car_type", nullable=false, columnDefinition="int(11) default -1 COMMENT '车辆类型'")
	private int carType;
	
	/** 车辆配置 */
	@Column(name="config", nullable=false, columnDefinition="varchar(30) COMMENT '车辆配置'")
	private String config;
	
	/** 座位数 */
	@Column(name="seat", nullable=false, columnDefinition="int(11) default 0 COMMENT '座位数'")
	private int seat;
	
	/** 价格 */
	@Column(name="price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '价格'")
	private double price;
	

	/**
	 * 获取 id
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置 key
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * 获取 订单参数对象id
	 * @return opid
	 */
	public long getOpid() {
		return opid;
	}

	/**
	 * 获取 优惠详情对象id
	 * @return disJson
	 */
	public String getDisJson() {
		return disJson;
	}

	/**
	 * 设置 优惠详情对象id
	 * @param disJson
	 */
	public void setDisJson(String disJson) {
		this.disJson = disJson;
	}

	/**
	 * 设置 订单参数对象id
	 * @param opid
	 */
	public void setOpid(long opid) {
		this.opid = opid;
	}

	/**
	 * 获取 操作账户
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}

	/**
	 * 设置 操作账户
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	/**
	 * 获取 价格阶梯id
	 * @return psid
	 */
	public long getPsid() {
		return psid;
	}

	/**
	 * 设置 价格阶梯id
	 * @param psid
	 */
	public void setPsid(long psid) {
		this.psid = psid;
	}

	/**
	 * 获取 车辆类型[0-大巴, 1-中巴, 2-商务, 4-小车]
	 * @return carType
	 */
	public int getCarType() {
		return carType;
	}

	/**
	 * 获取 车辆类型[0-大巴, 1-中巴, 2-商务, 4-小车]
	 * @param carType
	 */
	public void setCarType(int carType) {
		this.carType = carType;
	}

	/**
	 * 获取 车辆配置
	 * @return config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * 设置 车辆配置
	 * @param config
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * 获取 座位数
	 * @return seat
	 */
	public int getSeat() {
		return seat;
	}

	/**
	 * 设置 座位数
	 * @param seat
	 */
	public void setSeat(int seat) {
		this.seat = seat;
	}

	/**
	 * 获取 车价
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * 设置 车价
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
