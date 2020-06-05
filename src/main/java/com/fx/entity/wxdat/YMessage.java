package com.fx.entity.wxdat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 所有系统消息管理类
 * @author qfc
 * @Date 20200521
 */
@Entity
public class YMessage {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;
	/**
	 * 消息内容
	 */
	@Column(length=500)
	private String mesContent;
	/**
	 * 发送用户
	 */
	@Column
	private String sendUser;
	/**
	 * 接收用户
	 */
	@Column
	private String receiveUser;
	/**
	 * 发送时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime;
	/**
	 * 消息所属 0:后台管理  1：会员系统  2:车队管理系统
	 */
	@Column
	private int mesThe;
	/**
	 * 消息状态 0：未读 1：已读 2：暂时忽略 3：已删除 ...
	 */
	@Column
	private int mesState;
	/**
	 * 消息类型 0：普通消息 1：报价消息 2：订单消息 3:记事本 4：提现消息
	 */
	@Column
	private int mesType;
	/**
	 * 命令跳转URL
	 */
	@Column
	private String goUrl;
	
	
	
	/**  
	 * 获取id  
	 * @return id id  
	 */
	public long getId() {
		return id;
	}
	/**  
	 * 设置id  
	 * @param id id  
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**  
	 * 获取消息内容  
	 * @return mesContent 消息内容  
	 */
	public String getMesContent() {
		return mesContent;
	}
	/**  
	 * 设置消息内容  
	 * @param mesContent 消息内容  
	 */
	public void setMesContent(String mesContent) {
		this.mesContent = mesContent;
	}
	/**  
	 * 获取发送用户  
	 * @return sendUser 发送用户  
	 */
	public String getSendUser() {
		return sendUser;
	}
	/**  
	 * 设置发送用户  
	 * @param sendUser 发送用户  
	 */
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	/**  
	 * 获取接收用户  
	 * @return receiveUser 接收用户  
	 */
	public String getReceiveUser() {
		return receiveUser;
	}
	/**  
	 * 设置接收用户  
	 * @param receiveUser 接收用户  
	 */
	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
	/**  
	 * 获取发送时间  
	 * @return sendTime 发送时间  
	 */
	public Date getSendTime() {
		return sendTime;
	}
	/**  
	 * 设置发送时间  
	 * @param sendTime 发送时间  
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	/**  
	 * 获取消息所属0:后台管理1：会员系统2:车队管理系统  
	 * @return mesThe 消息所属0:后台管理1：会员系统2:车队管理系统  
	 */
	public int getMesThe() {
		return mesThe;
	}
	/**  
	 * 设置消息所属0:后台管理1：会员系统2:车队管理系统  
	 * @param mesThe 消息所属0:后台管理1：会员系统2:车队管理系统  
	 */
	public void setMesThe(int mesThe) {
		this.mesThe = mesThe;
	}
	/**  
	 * 获取消息状态0：未读1：已读2：暂时忽略3：已删除...  
	 * @return mesState 消息状态0：未读1：已读2：暂时忽略3：已删除...  
	 */
	public int getMesState() {
		return mesState;
	}
	/**  
	 * 设置消息状态0：未读1：已读2：暂时忽略3：已删除...  
	 * @param mesState 消息状态0：未读1：已读2：暂时忽略3：已删除...  
	 */
	public void setMesState(int mesState) {
		this.mesState = mesState;
	}
	/**  
	 * 获取消息类型0：普通消息1：报价消息2：订单消息3:记事本  
	 * @return mesType 消息类型0：普通消息1：报价消息2：订单消息3:记事本  
	 */
	public int getMesType() {
		return mesType;
	}
	/**  
	 * 设置消息类型0：普通消息1：报价消息2：订单消息3:记事本  
	 * @param mesType 消息类型0：普通消息1：报价消息2：订单消息3:记事本  
	 */
	public void setMesType(int mesType) {
		this.mesType = mesType;
	}
	/**  
	 * 获取命令跳转URL  
	 * @return goUrl 命令跳转URL  
	 */
	public String getGoUrl() {
		return goUrl;
	}
	/**  
	 * 设置命令跳转URL  
	 * @param goUrl 命令跳转URL  
	 */
	public void setGoUrl(String goUrl) {
		this.goUrl = goUrl;
	}
}
