package com.fx.entity.cus;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 单位-用户
 */
@Entity
@Table(name="company_user")
public class CompanyUser implements Serializable {
	private static final long serialVersionUID = -4833773985696162406L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 单位名称 */
	@Column(name="company_name", nullable=false, columnDefinition="varchar(50) COMMENT '单位名称'")
	private String companyName;
	
	/** 单位地址 eg：四川省-成都市-武侯区 遇见巴士科技公司 */
	@Column(name="company_addr", columnDefinition="varchar(100) COMMENT '单位地址'")
	private String companyAddr;
	
	/** 单位地址坐标 eg：103.666666,30.666666 */
	@Column(name="company_lnglat", columnDefinition="varchar(50) COMMENT '单位地址坐标'")
	private String companyLnglat;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num", columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	/**
	 * 单位管理用户名
	 */
	@Column(name="manager_name", columnDefinition="varchar(20) COMMENT '单位管理用户名'")
	private String managerName;
	/**
	 * 开票加收税金
	 */
	@Column(name="invoice_stand", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '开票加收税金'")
	private double invoicetand;
	
	
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
	 * 获取 用户基类id  
	 * @return baseUserId
	 */
	public BaseUser getBaseUserId() {
		return baseUserId;
	}

	/**  
	 * 设置 用户基类id  
	 * @param baseUserId
	 */
	public void setBaseUserId(BaseUser baseUserId) {
		this.baseUserId = baseUserId;
	}
	
	/**  
	 * 获取 单位名称  
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**  
	 * 设置 单位名称  
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**  
	 * 获取 单位地址eg：四川省-成都市-武侯区遇见巴士科技公司  
	 * @return companyAddr
	 */
	public String getCompanyAddr() {
		return companyAddr;
	}
	
	/**  
	 * 设置 单位地址eg：四川省-成都市-武侯区遇见巴士科技公司  
	 * @param companyAddr
	 */
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	
	/**  
	 * 获取 单位地址坐标eg：103.66666630.666666  
	 * @return companyLnglat
	 */
	public String getCompanyLnglat() {
		return companyLnglat;
	}
	
	/**  
	 * 设置 单位地址坐标eg：103.66666630.666666  
	 * @param companyLnglat
	 */
	public void setCompanyLnglat(String companyLnglat) {
		this.companyLnglat = companyLnglat;
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取 开票加收税金  
	 * @return invoicetand
	 */
	public double getInvoicetand() {
		return invoicetand;
	}

	/**  
	 * 设置 开票加收税金  
	 * @param invoicetand 
	 */
	public void setInvoicetand(double invoicetand) {
		this.invoicetand = invoicetand;
	}

	/**  
	 * 获取 单位管理用户名  
	 * @return managerName
	 */
	public String getManagerName() {
		return managerName;
	}

	/**  
	 * 设置 单位管理用户名  
	 * @param managerName
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
}
