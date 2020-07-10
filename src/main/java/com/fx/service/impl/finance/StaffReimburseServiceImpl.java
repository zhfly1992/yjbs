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
import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.back.FileManDao;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.dao.order.RouteTradeListDao;
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
import com.fx.service.finance.StaffReimburseService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class StaffReimburseServiceImpl extends BaseServiceImpl<StaffReimburse,Long> implements StaffReimburseService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
    private RedisUtil redis;
	
	@Autowired
	private StaffReimburseDao srDao;
	@Override
	public ZBaseDaoImpl<StaffReimburse, Long> getDao() {
		return srDao;
	}

	
	/** 单位客户-服务 */
	@Autowired
	private CompanyCustomDao ccusDao;
	
	/** 单位员工-服务 */
	@Autowired
	private StaffDao staffDao;
	
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao fcDao;
	
	/** 凭证记录-服务 */
	@Autowired
	private ReimburseListDao reimDao;
	
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	/** 文件管理-服务 */
	@Autowired
	private FileManDao fileManDao;
	/** 行程收支-服务 */
	@Autowired
	private RouteTradeListDao routeTradeListDao;
	
	
	@Override
	public Map<String, Object> findStaffReimburse(HttpServletRequest request,ReqSrc reqsrc, String page, String rows, String uname,String plateNum,
			String deptId,String remark,String money,String isCheck,String voucherNo,String operMark,String sTime,String eTime) {
		String logtxt = U.log(log, "获取-员工报账-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "凭证");
			/*****参数--验证--end******/
			if(fg){
				Page<StaffReimburse> pd =srDao.findStaffReimburse(reqsrc, page, rows, LU.getLUnitNum(request, redis), 
						uname,plateNum, deptId, remark, money, isCheck, voucherNo,operMark, sTime, eTime);
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
				for (StaffReimburse sr :pd.getResult()) {
					if(sr.getCarOrderReim()!=null) {
						Hibernate.initialize(sr.getCarOrderReim().getRouteMps());
						Hibernate.initialize(sr.getCarOrderReim().getTrades());
					}
					if(sr.getMainOrderReim()!=null)Hibernate.initialize(sr.getMainOrderReim().getMainCars());
					if(sr.getOrderTrade()!=null) {
						singleFee+=sr.getOrderTrade().getSingleFee();
						washingFee+=sr.getOrderTrade().getWashingFee();
						parkingFee+=sr.getOrderTrade().getParkingFee();
						roadFee+=sr.getOrderTrade().getRoadFee();
						livingFee+=sr.getOrderTrade().getLivingFee();
						otherFee+=sr.getOrderTrade().getOtherFee();
						waterFee+=sr.getOrderTrade().getWaterFee();
						stayFee+=sr.getOrderTrade().getStayFee();
					}
					totalGath+=sr.getGathMoney();
					totalPay+=sr.getPayMoney();
				}
				U.setPageData(map, pd);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
				fmap.put(U.getAtJsonFilter(CarOrder.class), new String[]{});
				fmap.put(U.getAtJsonFilter(MainCarOrder.class), new String[]{});
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
	public Map<String, Object> addStaffReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response, String staffReimInfo) {
		String logtxt = U.log(log,"单位-添加员工报账记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				
				CompanyCustom ccus=null;
				Staff staff=null;
				/*if(fg){
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
				}*/
				Map<Integer, BaseUser> fcmap = new HashMap<Integer, BaseUser>();//客户
				Map<Integer, Dept> bmmap = new HashMap<Integer, Dept>();//部门
				String [] staffInfos=null;
				if(fg) {
					if(StringUtils.isEmpty(staffReimInfo)){
						fg = U.setPutFalse(map, "[报账信息]不能为空");
					}else {
						staffInfos=staffReimInfo.split("@");
						String [] infos=null;
						for (int i = 0; i < staffInfos.length; i++) {
							infos=staffInfos[i].split("=");
							if(infos.length!=6) {
								fg = U.setPutFalse(map, "第"+(i+1)+"个[员工帐科目信息]格式错误");
								break;
							}else {
								ccus=ccusDao.findByField("baseUserId.uname", infos[0]);
								if(ccus==null) {
									fg = U.setPutFalse(map, "第"+(i+1)+"个报销人不存在");
									break;
								}
								fcmap.put(i, ccus.getBaseUserId());
								staff=staffDao.findByField("baseUserId.uname", infos[0]);
								if(staff!=null) {
									bmmap.put(i, staff.getDeptId());
								}
							}
						}
					}
				}
				if(fg){
					String operMark=DateUtils.getOrderNum(7);
					String [] staffInfo=null;
					for (int i = 0; i < staffInfos.length; i++) {
						staffInfo=staffInfos[i].split("=");
						StaffReimburse obj = new StaffReimburse();
						obj.setUnitNum(LU.getLUnitNum(request, redis));
						obj.setReimUser(fcmap.get(i));
						if(bmmap.get(i)!=null) obj.setDeptId(bmmap.get(i));//是员工就有部门
						obj.setGathMoney(Double.valueOf(staffInfo[2]));
						obj.setPayMoney(Double.valueOf(staffInfo[3]));
						obj.setRemark(staffInfo[4]);
						obj.setIsCheck(0);
						obj.setAddTime(new Date());
						obj.setReqsrc(reqsrc);
						if(StringUtils.isNotBlank(staffInfo[5]))obj.setReimVoucherUrl(staffInfo[5]);
						obj.setOperNote(LU.getLRealName(request, redis)+"[添加]");
						obj.setOperMark(operMark);
						srDao.save(obj);
					}
					U.setPut(map, 1, "添加成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> modifyStaffReimburse(ReqSrc reqsrc, HttpServletRequest request, 
			HttpServletResponse response,String updId, String uname,String plateNum, String gathMoney,
			String payMoney, String remark,String voucherUrl) {
		String logtxt = U.log(log, "单位-修改报销凭证记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				StaffReimburse obj=null;
				if(fg){
					if(StringUtils.isEmpty(updId)){
						fg = U.setPutFalse(map, "[修改记录id]不能为空");
					}else{
						updId = updId.trim();
						if(!FV.isLong(updId)){
							fg = U.setPutFalse(map, "[报销记录修改id]格式不正确");
						}else{
							obj = srDao.find(Long.parseLong(updId));
							if(obj == null){
								fg = U.setPutFalse(map, "该[修改记录]不存在");
							}else if(obj.getIsCheck()==2) {
								fg = U.setPutFalse(map, "该记录已生成凭证，不能修改，请另外添加");
							}
						}
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
				
				double _gathMoney = 0d;
				double _payMoney = 0d;
				if(fg){
					if(StringUtils.isBlank(gathMoney) && StringUtils.isBlank(payMoney)){
						fg = U.setPutFalse(map, "[报账金额]不能为空");
					}else{
						if(!"0".equals(gathMoney)) {
							gathMoney = gathMoney.trim();
							if(!FV.isDouble(gathMoney)){
								fg = U.setPutFalse(map, "[收入金额]格式不正确，应为数字");
							}else{
								_gathMoney = Double.parseDouble(gathMoney);
							}
						}else {
							payMoney = payMoney.trim();
							if(!FV.isDouble(payMoney)){
								fg = U.setPutFalse(map, "[支出金额]格式不正确，应为数字");
							}else{
								_payMoney = Double.parseDouble(payMoney);
							}
						}
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(remark)){
						fg = U.setPutFalse(map, "[摘要]不能为空");
					}else{
						remark = remark.trim();
						
						U.log(log, "[报销摘要] remark="+remark);
					}
				}
				///////////////参数验证--end///////////////////
				if(fg){
					String operMark=DateUtils.getOrderNum(7);
					obj.setReimUser(ccus.getBaseUserId());
					if(staff!=null) obj.setDeptId(staff.getDeptId());//是员工就有部门
					if(StringUtils.isNotBlank(plateNum)) obj.setPlateNum(plateNum);
					obj.setGathMoney(_gathMoney);
					obj.setPayMoney(_payMoney);
					obj.setRemark(remark);
					obj.setIsCheck(0);
					obj.setAddTime(new Date());
					obj.setReqsrc(reqsrc);
					if(StringUtils.isNotBlank(voucherUrl))obj.setReimVoucherUrl(voucherUrl);
					obj.setOperNote(obj.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "修改"));
					obj.setOperMark(obj.getOperMark()+","+operMark);
					srDao.update(obj);
					U.setPut(map, 1, "修改成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delStaffReim(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String delId) {
		String logtxt = U.log(log, "单位-删除员工报账记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				StaffReimburse obj=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						obj=srDao.findByField("id", Long.valueOf(delId));
						if(obj==null) {
							fg = U.setPutFalse(map, "记录不存在");
						}else {
							if(obj.getIsCheck()!=0) {
								fg = U.setPutFalse(map, "该记录状态不能删除");
							}
						}
					}
				}
				if(fg){
					srDao.delete(obj);
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
	public Map<String, Object> checkStaffReimburse(ReqSrc reqsrc,
			HttpServletRequest request, String ids, String note,
			String alNoticeChoice) {
		String logtxt = U.log(log, "单位审核-员工报账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(ids)){
						fg = U.setPutFalse(map, "[审核id]不能为空");
					}else{
						ids = ids.trim();
						U.log(log, "审核id="+ids);
					}
				}
				if(fg){
					if (StringUtils.isEmpty(note))note = "";
					String[] id = ids.split(",");
					StaffReimburse sr = null;
					String [] notice=alNoticeChoice.split("/");
					for (int i = 0; i < id.length; i++) {
						sr = srDao.findByField("id", Long.valueOf(id[i]));
						sr.setIsCheck(1);
						if (StringUtils.isNotEmpty(sr.getOperNote())) {
							sr.setOperNote(sr.getOperNote()+","+note+Util.getOperInfo(LU.getLRealName(request, redis), "审核"));
						} else {
							sr.setOperNote(note+Util.getOperInfo(LU.getLRealName(request, redis), "审核"));
						}
						srDao.update(sr);
						if(notice.length>0){
							//发送微信通知复核人
							/*for (String eachno:notice) {
								ymSer.orderWaitforCheckOfWxmsg(null, reim, eachno.split(",")[0]);
							}*/
						}
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
	
	@Override
	public Map<String, Object> checkRefuse(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response,
			String rId, String reason) {
		String logtxt = U.log(log, "后台财务-拒绝报销凭证", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				StaffReimburse obj = null;
				if(fg){
					if(StringUtils.isEmpty(rId)){
						fg = U.setPutFalse(map, "[记录id]不能为空");
					}else{
						rId = rId.trim();
						if(FV.isLong(rId)){
							obj = srDao.find(Long.parseLong(rId));
							if(obj == null){
								fg = U.setPutFalse(map, "该记录不存在");
							}
						}
						
						U.log(log, "[拒绝记录id] id="+rId);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(reason)){
						U.log(log, "[拒绝原因为空]");
					}else{
						reason = reason.trim();
					}
				}
				if(fg){
					if(obj.getIsCheck()!=0){
						fg = U.setPutFalse(map, "该记录已审核，不能拒绝");
					}
				}
				if(fg){
					obj.setIsCheck(-1);
					if(StringUtils.isNotBlank(reason))obj.setRefuseReason(reason);
					srDao.update(obj);
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
	public Map<String, Object> findStaffReimById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "查询-员工报账信息-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[查询id]不能为空");
				}
			}

			if (fg) {
				StaffReimburse reim = srDao.findByField("id", Long.valueOf(id));
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
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
	public Map<String, Object> findStaffReimRemarks(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request) {
		String logtxt = U.log(log, "查询-报销摘要列表-用于下拉框", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String hql="from StaffReimburse where unitNum = ?0 group by remark order by id asc";
			List<StaffReimburse> reims=srDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			if(reims.size()>0){
				List<String> remarks=new ArrayList<String>();
				for (StaffReimburse reim:reims) {
					remarks.add(reim.getRemark());
				}
				map.put("data", remarks);
			}
			U.setPut(map, 1, "查询成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	@Override
	public Map<String, Object> createReim(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
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
					for (int i = 0; i < faceInfos.length; i++) {
						if(faceInfos[i].split("=").length!=4) {
							fg = U.setPutFalse(map, "第"+(i+1)+"个[对方科目信息]格式错误");
							break;
						}
					}
				}
			}
			List<StaffReimburse> srlist=new ArrayList<StaffReimburse>();
			Map<Long, Object> fcmap = new HashMap<Long, Object>();
			if(fg){
				if(StringUtils.isEmpty(createInfo)){
					fg = U.setPutFalse(map, "[报账记录]不能为空");
				}else {
					String [] infos=createInfo.split("@");
					for (int i = 0; i < infos.length; i++) {
						if(infos[i].split("=").length!=2) {
							fg = U.setPutFalse(map, "第"+(i+1)+"个[员工报账科目信息]格式错误");
							break;
						}
					}
					String [] ids=null;
					StaffReimburse sr=null;
					FeeCourse fc=null;
					for (int i = 0; i < infos.length; i++) {
						ids=infos[i].split("=");
						sr=srDao.findByField("id", Long.valueOf(ids[0]));
						if(sr.getIsCheck()!=1) {
							fg = U.setPutFalse(map, "有报账记录状态非【已审核】状态，本次生成凭证失败");
							break;
						}
						srlist.add(sr);
						fc=fcDao.findByField("id", Long.valueOf(ids[1]));
						if(fc.getIsLastCourse()==0) {
							fg = U.setPutFalse(map, "科目【"+fc.getCourseName()+"】非末级科目，不能生成凭证");
							break;
						}
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
					fc=(FeeCourse)fcmap.get(eachsr.getId());
					if(fc.getCourseType()==0) {//收入
						tradeMoney+=MathUtils.add(tradeMoney, MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2);
					}else {//支出
						tradeMoney+=MathUtils.sub(tradeMoney, MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2);
					}
					FeeCourseTrade fct=new FeeCourseTrade();
					fct.setUnitNum(eachsr.getUnitNum());
					fct.setFeeCourseId(fc);
					fct.setRemark(eachsr.getRemark());
					fct.setGathMoney(eachsr.getGathMoney());
					fct.setPayMoney(eachsr.getPayMoney());
					fct.setAddTime(new Date());
					fct.setStaffReimId(eachsr);
					fctlist.add(fct);
					//更新对应科目余额
					fc.setBalance(MathUtils.add(fc.getBalance(), MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2));
					fcDao.update(fc);
					if(fc.getParentCourseId()!=null) {//更新父级科目余额
						FeeCourse parentOne=fc.getParentCourseId();
						fc.getParentCourseId().setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2));
						fcDao.update(parentOne);
						if(parentOne.getParentCourseId()!=null) {
							parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(), MathUtils.sub(eachsr.getGathMoney(), eachsr.getPayMoney(), 2), 2));
							fcDao.update(parentOne.getParentCourseId());
						}
					}
					//更新员工报账状态
					eachsr.setFeeCourseId(fc);
					eachsr.setIsCheck(2);
					srDao.update(eachsr);
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
					if(fc.getParentCourseId()!=null) {//更新父级科目余额
						FeeCourse parentOne=fc.getParentCourseId();
						parentOne.setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
						fcDao.update(parentOne);
						if(parentOne.getParentCourseId()!=null) {
							parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
							fcDao.update(parentOne.getParentCourseId());
						}
					}
				}
				//生成凭证
				ReimburseList obj=new ReimburseList();
				obj.setUnitNum(LU.getLUnitNum(request, redis));
				String hql="select count(id) from ReimburseList where unitNum=?0 and addTime>=?1 and addTime<=?2";
				Object sortNum=reimDao.findObj(hql, LU.getLUnitNum(request, redis),DateUtils.getStartTimeOfDay(),DateUtils.getEndTimeOfDay());
				obj.setCourseTrades(fctlist);
				obj.setGainTime(_gainTime);
				obj.setVoucherNum(UT.creatReimVoucher(LU.getLUName(request, redis),Integer.parseInt(sortNum.toString())));
				obj.setTotalMoney(tradeMoney);
				obj.setIsCheck(0);
				obj.setAddTime(new Date());
				obj.setReqsrc(reqsrc);
				obj.setOperNote(LU.getLRealName(request, redis)+"[添加]");
				reimDao.save(obj);
				U.setPut(map, 1, "凭证生成成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	
	@Override
	public Map<String, Object> addQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String flen, String plateNum, String jzDate, String jzFeeCourseId, 
		String jzMoney, String jzRemark) {
		String logtxt = U.log(log, "添加-其他记账", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
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
				driver = staffDao.getTeamDriver(lunitNum, luname);
				if(driver == null) {
					fg = U.setPutFalse(map, "当前用户不是驾驶员，不能操作");
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
							fg = U.setPutFalse(map, "请上传费用清单凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
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
			
			Date _jzDate = null;
			if(fg){
				if(StringUtils.isEmpty(jzDate)){
					fg = U.setPutFalse(map, "[记账日期]不能为空");
				}else{
					jzDate = jzDate.trim();
					if(!FV.isDate(jzDate)){
						fg = U.setPutFalse(map, "[记账日期]格式不正确");
					}else {
						_jzDate = DateUtils.strToDate(jzDate);
					}
					
					U.log(log, "记账日期：jzDate="+jzDate);
				}
			}
			
			FeeCourse jzFeeCourse = null;
			if(fg){
				if(StringUtils.isEmpty(jzFeeCourseId)){
					fg = U.setPutFalse(map, "[记账类型]不能为空");
				}else{
					jzFeeCourseId = jzFeeCourseId.trim();
					if(!FV.isLong(jzFeeCourseId)) {
						fg = U.setPutFalse(map, "[记账类型]格式错误");
					}else {
						jzFeeCourse = fcDao.findByField("id", Long.parseLong(jzFeeCourseId));
						if(jzFeeCourse == null) {
							fg = U.setPutFalse(map, "该记账类型不存在");
						}
					}
					
					U.log(log, "[记账类型] jzFeeCourseId="+jzFeeCourseId);
				}
			}
			
			double _jzMoney = 0d;
			if(fg){
				if(StringUtils.isEmpty(jzMoney)){
					fg = U.setPutFalse(map, "[记账金额]不能为空");
				}else{
					jzMoney = jzMoney.trim();
					if(!FV.isDouble(jzMoney)){
						fg = U.setPutFalse(map, "[记账金额]格式错误，应为正数");
					}else {
						_jzMoney = Double.parseDouble(jzMoney);
					}
					
					U.log(log, "[记账金额] jzMoney="+jzMoney);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jzRemark)){
					U.log(log, "[记账备注]为空");
				}else{
					jzRemark = jzRemark.trim();
					if(jzRemark.length() > 100){
						fg = U.setPutFalse(map, "[记账备注]最多填写100个字");
					}
					
					U.log(log, "[记账备注] jzRemark="+jzRemark);
				}
			}
			
			if(fg) {
				StaffReimburse sr = new StaffReimburse();
				sr.setUnitNum(lunitNum);
				sr.setReimUser(lbuser);
				sr.setDeptId(driver.getDeptId());
//				sr.setJzDate(_jzDate);
				sr.setFeeCourseId(jzFeeCourse);
				sr.setRemark(jzRemark);
				sr.setPayMoney(_jzMoney);
				sr.setIsCheck(0);
				sr.setJzType(JzType.QTJZ);
				sr.setReqsrc(reqsrc);
				sr.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
				sr.setAddTime(new Date());
//				sr.setDat(plateNum);
				srDao.save(sr);
				U.log(log, "添加-员工报账记录-完成");
				
				// 前端需要此id，保存图片
				map.put("uid", sr.getId());
				
				U.setPut(map, 1, "添加-其他记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> updQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String uid, String flen, String jzDate, String jzFeeCourseId, 
		String jzMoney, String jzRemark) {
		String logtxt = U.log(log, "修改-其他记账", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
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
				driver = staffDao.getTeamDriver(lunitNum, luname);
				if(driver == null) {
					fg = U.setPutFalse(map, "当前用户不是驾驶员，不能操作");
				}
			}
			
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(uid)) {
					fg = U.setPutFalse(map, "[其他记账id]不能为空");
				}else {
					uid = uid.trim();
					if(!FV.isLong(uid)) {
						fg = U.setPutFalse(map, "[其他记账id]格式错误");
					}else {
						sr = srDao.findByField("id", Long.parseLong(uid));
					}
					
					U.log(log, "[其他记账id] uid="+uid);
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
							fg = U.setPutFalse(map, "请上传费用清单凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jzDate)){
					fg = U.setPutFalse(map, "[记账日期]不能为空");
				}else{
					jzDate = jzDate.trim();
					if(!FV.isDate(jzDate)){
						fg = U.setPutFalse(map, "[记账日期]格式不正确");
					}
					
					U.log(log, "记账日期：jzDate="+jzDate);
				}
			}
			
			FeeCourse jzFeeCourse = null;
			if(fg){
				if(StringUtils.isEmpty(jzFeeCourseId)){
					fg = U.setPutFalse(map, "[记账类型]不能为空");
				}else{
					jzFeeCourseId = jzFeeCourseId.trim();
					if(!FV.isLong(jzFeeCourseId)) {
						fg = U.setPutFalse(map, "[记账类型]格式错误");
					}else {
						jzFeeCourse = fcDao.findByField("id", Long.parseLong(jzFeeCourseId));
						if(jzFeeCourse == null) {
							fg = U.setPutFalse(map, "该记账类型不存在");
						}
					}
					
					U.log(log, "[记账类型] jzFeeCourseId="+jzFeeCourseId);
				}
			}
			
			double _jzMoney = 0d;
			if(fg){
				if(StringUtils.isEmpty(jzMoney)){
					fg = U.setPutFalse(map, "[记账金额]不能为空");
				}else{
					jzMoney = jzMoney.trim();
					if(!FV.isDouble(jzMoney)){
						fg = U.setPutFalse(map, "[记账金额]格式错误，应为正数");
					}else {
						_jzMoney = Double.parseDouble(jzMoney);
					}
					
					U.log(log, "[记账金额] jzMoney="+jzMoney);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jzRemark)){
					U.log(log, "[记账备注]为空");
				}else{
					jzRemark = jzRemark.trim();
					if(jzRemark.length() > 100){
						fg = U.setPutFalse(map, "[记账备注]最多填写100个字");
					}
					
					U.log(log, "[记账备注] jzRemark="+jzRemark);
				}
			}
			
			if(fg) {
				sr.setRemark(jzRemark);
				sr.setFeeCourseId(jzFeeCourse);
				sr.setPayMoney(_jzMoney);
				sr.setIsCheck(0);
				sr.setOperNote(sr.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
				sr.setAddTime(new Date());
				srDao.update(sr);
				U.log(log, "修改-员工报账记录-完成");
				
				// 前端需要此id，保存图片
				map.put("uid", sr.getId());
				
				U.setPut(map, 1, "修改-其他记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> delQtjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String did) {
		String logtxt = U.log(log, "删除-其他记账", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(did)) {
					fg = U.setPutFalse(map, "[其他记账id]不能为空");
				}else {
					did = did.trim();
					if(!FV.isLong(did)) {
						fg = U.setPutFalse(map, "[其他记账id]格式错误");
					}else {
						sr = srDao.findByField("id", Long.parseLong(did));
					}
					
					U.log(log, "[其他记账id] did="+did);
				}
			}
			
			if(fg) {
				if(sr == null) {
					fg = U.setPutFalse(map, "该[其他记账]不存在");
				}else if(!sr.getReimUser().getUname().equals(luname)) {
					fg = U.setPutFalse(map, "该[其他记账]不是你添加的");
				}if (sr.getIsCheck() != -1 && sr.getIsCheck() != 0) {
					fg = U.setPutFalse(map, "删除失败，该[其他记账]已审核");
				}
			}

			if(fg) {
				srDao.delete(sr);
				U.log(log, "删除-其他记账-记录完成");
				
				// 删除其他记账对应凭证记录及图片
				fileManDao.delJzbxFile(sr.getUnitNum(), luname, FileType.QTJZ_IMG, sr.getId()+"");
				
				U.setPut(map, 1, "删除-其他记账记录-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> findQtjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-其他记账-列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			fg = U.valPageNo(map, page, rows, "其他记账");
			fg = U.valSEtime(map, stime, etime, "其他时间");
			
			if(fg) {
				String unitNum = LU.getLUnitNum(request, redis);
				String luname = LU.getLUName(request, redis);
				Page<StaffReimburse> pd = srDao.findPageQtjzList(reqsrc, page, rows, stime, etime, unitNum, luname);
				U.setPageData(map, pd);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
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
	public Map<String, Object> findQtjzDetail(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String id) {
		String logtxt = U.log(log, "获取-其他记账-详情", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[其他记账id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[其他记账id]格式错误");
					}else {
						sr = srDao.findByField("id", Long.parseLong(id));
					}
					
					U.log(log, "[其他记账id] id="+id);
				}
			}
			
			if(fg) {
				if(sr == null) {
					fg = U.setPutFalse(map, "该[其他记账]不存在");
				}else if(!sr.getReimUser().getUname().equals(luname)) {
					fg = U.setPutFalse(map, "该[其他记账]不是你添加的");
				}
			}
			
			if(fg) {
				map.put("data", sr);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
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
	public Map<String, Object> findXcjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String orderNum, String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-行程记账-列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			String lunitNum = LU.getLUnitNum(request, redis);
			String luname = LU.getLUName(request, redis);
			
			if(StringUtils.isBlank(orderNum)) {
				U.log(log, "查询用户所有行程记账列表");
				
				fg = U.valPageNo(map, page, rows, "行程记账");
				fg = U.valSEtime(map, stime, etime, "行程时间");
				
				if(fg) {
					Page<StaffReimburse> pd = srDao.findPageXcjzList(reqsrc, page, rows, stime, etime, lunitNum, luname);
					
					/****处理数据--begin******/
					List<Object> result = new ArrayList<Object>();
					List<StaffReimburse> list = pd.getResult();
					for (StaffReimburse sr : list) {
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("id", sr.getId()); 											// 员工记账id
						m.put("isCheck", sr.getIsCheck());									// 审核状态
						m.put("plateNum", sr.getCarOrderReim().getDisCar().getPlateNum());	// 车牌号
						m.put("orderNum", sr.getCarOrderReim().getOrderNum());				// 子订单编号
						m.put("mainOrderNum", sr.getMainOrderReim().getOrderNum());			// 主订单编号
						m.put("routeIncome", sr.getGathMoney());							// 团上现收
						m.put("routePay", sr.getPayMoney());								// 总开支
						m.put("atime", sr.getAddTime());									// 添加日期
						result.add(m);
					}
					/****处理数据--end********/
					U.setPageData(map, result);
					
					U.setPut(map, 1, "获取成功");
				}
			}else {
				U.log(log, "查询用户指定子订单行程记账列表");
				
				hql = "from StaffReimburse where unitNum = ?0 and reimUserId.uname = ?1 and carOrderReim.orderNum = ?2 order by id desc";
				List<StaffReimburse> list = srDao.findhqlList(hql, lunitNum, luname, orderNum);
				List<Object> result = new ArrayList<Object>();
				for (StaffReimburse sr : list) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("id", sr.getId()); 											// 员工记账id
					m.put("isCheck", sr.getIsCheck());									// 审核状态
					m.put("plateNum", sr.getCarOrderReim().getDisCar().getPlateNum());	// 车牌号
					m.put("orderNum", sr.getCarOrderReim().getOrderNum());				// 子订单编号
					m.put("mainOrderNum", sr.getMainOrderReim().getOrderNum());			// 主订单编号
					m.put("routeIncome", sr.getGathMoney());							// 团上现收
					m.put("routePay", sr.getPayMoney());								// 总开支
					m.put("atime", sr.getAddTime());									// 添加日期
					result.add(m);
				}
				map.put("data", result);
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> delXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String did) {
		String logtxt = U.log(log, "删除-员工记账（包含行程收支）", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(did)) {
					fg = U.setPutFalse(map, "[行程收支对应员工记账id]不能为空");
				}else {
					did = did.trim();
					if(!FV.isLong(did)) {
						fg = U.setPutFalse(map, "[行程收支对应员工记账id]格式错误");
					}else {
						sr = srDao.findByField("id", Long.parseLong(did));
					}
					
					U.log(log, "[行程收支对应员工记账id] did="+did);
				}
			}
			
			RouteTradeList rtl = null;
			if(fg) {
				if(sr == null) {
					fg = U.setPutFalse(map, "该[员工记账]不存在");
				}else if(!sr.getReimUser().getUname().equals(luname)) {
					fg = U.setPutFalse(map, "该[员工记账]不是你添加的");
				}if (sr.getIsCheck() != -1 && sr.getIsCheck() != 0) {
					fg = U.setPutFalse(map, "删除失败，该[员工记账]已审核");
				}else {
					rtl = sr.getOrderTrade();
					if(rtl == null) {
						U.log(log, "不存在对应的行程收支记录");
					}
				}
			}

			if(fg) {
				srDao.delete(sr);
				U.log(log, "删除-其他记账-记录完成");
				
				if(rtl != null) {
					routeTradeListDao.delete(rtl);
					U.log(log, "删除行程收支-完成");
				}
				
				// 删除行程记账对应凭证记录及图片
				fileManDao.delJzbxFile(sr.getUnitNum(), luname, FileType.XCSZ_IMG, sr.getId()+"");
				
				U.setPut(map, 1, "删除-行程记账记录-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
}
