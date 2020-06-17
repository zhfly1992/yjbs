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

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
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
import com.fx.dao.company.StaffDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Dept;
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
	
	@Autowired
	private ReimburseListDao reimDao;
	@Override
	public ZBaseDaoImpl<ReimburseList, Long> getDao() {
		return reimDao;
	}
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao fcDao;
	
	/** 单位客户-服务 */
	@Autowired
	private CompanyCustomDao ccusDao;
	
	/** 单位员工-服务 */
	@Autowired
	private StaffDao staffDao;
	/** 缓存-服务 */
	/** 子订单-服务 */
	@Autowired
	private CarOrderDao coDao;
	
	@Autowired
    private RedisUtil redis;
	
	
	
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
					/*if(reim.getCourseTrades().getCourseType()==0) {
						totalGath+=reim.getTotalMoney();
					}else {
						totalPay+=reim.getTotalMoney();
					}
					if(reim.getCarOrderReim()!=null) {
						if(reim.getCarOrderReim().getTrades()!=null) {
							for (RouteTradeList rtl:reim.getCarOrderReim().getTrades()){
								singleFee+=rtl.getSingleFee();washingFee+=rtl.getWashingFee();
								parkingFee+=rtl.getParkingFee();roadFee+=rtl.getRoadFee();
								livingFee+=rtl.getLivingFee();otherFee+=rtl.getOtherFee();
								waterFee+=rtl.getWaterFee();stayFee+=rtl.getStayFee();
							}
						}
					}*/
				}
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(ReimburseList.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
				fmap.put(U.getAtJsonFilter(FeeCourse.class), new String[]{});
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
	public Map<String, Object> adupReimburse(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String updId, 
		String unitNum,String uname, String feeCourseId, String totalMoney, String remark,String gainTime,String voucherUrl) {
		String logtxt = U.log(log, (StringUtils.isNotBlank(updId))?"修改":"添加"+"-报销凭证记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(unitNum)){
						fg = U.setPutFalse(map, "[单位编号]不能为空");
					}else{
						unitNum = unitNum.trim();
						
						U.log(log, "[单位编号] unitNum="+unitNum);
					}
				}
				
				CompanyCustom ccus=null;
				Staff staff=null;
				if(fg){
					if(StringUtils.isEmpty(uname)){
						fg = U.setPutFalse(map, "[报销人]不能为空");
					}else{
						uname = uname.trim();
						ccus=ccusDao.findByField("baseUserId.uname", uname);
						if(ccus==null) {
							fg = U.setPutFalse(map, "[报销人]不存在");
						}else {
							staff=staffDao.findByField("baseUserId.uname", uname);
						}
					}
				}
				
				FeeCourse fc=null;
				int fcStatus = 0;
				if(fg){
					if(StringUtils.isEmpty(feeCourseId)){
						fg = U.setPutFalse(map, "[科目]不能为空");
					}else{
						feeCourseId = feeCourseId.trim();
						fc=fcDao.findByField("id", Long.valueOf(feeCourseId));
						if(fc==null) {
							fg = U.setPutFalse(map, "该[科目]不存在");
						}else {
							fcStatus=fc.getCourseType();
							U.log(log, "[科目] feeCourseId="+feeCourseId);
						}
					}
				}
				
				double _totalMoney = 0d;
				if(fg){
					if(StringUtils.isEmpty(totalMoney)){
						fg = U.setPutFalse(map, "[金额]不能为空");
					}else{
						totalMoney = totalMoney.trim();
						if(!FV.isDouble(totalMoney)){
							fg = U.setPutFalse(map, "[金额]格式不正确，应为正数");
						}else{
							_totalMoney = Double.parseDouble(totalMoney);
						}
						
						U.log(log, "[金额] totalMoney="+totalMoney);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(remark)){
						U.log(log, "[报销摘要]为空");
					}else{
						remark = remark.trim();
						
						U.log(log, "[报销摘要] remark="+remark);
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
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(updId)){
						obj=new ReimburseList();
						obj.setUnitNum(unitNum);
						U.log(log, "[报销记录修改id]为空");
					}else{
						updId = updId.trim();
						if(!FV.isLong(updId)){
							fg = U.setPutFalse(map, "[报销记录修改id]格式不正确");
						}else{
							obj = reimDao.find(Long.parseLong(updId));
							if(reimDao == null){
								fg = U.setPutFalse(map, "该[报销记录]不存在");
							}
						}
						
						U.log(log, "[报销记录修改id] updId="+updId);
					}
				}
				///////////////参数验证--end///////////////////
				if(fg){
					String hql="select count(id) from ReimburseList where unitNum=?0 and addTime>=?1 and addTime<=?2";
					Object sortNum=reimDao.findObj(hql, LU.getLUnitNum(request, redis),DateUtils.getStartTimeOfDay(),DateUtils.getEndTimeOfDay());
					/*obj.setReimUserId(ccus.getBaseUserId());
					if(staff!=null) obj.setDeptId(staff.getDeptId());//是员工就有部门
					obj.setGainTime(_gainTime);
					obj.setFeeCourseId(fc);
					obj.setVoucherNum(UT.creatReimVoucher(ccus.getBaseUserId().getUname(),Integer.parseInt(sortNum.toString())));
					obj.setFeeStatus(fcStatus);
					obj.setTotalMoney(_totalMoney);
					obj.setRemark(remark);
					obj.setIsCheck(0);
					obj.setReqsrc(reqsrc);*/
					
					
					//obj.setStaffReims(srlist);
					//obj.setCourseTrades(fctlist);
					obj.setGainTime(_gainTime);
					obj.setVoucherNum(UT.creatReimVoucher(LU.getLUName(request, redis),Integer.parseInt(sortNum.toString())));
					obj.setTotalMoney(_totalMoney);
					obj.setIsCheck(0);
					
					if(StringUtils.isEmpty(updId)){
						obj.setOperNote(ccus.getBaseUserId().getRealName()+"[添加]");
						obj.setAddTime(new Date());
						reimDao.save(obj);
						U.setPut(map, 1, "添加成功");
					}else{
						obj.setOperNote(obj.getOperNote()+Util.getOperInfo(ccus.getBaseUserId().getRealName(), "修改"));
						reimDao.update(obj);
						U.setPut(map, 1, "修改成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	public Map<String, Object> modifyReim(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String createInfo, String faceCourseInfo,String gainTime) {
		String logtxt = U.log(log, "单位-员工报账列表生成凭证", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
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
				if(StringUtils.isEmpty(faceCourseInfo)){
					fg = U.setPutFalse(map, "[对方科目]不能为空");
				}else {
					faceInfos=faceCourseInfo.split("@");
				}
			}
			List<StaffReimburse> srlist=new ArrayList<StaffReimburse>();
			Map<Long, Object> fcmap = new HashMap<Long, Object>();
			if(fg){
				if(StringUtils.isEmpty(createInfo)){
					fg = U.setPutFalse(map, "[报账记录]不能为空");
				}else {
					String [] infos=createInfo.split("@");
					String [] ids=null;
					StaffReimburse sr=null;
					FeeCourse fc=null;
					for (int i = 0; i < infos.length; i++) {
						ids=infos[i].split("=");
						/*sr=srDao.findByField("id", Long.valueOf(ids[0]));
						if(sr.getIsCheck()==2) {
							fg = U.setPutFalse(map, "有报账记录已生成凭证，本次生成凭证失败");
							break;
						}*/
						srlist.add(sr);
						fc=fcDao.findByField("id", Long.valueOf(ids[1]));
						fcmap.put(sr.getId(), fc);
					}
				}
			}
			if(fg) {
				List<FeeCourseTrade> fctlist=new ArrayList<FeeCourseTrade>();
				FeeCourse fc=null;
				double tradeMoney=0;//凭证交易金额
				//添加员工报账科目交易记录
				for (StaffReimburse eachsr:srlist) {
					tradeMoney=MathUtils.add(tradeMoney, MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2);
					fc=(FeeCourse)fcmap.get(eachsr.getId());
					FeeCourseTrade fct=new FeeCourseTrade();
					fct.setUnitNum(eachsr.getUnitNum());
					fct.setFeeCourseId(fc);
					fct.setRemark(eachsr.getRemark());
					fct.setGathMoney(eachsr.getGathMoney());
					fct.setPayMoney(eachsr.getPayMoney());
					fct.setAddTime(new Date());
					fctlist.add(fct);
					//更新对应科目余额
					fc.setBalance(MathUtils.add(fc.getBalance(), MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2));
					fcDao.update(fc);
				}
				//添加对方平账科目交易记录
				String [] faceIds=null;
				FeeCourse face=null;
				double faceGath=0;//对方科目借方金额
				double facePay=0;//对方科目贷方金额
				for (int i = 0; i < faceInfos.length; i++) {
					faceIds=faceInfos[i].split("=");
					face=fcDao.findByField("id", Long.valueOf(faceIds[0]));
					faceGath=Double.valueOf(faceIds[2]);
					facePay=Double.valueOf(faceIds[3]);
					FeeCourseTrade fct=new FeeCourseTrade();
					fct.setUnitNum(LU.getLUnitNum(request, redis));
					fct.setFeeCourseId(face);
					fct.setRemark(faceIds[1]);
					fct.setGathMoney(faceGath);
					fct.setPayMoney(facePay);
					fct.setAddTime(new Date());
					fctlist.add(fct);
					//更新对应科目余额
					face.setBalance(MathUtils.add(face.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
					fcDao.update(face);
				}
				//生成凭证
				ReimburseList obj=new ReimburseList();
				obj.setUnitNum(LU.getLUnitNum(request, redis));
				String hql="select count(id) from ReimburseList where unitNum=?0 and addTime>=?1 and addTime<=?2";
				Object sortNum=reimDao.findObj(hql, LU.getLUnitNum(request, redis),DateUtils.getStartTimeOfDay(),DateUtils.getEndTimeOfDay());
				obj.setStaffReims(srlist);
				obj.setCourseTrades(fctlist);
				obj.setGainTime(_gainTime);
				obj.setVoucherNum(UT.creatReimVoucher(LU.getLUName(request, redis),Integer.parseInt(sortNum.toString())));
				obj.setTotalMoney(tradeMoney);
				obj.setIsCheck(0);
				obj.setAddTime(new Date());
				obj.setReqsrc(reqsrc);
				obj.setOperNote(LU.getLUSER(request, redis).getBaseUserId().getRealName()+"[添加]");
				reimDao.save(obj);
				U.setPut(map, 1, "凭证生成成功");
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
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(ReimburseList.class), new String[]{});
				fmap.put(U.getAtJsonFilter(StaffReimburse.class), new String[]{});
				fmap.put(U.getAtJsonFilter(FeeCourseTrade.class), new String[]{});
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(CarOrder.class), new String[]{});
				fmap.put(U.getAtJsonFilter(MainCarOrder.class), new String[]{});
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
					List<StaffReimburse> srlist=null;
					for (int i = 0; i < id.length; i++) {
						operMark=UT.creatOperMark();
						reim = reimDao.find(Long.valueOf(id[i]));
						reim.setOperNote(reim.getOperNote()+","+note+Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "核销"));
						reim.setIsCheck(1);
						reim.setOperMark(operMark);
						reim.setMyBankInfo(myBankInfo);
						reim.setTransferInfo(transInfo);
						reimDao.update(reim);
						srlist=reim.getStaffReims();
						if(srlist!=null) {
							for (StaffReimburse sr:srlist) {
								if(sr.getOrderTrade()!=null) {
									if(rtlMap.get(sr.getOrderTrade().getOrderNum())==null) {
										List<RouteTradeList> rtlOrder=new ArrayList<RouteTradeList>();
										rtlOrder.add(sr.getOrderTrade());
										rtlMap.put(sr.getOrderTrade().getOrderNum(), rtlOrder);
									}else {
										rtlMap.get(sr.getOrderTrade().getOrderNum()).add(sr.getOrderTrade());
									}
								}
							}
						}
						if(rtlMap.size()>0) {
							for(Map.Entry<String, List<RouteTradeList>> entry: rtlMap.entrySet()){
								CarOrder co=coDao.findByField("orderNum", entry.getKey());
								co.setTrades(entry.getValue());
								coDao.update(co);
							}
						}
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
							 	reim.setOperNote(Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "核销导入"));
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
