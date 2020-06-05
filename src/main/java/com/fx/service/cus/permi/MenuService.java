package com.fx.service.cus.permi;

import java.util.List;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.permi.Menu;

public interface MenuService extends BaseService<Menu, Long> {

	/**
	 * 获取-用户资源-列表
	 * @param uname 用户名
	 * @return 用户资源列表
	 */
	public List<Menu> findUserPermissions(String uname);
	
}
