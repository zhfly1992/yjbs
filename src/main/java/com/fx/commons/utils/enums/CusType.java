package com.fx.commons.utils.enums;

/**
 * 单位客户-类型
 */
public enum CusType {

	/** 旅行社 */
	TRAVEL,
	/** 加油站*/
	OILSTATION,
	/** 维修厂 */
	REPAIR,
	/** 合作车队*/
	PARTUNIT,
	/** 学校*/
	SCHOOL,
	/** 公司*/
	COMPANY,
	/** 政府*/
	GOVERNMENT,
	/** 个人*/
	PERSONAL,
	/** 其他*/
	OTHER;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case TRAVEL:
				return "旅行社";
			case OILSTATION:
				return "加油站";
			case REPAIR:
				return "维修厂";
			case PARTUNIT:
				return "合作单位";
			case SCHOOL:
				return "学校";
			case COMPANY:
				return "公司";
			case GOVERNMENT:
				return "政府";
			case PERSONAL:
				return "个人";
			default:
				return "其他";
		}
	}
	
}
