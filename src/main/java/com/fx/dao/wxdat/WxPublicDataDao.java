package com.fx.dao.wxdat;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.entity.wxdat.WxPublicData;

@Repository
public class WxPublicDataDao extends ZBaseDaoImpl<WxPublicData, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/**
	 * 获取-指定单位微信公众号数据
	 * @param companyNum 单位编号
	 * @return 单位对应公众号数据对象
	 */
	public WxPublicData getWxPublicData(String companyNum) {
		String logtxt = "获取-指定车队微信公众号数据";
		
		WxPublicData wpd = null;
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(companyNum)) {
					U.log(log, "为空则默认为客车帮车队编号");
					
					//fg = U.logFalse(log, "[车队编号]不能为空");
					companyNum = QC.DEF_COMPANY_NUM;// 为空则默认为客车帮车队编号
				}else {
					companyNum = companyNum.trim();
					
					U.log(log, "[车队编号] companyNum="+companyNum);
				}
			}
			
			if(fg) {
				wpd = findByField("companyNum", companyNum);
				if(wpd == null) {
					//fg = U.logFalse(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					U.log(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					
					wpd = findByField("teamNo", QC.DEF_COMPANY_NUM);
				}else {
					U.log(log, "获取微信公众号["+wpd.getWxPublicName()+"]的微信公众数据成功");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return wpd;
	}
	
}
