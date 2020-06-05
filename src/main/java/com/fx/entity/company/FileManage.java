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

@Entity
@Table(name="file_manage")
public class FileManage implements Serializable {
private static final long serialVersionUID = 2456416577758711132L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 上传文件用户名
	 */
	@Column(length = 100)
	private String fCName;
	/**
	 * 文件显示名称
	 */
	@Column(length = 100)
	private String fName;
	/**
	 * 文件真实名称
	 */
	@Column(length = 200)
	private String fRelName;
	/**
	 * 文件服务器路径
	 */
	@Column(length = 500)
	private String fUrl;
	/**
	 * 文件上传方式（0:系统管理后台 1:pc端客户 2:pc端单位 3:移动端客户 4:移动端单位）
	 */
	@Column
	private int fType;
	/**
	 * 文件状态 0-待保存；1-保存；
	 */
	@Column(name="status", columnDefinition="int default 0 COMMENT '文件状态'")
	private int status;
	/**
	 * 文件备注
	 */
	@Column(length = 1000)
	private String fRemark;
	/**
	 * 文件上传时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fUpTime;
	/**
	 * 属性名称
	 */
	@Column(name="filed_name", columnDefinition="varchar(50) COMMENT '属性名称'")
	private String filedName;
	/**
	 * 数据标志 eg:id或者编号
	 */
	@Column(name="data_id", columnDefinition="varchar(30) COMMENT '数据标志'")
	private String dataId;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the fCName
	 */
	public String getfCName() {
		return fCName;
	}
	/**
	 * @param fCName the fCName to set
	 */
	public void setfCName(String fCName) {
		this.fCName = fCName;
	}
	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**
	 * @return the fRelName
	 */
	public String getfRelName() {
		return fRelName;
	}
	/**
	 * @param fRelName the fRelName to set
	 */
	public void setfRelName(String fRelName) {
		this.fRelName = fRelName;
	}
	/**
	 * @return the fUrl
	 */
	public String getfUrl() {
		return fUrl;
	}
	/**
	 * @param fUrl the fUrl to set
	 */
	public void setfUrl(String fUrl) {
		this.fUrl = fUrl;
	}
	/**
	 * @return the fType
	 */
	public int getfType() {
		return fType;
	}
	/**
	 * @param fType the fType to set
	 */
	public void setfType(int fType) {
		this.fType = fType;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the fRemark
	 */
	public String getfRemark() {
		return fRemark;
	}
	/**
	 * @param fRemark the fRemark to set
	 */
	public void setfRemark(String fRemark) {
		this.fRemark = fRemark;
	}
	/**
	 * @return the fUpTime
	 */
	public Date getfUpTime() {
		return fUpTime;
	}
	/**
	 * @param fUpTime the fUpTime to set
	 */
	public void setfUpTime(Date fUpTime) {
		this.fUpTime = fUpTime;
	}
	/**
	 * @return the filedName
	 */
	public String getFiledName() {
		return filedName;
	}
	/**
	 * @param filedName the filedName to set
	 */
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	/**
	 * @return the dataId
	 */
	public String getDataId() {
		return dataId;
	}
	/**
	 * @param dataId the dataId to set
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
