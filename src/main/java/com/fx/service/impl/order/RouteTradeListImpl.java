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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.cus.WalletListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.dao.order.CarOrderDao;
import com.fx.dao.order.MainCarOrderDao;
import com.fx.dao.order.RouteTradeListDao;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.WalletList;
import com.fx.entity.finance.StaffReimburse;
import com.fx.entity.order.CarOrder;
import com.fx.entity.order.MainCarOrder;
import com.fx.entity.order.RouteTradeList;
import com.fx.service.order.RouteTradeListService;

@Service
@Transactional
public class RouteTradeListImpl extends BaseServiceImpl<RouteTradeList, Long> implements RouteTradeListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 行程收支-数据源 */
	@Autowired
	private RouteTradeListDao routeTradeListDao;
	@Override
	public ZBaseDaoImpl<RouteTradeList, Long> getDao() {
		return routeTradeListDao;
	}
	
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	/** 员工-服务 */
	@Autowired
	private StaffDao staffDao;
	/** 车辆订单-服务 */
	@Autowired
	private CarOrderDao carOrderDao;
	/** 员工记账-服务 */
	@Autowired
	private StaffReimburseDao staffReimburseDao;
	/** 主订单-服务 */
	@Autowired
	private MainCarOrderDao mainOrderDao;
	/** 交易记录-服务 */
	@Autowired
	private WalletListDao walletListDao;
	
	@Override
	public Map<String, Object> addXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String flen, String orderNum, String groupCash, String groupRebate,
		String routeRebate, String singleFee, String washingFee, String parkingFee, String roadFee,
		String livingFee, String otherFee, String waterFee, String stayFee, String remark) {
		String logtxt = U.log(log, "添加-行程记账", reqsrc);
		
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
							fg = U.setPutFalse(map, "请上传费用清单凭证图片");
						}
					}
					
					U.log(log, "[上传文件个数] flen="+flen);
				}
			}
			
			List<RouteTradeList> rtls = new ArrayList<RouteTradeList>();
			CarOrder order = null;
			MainCarOrder mainOrder = null;
			String plateNum = null;
			if(fg){
				if(StringUtils.isEmpty(orderNum)){
					fg = U.setPutFalse(map, "[订单编号]不能为空");
				}else{
					orderNum = orderNum.trim();
					
					order = carOrderDao.findByField("orderNum", orderNum);
					if(order == null){
						fg = U.setPutFalse(map, "当前订单不存在");
					}else{
						mainOrder = mainOrderDao.findByField("orderNum", order.getMainOrderNum());
						
						rtls = order.getTrades();
						
						plateNum = order.getDisCar().getPlateNum();
						
						U.log(log, "派车订单-车牌：plateNum="+plateNum);
					}
					
					U.log(log, "当前派车订单编号：orderNum="+orderNum);
				}
			}
			
			double _groupCash = 0d;
			if(fg){
				if(StringUtils.isEmpty(groupCash)){
					U.log(log, "[团上现收]为空");
				}else{
					groupCash = groupCash.trim();
					if(!FV.isDouble(groupCash)){
						fg = U.setPutFalse(map, "[团上现收]格式错误，应为正数");
					}else{
						_groupCash = Double.parseDouble(groupCash);
					}
					
					U.log(log, "[团上现收] groupCash="+groupCash);
				}
			}
			
			double _groupRebate = 0d;
			if(fg){
				if(StringUtils.isEmpty(groupRebate)){
					U.log(log, "[团上返点]为空");
				}else{
					groupRebate = groupRebate.trim();
					if(!FV.isDouble(groupRebate)){
						fg = U.setPutFalse(map, "[团上返点]格式错误，应为正数");
					}else{
						_groupRebate = Double.parseDouble(groupRebate);
					}
					
					U.log(log, "[团上返点] groupRebate="+groupRebate);
				}
			}
			
			double _routeRebate = 0d;
			if(fg){
				if(StringUtils.isEmpty(routeRebate)){
					U.log(log, "[行程加点]为空");
				}else{
					routeRebate = routeRebate.trim();
					if(!FV.isDouble(routeRebate)){
						fg = U.setPutFalse(map, "[行程加点]格式错误，应为正数");
					}else{
						_routeRebate = Double.parseDouble(routeRebate);
					}
					
					U.log(log, "[行程加点] routeRebate="+routeRebate);
				}
			}
			
			double _singleFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(singleFee)){
					U.log(log, "[单据费]为空");
				}else{
					singleFee = singleFee.trim();
					if(!FV.isDouble(singleFee)){
						fg = U.setPutFalse(map, "[单据费]格式错误，应为正数");
					}else{
						_singleFee = Double.parseDouble(singleFee);
					}
					
					U.log(log, "[单据费] singleFee="+singleFee);
				}
			}
			
			double _washingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(washingFee)){
					U.log(log, "[洗车费]为空");
				}else{
					washingFee = washingFee.trim();
					if(!FV.isDouble(washingFee)){
						fg = U.setPutFalse(map, "[洗车费]格式错误，应为正数");
					}else{
						_washingFee = Double.parseDouble(washingFee);
					}
					
					U.log(log, "[洗车费] washingFee="+washingFee);
				}
			}
			
			double _parkingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(parkingFee)){
					U.log(log, "[停车费]为空");
				}else{
					parkingFee = parkingFee.trim();
					if(!FV.isDouble(parkingFee)){
						fg = U.setPutFalse(map, "[停车费]格式错误，应为正数");
					}else{
						_parkingFee = Double.parseDouble(parkingFee);
					}
					
					U.log(log, "[停车费] parkingFee="+parkingFee);
				}
			}
			
			double _roadFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(roadFee)){
					U.log(log, "[过路费]为空");
				}else{
					roadFee = roadFee.trim();
					if(!FV.isDouble(roadFee)){
						fg = U.setPutFalse(map, "[过路费]格式错误，应为正数");
					}else{
						_roadFee = Double.parseDouble(roadFee);
					}
					
					U.log(log, "[过路费] roadFee="+roadFee);
				}
			}
			
			double _livingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(livingFee)){
					U.log(log, "[生活费]为空");
				}else{
					livingFee = livingFee.trim();
					if(!FV.isDouble(livingFee)){
						fg = U.setPutFalse(map, "[生活费]格式错误，应为正数");
					}else{
						_livingFee = Double.parseDouble(livingFee);
					}
					
					U.log(log, "[生活费] livingFee="+livingFee);
				}
			}
			
			double _otherFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(otherFee)){
					U.log(log, "[其他费用]为空");
				}else{
					otherFee = otherFee.trim();
					if(!FV.isDouble(otherFee)){
						fg = U.setPutFalse(map, "[其他费用]格式错误，应为正数");
					}else{
						_otherFee = Double.parseDouble(otherFee);
					}
					
					U.log(log, "[其他费用] otherFee="+otherFee);
				}
			}
			
			double _waterFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(waterFee)){
					U.log(log, "[买水费用]为空");
				}else{
					waterFee = waterFee.trim();
					if(!FV.isDouble(waterFee)){
						fg = U.setPutFalse(map, "[买水费用]格式错误，应为正数");
					}else{
						_waterFee = Double.parseDouble(waterFee);
					}
					
					U.log(log, "[买水费用] waterFee="+_waterFee);
				}
			}
			
			double _stayFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(stayFee)){
					U.log(log, "[住宿费用]为空");
				}else{
					stayFee = stayFee.trim();
					if(!FV.isDouble(stayFee)){
						fg = U.setPutFalse(map, "[住宿费用]格式错误，应为正数");
					}else{
						_stayFee = Double.parseDouble(stayFee);
					}
					
					U.log(log, "[住宿费用] waterFee="+_stayFee);
				}
			}
			if(fg){
				if(StringUtils.isEmpty(remark)){
					U.log(log, "[备注]为空");
				}else{
					remark = remark.trim();
					if(remark.length() > 40){
						fg = U.setPutFalse(map, "[备注]最多填写40个字");
					}
					
					U.log(log, "[备注] remark="+remark);
				}
			}
			
			// 团上收入, 团上支出
			double routeIncome = 0d, routePay = 0d;
			// 团上现收记录, 团上支出记录
			String incomeNote = "", payNote = "";
			if(fg) {
				U.log(log, "验证：现收项或者支出项必须有一个有金额");
				
				routeIncome = _groupCash + _groupRebate + _routeRebate;
				U.log(log, "收入金额："+routeIncome);
				
				if(_groupCash > 0) {
					incomeNote += "团上现收:"+_groupCash+"元,";
				}
				if(_groupRebate > 0) {
					incomeNote += "团上返点:"+_groupRebate+"元,";
				}
				if(_routeRebate > 0) {
					incomeNote += "行程加点:"+_routeRebate+"元,";
				}
				if(StringUtils.isNotBlank(incomeNote)) {
					incomeNote = incomeNote.substring(0, incomeNote.length() - 1) + ";";
				}
				
				routePay = _singleFee + _washingFee + _parkingFee + _roadFee + _livingFee + _otherFee + _waterFee + _stayFee;
				U.log(log, "支出金额："+routePay);
				
				if(_singleFee > 0) {
					payNote += "打单费:"+_singleFee+"元,";
				}
				if(_washingFee > 0) {
					payNote += "洗车费:"+_washingFee+"元,";
				}
				if(_parkingFee > 0) {
					payNote += "停车费:"+_parkingFee+"元,";
				}
				if(_roadFee > 0) {
					payNote += "过路费:"+_roadFee+"元,";
				}
				if(_livingFee > 0) {
					payNote += "生活费:"+_livingFee+"元,";
				}
				if(_otherFee > 0) {
					payNote += "其他费:"+_otherFee+"元,";
				}
				if(_waterFee > 0) {
					payNote += "买水费:"+_waterFee+"元,";
				}
				if(_stayFee > 0) {
					payNote += "住宿费:"+_stayFee+"元,";
				}
				if(StringUtils.isNotBlank(payNote)) {
					payNote = payNote.substring(0, payNote.length() - 1) + ";";
				}
				
				if(routeIncome <= 0 && routePay <= 0) {
					fg = U.setPutFalse(map, "[现收项与开支项]金额不能均为0");
				}
			}
			
			if(fg) {
				// 判断当前订单是否有未审核的行程收支记录，如果有，则修改该条行程收支
				for (RouteTradeList rtl : rtls) {
					if(rtl.getIsCheck() == 0) {
						fg = U.setPutFalse(map, "您有行程收支未审核，暂不能新添加");
						break;
					}
				}
			}
			
			
			if(fg){
				// 存在现收，则添加现收记录至用户交易记录
				WalletList wl = null;
				if(routeIncome > 0) {
					// 获取已有记录
					hql = "from WalletList where cName = ?0 and assist = ?1 and type = ?2 and atype = ?3 and status = ?4";
					wl = walletListDao.findObj(hql, luname, orderNum, 27, 1, 0);
					if(wl == null) {
						wl = new WalletList();
						wl.setcName(luname);
						wl.setAssist(orderNum);
						wl.setType(27);
						wl.setAtype(1);
						wl.setAmoney(routeIncome);
						wl.setStatus(0);
						wl.setCashBalance(0);
						wl.setNote(incomeNote);
						wl.setAtime(new Date());
						wl.setWdcNote(_groupCash +","+ _groupRebate +","+ _routeRebate);
						walletListDao.save(wl);
						U.log(log, "保存-驾驶员现收-完成");
					}else {
						wl.setAmoney(routeIncome);
						wl.setNote(incomeNote);
						wl.setAtime(new Date());
						wl.setWdcNote(_groupCash +","+ _groupRebate +","+ _routeRebate);
						walletListDao.update(wl);
						U.log(log, "更新-驾驶员现收-完成");
					}
				}
				
				// 存在团上支出，则添加行程收支记录
				RouteTradeList rtl = null;
				if(routePay > 0) {
					rtl = new RouteTradeList();
					rtl.setOrderNum(orderNum);
					rtl.setPlateNum(plateNum);
					rtl.setDriverInfo(lbuser);
					rtl.setSingleFee(_singleFee);
					rtl.setWashingFee(_washingFee);
					rtl.setParkingFee(_parkingFee);
					rtl.setRoadFee(_roadFee);
					rtl.setLivingFee(_livingFee);
					rtl.setOtherFee(_otherFee);
					rtl.setWaterFee(_waterFee);
					rtl.setStayFee(_stayFee);
					rtl.setRemark(remark);
					rtl.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
					rtl.setReqsrc(reqsrc);
					rtl.setIsCheck(0);
					rtl.setAddTime(new Date());
					routeTradeListDao.save(rtl);
					U.log(log, "添加-行程收支-完成");
					
					// 更新订单对应行程收支
					rtls.add(rtl);
					order.setTrades(rtls);
					carOrderDao.update(order);
					U.log(log, "更新订单对应行程收支-完成");
				}
				
				// 添加员工记账记录
				StaffReimburse sr = new StaffReimburse();
				sr.setUnitNum(lunitNum);
				sr.setReimUser(lbuser);
				sr.setDeptId(driver.getDeptId());
				sr.setRemark(rtl == null ? remark : rtl.getRemark());
				sr.setPayMoney(routePay);
				sr.setIsCheck(0);
				sr.setJzType(JzType.XCSZ);
				sr.setCarOrderReim(order);
				sr.setOrderTrade(rtl == null ? null : rtl);
				sr.setMainOrderReim(mainOrder);
//				sr.setDat(rtl == null ? null : lunitNum+"="+luname+"="+rtl.getId());
				sr.setReqsrc(reqsrc);
				sr.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
				sr.setAddTime(new Date());
				staffReimburseDao.save(sr);
				U.log(log, "添加-员工报账记录-完成");
				
				// 前端需要此id，保存图片
				map.put("uid", sr.getId());
				
				fg = U.setPutFalse(map, 1, "添加-行程收支-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> updXcjz(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String luname, String uid, String flen, String groupCash, String groupRebate,
		String routeRebate, String singleFee, String washingFee, String parkingFee, String roadFee,
		String livingFee, String otherFee, String waterFee, String stayFee, String remark) {
		String logtxt = U.log(log, "修改-行程收支记录", reqsrc);
		
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
			
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(uid)) {
					fg = U.setPutFalse(map, "[员工记账id]不能为空");
				}else {
					uid = uid.trim();
					sr = staffReimburseDao.findByField("id", Long.parseLong(uid));
					if(sr == null) {
						fg = U.setPutFalse(map, "该[记账记录]不存在");
					}
					
					U.log(log, "[员工记账id] uid="+uid);
				}
			}
			
			List<RouteTradeList> rtls = new ArrayList<RouteTradeList>();
			CarOrder order = null;
			String plateNum = null;
			if(fg){
				order = sr.getCarOrderReim();
				rtls = order.getTrades();
				plateNum = order.getDisCar().getPlateNum();
				
				U.log(log, "当前派车订单编号：orderNum="+order.getOrderNum());
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
			
			double _groupCash = 0d;
			if(fg){
				if(StringUtils.isEmpty(groupCash)){
					U.log(log, "[团上现收]为空");
				}else{
					groupCash = groupCash.trim();
					if(!FV.isDouble(groupCash)){
						fg = U.setPutFalse(map, "[团上现收]格式错误，应为正数");
					}else{
						_groupCash = Double.parseDouble(groupCash);
					}
					
					U.log(log, "[团上现收] groupCash="+groupCash);
				}
			}
			
			double _groupRebate = 0d;
			if(fg){
				if(StringUtils.isEmpty(groupRebate)){
					U.log(log, "[团上返点]为空");
				}else{
					groupRebate = groupRebate.trim();
					if(!FV.isDouble(groupRebate)){
						fg = U.setPutFalse(map, "[团上返点]格式错误，应为正数");
					}else{
						_groupRebate = Double.parseDouble(groupRebate);
					}
					
					U.log(log, "[团上返点] groupRebate="+groupRebate);
				}
			}
			
			double _routeRebate = 0d;
			if(fg){
				if(StringUtils.isEmpty(routeRebate)){
					U.log(log, "[行程加点]为空");
				}else{
					routeRebate = routeRebate.trim();
					if(!FV.isDouble(routeRebate)){
						fg = U.setPutFalse(map, "[行程加点]格式错误，应为正数");
					}else{
						_routeRebate = Double.parseDouble(routeRebate);
					}
					
					U.log(log, "[行程加点] routeRebate="+routeRebate);
				}
			}
			
			double _singleFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(singleFee)){
					U.log(log, "[单据费]为空");
				}else{
					singleFee = singleFee.trim();
					if(!FV.isDouble(singleFee)){
						fg = U.setPutFalse(map, "[单据费]格式错误，应为正数");
					}else{
						_singleFee = Double.parseDouble(singleFee);
					}
					
					U.log(log, "[单据费] singleFee="+singleFee);
				}
			}
			
			double _washingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(washingFee)){
					U.log(log, "[洗车费]为空");
				}else{
					washingFee = washingFee.trim();
					if(!FV.isDouble(washingFee)){
						fg = U.setPutFalse(map, "[洗车费]格式错误，应为正数");
					}else{
						_washingFee = Double.parseDouble(washingFee);
					}
					
					U.log(log, "[洗车费] washingFee="+washingFee);
				}
			}
			
			double _parkingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(parkingFee)){
					U.log(log, "[停车费]为空");
				}else{
					parkingFee = parkingFee.trim();
					if(!FV.isDouble(parkingFee)){
						fg = U.setPutFalse(map, "[停车费]格式错误，应为正数");
					}else{
						_parkingFee = Double.parseDouble(parkingFee);
					}
					
					U.log(log, "[停车费] parkingFee="+parkingFee);
				}
			}
			
			double _roadFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(roadFee)){
					U.log(log, "[过路费]为空");
				}else{
					roadFee = roadFee.trim();
					if(!FV.isDouble(roadFee)){
						fg = U.setPutFalse(map, "[过路费]格式错误，应为正数");
					}else{
						_roadFee = Double.parseDouble(roadFee);
					}
					
					U.log(log, "[过路费] roadFee="+roadFee);
				}
			}
			
			double _livingFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(livingFee)){
					U.log(log, "[生活费]为空");
				}else{
					livingFee = livingFee.trim();
					if(!FV.isDouble(livingFee)){
						fg = U.setPutFalse(map, "[生活费]格式错误，应为正数");
					}else{
						_livingFee = Double.parseDouble(livingFee);
					}
					
					U.log(log, "[生活费] livingFee="+livingFee);
				}
			}
			
			double _otherFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(otherFee)){
					U.log(log, "[其他费用]为空");
				}else{
					otherFee = otherFee.trim();
					if(!FV.isDouble(otherFee)){
						fg = U.setPutFalse(map, "[其他费用]格式错误，应为正数");
					}else{
						_otherFee = Double.parseDouble(otherFee);
					}
					
					U.log(log, "[其他费用] otherFee="+otherFee);
				}
			}
			
			double _waterFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(waterFee)){
					U.log(log, "[买水费用]为空");
				}else{
					waterFee = waterFee.trim();
					if(!FV.isDouble(waterFee)){
						fg = U.setPutFalse(map, "[买水费用]格式错误，应为正数");
					}else{
						_waterFee = Double.parseDouble(waterFee);
					}
					
					U.log(log, "[买水费用] waterFee="+_waterFee);
				}
			}
			
			double _stayFee = 0d;
			if(fg){
				if(StringUtils.isEmpty(stayFee)){
					U.log(log, "[住宿费用]为空");
				}else{
					stayFee = stayFee.trim();
					if(!FV.isDouble(stayFee)){
						fg = U.setPutFalse(map, "[住宿费用]格式错误，应为正数");
					}else{
						_stayFee = Double.parseDouble(stayFee);
					}
					
					U.log(log, "[住宿费用] waterFee="+_stayFee);
				}
			}
			if(fg){
				if(StringUtils.isEmpty(remark)){
					U.log(log, "[备注]为空");
				}else{
					remark = remark.trim();
					if(remark.length() > 40){
						fg = U.setPutFalse(map, "[备注]最多填写40个字");
					}
					
					U.log(log, "[备注] remark="+remark);
				}
			}
			
			// 团上收入, 团上支出
			double routeIncome = 0d, routePay = 0d;
			// 团上现收记录, 团上支出记录
			String incomeNote = "", payNote = "";
			if(fg) {
				U.log(log, "验证：现收项或者支出项必须有一个有金额");
				
				routeIncome = _groupCash + _groupRebate + _routeRebate;
				U.log(log, "收入金额："+routeIncome);
				if(_groupCash > 0) {
					incomeNote += "团上现收:"+_groupCash+"元,";
				}
				if(_groupRebate > 0) {
					incomeNote += "团上返点:"+_groupRebate+"元,";
				}
				if(_routeRebate > 0) {
					incomeNote += "行程加点:"+_routeRebate+"元,";
				}
				if(StringUtils.isNotBlank(incomeNote)) {
					incomeNote = incomeNote.substring(0, incomeNote.length() - 1) + ";";
				}
				
				routePay = _singleFee + _washingFee + _parkingFee + _roadFee + _livingFee + _otherFee + _waterFee + _stayFee;
				U.log(log, "支出金额："+routePay);
				
				if(_singleFee > 0) {
					payNote += "打单费:"+_singleFee+"元,";
				}
				if(_washingFee > 0) {
					payNote += "洗车费:"+_washingFee+"元,";
				}
				if(_parkingFee > 0) {
					payNote += "停车费:"+_parkingFee+"元,";
				}
				if(_roadFee > 0) {
					payNote += "过路费:"+_roadFee+"元,";
				}
				if(_livingFee > 0) {
					payNote += "生活费:"+_livingFee+"元,";
				}
				if(_otherFee > 0) {
					payNote += "其他费:"+_otherFee+"元,";
				}
				if(_waterFee > 0) {
					payNote += "买水费:"+_waterFee+"元,";
				}
				if(_stayFee > 0) {
					payNote += "住宿费:"+_stayFee+"元,";
				}
				if(StringUtils.isNotBlank(payNote)) {
					payNote = payNote.substring(0, payNote.length() - 1) + ";";
				}
				
				if(routeIncome <= 0 && routePay <= 0) {
					fg = U.setPutFalse(map, "[现收项与开支项]金额不能均为0");
				}
			}
			
			if(fg){
				// 存在现收，则添加现收记录至用户交易记录
				WalletList wl = null;
				if(routeIncome > 0) {
					// 获取已有记录
					hql = "from WalletList where cName = ?0 and assist = ?1 and type = ?2 and atype = ?3 and status = ?4";
					wl = walletListDao.findObj(hql, luname, order.getOrderNum(), 27, 1, 0);
					if(wl == null) {
						wl = new WalletList();
						wl.setcName(luname);
						wl.setAssist(order.getOrderNum());
						wl.setType(27);
						wl.setAtype(1);
						wl.setAmoney(routeIncome);
						wl.setStatus(0);
						wl.setCashBalance(0);
						wl.setNote(incomeNote);
						wl.setAtime(new Date());
						wl.setWdcNote(_groupCash +","+ _groupRebate +","+ _routeRebate);
						walletListDao.save(wl);
						U.log(log, "保存-驾驶员现收-完成");
					}else {
						wl.setAmoney(routeIncome);
						wl.setNote(incomeNote);
						wl.setAtime(new Date());
						wl.setWdcNote(_groupCash +","+ _groupRebate +","+ _routeRebate);
						walletListDao.update(wl);
						U.log(log, "更新-驾驶员现收-完成");
					}
				}
				
				// 存在团上支出，则添加行程收支记录
				RouteTradeList rtl = sr.getOrderTrade();
				if(rtl == null) {
					if(routePay > 0) {
						rtl = new RouteTradeList();
						rtl.setOrderNum(order.getOrderNum());
						rtl.setPlateNum(plateNum);
						rtl.setDriverInfo(lbuser);
						rtl.setSingleFee(_singleFee);
						rtl.setWashingFee(_washingFee);
						rtl.setParkingFee(_parkingFee);
						rtl.setRoadFee(_roadFee);
						rtl.setLivingFee(_livingFee);
						rtl.setOtherFee(_otherFee);
						rtl.setWaterFee(_waterFee);
						rtl.setStayFee(_stayFee);
						rtl.setRemark(remark);
						rtl.setOperNote(Util.getOperInfo(lbuser.getRealName(), "添加"));
						rtl.setReqsrc(reqsrc);
						rtl.setIsCheck(0);
						rtl.setAddTime(new Date());
						routeTradeListDao.save(rtl);
						U.log(log, "添加-行程收支-完成");
						
						// 更新订单对应行程收支
						rtls.add(rtl);
						order.setTrades(rtls);
						carOrderDao.update(order);
						U.log(log, "更新订单对应行程收支-完成");
					}
				}else {
					rtl.setSingleFee(_singleFee);
					rtl.setWashingFee(_washingFee);
					rtl.setParkingFee(_parkingFee);
					rtl.setRoadFee(_roadFee);
					rtl.setLivingFee(_livingFee);
					rtl.setOtherFee(_otherFee);
					rtl.setWaterFee(_waterFee);
					rtl.setStayFee(_stayFee);
					rtl.setRemark(remark);
					rtl.setOperNote(rtl.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
					rtl.setIsCheck(0);
					rtl.setAddTime(new Date());
					routeTradeListDao.update(rtl);
					U.log(log, "修改-行程收支-完成");
				}
				
				// 添加员工记账记录
				sr.setRemark(rtl == null ? remark : rtl.getRemark());
				sr.setGathMoney(routeIncome);
				sr.setPayMoney(routePay);
				sr.setIsCheck(0);
				sr.setOperNote(sr.getOperNote()+Util.getOperInfo(lbuser.getRealName(), "修改"));
				sr.setAddTime(new Date());
//				sr.setDat(rtl == null ? null : lunitNum+"="+luname+"="+rtl.getId());
				staffReimburseDao.update(sr);
				U.log(log, "修改-员工报账记录-完成");
				
				// 前端需要此id，保存图片
				map.put("uid", sr.getId());
				
				fg = U.setPutFalse(map, 1, "修改-行程收支-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findXcjzDetail(ReqSrc reqsrc, String lunitNum, String luname, String id) {
		String logtxt = U.log(log, "获取-行程记账-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			StaffReimburse sr = null;
			if(fg) {
				if(StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[员工记账id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[员工记账id]格式错误");
					}else {
						sr = staffReimburseDao.findByField("id", Long.parseLong(id));
						if(sr == null) {
							fg = U.setPutFalse(map, "此[员工记账]不存在");
						}
					}
					
					U.log(log, "[员工记账id] id="+id);
				}
			}
			
			if(fg) {
				// 获取-行程现收
				double groupCash = 0d, groupRebate = 0d, routeRebate = 0d;
				hql = "from WalletList where cName = ?0 and assist = ?1 and type = ?2 and atype = ?3 and status = ?4";
				WalletList wl = walletListDao.findObj(hql, luname, sr.getCarOrderReim().getOrderNum(), 27, 1, 0);
				if(wl != null) {
					U.log(log, "存在行程现收数据");
					String incomMoney[] = wl.getWdcNote().split(",");
					
					groupCash = Double.parseDouble(incomMoney[0]);
					groupRebate = Double.parseDouble(incomMoney[1]);
					routeRebate = Double.parseDouble(incomMoney[2]);
				}
				
				// 获取-行程开支
				double singleFee = 0d, washingFee = 0d, parkingFee = 0d, roadFee = 0d, 
					livingFee = 0d, otherFee = 0d, waterFee = 0d, stayFee = 0d;
				String imgUrls = "", remark = "";
				RouteTradeList rtl = sr.getOrderTrade();
				if(rtl != null) {
					U.log(log, "存在行程收支数据");
					
					singleFee = rtl.getSingleFee();
					washingFee = rtl.getWashingFee();
					parkingFee = rtl.getParkingFee();
					roadFee = rtl.getRoadFee();
					livingFee = rtl.getLivingFee();
					otherFee = rtl.getOtherFee();
					waterFee = rtl.getWaterFee();
					stayFee = rtl.getStayFee();
					imgUrls = rtl.getRouteVoucherUrl();
					remark = rtl.getRemark();
				}
				
				// 处理结果
				Map<String, Object> r = new HashMap<String, Object>();
				r.put("id", sr.getId());
				r.put("orderNum", sr.getCarOrderReim().getOrderNum());
				r.put("isCheck", sr.getIsCheck());
				/*********行程现收**************/
				r.put("groupCash", groupCash);
				r.put("groupRebate", groupRebate);
				r.put("routeRebate", routeRebate);
				/*********行程开支**************/
				r.put("singleFee", singleFee);
				r.put("washingFee", washingFee);
				r.put("parkingFee", parkingFee);
				r.put("roadFee", roadFee);
				r.put("livingFee", livingFee);
				r.put("otherFee", otherFee);
				r.put("waterFee", waterFee);
				r.put("stayFee", stayFee);
				r.put("routeVoucherUrl", imgUrls);
				r.put("remark", remark);
				
				map.put("data", r);
				
				U.setPut(map, 1, "获取数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
