package com.fx.service.impl.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.PayWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.FileManDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.finance.CarRepairListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CusWallet;
import com.fx.entity.cus.WalletList;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.finance.StaffReimburse;
import com.fx.service.cus.CusWalletService;
import com.fx.service.cus.WalletListService;
import com.fx.service.finance.CarRepairListService;
import com.fx.service.finance.ReimburseListService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CarRepairListServiceImpl extends BaseServiceImpl<CarRepairList,Long> implements CarRepairListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private CarRepairListDao cpaiDao;
	@Override
	public ZBaseDaoImpl<CarRepairList, Long> getDao() {
		return cpaiDao;
	}
	/** 缓存-服务 */
	@Autowired
    private RedisUtil redis;
	/** 用户钱包-服务 */
	@Autowired
	private CusWalletService cwSer;
	/** 交易记录-服务 */
	@Autowired
	private WalletListService wlSer;
	/** 报销凭证-服务 */
	@Autowired
	private ReimburseListService reimSer;
	/** 维修记账-服务 */
//	@Autowired
//	private CarRepairListDao wxjzDao;
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	/** 员工-服务 */
	@Autowired
	private StaffDao staffDao;
	/** 文件管理-服务 */
	@Autowired
	private FileManDao fileManDao;
	/** 员工记账-服务 */
	@Autowired
	private StaffReimburseDao staffReimburseDao;
	
	
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
				Staff cus = LU.getLStaff(request, redis);
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
						Staff lcus = LU.getLStaff(request, redis);
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
	public Map<String, Object> addWxjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String flen, String plateNum, String currKm, String wxPayWay, String wxStation, 
		String wxMoney, String wxDate, String wxRemark) {
		String logtxt = U.log(log, "添加-维修记账", reqsrc);

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
							fg = U.setPutFalse(map, "请上传维修费用清单凭证图片");
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
			
			double _currKm = 0d;
			if(fg){
				if(StringUtils.isEmpty(currKm)){
					U.log(log, "[当前公里数]为空");
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
					
					U.log(log, "[支付方式] wxPayWay="+wxPayWay);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(wxStation)){
					fg = U.setPutFalse(map, "[维修站名称]不能为空");
				}else{
					wxStation = wxStation.trim();
					
					U.log(log, "[维修站名称] wxStation="+wxStation);
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
					
					U.log(log, "[维修金额] wxMoney="+wxMoney);
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
					
					U.log(log, "[维修日期] wxDate="+wxDate);
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
					
					U.log(log, "[维修内容] wxRemark="+wxRemark);
				}
			}
			
			// 此次维修公里数，必须大于上一次的维修公里数
			double prevkm = 0d;
			if(fg){
				if(StringUtils.isNotBlank(currKm)){// 存在公里数才判断（注：此处实际上应该按照加油记录来判断，因为维修记录可以不填写）
					List<CarRepairList> cpais = new ArrayList<CarRepairList>();
					hql = "from CarRepairList where plateNum = ?0 and unitNum = ?1 order by id desc";
					cpais = cpaiDao.hqlListFirstMax(hql, 0, 1, plateNum, lunitNum);
					
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
			
			if(fg) {
				CarRepairList crl = new CarRepairList();
				crl.setUnitNum(lunitNum);
				crl.setRepairDriver(lbuser);
				crl.setPlateNum(plateNum);
				crl.setRepairName(wxStation);
				crl.setRepairMoney(_wxMoney);
				crl.setRepairPayWay(_wxPayWay);
				crl.setRepairRemark(wxRemark);
				crl.setIsCheck(0);
				crl.setAddTime(new Date());
				crl.setCurrentKilo(_currKm);
				crl.setRepDate(_wxDate);
				crl.setReqsrc(reqsrc);
				crl.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
				cpaiDao.save(crl);
				U.log(log, "添加-维修记账-完成");
				
				// 维修支付方式为非记账，则添加员工记账记录
				if(crl.getRepairPayWay() != 0) {
					StaffReimburse sr = new StaffReimburse();
					sr.setUnitNum(crl.getUnitNum());
					sr.setReimUserId(lbuser);
					sr.setDeptId(driver.getDeptId());
					sr.setJzDate(crl.getRepDate());
					sr.setRemark(crl.getRepairRemark());
					sr.setPayMoney(crl.getRepairMoney());
					sr.setIsCheck(0);
					sr.setJzType(JzType.WXJZ);
					sr.setReimVoucherUrl(crl.getRepairVoucherUrl());
					sr.setReqsrc(reqsrc);
					sr.setOperNote(crl.getOperNote());
					sr.setAddTime(crl.getAddTime());
					sr.setDat(crl.getId()+"");// 保存加油记账id
					staffReimburseDao.save(sr);
					U.log(log, "添加-员工报账记录-完成");
				}
				
				// 前端需要此id，保存图片
				map.put("uid", crl.getId());
				
				U.setPut(map, 1, "添加-维修记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> updWxjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String uid, String flen, String currKm, String wxPayWay, String wxStation, 
		String wxMoney, String wxDate, String wxRemark) {
		String logtxt = U.log(log, "修改-维修记账", reqsrc);

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
				driver = staffDao.getTeamDriver(lunitNum, luname);
				if(driver == null) {
					fg = U.setPutFalse(map, "当前用户不是驾驶员，不能操作");
				}
			}
			
			CarRepairList obj = null;
			if(fg) {
				obj = cpaiDao.findByField("id", Long.parseLong(uid));
				if(obj == null){
					fg = U.setPutFalse(map, "该[维修记录]不存在");
				}else if(obj.getIsCheck() != 0){
					fg = U.setPutFalse(map, "该维修记录不是[未审核]，不能修改");
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
							fg = U.setPutFalse(map, "请上传维修费用清单凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
				}
			}
			
			double _currKm = 0d;
			if(fg){
				if(StringUtils.isEmpty(currKm)){
					U.log(log, "[当前公里数]为空");
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
					
					U.log(log, "[支付方式] wxPayWay="+wxPayWay);
				}
			}
			
			if(fg) {
				// 公里数变化了
				if(_currKm != obj.getCurrentKilo()) {
					// 修改公里数时，公里数只能是顺序添加的自身前后两条数据的公里数之间
					Object[] minMax = staffReimburseDao.getMinMaxKm(obj, 4);
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
					
					U.log(log, "[维修站名称] wxStation="+wxStation);
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
					
					U.log(log, "[维修金额] wxMoney="+wxMoney);
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
					
					U.log(log, "[维修日期] wxDate="+wxDate);
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
					
					U.log(log, "[维修内容] wxRemark="+wxRemark);
				}
			}
			
			if(fg) {
				obj.setRepairName(wxStation);
				obj.setRepairMoney(_wxMoney);
				obj.setRepairPayWay(_wxPayWay);
				obj.setRepairRemark(wxRemark);
				obj.setIsCheck(0);
				obj.setAddTime(new Date());
				obj.setCurrentKilo(_currKm);
				obj.setRepDate(_wxDate);
				obj.setOperNote(obj.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
				cpaiDao.update(obj);
				U.log(log, "修改-维修记账-完成");
				
				// 修改/添加-对应的员工记账记录
				hql = "from StaffReimburse where unitNum = ?0 and reimUserId.uname = ?1 and dat = ?2 and jzType = ?3";
				StaffReimburse sr = staffReimburseDao.findObj(hql, lunitNum, luname, obj.getId()+"", JzType.JYJZ);
				if(sr != null) {// 存在即修改
					// 维修支付方式为非记账，则添加员工记账记录
					if(obj.getRepairPayWay() != 0) {
						sr.setRemark(obj.getRepairRemark());
						sr.setPayMoney(obj.getRepairMoney());
						sr.setIsCheck(0);
						sr.setReimVoucherUrl(obj.getRepairVoucherUrl());
						sr.setOperNote(obj.getOperNote());
						sr.setAddTime(obj.getAddTime());
						staffReimburseDao.update(sr);
						U.log(log, "修改-员工报账记录-完成");
					}else {
						// 删除对应员工记账记录
						staffReimburseDao.delete(sr);
						U.log(log, "删除-对应员工记账记录");
					}
				}else {
					// 维修支付方式为非记账，则添加员工记账记录
					if(obj.getRepairPayWay() != 0) {
						sr = new StaffReimburse();
						sr.setUnitNum(obj.getUnitNum());
						sr.setReimUserId(lbuser);
						sr.setDeptId(driver.getDeptId());
						sr.setJzDate(obj.getRepDate());
						sr.setRemark(obj.getRepairRemark());
						sr.setPayMoney(obj.getRepairMoney());
						sr.setIsCheck(0);
						sr.setJzType(JzType.WXJZ);
						sr.setReimVoucherUrl(obj.getRepairVoucherUrl());
						sr.setReqsrc(reqsrc);
						sr.setOperNote(obj.getOperNote());
						sr.setAddTime(obj.getAddTime());
						sr.setDat(obj.getId()+"");// 保存加油记账id
						staffReimburseDao.save(sr);
						U.log(log, "添加-员工报账记录-完成");
					}
				}
				
				// 前端需要此id，保存图片
				map.put("uid", obj.getId());
				
				U.setPut(map, 1, "修改-维修记账-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> delWxjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String lname, String did) {
		String logtxt = U.log(log, "师傅删除-维修记录", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;

		try {
			CarRepairList obj = null;
			String unitNum = null, uname = null;
			if (fg) {
				if (StringUtils.isEmpty(did)) {
					fg = U.setPutFalse(map, "[维修记录id]不能为空");
				} else {
					did = did.trim();
					if (!FV.isLong(did)) {
						fg = U.setPutFalse(map, "[维修记录id]格式错误，应为long类型");
					} else {
						obj = cpaiDao.find(Long.parseLong(did));
						if (obj == null) {
							fg = U.setPutFalse(map, "该[维修记录]不存在");
						}else {
							unitNum = obj.getUnitNum();
							uname = obj.getRepairDriver().getUname();
						}
					}

					U.log(log, "维修记录id：did=" + did);
				}
			}
			
			StaffReimburse sr = null;
			if(fg) {
				hql = "from StaffReimburse where unitNum = ?0 and reimUserId.uname = ?1 and dat = ?2";
				sr = staffReimburseDao.findObj(hql, unitNum, uname, obj.getId()+"");
				if(sr == null) {
					U.log(log, "未添加维修记账对应员工报账记录");
				}else {
					U.log(log, "存在维修记账对应员工报账记录");
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
						if (obj.getIsCheck() != -1 && obj.getIsCheck() != 0) {
							fg = U.setPutFalse(map, "删除失败，该[加油记录]已审核");
						}
					}
				}
				
				if(fg) {
					cpaiDao.delete(obj);
					U.log(log, "删除-维修记账-记录完成");
					
					// 删除维修记账对应凭证记录及图片
					fileManDao.delJzbxFile(unitNum, uname, FileType.WXJZ_IMG, obj.getId()+"");
					
					if(sr != null) {
						staffReimburseDao.delete(sr);
						U.log(log, "删除-维修记账-对应员工记账记录-完成");
					}
					
					U.setPut(map, 1, "删除-维修记账记录-成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> findWxjzDetail(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String luname, String id) {
		String logtxt = U.log(log, "获取-维修记账-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
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
			
			if(fg) {
				map.put("data", crl);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				
				U.setPut(map, 1, "获取[维修记账记录]成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findWxjzList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String page, String rows, String stime, String etime) {
		String logtxt = U.log(log, "获取-维修记账-列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			fg = U.valPageNo(map, page, rows, "维修记账");
			fg = U.valSEtime(map, stime, etime, "维修时间");
			
			if(fg) {
				String unitNum = LU.getLUnitNum(request, redis);
				String luname = LU.getLUName(request, redis);
				Page<CarRepairList> pd = cpaiDao.findPageWxjzList(reqsrc, page, rows, stime, etime, unitNum, luname);
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
	
}
