package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单优惠详情
 */
@Entity
@Table(name="discount_detail")
public class DiscountDetail implements Serializable {
	private static final long serialVersionUID = -4644801632626452283L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/**
	 * 订单号
	 */
	@Column(name="order_num", columnDefinition="varchar(30) COMMENT '订单号'")
	private String orderNum;
	/**
	 * 优惠券抵扣金额
	 */
	@Column(name="coupon_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '优惠券抵扣金额'")
	private double couponMoney;
	/**
	 * 使用优惠券id
	 */
	@Column(name="coupon_id", columnDefinition="bigint default 0 COMMENT '使用优惠券id'")
	private long couponId;
	/**
	 * 同行优惠抵扣金额
	 */
	@Column(name="peer_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '同行优惠抵扣金额'")
	private double peerMoney;
	/**
	 * 加班金额
	 */
	@Column(name="over_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '加班金额'")
	private double overMoney;
	/**
	 * 超范围金额
	 */
	@Column(name="beyond_range_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超范围金额'")
	private double beyondRangeMoney;
	/**
	 * 增加途经点金额
	 */
	@Column(name="add_point_money", columnDefinition="double(20,2) default 0 COMMENT '增加途经点金额'")
	private double addPointMoney;
	/**
	 * 乘客报价
	 */
	@Column(name="cus_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '乘客报价'")
	private double cusPrice;
	/**
	 * 支付金额
	 */
	@Column(name="pay_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '支付金额'")
	private double payMoney;
	
	
	
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
	 * 获取 订单号  
	 * @return orderNum  
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**  
	 * 设置 订单号  
	 * @param orderNum
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**  
	 * 获取 优惠券抵扣金额  
	 * @return couponMoney  
	 */
	public double getCouponMoney() {
		return couponMoney;
	}
	/**  
	 * 设置 优惠券抵扣金额  
	 * @param couponMoney
	 */
	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}
	/**
	 * 获取 优惠券id
	 * @return couponId
	 */
	public long getCouponId() {
		return couponId;
	}
	/**
	 * 设置 优惠券id
	 * @param couponId
	 */
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
	/**  
	 * 获取 同行优惠抵扣金额  
	 * @return peerMoney  
	 */
	public double getPeerMoney() {
		return peerMoney;
	}
	/**  
	 * 设置 同行优惠抵扣金额  
	 * @param peerMoney
	 */
	public void setPeerMoney(double peerMoney) {
		this.peerMoney = peerMoney;
	}
	/**  
	 * 获取 加班金额  
	 * @return overMoney  
	 */
	public double getOverMoney() {
		return overMoney;
	}
	/**  
	 * 设置 加班金额  
	 * @param overMoney
	 */
	public void setOverMoney(double overMoney) {
		this.overMoney = overMoney;
	}
	/**  
	 * 获取 超范围金额  
	 * @return beyondRangeMoney  
	 */
	public double getBeyondRangeMoney() {
		return beyondRangeMoney;
	}
	/**  
	 * 设置 超范围金额  
	 * @param beyondRangeMoney
	 */
	public void setBeyondRangeMoney(double beyondRangeMoney) {
		this.beyondRangeMoney = beyondRangeMoney;
	}
	/**  
	 * 获取 增加途经点金额  
	 * @return addPointMoney  
	 */
	public double getAddPointMoney() {
		return addPointMoney;
	}
	/**  
	 * 设置 增加途经点金额  
	 * @param addPointMoney
	 */
	public void setAddPointMoney(double addPointMoney) {
		this.addPointMoney = addPointMoney;
	}
	/**  
	 * 获取 乘客报价  
	 * @return cusPrice  
	 */
	public double getCusPrice() {
		return cusPrice;
	}
	/**  
	 * 设置 乘客报价  
	 * @param cusPrice
	 */
	public void setCusPrice(double cusPrice) {
		this.cusPrice = cusPrice;
	}
	/**  
	 * 获取 支付金额  
	 * @return payMoney  
	 */
	public double getPayMoney() {
		return payMoney;
	}
	/**  
	 * 设置 支付金额  
	 * @param payMoney
	 */
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
