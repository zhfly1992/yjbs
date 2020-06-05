package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.DisCarInfoDao;
import com.fx.entity.order.DisCarInfo;
import com.fx.service.order.DisCarInfoService;

@Service
@Transactional
public class DisCarInfoServiceImpl extends BaseServiceImpl<DisCarInfo, Long> implements DisCarInfoService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private DisCarInfoDao disCarInfoDao;
	@Override
	public ZBaseDaoImpl<DisCarInfo, Long> getDao() {
		return disCarInfoDao;
	}
	
}
