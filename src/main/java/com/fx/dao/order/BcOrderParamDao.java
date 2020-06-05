package com.fx.dao.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.entity.order.BcOrderParam;

@Repository
public class BcOrderParamDao extends ZBaseDaoImpl<BcOrderParam, Long> {
	
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	
	/**
	 * 获取-旅游包车临时订单参数列表
	 * @param mainOrderNum 	登录车队编号
	 * @return 数据列表
	 */
	public List<BcOrderParam> findBcOrderParamList(String mainOrderNum){
		String logtxt = U.log(log, "获取-旅游包车临时订单参数列表");
		
		List<BcOrderParam> list = new ArrayList<BcOrderParam>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(mainOrderNum)) {
					fg = U.logFalse(log, "[主订单编号]不能为空");
				}else {
					U.log(log, "[主订单编号] mainOrderNum="+mainOrderNum);
				}
			}
			
			if(fg) {
				list = findListByField("mainOrderNum", mainOrderNum);
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return list;
	}
	
}
