package com.fx.entity.finance;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.CourseCategory;



/**
 *  凭证科目列表
 *  @author xx
 *  @date 20200508
 */
@Entity
@Table(name="fee_course")
//@JsonFilter("fitFeeCourse")
public class FeeCourse implements Serializable {
	private static final long serialVersionUID = -4886305101465804656L;

	/** 唯一id 自增长 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/** 单位编号 eg：Uxxxx */
	@Column(name="unit_num",nullable=false, columnDefinition="varchar(20) COMMENT '单位编号'")
	private String unitNum;
	
	/** 科目类别 */
	@Enumerated(EnumType.STRING)
	@Column(name="course_category", columnDefinition="varchar(20) COMMENT '科目类别'")
	private CourseCategory courseCategory;
	
	/** 科目编码 */
	@Column(name="course_num", nullable=false, columnDefinition="varchar(100) COMMENT '科目编码'")
	private String courseNum;

	/** 科目名称 */
	@Column(name="course_name", nullable=false,unique=true, columnDefinition="varchar(50) COMMENT '科目名称'")
	private String courseName;
	
	/** 科目简拼 */
	@Column(name="pinyin_simple", nullable=false, columnDefinition="varchar(50) COMMENT '科目简拼'")
	private String pinyinSimple;
	
	/** 收支  0收 1支*/
	@Column(name="course_type", nullable=false, columnDefinition="int(11) default 0 COMMENT '0收 1支'")
	private int courseType;
	
	/** 状态  0可用 1不可用*/
	@Column(name="course_status", nullable=false, columnDefinition="int(11) default 0 COMMENT '状态  0可用 1不可用'")
	private String courseStatus;
	
	/** 添加时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", nullable=false, columnDefinition="datetime COMMENT '添加时间'")
	private Date addTime;
	
	/** 层级 */
	@Column(name="level", nullable=false, columnDefinition="int(11) default 1 COMMENT '科目层级，1为最高层级,3为最低级'")
	private int level;
	
	/** 上层科目id*/
	@Column(name="parent_id",columnDefinition="bigint(20)  COMMENT '上层科目id,若是根层级则为null'")
	private Long parentId;
	
	/** 余额 */
	@Column(name="balance", columnDefinition="double(10,2) default '0.00' COMMENT '余额'")
	private double balance;
	
	public FeeCourse() {}
	
	public FeeCourse(long id, String courseName) {
		super();
		this.id = id;
		this.courseName = courseName;
	}

	/**  
	 * 获取 唯一id自增长主键  
	 * @return 唯一id自增长主键
	 */
	public long getId() {
		return id;
	}

	/**  
	 * 设置 唯一id自增长主键  
	 * @param 唯一id自增长主键
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
	 * 获取 科目类别  
	 * @return courseCategory
	 */
	public CourseCategory getCourseCategory() {
		return courseCategory;
	}
	

	/**  
	 * 设置 科目类别  
	 * @param courseCategory 
	 */
	public void setCourseCategory(CourseCategory courseCategory) {
		this.courseCategory = courseCategory;
	}
	

	/**  
	 * 获取 科目编码  
	 * @return courseNum
	 */
	public String getCourseNum() {
		return courseNum;
	}
	

	/**  
	 * 设置 科目编码  
	 * @param courseNum 
	 */
	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}
	

	/**  
	 * 获取 科目名称  
	 * @return courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	

	/**  
	 * 设置 科目名称  
	 * @param courseName 
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	

	/**  
	 * 获取 科目简拼  
	 * @return pinyinSimple
	 */
	public String getPinyinSimple() {
		return pinyinSimple;
	}
	

	/**  
	 * 设置 科目简拼  
	 * @param pinyinSimple 
	 */
	public void setPinyinSimple(String pinyinSimple) {
		this.pinyinSimple = pinyinSimple;
	}
	


	/**  
	 * 获取 收支0收1支  
	 * @return courseType
	 */
	public int getCourseType() {
		return courseType;
	}
	

	/**  
	 * 设置 收支0收1支  
	 * @param courseType 
	 */
	public void setCourseType(int courseType) {
		this.courseType = courseType;
	}
	

	/**  
	 * 获取 状态0可用1不可用  
	 * @return courseStatus
	 */
	public String getCourseStatus() {
		return courseStatus;
	}
	

	/**  
	 * 设置 状态0可用1不可用  
	 * @param courseStatus 
	 */
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
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
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**  
	 * 获取 余额  
	 * @return balance
	 */
	public double getBalance() {
		return balance;
	}
	

	/**  
	 * 设置 余额  
	 * @param balance 
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	

}
