package com.fx.dao.back.scenic_spots_dat;

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
import com.fx.commons.utils.tools.U;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsPoint;

@Repository
public class ScenicSpotsPointDao extends ZBaseDaoImpl<ScenicSpotsPoint, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	
	/**
	 * 获取-景点地点数据-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param cityName	城市名称
	 * @param countyName城市区/县名称
	 * @param find		查询关键字
	 * @return Page<T>	分页数据
	 */
	public Page<ScenicSpotsPoint> findScenicSpotsPointList(ReqSrc reqsrc, String page, String rows, 
		String cityName, String countyName, String find) {
		String logtxt = U.log(log, "获取-景点地点数据-分页列表", reqsrc);
		
		Page<ScenicSpotsPoint> pd = new Page<ScenicSpotsPoint>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			comps.add(new Compositor("city", CompositorType.DESC));
			comps.add(new Compositor("sortNo", CompositorType.ASC));
			
			if(StringUtils.isNotBlank(cityName)) {
				filts.add(new Filtration(MatchType.LIKE, "%-"+cityName, "city"));
				
				if(StringUtils.isNotBlank(countyName)) {
					filts.add(new Filtration(MatchType.EQ, countyName, "county"));
				}
			}
			
			if(StringUtils.isNotBlank(find)) {
				filts.add(new Filtration(MatchType.LIKE, find, "mapAddr", "addrShort"));
			}
			
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = findPageByOrders(pd);								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
	
}
