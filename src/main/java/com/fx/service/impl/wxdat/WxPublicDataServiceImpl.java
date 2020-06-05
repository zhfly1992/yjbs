package com.fx.service.impl.wxdat;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.wxdat.WxPublicDataDao;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.wxdat.WxPublicDataService;

@Service
public class WxPublicDataServiceImpl extends BaseServiceImpl<WxPublicData, Long> implements WxPublicDataService {

	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	
	@Autowired
	private WxPublicDataDao wxPublicDataDao;
	@Override
	public ZBaseDaoImpl<WxPublicData, Long> getDao() {
		return wxPublicDataDao;
	}
	
	
	@Override
	public WxPublicData getWxPublicData(String teamNo) {
		String logtxt = "获取-指定车队微信公众号数据";
		
		WxPublicData wpd = null;
		boolean fg = true;
		
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(teamNo)) {
					U.log(log, "为空则默认为客车帮车队编号");
					
					//fg = U.logFalse(log, "[车队编号]不能为空");
					teamNo = QC.DEF_COMPANY_NUM;// 为空则默认为客车帮车队编号
				}else {
					teamNo = teamNo.trim();
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			if(fg) {
				wpd = wxPublicDataDao.findByField("teamNo", teamNo);
				if(wpd == null) {
					//fg = U.logFalse(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					U.log(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					
					wpd = wxPublicDataDao.findByField("teamNo", QC.DEF_COMPANY_NUM);
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
