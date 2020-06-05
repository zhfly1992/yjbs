package com.fx.service.impl.cus.permi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.permi.TbRoleMenuDao;
import com.fx.entity.cus.permi.RoleMenu;
import com.fx.service.cus.permi.RoleMenuService;

@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu, Long> implements RoleMenuService {
	
	/** 角色资源关联-数据源 */
	@Autowired
	private TbRoleMenuDao tbRoleMenuDao;
	@Override
	public ZBaseDaoImpl<RoleMenu, Long> getDao() {
		return tbRoleMenuDao;
	}
	
	
	
}
