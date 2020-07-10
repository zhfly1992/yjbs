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
import com.fx.commons.utils.tools.UT;
import com.fx.entity.finance.ReimburseList;

@Repository
public class ReimburseListDao extends ZBaseDaoImpl<ReimburseList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	public Page<ReimburseList> findReimList(ReqSrc reqsrc, String page, String rows,String unitNum, String uname,String vouNum,String status,
			String isCheck,String courseName,String myBank,String reimIsCar,String reimPlateNum,
			String reimZy,String reimMoney,String operMark,String sTime,String eTime) {
		String logtxt = U.log(log, "获取-凭证-分页列表", reqsrc);
		
		Page<ReimburseList> pd = new Page<ReimburseList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "unitNum"));
			}else {
				comps.add(new Compositor("id", CompositorType.DESC));
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前车队
				if(StringUtils.isNotEmpty(uname)){
					filts.add(new Filtration(MatchType.EQ, uname,"reimUserId.uname"));
				}
				if(StringUtils.isNotEmpty(vouNum)){
					filts.add(new Filtration(MatchType.LIKE, vouNum,"voucherNumber"));
				}
				if(StringUtils.isNotEmpty(status)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(status),"feeStatus"));
				}
				if(StringUtils.isNotEmpty(isCheck)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));//审核状态
				}else{//没有选银行时默认不显示已关联的记录
					if(StringUtils.isBlank(myBank)){
						filts.add(new Filtration(MatchType.NE, -1, "isCheck"));//已拒绝不显示
					}
				}
				if(StringUtils.isNotEmpty(courseName)){
					filts.add(new Filtration(MatchType.IN, courseName.split(","),"feeCourseId.course_name"));
				}
				if(StringUtils.isNotEmpty(myBank)){
					filts.add(new Filtration(MatchType.LIKE, myBank,"myBankInfo"));
				}
				if(StringUtils.isNotEmpty(reimIsCar)){
					if("0".equals(reimIsCar)){//公司开支
						filts.add(new Filtration(MatchType.ISNULL, "","plateNum"));
					}else{//车辆开支
						filts.add(new Filtration(MatchType.ISNOTNULL, "","plateNum"));
					}
				}
				if(StringUtils.isNotEmpty(reimZy)){
					filts.add(new Filtration(MatchType.LIKE, reimZy,"remark"));
				}
				if(StringUtils.isNotEmpty(reimMoney)){
					filts.add(new Filtration(MatchType.EQ, Double.valueOf(reimMoney), "totalMoney"));//金额
				}
				if(StringUtils.isNotEmpty(reimPlateNum)){
					filts.add(new Filtration(MatchType.LIKE, reimPlateNum,"plateNum"));
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
			pd.setFiltrations(filts);								// 查询条件
			pd = findPageByOrders(pd);								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}
	/**
	 * 获取凭证号
	 * @author xx
	 * @Date 20200702
	 * @param unitNum 单位编号
	 * @param uname   员工账号
	 */
	public String getVoucherNum(String unitNum,String uname) {
		String hql="select count(id) from ReimburseList where unitNum=?0 and addTime>=?1 and addTime<=?2";
		Object sortNum=findObj(hql, unitNum,DateUtils.getStartTimeOfDay(),DateUtils.getEndTimeOfDay());
		return UT.creatReimVoucher(uname,Integer.parseInt(sortNum.toString()));
	}
}
