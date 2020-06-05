package com.fx.entity.company;

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
 * @author XX
 * @date 20170215
 * 企业合作账号（车队）
 */
@Entity
@Table(name="pickup_partner")
public class PickupPartner implements Serializable{
	private static final long serialVersionUID = -1522849896722285123L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 车队编号
	 */
	@Column(name="team_no", columnDefinition="varchar(20) COMMENT '车队编号'")
	private String teamNo;
	/**
	 * 操作员
	 */
	@Column(name="operator", columnDefinition="varchar(20) COMMENT '操作员'")
	private String operator;
	/**
	 * 合作账号
	 */
	@Column(name="partner", columnDefinition="varchar(20) COMMENT '合作账号'")
	private String partner;
	/**
	 * 结算方式：0现金 1月结
	 */
	@Column(name="settle_way", nullable=false, columnDefinition="int default 0 COMMENT '结算方式：1现金 2月结'")
	private int settleWay;
	/**
	 * 提成返点
	 */
	@Column(name="rebates", columnDefinition="double(20,2) default 0 COMMENT '提成返点'")
	private double rebates;
	/**
	 * 区域返点比例
	 */
	@OneToOne(targetEntity = AreaRebateSet.class)
	@JoinColumn(name="area_rebate_set_id", referencedColumnName="id", columnDefinition="bigint COMMENT '区域返点比例设置'")
	private AreaRebateSet areaRebateSet;
	/**
	 * 执行方式：1-按照账号所在区域执行；2-按照订单所在区域执行（计算价格时以该区域最低比例计算）；
	 */
	@Column(name="area_exe_way", nullable=false, columnDefinition="int default 1 COMMENT '执行方式'")
	private int areaExeWay;
	/**
	 * 合作有效时间:单位 天
	 */
	@Column(name="valid_day", nullable=false, columnDefinition="int default 0 COMMENT '有效时间:单位 天'")
	private int validDay;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
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
	 * 获取 车队编号  
	 * @return teamNo  
	 */
	public String getTeamNo() {
		return teamNo;
	}
	/**  
	 * 设置 车队编号  
	 * @param teamNo
	 */
	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}
	/**  
	 * 获取 操作员  
	 * @return operator  
	 */
	public String getOperator() {
		return operator;
	}
	/**  
	 * 设置 操作员  
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**  
	 * 获取 合作账号  
	 * @return partner  
	 */
	public String getPartner() {
		return partner;
	}
	/**  
	 * 设置 合作账号  
	 * @param partner
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}
	/**  
	 * 获取 结算方式：0现金1月结  
	 * @return settleWay  
	 */
	public int getSettleWay() {
		return settleWay;
	}
	/**  
	 * 设置 结算方式：0现金1月结  
	 * @param settleWay
	 */
	public void setSettleWay(int settleWay) {
		this.settleWay = settleWay;
	}
	/**  
	 * 获取 提成返点  
	 * @return rebates  
	 */
	public double getRebates() {
		return rebates;
	}
	/**  
	 * 设置 提成返点  
	 * @param rebates
	 */
	public void setRebates(double rebates) {
		this.rebates = rebates;
	}
	/**  
	 * 获取 区域返点比例  
	 * @return areaRebateSet  
	 */
	public AreaRebateSet getAreaRebateSet() {
		return areaRebateSet;
	}
	/**  
	 * 设置 区域返点比例  
	 * @param areaRebateSet
	 */
	public void setAreaRebateSet(AreaRebateSet areaRebateSet) {
		this.areaRebateSet = areaRebateSet;
	}
	/**  
	 * 获取 执行方式：1-按照账号所在区域执行；2-按照订单所在区域执行；  
	 * @return areaExeWay  
	 */
	public int getAreaExeWay() {
		return areaExeWay;
	}
	/**  
	 * 设置 执行方式：1-按照账号所在区域执行；2-按照订单所在区域执行；  
	 * @param areaExeWay
	 */
	public void setAreaExeWay(int areaExeWay) {
		this.areaExeWay = areaExeWay;
	}
	/**  
	 * 获取 合作有效时间:单位天  
	 * @return validDay  
	 */
	public int getValidDay() {
		return validDay;
	}
	/**  
	 * 设置 合作有效时间:单位天  
	 * @param validDay
	 */
	public void setValidDay(int validDay) {
		this.validDay = validDay;
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
