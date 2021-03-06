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

import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Dept;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;
import com.fx.entity.order.RouteTradeList;

/**
 *  员工报账记录
 *  @author xx
 *  @date 20200611
 */
@Entity
@Table(name="staff_reimburse")
public class StaffReimburse implements Serializable{
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 添加人信息 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="add_user", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '添加人信息'")
	private BaseUser addUser;
	
	/** 报销人信息 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="reim_user", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '报销人信息'")
	private BaseUser reimUser;
	
	/** 车牌号 */
	@Column(name="plate_num", columnDefinition="text COMMENT '车牌号'")
	private String plateNum;
	
	/** 科目名称 */
	@OneToOne(targetEntity = FeeCourse.class)
	@JoinColumn(name="fee_course_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '科目id'")
	private FeeCourse feeCourseId;
	
	/** 对方科目名称 */
	@OneToOne(targetEntity = FeeCourse.class)
	@JoinColumn(name="face_course_id",  referencedColumnName="id", columnDefinition="varchar(30) COMMENT '对方科目id'")
	private FeeCourse faceCourseId;
	
	/** 下账/预存客户 */
	@OneToOne(targetEntity = CompanyCustom.class)
	@JoinColumn(name="pre_user_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '下账/预存客户'")
	private CompanyCustom preUserId;
	
	/** 业务部门 */
	@OneToOne(targetEntity = Dept.class)
	@JoinColumn(name="dept_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '业务部门id'")
	private Dept deptId;
	
	/** 摘要 */
	@Column(name="remark",  columnDefinition="varchar(255) COMMENT '摘要'")
	private String remark;

	/** 收入金额 */
	@Column(name="gath_money", columnDefinition="double(10,2) default '0.00' COMMENT '收入金额'")
	private double gathMoney;
	
	/** 支出金额 */
	@Column(name="pay_money", columnDefinition="double(10,2) default '0.00' COMMENT '支出金额'")
	private double payMoney;
	
	/** -1已驳回 0 未审核 1已审核 2已生成凭证*/
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '-1已驳回 0 未审核 1已审核 2已生成凭证'")
	private int isCheck;
	
	/** 凭证号码 */
	@Column(name="voucher_num",  columnDefinition="varchar(100) COMMENT '凭证号码'")
	private String voucherNum;
	
	/**  驳回原因 */
	@Column(name="refuse_reason",  columnDefinition="varchar(255) COMMENT '驳回原因'")
	private String refuseReason;
	
	/** 报账图片下载地址 */
	@Column(name="reim_voucher_url", columnDefinition="text COMMENT '报账图片下载地址'")
	private String reimVoucherUrl;
	
	/** 子订单引用 */
	@OneToOne(targetEntity = CarOrder.class)
	@JoinColumn(name="car_order_reim", referencedColumnName="order_num", columnDefinition="varchar(30) COMMENT '子订单引用'")
	private CarOrder carOrderReim;
	
	/** 团上开支记录引用 */
	@OneToOne(targetEntity = RouteTradeList.class)
	@JoinColumn(name="order_trade", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '团上开支记录引用'")
	private RouteTradeList orderTrade;
	
	/** 主订单引用 */
	@OneToOne(targetEntity = MainCarOrder.class)
	@JoinColumn(name="main_order_reim", referencedColumnName="order_num", columnDefinition="varchar(30) COMMENT '主订单引用'")
	private MainCarOrder mainOrderReim;
	
	/** 记账类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="jz_type", columnDefinition="varchar(20) COMMENT '记账类型'")
	private JzType jzType;
	
	/** 支付日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="pay_date", columnDefinition="datetime COMMENT '记账日期'")
	private Date payDate;
	
	/** 驾驶员记账引用 */
	@OneToOne(targetEntity = DriverJzRecord.class)
	@JoinColumn(name="driver_jz_record_id", referencedColumnName="id", columnDefinition="varchar(30) COMMENT '驾驶员记账引用'")
	private DriverJzRecord driverJzRecordId;
	
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
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
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
	 * 获取 团上开支记录引用  
	 * @return orderTrade
	 */
	public RouteTradeList getOrderTrade() {
		return orderTrade;
	}

	/**  
	 * 设置 团上开支记录引用  
	 * @param orderTrade 
	 */
	public void setOrderTrade(RouteTradeList orderTrade) {
		this.orderTrade = orderTrade;
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
	 * 获取 添加人信息  
	 * @return addUser
	 */
	public BaseUser getAddUser() {
		return addUser;
	}
	

	/**  
	 * 设置 添加人信息  
	 * @param addUser
	 */
	public void setAddUser(BaseUser addUser) {
		this.addUser = addUser;
	}
	

	/**  
	 * 获取 报销人信息  
	 * @return reimUser
	 */
	public BaseUser getReimUser() {
		return reimUser;
	}
	

	/**  
	 * 设置 报销人信息  
	 * @param reimUser
	 */
	public void setReimUser(BaseUser reimUser) {
		this.reimUser = reimUser;
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
	 * 获取 支付日期  
	 * @return payDate
	 */
	public Date getPayDate() {
		return payDate;
	}
	

	/**  
	 * 设置 支付日期  
	 * @param payDate
	 */
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	

	/**  
	 * 获取 驾驶员记账引用  
	 * @return driverJzRecordId
	 */
	public DriverJzRecord getDriverJzRecordId() {
		return driverJzRecordId;
	}
	

	/**  
	 * 设置 驾驶员记账引用  
	 * @param driverJzRecordId
	 */
	public void setDriverJzRecordId(DriverJzRecord driverJzRecordId) {
		this.driverJzRecordId = driverJzRecordId;
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
	 * 获取 对方科目名称  
	 * @return faceCourseId
	 */
	public FeeCourse getFaceCourseId() {
		return faceCourseId;
	}
	

	/**  
	 * 设置 对方科目名称  
	 * @param faceCourseId 
	 */
	public void setFaceCourseId(FeeCourse faceCourseId) {
		this.faceCourseId = faceCourseId;
	}
	


	/**  
	 * 获取 下账预存客户  
	 * @return preUserId
	 */
	public CompanyCustom getPreUserId() {
		return preUserId;
	}
	

	/**  
	 * 设置 下账预存客户  
	 * @param preUserId 
	 */
	public void setPreUserId(CompanyCustom preUserId) {
		this.preUserId = preUserId;
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
	 * 获取 记账类型  
	 * @return jzType
	 */
	public JzType getJzType() {
		return jzType;
	}

	/**  
	 * 设置 记账类型  
	 * @param jzType
	 */
	public void setJzType(JzType jzType) {
		this.jzType = jzType;
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
	 * 获取 收入金额  
	 * @return gathMoney
	 */
	public double getGathMoney() {
		return gathMoney;
	}

	/**  
	 * 设置 收入金额  
	 * @param gathMoney 
	 */
	public void setGathMoney(double gathMoney) {
		this.gathMoney = gathMoney;
	}

	/**  
	 * 获取 支出金额  
	 * @return payMoney
	 */
	public double getPayMoney() {
		return payMoney;
	}

	/**  
	 * 设置 支出金额  
	 * @param payMoney 
	 */
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	/**  
	 * 获取 -1已驳回0未审核1已审核 2已生成凭证
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}

	/**  
	 * 设置 -1已驳回0未审核1已审核  2已生成凭证
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
	 * 获取 报账图片下载地址  
	 * @return reimVoucherUrl
	 */
	public String getReimVoucherUrl() {
		return reimVoucherUrl;
	}

	/**  
	 * 设置 报账图片下载地址  
	 * @param reimVoucherUrl 
	 */
	public void setReimVoucherUrl(String reimVoucherUrl) {
		this.reimVoucherUrl = reimVoucherUrl;
	}

	/**  
	 * 获取 子订单引用  
	 * @return carOrderReim
	 */
	public CarOrder getCarOrderReim() {
		return carOrderReim;
	}

	/**  
	 * 设置 子订单引用  
	 * @param carOrderReim 
	 */
	public void setCarOrderReim(CarOrder carOrderReim) {
		this.carOrderReim = carOrderReim;
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
