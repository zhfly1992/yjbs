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
import com.fx.entity.finance.EtcList;


@Repository
public class EtcListDao extends ZBaseDaoImpl<EtcList, Long> {
	
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	public Page<EtcList> findEtcList(ReqSrc reqsrc, String page, String rows,String unitNum, 
			String orderNum, String sTime, String eTime,
			String plateNum,String driverName,String cardNo,String operMark) {
		String logtxt = U.log(log, "获取-单位ETC数据-分页列表", reqsrc);
		Page<EtcList> pd = new Page<EtcList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "unitNum"));
			}else {
				//设置别名
				List<String> alias = new ArrayList<String>();
				alias.add("etcDriver");
				pd.setAlias(alias);
				
				comps.add(new Compositor("id", CompositorType.DESC));
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前单位
				if(StringUtils.isNotEmpty(orderNum)){
					filts.add(new Filtration(MatchType.LIKE, orderNum, "orderNum"));//订单号
				}
				if(StringUtils.isNotEmpty(plateNum)){
					filts.add(new Filtration(MatchType.LIKE, plateNum, "plateNum"));//车牌号
				}
				if(StringUtils.isNotEmpty(driverName)){
					filts.add(new Filtration(MatchType.EQ, driverName, "etcDriver.phone","etcDriver.realName"));//驾驶员
				}
				if(StringUtils.isNotEmpty(cardNo)){
					filts.add(new Filtration(MatchType.IN, cardNo.split(","), "cardNo"));//卡号
				}
				if(StringUtils.isNotEmpty(operMark)){
					filts.add(new Filtration(MatchType.LIKE, operMark, "operMark"));//操作编号
				}
				if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
					filts.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
					filts.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
				}
				////////////////////////查询条件-e//////////////////////////
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
