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
 * 单位微信公众号-模板消息-数据
 */
@Entity
@Table(name="wx_tplmsg_data")
public class WxTplmsgData implements Serializable {
	private static final long serialVersionUID = -3579733898937719709L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 所属单位编号
	 */
	@Column(name="company_num", nullable=false, columnDefinition="varchar(30) COMMENT '所属单位编号'")
	private String companyNum;
	/**
	 * 自定义模板编号（例如：K001）
	 */
	@Column(name="tpl_no", nullable=false, columnDefinition="varchar(20) COMMENT '自定义模板编号'")
	private String tplNo;
	/**
	 * 模板消息标题
	 */
	@Column(name="tplmsg_title", nullable=false, columnDefinition="varchar(20) COMMENT '模板消息标题'")
	private String tplmsgTitle;
	/**
	 * 模板id
	 */
	@Column(name="tpl_id", nullable=false, columnDefinition="varchar(100) COMMENT '模板id'")
	private String tplId;
	/**
	 * 模板参数数组字符串
	 */
	@Column(name="ps_arr", nullable=false, columnDefinition="text COMMENT '模板参数数组字符串'")
	private String psArr;
	/**
	 * 添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="atime", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date atime;
	
	
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
	 * 获取 所属单位编号  
	 * @return companyNum
	 */
	public String getCompanyNum() {
		return companyNum;
	}

	/**  
	 * 设置 所属单位编号  
	 * @param companyNum
	 */
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	
	/**  
	 * 获取 自定义模板编号（例如：K001）  
	 * @return tplNo
	 */
	public String getTplNo() {
		return tplNo;
	}
	
	/**  
	 * 设置 自定义模板编号（例如：K001）  
	 * @param tplNo
	 */
	public void setTplNo(String tplNo) {
		this.tplNo = tplNo;
	}
	
	/**  
	 * 获取 模板消息标题  
	 * @return tplmsgTitle
	 */
	public String getTplmsgTitle() {
		return tplmsgTitle;
	}
	

	/**  
	 * 设置 模板消息标题  
	 * @param tplmsgTitle
	 */
	public void setTplmsgTitle(String tplmsgTitle) {
		this.tplmsgTitle = tplmsgTitle;
	}
	

	/**  
	 * 获取 模板id  
	 * @return tplId
	 */
	public String getTplId() {
		return tplId;
	}
	
	/**  
	 * 设置 模板id  
	 * @param tplId
	 */
	public void setTplId(String tplId) {
		this.tplId = tplId;
	}
	
	/**  
	 * 获取 模板参数数组字符串  
	 * @return psArr
	 */
	public String getPsArr() {
		return psArr;
	}
	
	/**  
	 * 设置 模板参数数组字符串  
	 * @param psArr
	 */
	public void setPsArr(String psArr) {
		this.psArr = psArr;
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
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
