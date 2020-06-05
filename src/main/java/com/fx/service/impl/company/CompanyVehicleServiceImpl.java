/*
 * File name: CompanyVehicleService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 Administrator 2020年4月20日
 * ... ... ...
 *
 ***************************************************/

package com.fx.service.impl.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.OrderTemp;
import com.fx.commons.utils.enums.BusinessType;
import com.fx.commons.utils.enums.CarNature;
import com.fx.commons.utils.enums.OrderStatus;
import com.fx.commons.utils.enums.PublicDataType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.HttpReqMeth;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.MyStringUtils;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.dao.company.PublicDataSetDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.company.PublicDataSet;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.order.CarOrder;
import com.fx.service.company.CompanyVehicleService;

/**
 * @Description:
 * @author: zh
 * @version: 1.0, 2020年4月20日
 */
@Service
@Transactional
public class CompanyVehicleServiceImpl extends BaseServiceImpl<CompanyVehicle, Long> implements CompanyVehicleService {
	/** 日志记录 */
	private Logger				log	= LogManager.getLogger(this.getClass());

	@Autowired
	private CompanyVehicleDao	companyVehicleDao;
	
	/**系统数据设置-服务**/
	@Autowired
	private PublicDataSetDao	pdsDao;
	
	/**行程订单-服务**/
	@Autowired
	private CarOrderDao	coDao;
	





	/**
	 * @see com.fx.commons.hiberantedao.service.BaseServiceImpl#getDao()
	 */
	@Override
	public ZBaseDaoImpl<CompanyVehicle, Long> getDao() {

		return companyVehicleDao;
	}



	/**
	 * 
	 * @see com.fx.service.company.CompanyVehicleService#companyVehicleAdd(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override

	public Map<String, Object> companyVehicleAdd(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "添加-车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			CompanyVehicle companyVehicle = jsonObject.toJavaObject(CompanyVehicle.class);

			if (fg) {
				if (StringUtils.isBlank(companyVehicle.getPlateNumber())) {
					U.logFalse(log, "添加车辆--失败--plateNumber为空");
					fg = U.setPutFalse(map, 0, "添加车辆失败，plateNumber为空");
				}
			}

			if (fg) {
				companyVehicleDao.save(companyVehicle);
				U.log(log, "添加车辆成功");
				U.setPut(map, 1, "添加车辆成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	/**
	 * @see com.fx.service.company.CompanyVehicleService#companyVehicleUpdate(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override

	public Map<String, Object> companyVehicleUpdate(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "修改-车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			CompanyVehicle companyVehicle = jsonObject.toJavaObject(CompanyVehicle.class);

			if (fg) {
				CompanyVehicle originalVehicle = companyVehicleDao.findByField("id", companyVehicle.getId());
				//判断图片是否修改，用于删除图片
				if (null != originalVehicle.getTravelLicensePhotoURL() && null != companyVehicle.getTravelLicensePhotoURL()) {
					if (!originalVehicle.getTravelLicensePhotoURL().equals(companyVehicle.getTravelLicensePhotoURL())) {
						LU.deletePic(originalVehicle.getTravelLicensePhotoURL());
					}
				}
				
				companyVehicleDao.getCurrentSession().clear();
				// boolean update = companyVehicleDao.update(companyVehicle);
				companyVehicleDao.update(companyVehicle);
				// if (update) {
				// U.log(log, "修改-车辆-成功");
				// U.setPut(map, 0, "修改车辆信息成功");
				// } else {
				// U.logFalse(log, "修改-车辆-失败");
				// U.setPutFalse(map, 1, "修改车辆失败");
				// }
				U.log(log, "修改-车辆-成功");
				U.setPut(map, 1, "修改车辆信息成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fx.service.company.CompanyVehicleService#companyVehicleDelete(com.fx.
	 * commons.utils.enums.ReqSrc, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.http.HttpServletRequest, com.alibaba.fastjson.JSONObject)
	 */
	@Override

	public Map<String, Object> companyVehicleDelete(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "删除-车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (!jsonObject.containsKey("id")) {
					U.logFalse(log, "删除-车辆-传参错误，参数中添加id");
					fg = U.setPutFalse(map, 0, "参数错误，需传入id");
				}
			}
			if (fg) {
				if (jsonObject.getLong("id") == null) {
					U.logFalse(log, "删除-车辆-id为空");
					fg = U.setPutFalse(map, 0, "id不能为空");
				}

			}
			if (fg) {
				long id = jsonObject.getLong("id");
				// boolean res = companyVehicleDao.deleteById(reqsrc, id);
				// if (res) {
				// U.setPut(map, 0, "删除成功");
				// } else {
				// U.setPut(map, 1, "删除失败");
				// }
				CompanyVehicle findByField = companyVehicleDao.findByField("id", id);

				//删除车辆图片
				LU.deletePic(findByField.getTravelLicensePhotoURL());
				companyVehicleDao.delete(id);
				U.setPut(map, 1, "删除成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> companyVehicleFindLists(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "获取-车辆-分页列表", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		// 驾驶员手机号
		String phone = jsonObject.getString("phone");
		// 车牌号
		String plateNumber = jsonObject.getString("plateNumber");
		// 座位数
		String seats = jsonObject.getString("seats");

		String unitNum = jsonObject.getString("unitNum");
		//所属公司
		String belongCompany = jsonObject.getString("belongCompany");
		//车辆性质
		CarNature carNature = null;
		if (!StringUtils.isBlank(jsonObject.getString("carUsage"))) {
			carNature = CarNature.valueOf(jsonObject.getString("carUsage"));
		}
		//小组名字
		String groupName = jsonObject.getString("groupName");
		//开始时间
		String startTime = jsonObject.getString("startTime");
		//结束时间
		String endTime = jsonObject.getString("endTime");

		boolean fg = true;
		try {
			if (fg)
				fg = U.valPageNo(map, page, rows, "用户");

			if (fg) {
				Page<CompanyVehicle> pd = companyVehicleDao.findCompanyVehicleList(reqsrc, page, rows, phone,
						plateNumber, seats, unitNum,belongCompany,carNature,groupName,startTime,endTime);

				U.setPageData(map, pd);

				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);

				U.setPut(map, 1, "请求数据成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> companyVehicleFindById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "查询-车辆信息-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (!jsonObject.containsKey("id")) {
					U.logFalse(log, "通过ID查询车辆信息-传参错误，参数中添加id");
					fg = U.setPutFalse(map, 0, "参数错误，需传入id");
				}
			}

			if (fg) {
				long id = jsonObject.getLongValue("id");
				CompanyVehicle companyVehicle = companyVehicleDao.findByField("id", id);
				map.put("data", companyVehicle);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> setDriverToVehicle(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "设置-车辆主驾驶员", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			long id = jsonObject.getLongValue("id");
			String uname = jsonObject.getString("uname");

			Map<String, Object> map2 = new HashMap<>();
			map2.put("id", id);
			map2.put("uname", uname);
			int res = companyVehicleDao.batchExecute("update CompanyVehicle set baseUserId.uname = :uname where id = :id", map2);
			// boolean setDriver = companyVehicleDao.setDriver(reqsrc,id,
			// uname);
			// if (setDriver) {
			// U.setPut(map, 0, "设置成功");
			// } else {
			// U.setPut(map, 1, "设置失败");
			// }
			if (res != 0) {
				U.log(log, "设置-车辆主驾驶员成功");
				U.setPut(map, 1, "设置成功");
			}
			else{
				U.logFalse(log, "设置-车辆主驾驶员失败");
				U.setPutFalse(map, 0, "设置失败");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> checkPlateNumExists(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum, String plateNum) {
		String logtxt = U.log(log, "车辆-校验车牌号是否重复", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (fg) {
				if (StringUtils.isBlank(plateNum)) {
					U.logFalse(log, "车辆-校验车牌号是否重复-车牌号为空");
					fg = U.setPutFalse(map, 0, "参数错误，需传入车牌号");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(unitNum)) {
					U.logFalse(log, "车辆-校验车牌号是否重复-unitNum为空");
					fg = U.setPutFalse(map, 0, "错误，unitNum为空");
				}
			}
			if (fg) {
				CompanyVehicle findObj = (CompanyVehicle)companyVehicleDao.findObj("from CompanyVehicle where plateNumber = ?0 and unitNum = ?1", plateNum,unitNum);
				if (findObj == null) {
					U.log(log,"车辆-校验车牌号是否重复-不重复");
					U.setPut(map, 1, "车牌号不存在");
				}
				else{
					U.logFalse(log, "车辆-校验车牌号是否重复-重复");
				    U.setPutFalse(map, 0, "车牌号已存在");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> lastSmartCar(ReqSrc reqsrc,OrderTemp ot, String firstCar, String seats, String force,
			String runArea, String plateNum, String selfOwned, double avgSpeed, String notContainPn, int isSmart) {
		String logtxt = U.log(log, (isSmart==0?"人工":"智能")+"派单获取最优车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//首先根据订单类型找出可以参与本次匹配的车辆类型
			String runAreas="0";//匹配车辆可跑区域
			if(ot.getServiceType().equals(ServiceType.PROVINCE_SER)){//不限区域和省际车
				runAreas="0,1";
			}else if(ot.getServiceType().equals(ServiceType.CITY_SER)){//不限区域和省际车和市际车
				runAreas="0,1,2";
			}else if(ot.getServiceType().equals(ServiceType.COUNTY_SER)){//不限区域和省际车和市际车和县际车
				runAreas="0,1,2,3";
			}
			List<CompanyVehicle> vehicles=new ArrayList<CompanyVehicle>();
			String hql="from CompanyVehicle where unitNum like :v0 and status=0";//获取座位数符合的非备用车辆
			if(StringUtils.isNotBlank(plateNum)) {
				hql+=" and plateNumber in (:v1)";
				Object[] obj = (String[])plateNum.split(",");
				vehicles=companyVehicleDao.findListIns(hql,"%"+ot.getUnitNum()+"%",obj);
			}else{
				/**找到已确认的冲突订单，排除该车辆 start*/
				if(!"1".equals(force)){//非强制查询
					String hqlCo="from CarOrder a where a.carOrderBase.unitNum=:v0 and a.status in (:v1) and a.disCar<>null and "
							+ "((a.stime>='"+ot.getStime()+"' and a.etime<='"+ot.getEtime()+"') or "
							+ "(a.stime<'"+ot.getStime()+"' and a.etime>='"+ot.getStime()+"' and a.etime<='"+ot.getEtime()+"') or "
							+ "(a.stime>='"+ot.getStime()+"' and a.stime<='"+ot.getEtime()+"' and a.etime>'"+ot.getEtime()+"') or "
							+ "(a.stime<'"+ot.getStime()+"' and a.etime>'"+ot.getEtime()+"'))";
					Object[] obj = {OrderStatus.DRIVER_NOT_CONFIRM,OrderStatus.DRIVER_CONFIRMED,OrderStatus.AL_TRAVEL,OrderStatus.COMPLETED};//已确认的订单状态
					List<CarOrder> colist=coDao.findListIns(hqlCo,ot.getUnitNum(),obj);
					Set<String>  conflictPn=new HashSet<String>();
					if(colist.size()>0){
						for (CarOrder eachco:colist) {
							conflictPn.add(eachco.getDisCar().getPlateNum());
						}
						Iterator<String> it = conflictPn.iterator();
						while (it.hasNext()) {
						  	if(StringUtils.isNotBlank(notContainPn)){
						  		notContainPn+=","+it.next();
							}else{
								notContainPn=it.next();
							}
						}
					}
				}
				/**找到已确认的冲突订单，排除该车辆 end*/
				if(ot.getRouteType().equals(RouteType.ONE_WAY) && isSmart==1){//单程接送&&智能派单
					hql+=" and seats>=:v1";
				}else{//包车订单和手动派单
					hql+=" and seats=:v1";
				}
				if(StringUtils.isNotBlank(runArea)){
					hql+=" and runningArea="+Integer.parseInt(runArea);
				} else{
					hql+=" and runningArea in ("+runAreas+")";
				}
				if(StringUtils.isNotBlank(notContainPn)){
					hql+=" and plateNumber not in ("+MyStringUtils.spilt(notContainPn)+")";
				}
				if(StringUtils.isNotBlank(selfOwned)) {
					hql+=" and businessType="+BusinessType.valueOf(selfOwned);
				}else{
					if("1".equals(firstCar)){//自营优先-顺序
						hql+=" order by businessType asc";
					}else if("2".equals(firstCar)){//挂靠优先-倒序
						hql+=" order by businessType desc";
					}
				}
				vehicles=companyVehicleDao.findListIns(hql,"%"+ot.getUnitNum()+"%",Integer.parseInt(seats));
			}
			if(vehicles.size()>0){
				Set<CompanyVehicle> conflictCar=new HashSet<CompanyVehicle>();
				/****排除限号的车辆*****/
				vehicles=restrictedCar(vehicles, conflictCar, ot.getLimitNum());
				/****排除限号的车辆*****/
				if(vehicles.size()>0){
					/****时间+距离排序获取最小值*****/
					if(avgSpeed==0){
						avgSpeed=30;//默认平均速度
						String pdshql="from PublicDataSet where setId=?0 and publicDataType=?1";
						PublicDataSet pds=pdsDao.findObj(pdshql,"18982208376", PublicDataType.AVG_SPEED);//20191129改为查询大后台设置，所有车队一致
						if(pds!=null){//平均速度
							avgSpeed=DateUtils.getAvgSpeed(ot.getStime(), ot.getEtime(), pds.getCusSmsSet());
						}
					}
					return colligate(vehicles, hql, ot.getStime(),ot.getEtime(), ot.getsLonLat(),ot.geteLonLat(),avgSpeed);
					/****时间+距离排序获取最小值*****/
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return null;
	}
	//排除限号的车辆
	private List<CompanyVehicle> restrictedCar(List<CompanyVehicle> carList,Set<CompanyVehicle> conflictCar,String restrictedNum) throws Exception{
		if(StringUtils.isNotBlank(restrictedNum)){
			for (CompanyVehicle each : carList) {
				if(restrictedNum.contains(MyStringUtils.getLastNum(each.getPlateNumber()))) conflictCar.add(each);//排除限号车辆
			}
			carList.removeAll(conflictCar);
		}
		return carList;
	}
	//综合优先:距离+时间的最小值
	private List<Map<String, Object>> colligate(List<CompanyVehicle> vehicles,String hql,
			Date stime,Date etime,String usLonLat,String ueLonLat,double avgSpeed) throws Exception{
		TreeMap<String, Map<String, Object>> bestMap = new TreeMap<String, Map<String, Object>>();//有订单的车辆
		TreeMap<String, Map<String, Object>> seatMap = new TreeMap<String,Map<String, Object>>();//无订单的车辆
		TreeMap<Integer, Integer> seats = new TreeMap<Integer, Integer>();//所有车辆的座位数
		String [] lestCount=null;
		List<CarOrder> colist=null;
		Date lastTime=null;
		//Object[] status = {OrderStatus.CANCELED,OrderStatus.REFUSED};//无效的订单状态
		for (CompanyVehicle each:vehicles) {
			lestCount=getSecondsDistanceSub(hql, lastTime, each.getPlateNumber(),each.getDockedLongitude()+","+each.getDockedLatitude(),
					stime, usLonLat,ueLonLat, "", null,avgSpeed, 1,0,"",etime,"0",1).split("/");
			if(Long.valueOf(lestCount[0])>0){//车辆符合接单
				Map<String, Object> fmap = new HashMap<String, Object>();
				StringBuffer sb=new StringBuffer();
				/**查询日程冲突，已确认的订单车辆已排除 start*/
				/*String hqlCo="from CarOrder where status not in (:v0) and "
						+ "((stime>='"+stime+"' and etime<='"+etime+"') or "
						+ "(stime<'"+stime+"' and etime>='"+stime+"' and etime<='"+etime+"') or "
						+ "(stime>='"+stime+"' and stime<='"+etime+"' and etime>'"+etime+"') or "
						+ "(stime<'"+stime+"' and etime>'"+etime+"'))";
				colist=coDao.findListIns(hqlCo,status);*/
				
				String dcihql="from CarOrder a where a.disCar.plateNum=?0 and "
						+ "((a.disCar.mainDriverStime>='"+stime+"' and a.disCar.mainDriverEtime<='"+etime+"') or "
						+ "(a.disCar.mainDriverStime<'"+stime+"' and a.disCar.mainDriverEtime>='"+stime+"' and a.disCar.mainDriverEtime<='"+etime+"') or "
						+ "(a.disCar.mainDriverStime>='"+stime+"' and a.disCar.mainDriverStime<='"+etime+"' and a.disCar.mainDriverEtime>'"+etime+"') or "
						+ "(a.disCar.mainDriverStime<'"+stime+"' and a.disCar.mainDriverEtime>'"+etime+"'))";
				colist=coDao.findhqlList(dcihql,each.getPlateNumber());
				if(colist.size()>0){
					for (CarOrder co:colist) {
						sb.append(co.getOrderNum()).append(",");
					}
					lestCount[2]="-1";//日程冲突当天一定是有订单的
				}
				/**查询日程冲突，已确认的订单车辆已排除 end*/
				
				if(!"0".equals(lestCount[1])){//接下一个订单冲突
					sb.append(lestCount[1]+",");
				}
				if(!sb.toString().contains(",")) {//非多个订单冲突的车辆才进入匹配，多个订单冲突也算一个寻找条件
					if(sb.length()>1){
						fmap.put("cancelNum", sb.deleteCharAt(sb.length() - 1).toString());
					}else{
						fmap.put("cancelNum", "0");
					}
					/*查询日程冲突*/
					fmap.put("choiceCar", each);
					fmap.put("haveRoute", lestCount[2]);
					if("0".equals(lestCount[2])){//当前车辆在用车开始当天没有订单
						seatMap.put(Long.valueOf(lestCount[0])+","+each.getId(),fmap);
						seats.put(each.getSeats(), each.getSeats());
					}else{
						bestMap.put(Long.valueOf(lestCount[0])+","+each.getId(),fmap);
					}
				}
			}
		}
		List<Map<String, Object>> fcar = new ArrayList<Map<String, Object>>();
		if(seatMap.size()>0){//当天没有订单,取小座位数里面取最优的
			List<Map<String, Object>> area = new ArrayList<Map<String, Object>>();//区域车辆
			TreeMap<Integer, Integer> runArea = new TreeMap<Integer, Integer>();//所有车辆可跑区域
			CompanyVehicle car=null;
			for(Map.Entry<String, Map<String, Object>> entry : seatMap.entrySet()){
			    Map<String, Object> fmap = entry.getValue();
			    car=(CompanyVehicle)fmap.get("choiceCar");
			    if(car.getSeats()==seats.get(seats.firstKey())){//依次循环最优值对应座位数刚好是最小的座位数
			    	area.add(fmap);
			    	runArea.put(car.getRunningArea(), car.getRunningArea());
			    }
			}
			if(area.size()>0){
				if(runArea.lastKey()==3){//订单一定是县际业务，因为非县际业务是无法找到县际车辆的，优先匹配川A车辆
					for (Map<String, Object> entry: area) {
					    car=(CompanyVehicle)entry.get("choiceCar");
					    if(car.getPlateNumber().startsWith("川A")){
			    			fcar.add(entry);
					    	break;
			    		}
					}
					
				}
				if(fcar.size()==0){
					for (Map<String, Object> entry: area) {
					    car=(CompanyVehicle)entry.get("choiceCar");
					    if(car.getRunningArea()==runArea.lastKey()){//依次循环可跑区域座位数刚好是订单业务类型
					    	fcar.add(entry);
					    	break;
					    }
					}
				}
			}
		}
		if(bestMap.size()>0){
			fcar.add(bestMap.get(bestMap.firstKey()));
			//return bestMap.get(bestMap.firstKey());
		}
		return fcar;
	}
	//获取当前车辆在用车开始时间之前最大的结束时间与用车开始时间的时间差值(秒)+获取当前车辆最后一个行程的结束地点与新订单开始时间的距离
	private String getSecondsDistanceSub(String hql,Date lastTime,String plateNum,String stayPosition,Date stime,
			String usLonLat,String ueLonLat,String distance,CarOrder beforeJcNext,double avgSpeed,long least,long endSecs,
			String lastJieKeren,Date etime,String cancelNum,int haveRoute) throws Exception{
		//是否能接到下个订单
		//hql="FROM CarOrder WHERE id=(SELECT MIN(id) FROM CarOrder WHERE plateNumber=:v0 AND stime >=:v1 and orderState not in (:v2))";
		hql="from CarOrder a where a.disCar.plateNum=?0 and a.disCar.mainDriverStime>=?1 order by a.disCar.mainDriverStime asc";
		beforeJcNext =coDao.findObj(hql,plateNum,etime,"LIMIT 1");//当前车辆在当前用车结束时间之后第一个行程
		if(beforeJcNext!=null){
			String [] res=HttpReqMeth.getRoutTimeAndDis_amap(ueLonLat,
					beforeJcNext.getRouteMps().get(0).getMapPoint().getLngLat(),"", "").split("-");//当前订单终点到下个订单起点
			if(MathUtils.div(Double.valueOf(res[0]), 1000, 2)>50)avgSpeed=70;//超过50公里平均速度按70算
			endSecs=(int)MathUtils.mul(MathUtils.div(MathUtils.div(Double.valueOf(res[0]), 1000, 2), avgSpeed, 2), 3600, 2);//接客人耗时最终需要的秒数
  			lastJieKeren=DateUtils.getPlusSeconds(DateUtils.yyyy_MM_dd_HH_mm_ss, etime, endSecs);
  			if(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, lastJieKeren).after(beforeJcNext.getStime())){//不满足接下个订单耗时
  				if(OrderStatus.DRIVER_NOT_CONFIRM.equals(beforeJcNext.getStatus()) || 
  				   OrderStatus.DRIVER_CONFIRMED.equals(beforeJcNext.getStatus()) ||
  				   OrderStatus.AL_TRAVEL.equals(beforeJcNext.getStatus())|| 
  				   OrderStatus.COMPLETED.equals(beforeJcNext.getStatus())){
  					return "-1/"+beforeJcNext.getOrderNum();//计调已确认派车并且接不到下个订单，当前车辆就不能被派单，不然就要取消计调已确认的订单
  				}else{
  					cancelNum=beforeJcNext.getOrderNum();
  				}
  			}
		}
		String sLonAndLat="";
		Date sTime=null;
		//hql="FROM CarOrder WHERE plateNumber=:v0 AND etime <=:v1 and orderState in (:v2) order by etime desc";
		//Object[] obj = {OrderStatus.JD_NOT_CONFIRM,OrderStatus.DRIVER_NOT_CONFIRM,OrderStatus.DRIVER_CONFIRMED,OrderStatus.AL_TRAVEL,OrderStatus.COMPLETED};//有效的订单状态
		hql="from CarOrder a where a.disCar.plateNum=?0 and a.disCar.mainDriverEtime<=?1 order by a.disCar.mainDriverEtime desc";
		beforeJcNext =coDao.findObj(hql,plateNum,etime,"LIMIT 1");//当前车辆在当前用车开始时间之前最后一个行程
		if(beforeJcNext!=null){
			sTime=beforeJcNext.getEtime();//默认以最后一个订单结束时间来算
			sLonAndLat=stayPosition;//默认以停靠点来算
			if(DateUtils.getHoursOfTowDiffDate(beforeJcNext.getEtime(), stime)<=3){//三小时内取最后一个时间和最后一个结束地点
				least=DateUtils.getSecondsOfTowDiffDate(beforeJcNext.getEtime(), stime);
				sLonAndLat=beforeJcNext.getRouteMps().get(1).getMapPoint().getLngLat();
			}else{//三小时外以停靠点来算
				if(org.apache.commons.lang3.time.DateUtils.isSameDay(beforeJcNext.getEtime(), stime)){//是同一天
					least=DateUtils.getSecondsOfTowDiffDate(beforeJcNext.getEtime(), stime);
				}else{
					least=DateUtils.getSecondsOfTowDiffDate(DateUtils.std_st(DateUtils.DateToStr(stime)), stime);
					sTime=DateUtils.std_st(DateUtils.DateToStr(stime));
					haveRoute=0;//用车当天没有订单
				}
			}
			distance=HttpReqMeth.getRoutTimeAndDis_amap(sLonAndLat,usLonLat, "", "").split("-")[0];
			least+=Long.valueOf(distance);
			if(MathUtils.div(Double.valueOf(distance), 1000, 2)>50)avgSpeed=70;//超过50公里平均速度按70算
			endSecs=(int)MathUtils.mul(MathUtils.div(MathUtils.div(Double.valueOf(distance), 1000, 2), 
					avgSpeed, 2), 3600, 2);//接客人耗时最终需要的秒数
			lastJieKeren=DateUtils.getPlusSeconds(DateUtils.yyyy_MM_dd_HH_mm_ss, sTime, endSecs);
			if(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, lastJieKeren).after(stime)){
  				least=-1;//当前车辆不满足当前订单
  			}
		}else {
			haveRoute=0;//用车当天没有订单
		}
		return least+"/"+cancelNum+"/"+haveRoute;
	}



	@Override
	public Map<String, Object> getAllVehicle(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum) {
		String logtxt = U.log(log, "车辆-获取单位的所有车辆信息", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.logFalse(log, "车辆-获取单位的所有车辆信息-unitNum为空");
				U.setPutFalse(map, 0, "获取单位编号失败");
			}
			else {
				List<CompanyVehicle> companyVehicleList = companyVehicleDao.findListByField("unitNum", unitNum);
				
				map.put("data", companyVehicleList);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.log(log, "车辆-获取单位的所有车辆信息-成功");
				U.setPut(map, 1, "查询成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getAllPlateSeats(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum,int type) {
		String logtxt = U.log(log, "车辆-获取单位的所有"+(type==1?"车牌号":"车辆座位数"), reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.setPutFalse(map, 0, "获取单位编号失败");
			}
			else {
				if(type==1) {
					List<String> plates=new ArrayList<String>();
					String hql="select new CompanyVehicle(plateNumber) where unitNum=?0";
					List<CompanyVehicle> companyVehicleList = companyVehicleDao.findhqlList(hql, unitNum);
					for (CompanyVehicle each : companyVehicleList) {
						plates.add(each.getPlateNumber());
					}
					map.put("plateNums", plates);
					U.log(log, "车辆-获取单位的所有车牌号-成功");
					U.setPut(map, 1, "查询成功");
				}else {
					List<String> seats=new ArrayList<String>();
					String hql="select new CompanyVehicle(seats) where unitNum=?0 group by seats";
					List<CompanyVehicle> companyVehicleList = companyVehicleDao.findhqlList(hql, unitNum);
					for (CompanyVehicle each : companyVehicleList) {
						seats.add(each.getSeats()+"");
					}
					map.put("seats", seats);
					U.log(log, "车辆-获取单位的所有车辆座位数-成功");
					U.setPut(map, 1, "查询成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> checkBeforeSetDriver(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String uname,String unitNum) {
		String logtxt = U.log(log, "车辆-设置驾驶员判断", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.setPutFalse(map, 0, "获取单位编号失败");
			}
			else {
				String hql = "from CompanyVehicle where unitNum = ?0 and baseUserId.uname = ?1";
				List<CompanyVehicle> list = companyVehicleDao.findhqlList(hql, unitNum,uname);
				if (list.size() == 0) {
					U.log(log, "车辆-设置驾驶员判断-用户" + uname + "可以设置为驾驶员");
					U.setPut(map, 1, "可以设置为驾驶员");
				}
				else{
					U.log(log, "车辆-设置驾驶员判断-用户" + uname + "不可以设置为驾驶员");
					U.setPutFalse(map, 0, "该人员已被设置为驾驶员");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}

}
