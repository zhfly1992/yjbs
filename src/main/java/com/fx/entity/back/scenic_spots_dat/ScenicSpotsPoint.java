package com.fx.entity.back.scenic_spots_dat;

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

/**
 * 景点地点-数据
 */
@Entity
@Table(name="scenic_spots_point")
public class ScenicSpotsPoint implements Serializable {
	private static final long serialVersionUID = 1312337645390322359L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 所属城市 eg:四川省-成都市
	 */
	@Column(name="city", columnDefinition="varchar(30) COMMENT '所属城市'")
	private String city;
	/**
	 * 所属区/县 eg:双流区
	 */
	@Column(name="county", columnDefinition="varchar(20) COMMENT '所属区/县'")
	private String county;
	/**
	 * 景点经纬度
	 * 格式（gcj02）：104.147788,30.635141
	 */
	@Column(name="lng_lat", columnDefinition="varchar(30) COMMENT '景点经纬度'")
	private String lngLat;
	/**
	 * 景点对应地图上完整地址 eg:成都市 华府大道地铁C2口
	 */
	@Column(name="map_addr", columnDefinition="varchar(50) COMMENT '景点对应地图上完整地址'")
	private String mapAddr;
	
	
	/**
	 * 景点简称 eg:华府大道
	 */
	@Column(name="addr_short", columnDefinition="varchar(10) COMMENT '景点简称'")
	private String addrShort;
	/**
	 * 景点排序
	 */
	@Column(name="sort_no", nullable=false, columnDefinition="int(11) default '0' COMMENT '景点排序'")
	private int sortNo;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	/**
	 * 景点说明
	 */
	@OneToOne(targetEntity = ScenicSpotsExplain.class)
	@JoinColumn(name="explainId", referencedColumnName="id", columnDefinition="bigint COMMENT '景点说明'")
	private ScenicSpotsExplain explainId;
	/**
	 * 操作备注 eg:张三-18888888888-添加=2020-03-07 10:30:00; 
	 */
	@Column(name="oper_note", columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	
	
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
	 * 获取 所属区县eg:双流区  
	 * @return county
	 */
	public String getCounty() {
		return county;
	}
	
	/**  
	 * 设置 所属区县eg:双流区  
	 * @param county
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	
	/**  
	 * 获取 景点经纬度格式（gcj02）：104.14778830.635141  
	 * @return lngLat
	 */
	public String getLngLat() {
		return lngLat;
	}
	
	/**  
	 * 设置 景点经纬度格式（gcj02）：104.14778830.635141  
	 * @param lngLat
	 */
	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}
	
	/**  
	 * 获取 景点对应地图上完整地址eg:成都市华府大道地铁C2口  
	 * @return mapAddr
	 */
	public String getMapAddr() {
		return mapAddr;
	}
	
	/**  
	 * 设置 景点对应地图上完整地址eg:成都市华府大道地铁C2口  
	 * @param mapAddr
	 */
	public void setMapAddr(String mapAddr) {
		this.mapAddr = mapAddr;
	}
	
	/**  
	 * 获取 景点简称eg:华府大道  
	 * @return addrShort
	 */
	public String getAddrShort() {
		return addrShort;
	}
	
	/**  
	 * 设置 景点简称eg:华府大道  
	 * @param addrShort
	 */
	public void setAddrShort(String addrShort) {
		this.addrShort = addrShort;
	}
	
	/**  
	 * 获取 景点排序  
	 * @return sortNo
	 */
	public int getSortNo() {
		return sortNo;
	}

	/**  
	 * 设置 景点排序  
	 * @param sortNo
	 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	/**  
	 * 获取 添加时间  
	 * @return atime
	 */
	public Date getAtime() {
		return atime;
	}
	
	/**  
	 * 设置 添加时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	
	/**  
	 * 获取 景点说明  
	 * @return explainId
	 */
	public ScenicSpotsExplain getExplainId() {
		return explainId;
	}
	
	/**  
	 * 设置 景点说明  
	 * @param explainId
	 */
	public void setExplainId(ScenicSpotsExplain explainId) {
		this.explainId = explainId;
	}
	
	/**  
	 * 获取 操作备注eg:张三-18888888888-添加=2020-03-0710:30:00;  
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	
	/**  
	 * 设置 操作备注eg:张三-18888888888-添加=2020-03-0710:30:00;  
	 * @param operNote
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
