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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.PublicDataType;

/**
 * 公用数据设置
 */
@Entity
@Table(name="public_data_set")
public class PublicDataSet implements Serializable {
	private static final long serialVersionUID = 3257930732841206924L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 设置数据标识
	 */
	@Column(name="set_id", nullable=false, columnDefinition="varchar(50) COMMENT '设置数据标识'")
	private String setId;
	/**
	 * 设置数据标识说明
	 */
	@Column(name="set_id_note", nullable=false, columnDefinition="varchar(50) COMMENT '设置数据标识说明'")
	private String setIdNote;
	
	/** 公共数据类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="public_data_type", nullable=false, columnDefinition="varchar(30) COMMENT '公共数据类型'")
	private PublicDataType publicDataType;
	
	/**
	 * 设置数据
	 */
	@Column(name="cus_sms_set",columnDefinition="longtext COMMENT '设置数据'")
	private String cusSmsSet;

	/**
	 * 是否开启
	 * 0-不开启；1-开启；默认不开启
	 */
	@Column(name="is_open", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否开启'")
	private int isOpen;
	
	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time", nullable=true, columnDefinition="datetime COMMENT '开始时间'")
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time", nullable=true, columnDefinition="datetime COMMENT '结束时间'")
	private Date endTime;
	
	

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
	 * 获取 设置数据标识  
	 * @return setId  
	 */
	public String getSetId() {
		return setId;
	}

	/**  
	 * 设置 设置数据标识  
	 * @param setId
	 */
	public void setSetId(String setId) {
		this.setId = setId;
	}

	/**  
	 * 获取 设置数据标识说明  
	 * @return setIdNote  
	 */
	public String getSetIdNote() {
		return setIdNote;
	}

	/**  
	 * 设置 设置数据标识说明  
	 * @param setIdNote
	 */
	public void setSetIdNote(String setIdNote) {
		this.setIdNote = setIdNote;
	}

	/**  
	 * 获取 公共数据类型  
	 * @return publicDataType
	 */
	public PublicDataType getPublicDataType() {
		return publicDataType;
	}

	/**  
	 * 设置 公共数据类型  
	 * @param publicDataType
	 */
	public void setPublicDataType(PublicDataType publicDataType) {
		this.publicDataType = publicDataType;
	}

	/**  
	 * 获取 设置数据  
	 * @return cusSmsSet  
	 */
	public String getCusSmsSet() {
		return cusSmsSet;
	}

	/**  
	 * 设置 设置数据  
	 * @param cusSmsSet
	 */
	public void setCusSmsSet(String cusSmsSet) {
		this.cusSmsSet = cusSmsSet;
	}

	/**  
	 * 获取 是否开启0-不开启；1-开启；默认不开启  
	 * @return isOpen  
	 */
	public int getIsOpen() {
		return isOpen;
	}

	/**  
	 * 设置 是否开启0-不开启；1-开启；默认不开启  
	 * @param isOpen
	 */
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	/**  
	 * 获取 开始时间  
	 * @return startTime  
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**  
	 * 设置 开始时间  
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**  
	 * 获取 结束时间  
	 * @return endTime  
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**  
	 * 设置 结束时间  
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
} 
