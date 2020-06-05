package com.fx.entity.finance;

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
 * 车辆上网预付费记录（车公司应收款）
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="online_prepayment")
public class OnlinePrepayment implements Serializable{
	private static final long serialVersionUID = -4184812063515072970L;
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	/**
	 * 订单号
	 */
	@Column(name="order_num", nullable=false, columnDefinition="varchar(20) COMMENT '订单号'")
	private String orderNum;
	/**
	 * 车公司
	 */
	@Column(name="company", nullable=false, columnDefinition="varchar(200) COMMENT '车公司'")
	private String company;
	/**
	 * 车牌号
	 */
	@Column(name="plate_num", nullable=false, columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	/**
	 * 应收总价
	 */
	@Column(name="total_price", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '应收总价'")
	private double totalPrice;
	/**
	 * 已收金额
	 */
	@Column(name="gath_price", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '已收金额'")
	private double gathPrice;
	/**
	 * 税点金额
	 */
	@Column(name="prepay_ratio_price", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '税点金额'")
	private double prepayRatioPrice;
	/**
	 * 付款时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="pay_time", columnDefinition="datetime COMMENT '付款时间'")
	private Date payTime;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	/**
	 * 备注
	 */
	@Column(name="remark",columnDefinition="varchar(255) COMMENT '备注'")
	private String remark;
	/**
	 * 0自网 1旅网
	 */
	@Column(name="online_type",nullable=false,columnDefinition="int default 0 COMMENT '0自网 1旅网'")
	private int onlineType;
	/**
	 * 上网费是否确认
	 * 0未确认 1已确认
	 */
	@Column(name="is_check_online",nullable=false,columnDefinition="int default 0 COMMENT '上网费是否确认 0未确认 1已确认'")
	private int isCheckOnline;
	/**
	 * 凭证图片下载地址
	 */
	@Column(name="online_voucher_url", columnDefinition="varchar(255) COMMENT '凭证图片下载地址'")
	private String onlineVoucherUrl;
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
	 * 获取 单位编号eg：Uxxxx  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	
	/**  
	 * 设置 单位编号eg：Uxxxx  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
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
	 * 获取 车公司  
	 * @return company
	 */
	public String getCompany() {
		return company;
	}
	
	/**  
	 * 设置 车公司  
	 * @param company 
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	
	/**  
	 * 获取 车牌号  
	 * @return plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	
	/**  
	 * 设置 车牌号  
	 * @param plateNum 
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	
	/**  
	 * 获取 应收总价  
	 * @return totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}
	
	/**  
	 * 设置 应收总价  
	 * @param totalPrice 
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	/**  
	 * 获取 已收金额  
	 * @return gathPrice
	 */
	public double getGathPrice() {
		return gathPrice;
	}
	
	/**  
	 * 设置 已收金额  
	 * @param gathPrice 
	 */
	public void setGathPrice(double gathPrice) {
		this.gathPrice = gathPrice;
	}
	
	/**  
	 * 获取 税点金额  
	 * @return prepayRatioPrice
	 */
	public double getPrepayRatioPrice() {
		return prepayRatioPrice;
	}
	
	/**  
	 * 设置 税点金额  
	 * @param prepayRatioPrice 
	 */
	public void setPrepayRatioPrice(double prepayRatioPrice) {
		this.prepayRatioPrice = prepayRatioPrice;
	}
	
	/**  
	 * 获取 付款时间  
	 * @return payTime
	 */
	public Date getPayTime() {
		return payTime;
	}
	
	/**  
	 * 设置 付款时间  
	 * @param payTime 
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	/**  
	 * 获取 添加时间  
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}
	
	/**  
	 * 设置 添加时间  
	 * @param addTime 
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	/**  
	 * 获取 备注  
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}
	
	/**  
	 * 设置 备注  
	 * @param remark 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**  
	 * 获取 0自网1旅网  
	 * @return onlineType
	 */
	public int getOnlineType() {
		return onlineType;
	}
	
	/**  
	 * 设置 0自网1旅网  
	 * @param onlineType 
	 */
	public void setOnlineType(int onlineType) {
		this.onlineType = onlineType;
	}
	
	/**  
	 * 获取 上网费是否确认0未确认1已确认  
	 * @return isCheckOnline
	 */
	public int getIsCheckOnline() {
		return isCheckOnline;
	}
	
	/**  
	 * 设置 上网费是否确认0未确认1已确认  
	 * @param isCheckOnline 
	 */
	public void setIsCheckOnline(int isCheckOnline) {
		this.isCheckOnline = isCheckOnline;
	}
	
	/**  
	 * 获取 凭证图片下载地址  
	 * @return onlineVoucherUrl
	 */
	public String getOnlineVoucherUrl() {
		return onlineVoucherUrl;
	}
	
	/**  
	 * 设置 凭证图片下载地址  
	 * @param onlineVoucherUrl 
	 */
	public void setOnlineVoucherUrl(String onlineVoucherUrl) {
		this.onlineVoucherUrl = onlineVoucherUrl;
	}
	
	
	
}