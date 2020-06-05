package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
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
}
