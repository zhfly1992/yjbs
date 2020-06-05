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

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.CommonDao;
import com.fx.dao.order.CompanyOrderTempDao;
import com.fx.entity.order.CompanyOrderTemp;
import com.fx.service.order.CompanyOrderTempService;

@Service
@Transactional
public class CompanyOrderTempServiceImpl extends BaseServiceImpl<CompanyOrderTemp, Long> implements CompanyOrderTempService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 单位-临时订单-数据源 */
	@Autowired
	private CompanyOrderTempDao cotDao;
	@Override
	public ZBaseDaoImpl<CompanyOrderTemp, Long> getDao() {
		return cotDao;
	}
	/** 公共-服务 */
	@Autowired
	private CommonDao commonDao;
	
	
	@Override
	public Map<String, Object> addDayRouteTemp(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			JSONObject jsonObject,String unitNum) {
		String logtxt = U.log(log, "添加-天数行程临时数据");
		String curDay=jsonObject.getString("curDay");//当前行程数
		String ywType=jsonObject.getString("ywType");//游玩类型
		String stime=jsonObject.getString("stime");//出发时间
		String etime=jsonObject.getString("etime");//到达时间
		String spoint=jsonObject.getString("spoint");//出发地点
		String epoint=jsonObject.getString("epoint");//到达地点
		String wpoints=jsonObject.getString("wpoints");//途径地点
		String isHighSpeed=jsonObject.getString("isHighSpeed");//是否走高速
		String otherPrice=jsonObject.getString("otherPrice");//其他费用
		String otherPriceNote=jsonObject.getString("otherPriceNote");//其他费用说明
		String routePrice=jsonObject.getString("routePrice");//行程价格
		String remindRouteCash=jsonObject.getString("remindRouteCash");//提醒师傅现收
		String limitNum=jsonObject.getString("limitNum");//限号
		String seats=jsonObject.getString("seats");//座位数
		String cars=jsonObject.getString("cars");//车辆数
		String note=jsonObject.getString("note");//备注
		String routeDetail=jsonObject.getString("routeDetail");//行程详情
		String routeLink=jsonObject.getString("routeLink");//乘车联系人
		String mainOrderNum=jsonObject.getString("mainOrderNum");//主订单编号 
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			
			int _curDay = 0;
			if(fg) {
				if(StringUtils.isEmpty(curDay)) {
					fg = U.setPutFalse(map, "[当前行程数]不能为空");
				}else {
					curDay = curDay.trim();
					if(!FV.isPosInteger(curDay)) {
						fg = U.setPutFalse(map, "[当前行程数]格式错误");
					}else {
						_curDay = Integer.parseInt(curDay);
					}
					
					U.log(log, "[当前行程数] curDay="+curDay);
				}
			}
			
			int _ywType = 0;
			if(fg) {
				if(StringUtils.isEmpty(ywType)) {
					fg = U.setPutFalse(map, "[游玩类型]不能为空");
				}else {
					ywType = ywType.trim();
					if(!FV.isPosInteger(ywType)) {
						fg = U.setPutFalse(map, "[游玩类型]格式错误");
					}else {
						_ywType = Integer.parseInt(ywType);
					}
					
					U.log(log, "[游玩类型] ywType="+ywType);
				}
			}
			
			Date _stime = null;
			if(fg) {
				if(StringUtils.isEmpty(stime)) {
					fg = U.setPutFalse(map, "[出发时间]不能为空");
				}else {
					stime = stime.trim();
					if(!FV.isDate(stime)) {
						fg = U.setPutFalse(map, "[出发时间]格式错误");
					}else {
						_stime = DateUtils.strToDate(stime);
					}
					
					U.log(log, "[出发时间] stime="+stime);
				}
			}
			
			Date _etime = null;
			if(fg) {
				if(StringUtils.isEmpty(etime)) {
					fg = U.setPutFalse(map, "[到达时间]不能为空");
				}else {
					etime = etime.trim();
					if(!FV.isDate(etime)) {
						fg = U.setPutFalse(map, "[到达时间]格式错误");
					}else {
						_etime = DateUtils.strToDate(etime);
						if(_stime.getTime() > _etime.getTime()) {
							fg = U.setPutFalse(map, "[出发时间]不能在[到达时间]之后");
						}
					}
					
					U.log(log, "[到达时间] etime="+etime);
				}
			}
			
			// 保存地点 省市县
			List<String> cs = new ArrayList<String>();
			
			String slnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(spoint)) {
					fg = U.setPutFalse(map, "[出发地点]不能为空");
				}else {
					spoint = spoint.trim();
					if(spoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[出发地点]格式错误");
					}else {
						slnglat = spoint.split("=")[1];
						cs.add(spoint.split("=")[2]);
						
						U.log(log, "[出发地点坐标] slnglat="+slnglat);
					}
					
					U.log(log, "[出发地点] spoint="+spoint);
				}
			}
			
			String elnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(epoint)) {
					fg = U.setPutFalse(map, "[到达地点]不能为空");
				}else {
					epoint = epoint.trim();
					if(epoint.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[到达地点]格式错误");
					}else {
						elnglat = epoint.split("=")[1];
						cs.add(epoint.split("=")[2]);
						
						U.log(log, "[到达地点坐标] elnglat="+elnglat);
					}
					
					U.log(log, "[到达地点] epoint="+epoint);
				}
			}
			
			String wlnglat = "";
			if(fg) {
				if(StringUtils.isEmpty(wpoints)) {
					U.log(log, "[途径地点]为空");
				}else {
					wpoints = wpoints.trim();
					if(wpoints.indexOf("=") == -1) {
						fg = U.setPutFalse(map, "[途径地点]格式错误");
					}else {
						String[] wps = wpoints.split(";");
						for(int i = 0; i < wps.length; i++) {
							wlnglat += wps[i].split("=")[1]+";";
							cs.add(wps[i].split("=")[2]);
						}
						
						U.log(log, "[途径地点坐标] wlnglat="+wlnglat);
					}
					
					U.log(log, "[途径地点] wpoints="+wpoints);
				}
			}
			
			ServiceType serviceType = null;
			if(fg) {
				serviceType = Util.getRouteServiceType(cs);
				
				U.log(log, "[行程业务类型] serviceType="+serviceType);
			}
			
			String puCounty = "";
			if(fg) {
				puCounty = spoint.split("=")[2].split("-")[2];
				
				U.log(log, "[订单发布区/县] puCounty="+puCounty);
			}
			
			int _isHighSpeed = 0;
			if(fg) {
				if(StringUtils.isEmpty(isHighSpeed)) {
					U.log(log, "[是否走高速]为空");
				}else {
					isHighSpeed = isHighSpeed.trim();
					if(!FV.isInteger(isHighSpeed)) {
						fg = U.setPutFalse(map, "[是否走高速]格式错误");
					}else {
						_isHighSpeed = Integer.parseInt(isHighSpeed);
						// 不为1，则表示不走高速
						if(_isHighSpeed != 1) _isHighSpeed = 0;
					}
					
					U.log(log, "[是否走高速] isHighSpeed="+isHighSpeed);
				}
			}
			if(fg){
				if(StringUtils.isEmpty(routePrice)){
					fg = U.setPutFalse(map, "[行程价格]不能为空");
				}else{
					routePrice = routePrice.trim();
					if(!FV.isDouble(routePrice)){
						fg = U.setPutFalse(map, "[行程价格]格式错误");
					}
					
					U.log(log, "[行程价格] routePrice="+routePrice);
				}
			}
			
			if(fg){
				if(StringUtils.isNotBlank(otherPrice)){
					routePrice = routePrice.trim();
					if(!FV.isDouble(otherPrice)){
						fg = U.setPutFalse(map, "[其他费用]格式错误");
					}
				}
				U.log(log, "[其他费用] otherPrice="+otherPrice);
			}
			
			if(fg){
				if(StringUtils.isNotBlank(remindRouteCash)){
					remindRouteCash = remindRouteCash.trim();
					if(!FV.isDouble(remindRouteCash)){
						fg = U.setPutFalse(map, "[师傅现收金额]格式错误");
					}
				}
				U.log(log, "[师傅现收] remindRouteCash="+remindRouteCash);
			}
			
			if(fg) {
				if(StringUtils.isEmpty(seats)) {
					fg = U.setPutFalse(map, "[座位数]不能为空");
				}else {
					seats = seats.trim();
					if(!FV.isInteger(seats)) {
						fg = U.setPutFalse(map, "[座位数]格式错误");
					}
					
					U.log(log, "[座位数] seats="+seats);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(cars)) {
					fg = U.setPutFalse(map, "[车辆数]不能为空");
				}else {
					cars = cars.trim();
					if(!FV.isInteger(cars)) {
						fg = U.setPutFalse(map, "[车辆数]格式错误");
					}
					
					U.log(log, "[车辆数] cars="+cars);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(routeLink)){
					fg = U.setPutFalse(map, "[乘车联系人]不能为空");
				}else{
					routeLink = routeLink.trim();
					if(!FV.isPhone(routeLink.split("-")[1])){
						fg = U.setPutFalse(map, "[联系人手机号]应为11位手机号");
					}
					
					U.log(log, "[乘车联系人] routeLink="+routeLink);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(routeDetail)){
					U.log(log, "[订单备注]为空");
				}else{
					routeDetail = routeDetail.trim();
					if(routeDetail.length() > 200){
						fg = U.setPutFalse(map, "[行程详情]最多填写200个字");
					}
					
					U.log(log, "[行程详情] routeDetail="+routeDetail);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(note)){
					U.log(log, "[订单备注]为空");
				}else{
					note = note.trim();
					if(note.length() > 100){
						fg = U.setPutFalse(map, "[[订单备注]最多填写100个字");
					}
					
					U.log(log, "[[订单备注] note="+note);
				}
			}
			
			CarRouteRes mp = null;
			if(fg){
				mp = commonDao.queryCarRouteRes(slnglat, elnglat, wlnglat, (_isHighSpeed == 1 ? "19" : "10"));
				if(mp == null){
					fg = U.setPutFalse(map, "获取行程距离失败，请重新获取");
				}
			}
			if(fg) {
				if(StringUtils.isBlank(mainOrderNum))mainOrderNum=UT.creatMainOrderNum(2);
				CompanyOrderTemp cot = null;
				hql = "from CompanyOrderTemp where unitNum = ?0 and routeNo = ?1 and mainOrderNum = ?2";
				cot = cotDao.findObj(hql, unitNum, _curDay, mainOrderNum);
				if(cot == null) {
					cot = new CompanyOrderTemp();
					cot.setUnitNum(unitNum);
					cot.setMainOrderNum(mainOrderNum);
					cot.setRouteNo(_curDay);
					cot.setServiceType(serviceType);
					cot.setYwType(_ywType);
					cot.setStime(_stime);
					cot.setEtime(_etime);
					cot.setSpoint(spoint);
					cot.setEpoint(epoint);
					if(StringUtils.isNotBlank(wpoints))cot.setWpoints(wpoints);
					cot.setWayCity(mp.getWayCity());
					cot.setPuCounty(puCounty);
					cot.setIsHighSpeed(_isHighSpeed);
					cot.setDistance(MathUtils.div(mp.getDistance(), 1000, 2));
					cot.setRouteTime(MathUtils.div(mp.getTimeCons(), 60, 2));
					cot.setRouteLink(routeLink);
					cot.setSeats(Integer.parseInt(seats));
					cot.setNeedCars(Integer.parseInt(cars));
					cot.setRoutePrice(Double.valueOf(routePrice));
					if(StringUtils.isNotBlank(otherPrice))cot.setOtherPrice(Double.valueOf(otherPrice));
					if(StringUtils.isNotBlank(otherPriceNote))cot.setOtherPriceNote(otherPriceNote);
					if(StringUtils.isNotBlank(remindRouteCash))cot.setRemindRouteCash(Double.valueOf(remindRouteCash));
					if(StringUtils.isNotBlank(limitNum))cot.setLimitNum(limitNum);
					if(StringUtils.isNotBlank(routeDetail))cot.setRouteDetail(routeDetail);
					if(StringUtils.isNotBlank(note))cot.setNote(note);
					cotDao.save(cot);
					
					map.put("mainOrderNum", mainOrderNum);
					
					U.setPut(map, 1, "添加成功");
				}else {
					cot.setServiceType(serviceType);
					cot.setYwType(_ywType);
					cot.setStime(_stime);
					cot.setEtime(_etime);
					cot.setSpoint(spoint);
					cot.setEpoint(epoint);
					cot.setWpoints(wpoints);
					cot.setWayCity(mp.getWayCity());
					cot.setPuCounty(puCounty);
					cot.setIsHighSpeed(_isHighSpeed);
					cot.setDistance(MathUtils.div(mp.getDistance(), 1000, 2));
					cot.setRouteTime(MathUtils.div(mp.getTimeCons(), 60, 2));
					cot.setRouteLink(routeLink);
					cot.setSeats(Integer.parseInt(seats));
					cot.setNeedCars(Integer.parseInt(cars));
					cot.setRoutePrice(Double.valueOf(routePrice));
					if(StringUtils.isNotBlank(otherPrice))cot.setOtherPrice(Double.valueOf(otherPrice));
					if(StringUtils.isNotBlank(otherPriceNote))cot.setOtherPriceNote(otherPriceNote);
					if(StringUtils.isNotBlank(remindRouteCash))cot.setRemindRouteCash(Double.valueOf(remindRouteCash));
					if(StringUtils.isNotBlank(limitNum))cot.setLimitNum(limitNum);
					if(StringUtils.isNotBlank(routeDetail))cot.setRouteDetail(routeDetail);
					if(StringUtils.isNotBlank(note))cot.setNote(note);
					cotDao.update(cot);
					
					map.put("mainOrderNum", mainOrderNum);
					U.setPut(map, 1, "修改成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
