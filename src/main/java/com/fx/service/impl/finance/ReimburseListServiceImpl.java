package com.fx.service.impl.finance;

import java.io.File;
import java.text.SimpleDateFormat;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.other.excel.POIUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.permi.Dept;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.order.CarOrder;
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
	/** 科目-服务 */
	@Autowired
	private FeeCourseDao fcDao;
	
	/** 单位客户-服务 */
	@Autowired
	private CompanyCustomDao ccusDao;
	
	/** 单位员工-服务 */
	@Autowired
	private StaffDao staffDao;
	
	@Autowired
    private RedisUtil redis;
	@Override
	public ZBaseDaoImpl<ReimburseList, Long> getDao() {
		return reimDao;
	}
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
					if(reim.getFeeCourseId().getCourseType()==0) {
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
					}
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
					obj.setReimUserId(ccus.getBaseUserId());
					if(staff!=null) obj.setDeptId(staff.getDeptId());//是员工就有部门
					obj.setGainTime(_gainTime);
					obj.setFeeCourseId(fc);
					obj.setVoucherNum(UT.creatReimVoucher(ccus.getBaseUserId().getUname(),Integer.parseInt(sortNum.toString())));
					obj.setFeeStatus(fcStatus);
					obj.setTotalMoney(_totalMoney);
					obj.setRemark(remark);
					obj.setIsCheck(0);
					obj.setAddTime(new Date());
					obj.setReqsrc(reqsrc);
					if(StringUtils.isNotBlank(voucherUrl))obj.setReimVoucherUrl(voucherUrl);
					
					
					if(StringUtils.isEmpty(updId)){
						obj.setOperNote(ccus.getBaseUserId().getRealName()+"[添加]");
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
	
	@Override
	public Page<ReimburseList> findOtherjzList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-其他记账-分页列表");
		
		Page<ReimburseList> pd = new Page<ReimburseList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.WX == reqsrc){// 移动端
				Customer cus = LU.getLUSER(request,redis);
				//WxId wx = LU.getLWx(request);
				//String unitNum = wx.getunitNum();
				
				/*if(CusRole.TEAM_DRIVER == wx.getLgRole()){// 驾驶员
					////////////////////--默认排序--//////////////////////////
					// 付款时间-倒序
					comps.add(new Compositor("payTime", CompositorType.DESC));
					///////////////////--条件--begin//////////////////////////
					// [单位]
					filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
					
					// [报销账号]
					List<Filtration> flor = new ArrayList<Filtration>();
					flor.add(new Filtration(MatchType.LIKE, cus.getcName(), "cName"));
					flor.add(new Filtration(MatchType.LIKE, cus.getPhone(), "cName"));
					filts.add(new Filtration(MatchType.OR, flor, ""));
					
					// [费用类别-其他]
					filts.add(new Filtration(MatchType.EQ, 2, "reimType"));
					
					// 查询-指定[维修日期]时间段
					if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
						List<Filtration> fland = new ArrayList<Filtration>();
						fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "useDayStart"));
						fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "useDayStart"));
						filts.add(new Filtration(MatchType.AND, fland, ""));
					}
					///////////////////--条件--end////////////////////////////
				}else{
					U.log(log, "请求[用户角色]不存在");
					filts.add(new Filtration(MatchType.ISNULL, null, "id"));
				}*/
			}else{// 查询id为空的数据（实际是没有这样的数据，因此会返回空集合）
				U.log(log, "数据[请求来源]不存在");
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = reimDao.findPageByOrders(pd);						// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
	
	@Override
	public Map<String, Object> findOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String id) {
		String logtxt = U.log(log, "获取-其他记账-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc){
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(id)){
						fg = U.setPutFalse(map, "[其他记账修改id]不能为空");
					}else{
						id = id.trim();
						if(!FV.isLong(id)){
							obj = reimDao.find(Long.parseLong(id));
							if(obj == null){
								fg = U.setPutFalse(map, "该[其他记账]不存在");
							}
						}
						
						U.log(log, "[其他记账修改id] id="+id);
					}
				}
				
				if(fg){
					String lname = LU.getLUName(request, redis);
					/*String unitNum = LU.getLWx(request).getunitNum();
					if(!obj.getcName().equals(lname)){
						fg = U.setPutFalse(map, "该[其他记账]不是您添加的");
					}else if(!obj.getunitNum().equals(unitNum)){
						fg = U.setPutFalse(map, "该[其他记账]不属于当前单位");
					}*/
				}
				
				if(fg){
					map.put("data", obj);
					
					U.setPut(map, 1, "获取成功");
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
	public Map<String, Object> addUpdOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String uid, 
		String lname, String plateNum, String jzDate, String jzType, String jzStatus, 
		String jzMoney, String jzRemark, String updPicId, String isCn) {
		String logtxt = U.log(log, "单位驾驶员-添加/修改-其他记账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(lname)){
						fg = U.setPutFalse(map, "[登录账号]不能为空");
					}else{
						lname = lname.trim();
						
						U.log(log, "登录账号：lname="+lname);
					}
				}
				
				String unitNum = null;
				/*if(fg){
					WxId wx = LU.getLWx(request);
					if(wx != null){
						unitNum = wx.getunitNum();
					}else{
						unitNum = WebUtil.getunitNum(request);
					}
					if(StringUtils.isEmpty(unitNum)){
						fg = U.setPutFalse(map, "您未登录，不能继续操作");
					}
				}*/
				
				/*DriverList dl = null;
				if(fg){
					dl = dlSer.findDriverOfCarTeam(lname, unitNum);
					if(dl == null){
						fg = U.setPutFalse(map, "当前驾驶员不存在");
					}else if(StringUtils.isEmpty(dl.getReimburseTipOper())) {
						fg = U.setPutFalse(map, 3, "请先设置报销通知操作员");
					}
				}*/
				
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(uid)){
						U.log(log, "[其他记账id]为空，则为添加");
					}else{
						uid = uid.trim();
						if(!FV.isLong(uid)){
							fg = U.setPutFalse(map, "[其他记账id]格式错误");
						}else{
							obj = reimDao.findByField("id", Long.parseLong(uid));
							if(obj == null){
								fg = U.setPutFalse(map, "该[其他记账]不存在");
							}
						}
						
						U.log(log, "其他记账id：uid="+uid);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(isCn)){
						U.log(log, "[是否是出纳]为空，默认为0");
					}else{
						isCn = isCn.trim();
						if(!FV.isInteger(isCn)){
							fg = U.setPutFalse(map, "[是否是出纳]格式错误，应为整数");
						}
						
						U.log(log, "是否是出纳：isCn="+isCn);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(plateNum)){
						//fg = U.setPutFalse(map, "[车牌号]不能为空");
						
						U.log(log, "[车牌号]为空");// 2020.01.13改
					}else{
						plateNum = plateNum.trim();
						
						U.log(log, "车牌号：plateNum="+plateNum);
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
				
				if(fg){
					if(StringUtils.isEmpty(jzType)){
						fg = U.setPutFalse(map, "[记账类型]不能为空");
					}else{
						jzType = jzType.trim();
						
						U.log(log, "[记账类型] jzType="+jzType);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(jzStatus)){
						fg = U.setPutFalse(map, "[收支状态]不能为空");
					}else{
						jzStatus = jzStatus.trim();
						if(!FV.isInteger(jzStatus)){
							fg = U.setPutFalse(map, "[收支状态]格式错误，应为整数");
						}
						
						U.log(log, "[收支状态] jzStatus="+jzStatus);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(jzMoney)){
						fg = U.setPutFalse(map, "[记账金额]不能为空");
					}else{
						jzMoney = jzMoney.trim();
						if(!FV.isDouble(jzMoney)){
							fg = U.setPutFalse(map, "[记账金额]格式错误，应为正数");
						}
						
						U.log(log, "记账金额：jzMoney="+jzMoney);
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
						
						U.log(log, "记账备注：jzRemark="+jzRemark);
					}
				}
				
//				if(fg){
//					if(!multReq.getFileNames().hasNext()){// 不存在文件
//						fg = U.setPutFalse(map, "[至少需要上传一张维修清单图片]"); 
//					}
//				}
				
				if(fg){
					if(StringUtils.isEmpty(updPicId)){
						U.log(log, "[修改后图片id]为空");
					}else{
						updPicId = updPicId.trim();
						
						U.log(log, "[修改后图片id] updPicId="+updPicId);
					}
				}
				
//				String voucherInfo = null;
//				if(fg){
//					voucherInfo = fmSer.upFiles(multReq, response, "其他记账凭证");
//					if(voucherInfo == null || voucherInfo.contains("-")){
//						
//					}else{
//						fg = U.setPutFalse(map, "操作异常：" + voucherInfo);
//					}
//				}
				
				if(fg){
					/*map = this.addUpdReimburse(reqsrc, request, response, multReq, uid, unitNum, 
						jzType, jzStatus, lname, plateNum, jzDate, null, 
						null, null, jzMoney, jzRemark, null, null, isCn,null,"0","","","","","","","","0","");*/
					
					U.log(log, "添加/修改-其他记账完成");
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
	public Map<String, Object> delOtherjz(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String lname, String did) {
		String logtxt = U.log(log, "师傅删除-其他记账/后台删除-财务记账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc || ReqSrc.PC_COMPANY == reqsrc){
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(did)){
						fg = U.setPutFalse(map, "[其他记账id]不能为空");
					}else{
						did = did.trim();
						if(!FV.isLong(did)){
							fg = U.setPutFalse(map, "[其他记账id]格式错误");
						}else{
							obj = reimDao.findByField("id", Long.parseLong(did));
							if(obj == null){
								fg = U.setPutFalse(map, "该[其他记账]不存在");
							}
						}
						
						U.log(log, "其他记账id：did="+did);
					}
				}
				
				if(ReqSrc.WX == reqsrc && fg){//微信端才判断
					if(StringUtils.isEmpty(lname)){
						fg = U.setPutFalse(map, "[登录用户]不能为空");
					}else{
						lname = lname.trim();
						
						U.log(log, "登录用户：lname="+lname);
					}
					if(fg){
						if(!obj.getReimUserId().getUname().contains(lname)){
							fg = U.setPutFalse(map, "删除失败，该[其他记账]不是您添加的");
						}else if(!obj.getUnitNum().equals(unitNum)){
							fg = U.setPutFalse(map, "删除失败，该[其他记账]不属于当前单位");
						}else if(obj.getIsCheck() != -1 && obj.getIsCheck() != 0){
							fg = U.setPutFalse(map, "删除失败，该[其他记账]已审核");
						}
					}
				}
				
				if(fg){
					//this.operReim(did, null);
					
					U.setPut(map, 1, "删除成功");
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
	public Page<ReimburseList> findAlljzList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime, 
		String dtype) {
		String logtxt = U.log(log, "获取-全部记账-分页列表");
		
		Page<ReimburseList> pd = new Page<ReimburseList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.WX == reqsrc){// 移动端
				/*Customers cus = LU.getLUser(request);
				WxId wx = LU.getLWx(request);
				String unitNum = wx.getunitNum();
				
				if(CusRole.TEAM_DRIVER == wx.getLgRole()){// 驾驶员
					////////////////////--默认排序--//////////////////////////
					// 付款时间-倒序
					comps.add(new Compositor("payTime", CompositorType.DESC));
					///////////////////--条件--begin//////////////////////////
					// [单位]
					filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
					
					// [报销账号]
					List<Filtration> flor = new ArrayList<Filtration>();
					flor.add(new Filtration(MatchType.LIKE, cus.getcName(), "cName"));
					flor.add(new Filtration(MatchType.LIKE, cus.getPhone(), "cName"));
					filts.add(new Filtration(MatchType.OR, flor, ""));
					
					// [数据类型]
					if(StringUtils.isNotEmpty(dtype)){
						if("0".equals(dtype)){// 未报账[0 未审核 1已审核 2已复核]
							filts.add(new Filtration(MatchType.NE, 3, "isCheck"));
						}else{// 已报账[3已核销]
							filts.add(new Filtration(MatchType.EQ, 3, "isCheck"));
						}
					}
					
					// 查询-指定[维修日期]时间段
					if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
						List<Filtration> fland = new ArrayList<Filtration>();
						fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "useDayStart"));
						fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "useDayStart"));
						filts.add(new Filtration(MatchType.AND, fland, ""));
					}
					///////////////////--条件--end////////////////////////////
				}else{
					U.log(log, "请求[用户角色]不存在");
					filts.add(new Filtration(MatchType.ISNULL, null, "id"));
				}*/
			}else{// 查询id为空的数据（实际是没有这样的数据，因此会返回空集合）
				U.log(log, "数据[请求来源]不存在");
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = reimDao.findPageByOrders(pd);						// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
	@Override
	public Map<String, Object> checkReimburse(ReqSrc reqsrc,
			HttpServletRequest request, String ids, String note,
			String alNoticeChoice) {
		String logtxt = U.log(log, "财务记账凭证-审核", reqsrc);
		
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
					ReimburseList reim = null;
					String [] notice=alNoticeChoice.split("/");
					for (int i = 0; i < id.length; i++) {
						reim = reimDao.findByField("id", Long.valueOf(id[i]));
						reim.setIsCheck(1);
						if (StringUtils.isNotEmpty(reim.getOperNote())) {
							reim.setOperNote(reim.getOperNote()+","+note+Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "审核"));
						} else {
							reim.setOperNote(note+Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "审核"));
						}
						reimDao.update(reim);
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
					double currMoney=0;
					String operMark="";
					ReimburseList reim=null;
					for (int i = 0; i < id.length; i++) {
						operMark=UT.creatOperMark();
						reim = reimDao.find(Long.valueOf(id[i]));
						if(ids.contains(",")){//多条核销
							currMoney=reim.getTotalMoney();
						}else{
							currMoney=Double.valueOf(money);
						}
						reim.setOperNote(reim.getOperNote()+","+note+Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "核销"));
						if(MathUtils.add(reim.getVerificationMoney(), currMoney, 2)>=reim.getTotalMoney()){//核销完金额
							reim.setIsCheck(2);
						}
						reim.setVerificationMoney(MathUtils.add(reim.getVerificationMoney(), currMoney, 2));//已核销金额
						reim.setOperMark(operMark);
						reim.setMyBankInfo(myBankInfo);
						reim.setTransferInfo(transInfo);
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
	
	@Override
	public Map<String, Object> checkRefuse(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response,
			String rId, String reason) {
		String logtxt = U.log(log, "后台财务-拒绝报销凭证", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				ReimburseList obj = null;
				if(fg){
					if(StringUtils.isEmpty(rId)){
						fg = U.setPutFalse(map, "[记录id]不能为空");
					}else{
						rId = rId.trim();
						if(FV.isLong(rId)){
							obj = reimDao.find(Long.parseLong(rId));
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
	}
	@Override
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
	}
	@Override
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
								/*if(feerow.get(2).trim().equals("正常报销")){
									reim.setReimType(0);
								}else if(feerow.get(2).trim().equals("内部账报销")){
									reim.setReimType(1);
								}else{
									reim.setReimType(2);
								}*/
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
	}
	@Override
	public Map<String, Object> updOtherJz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		MultipartHttpServletRequest multReq, String unitNum, String luname, String uid, String jzDate, String jzType, 
		String jzStatus, String jzMoney, String jzRemark, String imgIds) {
		String logtxt = U.log(log, "修改-其他记账记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(reqsrc != ReqSrc.WX) {
					//fg = U.setPutFalse(map, "该请求只支持["+reqsrc.getReqSrcText()+"]");
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(unitNum)){
					fg = U.setPutFalse(map, "您未登录单位，不能继续操作");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(luname)){
					fg = U.setPutFalse(map, "[登录账号]不能为空");
				}else{
					luname = luname.trim();
					
					U.log(log, "登录账号：luname="+luname);
				}
			}
			
		/*	DriverList dl = null;
			if(fg){
				dl = dlSer.findDriverOfCarTeam(luname, unitNum);
				if(dl == null){
					fg = U.setPutFalse(map, "当前驾驶员不存在");
				}else if(StringUtils.isEmpty(dl.getReimburseTipOper())) {
					fg = U.setPutFalse(map, 3, "请先设置报销通知操作员");
				}
				
				U.log(log, "[登录用户] driver="+dl.getDriverName());
			}*/
			
			if(fg){
				if(StringUtils.isEmpty(uid)){
					fg = U.setPutFalse(map, "[记账记录id]不能为空");
				}else{
					uid = uid.trim();
					if(!FV.isLong(uid)){
						fg = U.setPutFalse(map, "[记账记录id]格式错误");
					}
					
					U.log(log, "[记账记录id] uid="+uid);
				}
			}
			
			ReimburseList rl = null;
			String reimTypeTxt = "";
			if(fg) {
				rl = reimDao.findByField("id", Long.parseLong(uid));
				if(rl == null) {
					fg = U.setPutFalse(map, "该[记账记录]不存在");
				}else if(rl.getIsCheck() != 0) {
					fg = U.setPutFalse(map, "该记账记录不是[未审核]，不能修改");
				}else {
					// 获取报销类型
					//reimTypeTxt = com.ebam.mis.utils.kcb.others.Util.getReimburseTxt(rl.getReimType());
					U.log(log, "报销类型文本："+reimTypeTxt);
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
					}else{
						_jzDate = DateUtils.strToDate(jzDate);
					}
					
					U.log(log, "记账日期：jzDate="+jzDate);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jzType)){
					fg = U.setPutFalse(map, "[记账类型]不能为空");
				}else{
					jzType = jzType.trim();
					
					U.log(log, "[记账类型] jzType="+jzType);
				}
			}
			
			int _jzStatus = -1;
			if(fg){
				if(StringUtils.isEmpty(jzStatus)){
					fg = U.setPutFalse(map, "[收支状态]不能为空");
				}else{
					jzStatus = jzStatus.trim();
					if(!FV.isInteger(jzStatus)){
						fg = U.setPutFalse(map, "[收支状态]格式错误，应为整数");
					}else {
						_jzStatus = Integer.parseInt(jzStatus);
					}
					
					U.log(log, "[收支状态] jzStatus="+jzStatus);
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
					
					U.log(log, "记账金额：jzMoney="+jzMoney);
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
					
					U.log(log, "记账备注：jzRemark="+jzRemark);
				}
			}
			
			List<Object> delImgIds = new ArrayList<Object>();
			String imgUrlStr = "";
			if(fg) {
				if(StringUtils.isEmpty(imgIds)) {
					U.log(log, "[图片id数组字符串]为空，则说明用户将之前的图片都删除了");
					
					// 修改前的全部id数组
					/*if(StringUtils.isNotBlank(rl.getReimVoucherId())) {
						String[] allIds = rl.getReimVoucherId().split(",");
						for (String id1 : allIds) {
							delImgIds.add(Long.parseLong(id1));
						}
						U.log(log, "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(), ","));
					}*/
				}else {
					imgIds = imgIds.trim();
					
					// 不会删除的id数组
					String[] noDelIds = imgIds.split(",");
					
					// 修改前的全部id数组
					/*if(StringUtils.isNotBlank(rl.getReimVoucherId())) {
						String[] allIds = rl.getReimVoucherId().split(",");
						for (String id1 : allIds) {
							boolean is = false;
							for (String id2 : noDelIds) {
								if(StringUtils.isNotBlank(id2) && id1.equals(id2)) {
									is = true;
									break;// 跳出内层循环
								}
							}
							
							if(!is) {// 不存在，则添加
								delImgIds.add(Long.parseLong(id1));
							}
						}
						U.log(log, "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(), ","));
					}*/
					
					List<String> imgUrls = new ArrayList<String>();
					// 修改前的全部url数组
					if(StringUtils.isNotBlank(rl.getReimVoucherUrl())) {
						String[] allUrls = rl.getReimVoucherUrl().split(",");
						for (String id1 : allUrls) {
							boolean is = false;
							for (String id2 : noDelIds) {
								if(id1.contains("_"+id2+".")) {
									is = true;
									break;// 跳出内层循环
								}
							}
							
							if(is) {// 存在，则添加
								imgUrls.add(id1);
							}
						}
						imgUrlStr = StringUtils.join(imgUrls.toArray(), ",");
						
						U.log(log, "需保留的图片地址url数组字符串 imgUrls="+imgUrlStr);
					}
				}
				
			}
			
			String voucherInfo = null;
			if(fg){
				if(multReq.getFileNames().hasNext()){// 存在文件
					U.log(log, "[记账凭证图片]不为空，即用户修改/添加了图片");
					
					//voucherInfo = fmSer.upFiles(multReq, response, reimTypeTxt);
					if(voucherInfo == null || voucherInfo.contains("-")){
						U.log(log, "修改图片成功，结果："+voucherInfo);
					}else {
						fg = U.setPutFalse(map, "[记账记录]修改记账凭证失败");
					}
				}else {
					U.log(log, "[记账凭证图片]为空，即用户未修改图片");
				}
				
				if(fg) {
					// 存在需要删除的图片id数组字符串
					if(delImgIds.size() > 0) {
						//fmSer.delFileList(delImgIds.toArray(), Util.MFILEURL);
					}
				}
			}
			
			if(fg) {
				// 最新的图片id数组字符串, 最新的图片url数组字符串
				String newIds = "", newUrls = "";
				if (voucherInfo != null) {
					newIds = StringUtils.isEmpty(imgIds) ? voucherInfo.split("-")[0] : imgIds+","+voucherInfo.split("-")[0];
					newUrls = StringUtils.isEmpty(imgUrlStr) ? voucherInfo.split("-")[1] : imgUrlStr+","+voucherInfo.split("-")[1];
				}else {
					newIds = imgIds;
					newUrls = imgUrlStr;
				}
				rl.setReimVoucherUrl(newUrls);
				
				rl.setIsCheck(0);// 变成未审核
				//rl.setFeeType(jzType);
				rl.setFeeStatus(_jzStatus);
				rl.setTotalMoney(_jzMoney);
				rl.setRemark(jzRemark);
				// 记录修改操作人
				rl.setOperNote(Util.getOperInfo(LU.getLBuser().getRealName(), "修改"));
				reimDao.update(rl);
				
				U.setPut(map, 1, "修改["+reimTypeTxt+"]成功");
				
				/*-------通知驾驶员设置的职务人员--begin------------*/
				if(rl != null && rl.getIsCheck() == 0) {
					U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
					
				/*	if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
						String[] opers = dl.getReimburseTipOper().split(",");
						
						for (String o : opers) {
							ymSer.orderWaitforCheckOfWxmsg(null, rl, o);
						}
					}*/
				}else {
					U.log(log, "该报销记账不是：未审核，则不通知");
				}
				/*-------通知驾驶员设置的职务人员--end------------*/
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updReimburse(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		MultipartHttpServletRequest multReq, String unitNum, String luname, String uid, String jzDate, String jzType, 
		String jzStatus, String jzMoney, String jzRemark, String imgIds) {
		String logtxt = U.log(log, "修改-记账记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(reqsrc != ReqSrc.WX) {
					//fg = U.setPutFalse(map, "该请求只支持["+reqsrc.getReqSrcText()+"]");
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(unitNum)){
					fg = U.setPutFalse(map, "您未登录单位，不能继续操作");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(luname)){
					fg = U.setPutFalse(map, "[登录账号]不能为空");
				}else{
					luname = luname.trim();
					
					U.log(log, "登录账号：luname="+luname);
				}
			}
			
			/*DriverList dl = null;
			if(fg){
				dl = dlSer.findDriverOfCarTeam(luname, unitNum);
				if(dl == null){
					fg = U.setPutFalse(map, "当前驾驶员不存在");
				}else if(StringUtils.isEmpty(dl.getReimburseTipOper())) {
					fg = U.setPutFalse(map, 3, "请先设置报销通知操作员");
				}
				
				U.log(log, "[登录用户] driver="+dl.getDriverName());
			}*/
			
			if(fg){
				if(StringUtils.isEmpty(uid)){
					fg = U.setPutFalse(map, "[记账记录id]不能为空");
				}else{
					uid = uid.trim();
					if(!FV.isLong(uid)){
						fg = U.setPutFalse(map, "[记账记录id]格式错误");
					}
					
					U.log(log, "[记账记录id] uid="+uid);
				}
			}
			
			ReimburseList rl = null;
			String reimTypeTxt = "";
			if(fg) {
				rl = reimDao.findByField("id", Long.parseLong(uid));
				if(rl == null) {
					fg = U.setPutFalse(map, "该[记账记录]不存在");
				}else if(rl.getIsCheck() != 0) {
					fg = U.setPutFalse(map, "该记账记录不是[未审核]，不能修改");
				}else {
					// 获取报销类型
					//reimTypeTxt = com.ebam.mis.utils.kcb.others.Util.getReimburseTxt(rl.getReimType());
					U.log(log, "报销类型文本："+reimTypeTxt);
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
					}else{
						_jzDate = DateUtils.strToDate(jzDate);
					}
					
					U.log(log, "记账日期：jzDate="+jzDate);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(jzType)){
					fg = U.setPutFalse(map, "[记账类型]不能为空");
				}else{
					jzType = jzType.trim();
					
					U.log(log, "[记账类型] jzType="+jzType);
				}
			}
			
			int _jzStatus = -1;
			if(fg){
				if(StringUtils.isEmpty(jzStatus)){
					fg = U.setPutFalse(map, "[收支状态]不能为空");
				}else{
					jzStatus = jzStatus.trim();
					if(!FV.isInteger(jzStatus)){
						fg = U.setPutFalse(map, "[收支状态]格式错误，应为整数");
					}else {
						_jzStatus = Integer.parseInt(jzStatus);
					}
					
					U.log(log, "[收支状态] jzStatus="+jzStatus);
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
					
					U.log(log, "记账金额：jzMoney="+jzMoney);
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
					
					U.log(log, "记账备注：jzRemark="+jzRemark);
				}
			}
			
			List<Object> delImgIds = new ArrayList<Object>();
			String imgUrlStr = "";
			if(fg) {
				if(StringUtils.isEmpty(imgIds)) {
					U.log(log, "[图片id数组字符串]为空，则说明用户将之前的图片都删除了");
					
					// 修改前的全部id数组
					/*if(StringUtils.isNotBlank(rl.getReimVoucherId())) {
						String[] allIds = rl.getReimVoucherId().split(",");
						for (String id1 : allIds) {
							delImgIds.add(Long.parseLong(id1));
						}
						U.log(log, "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(), ","));
					}*/
				}else {
					imgIds = imgIds.trim();
					
					// 不会删除的id数组
					String[] noDelIds = imgIds.split(",");
					
					// 修改前的全部id数组
					/*if(StringUtils.isNotBlank(rl.getReimVoucherId())) {
						String[] allIds = rl.getReimVoucherId().split(",");
						for (String id1 : allIds) {
							boolean is = false;
							for (String id2 : noDelIds) {
								if(StringUtils.isNotBlank(id2) && id1.equals(id2)) {
									is = true;
									break;// 跳出内层循环
								}
							}
							
							if(!is) {// 不存在，则添加
								delImgIds.add(Long.parseLong(id1));
							}
						}
						U.log(log, "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(), ","));
					}*/
					
					List<String> imgUrls = new ArrayList<String>();
					// 修改前的全部url数组
					if(StringUtils.isNotBlank(rl.getReimVoucherUrl())) {
						String[] allUrls = rl.getReimVoucherUrl().split(",");
						for (String id1 : allUrls) {
							boolean is = false;
							for (String id2 : noDelIds) {
								if(id1.contains("_"+id2+".")) {
									is = true;
									break;// 跳出内层循环
								}
							}
							
							if(is) {// 存在，则添加
								imgUrls.add(id1);
							}
						}
						imgUrlStr = StringUtils.join(imgUrls.toArray(), ",");
						
						U.log(log, "需保留的图片地址url数组字符串 imgUrls="+imgUrlStr);
					}
				}
				
			}
			
			String voucherInfo = null;
			if(fg){
				if(multReq.getFileNames().hasNext()){// 存在文件
					U.log(log, "[记账凭证图片]不为空，即用户修改/添加了图片");
					
					//voucherInfo = fmSer.upFiles(multReq, response, reimTypeTxt);
					if(voucherInfo == null || voucherInfo.contains("-")){
						U.log(log, "修改图片成功，结果："+voucherInfo);
					}else {
						fg = U.setPutFalse(map, "[记账记录]修改记账凭证失败");
					}
				}else {
					U.log(log, "[记账凭证图片]为空，即用户未修改图片");
				}
				
				if(fg) {
					// 存在需要删除的图片id数组字符串
					if(delImgIds.size() > 0) {
						//fmSer.delFileList(delImgIds.toArray(), Util.MFILEURL);
					}
				}
			}
			
			if(fg) {
				// 最新的图片id数组字符串, 最新的图片url数组字符串
				String newIds = "", newUrls = "";
				if (voucherInfo != null) {
					newIds = StringUtils.isEmpty(imgIds) ? voucherInfo.split("-")[0] : imgIds+","+voucherInfo.split("-")[0];
					newUrls = StringUtils.isEmpty(imgUrlStr) ? voucherInfo.split("-")[1] : imgUrlStr+","+voucherInfo.split("-")[1];
				}else {
					newIds = imgIds;
					newUrls = imgUrlStr;
				}
				//rl.setReimVoucherId(newIds);
				rl.setReimVoucherUrl(newUrls);
				
				rl.setIsCheck(0);// 变成未审核
				//rl.setFeeType(jzType);
				rl.setFeeStatus(_jzStatus);
				rl.setTotalMoney(_jzMoney);
				rl.setRemark(jzRemark);
				// 记录修改操作人
				rl.setOperNote(Util.getOperInfo(LU.getLBuser().getRealName(), "修改"));
				rl.setAddTime(new Date());
				reimDao.update(rl);
				
				// 修改对应的加油、维修、其他、行程收支报账记录
				/*if(rl.getReimType() == 2) {// 其他报账
					
				}else if(rl.getReimType() == 3) {// 加油报账
					
				}else if(rl.getReimType() == 4) {// 维修报账
					
				}else if(rl.getReimType() == 5) {// 团上开支
					
				}*/
				
				U.setPut(map, 1, "修改["+reimTypeTxt+"]成功");
				
				/*-------通知驾驶员设置的职务人员--begin------------*/
				if(rl != null && rl.getIsCheck() == 0) {
					U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
					
					/*if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
						String[] opers = dl.getReimburseTipOper().split(",");
						
						for (String o : opers) {
							ymSer.orderWaitforCheckOfWxmsg(null, rl, o);
						}
					}*/
				}else {
					U.log(log, "该报销记账不是：未审核，则不通知");
				}
				/*-------通知驾驶员设置的职务人员--end------------*/
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findReimburseDetail(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String luname, String uid) {
		String logtxt = U.log(log, "获取-记账记录-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(luname)) {
					fg = U.setPutFalse(map, "[登录用户名]不能为空");
				}else {
					luname = luname.trim();
					
					U.log(log, "[登录用户名] luname="+luname);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(uid)) {
					fg = U.setPutFalse(map, "[记账对象id]不能为空");
				}else {
					uid = uid.trim();
					if(!FV.isLong(uid)) {
						fg = U.setPutFalse(map, "[记账id]格式错误");
					}
					
					U.log(log, "[记账id] uid="+uid);
				}
			}
			
			ReimburseList rl = null;
			if(fg) {
				rl = reimDao.findByField("id", Long.parseLong(uid));
				if(rl == null) {
					fg = U.setPutFalse(map, "[记账记录]不存在");
				}else if(!rl.getReimUserId().getUname().contains(luname)) {
					fg = U.setPutFalse(map, "该[记账记录]不是您添加的");
				}
			}
			
			if(fg) {
				map.put("data", rl);
				
				U.setPut(map, 1, "获取[记账记录]成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findSetReimburseTipOper(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String luname) {
		String logtxt = U.log(log, "获取-记账报销通知操作员-数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(ReqSrc.WX != reqsrc) {
					fg = U.setPutFalse(map, QC.ERRORS_MSG);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(unitNum)) {
					fg = U.setPutFalse(map, "[登录单位编号]不能为空");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(luname)){
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else{
					luname = luname.trim();
					
					U.log(log, "[登录用户] luname="+luname);
				}
			}
			
			/*DriverList dl = null;
			if(fg){
				dl = dlSer.findDriverOfCarTeam(luname, unitNum);
				if(dl == null) {
					fg = U.setPutFalse(map, "该驾驶员不存在");
				}else {
					map.put("data", dl.getReimburseTipOper());
					
					U.setPut(map, 1, "获取[记账报销通知职务]成功");
				}
				
				U.log(log, "[驾驶员姓名] driver="+dl.getDriverName());
			}*/
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updReimburseTipOper(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String luname, String selOper) {
		String logtxt = U.log(log, "设置-记账报销通知职务", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(ReqSrc.WX != reqsrc) {
					fg = U.setPutFalse(map, QC.ERRORS_MSG);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(unitNum)) {
					fg = U.setPutFalse(map, "[登录单位编号]不能为空");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(luname)){
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else{
					luname = luname.trim();
					
					U.log(log, "[登录用户] luname="+luname);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(selOper)){
					fg = U.setPutFalse(map, "[选择的操作员]不能为空");
				}else{
					selOper = selOper.trim();
					
					U.log(log, "[选择的操作员] selOper="+selOper);
				}
			}
			
			/*DriverList dl = null;
			if(fg){
				dl = dlSer.findDriverOfCarTeam(luname, unitNum);
				if(dl == null) {
					fg = U.setPutFalse(map, "该驾驶员不存在");
				}else {
					dl.setReimburseTipOper(selOper);
					dlSer.update(dl);
					U.setPut(map, 1, "设置成功");
				}
				
				U.log(log, "[驾驶员姓名] driver="+dl.getDriverName());
			}*/
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delReimburse(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String unitNum, String lname, String did) {
		String logtxt = U.log(log, "删除-记账报销记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(ReqSrc.WX != reqsrc) {
					fg = U.setPutFalse(map, QC.ERRORS_MSG);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(unitNum)) {
					fg = U.setPutFalse(map, "[登录单位编号]不能为空");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(lname)){
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else{
					lname = lname.trim();
					
					U.log(log, "登录用户：lname="+lname);
				}
			}
			
			ReimburseList obj = null;
			if(fg){
				if(StringUtils.isEmpty(did)){
					fg = U.setPutFalse(map, "[记账报销记录id]不能为空");
				}else{
					did = did.trim();
					if(!FV.isLong(did)){
						fg = U.setPutFalse(map, "[记账报销记录id]格式错误");
					}else{
						obj = reimDao.findByField("id", Long.parseLong(did));
						if(obj == null){
							fg = U.setPutFalse(map, "该[记账报销记录]不存在");
						}else if(!obj.getReimUserId().getUname().equals(lname)) {
							fg = U.setPutFalse(map, "该[记账报销记录]不是您添加的，不能删除");
						}if(!obj.getUnitNum().equals(unitNum)){
							fg = U.setPutFalse(map, "删除失败，该[其他记账]不属于当前单位");
						}else if(obj.getIsCheck() != -1 && obj.getIsCheck() != 0){
							fg = U.setPutFalse(map, "删除失败，该[其他记账]已审核");
						}
					}
					
					U.log(log, "[记账报销记录id] did="+did);
				}
			}
			
			if(fg){
				reimDao.delete(obj);
				
				U.setPut(map, 1, "删除成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
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
				fmap.put(U.getAtJsonFilter(Dept.class), new String[]{});
				fmap.put(U.getAtJsonFilter(FeeCourse.class), new String[]{});
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
	public Map<String, Object> findReimRemarks(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request) {
		String logtxt = U.log(log, "查询-凭证摘要列表-用于下拉框", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String hql="from ReimburseList where unitNum = ?0 group by remark order by id asc";
			List<ReimburseList> reims=reimDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			if(reims.size()>0){
				List<String> remarks=new ArrayList<String>();
				for (ReimburseList reim:reims) {
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
}
