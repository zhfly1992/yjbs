package com.fx.service.impl.cus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.entity.cus.CompanyUser;
import com.fx.service.cus.CompanyUserService;

@Service
@Transactional
public class CompanyUserServiceImpl extends BaseServiceImpl<CompanyUser, Long> implements CompanyUserService {
	
	/** 单位用户-数据源 */
	@Autowired
	private CompanyUserDao companyUserDao;
	@Override
	public ZBaseDaoImpl<CompanyUser, Long> getDao() {
		return companyUserDao;
	}
	
	
	
}
