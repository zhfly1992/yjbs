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

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fx.commons.utils.enums.RegWay;
import com.fx.commons.utils.enums.UState;

/**
 * 用户基类
 */
@Entity
@Table(name="base_user")
@JsonIgnoreProperties(value={"lpass", "salt", "isDel"})// 序列化过滤字段
@JsonFilter("fitBaseUser")
public class BaseUser implements Serializable {
	private static final long serialVersionUID = -6297416804442451408L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户名 */
	@Column(name="uname", unique=true, columnDefinition="varchar(30) COMMENT '用户名'")
	private String uname;
	
	/** 用户手机号 */
	@Column(name="phone", unique=true, columnDefinition="varchar(11) COMMENT '用户手机号'")
	private String phone;
	
	/** 用户真实姓名 */
	@Column(name="real_name", columnDefinition="varchar(30) default '匿名' COMMENT '用户真实姓名'")
	private String realName;
	
	/** 用户登录密码 */
	@Column(name="lpass", nullable=false, columnDefinition="varchar(32) COMMENT '用户登录密码'")
	private String lpass;
	
	/** 盐值 */
	@Column(name="salt", columnDefinition="varchar(100) COMMENT '盐值'")
	private String salt;
	
	/** 用户注册方式 */
	@Enumerated(EnumType.STRING)
	@Column(name="reg_way", nullable=false, columnDefinition="varchar(20) COMMENT '用户注册方式'")
	private RegWay regWay;
	
	/** 用户状态 */
	@Enumerated(EnumType.STRING)
	@Column(name="ustate", nullable=false, columnDefinition="varchar(20) COMMENT '用户状态'")
	private UState ustate;
	
//	/** 性别 */
//	@Enumerated(EnumType.STRING)
//	@Column(name="sex", columnDefinition="varchar(20) COMMENT '性别'")
//	private Sex sex;
//	
//	/** 昵称 */
//	@Column(name="nick_name", columnDefinition="varchar(30) COMMENT '昵称'")
//	private String nickName;
//	
//	/** 用户头像url */
//	@Column(name="head_img", columnDefinition="varchar(200) COMMENT '用户头像url'")
//	private String headImg;

	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	/** 是否软删除 0-否；1-是； */
	@Column(name="is_del", columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否软删除'")
	private int isDel;
	
	
	public BaseUser() {}
	
	public BaseUser(String uname) {
		super();
		this.uname = uname;
	}

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
	 * 获取 用户手机号  
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**  
	 * 设置 用户手机号  
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**  
	 * 获取 用户真实姓名  
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**  
	 * 设置 用户真实姓名  
	 * @param realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	/**  
	 * 获取 用户登录密码  
	 * @return lpass
	 */
	public String getLpass() {
		return lpass;
	}

	/**  
	 * 设置 用户登录密码  
	 * @param lpass
	 */
	public void setLpass(String lpass) {
		this.lpass = lpass;
	}
	
	/**  
	 * 获取 用户注册方式  
	 * @return regWay
	 */
	public RegWay getRegWay() {
		return regWay;
	}

	/**  
	 * 设置 用户注册方式  
	 * @param regWay
	 */
	public void setRegWay(RegWay regWay) {
		this.regWay = regWay;
	}
	
	/**  
	 * 获取 用户状态
	 * @return ustate
	 */
	public UState getUstate() {
		return ustate;
	}
	
	/**  
	 * 设置 用户状态
	 * @param ustate
	 */
	public void setUstate(UState ustate) {
		this.ustate = ustate;
	}

//	/**  
//	 * 获取 性别  
//	 * @return sex
//	 */
//	public Sex getSex() {
//		return sex;
//	}
//
//	/**  
//	 * 设置 性别  
//	 * @param sex
//	 */
//	public void setSex(Sex sex) {
//		this.sex = sex;
//	}
//
//	/**  
//	 * 获取 昵称  
//	 * @return nickName
//	 */
//	public String getNickName() {
//		return nickName;
//	}
//
//	/**  
//	 * 设置 昵称  
//	 * @param nickName
//	 */
//	public void setNickName(String nickName) {
//		this.nickName = nickName;
//	}
//
//	/**  
//	 * 获取 用户头像url  
//	 * @return headImg
//	 */
//	public String getHeadImg() {
//		return headImg;
//	}
//
//	/**  
//	 * 设置 用户头像url  
//	 * @param headImg
//	 */
//	public void setHeadImg(String headImg) {
//		this.headImg = headImg;
//	}

	/**  
	 * 获取 盐值  
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}

	/**  
	 * 设置 盐值  
	 * @param salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
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
	 * 获取 是否软删除  
	 * @return isDel
	 */
	public int getIsDel() {
		return isDel;
	}

	/**  
	 * 设置 是否软删除  
	 * @param isDel
	 */
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
