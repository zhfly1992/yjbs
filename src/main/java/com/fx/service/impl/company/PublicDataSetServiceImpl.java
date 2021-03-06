package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.PublicDataSetDao;
import com.fx.entity.company.PublicDataSet;
import com.fx.service.company.PublicDataSetService;

@Service
@Transactional
public class PublicDataSetServiceImpl extends BaseServiceImpl<PublicDataSet,Long> implements PublicDataSetService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private PublicDataSetDao publicDataSetDao;
	@Override
	public ZBaseDaoImpl<PublicDataSet, Long> getDao() {
		return publicDataSetDao;
	}
	
	
}
