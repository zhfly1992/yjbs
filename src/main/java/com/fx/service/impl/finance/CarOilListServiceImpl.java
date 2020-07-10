package com.fx.service.impl.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.OilWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.FileManDao;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.finance.CarOilListDao;
import com.fx.dao.finance.CarRepairListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CusWallet;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WalletList;
import com.fx.entity.finance.CarOilList;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.OilCardList;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.finance.StaffReimburse;
import com.fx.service.company.CompanyVehicleService;
import com.fx.service.cus.CusWalletService;
import com.fx.service.cus.WalletListService;
import com.fx.service.finance.CarOilListService;
import com.fx.service.finance.OilCardListService;
import com.fx.service.finance.ReimburseListService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CarOilListServiceImpl extends BaseServiceImpl<CarOilList, Long> implements CarOilListService {
	/** 日志记录 */
	private Logger					log	= LogManager.getLogger(this.getClass());
	@Autowired
	private RedisUtil				redis;

	/** 消息通知-服务 */
//	@Autowired
//	private YMessageService			ymSer;
	@Autowired
	private CarOilListDao			coiDao;
	/** 文件管理-服务 */
//	@Autowired
//	private FileService				fmSer;
	/** 驾驶员-服务 */
	@Autowired
	private StaffDao				dlDao;
	/** 油卡、油票-服务 */
	@Autowired
	private OilCardListService		oclSer;
	/** 车辆列表-服务 */
	@Autowired
	private CompanyVehicleService	clSer;
	@Autowired
	private CarRepairListDao		cpaiDao;
	@Autowired
	private ReimburseListService	reimSer;
	@Autowired
	private CusWalletService		cwSer;
	@Autowired
	private WalletListService		wlSer;
	@Autowired
	private BaseUserDao				baseUserDao;
	@Autowired
	private CompanyVehicleDao		companyVehicleDao;
	/** 文件-服务 */
	@Autowired
	private FileManDao fileManDao;
	/** 员工报账记录-服务 */
	@Autowired
	private StaffReimburseDao staffReimburseDao;



	@Override
	public ZBaseDaoImpl<CarOilList, Long> getDao() {
		return coiDao;
	}



	@Override
	public Map<String, Object> findCoiList(ReqSrc reqsrc, String unitNum, String plateNum, String oilStation,
			String sTime, String eTime, String isCheck, String driver, String oilWay, String timeType, String page,
			String rows) {
		// //////////////////////// 排序设置-s///////////////////
		// Compositor compositor = new Compositor("id", CompositorType.DESC);
		// if (StringUtils.isNotEmpty(repeat)) {
		// compositor = new Compositor("oilMoney", CompositorType.ASC);
		// }
		// pageData.setCompositor(compositor);
		// //////////////////////// 排序设置-e///////////////////
		// //////////////////////// 查询条件-s//////////////////////////
		// List<Filtration> filtrations = new ArrayList<Filtration>();
		// filtrations.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));//
		// 当前车队
		// if (StringUtils.isNotEmpty(find)) {
		// filtrations.add(new Filtration(MatchType.LIKE, find, "plateNum"));//
		// 车牌号
		// }
		// if (StringUtils.isNotEmpty(isCheck)) {
		// filtrations.add(new Filtration(MatchType.EQ,
		// Integer.parseInt(isCheck), "isCheck"));// 审核状态
		// }
		// if (StringUtils.isNotEmpty(oilStation)) {
		// filtrations.add(new Filtration(MatchType.EQ, oilStation,
		// "oilStation"));// 加油站
		// }
		//// else if (StringUtils.isNotEmpty(cardNoCoi)) {
		//// filtrations.add(new Filtration(MatchType.EQ, cardNoCoi,
		// "oilStation"));// 加油卡号
		//// }
		// if (StringUtils.isNotEmpty(driver)) {
		// filtrations.add(new Filtration(MatchType.EQ, driver,
		// "oilDriver.uname"));// 驾驶员
		// }
		// if (StringUtils.isNotEmpty(oilWay)) {
		// filtrations.add(new Filtration(MatchType.EQ, OilWay.valueOf(oilWay),
		// "oilWay"));// 加油方式
		// }
		// if (StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)) {
		// // 时间
		// if ("0".equals(timeType)) {
		// filtrations.add(new Filtration(MatchType.GE,
		// DateUtils.strToDate("yyyy-MM-dd", sTime), "addTime"));
		// filtrations.add(new Filtration(MatchType.LE,
		// DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", eTime + " 23:59:59"),
		// "addTime"));
		// } else {
		// filtrations.add(new Filtration(MatchType.GE,
		// DateUtils.strToDate("yyyy-MM-dd", sTime), "oilDate"));
		// filtrations.add(new Filtration(MatchType.LE,
		// DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", eTime + " 23:59:59"),
		// "oilDate"));
		// }
		// }
		// /*
		// * if(StringUtils.isNotEmpty(repeat)){ filtrations.add(new
		// * Filtration(MatchType.SQL, "",
		// * " (GROUP BY oil_money HAVING COUNT(oil_money)>1) "));//价格 }
		// */
		// //////////////////////// 查询条件-e//////////////////////////
		// pageData.setFiltrations(filtrations);
		// pageData = coiDao.findPage(pageData);
		// return pageData;
		String logtxt = U.log(log, "获取-加油记录-分页列表", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (fg)
				fg = U.valPageNo(map, page, rows, "用户");

			if (fg) {
				Page<CarOilList> findCoiList = coiDao.findCoiList(reqsrc, unitNum, plateNum, oilStation, sTime, eTime,
						isCheck, driver, oilWay, timeType, page, rows);
				U.setPageData(map, findCoiList);

				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
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
	public Map<String, Object> findCarOilRepair(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String unitNum, String luname, String type) {
		String logtxt = U.log(log, "车队驾驶员-获取-车队车牌号/站点/充值卡", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			if (ReqSrc.WX == reqsrc || ReqSrc.PC_COMPANY == reqsrc) {
				if (fg) {
					if (StringUtils.isEmpty(unitNum)) {
						fg = U.setPutFalse(map, "[车队编号]不能为空");
					} else {
						unitNum = unitNum.trim();

						U.log(log, "车队编号：unitNum=" + unitNum);
					}
				}

				if (fg) {
					if (StringUtils.isEmpty(luname)) {
						fg = U.setPutFalse(map, "[驾驶员账号]不能为空");
					} else {
						luname = luname.trim();

						U.log(log, "驾驶员账号：luname=" + luname);
					}
				}
				
				Staff driver = null; 
				if(fg){ 
					driver = dlDao.getTeamDriver(unitNum, luname); 
					if(driver == null){
						fg = U.setPutFalse(map, "您不是该车队驾驶员"); 
					}
				}

				int _type = -1;
				if (fg) {
					if (StringUtils.isEmpty(type)) {
						fg = U.setPutFalse(map, "[数据类型]不能为空");
					} else {
						type = type.trim();
						if (!FV.isInteger(type)) {
							fg = U.setPutFalse(map, "[数据类型]格式错误，应为整数");
						} else {
							_type = Integer.parseInt(type);
						}

						U.log(log, "数据类型：type=" + type);
					}
				}

				// 获取-加油方式
				if (fg) {
					if (_type == 0) {
						List<Item> its = new ArrayList<Item>();
						OilWay[] ows = OilWay.values();
						for (int i = 0; i < ows.length - 1; i++) {
							if (ows[i] != OilWay.YGC_JY) {// 取消油罐车
								its.add(new Item(ows[i].name(), ows[i].getOilWayText(), i));
							}
						}

						map.put("data", its);

						fg = U.setPutFalse(map, 1, "获取[加油方式]成功");
					}
				}

				// 获取-充值卡
				if (fg) {
					if (_type == 1) {
						// 20200221修改
						hql = "from OilCardList where unitNum = ?0 and cardType = ?1 order by oilType asc";
						List<OilCardList> oilCard = oclSer.findhqlList(hql, unitNum, 0);

						List<Item> its = new ArrayList<Item>();

						for (OilCardList o : oilCard) {
							its.add(new Item(o.getId() + "", o.getCardNo(), o.getBalance()));
						}

						map.put("data", its);

						fg = U.setPutFalse(map, 1, "获取[充值卡]成功");
					}
				}

				// 获取-加油站/加气站/充电站
				if (fg) {
					if(_type == 2){
//						hql = "from OilRepairStation where unitNum = ?0 and stationType in (?1, ?2, ?3) order by stationType asc"; 
//						List<OilRepairStation> oilStation = orsSer.findhqlList(hql, unitNum, 0, 2, 3);
//				  
//						List<Item> its = new ArrayList<Item>();
//						for(OilRepairStation o : oilStation){ 
//							its.add(new Item(o.getId()+"", o.getStationName(), o.getStationAddress())); 
//						}
//					  
//						map.put("data", its);
//					  
//						fg = U.setPutFalse(map, 1, "获取[加油站]成功"); 
					}
				}

				// 获取-维修站
				if (fg) {
					if(_type == 3){ 
//						hql = "from OilRepairStation where unitNum = ?0 and stationType = ?1 order by id asc"; 
//						List<OilRepairStation> repairStation = orsSer.findhqlList(hql, unitNum, 1);
//				  
//						List<Item> its = new ArrayList<Item>();
//						for(OilRepairStation o : repairStation){ 
//							its.add(new Item(o.getId()+"", o.getStationName(), o.getStationAddress())); 
//						}
//				  
//						map.put("data", its);
//				  
//						fg = U.setPutFalse(map, 1, "获取[维修站]成功"); 
					}
				}
			} else {
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> findJyjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-加油记账-列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			fg = U.valPageNo(map, page, rows, "加油记账");
			fg = U.valSEtime(map, stime, etime, "加油时间");
			
			if(fg) {
				String unitNum = LU.getLUnitNum(request, redis);
				String luname = LU.getLUName(request, redis);
				Page<CarOilList> pd = coiDao.findPageJyjzList(reqsrc, page, rows, stime, etime, unitNum, luname);
				U.setPageData(map, pd);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> findPrevkmAndMaxkm(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String unitNum, String lname, String plateNum, String uid, String type) {
		String logtxt = U.log(log, "获取-车辆上一次公里数和最大续航里数", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			if (ReqSrc.WX == reqsrc) {
				if (fg) {
					if (StringUtils.isEmpty(lname)) {
						fg = U.setPutFalse(map, "[登录账号]不能为空");
					} else {
						lname = lname.trim();

						U.log(log, "[登录账号] lname=" + lname);
					}
				}

				if (fg) {
					if (StringUtils.isBlank(unitNum)) {
						fg = U.setPutFalse(map, "[登录车队编号]不能为空");
					}else {
						unitNum = unitNum.trim();
					}

					U.log(log, "[登录车队编号] unitNum=" + unitNum);
				}

				if (fg) {
					if (StringUtils.isEmpty(plateNum)) {
						U.log(log, "[车牌号]为空，则默认登录用户所属车队的车辆");
						hql = "select new CompanyVehicle(id, carSeats, plateNumber) from CompanyVehicle where unitNum = ?0 and baseUserId.uname=?1 order by seats asc";
						CompanyVehicle cl = clSer.findObj(hql, unitNum, lname + "%");
						if (cl != null) {
							plateNum = cl.getPlateNumber();
						} else {
							fg = U.setPutFalse(map, "登录驾驶员没有车辆");
						}
					} else {
						plateNum = plateNum.trim();

						U.log(log, "[车牌号] plateNum=" + plateNum);
					}
				}

				if (fg) {
					if (StringUtils.isEmpty(uid)) {
						U.log(log, "[加油记账修改id]为空");
					} else {
						uid = uid.trim();
						if (!FV.isLong(uid)) {
							fg = U.setPutFalse(map, "[加油记账修改id]格式错误");
						}

						U.log(log, "[加油记账修改id] uid=" + uid);
					}
				}

				int _type = 0;
				if (fg) {
					if (StringUtils.isEmpty(type)) {
						fg = U.setPutFalse(map, "[查询类型]不能为空");
					} else {
						type = type.trim();
						if (!FV.isPosInteger(type)) {
							fg = U.setPutFalse(map, "[查询类型]格式错误");
						} else {
							_type = Integer.parseInt(type);
						}

						U.log(log, "[查询类型] type=" + type);
					}
				}

				double maxkm = 0d;// 最大续航里数
				if (fg) {
					hql = "from CompanyVehicle where plateNumber = ?0 and unitNum = ?1";
					CompanyVehicle car = clSer.findObj(hql, plateNum, unitNum);
					if (car != null) {
						if (car.getMileage() > 0) {
							maxkm = car.getMileage();
						} else {
							U.log(log, "获取[最大续航里数]为" + car.getMileage());
						}
					} else {
						U.log(log, "车辆不存在");
					}
				}

				if (fg) {
					double prevkm = 0d;

					if (_type == 1) {// 加油记账
						// 获取车辆在车队中上一次的公里数
						List<CarOilList> cols = new ArrayList<CarOilList>();
						if (StringUtils.isNotBlank(uid)) {// 修改
							hql = "from CarOilList where plateNum = ?0 and unitNum = ?1 and id <> ?2 order by id desc";
							cols = coiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum, Long.parseLong(uid));
						} else {// 添加
							hql = "from CarOilList where plateNum = ?0 and unitNum = ?1 order by id desc";
							cols = coiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum);
						}

						if (cols.size() > 0) {// 存在上一条-加油记账
							prevkm = cols.get(0).getCurrentKilo();
							maxkm = MathUtils.add(prevkm, maxkm, 2);
						} else {
							U.log(log, "[" + plateNum + "]不存在加油记录");
						}
					} else if (_type == 2) {// 维修记账
						// 获取车辆在车队中上一次的公里数
						List<CarRepairList> cpais = new ArrayList<CarRepairList>();
						if (StringUtils.isNotBlank(uid)) {// 修改
							hql = "from CarRepairList where plateNum = ?0 and unitNum = ?1 and id <> ?2 order by id desc";
							cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum, Long.parseLong(uid));
						} else {// 添加
							hql = "from CarRepairList where plateNum = ?0 and unitNum = ?1 order by id desc";
							cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum);
						}

						if (cpais.size() > 0) {// 存在上一条-加油记账
							prevkm = cpais.get(0).getCurrentKilo();
							maxkm = MathUtils.add(prevkm, maxkm, 2);
						} else {
							U.log(log, "[" + plateNum + "]不存在维修记录");
						}
					}

					map.put("plateNum", plateNum);
					map.put("prevkm", prevkm);
					map.put("maxkm", maxkm);

					U.setPut(map, 1, "获取成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> addJyjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String flen, String plateNum, String currKm, String jyWay, String jyStation, 
		String jyCount, String jyMoney, String jyCard, String jyDate, String jyRemark) {
		String logtxt = U.log(log, "添加-加油记账", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			if(fg) {
				if(reqsrc != ReqSrc.WX) {
					fg = U.setPutFalse(map, "[请求来源]错误");
				}
			}
			
			BaseUser lbuser = null;
			if(fg) {
				lbuser = baseUserDao.findByField("uname", luname);
				if(lbuser == null) {
					fg = U.setPutFalse(map, "[登录用户基类]不存在");
				}else {
					U.log(log, "[登录用户名] uname="+lbuser.getUname());
				}
			}
			
			Staff driver = null;
			if(fg) {
				driver = dlDao.getTeamDriver(lunitNum, luname);
				if(driver == null) {
					fg = U.setPutFalse(map, "当前用户不是驾驶员，不能操作");
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(plateNum)){
					fg = U.setPutFalse(map, "[车牌号]不能为空");
				}else{
					plateNum = plateNum.trim();
					
					U.log(log, "车牌号：plateNum="+plateNum);
				}
			}
			
			double _currKm = 0d;
			if(fg){
				if(StringUtils.isEmpty(currKm)){
					fg = U.setPutFalse(map, "[当前公里数]不能为空");
				}else{
					currKm = currKm.trim();
					if(!FV.isDouble(currKm)){
						fg = U.setPutFalse(map, "[当前公里数]格式错误");
					}else{
						_currKm = Double.parseDouble(currKm);
					}
					
					U.log(log, "[当前公里数] currKm="+currKm);
				}
			}
			
			OilWay _jyWay = null;
			if(fg){
				if(StringUtils.isEmpty(jyWay)){
					fg = U.setPutFalse(map, "[加油方式]不能为空");
				}else{
					jyWay = jyWay.trim();
					if(!FV.isOfEnum(OilWay.class, jyWay)){
						fg = U.setPutFalse(map, "[加油方式]格式不正确");
					}else{
						_jyWay = OilWay.valueOf(jyWay);
					}
					
					U.log(log, "[加油方式] jyWay="+jyWay);
				}
			}
			
			if(fg){
				if(OilWay.YP_JY == _jyWay){// 油票加油，验证-加油站
					if(fg){
						if(StringUtils.isEmpty(jyStation)){
							fg = U.setPutFalse(map, "[加油站名称]不能为空");
						}else{
							jyStation = jyStation.trim();
							
							U.log(log, "[加油站名称] jyStation="+jyStation);
						}
					}
				}else if(OilWay.CZK_JY == _jyWay){// 充值卡加油-验证加油卡
					if(fg){
						if(StringUtils.isEmpty(jyCard)){
							fg = U.setPutFalse(map, "[加油卡号]不能为空");
						}else{
							jyCard = jyCard.trim();
							
							U.log(log, "[加油卡号] jyCard="+jyCard);
						}
					}
				}
			}
			
			Date _jyDate = null;
			if(fg){
				if(StringUtils.isEmpty(jyDate)){
					fg = U.setPutFalse(map, "[加油日期]不能为空");
				}else{
					jyDate = jyDate.trim();
					if(!FV.isDate(jyDate)){
						fg = U.setPutFalse(map, "[加油日期]格式不正确");
					}else{
						_jyDate = DateUtils.strToDate(jyDate);
					}
					
					U.log(log, "[加油日期] jyDate="+jyDate);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jyRemark)){
					U.log(log, "[加油备注]为空");
				}else{
					jyRemark = jyRemark.trim();
					if(jyRemark.length() > 40){
						fg = U.setPutFalse(map, "[备注]最多填写40个字");
					}
					
					U.log(log, "[加油备注] jyRemark="+jyRemark);
				}
			}
			
			// 加油数量, 加油金额
			double _jyCount = 0d, _jyMoney = 0d;
			if(fg){
				if(OilWay.YP_JY == _jyWay) {
					if(StringUtils.isEmpty(jyCount) && StringUtils.isEmpty(jyMoney)) {
						fg = U.setPutFalse(map, "[加油数量]或[加油金额]至少填写一个");
					}else {
						if(fg){
							if(StringUtils.isEmpty(jyCount)){
								U.log(log, "[加油数量]为空");
							}else{
								jyCount = jyCount.trim();
								if(!FV.isDouble(jyCount)){
									fg = U.setPutFalse(map, "[加油数量]格式错误，应为正数");
								}else{
									_jyCount = Double.parseDouble(jyCount);
								}
								
								U.log(log, "[加油数量] jyCount="+jyCount);
							}
						}
						
						if(fg) {
							if(StringUtils.isEmpty(jyMoney)){
								U.log(log, "[加油金额]为空");
							}else{
								jyMoney = jyMoney.trim();
								if(!FV.isDouble(jyMoney)){
									fg = U.setPutFalse(map, "[加油金额]格式错误，应为正数");
								}else{
									_jyMoney = Double.parseDouble(jyMoney);
								}
								
								U.log(log, "[加油金额]jyMoney="+jyMoney);
							}
						}
					}
				}else {
					if(fg){
						if(StringUtils.isEmpty(jyMoney)){
							if (StringUtils.isNotEmpty(jyCard)) {// 加油卡不为空，则加油金额也不能为空
								fg = U.setPutFalse(map, "[加油金额]不能为空");
							}else{
								U.log(log, "[加油金额]为空");
							}
						}else{
							jyMoney = jyMoney.trim();
							if(!FV.isDouble(jyMoney)){
								fg = U.setPutFalse(map, "[加油金额]格式错误，应为正数");
							}else{
								_jyMoney = Double.parseDouble(jyMoney);
							}
							
							U.log(log, "[加油金额] jyMoney="+jyMoney);
						}
					}
				}
			}
			
			CarOilList lastOil = null;
			if(fg){
				hql = "from CarOilList where plateNum = ?0 and unitNum = ?1 order by id desc";
				List<CarOilList> lastCols = coiDao.hqlListFirstMax(hql, 0, 1, plateNum, lunitNum);
				if(lastCols.size() == 0){
					U.log(log, "是第一次添加");
				}else{
					lastOil = lastCols.get(0);
					U.log(log, "非第一次添加");
				}
			}
			
			if(fg){
				if(lastOil != null){// 非第一次添加才验证
					if(fg){
						if (_currKm <= lastOil.getCurrentKilo()) {
							fg = U.setPutFalse(map, "当前公里数必须大于上次公里数，上次为" + lastOil.getCurrentKilo() + "公里");
						}
					}
					
					if(fg){
						hql = "from CompanyVehicle where plateNumber = ?0 and unitNum = ?1";
						CompanyVehicle car = companyVehicleDao.findCompanyCar(plateNum, lunitNum);
						if(car != null){
							// 有续航里程判断是否超过了续航里程
							if (car.getMileage() > 0 && _currKm > MathUtils.add(lastOil.getCurrentKilo(), car.getMileage(), 2)) {
								fg = U.setPutFalse(map, "当前公里数不能超过最大续航里程，最大为" + MathUtils.add(lastOil.getCurrentKilo(), car.getMileage(), 2) + "公里");
							}
						}
					}
				}
			}
			
			if(fg) {
				if(StringUtils.isBlank(flen)) {
					fg = U.setPutFalse(map, "[上传文件个数]不能为空");
				}else {
					flen = flen.trim();
					if(!FV.isInteger(flen)) {
						fg = U.setPutFalse(map, "[上传文件个数]格式错误");
					}else {
						int _flen = Integer.parseInt(flen);
						if(_flen <= 0) {
							fg = U.setPutFalse(map, "请上传加油凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
				}
			}
			
			if(fg) {
				CarOilList col = new CarOilList();
				col.setReqsrc(reqsrc);
				col.setUnitNum(lunitNum);
				col.setOilDriver(lbuser);
				col.setPlateNum(plateNum);
				col.setCurrentKilo(_currKm);
				col.setOilWay(_jyWay);
				col.setOilStation(jyStation);
				col.setOilRise(_jyCount);
				col.setOilMoney(_jyMoney);
				col.setOilDate(_jyDate);
				col.setOilRemark(jyRemark);
				/******** 更新上一次加油油耗 *******/
				if (lastOil != null) {// 非第一次添加
					col.setLastKilo(lastOil.getCurrentKilo());// 添加才更新上次公里数
					// 油耗=上一次加油量/(当前公里数-上一次公里数)
					double oilWear = MathUtils.div(lastOil.getOilRise(), MathUtils.sub(_currKm, lastOil.getCurrentKilo(), 2), 2);
					lastOil.setOilWear(MathUtils.mul(oilWear, 100, 2));// 百公里油耗
					coiDao.update(lastOil);
					U.log(log, "更新上一次加油油耗-完成");
				}
				/******** 更新上一次加油油耗 *******/
				col.setAddTime(new Date());
				col.setIsCheck(0);
				// 设置操作信息
				col.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
				coiDao.save(col);
				U.log(log, "添加-加油记账记录-完成");
				
				// 油罐车和现金才加入记账报销，油票和充值卡在预存费里面审核
				if(col.getOilWay().equals(OilWay.YGC_JY) || col.getOilWay().equals(OilWay.XJ_JY)){
					StaffReimburse sr = new StaffReimburse();
					sr.setUnitNum(col.getUnitNum());
//					sr.setReimUserId(lbuser);
					sr.setDeptId(driver.getDeptId());
					sr.setRemark(col.getOilRemark());
					sr.setJzType(JzType.JYJZ);
//					sr.setJzDate(col.getOilDate());
					sr.setPayMoney(col.getOilMoney());
					sr.setIsCheck(0);
					sr.setReimVoucherUrl(col.getOilVoucherUrl());
					sr.setReqsrc(reqsrc);
					sr.setOperNote(col.getOperNote());
					sr.setAddTime(col.getAddTime());
//					sr.setDat(col.getId()+"");// 保存加油记账id
					staffReimburseDao.save(sr);
					U.log(log, "添加-员工报账记录-完成");
				}
				
				// 将加油记账id传给前端
				map.put("uid", col.getId());
				
				U.setPut(map, 1, "添加-加油记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> updJyjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String luname, String uid, String flen, String currKm, String jyWay, String jyStation, 
		String jyCount, String jyMoney, String jyCard, String jyDate, String jyRemark) {
		String logtxt = U.log(log, "修改-加油记账", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			if(fg) {
				if(reqsrc != ReqSrc.WX) {
					fg = U.setPutFalse(map, "[请求来源]错误");
				}
			}
			
			BaseUser lbuser = null;
			if(fg) {
				lbuser = baseUserDao.findByField("uname", luname);
				if(lbuser == null) {
					fg = U.setPutFalse(map, "[登录用户基类]不存在");
				}else {
					U.log(log, "[登录用户名] uname="+lbuser.getUname());
				}
			}
			
			Staff driver = null;
			if(fg) {
				driver = dlDao.getTeamDriver(lunitNum, luname);
				if(driver == null) {
					fg = U.setPutFalse(map, "当前用户不是驾驶员，不能操作");
				}
			}
			
			CarOilList col = null;
			if(fg) {
				if(StringUtils.isBlank(uid)) {
					fg = U.setPutFalse(map, "[加油记账id]不能为空");
				}else {
					uid = uid.trim();
					if(!FV.isLong(uid)) {
						fg = U.setPutFalse(map, "[加油记账id]格式错误");
					}else {
						col = coiDao.findByField("id", Long.parseLong(uid));
						if(col == null) {
							fg = U.setPutFalse(map, "[加油记账]不存在");
						}if(col.getIsCheck() != 0){
							fg = U.setPutFalse(map, "该[加油记账]已审核，不能修改");
						}
					}
					
					U.log(log, "[加油记账id] uid="+uid);
				}
			}
			
			if(fg) {
				if(StringUtils.isBlank(flen)) {
					fg = U.setPutFalse(map, "[上传文件个数]不能为空");
				}else {
					flen = flen.trim();
					if(!FV.isInteger(flen)) {
						fg = U.setPutFalse(map, "[上传文件个数]格式错误");
					}else {
						int _flen = Integer.parseInt(flen);
						if(_flen <= 0) {
							fg = U.setPutFalse(map, "请上传加油凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
				}
			}
			
			double _currKm = 0d;
			if(fg){
				if(StringUtils.isEmpty(currKm)){
					fg = U.setPutFalse(map, "[当前公里数]不能为空");
				}else{
					currKm = currKm.trim();
					if(!FV.isDouble(currKm)){
						fg = U.setPutFalse(map, "[当前公里数]格式错误，应为正数");
					}else{
						_currKm = Double.parseDouble(currKm);
					}
					
					U.log(log, "[当前公里数] currKm="+currKm);
				}
			}
			
			CarOilList lastOil = null;// 获取当前修改数据的下一条数据，为了修改该记录的上一条车辆公里数
			if(fg) {
				// 公里数变化了
				if(_currKm != col.getCurrentKilo()) {
					// 修改公里数时，公里数只能是顺序添加的自身前后两条数据的公里数之间
					Object[] minMax = staffReimburseDao.getMinMaxKm(col, 3);
					double min = Double.parseDouble(minMax[0].toString());
					double max = Double.parseDouble(minMax[1].toString());
					long nextId = Long.parseLong(minMax[2].toString());
					
					if(_currKm <= min || _currKm >= max) {
						fg = U.setPutFalse(map, -2, "公里数应该在"+(int)min+"到"+(int)max+"之间");
					}
					
					if(nextId != 0) {// 存在下一条记录
						lastOil = coiDao.findByField("id", nextId);
					}
				}
			}
			
			OilWay _jyWay = null;
			if(fg){
				if(StringUtils.isEmpty(jyWay)){
					fg = U.setPutFalse(map, "[加油方式]不能为空");
				}else{
					jyWay = jyWay.trim();
					if(!FV.isOfEnum(OilWay.class, jyWay)){
						fg = U.setPutFalse(map, "[加油方式]格式不正确");
					}else{
						_jyWay = OilWay.valueOf(jyWay);
					}
					
					U.log(log, "[加油方式] jyWay="+jyWay);
				}
			}
			
			if(fg){
				if(OilWay.YP_JY == _jyWay){// 油票加油，验证-加油站
					if(fg){
						if(StringUtils.isEmpty(jyStation)){
							fg = U.setPutFalse(map, "[加油站名称]不能为空");
						}else{
							jyStation = jyStation.trim();
							
							U.log(log, "[加油站名称] jyStation="+jyStation);
						}
					}
				}else if(OilWay.CZK_JY == _jyWay){// 充值卡加油-验证加油卡
					if(fg){
						if(StringUtils.isEmpty(jyCard)){
							fg = U.setPutFalse(map, "[加油卡号]不能为空");
						}else{
							jyCard = jyCard.trim();
							
							U.log(log, "[加油卡号] jyCard="+jyCard);
						}
					}
				}
			}
			
			Date _jyDate = null;
			if(fg){
				if(StringUtils.isEmpty(jyDate)){
					fg = U.setPutFalse(map, "[加油日期]不能为空");
				}else{
					jyDate = jyDate.trim();
					if(!FV.isDate(jyDate)){
						fg = U.setPutFalse(map, "[加油日期]格式不正确");
					}else{
						_jyDate = DateUtils.strToDate(jyDate);
					}
					
					U.log(log, "[加油日期] jyDate="+jyDate);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jyRemark)){
					U.log(log, "[加油备注]为空");
				}else{
					jyRemark = jyRemark.trim();
					if(jyRemark.length() > 40){
						fg = U.setPutFalse(map, "[备注]最多填写40个字");
					}
					
					U.log(log, "[加油备注] jyRemark="+jyRemark);
				}
			}
			
			// 加油数量, 加油金额
			double _jyCount = 0d, _jyMoney = 0d;
			if(fg){
				if(OilWay.YP_JY == _jyWay) {
					if(StringUtils.isEmpty(jyCount) && StringUtils.isEmpty(jyMoney)) {
						fg = U.setPutFalse(map, "[加油数量]或[加油金额]至少填写一个");
					}else {
						if(fg){
							if(StringUtils.isEmpty(jyCount)){
								U.log(log, "[加油数量]为空");
							}else{
								jyCount = jyCount.trim();
								if(!FV.isDouble(jyCount)){
									fg = U.setPutFalse(map, "[加油数量]格式错误，应为正数");
								}else{
									_jyCount = Double.parseDouble(jyCount);
								}
								
								U.log(log, "[加油数量] jyCount="+jyCount);
							}
						}
						
						if(fg) {
							if(StringUtils.isEmpty(jyMoney)){
								U.log(log, "[加油金额]为空");
							}else{
								jyMoney = jyMoney.trim();
								if(!FV.isDouble(jyMoney)){
									fg = U.setPutFalse(map, "[加油金额]格式错误，应为正数");
								}else{
									_jyMoney = Double.parseDouble(jyMoney);
								}
								
								U.log(log, "[加油金额]jyMoney="+jyMoney);
							}
						}
					}
				}else {
					if(fg){
						if(StringUtils.isEmpty(jyMoney)){
							if (StringUtils.isNotEmpty(jyCard)) {// 加油卡不为空，则加油金额也不能为空
								fg = U.setPutFalse(map, "[加油金额]不能为空");
							}else{
								U.log(log, "[加油金额]为空");
							}
						}else{
							jyMoney = jyMoney.trim();
							if(!FV.isDouble(jyMoney)){
								fg = U.setPutFalse(map, "[加油金额]格式错误，应为正数");
							}else{
								_jyMoney = Double.parseDouble(jyMoney);
							}
							
							U.log(log, "[加油金额] jyMoney="+jyMoney);
						}
					}
				}
			}
			
			if(fg) {
				col.setCurrentKilo(_currKm);
				col.setOilWay(_jyWay);
				col.setOilStation(jyStation);
				col.setOilRise(_jyCount);
				col.setOilMoney(_jyMoney);
				col.setOilDate(_jyDate);
				col.setOilRemark(jyRemark);
				/******** 更新上一次加油油耗 *******/
				if (lastOil != null) {// 非第一次添加
					col.setLastKilo(lastOil.getCurrentKilo());// 更新上次公里数
					// 油耗=上一次加油量/(当前公里数-上一次公里数)
					double oilWear = MathUtils.div(lastOil.getOilRise(), MathUtils.sub(_currKm, lastOil.getCurrentKilo(), 2), 2);
					lastOil.setOilWear(MathUtils.mul(oilWear, 100, 2));// 百公里油耗
					coiDao.update(lastOil);
					U.log(log, "更新上一次加油油耗-完成");
				}
				/******** 更新上一次加油油耗 *******/
				col.setIsCheck(0);
				// 设置操作信息
				col.setOperNote(col.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
				coiDao.update(col);
				U.log(log, "修改-加油记账记录-完成");
				
				// 获取-对应员工记账记录
				hql = "from StaffReimburse where unitNum = ?0 and reimUserId.uname = ?1 and dat = ?2 and jzType = ?3";
				StaffReimburse sr = staffReimburseDao.findObj(hql, col.getUnitNum(), luname, col.getId()+"", JzType.JYJZ);
				if(sr != null) {
					// 油罐车和现金才加入记账报销，油票和充值卡在预存费里面审核
					if(col.getOilWay().equals(OilWay.YGC_JY) || col.getOilWay().equals(OilWay.XJ_JY)){
						sr.setRemark(col.getOilRemark());
						sr.setPayMoney(col.getOilMoney());
						sr.setIsCheck(0);
						sr.setReimVoucherUrl(col.getOilVoucherUrl());
						sr.setOperNote(sr.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
						staffReimburseDao.update(sr);
						U.log(log, "修改-员工报账记录-完成");
					}else {
						// 删除对应员工记账记录
						staffReimburseDao.delete(sr);
						U.log(log, "删除-对应员工记账记录");
					}
				}else {
					// 油罐车和现金才加入记账报销，油票和充值卡在预存费里面审核
					if(col.getOilWay().equals(OilWay.YGC_JY) || col.getOilWay().equals(OilWay.XJ_JY)){
						sr = new StaffReimburse();
						sr.setUnitNum(col.getUnitNum());
//						sr.setReimUserId(lbuser);
						sr.setDeptId(driver.getDeptId());
						sr.setRemark(col.getOilRemark());
//						sr.setJzDate(col.getOilDate());
						sr.setPayMoney(col.getOilMoney());
						sr.setIsCheck(0);
						sr.setJzType(JzType.JYJZ);
						sr.setReimVoucherUrl(col.getOilVoucherUrl());
						sr.setReqsrc(reqsrc);
						sr.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
						sr.setAddTime(col.getAddTime());
//						sr.setDat(col.getId()+"");// 保存加油记账id
						staffReimburseDao.save(sr);
						U.log(log, "重新添加-员工报账记录-完成");
					}
				}
				
				// 前端需要此id，保存图片
				map.put("uid", col.getId());
				
				U.setPut(map, 1, "修改-加油记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> delJyjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String lname, String did) {
		String logtxt = U.log(log, "师傅删除-加油记录/后台删除", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			CarOilList col = null;
			String unitNum = null, uname = null;
			if (fg) {
				if (StringUtils.isEmpty(did)) {
					fg = U.setPutFalse(map, "[加油记录id]不能为空");
				} else {
					did = did.trim();
					if (!FV.isLong(did)) {
						fg = U.setPutFalse(map, "[加油记录id]格式错误，应为long类型");
					} else {
						col = coiDao.find(Long.parseLong(did));
						if (col == null) {
							fg = U.setPutFalse(map, "该[加油记录]不存在");
						}else {
							unitNum = col.getUnitNum();
							uname = col.getOilDriver().getUname();
						}
					}

					U.log(log, "加油记录id：did=" + did);
				}
			}
			
			StaffReimburse sr = null;
			if(fg) {
				hql = "from StaffReimburse where unitNum = ?0 and reimUserId.uname = ?1 and dat = ?2";
				sr = staffReimburseDao.findObj(hql, unitNum, uname, col.getId()+"");
				if(sr == null) {
					U.log(log, "未添加加油记账对应员工报账记录");
				}else {
					U.log(log, "存在加油记账对应员工报账记录");
				}
			}

			if (ReqSrc.WX == reqsrc) { // 微信端才判断
				if (fg) {
					if (StringUtils.isEmpty(lname)) {
						fg = U.setPutFalse(map, "[登录用户]不能为空");
					} else {
						lname = lname.trim();

						U.log(log, "登录用户：lname=" + lname);
					}
				}

				if (fg) {
					if (!uname.contains(lname)) {
						fg = U.setPutFalse(map, "删除失败，该[加油记录]不是您添加的");
					} else if (!unitNum.equals(lunitNum)) {
						fg = U.setPutFalse(map, "删除失败，该[加油记录]不属于当前车队");
					} else {
						if (col.getIsCheck() != -1 && col.getIsCheck() != 0) {
							fg = U.setPutFalse(map, "删除失败，该[加油记录]已审核");
						}
					}
				}
				
				if(fg) {
					coiDao.delete(col);
					U.log(log, "删除-加油记账-记录完成");
					
					// 删除加油记账对应凭证记录及图片
					fileManDao.delJzbxFile(unitNum, uname, FileType.JYJZ_IMG, col.getId()+"");
					
					if(sr != null) {
						staffReimburseDao.delete(sr);
						U.log(log, "删除-加油记账-对应员工记账记录-完成");
					}
					
					U.setPut(map, 1, "删除-加油记账记录-成功");
				}
			}else if (ReqSrc.PC_COMPANY == reqsrc) { // 后台
				if(fg) {
					if (!unitNum.equals(lunitNum)) {
						fg = U.setPutFalse(map, "删除失败，该[加油记录]不属于当前车队");
					}
				}
				
				if (fg) {
					if (col.getIsCheck() != -1 && col.getIsCheck() != 0) {
						fg = U.setPutFalse(map, "删除失败，该[加油记录]状态不能删除");
					}
				}
				
				if (fg) {
					this.operCoi(did, null);

					U.setPut(map, 1, "删除成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public int operCoi(String updId, CarOilList coi) {
		try {
			if (coi != null) {
				if (StringUtils.isNotEmpty(updId)) {
					coiDao.update(coi);
				} else {
					coiDao.save(coi);
				}
			} else {
				if (StringUtils.isNotEmpty(updId)) {
					CarOilList delcoi = coiDao.findByField("id", Long.parseLong(updId));
					String hql = "from ReimburseList where reimType=3 and orderNum=?";
					ReimburseList reim = reimSer.findObj(hql, updId + "");// 删除报销记录
					if (reim != null) {
						reimSer.delete(reim);
					}
					/*
					 * if(StringUtils.isNotBlank(delcoi.getOilVoucherUrl())){
					 * fmSer.delFile(Util.MFILEURL,
					 * Long.valueOf(delcoi.getOilVoucherId()));//删除凭证图片 }
					 */
					coiDao.delete(delcoi);
				} else {
					U.log(log, "删除失败，【加油记账id为空】");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}



	@Override
	public int checkCoi(String checkId, int state, String note, String operator, String remark, double currMoney,
			int isCash) {
		CarOilList coi = coiDao.find(Long.parseLong(checkId));
		if (coi != null) {
			if (state != -1) {
				if (state == 3) {// 核销
					if (isCash == 1) {// 现金付款并且选择电子钱包,将现金金额加入师傅钱包-提现
						CusWallet cw = cwSer.findByField("baseUserId.uname", coi.getOilDriver().getUname());
						if (cw != null) {
							cw.setCashBalance(MathUtils.add(cw.getCashBalance(), currMoney, 2));
							cwSer.update(cw);
							WalletList wl = new WalletList();
							wl.setcName(cw.getcName());
							wl.setAmoney(currMoney);
							wl.setCashBalance(cw.getCashBalance());
							wl.setAssist(coi.getId() + "");
							wl.setAtype(1);
							wl.setType(0);
							wl.setStatus(0);
							wl.setNote("现金加油");
							wl.setAtime(new Date());
							wlSer.save(wl);
						}
					}
					if (MathUtils.add(coi.getVerificationMoney(), currMoney, 2) >= coi.getOilMoney()) {// 核销完金额
						coi.setIsCheck(3);
					}
					coi.setVerificationMoney(MathUtils.add(coi.getVerificationMoney(), currMoney, 2));// 加入已核销金额
				} else {
					if ((coi.getOilWay().equals(OilWay.YP_JY) || coi.getOilWay().equals(OilWay.CZK_JY)) && state == 1) {// 如果是审核油票/充值卡加油直接为核销状态
						coi.setIsCheck(3);
						if (StringUtils.isNotEmpty(coi.getOilRemark())) {
							coi.setOilRemark(coi.getOilRemark() + "(" + DateUtils.DateToStr(new Date()) + "&nbsp;"
									+ note + ",操作员:" + operator + "[核销" + coi.getOilWay() + "])");
						} else {
							coi.setOilRemark(DateUtils.DateToStr(new Date()) + "&nbsp;" + note + ",操作员:" + operator
									+ "[核销" + coi.getOilWay() + "]");
						}
						// 找到预存加油站扣除相应加油量或余额
						String hql = "from OilCardList where unitNum=? and cardNo=?";
						OilCardList ocl = oclSer.findObj(hql, coi.getUnitNum(), coi.getOilStation());
						if (ocl != null) {
							if (ocl.getOilRise() > 0) {// 扣除加油量
								ocl.setOilRise(MathUtils.sub(ocl.getOilRise(), coi.getOilRise(), 2));
							} else {// 扣除余额
								ocl.setBalance(MathUtils.sub(ocl.getBalance(), coi.getOilMoney(), 2));
							}
							oclSer.update(ocl);
						}
					} else {
						coi.setIsCheck(state);
						if (StringUtils.isNotEmpty(coi.getOilRemark())) {
							coi.setOilRemark(coi.getOilRemark() + "(" + DateUtils.DateToStr(new Date()) + "&nbsp;"
									+ note + ",操作员:" + operator + "[" + remark + "])");
						} else {
							coi.setOilRemark(DateUtils.DateToStr(new Date()) + "&nbsp;" + note + ",操作员:" + operator
									+ "[" + remark + "]");
						}
					}
				}
			} else {
				coi.setIsCheck(-1);
				coi.setOilRemark(
						DateUtils.DateToStr(new Date()) + "&nbsp;" + note + ",操作员:" + operator + "[" + remark + "]");
			}
			coiDao.update(coi);
			return 1;
		}
		return 0;
	}



	@Override
	public Map<String, Object> findJyjzDetail(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String luname, String id) {
		String logtxt = U.log(log, "获取-加油记账-详情", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(luname)) {
					fg = U.setPutFalse(map, "[登录用户名]不能为空");
				} else {
					luname = luname.trim();

					U.log(log, "[登录用户名] luname=" + luname);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[加油记账对象id]不能为空");
				} else {
					id = id.trim();
					if (!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[加油记账id]格式错误");
					}

					U.log(log, "[加油记账id] id=" + id);
				}
			}

			CarOilList col = null;
			if (fg) {
				col = coiDao.findByField("id", Long.parseLong(id));
				if (col == null) {
					fg = U.setPutFalse(map, "[加油记账记录]不存在");
				} else if (!col.getOilDriver().getUname().contains(luname)) {
					fg = U.setPutFalse(map, "该[加油记账]不是您添加的");
				}
			}
			
			if (fg) {
				map.put("data", col);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);

				U.setPut(map, 1, "获取[加油记账记录]成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> cancelOil(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String cId) {
		String logtxt = U.log(log, "后台财务-撤销加油记录", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (ReqSrc.PC_COMPANY == reqsrc) {
				CarOilList obj = null;
				if (fg) {
					if (StringUtils.isEmpty(cId)) {
						fg = U.setPutFalse(map, "[记录id]不能为空");
					} else {
						cId = cId.trim();
						if (FV.isLong(cId)) {
							obj = coiDao.find(Long.parseLong(cId));
							if (obj == null) {
								fg = U.setPutFalse(map, "该记录不存在");
							}
						}

						U.log(log, "[撤销记录id] id=" + cId);
					}
				}
				if (fg) {
					if (obj.getIsCheck() == 0) {
						fg = U.setPutFalse(map, "该记录未审核，不能撤销");
					}
				}
				if (fg) {
					Map<String, Object> mapdel = new HashMap<String, Object>();
					// mapdel=tabSer.delTab(ReqSrc.PC_COMPANY, request, null,
					// obj.getOperMark());
					if (!"1".equals(mapdel.get("code").toString())) {
						U.setPut(map, 0, mapdel.get("msg").toString());
					} else {
						U.setPut(map, 1, "操作成功");
					}
					/*
					 * //找到预存加油站返回相应加油量或余额 String
					 * hql="from OilCardList where unitNum=? and cardNo=?";
					 * OilCardList ocl = oclSer.findObj(hql,
					 * obj.getUnitNum(),obj.getOilStation()); if(ocl!=null){
					 * if(ocl.getOilRise()>0){//扣除加油量
					 * ocl.setOilRise(MathUtils.add(ocl.getOilRise(),
					 * obj.getOilRise(), 2)); }else{//扣除余额 double
					 * oilMoney=MathUtils.mul(obj.getOilRise(),
					 * ocl.getOilPrice(), 2);
					 * ocl.setBalance(MathUtils.add(ocl.getBalance(), oilMoney,
					 * 2)); } oclSer.update(ocl); } obj.setIsCheck(0);
					 * obj.setRemark(obj.getRemark() + "("+
					 * DateUtils.DateToStr(new Date()) + "&nbsp;操作员:"+
					 * WebUtil.getLUser(request).getRealName()+ "[撤销])");
					 * coiDao.update(obj);
					 */
				}
			} else {
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> addCarOil(ReqSrc reqSrc, HttpServletRequest request, HttpServletResponse response,
			String unitNum, JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "加油记录表-添加", reqSrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			CarOilList carOilList = JSONObject.toJavaObject(jsonObject, CarOilList.class);
			String uname = jsonObject.getString("uname");
			BaseUser oilDriver = baseUserDao.findByUname(uname);
			String plateNum = carOilList.getPlateNum();


			
			if (fg) {
				if (null == oilDriver) {
					U.logFalse(log, "加油记录表-添加-失败-oilDriver为null");
					fg = U.setPutFalse(map, 0, "添加失败,获取加油账号失败");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(unitNum)) {
					U.logFalse(log, "加油记录表-添加-失败-unitNum为空");
					fg = U.setPutFalse(map, 0, "添加失败,获取单位编号失败");
				}
			}

			if (fg) {
				CompanyVehicle companyVehicle = companyVehicleDao.findByField("plateNumber", plateNum);
				if (null == companyVehicle) {
					U.logFalse(log, "加油记录表-添加-失败-车辆信息获取失败");
					fg = U.setPutFalse(map, 0, "添加失败,车辆信息获取失败");
				}
				else{
					Float mileage = companyVehicle.getMileage();
					double currentKilo = carOilList.getCurrentKilo();
					double lastKilo = carOilList.getLastKilo();
					if (currentKilo > lastKilo + (double)mileage) {
						U.logFalse(log, "加油记录表-添加-失败-行驶里程大于续航里程");
						fg = U.setPutFalse(map, 0, "添加失败,行驶里程大于续航里程");
					}
				}
			}

			if (fg) {

				carOilList.setOilDriver(oilDriver);
				carOilList.setAddTime(new Date());
				carOilList.setIsCheck(0);
				carOilList.setReqsrc(reqSrc);
				carOilList.setUnitNum(unitNum);
				carOilList.setVerificationMoney(0);
				coiDao.save(carOilList);
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}
	
	@Override
	public Map<String, Object> findCarJzDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lteamNo, String luname, String type) {
		String logtxt = U.log(log, "获取-车辆记账数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isBlank(lteamNo)) {
					fg = U.setPutFalse(map, "[登录车队编号]不能为空");
				}else {
					lteamNo = lteamNo.trim();
					
					U.log(log, "[登录车队编号] lteamNo="+lteamNo);
				}
			}
			
			if(fg) {
				if(StringUtils.isBlank(luname)) {
					fg = U.setPutFalse(map, "[登录用户名]不能为空");
				}else {
					luname = luname.trim();
					
					U.log(log, "[登录用户名] luname="+luname);
				}
			}
			
			int _type = -1;
			if(fg) {
				if(StringUtils.isBlank(type)) {
					fg = U.setPutFalse(map, "[查询数据类型]不能为空");
				}else {
					type = type.trim();
					if(!FV.isInteger(type)) {
						fg = U.setPutFalse(map, "[查询数据类型]格式错误");
					}else {
						_type = Integer.parseInt(type);
					}
					
					U.log(log, "[查询数据类型] type="+type);
				}
			}
			
			if(fg) {
				map = coiDao.findCarJzDat(lteamNo, luname, _type);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
