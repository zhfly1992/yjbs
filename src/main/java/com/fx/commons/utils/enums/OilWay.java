package com.fx.commons.utils.enums;


/**
 * 加油-方式
 */
public enum OilWay {
	/** 现金加油 */
	XJ_JY,
	/** 油票加油 */
	YP_JY,
	/** 油罐车加油 */
	YGC_JY,
	/** 充值卡加油 */
	CZK_JY,
	
	OTHER;
	/*===其他--end==============*/
	
	/**
	 * 文本描述
	 */
	public String getOilWayText(){
		switch(this){
			case XJ_JY:
				return "现金加油";
			case YP_JY:
				return "油票加油";
			case YGC_JY:
				return "油罐车加油";
			case CZK_JY:
				return "充值卡加油";
				
			default:
				return "其他";
		}
	}
	
	/**
	 * 获取-值
	 * @param text 文本描述
	 * @return 值
	 */
	public static OilWay getOilWayVal(String text){
		OilWay res = null;
		if("现金加油".equals(text)){
			res = OilWay.XJ_JY;
		}else if("油票加油".equals(text)){
			res = OilWay.YP_JY;
		}else if("油罐车加油".equals(text)){
			res = OilWay.YGC_JY;
		}else if("充值卡加油".equals(text)){
			res = OilWay.CZK_JY;
		}
			
		return res;
	}
	
}
