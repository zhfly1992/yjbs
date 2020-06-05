package com.fx.entity.back;

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
 * 中国节假日列表
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="feast_day_list")
public class FeastDayList{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 节日名称
	 */
	@Column(name="f_name",nullable=false, columnDefinition="varchar(20) COMMENT '节日名称'")
	private String fName;
	/**
	 * 节日时间，eg:2017-05-08 00:00:00
	 * 实际时间：2017-05-08 00:00:00~2017-05-08 23:59:59
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="f_time",nullable=false, columnDefinition="datetime COMMENT '节日时间，eg:2017-05-08 00:00:00'")
	private Date fTime;
	/**
	 * 节日备注
	 */
	@Column(name="f_note", columnDefinition="varchar(100) COMMENT '节日备注'")
	private String fNote;
	
	
	/**  
	 * 获取id  
	 * @return id id  
	 */
	public long getId() {
		return id;
	}
	/**  
	 * 设置id  
	 * @param id id  
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**  
	 * 获取节日名称备注  
	 * @return fName 节日名称备注  
	 */
	public String getfName() {
		return fName;
	}
	/**  
	 * 设置节日名称备注  
	 * @param fName 节日名称备注  
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**  
	 * 获取节日时间，eg:2017-05-0800:00:00实际时间：2017-05-0800:00:00~2017-05-0823:59:59  
	 * @return fTime 节日时间，eg:2017-05-0800:00:00实际时间：2017-05-0800:00:00~2017-05-0823:59:59  
	 */
	public Date getfTime() {
		return fTime;
	}
	/**  
	 * 设置节日时间，eg:2017-05-0800:00:00实际时间：2017-05-0800:00:00~2017-05-0823:59:59  
	 * @param fTime 节日时间，eg:2017-05-0800:00:00实际时间：2017-05-0800:00:00~2017-05-0823:59:59  
	 */
	public void setfTime(Date fTime) {
		this.fTime = fTime;
	}
	/**  
	 * 获取节日备注  
	 * @return fNote 节日备注  
	 */
	public String getfNote() {
		return fNote;
	}
	/**  
	 * 设置节日备注  
	 * @param fNote 节日备注  
	 */
	public void setfNote(String fNote) {
		this.fNote = fNote;
	}
} 