package com.fx.entity.order;

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

import com.fx.commons.utils.enums.MainOrderStatus;
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.RouteType;
import com.fx.entity.cus.BaseUser;

/**
 * 订单基本信息表
 */
@Entity
@Table(name="car_order_base")
public class CarOrderBase implements Serializable {
	private static final long serialVersionUID = 47175572696246752L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 */
	@Column(name="unit_num", nullable=false, columnDefinition="varchar(30) COMMENT '单位编号'")
	private String unitNum;
	
	/** 主订单用车状态 */
	@Enumerated(EnumType.STRING)
	@Column(name="status",columnDefinition="varchar(30) COMMENT '主订单用车状态'")
	private MainOrderStatus status;
	
	/** 用户基类id */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="base_user_id", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 用车单位名称 */
	@Column(name="company_name", nullable=false, columnDefinition="varchar(50) COMMENT '单位名称'")
	private String companyName;
	
	/** 用车业务负责人 姓名 */
	@Column(name="duty_service", nullable=false, columnDefinition="varchar(20) COMMENT '用车业务负责人 姓名'")
	private String dutyService;
	
	/** 乘车联系人 eg:姓名-15999999999 */
	@Column(name="route_link", columnDefinition="varchar(20) COMMENT '乘车联系人'")
	private String routeLink;
	
	/** 订单来源 */
	@Enumerated(EnumType.STRING)
	@Column(name="order_source",columnDefinition="varchar(30) COMMENT '订单来源'")
	private OrderSource orderSource;
	
	/** 业务员 姓名 */
	@Column(name="service_man", nullable=false, columnDefinition="varchar(20) COMMENT '业务员 姓名'")
	private String serviceMan;
	
	/** 确认收款人姓名*/
	@Column(name="confirm_collection_name", columnDefinition="varchar(20) COMMENT '确认收款人的姓名'")
	private String confirmCollectionName;
	
	/** 订单行程类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="route_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单行程类型'")
	private RouteType routeType;
	
	
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
	 * 获取 单位编号  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	

	/**  
	 * 设置 单位编号  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	
	/**  
	 * 获取 主订单用车状态  
	 * @return status
	 */
	public MainOrderStatus getStatus() {
		return status;
	}
	


	/**  
	 * 设置主 订单用车状态  
	 * @param status 
	 */
	public void setStatus(MainOrderStatus status) {
		this.status = status;
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
	 * 获取 用车单位名称  
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	

	/**  
	 * 设置 用车单位名称  
	 * @param companyName 
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	

	/**  
	 * 获取 用车业务负责人姓名  
	 * @return dutyService
	 */
	public String getDutyService() {
		return dutyService;
	}
	

	/**  
	 * 设置 用车业务负责人姓名  
	 * @param dutyService 
	 */
	public void setDutyService(String dutyService) {
		this.dutyService = dutyService;
	}
	

	/**  
	 * 获取 乘车联系人eg:姓名-15999999999  
	 * @return routeLink
	 */
	public String getRouteLink() {
		return routeLink;
	}
	

	/**  
	 * 设置 乘车联系人eg:姓名-15999999999  
	 * @param routeLink 
	 */
	public void setRouteLink(String routeLink) {
		this.routeLink = routeLink;
	}
	

	/**  
	 * 获取 订单来源  
	 * @return orderSource
	 */
	public OrderSource getOrderSource() {
		return orderSource;
	}
	

	/**  
	 * 设置 订单来源  
	 * @param orderSource 
	 */
	public void setOrderSource(OrderSource orderSource) {
		this.orderSource = orderSource;
	}
	

	/**  
	 * 获取 业务员姓名  
	 * @return serviceMan
	 */
	public String getServiceMan() {
		return serviceMan;
	}
	

	/**  
	 * 设置 业务员姓名  
	 * @param serviceMan 
	 */
	public void setServiceMan(String serviceMan) {
		this.serviceMan = serviceMan;
	}
	

	/**  
	 * 获取 订单行程类型  
	 * @return routeType
	 */
	public RouteType getRouteType() {
		return routeType;
	}
	

	/**  
	 * 设置 订单行程类型  
	 * @param routeType 
	 */
	public void setRouteType(RouteType routeType) {
		this.routeType = routeType;
	}
	



	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取 确认收款人姓名  
	 * @return confirmCollectionName
	 */
	public String getConfirmCollectionName() {
		return confirmCollectionName;
	}
	

	/**  
	 * 设置 确认收款人姓名  
	 * @param confirmCollectionName 
	 */
	public void setConfirmCollectionName(String confirmCollectionName) {
		this.confirmCollectionName = confirmCollectionName;
	}
	
	

}
