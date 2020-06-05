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
import com.fx.commons.utils.tools.U;
import com.fx.entity.company.CompanyGroup;

@Repository
public class CompanyGroupDao extends ZBaseDaoImpl<CompanyGroup, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * 获取-单位驾驶员分组-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find		查询关键字
	 * @return Page<T>	分页数据
	 */
	public Page<CompanyGroup> findGroupList(ReqSrc reqsrc, String page, String rows,String unitNum, String find) {
		String logtxt = U.log(log, "获取-单位驾驶员分组-分页列表", reqsrc);
		
		Page<CompanyGroup> pd = new Page<CompanyGroup>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			}else {
				comps.add(new Compositor("addTime", CompositorType.DESC));
				filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));//单位编号
				
				if(StringUtils.isNotBlank(find)) {//姓名,电话
					filts.add(new Filtration(MatchType.LIKE, find, "linkPhone","linkName","groupName"));
				}
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
