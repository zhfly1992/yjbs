package com.fx.entity.finance;

import java.io.Serializable;

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

import com.fx.commons.utils.enums.OilWay;
import com.fx.entity.company.CompanyCustom;
/**
 * 驾驶员记账记录
 * @author xx
 * @date 20200630
 */
@Entity
@Table(name="driver_jc_record")
public class DriverJzRecord implements Serializable{

	private static final long serialVersionUID = 7307352030511564195L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	
	/** 当前公里数 */
	@Column(name="current_kilo", nullable=false, columnDefinition="double(10,2) default '0.00' COMMENT '当前公里'")
	private double currentKilo;
	
	/** 站点客户引用 */
	@OneToOne(targetEntity = CompanyCustom.class)
	@JoinColumn(name="station_customer", nullable=false, referencedColumnName="id", columnDefinition="varchar(30) COMMENT '站点客户引用'")
	private CompanyCustom stationCustomer;
	
	/** 加油方式(加油站，充值卡等) */
	@Enumerated(EnumType.STRING)
	@Column(name="oil_way", columnDefinition="varchar(20) COMMENT '加油方式(油票，充值卡等)'")
	private OilWay oilWay;
	
	/** 加油数量:升 */
	@Column(name="oil_rise", columnDefinition="double(10,2) default '0.00' COMMENT '加油数量:升'")
	private double oilRise;
	
	/** 加油实付金额 */
	@Column(name="oil_real_money", columnDefinition="double(10,2) default '0.00' COMMENT '加油实付金额'")
	private double oilRealMoney;
	
	/** 0 记账 1现金 */
	@Column(name="repair_pay_way",  columnDefinition="int(11) default '0' COMMENT '0 记账 1现金'")
	private int repairPayWay;
	
	
	
	public DriverJzRecord() {}



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
	 * 获取 当前公里数  
	 * @return currentKilo
	 */
	public double getCurrentKilo() {
		return currentKilo;
	}
	



	/**  
	 * 设置 当前公里数  
	 * @param currentKilo
	 */
	public void setCurrentKilo(double currentKilo) {
		this.currentKilo = currentKilo;
	}
	



	/**  
	 * 获取 站点客户引用  
	 * @return stationCustomer
	 */
	public CompanyCustom getStationCustomer() {
		return stationCustomer;
	}
	



	/**  
	 * 设置 站点客户引用  
	 * @param stationCustomer
	 */
	public void setStationCustomer(CompanyCustom stationCustomer) {
		this.stationCustomer = stationCustomer;
	}
	



	/**  
	 * 获取 加油方式(加油站，充值卡等)  
	 * @return oilWay
	 */
	public OilWay getOilWay() {
		return oilWay;
	}
	



	/**  
	 * 设置 加油方式(加油站，充值卡等)  
	 * @param oilWay
	 */
	public void setOilWay(OilWay oilWay) {
		this.oilWay = oilWay;
	}
	



	/**  
	 * 获取 加油数量:升  
	 * @return oilRise
	 */
	public double getOilRise() {
		return oilRise;
	}
	



	/**  
	 * 设置 加油数量:升  
	 * @param oilRise
	 */
	public void setOilRise(double oilRise) {
		this.oilRise = oilRise;
	}
	



	/**  
	 * 获取 加油实付金额  
	 * @return oilRealMoney
	 */
	public double getOilRealMoney() {
		return oilRealMoney;
	}
	



	/**  
	 * 设置 加油实付金额  
	 * @param oilRealMoney
	 */
	public void setOilRealMoney(double oilRealMoney) {
		this.oilRealMoney = oilRealMoney;
	}
	



	/**  
	 * 获取 0记账1现金  
	 * @return repairPayWay
	 */
	public int getRepairPayWay() {
		return repairPayWay;
	}
	



	/**  
	 * 设置 0记账1现金  
	 * @param repairPayWay
	 */
	public void setRepairPayWay(int repairPayWay) {
		this.repairPayWay = repairPayWay;
	}
	



	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
	
}