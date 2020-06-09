package com.fx.entity.back.scenic_spots_dat;

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
 * 景点说明
 */
@Entity
@Table(name="scenic_spots_explain")
public class ScenicSpotsExplain implements Serializable {
	private static final long serialVersionUID = 3695472327175146605L;
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 开放时间-说明
	 */
	@Column(name="open_time_exp", columnDefinition="text COMMENT '开放时间-说明'")
	private String openTimeExp;
	/**
	 * 门票-说明
	 */
	@Column(name="ticket_exp", columnDefinition="text COMMENT '门票-说明'")
	private String ticketExp;
	/**
	 * 公告
	 */
	@Column(name="notice", columnDefinition="text COMMENT '公告'")
	private String notice;
	/**
	 * 其他-说明
	 */
	@Column(name="other_exp", columnDefinition="text COMMENT '其他-说明'")
	private String otherExp;
	/**
	 * 景区-简介（图文说明）
	 */
	@Column(name="detailExp", columnDefinition="longtext COMMENT '景区-简介（图文说明）'")
	private String detailExp;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	/**
	 * 操作备注 eg:张三-18888888888-添加=2020-03-07 10:30:00; 
	 */
	@Column(name="oper_note", columnDefinition="text COMMENT '操作备注'")
	private String operNote;
	
	
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
	 * 获取 开放时间-说明  
	 * @return openTimeExp
	 */
	public String getOpenTimeExp() {
		return openTimeExp;
	}
	
	/**  
	 * 设置 开放时间-说明  
	 * @param openTimeExp
	 */
	public void setOpenTimeExp(String openTimeExp) {
		this.openTimeExp = openTimeExp;
	}
	
	/**  
	 * 获取 门票-说明  
	 * @return ticketExp
	 */
	public String getTicketExp() {
		return ticketExp;
	}
	
	/**  
	 * 设置 门票-说明  
	 * @param ticketExp
	 */
	public void setTicketExp(String ticketExp) {
		this.ticketExp = ticketExp;
	}
	
	/**  
	 * 获取 公告  
	 * @return notice
	 */
	public String getNotice() {
		return notice;
	}
	
	/**  
	 * 设置 公告  
	 * @param notice
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	/**  
	 * 获取 其他-说明  
	 * @return otherExp
	 */
	public String getOtherExp() {
		return otherExp;
	}
	
	/**  
	 * 设置 其他-说明  
	 * @param otherExp
	 */
	public void setOtherExp(String otherExp) {
		this.otherExp = otherExp;
	}
	
	/**  
	 * 获取 景区-简介（图文说明）  
	 * @return detailExp
	 */
	public String getDetailExp() {
		return detailExp;
	}

	/**  
	 * 设置 景区-简介（图文说明）  
	 * @param detailExp
	 */
	public void setDetailExp(String detailExp) {
		this.detailExp = detailExp;
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
	 * 获取 操作备注eg:张三-18888888888-添加=2020-03-0710:30:00;  
	 * @return operNote
	 */
	public String getOperNote() {
		return operNote;
	}
	
	/**  
	 * 设置 操作备注eg:张三-18888888888-添加=2020-03-0710:30:00;  
	 * @param operNote
	 */
	public void setOperNote(String operNote) {
		this.operNote = operNote;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
