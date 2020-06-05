package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.PickupAreaSetDao;
import com.fx.entity.company.PickupAreaSet;
import com.fx.service.company.PickupAreaSetService;

@Service
@Transactional
public class PickupAreaSetServiceImpl extends BaseServiceImpl<PickupAreaSet,Long> implements PickupAreaSetService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private PickupAreaSetDao pickupAreaSetDao;
	@Override
	public ZBaseDaoImpl<PickupAreaSet, Long> getDao() {
		return pickupAreaSetDao;
	}
	
	
}
