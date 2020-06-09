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

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Dept;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;
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
	
	/** 业务部门 */
	@OneToOne(targetEntity = Dept.class)
	@JoinColumn(name="dept_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '业务部门id'")
	private Dept deptId;
	
	/** 凭证号码 */
	@Column(name="voucher_num",  columnDefinition="varchar(100) COMMENT '凭证号码'")
	private String voucherNum;
	
	/** 科目名称 */
	@OneToOne(targetEntity = FeeCourse.class)
	@JoinColumn(name="fee_course_id", nullable=false, referencedColumnName="id", columnDefinition="varchar(30) COMMENT '科目id'")
	private FeeCourse feeCourseId;
	
	/** 0收入 1支出 */
	@Column(name="fee_status",  columnDefinition="int(11) default '0' COMMENT '0收入 1支出'")
	private int feeStatus;
	
	/** 记账时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="gain_time", columnDefinition="datetime COMMENT '记账时间'")
	private Date gainTime;
	
	/** 车牌号 */
	@Column(name="plate_num",  columnDefinition="text COMMENT '车牌号'")
	private String plateNum;
	
	/** 报销人信息 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="reim_user_id", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '报销人信息'")
	private BaseUser reimUserId;
	
	/** 摘要 */
	@Column(name="remark",  columnDefinition="varchar(255) COMMENT '摘要'")
	private String remark;
	
	/** -1已驳回 0 未审核 1已审核 2已核销 3已关联 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '-1已驳回 0 未审核 1已审核 2已核销 3已关联'")
	private int isCheck;
	
	/**  驳回原因 */
	@Column(name="refuse_reason",  columnDefinition="varchar(255) COMMENT '驳回原因'")
	private String refuseReason;
	
	/** 凭证图片下载地址 */
	@Column(name="reim_voucher_url", columnDefinition="varchar(255) COMMENT '凭证图片下载地址'")
	private String reimVoucherUrl;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 总金额:元 */
	@Column(name="total_money", columnDefinition="double(10,2) default '0.00' COMMENT '总金额:元'")
	private double totalMoney;
	
	/** 已核销金额 */
	@Column(name="verification_money", columnDefinition="double(10,2) default '0.00' COMMENT '已核销金额'")
	private double verificationMoney;
	
	/** 我的银行户名/我的银行账号 */
	@Column(name="my_bank_info",  columnDefinition="varchar(100) COMMENT '我的银行户名/我的银行账号'")
	private String myBankInfo;
	
	/** 对方户名/对方账号 */
	@Column(name="transfer_info",  columnDefinition="varchar(100) COMMENT '对方户名/对方账号'")
	private String transferInfo;
	
	/** 子订单引用 */
	@OneToOne(targetEntity = CarOrder.class)
	@JoinColumn(name="car_order_reim", referencedColumnName="order_num", columnDefinition="varchar(30) COMMENT '子订单引用'")
	private CarOrder carOrderReim;
	
	/** 主订单引用 */
	@OneToOne(targetEntity = MainCarOrder.class)
	@JoinColumn(name="main_order_reim", referencedColumnName="order_num", columnDefinition="varchar(30) COMMENT '主订单引用'")
	private MainCarOrder mainOrderReim;
	
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
	 * 获取 业务部门  
	 * @return deptId
	 */
	public Dept getDeptId() {
		return deptId;
	}
	

	/**  
	 * 设置 业务部门  
	 * @param deptId 
	 */
	public void setDeptId(Dept deptId) {
		this.deptId = deptId;
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
	 * 获取 0收入1支出  
	 * @return feeStatus
	 */
	public int getFeeStatus() {
		return feeStatus;
	}
	

	/**  
	 * 设置 0收入1支出  
	 * @param feeStatus 
	 */
	public void setFeeStatus(int feeStatus) {
		this.feeStatus = feeStatus;
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
	 * 获取 报销人信息  
	 * @return reimUserId
	 */
	public BaseUser getReimUserId() {
		return reimUserId;
	}
	

	/**  
	 * 设置 报销人信息  
	 * @param reimUserId 
	 */
	public void setReimUserId(BaseUser reimUserId) {
		this.reimUserId = reimUserId;
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
	 * 获取 -1已驳回0未审核1已审核2已核销3已关联  
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}
	

	/**  
	 * 设置 -1已驳回0未审核1已审核2已核销3已关联  
	 * @param isCheck 
	 */
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	

	/**  
	 * 获取 驳回原因  
	 * @return refuseReason
	 */
	public String getRefuseReason() {
		return refuseReason;
	}
	

	/**  
	 * 设置 驳回原因  
	 * @param refuseReason 
	 */
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	

	/**  
	 * 获取 凭证图片下载地址  
	 * @return reimVoucherUrl
	 */
	public String getReimVoucherUrl() {
		return reimVoucherUrl;
	}
	

	/**  
	 * 设置 凭证图片下载地址  
	 * @param reimVoucherUrl 
	 */
	public void setReimVoucherUrl(String reimVoucherUrl) {
		this.reimVoucherUrl = reimVoucherUrl;
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
	 * 获取 付款方名称/付款方账号  
	 * @return transferInfo
	 */
	public String getTransferInfo() {
		return transferInfo;
	}
	

	/**  
	 * 设置 付款方名称/付款方账号  
	 * @param transferInfo 
	 */
	public void setTransferInfo(String transferInfo) {
		this.transferInfo = transferInfo;
	}
	

	/**  
	 * 获取 订单引用  
	 * @return carOrderReim
	 */
	public CarOrder getCarOrderReim() {
		return carOrderReim;
	}
	

	/**  
	 * 设置 订单引用  
	 * @param carOrderReim 
	 */
	public void setCarOrderReim(CarOrder carOrderReim) {
		this.carOrderReim = carOrderReim;
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

	/**  
	 * 获取 我的银行户名/我的银行账号  
	 * @return myBankInfo
	 */
	public String getMyBankInfo() {
		return myBankInfo;
	}
	

	/**  
	 * 设置 我的银行户名/我的银行账号  
	 * @param myBankInfo 
	 */
	public void setMyBankInfo(String myBankInfo) {
		this.myBankInfo = myBankInfo;
	}

	/**  
	 * 获取 主订单引用  
	 * @return mainOrderReim
	 */
	public MainCarOrder getMainOrderReim() {
		return mainOrderReim;
	}
	

	/**  
	 * 设置 主订单引用  
	 * @param mainOrderReim 
	 */
	public void setMainOrderReim(MainCarOrder mainOrderReim) {
		this.mainOrderReim = mainOrderReim;
	}
	
	
	
	
	
}