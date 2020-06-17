package com.fx.entity.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.ServiceType;


/**
 * 车辆主订单-表
 */
@Entity
@Table(name="main_car_order")
public class MainCarOrder implements Serializable {
	private static final long serialVersionUID = 47175572696246752L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 订单编号 */
	@Column(name="order_num", columnDefinition="varchar(50) COMMENT '订单编号'")
	private String orderNum;
	
	/** 订单基本信息 */
	@OneToOne(targetEntity = CarOrderBase.class,cascade = CascadeType.ALL)
	@JoinColumn(name="main_order_base", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT  '订单基本信息'")
	private CarOrderBase mainOrderBase;
	
	/** 所需车辆数 */
	@Column(name="need_cars", nullable=false, columnDefinition="int(11) default 0 COMMENT '所需车辆数'")
	private int needCars;
	
	/** 所需座位数：39,49,59 */
	@Column(name="need_seats", nullable=false, columnDefinition="varchar(120) COMMENT '所需座位数'")
	private String needSeats;
	
	/** 提醒师傅现收（元） 业务员填写了现收金额，则会提醒师傅现收 */
	@Column(name="rem_driver_charge", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '提醒师傅现收（元） '")
	private double remDriverCharge;
	
	/** 起点 */
	@OneToOne(targetEntity = MapPoint.class,cascade = CascadeType.ALL)
	@JoinColumn(name="spoint", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT  '起点'")
	private MapPoint spoint;
	
	/** 终点 */
	@OneToOne(targetEntity = MapPoint.class,cascade = CascadeType.ALL)
	@JoinColumn(name="epoint", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT  '终点'")
	private MapPoint epoint;
	
	/** 出发时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stime", nullable=false, columnDefinition="datetime COMMENT '出发时间'")
	private Date stime;
	
	/** 到达时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="etime", nullable=false, columnDefinition="datetime COMMENT '到达时间'")
	private Date etime;
	
	/** 行程详情,分号拼接 */
	@Column(name="route_detail", columnDefinition="text COMMENT '行程详情,分号拼接'")
	private String routeDetail;
	
	/** 订单价格（元） */
	@Column(name="price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '订单价格（元）'")
	private double price;
	
	/** 已收金额（元） */
	@Column(name="al_gath_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '已收金额（元）'")
	private double alGathPrice;
	
	/** 旅网金额（元） */
	@Column(name="travel_prepay_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '旅网金额（元）'")
	private double travelPrepayPrice;
	
	/** 自网金额（元） */
	@Column(name="self_prepay_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '自网金额（元）'")
	private double selfPrepayPrice;
	
	/** 订单业务类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="service_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单业务类型'")
	private ServiceType serviceType;
	
	/** 订单支付状态用于业务收款 */
	@Enumerated(EnumType.STRING)
	@Column(name="pay_status",columnDefinition="varchar(20) COMMENT '订单支付状态用于业务收款'")
	private OrderPayStatus payStatus;
	
	/** 是否外调 0-不外调；1-外调； */
	@Column(name="is_external", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否外调'")
	private int isExternal;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 派单车辆列表 */
	@OneToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
	@JoinColumn(name="main_order_id")
	private List<DisCarInfo> mainCars;
	
	/** 是否软删除 0-否；1-是；*/
	@Column(name="is_del", columnDefinition="tinyint(1) default 0 COMMENT '是否软删除'")
	private int isDel;
	
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
	 * 获取 订单编号  
	 * @return orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}

	/**  
	 * 设置 订单编号  
	 * @param orderNum 
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	


	/**  
	 * 获取 订单基本信息  
	 * @return mainOrderBase
	 */
	public CarOrderBase getMainOrderBase() {
		return mainOrderBase;
	}
	


	/**  
	 * 设置 订单基本信息  
	 * @param mainOrderBase 
	 */
	public void setMainOrderBase(CarOrderBase mainOrderBase) {
		this.mainOrderBase = mainOrderBase;
	}
	


	/**  
	 * 获取 所需车辆数  
	 * @return needCars
	 */
	public int getNeedCars() {
		return needCars;
	}
	


	/**  
	 * 设置 所需车辆数  
	 * @param needCars 
	 */
	public void setNeedCars(int needCars) {
		this.needCars = needCars;
	}
	
	/**  
	 * 获取 所需座位数：394959  
	 * @return needSeats
	 */
	public String getNeedSeats() {
		return needSeats;
	}
	


	/**  
	 * 设置 所需座位数：394959  
	 * @param needSeats 
	 */
	public void setNeedSeats(String needSeats) {
		this.needSeats = needSeats;
	}
	


	/**  
	 * 获取 旅网金额（元）  
	 * @return travelPrepayPrice
	 */
	public double getTravelPrepayPrice() {
		return travelPrepayPrice;
	}
	


	/**  
	 * 设置 旅网金额（元）  
	 * @param travelPrepayPrice 
	 */
	public void setTravelPrepayPrice(double travelPrepayPrice) {
		this.travelPrepayPrice = travelPrepayPrice;
	}
	


	/**  
	 * 获取 自网金额（元）  
	 * @return selfPrepayPrice
	 */
	public double getSelfPrepayPrice() {
		return selfPrepayPrice;
	}
	


	/**  
	 * 设置 自网金额（元）  
	 * @param selfPrepayPrice 
	 */
	public void setSelfPrepayPrice(double selfPrepayPrice) {
		this.selfPrepayPrice = selfPrepayPrice;
	}

	/**  
	 * 获取 提醒师傅现收（元）业务员填写了现收金额，则会提醒师傅现收  
	 * @return remDriverCharge
	 */
	public double getRemDriverCharge() {
		return remDriverCharge;
	}
	


	/**  
	 * 设置 提醒师傅现收（元）业务员填写了现收金额，则会提醒师傅现收  
	 * @param remDriverCharge 
	 */
	public void setRemDriverCharge(double remDriverCharge) {
		this.remDriverCharge = remDriverCharge;
	}
	


	/**  
	 * 获取 起点  
	 * @return spoint
	 */
	public MapPoint getSpoint() {
		return spoint;
	}
	


	/**  
	 * 设置 起点  
	 * @param spoint 
	 */
	public void setSpoint(MapPoint spoint) {
		this.spoint = spoint;
	}
	


	/**  
	 * 获取 终点  
	 * @return epoint
	 */
	public MapPoint getEpoint() {
		return epoint;
	}
	


	/**  
	 * 设置 终点  
	 * @param epoint 
	 */
	public void setEpoint(MapPoint epoint) {
		this.epoint = epoint;
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
	 * 获取 订单价格（元）  
	 * @return price
	 */
	public double getPrice() {
		return price;
	}
	


	/**  
	 * 设置 订单价格（元）  
	 * @param price 
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	


	/**  
	 * 获取 已收金额（元）  
	 * @return alGathPrice
	 */
	public double getAlGathPrice() {
		return alGathPrice;
	}
	


	/**  
	 * 设置 已收金额（元）  
	 * @param alGathPrice 
	 */
	public void setAlGathPrice(double alGathPrice) {
		this.alGathPrice = alGathPrice;
	}
	



	/**  
	 * 获取 订单业务类型  
	 * @return serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	


	/**  
	 * 设置 订单业务类型  
	 * @param serviceType 
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	


	/**  
	 * 获取 订单支付状态  
	 * @return payStatus
	 */
	public OrderPayStatus getPayStatus() {
		return payStatus;
	}
	


	/**  
	 * 设置 订单支付状态  
	 * @param payStatus 
	 */
	public void setPayStatus(OrderPayStatus payStatus) {
		this.payStatus = payStatus;
	}
	
	/**  
	 * 获取 是否外调0-不外调；1-外调；  
	 * @return isExternal
	 */
	public int getIsExternal() {
		return isExternal;
	}
	

	/**  
	 * 设置 是否外调0-不外调；1-外调；  
	 * @param isExternal 
	 */
	public void setIsExternal(int isExternal) {
		this.isExternal = isExternal;
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
	 * 获取 派单车辆列表  
	 * @return mainCars
	 */
	public List<DisCarInfo> getMainCars() {
		return mainCars;
	}
	


	/**  
	 * 设置 派单车辆列表  
	 * @param mainCars 
	 */
	public void setMainCars(List<DisCarInfo> mainCars) {
		this.mainCars = mainCars;
	}
	


	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getIsDel() {
		return isDel;
	}


	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	
	
	
	
}
