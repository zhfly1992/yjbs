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
 * 油卡、油票、etc卡记录表
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="oil_card_list")
public class OilCardList implements Serializable{
	private static final long serialVersionUID = 3336109024120895114L;
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
	 * 卡类型
	 * 0油卡 1油票 2etc
	 */
	@Column(name="card_type", nullable=false, columnDefinition="int(11) default '0' COMMENT '卡类型'")
	private int cardType;
	/**
	 * 卡名称
	 */
	@Column(name="card_name", nullable=false, columnDefinition="varchar(255) COMMENT '卡名称'")
	private String cardName;
	/**
	 * 卡单位
	 */
	@Column(name="card_unit", nullable=false, columnDefinition="varchar(255) COMMENT '卡单位'")
	private String cardUnit;
	/**
	 * 卡号/加油站
	 */
	@Column(name="card_no", nullable=false, columnDefinition="varchar(255) COMMENT '卡号/加油站'")
	private String cardNo;
	/**
	 * 油类型
	 */
	@Column(name="oil_type", nullable=false, columnDefinition="int(11) default '0' COMMENT '油类型'")
	private int oilType;
	/**
	 * 余额
	 */
	@Column(name="balance", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '余额'")
	private double balance;
	/**
	 * 使用人
	 */
	@Column(name="c_name",  columnDefinition="varchar(20) COMMENT '使用人'")
	private String cName;
	/**
	 * 车牌号
	 */
	@Column(name="plate_num", columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	/**
	 * 加油数量:升
	 */
	@Column(name="oil_rise", columnDefinition="double(10,2) default '0.00' COMMENT '加油数量:升'")
	private double oilRise;
	/**
	 * 加油单价
	 */
	@Column(name="oil_price", columnDefinition="double(10,2) default '0.00' COMMENT '加油单价'")
	private double oilPrice;
	/**
	 * 每升优惠金额
	 */
	@Column(name="oil_rise_favo", columnDefinition="double(10,2) default '0.00' COMMENT '每升优惠金额'")
	private double oilRiseFavo;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	/**
	 * 备注
	 */
	@Column(name="remark", columnDefinition="text COMMENT '备注'")
	private String remark;
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
	 * 获取 卡类型0油卡1油票2etc  
	 * @return cardType
	 */
	public int getCardType() {
		return cardType;
	}
	
	/**  
	 * 设置 卡类型0油卡1油票2etc  
	 * @param cardType 
	 */
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	
	/**  
	 * 获取 卡名称  
	 * @return cardName
	 */
	public String getCardName() {
		return cardName;
	}
	
	/**  
	 * 设置 卡名称  
	 * @param cardName 
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	/**  
	 * 获取 卡单位  
	 * @return cardUnit
	 */
	public String getCardUnit() {
		return cardUnit;
	}
	
	/**  
	 * 设置 卡单位  
	 * @param cardUnit 
	 */
	public void setCardUnit(String cardUnit) {
		this.cardUnit = cardUnit;
	}
	
	/**  
	 * 获取 卡号加油站  
	 * @return cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	
	/**  
	 * 设置 卡号加油站  
	 * @param cardNo 
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	/**  
	 * 获取 油类型  
	 * @return oilType
	 */
	public int getOilType() {
		return oilType;
	}
	
	/**  
	 * 设置 油类型  
	 * @param oilType 
	 */
	public void setOilType(int oilType) {
		this.oilType = oilType;
	}
	
	/**  
	 * 获取 余额  
	 * @return balance
	 */
	public double getBalance() {
		return balance;
	}
	
	/**  
	 * 设置 余额  
	 * @param balance 
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	/**  
	 * 获取 使用人  
	 * @return cName
	 */
	public String getcName() {
		return cName;
	}
	
	/**  
	 * 设置 使用人  
	 * @param cName 
	 */
	public void setcName(String cName) {
		this.cName = cName;
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
	 * 获取 加油数量:升  
	 * @return oilRise
	 */
	public double getOilRise() {
		return oilRise;
	}
	
	/**  
	 * 设置 加油数量:升  
	 * @param oilRise 
	 */
	public void setOilRise(double oilRise) {
		this.oilRise = oilRise;
	}
	
	/**  
	 * 获取 加油单价  
	 * @return oilPrice
	 */
	public double getOilPrice() {
		return oilPrice;
	}
	
	/**  
	 * 设置 加油单价  
	 * @param oilPrice 
	 */
	public void setOilPrice(double oilPrice) {
		this.oilPrice = oilPrice;
	}
	
	/**  
	 * 获取 每升优惠金额  
	 * @return oilRiseFavo
	 */
	public double getOilRiseFavo() {
		return oilRiseFavo;
	}
	
	/**  
	 * 设置 每升优惠金额  
	 * @param oilRiseFavo 
	 */
	public void setOilRiseFavo(double oilRiseFavo) {
		this.oilRiseFavo = oilRiseFavo;
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
	
	
	
}