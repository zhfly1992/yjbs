package com.fx.service.impl.cus.permi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.permi.TbUserRoleDao;
import com.fx.entity.cus.permi.UserRole;
import com.fx.service.cus.permi.UserRoleService;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, Long> implements UserRoleService {
	
	/** 用户角色关联-数据源 */
	@Autowired
	private TbUserRoleDao tbUserRoleDao;
	@Override
	public ZBaseDaoImpl<UserRole, Long> getDao() {
		return tbUserRoleDao;
	}
	
	
	
}
