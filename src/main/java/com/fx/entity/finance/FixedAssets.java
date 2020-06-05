package com.fx.entity.finance;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.entity.cus.BaseUser;


/**
 *   固定资产表
 * @author xx
 * @date 20200508
 */
@Entity
@Table(name="fixed_assets")
public class FixedAssets implements Serializable{
	private static final long serialVersionUID = 5812470415766674634L;
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 资产名称 */
	@Column(name="assets_name", nullable=false, columnDefinition="varchar(100) COMMENT '资产名称'")
	private String assetsName;
	
	/** 资产类型 */
	@Column(name="assets_type", columnDefinition="varchar(50) COMMENT '资产类型'")
	private String assetsType;
	
	/** 产品编号（单位编号+系统生成产品编号） */
	@Column(name="assets_num",  columnDefinition="varchar(100) COMMENT '资产编号'")
	private String assetsNum;
	
	/** 单位(件，台，条) */
	@Column(name="assets_unit",  columnDefinition="varchar(20) COMMENT '单位(件，台，条)'")
	private String assetsUnit;
	
	/** 数量 */
	@Column(name="count", columnDefinition="int default 0 COMMENT '数量'")
	private int count;
	
	/** 单价 */
	@Column(name="price", columnDefinition="double(10,2) default '0.00' COMMENT '单价'")
	private double price;
	
	/** 折旧期(失效期，比如10个轮胎的资产好久折完) */
	@Column(name="valid_day",  columnDefinition="varchar(20) COMMENT '折旧期(失效期，比如10个轮胎的资产好久折完) '")
	private String validDay;
	
	/** 采购时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="buy_time", nullable=false, columnDefinition="datetime COMMENT '采购时间'")
	private Date buyTime;
	
	/** 采购人 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_buy", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '采购人信息'")
	private BaseUser baseUserBuy;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 操作备注 */
	@Column(name="oper_note",columnDefinition="text COMMENT '操作备注'")
	private String operNote;

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
	 * 获取 资产名称  
	 * @return assetsName
	 */
	public String getAssetsName() {
		return assetsName;
	}
	

	/**  
	 * 设置 资产名称  
	 * @param assetsName 
	 */
	public void setAssetsName(String assetsName) {
		this.assetsName = assetsName;
	}
	

	/**  
	 * 获取 资产类型  
	 * @return assetsType
	 */
	public String getAssetsType() {
		return assetsType;
	}
	

	/**  
	 * 设置 资产类型  
	 * @param assetsType 
	 */
	public void setAssetsType(String assetsType) {
		this.assetsType = assetsType;
	}
	

	/**  
	 * 获取 产品编号（单位编号+系统生成产品编号）  
	 * @return assetsNum
	 */
	public String getAssetsNum() {
		return assetsNum;
	}
	

	/**  
	 * 设置 产品编号（单位编号+系统生成产品编号）  
	 * @param assetsNum 
	 */
	public void setAssetsNum(String assetsNum) {
		this.assetsNum = assetsNum;
	}
	

	/**  
	 * 获取 单位(件，台，条)  
	 * @return assetsUnit
	 */
	public String getAssetsUnit() {
		return assetsUnit;
	}
	

	/**  
	 * 设置 单位(件，台，条)  
	 * @param assetsUnit 
	 */
	public void setAssetsUnit(String assetsUnit) {
		this.assetsUnit = assetsUnit;
	}
	

	/**  
	 * 获取 数量  
	 * @return count
	 */
	public int getCount() {
		return count;
	}
	

	/**  
	 * 设置 数量  
	 * @param count 
	 */
	public void setCount(int count) {
		this.count = count;
	}
	

	/**  
	 * 获取 单价  
	 * @return price
	 */
	public double getPrice() {
		return price;
	}
	

	/**  
	 * 设置 单价  
	 * @param price 
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	

	/**  
	 * 获取 折旧期(失效期，比如10个轮胎的资产好久折完)  
	 * @return validDay
	 */
	public String getValidDay() {
		return validDay;
	}
	

	/**  
	 * 设置 折旧期(失效期，比如10个轮胎的资产好久折完)  
	 * @param validDay 
	 */
	public void setValidDay(String validDay) {
		this.validDay = validDay;
	}
	

	/**  
	 * 获取 采购时间  
	 * @return buyTime
	 */
	public Date getBuyTime() {
		return buyTime;
	}
	

	/**  
	 * 设置 采购时间  
	 * @param buyTime 
	 */
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	

	/**  
	 * 获取 采购人  
	 * @return baseUserBuy
	 */
	public BaseUser getBaseUserBuy() {
		return baseUserBuy;
	}
	

	/**  
	 * 设置 采购人  
	 * @param baseUserBuy 
	 */
	public void setBaseUserBuy(BaseUser baseUserBuy) {
		this.baseUserBuy = baseUserBuy;
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
	 * 获取 操作备注  
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	

	/**  
	 * 设置 操作备注  
	 * @param operNote 
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
	
	
	
}