package com.fx.entity.cus.permi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 单位菜单-关联表
 */
@Entity
@Table(name="company_menu")
public class CompanyMenu implements Serializable {
	private static final long serialVersionUID = -6675698387955906270L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 所属单位编号 */
	@Column(name="unit_num", nullable=false, columnDefinition="varchar(30) COMMENT '所属单位编号'")
	private String unitNum;
	
	/** 菜单资源对象 */
	@OneToOne(targetEntity = Menu.class)
	@JoinColumn(name="menu_id", nullable=false, referencedColumnName="id", columnDefinition="bigint COMMENT '菜单资源对象'")
	private Menu menuId;
	
	/** 父级id */
	@Column(name="pid", columnDefinition="bigint COMMENT '父级id'")
	private long pid;
	
	/** 菜单名称 */
	@Column(name="menu_name", nullable=false, columnDefinition="varchar(30) COMMENT '菜单名称'")
	private String menuName;
	
	/** 开关状态 1-开启；0-关闭； */
	@Column(name="oc_state", nullable=false, columnDefinition="int default 1 COMMENT '开关状态 1-开启；0-关闭；'")
	private int ocState;
	
	/** 备注序号 */
	@Column(name="sort_no", nullable=false, columnDefinition="int default 0 COMMENT '备注序号'")
	private int sortNo;
	
	public CompanyMenu() {}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the 所属单位编号
	 */
	public String getUnitNum() {
		return unitNum;
	}
	

	/**
	 * @param 所属单位编号 the unitNum to set
	 */
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	

	/**
	 * @return the 菜单资源对象
	 */
	public Menu getMenuId() {
		return menuId;
	}
	

	/**
	 * @param 菜单资源对象 the menuId to set
	 */
	public void setMenuId(Menu menuId) {
		this.menuId = menuId;
	}
	

	/**
	 * @return the 父级id
	 */
	public long getPid() {
		return pid;
	}
	

	/**
	 * @param 父级id the pid to set
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}
	

	/**
	 * @return the 菜单名称
	 */
	public String getMenuName() {
		return menuName;
	}
	

	/**
	 * @param 菜单名称 the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	

	/**
	 * @return the 开关状态0-开启；1-关闭；
	 */
	public int getOcState() {
		return ocState;
	}
	

	/**
	 * @param 开关状态0-开启；1-关闭； the ocState to set
	 */
	public void setOcState(int ocState) {
		this.ocState = ocState;
	}
	

	/**
	 * @return the 备注序号
	 */
	public int getSortNo() {
		return sortNo;
	}
	

	/**
	 * @param 备注序号 the sortNo to set
	 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}