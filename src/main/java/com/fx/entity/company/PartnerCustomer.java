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
 * 客车帮合作客户列表（平台）
 */
@Entity
@Table(name="partner_customer")
public class PartnerCustomer implements Serializable{
	private static final long serialVersionUID = -1900527472926168729L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 账号
	 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(20) COMMENT '账号'")
	private String uname;
	/**
	 * 所在区域
	 */
	@Column(name="in_area", nullable=false, columnDefinition="varchar(50) COMMENT '所在区域'")
	private String inArea;
	/**
	 * 客户级别
	 * 1大客户
	 * 2同行
	 */
	@Column(name="partner_level", nullable=false, columnDefinition="int(11) default '1' COMMENT '客户级别:1大客户,2同行'")
	private int partnerLevel;
	/**
	 * 到期时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="valid_time", nullable=false, columnDefinition="datetime COMMENT '到期时间'")
	private Date validTime;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
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
	 * 获取 账号  
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}
	
	/**  
	 * 设置 账号  
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	/**  
	 * 获取 所在区域  
	 * @return inArea
	 */
	public String getInArea() {
		return inArea;
	}
	
	/**  
	 * 设置 所在区域  
	 * @param inArea
	 */
	public void setInArea(String inArea) {
		this.inArea = inArea;
	}
	
	/**  
	 * 获取 客户级别1大客户2同行  
	 * @return partnerLevel
	 */
	public int getPartnerLevel() {
		return partnerLevel;
	}
	
	/**  
	 * 设置 客户级别1大客户2同行  
	 * @param partnerLevel
	 */
	public void setPartnerLevel(int partnerLevel) {
		this.partnerLevel = partnerLevel;
	}
	
	/**  
	 * 获取 到期时间  
	 * @return validTime
	 */
	public Date getValidTime() {
		return validTime;
	}
	
	/**  
	 * 设置 到期时间  
	 * @param validTime
	 */
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
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