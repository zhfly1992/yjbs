package com.fx.entity.back;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 城市列表
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="city_list")
public class CityList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 城市名称 */
	@Column(name="city_name", nullable=false, columnDefinition="varchar(20) COMMENT '城市名称'")
	private String cityName;
	
	/** 城市编号 */
	@Column(name="city_code", nullable=false, columnDefinition="varchar(20) COMMENT '城市编号'")
	private String cityCode;
	
	/** 城市标识 */
	@Column(name="city_mark", columnDefinition="varchar(20) COMMENT '城市标识'")
	private String cityMark;
	
	/** 城市对应省份编号 */
	@Column(name="province_code", nullable=false, columnDefinition="varchar(20) COMMENT '城市对应省份编号'")
	private String provinceCode;
	
	/** 全拼 */
	@Column(name="pinyin", columnDefinition="varchar(50) COMMENT '全拼'")
	private String pinyin;
	
	/** 拼音简写 */
	@Column(name="pinyin_simple", columnDefinition="varchar(50) COMMENT '拼音简写'")
	private String pinyinSimple;
	
	/** 热门排序 */
	@Column(name="hot_order", nullable=false, columnDefinition="int default 0 COMMENT '热门排序'")
	private int hotOrder;
	
	/** 车牌简称 */
	@Column(name="plate_num_short", nullable=false, columnDefinition="varchar(50) COMMENT '车牌简称'")
	private String plateNumShort;
	
	/** 特殊区域  默认0不是 1是 */
	@Column(name="special_area", nullable=false, columnDefinition="int default 0 COMMENT '特殊区域:默认0不是,1是'")
	private int specialArea;
	
	/** 起点详细地址 */
	@Column(name="start_address", columnDefinition="varchar(255) COMMENT '起点详细地址'")
	private String startAddress;
	
	/** 起点经纬度 格式：104.147788,30.635141 */
	@Column(name="lon_and_lat", columnDefinition="varchar(100) COMMENT '起点经纬度 格式：104.147788,30.635141'")
	private String lonAndLat;

	public CityList() {}
	public CityList(String cityName, String cityCode) {
		this.cityName = cityName;
		this.cityCode = cityCode;
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
	 * 获取 城市名称  
	 * @return cityName
	 */
	public String getCityName() {
		return cityName;
	}
	
	/**  
	 * 设置 城市名称  
	 * @param cityName 
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	/**  
	 * 获取 城市编号  
	 * @return cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	
	/**  
	 * 设置 城市编号  
	 * @param cityCode 
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	/**  
	 * 获取 城市标识  
	 * @return cityMark
	 */
	public String getCityMark() {
		return cityMark;
	}
	
	/**  
	 * 设置 城市标识  
	 * @param cityMark 
	 */
	public void setCityMark(String cityMark) {
		this.cityMark = cityMark;
	}
	
	/**  
	 * 获取 城市对应省份编号  
	 * @return provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	
	/**  
	 * 设置 城市对应省份编号  
	 * @param provinceCode 
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	/**  
	 * 获取 全拼  
	 * @return pinyin
	 */
	public String getPinyin() {
		return pinyin;
	}
	
	/**  
	 * 设置 全拼  
	 * @param pinyin 
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	/**  
	 * 获取 拼音简写  
	 * @return pinyinSimple
	 */
	public String getPinyinSimple() {
		return pinyinSimple;
	}
	
	/**  
	 * 设置 拼音简写  
	 * @param pinyinSimple 
	 */
	public void setPinyinSimple(String pinyinSimple) {
		this.pinyinSimple = pinyinSimple;
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
	
	/**  
	 * 获取 车牌简称  
	 * @return plateNumShort
	 */
	public String getPlateNumShort() {
		return plateNumShort;
	}
	
	/**  
	 * 设置 车牌简称  
	 * @param plateNumShort 
	 */
	public void setPlateNumShort(String plateNumShort) {
		this.plateNumShort = plateNumShort;
	}
	
	/**  
	 * 获取 特殊区域默认0不是1是  
	 * @return specialArea
	 */
	public int getSpecialArea() {
		return specialArea;
	}
	
	/**  
	 * 设置 特殊区域默认0不是1是  
	 * @param specialArea 
	 */
	public void setSpecialArea(int specialArea) {
		this.specialArea = specialArea;
	}
	
	/**  
	 * 获取 起点详细地址  
	 * @return startAddress
	 */
	public String getStartAddress() {
		return startAddress;
	}
	
	/**  
	 * 设置 起点详细地址  
	 * @param startAddress 
	 */
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	
	/**  
	 * 获取 起点经纬度格式：104.14778830.635141  
	 * @return lonAndLat
	 */
	public String getLonAndLat() {
		return lonAndLat;
	}
	
	/**  
	 * 设置 起点经纬度格式：104.14778830.635141  
	 * @param lonAndLat 
	 */
	public void setLonAndLat(String lonAndLat) {
		this.lonAndLat = lonAndLat;
	}
	
	
	
} 
