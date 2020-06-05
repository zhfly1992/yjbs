package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.RouteMapPointDao;
import com.fx.entity.order.RouteMapPoint;
import com.fx.service.order.RouteMapPointService;

@Service
@Transactional
public class RouteMapPointServiceImpl extends BaseServiceImpl<RouteMapPoint, Long> implements RouteMapPointService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private RouteMapPointDao routeMapPointDao;
	@Override
	public ZBaseDaoImpl<RouteMapPoint, Long> getDao() {
		return routeMapPointDao;
	}
	
}
