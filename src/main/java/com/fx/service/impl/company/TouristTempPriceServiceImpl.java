package com.fx.service.impl.company;

import java.util.Date;
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
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.TouristTempPriceDao;
import com.fx.entity.company.TouristDayPrice;
import com.fx.entity.company.TouristTempPrice;
import com.fx.service.company.TouristDayPriceService;
import com.fx.service.company.TouristTempPriceService;

@Service
@Transactional
public class TouristTempPriceServiceImpl extends BaseServiceImpl<TouristTempPrice,Long> implements TouristTempPriceService {
	private Logger log = LogManager.getLogger(this.getClass());//日志记录
	@Autowired
	private TouristTempPriceDao ttpDao;
	@Autowired
	private TouristDayPriceService tdpSer;
	@Override
	public ZBaseDaoImpl<TouristTempPrice, Long> getDao() {
		return ttpDao;
	}
	@Override
	public Page<TouristTempPrice> findTouristTempPriceList(
			Page<TouristTempPrice> pageData, String unitNum, String find,
			String sTime, String eTime) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, Object> addTempPirice(ReqSrc reqsrc,String tcId,String uname,String carSeats,String expireTime,String tempPrice){
		String logtxt = U.log(log, "后台添加临时价格设置", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isBlank(expireTime)){
						fg = U.setPutFalse(map, "请选择一个截止时间");
					}
				}
				if(fg){
					if(StringUtils.isBlank(tempPrice)){
						fg = U.setPutFalse(map, "请至少填写一个价格");
					}
				}
				if(fg){
					String hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and dayKm=? and carSeats=?";
					Date sTime=DateUtils.getStartTimeOfDay();
			 		long betweenDay=DateUtils.getDaysOfTowDiffDate(sTime, DateUtils.strToDate(expireTime))+1;
			 		String [] tempPriceCount=tempPrice.split(",");
			 		String [] timePrice=null;
			 		String [] price=null;
			 		if(betweenDay>1){//区间大于1天，要依次加入每天的记录
						Date tempeTime=null;
						for (int i = 1; i <= betweenDay; i++) {
							tempeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, DateUtils.DateToStr(DateUtils.yyyy_MM_dd, sTime)+" 23:59:59");
							for (int j = 0; j < tempPriceCount.length; j++) {
								timePrice=tempPriceCount[j].split("/");
								price=timePrice[1].split("=");
								TouristTempPrice ttp = ttpDao.findObj(hql, tcId,uname,sTime,tempeTime,
										Double.valueOf(timePrice[0]),Integer.parseInt(carSeats));
								if(ttp==null){//没有才新建
									ttp = new TouristTempPrice();
									ttp.setToucId(tcId);
									ttp.setUname(uname);
									ttp.setCarSeats(Integer.parseInt(carSeats));
									ttp.setDayKm(Double.valueOf(timePrice[0]));
									ttp.setStartTime(sTime);
									ttp.setEndTime(tempeTime);
								}
								ttp.setDayPrice(Double.valueOf(price[DateUtils.getDayOfWeek(sTime)-1]));
								ttp.setAddTime(new Date());
								ttpDao.save(ttp);
							}
							sTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd, DateUtils.getPlusDays(DateUtils.yyyy_MM_dd, sTime, 1));
						}
					}else{
						for (int j = 0; j < tempPriceCount.length; j++) {
							timePrice=tempPriceCount[j].split("/");
							price=timePrice[1].split("=");
							TouristTempPrice ttp = ttpDao.findObj(hql, tcId,uname,sTime,DateUtils.std_et(expireTime),
									Double.valueOf(timePrice[0]),Integer.parseInt(carSeats));
							if(ttp==null){
								ttp = new TouristTempPrice();
								ttp.setToucId(tcId);
								ttp.setUname(uname);
								ttp.setCarSeats(Integer.parseInt(carSeats));
								ttp.setDayKm(Double.valueOf(timePrice[0]));
								ttp.setStartTime(sTime);
								ttp.setEndTime(DateUtils.std_et(expireTime));
							}
							ttp.setDayPrice(Double.valueOf(price[DateUtils.getDayOfWeek(sTime)-1]));
							ttp.setAddTime(new Date());
							ttpDao.save(ttp);
						}
					}
			 		U.setPut(map, 1, "操作成功");
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public Map<String, Object> hasTempById(ReqSrc reqsrc,String uname, String tcId, String seats) {
		String logtxt = U.log(log, "获取已有临时价格设置", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isBlank(tcId)){
						fg = U.setPutFalse(map, "包车价格区域id不能为空");
					}
				}
				if(fg){
					if(StringUtils.isBlank(seats)){
						fg = U.setPutFalse(map, "包车价格区域座位数不能为空");
					}
				}
				if(fg){
					String hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and carSeats=?";
			  		TouristTempPrice ttp = ttpDao.findObj(hql, tcId,uname,DateUtils.getStartTimeOfDay(),
			  				DateUtils.getEndTimeOfDay(),Integer.parseInt(seats));
			  		if(ttp!=null){//已有临时价格设置
			  			map.put("dayPrice", ttp.getDayPrice());
			  		}else{
			  			hql="from TouristDayPrice where toucId=? and uname=? and (startTime>=? and startTime<=?) order by startTime asc";
			  			TouristDayPrice tdp=tdpSer.findObj(hql, tcId,uname,DateUtils.getStartTimeOfDay(),
			  						DateUtils.getEndTimeOfDay(),"LIMIT 1");
			  			if(tdp!=null){
			  				map.put("dayPrice", tdp.getDayPrice());
			  			}else{
			  				map.put("dayPrice", "none");
			  			}
			  		}
			 		U.setPut(map, 1, "操作成功");
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
}
