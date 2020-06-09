package com.fx.commons.utils.enums;


/**
 * 排序方式
 */
public enum SortType {
	/** 初始排序 */
	INIT,
	/** 向上 */
	UP,
	/** 向下 */
	DOWN,
	/** 切换 */
	EXCH,
	
	/** 其他 */
	OTHER;
	
	/** 文本描述 */
	public String getText(){
		switch(this){
			case INIT:
				return "初始排序";
			case UP:
				return "向上";
			case DOWN:
				return "向下";
			case EXCH:
				return "切换";
				
			default:
				return "其他";
		}
	}
	
}
