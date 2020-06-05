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

import com.fx.commons.utils.enums.UState;

/**
 * 角色表
 */
@Entity
@Table(name="role")
public class Role implements Serializable {
	private static final long serialVersionUID = 2117541789820855814L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 角色名称 */
	@Column(name="name", nullable=false, columnDefinition="varchar(20) COMMENT '角色名称'")
	private String name;
	
	/** 角色描述 */
	@Column(name="description", columnDefinition="varchar(100) COMMENT '角色描述'")
	private String description;
	
	/** 状态 */
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable=false, columnDefinition="varchar(20) COMMENT '状态'")
	private UState status;
	
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
	 * 获取 角色名称  
	 * @return name
	 */
	public String getName() {
		return name;
	}
	

	/**  
	 * 设置 角色名称  
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**  
	 * 获取 角色描述  
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	

	/**  
	 * 设置 角色描述  
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	

	/**  
	 * 获取 状态  
	 * @return status
	 */
	public UState getStatus() {
		return status;
	}
	

	/**  
	 * 设置 状态  
	 * @param status
	 */
	public void setStatus(UState status) {
		this.status = status;
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
