package com.fx.entity.company;

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

import com.fx.commons.utils.enums.StaffState;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Dept;
import com.fx.entity.cus.permi.Role;

/**
 * 单位的员工
 */
@Entity
@Table(name="staff")
public class Staff implements Serializable{
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 部门id */
	@OneToOne(targetEntity = Dept.class)
	@JoinColumn(name="dept_id",nullable=false, referencedColumnName="id", columnDefinition="varchar(30) COMMENT '部门id'")
	private Dept deptId;

	/** 角色(职务)id */
	@OneToOne(targetEntity = Role.class)
	@JoinColumn(name="role_id", nullable=false,referencedColumnName="id", columnDefinition="varchar(20) COMMENT '角色(职务)id'")
	private Role roleId;
	
	/** 入职时间 */
	@Temporal(TemporalType.DATE)
	@Column(name="entry_time", nullable=false, columnDefinition="datetime COMMENT '入职时间'")
	private Date entryTime;
	
	/** 入职状态 */
	@Enumerated(EnumType.STRING)
	@Column(name="staff_state", nullable=false, columnDefinition="varchar(20) COMMENT '入职状态'")
	private StaffState staffState;
	
	/** 试用截止/合同到期时间 */
	@Temporal(TemporalType.DATE)
	@Column(name="expire_time", nullable=false, columnDefinition="datetime COMMENT '试用截止/合同到期时间'")
	private Date expireTime;
	
	/** 入职公司 */
	@OneToOne(targetEntity = CompanyCustom.class)
	@JoinColumn(name="entry_company",referencedColumnName="id",columnDefinition="varchar(100) COMMENT '入职公司'")
	private CompanyCustom entryCompany;
	
	/** 社保单位 */
	@Column(name="social_unit",columnDefinition="varchar(100) COMMENT '社保单位'")	
	private String socialUnit;
	
	/** 身份证号 */
	@Column(name="id_card", nullable=false, columnDefinition="varchar(25) COMMENT '身份证号'")
	private String idCard;
	
	/** 实际生日 */
	@Temporal(TemporalType.DATE)
	@Column(name="birthday_time", nullable=false, columnDefinition="datetime COMMENT '实际生日'")
	private Date birthdayTime;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 是否软删除 */
	@Column(name="is_del", columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否软删除'")
	private int isDel;
	
	
	/** 驾驶员属性  */
	
	/** 是否驾驶员 */
	@Column(name="is_driver", columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否驾驶员 1是'")
	private int isDriver;
	
	/** 驾驶证领证时间 */
	@Temporal(TemporalType.DATE)
	@Column(name="take_drive_time", columnDefinition="datetime COMMENT '驾驶证领证时间'")
	private Date takeDriveTime;
	
	/** 资格证号 */
	@Column(name="certificate_num", columnDefinition="varchar(50) COMMENT '资格证号'")
	private String certificateNum;
	
	/** 资格证证件类型 */
	@Column(name="certificate_type", columnDefinition="varchar(50) COMMENT '资格证证件类型'")
	private String certificateType;
	
	/** 资格证领证时间 */
	@Temporal(TemporalType.DATE)
	@Column(name="take_certificate_time", columnDefinition="datetime COMMENT '资格证领证时间'")
	private Date takeCertificateTime;
	
	/** 准驾车型 */
	@Column(name="drive_type", columnDefinition="varchar(50) COMMENT '准驾车型'")
	private String driveType;
	
	/** 小组id */
	@OneToOne(targetEntity = CompanyGroup.class)
	@JoinColumn(name="group_id", referencedColumnName="id", unique=true,columnDefinition="varchar(20) COMMENT '小组名称'")
	private CompanyGroup groupId;
	
	/** 身份证正面url */
	@Column(name="id_card_front_img", columnDefinition="varchar(200) COMMENT '身份证正面url'")
	private String idCardFrontImg;
	
	/** 身份证背面url */
	@Column(name="id_card_back_img", columnDefinition="varchar(200) COMMENT '身份证背面url'")
	private String idCardBackImg;
	
	/** 驾驶证url */
	@Column(name="drive_img", columnDefinition="varchar(200) COMMENT '驾驶证url'")
	private String driveImg;
	
	/** 资格证url */
	@Column(name="certificate_img", columnDefinition="varchar(200) COMMENT '资格证url'")
	private String certificateImg;
	
	
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
	 * 获取 部门id  
	 * @return deptId
	 */
	public Dept getDeptId() {
		return deptId;
	}
	

	/**  
	 * 设置 部门id  
	 * @param deptId 
	 */
	public void setDeptId(Dept deptId) {
		this.deptId = deptId;
	}
	

	/**  
	 * 获取 角色(职务)id  
	 * @return roleId
	 */
	public Role getRoleId() {
		return roleId;
	}
	
	
	/**  
	 * 设置 角色(职务)id  
	 * @param roleId 
	 */
	public void setRoleId(Role roleId) {
		this.roleId = roleId;
	}
	

	/**  
	 * 获取 入职时间  
	 * @return entryTime
	 */
	public Date getEntryTime() {
		return entryTime;
	}
	

	/**  
	 * 设置 入职时间  
	 * @param entryTime 
	 */
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	

	/**  
	 * 获取 入职状态  
	 * @return staffState
	 */
	public StaffState getStaffState() {
		return staffState;
	}
	

	/**  
	 * 设置 入职状态  
	 * @param staffState 
	 */
	public void setStaffState(StaffState staffState) {
		this.staffState = staffState;
	}
	

	/**  
	 * 获取 试用截止合同到期时间  
	 * @return expireTime
	 */
	public Date getExpireTime() {
		return expireTime;
	}
	

	/**  
	 * 设置 试用截止合同到期时间  
	 * @param expireTime 
	 */
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	

	/**  
	 * 获取 入职公司  
	 * @return entryCompany
	 */
	public CompanyCustom getEntryCompany() {
		return entryCompany;
	}
	

	/**  
	 * 设置 入职公司  
	 * @param entryCompany 
	 */
	public void setEntryCompany(CompanyCustom entryCompany) {
		this.entryCompany = entryCompany;
	}
	

	/**  
	 * 获取 社保单位  
	 * @return socialUnit
	 */
	public String getSocialUnit() {
		return socialUnit;
	}
	

	/**  
	 * 设置 社保单位  
	 * @param socialUnit 
	 */
	public void setSocialUnit(String socialUnit) {
		this.socialUnit = socialUnit;
	}
	

	/**  
	 * 获取 身份证号  
	 * @return idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	

	/**  
	 * 设置 身份证号  
	 * @param idCard 
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	

	/**  
	 * 获取 实际生日  
	 * @return birthdayTime
	 */
	public Date getBirthdayTime() {
		return birthdayTime;
	}
	

	/**  
	 * 设置 实际生日  
	 * @param birthdayTime 
	 */
	public void setBirthdayTime(Date birthdayTime) {
		this.birthdayTime = birthdayTime;
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
	 * 获取 是否软删除  
	 * @return isDel
	 */
	public int getIsDel() {
		return isDel;
	}
	

	/**  
	 * 设置 是否软删除  
	 * @param isDel 
	 */
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	

	/**  
	 * 获取 是否驾驶员  
	 * @return isDriver
	 */
	public int getIsDriver() {
		return isDriver;
	}
	

	/**  
	 * 设置 是否驾驶员  
	 * @param isDriver 
	 */
	public void setIsDriver(int isDriver) {
		this.isDriver = isDriver;
	}
	

	/**  
	 * 获取 驾驶证领证时间  
	 * @return takeDriveTime
	 */
	public Date getTakeDriveTime() {
		return takeDriveTime;
	}
	

	/**  
	 * 设置 驾驶证领证时间  
	 * @param takeDriveTime 
	 */
	public void setTakeDriveTime(Date takeDriveTime) {
		this.takeDriveTime = takeDriveTime;
	}
	

	/**  
	 * 获取 资格证号  
	 * @return certificateNum
	 */
	public String getCertificateNum() {
		return certificateNum;
	}
	

	/**  
	 * 设置 资格证号  
	 * @param certificateNum 
	 */
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	

	/**  
	 * 获取 资格证证件类型  
	 * @return certificateType
	 */
	public String getCertificateType() {
		return certificateType;
	}
	

	/**  
	 * 设置 资格证证件类型  
	 * @param certificateType 
	 */
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	

	/**  
	 * 获取 资格证领证时间  
	 * @return takeCertificateTime
	 */
	public Date getTakeCertificateTime() {
		return takeCertificateTime;
	}
	

	/**  
	 * 设置 资格证领证时间  
	 * @param takeCertificateTime 
	 */
	public void setTakeCertificateTime(Date takeCertificateTime) {
		this.takeCertificateTime = takeCertificateTime;
	}
	

	/**  
	 * 获取 准驾车型  
	 * @return driveType
	 */
	public String getDriveType() {
		return driveType;
	}
	

	/**  
	 * 设置 准驾车型  
	 * @param driveType 
	 */
	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}
	

	/**  
	 * 获取 小组id  
	 * @return groupId
	 */
	public CompanyGroup getGroupId() {
		return groupId;
	}
	

	/**  
	 * 设置 小组id  
	 * @param groupId 
	 */
	public void setGroupId(CompanyGroup groupId) {
		this.groupId = groupId;
	}
	

	/**  
	 * 获取 身份证正面url  
	 * @return idCardFrontImg
	 */
	public String getIdCardFrontImg() {
		return idCardFrontImg;
	}
	

	/**  
	 * 设置 身份证正面url  
	 * @param idCardFrontImg 
	 */
	public void setIdCardFrontImg(String idCardFrontImg) {
		this.idCardFrontImg = idCardFrontImg;
	}
	

	/**  
	 * 获取 身份证背面url  
	 * @return idCardBackImg
	 */
	public String getIdCardBackImg() {
		return idCardBackImg;
	}
	

	/**  
	 * 设置 身份证背面url  
	 * @param idCardBackImg 
	 */
	public void setIdCardBackImg(String idCardBackImg) {
		this.idCardBackImg = idCardBackImg;
	}
	

	/**  
	 * 获取 驾驶证url  
	 * @return driveImg
	 */
	public String getDriveImg() {
		return driveImg;
	}
	

	/**  
	 * 设置 驾驶证url  
	 * @param driveImg 
	 */
	public void setDriveImg(String driveImg) {
		this.driveImg = driveImg;
	}
	

	/**  
	 * 获取 资格证url  
	 * @return certificateImg
	 */
	public String getCertificateImg() {
		return certificateImg;
	}
	

	/**  
	 * 设置 资格证url  
	 * @param certificateImg 
	 */
	public void setCertificateImg(String certificateImg) {
		this.certificateImg = certificateImg;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
