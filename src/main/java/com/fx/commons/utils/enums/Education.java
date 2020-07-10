package com.fx.commons.utils.enums;
/**
 * 学历
 */
public enum Education {

	/** 小学*/
	PRIMARYSCHOOL,
	/** 初中 */
	JUNIORSCHOOL,
	/** 中专、高中 */
	HIGHSCHOOL,
	/** 专科*/
	COLLEGEDEGREE,
	/**本科 */
	BACHELOR,
	/**硕士 */
	MASTER,
	/**博士 */
	DOCTOR;
	
	/** 获取-描述 */
	public String getText() {
		switch (this) {
			case PRIMARYSCHOOL:
				return "小学";
			case JUNIORSCHOOL:
				return "初中";
			case HIGHSCHOOL:
				return "中专/高中";
			case COLLEGEDEGREE:
				return "专科";
			case BACHELOR:
				return "本科";
			case MASTER:
				return "硕士";
			case DOCTOR:
				return "博士";
			default:
				return "其他";
		}
	}
}
