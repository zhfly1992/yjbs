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
import com.fx.entity.finance.FeeCourseTrade;

@Repository
public class FeeCourseTradeDao extends ZBaseDaoImpl<FeeCourseTrade, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 *  获取-单位科目交易记录-分页列表
	 *  @author xx
	 *  @date 20200708
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find		查询关键字
	 * @param unitNum   单位编号
	 * @param voucherNum 凭证号
	 * @param courseName 科目名称
	 * @param courseId   科目id，多个逗号拼接
	 * @param uname     报销人uname
	 * @param plateNum  车牌号
	 * @param sTime     添加开始时间
	 * @param eTime		添加结束时间
	 * @return Page<T>	分页数据
	 */
	public Page<FeeCourseTrade> findCourseTrades(ReqSrc reqsrc, String page, String rows,String unitNum, String voucherNum,
			String courseName,String courseId,String uname,String palteNum,String sTime, String eTime) {
		String logtxt = U.log(log, "获取-单位科目交易记录-分页列表", reqsrc);
		
		Page<FeeCourseTrade> pd = new Page<FeeCourseTrade>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "unitNum"));
			}else {
				//设置别名
				List<String> alias = new ArrayList<String>();
				alias.add("feeCourseId");
				alias.add("reimUserId");
				pd.setAlias(alias);
				comps.add(new Compositor("feeCourseId.courseNum", CompositorType.ASC));
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前单位
				if(StringUtils.isNotEmpty(voucherNum)){
					filts.add(new Filtration(MatchType.EQ, voucherNum, "voucherNum"));
				}
				if(StringUtils.isNotEmpty(courseName)){
					filts.add(new Filtration(MatchType.EQ, courseName, "feeCourseId.courseName"));
				}
				if(StringUtils.isNotEmpty(courseId)){
					String [] ids=courseId.split(",");
					Long [] arr= new Long[ids.length];
					for (int i = 0; i < ids.length; i++) {
						arr[i]=Long.valueOf(ids[i]);
					}
					filts.add(new Filtration(MatchType.IN, arr, "feeCourseId.id"));
				}
				if(StringUtils.isNotEmpty(uname)){
					filts.add(new Filtration(MatchType.EQ, uname, "reimUserId.uname"));
				}
				if(StringUtils.isNotEmpty(palteNum)){
					filts.add(new Filtration(MatchType.EQ, palteNum, "palteNum"));
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
