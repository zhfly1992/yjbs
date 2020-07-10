package com.fx.entity.back;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fx.commons.utils.enums.AdminRole;
import com.fx.entity.cus.BaseUser;

/**
 * 管理员-用户
 */
@Entity
@Table(name="sys_user")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 8459015851620411616L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 管理员角色 */
	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable=false, columnDefinition="varchar(20) COMMENT '管理员角色'")
	private AdminRole role;

	/** 昵称 */
	@Column(name="nick_name", columnDefinition="varchar(30) COMMENT '昵称'")
	private String nickName;
	
	/** 用户头像url */
	@Column(name="head_img", columnDefinition="varchar(200) COMMENT '用户头像url'")
	private String headImg;
	
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
	 * 获取 用户基类id  
	 * @return baseUserId
	 */
	public BaseUser getBaseUserId() {
		return baseUserId;
	}

	/**  
	 * 设置 用户基类id  
	 * @param baseUserId
	 */
	public void setBaseUserId(BaseUser baseUserId) {
		this.baseUserId = baseUserId;
	}

	/**  
	 * 获取 管理员角色  
	 * @return role
	 */
	public AdminRole getRole() {
		return role;
	}

	/**  
	 * 设置 管理员角色  
	 * @param role
	 */
	public void setRole(AdminRole role) {
		this.role = role;
	}
	
	/**  
	 * 获取 昵称  
	 * @return nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**  
	 * 设置 昵称  
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**  
	 * 获取 用户头像url  
	 * @return headImg
	 */
	public String getHeadImg() {
		return headImg;
	}

	/**  
	 * 设置 用户头像url  
	 * @param headImg
	 */
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
