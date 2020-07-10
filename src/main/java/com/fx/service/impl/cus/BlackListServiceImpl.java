package com.fx.service.impl.cus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.BlackListDao;
import com.fx.entity.cus.BlackList;
import com.fx.service.cus.BlackListService;

@Service
@Transactional
public class BlackListServiceImpl extends BaseServiceImpl<BlackList, Long> implements BlackListService {

	@Autowired
	private BlackListDao blackListDao;
	@Override
	public ZBaseDaoImpl<BlackList, Long> getDao() {
		return blackListDao;
	}
	
}
