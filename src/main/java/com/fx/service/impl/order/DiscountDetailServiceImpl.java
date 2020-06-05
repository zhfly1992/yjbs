package com.fx.service.impl.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.DiscountDetailDao;
import com.fx.entity.order.DiscountDetail;
import com.fx.service.order.DiscountDetailService;

@Service
@Transactional
public class DiscountDetailServiceImpl extends BaseServiceImpl<DiscountDetail, Long> implements DiscountDetailService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private DiscountDetailDao discountDetailDao;
	@Override
	public ZBaseDaoImpl<DiscountDetail, Long> getDao() {
		return discountDetailDao;
	}
	
	
}
