package com.fx.entity.company;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.BusinessType;
import com.fx.commons.utils.enums.CarNature;
import com.fx.commons.utils.enums.VehiclePowerSourceType;
import com.fx.commons.utils.enums.VehicleType;
import com.fx.entity.cus.BaseUser;

/**
 * 单位车辆
 */
@Entity
@Table(name="company_vehicle")
public class CompanyVehicle {
	
	public CompanyVehicle() {}
	public CompanyVehicle(int seats) {
		this.seats = seats;
	}
	public CompanyVehicle(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public CompanyVehicle(long id, int seats, String plateNumber) {
		super();
		this.id = id;
		this.seats = seats;
		this.plateNumber = plateNumber;
	}
	
	private static final long serialVersionUID = -4886305101465804656L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    /** 车牌号 eg：川A 88888 */
    @Column(name="plate_number",nullable=false, columnDefinition="varchar(12) COMMENT '车牌号'")
    private String plateNumber;

    /** 座位数 eg：40 */
    @Column(name="seats",nullable=false, columnDefinition="int COMMENT '座位数'")
    private Integer seats;

    /** 车辆类型
     * 0:大巴车
	 * 1:中巴车
	 * 2:商务车
	 * 3:越野车
	 * 4:轿车
	 */
    @Enumerated(EnumType.STRING)
    @Column(name="vehicle_type",nullable=false, columnDefinition="varchar(20) COMMENT '车辆类型'")
    private VehicleType vehicleType;

    /** 是否自营 eg：MYSELF */
    @Enumerated(EnumType.STRING)
    @Column(name="business_type",nullable=false, columnDefinition="varchar(20) COMMENT '是否自营：MYSELF自营 NOTSELF挂靠'")
    private BusinessType businessType;

    /** 车辆状态 eg：车辆状态 */
    @Column(name="status",nullable=false, columnDefinition="tinyint(1) DEFAULT '0' COMMENT '车辆状态 0正常，1维修，2报停'")
    private Integer status;
    
    /** 状态时间段 eg：2020-04-26 12:00-2020-04-26 14:00 */
	@Column(name="status_timeslot", columnDefinition="varchar(50) COMMENT '状态时间段，若是正常则为null'")
    private String  statusTimeslot;

	/** 购买日期 eg：2020 04/09 11:11 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="purchase_date",nullable=false, columnDefinition="datetime COMMENT '购买日期'")
    private Date purchaseDate;

    /** 车辆品牌ID  */
    @Column(name="brand_id",nullable=false, columnDefinition="int(11) COMMENT '车辆品牌ID'")
    private Integer brandId;

    /** 车辆性质 eg：旅游客运 */
    @Enumerated(EnumType.STRING)
    @Column(name="car_usage",nullable=false, columnDefinition="varchar(20) COMMENT '车辆性质'")
    private CarNature carUsage;
    
	/** 关联公司ID*/
    @Column(name="company_id",nullable=false, columnDefinition="bigint(11) COMMENT '关联公司ID'")
    private Long companyId;

    /** 动力来源*/
    @Enumerated(EnumType.STRING)
    @Column(name="power_source",nullable=false, columnDefinition="varchar(12) COMMENT '动力来源'")
    private VehiclePowerSourceType powerSource;

    /** 续航里程 eg：100 */
    @Column(name="mileage",nullable=false, columnDefinition="float COMMENT '续航里程'")
    private Float mileage;

    /** 正常油耗 eg：Uxxxx */
    @Column(name="fuel",nullable=false, columnDefinition="float COMMENT '正常油耗'")
    private Float fuel;

    /** 停靠地址全称 */
    @Column(name="docked_address",nullable=false, columnDefinition="varchar(20) COMMENT '停靠地址全称'")
    private String dockedAddress;

    /** 停靠地址简称 */
    @Column(name="simple_docked_address",nullable=false, columnDefinition="varchar(20) COMMENT '停靠地址简称'")
    private String simpleDockedAddress;

    /** 停靠地址纬度*/
    @Column(name="docked_latitude",nullable=false, columnDefinition="float COMMENT '停靠地址纬度'")
    private Double dockedLatitude;

    /** 停靠地址经度*/
    @Column(name="docked_longitude",nullable=false, columnDefinition="float COMMENT '停靠地址经度'")
    private Double dockedLongitude;

    /** 行驶证照 eg：Uxxxx */
    @Column(name="travel_license_photo_url",nullable=false, columnDefinition="varchar(512) COMMENT '行驶证照url'")
    private String travelLicensePhotoURL;

    /** 驾照需求 eg：A1 */
    @Column(name="driving_type",nullable=false, columnDefinition="varchar(12) COMMENT '驾照需求'")
    private String drivingType;
    
    /** 主驾驶人(用户基类id) */
	@OneToOne(targetEntity = BaseUser.class)
//	@JoinColumn(name="base_user_id", nullable=false, unique=true, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '用户基类用户名'")
	@JoinColumn(name="base_user_id", referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '主驾驶人，用户基类用户名'")
	private BaseUser baseUserId;
	
	/** 所属公司 eg：A1 */
    @Column(name="company_name", columnDefinition="varchar(100) COMMENT '所属公司全称'")
	private String belongCompanyName;
    
	/** 所属公司简称 eg：A1 */
    @Column(name="company_simple_name", columnDefinition="varchar(100) COMMENT '所属公司简称'")
	private String belongComapnySimName;
    
    /** 创建单位编号*/
    @Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
    private String unitNum;
	
    /** 可跑区域 */
    /**
     * 0-不限区域
     * 1-省际包车
     * 2-市级包车
     * 3-县级包车
     */
    @Column(name="running_area",nullable=false, columnDefinition="tinyint(1) COMMENT '可跑区域'")
    private Integer runningArea;


	/**
	 * @return the belongCompanyName
	 */
	public String getBelongCompanyName() {
		return belongCompanyName;
	}

	/**
	 * @param belongCompanyName the belongCompanyName to set
	 */
	public void setBelongCompanyName(String belongCompanyName) {
		this.belongCompanyName = belongCompanyName;
	}

	/**
	 * @return the belongComapnySimName
	 */
	public String getBelongComapnySimName() {
		return belongComapnySimName;
	}

	/**
	 * @param belongComapnySimName the belongComapnySimName to set
	 */
	public void setBelongComapnySimName(String belongComapnySimName) {
		this.belongComapnySimName = belongComapnySimName;
	}

	/**
	 * @return the unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}

	/**
	 * @param unitNum the unitNum to set
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}



    /**
	 * @return the carUsage
	 */
	public CarNature getCarUsage() {
		return carUsage;
	}

	/**
	 * @param carUsage the carUsage to set
	 */
	public void setCarUsage(CarNature carUsage) {
		this.carUsage = carUsage;
	}

	/**
	 * @return the powerSource
	 */
	public VehiclePowerSourceType getPowerSource() {
		return powerSource;
	}

	/**
	 * @param powerSource the powerSource to set
	 */
	public void setPowerSource(VehiclePowerSourceType powerSource) {
		this.powerSource = powerSource;
	}

	/**
	 * @return the mileage
	 */
	public Float getMileage() {
		return mileage;
	}

	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(Float mileage) {
		this.mileage = mileage;
	}



	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}

	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	/**
	 * @return the seats
	 */
	public Integer getSeats() {
		return seats;
	}

	/**
	 * @param seats the seats to set
	 */
	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	/**
	 * @return the vehicleType
	 */
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType the vehicleType to set
	 */
	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return the businessType
	 */
	public BusinessType getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the purchaseDate
	 */
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate the purchaseDate to set
	 */
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the brandId
	 */
	public Integer getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}



	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}


	/**
	 * @return the fuel
	 */
	public Float getFuel() {
		return fuel;
	}

	/**
	 * @param fuel the fuel to set
	 */
	public void setFuel(Float fuel) {
		this.fuel = fuel;
	}

	/**
	 * @return the dockedAddress
	 */
	public String getDockedAddress() {
		return dockedAddress;
	}

	/**
	 * @param dockedAddress the dockedAddress to set
	 */
	public void setDockedAddress(String dockedAddress) {
		this.dockedAddress = dockedAddress;
	}

	/**
	 * @return the simpleDockedAddress
	 */
	public String getSimpleDockedAddress() {
		return simpleDockedAddress;
	}

	/**
	 * @param simpleDockedAddress the simpleDockedAddress to set
	 */
	public void setSimpleDockedAddress(String simpleDockedAddress) {
		this.simpleDockedAddress = simpleDockedAddress;
	}

	/**
	 * @return the dockedLatitude
	 */
	public Double getDockedLatitude() {
		return dockedLatitude;
	}

	/**
	 * @param dockedLatitude the dockedLatitude to set
	 */
	public void setDockedLatitude(Double dockedLatitude) {
		this.dockedLatitude = dockedLatitude;
	}

	/**
	 * @return the dockedLongitude
	 */
	public Double getDockedLongitude() {
		return dockedLongitude;
	}

	/**
	 * @param dockedLongitude the dockedLongitude to set
	 */
	public void setDockedLongitude(Double dockedLongitude) {
		this.dockedLongitude = dockedLongitude;
	}

	/**
	 * @return the travelLicensePhotoURL
	 */
	public String getTravelLicensePhotoURL() {
		return travelLicensePhotoURL;
	}

	/**
	 * @param travelLicensePhotoURL the travelLicensePhotoURL to set
	 */
	public void setTravelLicensePhotoURL(String travelLicensePhotoURL) {
		this.travelLicensePhotoURL = travelLicensePhotoURL;
	}

	/**
	 * @return the drivingType
	 */
	public String getDrivingType() {
		return drivingType;
	}

	/**
	 * @param drivingType the drivingType to set
	 */
	public void setDrivingType(String drivingType) {
		this.drivingType = drivingType;
	}

	/**
	 * @return the baseUserId
	 */
	public BaseUser getBaseUserId() {
		return baseUserId;
	}

	/**
	 * @param baseUserId the baseUserId to set
	 */
	public void setBaseUserId(BaseUser baseUserId) {
		this.baseUserId = baseUserId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    /**
	 * @return the statusTimeslot
	 */
	public String getStatusTimeslot() {
		return statusTimeslot;
	}

	/**
	 * @param statusTimeslot the statusTimeslot to set
	 */
	public void setStatusTimeslot(String statusTimeslot) {
		this.statusTimeslot = statusTimeslot;
	}
	
    /**
	 * @return the runningArea
	 */
	public Integer getRunningArea() {
		return runningArea;
	}

	/**
	 * @param runningArea the runningArea to set
	 */
	public void setRunningArea(Integer runningArea) {
		this.runningArea = runningArea;
	}
	
}
