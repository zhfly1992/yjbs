package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.RouteLineInfoDao;
import com.fx.entity.order.RouteLineInfo;
import com.fx.service.order.RouteLineInfoService;

@Service
@Transactional
public class RouteLineInfoServiceImpl extends BaseServiceImpl<RouteLineInfo, Long> implements RouteLineInfoService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private RouteLineInfoDao routeLineInfoDao;
	@Override
	public ZBaseDaoImpl<RouteLineInfo, Long> getDao() {
		return routeLineInfoDao;
	}
	
}
