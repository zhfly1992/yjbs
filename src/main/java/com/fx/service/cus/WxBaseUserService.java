package com.fx.service.cus;

import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.WxBaseUser;

public interface WxBaseUserService extends BaseService<WxBaseUser, Long> {

	/**
	 * 设置-用户主微信号
	 * @param reqsrc 	请求来源 
	 * @param lunitNum 	登录单位编号
	 * @param luname 	登录用户名
	 * @param smsCode 	当前所绑定的手机号短信验证码
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> updMainWx(ReqSrc reqsrc, String lunitNum, String luname, String smsCode);
	
}
