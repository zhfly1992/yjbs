package com.fx.entity.wxdat;

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
 * @author XX
 * @date 20160317
 * 微信授权信息
 */
@Entity
@Table(name="wx_global")
public class WxGlobal implements Serializable {
	private static final long serialVersionUID = 1348569668531339516L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 所属车队编号
	 */
	@Column(name="team_no", nullable=false, columnDefinition="varchar(50) COMMENT '所属车队编号'")
	private String teamNo;
	/**
	 * 微信appId
	 */
	@Column(name="wechat_app_id", columnDefinition="varchar(20) COMMENT '微信appId'")
	private String wechatAppId;
	/**
	 * 微信appSecret
	 */
	@Column(name="wechat_app_secret", columnDefinition="varchar(20) COMMENT '微信appSecret'")
	private String wechatAppSecret;
	/**
	 * 微信商户号
	 */
	@Column(name="merchant_id", columnDefinition="varchar(20) COMMENT '微信商户号'")
	private String merchantId;
	/**
	 * 微信商户Key
	 */
	@Column(name="merchant_key", columnDefinition="varchar(20) COMMENT '微信商户Key'")
	private String merchantKey;
	/**
	 * 微信令牌
	 */
	@Column(name="access_token", columnDefinition="text COMMENT '唯一通过令牌'")
	private String accessToken;
	/**
	 * token有效时间（秒）
	 */
	@Column(name="valid_time", columnDefinition="Long COMMENT '有效时间'")
	private Long validTime;
	/**
	 * 令牌获取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '令牌获取时间'")
	private Date addTime;
	/**
	 * js接口的临时票据
	 */
	@Column(name="js_api_ticket", columnDefinition="text COMMENT 'js接口的临时票据'")
	private String jsApiTicket;
	/**
	 * js接口的临时票据有效时间（秒）
	 */
	@Column(name="js_api_valid_time", columnDefinition="Long COMMENT 'js接口的临时票据有效时间'")
	private Long jsApiValidTime;
	/**
	 * js票据获取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="js_api_add_time", columnDefinition="datetime COMMENT 'js票据获取时间'")
	private Date jsApiAddTime;
	/**
	 * 中信有效连接时间
	 */
	@Column(name="zxvalid_time", columnDefinition="Long COMMENT '中信有效连接时间'")
	private Long zxvalidTime;
	/**
	 * 中信最后一次连接时间
	 * 如果企业半小时未向银行发起直连报文。那么在发起对外支付时，需要先发起一笔查询类的报文，建议使用“余额查询”报文来启动自动联通
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="zx_time", columnDefinition="datetime COMMENT '中信最后一次连接时间'")
	private Date zxTime;
	
	
	/**
	 * [威富通]微信令牌token
	 */
	@Column(name="wft_access_token", columnDefinition="text COMMENT '[威富通]微信令牌token'")
	private String wftAccessToken;
	/**
	 * [威富通]微信令牌token获取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="wft_add_time", columnDefinition="datetime COMMENT '[威富通]微信令牌token获取时间'")
	private Date wftAddTime;
	/**
	 * [威富通]jsApi接口的临时票据
	 */
	@Column(name="wft_js_api_ticket", columnDefinition="text COMMENT '[威富通]jsApi接口的临时票据'")
	private String wftJsApiTicket;
	/**
	 * [威富通]jsApi接口的临时票据获取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="wft_js_api_add_time", columnDefinition="datetime COMMENT '[威富通]jsApi接口的临时票据获取时间'")
	private Date wftJsApiAddTime;
	
	
	public long getId() {
		return id;
	}
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
	
	public String getWechatAppId() {
		return wechatAppId;
	}
	public void setWechatAppId(String wechatAppId) {
		this.wechatAppId = wechatAppId;
	}
	public String getWechatAppSecret() {
		return wechatAppSecret;
	}
	public void setWechatAppSecret(String wechatAppSecret) {
		this.wechatAppSecret = wechatAppSecret;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * 获取 微信令牌token有效时间（秒）
	 * @return
	 */
	public Long getValidTime() {
		return validTime;
	}
	/**
	 * 设置 微信令牌token有效时间（秒）
	 * @param validTime
	 */
	public void setValidTime(Long validTime) {
		this.validTime = validTime;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getJsApiTicket() {
		return jsApiTicket;
	}
	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}
	/**
	 * js接口的临时票据有效时间（秒）
	 * @return jsApiValidTime
	 */
	public Long getJsApiValidTime() {
		return jsApiValidTime;
	}
	/**
	 * js接口的临时票据有效时间（秒）
	 * @param jsApiValidTime
	 */
	public void setJsApiValidTime(Long jsApiValidTime) {
		this.jsApiValidTime = jsApiValidTime;
	}
	public Date getJsApiAddTime() {
		return jsApiAddTime;
	}
	public void setJsApiAddTime(Date jsApiAddTime) {
		this.jsApiAddTime = jsApiAddTime;
	}
	public Long getZxvalidTime() {
		return zxvalidTime;
	}
	public void setZxvalidTime(Long zxvalidTime) {
		this.zxvalidTime = zxvalidTime;
	}
	public Date getZxTime() {
		return zxTime;
	}
	public void setZxTime(Date zxTime) {
		this.zxTime = zxTime;
	}
	
	
	/**
	 * [威富通]微信令牌token
	 * @return wftAccessToken
	 */
	public String getWftAccessToken() {
		return wftAccessToken;
	}
	/**
	 * [威富通]微信令牌token
	 * @param wftAccessToken
	 */
	public void setWftAccessToken(String wftAccessToken) {
		this.wftAccessToken = wftAccessToken;
	}
	/**
	 * [威富通]微信令牌token获取时间
	 * @return wftAddTime
	 */
	public Date getWftAddTime() {
		return wftAddTime;
	}
	/**
	 * [威富通]微信令牌token获取时间
	 * @param wftAddTime
	 */
	public void setWftAddTime(Date wftAddTime) {
		this.wftAddTime = wftAddTime;
	}
	/**
	 * [威富通]jsApi接口的临时票据
	 * @return wftJsApiTicket
	 */
	public String getWftJsApiTicket() {
		return wftJsApiTicket;
	}
	/**
	 * [威富通]jsApi接口的临时票据
	 * @param wftJsApiTicket
	 */
	public void setWftJsApiTicket(String wftJsApiTicket) {
		this.wftJsApiTicket = wftJsApiTicket;
	}
	/**
	 * [威富通]jsApi接口的临时票据获取时间
	 * @return wftJsApiAddTime
	 */
	public Date getWftJsApiAddTime() {
		return wftJsApiAddTime;
	}
	/**
	 * [威富通]jsApi接口的临时票据获取时间
	 * @param wftJsApiAddTime
	 */
	public void setWftJsApiAddTime(Date wftJsApiAddTime) {
		this.wftJsApiAddTime = wftJsApiAddTime;
	}
	
} 
