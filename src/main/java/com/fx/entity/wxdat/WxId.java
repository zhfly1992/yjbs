package com.fx.entity.wxdat;

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

import com.fx.commons.utils.enums.CusRole;

/**
 * 用户关注的微信公众号id列表-类
 */
@Entity
@Table(name="wx_id")
public class WxId implements Serializable {
	private static final long serialVersionUID = 6990689936068299657L;
	private long id;
	/** 用户id */
	private String uid;
	/** 单位编号 */
	private String unitNum;
	/** 微信id */
	private String wxid;
	/** 添加时间 */
	private Date atime;
	/** 登录用户微信id */
	private String lwxid;
	/** 登录用户角色 */
	private CusRole lgRole;
	/** 登录坐标 */
	private String lgLonLat;
	/** 登录ip */
	private String lgIp;
	/** 登录时间 */
	private Date lgTime;
	
	
	/**
	 * @return id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**  
	 * 获取 用户id
	 * @return uid 
	 */
	@Column(name="uid", nullable=false, columnDefinition="varchar(30) COMMENT '用户id'")
	public String getUid() {
		return uid;
	}
	/**  
	 * 设置 用户id  
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**  
	 * 获取 单位编号  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	
	/**  
	 * 设置 单位编号  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	
	/**  
	 * 获取 微信id
	 * @return wxid 
	 */
	@Column(name="wxid", nullable=false, columnDefinition="varchar(50) COMMENT '微信id'")
	public String getWxid() {
		return wxid;
	}
	/**  
	 * 设置 微信id
	 * @param wxid
	 */
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	/**  
	 * 获取 登录用户角色
	 * @return lgRole  
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="lg_role", columnDefinition="varchar(20) COMMENT '登录用户角色'")
	public CusRole getLgRole() {
		return lgRole;
	}
	/**  
	 * 设置 登录用户角色
	 * @param lgRole
	 */
	public void setLgRole(CusRole lgRole) {
		this.lgRole = lgRole;
	}
	/**  
	 * 获取 创建时间  
	 * @return atime  
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '创建时间'")
	public Date getAtime() {
		return atime;
	}
	/**  
	 * 设置 创建时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	/**  
	 * 获取 登录用户微信id
	 * @return lwxid 
	 */
	@Column(name="lwxid", columnDefinition="varchar(50) COMMENT '登录用户微信id'")
	public String getLwxid() {
		return lwxid;
	}
	/**  
	 * 设置 登录用户微信id
	 * @param lwxid
	 */
	public void setLwxid(String lwxid) {
		this.lwxid = lwxid;
	}
	/**  
	 * 获取 登录位置坐标
	 * @return lgLonLat 
	 */
	@Column(name="lg_lon_lat", columnDefinition="varchar(50) COMMENT '登录位置坐标'")
	public String getLgLonLat() {
		return lgLonLat;
	}
	/**  
	 * 设置 登录位置坐标
	 * @param lgLonLat
	 */
	public void setLgLonLat(String lgLonLat) {
		this.lgLonLat = lgLonLat;
	}
	/**  
	 * 获取 登录Ip
	 * @return lgIp 
	 */
	@Column(name="lg_ip", columnDefinition="varchar(30) COMMENT '登录ip'")
	public String getLgIp() {
		return lgIp;
	}
	/**  
	 * 设置 登录Ip
	 * @param lgIp
	 */
	public void setLgIp(String lgIp) {
		this.lgIp = lgIp;
	}
	/**  
	 * 获取 登录时间
	 * @return lgTime  
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lg_time", columnDefinition="datetime COMMENT '登录时间'")
	public Date getLgTime() {
		return lgTime;
	}
	/**  
	 * 设置 登录时间
	 * @param lgTime
	 */
	public void setLgTime(Date lgTime) {
		this.lgTime = lgTime;
	}
	/**
	 * @return serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}