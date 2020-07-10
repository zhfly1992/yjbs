package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fx.dao.company.StaffDao;
import com.fx.entity.company.Staff;
import com.fx.entity.finance.BankTradeList;

@Repository
public class BankTradeListDao extends ZBaseDaoImpl<BankTradeList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	/**员工服务*/
	@Autowired
	private StaffDao staffDao;
	
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
				//设置别名
				List<String> alias = new ArrayList<String>();
				alias.add("moneyTypeId");
				pd.setAlias(alias);
				
				comps.add(new Compositor("id", CompositorType.DESC));
				List<Staff> stafflist=new ArrayList<Staff>();
				////////////////////////查询条件-s//////////////////////////
				filts.add(new Filtration(MatchType.EQ, unitNum,"unitNum"));//当前单位
				if(StringUtils.isNotBlank(bankNo)){
					filts.add(new Filtration(MatchType.IN, bankNo.split(","), "myBankNum"));//我的银行账号
				}
				if(StringUtils.isNotBlank(transName)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+transName+"%", "transName"));//对方户名
				}
				if(StringUtils.isNotBlank(cusName)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+cusName+"%", "cusName"));//客户名称
				}
				if(StringUtils.isNotBlank(remark)){
					filts.add(new Filtration(MatchType.LIKE_, "%"+remark+"%", "remark"));//摘要
				}
				if(StringUtils.isNotBlank(openRole)){
					if(StringUtils.isNotBlank(serviceName)){//通过业务员搜索记录
						//根据业务员uname查询可查看的金额类型
						String hql = "from Staff where baseUserId.uname in(:v0)";
						stafflist=staffDao.findListIns(hql, (Object[])serviceName.split(","));
					}else {//通过角色搜索记录
						//根据角色查询员工列表
						stafflist=staffDao.findListByField("roleId.id", Long.valueOf(openRole));
					}
					if(stafflist.size()>0) {
						Set<String> mts=new HashSet<String>();
						for (int i = 0; i < stafflist.size(); i++) {//员工列表
							if(!stafflist.get(i).getMoneyTypes().isEmpty()) {//员工能查看的金额类型列表
								for (int j = 0; j < stafflist.get(i).getMoneyTypes().size(); j++) {
									mts.add(stafflist.get(i).getMoneyTypes().get(j).getTypeName());
								}
							}
							
						}
						if(mts.size()>0)filts.add(new Filtration(MatchType.IN, mts.toArray(), "moneyTypeId.typeName"));//业务员
					}
				}
				if(StringUtils.isNotBlank(voucherNum)){
					filts.add(new Filtration(MatchType.EQ, voucherNum, "voucherNumber"));//凭证号
				}
				if(StringUtils.isNotBlank(operMark)){
					filts.add(new Filtration(MatchType.LIKE, operMark, "operMark"));//操作编号
				}
				if(StringUtils.isNotBlank(moneyType)){
					filts.add(new Filtration(MatchType.IN, moneyType.split(","), "moneyTypeId.typeName"));//金额类型
				}
				if(StringUtils.isNotBlank(status)){
					if("0".equals(status)){
						filts.add(new Filtration(MatchType.GT, 0.0, "tradeInMoney"));//收入
					}else{
						filts.add(new Filtration(MatchType.GT, 0.0, "tradeOutMoney"));//支出
					}
				}
				if(StringUtils.isNotBlank(isCheck)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));//收入
				}
				if(StringUtils.isNotBlank(findMoney)){
					filts.add(new Filtration(MatchType.EQ, Double.valueOf(findMoney), "tradeInMoney","tradeOutMoney"));//金额
				}
				if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){ //时间
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
