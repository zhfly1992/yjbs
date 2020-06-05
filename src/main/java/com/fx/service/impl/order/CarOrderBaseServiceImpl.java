package com.fx.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.order.CarOrderBaseDao;
import com.fx.entity.order.CarOrderBase;
import com.fx.service.order.CarOrderBaseService;

@Service
@Transactional
public class CarOrderBaseServiceImpl extends BaseServiceImpl<CarOrderBase, Long> implements CarOrderBaseService {
	
	@Autowired
	private CarOrderBaseDao cobDao;
	@Override
	public ZBaseDaoImpl<CarOrderBase, Long> getDao() {
		return cobDao;
	}
	
}
