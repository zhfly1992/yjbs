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
 * 旅游包车-订单参数-临时表
 */
@Entity
@Table(name="bc_order_param")
public class BcOrderParam implements Serializable {
	private static final long serialVersionUID = -2152130684950565272L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 操作账户 */
	@Column(name="uname", nullable=false, columnDefinition="varchar(50) COMMENT '操作账户'")
	private String uname;
	
	/** 主订单编号 */
	@Column(name="main_order_num", columnDefinition="varchar(50) COMMENT '主订单编号'")
	private String mainOrderNum;
	
	/** 联系人手机号 */
	@Column(name="link_phone", nullable=false, columnDefinition="varchar(11) COMMENT '联系人手机号'")
	private String linkPhone;
	
	/** 账户所属单位编号 */
	@Column(name="company_num", nullable=false, columnDefinition="varchar(30) COMMENT '账户所属单位编号'")
	private String companyNum;
	
	/** 行程数 */
	@Column(name="route_no", nullable=false, columnDefinition="int(11) default 0 COMMENT '行程数'")
	private int routeNo;
	
	/** 行程业务类型 省际业务 市际业务 县际业务 */
	@Enumerated(EnumType.STRING)
	@Column(name="service_type", nullable=false, columnDefinition="varchar(20) COMMENT '订单业务类型'")
	private ServiceType serviceType;
	
	/** 订单类型 1-接送机；2-接送火车；3-接送汽车； */
	@Column(name="otype", nullable=false, columnDefinition="int(11) default 0 COMMENT '订单类型'")
	private int otype;
	
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

	/** 过路费（元） */
	@Column(name="tolls", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '过路费（元）'")
	private double tolls;
	
	
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
	 * 获取 行程数  
	 * @return routeNo
	 */
	public int getRouteNo() {
		return routeNo;
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
	 * 获取 订单类型1-接送机；2-接送火车；3-接送汽车；  
	 * @return otype
	 */
	public int getOtype() {
		return otype;
	}
	

	/**  
	 * 设置 订单类型1-接送机；2-接送火车；3-接送汽车；  
	 * @param otype
	 */
	public void setOtype(int otype) {
		this.otype = otype;
	}
	

	/**  
	 * 设置 行程数  
	 * @param routeNo
	 */
	public void setRouteNo(int routeNo) {
		this.routeNo = routeNo;
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
	 * 获取 途径地点eg：成都市天府广场=103.123456,30.123456=四川省-成都市-青羊区;成都市天府广场=103.123456,30.123456=四川省-成都市-青羊区 ;  
	 * @return wpoints
	 */
	public String getWpoints() {
		return wpoints;
	}

	/**  
	 * 设置 途径地点eg：成都市天府广场=103.123456,30.123456=四川省-成都市-青羊区 ;成都市天府广场=103.123456,30.123456=四川省-成都市-青羊区 ;  
	 * @param wpoint
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
	 * 获取 过路费（元）  
	 * @return tolls
	 */
	public double getTolls() {
		return tolls;
	}

	/**  
	 * 设置 过路费（元）  
	 * @param tolls
	 */
	public void setTolls(double tolls) {
		this.tolls = tolls;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
