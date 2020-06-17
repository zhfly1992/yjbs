package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.entity.finance.CarRepairList;



@Repository
public class CarRepairListDao extends ZBaseDaoImpl<CarRepairList, Long> {
	
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());
	
	
	public Page<CarRepairList> findRepairList(ReqSrc reqsrc,String unitNum, String page,String rows,
			String plateNum,String cpaiStation,String sTime, String eTime,String isCheck,String driver) {
		String logtxt = U.log(log, "获取-单位客户-分页列表", reqsrc);
		Page<CarRepairList> pd = new Page<CarRepairList>();
		try {
			
			List<Compositor> comps = new ArrayList<Compositor>();
			List<Filtration> filtrations = new ArrayList<Filtration>();
			
			comps.add(new Compositor("id", CompositorType.DESC));
		
			////////////////////////排序设置-e///////////////////
			////////////////////////查询条件-s//////////////////////////
			
			filtrations.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前车队
			if(StringUtils.isNotEmpty(plateNum)){
				filtrations.add(new Filtration(MatchType.LIKE, plateNum, "plateNum"));//车牌号
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
				filtrations.add(new Filtration(MatchType.LIKE, driver, "repairDriver.uname"));//驾驶员
			}
			if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
				filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
				filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
			}
			////////////////////////查询条件-e//////////////////////////
			pd.setCompositors(comps);
			pd.setFiltrations(filtrations);
			pd.setPageNo(Integer.parseInt(page));
			pd.setPageSize(Integer.parseInt(rows));
		    pd = findPageByOrders(pd); // 设置列表数据

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}

	/**
	 * 获取-维修记账-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @param unitNum 	单位编号
	 * @param uname 	记账用户账号
	 */
	public Page<CarRepairList> findPageWxjzList(ReqSrc reqsrc, String page, String rows, String stime, String etime,
		String unitNum, String uname) {
		String logtxt = U.log(log, "获取-维修记账-分页列表", reqsrc);
		
		Page<CarRepairList> pd = new Page<CarRepairList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			////////////////////--默认排序--//////////////////////////
			// 加油日期-倒序
			comps.add(new Compositor("repDate", CompositorType.DESC));
			/////////////////// --条件--begin//////////////////////////
			// 指定查询[单位]
			filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
	
			// 指定查询维修用户
			filts.add(new Filtration(MatchType.EQ, uname, "repairDriver.uname"));
	
			// 查询-指定[加油日期]时间段
			if (StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)) {
				List<Filtration> fland = new ArrayList<Filtration>();
				fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "repDate"));
				fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "repDate"));
				filts.add(new Filtration(MatchType.AND, fland, ""));
			}
			/////////////////// --条件--end////////////////////////////
			
			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = findPageByOrders(pd); 								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
}
