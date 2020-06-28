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
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.finance.BankTradeListDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.FeeCourseTradeDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.dao.order.MainCarOrderDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Dept;
import com.fx.entity.finance.BankTradeList;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.FeeCourseTrade;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.finance.StaffReimburse;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;
import com.fx.entity.order.RouteTradeList;
import com.fx.service.finance.ReimburseListService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class ReimburseListServiceImpl extends BaseServiceImpl<ReimburseList,Long> implements ReimburseListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	/** 缓存-服务 */
	@Autowired
    private RedisUtil redis;

	
	@Autowired
	private ReimburseListDao reimDao;
	@Override
	public ZBaseDaoImpl<ReimburseList, Long> getDao() {
		return reimDao;
	}
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao fcDao;
	
	/** 主订单-服务 */
	@Autowired
	private MainCarOrderDao mcoDao;
	
	/** 子订单-服务 */
	@Autowired
	private CarOrderDao coDao;
	
	/** 科目交易记录-服务 */
	@Autowired
	private FeeCourseTradeDao fctDao;
	
	/** 员工报账记录-服务 */
	@Autowired
	private StaffReimburseDao srDao;
	
	/** 银行账记录-服务 */
	@Autowired
	private BankTradeListDao btlDao;
	
	/** 单位客户-服务 */
	@Autowired
	private CompanyCustomDao ccDao;
	
	
	
	@Override
	public Map<String, Object> findReimList(ReqSrc reqsrc, String page, String rows,String unitNum, String uname,String vouNum,String status,
			String isCheck,String courseName,String myBank,String reimIsCar,String reimPlateNum,
			String reimZy,String reimMoney,String operMark,String sTime,String eTime) {
		String logtxt = U.log(log, "获取-凭证-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "凭证");
			/*****参数--验证--end******/
			if(fg){
				Page<ReimburseList> pd = reimDao.findReimList(reqsrc, page, rows, unitNum, uname, vouNum, status, 
						isCheck, courseName, myBank, reimIsCar, reimPlateNum, reimZy, reimMoney, operMark, sTime, eTime);
				U.setPageData(map, pd);
				double totalGath=0;//总收入
				double totalPay=0;//总支出
				double singleFee=0;//打单费
				double washingFee=0;//洗车费
				double parkingFee=0;//停车费
				double roadFee=0;//过路费
				double livingFee=0;//生活费
				double otherFee=0;//其他费
				double waterFee=0;//买水费
				double stayFee=0;//住宿费
				// 解决懒加载问题
				for (ReimburseList reim : pd.getResult()) {
					Hibernate.initialize(reim.getCourseTrades());
					if(!reim.getCourseTrades().isEmpty()) {
						for (FeeCourseTrade fct:reim.getCourseTrades()) {
							if(fct.getFeeCourseId().getCourseType()==0) {
								totalGath+=reim.getTotalMoney();
							}else {
								totalPay+=reim.getTotalMoney();
							}
							if(fct.getStaffReimId()!=null) {
								singleFee+=fct.getStaffReimId().getOrderTrade().getSingleFee();washingFee+=fct.getStaffReimId().getOrderTrade().getWashingFee();
								parkingFee+=fct.getStaffReimId().getOrderTrade().getParkingFee();roadFee+=fct.getStaffReimId().getOrderTrade().getRoadFee();
								livingFee+=fct.getStaffReimId().getOrderTrade().getLivingFee();otherFee+=fct.getStaffReimId().getOrderTrade().getOtherFee();
								waterFee+=fct.getStaffReimId().getOrderTrade().getWaterFee();stayFee+=fct.getStaffReimId().getOrderTrade().getStayFee();
							}
						}
					}
				}
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(ReimburseList.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
				fmap.put(U.getAtJsonFilter(FeeCourseTrade.class), new String[]{});
				fmap.put(U.getAtJsonFilter(StaffReimburse.class), new String[]{});
				fmap.put(U.getAtJsonFilter(BankTradeList.class), new String[]{});
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(CarOrder.class), new String[]{});
				
				map.put(QC.FIT_FIELDS, fmap);
				map.put("totalGath", totalGath);
				map.put("totalPay", totalPay);
				map.put("balance", MathUtils.sub(totalGath, totalPay, 2));
				map.put("singleFee", singleFee);
				map.put("washingFee", washingFee);
				map.put("parkingFee", parkingFee);
				map.put("roadFee", roadFee);
				map.put("livingFee", livingFee);
				map.put("otherFee", otherFee);
				map.put("waterFee", waterFee);
				map.put("stayFee", stayFee);
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> modifyReim(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String updId, String modifyFeeCourse,String gainTime) {//只能修改科目，记账时间
		String logtxt = U.log(log, "单位-修改凭证", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			ReimburseList obj=null;
			if(fg){
				if(StringUtils.isEmpty(updId)){
					fg = U.setPutFalse(map, "[修改记录]不能为空");
				}else{
					updId = updId.trim();
					obj=reimDao.findByField("id", Long.valueOf(updId));
				}
			}
			Date _gainTime = null;
			if(fg){
				if(StringUtils.isEmpty(gainTime)){
					fg = U.setPutFalse(map, "[记账时间]不能为空");
				}else{
					gainTime = gainTime.trim();
					if(!FV.isDate(gainTime)){
						fg = U.setPutFalse(map, "[记账时间]格式不正确");
					}else{
						_gainTime = DateUtils.strToDate(gainTime);
					}
					
					U.log(log, "[记账时间] _gainTime="+_gainTime);
				}
			}
			String [] faceInfos=null;
			if(fg) {
				if(StringUtils.isEmpty(modifyFeeCourse)){
					fg = U.setPutFalse(map, "[科目信息]不能为空");
				}else {
					faceInfos=modifyFeeCourse.split("@");
				}
			}
			if(fg) {
				//添加修改科目交易记录
				//List<FeeCourseTrade> fctlist=new ArrayList<FeeCourseTrade>();
				String [] feeIds=null;//原科目交易记录id=新科目id@...
				FeeCourseTrade fct=null;//原科目交易记录
				FeeCourse oldFee=null;//原科目
				FeeCourse newFee=null;//修改后的科目
				for (int i = 0; i < faceInfos.length; i++) {
					feeIds=faceInfos[i].split("=");
					fct=fctDao.findByField("id", Long.valueOf(feeIds[0]));
					oldFee=fct.getFeeCourseId();
					newFee=fcDao.findByField("id", Long.valueOf(feeIds[1]));//修改后的科目
					if(oldFee!=newFee) {//原科目交易记录不等于修改的科目
						//给原科目加1条平账记录
						FeeCourseTrade fctPz=new FeeCourseTrade();
						fctPz.setUnitNum(LU.getLUnitNum(request, redis));
						fctPz.setFeeCourseId(oldFee);
						if(fct.getGathMoney()>0) {//原来是收入，平账就加支出
							fctPz.setPayMoney(fct.getGathMoney());
						}else {//原来是支出，平账就加收入
							fctPz.setGathMoney(fct.getPayMoney());
						}
						fctPz.setRemark(fct.getRemark()+"（原科目改变为："+newFee.getCourseName()+"后平账）");
						fctPz.setAddTime(new Date());
						fctDao.save(fctPz);
						//更新原科目余额
						oldFee.setBalance(MathUtils.add(oldFee.getBalance(), MathUtils.sub(fctPz.getGathMoney(), fctPz.getPayMoney(), 2), 2));
						fcDao.update(oldFee);
						obj.getCourseTrades().remove(fct);
						
						//新科目加1条交易记录
						FeeCourseTrade fctNew=new FeeCourseTrade();
						fctNew.setUnitNum(LU.getLUnitNum(request, redis));
						fctNew.setFeeCourseId(newFee);
						fctNew.setRemark(fct.getRemark());
						fctNew.setGathMoney(fct.getGathMoney());
						fctNew.setPayMoney(fct.getPayMoney());
						fctNew.setAddTime(new Date());
						if(fct.getBankTradeId()!=null)fctNew.setBankTradeId(fct.getBankTradeId());//新科目关联银行账
						if(fct.getMainOrderId()!=null)fctNew.setMainOrderId(fct.getMainOrderId());//新科目关联主订单
						if(fct.getCarOrderId()!=null)fctNew.setCarOrderId(fct.getCarOrderId());  //新科目关联子订单
						if(fct.getStaffReimId()!=null)fctNew.setStaffReimId(fct.getStaffReimId());//新科目关联员工报账
						//fctlist.add(fctNew);
						//更新新科目余额
						newFee.setBalance(MathUtils.add(newFee.getBalance(), MathUtils.sub(fctNew.getGathMoney(), fctNew.getPayMoney(), 2), 2));
						fcDao.update(newFee);
						obj.getCourseTrades().add(fctNew);
						
						/*//报账记录里面更新科目
						if(!obj.getStaffReims().isEmpty()) {
							for (StaffReimburse eachsr:obj.getStaffReims()) {
								if(eachsr.getFeeCourseId()==oldFee) {
									eachsr.setFeeCourseId(newFee);
									srDao.update(eachsr);
								}
							}
						}
						//银行账记录里面更新科目
						if(!obj.getBankTrades().isEmpty()) {
							for (BankTradeList btl:obj.getBankTrades()) {
								if(btl.getFeeCourseId()==oldFee) {
									btl.setFeeCourseId(newFee);
									btlDao.update(btl);
								}
							}
						}*/
					}else {
						//fctlist.add(fct);//原科目不变
					}
				}
				//生成凭证
				//obj.setCourseTrades(fctlist);
				obj.setGainTime(_gainTime);
				obj.setOperNote(obj.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "修改"));
				reimDao.update(obj);
				U.setPut(map, 1, "凭证修改成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	
	@Override
	public Map<String, Object> delReim(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String delId) {
		String logtxt = U.log(log, "单位-删除凭证记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				ReimburseList reim=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						reim=reimDao.findByField("id", Long.valueOf(delId));
						if(reim==null) {
							fg = U.setPutFalse(map, "凭证不存在");
						}else {
							if(reim.getIsCheck()!=0) {
								fg = U.setPutFalse(map, "该凭证状态不能删除");
							}
						}
					}
				}
				if(fg){
					if(!reim.getCourseTrades().isEmpty()) {//有科目交易记录添加科目平账记录
						FeeCourse oldFee=null;
						for (FeeCourseTrade fct:reim.getCourseTrades()) {
							oldFee=fct.getFeeCourseId();
							FeeCourseTrade fctPz=new FeeCourseTrade();
							fctPz.setUnitNum(LU.getLUnitNum(request, redis));
							fctPz.setFeeCourseId(oldFee);
							if(fct.getGathMoney()>0) {//原来是收入，平账就加支出
								fctPz.setPayMoney(fct.getGathMoney());
							}else {//原来是支出，平账就加收入
								fctPz.setGathMoney(fct.getPayMoney());
							}
							fctPz.setRemark(fct.getRemark()+"（凭证删除后平账）");
							fctPz.setAddTime(new Date());
							fctDao.save(fctPz);
							//更新原科目余额
							oldFee.setBalance(MathUtils.add(oldFee.getBalance(), MathUtils.sub(fctPz.getGathMoney(), fctPz.getPayMoney(), 2), 2));
							fcDao.update(oldFee);
							if(fct.getStaffReimId()!=null) {////有员工报账记录恢复为未生成凭证
								fct.getStaffReimId().setFaceCourseId(null);
								fct.getStaffReimId().setIsCheck(1);
								fct.getStaffReimId().setOperNote(fct.getStaffReimId().getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "凭证删除"));
								srDao.update(fct.getStaffReimId());
							}
							if(fct.getBankTradeId()!=null) {//有银行账记录恢复为未生成凭证
								fct.getBankTradeId().setIsCheck(0);//未操作
								fct.getBankTradeId().setCheckMoney(0);
								fct.getBankTradeId().setCusName(null);//客户名称
								fct.getBankTradeId().setVoucherNumber(null);
								fct.getBankTradeId().setDocumentNumber(null);
								fct.getBankTradeId().setNoticeMan(null);
								fct.getBankTradeId().setNoticeRemark(null);
								fct.getBankTradeId().setOrderNum(null);
								fct.getBankTradeId().setOperNote(fct.getBankTradeId().getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "凭证删除"));
								btlDao.update(fct.getBankTradeId());
							}
							if(fct.getMainOrderId()!=null) {//有主订单恢复为未生成凭证
								MainCarOrder mco=fct.getMainOrderId();
								mco.setAlGathPrice(MathUtils.sub(mco.getAlGathPrice(),fct.getGathMoney(), 2));
								if(mco.getAlGathPrice()==0) {
									mco.setPayStatus(OrderPayStatus.UNPAID);// 未收款
								}else if(mco.getAlGathPrice()>0){
									mco.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已收定金
								}
								mcoDao.update(mco);
							}
							if(fct.getCarOrderId()!=null) {//有子订单恢复为未生成凭证
								CarOrder co=fct.getCarOrderId();
								co.setAlPayPrice(MathUtils.sub(co.getAlPayPrice(),fct.getPayMoney(), 2));
								if(co.getAlPayPrice()==0) {
									co.setPayStatus(OrderPayStatus.UNPAID);// 未付款
								}else if(co.getAlPayPrice()>0){
									co.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已付定金
								}
								coDao.update(co);
							}
						}
					}
					reimDao.delete(reim);
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
	public Map<String, Object> findReimById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "查询-凭证信息-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[查询id]不能为空");
				}
			}

			if (fg) {
				ReimburseList reim = reimDao.findByField("id", Long.valueOf(id));
				Hibernate.initialize(reim.getCourseTrades());
				
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(ReimburseList.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
				fmap.put(U.getAtJsonFilter(FeeCourseTrade.class), new String[]{});
				fmap.put(U.getAtJsonFilter(StaffReimburse.class), new String[]{});
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(CarOrder.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				map.put("data", reim);
				U.setPut(map, 1, "查询成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
	
	@Override
	public Map<String, Object> confirmReim(ReqSrc reqsrc, HttpServletRequest request, 
			String ids, String money, String note,String myBankInfo, String transInfo) {
		String logtxt = U.log(log, "财务记账凭证-核销", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(ids)){
						fg = U.setPutFalse(map, "[核销id]不能为空");
					}else{
						ids = ids.trim();
						U.log(log, "核销id="+ids);
					}
				}
				/*if(fg){//判断当前银行余额是否足够支付本次报销金额
					if("1".equals(currStatus) && "1".equals(isBzy)){
						String hql="FROM TeamAccountBook WHERE unitNum=? and receiveName like ? and bookType=2 order by addTime desc";
						TeamAccountBook tab=tabSer.findObj(hql, unitNum,"%"+WebUtil.getLUser(request).getcName()+"%","LIMIT 1");
						if(tab!=null && tab.getBalance()<Double.valueOf(payMoney)){
							fg = U.setPutFalse(map, "当前银行[余额不足]不能打款");
						}
					}
				}*/
				if(fg){
					String[] id = ids.split(",");
					String operMark="";
					ReimburseList reim=null;
					Map<String,List<RouteTradeList>> rtlMap=new HashMap<String,List<RouteTradeList>>();
					for (int i = 0; i < id.length; i++) {
						operMark=UT.creatOperMark();
						reim = reimDao.find(Long.valueOf(id[i]));
						if(!reim.getCourseTrades().isEmpty()) {//员工报账处理已收，已付，客户预存款，预存款下账，行程收支
							for (FeeCourseTrade fct:reim.getCourseTrades()) {
								/*if(sr.getFaceCourseId()!=null) {
									if("主营业务成本-外调车费".equals(sr.getFaceCourseId().getCourseName())) {//业务付款
										CarOrder co=sr.getCarOrderReim();
										// 更新已付款
										if (co.getDisPrice() <= MathUtils.add(co.getAlPayPrice(), sr.getPayMoney(), 2)) { // 已付款+本次付款>=行程总价
											co.setPayStatus(OrderPayStatus.FULL_PAID);// 全款已付
										} else {
											co.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已付定金
										}
										co.setAlPayPrice(MathUtils.add(co.getAlPayPrice(), sr.getPayMoney(), 2));
										coDao.update(co);
									}
									if("主营业务收入".equals(sr.getFaceCourseId().getCourseName())) {//业务收款
										MainCarOrder mco=sr.getMainOrderReim();
										// 更新已收款
										if (mco.getPrice() <= MathUtils.add(mco.getAlGathPrice(), sr.getGathMoney(), 2)) { // 已收款+本次收款>=行程总价
											mco.setPayStatus(OrderPayStatus.FULL_PAID);// 已收全款
										} else {
											mco.setPayStatus(OrderPayStatus.DEPOSIT_PAID);// 已收定金
										}
										mco.setAlGathPrice(MathUtils.add(mco.getAlGathPrice(), sr.getGathMoney(), 2));
										mcoDao.update(mco);
										if(sr.getFaceCourseId()!=null && sr.getPreUserId()!=null) {//本次收款是下客户账
											// 减去客户预存款
											CompanyCustom cc=sr.getPreUserId();
											cc.setPreMoney(MathUtils.sub(cc.getPreMoney(), sr.getGathMoney(), 2));
											ccDao.update(cc);
										}
									}
									if("预收账款".equals(sr.getFaceCourseId().getCourseName()) && sr.getGathMoney()>0) {//客户预存款
										// 增加客户预存款
										CompanyCustom cc=sr.getPreUserId();
										cc.setPreMoney(MathUtils.add(cc.getPreMoney(), sr.getGathMoney(), 2));
										ccDao.update(cc);
									}
								}*/
								if(fct.getStaffReimId().getOrderTrade()!=null) {//有行程开支
									if(rtlMap.get(fct.getStaffReimId().getOrderTrade().getOrderNum())==null) {
										List<RouteTradeList> rtlOrder=new ArrayList<RouteTradeList>();
										rtlOrder.add(fct.getStaffReimId().getOrderTrade());
										rtlMap.put(fct.getStaffReimId().getOrderTrade().getOrderNum(), rtlOrder);
									}else {
										rtlMap.get(fct.getStaffReimId().getOrderTrade().getOrderNum()).add(fct.getStaffReimId().getOrderTrade());
									}
								}
							}
						}
						if(rtlMap.size()>0) {//将行程收支添加到对应订单
							for(Map.Entry<String, List<RouteTradeList>> entry: rtlMap.entrySet()){
								CarOrder co=coDao.findByField("orderNum", entry.getKey());
								co.setTrades(entry.getValue());
								coDao.update(co);
							}
						}
						reim.setIsCheck(1);
						reim.setOperMark(operMark);
						reim.setMyBankInfo(myBankInfo);
						reim.setTransferInfo(transInfo);
						reim.setOperNote(reim.getOperNote()+","+note+Util.getOperInfo(LU.getLRealName(request, redis), "核销"));
						reimDao.update(reim);
						//发送微信通知报销人
						//ymSer.orderWaitforCheckOfWxmsg(null, reim, reim.getReimUserId().getUname());
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
	
/*	@Override
	public Map<String, Object> cancelReim(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response, String cId) {
		String logtxt = U.log(log, "后台财务-撤销财务已审核的报销凭证", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(cId)){
						fg = U.setPutFalse(map, "[记录id]不能为空");
					}else{
						cId = cId.trim();
						if(FV.isLong(cId)){
							obj = reimDao.find(Long.parseLong(cId));
							if(obj == null){
								fg = U.setPutFalse(map, "该记录不存在");
							}
						}
						
						U.log(log, "[撤销记录id] id="+cId);
					}
				}
				if(fg){
					if(StringUtils.isNotBlank(obj.getMyBankInfo())){
						fg = U.setPutFalse(map, "该记录已报销，不能撤销");
					}
				}
				if(fg){
					obj.setIsCheck(0);
					reimDao.update(obj);
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
	}*/
	/*@Override
	public Map<String, Object> reimExport(MultipartFile file,HttpServletRequest request,ReqSrc reqsrc) {
		String logtxt = U.log(log, "etc导入");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,
						file.getOriginalFilename().length());
				if(file.isEmpty() || suffix==null){
					fg=U.setPutFalse(map, "文件为空，请检查");
				}
				if(fg){
					if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
						fg=U.setPutFalse(map, "上传excel文件后缀必须是xls或者xlsx");
					}
				}
				if(fg){
					// 文件保存路径
					SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
					String filePath = request.getSession().getServletContext().getRealPath("/")+ "contractModel\\"+dfs.format(new Date())+file.getOriginalFilename();
					// 转存文件
					file.transferTo(new File(filePath));
					POIUtils poiUtils = new POIUtils(); //POI读写Excel
					// 路径 文件名 工作薄下标 开始行数
					List<List<String>> list = poiUtils.getExcelData(new File(filePath), "etcRecord."+suffix, 0, 1);
					//循环遍历从excel中获取的数据，进行计算
					ReimburseList reim=null;
					List<String> feerow=null;
					if(fg){
						String hql="from ReimburseList where unitNum=? and voucherNumber=? and cName=? and feeType=? " +
								"and totalMoney=?";
						for (int i = 0; i < list.size(); i++) {
							feerow = (List<String>) list.get(i); //获取每行数据，格式：wlx1,1000,2012-12-12 12:12:12（时间可以不填写）
							if(StringUtils.isNotBlank(feerow.get(8))){
								reim=reimDao.findObj(hql,LU.getLUnitNum(request, redis),feerow.get(1),feerow.get(4).trim(),feerow.get(3).trim(),
										Double.valueOf(feerow.get(8)));
							}else{
								reim=reimDao.findObj(hql,LU.getLUnitNum(request, redis),feerow.get(2),feerow.get(5).trim(),feerow.get(4).trim(),
										Double.valueOf(feerow.get(9)));
							}
							if(reim==null){
								reim = new ReimburseList(); //创建导入数据对象
								reim.setUnitNum(LU.getLUnitNum(request, redis));
								if(StringUtils.isNotBlank(feerow.get(0))){
									reim.setGainTime(DateUtils.strToDate(DateUtils.yyyy_MM_dd, feerow.get(0)));
								}else{
									reim.setGainTime(new Date());
								}
								if(StringUtils.isNotBlank(feerow.get(1)))reim.setVoucherNum(feerow.get(1));
								if(feerow.get(2).trim().equals("正常报销")){
									reim.setReimType(0);
								}else if(feerow.get(2).trim().equals("内部账报销")){
									reim.setReimType(1);
								}else{
									reim.setReimType(2);
								}
							 	//reim.setFeeType(feerow.get(3).trim());
							 	//reim.setcName(feerow.get(4).trim());
							 	//reim.setReimName(feerow.get(5).trim());
							 	if(StringUtils.isNotBlank(feerow.get(6)))reim.setPlateNum(feerow.get(6).trim());
							 	if(StringUtils.isNotBlank(feerow.get(7)))reim.setRemark(feerow.get(7).trim());
							 	if(StringUtils.isNotBlank(feerow.get(8))){
							 		reim.setFeeStatus(0);
							 		reim.setTotalMoney(Double.valueOf(feerow.get(8)));
							 	}else if(StringUtils.isNotBlank(feerow.get(9))){
							 		reim.setFeeStatus(1);
							 		reim.setTotalMoney(Double.valueOf(feerow.get(9)));
							 	}
							 	reim.setOperNote(Util.getOperInfo(LU.getLRealName(request, redis), "核销导入"));
							 	reim.setIsCheck(3);
								reim.setAddTime(new Date());
								reim.setReqsrc(reqsrc);
							 	reimDao.save(reim);
							}
						}
						U.setPut(map, 1, "导入成功");
					}
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}*/
	
	
}
