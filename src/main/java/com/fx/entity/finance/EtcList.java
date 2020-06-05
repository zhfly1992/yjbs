package com.fx.entity.finance;

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

import com.fx.entity.cus.BaseUser;
/**
 *    过路费etc表
 * @author xx
 * @date 20200525
 */
@Entity
@Table(name="etc_list")
public class EtcList implements Serializable{
	private static final long serialVersionUID = 754918761830625807L;
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/**etc卡号*/
	@Column(name="card_no", columnDefinition="varchar(20) COMMENT 'etc卡号'")
	private String cardNo;
	
	/**持卡人*/
	@Column(name="card_man", columnDefinition="varchar(20) COMMENT '持卡人'")
	private String cardMan;
	
	/**车牌号(即行驶证)*/
	@Column(name="plate_num",columnDefinition="varchar(20) COMMENT '车牌号'")
	private String plateNum;
	
	/** 驾驶员 */
	@OneToOne(targetEntity = BaseUser.class)
	@JoinColumn(name="etc_driver", nullable=false, referencedColumnName="uname", columnDefinition="varchar(30) COMMENT '驾驶员'")
	private BaseUser etcDriver;
	
	/**进站名称*/
	@Column(name="interStationName", columnDefinition="varchar(255) COMMENT '进站名称'")
	private String interStationName;
	
	/**进站时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="inter_station_time", columnDefinition="datetime COMMENT '进站时间'")
	private Date interStationTime;
	
	/**出站名称*/
	@Column(name="out_station_sime", columnDefinition="varchar(255) COMMENT '出站名称'")
	private String outStationName;
	
	/** 出站时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="out_station_time", columnDefinition="datetime COMMENT '出站时间'")
	private Date outStationTime;
	
	/**etc过路费*/
	@Column(name="etc_money", columnDefinition="double(10,2) default '0.00' COMMENT 'etc过路费'")
	private double etcMoney;
	
	/**订单号*/
	@Column(name="order_num", columnDefinition="varchar(20) COMMENT '订单号'")
	private String orderNum;
	
	/** 行程详情*/
	@Column(name="route_detail", columnDefinition="text COMMENT '行程详情'")
	private String routeDetail;
	
	/** 0 未审核 1已审核*/
	@Column(name="is_check",  columnDefinition="int(11) default '0' COMMENT '审核状态'")
	private int isCheck;
	
	/**添加时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/**每次操作标识号*/
	@Column(name="oper_mark", columnDefinition="varchar(50) COMMENT '每次操作标识号'")
	private String operMark;
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
	 * 获取 单位编号eg：Uxxxx  
	 * @return unitNum
	 */
	public String getUnitNum() {
		return unitNum;
	}
	
	/**  
	 * 设置 单位编号eg：Uxxxx  
	 * @param unitNum 
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	
	/**  
	 * 获取 etc卡号  
	 * @return cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	
	/**  
	 * 设置 etc卡号  
	 * @param cardNo 
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	/**  
	 * 获取 持卡人  
	 * @return cardMan
	 */
	public String getCardMan() {
		return cardMan;
	}
	
	/**  
	 * 设置 持卡人  
	 * @param cardMan 
	 */
	public void setCardMan(String cardMan) {
		this.cardMan = cardMan;
	}
	
	/**  
	 * 获取 车牌号(即行驶证)  
	 * @return plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	
	/**  
	 * 设置 车牌号(即行驶证)  
	 * @param plateNum 
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	
	
	/**  
	 * 获取 驾驶员  
	 * @return etcDriver
	 */
	public BaseUser getEtcDriver() {
		return etcDriver;
	}
	

	/**  
	 * 设置 驾驶员  
	 * @param etcDriver 
	 */
	public void setEtcDriver(BaseUser etcDriver) {
		this.etcDriver = etcDriver;
	}
	

	/**  
	 * 获取 进站名称  
	 * @return interStationName
	 */
	public String getInterStationName() {
		return interStationName;
	}
	
	/**  
	 * 设置 进站名称  
	 * @param interStationName 
	 */
	public void setInterStationName(String interStationName) {
		this.interStationName = interStationName;
	}
	
	/**  
	 * 获取 进站时间  
	 * @return interStationTime
	 */
	public Date getInterStationTime() {
		return interStationTime;
	}
	
	/**  
	 * 设置 进站时间  
	 * @param interStationTime 
	 */
	public void setInterStationTime(Date interStationTime) {
		this.interStationTime = interStationTime;
	}
	
	/**  
	 * 获取 出站名称  
	 * @return outStationName
	 */
	public String getOutStationName() {
		return outStationName;
	}
	
	/**  
	 * 设置 出站名称  
	 * @param outStationName 
	 */
	public void setOutStationName(String outStationName) {
		this.outStationName = outStationName;
	}
	
	/**  
	 * 获取 出站时间  
	 * @return outStationTime
	 */
	public Date getOutStationTime() {
		return outStationTime;
	}
	
	/**  
	 * 设置 出站时间  
	 * @param outStationTime 
	 */
	public void setOutStationTime(Date outStationTime) {
		this.outStationTime = outStationTime;
	}
	
	/**  
	 * 获取 etc过路费  
	 * @return etcMoney
	 */
	public double getEtcMoney() {
		return etcMoney;
	}
	
	/**  
	 * 设置 etc过路费  
	 * @param etcMoney 
	 */
	public void setEtcMoney(double etcMoney) {
		this.etcMoney = etcMoney;
	}
	
	/**  
	 * 获取 订单号  
	 * @return orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}
	
	/**  
	 * 设置 订单号  
	 * @param orderNum 
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	/**  
	 * 获取 行程详情  
	 * @return routeDetail
	 */
	public String getRouteDetail() {
		return routeDetail;
	}
	

	/**  
	 * 设置 行程详情  
	 * @param routeDetail 
	 */
	public void setRouteDetail(String routeDetail) {
		this.routeDetail = routeDetail;
	}
	

	/**  
	 * 获取 0未审核1已审核  
	 * @return isCheck
	 */
	public int getIsCheck() {
		return isCheck;
	}
	
	/**  
	 * 设置 0未审核1已审核  
	 * @param isCheck 
	 */
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
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
	
	/**  
	 * 获取 每次操作标识号  
	 * @return operMark
	 */
	public String getOperMark() {
		return operMark;
	}
	
	/**  
	 * 设置 每次操作标识号  
	 * @param operMark 
	 */
	public void setOperMark(String operMark) {
		this.operMark = operMark;
	}
	
	
	
}