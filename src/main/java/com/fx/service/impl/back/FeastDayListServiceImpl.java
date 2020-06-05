package com.fx.service.impl.back;

import java.util.ArrayList;
import java.util.Date;
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
import com.fx.dao.back.FeastDayListDao;
import com.fx.entity.back.FeastDayList;
import com.fx.service.back.FeastDayListService;

@Service
@Transactional
public class FeastDayListServiceImpl extends BaseServiceImpl<FeastDayList, Long> implements FeastDayListService {
	@Autowired
	private FeastDayListDao feastDayDao;

	@Override
	public ZBaseDaoImpl<FeastDayList, Long> getDao() {
		return feastDayDao;
	}
	
	@Override
	public Page<FeastDayList> getFeastDayList(Page<FeastDayList> pageData,
			String find) {
		//给pageData设置参数
  		Compositor compositor = new Compositor("fTime", CompositorType.ASC);
  		pageData.setCompositor(compositor);
  		List<Filtration> filtrations = new ArrayList<Filtration>();
  		/////////////////查询条件/////-s/////////////
  		if(StringUtils.isNotEmpty(find)){
  			filtrations.add(new Filtration(MatchType.LIKE, find, "fName"));
  		}
  		/////////////////查询条件/////-e//////////////
  		pageData.setFiltrations(filtrations);
  		pageData = feastDayDao.findPage(pageData);
  		return pageData;
	}
	
	@Override
	public int feastDayAdup(FeastDayList feastDay, String fName, Date fTime,
			String fNote) {
		try {
			if(StringUtils.isEmpty(fName) && fTime == null){//删除
				if(feastDay != null) feastDayDao.delete(feastDay);
			}else{//添加/修改
				feastDay.setfName(fName);
				feastDay.setfTime(fTime);
				feastDay.setfNote(fNote);
				if(feastDay.getId() == 0){//添加对象
					feastDayDao.save(feastDay);
				}else{//修改对象
					feastDayDao.update(feastDay);
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
