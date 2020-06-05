package com.fx.service.impl.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.fx.commons.utils.other.HttpReqMeth;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.CityListDao;
import com.fx.dao.back.FeastDayListDao;
import com.fx.dao.back.SpecialRouteDao;
import com.fx.dao.company.TouristCharterPriceDao;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.entity.back.CityList;
import com.fx.entity.back.FeastDayList;
import com.fx.entity.back.SpecialRoute;
import com.fx.entity.company.TouristCharter;
import com.fx.entity.company.TouristCharterPrice;
import com.fx.entity.company.TouristDayPrice;
import com.fx.entity.company.TouristRatio;
import com.fx.entity.company.TouristTempPrice;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.order.BcCarPrice;
import com.fx.entity.order.BcOrderParam;
import com.fx.service.company.TouristAverageService;
import com.fx.service.company.TouristCharterPriceService;
import com.fx.service.company.TouristCharterService;
import com.fx.service.company.TouristDayPriceService;
import com.fx.service.company.TouristRatioService;
import com.fx.service.company.TouristTempPriceService;
import com.fx.service.order.BcOrderParamService;

@Service
@Transactional
public class TouristCharterPriceServiceImpl extends BaseServiceImpl<TouristCharterPrice,Long> implements TouristCharterPriceService {
	private Logger logger = LogManager.getLogger(this.getClass());//日志记录
	
	@Autowired
	private TouristCharterPriceDao toupDao;
	@Override
	public ZBaseDaoImpl<TouristCharterPrice, Long> getDao() {
		return toupDao;
	}
	@Autowired
	private TouristDayPriceService tdpSer;
	@Autowired
	private TouristAverageService taSer;
	@Autowired
	private CompanyUserDao cuDao;
	@Autowired
	private TouristCharterService tcSer;
	@Autowired
	private TouristTempPriceService ttpSer;
	@Autowired
	private FeastDayListDao fdlDao;
	@Autowired
	private SpecialRouteDao srDao;
	@Autowired
	private CityListDao citylistDao;
	@Autowired
	private TouristRatioService trSer;
	@Autowired
	private BcOrderParamService bopSer;
	
	@Override
	public Map<String, Object> findTcpList(ReqSrc reqsrc,String page, String rows,String find,String sTime,String eTime){
		String logtxt = U.log(logger, "获取-包车价格阶梯-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "包车价格阶梯");
			/*****参数--验证--end******/
			if(fg){
				Page<TouristCharterPrice> pd = toupDao.findTcpList(reqsrc, page, rows, find, sTime, eTime);
				U.setPageData(map, pd);
				
				U.setPut(map, 0, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, logger, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	public double getDiscount(String uname,Date sTime, Date eTime,String areaName, int carSeats, double dayKilo){
		int hour=(int)DateUtils.getHoursOfTowDiffDate(sTime, eTime);//间隔小时数
		double discount=0;
		if(hour<=7){//7小时内才计算折扣
			if(StringUtils.isEmpty(uname)) uname="18982208376";
			String hql="from TouristCharterPrice where id=(SELECT MIN(id) FROM TouristCharterPrice WHERE " +
					"uname=? and areaName like ? and carSeats=? and dayKm>=? and (startTime<=? and endTime>=?) order by dayKm asc)";
			TouristCharterPrice tcpr=toupDao.findObj(hql,uname,"%"+areaName+"%",carSeats,dayKilo,sTime,eTime);
			if(tcpr!=null){
				if(hour<=4){
					discount=tcpr.getFourHourRebate();
				}else if(hour<=5){
					discount=tcpr.getFiveHourRebate();
				}else if(hour<=6){
					discount=tcpr.getSixHourRebate();
				}else if(hour<=7){
					discount=tcpr.getSevenHourRebate();
				}
			}
		}
		return discount;
	}
	
	@Override
	public Map<String, Object> findTour(String uname, String areaName, String endArea,String sTime, String eTime,
		 String km, String waypointCitys, String unitNum,String lon,String lat,String tolls,String mainOrderNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("查询合适的旅游包车价格设置>>>>>>>>>>>>>>>>>>>>>");
		try {
			List<BcCarPrice> bcpList=new ArrayList<BcCarPrice>();
			String hql="from BcOrderParam where mainOrderNum=? order by id asc";
			List<BcOrderParam> bop=bopSer.findhqlList(hql, mainOrderNum);
			int isFL=0;//是否是第1程或最后一程
			for (int i=0;i<bop.size();i++) {
				unitNum=bop.get(i).getCompanyNum();
				areaName=bop.get(i).getPuCounty().split("-")[0];
				sTime=DateUtils.DateToStr(DateUtils.yyyy_MM_dd_HH_mm_ss, bop.get(i).getStime());
				eTime=DateUtils.DateToStr(DateUtils.yyyy_MM_dd_HH_mm_ss, bop.get(i).getEtime());
				waypointCitys=bop.get(i).getWayCity().replaceAll("=", ",");
				lon=bop.get(i).getSpoint().split("=")[1];
				lat=bop.get(i).getEpoint().split("=")[1];
				tolls=bop.get(i).getTolls()+"";
				km=bop.get(i).getDistance()+"";
				if(i==0){//第1程
					isFL=1;
				} 
				if(i==bop.size()-1){//最后一程
					isFL=2;
				}
				bcpList=getAvgPrice(uname, areaName, sTime, eTime, km, waypointCitys, unitNum, 
						lon, lat, tolls, mainOrderNum,bcpList,i+1,isFL);
			}
			if(bcpList.size()>0){
				map.put("price", bcpList);
				U.setPut(map, 1, "匹配价格设置成功");
			}else{
				U.setPut(map, 0, "匹配不到合适的车辆价格设置");
			}
		} catch (Exception e) {
			logger.info(map.get("code")+"-"+map.get("msg")+">>>>>>>>>>>>>>>>>>>>>", e);
			e.printStackTrace();
		}
		return map;
	}
	private List<BcCarPrice> getAvgPrice(String uname, String areaName,String sTime, String eTime,String km, String waypointCitys,
			  String unitNum,String lon,String lat,String tolls,String mainOrderNum,List<BcCarPrice> bcpList,int routeNo,int isFL){
		long betweenDay = DateUtils.getDaysOfTowDiffDate(sTime, eTime); // 间隔天数
		double dayKilo = MathUtils.div(Double.valueOf(km), betweenDay, 2);// 每日公里数
		double invoiceStand=0;//默认开票比例为0
		List<String> tour=new ArrayList<String>();//车辆对应价格列表
		String count ="";//价格-油耗-免费公里数-超范围公里数平均单价-节假日浮动比例-座位数-配置-型号-类型-每日最低价
		String hql = "from TouristCharter where uname = ? and areaName like ?";
		if(StringUtils.isEmpty(uname) && StringUtils.isEmpty(unitNum)) {// 无账号取系统平均值
			tour = taSer.getAverage(areaName,"", dayKilo, DateUtils.std_st(sTime), DateUtils.std_et(eTime), tour);
		}else{
			if(StringUtils.isNotEmpty(unitNum)){
				CompanyUser ctl = cuDao.findByField("unitNum", unitNum);
				uname = ctl.getBaseUserId().getUname();
				invoiceStand=ctl.getInvoicetand();
			}
			List<TouristCharter> tclist = tcSer.findhqlList(hql, uname, "%"+areaName+"%");// 师傅的价格设置
			if(tclist.size()>0){
				//double objKilo=0;//每日公里数
				double averPrice = 0;//均价
				double feastRatio = 0;//节假日浮动比例
				List<TouristDayPrice> tdprlist=null;
				List<TouristTempPrice> ttpList=null;
				for (TouristCharter each : tclist) {
					/*objKilo = tcSer.closeToDayKilo(hql, each.getId()+"", dayKilo);
					logger.info("账号设置参数每日公里数>"+objKilo);*/
					if(StringUtils.isNotBlank(uname)){
						//先查询是否有临时价格设置如果有直接使用
						/*hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and dayKm=? and carSeats=?";
						ttpList=ttpSer.findhqlList(hql, each.getId()+"",uname,sTime,eTime,objKilo,each.getCarSeats());*/
						hql="from TouristTempPrice where toucId=? and uname=? and startTime>=? and endTime<=? and carSeats=?";
						ttpList=ttpSer.findhqlList(hql, each.getId()+"",uname,sTime,eTime,each.getCarSeats());
						if(ttpList.size()>0){
							averPrice = 0;
							for (TouristTempPrice eachttp : ttpList) {
								averPrice+=eachttp.getDayPrice();
							}
							//价格-油耗-免费公里数-超范围公里数平均单价-节假日浮动比例-座位数-配置-型号-类型-每日最低价
							count=MathUtils.div(averPrice, ttpList.size(), 2)+"-"+each.getOilWear()+"-"+
							each.getFreeKm()+"-"+each.getMoreFreeKmPrice()+"-"+MathUtils.div(feastRatio, ttpList.size(), 2)+
							"-"+each.getCarSeats()+"-"+each.getCarConfig()+"-"+each.getCarModel()+"-"+each.getCarType()+"-"+each.getDayLowPrice();
							logger.info("平均设置参数拼接完成>"+count);
							tour.add(count);
						}
					}
					if(ttpList==null || ttpList.size()==0){
						//根据城市，座位数，公里数，时间段查询出符合条件的记录准备求出平均价
						/*hql="from TouristDayPrice where toucId=? and dayKm=? and (startTime>=? and endTime<=?)";
						tdprlist=tdpSer.findhqlList(hql, each.getId()+"", objKilo, DateUtils.std_st(sTime), 
							DateUtils.std_et(eTime));*/	
						hql="from TouristDayPrice where toucId=? and (startTime>=? and endTime<=?)";
						tdprlist=tdpSer.findhqlList(hql, each.getId()+"", DateUtils.std_st(sTime), 
							DateUtils.std_et(eTime));
						if(tdprlist.size() > 0){
							averPrice = 0;feastRatio = 0;
							for (TouristDayPrice eachtdp : tdprlist) {
								averPrice += eachtdp.getDayPrice();
								feastRatio += eachtdp.getFeastRatio();
							}
							count=MathUtils.div(averPrice, tdprlist.size(), 2)+"-"+each.getOilWear()+"-"+each.getFreeKm()+
								"-"+each.getMoreFreeKmPrice()+"-"+MathUtils.div(feastRatio, tdprlist.size(), 2)+"-"+
									each.getCarSeats()+"-"+each.getCarConfig()+"-"+each.getCarModel()+"-"+each.getCarType()+
									"-"+each.getDayLowPrice();
							logger.info("账号设置参数拼接完成>"+count);
							tour.add(count);
						}
					}
				}
			}else{
				tour = taSer.getAverage(areaName,uname, dayKilo, DateUtils.std_st(sTime), DateUtils.std_et(eTime), tour);
			}
		}
		if(tour.size()>0){
			String lonAndLat="";//默认城市坐标为车辆停靠点坐标
			hql="from CityList where cityName like ?";
			CityList city = citylistDao.findObj(hql, "%"+areaName+"%");
			if(city != null){
				lonAndLat= city.getLonAndLat();
			}
			String [] tourCount=null;
			String note="";//车辆配置说明
			String tp2up2dp="";//超范围距离
			for (int i=0;i<tour.size();i++) {
				tourCount=tour.get(i).split("-");
				if(isFL==1){//第1程判断车辆停靠点到起点是否超范围：如果超范围要将多余公里数算进总公里数
					tp2up2dp = HttpReqMeth.getRoutTimeAndDis_amap(lonAndLat, lon,null, null);
				}else if(isFL==2){//最后1程判断终点到车辆停靠点是否超范围：如果超范围要将多余公里数算进总公里数
					tp2up2dp = HttpReqMeth.getRoutTimeAndDis_amap(lat,lonAndLat,null, null);
				}
				if(tp2up2dp!=""){
					double dis_c_o_a = Integer.parseInt(tp2up2dp.split("-")[0])/1000;
					if(dis_c_o_a>Double.valueOf(tourCount[2])){//大于免费公里数
						km=MathUtils.add(MathUtils.sub(dis_c_o_a, Double.valueOf(tourCount[2]), 2),Double.valueOf(km),2)+"";
						dayKilo = MathUtils.div(Double.valueOf(km), betweenDay, 2);// 每日公里数
					}
				}
				tourCount[0]=sumPrice(unitNum,sTime, eTime, Double.valueOf(tourCount[0]),Double.valueOf(tourCount[4]),
						Integer.parseInt(tourCount[5]),betweenDay, waypointCitys, uname, areaName, dayKilo,Double.valueOf(km),
						Double.valueOf(tourCount[1]),Double.valueOf(tourCount[2]),Double.valueOf(tourCount[3]),
						Double.valueOf(tolls),lon,lat,Double.valueOf(tourCount[9]),invoiceStand);
				if("4".equals(tourCount[8])){//小车拼接
					if(StringUtils.isEmpty(tourCount[6]) && StringUtils.isEmpty(tourCount[7])){
						note=tourCount[5]+"座小车"+tourCount[6]+tourCount[7];
					}else{
						note=tourCount[5]+"座"+tourCount[6]+tourCount[7];
					}
				}else if("2".equals(tourCount[8])){//商务车拼接
					note=tourCount[5]+"座"+tourCount[7]+"商务车";
				}else if("1".equals(tourCount[8])){//中巴车拼接
					note=tourCount[5]+"座"+tourCount[7]+"中巴车";
				}else if("0".equals(tourCount[8])){//大巴车拼接
					note=tourCount[5]+"座"+tourCount[7]+"大巴车";
				}
				/*if(StringUtils.isEmpty(tourCount[6]))tourCount[6]="0";
				if(StringUtils.isEmpty(tourCount[7]))tourCount[7]="0";*/
				//标识=描述=不要发票价格=需要发票价格=座位数
				//tour.set(i, i+"="+note+"="+tourCount[0]+"="+Integer.parseInt(tourCount[5]));
				for (BcCarPrice bcpe : bcpList) {
					if(tourCount[5].equals(bcpe.getSeat()+"") && note.equals(bcpe.getCarExplain())){//座位数存在并且描述一样就累计
						bcpe.setPrice(MathUtils.add(bcpe.getPrice(), Double.valueOf(tourCount[0].split("=")[0]), 2));
						bcpe.setBillPrice(MathUtils.add(bcpe.getBillPrice(), Double.valueOf(tourCount[0].split("=")[1]), 2));
						bcpe.setPriceDetail(bcpe.getPriceDetail()+";"+routeNo+"="+bcpe.getPrice()+"="+bcpe.getBillPrice());
					}else{//座位数不存在添加
						BcCarPrice bcp=new BcCarPrice();
						bcp.setMainOrderNum(mainOrderNum);
						bcp.setPrice(Double.valueOf(tourCount[0].split("=")[0]));
						bcp.setBillPrice(Double.valueOf(tourCount[0].split("=")[1]));
						bcp.setCarExplain(note);
						bcp.setSeat(Integer.parseInt(tourCount[5]));
						bcp.setPriceDetail(routeNo+"="+bcp.getPrice()+"="+bcp.getBillPrice());
						bcpList.add(bcp);
					}
				}
			}
		}
		return bcpList;
	}
	private String sumPrice(String unitNum,String sTime,String eTime,double price,double feastRatio,int carSeats,
			long betweenDay,String waypointCitys,String uname,String areaName,double dayKilo,double km,
			double oilWear,double freeKm,double freeKmPrice,double tolls,String lon,String lat,double dayLowPrice,double invoiceStand){
		double totalPrice=0;//最终支付金额
		/********判断公里数浮动和天数浮动*********/
		String hql = "from TouristRatio where carSeats=? and kilometre>=? order by kilometre asc";
		TouristRatio tr=trSer.findObj(hql, carSeats,dayKilo,"LIMIT 1");
		if(tr!=null){
			double kiloGain=MathUtils.mul(tr.getMarkupRatio(), dayKilo*betweenDay, 2);
			totalPrice+=MathUtils.mul(price, kiloGain, 2);//总价加上公里数利润
			double dayGain=MathUtils.mul(tr.getDayRatio(),betweenDay, 2);
			totalPrice+=MathUtils.mul(price, dayGain, 2);//总价加上天数利润
		}
		/********判断公里数浮动和天数浮动*********/
		/********判断节假日*********/
		hql = "from FeastDayList where (f_time>=? and f_time<=?)";
		List<FeastDayList> flist = fdlDao.findhqlList(hql, DateUtils.strToDate(DateUtils.yyyy_MM_dd, sTime),
			DateUtils.strToDate(DateUtils.yyyy_MM_dd, eTime));
		if(flist.size() > 0){// 行程包含节假日
			totalPrice += MathUtils.mul(price, flist.size() , 2);
			totalPrice += MathUtils.mul(totalPrice, feastRatio, 2);
			betweenDay = betweenDay-flist.size();
		}
		/********判断节假日*********/
		totalPrice += MathUtils.mul(price, betweenDay, 2);//总价=单价*天数
		/********判断特殊路线*********/
		String [] way = waypointCitys.split(",");
		hql = "from SpecialRoute where endArea like ?";
		SpecialRoute sr = null;
		double markup = 0;
		for (int i = 0; i < way.length; i++) {
			sr = srDao.findObj(hql, "%"+way[i]+"%");
			if(sr != null){
				if(sr.getMarkupRatio() > markup){
					markup = sr.getMarkupRatio();
				}
			}
		}
		if(markup > 0){//特殊区域需要加价
			totalPrice += MathUtils.mul(totalPrice,MathUtils.div(markup, 100, 2) , 2);	
		}
		/*hql="from SpecialRoute where startArea like ? and endArea like ?";
		SpecialRoute sr=srSer.findObj(hql, "%"+areaName+"%","%"+endArea+"%");
		if(sr!=null){//特殊路线需要加价
			price+=MathUtils.mul(price,MathUtils.div(sr.getMarkupRatio(), 100, 2) , 2);
		}*/
		/********小时内折扣********/
		double discount = getDiscount(uname, DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm, sTime+" 00:00"), 
			DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm, eTime), areaName, carSeats, dayKilo);
		if(discount > 0){
			totalPrice = MathUtils.mul(totalPrice, discount, 2);
		}
		/********小时内折扣********/
		
		totalPrice = MathUtils.add(totalPrice, km * oilWear + tolls * U.getTypeBySeats(carSeats), 2);
		
		/*logger.info("停靠点-作为起点："+lonAndLat+", 乘客终点-作为终点："+lat+", 油价："+oilWear+", 过路费*类型车："+tolls * WebUtil.getTypeBySeats(carSeats));
		logger.info("总价："+totalPrice);
		double dis_c_o_a = 0d; // 行程距离A
		// 获取超范围行程距离A：车辆【停靠点-->乘客起点-->乘客终点】的距离A    接送机的超范围计算方式此处不用，此处只算包车的价格
		String tp2up2dp = HttpReqMeth.getRoutTimeAndDis_amap(lonAndLat, lon, lat, null);
		if(StringUtils.isNotBlank(tp2up2dp)){
			dis_c_o_a = Integer.parseInt(tp2up2dp.split("-")[0])/1000;
			logger.info("成功：高德地图【行程距离A【停靠点-->乘客起点-->乘客终点】："+dis_c_o_a+"公里]】");
		}else{
			logger.info("失败：高德地图【停靠点-->乘客起点-->乘客终点】！！！");
		}
		double dis_c_o_b = 0d; // 行程距离B
		// 获取超范围行程距离B：车辆【停靠点-->乘客终点】的距离B
		String tp2dp = HttpReqMeth.getRoutTimeAndDis_amap(lonAndLat, lat, null, null);
		if(StringUtils.isNotBlank(tp2dp)){
			dis_c_o_b = Integer.parseInt(tp2dp.split("-")[0])/1000;
			logger.info("成功：高德地图【行程距离B【停靠点-->乘客终点】："+dis_c_o_b+"公里]】");
		}else{
			logger.info("失败：高德地图【停靠点-->乘客终点】！！！");
		}
		
		double beyondKm = 0d; // 超范围距离
		beyondKm = dis_c_o_a - dis_c_o_b - freeKm;// 距离A-距离B-超范围距离=是否超范围
		if(beyondKm > 0){// 超范围
			totalPrice = MathUtils.add(totalPrice, MathUtils.mul(beyondKm, freeKmPrice, 2), 0);
			logger.info("已经超范围："+beyondKm+"公里, 超范围后总价："+totalPrice);
		}*/
		//totalPrice = MathUtils.mul(pcSer.invoiceAmount(unitNum, null, totalPrice, 0), 1, 2);// 不开发票金额
		if(totalPrice<dayLowPrice) totalPrice=dayLowPrice;//低于每日最低价则取最低价
		//double billPrice=MathUtils.mul(pcSer.invoiceAmount(unitNum, null, totalPrice, 1), 1, 2);//开发票金额
		double billPrice=(totalPrice+=MathUtils.mul(totalPrice, invoiceStand, 2));//开发票金额
		if(billPrice<dayLowPrice) billPrice=dayLowPrice;//低于每日最低价则取最低价
		if(totalPrice%10!=0) totalPrice=totalPrice+(10-(totalPrice%10));//收整10
		if(billPrice%10!=0) billPrice=billPrice+(10-(billPrice%10));//收整10
		return (int)Math.ceil(totalPrice)+"="+(int)Math.ceil(billPrice);
	}
	@Override
	public Map<String, Object> adupdeTcpr(ReqSrc reqsrc, String updId, String uname, String dayPrice, String tcId,
			String dayKm, String fourHourRebate, String fiveHourRebate, String sixHourRebate, String sevenHourRebate) {
		String logtxt = U.log(logger, "添加/修改/删除价格阶梯设置",reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			TouristCharterPrice tcpr = null;
	 		Date startTime=null,endTime=null;
	 		String [] dayPriceCount=null;
			if(fg) {
				if(StringUtils.isNotEmpty(dayPrice)){//添加/修改
					if(fg) {
						if(StringUtils.isEmpty(dayPrice)) {
							fg = U.setPutFalse(map, "[价格阶梯]不能为空");
						}else {
							dayPriceCount=dayPrice.trim().split(",");
							U.log(logger, "[价格阶梯] dayPrice="+dayPrice);
						}
					}
					if(fg) {
		            	String [] timePrice=null;
						if(StringUtils.isNotEmpty(updId)){//修改
							//先删除
							String [] delId=updId.split(",");
							for (int i = 0; i < delId.length; i++) {
								operTcpr(delId[i], tcpr);
							}
							//再添加
							TouristCharter tc=tcSer.find(Long.valueOf(tcId));
							for (int i = 0; i < dayPriceCount.length; i++) {
			 					timePrice=dayPriceCount[i].split("/");
			 					startTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd, timePrice[0]);
			 					endTime=DateUtils.std_et(timePrice[1]);
			 					tcpr = new TouristCharterPrice();
			 					tcpr.setUname(uname);
			 	 	  			tcpr.setToucId(tcId);
			 	 	  			tcpr.setAreaName(tc.getAreaName());
			 	 	  			tcpr.setCarSeats(tc.getCarSeats());
			 	 	  			//tcpr.setDayKm(Double.valueOf(dayKm));
			 	 	 			tcpr.setDayPrice(timePrice[2]);
			 	 	 			tcpr.setFourHourRebate(MathUtils.div(Double.valueOf(fourHourRebate), 100, 2));
			 	 	 			tcpr.setFiveHourRebate(MathUtils.div(Double.valueOf(fiveHourRebate), 100, 2));
			 	 	 			tcpr.setSixHourRebate(MathUtils.div(Double.valueOf(sixHourRebate), 100, 2));
			 	 	 			tcpr.setSevenHourRebate(MathUtils.div(Double.valueOf(sevenHourRebate), 100, 2));
			 	 	 			tcpr.setStartTime(startTime);
			 	 	 			tcpr.setEndTime(endTime);
			 	 	 			tcpr.setFeastRatio(MathUtils.div(Double.valueOf(timePrice[3]), 100, 2));
			 	 	 			tcpr.setAddTime(new Date());
			 	 	 			toupDao.save(tcpr);
			 	 	 			int result=tdpSer.addTdPirice(tcId,uname, tc.getAreaName(), tc.getCarSeats(), dayKm, 
			 	 	 					startTime, endTime, timePrice[2],tcpr.getFeastRatio());
			 	 	 			if(result==1){//进入平均值计算
			 	 	 				taSer.operTcprAverage(1,tcId,startTime, endTime,timePrice[2],tcpr.getFeastRatio());//计算平均值
			 	 	 			}
			 				}
							 U.setPut(map, 1, "操作成功");
			 	  		}else{ //添加
			 				String hql="from TouristCharterPrice where toucId=?";
			 				List<TouristCharterPrice> tcprlist=toupDao.findhqlList(hql,tcId);
			 				/***先判断时间是否冲突***/
			 				for (int i = 0; i < dayPriceCount.length; i++) {
			 					timePrice=dayPriceCount[i].split("/");
			 					startTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd, timePrice[0]);
			 					endTime=DateUtils.std_et(timePrice[1]);
			 					if(tcprlist.size()>0){
			 						for (TouristCharterPrice each : tcprlist) {
			 							if(DateUtils.isNotConflict(startTime,endTime, each.getStartTime(), each.getEndTime()) ||
			 							   startTime.equals(each.getStartTime()) || endTime.equals(each.getEndTime())){
			 								fg = U.setPutFalse(map, "【"+timePrice[0]+
				 									"至"+timePrice[1]+"】该时间与之前设置有冲突，请仔细查看后添加");
			 								break;
			 							}
			 						}
			 					}
			 						
			 				}
			 				/***先判断时间是否冲突***/
			 	  			if(fg) {
			 	  				TouristCharter tc=tcSer.find(Long.valueOf(tcId));
				 	  			for (int i = 0; i < dayPriceCount.length; i++) {
				 					timePrice=dayPriceCount[i].split("/");
				 					startTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd, timePrice[0]);
				 					endTime=DateUtils.std_et(timePrice[1]);
				 					tcpr = new TouristCharterPrice();
				 	 	  			tcpr.setUname(uname);
				 	 	  			tcpr.setToucId(tcId);
				 	 	  			tcpr.setAreaName(tc.getAreaName());
				 	 	  			tcpr.setCarSeats(tc.getCarSeats());
				 	 	  			//tcpr.setDayKm(Double.valueOf(dayKm));
				 	 	 			tcpr.setDayPrice(timePrice[2]);
				 	 	 			tcpr.setFourHourRebate(MathUtils.div(Double.valueOf(fourHourRebate), 100, 2));
				 	 	 			tcpr.setFiveHourRebate(MathUtils.div(Double.valueOf(fiveHourRebate), 100, 2));
				 	 	 			tcpr.setSixHourRebate(MathUtils.div(Double.valueOf(sixHourRebate), 100, 2));
				 	 	 			tcpr.setSevenHourRebate(MathUtils.div(Double.valueOf(sevenHourRebate), 100, 2));
				 	 	 			tcpr.setStartTime(startTime);
				 	 	 			tcpr.setEndTime(endTime);
				 	 	 			tcpr.setFeastRatio(MathUtils.div(Double.valueOf(timePrice[3]), 100, 2));
				 	 	 			tcpr.setAddTime(new Date());
				 	 	 			toupDao.save(tcpr);
				 	 	 			int result=tdpSer.addTdPirice(tcId,uname, tc.getAreaName(), tc.getCarSeats(), dayKm, 
				 	 	 					startTime, endTime, timePrice[2],tcpr.getFeastRatio());
				 	 	 			if(result==1){//进入平均值计算
				 	 	 				taSer.operTcprAverage(1,tcId,startTime, endTime,timePrice[2],tcpr.getFeastRatio());//计算平均值
				 	 	 			}
				 				}
				 	  			U.setPut(map, 1, "操作成功");
			 	  			}
			 	  		}
		            }
				}else{//删除
					if(fg) {
						if(StringUtils.isEmpty(updId)) {
							fg = U.setPutFalse(map, "[删除id]不能为空");
						}else {
							U.log(logger, "[删除id] updId="+updId);
						}
					}
					if(fg) {
						int flag = operTcpr(updId, tcpr);
				 		if(flag == 1){
				 			U.setPut(map, 1, "操作成功");
				 		}else{
				 			U.setPutFalse(map, 0, "操作失败");
				 		}
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, logger, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
		
	}
	public int operTcpr(String updId, TouristCharterPrice tcpr){
		try {
			if(tcpr != null){
				if(StringUtils.isNotEmpty(updId)){
					toupDao.update(tcpr);
				}else{
					toupDao.save(tcpr);
				}
			}else{
				TouristCharterPrice del = toupDao.find(Long.parseLong(updId));
				Date startTime=del.getStartTime();Date endTime=del.getEndTime();
				double dayKm=del.getDayKm();String tcId=del.getToucId();
				toupDao.delete(del);
				int result=tdpSer.delTdPirice(tcId, dayKm, startTime, endTime);//删除这段时间的每日价格记录
				if(result==1){
					taSer.operTcprAverage(2,tcId,startTime, endTime,"0",0);//计算平均值
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
}
