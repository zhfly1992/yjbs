package com.fx.entity.finance;

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
 *  凭证字列表
 *  @author xx
 *  @date 20200508
 */
@Entity
@Table(name="voucher_name")
public class VoucherName implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 凭证字 */
	@Column(name="voucher_name", nullable=false, columnDefinition="varchar(20) COMMENT '凭证字'")
	private String voucherName;

	/** 凭证标题 */
	@Column(name="voucher_title", nullable=false, columnDefinition="varchar(20) COMMENT '凭证标题'")
	private String voucherTitle;
	
	/** 默认  0否 1是*/
	@Column(name="is_default", nullable=false, columnDefinition="int(11) default 0 COMMENT '默认  0否 1是'")
	private int isDefault;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
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
	 * 获取 单位编号eg：Uxxxx  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	

	/**  
	 * 设置 单位编号eg：Uxxxx  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}

	/**  
	 * 获取 凭证字  
	 * @return voucherName
	 */
	public String getVoucherName() {
		return voucherName;
	}
	

	/**  
	 * 设置 凭证字  
	 * @param voucherName 
	 */
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	

	/**  
	 * 获取 凭证标题  
	 * @return voucherTitle
	 */
	public String getVoucherTitle() {
		return voucherTitle;
	}
	

	/**  
	 * 设置 凭证标题  
	 * @param voucherTitle 
	 */
	public void setVoucherTitle(String voucherTitle) {
		this.voucherTitle = voucherTitle;
	}
	

	/**  
	 * 获取 默认0否1是  
	 * @return isDefault
	 */
	public int getIsDefault() {
		return isDefault;
	}
	

	/**  
	 * 设置 默认0否1是  
	 * @param isDefault 
	 */
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	

	/**  
	 * 获取 添加时间  
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}
	

	/**  
	 * 设置 添加时间  
	 * @param addTime 
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	


}
