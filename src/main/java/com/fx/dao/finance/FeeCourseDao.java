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
import com.fx.commons.utils.enums.CourseCategory;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.entity.finance.FeeCourse;

@Repository
public class FeeCourseDao extends ZBaseDaoImpl<FeeCourse, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 *  获取-单位科目-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param unitNum 单位编号
	 * @param find 查询条件（科目名称，科目编码，科目简拼）
	 * @param courseType 收支状态
	 * @param courseStatus 使用状态
	 * @param courseCategory 科目类别
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return Page<T>	分页数据
	 */
	public Page<FeeCourse> findFeeCourseList(ReqSrc reqsrc, String page, String rows, String unitNum, String find,String courseType,
			String courseStatus,String courseCategory ,String sTime, String eTime) {
		String logtxt = U.log(log, "获取-单位科目-分页列表", reqsrc);
		
		Page<FeeCourse> pd = new Page<FeeCourse>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "unitNum"));
			}else {
				comps.add(new Compositor("courseNum", CompositorType.ASC));
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前单位
				if(StringUtils.isNotEmpty(find)){
					filts.add(new Filtration(MatchType.LIKE, find, "courseNum","courseName","pinyinSimple"));
				}
				if(StringUtils.isNotEmpty(courseType)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(courseType), "courseType"));
				}
				if(StringUtils.isNotEmpty(courseStatus)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(courseStatus), "courseStatus"));
				}
				if(StringUtils.isNotEmpty(courseCategory)){
					filts.add(new Filtration(MatchType.EQ, CourseCategory.valueOf(courseCategory), "courseCategory"));
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
