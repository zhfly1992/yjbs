package com.fx.service.impl.company;

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
import com.fx.dao.company.TouristRatioDao;
import com.fx.entity.company.TouristRatio;
import com.fx.service.company.TouristRatioService;

@Service
@Transactional
public class TouristRatioServiceImpl extends BaseServiceImpl<TouristRatio,Long> implements TouristRatioService {
	@Autowired
	private TouristRatioDao trDao;
	@Override
	public ZBaseDaoImpl<TouristRatio, Long> getDao() {
		return trDao;
	}
	@Override
	public Page<TouristRatio> findRatioList(Page<TouristRatio> pageData, String find,String sTime, String eTime) {
		////////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		if(StringUtils.isNotEmpty(find)){
			filtrations.add(new Filtration(MatchType.EQ, Double.valueOf(find), "kilometre"));//公里数
		}
		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = trDao.findPage(pageData);
		return pageData;
	}
	@Override
	public int operTr(String trId, TouristRatio ratio) {
		try {
			if(ratio != null){
				if(StringUtils.isNotEmpty(trId)){
					trDao.update(ratio);
				}else{
					trDao.save(ratio);
				}
			}else{
				TouristRatio delTr = trDao.find(Long.parseLong(trId));
				trDao.delete(delTr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
