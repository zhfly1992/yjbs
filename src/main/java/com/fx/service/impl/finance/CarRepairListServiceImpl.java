package com.fx.service.impl.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.CarRepairListDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CusWallet;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WalletList;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.ReimburseList;
import com.fx.service.company.FileService;
import com.fx.service.cus.CusWalletService;
import com.fx.service.cus.WalletListService;
import com.fx.service.finance.CarOilListService;
import com.fx.service.finance.CarRepairListService;
import com.fx.service.finance.ReimburseListService;
import com.fx.service.wxdat.YMessageService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CarRepairListServiceImpl extends BaseServiceImpl<CarRepairList,Long> implements CarRepairListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
    private RedisUtil redis;

	/** 车辆加油记录-服务 */
	@Autowired
	private CarOilListService coiSer;
	/** 消息通知-服务 */
	@Autowired
	private YMessageService ymSer;
	@Autowired
	private CarRepairListDao cpaiDao;
	@Autowired
	private FileService fmSer;   //文件管理
	@Autowired
	private CusWalletService cwSer;
	@Autowired
	private WalletListService wlSer;
	@Autowired
	private ReimburseListService reimSer;
	@Override
	public ZBaseDaoImpl<CarRepairList, Long> getDao() {
		return cpaiDao;
	}
	@Override
	public Page<CarRepairList> findRepairList(Page<CarRepairList> pageData,String teamNo,
			String find,String cpaiStation,String sTime,String eTime,String isCheck,String driver,String repeat) {
		//////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		if(StringUtils.isNotEmpty(repeat)){
			compositor = new Compositor("repairMoney", CompositorType.ASC);
		}
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		filtrations.add(new Filtration(MatchType.EQ, teamNo,"unitNum"));//当前车队
		if(StringUtils.isNotEmpty(find)){
			filtrations.add(new Filtration(MatchType.LIKE, find, "plateNum"));//车牌号
		}
		if(StringUtils.isNotEmpty(isCheck)){
			filtrations.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));//审核状态
		}else{//默认不显示已核销的记录
			filtrations.add(new Filtration(MatchType.NE, 3, "isCheck"));//审核状态
		}
		if(StringUtils.isNotEmpty(cpaiStation)){
			filtrations.add(new Filtration(MatchType.EQ, cpaiStation, "repairName"));//维修站
		}
		if(StringUtils.isNotEmpty(driver)){
			filtrations.add(new Filtration(MatchType.LIKE, driver, "cName"));//驾驶员
		}
		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = cpaiDao.findPage(pageData);
		return pageData;
	}
	@Override
	public Page<CarRepairList> findRepairList(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-维修记账-分页列表");
		
		Page<CarRepairList> pd = new Page<CarRepairList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.WX == reqsrc){// 移动端
				Customer cus = LU.getLUSER(request, redis);
				WxBaseUser wx = LU.getLWx(request, redis);
				String unitNum = LU.getLUnitNum(request, redis);
				
				if(CusRole.TEAM_DRIVER == wx.getLgRole()){// 驾驶员
					////////////////////--默认排序--//////////////////////////
					// 维修日期-倒序
					comps.add(new Compositor("repDate", CompositorType.DESC));
					///////////////////--条件--begin//////////////////////////
					// 指定查询[车队]
					filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
					
					// 指定查询[维修记账用户/手机号]
					List<Filtration> flor = new ArrayList<Filtration>();
					flor.add(new Filtration(MatchType.LIKE, cus.getBaseUserId().getUname(), "baseUserId.uname"));
					flor.add(new Filtration(MatchType.LIKE, cus.getBaseUserId().getPhone(), "baseUserId.phone"));
					filts.add(new Filtration(MatchType.OR, flor, ""));
					
					// 查询-指定[维修日期]时间段
					if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
						List<Filtration> fland = new ArrayList<Filtration>();
						fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "repDate"));
						fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "repDate"));
						filts.add(new Filtration(MatchType.AND, fland, ""));
					}
					///////////////////--条件--end////////////////////////////
				}else{
					U.log(log, "请求[用户角色]不存在");
					filts.add(new Filtration(MatchType.ISNULL, null, "id"));
				}
			}else{// 查询id为空的数据（实际是没有这样的数据，因此会返回空集合）
				U.log(log, "数据[请求来源]不存在");
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = cpaiDao.findPageByOrders(pd);						// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
	
	@Override
	public Map<String, Object> findRepair(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String id) {
		String logtxt = U.log(log, "获取-维修记录-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc){
				CarRepairList obj = null;
				if(fg){
					if(StringUtils.isEmpty(id)){
						fg = U.setPutFalse(map, "[维修记录修改id]不能为空");
					}else{
						id = id.trim();
						if(!FV.isLong(id)){
							obj = cpaiDao.find(Long.parseLong(id));
							if(obj == null){
								fg = U.setPutFalse(map, "该[维修记录]不存在");
							}
						}
						
						U.log(log, "[维修记录修改id] id="+id);
					}
				}
				
				if(fg){
					String lname = LU.getLUName(request, redis);
					String unitNum = LU.getLWx(request, redis).getCompanyNum();
					if(!obj.getRepairDriver().getUname().equals(lname)){
						fg = U.setPutFalse(map, "该[维修记录]不是您添加的");
					}else if(!obj.getUnitNum().equals(unitNum)){
						fg = U.setPutFalse(map, "该[维修记录]不属于当前车队");
					}
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
	public Map<String, Object> addUpdRepair(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, MultipartHttpServletRequest multReq, String uid, 
		String lname, String plateNum, String currKm, String wxPayWay, String wxStation,
		String wxMoney, String wxDate, String wxRemark, String updPicId, String isCn) {
		String logtxt = U.log(log, "车队驾驶员-添加/修改-维修油记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc){
				// 此处记录下之前保存的图片文件，当修改的图片保存成功后，删除原来的图片
				
				/*if(fg){
					if(StringUtils.isEmpty(lname)){
						fg = U.setPutFalse(map, "[登录账号]不能为空");
					}else{
						lname = lname.trim();
						
						U.log(log, "登录账号：lname="+lname);
					}
				}
				
				String unitNum = null;
				if(fg){
					WxId wx = LU.getLWx(request);
					if(wx != null){
						unitNum = wx.getUnitNum();
					}else{
						unitNum = WebUtil.getUnitNum(request);
					}
					if(StringUtils.isEmpty(unitNum)){
						fg = U.setPutFalse(map, "您未登录车队，不能继续操作");
					}
				}
				
				String driver = null;
				DriverList dl = null;
				if(fg){
					dl = dlSer.findDriverOfCarTeam(lname, unitNum);
					if(dl == null){
						fg = U.setPutFalse(map, "当前驾驶员不存在");
					}else if(StringUtils.isEmpty(dl.getReimburseTipOper())) {
						fg = U.setPutFalse(map, 3, "请先设置报销通知操作员");
					}else{
						driver = dl.getcName()+","+dl.getDriverName();
					}
				}
				
				CarRepairList obj = null;
				if(fg){
					if(StringUtils.isEmpty(uid)){
						U.log(log, "[维修记录id]为空，则为添加");
					}else{
						uid = uid.trim();
						if(!FV.isLong(uid)){
							fg = U.setPutFalse(map, "[维修记录id]格式错误");
						}else{
							obj = cpaiDao.findByField("id", Long.parseLong(uid));
							if(obj == null){
								fg = U.setPutFalse(map, "该[维修记录]不存在");
							}
						}
						
						U.log(log, "维修记录id：uid="+uid);
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
//						fg = U.setPutFalse(map, "[当前公里数]不能为空");
						U.log(log, "[当前公里数]为空");
					}else{
						currKm = currKm.trim();
						if(!FV.isDouble(currKm)){
							fg = U.setPutFalse(map, "[当前公里数]格式错误，应为正数");
						}else{
							_currKm = Double.parseDouble(currKm);
						}
						
						U.log(log, "当前公里数：currKm="+currKm);
					}
				}
				
				int _wxPayWay = -1;// 0-记账； 1-现金；2019-05-15- 14:36 改
				if(fg){
					if(StringUtils.isEmpty(wxPayWay)){
						fg = U.setPutFalse(map, "[支付方式]不能为空");
					}else{
						wxPayWay = wxPayWay.trim();
						if(!FV.isOfEnum(PayWay.class, wxPayWay)){
							fg = U.setPutFalse(map, "[支付方式]格式不正确");
						}else{
							if(PayWay.CASH_PAY.name().equals(wxPayWay)){
								_wxPayWay = 1;
							}else if(PayWay.JZ_PAY.name().equals(wxPayWay)){
								_wxPayWay = 0;
							}
						}
						
						U.log(log, "支付方式：wxPayWay="+wxPayWay);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(wxStation)){
						fg = U.setPutFalse(map, "[维修站名称]不能为空");
					}else{
						wxStation = wxStation.trim();
						
						U.log(log, "维修站名称：wxStation="+wxStation);
					}
				}
				
				double _wxMoney = 0d;
				if(fg){
					if(StringUtils.isEmpty(wxMoney)){
						fg = U.setPutFalse(map, "[维修金额]不能为空");
					}else{
						wxMoney = wxMoney.trim();
						if(!FV.isDouble(wxMoney)){
							fg = U.setPutFalse(map, "[维修金额]格式错误，应为正数");
						}else{
							_wxMoney = Double.parseDouble(wxMoney);
						}
						
						U.log(log, "维修金额：wxMoney="+wxMoney);
					}
				}
				
				Date _wxDate = null;
				if(fg){
					if(StringUtils.isEmpty(wxDate)){
						fg = U.setPutFalse(map, "[维修日期]不能为空");
					}else{
						wxDate = wxDate.trim();
						if(!FV.isDate(wxDate)){
							fg = U.setPutFalse(map, "[维修日期]格式不正确");
						}else{
							_wxDate = DateUtils.strToDate(wxDate);
						}
						
						U.log(log, "维修日期：wxDate="+wxDate);
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(wxRemark)){
						fg = U.setPutFalse(map, "[维修内容]不能为空");
					}else{
						wxRemark = wxRemark.trim();
						if(wxRemark.length() > 100){
							fg = U.setPutFalse(map, "[维修内容]最多填写100个字");
						}
						
						U.log(log, "维修内容：wxRemark="+wxRemark);
					}
				}
				
				if(fg){
					if(!multReq.getFileNames().hasNext()){// 不存在文件
						fg = U.setPutFalse(map, "[至少需要上传一张维修清单图片]"); 
					}
				}
				
				// 此次维修公里数，必须大于上一次的维修公里数
				double prevkm = 0d;
				if(fg){
					if(StringUtils.isNotBlank(currKm)){// 存在公里数才判断（注：此处实际上应该按照加油记录来判断，因为维修记录可以不填写）
						List<CarRepairList> cpais = new ArrayList<CarRepairList>();
						if(StringUtils.isNotBlank(uid)){// 修改
							hql = "from CarRepairList where plateNum = ? and unitNum = ? and id <> ? order by id desc";
							cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum, Long.parseLong(uid));
						}else{// 添加
							hql = "from CarRepairList where plateNum = ? and unitNum = ? order by id desc";
							cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, unitNum);
						}
						
						if(cpais.size() > 0){// 存在上一条-加油记账
							prevkm = cpais.get(0).getCurrentKilo();
						}else{
							U.log(log, "["+plateNum+"]不存在维修记录");
						}
						
						// 非第一次添加才验证
						if(cpais.size() > 0 && _currKm <= prevkm){
							fg = U.setPutFalse(map, "当前公里数必须大于上次公里数，上次为"+ prevkm + "公里");
						}
					}
				}
				
				if(fg){
					if(StringUtils.isEmpty(updPicId)){
						U.log(log, "[修改后图片id]为空");
					}else{
						updPicId = updPicId.trim();
						
						U.log(log, "[修改后图片id] updPicId="+updPicId);
					}
				}
				
				String voucherInfo = null;
				if(fg){
					voucherInfo = fmSer.upFiles(multReq, response, "维修凭证");
					if(voucherInfo == null || voucherInfo.contains("-")){
						
					}else{
						fg = U.setPutFalse(map, "操作异常" + voucherInfo);
					}
				}
				
				// 添加
				if(fg){
					if(StringUtils.isEmpty(uid)){
						obj = new CarRepairList();
						obj.setReqsrc(reqsrc);
						obj.setTeamNo(unitNum);
						obj.setcName(driver);
						if (voucherInfo != null) {
							obj.setRepairVoucherId(voucherInfo.split("-")[0]);
							obj.setRepairVoucherUrl(voucherInfo.split("-")[1]);
						}
						obj.setPlateNum(plateNum);
						obj.setRepairName(wxStation);
						obj.setCurrentKilo(_currKm);
						obj.setRepairPayWay(_wxPayWay);
						obj.setRepairMoney(_wxMoney);
						obj.setRemark(wxRemark);
						obj.setAddTime(new Date());
						obj.setNote(com.ebam.mis.utils.kcb.others.Util.getOperInfo(dl.getDriverName(), "添加"));
						2019-05-12 增-维修日期、维修内容
						obj.setRepDate(_wxDate);
						
						int result = this.operRepair(null, obj);
						
						if (result == 1) {
							ReimburseList reim = null;
							
							if(obj.getRepairPayWay()!=0){//非记账才报销，记账不需要报销给师傅20200118
								*//***xx添加报销记录 20190527***//*
								reim = new ReimburseList();
								reim.setTeamNo(obj.getUnitNum());
								reim.setFeeType("维修");
								reim.setFeeStatus(1);
								reim.setPlateNum(obj.getPlateNum());
								reim.setReimName(obj.getcName().split(",")[1]);//姓名
								reim.setcName(obj.getcName().split(",")[0]);//账号
								reim.setTotalMoney(obj.getRepairMoney());
								reim.setRemark(obj.getRemark());
								reim.setOperator(WebUtil.getLUser(request).getRealName());
								reim.setAddTime(new Date());
								reim.setUseDayStart(obj.getRepDate());
								reim.setOrderNum(obj.getId()+"");
								reim.setReimType(4);//维修报销
								reim.setCustomer(obj.getRepairName());
								reim.setReqsrc(reqsrc);
								reim.setReimVoucherId(obj.getRepairVoucherId());
								reim.setReimVoucherUrl(obj.getRepairVoucherUrl());
								reim.setNote(com.ebam.mis.utils.kcb.others.Util.getOperInfo(dl.getDriverName(), "添加"));
								reimSer.save(reim);
								*//***xx添加报销记录 20190527***//*
							}
							fg = U.setPutFalse(map, 1, "添加成功");
							
							-------通知驾驶员设置的职务人员--begin------------
							if(reim != null && reim.getIsCheck() == 0) {
								U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
								
								if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
									String[] opers = dl.getReimburseTipOper().split(",");
									
									for (String o : opers) {
										ymSer.orderWaitforCheckOfWxmsg(null, reim, o);
									}
								}
							}else {
								U.log(log, "该报销记账不是：未审核，则不通知");
							}
							-------通知驾驶员设置的职务人员--end------------
						}else{
							fg = U.setPutFalse(map, 1, "添加失败");
						}
					}
				}
				
				// 修改
				if(fg){
					if(StringUtils.isNotEmpty(uid)){
						if(fg){
							if(obj.getIsCheck() != 0){
								fg = U.setPutFalse(map, "该[维修记录]已审核，不能修改");
							}
						}
						
						String newPicId = null, newPicUrl = null;
						if(fg){
							if (voucherInfo != null) {
								*//****** 判断修改添加的图片 *****//*
								String[] updAfterId = voucherInfo.split("-")[0].split(",");
								for (int i = 0; i < updAfterId.length; i++) {
									if (!obj.getRepairVoucherId().contains(updAfterId[i])) {
										newPicId += "," + updAfterId[i];
									}
								}
								
								*//****** 判断修改添加的url *****//*
								String[] updAfterUrl = voucherInfo.split("-")[1].split(",");
								for (int j = 0; j < updAfterUrl.length; j++) {
									if (!obj.getRepairVoucherUrl().contains(updAfterUrl[j])) {
										newPicUrl += "," + updAfterUrl[j];
									}
								}
							}
						}
						
						if(fg){
							if (StringUtils.isNotEmpty(updPicId)) {
								List<String> delId = new ArrayList<String>();
								
								*//****** 判断删除的图片 *****//*
								String[] delAfterId = obj.getRepairVoucherId().split(",");
								for (int i = 0; i < delAfterId.length; i++) {
									if (updPicId.contains(delAfterId[i])) {// 原图片未删除
										newPicId += "," + delAfterId[i];
									} else {// 原图片删除
										delId.add(delAfterId[i]);
									}
								}
								
								*//****** 判断修改添加的url *****//*
								FileManage fm = null;
								String[] delAfterUrl = obj.getRepairVoucherUrl().split(",");
								if (delId.size() > 0) {
									for (int j = 0; j < delId.size(); j++) {
										fm = fmSer.find(Long.valueOf(delId.get(j)));
										for (int k = 0; k < delAfterUrl.length; k++) {
											if (!delAfterUrl[k].contains(fm.getfRelName())) {
												newPicUrl += "," + delAfterUrl[k];
											}
										}
										
										fmSer.delFile(Util.MFILEURL, Long.valueOf(delId.get(j)));// 删除凭证图片
									}
								} else {
									newPicUrl += "," + obj.getRepairVoucherUrl();
								}
							}
						}
						
						if(fg){
							if (newPicId != null) {
								obj.setRepairVoucherId(newPicId.substring(1, newPicId.length()));
								obj.setRepairVoucherUrl(newPicUrl.substring(1, newPicUrl.length()));
							}
							//wx端不会出现出纳修改返回撤销的问题
							
							obj.setPlateNum(plateNum);
							obj.setRepairName(wxStation);
							obj.setCurrentKilo(_currKm);
							obj.setRepairPayWay(_wxPayWay);
							obj.setRepairMoney(_wxMoney);
							obj.setAddTime(new Date());
							2019-05-12 增-维修日期、维修内容
							obj.setRepDate(_wxDate);
							
							int result = this.operRepair(uid, obj);
							if (result == 1) {
								ReimburseList reim = null;
								
								*//***xx添加报销记录 20190524***//*
								hql="from ReimburseList where reimType=4 and orderNum=?";
								reim=reimSer.findObj(hql, uid);
								if(reim!=null){
									reim.setPlateNum(obj.getPlateNum());
									reim.setReimName(obj.getcName().split(",")[1]);//姓名
									reim.setcName(obj.getcName().split(",")[0]);//账号
									reim.setTotalMoney(obj.getRepairMoney());
									reim.setRemark(obj.getRemark());
									reim.setOperator(reim.getOperator()+","+WebUtil.getLUser(request).getRealName());
									reim.setUseDayStart(obj.getRepDate());
									reim.setCustomer(obj.getRepairName());
									reim.setReimVoucherId(obj.getRepairVoucherId());
									reim.setReimVoucherUrl(obj.getRepairVoucherUrl());
									reimSer.update(reim);
								}
								*//***xx添加报销记录 20190524***//*
								fg = U.setPutFalse(map, 1, "修改成功");
								
								-------通知驾驶员设置的职务人员--begin------------
								if(reim != null && reim.getIsCheck() == 0) {
									U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
									
									if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
										String[] opers = dl.getReimburseTipOper().split(",");
										
										for (String o : opers) {
											ymSer.orderWaitforCheckOfWxmsg(null, reim, o);
										}
									}
								}else {
									U.log(log, "该报销记账不是：未审核，则不通知");
								}
								-------通知驾驶员设置的职务人员--end------------
							}else{
								fg = U.setPutFalse(map, 1, "修改失败");
							}
							
							// 此处删除之前的图片
						}
					}
				}
			}else if(ReqSrc.PC_COMPANY == reqsrc){
				if(StringUtils.isNotBlank(currKm)){// 存在公里数才判断
					double prevkm = 0d;
					List<CarRepairList> cpais = new ArrayList<CarRepairList>();
					if(StringUtils.isNotBlank(uid)){// 修改
						hql = "from CarRepairList where plateNum = ? and unitNum = ? and id <> ? order by id desc";
						cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, WebUtil.getUnitNum(multReq), Long.parseLong(uid));
					}else{// 添加
						hql = "from CarRepairList where plateNum = ? and unitNum = ? order by id desc";
						cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, WebUtil.getUnitNum(multReq));
					}
					if(cpais.size() > 0){// 存在上一条-加油记账
						prevkm = cpais.get(0).getCurrentKilo();
					}
					// 非第一次添加才验证
					if(cpais.size() > 0 && Double.valueOf(currKm) <= prevkm){
						fg = U.setPutFalse(map,  "操作错误，当前公里数必须大于上次公里数，上次为"+ prevkm+ "公里");
					}
				}
				if(fg){
					String voucherInfo = fmSer.upFiles(multReq, response, "维修凭证");
					if (voucherInfo == null || voucherInfo.contains("-")) {// 修改操作未修改图片或者添加图片
						CarRepairList cpai = null;
						if (StringUtils.isNotEmpty(plateNum)) {
							if (StringUtils.isNotEmpty(uid)) {// 修改
								cpai = cpaiDao.find(Long.parseLong(uid));
								String newPicId = "";
								String newPicUrl = "";
								if (voucherInfo != null) {
									*//****** 判断修改添加的图片 *****//*
									String[] updAfterId = voucherInfo.split("-")[0]
											.split(",");
									for (int i = 0; i < updAfterId.length; i++) {
										if (!cpai.getRepairVoucherId().contains(
												updAfterId[i])) {
											newPicId += "," + updAfterId[i];
										}
									}
									*//****** 判断修改添加的图片 *****//*
									*//****** 判断修改添加的url *****//*
									String[] updAfterUrl = voucherInfo.split("-")[1]
											.split(",");
									for (int j = 0; j < updAfterUrl.length; j++) {
										if (!cpai.getRepairVoucherUrl().contains(updAfterUrl[j])) {
											newPicUrl += "," + updAfterUrl[j];
										}
									}
									*//****** 判断修改添加的url *****//*
								}
								if (StringUtils.isNotEmpty(updPicId)) {
									List<String> delId = new ArrayList<String>();
									*//****** 判断删除的图片 *****//*
									String[] delAfterId = cpai.getRepairVoucherId()
											.split(",");
									for (int i = 0; i < delAfterId.length; i++) {
										if (updPicId.contains(delAfterId[i])) {// 原图片未删除
											newPicId += "," + delAfterId[i];
										} else {// 原图片删除
											delId.add(delAfterId[i]);
										}
									}
									*//****** 判断删除的图片 *****//*
									*//****** 判断修改添加的url *****//*
									FileManage fm = null;
									String[] delAfterUrl = cpai.getRepairVoucherUrl()
											.split(",");
									if (delId.size() > 0) {
										for (int j = 0; j < delId.size(); j++) {
											fm = fmSer.find(Long.valueOf(delId.get(j)));
											for (int k = 0; k < delAfterUrl.length; k++) {
												if (!delAfterUrl[k].contains(fm.getfRelName())) {
													newPicUrl += "," + delAfterUrl[k];
												}
											}
											fmSer.delFile(Util.MFILEURL,Long.valueOf(delId.get(j)));// 删除凭证图片
										}
									} else {
										newPicUrl += "," + cpai.getRepairVoucherUrl();
									}
									*//****** 判断修改添加的url *****//*
								}
								if (newPicId != null) {
									cpai.setRepairVoucherId(newPicId.substring(1,newPicId.length()));
									cpai.setRepairVoucherUrl(newPicUrl.substring(1,newPicUrl.length()));
								}
								//出纳修改并且是已审核或者已复核记录，重新添加一条利润记录因为要重新审核20190408
								if(isCn.equals("1") && (cpai.getIsCheck()==1 || cpai.getIsCheck()==2)){
									TeamAccountBook tabPay = new TeamAccountBook();
									tabPay.setTeamNo(WebUtil.getUnitNum(multReq));
									tabPay.setAccountMoney(cpai.getRepairMoney());
									tabPay.setBalance(tabSer.findBalance(WebUtil.getUnitNum(multReq), "1", cpai.getRepairMoney(),0,null));
									tabPay.setRemark(cpai.getRemark());
									tabPay.setOrderNum(cpai.getId() + "");// 备注记录Id
									tabPay.setAccountName("返回撤销维修");
									tabPay.setAccountType(1);// 自营
									tabPay.setPlateNum(cpai.getPlateNum());
									tabPay.setAddTime(new Date());
									tabPay.setAccountTime(new Date());
									tabPay.setNote("操作员："+WebUtil.getLUser(request).getRealName()+"撤销");
									tabPay.setBookType(1);
									tabSer.save(tabPay);
									cpai.setIsCheck(0);//变成未审核
									cpai.setNote(cpai.getNote() + "("+ DateUtils.DateToStr(new Date()) + "&nbsp;"+ ",操作员:"
											+ WebUtil.getLUser(multReq).getRealName()+ "[返回撤销])");
								}
							} else { // 添加
								cpai = new CarRepairList();
								cpai.setTeamNo(WebUtil.getUnitNum(multReq));
								cpai.setcName(lname);
								if (voucherInfo != null) {
									cpai.setRepairVoucherId(voucherInfo.split("-")[0]);
									cpai.setRepairVoucherUrl(voucherInfo.split("-")[1]);
								}
							}
							cpai.setPlateNum(plateNum);
							cpai.setRepairName(wxStation);
							cpai.setRepairPayWay(Integer.parseInt(wxPayWay));
							cpai.setRepairMoney(Double.valueOf(wxMoney));
							cpai.setRemark(wxRemark);
							cpai.setAddTime(new Date());
							cpai.setRepDate(DateUtils.strToDate(DateUtils.yyyy_MM_dd, wxDate));
							cpai.setReqsrc(reqsrc);
						}
						int result = this.operRepair(uid, cpai);
						if (result == 1) {
							*//***xx添加报销记录 20190524***//*
							if(StringUtils.isBlank(uid)){//添加
								if(cpai.getRepairPayWay()!=0){//非记账才报销，记账不需要报销给师傅20200118
									ReimburseList reim=new ReimburseList();
									reim.setTeamNo(cpai.getUnitNum());
									reim.setFeeType("维修");
									reim.setFeeStatus(1);
									reim.setPlateNum(cpai.getPlateNum());
									reim.setReimName(cpai.getcName().split(",")[1]);//姓名
									reim.setcName(cpai.getcName().split(",")[0]);//账号
									reim.setTotalMoney(cpai.getRepairMoney());
									reim.setRemark(cpai.getRemark());
									reim.setOperator(WebUtil.getLUser(request).getRealName());
									reim.setAddTime(new Date());
									reim.setUseDayStart(cpai.getRepDate());
									reim.setOrderNum(cpai.getId()+"");
									reim.setReimType(4);//维修报销
									reim.setCustomer(cpai.getRepairName());
									reim.setReqsrc(reqsrc);
									reim.setReimVoucherId(cpai.getRepairVoucherId());
									reim.setReimVoucherUrl(cpai.getRepairVoucherUrl());
									reimSer.save(reim);
								}
							}else{//修改
								hql="from ReimburseList where reimType=4 and orderNum=?";
								ReimburseList reim=reimSer.findObj(hql, uid);
								if(reim!=null){
									reim.setPlateNum(cpai.getPlateNum());
									reim.setReimName(cpai.getcName().split(",")[1]);//姓名
									reim.setcName(cpai.getcName().split(",")[0]);//账号
									reim.setTotalMoney(cpai.getRepairMoney());
									reim.setRemark(cpai.getRemark());
									reim.setOperator(reim.getOperator()+","+WebUtil.getLUser(request).getRealName());
									reim.setUseDayStart(cpai.getRepDate());
									reim.setCustomer(cpai.getRepairName());
									if((reim.getIsCheck()==1 || reim.getIsCheck()==2) && isCn.equals("1")){//出纳修改已审核或已复核
										reim.setIsCheck(0);
										reim.setNote(reim.getNote() + "("+ DateUtils.DateToStr(new Date()) + "&nbsp;"+ ",操作员:"
												+ WebUtil.getLUser(request).getRealName()+ "[返回撤销])");
									}
									reim.setReimVoucherId(cpai.getRepairVoucherId());
									reim.setReimVoucherUrl(cpai.getRepairVoucherUrl());
									reimSer.update(reim);
								}
							}
							*//***xx添加报销记录 20190524***//*
							U.setPut(map, 1, "操作成功");
						} else {
							fg = U.setPutFalse(map, "操作异常" + result);
						}
					}else {
						fg = U.setPutFalse(map, "操作异常" + voucherInfo);
					}
				}*/
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
	public Map<String, Object> updRepair(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		MultipartHttpServletRequest multReq, String unitNum, String luname, String uid, String currKm, String wxPayWay,
		String wxStation, String wxMoney, String wxDate, String wxRemark, String imgIds) {
		String logtxt = U.log(log, "修改-维修油记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			/*if(fg){
				if(StringUtils.isEmpty(unitNum)){
					fg = U.setPutFalse(map, "您未登录车队，不能继续操作");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录车队编号] unitNum="+unitNum);
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
			
			DriverList dl = null;
			if(fg){
				dl = dlSer.findDriverOfCarTeam(luname, unitNum);
				if(dl == null){
					fg = U.setPutFalse(map, "当前驾驶员不存在");
				}else if(StringUtils.isEmpty(dl.getReimburseTipOper())) {
					fg = U.setPutFalse(map, 3, "请先设置报销通知操作员");
				}
				
				U.log(log, "[登录用户] driver="+dl.getDriverName());
			}
			
			if(fg){
				if(StringUtils.isEmpty(uid)){
					fg = U.setPutFalse(map, "[维修记录id]不能为空");
				}else{
					uid = uid.trim();
					if(!FV.isLong(uid)){
						fg = U.setPutFalse(map, "[维修记录id]格式错误");
					}
					
					U.log(log, "[维修记录id] uid="+uid);
				}
			}
			
			CarRepairList crl = null;
			if(fg) {
				crl = cpaiDao.findByField("id", Long.parseLong(uid));
				if(crl == null){
					fg = U.setPutFalse(map, "该[维修记录]不存在");
				}else if(crl.getIsCheck() != 0){
					fg = U.setPutFalse(map, "该维修记录不是[未审核]，不能修改");
				}
				
			}
			
			ReimburseList rl = null;
			if(fg) {
				hql = "from ReimburseList where reimType = ? and orderNum = ?";
				rl = reimSer.findObj(hql, 4, crl.getId()+"");
				if(rl == null) {
					fg = U.setPutFalse(map, "该维修记录对应[财务报销记录]不存在，请联系管理员");
				}else {
					U.log(log, "[财务报销记录id] id="+rl.getId());
				}
			}
			
			double _currKm = 0d;
			if(fg){
				if(StringUtils.isEmpty(currKm)){
					fg = U.setPutFalse(map, "[车辆公里数]不能为空");
				}else{
					currKm = currKm.trim();
					if(!FV.isDouble(currKm)){
						fg = U.setPutFalse(map, "[车辆公里数]格式不正确");
					}else{
						_currKm = Double.parseDouble(currKm);
					}
					
					U.log(log, "[车辆公里数] currKm="+currKm);
				}
			}
			
			int _wxPayWay = -1;// 0-记账； 1-现金；2019-05-15- 14:36 改
			if(fg){
				if(StringUtils.isEmpty(wxPayWay)){
					fg = U.setPutFalse(map, "[支付方式]不能为空");
				}else{
					wxPayWay = wxPayWay.trim();
					if(!FV.isOfEnum(PayWay.class, wxPayWay)){
						fg = U.setPutFalse(map, "[支付方式]格式不正确");
					}else{
						if(PayWay.CASH_PAY.name().equals(wxPayWay)){
							_wxPayWay = 1;
						}else if(PayWay.JZ_PAY.name().equals(wxPayWay)){
							_wxPayWay = 0;
						}
					}
					
					U.log(log, "支付方式：wxPayWay="+wxPayWay);
				}
			}
			
			if(fg) {
				// 公里数变化了
				if(_currKm != crl.getCurrentKilo()) {
					// 修改公里数时，公里数只能是顺序添加的自身前后两条数据的公里数之间
					Object[] minMax = coiSer.getMinMaxKm(crl, 4);
					double min = Double.parseDouble(minMax[0].toString());
					double max = Double.parseDouble(minMax[1].toString());
					
					if(_currKm <= min || _currKm >= max) {
						fg = U.setPutFalse(map, -2, "公里数应该在"+(int)min+"到"+(int)max+"之间");
					}
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(wxStation)){
					fg = U.setPutFalse(map, "[维修站名称]不能为空");
				}else{
					wxStation = wxStation.trim();
					
					U.log(log, "维修站名称：wxStation="+wxStation);
				}
			}
			
			double _wxMoney = 0d;
			if(fg){
				if(StringUtils.isEmpty(wxMoney)){
					fg = U.setPutFalse(map, "[维修金额]不能为空");
				}else{
					wxMoney = wxMoney.trim();
					if(!FV.isDouble(wxMoney)){
						fg = U.setPutFalse(map, "[维修金额]格式错误，应为正数");
					}else{
						_wxMoney = Double.parseDouble(wxMoney);
					}
					
					U.log(log, "维修金额：wxMoney="+wxMoney);
				}
			}
			
			Date _wxDate = null;
			if(fg){
				if(StringUtils.isEmpty(wxDate)){
					fg = U.setPutFalse(map, "[维修日期]不能为空");
				}else{
					wxDate = wxDate.trim();
					if(!FV.isDate(wxDate)){
						fg = U.setPutFalse(map, "[维修日期]格式不正确");
					}else{
						_wxDate = DateUtils.strToDate(wxDate);
					}
					
					U.log(log, "维修日期：wxDate="+wxDate);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(wxRemark)){
					fg = U.setPutFalse(map, "[维修内容]不能为空");
				}else{
					wxRemark = wxRemark.trim();
					if(wxRemark.length() > 100){
						fg = U.setPutFalse(map, "[维修内容]最多填写100个字");
					}
					
					U.log(log, "维修内容：wxRemark="+wxRemark);
				}
			}
			
			List<Object> delImgIds = new ArrayList<Object>();
			String imgUrlStr = "";
			if(fg) {
				if(StringUtils.isEmpty(imgIds)) {
					U.log(log, "[图片id数组字符串]为空，则说明用户将之前的图片都删除了");
					
					// 修改前的全部id数组
					if(StringUtils.isNotBlank(crl.getRepairVoucherId())) {
						String[] allIds = crl.getRepairVoucherId().split(",");
						for (String id1 : allIds) {
							delImgIds.add(Long.parseLong(id1));
						}
						U.log(log, "需要删除的图片id数组字符串 delImgIds="+StringUtils.join(delImgIds.toArray(), ","));
					}
				}else {
					imgIds = imgIds.trim();
					
					// 不会删除的id数组
					String[] noDelIds = imgIds.split(",");
					
					// 修改前的全部id数组
					if(StringUtils.isNotBlank(crl.getRepairVoucherId())) {
						String[] allIds = crl.getRepairVoucherId().split(",");
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
					}
					
					List<String> imgUrls = new ArrayList<String>();
					// 修改前的全部url数组
					if(StringUtils.isNotBlank(crl.getRepairVoucherUrl())) {
						String[] allUrls = crl.getRepairVoucherUrl().split(",");
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
					U.log(log, "[维修凭证图片]不为空，即用户修改/添加了图片");
					
					voucherInfo = fmSer.upFiles(multReq, response, "维修凭证");
					if(voucherInfo == null || voucherInfo.contains("-")){
						U.log(log, "修改图片成功，结果："+voucherInfo);
					}else {
						fg = U.setPutFalse(map, "[维修记录]修改维修凭证失败");
					}
				}else {
					U.log(log, "[维修凭证图片]为空，即用户未修改图片");
				}
				
				if(fg) {
					// 存在需要删除的图片id数组字符串
					if(delImgIds.size() > 0) {
						fmSer.delFileList(delImgIds.toArray(), Util.MFILEURL);
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
				crl.setRepairVoucherId(newIds);
				crl.setRepairVoucherUrl(newUrls);
				crl.setIsCheck(0);// 变成未审核
				crl.setRepairName(wxStation);
				crl.setRepairPayWay(_wxPayWay);
				crl.setRepairMoney(_wxMoney);
				crl.setRepDate(_wxDate);
				crl.setCurrentKilo(_currKm);
				
				String operNote = com.ebam.mis.utils.kcb.others.Util.getOperInfo(dl.getDriverName(), "修改");
				if(StringUtils.isNotBlank(crl.getNote())) {
					operNote += crl.getNote();
				}
				crl.setNote(operNote);
				cpaiDao.update(crl);
				U.log(log, "更新[维修记录]完成");
				
				// 修改对应财务报销记录
				rl.setIsCheck(0);// 变成未审核
				rl.setTotalMoney(crl.getRepairMoney());
				rl.setRemark(crl.getRemark());
				rl.setOperator(rl.getOperator()+","+dl.getDriverName());
				rl.setUseDayStart(crl.getRepDate());
				rl.setCustomer(crl.getRepairName());
				rl.setReimVoucherId(crl.getRepairVoucherId());
				rl.setReimVoucherUrl(crl.getRepairVoucherUrl());
				
				operNote = com.ebam.mis.utils.kcb.others.Util.getOperInfo(dl.getDriverName(), "修改");
				if(StringUtils.isNotBlank(rl.getNote())) {
					operNote += rl.getNote();
				}
				rl.setNote(operNote);
				reimSer.update(rl);
				U.log(log, "更新[对应财务报销记录]完成");
				
				U.setPut(map, 1, "修改[维修记账]成功");
				
				-------通知驾驶员设置的职务人员--begin------------
				if(rl != null && rl.getIsCheck() == 0) {
					U.log(log, "微信通知驾驶员所设置的职务人员：未审核才通知");
					
					if(StringUtils.isNotBlank(dl.getReimburseTipOper())) {
						String[] opers = dl.getReimburseTipOper().split(",");
						
						for (String o : opers) {
							ymSer.orderWaitforCheckOfWxmsg(null, rl, o);
						}
					}
				}else {
					U.log(log, "该报销记账不是：未审核，则不通知");
				}
				-------通知驾驶员设置的职务人员--end------------
				
			}*/
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delRepair(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String lname, String did) {
		String logtxt = U.log(log, "删除-维修记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.WX == reqsrc || ReqSrc.PC_COMPANY == reqsrc){
				
				CarRepairList obj = null;
				if(fg){
					if(StringUtils.isEmpty(did)){
						fg = U.setPutFalse(map, "[维修记录id]不能为空");
					}else{
						did = did.trim();
						if(!FV.isLong(did)){
							fg = U.setPutFalse(map, "[维修记录id]格式错误");
						}else{
							obj = cpaiDao.find(Long.parseLong(did));
							if(obj == null){
								fg = U.setPutFalse(map, "该[维修记录]不存在");
							}
						}
						
						U.log(log, "加油记录id：did="+did);
					}
				}
				if(ReqSrc.WX == reqsrc){ //微信端才判断
					if(fg){
						if(StringUtils.isEmpty(lname)){
							fg = U.setPutFalse(map, "[登录用户]不能为空");
						}else{
							lname = lname.trim();
							
							U.log(log, "登录用户：lname="+lname);
						}
					}
					if(fg){
						String unitNum = LU.getLUnitNum(request, redis);
						Customer lcus = LU.getLUSER(request, redis);
						if(!obj.getRepairDriver().getUname().contains(lname) && !obj.getRepairDriver().getPhone().contains(lcus.getBaseUserId().getPhone())){
							fg = U.setPutFalse(map, "删除失败，该[维修记录]不是您添加的");
						}else if(!obj.getUnitNum().equals(unitNum)){
							fg = U.setPutFalse(map, "删除失败，该[维修记录]不属于当前车队");
						}else if(obj.getIsCheck() != -1 && obj.getIsCheck() != 0){
							fg = U.setPutFalse(map, "删除失败，该[维修记录]已审核");
						}
					}
				}
				if(ReqSrc.PC_COMPANY == reqsrc){ //后台
					String unitNum =LU.getLUnitNum(request, redis);
					if(!obj.getUnitNum().equals(unitNum)){
						fg = U.setPutFalse(map, "删除失败，该[维修记录]不属于当前车队");
					}if(fg){
						if(obj.getIsCheck() != -1 && obj.getIsCheck() != 0){
							fg = U.setPutFalse(map, "删除失败，该[维修记录]状态不能删除");
						}
					}
				}
				if(fg){
					this.operRepair(did, null);
					
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
	public int operRepair(String upd, CarRepairList rel) {
		try {
			if(rel != null){
				if(StringUtils.isNotEmpty(upd)){
					cpaiDao.update(rel);
				}else{
					cpaiDao.save(rel);
				}
			}else{
				CarRepairList delrel = cpaiDao.find(Long.parseLong(upd));
				String hql="from ReimburseList where reimType=4 and orderNum=?";
				ReimburseList reim=reimSer.findObj(hql, upd+"");//删除报销记录
				if(reim!=null){
					reimSer.delete(reim);
				}
				/*if(StringUtils.isNotBlank(delrel.getRepairVoucherId())){
					String [] fmid=delrel.getRepairVoucherId().split(",");
					for (int i = 0; i < fmid.length; i++) {
						fmSer.delFile(Util.MFILEURL, Long.valueOf(fmid[i]));//删除凭证图片
					}
				}*/
				cpaiDao.delete(delrel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	
	@Override
	public int checkCpai(String checkId, int state, String note,String operator, String remark,double currMoney,int isCash) {
		CarRepairList cpai = cpaiDao.find(Long.parseLong(checkId));
		if(cpai!=null){
			if(state==3){
				if (isCash==1) {// 现金付款并且选择电子钱包,将现金金额加入师傅钱包-提现
					CusWallet cw = cwSer.findByField("baseUserId.uname",cpai.getRepairDriver().getUname());
					if (cw != null) {
						cw.setCashBalance(MathUtils.add(cw.getCashBalance(),currMoney, 2));
						cwSer.update(cw);
						WalletList wl = new WalletList();
						wl.setcName(cw.getcName());
						wl.setAmoney(currMoney);
						wl.setCashBalance(cw.getCashBalance());
						wl.setAssist(cpai.getId() + "");
						wl.setAtype(1);
						wl.setType(0);
						wl.setStatus(0);
						wl.setNote("现金加油");
						wl.setAtime(new Date());
						wlSer.save(wl);
					}
				}
				if(MathUtils.add(cpai.getVerificationMoney(), currMoney, 2)>=cpai.getRepairMoney()){//核销完金额
					cpai.setIsCheck(3);
				}
				cpai.setVerificationMoney(MathUtils.add(cpai.getVerificationMoney(), currMoney, 2));//加入已核销金额
			}else{
				cpai.setIsCheck(state);
			}
			if (StringUtils.isNotEmpty(cpai.getOperNote())) {
				cpai.setOperNote(cpai.getOperNote() + "("+ DateUtils.DateToStr(new Date()) + "&nbsp;"
						+ note + ",操作员:"+ operator+ "["+remark+"])");
			} else {
				cpai.setOperNote(DateUtils.DateToStr(new Date())+ "&nbsp;" + note + ",操作员:"+ operator+ "["+remark+"]");
			}
			cpaiDao.update(cpai);
			return 1;
		}
		return 0;
	}
	
	@Override
	public Map<String, Object> findRepairDetail(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String luname, String id) {
		String logtxt = U.log(log, "获取-维修记账-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
//		String hql = "";
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
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[维修记账对象id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[维修记账id]格式错误");
					}
					
					U.log(log, "[维修记账id] id="+id);
				}
			}
			
			CarRepairList crl = null;
			if(fg) {
				crl = cpaiDao.findByField("id", Long.parseLong(id));
				if(crl == null) {
					fg = U.setPutFalse(map, "[维修记账记录]不存在");
				}else if(!crl.getRepairDriver().getUname().contains(luname)) {
					fg = U.setPutFalse(map, "该[维修记账]不是您添加的");
				}
			}
			
//			// 获取-维修记账-对应的车队财务报销记录
//			ReimburseList rl = null;
//			if(fg) {
//				hql = "from ReimburseList where reimType = ? and orderNum = ?";
//				rl = reimSer.findObj(hql, 4, col.getId());
//				if(rl == null) {
//					fg = U.setPutFalse(map, "该维修记账对应[财务报销记录]不存在");
//				}
//			}
			if(fg) {
				map.put("data", crl);
				
				U.setPut(map, 1, "获取[维修记账记录]成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}		
		return map;
	}
	
	
	
	@Override
	public Map<String, Object> getCarRepairList(ReqSrc reqsrc,HttpServletRequest request, HttpServletResponse response,
			JSONObject jsonObject,String unitNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-车辆维修记录-分页列表", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			String page = jsonObject.getString("page");
			String rows = jsonObject.getString("rows");
			String plateNum = jsonObject.getString("plateNum");
			//维修站
			String cpaiStation = jsonObject.getString("cpaiStation");
			String sTime = jsonObject.getString("sTime");
			String eTime = jsonObject.getString("eTime");
			String isCheck = jsonObject.getString("isCheck");
			String driver = jsonObject.getString("driver");
			if (fg)
				fg = U.valPageNo(map, page, rows, "用户");

			if (fg) {
				Page<CarRepairList> findRepairList = cpaiDao.findRepairList(reqsrc, unitNum, page, rows, plateNum, cpaiStation, sTime, eTime, isCheck, driver);
				U.setPageData(map, findRepairList);

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
	public Map<String, Object> addCarRepair(ReqSrc reqSrc, HttpServletRequest request, HttpServletResponse response,
			String unitNum, JSONObject jsonObject) {
		String logtxt = U.log(log, "车辆维修记录-添加", reqSrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		return null;
	}
	
}
