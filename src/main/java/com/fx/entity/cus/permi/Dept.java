package com.fx.entity.cus.permi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * 部门表
 */
@Entity
@Table(name="dept")
@JsonFilter("fitDept")
public class Dept implements Serializable {
	private static final long serialVersionUID = -7332193134306390909L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 所属单位编号 */
	@Column(name="unit_num", nullable=false, columnDefinition="varchar(30) COMMENT '所属单位编号'")
	private String unitNum;
	
	/** 部门名称 */
	@Column(name="name", nullable=false,unique=true, columnDefinition="varchar(30) COMMENT '部门名称'")
	private String name;
	
	/** 父级ID */
	@Column(name="pid", columnDefinition="bigint COMMENT '父级ID'")
	private long pid;
	
	/** 角色列表 */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="dept_role_id")
	private List<Role> deptRoleId;
	
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
	 * @return the 所属单位编号
	 */
	public String getUnitNum() {
		return unitNum;
	}
	

	/**
	 * @param 所属单位编号 the unitNum to set
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	

	/**  
	 * 获取 部门名称  
	 * @return name
	 */
	public String getName() {
		return name;
	}
	

	/**  
	 * 设置 部门名称  
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
	 * @return the 角色列表
	 */
	public List<Role> getDeptRoleId() {
		return deptRoleId;
	}
	


	/**
	 * @param 角色列表 the deptRoleId to set
	 */
	public void setDeptRoleId(List<Role> deptRoleId) {
		this.deptRoleId = deptRoleId;
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
