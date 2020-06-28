package com.fx.entity.cus;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 个人-用户
 */
@Entity
@Table(name="customer")
@JsonIgnoreProperties(value={"payPass"})// 序列化过滤字段
@JsonFilter("fitCustomer")
public class Customer implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 推荐用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="rec_base_user_id", referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '推荐用户基类用户名'")
	private BaseUser recBaseUserId;
	
	/** 推荐用户标志 */
	@Column(name="rec_id", nullable=false, columnDefinition="text COMMENT '推荐用户标志'")
	private String recId;
	
	/** 用户支付密码 */
	@Column(name="pay_pass", nullable=false, columnDefinition="varchar(32) COMMENT '用户支付密码'")
	private String payPass;
	
	
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
	 * 获取 推荐用户基类id  
	 * @return recBaseUserId
	 */
	public BaseUser getRecBaseUserId() {
		return recBaseUserId;
	}

	/**  
	 * 设置 推荐用户基类id  
	 * @param recBaseUserId
	 */
	public void setRecBaseUserId(BaseUser recBaseUserId) {
		this.recBaseUserId = recBaseUserId;
	}

	/**  
	 * 获取 推荐用户标志  
	 * @return recId
	 */
	public String getRecId() {
		return recId;
	}

	/**  
	 * 设置 推荐用户标志  
	 * @param recId
	 */
	public void setRecId(String recId) {
		this.recId = recId;
	}

	/**  
	 * 获取 用户名支付密码  
	 * @return payPass
	 */
	public String getPayPass() {
		return payPass;
	}

	/**  
	 * 设置 用户名支付密码  
	 * @param payPass
	 */
	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
