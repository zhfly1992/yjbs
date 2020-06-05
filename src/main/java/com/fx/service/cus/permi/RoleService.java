package com.fx.service.cus.permi;

import java.util.List;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.permi.Role;

public interface RoleService extends BaseService<Role, Long> {

	/**
	 * 获取-用户角色-列表
	 * @param uname 用户名
	 * @return 角色列表
	 */
	public List<Role> findUserRole(String uname);
	
}
