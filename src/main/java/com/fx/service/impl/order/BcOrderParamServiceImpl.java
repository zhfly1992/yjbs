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
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.ServiceType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.CommonDao;
import com.fx.dao.order.BcOrderParamDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.order.BcOrderParam;
import com.fx.service.order.BcOrderParamService;

@Service
@Transactional
public class BcOrderParamServiceImpl extends BaseServiceImpl<BcOrderParam, Long> implements BcOrderParamService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private BcOrderParamDao bcOrderParamDao;
	@Override
	public ZBaseDaoImpl<BcOrderParam, Long> getDao() {
		return bcOrderParamDao;
	}
	/** 公共-服务 */
	@Autowired
	private CommonDao commonDao;
	
	
	@Override
	public Map<String, Object> addDayRouteDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String unitNum, BaseUser bu, String curDay, String ywType, String stime, String etime, String spoint, 
		String epoint, String wpoints, String isHighSpeed) {
		String logtxt = U.log(log, "添加-天数行程临时数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(unitNum)) {
					fg = U.setPutFalse(map, "[登录单位编号]不能为空");
				}else {
					unitNum = unitNum.trim();
					
					U.log(log, "[登录单位编号] unitNum="+unitNum);
				}
			}
			
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
					if(wpoints.indexOf(";") == -1) {
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
			
			CarRouteRes mp = null;
			if(fg){
				mp = commonDao.queryCarRouteRes(slnglat, elnglat, wlnglat, (_isHighSpeed == 1 ? "19" : "10"));
				if(mp == null){
					fg = U.setPutFalse(map, "获取行程距离失败，请重新获取");
				}
			}
			
			BcOrderParam o = null;
			if(fg) {
				hql = "from BcOrderParam where companyNum = ? and routeNo = ? and uname = ?";
				o = bcOrderParamDao.findObj(hql, unitNum, _curDay, bu.getUname());
				if(o == null) {
					o = new BcOrderParam();
					o.setUname(bu.getUname());
					o.setLinkPhone(bu.getPhone());
					o.setCompanyNum(unitNum);
					o.setServiceType(serviceType);
					o.setRouteNo(_curDay);
					o.setYwType(_ywType);
					o.setStime(_stime);
					o.setEtime(_etime);
					o.setSpoint(spoint);
					o.setEpoint(epoint);
					o.setWpoints(wpoints);
					o.setWayCity(mp.getWayCity());
					o.setPuCounty(puCounty);
					o.setIsHighSpeed(_isHighSpeed);
					o.setDistance(MathUtils.div(mp.getDistance(), 1000, 2));
					o.setRouteTime(MathUtils.div(mp.getTimeCons(), 60, 2));
					o.setTolls(mp.getTolls());
					bcOrderParamDao.save(o);
					
					map.put("id", o.getId());
					
					U.setPut(map, 1, "添加成功");
				}else {
					o.setServiceType(serviceType);
					o.setYwType(_ywType);
					o.setStime(_stime);
					o.setEtime(_etime);
					o.setSpoint(spoint);
					o.setEpoint(epoint);
					o.setWpoints(wpoints);
					o.setWayCity(mp.getWayCity());
					o.setPuCounty(puCounty);
					o.setIsHighSpeed(_isHighSpeed);
					o.setDistance(MathUtils.div(mp.getDistance(), 1000, 2));
					o.setRouteTime(MathUtils.div(mp.getTimeCons(), 60, 2));
					o.setTolls(mp.getTolls());
					bcOrderParamDao.update(o);
					
					map.put("id", o.getId());
					
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
