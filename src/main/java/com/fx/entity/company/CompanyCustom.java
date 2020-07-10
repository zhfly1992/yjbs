package com.fx.entity.company;

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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.JSONArray;
import com.fx.entity.cus.BaseUser;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

/**
 * 单位的客户
 */
@Entity
@Table(name="company_custom")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CompanyCustom implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
//	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	@JoinColumn(name="base_user_id", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 客户单位全称 */
	@Column(name="unit_name", nullable=false, columnDefinition="varchar(100) COMMENT '客户单位全称'")
	private String unitName;
	
	/** 客户类型 */
	@OneToOne(targetEntity = CustomType.class)
	@JoinColumn(name="cus_type_id", referencedColumnName="id", columnDefinition="bigint COMMENT '客户类型id'")
	private CustomType cusTypeId;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 是否挂靠 1是*/
	@Column(name="is_depend",nullable=false, columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否挂靠 1是'")
	private int isDepend;
	
	/** 客户地址描述 */
	@Column(name="address", columnDefinition="varchar(100) COMMENT '客户地址描述'")
	private String address;
	
	/** 客户地址坐标 */
	@Column(name="address_lonlat", columnDefinition="varchar(100) COMMENT '客户地址坐标'")
	private String addressLonlat;
	
	/** 职务 */
	@Column(name="cus_role", nullable=false, columnDefinition="varchar(50) COMMENT '职务'")
	private String cusRole;
	
	/** 业务员 */
	@Column(name="service_man", nullable=false, columnDefinition="varchar(50) COMMENT '业务员'")
	private String serviceMan;

	/** 推荐人 */
	@Column(name="recom_man", nullable=false, columnDefinition="varchar(50) COMMENT '推荐人'")
	private String recomMan;
	
	/** 是否软删除 */
	@Column(name="is_del", columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否软删除'")
	private int isDel;
	
	/** 供应商属性 */
	
	/** 服务内容 */
	@Column(name="service_content", columnDefinition="varchar(255) COMMENT '服务内容'")
	private String serviceContent;
	
	/** 单位简称 */
	@Column(name="unit_simple", columnDefinition="varchar(10) COMMENT '单位简称'")
	private String unitSimple;
	
	/** 营业执照号 */
	@Column(name="business_num", columnDefinition="varchar(100) COMMENT '营业执照号'")
	private String businessNum;
	
	/** 身份证号 */
	@Column(name="id_card", columnDefinition="varchar(25) COMMENT '身份证号'")
	private String idCard;
	
	/** 身份证正面url */
	@Column(name="id_card_front_img", columnDefinition="varchar(200) COMMENT '身份证正面url'")
	private String idCardFrontImg;
	
	/** 身份证背面url */
	@Column(name="id_card_back_img", columnDefinition="varchar(200) COMMENT '身份证背面url'")
	private String idCardBackImg;
	
	/** 营业执照url */
	@Column(name="business_img", columnDefinition="varchar(200) COMMENT '营业执照url'")
	private String businessImg;
	/** 业务负责人*/
	@Type(type = "json")
	@Column(name="person_in_charge",columnDefinition="text COMMENT '业务负责人'")
	private JSONArray personInCharge;
	
	/** 是否为供应商*/
	@Column(name="is_supply",columnDefinition="tinyint(1) DEFAULT '0' COMMENT '是否供应商,0不是,1是'")
	private int isSupply;
	
	/** 客户预存款余额:元 */
	@Column(name="pre_money", columnDefinition="double(10,2) default '0.00' COMMENT '客户才有预存款余额'")
	private double preMoney;
	
	/** 驾驶员押金余额:元 */
	@Column(name="deposit_money", columnDefinition="double(10,2) default '0.00' COMMENT '驾驶员才有押金余额'")
	private double depositMoney;

	/** 员工借款余额:元 */
	@Column(name="borrow_money", columnDefinition="double(10,2) default '0.00' COMMENT '员工才有借款余额'")
	private double borrowMoney;
	
	





	/**
	 * @return the isSupply
	 */
	public int getIsSupply() {
		return isSupply;
	}




	/**
	 * @param isSupply the isSupply to set
	 */
	public void setIsSupply(int isSupply) {
		this.isSupply = isSupply;
	}




	/**
	 * @return the personInCharg
	 */
	public JSONArray getPersonInCharge() {
		return personInCharge;
	}




	/**
	 * @param personInCharg the personInCharg to set
	 */
	public void setPersonInCharge(JSONArray personInCharge) {
		this.personInCharge = personInCharge;
	}




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
	 * 获取 客户单位全称  
	 * @return unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	

	/**  
	 * 设置 客户单位全称  
	 * @param unitName 
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	

	/**  
	 * 获取 客户类型  
	 * @return cusTypeId
	 */
	public CustomType getCusTypeId() {
		return cusTypeId;
	}
	




	/**  
	 * 设置 客户类型  
	 * @param cusTypeId
	 */
	public void setCusTypeId(CustomType cusTypeId) {
		this.cusTypeId = cusTypeId;
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
	 * 获取 是否挂靠1是  
	 * @return isDepend
	 */
	public int getIsDepend() {
		return isDepend;
	}
	

	/**  
	 * 设置 是否挂靠1是  
	 * @param isDepend 
	 */
	public void setIsDepend(int isDepend) {
		this.isDepend = isDepend;
	}
	

	/**  
	 * 获取 客户地址描述  
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	

	/**  
	 * 设置 客户地址描述  
	 * @param address 
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	

	/**  
	 * 获取 客户地址坐标  
	 * @return addressLonlat
	 */
	public String getAddressLonlat() {
		return addressLonlat;
	}
	

	/**  
	 * 设置 客户地址坐标  
	 * @param addressLonlat 
	 */
	public void setAddressLonlat(String addressLonlat) {
		this.addressLonlat = addressLonlat;
	}
	

	/**  
	 * 获取 职务  
	 * @return cusRole
	 */
	public String getCusRole() {
		return cusRole;
	}
	

	/**  
	 * 设置 职务  
	 * @param cusRole 
	 */
	public void setCusRole(String cusRole) {
		this.cusRole = cusRole;
	}
	

	/**  
	 * 获取 业务员  
	 * @return serviceMan
	 */
	public String getServiceMan() {
		return serviceMan;
	}
	

	/**  
	 * 设置 业务员  
	 * @param serviceMan 
	 */
	public void setServiceMan(String serviceMan) {
		this.serviceMan = serviceMan;
	}
	

	/**  
	 * 获取 推荐人  
	 * @return recomMan
	 */
	public String getRecomMan() {
		return recomMan;
	}
	

	/**  
	 * 设置 推荐人  
	 * @param recomMan 
	 */
	public void setRecomMan(String recomMan) {
		this.recomMan = recomMan;
	}
	

	/**  
	 * 获取 是否软删除  
	 * @return isDel
	 */
	public int getIsDel() {
		return isDel;
	}
	

	/**  
	 * 设置 是否软删除  
	 * @param isDel 
	 */
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	

	/**  
	 * 获取 服务内容  
	 * @return serviceContent
	 */
	public String getServiceContent() {
		return serviceContent;
	}
	

	/**  
	 * 设置 服务内容  
	 * @param serviceContent 
	 */
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	

	/**  
	 * 获取 单位简称  
	 * @return unitSimple
	 */
	public String getUnitSimple() {
		return unitSimple;
	}
	

	/**  
	 * 设置 单位简称  
	 * @param unitSimple 
	 */
	public void setUnitSimple(String unitSimple) {
		this.unitSimple = unitSimple;
	}
	

	/**  
	 * 获取 营业执照号  
	 * @return businessNum
	 */
	public String getBusinessNum() {
		return businessNum;
	}
	

	/**  
	 * 设置 营业执照号  
	 * @param businessNum 
	 */
	public void setBusinessNum(String businessNum) {
		this.businessNum = businessNum;
	}
	

	/**  
	 * 获取 身份证号  
	 * @return idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	

	/**  
	 * 设置 身份证号  
	 * @param idCard 
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	

	/**  
	 * 获取 身份证正面url  
	 * @return idCardFrontImg
	 */
	public String getIdCardFrontImg() {
		return idCardFrontImg;
	}
	

	/**  
	 * 设置 身份证正面url  
	 * @param idCardFrontImg 
	 */
	public void setIdCardFrontImg(String idCardFrontImg) {
		this.idCardFrontImg = idCardFrontImg;
	}
	

	/**  
	 * 获取 身份证背面url  
	 * @return idCardBackImg
	 */
	public String getIdCardBackImg() {
		return idCardBackImg;
	}
	

	/**  
	 * 设置 身份证背面url  
	 * @param idCardBackImg 
	 */
	public void setIdCardBackImg(String idCardBackImg) {
		this.idCardBackImg = idCardBackImg;
	}
	

	/**  
	 * 获取 营业执照url  
	 * @return businessImg
	 */
	public String getBusinessImg() {
		return businessImg;
	}
	

	/**  
	 * 设置 营业执照url  
	 * @param businessImg 
	 */
	public void setBusinessImg(String businessImg) {
		this.businessImg = businessImg;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	/**  
	 * 获取 客户预存款余额:元  
	 * @return preMoney
	 */
	public double getPreMoney() {
		return preMoney;
	}
	




	/**  
	 * 设置 客户预存款余额:元  
	 * @param preMoney 
	 */
	public void setPreMoney(double preMoney) {
		this.preMoney = preMoney;
	}
	




	/**  
	 * 获取 驾驶员押金余额:元  
	 * @return depositMoney
	 */
	public double getDepositMoney() {
		return depositMoney;
	}
	




	/**  
	 * 设置 驾驶员押金余额:元  
	 * @param depositMoney 
	 */
	public void setDepositMoney(double depositMoney) {
		this.depositMoney = depositMoney;
	}
	




	/**  
	 * 获取 员工借款余额:元  
	 * @return borrowMoney
	 */
	public double getBorrowMoney() {
		return borrowMoney;
	}
	




	/**  
	 * 设置 员工借款余额:元  
	 * @param borrowMoney 
	 */
	public void setBorrowMoney(double borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	

}
