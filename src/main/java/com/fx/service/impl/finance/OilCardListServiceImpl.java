package com.fx.service.impl.finance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.fx.dao.finance.OilCardListDao;
import com.fx.entity.finance.OilCardList;
import com.fx.service.finance.OilCardListService;

@Service
@Transactional
public class OilCardListServiceImpl extends BaseServiceImpl<OilCardList,Long> implements OilCardListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private OilCardListDao oclDao;
	//@Autowired
	//private FileManageService fmSer;   //文件管理
	@Override
	public ZBaseDaoImpl<OilCardList, Long> getDao() {
		return oclDao;
	}
	@Override
	public Page<OilCardList> findOclList(Page<OilCardList> pageData,String teamNo,
			String find,String sTime,String eTime,String oilType,String cName) {
		////////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		filtrations.add(new Filtration(MatchType.EQ, teamNo,"teamNo"));//当前车队
		if(StringUtils.isNotEmpty(find)){
			filtrations.add(new Filtration(MatchType.LIKE, find, "cardNo"));//卡号
		}
		if(StringUtils.isNotEmpty(oilType)){
			filtrations.add(new Filtration(MatchType.EQ, 0, "cardType"));//油卡
			filtrations.add(new Filtration(MatchType.EQ, Integer.parseInt(oilType), "oilType"));//油类型
		}
		if(StringUtils.isNotEmpty(cName)){
			filtrations.add(new Filtration(MatchType.IN, new Object[]{0,2}, "cardType"));//油卡,etc
			filtrations.add(new Filtration(MatchType.LIKE, cName, "cName"));//使用人
		}
		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = oclDao.findPage(pageData);
		return pageData;
	}
	@Override
	public int operOcl(String updId, OilCardList ocl){
		try {
			if(ocl != null){
				if(StringUtils.isNotEmpty(updId)){
					oclDao.update(ocl);
				}else{
					oclDao.save(ocl);
				}
			}else{
				OilCardList delocl = oclDao.find(Long.parseLong(updId));
				//fmSer.delFile(Util.MFILEURL, Long.valueOf(delcoi.getOilVoucherId()));//删除凭证图片
				oclDao.delete(delocl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
