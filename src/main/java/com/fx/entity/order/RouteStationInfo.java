package com.fx.entity.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 行程站点信息-表
 */
@Entity
@Table(name="route_station_info")
public class RouteStationInfo implements Serializable {
	private static final long serialVersionUID = -2687187264852346994L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 查询编号 eg：航班号/车次号 */
	@Column(name="num", nullable=false, columnDefinition="varchar(20) COMMENT '查询编号'")
	private String num;
	
	/** 选定时间 eg：起飞/降落、出站/进站时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="seltime", columnDefinition="datetime COMMENT '选定时间'")
	private Date seltime;
	
	/** 出发时间 eg：起飞出站时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dep_time", columnDefinition="datetime COMMENT '出发时间'")
	private Date depTime;
	
	/** 到达时间 eg：降落/进站时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="arr_time", columnDefinition="datetime COMMENT '到达时间'")
	private Date arrTime;
	
	/** 出发站点 eg：起飞/出站站点 */
	@Column(name="dep_station", columnDefinition="varchar(20) COMMENT '出发站点'")
	private String depStation;
	
	/** 到达站点 eg：降落/进站站点 */
	@Column(name="arr_station", columnDefinition="varchar(20) COMMENT '到达站点'")
	private String arrStation;
	
	/** 其他时间 eg：接-到站30分钟后；送-提前120分钟； */
	@Column(name="otime", columnDefinition="int(11) default 0 COMMENT '其他时间'")
	private int otime;

	
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
	 * 获取 查询编号eg：航班号车次号  
	 * @return num
	 */
	public String getNum() {
		return num;
	}
	

	/**  
	 * 设置 查询编号eg：航班号车次号  
	 * @param num
	 */
	public void setNum(String num) {
		this.num = num;
	}
	

	/**  
	 * 获取 选定时间eg：起飞降落、出站进站时间  
	 * @return seltime
	 */
	public Date getSeltime() {
		return seltime;
	}
	

	/**  
	 * 设置 选定时间eg：起飞降落、出站进站时间  
	 * @param seltime
	 */
	public void setSeltime(Date seltime) {
		this.seltime = seltime;
	}
	

	/**  
	 * 获取 出发时间eg：起飞出站时间  
	 * @return depTime
	 */
	public Date getDepTime() {
		return depTime;
	}
	

	/**  
	 * 设置 出发时间eg：起飞出站时间  
	 * @param depTime
	 */
	public void setDepTime(Date depTime) {
		this.depTime = depTime;
	}
	

	/**  
	 * 获取 到达时间eg：降落进站时间  
	 * @return arrTime
	 */
	public Date getArrTime() {
		return arrTime;
	}
	

	/**  
	 * 设置 到达时间eg：降落进站时间  
	 * @param arrTime
	 */
	public void setArrTime(Date arrTime) {
		this.arrTime = arrTime;
	}
	

	/**  
	 * 获取 出发站点eg：起飞出站站点  
	 * @return depStation
	 */
	public String getDepStation() {
		return depStation;
	}
	

	/**  
	 * 设置 出发站点eg：起飞出站站点  
	 * @param depStation
	 */
	public void setDepStation(String depStation) {
		this.depStation = depStation;
	}
	

	/**  
	 * 获取 到达站点eg：降落进站站点  
	 * @return arrStation
	 */
	public String getArrStation() {
		return arrStation;
	}
	

	/**  
	 * 设置 到达站点eg：降落进站站点  
	 * @param arrStation
	 */
	public void setArrStation(String arrStation) {
		this.arrStation = arrStation;
	}
	

	/**  
	 * 获取 其他时间eg：接-到站30分钟后；送-提前120分钟；  
	 * @return otime
	 */
	public int getOtime() {
		return otime;
	}
	

	/**  
	 * 设置 其他时间eg：接-到站30分钟后；送-提前120分钟；  
	 * @param otime
	 */
	public void setOtime(int otime) {
		this.otime = otime;
	}
	

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
