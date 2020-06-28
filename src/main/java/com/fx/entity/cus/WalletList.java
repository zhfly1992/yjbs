package com.fx.entity.cus;

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
 * 用户钱包明细表
 * @author qfc
 * @Date 20200426
 */
@Entity
@Table(name="wallet_list")
public class WalletList implements Serializable{
	private static final long serialVersionUID = -6803272383341501829L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 操作账户 唯一
	 */
	@Column(name="c_name", nullable=false, columnDefinition="varchar(50) COMMENT '操作账户 唯一'")
	private String cName;
	/**
	 * 根据类型的辅助字段
	 */
	@Column(name="assist", columnDefinition="text COMMENT '根据类型的辅助字段'")
	private String assist;
	/**
	 * 交易类型
	 * 0：充值
	 * 1：用车方提成
	 * 2：车主方提成
	 * 3：置顶付费
	 * 4：手续费(出租方,押金)
	 * 5：付定金
	 * 6：付尾款
	 * 7：赠送红包
	 * 8：提现
	 * 9：认证提成
	 * 10:租车奖励(用车方奖励)
	 * 11：车队车辆管理费(车队：邀请方管理员余额 ；个人余额) 
	 * 12:押金
	 * 13:未完团定金
	 * 14:完团定金
	 * 15:红包抵扣
	 * 16:退定金
	 * 17:车队服务费
	 * 18:线下收款
	 * 19:查看电话扣费
	 * 20:短信费用
	 * 21:转至余额
	 * 22:消费奖金分红
	 * 23:推荐奖励
	 * 24:业务对冲
	 * 25:订单完团
	 * 26:提现退款
	 * 27:行程团上现收
	 */
	@Column(name="type", nullable=false, columnDefinition="int COMMENT '交易类型'")
	private int type;
	/**
	 * 金额类型 
	 * 1-现金余额；
	 * 2-押金；
	 * 3-未完团定金；
	 * 4-提成金额；
	 * 5-红包；
	 * 6-分红；
	 * 7-推荐奖励；
	 * 8-完团余额；
	 * 9-完团总额；
	 * 10-消费金额；
	 */
	@Column(name="atype", nullable=false, columnDefinition="int default 1 COMMENT '金额类型'")
	private int atype=1;
	/**
	 * 操作金额
	 */
	@Column(name="amoney", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '操作金额'")
	private double amoney;
	/**
	 * 状态 1:支出  0:收入
	 */
	@Column(name="status", nullable=false, columnDefinition="int COMMENT '状态1:支出  0:收入'")
	private int status;
	/**
	 * 钱包余额
	 */
	@Column(name="cash_balance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '钱包余额'")
	private double cashBalance;
	/**
	 * 备注
	 */
	@Column(name="note", columnDefinition="varchar(200) default '' COMMENT '备注'")
	private String note;
	/**
	 * 操作时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '创建时间'")
	private Date atime;
	/**
	 * 提现备注（提现银行卡：交易流水号,开户行行号,开户行卡号,银行卡绑定姓名,银行卡预留手机）
	 */
	@Column(name="wdc_note", columnDefinition="varchar(200) COMMENT '提现备注（提现银行卡）'")
	private String wdcNote;
	
	
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
	 * 获取 操作账户唯一  
	 * @return cName  
	 */
	public String getcName() {
		return cName;
	}
	/**  
	 * 设置 操作账户唯一  
	 * @param cName
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}
	/**  
	 * 获取 根据类型的辅助字段  
	 * @return assist  
	 */
	public String getAssist() {
		return assist;
	}
	/**  
	 * 设置 根据类型的辅助字段  
	 * @param assist
	 */
	public void setAssist(String assist) {
		this.assist = assist;
	}
	/**  
	 * 获取 交易类型
	 * 0：充值
	 * 1：用车方提成
	 * 2：车主方提成
	 * 3：置顶付费
	 * 4：手续费(出租方,押金)
	 * 5：付定金
	 * 6：付尾款
	 * 7：赠送红包
	 * 8：提现
	 * 9：认证提成
	 * 10:租车奖励(用车方奖励)
	 * 11：车队车辆管理费(车队：邀请方管理员余额 ；个人余额) 
	 * 12:押金
	 * 13:未完团定金
	 * 14:完团定金
	 * 15:红包抵扣
	 * 16:退定金
	 * 17:车队服务费
	 * 18:线下收款
	 * 19:查看电话扣费
	 * 20:短信费用
	 * 21:转至余额
	 * 22:消费奖金分红
	 * 23:推荐奖励
	 * 24:业务对冲
	 * 25:订单完团
	 * 26:提现退款
	 * 27:行程团上现收
	 * @return type  
	 */
	public int getType() {
		return type;
	}
	/**  
	 * 设置 交易类型
	 * 0：充值
	 * 1：用车方提成
	 * 2：车主方提成
	 * 3：置顶付费
	 * 4：手续费(出租方,押金)
	 * 5：付定金
	 * 6：付尾款
	 * 7：赠送红包
	 * 8：提现
	 * 9：认证提成
	 * 10:租车奖励(用车方奖励)
	 * 11：车队车辆管理费(车队：邀请方管理员余额 ；个人余额) 
	 * 12:押金
	 * 13:未完团定金
	 * 14:完团定金
	 * 15:红包抵扣
	 * 16:退定金
	 * 17:车队服务费
	 * 18:线下收款
	 * 19:查看电话扣费
	 * 20:短信费用
	 * 21:转至余额
	 * 22:消费奖金分红
	 * 23:推荐奖励
	 * 24:业务对冲
	 * 25:订单完团
	 * 26:提现退款
	 * 27:行程团上现收
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**  
	 * 获取 操作金额  
	 * @return amoney  
	 */
	public double getAmoney() {
		return amoney;
	}
	/**  
	 * 设置 操作金额  
	 * @param amoney
	 */
	public void setAmoney(double amoney) {
		this.amoney = amoney;
	}
	/**  
	 * 获取 状态1:支出0:收入  
	 * @return status  
	 */
	public int getStatus() {
		return status;
	}
	/**  
	 * 设置 状态1:支出0:收入  
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**  
	 * 获取 钱包余额  
	 * @return cashBalance  
	 */
	public double getCashBalance() {
		return cashBalance;
	}
	/**  
	 * 设置 钱包余额  
	 * @param cashBalance
	 */
	public void setCashBalance(double cashBalance) {
		this.cashBalance = cashBalance;
	}
	/**  
	 * 获取 备注  
	 * @return note  
	 */
	public String getNote() {
		return note;
	}
	/**  
	 * 设置 备注  
	 * @param note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**  
	 * 获取 操作时间  
	 * @return atime  
	 */
	public Date getAtime() {
		return atime;
	}
	/**  
	 * 设置 操作时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	/**  
	 * 获取 金额类型1-现金余额；2-押金；
	 * 3-未完团定金；4-提成金额； 5-红包；
	 * 6-分红；7-推荐奖励；8-完团余额；
	 * 9-完团总额；10-消费金额；
	 * @return atype  
	 */
	public int getAtype() {
		return atype;
	}
	/**  
	 * 设置 金额类型1-现金余额；2-押金；
	 * 3-未完团定金；4-提成金额；5-红包；
	 *  6-分红；7-推荐奖励；8-完团余额；
	 * 9-完团总额；10-消费金额；
	 * @param atype
	 */
	public void setAtype(int atype) {
		this.atype = atype;
	}
	/**  
	 * 获取 提现备注（提现银行卡：交易流水号,开户行行号,开户行卡号,银行卡绑定姓名,银行卡预留手机）  
	 * @return wdcNote  
	 */
	public String getWdcNote() {
		return wdcNote;
	}
	/**  
	 * 设置 提现备注（提现银行卡：交易流水号,开户行行号,开户行卡号,银行卡绑定姓名,银行卡预留手机）
	 * @param wdcNote
	 */
	public void setWdcNote(String wdcNote) {
		this.wdcNote = wdcNote;
	}
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
