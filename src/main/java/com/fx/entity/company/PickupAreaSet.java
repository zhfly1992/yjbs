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
 * 车队接送区域设置表
 */
@Entity
@Table(name="pickup_area_set")
public class PickupAreaSet implements Serializable {
	private static final long serialVersionUID = -8131518901508572417L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 操作用户名
	 */
	@Column(name="uname", columnDefinition="varchar(20) COMMENT '操作用户名'")
	private String uname;
	/**
	 * 可跑区域名称(可任意设置)
	 */
	@Column(name="area_name", columnDefinition="varchar(20) COMMENT '可跑区域名称(可任意设置)'")
	private String areaName;
	/**
	 * 地图地点
	 */
	@OneToOne(targetEntity = MapPoint.class)
	@JoinColumn(name="map_point", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '地图地点'")
	private MapPoint mapPoint;
	/**
	 * 正常可跑区域范围
	 * 单位：米
	 */
	@Column(name="def_area_round", nullable=false, columnDefinition="int default 0 COMMENT '正常可跑区域范围(单位：米)'")
	private int defAreaRound;
	/**
	 * 区域是否开启
	 * 0:关闭
	 * 1:开启
	 */
	@Column(name="is_open", columnDefinition="tinyint(1) default 1 COMMENT '区域是否开启'")
	private int isOpen;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
	/**  
	 * 获取id  
	 * @return id id  
	 */
	public long getId() {
		return id;
	}
	/**  
	 * 设置id  
	 * @param id id  
	 */
	public void setId(long id) {
		this.id = id;
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
	 * 获取可跑区域名称(可任意设置)  
	 * @return areaName 可跑区域名称(可任意设置)  
	 */
	public String getAreaName() {
		return areaName;
	}
	/**  
	 * 设置可跑区域名称(可任意设置)  
	 * @param areaName 可跑区域名称(可任意设置)  
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	/**  
	 * 获取正常可跑区域范围单位：米  
	 * @return defAreaRound 正常可跑区域范围单位：米  
	 */
	public int getDefAreaRound() {
		return defAreaRound;
	}
	/**  
	 * 设置正常可跑区域范围单位：米  
	 * @param defAreaRound 正常可跑区域范围单位：米  
	 */
	public void setDefAreaRound(int defAreaRound) {
		this.defAreaRound = defAreaRound;
	}
	
	/**  
	 * 获取 操作用户名  
	 * @return uname
	 */
	public String getUname() {
		return uname;
	}
	
	/**  
	 * 设置 操作用户名  
	 * @param uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	/**  
	 * 获取 区域是否开启0:关闭1:开启  
	 * @return isOpen
	 */
	public int getIsOpen() {
		return isOpen;
	}
	
	/**  
	 * 设置 区域是否开启0:关闭1:开启  
	 * @param isOpen
	 */
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	
	/**  
	 * 获取serialVersionUID  
	 * @return serialVersionUID serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**  
	 * 获取添加时间  
	 * @return addTime 添加时间  
	 */
	public Date getAddTime() {
		return addTime;
	}
	/**  
	 * 设置添加时间  
	 * @param addTime 添加时间  
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
} 
