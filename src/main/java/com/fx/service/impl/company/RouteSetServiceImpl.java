package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.RouteSetDao;
import com.fx.entity.company.RouteSet;
import com.fx.service.company.RouteSetService;

@Service
@Transactional
public class RouteSetServiceImpl extends BaseServiceImpl<RouteSet,Long> implements RouteSetService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private RouteSetDao routeSetDao;
	@Override
	public ZBaseDaoImpl<RouteSet, Long> getDao() {
		return routeSetDao;
	}
	
	
}
