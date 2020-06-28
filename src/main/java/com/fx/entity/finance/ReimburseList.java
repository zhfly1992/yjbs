package com.fx.entity.finance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fx.commons.utils.enums.ReqSrc;
/**
 * 单位凭证记录
 * @author xx
 * @date 20200508
 */
@Entity
@Table(name="reimburse_list")
@JsonFilter("fitReimburseList")
public class ReimburseList implements Serializable{
	private static final long serialVersionUID = -3575505004342155970L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 凭证号码 */
	@Column(name="voucher_num",  columnDefinition="varchar(100) COMMENT '凭证号码'")
	private String voucherNum;
	
	/** 科目交易列表 */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="reim_course_id")
	private List<FeeCourseTrade> courseTrades;
	
	/** 记账时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="gain_time", columnDefinition="datetime COMMENT '记账时间'")
	private Date gainTime;
	
	/** 车牌号 */
	@Column(name="plate_num", columnDefinition="text COMMENT '车牌号'")
	private String plateNum;
	
	/** 0未核销 1已核销 2已关联 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '0未核销 1已核销 2已关联'")
	private int isCheck;
	
	/** 总金额:元 */
	@Column(name="total_money", columnDefinition="double(10,2) default '0.00' COMMENT '总金额:元'")
	private double totalMoney;
	
	/** 我的银行户名/我的银行账号 */
	@Column(name="my_bank_info",  columnDefinition="varchar(100) COMMENT '我的银行户名/我的银行账号'")
	private String myBankInfo;
	
	/** 对方户名/对方账号 */
	@Column(name="transfer_info",  columnDefinition="varchar(100) COMMENT '对方户名/对方账号'")
	private String transferInfo;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/**  数据来源 */
	@Enumerated(EnumType.STRING)
	@Column(name="reqsrc", columnDefinition="varchar(20) COMMENT '数据来源'")
	private ReqSrc reqsrc;
	
	
	
	/** 每次操作标识号 */
	@Column(name="oper_mark", columnDefinition="text COMMENT '每次操作标识号'")
	private String operMark;
	
	/** 操作备注 */
	@Column(name="oper_note",columnDefinition="text COMMENT '操作备注'")
	private String operNote;

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
	 * 获取 凭证号码  
	 * @return voucherNum
	 */
	public String getVoucherNum() {
		return voucherNum;
	}
	

	/**  
	 * 设置 凭证号码  
	 * @param voucherNum 
	 */
	public void setVoucherNum(String voucherNum) {
		this.voucherNum = voucherNum;
	}
	
	/**  
	 * 获取 科目交易列表  
	 * @return courseTrades
	 */
	public List<FeeCourseTrade> getCourseTrades() {
		return courseTrades;
	}
	


	/**  
	 * 设置 科目交易列表  
	 * @param courseTrades 
	 */
	public void setCourseTrades(List<FeeCourseTrade> courseTrades) {
		this.courseTrades = courseTrades;
	}
	


	/**  
	 * 获取 记账时间  
	 * @return gainTime
	 */
	public Date getGainTime() {
		return gainTime;
	}
	

	/**  
	 * 设置 记账时间  
	 * @param gainTime 
	 */
	public void setGainTime(Date gainTime) {
		this.gainTime = gainTime;
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
	 * 获取 0未核销1已核销2已关联  
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}
	

	/**  
	 * 设置 0未核销1已核销2已关联  
	 * @param isCheck 
	 */
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	

	/**  
	 * 获取 总金额:元  
	 * @return totalMoney
	 */
	public double getTotalMoney() {
		return totalMoney;
	}
	

	/**  
	 * 设置 总金额:元  
	 * @param totalMoney 
	 */
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	

	/**  
	 * 获取 我的银行户名我的银行账号  
	 * @return myBankInfo
	 */
	public String getMyBankInfo() {
		return myBankInfo;
	}
	

	/**  
	 * 设置 我的银行户名我的银行账号  
	 * @param myBankInfo 
	 */
	public void setMyBankInfo(String myBankInfo) {
		this.myBankInfo = myBankInfo;
	}
	

	/**  
	 * 获取 对方户名对方账号  
	 * @return transferInfo
	 */
	public String getTransferInfo() {
		return transferInfo;
	}
	

	/**  
	 * 设置 对方户名对方账号  
	 * @param transferInfo 
	 */
	public void setTransferInfo(String transferInfo) {
		this.transferInfo = transferInfo;
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
	
	
	
	
}