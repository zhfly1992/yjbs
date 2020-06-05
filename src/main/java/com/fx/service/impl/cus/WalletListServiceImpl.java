package com.fx.service.impl.cus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.CustomerDao;
import com.fx.dao.cus.WalletListDao;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WalletList;
import com.fx.service.cus.WalletListService;

@Service
@Transactional
public class WalletListServiceImpl extends BaseServiceImpl<WalletList,Long> implements WalletListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private WalletListDao walletLDao;
	@Autowired
	private CustomerDao cusDao;  //注册信息
	@Override
	public ZBaseDaoImpl<WalletList, Long> getDao() {
		return walletLDao;
	}
	@Override
	public Page<WalletList> getTradeList(Page<WalletList> pageData, Object obj[],
		String loginName, String find, int status, String tradeST, String tradeET, 
		String like) {
		Compositor compositor = new Compositor("atime", CompositorType.DESC);
		pageData.setCompositor(compositor);
		List<Filtration> filtrations = new ArrayList<Filtration>();
		////////////////////////查询条件-s//////////////////////////
		filtrations.add(new Filtration(MatchType.GT, 0.0, "amoney"));//交易金额大于0
		filtrations.add(new Filtration(MatchType.SQL, "", " (note <> '接送机手续费' and note <> '顺风车手续费') "));
		if(StringUtils.isNotEmpty(loginName)){
			if(!loginName.equals("17828028717")){
				String hql = "from Customer where recBaseUserId.uname = ?";//推荐用户
				List<Customer> hList = cusDao.findhqlList(hql, loginName);
				HashSet<String> hs = new HashSet<String>();
				hs.add(loginName);
				for (Customer each : hList) {
					hs.add(each.getBaseUserId().getUname());
				}
				List<String> names = new ArrayList<String>(hs);
				Object[] obj1 = (String[])names.toArray(new String[names.size()]);
				if(obj1.length > 0){
					filtrations.add(new Filtration(MatchType.IN, obj1, "baseUserId.uname"));
				}else{
					filtrations.add(new Filtration(MatchType.EQ, "-1", "baseUserId.uname"));
				}
			}else{
				filtrations.add(new Filtration(MatchType.EQ, loginName, "baseUserId.uname"));//订单号/账户名
			}
		}
		if(StringUtils.isNotEmpty(find)){
			filtrations.add(new Filtration(MatchType.LIKE, find, "baseUserId.uname","assist","note"));//订单号/账户名
		}
		if(StringUtils.isNotEmpty(like)){ //报表查询
			String [] count=like.split(",");
			filtrations.add(new Filtration(MatchType.SQL, "", " (assist like '"+count[0]+"%' or assist like '"+count[1]+"%') "));
		}
		if(obj!=null){ //交易类型
			filtrations.add(new Filtration(MatchType.IN, obj, "type"));//类型
		}
		if(status!=-1){
			filtrations.add(new Filtration(MatchType.EQ, status, "status"));//状态
		}		
		if(StringUtils.isNotEmpty(tradeST) && StringUtils.isNotEmpty(tradeET)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",tradeST),"atime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",tradeET+" 23:59:59"),"atime"));
		}
		////////////////////////查询条件-e//////////////////////////
		pageData.setFiltrations(filtrations);
		pageData = walletLDao.findPage(pageData);
		return pageData;
	}
	
	@Override
	public Page<WalletList> getWalletRecord(Page<WalletList> pageData, String atype,
		String status, String loginName, String find, String stime, String etime) {
		
		////////////////////////排序设置//////////////////////////
		List<Compositor> compositors = new ArrayList<Compositor>();
		compositors.add(new Compositor("atime", CompositorType.DESC));
		
		////////////////////////查询条件-s//////////////////////////
		List<Filtration> filtrations = new ArrayList<Filtration>();
		
		if(StringUtils.isNotEmpty(atype)){
			filtrations.add(new Filtration(MatchType.EQ, Integer.parseInt(atype), "atype"));
		}
		
		if(StringUtils.isNotEmpty(status)){
			filtrations.add(new Filtration(MatchType.EQ, Integer.parseInt(status), "status"));
		}
		
		if(StringUtils.isNotEmpty(loginName)){
			filtrations.add(new Filtration(MatchType.EQ, loginName, "cName"));
		}
		
		if(StringUtils.isNotEmpty(find)){
			// 订单号 || 账户名
			filtrations.add(new Filtration(MatchType.LIKE, find, "cName", "assist"));
		}
		
			
		if(StringUtils.isNotEmpty(stime) || StringUtils.isNotEmpty(etime)){
			if(StringUtils.isNotEmpty(stime)){
				filtrations.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "atime"));
			}
			
			if(StringUtils.isNotEmpty(etime)){
				filtrations.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "atime"));
			}
		}
		////////////////////////查询条件-e//////////////////////////
		
		pageData.setCompositors(compositors);// 设置排序参数
		pageData.setFiltrations(filtrations);// 设置过滤条件参数
		pageData = walletLDao.findPageByOrders(pageData);
		return pageData;
	}
	
	
	/****移动端====begin**************************/
	@Override
	public Page<WalletList> findWalletList(ReqSrc reqsrc, String page, String rows, 
		String uid, String stime, String etime, String atype) {
		U.log(log, "查询-用户["+uid+"]-钱包交易记录");
		
		Page<WalletList> pd = new Page<WalletList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.WX == reqsrc){// 移动端
				////////////////////--默认排序--//////////////////////////
				// 交易时间-降序-排序
				comps.add(new Compositor("atime", CompositorType.DESC));
		
				///////////////////--条件--begin//////////////////////////
				// 查询-指定用户
				filts.add(new Filtration(MatchType.EQ, uid, "cName"));
		
				if(StringUtils.isNotBlank(atype)){
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(atype), "atype"));
				}
				
				// 查询-指定[交易]时间段
				if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
					List<Filtration> fland = new ArrayList<Filtration>();
					fland.add(new Filtration(MatchType.GT, DateUtils.std_st(stime), "atime"));
					fland.add(new Filtration(MatchType.LT, DateUtils.std_et(etime), "atime"));
					filts.add(new Filtration(MatchType.AND, fland, ""));
				}
				///////////////////--条件--end////////////////////////////
			}else{// 查询id为空的数据（实际是没有这样的数据，因此会返回空集合）
				U.log(log, "数据[请求来源]不存在");
				filts.add(new Filtration(MatchType.ISNULL, null, "id"));
			}
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = walletLDao.findPageByOrders(pd);					// 设置列表数据
		} catch (Exception e) {
			U.log(log, "查询-用户["+uid+"]-钱包交易记录", e);
			e.printStackTrace();
		}
		
		return pd;
	}
	
	/****移动端====end**************************/
	
}
