package com.fx.entity.cus.permi;

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

import com.fx.commons.utils.enums.MenuType;
import com.fx.commons.utils.enums.ReqSrc;

/**
 * 资源表
 */
@Entity
@Table(name="menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = 2849466153797974181L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 所属系统 */
	@Enumerated(EnumType.STRING)
	@Column(name="systype", columnDefinition="varchar(20) COMMENT '所属系统'")
	private ReqSrc systype;
	
	/** 资源名称 */
	@Column(name="name", nullable=false, columnDefinition="varchar(30) COMMENT '资源名称'")
	private String name;
	
	/** 父级ID */
	@Column(name="pid", columnDefinition="bigint COMMENT '父级ID'")
	private long pid;
	
	/** 资源URL */
	@Column(name="url", columnDefinition="varchar(200) COMMENT '资源URL'")
	private String url;
	
	/** 权限标识 */
	@Column(name="perms", columnDefinition="text COMMENT '权限标志'")
	private String perms;
	
	/** 菜单类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="mtype", nullable=false, columnDefinition="varchar(20) COMMENT '状态'")
	private MenuType mtype;
	
	/** 菜单图标 */
	@Column(name="micon", columnDefinition="varchar(30) COMMENT '菜单图标'")
	private String micon;
	
	/** 菜单资源编号 */
	@Column(name="num", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '菜单资源编号'")
	private String num;
	
	/** 开关状态 1-开启；0-关闭； */
	@Column(name="ocState", nullable=false, columnDefinition="int default 1 COMMENT '开关状态 1-开启；0-关闭；'")
	private int ocState;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;

	
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
	 * @return the 所属系统
	 */
	public ReqSrc getSystype() {
		return systype;
	}

	/**
	 * @param 所属系统 the systype to set
	 */
	public void setSystype(ReqSrc systype) {
		this.systype = systype;
	}

	/**  
	 * 获取 操作用户  
	 * @return name
	 */
	public String getName() {
		return name;
	}
	

	/**  
	 * 设置 操作用户  
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**  
	 * 获取 父级ID  
	 * @return pid
	 */
	public long getPid() {
		return pid;
	}
	

	/**  
	 * 设置 父级ID  
	 * @param pid
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}
	

	/**  
	 * 获取 资源URL  
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	

	/**  
	 * 设置 资源URL  
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	

	/**  
	 * 获取 权限标志  
	 * @return perms
	 */
	public String getPerms() {
		return perms;
	}
	

	/**  
	 * 设置 权限标志  
	 * @param perms
	 */
	public void setPerms(String perms) {
		this.perms = perms;
	}

	/**
	 * @return the 菜单类型
	 */
	public MenuType getMtype() {
		return mtype;
	}

	/**
	 * @param 菜单类型 the mtype to set
	 */
	public void setMtype(MenuType mtype) {
		this.mtype = mtype;
	}

	/**  
	 * 获取 菜单图标  
	 * @return micon
	 */
	public String getMicon() {
		return micon;
	}
	

	/**  
	 * 设置 菜单图标  
	 * @param micon
	 */
	public void setMicon(String micon) {
		this.micon = micon;
	}
	
	/**
	 * 获取 资源编号
	 * @return num
	 */
	public String getNum() {
		return num;
	}
	/**
	 * 设置 资源编号
	 * @param num
	 */
	public void setNum(String num) {
		this.num = num;
	}


	/**
	 * @return the 开关状态1-开启；0-关闭；
	 */
	public int getOcState() {
		return ocState;
	}


	/**
	 * @param 开关状态1-开启；0-关闭； the ocState to set
	 */
	public void setOcState(int ocState) {
		this.ocState = ocState;
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
