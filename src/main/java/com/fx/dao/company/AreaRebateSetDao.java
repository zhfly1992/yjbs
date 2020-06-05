package com.fx.dao.company;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.entity.company.AreaRebateSet;

@Repository
public class AreaRebateSetDao extends ZBaseDaoImpl<AreaRebateSet, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * 查询区域返点列表数据
	 * @author qfc
	 * @date 20200426
	 * @param unitNum 查询的车队编号
	 * @param area 查询的区域名称
	 */
	public List<AreaRebateSet> findAreaRebateSetList(String unitNum, String area) {
		log.info(">>>>>>>>>>>>>>>>查询区域返点比例设置列表--begin");
		List<AreaRebateSet> arss = new ArrayList<AreaRebateSet>();
		String hql = "";
		
		try {
			if(StringUtils.isNotBlank(area)){
				area = area.trim();
				unitNum = unitNum.trim();
				U.log(log, "[区域名称] area="+area);
				U.log(log, "[车队编号] unitNum="+unitNum);
				
				hql = "select new AreaRebateSet(id, rebateName, area, rebate) from AreaRebateSet where unitNum = ? and area like ? order by area asc,rebate asc";
				arss = findhqlList(hql, unitNum, "%"+area+"%");
			}else{
				hql = "select new AreaRebateSet(id, rebateName, area, rebate) from AreaRebateSet order by area asc,rebate asc";
				arss = findhqlList(hql);
			}
		} catch (Exception e) {
			log.info("异常：查询区域返点比例设置列表", e);
			e.printStackTrace();
		}
		
		log.info(">>>>>>>>>>>>>>>>查询区域返点比例设置列表--end--");
		
		return arss;
	}

}
