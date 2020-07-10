package com.fx.service.impl.cus.permi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.permi.CompanyMenuDao;
import com.fx.entity.cus.permi.CompanyMenu;
import com.fx.service.cus.permi.CompanyMenuService;

@Service
@Transactional
public class CompanyMenuServiceImpl extends BaseServiceImpl<CompanyMenu, Long> implements CompanyMenuService {
	
	/** 单位菜单-数据源 */
	@Autowired
	private CompanyMenuDao companyMenuDao;
	@Override
	public ZBaseDaoImpl<CompanyMenu, Long> getDao() {
		return companyMenuDao;
	}
	
	
	
}
