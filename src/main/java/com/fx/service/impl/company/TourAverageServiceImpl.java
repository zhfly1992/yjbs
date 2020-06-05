package com.fx.service.impl.company;
import java.util.Date;
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
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.dao.company.TouristAverageDao;
import com.fx.dao.company.TouristDayPriceDao;
import com.fx.dao.company.TouristTempPriceDao;
import com.fx.entity.company.TouristAverage;
import com.fx.entity.company.TouristCharter;
import com.fx.entity.company.TouristDayPrice;
import com.fx.entity.company.TouristTempPrice;
import com.fx.service.company.TouristAverageService;
import com.fx.service.company.TouristCharterService;

@Service
@Transactional
public class TourAverageServiceImpl extends BaseServiceImpl<TouristAverage,Long> implements TouristAverageService {
	private Logger logger = LogManager.getLogger(this.getClass());//日志记录
	@Autowired
	private TouristAverageDao taDao;
	@Autowired
	private TouristCharterService tcSer;
	@Autowired
	private TouristDayPriceDao tdpDao;
	@Autowired
	private TouristTempPriceDao ttpDao;
	@Override
	public ZBaseDaoImpl<TouristAverage, Long> getDao() {
		return taDao;
	}
	@Override
	public Page<TouristAverage> findTAList(Page<TouristAverage> pageData) {
		////////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		////////////////////////查询条件-e//////////////////////////
		pageData = taDao.findPage(pageData);
		return pageData;
	}
	@Override
	public int operTA(String updId, TouristAverage ta) {
		try {
			if(ta != null){
				if(StringUtils.isNotEmpty(updId)){
					taDao.update(ta);
				}else{
					taDao.save(ta);
				}
			}else{
				TouristAverage delta = taDao.find(Long.parseLong(updId));
				taDao.delete(delta);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	@Override
	public int operToucAverage(int state,String areaName,int carSeats,double averageOil,double averageFreeKmPrice) {
		try {
			String hql="from TouristCharter where uname=? and areaName like ? and carSeats=?";
			TouristCharter tc=tcSer.findObj(hql,"18982208376","%"+areaName+"%",carSeats);
			if(tc!=null){
				double maxOil=0,minOil=0;//浮动最低油耗-最高油耗
				double maxKmPrice=0,minKmPrice=0;//浮动最低超范围价格-最高超范围价
				maxOil=MathUtils.add(tc.getOilWear(), MathUtils.mul(tc.getOilWear(), 0.2, 2), 2);
				minOil=MathUtils.sub(tc.getOilWear(), MathUtils.mul(tc.getOilWear(), 0.2, 2), 2);
				maxKmPrice=MathUtils.add(tc.getMoreFreeKmPrice(), MathUtils.mul(tc.getMoreFreeKmPrice(), 0.2, 2), 2);
				minKmPrice=MathUtils.sub(tc.getMoreFreeKmPrice(), MathUtils.mul(tc.getMoreFreeKmPrice(), 0.2, 2), 2);
				if(state==1){//添加
					if((averageOil>=minOil && averageOil<=maxOil && averageOil!=0) ||
							averageFreeKmPrice>=minKmPrice && averageFreeKmPrice<=maxKmPrice && averageFreeKmPrice!=0){//在浮动范围内
						setAverageOilFreeKm(hql, areaName, carSeats, minOil, maxOil, minKmPrice, maxKmPrice);
					}	
				}else if(state==2){//修改或删除直接全部重新求平均
					setAverageOilFreeKm(hql, areaName, carSeats, minOil, maxOil, minKmPrice, maxKmPrice);
				}
				/*if(averageFreeKm!=0 && ta.getAverageFreeKm()<averageFreeKm){
					ta.setAverageFreeKm(averageFreeKm);
				}*/
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//计算油耗和超范围单价的平均值
	public void setAverageOilFreeKm(String hql,String areaName,int carSeats,double minOil,
			double maxOil,double minKmPrice,double maxKmPrice){
		hql="from TouristCharter where areaName like ? and carSeats=? and oilWear>? and oilWear<=? and " +
				"moreFreeKmPrice>=? and moreFreeKmPrice<=?";
		List<TouristCharter> tclist=tcSer.findhqlList(hql, "%"+areaName+"%",carSeats,minOil,maxOil,minKmPrice,maxKmPrice);
		if(tclist.size()>0){
			double freeKm=tclist.get(0).getFreeKm();//免费公里数
			double averOil=0;//油耗
			double freeKmPrice=0;//超范围单价
			boolean isExit=true;//默认存在
			hql="from TouristAverage where areaName like ? and carSeats=? and startTime is null";
			TouristAverage ta=taDao.findObj(hql,"%"+areaName+"%",carSeats);
			if(ta==null){//不存在就创建平均记录
				isExit=false;
				ta=new TouristAverage();
				ta.setAreaName(areaName);
				ta.setCarSeats(carSeats);
			}
			for (TouristCharter each : tclist) {
				averOil+=each.getOilWear();
				if(each.getFreeKm()<freeKm){
					freeKm=each.getFreeKm();
				}
				freeKmPrice+=each.getMoreFreeKmPrice();
			}
			ta.setAverageFreeKm(freeKm);
			ta.setAverageOil(MathUtils.div(averOil, tclist.size(), 2));
			ta.setAverageFreeKmPrice(MathUtils.div(freeKmPrice, tclist.size(), 2));
			ta.setAddTime(new Date());
			if(isExit){
				taDao.update(ta);
			}else{
				taDao.save(ta);
			}
		}
	}
	@Override
	public int operTcprAverage(int state,String tcId, Date sTime,
		Date eTime, String averagePrice, double averageFeastRatio) {
		try {
			TouristCharter tc=tcSer.find(Long.valueOf(tcId));
			if(!tc.getUname().equals("18982208376")){//非后台账号设置
				String hql="from TouristCharter where uname=? and areaName like ? and carSeats=?";
				tc=tcSer.findObj(hql,"18982208376","%"+tc.getAreaName()+"%",tc.getCarSeats());	
			}
			/*String hql="SELECT dayKm FROM TouristCharterPrice WHERE " +
					"toucId=? and dayKm>=? order by dayKm asc)";
			Object objKilo=tcpDao.findObj(hql,tc.getId()+"",dayKilo,"LIMIT 1");//查询最接近行程公里的值
			if(objKilo!=null){
				//根据城市，座位数，公里数，时间段查询出符合条件的记录准备求出平均价
				String hql="from TouristDayPrice where toucId=? and dayKm=? and (startTime>=? and endTime<=?)";
				List<TouristDayPrice> tdprlist=tdpDao.findhqlList(hql, tc.getId()+"",(Double)objKilo,
						sTime,eTime);*/
				String hql="from TouristDayPrice where toucId=? and (startTime>=? and endTime<=?)";
				List<TouristDayPrice> tdprlist=tdpDao.findhqlList(hql, tc.getId()+"",sTime,eTime);
				if(tdprlist.size()>0){
					String [] dayPrice=averagePrice.split("=");
					double maxDayPrice=0,minDayPrice=0;//浮动每日价格-最高每日价
					double maxFeRatio=0,minFeRatio=0;//节假日上浮比例范围
					double currPrice=0;
					List<TouristDayPrice> averlist=null;
					TouristAverage ta=null;
					for (TouristDayPrice each : tdprlist) {
						currPrice=Double.valueOf(dayPrice[DateUtils.getDayOfWeek(each.getStartTime())-1]);
						maxDayPrice=MathUtils.add(each.getDayPrice(), MathUtils.mul(each.getDayPrice(), 0.2, 2), 2);
						minDayPrice=MathUtils.sub(each.getDayPrice(), MathUtils.mul(each.getDayPrice(), 0.2, 2), 2);
						maxFeRatio=MathUtils.add(each.getFeastRatio(), MathUtils.mul(each.getFeastRatio(), 0.2, 2), 2);
						minFeRatio=MathUtils.sub(each.getFeastRatio(), MathUtils.mul(each.getFeastRatio(), 0.2, 2), 2);
						if(state==1){//添加
							if(currPrice>=minDayPrice && currPrice<=maxDayPrice && currPrice!=0  ||
									averageFeastRatio>=minFeRatio && averageFeastRatio<=maxFeRatio && averageFeastRatio!=0){
								hql="from TouristDayPrice where areaName like ? and carSeats=? and (startTime>=? and endTime<=?)" +
										" and dayKm=? and feastRatio>=? and feastRatio<=? and dayPrice>=? and dayPrice<=?";
								averlist=tdpDao.findhqlList(hql,"%"+tc.getAreaName()+"%",tc.getCarSeats(),each.getStartTime(),
										each.getEndTime(),each.getDayKm(),minFeRatio,maxFeRatio,minDayPrice,maxDayPrice);
								if(averlist.size()>0){
									setAveragePriceFeast(hql, tc.getAreaName(), tc.getCarSeats(), 
											each.getDayKm(), each.getStartTime(), each.getEndTime(), ta, averlist);
								}
							}
						}else{//修改或删除直接全部重新求平均
							hql="from TouristDayPrice where areaName like ? and carSeats=? and (startTime>=? and endTime<=?)" +
									" and dayKm=? and feastRatio>=? and feastRatio<=? and dayPrice>=? and dayPrice<=?";
							averlist=tdpDao.findhqlList(hql,"%"+tc.getAreaName()+"%",tc.getCarSeats(),each.getStartTime(),
									each.getEndTime(),each.getDayKm(),minFeRatio,maxFeRatio,minDayPrice,maxDayPrice);
							if(averlist.size()>0){
								setAveragePriceFeast(hql, tc.getAreaName(), tc.getCarSeats(), 
										each.getDayKm(), each.getStartTime(), each.getEndTime(), ta, averlist);
							}
						}
					}
					return 1;
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//计算每日单价和浮动比例的平均值
	public void setAveragePriceFeast(String hql,String areaName,int carSeats,double dayKilo,
			Date startTime,Date endTime,TouristAverage ta,List<TouristDayPrice> averlist){
		double averPrice=0;//均价
		double feastRatio=0;//节假日浮动比例
		boolean isExit=true;//默认存在
		hql="from TouristAverage where areaName like ? and carSeats=? and dayKm=? and (startTime>=? and endTime<=?)";
		ta=taDao.findObj(hql,"%"+areaName+"%",carSeats,dayKilo,startTime,endTime);
		if(ta==null){//不存在就创建平均记录
			isExit=false;
			ta=new TouristAverage();
			ta.setAreaName(areaName);
			ta.setCarSeats(carSeats);
			ta.setDayKm(dayKilo);
			ta.setStartTime(startTime);
			ta.setEndTime(endTime);
		}
		for (TouristDayPrice each : averlist) {
			averPrice+=each.getDayPrice();
			feastRatio+=each.getFeastRatio();
		}
		ta.setAveragePrice(MathUtils.div(averPrice, averlist.size(), 2));
		ta.setAverageFeastRatio(MathUtils.div(feastRatio, averlist.size(), 2));
		ta.setAddTime(new Date());
		if(isExit){
			taDao.update(ta);
		}else{
			taDao.save(ta);
		}
		/*ta=taDao.findObj(hql,"%"+tc.getAreaName()+"%",tc.getCarSeats(),dayKilo,each.getStartTime(),each.getEndTime());
		if(ta==null){//不存在就创建平均记录
			ta=new TouristAverage();
			ta.setAreaName(each.getAreaName());
			ta.setCarSeats(each.getCarSeats());
			ta.setDayKm(each.getDayKm());
			ta.setStartTime(each.getStartTime());
			ta.setEndTime(each.getEndTime());
		}
		for (TouristCharterPrice eachs : averlist) {
			averPrice+=eachs.getDayPrice();
			feastRatio+=eachs.getFeastRatio();
		}
		ta.setAveragePrice(MathUtils.div(averPrice, averlist.size(), 2));
		ta.setAverageFeastRatio(MathUtils.div(feastRatio, averlist.size(), 2));
		taDao.update(ta);*/
	}
	@Override
	public List<String> getAverage(String areaName,String uname, double dayKilo,
			Date sTime, Date eTime,List<String> tour) {
		logger.info("平均设置参数>"+areaName+"-"+dayKilo+"-"+sTime+"-"+eTime);
		String hql="from TouristCharter where uname=? and areaName like ?";
		List<TouristCharter> tclist=tcSer.findhqlList(hql,"18982208376","%"+areaName+"%");//师傅的价格设置
		if(tclist.size()>0){
			//double objKilo=0;//每日公里数
			double averPrice=0;//均价
			double feastRatio=0;//节假日浮动比例
			String count="";
			TouristAverage tatouc=null;
			List<TouristAverage> talist=null;
			List<TouristTempPrice> ttpList=null;
			for (TouristCharter each : tclist) {
				hql="from TouristAverage where areaName like ? and carSeats=? and startTime is null";
				tatouc=taDao.findObj(hql,"%"+areaName+"%",each.getCarSeats());
				if(tatouc!=null){
					/*objKilo=tcSer.closeToDayKilo(hql,each.getId()+"",dayKilo);
					logger.info("平均设置参数每日公里数>"+objKilo);*/
					if(StringUtils.isNotBlank(uname)){
						//先查询是否有临时价格设置如果有直接使用
						/*hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and dayKm=? and carSeats=?";
						ttpList=ttpDao.findhqlList(hql, each.getId()+"",uname,sTime,eTime,objKilo,each.getCarSeats());*/
						hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and carSeats=?";
						ttpList=ttpDao.findhqlList(hql, each.getId()+"",uname,sTime,eTime,each.getCarSeats());
						if(ttpList.size()>0){
							averPrice = 0;
							for (TouristTempPrice eachttp : ttpList) {
								averPrice+=eachttp.getDayPrice();
							}
							//价格-油耗-免费公里数-超范围公里数平均单价-节假日浮动比例-座位数-配置-型号-类型-每日最低价
							count=MathUtils.div(averPrice, ttpList.size(), 2)+"-"+tatouc.getAverageOil()+"-"+
							tatouc.getAverageFreeKm()+"-"+tatouc.getAverageFreeKmPrice()+"-"+MathUtils.div(feastRatio, ttpList.size(), 2)+
							"-"+each.getCarSeats()+"-"+each.getCarConfig()+"-"+each.getCarModel()+"-"+each.getCarType()+"-"+each.getDayLowPrice();
							logger.info("平均设置参数拼接完成>"+count);
							tour.add(count);
						}
					}
					if(ttpList==null || ttpList.size()==0){
						/*hql="from TouristAverage where areaName like ? and carSeats=? and dayKm=? and startTime>=? and endTime<=?";
						talist=taDao.findhqlList(hql,"%"+areaName+"%",each.getCarSeats(),objKilo,sTime,eTime);*/
						hql="from TouristAverage where areaName like ? and carSeats=? and startTime>=? and endTime<=?";
						talist=taDao.findhqlList(hql,"%"+areaName+"%",each.getCarSeats(),sTime,eTime);
						if(talist.size()>0){
							averPrice = 0;feastRatio = 0;
							for (TouristAverage eachta : talist) {
								averPrice+=eachta.getAveragePrice();
								feastRatio+=eachta.getAverageFeastRatio();
							}
							//价格-油耗-免费公里数-超范围公里数平均单价-节假日浮动比例-座位数-配置-型号-类型-每日最低价
							count=MathUtils.div(averPrice, talist.size(), 2)+"-"+tatouc.getAverageOil()+"-"+
							tatouc.getAverageFreeKm()+"-"+tatouc.getAverageFreeKmPrice()+"-"+MathUtils.div(feastRatio, talist.size(), 2)+
							"-"+each.getCarSeats()+"-"+each.getCarConfig()+"-"+each.getCarModel()+"-"+each.getCarType()+"-"+each.getDayLowPrice();
							logger.info("平均设置参数拼接完成>"+count);
							tour.add(count);
						}
					}
				}
			}
		}
		return tour;
	}
}
