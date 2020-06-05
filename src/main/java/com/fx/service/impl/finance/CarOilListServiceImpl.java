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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.OilWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.finance.CarOilListDao;
import com.fx.dao.finance.CarRepairListDao;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CusWallet;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WalletList;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.finance.CarOilList;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.OilCardList;
import com.fx.entity.finance.ReimburseList;
import com.fx.service.company.CompanyVehicleService;
import com.fx.service.company.FileService;
import com.fx.service.company.StaffService;
import com.fx.service.cus.CusWalletService;
import com.fx.service.cus.WalletListService;
import com.fx.service.finance.CarOilListService;
import com.fx.service.finance.OilCardListService;
import com.fx.service.finance.ReimburseListService;
import com.fx.service.wxdat.YMessageService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CarOilListServiceImpl extends BaseServiceImpl<CarOilList, Long> implements CarOilListService {
	/** 日志记录 */
	private Logger					log	= LogManager.getLogger(this.getClass());
	@Autowired
	private RedisUtil				redis;

	/** 消息通知-服务 */
	@Autowired
	private YMessageService			ymSer;
	@Autowired
	private CarOilListDao			coiDao;
	/** 文件管理-服务 */
	@Autowired
	private FileService				fmSer;
	/** 驾驶员-服务 */
	@Autowired
	private StaffService			dlSer;
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
			String unitNum, String driver, String type) {
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
					if (StringUtils.isEmpty(driver)) {
						fg = U.setPutFalse(map, "[驾驶员账号]不能为空");
					} else {
						driver = driver.trim();
						if (!FV.isPhone(driver)) {
							fg = U.setPutFalse(map, "[驾驶员账号]格式错误，应为手机号");
						}

						U.log(log, "驾驶员账号：driver=" + driver);
					}
				}

				/*
				 * DriverList dl = null; if(fg){ dl =
				 * dlSer.findDriverOfCarTeam(driver, unitNum); if(dl == null){
				 * fg = U.setPutFalse(map, "您不是该车队驾驶员"); }else{
				 * map.put("unitNum", dl.getUnitNum()); } }
				 */

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
						/*
						 * hql =
						 * "from OilCardList where unitNum = ? and cName like ? order by oilType asc"
						 * ; List<OilCardList> oilCard = oclSer.findhqlList(hql,
						 * dl.getUnitNum(), "%"+dl.getPhone() + "%");
						 */
						// 20200221修改
						hql = "from OilCardList where unitNum = ? and cardType=0 order by oilType asc";
						List<OilCardList> oilCard = oclSer.findhqlList(hql, LU.getLUnitNum(request, redis));

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
					/*
					 * if(_type == 2){ hql =
					 * "from OilRepairStation where unitNum = ? and stationType in (?,?,?) order by stationType asc"
					 * ; List<OilRepairStation> oilStation =
					 * orsSer.findhqlList(hql, dl.getUnitNum(), 0, 2, 3);
					 * 
					 * List<Item> its = new ArrayList<Item>();
					 * 
					 * for(OilRepairStation o : oilStation){ its.add(new
					 * Item(o.getId()+"", o.getStationName(),
					 * o.getStationAddress())); }
					 * 
					 * map.put("data", its);
					 * 
					 * fg = U.setPutFalse(map, 1, "获取[加油站]成功"); }
					 */
				}

				// 获取-维修站
				if (fg) {
					/*
					 * if(_type == 3){ hql =
					 * "from OilRepairStation where unitNum = ? and stationType = ? order by id asc"
					 * ; List<OilRepairStation> repairStation =
					 * orsSer.findhqlList(hql, dl.getUnitNum(), 1);
					 * 
					 * List<Item> its = new ArrayList<Item>();
					 * 
					 * for(OilRepairStation o : repairStation){ its.add(new
					 * Item(o.getId()+"", o.getStationName(),
					 * o.getStationAddress())); }
					 * 
					 * map.put("data", its);
					 * 
					 * fg = U.setPutFalse(map, 1, "获取[维修站]成功"); }
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
	public Page<CarOilList> findCoiList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-加油记账-分页列表");

		Page<CarOilList> pd = new Page<CarOilList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.WX == reqsrc) {// 移动端
				Customer cus = LU.getLUSER(request, redis);
				WxBaseUser wx = LU.getLWx(request, redis);
				String unitNum = wx.getCompanyNum();

				if (CusRole.TEAM_DRIVER == wx.getLgRole()) {// 驾驶员
					//////////////////// --默认排序--//////////////////////////
					// 加油日期-倒序
					comps.add(new Compositor("oilDate", CompositorType.DESC));
					/////////////////// --条件--begin//////////////////////////
					// 指定查询[单位]
					filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));

					// 指定查询加油用户/手机号
					List<Filtration> flor = new ArrayList<Filtration>();
					flor.add(new Filtration(MatchType.LIKE, cus.getBaseUserId().getUname(), "baseUserId.uname"));
					flor.add(new Filtration(MatchType.LIKE, cus.getBaseUserId().getPhone(), "baseUserId.phone"));
					filts.add(new Filtration(MatchType.OR, flor, ""));

					// 查询-指定[加油日期]时间段
					if (StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)) {
						List<Filtration> fland = new ArrayList<Filtration>();
						fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "oilDate"));
						fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "oilDate"));
						filts.add(new Filtration(MatchType.AND, fland, ""));
					}
					/////////////////// --条件--end////////////////////////////
				} else {
					U.log(log, "请求[用户角色]不存在");
					filts.add(new Filtration(MatchType.ISNULL, null, "id"));
				}
			} else {// 查询id为空的数据（实际是没有这样的数据，因此会返回空集合）
				U.log(log, "数据[请求来源]不存在");
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}
			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = coiDao.findPageByOrders(pd); // 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}

		return pd;
	}



	@Override
	public Map<String, Object> findPrevkmAndMaxkm(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, String lname, String plateNum, String uid, String type) {
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

				String unitNum = null;
				if (fg) {
					WxBaseUser wx = LU.getLWx(request, redis);
					if (wx != null) {
						unitNum = wx.getCompanyNum();
					} else {
						unitNum = LU.getLUnitNum(request, redis);
					}
					if (StringUtils.isEmpty(unitNum)) {
						fg = U.setPutFalse(map, "您未登录车队，不能继续操作");
					}

					U.log(log, "[车队编号] unitNum=" + unitNum);
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
							hql = "from CarOilList where plateNum = ? and unitNum = ? and id <> ? order by id desc";
							cols = coiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum, Long.parseLong(uid));
						} else {// 添加
							hql = "from CarOilList where plateNum = ? and unitNum = ? order by id desc";
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
							hql = "from CarRepairList where plateNum = ? and unitNum = ? and id <> ? order by id desc";
							cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum, Long.parseLong(uid));
						} else {// 添加
							hql = "from CarRepairList where plateNum = ? and unitNum = ? order by id desc";
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
	public Map<String, Object> addUpdCoi(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			MultipartHttpServletRequest multReq, String uid, String lname, String plateNum, String currentKilo,
			String addOilWay, String oilStation, String oilRise, String oilMoney, String oilCard, String jyDate,
			String jyRemark, String isCn, String oilRealMoney) {
		String logtxt = U.log(log, "车队驾驶员-添加/修改-加油记录", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			if (ReqSrc.WX == reqsrc || ReqSrc.PC_COMPANY == reqsrc) {

				/*
				 * String unitNum = null; if(fg){ WxId wx =
				 * LU.getLWx(request,redis); if(wx != null){ unitNum =
				 * wx.getUnitNum(); }else{ unitNum = LU.getLUnitNum(request,
				 * redis); } if(StringUtils.isEmpty(unitNum)){ fg =
				 * U.setPutFalse(map, "您未登录车队，不能继续操作"); } }
				 * 
				 * CarOilList col = null; if(fg){ if(StringUtils.isEmpty(uid)){
				 * U.log(log, "[加油记录id]为空，则为添加"); }else{ uid = uid.trim();
				 * if(!FV.isLong(uid)){ fg = U.setPutFalse(map,
				 * "[加油记录id]格式错误，应为long类型"); }else{ col =
				 * coiDao.findByField("id", Long.parseLong(uid)); if(col ==
				 * null){ fg = U.setPutFalse(map, "该[加油记录]不存在"); } }
				 * 
				 * U.log(log, "加油记录id：uid="+uid); } }
				 * 
				 * int _isCn = -1; if(fg){ if(StringUtils.isEmpty(isCn)){ _isCn
				 * = 0; U.log(log, "[是否是出纳]为空，默认为0"); }else{ isCn = isCn.trim();
				 * if(!FV.isInteger(isCn)){ fg = U.setPutFalse(map,
				 * "[是否是出纳]格式错误，应为整数"); }else{ _isCn = Integer.parseInt(isCn); }
				 * 
				 * U.log(log, "是否是出纳：isCn="+isCn); } }
				 * 
				 * if(fg){ if(StringUtils.isEmpty(plateNum)){ fg =
				 * U.setPutFalse(map, "[车牌号]不能为空"); }else{ plateNum =
				 * plateNum.trim();
				 * 
				 * U.log(log, "车牌号：plateNum="+plateNum); } }
				 * 
				 * double _currentKilo = 0d; if(fg){
				 * if(StringUtils.isEmpty(currentKilo)){ fg = U.setPutFalse(map,
				 * "[当前公里数]不能为空"); }else{ currentKilo = currentKilo.trim();
				 * if(!FV.isDouble(currentKilo)){ fg = U.setPutFalse(map,
				 * "[当前公里数]格式错误，应为正数"); }else{ _currentKilo =
				 * Double.parseDouble(currentKilo); }
				 * 
				 * U.log(log, "当前公里数：currentKilo="+currentKilo); } }
				 * 
				 * OilWay jyWay = null; if(fg){
				 * if(StringUtils.isEmpty(addOilWay)){ fg = U.setPutFalse(map,
				 * "[加油方式]不能为空"); }else{ addOilWay = addOilWay.trim();
				 * if(!FV.isOfEnum(OilWay.class, addOilWay)){ fg =
				 * U.setPutFalse(map, "[加油方式]格式不正确"); }else{ jyWay =
				 * OilWay.valueOf(addOilWay); }
				 * 
				 * U.log(log, "加油方式：addOilWay="+addOilWay); } }
				 * 
				 * if(fg){ if(OilWay.YP_JY == jyWay){// 油票加油，验证-加油站 if(fg){
				 * if(StringUtils.isEmpty(oilStation)){ fg = U.setPutFalse(map,
				 * "[加油站名称]不能为空"); }else{ oilStation = oilStation.trim();
				 * 
				 * U.log(log, "加油站名称：oilStation="+oilStation); } } }else
				 * if(OilWay.CZK_JY == jyWay){// 充值卡加油-验证加油卡 if(fg){
				 * if(StringUtils.isEmpty(oilCard)){ fg = U.setPutFalse(map,
				 * "[加油卡号]不能为空"); }else{ oilCard = oilCard.trim();
				 * 
				 * U.log(log, "加油卡号：oilCard="+oilCard); } } } }
				 * 
				 * Date _jyDate = null; if(fg){ if(StringUtils.isEmpty(jyDate)){
				 * fg = U.setPutFalse(map, "[加油日期]不能为空"); }else{ jyDate =
				 * jyDate.trim(); if(!FV.isDate(jyDate)){ fg =
				 * U.setPutFalse(map, "[加油日期]格式不正确"); }else{ _jyDate =
				 * DateUtils.strToDate(jyDate); }
				 * 
				 * U.log(log, "加油日期：jyDate="+jyDate); } }
				 * 
				 * if(fg){ if(StringUtils.isEmpty(jyRemark)){ U.log(log,
				 * "[加油备注]为空"); }else{ jyRemark = jyRemark.trim();
				 * if(jyRemark.length() > 40){ fg = U.setPutFalse(map,
				 * "[备注]最多填写40个字"); }
				 * 
				 * U.log(log, "加油备注：jyRemark="+jyRemark); } }
				 * 
				 * double _oilRise = 0d; double _oilMoney = 0d; if(ReqSrc.WX ==
				 * reqsrc){ if(fg){ if(!multReq.getFileNames().hasNext()){//
				 * 不存在文件 fg = U.setPutFalse(map, "[加油小票图片]不能为空"); } }
				 * 
				 * if(fg){ if(OilWay.YP_JY == jyWay) {
				 * if(StringUtils.isEmpty(oilRise) &&
				 * StringUtils.isEmpty(oilMoney)) { fg = U.setPutFalse(map,
				 * "[加油数量]或[加油金额]至少填写一个"); }else { if(fg){
				 * if(StringUtils.isEmpty(oilRise)){ U.log(log, "[加油数量]为空");
				 * }else{ oilRise = oilRise.trim(); if(!FV.isDouble(oilRise)){
				 * fg = U.setPutFalse(map, "[加油数量]格式错误，应为正数"); }else{ _oilRise =
				 * Double.parseDouble(oilRise); }
				 * 
				 * U.log(log, "加油数量：oilRise="+oilRise); } }
				 * 
				 * if(fg) { if(StringUtils.isEmpty(oilMoney)){ U.log(log,
				 * "[加油金额]为空"); }else{ oilMoney = oilMoney.trim();
				 * if(!FV.isDouble(oilMoney)){ fg = U.setPutFalse(map,
				 * "[加油金额]格式错误，应为正数"); }else{ _oilMoney =
				 * Double.parseDouble(oilMoney); }
				 * 
				 * U.log(log, "加油金额：oilMoney="+oilMoney); } } } }else { if(fg){
				 * if(StringUtils.isEmpty(oilMoney)){ if
				 * (StringUtils.isNotEmpty(oilCard)) {// 加油卡不为空，则加油金额也不能为空 fg =
				 * U.setPutFalse(map, "[加油金额]不能为空"); }else{ U.log(log,
				 * "[加油金额]为空"); } }else{ oilMoney = oilMoney.trim();
				 * if(!FV.isDouble(oilMoney)){ fg = U.setPutFalse(map,
				 * "[加油金额]格式错误，应为正数"); }else{ _oilMoney =
				 * Double.parseDouble(oilMoney); }
				 * 
				 * U.log(log, "加油金额：oilMoney="+oilMoney); } } }
				 * 
				 * }
				 * 
				 * if(fg){ if(StringUtils.isEmpty(lname)){ fg =
				 * U.setPutFalse(map, "[登录账号]不能为空"); }else{ lname =
				 * lname.trim();
				 * 
				 * U.log(log, "登录账号：lname="+lname); } } String driver = null;
				 * DriverList dl = null; if(fg){ dl =
				 * dlSer.findDriverOfCarTeam(lname, unitNum); if(dl == null){ fg
				 * = U.setPutFalse(map, "当前驾驶员不存在"); }else
				 * if(StringUtils.isEmpty(dl.getReimburseTipOper())) { fg =
				 * U.setPutFalse(map, 3, "请先设置报销通知操作员"); }else{ driver =
				 * dl.getcName()+","+dl.getDriverName(); } }
				 * 
				 * CarOilList lastOil = null; if(fg){ // hql =
				 * "FROM CarOilList WHERE id = (SELECT MAX(id) FROM CarOilList WHERE plateNum = ? and unitNum = ? group by plateNum)"
				 * ; // lastOil = coiDao.findObj(hql, plateNum, unitNum);//
				 * 查询上一次的公里数
				 * 
				 * hql =
				 * "from CarOilList where plateNum = ? and unitNum = ? order by id desc"
				 * ; List<CarOilList> lastCols = coiDao.hqlListFirstMax(hql, 0,
				 * 1, plateNum, unitNum); if(lastCols.size() == 0){ U.log(log,
				 * "是第一次添加"); }else{ lastOil = lastCols.get(0); U.log(log,
				 * "非第一次添加"); } }
				 * 
				 * if(fg){ if(lastOil != null){// 非第一次添加才验证 // 修改
				 * if(StringUtils.isNotEmpty(uid)){ if(fg){ if(col.getIsCheck()
				 * != 0){ fg = U.setPutFalse(map, "该[加油记账]已审核，不能修改"); } }
				 * 
				 * if(fg){ if(fg){ CarOilList beforeLast = null; // hql =
				 * "FROM CarOilList WHERE id = (SELECT MAX(id) FROM CarOilList WHERE plateNum = ? and unitNum = ? and id < ? group by plateNum)"
				 * ; // CarOilList beforeLast = coiDao.findObj(hql, plateNum,
				 * unitNum, lastOil.getId());
				 * 
				 * hql =
				 * "from CarOilList where plateNum = ? and unitNum = ? and id <> ? order by id desc"
				 * ; List<CarOilList> beforeCols = coiDao.hqlListFirstMax(hql,
				 * 0, 1, plateNum, unitNum, Long.parseLong(uid));
				 * if(beforeCols.size() > 0){ beforeLast = beforeCols.get(0);
				 * 
				 * if (_currentKilo < beforeLast.getCurrentKilo()) { fg =
				 * U.setPutFalse(map, "当前公里数必须大于上次公里数，上次为"+
				 * beforeLast.getCurrentKilo()+ "公里"); }
				 * 
				 * if(fg){ hql =
				 * "from CarList where plateNum = ? and unitNum = ?"; CarList
				 * car = clSer.findObj(hql, plateNum, unitNum); //
				 * 有续航里程判断是否超过了续航里程 if (car.getCarMileage() > 0 && _currentKilo
				 * > MathUtils.add(beforeLast.getCurrentKilo(),
				 * car.getCarMileage(), 2)) { fg = U.setPutFalse(map,
				 * "当前公里数不能超过最大续航里程，最大为" +
				 * MathUtils.add(beforeLast.getCurrentKilo(),
				 * car.getCarMileage(), 2) + "公里"); } } } } } }
				 * 
				 * // 添加 if(StringUtils.isEmpty(uid)){ if(fg){ if (_currentKilo
				 * <= lastOil.getCurrentKilo()) { fg = U.setPutFalse(map,
				 * "当前公里数必须大于上次公里数，上次为" + lastOil.getCurrentKilo() + "公里"); } }
				 * 
				 * if(fg){ hql =
				 * "from CarList where plateNum = ? and unitNum = ?"; CarList
				 * car = clSer.findObj(hql, plateNum, unitNum); if(car != null){
				 * // 有续航里程判断是否超过了续航里程 if (car.getCarMileage() > 0 &&
				 * _currentKilo > MathUtils.add(lastOil.getCurrentKilo(),
				 * car.getCarMileage(), 2)) { fg = U.setPutFalse(map,
				 * "当前公里数不能超过最大续航里程，最大为" +
				 * MathUtils.add(lastOil.getCurrentKilo(), car.getCarMileage(),
				 * 2) + "公里"); } } } } } }
				 * 
				 * if(fg){ // 此处记录下之前保存的图片文件
				 * 
				 * String voucherInfo = fmSer.upFiles(multReq, response,
				 * "加油凭证"); if(voucherInfo == null ||
				 * voucherInfo.contains("-")){ if
				 * (StringUtils.isNotEmpty(plateNum)) { if
				 * (StringUtils.isNotEmpty(uid)) {// 修改 //wx端不会出现出纳修改返回撤销的问题 }
				 * else { // 添加 col = new CarOilList(); col.setReqsrc(reqsrc);
				 * col.setTeamNo(unitNum); col.setcName(driver); }
				 * col.setPlateNum(plateNum);
				 * col.setOilWay(OilWay.valueOf(addOilWay));
				 * col.setAddOilWay(OilWay.valueOf(addOilWay).getOilWayText());
				 * if (StringUtils.isNotEmpty(oilRise))
				 * col.setOilRise(_oilRise);// 加油量 if
				 * (StringUtils.isNotEmpty(oilStation))
				 * col.setOilStation(oilStation); if
				 * (StringUtils.isNotEmpty(oilCard)) {
				 * col.setOilStation(oilCard);// 加油卡 } if
				 * (StringUtils.isNotEmpty(oilMoney)) {// 加油金额
				 * col.setOilMoney(_oilMoney); } else {// 非现金根据后台设置计算出价格 hql =
				 * "from PublicDataSet where setId = ? and type = ?";
				 * PublicDataSet pds = pdsDao.findObj(hql,
				 * KCBConstans.POWER_PRICE, 9);// 获取对应类型的数据对象 if (pds != null) {
				 * CarList cl = clSer.findByField("plateNum", plateNum);//
				 * 获取车辆动力来源 String[] price = pds.getCusSmsSet().split(",");
				 * double money = MathUtils.mul(_oilRise,
				 * Double.valueOf(price[cl.getCarPower()]), 2);
				 * col.setOilMoney(money); } } if (voucherInfo != null) {
				 * col.setOilVoucherId(voucherInfo.split("-")[0]);
				 * col.setOilVoucherUrl(voucherInfo.split("-")[1]); }
				 * col.setAddTime(new Date()); 2019-05-08 增-加油日期、加油备注
				 * col.setOilDate(_jyDate); col.setOilRemark(jyRemark);
				 *//******** 更新上一次加油油耗 *******/
				/*
				 * if (lastOil != null && StringUtils.isEmpty(uid)) {// 非第一次添加
				 * if (StringUtils.isEmpty(uid))
				 * col.setLastKilo(lastOil.getCurrentKilo());// 添加才更新上次公里数 //
				 * 油耗=上一次加油量/(当前公里数-上一次公里数) double oilWear =
				 * MathUtils.div(lastOil.getOilRise(),
				 * MathUtils.sub(_currentKilo, lastOil.getCurrentKilo(), 2), 2);
				 * lastOil.setOilWear(MathUtils.mul(oilWear, 100, 2));// 百公里油耗
				 * coiDao.update(lastOil); }
				 *//******** 更新上一次加油油耗 *******/
				/*
				 * col.setCurrentKilo(_currentKilo);
				 * 
				 * // 设置操作信息
				 * col.setRemark(com.ebam.mis.utils.kcb.others.Util.getOperInfo(
				 * dl.getDriverName(), "添加")); } int result = this.operCoi(uid,
				 * col);
				 * 
				 * // 此处删除之前的图片
				 * 
				 * 
				 * if (result == 1) { ReimburseList reim = null;
				 * 
				 * //油罐车和现金才加入记账报销，油票和充值卡在预存费里面审核
				 * if(col.getOilWay().equals(OilWay.YGC_JY) ||
				 * col.getOilWay().equals(OilWay.XJ_JY)){
				 *//*** xx添加报销记录 20190524 ***/
				/*
				 * if(StringUtils.isBlank(uid)){//添加 reim=new ReimburseList();
				 * reim.setTeamNo(col.getUnitNum()); reim.setFeeType("加油");
				 * reim.setFeeStatus(1); reim.setPlateNum(col.getPlateNum());
				 * reim.setReimName(col.getcName().split(",")[1]);//姓名
				 * reim.setcName(col.getcName().split(",")[0]);//账号
				 * reim.setTotalMoney(col.getOilMoney());
				 * reim.setRemark(col.getOilRemark());
				 * reim.setOperator(WebUtil.getLUser(request).getRealName());
				 * reim.setAddTime(new Date());
				 * reim.setUseDayStart(col.getOilDate());
				 * reim.setOrderNum(col.getId()+""); reim.setReqsrc(reqsrc);
				 * reim.setReimVoucherId(col.getOilVoucherId());
				 * reim.setReimVoucherUrl(col.getOilVoucherUrl());
				 * reim.setReimType(3);//加油报销
				 * reim.setNote(com.ebam.mis.utils.kcb.others.Util.getOperInfo(
				 * dl.getDriverName(), "添加")); reimSer.save(reim); }else{//修改
				 * hql="from ReimburseList where reimType=3 and orderNum=?";
				 * reim=reimSer.findObj(hql, uid); if(reim!=null){
				 * reim.setPlateNum(col.getPlateNum());
				 * reim.setReimName(col.getcName().split(",")[1]);//姓名
				 * reim.setcName(col.getcName().split(",")[0]);//账号
				 * reim.setTotalMoney(col.getOilMoney());
				 * reim.setRemark(col.getOilRemark());
				 * reim.setOperator(reim.getOperator()+","+WebUtil.getLUser(
				 * request).getRealName());
				 * reim.setUseDayStart(col.getOilDate());
				 * reim.setReimVoucherId(col.getOilVoucherId());
				 * reim.setReimVoucherUrl(col.getOilVoucherUrl());
				 * reimSer.update(reim); } }
				 *//*** xx添加报销记录 20190524 ***/
				/*
				 * } fg = U.setPutFalse(map, 1, "操作成功");
				 * 
				 * -------通知驾驶员设置的职务人员--begin------------ if(reim != null &&
				 * reim.getIsCheck() == 0) { U.log(log,
				 * "微信通知驾驶员所设置的职务人员：未审核才通知");
				 * if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
				 * String[] opers = dl.getReimburseTipOper().split(",");
				 * 
				 * for (String o : opers) { ymSer.orderWaitforCheckOfWxmsg(null,
				 * reim, o); } } }else { U.log(log, "该报销记账不是：未审核，则不通知"); }
				 * -------通知驾驶员设置的职务人员--end------------ } else { fg =
				 * U.setPutFalse(map, 1, "操作异常"); } }else{ fg =
				 * U.setPutFalse(map, "操作异常" + voucherInfo); } } }else
				 * if(ReqSrc.PC_COMPANY == reqsrc){ // 油票加油/油罐车加油，验证-加油数量
				 * if(OilWay.YP_JY == jyWay || OilWay.YGC_JY == jyWay){ if(fg){
				 * if(StringUtils.isEmpty(oilRise)){ U.log(log, "[加油数量]为空");
				 * }else{ oilRise = oilRise.trim(); if(!FV.isDouble(oilRise)){
				 * fg = U.setPutFalse(map, "[加油数量]格式错误，应为正数"); }else{ _oilRise =
				 * Double.parseDouble(oilRise); }
				 * 
				 * U.log(log, "加油数量：oilRise="+oilRise); } } } if(fg){
				 * if(StringUtils.isEmpty(oilMoney)){ fg = U.setPutFalse(map,
				 * "[加油金额]不能为空"); if (OilWay.CZK_JY == jyWay || OilWay.XJ_JY ==
				 * jyWay || OilWay.YGC_JY == jyWay || (OilWay.YP_JY == jyWay &&
				 * Double.valueOf(oilStation.split("/")[1])==0))
				 * {//充值卡或现金或油罐车或(油票加油的加油站没有加油量) fg = U.setPutFalse(map,
				 * "[加油金额]不能为空"); }else{ U.log(log, "[加油金额]为空"); } }else{
				 * oilMoney = oilMoney.trim(); if(!FV.isDouble(oilMoney)){ fg =
				 * U.setPutFalse(map, "[加油金额]格式错误，应为正数"); }else{ _oilMoney =
				 * Double.parseDouble(oilMoney); } if(OilWay.YP_JY ==
				 * jyWay){//油票加油 20200203 取消判断
				 * hql="from OilCardList where unitNum=? and cardNo=?";
				 * OilCardList ocl = oclSer.findObj(hql,
				 * WebUtil.getUnitNum(request),oilStation.split("/")[0]);
				 * if(ocl!=null){
				 * if(Double.valueOf(oilStation.split("/")[1])==0){//
				 * 加油站没有预存加油量重新计算真正的总金额:((加油金额/加油升数)-优惠金额)*加油升数=本次加油金额 double
				 * preMoney=MathUtils.mul(MathUtils.sub(MathUtils.div(_oilMoney,
				 * _oilRise, 2), ocl.getOilRiseFavo(), 2),_oilRise, 2);
				 * if(ocl.getBalance()<preMoney){ fg=U.setPutFalse(map,
				 * "["+oilStation.split("/")[0]+"]预存金额不足，请联系财务人员"); }
				 * }else{//加油站有预存加油量直接计算总金额 //_oilMoney=MathUtils.mul(_oilRise,
				 * ocl.getOilPrice(), 2); if(ocl.getOilRise()<_oilRise){
				 * fg=U.setPutFalse(map,
				 * "["+oilStation.split("/")[0]+"]预存油量不足，请联系财务人员"); } }
				 * oilStation=oilStation.split("/")[0]; }else{
				 * fg=U.setPutFalse(map, "["+oilStation.split("/")[0]+"]没有预存");
				 * } }
				 * 
				 * U.log(log, "加油金额：oilMoney="+_oilMoney); } } CarOilList
				 * lastOil = null; if(fg){ hql =
				 * "FROM CarOilList WHERE id = (SELECT MAX(id) FROM CarOilList WHERE plateNum = ? and unitNum = ? group by plateNum)"
				 * ; lastOil = coiDao.findObj(hql, plateNum, unitNum);//
				 * 查询上一次的公里数
				 * 
				 * if(lastOil == null){ U.log(log, "是第一次添加"); }else{ U.log(log,
				 * "非第一次添加"); } } if(fg){ if(lastOil != null){ // 修改
				 * if(StringUtils.isNotEmpty(uid)){ if(fg){ if (lastOil.getId()
				 * == col.getId()) {// 修改最后一条记录 if(fg){ hql =
				 * "FROM CarOilList WHERE id = (SELECT MAX(id) FROM CarOilList WHERE plateNum = ? and unitNum = ? and id < ? group by plateNum)"
				 * ; CarOilList beforeLast = coiDao.findObj(hql, plateNum,
				 * unitNum, lastOil.getId()); if(beforeLast != null){ if
				 * (_currentKilo < beforeLast.getCurrentKilo()) { fg =
				 * U.setPutFalse(map, "当前公里数必须大于上次公里数，上次为"+
				 * beforeLast.getCurrentKilo()+ "公里"); }
				 * 
				 * if(fg){ hql =
				 * "from CarList where plateNum = ? and unitNum = ?"; CarList
				 * car = clSer.findObj(hql, plateNum, unitNum); //
				 * 有续航里程判断是否超过了续航里程 if (car.getCarMileage() > 0 && _currentKilo
				 * > MathUtils.add(beforeLast.getCurrentKilo(),
				 * car.getCarMileage(), 2)) { fg = U.setPutFalse(map,
				 * "当前公里数不能超过最大续航里程，最大为" +
				 * MathUtils.add(beforeLast.getCurrentKilo(),
				 * car.getCarMileage(), 2) + "公里"); } } } } }else{//
				 * 非最后一条记录当前公里数不能修改 if (col.getCurrentKilo() != _currentKilo) {
				 * fg = U.setPutFalse(map, "非该车辆最后一条加油记录，当前公里数不能修改"); } } } }
				 * 
				 * // 添加 if(StringUtils.isEmpty(uid)){ if(fg){ if (_currentKilo
				 * <= lastOil.getCurrentKilo()) { fg = U.setPutFalse(map,
				 * "当前公里数必须大于上次公里数，上次为" + lastOil.getCurrentKilo() + "公里"); } }
				 * 
				 * if(fg){ hql =
				 * "from CarList where plateNum = ? and unitNum = ?"; CarList
				 * car = clSer.findObj(hql, plateNum, unitNum); if(car != null){
				 * // 有续航里程判断是否超过了续航里程 if (car.getCarMileage() > 0 &&
				 * _currentKilo > MathUtils.add(lastOil.getCurrentKilo(),
				 * car.getCarMileage(), 2)) { fg = U.setPutFalse(map,
				 * "当前公里数不能超过最大续航里程，最大为" +
				 * MathUtils.add(lastOil.getCurrentKilo(), car.getCarMileage(),
				 * 2) + "公里"); } } } } } }
				 * 
				 * if(fg){ // 此处记录下之前保存的图片文件
				 * 
				 * String voucherInfo = fmSer.upFiles(multReq, response,
				 * "加油凭证"); if(voucherInfo == null ||
				 * voucherInfo.contains("-")){ if
				 * (StringUtils.isNotEmpty(plateNum)) { if
				 * (StringUtils.isNotEmpty(uid)) {// 修改
				 * //出纳修改并且是已审核或者已复核记录，重新添加一条利润记录因为要重新审核20190408 if(1 == _isCn
				 * && (col.getIsCheck() == 1 || col.getIsCheck() == 2)){
				 * TeamAccountBook tabPay = new TeamAccountBook();
				 * tabPay.setTeamNo(unitNum);
				 * tabPay.setAccountMoney(col.getOilMoney());
				 * tabPay.setBalance(tabSer.findBalance(unitNum, "1",
				 * col.getOilMoney(),0, null));
				 * tabPay.setRemark(col.getOilWay().getOilWayText());
				 * tabPay.setOrderNum(col.getId() + "");// 备注记录Id
				 * tabPay.setAccountName("返回撤销加油"); tabPay.setAccountType(1);//
				 * 自营 tabPay.setPlateNum(col.getPlateNum());
				 * tabPay.setAddTime(new Date()); tabPay.setAccountTime(new
				 * Date());
				 * tabPay.setNote("操作员："+WebUtil.getLUser(request).getRealName()
				 * +"撤销"); tabPay.setBookType(1); tabSer.save(tabPay);
				 * col.setIsCheck(0);//变成未审核 col.setRemark(col.getRemark() +
				 * "("+ DateUtils.DateToStr(new Date()) + "&nbsp;"+ ",操作员:" +
				 * WebUtil.getLUser(request).getRealName()+ "[返回撤销])"); } } else
				 * { // 添加 col = new CarOilList(); col.setReqsrc(reqsrc);
				 * col.setTeamNo(unitNum); col.setcName(lname); }
				 * col.setPlateNum(plateNum);
				 * col.setOilWay(OilWay.valueOf(addOilWay));
				 * col.setAddOilWay(OilWay.valueOf(addOilWay).getOilWayText());
				 * if
				 * (StringUtils.isNotEmpty(oilRise))col.setOilRise(_oilRise);//
				 * 加油量 if (StringUtils.isNotEmpty(oilStation))col.setOilStation(
				 * oilStation);//加油站 if (StringUtils.isNotEmpty(oilCard))
				 * col.setOilStation(oilCard);// 加油卡 if
				 * (StringUtils.isNotEmpty(oilRealMoney))
				 * col.setOilRealMoney(Double.valueOf(oilRealMoney));// 实付金额 if
				 * (StringUtils.isNotEmpty(oilMoney)) {// 加油金额
				 * col.setOilMoney(_oilMoney); } else {// 非现金根据后台设置计算出价格 hql =
				 * "from PublicDataSet where setId = ? and type = ?";
				 * PublicDataSet pds = pdsDao.findObj(hql,
				 * KCBConstans.POWER_PRICE, 9);// 获取对应类型的数据对象 if (pds != null) {
				 * CarList cl = clSer.findByField("plateNum", plateNum);//
				 * 获取车辆动力来源 String[] price = pds.getCusSmsSet().split(",");
				 * double money = MathUtils.mul(_oilRise,
				 * Double.valueOf(price[cl.getCarPower()]), 2);
				 * col.setOilMoney(money); } } if (voucherInfo != null) {
				 * col.setOilVoucherId(voucherInfo.split("-")[0]);
				 * col.setOilVoucherUrl(voucherInfo.split("-")[1]); }
				 * col.setAddTime(new Date()); 2019-05-08 增-加油日期、加油备注
				 * col.setOilDate(_jyDate); col.setOilRemark(jyRemark);
				 *//******** 更新上一次加油油耗 *******/
				/*
				 * if (lastOil != null && StringUtils.isEmpty(uid)) {// 非第一次添加
				 * if (StringUtils.isEmpty(uid))col.setLastKilo(lastOil.
				 * getCurrentKilo());// 添加才更新上次公里数 // 油耗=上一次加油量/(当前公里数-上一次公里数)
				 * double oilWear = MathUtils.div(lastOil.getOilRise(),
				 * MathUtils.sub(_currentKilo, lastOil.getCurrentKilo(), 2), 2);
				 * lastOil.setOilWear(MathUtils.mul(oilWear, 100, 2));// 百公里油耗
				 * coiDao.update(lastOil); }
				 *//******** 更新上一次加油油耗 *******/
				/*
				 * col.setCurrentKilo(_currentKilo); col.setReqsrc(reqsrc); }
				 * int result = this.operCoi(uid, col);
				 * 
				 * // 此处删除之前的图片
				 * 
				 * 
				 * if (result == 1) { //油罐车和现金才加入记账报销，油票和充值卡在预存费里面审核20190617
				 * if(col.getOilWay().equals(OilWay.YGC_JY) ||
				 * col.getOilWay().equals(OilWay.XJ_JY)){
				 *//*** xx添加报销记录 20190524 ***/
				/*
				 * if(StringUtils.isBlank(uid)){//添加 ReimburseList reim=new
				 * ReimburseList(); reim.setTeamNo(col.getUnitNum());
				 * reim.setFeeType("加油"); reim.setFeeStatus(1);
				 * reim.setPlateNum(col.getPlateNum());
				 * reim.setReimName(col.getcName().split(",")[1]);//姓名
				 * reim.setcName(col.getcName().split(",")[0]);//账号
				 * reim.setTotalMoney(col.getOilMoney());
				 * reim.setRemark(col.getOilRemark());
				 * reim.setOperator(WebUtil.getLUser(request).getRealName());
				 * reim.setAddTime(new Date());
				 * reim.setUseDayStart(col.getOilDate());
				 * reim.setOrderNum(col.getId()+""); reim.setReimType(3);//加油报销
				 * reim.setCustomer(col.getOilStation());
				 * reim.setReqsrc(reqsrc);
				 * reim.setReimVoucherId(col.getOilVoucherId());
				 * reim.setReimVoucherUrl(col.getOilVoucherUrl());
				 * reimSer.save(reim); }else{//修改
				 * hql="from ReimburseList where reimType=3 and orderNum=?";
				 * ReimburseList reim=reimSer.findObj(hql, uid); if(reim!=null){
				 * reim.setPlateNum(col.getPlateNum());
				 * reim.setReimName(col.getcName().split(",")[1]);//姓名
				 * reim.setcName(col.getcName().split(",")[0]);//账号
				 * reim.setTotalMoney(col.getOilMoney());
				 * reim.setRemark(col.getOilRemark());
				 * reim.setOperator(reim.getOperator()+","+WebUtil.getLUser(
				 * request).getRealName());
				 * reim.setUseDayStart(col.getOilDate());
				 * reim.setCustomer(col.getOilStation());
				 * if((reim.getIsCheck()==1 || reim.getIsCheck()==2) &&
				 * isCn.equals("1")){//出纳修改已审核或已复核 reim.setIsCheck(0);
				 * reim.setNote(reim.getNote() + "("+ DateUtils.DateToStr(new
				 * Date()) + "&nbsp;"+ ",操作员:" +
				 * WebUtil.getLUser(request).getRealName()+ "[返回撤销])"); }
				 * reim.setReimVoucherId(col.getOilVoucherId());
				 * reim.setReimVoucherUrl(col.getOilVoucherUrl());
				 * reimSer.update(reim); } }
				 *//*** xx添加报销记录 20190524 ***//*
												 * } U.setPut(map, 1, "操作成功"); }
												 * else { fg =
												 * U.setPutFalse(map, 1,
												 * "操作异常"); } }else{ fg =
												 * U.setPutFalse(map, "操作异常" +
												 * voucherInfo); } } }
												 */
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
	public Object[] getMinMaxKm(Object obj, int reimType) {
		String logtxt = U.log(log, "判断-指定记录公里数是否在其前后记录公里数之间");

		boolean fg = true;
		String hql = "";
		Object[] res = new Object[] { 0, 0, 0 };

		try {
			/*
			 * int index = -1; if(reimType == 3) { U.log(log, "加油报销");
			 * 
			 * CarOilList o = (CarOilList)obj;
			 * 
			 * // 查询记录的最小和最大里程（此条件控制了数据筛选量变少） double minMil = 0d, maxMil = 0d;
			 * CarList cl = null; if(fg) { hql =
			 * "from CarList where unitNum = ? and plateNum = ?"; cl =
			 * clSer.findObj(hql, o.getUnitNum(), o.getPlateNum()); if(cl ==
			 * null) { fg = U.logFalse(log,
			 * "【车辆"+o.getPlateNum()+"不存在，请联系管理员】"); }else { //
			 * 按照要修改的公里数向前加车辆里程作为最大值，向后减车辆里程作为最小值 minMil =
			 * MathUtils.sub(o.getCurrentKilo(), cl.getCarMileage(), 2); maxMil
			 * = MathUtils.add(o.getCurrentKilo(), cl.getCarMileage(), 2);
			 * 
			 * U.log(log, "查询加油记账车辆公里数区间为：["+minMil+", "+maxMil+"]"); } }
			 * 
			 * List<CarOilList> list = new ArrayList<CarOilList>(); if(fg) { //
			 * 获取车辆加油记账，公里数控制在区间内 hql =
			 * "select new CarOilList(id, currentKilo) from CarOilList where unitNum = ? and plateNum = ? and (currentKilo >= ? and currentKilo <= ?) order by id desc"
			 * ; list = coiDao.findhqlList(hql, o.getUnitNum(), o.getPlateNum(),
			 * minMil, maxMil); if(list.size() == 0) { fg = U.logFalse(log,
			 * "没有获取到驾驶员["+o.getcName()+"]的加油记账列表"); } }
			 * 
			 * // 遍历列表，找到指定id数据所在列表的下标 for (int i = 0; i < list.size(); i++) {
			 * if(list.get(i).getId() == o.getId()) { index = i; break; } }
			 * 
			 * if(fg) { if(index < 0) { fg = U.logFalse(log,
			 * "没有找到指定数据id["+o.getId()+"]所在列表的下标index"); }else { //
			 * 找到指定id的下标，则获取该下标的前后两条数据 double maxKm = 0d, minKm = 0d;//
			 * 最大里程数，最小里程数
			 * 
			 * if(index == 0) { U.log(log, "不存在上一个下标的数据，则上限是该车辆的续航里程数");
			 * 
			 * // 车辆最大续航里程 = 上一次记录的车辆总里程 + 车辆里程数 maxKm =
			 * MathUtils.add(list.get(index + 1).getCurrentKilo(),
			 * cl.getCarMileage(), 2); }else { maxKm = list.get(index -
			 * 1).getCurrentKilo(); res[2] = list.get(index - 1).getId(); }
			 * 
			 * if(index >= list.size() - 1) { U.log(log, "不存在下一个下标的数据，则下限是0");
			 * minKm = 0; }else { minKm = list.get(index + 1).getCurrentKilo();
			 * } U.log(log, "最大里程数为："+maxKm+", 最小里程数为："+minKm);
			 * 
			 * res[0] = minKm; res[1] = maxKm; } } }else if(reimType == 4) {
			 * U.log(log, "维修报销");
			 * 
			 * CarRepairList o = (CarRepairList)obj;
			 * 
			 * // 查询记录的最小和最大里程（此条件控制了数据筛选量变少） double minMil = 0d, maxMil = 0d;
			 * CarList cl = null; if(fg) { hql =
			 * "from CarList where unitNum = ? and plateNum = ?"; cl =
			 * clSer.findObj(hql, o.getUnitNum(), o.getPlateNum()); if(cl ==
			 * null) { fg = U.logFalse(log,
			 * "【车辆"+o.getPlateNum()+"不存在，请联系管理员】"); }else { //
			 * 按照要修改的公里数向前加车辆里程作为最大值，向后减车辆里程作为最小值 minMil =
			 * MathUtils.sub(o.getCurrentKilo(), cl.getCarMileage(), 2); maxMil
			 * = MathUtils.add(o.getCurrentKilo(), cl.getCarMileage(), 2);
			 * 
			 * U.log(log, "查询加油记账车辆公里数区间为：["+minMil+", "+maxMil+"]"); } }
			 * 
			 * List<CarRepairList> list = new ArrayList<CarRepairList>(); if(fg)
			 * { // 获取驾驶员所有维修记账，按照车辆公里数倒序排序（因为一般修改都是修改最近的数据） hql =
			 * "select new CarRepairList(id, currentKilo) from CarRepairList where unitNum = ? and plateNum = ? and (currentKilo >= ? and currentKilo <= ?) order by id desc"
			 * ; list = coiDao.findhqlList(hql, o.getUnitNum(), o.getPlateNum(),
			 * minMil, maxMil); if(list.size() == 0) { fg = U.logFalse(log,
			 * "没有获取到驾驶员["+o.getcName()+"]的加油记账列表"); } }
			 * 
			 * // 遍历列表，找到指定id数据所在列表的下标 for (int i = 0; i < list.size(); i++) {
			 * if(list.get(i).getId() == o.getId()) { index = i; break; } }
			 * 
			 * if(fg) { if(index < 0) { fg = U.logFalse(log,
			 * "没有找到指定数据id["+o.getId()+"]所在列表的下标index"); }else { //
			 * 找到指定id的下标，则获取该下标的前后两条数据 double maxKm = 0d, minKm = 0d;//
			 * 最大里程数，最小里程数
			 * 
			 * if(index == 0) { U.log(log, "不存在上一个下标的数据，则上限是该车辆的续航里程数");
			 * 
			 * // 车辆最大续航里程 = 上一次记录的车辆总里程 + 车辆里程数 maxKm =
			 * MathUtils.add(list.get(index + 1).getCurrentKilo(),
			 * cl.getCarMileage(), 2); }else { maxKm = list.get(index -
			 * 1).getCurrentKilo(); res[2] = list.get(index - 1).getId(); }
			 * 
			 * if(index >= list.size() - 1) { U.log(log, "不存在下一个下标的数据，则下限是0");
			 * minKm = 0; }else { minKm = list.get(index + 1).getCurrentKilo();
			 * } U.log(log, "最大里程数为："+maxKm+", 最小里程数为："+minKm);
			 * 
			 * res[0] = minKm; res[1] = maxKm; } } }
			 */
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}

		return res;
	}



	@Override
	public Map<String, Object> updCoi(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			MultipartHttpServletRequest multReq, String unitNum, String luname, String uid, String currKm,
			String addOilWay, String oilStation, String oilRise, String oilMoney, String oilCard, String jyDate,
			String jyRemark, String imgIds) {
		String logtxt = U.log(log, "修改-加油记录", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			/*
			 * if(fg){ if(StringUtils.isEmpty(unitNum)){ fg = U.setPutFalse(map,
			 * "您未登录车队，不能继续操作"); }else { unitNum = unitNum.trim();
			 * 
			 * U.log(log, "[登录车队编号] unitNum="+unitNum); } }
			 * 
			 * if(fg){ if(StringUtils.isEmpty(luname)){ fg = U.setPutFalse(map,
			 * "[登录账号]不能为空"); }else{ luname = luname.trim();
			 * 
			 * U.log(log, "登录账号：luname="+luname); } }
			 * 
			 * DriverList dl = null; if(fg){ dl =
			 * dlSer.findDriverOfCarTeam(luname, unitNum); if(dl == null){ fg =
			 * U.setPutFalse(map, "当前驾驶员不存在"); }else
			 * if(StringUtils.isEmpty(dl.getReimburseTipOper())) { fg =
			 * U.setPutFalse(map, 3, "请先设置报销通知操作员"); }
			 * 
			 * U.log(log, "[登录驾驶员] driver="+dl.getDriverName()); }
			 * 
			 * 
			 * if(fg){ if(StringUtils.isEmpty(uid)){ fg = U.setPutFalse(map,
			 * "[加油记录id]不能为空"); }else{ uid = uid.trim(); if(!FV.isLong(uid)){ fg
			 * = U.setPutFalse(map, "[加油记录id]格式错误，应为long类型"); }
			 * 
			 * U.log(log, "加油记录id：uid="+uid); } }
			 * 
			 * CarOilList col = null; if(fg) { col = coiDao.findByField("id",
			 * Long.parseLong(uid)); if(col == null){ fg = U.setPutFalse(map,
			 * "该[加油记录]不存在"); }else if(col.getIsCheck() != 0){ fg =
			 * U.setPutFalse(map, "该加油记账不是[未审核]，不能修改"); } }
			 * 
			 * ReimburseList rl = null; if(fg && (col.getOilWay() ==
			 * OilWay.YGC_JY || col.getOilWay() == OilWay.XJ_JY)) { hql =
			 * "from ReimburseList where reimType = ? and orderNum = ?"; rl =
			 * reimSer.findObj(hql, 3, col.getId()+""); if(rl == null) { fg =
			 * U.setPutFalse(map, "该加油记录对应[财务报销记录]不存在，请联系管理员"); }else {
			 * U.log(log, "[财务报销记录id] id="+rl.getId()); } }
			 * 
			 * double _currKm = 0d; if(fg){ if(StringUtils.isEmpty(currKm)){ fg
			 * = U.setPutFalse(map, "[车辆公里数]不能为空"); }else{ currKm =
			 * currKm.trim(); if(!FV.isDouble(currKm)){ fg = U.setPutFalse(map,
			 * "[车辆公里数]格式不正确"); }else{ _currKm = Double.parseDouble(currKm); }
			 * 
			 * U.log(log, "[车辆公里数] currKm="+currKm); } }
			 * 
			 * CarOilList lastCol = null;// 获取当前修改数据的下一条数据，为了修改该记录的上一条车辆公里数
			 * if(fg) { // 公里数变化了 if(_currKm != col.getCurrentKilo()) { //
			 * 修改公里数时，公里数只能是顺序添加的自身前后两条数据的公里数之间 Object[] minMax =
			 * getMinMaxKm(col, 3); double min =
			 * Double.parseDouble(minMax[0].toString()); double max =
			 * Double.parseDouble(minMax[1].toString()); long nextId =
			 * Long.parseLong(minMax[2].toString());
			 * 
			 * if(_currKm <= min || _currKm >= max) { fg = U.setPutFalse(map,
			 * -2, "公里数应该在"+(int)min+"到"+(int)max+"之间"); }
			 * 
			 * if(nextId != 0) {// 存在下一条记录 lastCol = coiDao.findByField("id",
			 * nextId); } } }
			 * 
			 * OilWay jyWay = null; if(fg){ if(StringUtils.isEmpty(addOilWay)){
			 * fg = U.setPutFalse(map, "[加油方式]不能为空"); }else{ addOilWay =
			 * addOilWay.trim(); if(!FV.isOfEnum(OilWay.class, addOilWay)){ fg =
			 * U.setPutFalse(map, "[加油方式]格式不正确"); }else{ jyWay =
			 * OilWay.valueOf(addOilWay); }
			 * 
			 * U.log(log, "加油方式：addOilWay="+addOilWay); } }
			 * 
			 * if(fg){ if(OilWay.YP_JY == jyWay){// 油票加油，验证-加油站 if(fg){
			 * if(StringUtils.isEmpty(oilStation)){ fg = U.setPutFalse(map,
			 * "[加油站名称]不能为空"); }else{ oilStation = oilStation.trim();
			 * 
			 * U.log(log, "加油站名称：oilStation="+oilStation); } } }else
			 * if(OilWay.CZK_JY == jyWay){// 充值卡加油-验证加油卡 if(fg){
			 * if(StringUtils.isEmpty(oilCard)){ fg = U.setPutFalse(map,
			 * "[加油卡号]不能为空"); }else{ oilCard = oilCard.trim();
			 * 
			 * U.log(log, "加油卡号：oilCard="+oilCard); } } } }
			 * 
			 * Date _jyDate = null; if(fg){ if(StringUtils.isEmpty(jyDate)){ fg
			 * = U.setPutFalse(map, "[加油日期]不能为空"); }else{ jyDate =
			 * jyDate.trim(); if(!FV.isDate(jyDate)){ fg = U.setPutFalse(map,
			 * "[加油日期]格式不正确"); }else{ _jyDate = DateUtils.strToDate(jyDate); }
			 * 
			 * U.log(log, "加油日期：jyDate="+jyDate); } }
			 * 
			 * if(fg){ if(StringUtils.isEmpty(jyRemark)){ U.log(log,
			 * "[加油备注]为空"); }else{ jyRemark = jyRemark.trim();
			 * if(jyRemark.length() > 40){ fg = U.setPutFalse(map,
			 * "[备注]最多填写40个字"); }
			 * 
			 * U.log(log, "加油备注：jyRemark="+jyRemark); } }
			 * 
			 * double _oilRise = 0d; double _oilMoney = 0d; if(fg){
			 * if(OilWay.YP_JY == jyWay) { if(StringUtils.isEmpty(oilRise) &&
			 * StringUtils.isEmpty(oilMoney)) { fg = U.setPutFalse(map,
			 * "[加油数量]或[加油金额]至少填写一个"); }else { if(fg){
			 * if(StringUtils.isEmpty(oilRise)){ U.log(log, "[加油数量]为空"); }else{
			 * oilRise = oilRise.trim(); if(!FV.isDouble(oilRise)){ fg =
			 * U.setPutFalse(map, "[加油数量]格式错误，应为正数"); }else{ _oilRise =
			 * Double.parseDouble(oilRise); }
			 * 
			 * U.log(log, "加油数量：oilRise="+oilRise); } }
			 * 
			 * if(fg) { if(StringUtils.isEmpty(oilMoney)){ U.log(log,
			 * "[加油金额]为空"); }else{ oilMoney = oilMoney.trim();
			 * if(!FV.isDouble(oilMoney)){ fg = U.setPutFalse(map,
			 * "[加油金额]格式错误，应为正数"); }else{ _oilMoney =
			 * Double.parseDouble(oilMoney); }
			 * 
			 * U.log(log, "加油金额：oilMoney="+oilMoney); } } } }else { if(fg){
			 * if(StringUtils.isEmpty(oilMoney)){ if
			 * (StringUtils.isNotEmpty(oilCard)) {// 加油卡不为空，则加油金额也不能为空 fg =
			 * U.setPutFalse(map, "[加油金额]不能为空"); }else{ U.log(log, "[加油金额]为空");
			 * } }else{ oilMoney = oilMoney.trim(); if(!FV.isDouble(oilMoney)){
			 * fg = U.setPutFalse(map, "[加油金额]格式错误，应为正数"); }else{ _oilMoney =
			 * Double.parseDouble(oilMoney); }
			 * 
			 * U.log(log, "加油金额：oilMoney="+oilMoney); } } } }
			 * 
			 * List<Object> delImgIds = new ArrayList<Object>(); String
			 * imgUrlStr = ""; if(fg) { if(StringUtils.isEmpty(imgIds)) {
			 * U.log(log, "[图片id数组字符串]为空，则说明用户将之前的图片都删除了");
			 * 
			 * // 修改前的全部id数组 if(StringUtils.isNotBlank(col.getOilVoucherId())) {
			 * String[] allIds = col.getOilVoucherId().split(","); for (String
			 * id1 : allIds) { delImgIds.add(Long.parseLong(id1)); } U.log(log,
			 * "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(),
			 * ",")); } }else { imgIds = imgIds.trim();
			 * 
			 * // 不会删除的id数组 String[] noDelIds = imgIds.split(",");
			 * 
			 * // 修改前的全部id数组 if(StringUtils.isNotBlank(col.getOilVoucherId())) {
			 * String[] allIds = col.getOilVoucherId().split(","); for (String
			 * id1 : allIds) { boolean is = false; for (String id2 : noDelIds) {
			 * if(StringUtils.isNotBlank(id2) && id1.equals(id2)) { is = true;
			 * break;// 跳出内层循环 } }
			 * 
			 * if(!is) {// 不存在，则添加 delImgIds.add(Long.parseLong(id1)); } }
			 * U.log(log,
			 * "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(),
			 * ",")); }
			 * 
			 * List<String> imgUrls = new ArrayList<String>(); // 修改前的全部url数组
			 * if(StringUtils.isNotBlank(col.getOilVoucherUrl())) { String[]
			 * allUrls = col.getOilVoucherUrl().split(","); for (String id1 :
			 * allUrls) { boolean is = false; for (String id2 : noDelIds) {
			 * if(id1.contains("_"+id2+".")) { is = true; break;// 跳出内层循环 } }
			 * 
			 * if(is) {// 存在，则添加 imgUrls.add(id1); } } imgUrlStr =
			 * StringUtils.join(imgUrls.toArray(), ",");
			 * 
			 * U.log(log, "需保留的图片地址url数组字符串 imgUrls="+imgUrlStr); } }
			 * 
			 * }
			 * 
			 * String voucherInfo = null; if(fg){
			 * if(multReq.getFileNames().hasNext()){// 存在文件 U.log(log,
			 * "[加油小票图片]不为空，即用户修改/添加了图片");
			 * 
			 * voucherInfo = fmSer.upFiles(multReq, response, "加油凭证");
			 * if(voucherInfo == null || voucherInfo.contains("-")){ U.log(log,
			 * "修改图片成功，结果："+voucherInfo); }else { fg = U.setPutFalse(map,
			 * "[加油记录]修改图片凭证失败"); } }else { U.log(log, "[加油小票图片]为空，即用户未修改图片"); }
			 * 
			 * if(fg) { // 存在需要删除的图片id数组字符串 if(delImgIds.size() > 0) {
			 * fmSer.delFileList(delImgIds.toArray(), Util.MFILEURL); } } }
			 * 
			 * if(fg) { // 最新的图片id数组字符串, 最新的图片url数组字符串 String newIds = "",
			 * newUrls = ""; if (voucherInfo != null) { newIds =
			 * StringUtils.isEmpty(imgIds) ? voucherInfo.split("-")[0] :
			 * imgIds+","+voucherInfo.split("-")[0]; newUrls =
			 * StringUtils.isEmpty(imgUrlStr) ? voucherInfo.split("-")[1] :
			 * imgUrlStr+","+voucherInfo.split("-")[1]; }else { newIds = imgIds;
			 * newUrls = imgUrlStr; } col.setOilVoucherId(newIds);
			 * col.setOilVoucherUrl(newUrls); col.setIsCheck(0);// 变成未审核
			 * col.setOilWay(jyWay); col.setAddOilWay(jyWay.getOilWayText());
			 * col.setOilRise(_oilRise); if (StringUtils.isNotEmpty(oilStation))
			 * { col.setOilStation(oilStation);// 加油站 }else if
			 * (StringUtils.isNotEmpty(oilCard)) { col.setOilStation(oilCard);//
			 * 加油卡 }
			 * 
			 * if (StringUtils.isNotEmpty(oilMoney)) {// 加油金额
			 * col.setOilMoney(_oilMoney); } else {// 非现金根据后台设置计算出价格 //
			 * 获取对应类型的数据对象 hql =
			 * "from PublicDataSet where setId = ? and type = ?"; PublicDataSet
			 * pds = pdsDao.findObj(hql, KCBConstans.POWER_PRICE, 9); if (pds !=
			 * null) { // 获取车辆动力来源 CarList cl = clSer.findByField("plateNum",
			 * col.getPlateNum()); String[] price =
			 * pds.getCusSmsSet().split(","); double money =
			 * MathUtils.mul(_oilRise, Double.valueOf(price[cl.getCarPower()]),
			 * 2); col.setOilMoney(money); } }
			 * 
			 * col.setOilDate(_jyDate); col.setOilRemark(jyRemark);
			 * col.setCurrentKilo(_currKm);
			 * 
			 * String operNote =
			 * com.ebam.mis.utils.kcb.others.Util.getOperInfo(luname, "修改");
			 * if(StringUtils.isNotBlank(col.getRemark())) { operNote +=
			 * col.getRemark(); } col.setRemark(operNote); coiDao.update(col);
			 * U.log(log, "更新[加油记账]完成");
			 * 
			 * if(lastCol != null) {// 存在下一条记录，则修改对应的最后一条记录的公里数
			 * lastCol.setLastKilo(_currKm); coiDao.update(lastCol); U.log(log,
			 * "更新[下一条加油记账-最后一次公里数]完成"); }
			 * 
			 * // 修改对应财务报销记录 if(rl != null) { rl.setIsCheck(0);// 变成未审核
			 * rl.setTotalMoney(col.getOilMoney());
			 * rl.setRemark(col.getOilRemark()); // 记录修改操作人
			 * rl.setOperator(rl.getOperator()+","+dl.getDriverName());
			 * rl.setUseDayStart(col.getOilDate());
			 * rl.setReimVoucherId(col.getOilVoucherId());
			 * rl.setReimVoucherUrl(col.getOilVoucherUrl());
			 * 
			 * operNote = com.ebam.mis.utils.kcb.others.Util.getOperInfo(luname,
			 * "修改"); if(StringUtils.isNotBlank(rl.getNote())) { operNote +=
			 * rl.getNote(); } rl.setNote(operNote); reimSer.update(rl);
			 * U.log(log, "更新[对应财务报销记录]完成");
			 * 
			 * -------通知驾驶员设置的职务人员--begin------------ if(rl != null &&
			 * rl.getIsCheck() == 0) { U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
			 * 
			 * if(StringUtils.isNotBlank(dl.getReimburseTipOper())) { String[]
			 * opers = dl.getReimburseTipOper().split(",");
			 * 
			 * for (String o : opers) { ymSer.orderWaitforCheckOfWxmsg(null, rl,
			 * o); } } }else { U.log(log, "该报销记账不是：未审核，则不通知"); }
			 * -------通知驾驶员设置的职务人员--end------------ }
			 * 
			 * U.setPut(map, 1, "修改[加油记账]成功"); }
			 */
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> delCoi(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String lname, String did) {
		String logtxt = U.log(log, "师傅删除-加油记录/后台删除", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (ReqSrc.WX == reqsrc || ReqSrc.PC_COMPANY == reqsrc) {
				CarOilList col = null;
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
							}
						}

						U.log(log, "加油记录id：did=" + did);
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
						String unitNum = LU.getLUnitNum(request, redis);
						Customer lcus = LU.getLUSER(request, redis);
						if (!col.getOilDriver().getUname().contains(lname)
								&& !col.getOilDriver().getPhone().contains(lcus.getBaseUserId().getPhone())) {
							fg = U.setPutFalse(map, "删除失败，该[加油记录]不是您添加的");
						} else if (!col.getUnitNum().equals(unitNum)) {
							fg = U.setPutFalse(map, "删除失败，该[加油记录]不属于当前车队");
						} else {
							if (col.getIsCheck() != -1 && col.getIsCheck() != 0) {
								fg = U.setPutFalse(map, "删除失败，该[加油记录]已审核");
							}
						}
					}
				}
				if (ReqSrc.PC_COMPANY == reqsrc) { // 后台
					String unitNum = LU.getLUnitNum(request, redis);
					if (!col.getUnitNum().equals(unitNum)) {
						fg = U.setPutFalse(map, "删除失败，该[加油记录]不属于当前车队");
					}
					if (fg) {
						if (col.getIsCheck() != -1 && col.getIsCheck() != 0) {
							fg = U.setPutFalse(map, "删除失败，该[加油记录]状态不能删除");
						}
					}
				}
				if (fg) {
					this.operCoi(did, null);

					U.setPut(map, 1, "删除成功");
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
	public Map<String, Object> findCarOilListDetail(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, String luname, String id) {
		String logtxt = U.log(log, "获取-加油记账-详情", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		// String hql = "";
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

			// // 获取-加油记账-对应的车队财务报销记录
			// ReimburseList rl = null;
			// if(fg) {
			// hql = "from ReimburseList where reimType = ? and orderNum = ?";
			// rl = reimSer.findObj(hql, 3, col.getId());
			// if(rl == null) {
			// fg = U.setPutFalse(map, "该加油记账对应[财务报销记录]不存在");
			// }
			// }

			if (fg) {
				map.put("data", col);

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
}
