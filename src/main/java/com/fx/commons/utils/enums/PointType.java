package com.fx.commons.utils.enums;

/**
 * 地图点类型
 */
public enum PointType {
	/** 上车点 */
	UP_POINT,
	/** 下车点 */
	DOWN_POINT,
	/** 途径点 */
	WAY_POINT,
	
	/** 其他 */
	OTHER;
	/*===其他--end==============*/
	
	/**
	 * 获取-描述
	 */
	public String getText(){
		switch(this){
			case UP_POINT:
				return "上车点";
			case DOWN_POINT:
				return "下车点";
			case WAY_POINT:
				return "途径点";
			default:
				return "其他";
		}
	}
	
}
