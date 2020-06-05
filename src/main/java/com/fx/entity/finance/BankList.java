package com.fx.entity.finance;

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
 * 收付款方式表
 * @author xx
 * @date 20200505
 */
@Entity
@Table(name="bank_list")
public class BankList implements Serializable{
	private static final long serialVersionUID = -93869288277364472L;
	
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
	
	/** 账户名称 */
	@Column(name="bankName", nullable=false, columnDefinition="varchar(100) COMMENT '账户名称'")
	private String bankName;
	
	/** 卡号 */
	@Column(name="card_no", columnDefinition="varchar(100) COMMENT '卡号'")
	private String cardNo;
	
	/** 开户行 */
	@Column(name="card_name",  columnDefinition="varchar(100) COMMENT '开户行'")
	private String cardName;
	
	/** 开启/关闭银行 0:关闭 1:开启 */
	@Column(name="is_open", columnDefinition="int default 0 COMMENT '开启/关闭银行 0:关闭 1:开启'")
	private int isOpen=0;
	
	/** 操作备注 */
	@Column(name="oper_note",  columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	
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
	 * 获取 银行名称  
	 * @return bankName
	 */
	public String getBankName() {
		return bankName;
	}
	
	/**  
	 * 设置 银行名称  
	 * @param bankName 
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	/**  
	 * 获取 卡号  
	 * @return cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	
	/**  
	 * 设置 卡号  
	 * @param cardNo 
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	/**  
	 * 获取 账户姓名  
	 * @return cardName
	 */
	public String getCardName() {
		return cardName;
	}
	
	/**  
	 * 设置 账户姓名  
	 * @param cardName 
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	/**  
	 * 获取 开启关闭银行账0:关闭1:开启  
	 * @return isOpen
	 */
	public int getIsOpen() {
		return isOpen;
	}
	
	/**  
	 * 设置 开启关闭银行账0:关闭1:开启  
	 * @param isOpen 
	 */
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
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
	 * 获取 操作备注  
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	

	/**  
	 * 设置 操作备注  
	 * @param operNote 
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
	
	
	
	
}