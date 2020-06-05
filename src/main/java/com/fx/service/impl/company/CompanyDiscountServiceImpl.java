package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.CompanyDiscountDao;
import com.fx.entity.company.CompanyDiscount;
import com.fx.service.company.CompanyDiscountService;

@Service
@Transactional
public class CompanyDiscountServiceImpl extends BaseServiceImpl<CompanyDiscount,Long> implements CompanyDiscountService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private CompanyDiscountDao companyDiscountDao;
	@Override
	public ZBaseDaoImpl<CompanyDiscount, Long> getDao() {
		return companyDiscountDao;
	}
	
	
}
