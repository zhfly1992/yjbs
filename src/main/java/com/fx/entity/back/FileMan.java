package com.fx.entity.back;

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

import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.enums.ReqSrc;

/**
 * 文件管理类
 */
@Entity
@Table(name="file_man")
public class FileMan implements Serializable {
	private static final long serialVersionUID = 8619194646107747673L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 自定义文件id 唯一
	 */
	@Column(name="fid", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '自定义文件id'")
	private String fid;
	
	/**
	 * 文件名称
	 */
	@Column(name="fname", nullable=false, columnDefinition="varchar(500) COMMENT '文件名称'")
	private String fname;
	
	/**
	 * 所属文件夹：按照年月份2018/1/
	 */
	@Column(name="folder_name", nullable=false, columnDefinition="varchar(200) COMMENT '图片所属文件夹'")
	private String folderName;
	
	/**
	 * 上传来源
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="reqsrc", nullable=false, columnDefinition="varchar(20) COMMENT '上传来源'")
	private ReqSrc reqsrc;
	
	/**
	 * 文件类型
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="ftype", nullable=false, columnDefinition="varchar(20) COMMENT '图片所属类型'")
	private FileType ftype;
	
	/**
	 * 文件备用数据
	 */
	@Column(name="fdat", columnDefinition="text COMMENT '备用数据'")
	private String fdat;
	
	/** 上传时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '上传时间'")
	private Date atime;

	
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
	 * 获取 自定义文件id 唯一  
	 * @return fid
	 */
	public String getFid() {
		return fid;
	}
	

	/**  
	 * 设置 自定义文件id 唯一  
	 * @param fid
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}
	

	/**  
	 * 获取 文件名称  
	 * @return fname
	 */
	public String getFname() {
		return fname;
	}
	

	/**  
	 * 设置 文件名称  
	 * @param fname
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	

	/**  
	 * 获取 所属文件夹：按照年月份20181  
	 * @return folderName
	 */
	public String getFolderName() {
		return folderName;
	}
	

	/**  
	 * 设置 所属文件夹：按照年月份20181  
	 * @param folderName
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	

	/**  
	 * 获取 上传来源  
	 * @return reqsrc
	 */
	public ReqSrc getReqsrc() {
		return reqsrc;
	}
	

	/**  
	 * 设置 上传来源  
	 * @param reqsrc
	 */
	public void setReqsrc(ReqSrc reqsrc) {
		this.reqsrc = reqsrc;
	}
	

	/**  
	 * 获取 文件类型  
	 * @return ftype
	 */
	public FileType getFtype() {
		return ftype;
	}
	

	/**  
	 * 设置 文件类型  
	 * @param ftype
	 */
	public void setFtype(FileType ftype) {
		this.ftype = ftype;
	}
	

	/**  
	 * 获取 文件备用数据  
	 * @return fdat
	 */
	public String getFdat() {
		return fdat;
	}
	

	/**  
	 * 设置 文件备用数据  
	 * @param fdat
	 */
	public void setFdat(String fdat) {
		this.fdat = fdat;
	}
	

	/**  
	 * 获取 上传时间  
	 * @return atime
	 */
	public Date getAtime() {
		return atime;
	}
	

	/**  
	 * 设置 上传时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}