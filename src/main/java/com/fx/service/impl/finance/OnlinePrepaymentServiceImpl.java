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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.OnlinePrepaymentDao;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.finance.OnlinePrepayment;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.finance.TeamAccountBook;
import com.fx.service.company.CompanyVehicleService;
import com.fx.service.company.FileService;
import com.fx.service.finance.OnlinePrepaymentService;
import com.fx.service.finance.ReimburseListService;
import com.fx.service.order.CarOrderService;

@Service
@Transactional
public class OnlinePrepaymentServiceImpl extends BaseServiceImpl<OnlinePrepayment,Long> implements OnlinePrepaymentService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private OnlinePrepaymentDao opmDao;
	@Autowired
	private CompanyVehicleService carSer;
	@Autowired
	private ReimburseListService reimSer;
	@Autowired
	private CarOrderService coSer;
	/** 文件管理-服务 */
	@Autowired
	private FileService fmSer;
	@Override
	public ZBaseDaoImpl<OnlinePrepayment, Long> getDao() {
		return opmDao;
	}
	@Override
	public Page<OnlinePrepayment> findOpmList(Page<OnlinePrepayment> pageData,String teamNo, String find,
			String moneyType,String timeType,String sTime, String eTime,String plateNum) {
		////////////////////////排序设置-s///////////////////
		Compositor compositor = new Compositor("id", CompositorType.DESC);
		pageData.setCompositor(compositor);
		////////////////////////排序设置-e///////////////////
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		filtrations.add(new Filtration(MatchType.EQ, teamNo, "teamNo"));//车队编号
		if(StringUtils.isNotEmpty(find)){
			filtrations.add(new Filtration(MatchType.LIKE, find, "company","orderNum"));//车公司，订单号
		}
		if(StringUtils.isNotEmpty(plateNum)){
			filtrations.add(new Filtration(MatchType.LIKE, plateNum, "plateNum"));//车牌号
		}
		if(StringUtils.isNotEmpty(moneyType)){
			if(moneyType.equals("1")){//应收
				//总价大于已收
				filtrations.add(new Filtration(MatchType.SQL, "", " (total_price > gath_price) "));
			}else if(moneyType.equals("2")){//已收
				//已收大于0
				filtrations.add(new Filtration(MatchType.SQL, "", " (gath_price>0) "));
			}
		}else{
			filtrations.add(new Filtration(MatchType.SQL, "", " ((total_price > gath_price) or gath_price>0) "));
		}
		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			if(timeType.equals("0")){//添加时间
				filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
				filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
			}else{
				filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"payTime"));
				filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"payTime"));
			}
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = opmDao.findPage(pageData);
		return pageData;
	}
	@Override
	public int operOpm(String opmId, OnlinePrepayment opm) {
		try {
			if(opm != null){
				if(StringUtils.isNotEmpty(opmId)){
					opmDao.update(opm);
				}else{
					opmDao.save(opm);
				}
			}else{
				OnlinePrepayment delAgt = opmDao.find(Long.parseLong(opmId));
				opmDao.delete(delAgt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	@Override
	public Map<String, Object> adupOnline(ReqSrc reqsrc,HttpServletRequest request, HttpServletResponse response,
			MultipartHttpServletRequest multReq, String teamNo,String orderNum, String payMoney, String payNote, String payTime,
			String payAccount, String isCash) {
		String logtxt = U.log(log, "上网预付费提交", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				/*JuniorCarOrder juniOrder = jcSer.findByField("orderNum", orderNum);
				if (juniOrder != null) {
					CarList car=null;
					double prepayRatioMoney = 0;//默认为0
					if(juniOrder.getOnlinePrepayPrice()>0){
						fg = U.setPutFalse(map, "该行程已付上网费");
					}
					if(fg){
						car = carSer.findByField("plateNum",juniOrder.getPlateNum());
						if (car!=null) {
							if(car.getTeamNo().equals(teamNo)){//本车队的车
								if(StringUtils.isEmpty(car.getCompanyBelong())){//没有车公司不能提交
									fg = U.setPutFalse(map, "本车队该车辆无车公司不能提交");
								}else{//20200110 税点单独做报销记录
									UnderLineTeam ult=ultSer.findByField("teamName", car.getCompanyBelong());
									if(ult!=null){
										prepayRatioMoney = MathUtils.mul(Double.valueOf(payMoney),ult.getPrepayRatio(), 2);
									}
								}
							}
						}
					}
					if(fg){
						String voucherInfo = fmSer.upFiles(multReq, response, "上网费凭证");
						if(StringUtils.isNotEmpty(payNote)){20200110 税点单独做报销记录
							payNote=payNote+ ",已扣税点：" + prepayRatioMoney + "元";
						}else{
							payNote="已扣税点：" + prepayRatioMoney + "元";
						}
						if ("0".equals(isCash)) {// 自网车方垫支
							if(car!=null && StringUtils.isNotEmpty(car.getCompanyBelong())){
								String hql ="from OnlinePrepayment where orderNum=? and plateNum=?";
								OnlinePrepayment opm = opmDao.findObj(hql, juniOrder.getOrderNum(),juniOrder.getPlateNum());
								if(opm==null){//添加
									opm = new OnlinePrepayment();
								}
								opm.setTeamNo(car.getTeamNo());
								opm.setCompany(car.getCompanyBelong());
								opm.setTotalPrice(MathUtils.sub(Double.valueOf(payMoney),prepayRatioMoney, 2));
								opm.setOrderNum(orderNum);
								if(StringUtils.isNotEmpty(payTime)){
									opm.setPayTime(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, payTime + ":00:00"));
								}
								opm.setAddTime(new Date());
								opm.setRemark(payNote);
								opm.setPrepayRatioPrice(prepayRatioMoney);
								opm.setPlateNum(juniOrder.getPlateNum());
								if(voucherInfo!=null){
									opm.setOnlineVoucherUrl(voucherInfo.split("-")[1]);
								}
								opm.setOnlineType(0);
								opmDao.save(opm);
								if(voucherInfo!=null){//上传了凭证 20200110确认不加税点利润账，是先收全款，再付税点
									//添加利润账记录
									TeamAccountBook tabPay = new TeamAccountBook();
									tabPay.setTeamNo(car.getTeamNo());
									tabPay.setRemark(payNote);
									tabPay.setOrderNum(opm.getId() + "");// 备注记录Id
									tabPay.setAccountName("上网费税点");
									tabPay.setCreditMoney(prepayRatioMoney);
									tabPay.setBalance(tabSer.findBalance(teamNo, "1",0, prepayRatioMoney,null));
									tabPay.setPlateNum(juniOrder.getPlateNum());
									tabPay.setAccountType(car.getSelfOwned()+1);// 自营
									tabPay.setAddTime(new Date());
									tabPay.setAccountTime(juniOrder.getUseDayStart());
									tabPay.setNote("操作员："+WebUtil.getLUser(request).getRealName());
									tabPay.setBookType(1);
									tabSer.save(tabPay);
								}
							}
							if(voucherInfo!=null){//上传了凭证,第二次上传就修改报销记录
								//添加报销记录 20190429 业务员先付款，添加报销记录,审核时不加利润账，只是一种垫支行为，不产生利润
								ReimburseList reim=new ReimburseList();
								reim.setTeamNo(WebUtil.getTeamNo(request));
								reim.setFeeType("上网费");
								reim.setFeeStatus(1);
								reim.setPlateNum(juniOrder.getPlateNum());
								reim.setcName(payAccount.split(",")[1]);
								reim.setReimName(payAccount.split(",")[0]);
								reim.setTotalMoney(Double.valueOf(payMoney));
								reim.setRemark(payNote);
								reim.setOperator(WebUtil.getLUser(request).getRealName());
								reim.setAddTime(new Date());
								reim.setUseDayStart(juniOrder.getUseDayStart());
								reim.setVieWay(juniOrder.getVieWay());
								reim.setOrderNum(juniOrder.getOrderNum());
								reimSer.save(reim);
							}
						} else {//旅网用车方付费
							if(car!=null && StringUtils.isNotEmpty(car.getCompanyBelong())){
								String hql ="from OnlinePrepayment where orderNum=? and plateNum=?";
								OnlinePrepayment opm = opmDao.findObj(hql, juniOrder.getOrderNum(),juniOrder.getPlateNum());
								if(opm==null){//添加
									opm = new OnlinePrepayment();
								}
								opm.setTeamNo(car.getTeamNo());
								opm.setCompany(car.getCompanyBelong());
								opm.setTotalPrice(MathUtils.sub(Double.valueOf(payMoney),prepayRatioMoney, 2));
								opm.setOrderNum(orderNum);
								if(StringUtils.isNotEmpty(payTime)){
									opm.setPayTime(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, payTime + ":00:00"));
								}
								opm.setAddTime(new Date());
								opm.setRemark(payNote);
								opm.setPrepayRatioPrice(prepayRatioMoney);
								opm.setPlateNum(juniOrder.getPlateNum());
								if(voucherInfo!=null){
									opm.setOnlineVoucherUrl(voucherInfo.split("-")[1]);
									
								}
								opm.setOnlineType(1);
								opmDao.save(opm);
								if(voucherInfo!=null){//上传了凭证 20200110确认不加税点利润账，是先收全款，再付税点
									//添加利润账记录
									TeamAccountBook tabPay = new TeamAccountBook();
									tabPay.setTeamNo(car.getTeamNo());
									tabPay.setRemark(payNote);
									tabPay.setOrderNum(opm.getId() + "");// 备注记录Id
									tabPay.setAccountName("上网费税点");
									tabPay.setCreditMoney(prepayRatioMoney);
									tabPay.setBalance(tabSer.findBalance(teamNo, "1",0, prepayRatioMoney,null));
									tabPay.setPlateNum(juniOrder.getPlateNum());
									tabPay.setAccountType(car.getSelfOwned()+1);// 自营
									tabPay.setAddTime(new Date());
									tabPay.setAccountTime(juniOrder.getUseDayStart());
									tabPay.setNote("操作员："+WebUtil.getLUser(request).getRealName());
									tabPay.setBookType(1);
									tabSer.save(tabPay);
								}
							}
							//旅网加主订单
							if(voucherInfo!=null){//上传了凭证
								CarOrderList col=coSer.findByField("orderNum", juniOrder.getMainOrderNum());//主订单
								col.setTravelPrepayPrice(MathUtils.add(col.getTravelPrepayPrice(),Double.valueOf(payMoney), 2));//旅网金额（因为要减掉用车方的应付款）
								col.setRemarkPos(col.getRemarkPos()+",上网预付费："+Double.valueOf(payMoney)+"元");
								coSer.update(col);
								if(StringUtils.isNotEmpty(col.getMainOrderNum())){
									CarOrderList mainCol=coSer.findByField("orderNum", col.getMainOrderNum());
									if(mainCol!=null){
										mainCol.setTravelPrepayPrice(MathUtils.add(mainCol.getTravelPrepayPrice(),Double.valueOf(payMoney), 2));//旅网金额
										mainCol.setRemarkPos(col.getRemarkPos()+","+juniOrder.getPlateNum()+"上网预付费："+Double.valueOf(payMoney)+"元");
										coSer.update(mainCol);
									}
								}
							}
						}
						//加入派车成功订单：没传凭证只加未确认，传了后要减掉未确认，然后加入已确认
						if(voucherInfo!=null){
							if(juniOrder.getNoconPrepayPrice()>0){//表示之前第一次未传凭证
								juniOrder.setNoconPrepayPrice(MathUtils.sub(juniOrder.getNoconPrepayPrice(),Double.valueOf(payMoney), 2));//减掉自网未确认
							}
							juniOrder.setOnlinePrepayPrice(MathUtils.add(juniOrder.getOnlinePrepayPrice(),Double.valueOf(payMoney), 2));//加上自网已确认
							juniOrder.setRemarkPos(juniOrder.getRemarkPos()+","+juniOrder.getPlateNum()+"上网预付费："+Double.valueOf(payMoney)+"元");
						}else{
							juniOrder.setNoconPrepayPrice(MathUtils.add(juniOrder.getNoconPrepayPrice(),Double.valueOf(payMoney), 2));//加到自网未确认
						}
						jcSer.update(juniOrder);
						U.setPut(map, 1, "操作成功");
					}
				}else{
					U.setPutFalse(map,0, "订单不存在");
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
	public Map<String, Object> delPrepay(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response, String did) {
		String logtxt = U.log(log, "上网预付费删除", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				/*OnlinePrepayment opm = opmDao.find(Long.valueOf(did));
				if(fg){
					opm = opmDao.find(Long.valueOf(did));
					if(opm==null){
						fg=U.setPutFalse(map,0, "记录不存在");
					}
				}
				if(fg){
					if(opm.getIsCheckOnline()!=0){
						fg=U.setPutFalse(map,0, "该记录已确认，不能删除");
					}
				}
				JuniorCarOrder juniOrder=null;
				if(fg){
					juniOrder = jcSer.findByField("orderNum", opm.getOrderNum());
					if(juniOrder==null){
						fg=U.setPutFalse(map,0, "上网费订单已不存在，不能删除");
					}
				}
				if(fg){
					if(!opm.getPlateNum().equals(juniOrder.getPlateNum())){
						fg=U.setPutFalse(map,0, "上网费车牌号和订单车牌号不相符，不能删除");
					}
				}
				if(fg){
					String remark=","+juniOrder.getPlateNum()+"上网预付费："+opm.getTotalPrice()+"元";
					if(StringUtils.isNotBlank(opm.getOnlineVoucherUrl())){//上传了凭证
						juniOrder.setOnlinePrepayPrice(MathUtils.sub(juniOrder.getOnlinePrepayPrice(),opm.getTotalPrice(), 2));//从已确认里面减去
						if(opm.getOnlineType()==1){//旅网要减主订单
							CarOrderList col=coSer.findByField("orderNum", juniOrder.getMainOrderNum());//主订单
							col.setTravelPrepayPrice(MathUtils.sub(col.getTravelPrepayPrice(),opm.getTotalPrice(), 2));//减掉旅网金额
							col.setRemarkPos(col.getRemarkPos().replace(remark, ""));
							coSer.update(col);
							if(StringUtils.isNotEmpty(col.getMainOrderNum())){
								CarOrderList mainCol=coSer.findByField("orderNum", col.getMainOrderNum());
								if(mainCol!=null){
									mainCol.setTravelPrepayPrice(MathUtils.sub(mainCol.getTravelPrepayPrice(),opm.getTotalPrice(), 2));//减掉旅网金额
									mainCol.setRemarkPos(mainCol.getRemarkPos().replace(remark, ""));
									coSer.update(mainCol);
								}
							}
						}else{//自网删除报销记录
							String hql ="from ReimburseList where orderNum=? and plateNum=? and feeType=?";
							ReimburseList reim=reimSer.findObj(hql, opm.getOrderNum(),opm.getPlateNum(),"上网费");
							if(reim!=null) reimSer.delete(reim);
						}
						String fmId=opm.getOnlineVoucherUrl().split("\\.")[0].split("_")[3];//图片id
						fmSer.delFile(Util.MFILEURL, Long.valueOf(fmId));//删除凭证图片
					}else{
						juniOrder.setNoconPrepayPrice(MathUtils.sub(juniOrder.getNoconPrepayPrice(),opm.getTotalPrice(), 2));//从未确认里面减去
					}
					juniOrder.setRemarkPos(juniOrder.getRemarkPos().replace(remark, ""));
					jcSer.update(juniOrder);
					opmDao.delete(opm);
					U.setPut(map, 1, "操作成功");
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
	public Map<String, Object> gathPrepaySub(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response,
			String ids, double money, String rootIn, String gathTime,
			String voucherNo, String isBzy) {
		String logtxt = U.log(log, "收上网预付费提交", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				/*if(fg){
					if(StringUtils.isEmpty(ids)){
						fg = U.setPutFalse(map, "[上网费id]不能为空");
					}else{
						ids = ids.trim();
						U.log(log, "上网费id="+ids);
					}
				}
				if(fg){
					String[] id = ids.split(",");
					String fault = "";// 记录收款异常
					OnlinePrepayment opm = null;
					double currMoney=0;
					String operMark="";
					for (int i = 0; i < id.length; i++) {
						opm = opmDao.find(Long.valueOf(id[i]));
						if (opm != null) {
							if(ids.contains(",")){
								currMoney=opm.getTotalPrice();
							}else{
								currMoney=money;
							}
							opm.setGathPrice(currMoney);
							opm.setIsCheckOnline(1);//20200110改为收款后确认
							opmDao.update(opm);
							//更新主订单车公司代收金额
							CarOrderList col=coSer.findByField("orderNum", opm.getOrderNum());
							if(col!=null){//说明是代收
								//col.setTravelPrepayPrice(MathUtils.add(col.getTravelPrepayPrice(), currMoney, 2));//加入旅网
								col.setCompanyPrice(MathUtils.sub(col.getCompanyPrice(), currMoney, 2));
								col.setRemarkPos(col.getRemarkPos()+",已收"+opm.getCompany()+"："+currMoney+"元");
								coSer.update(col);
								if(StringUtils.isNotEmpty(col.getMainOrderNum())){
									CarOrderList mainCol=coSer.findByField("orderNum", col.getMainOrderNum());
									if(mainCol!=null){
										//mainCol.setTravelPrepayPrice(MathUtils.add(mainCol.getTravelPrepayPrice(), currMoney, 2));
										mainCol.setCompanyPrice(MathUtils.sub(mainCol.getCompanyPrice(), currMoney, 2));
										mainCol.setRemarkPos(col.getRemarkPos()+",已收"+opm.getCompany()+"："+currMoney+"元");
										coSer.update(mainCol);
									}
								}
								operMark=DateUtils.getOrderNum(7);
								double accountMoney=MathUtils.add(currMoney, opm.getPrepayRatioPrice(), 2);
								ReimburseList reim=new ReimburseList();
								reim.setTeamNo(WebUtil.getTeamNo(request));
								reim.setFeeType("收车公司款");
								reim.setFeeStatus(0);
								reim.setPlateNum(col.getPlateNum());
								reim.setcName(WebUtil.getLName(request));
								reim.setReimName(WebUtil.getLUser(request).getRealName());//默认当前登录人：报账员
								reim.setTotalMoney(accountMoney);
								reim.setRemark(col.getRemarkPos());
								reim.setOperator(WebUtil.getLUser(request).getRealName());
								reim.setAddTime(new Date());
								reim.setUseDayStart(col.getUseDayStart());
								reim.setVieWay(col.getVieWay());
								reim.setCustomer(col.getRealName());
								reim.setOrderNum(col.getOrderNum());
								reim.setVoucherNumber(voucherNo);
								reim.setIsCheck(3);
								reim.setReqsrc(ReqSrc.PC_COMPANY);
								reim.setOperMark(operMark);
								reimSer.save(reim);
							}
						} else {
							fault+=","+id[i];
						}
					}
					if(StringUtils.isEmpty(fault)){
						U.setPut(map, 1, "全部记录收款成功");
					}else{
						U.setPut(map, 2, "操作成功，其中记录id为"+ fault.substring(1, fault.length())+ "的订单收款未成功，请检查或刷新页面后再试");
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
}
