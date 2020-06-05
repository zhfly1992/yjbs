package com.fx.entity.company;

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
 * 驾驶员小组列表
 * @author xx
 * @date 20200326
 */
@Entity
@Table(name="company_group")
public class CompanyGroup implements Serializable{
	private static final long serialVersionUID = -4886305101465804656L;
	public CompanyGroup() {}
	public CompanyGroup(String groupName) {
		this.groupName = groupName;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 小组名称 */
	@Column(name="group_name",nullable=false,unique=true, columnDefinition="varchar(20) COMMENT '小组名称'")
	private String groupName;
	
	/** 组长联系电话 */
	@Column(name="link_phone",nullable=false, columnDefinition="varchar(20) COMMENT '组长联系电话'")
	private String linkPhone;
	
	/** 组长姓名 */
	@Column(name="link_name",nullable=false, columnDefinition="varchar(20) COMMENT '组长姓名'")
	private String linkName;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time",nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
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
	
	/**  
	 * 获取 小组名称  
	 * @return groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	
	/**  
	 * 设置 小组名称  
	 * @param groupName 
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**  
	 * 获取 组长联系电话  
	 * @return linkPhone
	 */
	public String getLinkPhone() {
		return linkPhone;
	}
	
	/**  
	 * 设置 组长联系电话  
	 * @param linkPhone 
	 */
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	
	/**  
	 * 获取 组长姓名  
	 * @return linkName
	 */
	public String getLinkName() {
		return linkName;
	}
	
	/**  
	 * 设置 组长姓名  
	 * @param linkName 
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
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
	
	
}