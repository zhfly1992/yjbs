package com.fx.entity.order;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

import com.fx.commons.utils.enums.PointType;

/**
 * 行程地点表
 */
@Entity
@Table(name="route_map_point")
public class RouteMapPoint implements Serializable {
	private static final long serialVersionUID = 6762292739770511741L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * 地图地点
	 */
	@OneToOne(targetEntity = MapPoint.class,cascade = CascadeType.ALL)
	@JoinColumn(name="map_point", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '地图地点'")
	private MapPoint mapPoint;
	
	/** 地点序号 排序从1开始 */
	@Column(name="sort_no", nullable=false, columnDefinition="int(11) default 0 COMMENT '地点序号 排序从1开始'")
	private int sortNo;
	
	/** 地点类型 */
	@Enumerated(EnumType.STRING)
	@Column(name="ptype", nullable=false, columnDefinition="varchar(20) COMMENT '地点类型'")
	private PointType ptype;

	
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
	 * 获取 地点序号排序从1开始  
	 * @return sortNo
	 */
	public int getSortNo() {
		return sortNo;
	}

	/**  
	 * 设置 地点序号排序从1开始  
	 * @param sortNo
	 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	/**  
	 * 获取 地点类型  
	 * @return ptype
	 */
	public PointType getPtype() {
		return ptype;
	}

	/**  
	 * 设置 地点类型  
	 * @param ptype
	 */
	public void setPtype(PointType ptype) {
		this.ptype = ptype;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
