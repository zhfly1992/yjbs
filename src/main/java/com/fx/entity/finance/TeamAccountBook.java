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
 * 现金日记账
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="team_account_book")
public class TeamAccountBook implements Serializable,Cloneable {
	private static final long serialVersionUID = -5133792909562466506L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 加油，维修，团上开支，对应报销类型，行程收入，外调车支出 */
	@Column(name="accountName", columnDefinition="varchar(255) COMMENT '名称'")
	private String accountName;
	
	/** 收付款方式 */
	@Column(name="account_way", columnDefinition="text COMMENT '收付款方式'")
	private String accountWay;
	
	/**
	 * 收款金额（借方金额）
	 */
	@Column(name="account_money", columnDefinition="double(10,2) default '0.00' COMMENT '收款金额'")
	private double accountMoney;
	
	/**
	 * 贷方金额
	 */
	@Column(name="credit_money", columnDefinition="double(10,2) default '0.00' COMMENT '贷方金额'")
	private double creditMoney;
	
	/**
	 * 油耗金额
	 */
	@Column(name="oil_money", columnDefinition="double(10,2) default '0.00' COMMENT '油耗金额'")
	private double oilMoney;
	
	/**
	 * 余额
	 */
	@Column(name="balance", columnDefinition="double(10,2) default '0.00' COMMENT '余额'")
	private double balance;
	
	/**
	 * 凭证号
	 */
	@Column(name="voucher_num", columnDefinition="varchar(20) COMMENT '凭证号'")
	private String voucherNum;
	
	/**
	 * 订单号/记录id
	 */
	@Column(name="order_num", columnDefinition="varchar(200) COMMENT '订单号/记录id'")
	private String orderNum;
	
	/**
	 * 车牌号(派单时加入利润记录)
	 */
	@Column(name="plate_num", columnDefinition="varchar(500) COMMENT '车牌号'")
	private String plateNum;
	
	/**
	 * 业务员
	 */
	@Column(name="service_man", columnDefinition="varchar(20) COMMENT '业务员'")
	private String serviceMan;
	
	/**
	 * 摘要
	 */
	@Column(name="remark", columnDefinition="text COMMENT '摘要'")
	private String remark;
	
	/**
	 * 备注
	 */
	@Column(name="note", columnDefinition="text COMMENT 'note'")
	private String note;
	
	/**
	 * 0 其他  1自营 2非自营
	 */
	@Column(name="account_type",  columnDefinition="int(11) default '0' COMMENT '0 其他  1自营 2非自营'")
	private int accountType;
	
	/**
	 * 收付款时间/利润账：用车开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="account_time", columnDefinition="datetime COMMENT '收付款时间'")
	private Date accountTime;
	
	/**
	 * 记录添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '申请时间'")
	private Date addTime;
	
	/**
	 * 客户名称:正常报销(报销人) 内部账报销(款项负责人)
	 */
	@Column(name="receive_name", columnDefinition="varchar(200) COMMENT '客户名称'")
	private String receiveName;
	
	/**
	 * 对方账号
	 */
	@Column(name="transfer_account",  columnDefinition="varchar(100) COMMENT '付款方账号'")
	private String transferAccount;
	
	/**
	 * 驾驶员
	 */
	@Column(name="driver", columnDefinition="varchar(500) COMMENT '驾驶员'")
	private String driver;
	
	/**
	 * 真实客户
	 */
	@Column(name="customer", columnDefinition="varchar(500) COMMENT '真实客户'")
	private String customer;
	
	/**
	 * 每次操作标识号
	 */
	@Column(name="oper_mark", columnDefinition="text COMMENT '每次操作标识号'")
	private String operMark;
	
	/**
	 * 行程详情
	 */
	@Column(name="vie_way",  columnDefinition="text COMMENT '行程详情'")
	private String vieWay;
	
	/**
	 * 1预存油量 2每升优惠
	 */
	@Column(name="pre_type",  columnDefinition="int(11) default '0' COMMENT '1预存油量 2每升优惠'")
	private int preType;
	
	/**
	 * 每升优惠/采购油量
	 */
	@Column(name="oil_faour", columnDefinition="double(10,2) default '0.00' COMMENT '每升优惠/采购油量'")
	private double oilFavour;
	
	/**
	 * 0非公司账 1公司账
	 */
	@Column(name="company_book",  columnDefinition="int(11) default '0' COMMENT '0非公司账 1公司账'")
	private int companyBook;
	
	public long getId() {
		return id;
	}
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
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountWay() {
		return accountWay;
	}
	public void setAccountWay(String accountWay) {
		this.accountWay = accountWay;
	}
	public double getAccountMoney() {
		return accountMoney;
	}
	public void setAccountMoney(double accountMoney) {
		this.accountMoney = accountMoney;
	}
	public double getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(double creditMoney) {
		this.creditMoney = creditMoney;
	}
	public double getOilMoney() {
		return oilMoney;
	}
	public void setOilMoney(double oilMoney) {
		this.oilMoney = oilMoney;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getVoucherNum() {
		return voucherNum;
	}
	public void setVoucherNum(String voucherNum) {
		this.voucherNum = voucherNum;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getAccountTime() {
		return accountTime;
	}
	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public String getServiceMan() {
		return serviceMan;
	}
	public void setServiceMan(String serviceMan) {
		this.serviceMan = serviceMan;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getTransferAccount() {
		return transferAccount;
	}
	public void setTransferAccount(String transferAccount) {
		this.transferAccount = transferAccount;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getOperMark() {
		return operMark;
	}
	public void setOperMark(String operMark) {
		this.operMark = operMark;
	}
	/**  
	 * 获取 vieWay  
	 * @return vieWay 
	 */
	public String getVieWay() {
		return vieWay;
	}
	/**  
	 * 设置 vieWay  
	 * @param vieWay  
	 */
	public void setVieWay(String vieWay) {
		this.vieWay = vieWay;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**  
	 * 获取 oilFavour  
	 * @return oilFavour 
	 */
	public double getOilFavour() {
		return oilFavour;
	}
	/**  
	 * 设置 oilFavour  
	 * @param oilFavour  
	 */
	public void setOilFavour(double oilFavour) {
		this.oilFavour = oilFavour;
	}
	/**  
	 * 获取 preType  
	 * @return preType 
	 */
	public int getPreType() {
		return preType;
	}
	/**  
	 * 设置 preType  
	 * @param preType  
	 */
	public void setPreType(int preType) {
		this.preType = preType;
	}
	/**  
	 * 获取 companyBook  
	 * @return companyBook 
	 */
	public int getCompanyBook() {
		return companyBook;
	}
	/**  
	 * 设置 companyBook  
	 * @param companyBook  
	 */
	public void setCompanyBook(int companyBook) {
		this.companyBook = companyBook;
	}
} 
