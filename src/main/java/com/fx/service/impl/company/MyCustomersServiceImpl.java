package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.MyCustomersDao;
import com.fx.entity.company.MyCustomers;
import com.fx.service.company.MyCustomersService;

@Service
@Transactional
public class MyCustomersServiceImpl extends BaseServiceImpl<MyCustomers,Long> implements MyCustomersService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MyCustomersDao myCustomersDao;
	@Override
	public ZBaseDaoImpl<MyCustomers, Long> getDao() {
		return myCustomersDao;
	}
	
	
}
