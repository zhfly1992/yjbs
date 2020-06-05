package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.StationListDao;
import com.fx.entity.order.StationList;
import com.fx.service.order.StationListService;

@Service
@Transactional
public class StationListServiceImpl extends BaseServiceImpl<StationList, Long> implements StationListService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 地图地点-数据源 */
	@Autowired
	private StationListDao stationListDao;
	@Override
	public ZBaseDaoImpl<StationList, Long> getDao() {
		return stationListDao;
	}
	
}
