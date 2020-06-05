package com.fx.service.wxdat;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.wxdat.WxPublicData;

public interface WxPublicDataService extends BaseService<WxPublicData, Long> {
	
	/**
	 * 获取-指定车队微信公众号数据
	 * @param teamNo 车队编号
	 * @return 数据对象
	 */
	public WxPublicData getWxPublicData(String teamNo);
	
}
