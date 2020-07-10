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
 * 银行帐金额类型列表
 * @author xx
 * @date 20200701
 */
@Entity
@Table(name="mone_type")
public class MoneyType implements Serializable{
	private static final long serialVersionUID = -4886305101465804656L;
	public MoneyType() {}
	public MoneyType(String typeName) {
		this.typeName = typeName;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 类型名称 */
	@Column(name="type_name",nullable=false,unique=true, columnDefinition="varchar(50) COMMENT '金额类型名称'")
	private String typeName;
		
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
	 * 获取 类型名称  
	 * @return typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**  
	 * 设置 类型名称  
	 * @param typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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