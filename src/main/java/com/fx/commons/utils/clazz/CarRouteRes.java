package com.fx.commons.utils.clazz;

import java.io.Serializable;

/**
 * 车辆行程结果
 */
public class CarRouteRes implements Serializable {
	private static final long serialVersionUID = -2316103457639994875L;
	
	private String id;
	
	/** 查询线路策略 */
	private String stg;
	
	/** 起点坐标 eg：103.666666,40.666666 */
	private String slnglat;
	
	/** 起点坐标 eg：103.666666,40.666666 */
	private String elnglat;
	
	/** 途径点坐标 eg：103.666666,40.666666;103.666666,40.666666; */
	private String wlnglat;
	
	/** 途径城市 eg：成都市=德阳市... */
	private String wayCity;
	
	/** 耗时（秒）[距离中心点行程耗时] */
	private int timeCons;
	
	/** 距离（米）[距离中心点的行程距离] */
	private int distance;
	
	/** 过路费（元）[行程中的过路费总和] */
	private int tolls;
	
	/** 车牌 */
	private String plateNum;
	
	/** 备注 */
	private String note;
	
	/** 数据 */
	private Object data;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	/**  
	 * 获取 查询线路策略  
	 * @return stg
	 */
	public String getStg() {
		return stg;
	}
	
	/**  
	 * 设置 查询线路策略  
	 * @param stg
	 */
	public void setStg(String stg) {
		this.stg = stg;
	}
	
	/**  
	 * 获取 起点坐标eg：103.666666,40.666666  
	 * @return slnglat
	 */
	public String getSlnglat() {
		return slnglat;
	}
	
	/**  
	 * 设置 起点坐标eg：103.666666,40.666666  
	 * @param slnglat
	 */
	public void setSlnglat(String slnglat) {
		this.slnglat = slnglat;
	}
	
	/**  
	 * 获取 起点坐标eg：103.666666,40.666666  
	 * @return elnglat
	 */
	public String getElnglat() {
		return elnglat;
	}
	
	/**  
	 * 设置 起点坐标eg：103.666666,40.666666  
	 * @param elnglat
	 */
	public void setElnglat(String elnglat) {
		this.elnglat = elnglat;
	}
	
	/**  
	 * 获取 途径点坐标eg：103.66666640.666666;103.66666640.666666;  
	 * @return wlnglat
	 */
	public String getWlnglat() {
		return wlnglat;
	}
	
	/**  
	 * 设置 途径点坐标eg：103.66666640.666666;103.66666640.666666;  
	 * @param wlnglat
	 */
	public void setWlnglat(String wlnglat) {
		this.wlnglat = wlnglat;
	}
	
	/**  
	 * 获取 途径城市eg：成都市=德阳市...  
	 * @return wayCity
	 */
	public String getWayCity() {
		return wayCity;
	}
	
	/**  
	 * 设置 途径城市eg：成都市=德阳市...  
	 * @param wayCity
	 */
	public void setWayCity(String wayCity) {
		this.wayCity = wayCity;
	}
	
	/**
	 * 获取 耗时（秒）[距离中心点行程耗时]
	 * @return timeCons
	 */
	public int getTimeCons() {
		return timeCons;
	}
	/**
	 * 设置 耗时（秒）[距离中心点行程耗时]
	 * @param timeCons
	 */
	public void setTimeCons(int timeCons) {
		this.timeCons = timeCons;
	}
	/**
	 * 获取 距离（米）[距离中心点的行程距离]
	 * @return distance
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * 设置 距离（米）[距离中心点的行程距离]
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	/**
	 * 获取 过路费（元）[行程中的过路费总和]
	 * @return tolls
	 */
	public int getTolls() {
		return tolls;
	}
	/**
	 * 设置 过路费（元）[行程中的过路费总和]
	 * @param tolls
	 */
	public void setTolls(int tolls) {
		this.tolls = tolls;
	}
	/**
	 * 获取 车牌
	 * @return plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	/**
	 * 设置 车牌
	 * @param plateNum
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
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
	 * 获取 数据
	 * @return data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 设置 数据
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
