package com.fx.service.cus.permi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.permi.Role;

public interface RoleService extends BaseService<Role, Long> {

	/**
	 * 获取-用户角色-列表
	 * @param uname 用户名
	 * @return 角色列表
	 */
	public List<Role> findUserRole(String uname);
	
	
	public Map<String, Object> getRoleByDeptId(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request,String deptId);
	
}
