package com.fx.entity.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.ServiceType;

/**
 * 单程接送-订单参数-临时表
 */
@Entity
@Table(name="order_param")
public class OrderParam implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 操作账户 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(50) COMMENT '操作账户'")
	private String uname;
	
	/** 联系人手机号 */
	@Column(name="link_phone", nullable=false, columnDefinition="varchar(11) COMMENT '联系人手机号'")
	private String linkPhone;
	
	/** 账户所属单位编号 */
	@Column(name="company_num", nullable=false, columnDefinition="varchar(30) COMMENT '账户所属单位编号'")
	private String companyNum;
	
	/** 行程业务类型 省际业务 市际业务 县际业务 */
	@Enumerated(EnumType.STRING)
	@Column(name="service_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单业务类型'")
	private ServiceType serviceType;
	
	/** 出发时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stime", nullable=false, columnDefinition="datetime COMMENT '出发时间'")
	private Date stime;
	
	/** 到达时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="etime", nullable=false, columnDefinition="datetime COMMENT '到达时间'")
	private Date etime;
	
	/** 出发地点 eg：成都市 天府广场=103.123456,30.123456=四川省-成都市-青羊区 */
	@Column(name="spoint", nullable=false, columnDefinition="varchar(100) COMMENT '出发地点'")
	private String spoint;
	
	/** 到达地点 eg：成都市 天府广场=103.123456,30.123456=四川省-成都市-青羊区 */
	@Column(name="epoint", nullable=false, columnDefinition="varchar(100) COMMENT '到达地点'")
	private String epoint;
	
	/** 途径地点 eg：成都市 天府广场=103.123456,30.123456=四川省-成都市-青羊区;成都市 天府广场=103.123456,30.123456=四川省-成都市-青羊区; */
	@Column(name="wpoints", columnDefinition="text COMMENT '途径地点'")
	private String wpoints;
	
	/** 订单类型：1-接送机；2-接送火车；3-接送汽车； */
	@Column(name="otype", nullable=false, columnDefinition="int(11) COMMENT '订单类型：1-接送机；2-接送火车；3-接送汽车；'")
	private int otype;
	
	/** 是否接送：0-接；1-送； */
	@Column(name="is_shuttle", nullable=false, columnDefinition="int(11) COMMENT '是否接送'")
	private int isShuttle;
	
	/** 航班号 */
	@Column(name="flight_num", columnDefinition="varchar(50) COMMENT '航班号'")
	private String flightNum;
	
	/** 起飞或降落时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fdtime", columnDefinition="datetime COMMENT '起飞或降落时间'")
	private Date fdtime;
	
	/** 起止行程距离（公里） */
	@Column(name="distance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '起止行程距离（公里）'")
	private double distance;
	
	/** 起止行程距离耗时（分钟） */
	@Column(name="route_time", nullable=false, columnDefinition="int(11) default 0 COMMENT '起止行程距离耗时（分钟）'")
	private int routeTime;
	
	/** 使用优惠券id */
	@Column(name="coupon_id", columnDefinition="bigint COMMENT '使用优惠券id'")
	private long couponId;
	
	/** 优惠券使用场景：1-单程接送；2-旅游包车；3-顺风车； */
	@Column(name="use_case", nullable=false, columnDefinition="int(11) default 1 COMMENT '使用场景：1-单程接送；2-旅游包车；3-顺风车；'")
	private int useCase;
	
	/** 返程关联订单编号 */
	@Column(name="back_rel_num", columnDefinition="varchar(50) COMMENT '返程关联订单编号'")
	private String backRelNum;
	
	/** 行程详情 */
	@Column(name="route_detail", columnDefinition="text COMMENT '行程详情'")
	private String routeDetail;
	

	/**
	 * 获取 id
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置 key
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * 获取 操作账户
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}

	/**
	 * 设置 操作账户
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	/**
	 * 获取 联系人手机号
	 * @return linkPhone
	 */
	public String getLinkPhone() {
		return linkPhone;
	}

	/**
	 * 设置 联系人手机号
	 * @param linkPhone
	 */
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	/**  
	 * 获取 账户所属单位编号  
	 * @return companyNum
	 */
	public String getCompanyNum() {
		return companyNum;
	}

	/**  
	 * 设置 账户所属单位编号  
	 * @param companyNum
	 */
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	
	/**
	 * 获取 订单类型：1-接送机；2-接送火车；3-接送汽车；
	 * @return otype
	 */
	public int getOtype() {
		return otype;
	}

	/**
	 * 设置 出行方式：1-接送机；2-接送火车；3-接送汽车；
	 * @param otype
	 */
	public void setOtype(int otype) {
		this.otype = otype;
	}

	/**
	 * 获取 是否接送：0-接；1-送；
	 * @return isShuttle
	 */
	public int getIsShuttle() {
		return isShuttle;
	}

	/**
	 * 设置 是否接送：0-接；1-送；
	 * @param isShuttle
	 */
	public void setIsShuttle(int isShuttle) {
		this.isShuttle = isShuttle;
	}

	/**
	 * 获取 航班编号
	 * @return flightNum
	 */
	public String getFlightNum() {
		return flightNum;
	}

	/**
	 * 设置 航班编号
	 * @param flightNum
	 */
	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}

	/**
	 * 获取 起飞或降落时间
	 * @return fdtime
	 */
	public Date getFdtime() {
		return fdtime;
	}

	/**
	 * 设置 起飞或降落时间
	 * @param fdtime
	 */
	public void setFdtime(Date fdtime) {
		this.fdtime = fdtime;
	}
	
	/**
	 * 获取 起止行程距离（公里）
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * 设置 起止行程距离（公里）
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * 获取 起止行程距离耗时（分钟）
	 * @return routeTime
	 */
	public int getRouteTime() {
		return routeTime;
	}

	/**
	 * 设置 起止行程距离耗时（分钟）
	 * @param routeTime
	 */
	public void setRouteTime(int routeTime) {
		this.routeTime = routeTime;
	}

	/**
	 * 获取 出发时间
	 * @return stime
	 */
	public Date getStime() {
		return stime;
	}

	/**
	 * 设置 出发时间
	 * @param stime
	 */
	public void setStime(Date stime) {
		this.stime = stime;
	}

	/**
	 * 获取 到达时间
	 * @return etime
	 */
	public Date getEtime() {
		return etime;
	}

	/**
	 * 设置 到达时间
	 * @param etime
	 */
	public void setEtime(Date etime) {
		this.etime = etime;
	}
	
	/**
	 * 获取 优惠券id
	 * @return couponId
	 */
	public long getCouponId() {
		return couponId;
	}
	/**
	 * 设置 优惠券id
	 * @param couponId
	 */
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	/**
	 * 获取 返程关联订单编号
	 * @return backRelNum
	 */
	public String getBackRelNum() {
		return backRelNum;
	}
	
	/**
	 * 设置 返程关联订单编号
	 * @param backRelNum
	 */
	public void setBackRelNum(String backRelNum) {
		this.backRelNum = backRelNum;
	}
	
	/**  
	 * 获取 行程详情  
	 * @return routeDetail
	 */
	public String getRouteDetail() {
		return routeDetail;
	}

	/**  
	 * 设置 行程详情  
	 * @param routeDetail
	 */
	public void setRouteDetail(String routeDetail) {
		this.routeDetail = routeDetail;
	}

	/**  
	 * 获取 行程业务类型省际业务市际业务县际业务  
	 * @return serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	

	/**  
	 * 设置 行程业务类型省际业务市际业务县际业务  
	 * @param serviceType
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	

	/**  
	 * 获取 出发地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区  
	 * @return spoint
	 */
	public String getSpoint() {
		return spoint;
	}
	

	/**  
	 * 设置 出发地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区  
	 * @param spoint
	 */
	public void setSpoint(String spoint) {
		this.spoint = spoint;
	}
	

	/**  
	 * 获取 到达地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区  
	 * @return epoint
	 */
	public String getEpoint() {
		return epoint;
	}
	

	/**  
	 * 设置 到达地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区  
	 * @param epoint
	 */
	public void setEpoint(String epoint) {
		this.epoint = epoint;
	}
	

	/**  
	 * 获取 途径地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区;成都市天府广场=103.12345630.123456=四川省-成都市-青羊区;  
	 * @return wpoints
	 */
	public String getWpoints() {
		return wpoints;
	}
	

	/**  
	 * 设置 途径地点eg：成都市天府广场=103.12345630.123456=四川省-成都市-青羊区;成都市天府广场=103.12345630.123456=四川省-成都市-青羊区;  
	 * @param wpoints
	 */
	public void setWpoints(String wpoints) {
		this.wpoints = wpoints;
	}
	

	/**  
	 * 获取 优惠券使用场景：1-单程接送；2-旅游包车；3-顺风车；  
	 * @return useCase
	 */
	public int getUseCase() {
		return useCase;
	}
	

	/**  
	 * 设置 优惠券使用场景：1-单程接送；2-旅游包车；3-顺风车；  
	 * @param useCase
	 */
	public void setUseCase(int useCase) {
		this.useCase = useCase;
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
