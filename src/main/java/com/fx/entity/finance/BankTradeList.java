package com.fx.entity.finance;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 银行日记账
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="bank_trade_list")
public class BankTradeList implements Serializable{
	
	private static final long serialVersionUID = 8443490621704402489L;

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
	
	/** 我的银行名称 */
	@Column(name="my_bank_name", columnDefinition="varchar(100) COMMENT '我的银行名称'")
	private String myBankName;
	
	/** 我的银行账号 */
	@Column(name="my_bank_num", columnDefinition="varchar(50) COMMENT '我的银行账号'")
	private String myBankNum;
	
	/** 对方户名 */
	@Column(name="trans_name",columnDefinition="varchar(100) COMMENT '对方户名'")
	private String transName;
	
	/** 对方账号 */
	@Column(name="trans_num", columnDefinition="varchar(50) COMMENT '对方账号'")
	private String transNum;
	
	/** 下账客户id */
	@Column(name="company_cus_id", columnDefinition="varchar(20) COMMENT '下账客户id'")
	private String companyCusId;
	
	/** 客户名称 */
	@Column(name="cus_name", columnDefinition="varchar(50) COMMENT '客户名称'")
	private String cusName;
	
	/** 金额类型 */
	@OneToOne(targetEntity = MoneyType.class)
	@JoinColumn(name="money_type_id", referencedColumnName="id", columnDefinition="bigint COMMENT '金额类型id'")
	private MoneyType moneyTypeId;
	
	/** 交易时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="trade_time", columnDefinition="datetime COMMENT '交易时间'")
	private Date tradeTime;
	
	/** 收入 */
	@Column(name="trade_in_money", columnDefinition="double(10,2) default '0.00' COMMENT '收入'")
	private double tradeInMoney;
	
	/** 支出 */
	@Column(name="trade_out_money", columnDefinition="double(10,2) default '0.00' COMMENT '支出'")
	private double tradeOutMoney;
	
	/** 余额 */
	@Column(name="balance", columnDefinition="double(10,2) default '0.00' COMMENT '余额'")
	private double balance;
	
	/** 摘要 */
	@Column(name="remark", columnDefinition="text COMMENT '摘要'")
	private String remark;
	
	/** -1已报销完成  0未操作  1待审核 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '审核状态'")
	private int isCheck;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 凭证号码 */
	@Column(name="voucher_number",  columnDefinition="varchar(50) COMMENT '凭证号码'")
	private String voucherNumber;
	
	/** 通报人手机号 */
	@Column(name="notice_man",  columnDefinition="varchar(50) COMMENT '通报人手机号'")
	private String noticeMan;
	
	/** 通报内容 */
	@Column(name="notice_remark", columnDefinition="text COMMENT '通报内容'")
	private String noticeRemark;
	
	/** 下账订单号 */
	@Column(name="order_num", columnDefinition="text COMMENT '下账订单号'")
	private String orderNum;
		
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
	 * 获取 我的银行名称  
	 * @return myBankName
	 */
	public String getMyBankName() {
		return myBankName;
	}
	

	/**  
	 * 设置 我的银行名称  
	 * @param myBankName 
	 */
	public void setMyBankName(String myBankName) {
		this.myBankName = myBankName;
	}
	

	/**  
	 * 获取 我的银行账号  
	 * @return myBankNum
	 */
	public String getMyBankNum() {
		return myBankNum;
	}
	

	/**  
	 * 设置 我的银行账号  
	 * @param myBankNum 
	 */
	public void setMyBankNum(String myBankNum) {
		this.myBankNum = myBankNum;
	}
	

	/**  
	 * 获取 对方户名  
	 * @return transName
	 */
	public String getTransName() {
		return transName;
	}
	

	/**  
	 * 设置 对方户名  
	 * @param transName 
	 */
	public void setTransName(String transName) {
		this.transName = transName;
	}
	

	/**  
	 * 获取 对方账号  
	 * @return transNum
	 */
	public String getTransNum() {
		return transNum;
	}
	

	/**  
	 * 设置 对方账号  
	 * @param transNum 
	 */
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	

	/**  
	 * 获取 客户名称  
	 * @return cusName
	 */
	public String getCusName() {
		return cusName;
	}
	

	/**  
	 * 设置 客户名称  
	 * @param cusName 
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	

	/**  
	 * 获取 交易时间  
	 * @return tradeTime
	 */
	public Date getTradeTime() {
		return tradeTime;
	}
	

	/**  
	 * 设置 交易时间  
	 * @param tradeTime 
	 */
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	

	/**  
	 * 获取 收入  
	 * @return tradeInMoney
	 */
	public double getTradeInMoney() {
		return tradeInMoney;
	}
	

	/**  
	 * 设置 收入  
	 * @param tradeInMoney 
	 */
	public void setTradeInMoney(double tradeInMoney) {
		this.tradeInMoney = tradeInMoney;
	}
	

	/**  
	 * 获取 支出  
	 * @return tradeOutMoney
	 */
	public double getTradeOutMoney() {
		return tradeOutMoney;
	}
	

	/**  
	 * 设置 支出  
	 * @param tradeOutMoney 
	 */
	public void setTradeOutMoney(double tradeOutMoney) {
		this.tradeOutMoney = tradeOutMoney;
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
	 * 获取 摘要  
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}
	

	/**  
	 * 设置 摘要  
	 * @param remark 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	/**  
	 * 获取-1已报销完成  0未操作  1待审核  
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}
	

	/**  
	 * 设置 -1已报销完成  0未操作  1待审核  
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
	 * 获取 凭证号码  
	 * @return voucherNumber
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}
	

	/**  
	 * 设置 凭证号码  
	 * @param voucherNumber 
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	


	/**  
	 * 获取 通报人手机号  
	 * @return noticeMan
	 */
	public String getNoticeMan() {
		return noticeMan;
	}
	

	/**  
	 * 设置 通报人手机号  
	 * @param noticeMan 
	 */
	public void setNoticeMan(String noticeMan) {
		this.noticeMan = noticeMan;
	}
	

	/**  
	 * 获取 通报内容  
	 * @return noticeRemark
	 */
	public String getNoticeRemark() {
		return noticeRemark;
	}
	

	/**  
	 * 设置 通报内容  
	 * @param noticeRemark 
	 */
	public void setNoticeRemark(String noticeRemark) {
		this.noticeRemark = noticeRemark;
	}
	

	/**  
	 * 获取 下账订单号  
	 * @return orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}
	

	/**  
	 * 设置 下账订单号  
	 * @param orderNum 
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	

	/**  
	 * 获取 客户id  
	 * @return companyCusId
	 */
	public String getCompanyCusId() {
		return companyCusId;
	}
	


	/**  
	 * 设置 客户id  
	 * @param companyCusId 
	 */
	public void setCompanyCusId(String companyCusId) {
		this.companyCusId = companyCusId;
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
	 * 获取 金额类型  
	 * @return moneyTypeId
	 */
	public MoneyType getMoneyTypeId() {
		return moneyTypeId;
	}
	


	/**  
	 * 设置 金额类型  
	 * @param moneyTypeId
	 */
	public void setMoneyTypeId(MoneyType moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}
	
	
	
	
}