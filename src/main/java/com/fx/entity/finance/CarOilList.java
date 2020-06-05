package com.fx.entity.finance;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.OilWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.BaseUser;
/**
 * 车辆加油记录表
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="car_oil_list")
public class CarOilList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5456678969617141878L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 加油账号 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="oil_driver", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '加油账号'")
	private BaseUser oilDriver;
	
	/** 车牌号(即行驶证) */
	@Column(name="plate_num", nullable=false, columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	
	/** 上一次公里数 */
	@Column(name="last_kilo", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '上一次公里数'")
	private double lastKilo;
	
	/** 当前公里数 */
	@Column(name="current_kilo", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '当前公里'")
	private double currentKilo;
	
	/** 加油方式(加油站，充值卡等) */
	@Enumerated(EnumType.STRING)
	@Column(name="oil_way", columnDefinition="varchar(20) COMMENT '加油方式(油票，充值卡等)'")
	private OilWay oilWay;
	
	/** 加油站名称 */
	@Column(name="oil_station",  columnDefinition="varchar(255) COMMENT '加油站名称'")
	private String oilStation;
	
	/** 加油数量:升 */
	@Column(name="oil_rise", columnDefinition="double(10,2) default '0.00' COMMENT '加油数量:升'")
	private double oilRise;
	
	/** 加油金额:元 */
	@Column(name="oil_money", columnDefinition="double(10,2) default '0.00' COMMENT '加油金额:元'")
	private double oilMoney;
	
	/** 油耗:升/百公里 */
	@Column(name="oil_wear", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '油耗:升/百公里'")
	private double oilWear;
	
	/** 凭证图片下载地址 */
	@Column(name="oil_voucher_url", columnDefinition="varchar(255) COMMENT '凭证图片下载地址'")
	private String oilVoucherUrl;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/**  -1驳回 0 未审核 1已审核 2已复核 3已核销 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '审核状态'")
	private int isCheck;
	
	/** 已核销金额 */
	@Column(name="verification_money", columnDefinition="double(10,2) default '0.00' COMMENT '已核销金额'")
	private double verificationMoney;
	
	/** 加油备注 */
	@Column(name="oil_remark",columnDefinition="varchar(255) COMMENT '加油备注'")
	private String oilRemark;
	
	/** 加油日期 */
	@Temporal(TemporalType.DATE)
	@Column(name="oil_date", columnDefinition="datetime COMMENT '加油日期'")
	private Date oilDate;
	
	/** 数据来源 */
	@Enumerated(EnumType.STRING)
	@Column(name="reqsrc", columnDefinition="varchar(20) COMMENT '数据来源'")
	private ReqSrc reqsrc;
	
	/** 实付金额 */
	@Column(name="oil_real_money", columnDefinition="double(10,2) default '0.00' COMMENT '实付金额'")
	private double oilRealMoney;
	
	/** 每次操作标识号 */
	@Column(name="oper_mark", columnDefinition="text COMMENT '每次操作标识号'")
	private String operMark;
	
	/** 操作备注 */
	@Column(name="oper_note",columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	
	
	public CarOilList() {}
	
	public CarOilList(long id, double currentKilo) {
		super();
		this.id = id;
		this.currentKilo = currentKilo;
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
	 * 获取 加油账号  
	 * @return oilDriver
	 */
	public BaseUser getOilDriver() {
		return oilDriver;
	}
	

	/**  
	 * 设置 加油账号  
	 * @param oilDriver 
	 */
	public void setOilDriver(BaseUser oilDriver) {
		this.oilDriver = oilDriver;
	}
	

	/**  
	 * 获取 车牌号(即行驶证)  
	 * @return plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	

	/**  
	 * 设置 车牌号(即行驶证)  
	 * @param plateNum 
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	

	/**  
	 * 获取 上一次公里数  
	 * @return lastKilo
	 */
	public double getLastKilo() {
		return lastKilo;
	}
	

	/**  
	 * 设置 上一次公里数  
	 * @param lastKilo 
	 */
	public void setLastKilo(double lastKilo) {
		this.lastKilo = lastKilo;
	}
	

	/**  
	 * 获取 当前公里数  
	 * @return currentKilo
	 */
	public double getCurrentKilo() {
		return currentKilo;
	}
	

	/**  
	 * 设置 当前公里数  
	 * @param currentKilo 
	 */
	public void setCurrentKilo(double currentKilo) {
		this.currentKilo = currentKilo;
	}
	

	/**  
	 * 获取 加油方式(加油站，充值卡等)  
	 * @return oilWay
	 */
	public OilWay getOilWay() {
		return oilWay;
	}
	

	/**  
	 * 设置 加油方式(加油站，充值卡等)  
	 * @param oilWay 
	 */
	public void setOilWay(OilWay oilWay) {
		this.oilWay = oilWay;
	}
	

	/**  
	 * 获取 加油站名称  
	 * @return oilStation
	 */
	public String getOilStation() {
		return oilStation;
	}
	

	/**  
	 * 设置 加油站名称  
	 * @param oilStation 
	 */
	public void setOilStation(String oilStation) {
		this.oilStation = oilStation;
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
	 * 获取 加油金额:元  
	 * @return oilMoney
	 */
	public double getOilMoney() {
		return oilMoney;
	}
	

	/**  
	 * 设置 加油金额:元  
	 * @param oilMoney 
	 */
	public void setOilMoney(double oilMoney) {
		this.oilMoney = oilMoney;
	}
	

	/**  
	 * 获取 油耗:升百公里  
	 * @return oilWear
	 */
	public double getOilWear() {
		return oilWear;
	}
	

	/**  
	 * 设置 油耗:升百公里  
	 * @param oilWear 
	 */
	public void setOilWear(double oilWear) {
		this.oilWear = oilWear;
	}
	

	/**  
	 * 获取 凭证图片下载地址  
	 * @return oilVoucherUrl
	 */
	public String getOilVoucherUrl() {
		return oilVoucherUrl;
	}
	

	/**  
	 * 设置 凭证图片下载地址  
	 * @param oilVoucherUrl 
	 */
	public void setOilVoucherUrl(String oilVoucherUrl) {
		this.oilVoucherUrl = oilVoucherUrl;
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
	 * 获取 -1驳回0未审核1已审核2已复核3已核销  
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}
	

	/**  
	 * 设置 -1驳回0未审核1已审核2已复核3已核销  
	 * @param isCheck 
	 */
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	

	/**  
	 * 获取 已核销金额  
	 * @return verificationMoney
	 */
	public double getVerificationMoney() {
		return verificationMoney;
	}
	

	/**  
	 * 设置 已核销金额  
	 * @param verificationMoney 
	 */
	public void setVerificationMoney(double verificationMoney) {
		this.verificationMoney = verificationMoney;
	}
	

	/**  
	 * 获取 加油备注  
	 * @return oilRemark
	 */
	public String getOilRemark() {
		return oilRemark;
	}
	

	/**  
	 * 设置 加油备注  
	 * @param oilRemark 
	 */
	public void setOilRemark(String oilRemark) {
		this.oilRemark = oilRemark;
	}
	

	/**  
	 * 获取 加油日期  
	 * @return oilDate
	 */
	public Date getOilDate() {
		return oilDate;
	}
	

	/**  
	 * 设置 加油日期  
	 * @param oilDate 
	 */
	public void setOilDate(Date oilDate) {
		this.oilDate = oilDate;
	}
	

	/**  
	 * 获取 数据来源  
	 * @return reqsrc
	 */
	public ReqSrc getReqsrc() {
		return reqsrc;
	}
	

	/**  
	 * 设置 数据来源  
	 * @param reqsrc 
	 */
	public void setReqsrc(ReqSrc reqsrc) {
		this.reqsrc = reqsrc;
	}
	

	/**  
	 * 获取 实付金额  
	 * @return oilRealMoney
	 */
	public double getOilRealMoney() {
		return oilRealMoney;
	}
	

	/**  
	 * 设置 实付金额  
	 * @param oilRealMoney 
	 */
	public void setOilRealMoney(double oilRealMoney) {
		this.oilRealMoney = oilRealMoney;
	}
	

	/**  
	 * 获取 每次操作标识号  
	 * @return operMark
	 */
	public String getOperMark() {
		return operMark;
	}
	

	/**  
	 * 设置 每次操作标识号  
	 * @param operMark 
	 */
	public void setOperMark(String operMark) {
		this.operMark = operMark;
	}
	

	/**  
	 * 获取 操作备注  
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	

	/**  
	 * 设置 操作备注  
	 * @param operNote 
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
	
	
}