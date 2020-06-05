package com.fx.entity.order;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.BaseUser;
/**
 * 驾驶员行程开支记录表
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="route_trade_list")
public class RouteTradeList{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 车牌号
	 */
	@Column(name="plate_num", nullable=false,columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	/** 驾驶员信息 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="driver_info", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '驾驶员信息'")
	private BaseUser driverInfo;
	/**
	 * 团上现收:元
	 *//*
	@Column(name="group_cash", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '团上现收:元'")
	private double groupCash;
	*//**
	 * 团上返点:元
	 *//*
	@Column(name="group_rebate", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '团上返点:元'")
	private double groupRebate;
	*//**
	 * 行程加点:元
	 *//*
	@Column(name="route_rebate", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '行程加点:元'")
	private double routeRebate;*/
	/**
	 * 打单费:元
	 */
	@Column(name="single_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '打单费:元'")
	private double singleFee;
	/**
	 * 洗车费:元
	 */
	@Column(name="washing_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '洗车费:元'")
	private double washingFee;
	/**
	 * 停车费:元
	 */
	@Column(name="parking_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '停车费:元'")
	private double parkingFee;
	/**
	 * 过路费:元
	 */
	@Column(name="road_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '过路费:元'")
	private double roadFee;
	/**
	 * 生活费:元
	 */
	@Column(name="living_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '生活费:元'")
	private double livingFee;
	/**
	 * 其他费:元
	 */
	@Column(name="other_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '其他费:元'")
	private double otherFee;
	/**
	 * 买水费:元
	 */
	@Column(name="water_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '买水费:元'")
	private double waterFee;
	/**
	 * 住宿费:元
	 */
	@Column(name="stay_fee", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '住宿费:元'")
	private double stayFee;
	/**
	 * 提交备注
	 */
	@Column(name="remark",columnDefinition="text COMMENT '提交备注'")
	private String remark;
	/**
	 * 操作备注
	 */
	@Column(name="oper_note",columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	/**
	 * 凭证图片下载地址,逗号分隔，最多12张图片
	 */
	@Column(name="route_voucher_url", columnDefinition="text COMMENT '凭证图片下载地址'")
	private String routeVoucherUrl;
	/**
	 * 数据来源
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="reqsrc", columnDefinition="varchar(20) COMMENT '数据来源'")
	private ReqSrc reqsrc;
	/**
	 * -1驳回 0 未审核 1已审核 2已复核 3已核销
	 */
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '0 未审核 1已审核 2已复核 3已核销'")
	private int isCheck;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
/*	public double getGroupCash() {
		return groupCash;
	}
	public void setGroupCash(double groupCash) {
		this.groupCash = groupCash;
	}
	public double getGroupRebate() {
		return groupRebate;
	}
	public void setGroupRebate(double groupRebate) {
		this.groupRebate = groupRebate;
	}
	public double getRouteRebate() {
		return routeRebate;
	}
	public void setRouteRebate(double routeRebate) {
		this.routeRebate = routeRebate;
	}*/
	public double getSingleFee() {
		return singleFee;
	}
	public void setSingleFee(double singleFee) {
		this.singleFee = singleFee;
	}
	public double getWashingFee() {
		return washingFee;
	}
	public void setWashingFee(double washingFee) {
		this.washingFee = washingFee;
	}
	public double getParkingFee() {
		return parkingFee;
	}
	public void setParkingFee(double parkingFee) {
		this.parkingFee = parkingFee;
	}
	public double getRoadFee() {
		return roadFee;
	}
	public void setRoadFee(double roadFee) {
		this.roadFee = roadFee;
	}
	public double getLivingFee() {
		return livingFee;
	}
	public void setLivingFee(double livingFee) {
		this.livingFee = livingFee;
	}
	public double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}
	public double getWaterFee() {
		return waterFee;
	}
	public void setWaterFee(double waterFee) {
		this.waterFee = waterFee;
	}
	public double getStayFee() {
		return stayFee;
	}
	public void setStayFee(double stayFee) {
		this.stayFee = stayFee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	public String getRouteVoucherUrl() {
		return routeVoucherUrl;
	}
	public void setRouteVoucherUrl(String routeVoucherUrl) {
		this.routeVoucherUrl = routeVoucherUrl;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	/**
	 * 获取 数据来源
	 * @return reqsrc
	 */
	public ReqSrc getReqsrc() {
		return reqsrc;
	}
	/**
	 * 设置 数据来源
	 * @param reqsrc
	 */
	public void setReqsrc(ReqSrc reqsrc) {
		this.reqsrc = reqsrc;
	}
	/**
	 * 操作备注
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	/**
	 * 操作备注
	 * @param operNote
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
}