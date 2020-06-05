package com.fx.service.wxdat;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.wxdat.WxGlobal;

public interface WxGlobalService extends BaseService<WxGlobal,Long> {

	/**
	 * 获取微信公众号授权token或者jsApiTicket票据, 如过期则重新获取
	 * @param teamNo 	车队编号
	 * @param type 		1-获取token；2-获取jsApiTicket票据；
	 */
	public String findTokenOrTicket(String teamNo, int type);
}
