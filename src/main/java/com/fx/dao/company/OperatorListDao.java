package com.fx.dao.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.service.company.OperatorList;

@Repository
public class OperatorListDao extends ZBaseDaoImpl<OperatorList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * 获取-单位所有计调
	 * @param companyNum 单位编号
	 * @return 计调操作员列表
	 */
	public List<OperatorList> getTeamAllJd(String companyNum) {
		String logtxt = U.log(log, "获取-单位所有计调");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		List<OperatorList> ols = new ArrayList<OperatorList>();
		
		try {
			if(fg){
				if(StringUtils.isEmpty(companyNum)){
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else{
					companyNum = companyNum.trim();
					
					hql = "from OperatorList where companyNum = ? and operTypeName = ? order by id asc";
					ols = findhqlList(hql, companyNum, "计调");
					
					U.log(log, "[单位编号] companyNum="+companyNum);
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return ols;
	}
	
}
