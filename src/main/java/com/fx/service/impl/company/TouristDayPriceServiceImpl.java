package com.fx.service.impl.company;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.dao.company.TouristDayPriceDao;
import com.fx.entity.company.TouristDayPrice;
import com.fx.service.company.TouristDayPriceService;

@Service
@Transactional
public class TouristDayPriceServiceImpl extends BaseServiceImpl<TouristDayPrice,Long> implements TouristDayPriceService {
	@Autowired
	private TouristDayPriceDao tdpDao;
	@Override
	public ZBaseDaoImpl<TouristDayPrice, Long> getDao() {
		return tdpDao;
	}
	@Override
	public int addTdPirice(String tcId,String uname, String areaName, int carSeats,
			String dayKm, Date sTime, Date eTime, String dayPrice,double feastRatio) {
		try {
			long betweenDay=DateUtils.getDaysOfTowDiffDate(sTime, eTime)+1;
			String [] price=dayPrice.split("=");
			if(betweenDay>1){//区间大于1天，要依次加入每天的记录
				Date tempeTime=null;
				for (int i = 1; i <= betweenDay; i++) {
					tempeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, DateUtils.DateToStr(DateUtils.yyyy_MM_dd, sTime)+" 23:59:59");
					TouristDayPrice tdp = new TouristDayPrice();
					tdp.setToucId(tcId);
					tdp.setUname(uname);
					tdp.setAreaName(areaName);
					tdp.setCarSeats(carSeats);
					if(StringUtils.isNotBlank(dayKm))tdp.setDayKm(Double.valueOf(dayKm));
					tdp.setDayPrice(Double.valueOf(price[DateUtils.getDayOfWeek(sTime)-1]));
					tdp.setFeastRatio(feastRatio);
					tdp.setStartTime(sTime);
					tdp.setEndTime(tempeTime);
					tdp.setAddTime(new Date());
					tdpDao.save(tdp);
					sTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd, DateUtils.getPlusDays(DateUtils.yyyy_MM_dd, sTime, 1));
				}
			}else{
				TouristDayPrice tdp = new TouristDayPrice();
				tdp.setToucId(tcId);
				tdp.setUname(uname);
				tdp.setAreaName(areaName);
				tdp.setCarSeats(carSeats);
				if(StringUtils.isNotBlank(dayKm))tdp.setDayKm(Double.valueOf(dayKm));
				tdp.setDayPrice(Double.valueOf(price[DateUtils.getDayOfWeek(sTime)-1]));
				tdp.setFeastRatio(feastRatio);
				tdp.setStartTime(sTime);
				tdp.setEndTime(eTime);
				tdp.setAddTime(new Date());
				tdpDao.save(tdp);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int delTdPirice(String tcId,double dayKm, Date sTime, Date eTime) {
		try {
			String hql="from TouristDayPrice where toucId=? and dayKm=? and (startTime>=? and endTime<=?)";
			List<TouristDayPrice> tdprlist=tdpDao.findhqlList(hql, tcId,dayKm,sTime,eTime);
			if(tdprlist.size()>0){
				for (TouristDayPrice each:tdprlist) {
					tdpDao.delete(each);
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
