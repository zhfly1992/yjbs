package com.fx.service.impl.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.company.OperatorListDao;
import com.fx.service.company.OperatorList;
import com.fx.service.company.OperatorListService;

@Service
@Transactional
public class OperatorListServiceImpl extends BaseServiceImpl<OperatorList,Long> implements OperatorListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private OperatorListDao operatorListDao;
	@Override
	public ZBaseDaoImpl<OperatorList, Long> getDao() {
		return operatorListDao;
	}
	
	
}
