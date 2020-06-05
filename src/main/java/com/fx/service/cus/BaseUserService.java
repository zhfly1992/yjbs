package com.fx.service.cus;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.BaseUser;

public interface BaseUserService extends BaseService<BaseUser, Long> {

	/**
	 * 获取-用户通过用户名
	 * @param uname 用户名
	 * @return 用户对象
	 */
	public BaseUser findByUname(String uname);
	
	/**
	 * 获取-用户通过手机号
	 * @param phone 手机号
	 * @return 用户对象
	 */
	public BaseUser findByPhone(String phone);
	
}
