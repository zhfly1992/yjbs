package com.fx.entity.back;
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
 * 特殊路线设置
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="special_route")
public class SpecialRoute{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 起点 */
	@Column(name="start_area", nullable=false, columnDefinition="varchar(50) COMMENT '起点'")
	private String startArea;
	
	/** 目的地 */
	@Column(name="end_area", nullable=false, columnDefinition="varchar(50) COMMENT '目的地'")
	private String endArea;
	
	/** 加价比例 */
	@Column(name="markup_ratio",nullable=false, columnDefinition="double(20,2) default 0 COMMENT '加价比例'")
	private double markupRatio;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;

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
	 * 获取 起点  
	 * @return startArea
	 */
	public String getStartArea() {
		return startArea;
	}
	

	/**  
	 * 设置 起点  
	 * @param startArea 
	 */
	public void setStartArea(String startArea) {
		this.startArea = startArea;
	}
	

	/**  
	 * 获取 目的地  
	 * @return endArea
	 */
	public String getEndArea() {
		return endArea;
	}
	

	/**  
	 * 设置 目的地  
	 * @param endArea 
	 */
	public void setEndArea(String endArea) {
		this.endArea = endArea;
	}
	

	/**  
	 * 获取 加价比例  
	 * @return markupRatio
	 */
	public double getMarkupRatio() {
		return markupRatio;
	}
	

	/**  
	 * 设置 加价比例  
	 * @param markupRatio 
	 */
	public void setMarkupRatio(double markupRatio) {
		this.markupRatio = markupRatio;
	}
	

	/**  
	 * 获取 添加时间  
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}
	

	/**  
	 * 设置 添加时间  
	 * @param addTime 
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	
	
	
}