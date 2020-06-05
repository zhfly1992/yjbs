package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 站点列表
 */
@Entity
@Table(name="station_list")
public class StationList implements Serializable {
	private static final long serialVersionUID = 5282487591029622873L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 位置名称
	 */
	@Column(name="name", columnDefinition="varchar(20) COMMENT '位置名称'")
	private String name;
	/**
	 * 位置名称备注名
	 */
	@Column(name="name_note", columnDefinition="varchar(200) COMMENT '位置名称备注名'")
	private String nameNote;
	/**
	 * 地图地点
	 */
	@OneToOne(targetEntity = MapPoint.class)
	@JoinColumn(name="map_point", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '地图地点'")
	private MapPoint mapPoint;
	/**
	 * 位置编号
	 */
	@Column(name="pos_code", columnDefinition="varchar(20) COMMENT '位置编号'")
	private String posCode;
	/**
	 * 位置对应城市ID
	 */
	@Column(name="city_id", columnDefinition="varchar(20) COMMENT '位置对应城市ID'")
	private String cityId;
	/**
	 * 位置对应城市名称
	 */
	@Column(name="city_name", columnDefinition="varchar(50) COMMENT '位置对应城市名称'")
	private String cityName;
	/**
	 * 省份-城市-区/县 eg：四川省-成都市-武侯区
	 */
	@Column(name="county", columnDefinition="varchar(50) COMMENT '省份-城市-区/县'")
	private String county;
	/**
	 * 位置类型 1飞机 2火车 3汽车
	 */
	@Column(name="type", columnDefinition="int default 0 COMMENT '位置类型 1飞机 2火车 3汽车'")
	private int type;
	
	
	public StationList() {}
	
	public StationList(String name, String posCode) {
		this.name = name;
		this.posCode = posCode;
	}

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
	 * 获取 位置名称  
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**  
	 * 设置 位置名称  
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * 获取 位置名称备注名  
	 * @return nameNote
	 */
	public String getNameNote() {
		return nameNote;
	}

	/**  
	 * 设置 位置名称备注名  
	 * @param nameNote
	 */
	public void setNameNote(String nameNote) {
		this.nameNote = nameNote;
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取位置编号  
	 * @return posCode 位置编号  
	 */
	public String getPosCode() {
		return posCode;
	}

	/**  
	 * 设置位置编号  
	 * @param posCode 位置编号  
	 */
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	/**  
	 * 获取 省份-城市-区县  
	 * @return county
	 */
	public String getCounty() {
		return county;
	}
	

	/**  
	 * 设置 省份-城市-区县  
	 * @param county
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	

	/**  
	 * 获取位置对应城市ID  
	 * @return cityId 位置对应城市ID  
	 */
	public String getCityId() {
		return cityId;
	}

	/**  
	 * 设置位置对应城市ID  
	 * @param cityId 位置对应城市ID  
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * 获取 位置对应城市名称
	 * @return cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * 设置 位置对应城市名称
	 * @param cityName
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**  
	 * 获取接送机位置类型：1飞机2火车3汽车  
	 * @return type 接送机位置类型：1飞机2火车3汽车  
	 */
	public int getType() {
		return type;
	}

	/**  
	 * 设置接送机位置类型：1飞机2火车3汽车  
	 * @param type 接送机位置类型：1飞机2火车3汽车  
	 */
	public void setType(int type) {
		this.type = type;
	}
} 
