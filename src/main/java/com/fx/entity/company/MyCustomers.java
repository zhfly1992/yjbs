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

import com.fx.entity.order.MapPoint;

/**
 * 我的合作客户表
 */
@Entity
@Table(name="my_customers")
public class MyCustomers implements Serializable {
	private static final long serialVersionUID = 3323813637641659935L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 我的用户名
	 */
	@Column(name="my_name", nullable=false, columnDefinition="varchar(50) COMMENT '我的用户名'")
	private String myName;
	/**
	 * 客户用户名
	 */
	@Column(name="cus_name", nullable=false, columnDefinition="varchar(50) COMMENT '客户用户名'")
	private String cusName;
	/**
	 * 客户真实姓名
	 */
	@Column(name="cus_relname", nullable=false, columnDefinition="varchar(50) COMMENT '客户真实姓名'")
	private String cusRelname;
	/**
	 * 客户手机号
	 */
	@Column(name="cus_phone", columnDefinition="varchar(11) COMMENT '客户手机号'")
	private String cusPhone;
	/**
	 * 客户级别
	 * 0直客
	 * 1大客户
	 * 2同行
	 */
	@Column(name="cus_level", nullable=false, columnDefinition="int default 0 COMMENT '客户级别:0直客 1大客户 2同行'")
	private int cusLevel;
	/**
	 * 客户角色
	 * 0个人  1旅行社 2车队(邀请添加)
	 */
	@Column(name="cus_role", columnDefinition="int default 0 COMMENT '客户角色  0个人  1旅行社 2车队'")
	private int cusRole;
	/**
	 * 客户单位名称
	 */
	@Column(name="cus_unitname", columnDefinition="varchar(200) COMMENT '客户单位名称'")
	private String cusUnitname;
	/**
	 * 诚信星级 0:无星 1:1星 2:2星
	 */
	@Column(name="cus_star_level", columnDefinition="int default 0 COMMENT '诚信星级：0无星 1星 2星'")
	private int cusStarLevel;
	/**
	 * 推荐人
	 */
	@Column(name="rec_name", columnDefinition="varchar(20) COMMENT '推荐人'")
	private String recName;
	/**
	 * 业务员
	 */
	@Column(name="service_man", columnDefinition="varchar(20) COMMENT '业务员'")
	private String serviceMan;
	/**
	 * 地图地点
	 */
	@OneToOne(targetEntity = MapPoint.class)
	@JoinColumn(name="map_point", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '地图地点'")
	private MapPoint mapPoint;
	/**
	 * 0否 1是
	 */
	@Column(name="is_internal", columnDefinition="int default 0 COMMENT '是否是内部账客户'")
	private int isInternal;
	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '创建时间'")
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
	 * 获取 我的用户名  
	 * @return myName
	 */
	public String getMyName() {
		return myName;
	}
	
	/**  
	 * 设置 我的用户名  
	 * @param myName
	 */
	public void setMyName(String myName) {
		this.myName = myName;
	}
	
	/**  
	 * 获取 客户用户名  
	 * @return cusName
	 */
	public String getCusName() {
		return cusName;
	}
	
	/**  
	 * 设置 客户用户名  
	 * @param cusName
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
	/**  
	 * 获取 客户真实姓名  
	 * @return cusRelname
	 */
	public String getCusRelname() {
		return cusRelname;
	}
	
	/**  
	 * 设置 客户真实姓名  
	 * @param cusRelname
	 */
	public void setCusRelname(String cusRelname) {
		this.cusRelname = cusRelname;
	}
	
	/**  
	 * 获取 客户手机号  
	 * @return cusPhone
	 */
	public String getCusPhone() {
		return cusPhone;
	}
	
	/**  
	 * 设置 客户手机号  
	 * @param cusPhone
	 */
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}
	
	/**  
	 * 获取 客户级别0直客1大客户2同行  
	 * @return cusLevel
	 */
	public int getCusLevel() {
		return cusLevel;
	}
	
	/**  
	 * 设置 客户级别0直客1大客户2同行  
	 * @param cusLevel
	 */
	public void setCusLevel(int cusLevel) {
		this.cusLevel = cusLevel;
	}
	
	/**  
	 * 获取 客户角色0个人1旅行社2车队(邀请添加)  
	 * @return cusRole
	 */
	public int getCusRole() {
		return cusRole;
	}
	
	/**  
	 * 设置 客户角色0个人1旅行社2车队(邀请添加)  
	 * @param cusRole
	 */
	public void setCusRole(int cusRole) {
		this.cusRole = cusRole;
	}
	
	/**  
	 * 获取 客户单位名称  
	 * @return cusUnitname
	 */
	public String getCusUnitname() {
		return cusUnitname;
	}
	
	/**  
	 * 设置 客户单位名称  
	 * @param cusUnitname
	 */
	public void setCusUnitname(String cusUnitname) {
		this.cusUnitname = cusUnitname;
	}
	
	/**  
	 * 获取 诚信星级0:无星1:1星2:2星  
	 * @return cusStarLevel
	 */
	public int getCusStarLevel() {
		return cusStarLevel;
	}
	
	/**  
	 * 设置 诚信星级0:无星1:1星2:2星  
	 * @param cusStarLevel
	 */
	public void setCusStarLevel(int cusStarLevel) {
		this.cusStarLevel = cusStarLevel;
	}
	
	/**  
	 * 获取 推荐人  
	 * @return recName
	 */
	public String getRecName() {
		return recName;
	}
	
	/**  
	 * 设置 推荐人  
	 * @param recName
	 */
	public void setRecName(String recName) {
		this.recName = recName;
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
	 * 获取 地图地点  
	 * @return mapPoint
	 */
	public MapPoint getMapPoint() {
		return mapPoint;
	}
	
	/**  
	 * 设置 地图地点  
	 * @param mapPoint
	 */
	public void setMapPoint(MapPoint mapPoint) {
		this.mapPoint = mapPoint;
	}
	
	/**  
	 * 获取 0否1是  
	 * @return isInternal
	 */
	public int getIsInternal() {
		return isInternal;
	}
	
	/**  
	 * 设置 0否1是  
	 * @param isInternal
	 */
	public void setIsInternal(int isInternal) {
		this.isInternal = isInternal;
	}
	
	/**  
	 * 获取 创建时间  
	 * @return atime
	 */
	public Date getAtime() {
		return atime;
	}
	
	/**  
	 * 设置 创建时间  
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