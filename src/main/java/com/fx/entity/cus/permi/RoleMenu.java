package com.fx.entity.cus.permi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色资源关联表
 */
@Entity
@Table(name="role_menu")
public class RoleMenu implements Serializable {
	private static final long serialVersionUID = 8458333352156262338L;
	
	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 角色ID */
	@Column(name="role_id", columnDefinition="bigint COMMENT '角色ID'")
	private long roleId;
	
	/** 关系类型：1-角色；2-用户； */
	@Column(name="type", nullable=false, columnDefinition="int COMMENT '关系类型：1-角色；2-用户；'")
	private int type;
	
	/** 菜单/按钮ID */
	@Column(name="menu_id", columnDefinition="bigint COMMENT '菜单/按钮ID'")
	private long menuId;
	

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
	 * @return the 关系类型：1-角色；2-用户；
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param 关系类型：1-角色；2-用户； the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**  
	 * 获取 菜单按钮ID  
	 * @return menuId
	 */
	public long getMenuId() {
		return menuId;
	}

	/**  
	 * 设置 菜单按钮ID  
	 * @param menuId
	 */
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
