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
import com.fx.entity.finance.BankTradeList;

@Repository
public class BankTradeListDao extends ZBaseDaoImpl<BankTradeList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	public Page<BankTradeList> findBankTradeList(ReqSrc reqsrc, String page, String rows,String unitNum,String bankNo,String transName,
			String remark,String timeType,String sTime,String eTime,String status,String isCheck,String findMoney,
			String openRole,String voucherNum,String operMark,String openSel,String moneyType,String cusName,String serviceName) {
		String logtxt = U.log(log, "获取-银行账-分页列表", reqsrc);
		
		Page<BankTradeList> pd = new Page<BankTradeList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "unitNum"));
			}else {
				comps.add(new Compositor("id", CompositorType.DESC));
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前单位
				if(StringUtils.isNotEmpty(bankNo)){
					filts.add(new Filtration(MatchType.IN, bankNo.split(","), "myBankNum"));//我的银行账号
				}
				if(StringUtils.isNotEmpty(transName)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+transName+"%", "transName"));//对方户名
				}
				if(StringUtils.isNotEmpty(cusName)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+cusName+"%", "cusName"));//客户名称
				}
				if(StringUtils.isNotEmpty(serviceName)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+serviceName+"%", "remark"));//业务员
				}
				if(StringUtils.isNotEmpty(remark)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+remark+"%", "remark"));//摘要
				}
				if(StringUtils.isNotEmpty(openRole)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+openRole+"%", "openRole"));//开放角色
				}
				if(StringUtils.isNotEmpty(voucherNum)){
					filts.add(new Filtration(MatchType.EQ, voucherNum, "voucherNumber"));//凭证号
				}
				if(StringUtils.isNotEmpty(operMark)){
					filts.add(new Filtration(MatchType.LIKE, operMark, "operMark"));//操作编号
				}
				if(StringUtils.isNotEmpty(moneyType)){
					filts.add(new Filtration(MatchType.LIKE, moneyType, "moneyType"));//金额类型
				}
				if(StringUtils.isNotEmpty(status)){
					if("0".equals(status)){
						filts.add(new Filtration(MatchType.GT, 0.0, "tradeInMoney"));//收入
					}else{
						filts.add(new Filtration(MatchType.GT, 0.0, "tradeOutMoney"));//支出
					}
				}
				if(StringUtils.isNotEmpty(openSel)){
					if("0".equals(openSel)){//未开放查询
						filts.add(new Filtration(MatchType.ISNULL, "", "openRole"));
					}else{//已开放查询
						filts.add(new Filtration(MatchType.ISNOTNULL, "", "openRole"));
					}
				}
				if(StringUtils.isNotEmpty(isCheck)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));//收入
				}
				if(StringUtils.isNotEmpty(findMoney)){
					filts.add(new Filtration(MatchType.EQ, Double.valueOf(findMoney), "tradeInMoney","tradeOutMoney"));//金额
				}
				if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
					if("0".equals(timeType)){
						filts.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
						filts.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
					}else{
						filts.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"tradeTime"));
						filts.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"tradeTime"));
					}
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
