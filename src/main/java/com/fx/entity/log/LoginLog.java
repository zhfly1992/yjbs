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
 * 用户登录日志表
 */
@Entity
@Table(name="login_log")
public class LoginLog implements Serializable {
	private static final long serialVersionUID = 8260460780240716902L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 操作用户 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(30) COMMENT '操作用户'")
	private String uname;
	
	/** IP地址 */
	@Column(name="oper_ip", columnDefinition="varchar(100) COMMENT 'IP地址'")
	private String operIp;
	
	/** 操作地点 */
	@Column(name="oper_location", columnDefinition="varchar(30) COMMENT '操作地点'")
	private String operLocation;
	
	/** 操作设备 */
	@Column(name="oper_device", columnDefinition="varchar(255) COMMENT '操作设备'")
	private String operDevice;
	
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
	 * 获取 操作设备  
	 * @return operDevice
	 */
	public String getOperDevice() {
		return operDevice;
	}
	

	/**  
	 * 设置 操作设备  
	 * @param operDevice
	 */
	public void setOperDevice(String operDevice) {
		this.operDevice = operDevice;
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
