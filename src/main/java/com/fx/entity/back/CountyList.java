package com.fx.entity.back;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 区县列表
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="county_list")
public class CountyList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 区县名称 */
	@Column(name="county_name", nullable=false, columnDefinition="varchar(20) COMMENT '区县名称'")
	private String countyName;
	
	/** 区县编号 */
	@Column(name="county_code", nullable=false, columnDefinition="varchar(20) COMMENT '区县编号'")
	private String countyCode;
	
	/** 区县标识 */
	@Column(name="county_mark", columnDefinition="varchar(20) COMMENT '区县标识'")
	private String countyMark;
	
	/** 区县对应城市编号 */
	@Column(name="city_code", nullable=false, columnDefinition="varchar(20) COMMENT '区县对应城市编号'")
	private String cityCode;
	
	/** 起止区县景点 九寨沟,四姑娘山,瑶池 */
	@Column(name="view_spots", columnDefinition="text COMMENT '起止区县景点'")
	private String viewSpots;
	
	/** 热门排序 */
	@Column(name="hot_order", nullable=false, columnDefinition="int default 0 COMMENT '热门排序'")
	private int hotOrder;
	
	public CountyList() {
	}
	public CountyList(String countyName, String countyCode) {
		this.countyName = countyName;
		this.countyCode = countyCode;
	}
	public CountyList(String countyName, String countyCode, String viewSpots) {
		this.countyName = countyName;
		this.countyCode = countyCode;
		this.viewSpots = viewSpots;
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
	 * 获取 区县名称  
	 * @return countyName
	 */
	public String getCountyName() {
		return countyName;
	}
	
	/**  
	 * 设置 区县名称  
	 * @param countyName 
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	
	/**  
	 * 获取 区县编号  
	 * @return countyCode
	 */
	public String getCountyCode() {
		return countyCode;
	}
	
	/**  
	 * 设置 区县编号  
	 * @param countyCode 
	 */
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	
	/**  
	 * 获取 区县标识  
	 * @return countyMark
	 */
	public String getCountyMark() {
		return countyMark;
	}
	
	/**  
	 * 设置 区县标识  
	 * @param countyMark 
	 */
	public void setCountyMark(String countyMark) {
		this.countyMark = countyMark;
	}
	
	/**  
	 * 获取 区县对应城市编号  
	 * @return cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	
	/**  
	 * 设置 区县对应城市编号  
	 * @param cityCode 
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	/**  
	 * 获取 起止区县景点九寨沟四姑娘山瑶池  
	 * @return viewSpots
	 */
	public String getViewSpots() {
		return viewSpots;
	}
	
	/**  
	 * 设置 起止区县景点九寨沟四姑娘山瑶池  
	 * @param viewSpots 
	 */
	public void setViewSpots(String viewSpots) {
		this.viewSpots = viewSpots;
	}
	
	/**  
	 * 获取 热门排序  
	 * @return hotOrder
	 */
	public int getHotOrder() {
		return hotOrder;
	}
	
	/**  
	 * 设置 热门排序  
	 * @param hotOrder 
	 */
	public void setHotOrder(int hotOrder) {
		this.hotOrder = hotOrder;
	}
	
	
	
    
	
	
} 
