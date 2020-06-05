package com.fx.entity.back;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 中国省份表
 * @author xx
 * @date 20200426
 */
@Entity
@Table(name="province_list")
public class ProvinceList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    /**
     * 省份名称
     */
	@Column(name="province_name", nullable=false, columnDefinition="varchar(20) COMMENT '省份名称'")
	private String provinceName;
	/**
	 * 省份编号
	 */
	@Column(name="province_code", nullable=false, columnDefinition="varchar(20) COMMENT '省份编号'")
	private String provinceCode;
	/**
	 * 省份标识
	 */
	@Column(name="province_mark", columnDefinition="varchar(20) COMMENT '省份标识'")
	private String provinceMark;
	
	public ProvinceList() {
	}
	public ProvinceList(String provinceName, String provinceCode) {
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}
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
	 * 获取 省份名称  
	 * @return provinceName
	 */
	public String getProvinceName() {
		return provinceName;
	}
	
	/**  
	 * 设置 省份名称  
	 * @param provinceName 
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	/**  
	 * 获取 省份编号  
	 * @return provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	
	/**  
	 * 设置 省份编号  
	 * @param provinceCode 
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	/**  
	 * 获取 省份标识  
	 * @return provinceMark
	 */
	public String getProvinceMark() {
		return provinceMark;
	}
	
	/**  
	 * 设置 省份标识  
	 * @param provinceMark 
	 */
	public void setProvinceMark(String provinceMark) {
		this.provinceMark = provinceMark;
	}
	
	

}
