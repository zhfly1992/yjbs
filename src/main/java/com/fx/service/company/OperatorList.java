package com.fx.service.company;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.entity.cus.BaseUser;

/**
 * 操作员列表（单位使用）
 */
@Entity
public class OperatorList implements Serializable,Cloneable {
	private static final long serialVersionUID = 2897166955836992422L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 单位编号
	 */
	@Column(name="company_num", columnDefinition="varchar(20) COMMENT '单位编号'")
	private String companyNum;
	/** 操作员用户 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	/**
	 * 操作员编号
	 */
	@Column(name="oper_no", columnDefinition="varchar(20) COMMENT '操作员编号'")
	private String operNo;
	/**
	 * 操作员类型
	 */
	@Column(name="oper_type", nullable=false, columnDefinition="int default 0 COMMENT '操作员类型'")
	private int operType;
	/**
	 * 操作员类型名称
	 */
	@Column(name="oper_type_name", nullable=false, columnDefinition="varchar(20) COMMENT '操作员类型名称'")
	private String operTypeName;
	/**
	 * 业务员
	 * 0：否 默认
	 * 1：是
	 */
	@Column(name="service_man", nullable=false, columnDefinition="int default 0 COMMENT '业务员0：否 默认 1是'")
	private int serviceMan;
	/**
	 * 报账员
	 * 0：否 默认
	 * 1：是
	 */
	@Column(name="render_man", nullable=false, columnDefinition="int default 0 COMMENT '报账员0：否 默认 1是'")
	private int renderMan;
	/**
	 * 界面主题
	 */
	@Column(name="view_theme", nullable=true, columnDefinition="varchar(50) COMMENT '界面主题'")
	private String viewTheme;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	/**
	 * 每日任务
	 * 单位-元
	 */
	@Column(name="everyday_task", columnDefinition="double(20,2) default 0 COMMENT '每日任务'")
	private double everydayTask;
	
	/** 父级id */
	@Column(name="pid", columnDefinition="bigint default 0 COMMENT '父级id'")
	private long pid;
	/**
	 * 设置的开放查询角色id/@name
	 */
	@Column(name="open_role", columnDefinition="text COMMENT '设置的开放查询角色id/@name'")
	private String openRole;
	/**
	 * 查看银行账余额
	 * 0：否 默认
	 * 1：是
	 */
	@Column(name="see_balance", nullable=false, columnDefinition="int default 0 COMMENT '查看银行账余额 0：否 默认 1是'")
	private int seeBalance;
	/**
	 * 查询空车延长小时
	 */
	@Column(name="empty_hour", nullable=false, columnDefinition="int default 0 COMMENT '查询空车延长小时'")
	private int emptyHour;
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
	 * 获取 单位编号  
	 * @return companyNum
	 */
	public String getCompanyNum() {
		return companyNum;
	}
	


	/**  
	 * 设置 单位编号  
	 * @param companyNum
	 */
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	


	/**  
	 * 获取 操作员用户  
	 * @return baseUserId
	 */
	public BaseUser getBaseUserId() {
		return baseUserId;
	}
	


	/**  
	 * 设置 操作员用户  
	 * @param baseUserId
	 */
	public void setBaseUserId(BaseUser baseUserId) {
		this.baseUserId = baseUserId;
	}
	


	/**  
	 * 获取 操作员编号  
	 * @return operNo
	 */
	public String getOperNo() {
		return operNo;
	}
	


	/**  
	 * 设置 操作员编号  
	 * @param operNo
	 */
	public void setOperNo(String operNo) {
		this.operNo = operNo;
	}
	


	/**  
	 * 获取 操作员类型  
	 * @return operType
	 */
	public int getOperType() {
		return operType;
	}
	


	/**  
	 * 设置 操作员类型  
	 * @param operType
	 */
	public void setOperType(int operType) {
		this.operType = operType;
	}
	


	/**  
	 * 获取 操作员类型名称  
	 * @return operTypeName
	 */
	public String getOperTypeName() {
		return operTypeName;
	}
	


	/**  
	 * 设置 操作员类型名称  
	 * @param operTypeName
	 */
	public void setOperTypeName(String operTypeName) {
		this.operTypeName = operTypeName;
	}
	


	/**  
	 * 获取 业务员0：否默认1：是  
	 * @return serviceMan
	 */
	public int getServiceMan() {
		return serviceMan;
	}
	


	/**  
	 * 设置 业务员0：否默认1：是  
	 * @param serviceMan
	 */
	public void setServiceMan(int serviceMan) {
		this.serviceMan = serviceMan;
	}
	


	/**  
	 * 获取 报账员0：否默认1：是  
	 * @return renderMan
	 */
	public int getRenderMan() {
		return renderMan;
	}
	


	/**  
	 * 设置 报账员0：否默认1：是  
	 * @param renderMan
	 */
	public void setRenderMan(int renderMan) {
		this.renderMan = renderMan;
	}
	


	/**  
	 * 获取 界面主题  
	 * @return viewTheme
	 */
	public String getViewTheme() {
		return viewTheme;
	}
	


	/**  
	 * 设置 界面主题  
	 * @param viewTheme
	 */
	public void setViewTheme(String viewTheme) {
		this.viewTheme = viewTheme;
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
	 * 获取 每日任务单位-元  
	 * @return everydayTask
	 */
	public double getEverydayTask() {
		return everydayTask;
	}
	


	/**  
	 * 设置 每日任务单位-元  
	 * @param everydayTask
	 */
	public void setEverydayTask(double everydayTask) {
		this.everydayTask = everydayTask;
	}
	


	/**  
	 * 获取 父级id  
	 * @return pid
	 */
	public long getPid() {
		return pid;
	}
	


	/**  
	 * 设置 父级id  
	 * @param pid
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}
	


	/**  
	 * 获取 设置的开放查询角色id@name  
	 * @return openRole
	 */
	public String getOpenRole() {
		return openRole;
	}
	


	/**  
	 * 设置 设置的开放查询角色id@name  
	 * @param openRole
	 */
	public void setOpenRole(String openRole) {
		this.openRole = openRole;
	}
	


	/**  
	 * 获取 查看银行账余额0：否默认1：是  
	 * @return seeBalance
	 */
	public int getSeeBalance() {
		return seeBalance;
	}
	


	/**  
	 * 设置 查看银行账余额0：否默认1：是  
	 * @param seeBalance
	 */
	public void setSeeBalance(int seeBalance) {
		this.seeBalance = seeBalance;
	}
	


	/**  
	 * 获取 查询空车延长小时  
	 * @return emptyHour
	 */
	public int getEmptyHour() {
		return emptyHour;
	}
	


	/**  
	 * 设置 查询空车延长小时  
	 * @param emptyHour
	 */
	public void setEmptyHour(int emptyHour) {
		this.emptyHour = emptyHour;
	}
	


	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
} 
