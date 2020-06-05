package com.fx.dao.log;

import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.entity.log.LoginLog;

@Repository
public class LoginLogDao extends ZBaseDaoImpl<LoginLog, Long> {
	
}
