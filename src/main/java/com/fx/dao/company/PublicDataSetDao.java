package com.fx.dao.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.entity.company.PublicDataSet;

@Repository
public class PublicDataSetDao extends ZBaseDaoImpl<PublicDataSet, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	

}
