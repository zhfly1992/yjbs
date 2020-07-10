package com.fx.entity.cus;

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
 * 黑名单
 */
@Entity
@Table(name="black_list")
public class BlackList implements Serializable{
	private static final long serialVersionUID = -8608720980173688902L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 账号/IP
	 */
	@Column(name="uname", unique=true, columnDefinition="varchar(50) COMMENT '账号/IP'")
	private String uname;
	/**
	 * 备注
	 */
	@Column(name="note", columnDefinition="varchar(255) COMMENT '备注'")
	private String note;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", columnDefinition="datetime COMMENT '添加时间'")
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
	 * 获取 账号/IP 
	 * @return uname  
	 */
	public String getUname() {
		return uname;
	}
	/**  
	 * 获取 账号/IP
	 * @return uname  
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	/**  
	 * 获取 备注 
	 * @return note  
	 */
	public String getNote() {
		return note;
	}
	/**  
	 * 获取 备注 
	 * @return note  
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**  
	 * 获取 添加时间 
	 * @return atime  
	 */
	public Date getAtime() {
		return atime;
	}
	/**  
	 * 获取 添加时间
	 * @return atime  
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}