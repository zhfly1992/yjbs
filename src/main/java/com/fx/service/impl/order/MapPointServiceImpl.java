package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.MapPointDao;
import com.fx.entity.order.MapPoint;
import com.fx.service.order.MapPointService;

@Service
@Transactional
public class MapPointServiceImpl extends BaseServiceImpl<MapPoint, Long> implements MapPointService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 地图地点-数据源 */
	@Autowired
	private MapPointDao mapPointDao;
	@Override
	public ZBaseDaoImpl<MapPoint, Long> getDao() {
		return mapPointDao;
	}
	
}
