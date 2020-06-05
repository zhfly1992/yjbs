package com.fx.commons.utils.enums;


/**
 * 显示/隐藏-状态
 */
public enum ShStatus {
	/** 隐藏-状态 */
	HIDDEN_STA,
	/** 显示-状态 */
	SHOW_STA,
	
	OTHER;
	/*===其他--end==============*/
	
	/**
	 * 文本描述
	 */
	public String getShStatusText(){
		switch(this){
			case HIDDEN_STA:
				return "隐藏";
			case SHOW_STA:
				return "显示";
				
			default:
				return "其他";
		}
	}
	
}
