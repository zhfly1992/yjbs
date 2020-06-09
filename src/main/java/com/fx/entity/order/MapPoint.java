package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 行程地点表
 */
@Entity
@Table(name="map_point")
public class MapPoint implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 经度 eg：103.666666 */
	@Column(name="lng", nullable=false, columnDefinition="double(10,6) default 0 COMMENT '经度'")
	private double lng;
	
	/** 纬度 eg：30.666666 */
	@Column(name="lat", nullable=false, columnDefinition="double(10,6) default 0 COMMENT '经度'")
	private double lat;
	
	/** 经纬度 eg：103.666666,30.666666 */
	@Column(name="lng_lat", nullable=false, columnDefinition="varchar(30) COMMENT '经纬度'")
	private String lngLat;
	
	/** 详细地址 eg: 成都市 天府广场H口 */
	@Column(name="address", nullable=false, columnDefinition="varchar(50) COMMENT '详细地址'")
	private String address;
	
	/** 所属城市 eg:四川省-成都市 */
	@Column(name="city", nullable=false, columnDefinition="varchar(30) COMMENT '所属城市'")
	private String city;
	
	/** 所属区县 eg: 青羊区 */
	@Column(name="county", nullable=false, columnDefinition="varchar(30) COMMENT '所属区县'")
	private String county;

	
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
	 * 获取 经度eg：103.666666  
	 * @return lng
	 */
	public double getLng() {
		return lng;
	}

	/**  
	 * 设置 经度eg：103.666666  
	 * @param lng
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**  
	 * 获取 纬度eg：30.666666  
	 * @return lat
	 */
	public double getLat() {
		return lat;
	}

	/**  
	 * 设置 纬度eg：30.666666  
	 * @param lat
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**  
	 * 获取 经纬度eg：103.666666,30.666666  
	 * @return lngLat
	 */
	public String getLngLat() {
		return lngLat;
	}

	/**  
	 * 设置 经纬度eg：103.666666,30.666666  
	 * @param lngLat
	 */
	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}

	/**  
	 * 获取 详细地址eg:成都市天府广场H口  
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**  
	 * 设置 详细地址eg:成都市天府广场H口  
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**  
	 * 获取 所属城市eg:四川省-成都市  
	 * @return city
	 */
	public String getCity() {
		return city;
	}
	
	/**  
	 * 设置 所属城市eg:四川省-成都市  
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**  
	 * 获取 所属区县eg:青羊区  
	 * @return county
	 */
	public String getCounty() {
		return county;
	}

	/**  
	 * 设置 所属区县eg:青羊区  
	 * @param county
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
