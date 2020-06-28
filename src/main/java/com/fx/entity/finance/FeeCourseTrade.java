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

import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;



/**
 * 	 科目交易记录
 *  @author xx
 *  @date 20200611
 */
@Entity
@Table(name="fee_course_trade")
public class FeeCourseTrade implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 报账科目名称 */
	@OneToOne(targetEntity = FeeCourse.class)
	@JoinColumn(name="fee_course_id", nullable=false, referencedColumnName="id", columnDefinition="varchar(30) COMMENT '报账科目id'")
	private FeeCourse feeCourseId;
	
	/** 摘要 */
	@Column(name="remark",  columnDefinition="varchar(255) COMMENT '摘要'")
	private String remark;
	
	/** 借方金额 */
	@Column(name="gath_money", columnDefinition="double(10,2) default '0.00' COMMENT '借方金额'")
	private double gathMoney;
	
	/** 贷方金额 */
	@Column(name="pay_money", columnDefinition="double(10,2) default '0.00' COMMENT '贷方金额'")
	private double payMoney;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 银行账引用 */
	@OneToOne(targetEntity = BankTradeList.class)
	@JoinColumn(name="bank_trade_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '银行账引用'")
	private BankTradeList bankTradeId;
	
	/** 主订单引用 */
	@OneToOne(targetEntity = BankTradeList.class)
	@JoinColumn(name="main_order_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '主订单引用'")
	private MainCarOrder mainOrderId;
	
	/** 子订单引用 */
	@OneToOne(targetEntity = BankTradeList.class)
	@JoinColumn(name="car_order_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '子订单引用'")
	private CarOrder carOrderId;
	
	/** 员工报账引用 */
	@OneToOne(targetEntity = BankTradeList.class)
	@JoinColumn(name="staff_reim_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '员工报账引用'")
	private StaffReimburse staffReimId;

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
	 * 获取 科目名称  
	 * @return feeCourseId
	 */
	public FeeCourse getFeeCourseId() {
		return feeCourseId;
	}
	

	/**  
	 * 设置 科目名称  
	 * @param feeCourseId 
	 */
	public void setFeeCourseId(FeeCourse feeCourseId) {
		this.feeCourseId = feeCourseId;
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


	/**  
	 * 获取 银行账引用  
	 * @return bankTradeId
	 */
	public BankTradeList getBankTradeId() {
		return bankTradeId;
	}
	


	/**  
	 * 设置 银行账引用  
	 * @param bankTradeId 
	 */
	public void setBankTradeId(BankTradeList bankTradeId) {
		this.bankTradeId = bankTradeId;
	}
	


	/**  
	 * 获取 主订单引用  
	 * @return mainOrderId
	 */
	public MainCarOrder getMainOrderId() {
		return mainOrderId;
	}
	


	/**  
	 * 设置 主订单引用  
	 * @param mainOrderId 
	 */
	public void setMainOrderId(MainCarOrder mainOrderId) {
		this.mainOrderId = mainOrderId;
	}
	


	/**  
	 * 获取 子订单引用  
	 * @return carOrderId
	 */
	public CarOrder getCarOrderId() {
		return carOrderId;
	}
	


	/**  
	 * 设置 子订单引用  
	 * @param carOrderId 
	 */
	public void setCarOrderId(CarOrder carOrderId) {
		this.carOrderId = carOrderId;
	}


	/**  
	 * 获取 员工报账引用  
	 * @return staffReimId
	 */
	public StaffReimburse getStaffReimId() {
		return staffReimId;
	}
	


	/**  
	 * 设置 员工报账引用  
	 * @param staffReimId 
	 */
	public void setStaffReimId(StaffReimburse staffReimId) {
		this.staffReimId = staffReimId;
	}
	
	
	
	

}
