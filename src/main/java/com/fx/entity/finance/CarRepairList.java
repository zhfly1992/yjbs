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

import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.BaseUser;
/**
 * 车辆维修记录表
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="car_repair_list")
public class CarRepairList implements Serializable{
	private static final long serialVersionUID = 790681427799404372L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 维修账号 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="repair_driver", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '维修账号'")
	private BaseUser repairDriver;
	
	/**  车牌号(即行驶证) */
	@Column(name="plate_num", nullable=false, columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	
	/** 维修站名称*/
	@Column(name="repair_name", nullable=false, columnDefinition="varchar(50) COMMENT '维修站名称'")
	private String repairName;
	
	/** 维修金额 */
	@Column(name="repair_money", columnDefinition="double(10,2) default '0.00' COMMENT '维修金额'")
	private double repairMoney;
	
	/** 0 记账 1现金 */
	@Column(name="repair_pay_way",  columnDefinition="int(11) default '0' COMMENT '0 记账 1现金'")
	private int repairPayWay;
	
	/** 凭证图片下载地址 */
	@Column(name="repair_voucher_url", columnDefinition="text COMMENT '凭证图片下载地址'")
	private String repairVoucherUrl;
	
	/** 维修摘要 */
	@Column(name="repair_remark",columnDefinition="varchar(255) COMMENT '维修摘要'")
	private String repairRemark;
	
	/** -1驳回 0 未审核 1已审核 2已复核 3已核销 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '0 未审核 1已审核 2已复核 3已核销'")
	private int isCheck;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 已核销金额 */
	@Column(name="verification_money", columnDefinition="double(10,2) default '0.00' COMMENT '已核销金额'")
	private double verificationMoney;
	
	/** 当前公里数 */
	@Column(name="current_kilo", columnDefinition="double(10,2) default '0.00' COMMENT '当前公里数'")
	private double currentKilo;
	
	/** 维修日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="rep_date", columnDefinition="datetime COMMENT '维修日期'")
	private Date repDate;
	
	/** 数据来源 */
	@Enumerated(EnumType.STRING)
	@Column(name="reqsrc", columnDefinition="varchar(20) COMMENT '数据来源'")
	private ReqSrc reqsrc;
	
	/** 每次操作标识号 */
	@Column(name="oper_mark", columnDefinition="text COMMENT '每次操作标识号'")
	private String operMark;
	/**
	 * 操作备注
	 */
	@Column(name="oper_note",columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	
	
	public CarRepairList() {}
	
	public CarRepairList(long id, double currentKilo) {
		super();
		this.id = id;
		this.currentKilo = currentKilo;
	}
	
	/**  
	 * 获取 维修摘要  
	 * @return repairRemark
	 */
	public String getRepairRemark() {
		return repairRemark;
	}
	

	/**  
	 * 设置 维修摘要  
	 * @param repairRemark 
	 */
	public void setRepairRemark(String repairRemark) {
		this.repairRemark = repairRemark;
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
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * 获取 维修账号  
	 * @return repairDriver
	 */
	public BaseUser getRepairDriver() {
		return repairDriver;
	}
	

	/**  
	 * 设置 维修账号  
	 * @param repairDriver 
	 */
	public void setRepairDriver(BaseUser repairDriver) {
		this.repairDriver = repairDriver;
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
	 * 获取 维修站名称  
	 * @return repairName
	 */
	public String getRepairName() {
		return repairName;
	}
	

	/**  
	 * 设置 维修站名称  
	 * @param repairName 
	 */
	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}
	

	/**  
	 * 获取 维修金额  
	 * @return repairMoney
	 */
	public double getRepairMoney() {
		return repairMoney;
	}
	

	/**  
	 * 设置 维修金额  
	 * @param repairMoney 
	 */
	public void setRepairMoney(double repairMoney) {
		this.repairMoney = repairMoney;
	}
	

	/**  
	 * 获取 0记账1现金  
	 * @return repairPayWay
	 */
	public int getRepairPayWay() {
		return repairPayWay;
	}
	

	/**  
	 * 设置 0记账1现金  
	 * @param repairPayWay 
	 */
	public void setRepairPayWay(int repairPayWay) {
		this.repairPayWay = repairPayWay;
	}
	

	/**  
	 * 获取 凭证图片下载地址  
	 * @return repairVoucherUrl
	 */
	public String getRepairVoucherUrl() {
		return repairVoucherUrl;
	}
	

	/**  
	 * 设置 凭证图片下载地址  
	 * @param repairVoucherUrl 
	 */
	public void setRepairVoucherUrl(String repairVoucherUrl) {
		this.repairVoucherUrl = repairVoucherUrl;
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
	 * 获取 维修日期  
	 * @return repDate
	 */
	public Date getRepDate() {
		return repDate;
	}
	

	/**  
	 * 设置 维修日期  
	 * @param repDate 
	 */
	public void setRepDate(Date repDate) {
		this.repDate = repDate;
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
	

}