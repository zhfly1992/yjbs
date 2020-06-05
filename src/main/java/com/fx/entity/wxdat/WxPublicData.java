package com.fx.entity.wxdat;

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
 * 单位微信公众号数据
 */
@Entity
@Table(name="wx_public_data")
public class WxPublicData implements Serializable {
	private static final long serialVersionUID = -8305828061009272671L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 所属单位编号
	 */
	@Column(name="company_num", unique=true, columnDefinition="varchar(30) COMMENT '所属单位编号'")
	private String companyNum;
	/**
	 * 微信公众号名称
	 */
	@Column(name="wx_public_name", nullable=false, columnDefinition="varchar(50) COMMENT '微信公众号名称'")
	private String wxPublicName;
	/**
	 * 微信公众号AppID
	 */
	@Column(name="wx_app_id", nullable=false, columnDefinition="varchar(50) COMMENT '微信公众号AppID'")
	private String wxAppId;
	/**
	 *微信公众号AppSecret
	 */
	@Column(name="wx_app_secret", nullable=false, columnDefinition="varchar(50) COMMENT '微信公众号AppSecret'")
	private String wxAppSecret;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	
	/**  
	 * 获取 id  
	 * @return id
	 */
	public long getId() {
		return id;
	}
	
	/**  
	 * 设置 id  
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**  
	 * 获取 所属单位编号  
	 * @return companyNum
	 */
	public String getCompanyNum() {
		return companyNum;
	}

	/**  
	 * 设置 所属单位编号  
	 * @param companyNum
	 */
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	/**  
	 * 获取 微信公众号名称  
	 * @return wxPublicName
	 */
	public String getWxPublicName() {
		return wxPublicName;
	}
	
	/**  
	 * 设置 微信公众号名称  
	 * @param wxPublicName
	 */
	public void setWxPublicName(String wxPublicName) {
		this.wxPublicName = wxPublicName;
	}
	
	/**  
	 * 获取 微信公众号AppID  
	 * @return wxAppId
	 */
	public String getWxAppId() {
		return wxAppId;
	}
	
	/**  
	 * 设置 微信公众号AppID  
	 * @param wxAppId
	 */
	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}
	
	/**  
	 * 获取 微信公众号AppSecret  
	 * @return wxAppSecret
	 */
	public String getWxAppSecret() {
		return wxAppSecret;
	}
	
	/**  
	 * 设置 微信公众号AppSecret  
	 * @param wxAppSecret
	 */
	public void setWxAppSecret(String wxAppSecret) {
		this.wxAppSecret = wxAppSecret;
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
