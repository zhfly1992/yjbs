package com.fx.dao.company;

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
import com.fx.entity.company.TouristCharterPrice;

@Repository
public class TouristCharterPriceDao extends ZBaseDaoImpl<TouristCharterPrice, Long> {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());
	
	/**
	 * 
	 * @Description:
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find 		查询条件
	 * @param sTime 	开始时间
	 * @param eTime 	结束时间
	 * @return Page<T> 分页数据
	 * @author :xx
	 * @version 20200427
	 */
	public Page<TouristCharterPrice> findTcpList(ReqSrc reqsrc,String page, String rows,String find,String sTime,String eTime) {

		String logtxt = U.log(log, "获取-包车价格区域-分页列表", reqsrc);

		Page<TouristCharterPrice> pd = new Page<TouristCharterPrice>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			} else {
				////////////////////////排序设置-s///////////////////
				comps.add(new Compositor("id", CompositorType.DESC));
				////////////////////////排序设置-e///////////////////
				////////////////////////查询条件-s//////////////////////////
				if(StringUtils.isNotEmpty(find)){
					filts.add(new Filtration(MatchType.EQ, find, "toucId"));//包车设置id
				}
				if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
					filts.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"startTime"));
					filts.add(new Filtration(MatchType.LE,DateUtils.std_et(eTime),"endTime"));
				}
				////////////////////////查询条件-e//////////////////////////
				////////////////////////查询条件-e//////////////////////////
			}
			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = findPageByOrders(pd); // 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}
}
