package com.fx.entity.company;

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
 * 区域返点比例设置表
 * @author qfc
 * @date 20200426
 */
@Entity
@Table(name="area_rebate_set")
public class AreaRebateSet implements Serializable {
	private static final long serialVersionUID = -6336162810027313483L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 所属车队编号
	 */
	@Column(name="team_no", nullable=false, columnDefinition="varchar(20) COMMENT '车队编号'")
	private String teamNo;
	/**
	 * 返点名称
	 */
	@Column(name="rebate_name", nullable=false, columnDefinition="varchar(50) COMMENT '返点名称'")
	private String rebateName;
	/**
	 * 所属区域[成都-武侯区]
	 */
	@Column(name="area", nullable=false, columnDefinition="varchar(50) COMMENT '所属区域'")
	private String area;
	/**
	 * 返点比例
	 */
	@Column(name="rebate", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '返点比例'")
	private double rebate;
	/**
	 * 超公里调节参数
	 */
	@Column(name="overkm_param", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '超公里调节参数'")
	private double overkmParam;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	
	public AreaRebateSet() {}
	
	public AreaRebateSet(long id) {
		super();
		this.id = id;
	}

	public AreaRebateSet(long id, String rebateName, String area, double rebate) {
		super();
		this.id = id;
		this.rebateName = rebateName;
		this.area = area;
		this.rebate = rebate;
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
	 * 获取 所属车队编号  
	 * @return teamNo  
	 */
	public String getTeamNo() {
		return teamNo;
	}
	/**  
	 * 设置 所属车队编号  
	 * @param teamNo
	 */
	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}
	/**  
	 * 获取 返点名称  
	 * @return rebateName  
	 */
	public String getRebateName() {
		return rebateName;
	}
	/**  
	 * 设置 返点名称  
	 * @param rebateName
	 */
	public void setRebateName(String rebateName) {
		this.rebateName = rebateName;
	}
	/**  
	 * 获取 所属区域[成都-武侯区]  
	 * @return area  
	 */
	public String getArea() {
		return area;
	}
	/**  
	 * 设置 所属区域[成都-武侯区]  
	 * @param area
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**  
	 * 获取 返点比例  
	 * @return rebate  
	 */
	public double getRebate() {
		return rebate;
	}
	/**  
	 * 设置 返点比例  
	 * @param rebate
	 */
	public void setRebate(double rebate) {
		this.rebate = rebate;
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
	 * 获取 超公里调节参数  
	 * @return overkmParam  
	 */
	public double getOverkmParam() {
		return overkmParam;
	}

	/**  
	 * 设置 超公里调节参数  
	 * @param overkmParam
	 */
	public void setOverkmParam(double overkmParam) {
		this.overkmParam = overkmParam;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
} 