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
 * 	 科目期初设置
 *  @author xx
 *  @date 20200709
 */
@Entity
@Table(name="fee_course_trade_first")
public class FeeCourseTradeFirst implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 摘要 */
	@Column(name="remark",  columnDefinition="varchar(255) COMMENT '摘要'")
	private String remark;
	
	/** 借方金额 */
	@Column(name="gath_money", columnDefinition="double(10,2) default '0.00' COMMENT '借方金额'")
	private double gathMoney;
	
	/** 贷方金额 */
	@Column(name="pay_money", columnDefinition="double(10,2) default '0.00' COMMENT '贷方金额'")
	private double payMoney;
	
	/** 余额 */
	@Column(name="balance", columnDefinition="double(10,2) default '0.00' COMMENT '余额'")
	private double balance;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;

	/**  
	 * 获取 唯一id自增长主键  
	 * @return 唯一id自增长主键
	 */
	public long getId() {
		return id;
	}
	

	/**  
	 * 设置 唯一id自增长主键  
	 * @param 唯一id自增长主键
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
	 * 获取 借方金额  
	 * @return gathMoney
	 */
	public double getGathMoney() {
		return gathMoney;
	}
	

	/**  
	 * 设置 借方金额  
	 * @param gathMoney
	 */
	public void setGathMoney(double gathMoney) {
		this.gathMoney = gathMoney;
	}
	

	/**  
	 * 获取 贷方金额  
	 * @return payMoney
	 */
	public double getPayMoney() {
		return payMoney;
	}
	

	/**  
	 * 设置 贷方金额  
	 * @param payMoney
	 */
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
