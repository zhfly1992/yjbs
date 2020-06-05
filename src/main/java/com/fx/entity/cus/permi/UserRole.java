package com.fx.entity.cus.permi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户角色关联表
 */
@Entity
@Table(name="user_role")
public class UserRole implements Serializable {
	private static final long serialVersionUID = -6499024278286676825L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户ID */
	@Column(name="user_id", columnDefinition="bigint COMMENT '用户ID'")
	private long userId;
	
	/** 角色ID */
	@Column(name="role_id", columnDefinition="bigint COMMENT '角色ID'")
	private long roleId;

	
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
	 * 获取 用户ID  
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}
	

	/**  
	 * 设置 用户ID  
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	

	/**  
	 * 获取 角色ID  
	 * @return roleId
	 */
	public long getRoleId() {
		return roleId;
	}
	

	/**  
	 * 设置 角色ID  
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
