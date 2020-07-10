package com.fx.service.impl.cus.permi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.permi.RoleMenuDao;
import com.fx.entity.cus.permi.RoleMenu;
import com.fx.service.cus.permi.RoleMenuService;

@Service
@Transactional
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu, Long> implements RoleMenuService {
	
	/** 角色资源关联-数据源 */
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Override
	public ZBaseDaoImpl<RoleMenu, Long> getDao() {
		return roleMenuDao;
	}
	
	
	
}
