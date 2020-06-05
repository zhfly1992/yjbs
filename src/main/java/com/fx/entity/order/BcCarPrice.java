package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 旅游包车-用户选车价格-临时表
 */
@Entity
@Table(name="bc_car_price")
public class BcCarPrice implements Serializable {
	private static final long serialVersionUID = 7037473048949713332L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 主订单编号 */
	@Column(name="main_order_num", nullable=false, columnDefinition="varchar(50) COMMENT '主订单编号'")
	private String mainOrderNum;
	
	/** 座位数 */
	@Column(name="seat", nullable=false, columnDefinition="int(11) default 0 COMMENT '座位数'")
	private int seat;
	
	/** 不开发票总价格 */
	@Column(name="price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '不开发票总价格'")
	private double price;
	
	/** 开发票总价格 */
	@Column(name="bill_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '开发票总价格'")
	private double billPrice;
	
	/** 车辆描述（类型、配置） */
	@Column(name="car_explain", nullable=false, columnDefinition="varchar(20) COMMENT '车辆描述'")
	private String carExplain;
	
	/** 价格详情 eg：[第几程=不开发票价格=开发票价格;]1=100=200;2=200=300; */
	@Column(name="price_detail", nullable=false, columnDefinition="text COMMENT '价格详情'")
	private String priceDetail;

	
	public BcCarPrice() {}
	
	public BcCarPrice(String mainOrderNum, int seat, double price, 
		double billPrice, String carExplain, String priceDetail) {
		super();
		this.mainOrderNum = mainOrderNum;
		this.seat = seat;
		this.price = price;
		this.billPrice = billPrice;
		this.carExplain = carExplain;
		this.priceDetail = priceDetail;
	}

	/**  
	 * 获取 id  
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**  
	 * 设置 id  
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**  
	 * 获取 主订单编号  
	 * @return mainOrderNum
	 */
	public String getMainOrderNum() {
		return mainOrderNum;
	}

	/**  
	 * 设置 主订单编号  
	 * @param mainOrderNum
	 */
	public void setMainOrderNum(String mainOrderNum) {
		this.mainOrderNum = mainOrderNum;
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
	 * 获取 不开发票总价格  
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**  
	 * 设置 不开发票总价格  
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**  
	 * 获取 开发票总价格  
	 * @return billPrice
	 */
	public double getBillPrice() {
		return billPrice;
	}

	/**  
	 * 设置 开发票总价格  
	 * @param billPrice
	 */
	public void setBillPrice(double billPrice) {
		this.billPrice = billPrice;
	}

	/**  
	 * 获取 车辆描述（类型、配置）  
	 * @return carExplain
	 */
	public String getCarExplain() {
		return carExplain;
	}

	/**  
	 * 设置 车辆描述（类型、配置）  
	 * @param carExplain
	 */
	public void setCarExplain(String carExplain) {
		this.carExplain = carExplain;
	}

	/**  
	 * 获取 价格详情eg：[第几程=不开发票价格=开发票价格;]1=100=200;2=200=300;  
	 * @return priceDetail
	 */
	public String getPriceDetail() {
		return priceDetail;
	}

	/**  
	 * 设置 价格详情eg：[第几程=不开发票价格=开发票价格;]1=100=200;2=200=300;  
	 * @param priceDetail
	 */
	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
