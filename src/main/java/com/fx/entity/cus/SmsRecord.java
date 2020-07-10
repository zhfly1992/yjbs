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
 * 短信发送-记录
 */
@Entity
@Table(name="sms_record")
public class SmsRecord implements Serializable{
	private static final long serialVersionUID = -1782171283638562761L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 发送IP
	 */
	@Column(name="sip", columnDefinition="varchar(50) COMMENT '发送IP'")
	private String sip;
	/**
	 * 发送方式
	 */
	@Column(name="sway", columnDefinition="varchar(500) COMMENT '发送方式'")
	private String sway;
	/**
	 * 请求地址
	 */
	@Column(name="requrl", columnDefinition="varchar(255) COMMENT '请求地址'")
	private String requrl;
	/**
	 * 发送手机号
	 */
	@Column(name="phone", columnDefinition="varchar(11) COMMENT '发送手机号'")
	private String phone;
	/**
	 * 发送内容
	 */
	@Column(name="context", columnDefinition="varchar(500) COMMENT '发送内容'")
	private String context;
	/**
	 * 发送状态：1-成功；0-失败；
	 */
	@Column(name="s_state", columnDefinition="int(11) default '0' COMMENT '发送状态：1-成功；0-失败；'")
	private int sState;
	/**
	 * 发送时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stime", columnDefinition="datetime COMMENT '发送时间'")
	private Date stime;
	
	
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
	 * 获取 发送IP 
	 * @return sip  
	 */
	public String getSip() {
		return sip;
	}
	/**  
	 * 设置 发送IP 
	 * @param sip
	 */
	public void setSip(String sip) {
		this.sip = sip;
	}
	/**  
	 * 获取 发送方式 
	 * @return sway 
	 */
	public String getSway() {
		return sway;
	}
	/**  
	 * 设置 请求地址
	 * @param requrl
	 */
	public void setRequrl(String requrl) {
		this.requrl = requrl;
	}
	/**  
	 * 获取 请求地址
	 * @return requrl 
	 */
	public String getRequrl() {
		return requrl;
	}
	/**  
	 * 设置 发送方式
	 * @param sway
	 */
	public void setSway(String sway) {
		this.sway = sway;
	}
	/**  
	 * 获取 发送手机号
	 * @return phone  
	 */
	public String getPhone() {
		return phone;
	}
	/**  
	 * 设置 发送手机号
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**  
	 * 获取 发送内容 
	 * @return context  
	 */
	public String getContext() {
		return context;
	}
	/**  
	 * 设置 发送状态：1-成功；0-失败；
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}
	/**  
	 * 获取 发送状态：1-成功；0-失败；
	 * @return sState  
	 */
	public int getsState() {
		return sState;
	}
	/**  
	 * 设置 发送内容
	 * @param sState
	 */
	public void setsState(int sState) {
		this.sState = sState;
	}
	/**  
	 * 获取 发送时间
	 * @return stime  
	 */
	public Date getStime() {
		return stime;
	}
	/**  
	 * 设置 发送时间 
	 * @param stime
	 */
	public void setStime(Date stime) {
		this.stime = stime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}