package com.fx.service.impl.order;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.clazz.OrderTemp;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.MainOrderStatus;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.OrderStatus;
import com.fx.commons.utils.enums.PointType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.CommonDao;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.CompanyDiscountDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.dao.cus.CusWalletDao;
import com.fx.dao.cus.WalletListDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.dao.order.BcCarPriceDao;
import com.fx.dao.order.BcOrderParamDao;
import com.fx.dao.order.CarOrderBaseDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.dao.order.CarPriceDao;
import com.fx.dao.order.CompanyOrderTempDao;
import com.fx.dao.order.DisCarInfoDao;
import com.fx.dao.order.DiscountDetailDao;
import com.fx.dao.order.MainCarOrderDao;
import com.fx.dao.order.MapPointDao;
import com.fx.dao.order.OrderParamDao;
import com.fx.dao.order.RouteMapPointDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.CompanyDiscount;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.CusWallet;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WalletList;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.StaffReimburse;
import com.fx.entity.order.BcCarPrice;
import com.fx.entity.order.BcOrderParam;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.CarOrderBase;
import com.fx.entity.order.CarPrice;
import com.fx.entity.order.CompanyOrderTemp;
import com.fx.entity.order.DisCarInfo;
import com.fx.entity.order.DiscountDetail;
import com.fx.entity.order.MainCarOrder;
import com.fx.entity.order.MapPoint;
import com.fx.entity.order.OrderParam;
import com.fx.entity.order.RouteLineInfo;
import com.fx.entity.order.RouteMapPoint;
import com.fx.entity.order.RouteStationInfo;
import com.fx.service.company.CompanyVehicleService;
import com.fx.service.order.CarOrderService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CarOrderServiceImpl extends BaseServiceImpl<CarOrder, Long> implements CarOrderService {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	@Override
	public ZBaseDaoImpl<CarOrder, Long> getDao() {
		return carOrderDao;
	}

	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 车辆订单-数据源 */
	@Autowired
	private CarOrderDao	carOrderDao;
	/** 派车信息-服务 */
	@Autowired
	private DisCarInfoDao			disCarInfoDao;
	/** 单程接送-临时订单参数-服务 */
	@Autowired
	private OrderParamDao			orderParamDao;
	/** 单程接送-临时车辆价格-服务 */
	@Autowired
	private CarPriceDao				carPriceDao;
	/** 单位-优惠券-服务 */
	@Autowired
	private CompanyDiscountDao		companyDiscountDao;
	/** 优惠券详情-服务 */
	@Autowired
	private DiscountDetailDao		discountDetailDao;
	/** 单位-服务 */
	@Autowired
	private CompanyUserDao			companyUserDao;
	/** 地图地点-服务 */
	@Autowired
	private MapPointDao				mapPointDao;
	/** 行程地点-服务 */
	@Autowired
	private RouteMapPointDao		routeMapPointDao;
	/** 我的客户-服务 */
	@Autowired
	private CompanyCustomDao		cpyCusDao;
	/** 公共-服务 */
	@Autowired
	private CommonDao				commonDao;
	/** 用户钱包-服务 */
	@Autowired
	private CusWalletDao cusWalletDao;
	/** 交易记录-服务 */
	@Autowired
	private WalletListDao walletListDao;

	// /** 操作员-服务 */
	// @Autowired
	// private OperatorListDao operatorListDao;
	// /** 微信模板消息数据-服务 */
	// @Autowired
	// private WxTplmsgDataDao wxTplmsgDataDao;
	// /** 微信公众号数据-服务 */
	// @Autowired
	// private WxPublicDataDao wxPublicDataDao;

	/** 旅游包车-临时车辆价格-服务 */
	@Autowired
	private BcCarPriceDao			bcCarPriceDao;
	/** 旅游包车-临时订单参数-服务 */
	@Autowired
	private BcOrderParamDao			bcOrderParamDao;

	/** 单位-往返临时订单-服务 */
	@Autowired
	private CompanyOrderTempDao		cotDao;

	/** 主订单-服务 */
	@Autowired
	private MainCarOrderDao			mcarOrderDao;

	/** 单位车辆-服务 */
	@Autowired
	private CompanyVehicleService	carSer;

	/** 订单基本信息-服务 **/
	@Autowired
	private CarOrderBaseDao			cobDao;

	/** 单位客户-服务 **/
	@Autowired
	private CompanyCustomDao		companyCustomDao;

	/** 单位员工-服务 */
	@Autowired
	private StaffDao				staffDao;

	/** 单位员工报账-服务 */
	@Autowired
	private StaffReimburseDao		srDao;
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao			fcDao;



	@Override
	public Map<String, Object> getCarOrderListForPayment(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject, String unitNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-单位管理-业务付款分页", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg)
				fg = U.valPageNo(map, jsonObject.getString("page"), jsonObject.getString("rows"), "订单");
			String page = jsonObject.getString("page");
			String rows = jsonObject.getString("rows");

			// 订单支付状态
			OrderPayStatus payStatus = null;
			if (!StringUtils.isBlank(jsonObject.getString("orderPayStatus"))) {
				payStatus = OrderPayStatus.valueOf(jsonObject.getString("orderPayStatus"));
			}
			// 开始时间
			String startTime = jsonObject.getString("startTime");
			// 结束时间
			String endTime = jsonObject.getString("endTime");
			// 升序降序
			String compositor = jsonObject.getString("compositor");
			CompositorType compositorType = CompositorType.valueOf(compositor);
			// 时间类型
			String timeType = jsonObject.getString("timeType");
			// 驾驶员
			String driver = jsonObject.getString("driver");
			// 行程详情
			String routeDetail = jsonObject.getString("routeDetail");
			// 用车方负责人
			String dutyService = jsonObject.getString("dutyService");

			// 供车方（收款方）搜索
			String suppCar = jsonObject.getString("suppCar");
			// 车牌号
			String plateNum = jsonObject.getString("plateNum");
			// 订单号
			String orderNum = jsonObject.getString("orderNum");
			// 业务员
			String serviceMan = jsonObject.getString("serviceMan");
			// 用车方uname
			String customer = jsonObject.getString("customer");

			if (fg) {
				Page<CarOrder> pd = carOrderDao.getCarOrderForPayment(reqsrc, page, rows, payStatus, startTime, endTime,
						compositorType, timeType, driver, dutyService, suppCar, plateNum, orderNum, routeDetail,
						serviceMan, unitNum, customer);

				// 解决懒加载问题
				for (CarOrder carOrder : pd.getResult()) {
					Hibernate.initialize(carOrder.getRouteMps());
					Hibernate.initialize(carOrder.getTrades());
				}
				U.setPageData(map, pd);
				Map<String, Object> countCarOrderForPayment = carOrderDao.countCarOrderForPayment(reqsrc, page,
						"100000", payStatus, startTime, endTime, compositorType, timeType, driver, dutyService, suppCar,
						plateNum, orderNum, routeDetail, serviceMan, unitNum, customer);
				map.put("statics", countCarOrderForPayment);
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



	// @Override
	// public Map<String, Object> externalOperate(ReqSrc reqsrc,
	// HttpServletResponse response, HttpServletRequest request,
	// List<String> odrderIdList, String operate, JSONObject jsonObject) {
	// String logtxt = U.log(log, "单位管理-订单操作-改变外调状态", reqsrc);
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// boolean fg = true;
	// // TODO Auto-generated method stub
	// try {
	// if (orderId == null) {
	// U.logFalse(log, "单位管理-订单操作-改变外调状态失败-参数非法");
	// fg = U.setPutFalse(map, 0, "传入参数非法，订单id为空");
	// }
	// CarOrder carOrder = null;
	//
	// if (fg) {
	// carOrder = carOrderDao.findByField("id", Long.parseLong(orderId));
	// if (carOrder == null) {
	// U.logFalse(log, "单位管理-订单操作-改变外调状态失败-查询订单失败");
	// fg = U.setPutFalse(map, 0, "传入订单id有误");
	// }
	// }
	// if (fg) {
	// // 当前外调状态
	// int isExternal = carOrder.getIsExternal();
	// // 操作为设置外调
	// if (operate.equals("setExternal")) {
	// // 当前状态为外调和外调已锁定
	// if (isExternal != 0) {
	// U.logFalse(log, "单位管理-订单操作-设置外调失败-订单已经处于外调状态");
	// fg = U.setPutFalse(map, 0, "外调失败，订单已处于外调状态");
	// } else {
	// carOrder.setIsExternal(1);
	// }
	// }
	// // 操作为取消外调
	// else if (operate.equals("cancelExternal")) {
	// // 当前状态为锁定状态
	// if (isExternal == 2) {
	// U.logFalse(log, "单位管理-订单操作-取消外调失败-订单处于锁定外调状态");
	// fg = U.setPutFalse(map, 0, "订单处于锁定外调状态");
	// }
	// if (isExternal == 0) {
	// U.logFalse(log, "单位管理-订单操作-取消外调失败-订单处于非外调状态");
	// fg = U.setPutFalse(map, 0, "订单处于非外调状态");
	// } else {
	// carOrder.setIsExternal(0);
	// }
	// }
	// // 操作为锁定外调
	// else if (operate.equals("lockExternal")) {
	// if (isExternal != 1) {
	// U.logFalse(log, "单位管理-订单操作-锁定外调失败-订单不处于外调状态");
	// fg = U.setPutFalse(map, 0, "订单不处于外调状态,无法锁定");
	// } else {
	// carOrder.setIsExternal(2);
	// }
	// }
	// // 操作为解锁外调
	// else if (operate.equals("unlockExternal")) {
	// // 先判断订单是否处于锁定外调状态
	// if (isExternal != 2) {
	// U.logFalse(log, "单位管理-订单操作-解锁外调失败-订单不处于锁定外调状态");
	// fg = U.setPutFalse(map, 0, "订单不处于锁定外调状态,无法解锁外调");
	// }
	//
	// else {
	// // 处于锁定外调状态，订单需处于未派单状态
	// if (!carOrder.getStatus().toString().equals("NOT_DIS_CAR")) {
	// U.logFalse(log, "单位管理-订单操作-解锁外调失败-订单不处于未派单状态");
	// fg = U.setPutFalse(map, 0, "订单不处于未派单状态,无法解锁外调");
	// } else {
	// carOrder.setIsExternal(1);
	// }
	// }
	// }
	// }
	// if (fg) {
	// carOrderDao.update(carOrder);
	// U.log(log, "单位管理-订单操作-改变外调状态-成功");
	// U.setPut(map, 1, "操作成功");
	// }
	// } catch (Exception e) {
	// U.setPutEx(map, log, e, logtxt);
	// e.printStackTrace();
	// }
	// return map;
	// }

	@Override
	public Map<String, Object> deleteOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "单位订单-删除", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		// 获取要删除的子订单id
		JSONArray jsonArray = jsonObject.getJSONArray("idList");
		// 传入子订单所属的主订单编号
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
		try {
			if (fg) {
				if (idList.isEmpty()) {
					U.logFalse(log, "单位-子订单操作-删除-传入订单id为空");
					fg = U.setPutFalse(map, 0, "传入订单id为空");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {
					U.logFalse(log, "单位-子订单操作-删除-传入主订单编号为空");
					fg = U.setPutFalse(map, 0, "主订单编号为空");
				}
			}
			if (fg) {
				for (Long id : idList) {
					CarOrder carOrder = carOrderDao.findByField("id", id);
					if (carOrder.getMainOrderNum().equals(mainOrderNum)) {
						if (carOrder.getDisCar() != null) {
							// 订单下有派车，无发删除
							U.logFalse(log, "单位-子订单操作-删除-子订单id:" + id + "无法删除，有派车");
							fg = U.setPutFalse(map, 0, "子订单" + carOrder.getOrderNum() + "有派车，无法删除");
						}
					} else {
						U.logFalse(log, "单位-子订单操作-删除-子订单id:" + id + "无法删除，不属于主订单:" + mainOrderNum);
						fg = U.setPutFalse(map, 0, "子订单" + carOrder.getOrderNum() + "不属于主订单，无法删除");
					}
				}
			}
			if (fg) {
				String carOrderHql = "update CarOrder set isDel = 1 where id in ?0";
				carOrderDao.batchExecute(carOrderHql, idList);
				// 获取没被删除的子订单
				String hql = "from CarOrder where mainOrderNum = ?0 and isDel = 0";
				List<CarOrder> list = carOrderDao.findhqlList(hql, mainOrderNum);
				if (list == null || list.isEmpty()) {
					// 主订单下所有子订单被删除，删除主订单
					mcarOrderDao.modifyMainOrder(reqsrc, null, null, mainOrderNum, 3);
					String delghql = "update MainCarOrder set isDel = 1 where orderNum = ?0";
					mcarOrderDao.batchExecute(delghql, mainOrderNum);
					U.log(log, "单位-子订单操作-删除-成功");
					U.setPut(map, 1, "删除成功");
				} else {
					mcarOrderDao.modifyMainOrder(reqsrc, list, null, mainOrderNum, 3);
					U.log(log, "单位-子订单操作-删除-成功");
					U.setPut(map, 1, "删除成功");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getCarOrderById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位-子订单-查询-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("id")) {
				U.setPutFalse(map, 0, "id参数不为空");
				return map;
			}
			CarOrder carOrder = carOrderDao.findByField("id", jsonObject.getLong("id"));
			if (carOrder != null) {
				U.log(log, "单位-子订单-查询—通过id查找子订单成功");
				// 解决懒加载问题
				// Hibernate.initialize(carOrder.getDisCars());
				Hibernate.initialize(carOrder.getRouteMps());
				Hibernate.initialize(carOrder.getTrades());
				map.put("data", carOrder);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找订单失败");
				U.setPutFalse(map, 0, "查找失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}



	@Override
	public Map<String, Object> cancelDisCar(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "单位-子订单-撤销派车", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			if (StringUtils.isBlank(jsonObject.getString("orderId"))) {
				U.logFalse(log, "单位-子订单-撤销派车-失败-订单id为空");
				U.setPutFalse(map, 0, "失败，传入订单id为空");
				return map;
			}
			String orderId = jsonObject.getString("orderId");
			boolean res = disCarInfoDao.cancelDisCar(orderId);
			if (res) {
				U.log(log, "单位-子订单-撤销派车-discar表记录清除成功");
				// 更改订单状态
				CarOrder carOrder = carOrderDao.findByField("id", orderId);
				carOrder.setStatus(OrderStatus.NOT_DIS_CAR);
				carOrderDao.update(carOrder);
				U.log(log, "单位-子订单-撤销派车-子订单状态更改成功");
				U.setPut(map, 1, "撤销成功");
				return map;
			} else {
				U.logFalse(log, "单位订单-撤销派车-discar表记录清除失败");
				U.setPutFalse(map, 0, "撤销失败");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
			return map;
		}
	}



	@Override
	public Map<String, Object> updateOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "单位-编辑订单", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {

			// 获取要删除的子订单id
			JSONArray jsonArray = jsonObject.getJSONArray("idList");
			List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
			// 传入子订单所属的主订单编号
			String mainOrderNum = jsonObject.getString("mainOrderNum");
			// 行程数，多个订单修改时行程数要相同
			int routeNo = jsonObject.getInteger("routeNo");
			// 用车方名称
			String companyName = "";
			// 乘车联系人
			String routeLink = jsonObject.getString("routeLink");
			// 业务员姓名
			String serviceMan = "";
			// 用车业务负责人姓名
			String dutyService = jsonObject.getString("dutyService");
			// 客户用户基类
			BaseUser baseUser = null;
			// 出发时间
			String stime = jsonObject.getString("stime");
			// 到达时间
			String etime = jsonObject.getString("etime");
			// 座位数
			int needSeats = jsonObject.getIntValue("needSeats");
			// 车辆数
			int needCars = jsonObject.getIntValue("needCars");
			// 收款价格
			double price = jsonObject.getDoubleValue("price");
			// 派单价格
			double disPrice = jsonObject.getDoubleValue("disPrice");
			// 行程详情
			String routeDetail = jsonObject.getString("routeDetail");
			// 备注
			String note = jsonObject.getString("note");
			// 其他原因增加时间
			double reasonTime = jsonObject.getDoubleValue("reasonTime");
			// 车辆限号
			String limitNum = jsonObject.getString("limitNum");
			// 其他费用
			double otherPrice = jsonObject.getDoubleValue("otherPrice");
			// 其他费用说明
			String otherPriceNote = jsonObject.getString("otherPriceNote");
			// 提醒师傅现收
			double remDriverCharge = jsonObject.getDoubleValue("remDriverCharge");
			// 客户id
			String customId = jsonObject.getString("companyCusId");
			if (fg) {
				if (idList.isEmpty()) {
					U.logFalse(log, "单位-编辑订单-修改id为空");
					fg = U.setPutFalse(map, 0, "订单id为空");
					return map;
				}
				if (needCars <= 0) {
					U.logFalse(log, "单位-编辑订单-车辆数不能为0");
					fg = U.setPutFalse(map, 0, "车辆数不能为0");
					return map;
				}
			}
			if (fg) {
				if (StringUtils.isBlank(customId)) {
					U.logFalse(log, "单位-编辑订单-客户id为空");
					fg = U.setPutFalse(map, 0, "客户id为空");
					return map;
				} else {
					CompanyCustom companyCustom = companyCustomDao.findByField("id", Long.parseLong(customId));
					if (null == companyCustom) {
						U.logFalse(log, "单位-编辑订单-客户信息查询错误");
						fg = U.setPutFalse(map, 0, "客户信息查询错误");
						return map;
					} else {
						companyName = companyCustom.getUnitName();
						serviceMan = companyCustom.getServiceMan();
						baseUser = companyCustom.getBaseUserId();
					}
				}
			}
			if (fg) {
				if (idList.size() == 1) {
					// 修改的是一个订单
					CarOrder carOrder = carOrderDao.findByField("id", idList.get(0));
					if (!carOrder.getMainOrderNum().equals(mainOrderNum)) {
						U.logFalse(log, "单位-编辑订单-传入数据错误，子订单不属于指定主订单");
						fg = U.setPutFalse(map, 0, "传入数据错误，子订单不属于指定主订单");
						return map;
					}
					
					MainCarOrder mainCarOrder = mcarOrderDao.findByField("orderNum", mainOrderNum);
					
					// 不论是否派车，先修改不用判断派车就可修改的信息
					carOrder.setRemDriverCharge(remDriverCharge);
					carOrder.setOtherPrice(otherPrice);
					carOrder.setOtherPriceNote(otherPriceNote);
					carOrder.setLimitNum(limitNum);
					carOrder.setReasonTime(reasonTime);
					carOrder.setRouteDetail(routeDetail);
					carOrder.setNote(note);

					if (StringUtils.isBlank(carOrder.getConfirmPaymentName())) {
						// 未确认付款价格，可以修改派单价格
						carOrder.setDisPrice(disPrice);
					}
					if (StringUtils.isBlank(carOrder.getCarOrderBase().getConfirmCollectionName())) {
						// 未确认收款价格，可以修改订单收款价格
						carOrder.setPrice(price);
					}
					if (carOrder.getDisCar() == null && carOrder.getStatus() == (OrderStatus.NOT_DIS_CAR)) {
						// 未派车,修改相应信息
						carOrder.getCarOrderBase().setCompanyName(companyName);
						carOrder.getCarOrderBase().setBaseUserId(baseUser);
						carOrder.getCarOrderBase().setDutyService(dutyService);
						carOrder.getCarOrderBase().setRouteLink(routeLink);
						carOrder.getCarOrderBase().setServiceMan(serviceMan);
						carOrder.setStime(DateUtils.strToDate(stime));
						carOrder.setEtime(DateUtils.strToDate(etime));
						//主单处于 “未确认用车”状态，子单修改时增加 或 减少 车辆数,增、减 只做车辆数的变化，不做订单复制
						if (mainCarOrder.getMainOrderBase().getStatus() == MainOrderStatus.NOT_CONFIRM) {
							carOrder.setNeedCars(needCars);
						}else{
							carOrder.setNeedCars(1);
						}						
						carOrderDao.update(carOrder);
						U.log(log, "单位-编辑订单-单个订单-未派车-修改相应信息");
						if (needCars != 1 && mainCarOrder.getMainOrderBase().getStatus() != MainOrderStatus.NOT_CONFIRM) {
							// 增加车辆数
							U.log(log, "单位-编辑订单-单个订单-未派车-复制订单");
					//		CarOrder temp =  carOrder;
							for (int i = 1; i < needCars; i++) {
								//复制订单的操作
								CarOrder add = (CarOrder)carOrder.clone();
								add.setAddTime(new Date());
								add.setOrderNum(UT.creatOrderNum(
										carOrder.getCarOrderBase().getRouteType().getValue(), add.getAddTime()));
								add.setNeedCars(1);
								add.setTrades(null);
								add.setDisCar(null);
								if (carOrder.getRouteLineInfo() !=null) {
									RouteLineInfo newRouteLineInfo = (RouteLineInfo)carOrder.getRouteLineInfo().clone();
									//不添加时会报“A different object with the same identifier value was already associated with the session : [com.fx.entity.order.RouteLineInfo#25]; nested exception is org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already associated with the session : [com.fx.entity.order.RouteLineInfo#25]”
									newRouteLineInfo.setId((long)0);
									add.setRouteLineInfo(newRouteLineInfo);
								}
								
								if (carOrder.getRouteStationInfo() != null) {
									RouteStationInfo newRouteStationInfo = (RouteStationInfo)carOrder.getRouteStationInfo().clone();
									newRouteStationInfo.setId((long)0);
									add.setRouteStationInfo(newRouteStationInfo);
								}
								if (carOrder.getConfmStart() != null) {
									MapPoint newConfmStart = (MapPoint)carOrder.getConfmStart().clone();
									newConfmStart.setId((long)0);
									add.setConfmStart(newConfmStart);
								}
								if (carOrder.getConfmStart() != null) {
									MapPoint newConfmEnd = (MapPoint)carOrder.getConfmEnd().clone();
									newConfmEnd.setId((long)0);
									add.setConfmEnd(newConfmEnd);
								}

								
								
								List<RouteMapPoint> oldRouteMps = carOrder.getRouteMps();
								if (null != oldRouteMps && !oldRouteMps.isEmpty()) {
									List<RouteMapPoint> newRouteMps = new ArrayList<RouteMapPoint>(oldRouteMps.size());
									for (RouteMapPoint rmp : oldRouteMps) {
										RouteMapPoint routeMapPoint = new RouteMapPoint();
										MapPoint newMapPoint = new MapPoint();
										MapPoint oldmapPoint = rmp.getMapPoint();
										newMapPoint.setAddress(oldmapPoint.getAddress());
										newMapPoint.setCity(oldmapPoint.getCity());
										newMapPoint.setCounty(oldmapPoint.getCounty());
										newMapPoint.setLat(oldmapPoint.getLat());
										newMapPoint.setLng(oldmapPoint.getLng());
										newMapPoint.setLngLat(oldmapPoint.getLngLat());
										
										routeMapPoint.setMapPoint(newMapPoint);
										routeMapPoint.setPtype(rmp.getPtype());
										routeMapPoint.setSortNo(rmp.getSortNo());
										newRouteMps.add(routeMapPoint);
									}
									
									add.setRouteMps(newRouteMps);
									
								}

								carOrderDao.save(add);
						//		temp = add;
							}
						}
						fg = false;
					} else {
						// 已派车，修改相应信息
						carOrderDao.update(carOrder);
						U.log(log, "单位-编辑订单-单个订单-已派车，修改相应信息");
						fg = false;
					}
				}
			}
			if (fg) {
				// 多个订单修改
				String hqlString = "from CarOrder where id in ?0";
				List<CarOrder> changeList = carOrderDao.findhqlList(hqlString, idList);
				for (CarOrder co : changeList) {
					if (co.getRouteNo() != routeNo) {
						U.logFalse(log, "单位-编辑订单-多个订单-订单：" + co.getId() + "行程数不匹配传入行程数" + routeNo);
						fg = U.setPutFalse(map, 0, "修改失败,修改的订单行程数不同");
						return map;
					}
					if (!co.getMainOrderNum().equals(mainOrderNum)) {
						U.logFalse(log, "单位-编辑订单-多个订单-订单：" + co.getId() + "不属于主订单" + mainOrderNum);
						fg = U.setPutFalse(map, 0, "修改失败,修改的订单需处于同一主订单");
						return map;
					}
				}
				// 多个订单符合修改条件，修改订单
				for (CarOrder co : changeList) {
					co.setRemDriverCharge(remDriverCharge);
					co.setOtherPrice(otherPrice);
					co.setOtherPriceNote(otherPriceNote);
					co.setLimitNum(limitNum);
					co.setReasonTime(reasonTime);
					co.setRouteDetail(routeDetail);
					co.setNote(note);
					if (StringUtils.isBlank(co.getConfirmPaymentName())) {
						// 未确认付款价格，可以修改派单价格
						co.setDisPrice(disPrice);
					}
					if (StringUtils.isBlank(co.getCarOrderBase().getConfirmCollectionName())) {
						// 未确认收款价格，可以修改订单收款价格
						co.setPrice(price);
					}
					if (co.getDisCar() == null && co.getStatus() == (OrderStatus.NOT_DIS_CAR)) {
						// 该订单未派车，可以修改下列属性
						co.getCarOrderBase().setCompanyName(companyName);
						co.getCarOrderBase().setBaseUserId(baseUser);
						co.getCarOrderBase().setDutyService(dutyService);
						co.getCarOrderBase().setRouteLink(routeLink);
						co.getCarOrderBase().setServiceMan(serviceMan);
						co.setStime(DateUtils.strToDate(stime));
						co.setEtime(DateUtils.strToDate(etime));
						co.setNeedSeats(needSeats);
					}
					carOrderDao.update(co);
				}
				U.log(log, "单位-编辑订单-多个订单-修改子订单信息成功");
			}
		//	carOrderDao.getCurrentSession().clear();
			// 子订单的信息修改完成，更新主订单
			String hql = "from CarOrder where mainOrderNum = ?0 and isDel = 0";
			List<CarOrder> list = carOrderDao.findhqlList(hql, mainOrderNum);
			mcarOrderDao.modifyMainOrder(reqsrc, list, null, mainOrderNum, 2);
			U.log(log, "单位-编辑订单-主订单信息更新成功");
			U.setPut(map, 1, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getAllCarOrderByMainOrderNum(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "单位-订单-获取主订单下所有子订单", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (fg) {
				if (!jsonObject.containsKey("mainOrderNum")) {
					U.logFalse(log, "单位-订单-获取主订单下所有子订单-传入参数需包含mainOrderNum");
					fg = U.setPutFalse(map, 0, "mainOrderNum不能为空");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(jsonObject.getString("mainOrderNum"))) {
					U.logFalse(log, "单位-订单-获取主订单下所有子订单-mainOrderNum不能为空");
					fg = U.setPutFalse(map, 0, "mainOrderNum不能为空");
				}
			}
			if (fg) {
				String hql = "from CarOrder where mainOrderNum = ?0 and isDel = 0";
				List<CarOrder> carOrders = carOrderDao.findhqlList(hql, jsonObject.getString("mainOrderNum"));
				// / List<CarOrder> carOrders =
				// carOrderDao.findListByField("mainOrderNum",
				// jsonObject.getString("mainOrderNum"));
				U.log(log, "单位-订单-获取主订单下所有子订单-查找成功");
				// 解决懒加载问题
				 Collections.sort(carOrders, new Comparator<CarOrder>()
				    {
				        public int compare(CarOrder a1, CarOrder a2)
				        {
				                int io1 = a1.getRouteNo();
				                int io2 = a2.getRouteNo();
				                return io1 - io2;
				        }
				    });

				
				
				for (CarOrder order : carOrders) {
					// Hibernate.initialize(order.getDisCars());
					Hibernate.initialize(order.getRouteMps());
					Hibernate.initialize(order.getTrades());
				}

				map.put("data", carOrders);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> addOnewayTransferOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, Customer lcus, OrderSource ordersrc, String selCarId, String linkPhone,
			String upCarCount, String note, String couponId) {
		String logtxt = U.log(log, "添加-单程接送-订单", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		// String hql = "";
		boolean fg = false;

		try {
			String lname = "", lphone = "", realName = "";// 默认发送短信手机号
			if (fg) {
				if (lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]为空");
				} else {
					lname = lcus.getBaseUserId().getUname();
					lphone = lcus.getBaseUserId().getPhone();
					realName = lphone + "【" + lcus.getBaseUserId().getRealName() + "】";

					U.log(log, "[用户名] lname=" + lname + ", [用户手机号] lphone=" + lphone + ", [真实姓名] realName=" + realName);
				}
			}

			CarPrice cp = null;
			OrderParam op = null;
			if (fg) {
				if (StringUtils.isEmpty(selCarId)) {
					fg = U.setPutFalse(map, "[车辆价格对象id]不能为空");
				} else {
					selCarId = selCarId.trim();
					if (!FV.isLong(selCarId)) {
						fg = U.setPutFalse(map, "[车辆价格对象id]格式错误");
					} else {
						cp = carPriceDao.findByField("id", Long.parseLong(selCarId));
						if (cp == null) {
							fg = U.setPutFalse(map, "[车辆价格对象]不存在");
						} else {
							op = orderParamDao.findByField("id", cp.getOpid());
							if (op == null) {
								fg = U.setPutFalse(map, "[订单参数对象]不存在");
							}
						}
					}

					U.log(log, "[订单参数对象id] selCarId=" + selCarId);
				}
			}

			CompanyUser comUser = null;
			if (fg) {
				comUser = companyUserDao.findByField("unitNum", op.getCompanyNum());
				if (comUser == null) {
					fg = U.setPutFalse(map, "当前下单单位已不存在");
				} else {
					U.log(log, "[下单单位名称] companyName=" + comUser.getCompanyName());
				}
			}

			List<RouteMapPoint> routeMps = new ArrayList<RouteMapPoint>();
			if (fg) {
				String [] sarea=op.getSpoint().split("=")[2].split("-");
				MapPoint sp = new MapPoint();
				sp.setAddress(op.getSpoint().split("=")[0]);
				sp.setLngLat(op.getSpoint().split("=")[1]);
				sp.setCounty(sarea[2]);
				sp.setCity(sarea[0]+"-"+sarea[1]);
				sp.setLng(Double.parseDouble(sp.getLngLat().split(",")[0]));
				sp.setLat(Double.parseDouble(sp.getLngLat().split(",")[1]));
				U.log(log, "保存-[地图起点]数据-完成，并未保存至数据库");

				RouteMapPoint rmp_sp = new RouteMapPoint();
				rmp_sp.setMapPoint(sp);
				rmp_sp.setSortNo(1);
				rmp_sp.setPtype(PointType.UP_POINT);
				routeMps.add(rmp_sp);
				U.log(log, "保存-[行程起点]数据-完成，并未保存至数据库");
				
				String [] earea=op.getEpoint().split("=")[2].split("-");
				MapPoint ep = new MapPoint();
				ep.setAddress(op.getEpoint().split("=")[0]);
				ep.setLngLat(op.getEpoint().split("=")[1]);
				ep.setCounty(earea[2]);
				ep.setCity(earea[0]+"-"+earea[1]);
				ep.setLng(Double.parseDouble(ep.getLngLat().split(",")[0]));
				ep.setLat(Double.parseDouble(ep.getLngLat().split(",")[1]));
				U.log(log, "保存-[地图终点]数据-完成，并未保存至数据库");

				RouteMapPoint rmp_ep = new RouteMapPoint();
				rmp_ep.setMapPoint(ep);
				rmp_ep.setSortNo(2);
				rmp_ep.setPtype(PointType.DOWN_POINT);
				routeMps.add(rmp_ep);
				U.log(log, "保存-[行程终点]数据-完成，并未保存至数据库");
			}

			RouteLineInfo rli = null;
			if (fg) {
				rli = new RouteLineInfo();
				int dayNum = Integer.parseInt(DateUtils.getDaysOfTowDiffDate(op.getStime(), op.getEtime()) + "");
				rli.setDayNum(dayNum);
				rli.setDistance(op.getDistance());
				rli.setRouteTime(op.getRouteTime());
				U.log(log, "保存-[行程线路信息]数据-完成，并未保存至数据库");
			}

			RouteStationInfo rsi = null;
			if (fg) {
				rsi = new RouteStationInfo();
				if (StringUtils.isNoneBlank(op.getFlightNum())) {
					rsi.setNum(op.getFlightNum());
					rsi.setSeltime(op.getFdtime());
					U.log(log, "保存-[行程站点信息]数据-完成，并未保存至数据库");
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(linkPhone)) {
					fg = U.setPutFalse(map, "[联系人手机号]不能为空");
				} else {
					linkPhone = linkPhone.trim();
					if (!FV.isPhone(linkPhone)) {
						fg = U.setPutFalse(map, "[联系人手机号]应为11位手机号");
					}

					U.log(log, "[帮人叫车信息] linkPhone=" + linkPhone);
				}
			}

			int _upCarCount = 0;
			if (fg) {
				if (StringUtils.isEmpty(upCarCount)) {
					U.log(log, "[上车人数]为空");
				} else {
					upCarCount = upCarCount.trim();
					if (!FV.isPosInteger(upCarCount)) {
						fg = U.setPutFalse(map, "[上车人数]格式错误");
					} else {
						_upCarCount = Integer.parseInt(upCarCount);
					}

					U.log(log, "[上车人数] upCarCount=" + upCarCount);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(note)) {
					U.log(log, "[订单备注]为空");
				} else {
					note = note.trim();
					if (note.length() > 100) {
						fg = U.setPutFalse(map, "[[订单备注]最多填写100个字");
					}

					U.log(log, "[[订单备注] note=" + note);
				}
			}

			double cdUseMoney = 0d;
			CompanyDiscount ctd = null;
			if (fg) {
				if (StringUtils.isEmpty(couponId)) {
					U.log(log, "[优惠券]为空");
				} else {
					couponId = couponId.trim();
					if (!FV.isLong(couponId)) {
						fg = U.setPutFalse(map, "[优惠券id]格式错误");
					} else if (op.getCouponId() == 0 || Long.parseLong(couponId) != op.getCouponId()) {
						fg = U.setPutFalse(map, "请重新选择[优惠券]");
					} else {
						ctd = companyDiscountDao.findByField("id", op.getCouponId());
						if (ctd == null) {
							fg = U.setPutFalse(map, "使用的[优惠券]不存在");
						} else if (ctd.getUseState() >= 1) {
							fg = U.setPutFalse(map, "该[优惠券]已被使用");
						} else {
							cdUseMoney = ctd.getHighMoney();
						}
					}

					U.log(log, "[优惠券id] couponId=" + couponId);
				}
			}

			if (fg) {
				double orderPrice = cp.getPrice();// 实际订单价格，初始为选择车辆金额

				Date atime = new Date();
				String orderNum = UT.creatOrderNum(1, atime);
				DiscountDetail dd = (DiscountDetail) U.toJsonBean(cp.getDisJson(), DiscountDetail.class);
				dd.setOrderNum(orderNum);
				if (ctd != null) {
					dd.setCouponId(ctd.getId()); // 设置使用优惠券id
					dd.setCouponMoney(cdUseMoney); // 设置使用优惠券金额
				}
				discountDetailDao.save(dd);
				U.log(log, "添加-接送机订单-优惠详情-完成");

				// 计算新价格
				orderPrice = MathUtils.sub(orderPrice, dd.getCouponMoney(), 2);
				U.log(log, "选车初始单价[" + cp.getPrice() + "] - 使用优惠券金额[" + dd.getCouponMoney() + "] = " + orderPrice);
				if (orderPrice <= 0)
					orderPrice = 0.01;// 订单金额至少为0.01
				U.log(log, "最终价格 = " + orderPrice);
				/********** 行程基本信息 ********/
				CarOrderBase cob = new CarOrderBase();
				cob.setUnitNum(op.getCompanyNum());
				cob.setBaseUserId(lcus.getBaseUserId());
				cob.setCompanyName(comUser.getCompanyName());
				cob.setDutyService(lcus.getBaseUserId().getRealName());
				cob.setServiceMan(cpyCusDao.getOrderServeMan(comUser.getUnitNum(), lcus.getBaseUserId().getUname()));
				cob.setOrderSource(ordersrc);
				cob.setRouteType(RouteType.ONE_WAY);
				cobDao.save(cob);
				/********** 行程基本信息 ********/
				CarOrder co = new CarOrder();
				co.setCarOrderBase(cob);
				;
				co.setOrderNum(orderNum);
				co.setBackRelNum(op.getBackRelNum());

				// 将行程地点数据保存至数据库
				if (routeMps.size() > 0) {
					for (RouteMapPoint rmp : routeMps) {
						mapPointDao.save(rmp.getMapPoint());
						routeMapPointDao.save(rmp);
						U.log(log, "保存行程[" + rmp.getPtype().getText() + "]完成");
					}
					co.setRouteMps(routeMps);
				}

				co.setStime(op.getStime());
				co.setEtime(op.getEtime());
				co.setNeedCars(1);
				co.setNeedSeats(cp.getSeat());
				co.setRealSeats(_upCarCount);
				co.setStatus(OrderStatus.NOT_DIS_CAR);
				co.setPayStatus(OrderPayStatus.UNPAID);
				// co.setServiceMan(myCustomerDao.getOrderServeMan(op.getCompanyNum(),
				// lphone));
				co.setPrice(cp.getPrice());
				co.setRouteLineInfo(rli);
				co.setIsShuttle(op.getIsShuttle());
				co.setRouteStationInfo(rsi);
				co.setServiceType(op.getServiceType());
				co.setAddTime(atime);
				carOrderDao.save(co);

				// 优惠券使用后，修改优惠券数据
				if (ctd != null) {
					ctd.setUseMoney(MathUtils.add(ctd.getUseMoney(), dd.getCouponMoney(), 2));
					ctd.setUseState(1);// 已使用1次
					ctd.setUseTime(DateUtils.DateToStr(atime));
					companyDiscountDao.update(ctd);

					U.log(log, "更新优惠券数据-完成");
				}

				/********** 发送微信公众号通知消息--begin *******************/
				// List<OperatorList> ols =
				// operatorListDao.getTeamAllJd(co.getCompanyNum());
				// for(OperatorList o : ols){
				// if(StringUtils.isNotBlank(o.getBaseUserId().getUname())){
				// wxPublicDataDao.jdOfWxmsg(null, co,
				// o.getBaseUserId().getUname());
				// }
				// }
				/********** 发送微信公众号通知消息--end *******************/

				map.put("id", co.getId());
				map.put("orderNum", co.getOrderNum());

				String msg = StringUtils.isEmpty(op.getBackRelNum()) ? "添加订单成功" : "添加返程订单成功";
				U.setPut(map, 1, msg);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> addLybcOrder(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,

			String unitNum, Customer lcus, OrderSource ordersrc, String id, String isBill) {

		String logtxt = U.log(log, "添加-旅游包车订单", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			CompanyUser comUser = null;

			if (fg) {
				if (StringUtils.isEmpty(unitNum)) {

					fg = U.setPutFalse(map, "[车队编号]不能为空");
				} else {
					comUser = companyUserDao.findByField("unitNum", unitNum);
					if (comUser == null) {
						fg = U.setPutFalse(map, "[当前车队]不存在");
					}

					U.log(log, "[登录车队编号] unitNum=" + unitNum);

				}
			}

			if (fg) {
				if (lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				} else {
					U.log(log, "[登录用户名] uname=" + lcus.getBaseUserId().getUname());
				}
			}

			BcCarPrice bcp = null;
			if (fg) {
				if (StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[车辆价格对象id]不能为空");
				} else {
					id = id.trim();
					if (!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[车辆价格对象id]格式错误");
					} else {
						bcp = bcCarPriceDao.findByField("id", Long.parseLong(id));
						if (bcp == null) {
							fg = U.setPutFalse(map, "选择的车辆价格数据已不存在，请重新选择");
						}
					}

					U.log(log, "[车辆价格对象id] id=" + id);
				}
			}

			int _isBill = 0;
			if (fg) {
				if (StringUtils.isEmpty(isBill)) {
					fg = U.setPutFalse(map, "[是否开发票]不能为空");
				} else {
					if (!FV.isInteger(isBill)) {
						fg = U.setPutFalse(map, "[是否开发票]格式错误");
					} else {
						_isBill = Integer.parseInt(isBill);
					}

					U.log(log, "[是否开发票] isBill=" + isBill);
				}
			}

			List<BcOrderParam> list = new ArrayList<BcOrderParam>();
			if (fg) {
				list = bcOrderParamDao.findBcOrderParamList(bcp.getMainOrderNum());
				if (list.size() == 0) {
					fg = U.setPutFalse(map, "天数行程已不存在，请重新下单");
				}

				U.log(log, "存在" + list.size() + "条天数行程");
			}

			if (fg) {
				// 获取业务员
				// String serviceMan =
				// myCustomerDao.getOrderServeMan(comUser.getUnitNum(),
				// lcus.getBaseUserId().getPhone());
				String serviceMan = cpyCusDao.getOrderServeMan(comUser.getUnitNum(), lcus.getBaseUserId().getUname());
				String[] price = bcp.getPriceDetail().split(";");
				/********** 行程基本信息 ********/
				CarOrderBase cob = new CarOrderBase();
				cob.setUnitNum(comUser.getUnitNum());
				cob.setBaseUserId(lcus.getBaseUserId());
				cob.setCompanyName(comUser.getCompanyName());
				cob.setDutyService(lcus.getBaseUserId().getRealName());
				cob.setServiceMan(serviceMan);
				cob.setOrderSource(ordersrc);
				cob.setRouteType(RouteType.TRAVEL_BC);
				cobDao.save(cob);
				/********** 行程基本信息 ********/
				for (int i = 0; i < list.size(); i++) {
					BcOrderParam o = list.get(i);

					Date atime = new Date();
					String orderNum = UT.creatOrderNum(2, atime);

					CarOrder co = new CarOrder();
					co.setCarOrderBase(cob);
					co.setMainOrderNum(bcp.getMainOrderNum());
					co.setOrderNum(orderNum);

					// 将行程地点数据保存至数据库
					List<RouteMapPoint> routeMps = new ArrayList<RouteMapPoint>();
					RouteLineInfo rli = null;
					UT.setLybcOrderRouteDat(o, routeMps, rli);
					if (routeMps.size() > 0) {
						for (RouteMapPoint rmp : routeMps) {
							mapPointDao.save(rmp.getMapPoint());
							routeMapPointDao.save(rmp);
							U.log(log, "保存行程[" + rmp.getPtype().getText() + "]完成");
						}
						co.setRouteMps(routeMps);
					}

					co.setStime(o.getStime());
					co.setEtime(o.getEtime());
					co.setNeedCars(1);
					co.setNeedSeats(bcp.getSeat());
					co.setRealSeats(bcp.getSeat());
					co.setStatus(OrderStatus.NOT_DIS_CAR);
					co.setPayStatus(OrderPayStatus.UNPAID);
					if (_isBill == 0) {
						co.setPrice(Double.parseDouble(price[i].split("=")[0]));
					} else {
						co.setPrice(Double.parseDouble(price[i].split("=")[1]));
					}
					co.setRouteLineInfo(rli);
					co.setIsShuttle(2);
					co.setRouteStationInfo(null);
					co.setServiceType(o.getServiceType());
					co.setAddTime(atime);
					carOrderDao.save(co);

					U.log(log, "添加第" + o.getRouteNo() + "天的行程-完成");
				}

				map.put("mainOrderNum", bcp.getMainOrderNum());

				U.setPut(map, 1, "添加-旅游包车订单-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> addCompanyLybcOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, String companyName, String unitNum, String companyCusId, String serviceMan,
			String dutyService, String routeLink, String mainOrderNum) {
		String logtxt = U.log(log, "单位添加-旅游包车订单", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			CompanyCustom cpyCus = null;
			if (fg) {
				if (StringUtils.isEmpty(companyCusId)) {
					fg = U.setPutFalse(map, "[客户id]不能为空");
				} else {
					companyCusId = companyCusId.trim();
					if (!FV.isLong(companyCusId)) {
						fg = U.setPutFalse(map, "[客户id]格式错误");
					} else {
						cpyCus = cpyCusDao.findByField("id", Long.parseLong(companyCusId));
						if (cpyCus == null) {
							fg = U.setPutFalse(map, "客户不存在");
						}
					}

					U.log(log, "[客户id] id=" + companyCusId);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(serviceMan)) {
					fg = U.setPutFalse(map, "[业务员]不能为空");
				} else {
					U.log(log, "[业务员] serviceMan=" + serviceMan);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(dutyService)) {
					fg = U.setPutFalse(map, "[业务负责人]不能为空");
				} else {
					U.log(log, "[业务负责人] dutyService=" + dutyService);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(routeLink)) {
					fg = U.setPutFalse(map, "[乘车联系人]不能为空");
				} else {
					routeLink = routeLink.trim();
					if (!FV.isPhone(routeLink.split("-")[1])) {
						fg = U.setPutFalse(map, "[联系人手机号]应为11位手机号");
					}

					U.log(log, "[乘车联系人] routeLink=" + routeLink);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(mainOrderNum)) {
					fg = U.setPutFalse(map, "[主订单编号]不能为空");
				} else {
					MainCarOrder isExit = mcarOrderDao.findByField("orderNum", mainOrderNum);
					if (isExit != null) {
						fg = U.setPutFalse(map, "[该主订单]已存在");
					}
					U.log(log, "[主订单编号] mainOrderNum=" + mainOrderNum);
				}
			}
			List<CompanyOrderTemp> list = new ArrayList<CompanyOrderTemp>();
			if (fg) {
				list = cotDao.findBcOrderParamList(mainOrderNum);
				if (list.size() == 0) {
					fg = U.setPutFalse(map, "天数行程已不存在，请重新下单");
				}

				U.log(log, "存在" + list.size() + "条天数行程");
			}

			if (fg) {
				/********** 行程基本信息 ********/
				CarOrderBase cob = new CarOrderBase();
				cob.setUnitNum(unitNum);
				cob.setBaseUserId(cpyCus.getBaseUserId());
				cob.setCompanyName(companyName);
				cob.setDutyService(dutyService);
				cob.setRouteLink(routeLink);
				cob.setOrderSource(OrderSource.PC_COMPANY);
				cob.setServiceMan(serviceMan);
				cob.setRouteType(RouteType.TRAVEL_BC);
				cob.setStatus(MainOrderStatus.NOT_CONFIRM);
				cobDao.save(cob);
				/********** 行程基本信息 ********/

				List<CarOrder> coist = new ArrayList<CarOrder>();
				CompanyOrderTemp cot = null;
				for (int i = 0; i < list.size(); i++) {
					cot = list.get(i);
					Date atime = new Date();
					String orderNum = UT.creatOrderNum(2, atime);

					CarOrder co = new CarOrder();
					co.setCarOrderBase(cob);
					co.setMainOrderNum(mainOrderNum);
					co.setRouteNo(cot.getRouteNo());
					co.setOrderNum(orderNum);

					// 将行程地点数据保存至数据库
					List<RouteMapPoint> routeMps = new ArrayList<RouteMapPoint>();
					RouteLineInfo rli = new RouteLineInfo();
					UT.setLybcOrderRouteCompany(cot, routeMps, rli);
					if (routeMps.size() > 0) {
						co.setRouteMps(routeMps);
					}

					co.setStime(cot.getStime());
					co.setEtime(cot.getEtime());
					co.setNeedCars(cot.getNeedCars());
					co.setNeedSeats(cot.getSeats());
					co.setRealSeats(cot.getSeats());
					co.setStatus(OrderStatus.NOT_DIS_CAR);
					co.setPayStatus(OrderPayStatus.UNPAID);
					co.setPrice(cot.getRoutePrice());
					co.setRouteLineInfo(rli);
					co.setIsShuttle(2);
					co.setRouteStationInfo(null);
					co.setServiceType(cot.getServiceType());
					co.setIsHighSpeed(cot.getIsHighSpeed());
					co.setLimitNum(cot.getLimitNum());
					co.setRemDriverCharge(cot.getRemindRouteCash());
					co.setOtherPrice(cot.getOtherPrice());
					co.setOtherPriceNote(cot.getOtherPriceNote());
					if (StringUtils.isNotBlank(cot.getRouteDetail())) {
						co.setRouteDetail(cot.getRouteDetail());
					}
					co.setNote(cot.getNote());
					co.setAddTime(new Date());
					carOrderDao.save(co);
					// 加入主订单
					coist.add(co);
					U.log(log, "添加第" + cot.getRouteNo() + "天的行程-完成");
				}

				/************** 添加主行程信息*******-s *******/
				MainCarOrder mco = new MainCarOrder();// 生成主订单
				mco.setMainOrderBase(cob);
				mco.setOrderNum(mainOrderNum);
				mcarOrderDao.modifyMainOrder(reqsrc, coist, mco, mainOrderNum, 1);
				/************** 添加主行程信息*******-e *******/
				map.put("mainOrderNum", cot.getMainOrderNum());

				U.setPut(map, 1, "添加-旅游包车订单-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> addCompanyOnewayTransferOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject, String companyName, String unitNum) {
		String logtxt = U.log(log, "单位添加-单程接送-订单", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			String companyCusId = jsonObject.getString("companyCusId");// 下单客户id
			String serviceMan = jsonObject.getString("serviceMan");// 下单业务员姓名
			String dutyService = jsonObject.getString("dutyService");// 客户业务负责人姓名
			String stime = "", fdTime = "", spoint = "", epoint = "", wpoints = "", isHighSpeed = "", otherPrice = "",
					otherPriceNote = "", routePrice = "", remindRouteCash = "", limitNum = "", seats = "", cars = "",
					reasonTime = "", note = "", routeDetail = "", routeLink = "", isShuttle = "", wayNum = "",
					mainOrderNum = "", routeNo = "";
			/** 去程参数 */
			stime = jsonObject.getString("stime");// 出发时间
			fdTime = jsonObject.getString("fdTime");// 起飞/降落时间
			spoint = jsonObject.getString("spoint");// 出发地点
			epoint = jsonObject.getString("epoint");// 到达地点
			wpoints = jsonObject.getString("wpoints");// 途径地点
			isHighSpeed = jsonObject.getString("isHighSpeed");// 是否走高速
			otherPrice = jsonObject.getString("otherPrice");// 其他费用
			otherPriceNote = jsonObject.getString("otherPriceNote");// 其他费用说明
			routePrice = jsonObject.getString("routePrice");// 行程价格
			remindRouteCash = jsonObject.getString("remindRouteCash");// 提醒师傅现收
			limitNum = jsonObject.getString("limitNum");// 限号
			seats = jsonObject.getString("seats");// 座位数
			cars = jsonObject.getString("cars");// 车辆数
			reasonTime = jsonObject.getString("reasonTime");// 其他原因延长时间
			note = jsonObject.getString("note");// 备注
			routeDetail = jsonObject.getString("routeDetail");// 行程详情
			routeLink = jsonObject.getString("routeLink");// 乘车联系人
			isShuttle = jsonObject.getString("isShuttle");// 0-接；1-送；
			wayNum = jsonObject.getString("wayNum");// 航班号/车次号
			mainOrderNum = jsonObject.getString("mainOrderNum");// 主订单编号
			routeNo = jsonObject.getString("routeNo");// 行程数 1去程 2返程
			/** 去程参数 */

			String lname = "", lphone = "", realName = "";// 默认发送短信手机号
			CompanyCustom cpyCus = null;
			if (fg) {
				if (StringUtils.isEmpty(companyCusId)) {
					fg = U.setPutFalse(map, "[客户id]不能为空");
				} else {
					companyCusId = companyCusId.trim();
					if (!FV.isLong(companyCusId)) {
						fg = U.setPutFalse(map, "[客户id]格式错误");
					} else {
						cpyCus = cpyCusDao.findByField("id", Long.parseLong(companyCusId));
						if (cpyCus == null) {
							fg = U.setPutFalse(map, "客户不存在");
						} else {
							lname = cpyCus.getBaseUserId().getUname();
							lphone = cpyCus.getBaseUserId().getPhone();
							realName = lphone + "【" + cpyCus.getBaseUserId().getRealName() + "】";

							U.log(log, "[用户名] lname=" + lname + ", [用户手机号] lphone=" + lphone + ", [真实姓名] realName="
									+ realName);
						}
					}

					U.log(log, "[客户id] id=" + companyCusId);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(serviceMan)) {
					fg = U.setPutFalse(map, "[业务员]不能为空");
				} else {
					U.log(log, "[业务员] serviceMan=" + serviceMan);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(dutyService)) {
					fg = U.setPutFalse(map, "[业务负责人]不能为空");
				} else {
					U.log(log, "[业务负责人] dutyService=" + dutyService);
				}
			}
			Date _stime = null;
			if (fg) {
				if (StringUtils.isEmpty(stime)) {
					fg = U.setPutFalse(map, "[出发时间]不能为空");
				} else {
					stime = stime.trim();
					if (!FV.isDate(stime)) {
						fg = U.setPutFalse(map, "[出发时间]格式错误");
					} else {
						_stime = DateUtils.strToDate(stime);
					}

					U.log(log, "[出发时间] stime=" + stime);
				}
			}
			Date _fdTime = null;
			if (fg) {
				if (StringUtils.isEmpty(fdTime)) {
					U.log(log, "[起飞/降落时间为空] fdTime=" + fdTime);
				} else {
					fdTime = fdTime.trim();
					if (!FV.isDate(fdTime)) {
						fg = U.setPutFalse(map, "[起飞/降落时间]格式错误");
					} else {
						_fdTime = DateUtils.strToDate(fdTime);
					}

					U.log(log, "[起飞/降落时间为空] fdTime=" + fdTime);
				}
			}
			// 保存地点 省市县
			List<String> cs = new ArrayList<String>();
			String slnglat = "";
			if (fg) {
				if (StringUtils.isEmpty(spoint)) {
					fg = U.setPutFalse(map, "[出发地点]不能为空");
				} else {
					spoint = spoint.trim();
					if (spoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[出发地点]格式错误");
					} else {
						slnglat = spoint.split("=")[1];
						cs.add(spoint.split("=")[2]);

						U.log(log, "[出发地点坐标] slnglat=" + slnglat);
					}

					U.log(log, "[出发地点] spoint=" + spoint);
				}
			}

			String elnglat = "";
			if (fg) {
				if (StringUtils.isEmpty(epoint)) {
					fg = U.setPutFalse(map, "[到达地点]不能为空");
				} else {
					epoint = epoint.trim();
					if (epoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[到达地点]格式错误");
					} else {
						elnglat = epoint.split("=")[1];
						cs.add(epoint.split("=")[2]);

						U.log(log, "[到达地点坐标] elnglat=" + elnglat);
					}

					U.log(log, "[到达地点] epoint=" + epoint);
				}
			}

			String wlnglat = "";
			if (fg) {
				if (StringUtils.isEmpty(wpoints)) {
					U.log(log, "[途径地点]为空");
				} else {
					wpoints = wpoints.trim();
					if (wpoints.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[途径地点]格式错误");
					} else {
						String[] wps = wpoints.split(";");
						for (int i = 0; i < wps.length; i++) {
							wlnglat += wps[i].split("=")[1] + ";";
							cs.add(wps[i].split("=")[2]);
						}

						U.log(log, "[途径地点坐标] wlnglat=" + wlnglat);
					}

					U.log(log, "[途径地点] wpoints=" + wpoints);
				}
			}
			ServiceType serviceType = null;
			if (fg) {
				serviceType = Util.getRouteServiceType(cs);

				U.log(log, "[行程业务类型] serviceType=" + serviceType);
			}
			int _isHighSpeed = 0;
			if (fg) {
				if (StringUtils.isEmpty(isHighSpeed)) {
					U.log(log, "[是否走高速]为空");
				} else {
					isHighSpeed = isHighSpeed.trim();
					if (!FV.isInteger(isHighSpeed)) {
						fg = U.setPutFalse(map, "[是否走高速]格式错误");
					} else {
						_isHighSpeed = Integer.parseInt(isHighSpeed);
						// 不为1，则表示不走高速
						if (_isHighSpeed != 1)
							_isHighSpeed = 0;
					}

					U.log(log, "[是否走高速] isHighSpeed=" + isHighSpeed);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(routePrice)) {
					fg = U.setPutFalse(map, "[行程价格]不能为空");
				} else {
					routePrice = routePrice.trim();
					if (!FV.isDouble(routePrice)) {
						fg = U.setPutFalse(map, "[行程价格]格式错误");
					}

					U.log(log, "[行程价格] routePrice=" + routePrice);
				}
			}

			if (fg) {
				if (StringUtils.isNotBlank(otherPrice)) {
					routePrice = routePrice.trim();
					if (!FV.isDouble(otherPrice)) {
						fg = U.setPutFalse(map, "[其他费用]格式错误");
					}
				}
				U.log(log, "[其他费用] otherPrice=" + otherPrice);
			}

			if (fg) {
				if (StringUtils.isNotBlank(remindRouteCash)) {
					remindRouteCash = remindRouteCash.trim();
					if (!FV.isDouble(remindRouteCash)) {
						fg = U.setPutFalse(map, "[师傅现收金额]格式错误");
					}
				}
				U.log(log, "[师傅现收] remindRouteCash=" + remindRouteCash);
			}

			if (fg) {
				if (StringUtils.isEmpty(seats)) {
					fg = U.setPutFalse(map, "[座位数]不能为空");
				} else {
					seats = seats.trim();
					if (!FV.isInteger(seats)) {
						fg = U.setPutFalse(map, "[座位数]格式错误");
					}

					U.log(log, "[座位数] seats=" + seats);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(cars)) {
					fg = U.setPutFalse(map, "[车辆数]不能为空");
				} else {
					cars = cars.trim();
					if (!FV.isInteger(cars)) {
						fg = U.setPutFalse(map, "[车辆数]格式错误");
					}

					U.log(log, "[车辆数] cars=" + cars);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(routeLink)) {
					fg = U.setPutFalse(map, "[乘车联系人]不能为空");
				} else {
					routeLink = routeLink.trim();
					if (!FV.isPhone(routeLink.split("-")[1])) {
						fg = U.setPutFalse(map, "[联系人手机号]应为11位手机号");
					}

					U.log(log, "[乘车联系人] routeLink=" + routeLink);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(isShuttle)) {
					fg = U.setPutFalse(map, "[接送类型]不能为空");
				} else {
					isShuttle = isShuttle.trim();
					if (!FV.isInteger(isShuttle)) {
						fg = U.setPutFalse(map, "[接送类型]格式错误");
					}

					U.log(log, "[接送类型] isShuttle=" + isShuttle);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(routeDetail)) {
					U.log(log, "[订单备注]为空");
				} else {
					routeDetail = routeDetail.trim();
					if (routeDetail.length() > 200) {
						fg = U.setPutFalse(map, "[行程详情]最多填写200个字");
					}

					U.log(log, "[行程详情] routeDetail=" + routeDetail);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(note)) {
					U.log(log, "[订单备注]为空");
				} else {
					note = note.trim();
					if (note.length() > 100) {
						fg = U.setPutFalse(map, "[[订单备注]最多填写100个字");
					}

					U.log(log, "[[订单备注] note=" + note);
				}
			}

			if (fg) {
				if (StringUtils.isEmpty(routeNo)) {
					fg = U.setPutFalse(map, "[行程数]不能为空");
				} else {
					isShuttle = isShuttle.trim();
					if (!FV.isInteger(isShuttle)) {
						fg = U.setPutFalse(map, "[行程数]格式错误");
					}

					U.log(log, "[行程数] routeNo=" + routeNo);
				}
			}
			CarRouteRes mp = null;
			Date _etime = null;
			if (fg) {
				mp = commonDao.queryCarRouteRes(slnglat, elnglat, wlnglat, (_isHighSpeed == 1 ? "19" : "10"));
				if (mp == null) {
					fg = U.setPutFalse(map, "获取行程距离失败，请重新获取");
				} else {
					// 结束时间=开始时间+行程耗时+其他原因增加时间
					_etime = DateUtils.getPlusSecondsDate(DateUtils.yyyy_MM_dd_HH_mm_ss, _stime, mp.getTimeCons());
				}
			}
			if (fg) {
				if (StringUtils.isNotBlank(reasonTime)) {
					_etime = DateUtils.getPlusSecondsDate(DateUtils.yyyy_MM_dd_HH_mm_ss, _etime,
							(int) MathUtils.mul(Double.valueOf(reasonTime), 3600, 2));
				}
			}
			RouteLineInfo rli = null;
			if (fg) {
				rli = new RouteLineInfo();
				int dayNum = Integer.parseInt(DateUtils.getDaysOfTowDiffDate(_stime, _etime) + "");
				rli.setDayNum(dayNum);
				rli.setDistance(MathUtils.div(mp.getDistance(), 1000, 2));
				rli.setRouteTime((int) MathUtils.div(mp.getTimeCons(), 60, 2));
				U.log(log, "保存-[行程线路信息]数据-完成，并未保存至数据库");
			}

			RouteStationInfo rsi = null;
			if (fg) {
				if (StringUtils.isNotBlank(wayNum)) {
					rsi = new RouteStationInfo();
					rsi.setNum(wayNum);
					rsi.setSeltime(_fdTime);
					U.log(log, "保存-[行程站点信息]数据-完成，并未保存至数据库");
				}
			}
			MainCarOrder mco = null;
			CarOrderBase cob = null;
			CarOrder co = null;
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {// 添加去程时没有主订单编号
					mainOrderNum = UT.creatMainOrderNum(1);
					mco = new MainCarOrder();
					mco.setOrderNum(mainOrderNum);
					/********** 行程基本信息****-s ****/
					cob = new CarOrderBase();
					cob.setUnitNum(unitNum);
					cob.setBaseUserId(cpyCus.getBaseUserId());
					cob.setCompanyName(companyName);
					cob.setDutyService(dutyService);
					cob.setRouteLink(routeLink);
					cob.setOrderSource(OrderSource.PC_COMPANY);
					cob.setServiceMan(serviceMan);
					cob.setRouteType(RouteType.ONE_WAY);
					cob.setStatus(MainOrderStatus.NOT_CONFIRM);
					cobDao.save(cob);
					mco.setMainOrderBase(cob);
					/********** 行程基本信息****-e ****/
				} else {// 返程时已有主订单编号
					String hql = "from CarOrder where mainOrderNum=?0 and routeNo=?1";
					CarOrder isExit = carOrderDao.findObj(hql, mainOrderNum, Integer.parseInt(routeNo));
					if (isExit != null) {
						fg = U.setPutFalse(map, "当前[行程数]已存在");
					} else {
						mco = mcarOrderDao.findByField("orderNum", mainOrderNum);
						cob = mco.getMainOrderBase();
					}
				}
			}
			if (fg) {
				/************** 子行程信息*******-s *******/
				co = new CarOrder();
				co.setCarOrderBase(cob);
				co.setMainOrderNum(mainOrderNum);
				co.setOrderNum(UT.creatOrderNum(1, new Date()));
				co.setRouteNo(Integer.parseInt(routeNo));
				co.setNeedCars(Integer.parseInt(cars));

				/********* 行程地点数据*****start ****/
				List<RouteMapPoint> routeMps = new ArrayList<RouteMapPoint>();
				String [] sarea=spoint.split("=")[2].split("-");
				MapPoint sp = new MapPoint();
				sp.setAddress(spoint.split("=")[0]);
				sp.setLngLat(spoint.split("=")[1]);
				sp.setCounty(sarea[2]);
				sp.setCity(sarea[0]+"-"+sarea[1]);
				sp.setLng(Double.parseDouble(sp.getLngLat().split(",")[0]));
				sp.setLat(Double.parseDouble(sp.getLngLat().split(",")[1]));
				U.log(log, "保存-[地图起点]数据-完成，并未保存至数据库");

				RouteMapPoint rmp_sp = new RouteMapPoint();
				rmp_sp.setMapPoint(sp);
				rmp_sp.setSortNo(1);
				rmp_sp.setPtype(PointType.UP_POINT);
				routeMps.add(rmp_sp);
				U.log(log, "保存-[行程起点]数据-完成，并未保存至数据库");
				
				String [] earea=epoint.split("=")[2].split("-");
				MapPoint ep = new MapPoint();
				ep.setAddress(epoint.split("=")[0]);
				ep.setLngLat(epoint.split("=")[1]);
				ep.setCounty(earea[2]);
				ep.setCity(earea[0]+"-"+earea[1]);
				ep.setLng(Double.parseDouble(ep.getLngLat().split(",")[0]));
				ep.setLat(Double.parseDouble(ep.getLngLat().split(",")[1]));
				U.log(log, "保存-[地图终点]数据-完成，并未保存至数据库");

				RouteMapPoint rmp_ep = new RouteMapPoint();
				rmp_ep.setMapPoint(ep);
				rmp_ep.setSortNo(2);
				rmp_ep.setPtype(PointType.DOWN_POINT);
				routeMps.add(rmp_ep);
				U.log(log, "保存-[行程终点]数据-完成，并未保存至数据库");

				if (StringUtils.isNotEmpty(wpoints)) {
					String[] wps = wpoints.split(";");
					String [] warea=null;
					for (int i = 0; i < wps.length; i++) {
						warea=wps[i].split("=")[2].split("-");
						MapPoint wp = new MapPoint();
						wp.setAddress(wps[i].split("=")[0]);
						wp.setLngLat(wps[i].split("=")[1]);
						wp.setCounty(warea[2]);
						wp.setCity(warea[0]+"-"+warea[1]);
						wp.setLng(Double.parseDouble(wp.getLngLat().split(",")[0]));
						wp.setLat(Double.parseDouble(wp.getLngLat().split(",")[1]));
						U.log(log, "保存-[途径地]数据-完成，并未保存至数据库");

						RouteMapPoint rmp_wp = new RouteMapPoint();
						rmp_wp.setMapPoint(wp);
						rmp_wp.setSortNo(i + 3);
						rmp_wp.setPtype(PointType.WAY_POINT);
						routeMps.add(rmp_wp);
						U.log(log, "保存-[行程途径地]数据-完成，并未保存至数据库");
					}
				}
				// 将行程地点数据保存至数据库
				if (routeMps.size() > 0) {
					co.setRouteMps(routeMps);
				}
				/********* 行程地点数据****end *****/
				co.setStime(_stime);
				co.setEtime(_etime);
				co.setNeedSeats(Integer.parseInt(seats));
				co.setRealSeats(Integer.parseInt(seats));
				co.setStatus(OrderStatus.NOT_DIS_CAR);
				co.setPayStatus(OrderPayStatus.UNPAID);
				co.setRouteLineInfo(rli);
				co.setIsShuttle(Integer.parseInt(isShuttle));
				if (rsi != null)
					co.setRouteStationInfo(rsi);
				co.setServiceType(serviceType);
				if (StringUtils.isNotBlank(limitNum))
					co.setLimitNum(limitNum);
				if (StringUtils.isNotBlank(remindRouteCash))
					co.setRemDriverCharge(Double.valueOf(remindRouteCash));
				if (StringUtils.isNotBlank(otherPrice)) {// 其他费用加入行程价格
					co.setOtherPrice(Double.valueOf(otherPrice));
					co.setPrice((MathUtils.add(Double.valueOf(routePrice), Double.valueOf(otherPrice), 2)));
				} else {
					co.setPrice(Double.valueOf(routePrice));
				}
				if (StringUtils.isNotBlank(otherPriceNote))
					co.setOtherPriceNote(otherPriceNote);
				if (StringUtils.isNotBlank(routeDetail)) {
					co.setRouteDetail(routeDetail);
				}
				if (StringUtils.isNotBlank(note))
					co.setNote(note);
				if (StringUtils.isNotBlank(reasonTime))
					co.setReasonTime(Double.valueOf(reasonTime));
				co.setAddTime(new Date());
				co.setIsHighSpeed(_isHighSpeed);
				carOrderDao.save(co);
				/************** 子行程信息********-e ******/
				/************** 添加主行程信息*******-s *******/
				if ("1".equals(routeNo)) {
					mcarOrderDao.modifyMainOrder(reqsrc, null, mco, mainOrderNum, 1);
				} else {
					mcarOrderDao.modifyMainOrder(reqsrc, null, mco, mainOrderNum, 2);
				}
				/************** 添加主行程信息*******-e *******/
				map.put("mainOrderNum", mainOrderNum);
				U.setPut(map, 1, "订单添加成功");
			}
			/********** 发送微信公众号通知消息--begin *******************/
			// List<OperatorList> ols =
			// operatorListDao.getTeamAllJd(co.getCompanyNum());
			// for(OperatorList o : ols){
			// if(StringUtils.isNotBlank(o.getBaseUserId().getUname())){
			// wxPublicDataDao.jdOfWxmsg(null, co,
			// o.getBaseUserId().getUname());
			// }
			// }
			/********** 发送微信公众号通知消息--end *******************/
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> confirmPayment(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject, Customer customer) {
		String logtxt = U.log(log, "单位订单-确认付款价格", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			String id = jsonObject.getString("id");

			if (fg) {
				if (StringUtils.isBlank(id)) {
					U.logFalse(log, "单位订单-确认付款价格-订单id为空");
					fg = U.setPutFalse(map, 0, "订单id为空");
				}
			}
			if (fg) {
				if (null == customer) {
					U.logFalse(log, "单位订单-确认付款价格-获取账号信息失败");
					fg = U.setPutFalse(map, 0, "获取账号信息失败");
				}
			}
			if (fg) {
				int res = carOrderDao.batchExecute("update CarOrder set confirmPaymentName = ?0 where id = ?1",
						customer.getBaseUserId().getRealName(), Long.parseLong(id));
				if (res > 0) {
					U.log(log, "单位订单-确认付款价格-success");
					U.setPut(map, 1, "确认付款价格成功");
				} else {
					U.logFalse(log, "单位订单-确认付款价格-fail");
					U.setPutFalse(map, 0, "确认付款价格失败");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> confirmUseCar(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "获取-单位管理-确认用车", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();

		boolean fg = true;

		try {
			// 获取主订单id
			String mainOrderId = jsonObject.getString("mainOrderId");
			if (fg) {
				if (StringUtils.isBlank(mainOrderId)) {
					U.logFalse(log, "单位订单-确认用车-主订单id为空");
					fg = U.setPutFalse(map, 0, "主订单id为空");
				}
			}

			MainCarOrder mainCarOrder = mcarOrderDao.findByField("id", Long.parseLong(mainOrderId));

			if (fg) {
				if (mainCarOrder.getMainOrderBase().getStatus() != MainOrderStatus.NOT_CONFIRM) {
					U.logFalse(log, "单位订单-确认用车-主订单未处于[未确认用车]状态");
					fg = U.setPutFalse(map, 0, "主订单未处于[未确认用车状态]");
				}
			}
			if (fg) {
				// 修改主订单状态
				// 主订单状态由 未确认用车 变为 未派车
				mainCarOrder.getMainOrderBase().setStatus(MainOrderStatus.NOT_DIS_CAR);

				String mainOrderNum = mainCarOrder.getOrderNum();
				/*** 生成符合车辆数的子订单 ***/
				List<CarOrder> findListByField = carOrderDao.findListByField("mainOrderNum", mainOrderNum);
				if (null != findListByField) {
					for (CarOrder carOrder : findListByField) {
						// 根据现有子订单生成新的订单
						int needCars = carOrder.getNeedCars();
						if (needCars != 1) {
							// 需要额外生成新的订单;
							for (int i = 1; i <= needCars - 1; i++) {
								CarOrder carOrderForAdd = new CarOrder();
								carOrderForAdd.setAddTime(new Date());
								carOrderForAdd.setAlPayPrice(carOrder.getAlPayPrice());
								carOrderForAdd.setBackRelNum(carOrder.getBackRelNum());
								carOrderForAdd.setCarOrderBase(carOrder.getCarOrderBase());
								carOrderForAdd.setConfirmPaymentName(carOrder.getConfirmPaymentName());
								// carOrderForAdd.setDisCar(carOrder.getDisCar());复制的新订单是没有派车的，因为一辆车不可能跑两个一样的行程
								carOrderForAdd.setDisPrice(carOrder.getDisPrice());
								carOrderForAdd.setEtime(carOrder.getEtime());
								carOrderForAdd.setIsExternal(carOrder.getIsExternal());
								carOrderForAdd.setIsShuttle(carOrder.getIsShuttle());
								carOrderForAdd.setLimitNum(carOrder.getLimitNum());
								carOrderForAdd.setMainOrderNum(carOrder.getMainOrderNum());
								carOrderForAdd.setNeedCars(1);
								carOrderForAdd.setNeedSeats(carOrder.getNeedSeats());
								carOrderForAdd.setNote(carOrder.getNote());
								carOrderForAdd.setOrderNum(
										UT.creatOrderNum(carOrder.getCarOrderBase().getRouteType().getValue(),
												carOrderForAdd.getAddTime()));
								carOrderForAdd.setOtherPrice(carOrder.getOtherPrice());
								carOrderForAdd.setOtherPriceNote(carOrder.getOtherPriceNote());
								carOrderForAdd.setPrice(carOrder.getPrice());
								carOrderForAdd.setRealSeats(carOrder.getRealSeats());
								carOrderForAdd.setReasonTime(carOrder.getReasonTime());
								carOrderForAdd.setRemDriverCharge(carOrder.getRemDriverCharge());
								carOrderForAdd.setRouteDetail(carOrder.getRouteDetail());
								// carOrderForAdd.setRouteMps(carOrder.getRouteMps());
								List<RouteMapPoint> oldRouteMps = carOrder.getRouteMps();
								if (null != oldRouteMps && !oldRouteMps.isEmpty()) {
									List<RouteMapPoint> newRouteMps = new ArrayList<RouteMapPoint>(oldRouteMps.size());
									for (RouteMapPoint rmp : oldRouteMps) {
										RouteMapPoint routeMapPoint = new RouteMapPoint();
										routeMapPoint.setMapPoint(rmp.getMapPoint());
										routeMapPoint.setPtype(rmp.getPtype());
										routeMapPoint.setSortNo(rmp.getSortNo());
										newRouteMps.add(routeMapPoint);
									}
									carOrderForAdd.setRouteMps(newRouteMps);
								}
								carOrderForAdd.setRouteNo(carOrder.getRouteNo());
								
								if (carOrder.getRouteLineInfo() !=null) {
									RouteLineInfo newRouteLineInfo = (RouteLineInfo)carOrder.getRouteLineInfo().clone();
									//不添加时会报“A different object with the same identifier value was already associated with the session : [com.fx.entity.order.RouteLineInfo#25]; nested exception is org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already associated with the session : [com.fx.entity.order.RouteLineInfo#25]”
									newRouteLineInfo.setId((long)0);
									carOrderForAdd.setRouteLineInfo(newRouteLineInfo);
								}
								
								if (carOrder.getRouteStationInfo() != null) {
									RouteStationInfo newRouteStationInfo = (RouteStationInfo)carOrder.getRouteStationInfo().clone();
									newRouteStationInfo.setId((long)0);
									carOrderForAdd.setRouteStationInfo(newRouteStationInfo);
								}
								if (carOrder.getConfmStart() != null) {
									MapPoint newConfmStart = (MapPoint)carOrder.getConfmStart().clone();
									newConfmStart.setId((long)0);
									carOrderForAdd.setConfmStart(newConfmStart);
								}
								if (carOrder.getConfmStart() != null) {
									MapPoint newConfmEnd = (MapPoint)carOrder.getConfmEnd().clone();
									newConfmEnd.setId((long)0);
									carOrderForAdd.setConfmEnd(newConfmEnd);
								}
//								carOrderForAdd.setRouteLineInfo(carOrder.getRouteLineInfo());
//								carOrderForAdd.setRouteStationInfo(carOrder.getRouteStationInfo());
								carOrderForAdd.setSelfPrepayPrice(carOrder.getSelfPrepayPrice());
								carOrderForAdd.setServiceType(carOrder.getServiceType());
								carOrderForAdd.setStatus(carOrder.getStatus());
								carOrderForAdd.setTrades(null);
								// List<RouteTradeList> oldRouteTrade =
								// carOrder.getTrades();
								// if (null != oldRouteTrade &&
								// !oldRouteTrade.isEmpty()) {
								// List<RouteTradeList> newRouteTrade = new
								// ArrayList<RouteTradeList>(
								// oldRouteTrade.size());
								// for (RouteTradeList rt : newRouteTrade) {
								// RouteTradeList routeTradeList = new
								// RouteTradeList();
								// routeTradeList.setAddTime(addTime);
								// newRouteTrade.add(rt);
								// }
								// carOrderForAdd.setTrades(newRouteTrade);
								// }

								carOrderForAdd.setStime(carOrder.getStime());
								carOrderForAdd.setTravelPrepayPrice(carOrder.getTravelPrepayPrice());
								carOrderDao.save(carOrderForAdd);
							}
							carOrder.setNeedCars(1);
							carOrderDao.update(carOrder);
						}
					}
				}
				mcarOrderDao.update(mainCarOrder);
				U.log(log, "单位订单-确认用车-成功");
				U.setPut(map, 1, "确认用车成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		System.out.println("1231");
		return map;
	}



	@Override
	public Map<String, Object> handleSendCar(ReqSrc reqsrc, String sendOrderNum, String seats, String force,
			String runArea, String plateNum, String selfOwned, String avgSpeed, String notContainPn) {
		String logtxt = U.log(log, "单位-人工派单-获取车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(sendOrderNum)) {
				fg = U.setPutFalse(map, 0, "派单订单号不能为空");
			}
			if (fg) {
				if (StringUtils.isBlank(seats)) {
					fg = U.setPutFalse(map, 0, "派单座位数不能为空");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(avgSpeed)) {
					fg = U.setPutFalse(map, 0, "平均速度不能为空");
				}
			}
			if (fg) {
				String hql = "from CarOrder where orderNum in (:v0) order by routeNo asc";
				List<CarOrder> coist = carOrderDao.findListIns(hql, (Object[]) sendOrderNum.split(","));
				map = commonDao.sendTempParam(coist, 0);
				if ("1".equals(map.get("code").toString())) {
					OrderTemp ot = (OrderTemp) map.get("ot");
					List<Map<String, Object>> choice = carSer.lastSmartCar(reqsrc, ot, "0", seats, force, runArea,
							plateNum, selfOwned, Double.valueOf(avgSpeed), notContainPn, 0,1);
					map.remove("ot");// 删除临时参数，前端不需要
					if (choice != null && choice.size() == 1) {
						if ("0".equals(choice.get(0).get("cancelNum").toString())) {// 无冲突订单
							map.put("data", choice.get(0).get("choiceCar"));
							// 字段过滤
							Map<String, Object> fmap = new HashMap<String, Object>();
							fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
							map.put(QC.FIT_FIELDS, fmap);
							U.setPut(map, 1, "查询车辆成功");
						} else {
							U.setPutFalse(map, "未找到符合条件的车辆");
						}
					} else {
						U.setPutFalse(map, "未找到符合条件的车辆");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> smartSendOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String sendOrderNum, String firstCar, String seats, String notContainPn, String sendPlate,String sendModel) {
		String logtxt = U.log(log, "单位-智能派单-获取车辆", reqsrc);
		Map<String, Object> sendMap = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(sendOrderNum)) {
				fg = U.setPutFalse(sendMap, 0, "派单订单号不能为空");
			}
			if (fg) {
				// CarOrder co = carOrderDao.findByField("orderNum",
				// sendOrderNum);
				String hql = "from CarOrder where orderNum in (:v0) order by routeNo asc";
				List<CarOrder> coist = carOrderDao.findListIns(hql, (Object[]) sendOrderNum.split(","));
				sendMap = commonDao.sendTempParam(coist, 1);
				if ("1".equals(sendMap.get("code").toString())) {
					OrderTemp ot = (OrderTemp) sendMap.get("ot");
					if (StringUtils.isBlank(seats)) {
						seats = ot.getNeedSeats() + "";
					}
					List<Map<String, Object>> findCar = carSer.lastSmartCar(reqsrc, ot, firstCar, seats, "0", "",
							sendPlate, "", 0, notContainPn, 1,Integer.parseInt(sendModel));// 最终选择车辆和需要取消的订单
					sendMap.remove("ot");// 删除临时参数，前端不需要
					if (findCar != null && findCar.size() > 0) {
						if (findCar.size() == 1) {// 找到1辆车
							Map<String, Object> fmap = findCar.get(0);
							CompanyVehicle car = (CompanyVehicle) fmap.get("choiceCar");
							if ("0".equals(fmap.get("cancelNum").toString())) {// 当前订单派单后与后续订单不冲突并且没有其他日程冲突订单直接派单
								sendMap = confirmSendSmart(reqsrc, sendMap, null, null, null, coist, car);
							} else {// 当前订单派单后与后续订单冲突，一定是单个冲突，多个冲突的车辆已排除
									// 3月1号10点成都市 天府广场-{途经点}-成都市
									// 天府三街1日县/市/省际包车/单程接送(订单号)
								CarOrder jcNext = carOrderDao.findByField("orderNum", fmap.get("cancelNum").toString());
								String msg = DateUtils.get_MDM_str(jcNext.getStime());
								msg += jcNext.getRouteMps().get(0).getMapPoint().getAddress() + "─";// 起点地址
								if (jcNext.getRouteMps().size() > 2) {
									msg += "途径地：（";
									for (int i = 2; i < jcNext.getRouteMps().size(); i++) {
										if (i != jcNext.getRouteMps().size() - 1) {
											msg += jcNext.getRouteMps().get(i).getMapPoint().getAddress() + ",";
										} else {
											msg += jcNext.getRouteMps().get(i).getMapPoint().getAddress() + "）";
										}
									}
								}
								msg += "─" + jcNext.getRouteMps().get(1).getMapPoint().getAddress();// 终点地址
								msg += (DateUtils.getDaysOfTowDiffDate(jcNext.getStime(), jcNext.getEtime()) + 1) + "日"
										+ jcNext.getServiceType().getKey();
								msg += "订单号：" + jcNext.getOrderNum();
								sendMap.put("currPlateNum", car.getPlateNumber());// 当前车牌号
								sendMap.put("cancelNum", jcNext.getOrderNum());
								if (ot.getRouteType().equals(RouteType.ONE_WAY)) {// 单车接送
									sendMap.put("nextSeats", ot.getNeedSeats());// 下一个座位数
								} else {
									sendMap.put("nextSeats", seats);// 下一个座位数
								}
								U.setPut(sendMap, -1, "找到【" + car.getPlateNumber() + "," + car.getSeats()
										+ "座】的车最适合当前订单！但与该车【" + msg + "】冲突，是否继续？继续派车将取消已有行程重新派单");
							}
						} else {// 找到2辆车，这种情况只会出现在同时有空车和非空车的时候
							String msg = "找到两辆最优车辆：";
							CompanyVehicle fcar = null;
							String carStr = "";
							for (Map<String, Object> fmap : findCar) {
								fcar = (CompanyVehicle) fmap.get("choiceCar");
								msg+="【"+fcar.getPlateNumber()+","+fcar.getSeats()+"座】"+fmap.get("tips").toString()+"；";
								
								/*if ("0".equals(fmap.get("haveRoute").toString())) {
									msg += "【" + fcar.getPlateNumber() + "," + fcar.getSeats() + "座,空车】";
								} else if ("1".equals(fmap.get("haveRoute").toString())) {
									msg += "【" + fcar.getPlateNumber() + "," + fcar.getSeats() + "座,有业务能套单！】请选择一辆派车！";
								} else {
									CarOrder jcNext = carOrderDao.findByField("orderNum",
											fmap.get("cancelNum").toString());
									msg += "【" + fcar.getPlateNumber() + "," + fcar.getSeats() + "座,有"
											+ DateUtils.get_MDM_str(jcNext.getStime()) + "至"
											+ DateUtils.get_MDM_str(jcNext.getEtime()) + "的"
											+ jcNext.getServiceType().getKey() + "冲突！】请选择其中一辆派车（如选有冲突车辆将取消现有行程重派）！";
								}*/
								
								carStr += fcar.getPlateNumber() + "," + fmap.get("cancelNum").toString() + "/@";
							}
							sendMap.put("carOne", carStr.split("/@")[0]);
							sendMap.put("carTwo", carStr.split("/@")[1]);
							msg += "否则请关闭此页！";
							U.setPut(sendMap, -2, msg);
						}
					} else {
						if (ot.getRouteType().equals(RouteType.ONE_WAY)) {// 单车接送
							U.setPut(sendMap, 0, "操作成功，当前智能派单未找到车辆，请手动派单");
						} else {// 包车订单
							hql = "select new CompanyVehicle(seats) from CompanyVehicle where unitNum=?0 and seats>?1 group by seats order by seats asc";
							List<CompanyVehicle> seatlist = carSer.findhqlList(hql, ot.getUnitNum(),
									Integer.parseInt(seats));
							if (seatlist.size() > 0) {
								sendMap.put("nextSeats", seatlist.get(0).getSeats());// 下一个座位数
								U.setPut(sendMap, -3, "操作成功（智能派单" + seats + "座运力已饱和）是否启用" + seatlist.get(0).getSeats()
										+ "座智能派单？不启用则手动派单");
							} else {
								U.setPut(sendMap, 0, "操作成功（智能派单" + seats + "座运力已饱和并且没有更大车型可选，请手动派单）");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(sendMap, log, e, logtxt);
			e.printStackTrace();
		}
		return sendMap;
	}



	@Override
	public Map<String, Object> confirmSendSmart(ReqSrc reqsrc, Map<String, Object> sendMap, String cancelNum,
			String sendOrderNum, String sendPlateNum, List<CarOrder> coist, CompanyVehicle car) {
		String logtxt = U.log(log, "单位-人工/智能-确认派车", reqsrc);
		try {
			if (coist == null) {// 智能派单有冲突时后续调起确认派单该值为空
				String hql = "from CarOrder where orderNum in (:v0) order by routeNo asc";
				coist = carOrderDao.findListIns(hql, (Object[]) sendOrderNum.split(","));
			}
			if (car == null) { // 智能派单有冲突时后续调起确认派单该值为空
				car = carSer.findByField("plateNumber", sendPlateNum);
			}
			if (StringUtils.isNotBlank(cancelNum)) {
				U.log(log, "单位-智能派单时[" + car.getPlateNumber() + "-" + cancelNum + "]需要取消");
				CarOrder cancelOrder = carOrderDao.findByField("orderNum", cancelNum);
				disCarInfoDao.delete(cancelOrder.getDisCar());// 删除派车信息
				cancelOrder.setDisCar(null);// 清空派车信息
				carOrderDao.update(cancelOrder);
				U.log(log, "车辆[" + car.getPlateNumber() + "-" + cancelNum + "]被新订单[" + sendOrderNum + "]取消");
				sendMap.put("cancelNum", cancelNum);
			}
			// 主订单
			MainCarOrder mco = mcarOrderDao.findByField("orderNum", coist.get(0).getMainOrderNum());
			for (CarOrder co : coist) {
				// 设置派车信息
				DisCarInfo disCarInfo = new DisCarInfo();
				disCarInfo.setPlateNum(car.getPlateNumber());
				disCarInfo.setSeats(car.getSeats());
				disCarInfo.setMainDriverStime(co.getStime());
				disCarInfo.setMainDriverEtime(co.getEtime());
				disCarInfo.setMain_driver(car.getBaseUserId());
				disCarInfo.setOrderNum(co.getOrderNum());// 20200519
															// xx此处添加订单号方便智能派单查询
				// 派车信息添加进订单信息
				co.setDisCar(disCarInfo);
				// 修改订单状态
				co.setStatus(OrderStatus.JL_NOT_CONFIRM);
				carOrderDao.update(co);
				// 派车信息添加进主订单
				mco.getMainCars().add(disCarInfo);
			}
			// 如果派单车辆数等于主订单车辆数更新主订单
			if (mco.getMainCars().size() == mco.getNeedCars())
				mco.getMainOrderBase().setStatus(MainOrderStatus.FINISHED_DIS_CAR);
			mcarOrderDao.update(mco);
			U.log(log, "单位-人工/智能派单-找到车辆[" + car.getPlateNumber() + "]派车完成");
			if (car.getBaseUserId() == null) {// 没有驾驶员
				U.setPut(sendMap, 1, "操作成功，派单未匹配到合适的师傅，请在订单里面手动选择师傅");// 拼接派单成功订单号供前台提示
			} else {
				U.setPut(sendMap, 1, "操作成功，派单车辆为【" + car.getPlateNumber() + "】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(sendMap, log, e, logtxt);
		}
		return sendMap;

	}



	@Override
	public Map<String, Object> findOrderByPlateTime(ReqSrc reqsrc, HttpServletRequest request, String plateNum,
			String inTime, String ouTime) {
		String logtxt = U.log(log, "根据车牌号和时间找到订单");

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String hql = "from DisCarInfo where plateNum=?0" + "((mainDriverStime>='" + inTime
					+ "' and mainDriverEtime<='" + ouTime + "') or " + "(mainDriverStime<'" + inTime
					+ "' and mainDriverEtime>='" + inTime + "' and mainDriverEtime<='" + ouTime + "') or "
					+ "(mainDriverStime>='" + inTime + "' and mainDriverStime<='" + ouTime + "' and mainDriverEtime>'"
					+ ouTime + "') or " + "(mainDriverStime<'" + inTime + "' and mainDriverEtime>'" + ouTime + "'))";
			List<DisCarInfo> dciList = disCarInfoDao.findhqlList(hql, plateNum);
			if (dciList.size() == 1) {
				CarOrder co = carOrderDao.findByField("orderNum", dciList.get(0).getOrderNum());
				// 返回订单号，行程详情，驾驶员
				map.put("orderNum", co.getOrderNum());
				map.put("vieWay", co.getRouteDetail().replaceAll("\r|\n", " "));
				map.put("driver", dciList.get(0).getMain_driver());
				U.setPut(map, 1, "ETC找到订单：【" + co.getOrderNum() + "】");
				U.log(log, "ETC找到订单：【" + co.getOrderNum() + "】");
			} else {
				U.setPut(map, 0, "ETC未找到订单");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> confirmSendUnder(ReqSrc reqsrc, String sendOrderNum, String suppCar, String suppCarHead,
			String sendPlateNum, String driverInfo, String sendPrice, String driverGath) {
		String logtxt = U.log(log, "单位-人工派单-线下车辆", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CarOrder co = carOrderDao.findByField("orderNum", sendOrderNum);
			if (co != null) {
				co.setDisPrice(Double.valueOf(sendPrice));
				// 师傅现收加入派车订单已付金额
				if (!"0".equals(driverGath))
					co.setAlPayPrice(Double.valueOf(driverGath));
				// 设置派车信息
				DisCarInfo disCarInfo = new DisCarInfo();
				disCarInfo.setPlateNum(sendPlateNum);
				disCarInfo.setSeats(co.getNeedSeats());
				disCarInfo.setMainDriverStime(co.getStime());
				disCarInfo.setMainDriverEtime(co.getEtime());
				disCarInfo.setSuppCar(suppCar);
				disCarInfo.setSuppCarHead(suppCarHead);
				disCarInfo.setOutDriverInfo(driverInfo);
				disCarInfo.setOrderNum(co.getOrderNum());// 20200519
															// xx此处添加订单号方便智能派单查询
				// 派车信息添加进订单信息
				co.setDisCar(disCarInfo);
				// 修改订单状态
				co.setStatus(OrderStatus.DRIVER_CONFIRMED);// 线下默认派车师傅已确认
				carOrderDao.update(co);
				// 派车信息添加进主订单
				MainCarOrder mco = mcarOrderDao.findByField("orderNum", co.getMainOrderNum());
				mco.getMainCars().add(disCarInfo);
				// 师傅现收加入主订单已收金额
				if (!"0".equals(driverGath))
					mco.setAlGathPrice(MathUtils.add(mco.getAlGathPrice(), Double.valueOf(driverGath), 2));
				if (mco.getMainCars().size() == mco.getNeedCars())
					mco.getMainOrderBase().setStatus(MainOrderStatus.FINISHED_DIS_CAR);
				mcarOrderDao.update(mco);
				U.log(log, "单位-人工线下派单-派车完成");
				U.setPut(map, 1, "操作成功");
			} else {
				U.setPut(map, 0, "订单不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> setExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位管理-订单操作-订单外调", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();

		boolean fg = true;

		JSONArray jsonArray = jsonObject.getJSONArray("idList");
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
		List<CarOrder> carOrders = new ArrayList<>();
		try {
			if (fg) {
				if (idList.size() == 0) {
					U.logFalse(log, "单位管理-订单操作-订单外调-传入订单id为空");
					fg = U.setPutFalse(map, 0, "传入订单id有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {
					U.logFalse(log, "单位管理-订单操作-订单外调-传入主订单编号为空");
					fg = U.setPutFalse(map, 0, "传入主订单编号有误");
				}
			}
			if (fg) {
				for (long orderId : idList) {
					CarOrder carOrder = carOrderDao.findByField("id", orderId);
					if (carOrder.getIsExternal() != 0) {
						U.logFalse(log, "单位管理-订单操作-订单外调-订单id:" + orderId + "不处于非外调，不能外调");
						fg = U.setPutFalse(map, 0, "订单[" + carOrder.getOrderNum() + "]已经外调，不能外调");
						break;
					} else {
						carOrders.add(carOrder);
					}
				}
			}
			if (fg) {
				for (CarOrder carOrder : carOrders) {
					carOrder.setIsExternal(1);
					carOrderDao.update(carOrder);
					U.log(log, "单位管理-订单操作-订单外调-订单id:" + carOrder.getId() + "外调成功");
				}

				MainCarOrder mainCarOrder = mcarOrderDao.findByField("orderNum", mainOrderNum);
				if (mainCarOrder.getIsExternal() == 0) {
					// 主订单处于非外调状态，改变为外调状态
					mainCarOrder.setIsExternal(1);
					mcarOrderDao.update(mainCarOrder);
					U.log(log, "单位管理-订单操作-订单外调-主订单" + mainOrderNum + "外调成功");
				} else {
					U.log(log, "单位管理-订单操作-订单外调-主订单" + mainOrderNum + "无需改变外调状态");
				}
				U.setPut(map, 1, "外调成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> cancelExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位管理-订单操作-订单外调", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();

		boolean fg = true;

		JSONArray jsonArray = jsonObject.getJSONArray("idList");
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
		List<CarOrder> carOrders = new ArrayList<>();
		try {
			if (fg) {
				if (idList.size() == 0) {
					U.logFalse(log, "单位管理-订单操作-取消外调-传入订单id为空");
					fg = U.setPutFalse(map, 0, "传入订单id有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {
					U.logFalse(log, "单位管理-订单操作-取消外调-传入主订单编号为空");
					fg = U.setPutFalse(map, 0, "传入主订单编号有误");
				}
			}
			if (fg) {
				for (long orderId : idList) {
					CarOrder carOrder = carOrderDao.findByField("id", orderId);
					if (carOrder.getIsExternal() != 1) {
						U.logFalse(log, "单位管理-订单操作-取消外调-订单id:" + orderId + "无法取消外调");
						fg = U.setPutFalse(map, 0, "订单[" + carOrder.getOrderNum() + "]无法取消外调,请检查订单外调状态");
						break;
					} else {
						carOrders.add(carOrder);
					}
				}
			}
			if (fg) {
				for (CarOrder carOrder : carOrders) {
					carOrder.setIsExternal(0);
					carOrderDao.update(carOrder);
					U.log(log, "单位管理-订单操作-取消外调-订单id:" + carOrder.getId() + "取消成功");
				}

				MainCarOrder mainCarOrder = mcarOrderDao.findByField("orderNum", mainOrderNum);
				if (mainCarOrder.getIsExternal() != 1) {
					// 主订单处于不外调或者外调锁定状态，无需更改主订单状态
					U.log(log, "单位管理-订单操作-取消外调-主订单" + mainOrderNum + "无需改变外调状态");
				} else {
					// 主订单当前处于外调状态，需要判断子订单是否全部处于不外调状态
					// 先获取所有子订单
					List<CarOrder> allCarOrder = carOrderDao.findListByField("mainOrderNum", mainOrderNum);

					boolean change = true;
					for (CarOrder co : allCarOrder) {
						if (co.getIsExternal() != 0) {
							// 有子订单处于不外调状态，主订单状态无需改变
							change = false;
						}
					}
					if (change) {
						mainCarOrder.setIsExternal(0);
						mcarOrderDao.update(mainCarOrder);
						U.log(log, "单位管理-订单操作-取消外调-主订单" + mainOrderNum + "取消外调成功");
					} else {
						U.log(log, "单位管理-订单操作-取消外调-主订单" + mainOrderNum + "无法改变为不外调状态,有子订单处于外调");
					}
				}
				U.setPut(map, 1, "取消外调成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> lockExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject, String externalLockMan) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位管理-订单操作-锁定外调", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();

		boolean fg = true;

		JSONArray jsonArray = jsonObject.getJSONArray("idList");
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
		List<CarOrder> carOrders = new ArrayList<>();
		try {
			if (fg) {
				if (idList.size() == 0) {
					U.logFalse(log, "单位管理-订单操作-锁定外调-传入订单id为空");
					fg = U.setPutFalse(map, 0, "传入订单id有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {
					U.logFalse(log, "单位管理-订单操作-锁定外调-传入主订单编号为空");
					fg = U.setPutFalse(map, 0, "传入主订单编号有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(externalLockMan)) {
					U.logFalse(log, "单位管理-订单操作-锁定外调-获得账号realName出错");
					fg = U.setPutFalse(map, 0, "读取账号信息出错");
				}
			}
			if (fg) {
				for (long orderId : idList) {
					CarOrder carOrder = carOrderDao.findByField("id", orderId);
					if (carOrder.getIsExternal() == 1) {
						carOrders.add(carOrder);
					} else {
						U.logFalse(log, "单位管理-订单操作-锁定外调-订单id:" + orderId + "锁定失败，订单不处于外调状态");
						fg = U.setPutFalse(map, 0, "订单[" + carOrder.getOrderNum() + "]不能锁定");
						break;
					}
				}
			}
			if (fg) {
				for (CarOrder carOrder : carOrders) {
					// 订单处于外调状态，可以锁定
					carOrder.setIsExternal(2);
					carOrder.setExternalLockMan(externalLockMan);
					carOrderDao.update(carOrder);
					U.log(log, "单位管理-订单操作-锁定外调-订单id:" + carOrder.getId() + "锁定外调成功");

				}

				MainCarOrder mainCarOrder = mcarOrderDao.findByField("orderNum", mainOrderNum);
				if (mainCarOrder.getIsExternal() != 1) {
					// 主订单处于不外调或者外调锁定状态，无需更改订单状态
					U.log(log, "单位管理-订单操作-锁定外调-主订单" + mainOrderNum + "无需改变外调状态");
				} else {
					// 主订单当前处于外调状态，更改为锁定外调
					mainCarOrder.setIsExternal(2);
					mcarOrderDao.update(mainCarOrder);
					U.log(log, "单位管理-订单操作-锁定外调-主订单" + mainOrderNum + "锁定成功");
				}
				U.setPut(map, 1, "锁定外调成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> unlockExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject, String externalUnLockMan) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位管理-订单操作-解锁外调", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();

		boolean fg = true;

		JSONArray jsonArray = jsonObject.getJSONArray("idList");
		String mainOrderNum = jsonObject.getString("mainOrderNum");
		List<Long> idList = JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
		List<CarOrder> carOrders = new ArrayList<>();
		try {
			if (fg) {
				if (idList.size() == 0) {
					U.logFalse(log, "单位管理-订单操作-解锁外调-传入订单id为空");
					fg = U.setPutFalse(map, 0, "传入订单id有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(mainOrderNum)) {
					U.logFalse(log, "单位管理-订单操作-解锁外调-传入主订单编号为空");
					fg = U.setPutFalse(map, 0, "传入主订单编号有误");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(externalUnLockMan)) {
					U.logFalse(log, "单位管理-订单操作-解锁外调-获得账号realName出错");
					fg = U.setPutFalse(map, 0, "读取账号信息出错");
				}
			}
			if (fg) {
				for (long orderId : idList) {
					CarOrder carOrder = carOrderDao.findByField("id", orderId);
					if (carOrder.getIsExternal() == 2 && carOrder.getStatus().toString().equals("NOT_DIS_CAR")) {
						carOrders.add(carOrder);
					} else {
						U.logFalse(log, "单位管理-订单操作-解锁外调-订单id:" + orderId + "无法解锁");
						fg = U.setPutFalse(map, 0, "订单[" + carOrder.getOrderNum() + "]无法解锁");
						break;
					}

				}
			}
			if (fg) {
				for (CarOrder carOrder : carOrders) {
						// 订单处于外调锁定状态，且未派单，可以取消锁定
						carOrder.setIsExternal(1);
						carOrder.setExternalLockMan(externalUnLockMan);
						carOrderDao.update(carOrder);
						U.log(log, "单位管理-订单操作-解锁外调-订单id:" + carOrder.getId() + "解锁外调成功");
					
				}

				MainCarOrder mainCarOrder = mcarOrderDao.findByField("orderNum", mainOrderNum);
				if (mainCarOrder.getIsExternal() != 2) {
					// 主订单不处于外调锁定状态，无需取消锁定
					U.log(log, "单位管理-订单操作-解锁外调-主订单:" + mainOrderNum + "不处于锁定状态，无需改变外调状态");
				} else {
					// 主订单当前处于外调锁定状态，需要判断子订单是否全部不处于外调锁定状态
					// 先获取所有子订单
					List<CarOrder> allCarOrder = carOrderDao.findListByField("mainOrderNum", mainOrderNum);

					boolean change = true;
					for (CarOrder co : allCarOrder) {
						if (co.getIsExternal() == 2) {
							// 有子订单处于锁定外调，主订单状态无需改变
							change = false;
						}
					}
					if (change) {
						mainCarOrder.setIsExternal(1);
						mcarOrderDao.update(mainCarOrder);
						U.log(log, "单位管理-订单操作-解锁外调-主订单" + mainOrderNum + "解锁外调成功");
					} else {
						U.log(log, "单位管理-订单操作-解锁外调-主订单" + mainOrderNum + "解锁失败,有子订单处于锁定外调状态");
					}
				}
				U.setPut(map, 1, "解锁外调成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> cancelConfirmPayment(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "业务付款-取消确认付款价格", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id = jsonObject.getString("id");

			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "业务付款-取消确认付款价格-订单id为空");
				U.setPutFalse(map, 0, "订单id为空");
				return map;
			}
			CarOrder carOrder = carOrderDao.findByField("id", Long.parseLong(id));
			if (carOrder == null) {
				U.logFalse(log, "业务付款-取消确认付款价格-查询不到订单");
				U.setPutFalse(map, 0, "查询不到订单，请确认id正确");
				return map;
			}
			carOrder.setConfirmPaymentName(null);
			carOrderDao.update(carOrder);
			U.log(log, "业务付款-取消确认付款价格-成功");
			U.setPut(map, 1, "取消车价确认成功");
			return map;
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			return map;
		}
	}



	@Override
	public Map<String, Object> findDriverOrderList(ReqSrc reqsrc, BaseUser buser, CompanyUser comUser, String page,
			String rows, String stime, String etime, String isTrip) {
		String logtxt = U.log(log, "获取-车队驾驶员-订单列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg)
				fg = U.valPageNo(map, page, rows, null); // 验证-页码&页大小
			if (fg)
				fg = U.valSEtime(map, stime, etime, "出行"); // 验证-开始时间&结束时间

			if (fg) {
				if (StringUtils.isEmpty(isTrip)) {
					fg = U.setPutFalse(map, "[出行类型]不能为空");
				} else {
					isTrip = isTrip.trim();
					if (!FV.isInteger(isTrip)) {
						fg = U.setPutFalse(map, "[出行类型]格式错误");
					}

					U.log(log, "[是否出行] isTrip=" + isTrip);
				}
			}

			if (fg) {
				Page<CarOrder> pd = carOrderDao.findPageDriverOrderList(buser, comUser, page, rows, stime, etime,
						isTrip);
				U.setPageData(map, pd);

				U.setPut(map, 1, "获取-驾驶员订单列表-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> servicePay(ReqSrc reqsrc, HttpServletRequest request, String unitNum, String uname,
			String ids, String payMoney, String payRemark) {
		String logtxt = U.log(log, "单位-业务付款");

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (ReqSrc.PC_COMPANY == reqsrc) {
				if (fg) {
					if (StringUtils.isEmpty(ids)) {
						fg = U.setPutFalse(map, "[付款id]不能为空");
					} else {
						ids = ids.trim();
						U.log(log, "付款id=" + ids);
					}
				}
				double _payMoney = 0.0;
				if (fg) {
					if (StringUtils.isEmpty(payMoney)) {
						fg = U.setPutFalse(map, "[付款金额]不能为空");
					} else {
						_payMoney = Double.valueOf(payMoney.trim());
						if (_payMoney == 0) {
							fg = U.setPutFalse(map, "[付款金额]不能为0");
						}
						U.log(log, "付款金额=" + payMoney);
					}
				}
				if (fg) {
					if (StringUtils.isEmpty(payRemark)) {
						fg = U.setPutFalse(map, "[付款摘要]不能为空");
					} else {
						payRemark = payRemark.trim();
						U.log(log, "付款摘要=" + payRemark);
					}
				}
				FeeCourse fc = null;
				if (fg) {
					fc = fcDao.findByField("courseName", "业务付款");
					if (fc == null) {
						fg = U.setPutFalse(map, "[科目“业务付款”]不存在");
					}
				}
				List<CarOrder> colist=new ArrayList<CarOrder>();
				CarOrder co = null;
				if(fg){
					String payName="";//收款方
					String[] id = ids.split(",");
					for (int i = 0; i < id.length; i++) {
						co = carOrderDao.find(Long.valueOf(id[i]));
						if(co.getDisPrice()<=co.getAlPayPrice()) {
							fg = U.setPutFalse(map, "订单【"+co.getOrderNum()+"】已付全款，不能继续付款");
							break;
						}
						if(StringUtils.isBlank(co.getConfirmPaymentName())){
							fg = U.setPutFalse(map, "订单【"+co.getOrderNum()+"】未确认车价，请先确认");
							break;
						}
						if(StringUtils.isNotBlank(payName)) {
							if(co.getDisCar().getSuppCar().contains(payName)) {
								fg = U.setPutFalse(map, "请选择同一个客户的订单");
								break;
							}
						}else {
							payName=co.getDisCar().getSuppCar();
						}
						colist.add(co);
					}
				}
				if(fg){
					Staff staff=staffDao.findByField("baseUserId.uname", uname);//当前员工，正常情况是出纳
					String operMark="";
					for (int i=0;i<colist.size();i++) {
						co = colist.get(i);
							operMark = DateUtils.getOrderNum(7);
						if (colist.size() > 1) {// 多个订单收款默认每个订单均付款完成
								_payMoney = MathUtils.sub(Double.valueOf(co.getDisPrice()),
										Double.valueOf(co.getAlPayPrice()), 2);
							}
							// 更新已付款
							if (co.getDisPrice() <= MathUtils.add(co.getAlPayPrice(), _payMoney, 2)) { // 已付款+本次付款>=行程总价
								co.setPayStatus(OrderPayStatus.FULL_PAID);// 全款已付
							} else {
								co.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已付定金
							}
							co.setAlPayPrice(MathUtils.add(co.getAlPayPrice(), _payMoney, 2));
							carOrderDao.update(co);
							// 添加付款凭证
							/*ReimburseList reim = new ReimburseList();
							reim.setUnitNum(unitNum);
							reim.setReimUserId(co.getCarOrderBase().getBaseUserId());
							reim.setDeptId(staff.getDeptId());
							reim.setGainTime(co.getStime());
							reim.setFeeCourseId(fc);
							reim.setVoucherNum(UT.creatReimVoucher(staff.getBaseUserId().getUname(),
									Integer.parseInt(sortNum.toString() + i)));
							reim.setFeeStatus(fc.getCourseType());
							reim.setTotalMoney(_payMoney);
							reim.setRemark(payRemark);
							reim.setIsCheck(0);
							reim.setAddTime(new Date());
							reim.setReqsrc(reqsrc);
							reim.setOperMark(operMark);
							reim.setCarOrderReim(co);
							reim.setOperNote(staff.getBaseUserId().getRealName() + "[添加]");
							reimDao.save(reim);*/
							
							StaffReimburse obj = new StaffReimburse();
							obj.setUnitNum(LU.getLUnitNum(request, redis));
							obj.setReimUserId(co.getCarOrderBase().getBaseUserId());
							if(staff!=null) obj.setDeptId(staff.getDeptId());//是员工就有部门
							obj.setGathMoney(0);
							obj.setPayMoney(_payMoney);
							obj.setRemark(payRemark);
							obj.setIsCheck(0);
							obj.setAddTime(new Date());
							obj.setReqsrc(reqsrc);
							obj.setOperNote(staff.getBaseUserId().getRealName()+"[添加]");
							obj.setOperMark(operMark);
							obj.setCarOrderReim(co);
							srDao.update(obj);
						}
					U.setPut(map, 1, "付款成功");
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
	public Map<String, Object> JLComfirm(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String carOrderId) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "订单-经理确认派单", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank("carOrderId")) {
				U.logFalse(log, "订单-经理确认派单-失败-orderid为空");
				U.setPutFalse(map, 0, "确认失败，订单id为空 ");
				return map;
			}
			CarOrder carOrder = carOrderDao.findByField("id", Long.parseLong(carOrderId));
			if (null == carOrder) {
				U.logFalse(log, "订单-经理确认派单-失败-查询不到订单信息");
				U.setPutFalse(map, 0, "确认失败，查询不到订单信息 ");
				return map;
			}
			if (carOrder.getStatus() != OrderStatus.JL_NOT_CONFIRM) {
				U.logFalse(log, "订单-经理确认派单-失败-订单当前状态有误");
				U.setPutFalse(map, 0, "确认失败，确认当前订单状态 ");
				return map;
			}
			carOrder.setStatus(OrderStatus.DRIVER_NOT_CONFIRM);
			carOrderDao.update(carOrder);
			U.log(log, "订单-经理确认派单-成功");
			U.setPut(map, 1, "确认成功");
			return map;
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			return map;
		}
	}
	
	@Override
	public Map<String, Object> driverCofmOrder(ReqSrc reqsrc, CusRole role, HttpServletRequest request,
		HttpServletResponse response, String orderNum, String isAgree, String reason) {
		String logtxt = U.log(log, "驾驶员-确认订单", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(CusRole.TEAM_DRIVER == role){// 车队驾驶员
				if(fg){
					if(StringUtils.isEmpty(orderNum)){
						fg = U.setPutFalse(map, "[订单编号]不能为空");
					}else{
						orderNum = orderNum.trim();
						
						U.log(log, "订单编号：orderNum="+orderNum);
					}
				}
				
				int _isAgree = 0;
				if(fg){
					if(StringUtils.isEmpty(isAgree)){
						fg = U.setPutFalse(map, "[是否同意]不能为空");
					}else{
						isAgree = isAgree.trim();
						if(!FV.isInteger(isAgree)){
							fg = U.setPutFalse(map, "[是否同意]为整数");
						}else{
							_isAgree = Integer.parseInt(isAgree);
						}
						
						U.log(log, "是否同意：isAgree="+isAgree);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(reason)){
						U.log(log, "[拒绝原因]为空");
					}else{
						reason = reason.trim();
						if(reason.length() > 100){
							fg = U.setPutFalse(map, "[拒绝原因]文本长度不能超过100");
						}
						
						U.log(log, "拒绝原因：reason="+reason);
					}
				}
				
				CarOrder jco = null;
				if(fg){
					jco = carOrderDao.findByField("orderNum", orderNum);
					if(jco == null){
						fg = U.setPutFalse(map, "该订单不存在");
					}else{
						if(jco.getStatus() != OrderStatus.DRIVER_NOT_CONFIRM){
							fg = U.setPutFalse(map, "该订单已为确认订单");
						}
					}
				}
				
//				if(fg){
//					map = isCallAllLinkman(jco);// 验证司机师傅全部拨打地点联系人电话
//					if(!"1".equals(map.get("code").toString())){
//						fg = false;
//					}
//				}
				
				if(fg){
					if(_isAgree != 1){// 拒绝【暂不做】
						// 按照主驾驶员更新驾驶员状态
						jco.setStatus(OrderStatus.REFUSED);
						jco.setNote(reason); // 拒绝原因
						carOrderDao.update(jco);
						U.log(log, "更新订单成功");
						U.setPut(map, 1, "操作完成（订单已拒绝）");
						
						
//						DisCarInfo disCar = jco.getDisCar();
//						
//						// 主驾驶员信息
//						DriverList driverInfo = dlSer.findDriverOfCarTeam(jco.getDriver().split(",")[0], jco.getTeamNo());
//						driverInfo.setDriverState(0);// 空闲
//						dlSer.update(driverInfo);
//						U.log(log, "更新主驾驶员信息成功");
//						
//						// 副驾驶员信息
//						if(StringUtils.isNotEmpty(jco.getDriverPhone())){
//							driverInfo = dlSer.findDriverOfCarTeam(jco.getDriverPhone().split(",")[0], jco.getTeamNo());
//							driverInfo.setDriverState(0);// 空闲
//							dlSer.update(driverInfo);
//							U.log(log, "更新副驾驶员信息成功");	
//						}
//						
//						// 删除车辆行程记录
//						hql = "from CaRenTimeList where orderNum = ?";
//						List<CaRenTimeList> delRenTime = crtlSer.findhqlList(hql, jco.getOrderNum());
//						if(delRenTime.size() > 0){
//							for (CaRenTimeList each : delRenTime) {
//								crtlSer.delete(each);
//							}
//						}
					}else{
						jco.setStatus(OrderStatus.DRIVER_CONFIRMED);
						carOrderDao.update(jco);
						
						// 传入前端，方便修改
						map.put("status", jco.getStatus());
						
						U.setPut(map, 1, "操作完成（订单确认成功）");
						
//						if(jco.getOrderState() != 5) jco.setOrderState(_isAgree);
//						jco.setGetOrderTime(new Date());
//						
//						CarList car = clSer.findByField("plateNum", jco.getPlateNum());
//						// 挂靠车辆并且设置了主驾驶员加交易记录
//						if(car != null && car.getSelfOwned() == 1 && StringUtils.isNotEmpty(car.getMbName())){
//							// 调用方法添加收付款记录
//							String note = "线下订单(定金)";
//							if("0".equals(jco.getOrderHandSelPrice())) note = "线下诚信订单";
//							wallSer.teamPayFinal(5, jco.getOrderHandSelPrice(), jco.getMbName(), car.getMbName().split(",")[0], 
//								note, null, jco.getOrderNum(), 1);
//							
//							// 有车公司代收
//							if(jco.getCompanyPrice() > 0 && StringUtils.isNotEmpty(car.getCarCompany())){
//								double prepayRatioMoney = 0;// 默认税点为0元
//								UnderLineTeam ult=ultSer.findByField("teamName", car.getCompanyBelong());
//								if(ult!=null){
//									prepayRatioMoney = MathUtils.mul(jco.getCompanyPrice(),ult.getPrepayRatio(), 2);
//									OnlinePrepayment opm = new OnlinePrepayment();
//									opm.setTeamNo(jco.getTeamNo());
//									opm.setCompany(car.getCompanyBelong());
//									opm.setTotalPrice(MathUtils.sub(jco.getCompanyPrice(),prepayRatioMoney, 2));
//									opm.setOrderNum(orderNum);
//									opm.setPayTime(new Date());
//									opm.setAddTime(new Date());
//									opm.setRemark("车公司代收,已扣税点：" + prepayRatioMoney + "元");
//									opm.setPrepayRatioPrice(prepayRatioMoney);
//									opmSer.save(opm);
//									CarOrderList col = colDao.findByField("orderNum", jco.getMainOrderNum());
//									col.setOrderHandSelPrice(MathUtils.add(col.getOrderHandSelPrice(),jco.getCompanyPrice(), 2));//已收未确认
//									col.setRemarkPos(col.getRemarkPos() + ",车公司代收：" + jco.getCompanyPrice() + "元");
//									colDao.update(col);
//								}
//							}
//						}
//						
//						// 通知用车方
//						if(jco.getUseDayStart().after(new Date())){
//							String useCarStart = DateUtils.get_MDM_str(jco.getUseDayStart());
//							// 主驾驶员信息
//							DriverList sendInfo = dlSer.findDriverOfCarTeam(jco.getDriver().split(",")[0], jco.getTeamNo());
//							if(sendInfo != null){
//								// 获取所有指定派单编号的地点联系人
//								List<PointLinkman> plms = plmSer.findListByField("dispatchNum", jco.getOrderNum());
//								if(plms.size() > 0){
//									// 遍历循环为地点联系人发送短信
//									for(int p = 0; p < plms.size(); p++){
//										String txtcon = "";
//										//模板：【商旅客车帮】您预订的5月21号10:00崇州到九寨沟的订单，已安排：川AV6577杜伟18980887490为你服务！客户经理：高17828174933
//										if(StringUtils.isNotEmpty(jco.getServiceMan())){//有业务员
//											txtcon = "您预订的" + useCarStart + jco.getDepartPlace() + "到" + jco.getDestination() + "的订单，已安排：" + 
//												jco.getPlateNum() + jco.getDriver().split(",")[1] + jco.getDriver().split(",")[0] + "为您服务！";
//											SMS.sendSMS(txtcon, plms.get(p).getLinkPhone(), "客户经理：" + jco.getServiceMan().split("-")[0]);
//										}else{
//											txtcon = "您预订的" + useCarStart+jco.getDepartPlace() + "到" + jco.getDestination() + "的订单，已安排：" + 
//												jco.getPlateNum() + jco.getDriver()+"为您服务！";
//											SMS.sendSMS(txtcon, plms.get(p).getLinkPhone(), "");
//										}
//									}
//								}else{
//									U.log(log, "订单【"+jco.getOrderNum()+"】没有地点联系人");
//								}
//								
//								/*********发送微信模板消息--begin******************/
//								// 2019-4-15驾驶员确认订单成功，通知业务员（派车订单的serverMan）和旅行社（乘客订单的mbName）
//								if(StringUtils.isNotBlank(jco.getServiceMan())){
//									ymsgSer.useCarOfWxmsg(null, jco, jco.getServiceMan().split("-")[0]);// 通知业务员
//								}
//								
//								if(StringUtils.isNotBlank(jco.getMainOrderNum())){
//									CarOrderList col = colDao.findByField("orderNum", jco.getMainOrderNum());
//									ymsgSer.useCarOfWxmsg(null, jco, col.getMbName());// 通知旅行社
//								}
//								/*********发送微信模板消息--end******************/
//							}
//						}
//						
//						jcoSer.update(jco);
//						U.setPut(map, 1, "处理成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> driverCofmDownCar(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String orderNum, String dayId, String lnglat, String isArr) {
		String logtxt = U.log(log, "确认-完团（乘客下车）", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc){
				CarOrder jco = null;// 派车订单
				if(fg){
					if(StringUtils.isEmpty(orderNum)){
						fg = U.setPutFalse(map, "[订单编号]不能为空");
					}else{
						orderNum = orderNum.trim();
						jco = carOrderDao.findByField("orderNum", orderNum);
						if(jco == null){
							fg = U.setPutFalse(map, "该订单不存在");
						}else if(jco.getStatus() == OrderStatus.DRIVER_NOT_CONFIRM){
							fg = U.setPutFalse(map, "该订单驾驶员还未确认");
						}else if(jco.getStatus() == OrderStatus.COMPLETED){
							fg = U.setPutFalse(map, "该行程已确认完团");
						}
						
						U.log(log, "订单编号：orderNum="+orderNum);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(lnglat)){
						fg = U.setPutFalse(map, "[完团地点坐标]不能为空");
					}else{
						U.log(log, "完团地点坐标：lnglat="+lnglat);
						
						lnglat = lnglat.trim();
						if(lnglat.indexOf("|") == -1){
							fg = U.setPutFalse(map, "[完团地点坐标]格式错误");
						}else{
							// 坐标经纬度保留小数点后6位
							String[] p = lnglat.split("\\|");
							lnglat = MathUtils.saveBit(p[0], 6) + "|" + MathUtils.saveBit(p[1], 6);
						}
					
						U.log(log, "完团地点坐标：lnglat="+lnglat);
					}
				}
				
				Customer cus = LU.getLUSER(request, redis);
				if(fg){
					if(cus == null){
						fg = U.setPutFalse(map, 401, "登录失效，请重新登录");
					}else if(!jco.getDisCar().getMain_driver().getUname().contains(cus.getBaseUserId().getUname())){
						// 既不是用户名也不是手机号
						fg = U.setPutFalse(map, "该订单驾驶员不是您");
					}
				}
				
				int _isarr = 0;
				if(fg){
					if(StringUtils.isEmpty(isArr)){
						fg = U.setPutFalse(map, "[是否到达]不能为空");
					}else{
						isArr = isArr.trim();
						if(!FV.isPosInteger(isArr)){
							fg = U.setPutFalse(map, "[是否到达]格式错误");
						}else{
							_isarr = Integer.parseInt(isArr);
						}
						
						U.log(log, "[是否到达] isArr="+isArr);
					}
				}
				
				if(StringUtils.isEmpty(dayId)){// 不是操作天数行程
					if(jco.getCarOrderBase().getRouteType() == RouteType.ONE_WAY){
						U.log(log, "是‘单程接送’");
						
						if(_isarr == 2){
							U.log(log, "在完团地点范围内");
							
							if(new Date().getTime() < jco.getEtime().getTime()){
								U.log(log, "当前时间在结束时间之前，修改结束时间");
								
								jco.setEtime(new Date());
							}else{
								U.log(log, "当前时间在结束时间之后，不改变结束时间");
							}
						}else if(_isarr == 3){
							U.log(log, "不在完团地点范围内");
							
							if(new Date().getTime() < jco.getEtime().getTime()){
								U.log(log, "当前时间在结束时间之前");
								
								fg = U.setPutFalse(map, "未到达完团地点或未完团，不能确认");
							}else{
								U.log(log, "当前时间在结束时间之后，不改变结束时间");
							}
						}
					}else{// 旅游包车
						U.log(log, "行程是：旅游包车");
						
						if(_isarr == 2){
							U.log(log, "在完团地点范围内");
							
							if(new Date().getTime() < jco.getEtime().getTime()){
								U.log(log, "当前时间在结束时间之前");
								
								boolean flag = DateUtils.isDateSame(new Date(), jco.getEtime());
								if(flag){
									U.log(log, "日期是行程结束最后一天");
									
									jco.setEtime(new Date());// 修改结束时间
								}else{
									U.log(log, "日期不是行程结束最后一天");
									
									fg = U.setPutFalse(map, "行程未完团，不能确认");
								}
							}else{
								U.log(log, "当前时间在结束时间之后，确认完团，不改变结束时间");
							}
						}else if(_isarr == 3){
							U.log(log, "不在完团地点范围内");
							
							if(new Date().getTime() < jco.getEtime().getTime()){
								U.log(log, "当前时间在结束时间之前");
								
								boolean flag = DateUtils.isDateSame(new Date(), jco.getEtime());
								if(flag){
									U.log(log, "日期是行程结束最后一天");
									
									// 重新计算行程距离和完团时间
									carOrderDao.updAgainCalcEtime(jco, 2);
								}else{
									U.log(log, "日期不是行程结束最后一天");
									
									fg = U.setPutFalse(map, "行程未完团，不能确认");
								}
							}else{
								U.log(log, "当前时间在结束时间之后，不改变结束时间");
							}
						}else{
							fg = U.setPutFalse(map, "[是否到达]类型不存在");
						}
					}
					
					if(fg){
						jco.setStatus(OrderStatus.COMPLETED);// 交易完成
						carOrderDao.update(jco);
						U.log(log, "驾驶员[确认完团]成功");
						
						U.setPut(map, 1, "操作成功");
					}
				}else{// 指定天数行程
//					CarOrderDay cod = null;
//					if(fg){
//						if(StringUtils.isEmpty(dayId)){
//							fg = U.setPutFalse(map, "[天数行程id]不能为空");
//						}else{
//							dayId = dayId.trim();
//							if(!FV.isLong(dayId)){
//								cod = carOrderDao.find(Long.parseLong(dayId));
//								if(cod == null){
//									fg = U.setPutFalse(map, "[天数行程]不存在");
//								}
//							}
//							
//							U.log(log, "[天数行程id] dayId="+dayId);
//						}
//					}
					
					if(_isarr == 2){
						U.log(log, "在完团地点范围内");
						
						if(new Date().getTime() < jco.getEtime().getTime()){
							U.log(log, "当前时间在结束时间之前");
							
							jco.setEtime(new Date());// 修改结束时间
						}else{
							U.log(log, "当前时间在结束时间之后，确认完团，不改变结束时间");
						}
					}else if(_isarr == 3){
						U.log(log, "不在完团地点范围内");
						
						if(new Date().getTime() < jco.getEtime().getTime()){
							U.log(log, "当前时间在结束时间之前");
							
							// 重新计算行程距离和完团时间
							carOrderDao.updAgainCalcEtime(jco, 2);
						}else{
							U.log(log, "当前时间在结束时间之后，不改变结束时间");
						}
					}else{
						fg = U.setPutFalse(map, "[是否到达]类型不存在");
					}
					
					if(fg){
						jco.setStatus(OrderStatus.COMPLETED);// 交易完成
						carOrderDao.update(jco);
						U.log(log, "驾驶员[确认完团]成功");
						
						U.setPut(map, 1, "操作成功");
					}
					
				}
				
//				DiscountDetail disDetail = null;// 订单优惠详情
//				if(fg){
//					U.log(log, "解冻-优惠券推荐人奖励金为消费金");
//					
//					// 获取-真正订单编号
//					String realOrderNum = jco.getMainOrderNum();
//					disDetail = discountDetailDao.findByField("orderNum", realOrderNum);
//					if(disDetail == null || disDetail.getCouponId() == 0){
//						U.log(log, "["+realOrderNum+"]未使用优惠券");
//					}else{
//						// 如果当前订单使用了优惠券，优惠券存在推荐用户
//						CompanyDiscount ctd = companyDiscountDao.findByField("id", disDetail.getCouponId());
//						if(ctd == null){
//							U.logEx(log, "异常：["+realOrderNum+"]使用的优惠券不存在");
//						}else{
//							// 更改优惠券状态为：使用完成
//							ctd.setUseState(-2);
//							companyDiscountDao.update(ctd);
//							U.log(log, "更改优惠券状态为：使用完成");
//							
//							if(StringUtils.isEmpty(ctd.getRecName())){
//								U.log(log, "优惠券没有推荐用户");
//							}else{
//								// 存在推广账号，此处为推广用户产生消费金额（驾驶员确认订单才为推荐用户结算结算）
//								U.log(log, "为["+ctd.getRecName()+"]解冻消费金额");
//								
//								String snote = "";
//								if (ctd.getValidScene() == 1) {
//									snote = "单程接送";
//								} else if (ctd.getValidScene() == 2) {
//									snote = "旅游包车";
//								} else if (ctd.getValidScene() == 3){
//									snote = "顺风车";
//								}
//								wallSer.recomMoney(0, ctd.getRecName(), snote, 1, ctd.getHighMoney());
//								
//								U.log(log, "计算分享用户["+ctd.getRecName()+"]推荐金额 转 消费金额-完成");
//							}
//						}
//					}
//				}
				
//				if(fg && disDetail != null){
//					U.log(log, "计算客户星级");
					
//					mycusSer.sumCusOrder(reqsrc, request, response, col.getCarOrderBase().getUnitNum(), col.getDisCar().getMain_driver(), col.getRealName().split("【")[0], col.getStime());
//				}
				
				if(fg){
					U.log(log, "计算驾驶员-完团金额&完团余额");
					
					CusWallet w = cusWalletDao.findByField("cName", cus.getBaseUserId().getUname());
					
					double driverGetMoney = MathUtils.mul(jco.getPrice(), QC.DRIVER_GET_MONEY_PROP, 2);
					
					// 增加-完团金额&完团余额
					w.setFinTotal(MathUtils.add(w.getFinTotal(), driverGetMoney, 2));
					w.setFinBalance(MathUtils.add(w.getFinBalance(), driverGetMoney, 2));
					cusWalletDao.update(w);
					U.log(log, "更新-完团金额&完团余额-完成");
					
					// 添加-完团金额-记录
					WalletList wl_ft = new WalletList();
					wl_ft.setcName(cus.getBaseUserId().getUname());
					wl_ft.setAssist(jco.getOrderNum());
					wl_ft.setType(25);
					wl_ft.setAmoney(driverGetMoney);
					wl_ft.setStatus(0);
					wl_ft.setAtype(9);
					wl_ft.setCashBalance(w.getFinTotal());
					wl_ft.setNote("订单完团");
					wl_ft.setAtime(new Date());
					walletListDao.save(wl_ft);
					U.log(log, "添加-完团总额-交易记录");
					
					// 添加-完团余额-记录
					WalletList wl_fb = new WalletList();
					wl_fb.setcName(cus.getBaseUserId().getUname());
					wl_fb.setAssist(jco.getOrderNum());
					wl_fb.setType(25);
					wl_fb.setAmoney(driverGetMoney);
					wl_fb.setStatus(0);
					wl_fb.setAtype(8);
					wl_fb.setCashBalance(w.getFinBalance());
					wl_fb.setNote("订单完团");
					wl_fb.setAtime(new Date());
					walletListDao.save(wl_fb);
					U.log(log, "添加-完团余额-交易记录");
					
					U.setPut(map, 1, "操作成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}	
	
	@Override
	public Map<String, Object> findXcjzOrderList(ReqSrc reqsrc, String lunitNum, String luname, String mid) {
		String logtxt = U.log(log, "获取-子订单列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			MainCarOrder mco = null;
			if(fg) {
				if(StringUtils.isBlank(mid)) {
					fg = U.setPutFalse(map, "[主订单id]不能为空");
				}else {
					mid = mid.trim();
					if(!FV.isLong(mid)) {
						fg = U.setPutFalse(map, "[主订单编号]格式错误");
					}else {
						mco = mcarOrderDao.findByField("id", Long.parseLong(mid));
						if(mco == null) {
							fg = U.setPutFalse(map, "此[主订单]不存在");
						}else if(mco.getMainCars().size() == 0) {
							fg = U.setPutFalse(map, "此[主订单]还未派单");
						}
					}
					
					U.log(log, "[主订单编号] mid="+mid);
				}
			}
			
			if(fg) {
				List<Object> ons = new ArrayList<Object>();
				for (DisCarInfo dci : mco.getMainCars()) {
					ons.add(dci.getOrderNum());
				}
				
				hql = "from CarOrder where orderNum in(:v0) order by stime asc";
				List<CarOrder> cols = carOrderDao.findListIns(hql, ons.toArray());
				
				// 被派的订单列表
				List<Map<String, Object>> disOrders = new ArrayList<Map<String, Object>>();
				for (CarOrder co : cols) {
					if(co.getMainOrderNum().equals(mco.getOrderNum())) {
						Map<String, Object> o = new HashMap<String, Object>();
						o.put("id", co.getId()); 											// 订单id
						o.put("orderNum", co.getOrderNum());								// 订单编号
						o.put("status", co.getStatus()); 									// 订单状态
						o.put("saddr", co.getRouteMps().get(0).getMapPoint().getAddress()); // 出发地址
						o.put("eaddr", co.getRouteMps().get(1).getMapPoint().getAddress()); // 到达地址
						o.put("stime", co.getStime()); 										// 出发时间
						o.put("etime", co.getEtime()); 										// 到达时间
						o.put("isjz", co.getTrades().size() > 0 ? true : false); 			// 是否已有记账
						disOrders.add(o);
					}
				}
				map.put("data", disOrders);
				
				U.setPut(map, 1, "获取子订单成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
