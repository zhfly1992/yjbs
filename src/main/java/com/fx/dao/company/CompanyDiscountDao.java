package com.fx.dao.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.dao.order.CarPriceDao;
import com.fx.dao.order.OrderParamDao;
import com.fx.entity.company.CompanyDiscount;
import com.fx.entity.company.MyCustomers;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.CarPrice;
import com.fx.entity.order.OrderParam;

@Repository
public class CompanyDiscountDao extends ZBaseDaoImpl<CompanyDiscount, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());

	/** 单位-服务 */
	@Autowired
	private CompanyUserDao companyUserDao;
	/** 单程接送-临时订单参数-服务 */
	@Autowired
	private OrderParamDao orderParamDao;
	/** 单程接送-临时车辆价格-服务 */
	@Autowired
	private CarPriceDao carPriceDao;
	/** 车辆订单-服务 */
	@Autowired
	private CarOrderDao carOrderDao;
	/** 我的合作客户-服务 */
	@Autowired
	private MyCustomersDao myCustomersDao;
	
	
	
	public Map<String, Object> findUseCouponList(ReqSrc reqsrc, Customer lcus, String companyNum, String selCarId) {
		String logtxt = U.log(log, "获取-用户所有优惠券列表和订单可用优惠券编号数组", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg){
				if(lcus == null){
					fg = U.setPutFalse(map, "[用户]不能为空");
				}
				
				U.log(log, "[用户名] uname="+lcus.getBaseUserId().getUname());
			}
			
			CompanyUser ctl = null;// 优惠券所属车队
			if(fg){
				if(StringUtils.isEmpty(companyNum)){
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else{
					companyNum = companyNum.trim();
					ctl = companyUserDao.findByField("unitNum", companyNum);
					if(ctl == null){
						fg = U.setPutFalse(map, "[车队]不存在");
					}
					
					U.log(log, "[车队编号] companyNum="+companyNum);
				}
			}
			
			CarPrice cp = null;
			OrderParam op = null;
			if(fg){
				if(StringUtils.isEmpty(selCarId)){
					fg = U.setPutFalse(map, "[选择车辆数据id]不能为空");
				}else{
					selCarId = selCarId.trim();
					if(!FV.isLong(selCarId)){
						fg = U.setPutFalse(map, "[选择车辆数据id]格式错误");
					}else{
						cp = carPriceDao.findByField("id", Long.parseLong(selCarId));
						if(cp == null){
							fg = U.setPutFalse(map, "[选择车辆数据]不存在");
						}else if(!lcus.getBaseUserId().getUname().equals(cp.getUname())){
							fg = U.setPutFalse(map, "该[选择车辆数据]不是您的");
						}else{
							op = orderParamDao.findByField("id", cp.getOpid());
							if(op == null){
								fg = U.setPutFalse(map, "[发布订单参数对象]为空");
							}
						}
					}
					
					U.log(log, "[选择车辆数据id] selCarId="+selCarId);
				}
			}
			
			List<CompanyDiscount> mylist = new ArrayList<CompanyDiscount>();
			if(fg){
				// 条件：指定车队、指定用户、未使用、指定优惠券使用有效期之间
				hql = "from CompanyDiscount where discountNo like ? and receiptName = ? and useState = ? and (receiptStart <= ? and receiptEnd >= ?) order by receiptStart asc";
				mylist = findhqlList(hql, "%"+companyNum, lcus.getBaseUserId().getUname(), 0, new Date(), new Date());
			}
			
			if(fg){
				List<CompanyDiscount> uselist = new ArrayList<CompanyDiscount>();
				List<CompanyDiscount> nouselist = new ArrayList<CompanyDiscount>();
				
				for (CompanyDiscount ctd : mylist) {
					Map<String, Object> ism = getCouponRealMoney(reqsrc, ctd, cp, op);
					if("1".equals(ism.get("code").toString())){
						uselist.add(ctd);
					}else{
						nouselist.add(ctd);
					}
				}
				
				map.put("uselist", uselist);
				map.put("nouselist", nouselist);
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	public Map<String, Object> getCouponRealMoney(ReqSrc reqsrc, CompanyDiscount ctd, CarPrice cp, 
		OrderParam op){
		String logtxt = U.log(log, "获取-指定优惠券[实际优惠金额]", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		double useMoney = 0d;
		
		try {
			// 判断-优惠券使用有效期
			if(fg){
				if(ctd.getReceiptStart().getTime() > new Date().getTime()){
					fg = U.setPutFalse(map, "该[优惠券]未到使用时间");
				}else if(ctd.getReceiptEnd().getTime() < new Date().getTime()){
					fg = U.setPutFalse(map, "该[优惠券]已过使用时间");
				}
			}
			
			// 判断-优惠券使用场景
			if(fg){
				if(ctd.getValidScene() != op.getUseCase()){
					fg = U.setPutFalse(map, "该[优惠券]不适用此订单");
				}
			}
			
			// 判断-优惠券适用车型（即座位数）
			if(fg){
				String[] seatArr = ctd.getValidSeats().split(",");
				if(!Arrays.asList(seatArr).contains(cp.getSeat()+"")){
					fg = U.setPutFalse(map, "该[优惠券]不适用当前所选车型座位数");
				}
			}
			
			// 判断-优惠券金额是否已使用完
			if(fg){
				if(ctd.getUseMoney() >= ctd.getHighMoney()){
					fg = U.setPutFalse(map, "该[优惠券]金额已使用完");
				}
			}
			
			// 判断-适用用户
			if(fg){
				if(ctd.getValidCustomer() == 2){// 是否是：新客户
					// 新客户条件：1.没有任何客户订单； 2.最后一条订单的下单时间与当前时间的天数大于validCount设置的天数；
					
					// 条件：指定车队、指定用户、订单状态0,1,5,6
					hql = "from CarOrder where teamNo = ? and mbName = ? and (orderState = ? or orderState = ? or orderState = ? or orderState = ?) and (askTime <= ? and askTime >= ?) order by askTime desc";
					List<CarOrder> lastcol = carOrderDao.hqlListFirstMax(hql, 0, 1, op.getCompanyNum(), op.getUname(), 0, 1, 5, 6, DateUtils.getPlusDaysD(new Date(), ctd.getValidCount()), new Date());
					if(lastcol.size() == 0){// 几天之内为下单
						fg = U.setPutFalse(map, "您不是新用户，不适用该[优惠券]");
					}
				}else if(ctd.getValidCustomer() == 3){// 下单成功客户
					// 下单成功客户：多久开始下过多少订单内的用户；
					
					// 条件：指定车队、指定用户、订单状态未取消&未拒绝、指定下单时间开始
					hql = "select count(id) from CarOrder where companyNum = ? and mbName = ? and (orderState <> ? and orderState <> ?) and askTime >= ?";
					Object o = carOrderDao.findObj(hql, op.getCompanyNum(), op.getUname(), 2, 4, ctd.getSuccessStart());
					if(o != null && Integer.parseInt(o.toString()) > ctd.getValidCount()){
						fg = U.setPutFalse(map, "您的下单量不适用该[优惠券]");
					}
				}else if(ctd.getValidCustomer() == 4){// 客户星级
					// 获取-订单所属车队
					CompanyUser ctl = companyUserDao.findByField("unitNum", op.getCompanyNum());
					
					hql = "from MyCustomers where myName = ? and cusName = ?";
					MyCustomers mcus = myCustomersDao.findObj(hql, ctl.getManagerName(), op.getUname());
					if(mcus != null && mcus.getCusLevel() < ctd.getValidCount()){
						fg = U.setPutFalse(map, "抱歉，您的星级不够，不能适用该[优惠券]");
					}
				}
			}
			
//			// 使用金额 【暂时关闭】
//			if(fg){
//				// 优惠券余额 = 优惠券金额 - 已使用金额
//				double ctdBalance = MathUtils.sub(ctd.getHighMoney(), ctd.getUseMoney(), 2);
//				
//				// 判断-使用方式
//				if(ctd.getValidWay() == 2){
//					U.log(log, "按订单比例");
//					
//					// 订单可用金额 = 订单金额 * 使用比例
//					double orderUseMoney = MathUtils.mul(cp.getPrice(), ctd.getUseRatio(), 2);
//					if(ctdBalance >= orderUseMoney){
//						U.log(log, "优惠券余额 >= 订单可用金额（最终使用金额 = 订单可用金额）");
//						
//						useMoney = orderUseMoney;
//					}else{
//						U.log(log, "优惠券余额 < 订单可用金额（最终使用金额 = 优惠券余额）");
//						
//						useMoney = ctdBalance;
//					}
//				}else if(ctd.getValidWay() == 3){
//					U.log(log, "按优惠券比例");
//					
//					// 优惠券可用金额 = 优惠券金额 * 使用比例
//					double ctdUseMoney = MathUtils.mul(ctd.getHighMoney(), ctd.getUseRatio(), 2);
//					if(ctdBalance >= ctdUseMoney){
//						U.log(log, "优惠券余额 >= 优惠券可用金额（最终使用金额 = 优惠券可用金额）");
//						
//						useMoney = ctdUseMoney;
//					}else{
//						U.log(log, "优惠券余额 < 优惠券可用金额（最终使用金额 = 优惠券余额）");
//						
//						useMoney = ctdBalance;
//					}
//				}else{
//					U.log(log, "全额抵扣（最终使用金额 = 优惠券余额）");
//					
//					useMoney = ctdBalance;
//				}
//			}
			
			if(fg){
				useMoney = ctd.getHighMoney();
				map.put("useMoney", useMoney);
				
				U.setPut(map, 1, "优惠成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	

}
