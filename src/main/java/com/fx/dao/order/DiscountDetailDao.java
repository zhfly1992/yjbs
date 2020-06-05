package com.fx.dao.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.entity.order.DiscountDetail;

@Repository
public class DiscountDetailDao extends ZBaseDaoImpl<DiscountDetail, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	
}
