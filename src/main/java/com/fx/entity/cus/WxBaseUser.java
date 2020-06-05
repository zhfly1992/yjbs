package com.fx.entity.cus;

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
 * 微信用户基类
 */
@Entity
@Table(name="wx_base_user")
public class WxBaseUser implements Serializable {
	private static final long serialVersionUID = 8539604717270435107L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户名 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(30) COMMENT '用户名'")
	private String uname;
	
	/** 所属单位编号（一个用户只能在一个单位公众号绑定微信，即用户和单位编号为组合主键） */
	@Column(name="company_num", nullable=false, columnDefinition="varchar(30) COMMENT '所属单位编号'")
	private String companyNum;
	
	/** 用户微信openid */
	@Column(name="wxid", nullable=false, columnDefinition="varchar(50) COMMENT '用户微信openid'")
	private String wxid;

	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	/** 登录微信openid */
	@Column(name="lg_wxid", columnDefinition="varchar(50) COMMENT '登录微信openid'")
	private String lgWxid;
	
	/** 登录用户角色 */
	@Enumerated(EnumType.STRING)
	@Column(name="lg_role", columnDefinition="varchar(20) COMMENT '登录用户角色'")
	private CusRole lgRole;
	
	/** 登录坐标 eg：103.666666,30.666666 */
	@Column(name="lg_lng_lat", columnDefinition="varchar(30) COMMENT '登录坐标'")
	private String lgLngLat;
	
	/** 登录ip */
	@Column(name="lg_ip", columnDefinition="varchar(30) COMMENT '登录ip'")
	private String lgIp;
	
	/** 登录时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lg_time", columnDefinition="datetime COMMENT '登录时间'")
	private Date lgTime;
	

	/**  
	 * 获取 唯一id自增长主键  
	 * @return 唯一id自增长主键
	 */
	public long getId() {
		return id;
	}

	/**  
	 * 设置 唯一id自增长主键  
	 * @param 唯一id自增长主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**  
	 * 获取 用户名  
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}

	/**  
	 * 设置 用户名  
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}

	/**  
	 * 获取 所属单位编号（一个用户只能在一个单位公众号绑定微信，即用户和单位编号为组合主键） 
	 * @return companyNum
	 */
	public String getCompanyNum() {
		return companyNum;
	}

	/**  
	 * 设置 所属单位编号 （一个用户只能在一个单位公众号绑定微信，即用户和单位编号为组合主键）
	 * @param companyNum
	 */
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	/**  
	 * 获取 用户微信openid  
	 * @return wxid
	 */
	public String getWxid() {
		return wxid;
	}

	/**  
	 * 设置 用户微信openid  
	 * @param wxid
	 */
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	/**  
	 * 获取 添加时间  
	 * @return atime
	 */
	public Date getAtime() {
		return atime;
	}

	/**  
	 * 设置 添加时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}

	/**  
	 * 获取 登录微信openid  
	 * @return lgWxid
	 */
	public String getLgWxid() {
		return lgWxid;
	}

	/**  
	 * 设置 登录微信openid  
	 * @param lgWxid
	 */
	public void setLgWxid(String lgWxid) {
		this.lgWxid = lgWxid;
	}

	/**  
	 * 获取 登录用户角色  
	 * @return lgRole
	 */
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
	 * 获取 登录坐标eg：103.66666630.666666  
	 * @return lgLngLat
	 */
	public String getLgLngLat() {
		return lgLngLat;
	}

	/**  
	 * 设置 登录坐标eg：103.66666630.666666  
	 * @param lgLngLat
	 */
	public void setLgLngLat(String lgLngLat) {
		this.lgLngLat = lgLngLat;
	}

	/**  
	 * 获取 登录ip  
	 * @return lgIp
	 */
	public String getLgIp() {
		return lgIp;
	}

	/**  
	 * 设置 登录ip  
	 * @param lgIp
	 */
	public void setLgIp(String lgIp) {
		this.lgIp = lgIp;
	}

	/**  
	 * 获取 登录时间  
	 * @return lgTime
	 */
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
