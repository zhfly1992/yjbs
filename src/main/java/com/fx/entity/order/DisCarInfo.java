package com.fx.entity.order;

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
 * 派单车辆信息表
 */
@Entity
@Table(name="dis_car_info")
public class DisCarInfo implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 车牌号 eg：川A12345、川D123456 */
	@Column(name="plate_num", nullable=false, columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	
	/** 座位数 */
	@Column(name="seats", nullable=false, columnDefinition="int(11) default 0 COMMENT '座位数'")
	private int seats;
	
	/** 主驾驶用户 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="main_driver",referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '主驾驶用户名'")
	private BaseUser main_driver;
	
	/** 副驾驶用户 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="vice_driver", referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '副驾驶用户名'")
	private BaseUser vice_driver;
	
	/** 主驾开始时间 eg：2020-03-12 10:00:00 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="main_driver_stime", columnDefinition="varchar(50) COMMENT '主驾开始时间'")
	private Date mainDriverStime;
	
	/** 主驾结束时间 eg：2020-03-12 10:00:00 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="main_driver_etime", columnDefinition="varchar(50) COMMENT '主驾结束时间'")
	private Date mainDriverEtime;
	
	/** 副驾开始时间 eg：2020-03-12 10:00:00 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="vice_driver_stime", columnDefinition="varchar(50) COMMENT '副驾开始时间'")
	private Date viceDriverStime;
	
	/** 副驾结束时间 eg：2020-03-12 10:00:00 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="vice_driver_etime", columnDefinition="varchar(50) COMMENT '副驾结束时间'")
	private Date viceDriverEtime;
	
	/** 外部车主副驾驶员信息 eg：主驾驶员手机号,姓名;副驾驶员手机号,姓名; */
	@Column(name="out_driver_info", columnDefinition="varchar(50) COMMENT '外部车主副驾驶员信息'")
	private String outDriverInfo;
	
	/** 供车方 提供车辆的用户，一般是单位 */
	@Column(name="supp_car", columnDefinition="varchar(50) COMMENT '供车方'")
	private String suppCar;
	
	/** 供车方负责人 提供车辆的用户负责人，一般是单位的负责人 */
	@Column(name="supp_car_head", columnDefinition="varchar(50) COMMENT '供车方负责人'")
	private String suppCarHead;
	
	/** 订单编号 唯一 */
	@Column(name="order_num", nullable=false, unique=true, columnDefinition="varchar(30) COMMENT '订单编号'")
	private String orderNum;

	
	public DisCarInfo() {}
	public DisCarInfo(long id) {
		super();
		this.id = id;
	}
	public DisCarInfo(String orderNum) {
		super();
		this.orderNum = orderNum;
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
	 * 获取 车牌号eg：川A12345、川D123456  
	 * @return plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	

	/**  
	 * 设置 车牌号eg：川A12345、川D123456  
	 * @param plateNum
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
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
	 * 获取 主驾驶用户  
	 * @return main_driver
	 */
	public BaseUser getMain_driver() {
		return main_driver;
	}
	

	/**  
	 * 设置 主驾驶用户  
	 * @param main_driver
	 */
	public void setMain_driver(BaseUser main_driver) {
		this.main_driver = main_driver;
	}
	

	/**  
	 * 获取 副驾驶用户  
	 * @return vice_driver
	 */
	public BaseUser getVice_driver() {
		return vice_driver;
	}
	

	/**  
	 * 设置 副驾驶用户  
	 * @param vice_driver
	 */
	public void setVice_driver(BaseUser vice_driver) {
		this.vice_driver = vice_driver;
	}
	


	


	

	/**  
	 * 获取 外部车主副驾驶员信息eg：主驾驶员手机号=姓名;副驾驶员手机号=姓名;  
	 * @return outDriverInfo
	 */
	public String getOutDriverInfo() {
		return outDriverInfo;
	}
	

	/**  
	 * 设置 外部车主副驾驶员信息eg：主驾驶员手机号=姓名;副驾驶员手机号=姓名;  
	 * @param outDriverInfo
	 */
	public void setOutDriverInfo(String outDriverInfo) {
		this.outDriverInfo = outDriverInfo;
	}
	

	/**  
	 * 获取 供车方提供车辆的用户，一般是单位  
	 * @return suppCar
	 */
	public String getSuppCar() {
		return suppCar;
	}
	

	/**  
	 * 设置 供车方提供车辆的用户，一般是单位  
	 * @param suppCar
	 */
	public void setSuppCar(String suppCar) {
		this.suppCar = suppCar;
	}
	

	/**  
	 * 获取 供车方负责人提供车辆的用户负责人，一般是单位的负责人  
	 * @return suppCarHead
	 */
	public String getSuppCarHead() {
		return suppCarHead;
	}
	

	/**  
	 * 设置 供车方负责人提供车辆的用户负责人，一般是单位的负责人  
	 * @param suppCarHead
	 */
	public void setSuppCarHead(String suppCarHead) {
		this.suppCarHead = suppCarHead;
	}
	
	
	

	/**
	 * @return the mainDriverStime
	 */
	public Date getMainDriverStime() {
		return mainDriverStime;
	}


	/**
	 * @param mainDriverStime the mainDriverStime to set
	 */
	public void setMainDriverStime(Date mainDriverStime) {
		this.mainDriverStime = mainDriverStime;
	}


	/**
	 * @return the mainDriverEtime
	 */
	public Date getMainDriverEtime() {
		return mainDriverEtime;
	}


	/**
	 * @param mainDriverEtime the mainDriverEtime to set
	 */
	public void setMainDriverEtime(Date mainDriverEtime) {
		this.mainDriverEtime = mainDriverEtime;
	}


	/**
	 * @return the viceDriverStime
	 */
	public Date getViceDriverStime() {
		return viceDriverStime;
	}


	/**
	 * @param viceDriverStime the viceDriverStime to set
	 */
	public void setViceDriverStime(Date viceDriverStime) {
		this.viceDriverStime = viceDriverStime;
	}


	/**
	 * @return the viceDriverEtime
	 */
	public Date getViceDriverEtime() {
		return viceDriverEtime;
	}


	/**
	 * @param viceDriverEtime the viceDriverEtime to set
	 */
	public void setViceDriverEtime(Date viceDriverEtime) {
		this.viceDriverEtime = viceDriverEtime;
	}


	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
	
}
