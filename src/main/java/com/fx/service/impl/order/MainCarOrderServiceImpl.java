package com.fx.service.impl.order;

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
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.MainOrderStatus;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.OrderStatus;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.RouteType;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.dao.order.MainCarOrderDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.order.MainCarOrder;

import com.fx.service.order.MainCarOrderService;

@Service
@Transactional
public class MainCarOrderServiceImpl extends BaseServiceImpl<MainCarOrder, Long> implements MainCarOrderService {

	/** 日志记录 */
	private Logger			log	= LogManager.getLogger(this.getClass());
	/** 主订单-数据源 */
	@Autowired
	private MainCarOrderDao	mcoDao;
	
	/** 单位员工-服务 */
	@Autowired
	private StaffDao staffDao;
	
	/** 单位凭证-服务 */
	@Autowired
	private ReimburseListDao reimDao;
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao fcDao;
	/** 单位客户-服务 */
	@Autowired
	private CompanyCustomDao ccDao;




	@Override
	public ZBaseDaoImpl<MainCarOrder, Long> getDao() {
		return mcoDao;
	}



	@Override
	public Map<String, Object> findMainCarOrderList(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject, String companyNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-单位管理-订单分页", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				fg = U.valPageNo(map, jsonObject.getString("page"), jsonObject.getString("row"), "订单");
			}
			String page = jsonObject.getString("page");
			String rows = jsonObject.getString("row");
			// 获取搜索关键词
			String find = jsonObject.getString("find");
			if (fg) {
				if (StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				} else {
					find = find.trim();

					U.log(log, "[查询关键字] find=" + find);
				}
			}
			if (fg) {
				if (StringUtils.isBlank(companyNum)) {
					U.logFalse(log, "获取-单位管理-订单分页-companyNum为空");
					fg = U.setPutFalse(map, 0, "获取单位编号失败");
				}
			}
			OrderPayStatus payStatus = null;
			if (!StringUtils.isBlank(jsonObject.getString("orderPayStatus"))) {
				payStatus = OrderPayStatus.valueOf(jsonObject.getString("orderPayStatus"));
			}

			// 订单来源
			OrderSource orderSource = null;
			if (!StringUtils.isBlank(jsonObject.getString("orderSource"))) {
				orderSource = OrderSource.valueOf(jsonObject.getString("orderSource"));
			}

			// 订单状态
			MainOrderStatus orderStatus = null;
			if (!StringUtils.isBlank(jsonObject.getString("orderStatus"))) {
				orderStatus = MainOrderStatus.valueOf(jsonObject.getString("orderStatus"));
			}
			// 订单行程类型
			RouteType routeType = null;
			if (!StringUtils.isBlank(jsonObject.getString("routeType"))) {
				routeType = RouteType.valueOf(jsonObject.getString("routeType"));
			}
			// 订单业务类型
			ServiceType serviceType = null;
			if (!StringUtils.isBlank(jsonObject.getString("serviceType"))) {
				serviceType = ServiceType.valueOf(jsonObject.getString("serviceType"));
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
			// 座位数
			Integer seats = jsonObject.getInteger("seats");
			// 用车方负责人
			String dutyMan = jsonObject.getString("dutyMan");
			// 供车方负责人
			String suppMan = jsonObject.getString("suppMan");
			// 车牌号
			String plateNum = jsonObject.getString("plateNum");

			if (fg) {
				Map<String, Object> pd = mcoDao.findMainCarOrderList(reqsrc, page, rows, find, payStatus, orderSource,
						orderStatus, startTime, endTime, compositorType, timeType, driver, seats, dutyMan, suppMan,
						plateNum, routeType, serviceType, companyNum);

				@SuppressWarnings("unchecked")
				List<MainCarOrder> totaList = (List<MainCarOrder>) pd.get("data");
				int pageint = Integer.parseInt(page);
				int rowint = Integer.parseInt(rows);
				int start = (pageint - 1) * rowint;

				List<MainCarOrder> resultList = new ArrayList<>();
				// start+m<list.size()防止越界
				for (int m = 0; m < rowint && start + m < totaList.size(); m++) {
					resultList.add(totaList.get(start + m));
				}

				// 车辆总数,一个订单算一个
				int carCount = 0;
				// 订单总价
				double totalPrice = 0;
				// 当前页总价
				double pageTotalPrice = 0;
				// 外调总价
				double externalPrice = 0;
				// 旅网总价
				double totalTravelPrepayPrice = 0;
				// 自网总价
				double totalSelfPrepayPrice = 0;
				// 已收款
				double totalAlGathPrice = 0;
				// 已付款
				// double totalAlPayPrice = 0;
				// 总的派单价格
				// double totalDisPrice = 0;
				// 应收余额,应收=总价-已收
				double shGathPrice = 0;
				// 应付余额,应付余额=总的派单价格-已付
				// double shPayPrice = 0;

				// 解决懒加载问题，统计
				/*
				 * for (MainCarOrder mainCarOrder : totaList) {
				 * 
				 * Hibernate.initialize(mainCarOrder.getOrders()); // 统计车辆数
				 * carCount += mainCarOrder.getOrders().size(); for (CarOrder
				 * carOrder : mainCarOrder.getOrders()) {
				 * Hibernate.initialize(carOrder.getDisCars());
				 * Hibernate.initialize(carOrder.getRouteMps());
				 * Hibernate.initialize(carOrder.getTrades()); // 统计订单总价
				 * totalPrice += carOrder.getPrice(); // 统计外调总价 if
				 * (carOrder.getIsExternal() != 0) { externalPrice +=
				 * carOrder.getPrice(); } // 统计旅网总价 totalTravelPrepayPrice +=
				 * carOrder.getTravelPrepayPrice(); // 统计自网总价
				 * totalSelfPrepayPrice += carOrder.getSelfPrepayPrice(); //
				 * 统计已收款 totalAlGathPrice += carOrder.getAlGathPrice(); // 统计已付款
				 * totalAlPayPrice += carOrder.getAlPayPrice(); // 统计总的派单价格
				 * totalDisPrice += carOrder.getDisPrice(); } }
				 */

				for (MainCarOrder mainCarOrder : totaList) {

					// 统计车辆数
					carCount += mainCarOrder.getNeedCars();
					// 统计订单总价
					totalPrice += mainCarOrder.getPrice();
					// 统计外调总价
					if (mainCarOrder.getIsExternal() != 0) {
						externalPrice += mainCarOrder.getPrice();
					}
					// 统计旅网总价
					totalTravelPrepayPrice += mainCarOrder.getTravelPrepayPrice();
					// 统计自网总价
					totalSelfPrepayPrice += mainCarOrder.getSelfPrepayPrice();
					// 统计已收款
					totalAlGathPrice += mainCarOrder.getAlGathPrice();

				}
				shGathPrice = totalPrice - totalAlGathPrice;
				// shPayPrice = totalDisPrice - totalAlPayPrice;

				Map<String, Object> statics = new HashMap<>();
				statics.put("carCount", carCount);
				statics.put("totalPrice", totalPrice);
				statics.put("externalPrice", externalPrice);
				statics.put("totalTravelPrepayPrice", totalTravelPrepayPrice);
				statics.put("totalSelfPrepayPrice", totalSelfPrepayPrice);
				statics.put("totalAlGathPrice", totalAlGathPrice);
				statics.put("shGathPrice", shGathPrice);

				// 统计当前页总价
				for (MainCarOrder mainCarOrder : resultList) {
					// 解决懒加载问题
					Hibernate.initialize(mainCarOrder.getMainCars());
					pageTotalPrice += mainCarOrder.getPrice();
				}
				statics.put("pageTotalPrice", pageTotalPrice);

				map.put("count", pd.get("count"));
				map.put("data", resultList);
				map.put("statics", statics);
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
	public Map<String, Object> cancelMainCarOrder(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "单位-主订单-取消", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			// 参数校验
			if (!jsonObject.containsKey("mainOrderId")) {
				U.logFalse(log, "单位-主订单-取消失败-传入参数需包含mainOrderId");
				U.setPutFalse(map, 0, "主订单id不能为空");
				return map;
			}
			if (StringUtils.isBlank(jsonObject.getString("mainOrderId"))) {
				U.logFalse(log, "单位-主订单-取消失败-mainOrderId不能为空");
				U.setPutFalse(map, 0, "主订单id不能为空");
				return map;
			}
			MainCarOrder mainCarOrder = mcoDao.findByField("id", Long.valueOf(jsonObject.getString("mainOrderId")));
			if (mainCarOrder == null) {
				U.logFalse(log, "单位-主订单-取消失败-查询不到该订单信息，mainOrderId有误");
				U.setPutFalse(map, 0, "查询不到该订单信息，mainOrderId有误");
				return map;
			}

			if (mainCarOrder.getMainOrderBase().getStatus() == MainOrderStatus.FINISHED_DIS_CAR
					|| mainCarOrder.getMainOrderBase().getStatus() == MainOrderStatus.CANCELED) {
				U.logFalse(log, "单位-主订单-取消失败-主订单状态不能取消");
				U.setPutFalse(map, 0, "取消失败,主订单状态不能取消");
				return map;
			}
			if (mainCarOrder.getMainCars().size() == 0) {
				// 派车列表为空，可以取消订单
				mainCarOrder.getMainOrderBase().setStatus(MainOrderStatus.CANCELED);// 主订单状态更改
				// 子订单状态更改
				String mainOrderNum = mainCarOrder.getOrderNum();
				String hql = "update CarOrder set status = ?0 where mainOrderNum = ?1";
				mcoDao.batchExecute(hql, OrderStatus.CANCELED, mainOrderNum);
				mcoDao.update(mainCarOrder);
				U.log(log, "单位-主订单-取消成功");
				U.setPut(map, 1, "取消成功");
				return map;
			} else {
				U.logFalse(log, "单位-主订单-取消失败-还有派单");
				U.setPutFalse(map, 0, "取消失败，还有派单");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
			return map;
		}
	}



	@Override
	public Map<String, Object> getMainCarOrderById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "单位-主订单-查询-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(jsonObject.getString("id"))) {
				U.setPutFalse(map, 0, "id参数不为空");
				return map;
			}
			MainCarOrder mainCarOrder = mcoDao.findByField("id", jsonObject.getLong("id"));
			if (mainCarOrder != null) {
				U.log(log, "单位-主订单-查询—通过id查找主订单成功");
				// 解决懒加载问题
				Hibernate.initialize(mainCarOrder.getMainCars());
				map.put("data", mainCarOrder);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "单位-主订单-查询—通过id查找主订单失败");
				U.setPutFalse(map, 0, "查找失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}



	@Override
	public Map<String, Object> cancelConfirmCollection(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "业务收款-取消车价确认", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id = jsonObject.getString("id");

			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "业务收款-取消车价确认-订单id为空");
				U.setPutFalse(map, 0, "订单id为空");
				return map;
			}
			MainCarOrder mainCarOrder = mcoDao.findByField("id", Long.parseLong(id));
			if (mainCarOrder == null) {
				U.logFalse(log, "业务收款-取消车价确认-查询不到订单");
				U.setPutFalse(map, 0, "查询不到订单，请确认id正确");
				return map;
			}
			mainCarOrder.getMainOrderBase().setConfirmCollectionName(null);
			mcoDao.update(mainCarOrder);
			U.log(log, "业务收款-取消车价确认-成功");
			U.setPut(map, 1, "取消车价确认成功");
			return map;
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			return map;
		}
	}
	
	@Override
	public Map<String, Object> serviceGath(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response, String unitNum,String operUname,String gathType, String ids,
			String gathMoney, String gathRemark,String routeDriver,String cusId,String preMoney) {
		String logtxt = U.log(log, "单位-业务收款", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(ids)){
						fg = U.setPutFalse(map, "[收款id]不能为空");
					}else{
						ids = ids.trim();
						U.log(log, "收款id="+ids);
					}
				}
				double _gathMoney=0.0;
				if(fg){
					if(StringUtils.isEmpty(gathMoney)){
						fg = U.setPutFalse(map, "[收款金额]不能为空");
					}else{
						_gathMoney = Double.valueOf(gathMoney.trim());
						if(_gathMoney==0) {
							fg = U.setPutFalse(map, "[收款金额]不能为0");
						}
						U.log(log, "收款金额="+gathMoney);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(gathRemark)){
						fg = U.setPutFalse(map, "[收款摘要]不能为空");
					}else{
						gathRemark = gathRemark.trim();
						U.log(log, "收款摘要="+gathRemark);
					}
				}
				FeeCourse fc=null;
				if(fg) {
					fc=fcDao.findByField("courseName", "业务收款");
					if(fc==null) {
						fg = U.setPutFalse(map, "[科目“业务收款”]不存在");
					}
				}
				CompanyCustom cc=null;
				FeeCourse fcDown=null;
				if(fg && StringUtils.isNotBlank(cusId)){
					fcDown=fcDao.findByField("courseName", "内部下账");
					if(fcDown==null) {
						fg = U.setPutFalse(map, "[科目“内部下账”]不存在");
					}else {
						cc=ccDao.findByField("id", cusId);
						if(cc!=null && cc.getPreMoney()<_gathMoney) {
							fg = U.setPutFalse(map, "该客户可下账余额（"+cc.getPreMoney()+"）不足");
						}
					}
				}
				FeeCourse fcPre=null;
				if(fg) {
					if(StringUtils.isNotBlank(preMoney)) {
						fcPre=fcDao.findByField("courseName", "客户预存款");
						if(fcPre==null) {
							fg = U.setPutFalse(map, "[科目“客户预存款”]不存在");
						}else {
							_gathMoney=MathUtils.add(_gathMoney, Double.valueOf(preMoney), 2);
						}
						if(cc==null) {//客户预存款
							cc=ccDao.findByField("id", cusId);
						}
					}
				}
				List<MainCarOrder> mcolist=new ArrayList<MainCarOrder>();
				MainCarOrder mco = null;
				if(fg){
					String[] id = ids.split(",");
					for (int i = 0; i < id.length; i++) {
						mco = mcoDao.find(Long.valueOf(id[i]));
						if("2".equals(gathType)) {//客户自己付尾款需要判断订单是否已经确认车价
							if(StringUtils.isBlank(mco.getMainOrderBase().getConfirmCollectionName())){
								fg = U.setPutFalse(map, "订单【"+mco.getOrderNum()+"】未确认车价，请先确认");
								break;
							}
						}
						mcolist.add(mco);
					}
				}
				if(fg){
					String operMark="";//操作标识号
					Staff staff=staffDao.findByField("baseUserId.uname", operUname);//当前员工，正常情况是出纳
					String hql="select count(id) from ReimburseList where unitNum=?0 and addTime>=?1 and addTime<=?2";
					Object sortNum=reimDao.findObj(hql, unitNum,DateUtils.getStartTimeOfDay(),DateUtils.getEndTimeOfDay());
					int sortVou=Integer.parseInt(sortNum.toString());//单位当天添加的最后一条凭证记录
					for (int i=0;i<mcolist.size();i++) {
						sortVou=sortVou+i;
						mco=mcolist.get(i);
						operMark=DateUtils.getOrderNum(7);
						if (mcolist.size() > 1) {// 多个订单收款默认每个订单均收款完成
							_gathMoney = MathUtils.sub(mco.getPrice(),mco.getAlGathPrice(),2);
						}
						// 更新已收款
						if (mco.getPrice() <= MathUtils.add(mco.getAlGathPrice(), _gathMoney, 2)) { // 已收款+本次收款>=行程总价
							mco.setPayStatus(OrderPayStatus.FULL_PAID);//已收全款
						}else {
							mco.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已收定金
						}
						mco.setAlGathPrice(MathUtils.add(mco.getAlGathPrice(), _gathMoney, 2));
						mcoDao.update(mco);
						//添加收款凭证
						ReimburseList reim=new ReimburseList();
						reim.setUnitNum(unitNum);
						reim.setReimUserId(mco.getMainOrderBase().getBaseUserId());
						reim.setDeptId(staff.getDeptId());
						reim.setGainTime(mco.getStime());
						reim.setFeeCourseId(fc);
						reim.setVoucherNum(UT.creatReimVoucher(staff.getBaseUserId().getUname(),sortVou));
						reim.setFeeStatus(fc.getCourseType());
						reim.setTotalMoney(_gathMoney);
						reim.setRemark(gathRemark);
						reim.setIsCheck(3);
						reim.setAddTime(new Date());
						reim.setReqsrc(reqsrc);
						reim.setOperMark(operMark);
						reim.setMainOrderReim(mco);
						reim.setOperNote(staff.getBaseUserId().getRealName()+"[添加]");
						reimDao.save(reim);
						if(cc!=null){//有下账客户添加支出凭证
							sortVou=sortVou+1;
							ReimburseList reimDown=new ReimburseList();
							reimDown.setUnitNum(unitNum);
							reimDown.setReimUserId(mco.getMainOrderBase().getBaseUserId());
							reimDown.setDeptId(staff.getDeptId());
							reimDown.setGainTime(mco.getStime());
							reimDown.setFeeCourseId(fcDown);
							reimDown.setVoucherNum(UT.creatReimVoucher(staff.getBaseUserId().getUname(),sortVou));
							reimDown.setFeeStatus(fcDown.getCourseType());
							reimDown.setTotalMoney(_gathMoney);
							reimDown.setRemark(gathRemark);
							reimDown.setIsCheck(3);
							reimDown.setAddTime(new Date());
							reimDown.setReqsrc(reqsrc);
							reimDown.setOperMark(operMark);
							reimDown.setMainOrderReim(mco);
							reimDown.setOperNote(staff.getBaseUserId().getRealName()+"[添加]");
							reimDao.save(reimDown);
							//减去客户预存款
							cc.setPreMoney(MathUtils.sub(cc.getPreMoney(), _gathMoney, 2));
							ccDao.update(cc);
						}
					}
					if(fcPre!=null){//客户预存款
						sortVou=sortVou+1;
						ReimburseList reimPre=new ReimburseList();
						reimPre.setUnitNum(unitNum);
						reimPre.setReimUserId(mco.getMainOrderBase().getBaseUserId());
						reimPre.setDeptId(staff.getDeptId());
						reimPre.setGainTime(mco.getStime());
						reimPre.setFeeCourseId(fcPre);
						reimPre.setVoucherNum(UT.creatReimVoucher(staff.getBaseUserId().getUname(),sortVou));
						reimPre.setFeeStatus(fcPre.getCourseType());
						reimPre.setTotalMoney(Double.valueOf(preMoney));
						reimPre.setRemark(gathRemark);
						reimPre.setIsCheck(3);
						reimPre.setAddTime(new Date());
						reimPre.setReqsrc(reqsrc);
						reimPre.setOperMark(DateUtils.getOrderNum(7));
						reimPre.setMainOrderReim(mco);
						reimPre.setOperNote(staff.getBaseUserId().getRealName()+"[添加]");
						reimDao.save(reimPre);
						//添加客户预存款
						cc.setPreMoney(MathUtils.add(cc.getPreMoney(), Double.valueOf(preMoney), 2));
						ccDao.update(cc);
					}
					U.setPut(map, 1,  "收款成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	


	@Override
	public Map<String, Object> getMainCarOrderForCollection(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject,String unitNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-单位管理-业务收款分页", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg)
				fg = U.valPageNo(map, jsonObject.getString("page"), jsonObject.getString("rows"), "订单");
			String page = jsonObject.getString("page");
			String rows = jsonObject.getString("rows");

			// 订单支付状态
			OrderPayStatus payStatus = null;
			if (!StringUtils.isBlank(jsonObject.getString("payStatus"))) {
				payStatus = OrderPayStatus.valueOf(jsonObject.getString("payStatus"));
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
			
			// 车牌号
			String plateNum = jsonObject.getString("plateNum");
			// 订单号
			String orderNum = jsonObject.getString("orderNum");
			// 业务员
			String serviceMan = jsonObject.getString("serviceMan");
			// 用车方uname
			String customer = jsonObject.getString("customer");
			// 是否自营
			String businessType = jsonObject.getString("businessType");

			if (fg) {
				Page<MainCarOrder> pd = mcoDao.getMainCarOrderForCollection(reqsrc, page, rows, payStatus, startTime, endTime, compositorType, timeType, driver, dutyService, plateNum, orderNum, routeDetail, serviceMan, unitNum, customer, businessType);
				// 解决懒加载问题
				for (MainCarOrder mainCarOrder : pd.getResult()) {
					Hibernate.initialize(mainCarOrder.getMainCars());
				}
				U.setPageData(map, pd);
				Map<String, Object> countMainCarOrderForCollection = mcoDao.countMainCarOrderForCollection(reqsrc, page, "100000", payStatus, startTime, endTime, compositorType, timeType, driver, dutyService, plateNum, orderNum, routeDetail, serviceMan, unitNum, customer, businessType);
				map.put("statics", countMainCarOrderForCollection);
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
	public Map<String, Object> confirmCollection(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject,Customer customer) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "单位订单-确认收款价格", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			String id = jsonObject.getString("mainOrderid");
			
			if (fg) {
				if (StringUtils.isBlank(id)) {
					U.logFalse(log, "单位订单-确认收款价格-订单id为空");
					fg = U.setPutFalse(map, 0, "订单id为空");
				}
			}
			if (fg) {
				if (null == customer) {
					U.logFalse(log, "单位订单-确认收款价格-获取账户信息失败");
					fg = U.setPutFalse(map, 0, "获取账号信息失败");
				}
			}
			if (fg) {
				
				MainCarOrder mainCarOrder = mcoDao.findByField("id", Long.parseLong(id));
				
				if (mainCarOrder != null) {
					if (mainCarOrder.getMainOrderBase().getServiceMan().equals(customer.getBaseUserId().getRealName())) {
						//确认人是主订单业务员,可以确认
						mainCarOrder.getMainOrderBase().setConfirmCollectionName(customer.getBaseUserId().getRealName());
						mcoDao.update(mainCarOrder);
						U.log(log, "单位订单-确认收款价格-success");
						U.setPut(map, 1, "确认收款价格成功");
					}
					else{
						U.logFalse(log, "单位订单-确认收款价格-fail,业务员与当前账号不匹配");
						U.setPutFalse(map, 0, "确认收款价格失败,订单【" + mainCarOrder.getOrderNum() + "】业务员不是你");
					}					
				} else {
					U.logFalse(log, "单位订单-确认收款价格-fail,查询不到订单信息");
					U.setPutFalse(map, 0, "确认收款价格失败,查询不到订单信息");
				}
			}
		
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
}
