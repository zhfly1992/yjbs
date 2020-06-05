package com.fx.service.impl.company;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.TouristCharterDao;
import com.fx.entity.company.TouristCharter;
import com.fx.service.company.TouristAverageService;
import com.fx.service.company.TouristCharterService;

@Service
@Transactional
public class TouristCharterServiceImpl extends BaseServiceImpl<TouristCharter,Long> implements TouristCharterService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private TouristCharterDao tcDao;
	@Autowired
	private TouristAverageService taSer;
	@Override
	public ZBaseDaoImpl<TouristCharter, Long> getDao() {
		return tcDao;
	}
	@Override
	public Map<String, Object> findTcList(ReqSrc reqsrc,String page, String rows,String find,
			String carSeats,String areaType,String sTime,String eTime,String areaName) {
		String logtxt = U.log(log, "获取-包车价格区域-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "包车价格区域");
			/*****参数--验证--end******/
			if(fg){
				Page<TouristCharter> pd = tcDao.findTcList(reqsrc, page, rows, find, carSeats, areaType, sTime, eTime, areaName);
				U.setPageData(map, pd);
				
				U.setPut(map, 0, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public int operTc(String tcId, TouristCharter tc) {  
		try {
			if(tc != null){
				if(StringUtils.isNotEmpty(tcId)){
					tcDao.update(tc);
				}else{
					tcDao.save(tc);
				}
				taSer.operToucAverage(1,tc.getAreaName(),tc.getCarSeats(),tc.getOilWear(),tc.getMoreFreeKmPrice());//计算平均值
			}else{
				TouristCharter del = tcDao.find(Long.parseLong(tcId));
				String areaName=del.getAreaName();int carSeats= del.getCarSeats();
				tcDao.delete(del);
				taSer.operToucAverage(2,areaName, carSeats,0,0);//计算平均值
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	@Override
	public double closeToDayKilo(String hql,String tcId,double dayKilo) {
		hql="SELECT dayKm FROM TouristCharterPrice WHERE id=(SELECT MIN(id) FROM TouristCharterPrice WHERE " +
				"toucId=? and dayKm>=? order by dayKm asc)";
		Object objKilo=tcDao.findObj(hql,tcId,dayKilo);//查询最接近行程公里的值
		if(objKilo!=null){
			return (Double)objKilo;
		}
		return 0;
	}
}
