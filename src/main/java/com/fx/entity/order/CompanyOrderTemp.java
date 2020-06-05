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
 * 单位往返订单-临时表
 */
@Entity
@Table(name="company_order_temp")
public class CompanyOrderTemp implements Serializable {
	private static final long serialVersionUID = -2152130684950565272L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 */
	@Column(name="unit_num", columnDefinition="varchar(50) COMMENT '单位编号'")
	private String unitNum;
	
	/** 主订单编号 */
	@Column(name="main_order_num", columnDefinition="varchar(50) COMMENT '主订单编号'")
	private String mainOrderNum;
	
	/** 需要车辆数 */
	@Column(name="need_cars", nullable=false, columnDefinition="int(11) default 0 COMMENT '需要车辆数'")
	private int needCars;
	
	/** 行程数 */
	@Column(name="route_no", nullable=false, columnDefinition="int(11) default 0 COMMENT '行程数'")
	private int routeNo;
	
	/** 行程业务类型 省际业务 市际业务 县际业务 */
	@Enumerated(EnumType.STRING)
	@Column(name="service_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单业务类型'")
	private ServiceType serviceType;
	
	/** 游玩类型 1-周边游；2-市内一日游；3-周边一日游； */
	@Column(name="yw_type", nullable=false, columnDefinition="int(11) default 0 COMMENT '游玩类型'")
	private int ywType;
	
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
	
	/** 途径城市 eg：成都市=德阳市 */
	@Column(name="way_city", columnDefinition="varchar(255) COMMENT '途径城市'")
	private String wayCity;
	
	/** 发单城市区县 eg：成都市-武侯区 */
	@Column(name="pu_county", columnDefinition="varchar(50) COMMENT '发单城市区县'")
	private String puCounty;
	
	/** 是否走高速 1-走高速；0-不走高速； */
	@Column(name="is_high_speed", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否走高速'")
	private int isHighSpeed;
	
	/** 起止行程距离（公里） */
	@Column(name="distance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '起止行程距离（公里）'")
	private double distance;
	
	/** 起止行程距离耗时（分钟） */
	@Column(name="route_time", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '起止行程距离耗时（分钟）'")
	private double routeTime;
	
	/** 其他费用（元） */
	@Column(name="other_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '其他费用（元）'")
	private double otherPrice;
	
	/** 其他费用说明 */
	@Column(name="other_price_note", columnDefinition="varchar(100) COMMENT '其他费用说明'")
	private String otherPriceNote;
	
	/** 座位数 */
	@Column(name="seats", nullable=false, columnDefinition="int(11) default 0 COMMENT '座位数'")
	private int seats;
	
	/** 行程价格（元） */
	@Column(name="route_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '行程价格（元）'")
	private double routePrice;
	
	/** 提醒师傅现收（元） */
	@Column(name="remind_route_cash", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '提醒师傅现收（元）'")
	private double remindRouteCash;
	
	/** 车辆限号 */
	@Column(name="limit_num", columnDefinition="varchar(20) COMMENT '车辆限号'")
	private String limitNum;
	
	/** 乘车联系人 eg:姓名-15999999999 */
	@Column(name="route_link", columnDefinition="varchar(20) COMMENT '乘车联系人'")
	private String routeLink;
	
	/** 行程详情 */
	@Column(name="route_detail", columnDefinition="text COMMENT '行程详情'")
	private String routeDetail;
	
	/** 备注 */
	@Column(name="note", columnDefinition="text COMMENT '备注'")
	private String note;

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
	 * 获取 主订单编号  
	 * @return mainOrderNum
	 */
	public String getMainOrderNum() {
		return mainOrderNum;
	}
	

	/**  
	 * 设置 主订单编号  
	 * @param mainOrderNum 
	 */
	public void setMainOrderNum(String mainOrderNum) {
		this.mainOrderNum = mainOrderNum;
	}
	
	/**  
	 * 获取 行程数  
	 * @return routeNo
	 */
	public int getRouteNo() {
		return routeNo;
	}
	

	/**  
	 * 获取 需要车辆数  
	 * @return needCars
	 */
	public int getNeedCars() {
		return needCars;
	}
	


	/**  
	 * 设置 需要车辆数  
	 * @param needCars 
	 */
	public void setNeedCars(int needCars) {
		this.needCars = needCars;
	}
	


	/**  
	 * 设置 行程数  
	 * @param routeNo 
	 */
	public void setRouteNo(int routeNo) {
		this.routeNo = routeNo;
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
	 * 获取 游玩类型1-周边游；2-市内一日游；3-周边一日游；  
	 * @return ywType
	 */
	public int getYwType() {
		return ywType;
	}
	

	/**  
	 * 设置 游玩类型1-周边游；2-市内一日游；3-周边一日游；  
	 * @param ywType 
	 */
	public void setYwType(int ywType) {
		this.ywType = ywType;
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
	 * 获取 途径城市eg：成都市=德阳市  
	 * @return wayCity
	 */
	public String getWayCity() {
		return wayCity;
	}
	

	/**  
	 * 设置 途径城市eg：成都市=德阳市  
	 * @param wayCity 
	 */
	public void setWayCity(String wayCity) {
		this.wayCity = wayCity;
	}
	

	/**  
	 * 获取 发单城市区县eg：成都市-武侯区  
	 * @return puCounty
	 */
	public String getPuCounty() {
		return puCounty;
	}
	

	/**  
	 * 设置 发单城市区县eg：成都市-武侯区  
	 * @param puCounty 
	 */
	public void setPuCounty(String puCounty) {
		this.puCounty = puCounty;
	}
	

	/**  
	 * 获取 是否走高速1-走高速；0-不走高速；  
	 * @return isHighSpeed
	 */
	public int getIsHighSpeed() {
		return isHighSpeed;
	}
	


	/**  
	 * 设置 是否走高速1-走高速；0-不走高速；  
	 * @param isHighSpeed 
	 */
	public void setIsHighSpeed(int isHighSpeed) {
		this.isHighSpeed = isHighSpeed;
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
	public double getRouteTime() {
		return routeTime;
	}
	

	/**  
	 * 设置 起止行程距离耗时（分钟）  
	 * @param routeTime 
	 */
	public void setRouteTime(double routeTime) {
		this.routeTime = routeTime;
	}
	

	/**  
	 * 获取 其他费用（元）  
	 * @return otherPrice
	 */
	public double getOtherPrice() {
		return otherPrice;
	}
	

	/**  
	 * 设置 其他费用（元）  
	 * @param otherPrice 
	 */
	public void setOtherPrice(double otherPrice) {
		this.otherPrice = otherPrice;
	}
	

	/**  
	 * 获取 其他费用说明  
	 * @return otherPriceNote
	 */
	public String getOtherPriceNote() {
		return otherPriceNote;
	}
	

	/**  
	 * 设置 其他费用说明  
	 * @param otherPriceNote 
	 */
	public void setOtherPriceNote(String otherPriceNote) {
		this.otherPriceNote = otherPriceNote;
	}
	

	/**  
	 * 获取 座位数  
	 * @return seats
	 */
	public int getSeats() {
		return seats;
	}
	

	/**  
	 * 设置 座位数  
	 * @param seats 
	 */
	public void setSeats(int seats) {
		this.seats = seats;
	}
	

	/**  
	 * 获取 行程价格（元）  
	 * @return routePrice
	 */
	public double getRoutePrice() {
		return routePrice;
	}
	

	/**  
	 * 设置 行程价格（元）  
	 * @param routePrice 
	 */
	public void setRoutePrice(double routePrice) {
		this.routePrice = routePrice;
	}
	

	/**  
	 * 获取 提醒师傅现收（元）  
	 * @return remindRouteCash
	 */
	public double getRemindRouteCash() {
		return remindRouteCash;
	}
	

	/**  
	 * 设置 提醒师傅现收（元）  
	 * @param remindRouteCash 
	 */
	public void setRemindRouteCash(double remindRouteCash) {
		this.remindRouteCash = remindRouteCash;
	}
	

	/**  
	 * 获取 车辆限号  
	 * @return limitNum
	 */
	public String getLimitNum() {
		return limitNum;
	}
	

	/**  
	 * 设置 车辆限号  
	 * @param limitNum 
	 */
	public void setLimitNum(String limitNum) {
		this.limitNum = limitNum;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * 获取 备注  
	 * @return note
	 */
	public String getNote() {
		return note;
	}
	


	/**  
	 * 设置 备注  
	 * @param note 
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	
	
}
