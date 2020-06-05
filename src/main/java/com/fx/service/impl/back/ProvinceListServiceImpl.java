package com.fx.service.impl.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.back.ProvinceListDao;
import com.fx.entity.back.ProvinceList;
import com.fx.service.back.ProvinceListService;

@Service
@Transactional
public class ProvinceListServiceImpl extends BaseServiceImpl<ProvinceList, Long> implements ProvinceListService {

	@Autowired
	private ProvinceListDao provinceListDao;
	
	@Override
	public ZBaseDaoImpl<ProvinceList, Long> getDao() {
		return provinceListDao;
	}
    
}
