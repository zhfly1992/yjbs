package com.fx.service.impl.back;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.dao.back.SpecialRouteDao;
import com.fx.entity.back.SpecialRoute;
import com.fx.service.back.SpecialRouteService;

@Service
@Transactional
public class SpecialRouteServiceImpl extends BaseServiceImpl<SpecialRoute,Long> implements SpecialRouteService {
	@Autowired
	private SpecialRouteDao srDao;
	@Override
	public ZBaseDaoImpl<SpecialRoute, Long> getDao() {
		return srDao;
	}
	@Override
	public Page<SpecialRoute> findRouteList(Page<SpecialRoute> pageData, String startArea,String sTime, String eTime) {
		////////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		if(StringUtils.isNotEmpty(startArea)){
			filtrations.add(new Filtration(MatchType.LIKE, startArea, "startArea"));//起点
		}
		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = srDao.findPage(pageData);
		return pageData;
	}
	@Override
	public int operSr(String srId, SpecialRoute sr) {
		try {
			if(sr != null){
				if(StringUtils.isNotEmpty(srId)){
					srDao.update(sr);
				}else{
					srDao.save(sr);
				}
			}else{
				SpecialRoute delSr = srDao.find(Long.parseLong(srId));
				srDao.delete(delSr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
