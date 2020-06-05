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
import com.fx.commons.utils.enums.OrderStatus;
import com.fx.commons.utils.enums.ServiceType;

/**
 * 车辆订单-表
 */
@Entity
@Table(name="car_order")
public class CarOrder implements Serializable{
	private static final long serialVersionUID = 47175572696246752L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 订单基本信息 */
	@OneToOne(targetEntity = CarOrderBase.class,cascade = CascadeType.MERGE)
	@JoinColumn(name="car_order_base", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT  '订单基本信息'")
	private CarOrderBase carOrderBase;
	
	/** 主订单编号 */
	@Column(name="main_order_num", columnDefinition="varchar(50) COMMENT '主订单编号'")
	private String mainOrderNum;
	
	/** 订单编号 唯一 */
	@Column(name="order_num", nullable=false, unique=true, columnDefinition="varchar(30) COMMENT '账户所属订单编号'")
	private String orderNum;
	
	/** 返程关联订单编号 */
	@Column(name="back_rel_num", columnDefinition="varchar(50) COMMENT '返程关联订单编号'")
	private String backRelNum;
	
	/** 行程地点列表 */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="car_order_id")
	private List<RouteMapPoint> routeMps;
	
	/** 出发时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stime", nullable=false, columnDefinition="datetime COMMENT '出发时间'")
	private Date stime;
	
	/** 到达时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="etime", nullable=false, columnDefinition="datetime COMMENT '到达时间'")
	private Date etime;
	
	/** 派单车辆 */
	@OneToOne(targetEntity = DisCarInfo.class,cascade = CascadeType.ALL)
	@JoinColumn(name="dis_car_id", referencedColumnName="id", columnDefinition="bigint COMMENT  '派单车辆'")
	private DisCarInfo disCar;
	
	/** 当前行程所需车辆数 */
	@Column(name="need_cars", nullable=false, columnDefinition="int(11) default 0 COMMENT '当前行程所需车辆数'")
	private int needCars;
	
	/** 当前行程所需座位数 */
	@Column(name="need_seats", nullable=false, columnDefinition="int(11) default 0 COMMENT '当前行程所需座位数'")
	private int needSeats;
	
	/** 当前行程已派实际座位数 */
	@Column(name="real_seats", nullable=false, columnDefinition="int(11) default 0 COMMENT '当前行程已派实际座位数'")
	private int realSeats;
	
	/** 订单派车状态 */
	@Enumerated(EnumType.STRING)
	@Column(name="status",columnDefinition="varchar(30) COMMENT '订单派车状态'")
	private OrderStatus status;
	
	/** 订单支付状态用于业务付款 */
	@Enumerated(EnumType.STRING)
	@Column(name="pay_status",columnDefinition="varchar(20) COMMENT '订单支付状态用于业务付款'")
	private OrderPayStatus payStatus;
	
	/** 订单收款价格（元） */
	@Column(name="price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '订单收款价格（元）'")
	private double price;
	
	/** 派单价格（元） */
	@Column(name="dis_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '派单价格（元）'")
	private double disPrice;
	
	/** 已付金额（元） */
	@Column(name="al_pay_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '已付金额（元）'")
	private double alPayPrice;
	
	/** 旅网金额（元） */
	@Column(name="travel_prepay_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '旅网金额（元）'")
	private double travelPrepayPrice;
	
	/** 自网金额（元） */
	@Column(name="self_prepay_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '自网金额（元）'")
	private double selfPrepayPrice;
	
	/** 提醒师傅现收（元） 业务员填写了现收金额，则会提醒师傅现收 */
	@Column(name="rem_driver_charge", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '提醒师傅现收（元） '")
	private double remDriverCharge;
	
	/** 行程线路信息 */
	@OneToOne(targetEntity = RouteLineInfo.class,cascade = CascadeType.ALL)
	@JoinColumn(name="route_line_info", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '行程线路信息'")
	private RouteLineInfo routeLineInfo;
	
	/** 订单业务类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="service_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单业务类型'")
	private ServiceType serviceType;
	
	/** 接送类型 0-接；1-送；2-接/送； */
	@Column(name="is_shuttle", nullable=false, columnDefinition="int(11) COMMENT '接送类型'")
	private int isShuttle;
	
	/** 行程站点信息 */
	@OneToOne(targetEntity = RouteStationInfo.class,cascade = CascadeType.ALL)
	@JoinColumn(name="route_station_info", referencedColumnName="id", columnDefinition="bigint COMMENT '行程站点信息'")
	private RouteStationInfo routeStationInfo;
	
	/** 行程开支记录 */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="car_order_id")
	private List<RouteTradeList> trades;
	
	/** 确认付款人姓名*/
	@Column(name="confirm_payment_name", columnDefinition="varchar(20) COMMENT '确认付款人的姓名'")
	private String confirmPaymentName;
	
	/** 其他费用（元） */
	@Column(name="other_price", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '其他费用（元）'")
	private double otherPrice;
	
	/** 其他费用说明 */
	@Column(name="other_price_note", columnDefinition="varchar(100) COMMENT '其他费用说明'")
	private String otherPriceNote;
	
	/** 车辆限号 */
	@Column(name="limit_num", columnDefinition="varchar(20) COMMENT '车辆限号'")
	private String limitNum;
	
	/** 其他原因增加时间(小时) */
	@Column(name="reason_time", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '其他原因增加时间(小时)'")
	private double reasonTime;
	
	/** 行程详情 */
	@Column(name="route_detail", columnDefinition="text COMMENT '行程详情'")
	private String routeDetail;
	
	/** 备注 */
	@Column(name="note", columnDefinition="text COMMENT '备注'")
	private String note;
	
	/** 行程数 */
	@Column(name="route_no", nullable=false, columnDefinition="int(11) default 0 COMMENT '行程数'")
	private int routeNo;
	
	/** 是否外调 0-不外调；1-外调； */
	@Column(name="is_external", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否外调'")
	private int isExternal;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	/** 外调锁定/解锁人姓名 **/
	@Column(name="external_lock_man", columnDefinition="varchar(20) COMMENT '外调锁定/解锁人姓名'")
	private String externalLockMan;
	
	/** 是否软删除 0-否；1-是；*/
	@Column(name="is_del", columnDefinition="tinyint(1) default 0 COMMENT '是否软删除'")
	private int isDel;
	
	/** 是否走高速 1-走高速；0-不走高速； */
	@Column(name="is_high_speed", nullable=false, columnDefinition="int(11) default 0 COMMENT '是否走高速'")
	private int isHighSpeed;
	
	
	public CarOrder() {}
	public CarOrder(CarOrderBase carOrderBase) {
		super();
		this.carOrderBase = carOrderBase;
	}
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
	 * 获取 订单编号唯一  
	 * @return orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}

	/**  
	 * 设置 订单编号唯一  
	 * @param orderNum
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	 * 获取 订单基本信息  
	 * @return carOrderBase
	 */
	public CarOrderBase getCarOrderBase() {
		return carOrderBase;
	}
	

	/**  
	 * 设置 订单基本信息  
	 * @param carOrderBase 
	 */
	public void setCarOrderBase(CarOrderBase carOrderBase) {
		this.carOrderBase = carOrderBase;
	}
	

	/**  
	 * 获取 行程地点列表  
	 * @return routeMps
	 */

	public List<RouteMapPoint> getRouteMps() {
		return routeMps;
	}

	/**  
	 * 设置 行程地点列表  
	 * @param routeMps
	 */
	public void setRouteMps(List<RouteMapPoint> routeMps) {
		this.routeMps = routeMps;
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
	 * 获取 派单车辆  
	 * @return disCar
	 */
	public DisCarInfo getDisCar() {
		return disCar;
	}
	

	/**  
	 * 设置 派单车辆  
	 * @param disCar 
	 */
	public void setDisCar(DisCarInfo disCar) {
		this.disCar = disCar;
	}
	

	/**  
	 * 获取 当前行程所需车辆数  
	 * @return needCars
	 */
	public int getNeedCars() {
		return needCars;
	}

	/**  
	 * 设置 当前行程所需车辆数  
	 * @param needCars
	 */
	public void setNeedCars(int needCars) {
		this.needCars = needCars;
	}

	/**  
	 * 获取 当前行程所需座位数  
	 * @return needSeats
	 */
	public int getNeedSeats() {
		return needSeats;
	}

	/**  
	 * 设置 当前行程所需座位数  
	 * @param needSeats
	 */
	public void setNeedSeats(int needSeats) {
		this.needSeats = needSeats;
	}

	/**  
	 * 获取 当前行程所需实际座位数  
	 * @return realSeats
	 */
	public int getRealSeats() {
		return realSeats;
	}

	/**  
	 * 设置 当前行程所需实际座位数  
	 * @param realSeats
	 */
	public void setRealSeats(int realSeats) {
		this.realSeats = realSeats;
	}

	/**  
	 * 获取 订单状态  
	 * @return status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**  
	 * 设置 订单状态  
	 * @param status
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
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
	 * 获取 派单价格（元）  
	 * @return disPrice
	 */
	public double getDisPrice() {
		return disPrice;
	}

	/**  
	 * 设置 派单价格（元）  
	 * @param disPrice
	 */
	public void setDisPrice(double disPrice) {
		this.disPrice = disPrice;
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
	 * 获取 接送类型0-接；1-送；2-接送；  
	 * @return isShuttle
	 */
	public int getIsShuttle() {
		return isShuttle;
	}
	

	/**  
	 * 设置 接送类型0-接；1-送；2-接送；  
	 * @param isShuttle
	 */
	public void setIsShuttle(int isShuttle) {
		this.isShuttle = isShuttle;
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
	 * 获取 行程线路信息  
	 * @return routeLineInfo
	 */
	public RouteLineInfo getRouteLineInfo() {
		return routeLineInfo;
	}

	/**  
	 * 设置 行程线路信息  
	 * @param routeLineInfo
	 */
	public void setRouteLineInfo(RouteLineInfo routeLineInfo) {
		this.routeLineInfo = routeLineInfo;
	}

	/**  
	 * 获取 行程站点信息  
	 * @return routeStationInfo
	 */
	public RouteStationInfo getRouteStationInfo() {
		return routeStationInfo;
	}

	/**  
	 * 设置 行程站点信息  
	 * @param routeStationInfo
	 */
	public void setRouteStationInfo(RouteStationInfo routeStationInfo) {
		this.routeStationInfo = routeStationInfo;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取 行程收支记录  
	 * @return trades
	 */
	public List<RouteTradeList> getTrades() {
		return trades;
	}
	

	/**  
	 * 设置 行程收支记录  
	 * @param trades 
	 */
	public void setTrades(List<RouteTradeList> trades) {
		this.trades = trades;
	}

	/**  
	 * 获取 已付金额（元）  
	 * @return alPayPrice
	 */
	public double getAlPayPrice() {
		return alPayPrice;
	}
	

	/**  
	 * 设置 已付金额（元）  
	 * @param alPayPrice 
	 */
	public void setAlPayPrice(double alPayPrice) {
		this.alPayPrice = alPayPrice;
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
	 * 获取 确认付款人姓名  
	 * @return confirmPaymentName
	 */
	public String getConfirmPaymentName() {
		return confirmPaymentName;
	}
	

	/**  
	 * 设置 确认付款人姓名  
	 * @param confirmPaymentName 
	 */
	public void setConfirmPaymentName(String confirmPaymentName) {
		this.confirmPaymentName = confirmPaymentName;
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

	/**  
	 * 获取 其他原因增加时间(小时)  
	 * @return reasonTime
	 */
	public double getReasonTime() {
		return reasonTime;
	}
	

	/**  
	 * 设置 其他原因增加时间(小时)  
	 * @param reasonTime 
	 */
	public void setReasonTime(double reasonTime) {
		this.reasonTime = reasonTime;
	}

	/**  
	 * 获取 行程数  
	 * @return routeNo
	 */
	public int getRouteNo() {
		return routeNo;
	}
	

	/**  
	 * 设置 行程数  
	 * @param routeNo 
	 */
	public void setRouteNo(int routeNo) {
		this.routeNo = routeNo;
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
	 * @return the externalLockMan
	 */
	public String getExternalLockMan() {
		return externalLockMan;
	}

	/**
	 * @param externalLockMan the externalLockMan to set
	 */
	public void setExternalLockMan(String externalLockMan) {
		this.externalLockMan = externalLockMan;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
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
}
