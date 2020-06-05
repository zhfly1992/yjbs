package com.fx.service.impl.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.log.LoginLogDao;
import com.fx.entity.log.LoginLog;
import com.fx.service.log.LoginLogService;

@Service
@Transactional
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, Long> implements LoginLogService {
	
	/** 用户登录日志-数据源 */
	@Autowired
	private LoginLogDao tbLoginLogDao;
	@Override
	public ZBaseDaoImpl<LoginLog, Long> getDao() {
		return tbLoginLogDao;
	}
	
	
	
}
