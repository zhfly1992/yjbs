package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.RouteStationInfoDao;
import com.fx.entity.order.RouteStationInfo;
import com.fx.service.order.RouteStationInfoService;

@Service
@Transactional
public class RouteStationInfoServiceImpl extends BaseServiceImpl<RouteStationInfo, Long> implements RouteStationInfoService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private RouteStationInfoDao routeStationInfoDao;
	@Override
	public ZBaseDaoImpl<RouteStationInfo, Long> getDao() {
		return routeStationInfoDao;
	}
	
}
