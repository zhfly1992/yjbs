package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.PriceStairDao;
import com.fx.entity.company.PriceStair;
import com.fx.service.company.PriceStairService;

@Service
@Transactional
public class PriceStairServiceImpl extends BaseServiceImpl<PriceStair,Long> implements PriceStairService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private PriceStairDao priceStairDao;
	@Override
	public ZBaseDaoImpl<PriceStair, Long> getDao() {
		return priceStairDao;
	}
	
	
}
