package com.fx.entity.log;

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
 * 系统日志表
 */
@Entity
@Table(name="sys_log")
public class SysLog implements Serializable {
	private static final long serialVersionUID = 8642935769458674446L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 操作用户 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(30) COMMENT '操作用户'")
	private String uname;
	
	/** 操作描述 */
	@Column(name="oper_exp", columnDefinition="varchar(100) COMMENT '操作描述'")
	private String operExp;
	
	/** 操作耗时（毫秒） */
	@Column(name="oper_time", columnDefinition="bigint COMMENT '操作耗时（毫秒）'")
	private long operTime;
	
	/** 操作方法 */
	@Column(name="oper_method", columnDefinition="varchar(200) COMMENT '操作方法'")
	private String operMethod;
	
	/** 操作参数 */
	@Column(name="oper_params", columnDefinition="text COMMENT '操作参数'")
	private String operParams;
	
	/** IP地址 */
	@Column(name="oper_ip", columnDefinition="varchar(100) COMMENT 'IP地址'")
	private String operIp;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	/** 操作地点 */
	@Column(name="oper_location", columnDefinition="varchar(30) COMMENT '操作地点'")
	private String operLocation;

	
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
	 * 获取 操作用户  
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}
	

	/**  
	 * 设置 操作用户  
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	

	/**  
	 * 获取 操作描述  
	 * @return operExp
	 */
	public String getOperExp() {
		return operExp;
	}
	

	/**  
	 * 设置 操作描述  
	 * @param operExp
	 */
	public void setOperExp(String operExp) {
		this.operExp = operExp;
	}
	

	/**  
	 * 获取 操作耗时（毫秒）  
	 * @return operTime
	 */
	public long getOperTime() {
		return operTime;
	}
	

	/**  
	 * 设置 操作耗时（毫秒）  
	 * @param operTime
	 */
	public void setOperTime(long operTime) {
		this.operTime = operTime;
	}
	

	/**  
	 * 获取 操作方法  
	 * @return operMethod
	 */
	public String getOperMethod() {
		return operMethod;
	}
	

	/**  
	 * 设置 操作方法  
	 * @param operMethod
	 */
	public void setOperMethod(String operMethod) {
		this.operMethod = operMethod;
	}
	

	/**  
	 * 获取 操作参数  
	 * @return operParams
	 */
	public String getOperParams() {
		return operParams;
	}
	

	/**  
	 * 设置 操作参数  
	 * @param operParams
	 */
	public void setOperParams(String operParams) {
		this.operParams = operParams;
	}
	

	/**  
	 * 获取 IP地址  
	 * @return operIp
	 */
	public String getOperIp() {
		return operIp;
	}
	

	/**  
	 * 设置 IP地址  
	 * @param operIp
	 */
	public void setOperIp(String operIp) {
		this.operIp = operIp;
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
	 * 获取 操作地点  
	 * @return operLocation
	 */
	public String getOperLocation() {
		return operLocation;
	}
	

	/**  
	 * 设置 操作地点  
	 * @param operLocation
	 */
	public void setOperLocation(String operLocation) {
		this.operLocation = operLocation;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
