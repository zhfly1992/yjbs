package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.PartnerCustomerDao;
import com.fx.entity.company.PartnerCustomer;
import com.fx.service.company.PartnerCustomerService;

@Service
@Transactional
public class PartnerCustomerServiceImpl extends BaseServiceImpl<PartnerCustomer,Long> implements PartnerCustomerService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private PartnerCustomerDao partnerCustomerDao;
	@Override
	public ZBaseDaoImpl<PartnerCustomer, Long> getDao() {
		return partnerCustomerDao;
	}
	
	
}
