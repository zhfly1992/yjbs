package com.fx.dao.wxdat;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.entity.wxdat.WxTplmsgData;

@Repository
public class WxTplmsgDataDao extends ZBaseDaoImpl<WxTplmsgData, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	public WxTplmsgData getWxTplmsgData(String teamNo, String tplNo) {
		String logtxt = "获取-指定模板编号的微信模板消息对象数据";
		
		WxTplmsgData wtd = null;
		String hql = "";
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
				if(StringUtils.isEmpty(tplNo)) {
					fg = U.logFalse(log, "[微信模板消息模板编号]不能为空");
				}else {
					tplNo = tplNo.trim();
					
					U.log(log, "[微信模板消息模板编号] tplNo="+tplNo);
				}
			}
			
			if(fg) {
				hql = "from WxTplmsgData where teamNo = ? and tplNo = ?";
				wtd = findObj(hql, teamNo, tplNo);
				if(wtd == null) {
//					fg = U.logFalse(log, "指定模板编号模板对象不存在");
					
					U.log(log, "指定模板编号模板对象不存在，则使用“客车帮”公众号模板消息数据");
					
					wtd = findObj(hql, QC.DEF_COMPANY_NUM, tplNo);
				}else {
					U.log(log, "获取模板对象成功");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return wtd;
	}
	
}
