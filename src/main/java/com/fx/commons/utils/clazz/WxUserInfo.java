package com.fx.commons.utils.clazz;

import java.io.Serializable;

/**
 * 微信用户信息-类
 */
public class WxUserInfo implements Serializable {
	private static final long serialVersionUID = 1956054805533173238L;
	
	/** 微信id */
	private String openid;
	/** 昵称 */
	private String nickname;
	/** 性别 */
	private String sex;
	/** 用户的语言 */
	private String language;
	/** 用户所在城市 */
	private String city;
	/** 用户所在省份 */
	private String province;
	/** 用户所在国家 */
	private String country;
	/** 用户头像 */
	private String headimgurl;
	/** 其他 */
	private String privilege;
	
	
	/**  
	 * 获取 微信id  
	 * @return openid
	 */
	public String getOpenid() {
		return openid;
	}
	
	/**  
	 * 设置 微信id  
	 * @param openid
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	/**  
	 * 获取 昵称  
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**  
	 * 设置 昵称  
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**  
	 * 获取 性别  
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}
	
	/**  
	 * 设置 性别  
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**  
	 * 获取 用户的语言  
	 * @return language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**  
	 * 设置 用户的语言  
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**  
	 * 获取 用户所在城市  
	 * @return city
	 */
	public String getCity() {
		return city;
	}
	
	/**  
	 * 设置 用户所在城市  
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**  
	 * 获取 用户所在省份  
	 * @return province
	 */
	public String getProvince() {
		return province;
	}
	
	/**  
	 * 设置 用户所在省份  
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	
	/**  
	 * 获取 用户所在国家  
	 * @return country
	 */
	public String getCountry() {
		return country;
	}
	
	/**  
	 * 设置 用户所在国家  
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**  
	 * 获取 用户头像  
	 * @return headimgurl
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}
	
	/**  
	 * 设置 用户头像  
	 * @param headimgurl
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	
	/**  
	 * 获取 其他  
	 * @return privilege
	 */
	public String getPrivilege() {
		return privilege;
	}
	
	/**  
	 * 设置 其他  
	 * @param privilege
	 */
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
