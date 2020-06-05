package com.fx.dao.cus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.entity.cus.CompanyUser;

@Repository
public class CompanyUserDao extends ZBaseDaoImpl<CompanyUser, Long> {

	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	
}
