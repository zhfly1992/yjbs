package com.fx.service.impl.wxdat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.WeiXinUtil;
import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.U;
import com.fx.dao.wxdat.WxGlobalDao;
import com.fx.dao.wxdat.WxPublicDataDao;
import com.fx.entity.wxdat.WxGlobal;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.wxdat.WxGlobalService;

@Service
@Transactional
public class WxGlobalServiceImpl extends BaseServiceImpl<WxGlobal,Long> implements WxGlobalService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 微信全局数据-服务 */
	@Autowired
	private WxGlobalDao wxGlobalDao;
	@Override
	public ZBaseDaoImpl<WxGlobal, Long> getDao() {
		return wxGlobalDao;
	}
	/** 配置数据-服务 */
	@Autowired
	private ConfigPs configPs;
	/** 微信公众号数据-服务 */
	@Autowired
	private WxPublicDataDao wxPublicDataDao;
	
	
	@Override
	public String findTokenOrTicket(String teamNo, int type) {
		String logtxt = U.log(log, "获取微信公众号授权token或者jsApiTicket票据, 如过期则重新获取");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		String res = "";
		
		try {
			if(fg){
				if(!configPs.isIPpass()){
					fg = U.setPutFalse(map, "[访问IP没有权限]");
				}
			}
			
			WxPublicData wpd = null;
			if(fg) {
				if(StringUtils.isEmpty(teamNo)) {
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else {
					teamNo = teamNo.trim();
					wpd = wxPublicDataDao.findByField("companyNum", teamNo);
					if(wpd == null) {
						fg = U.setPutFalse(map, "不存在车队["+teamNo+"]的微信公众号数据");
					}else {
						U.log(log, "已经获取["+wpd.getWxPublicName()+"]公众号的数据");
					}
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			WxGlobal wcg = null;
			if(fg){
				hql = "from WxGlobal where teamNo = ?";
				wcg = wxGlobalDao.findObj(hql, wpd.getCompanyNum());
				if(wcg == null){
					U.log(log, "不存在保存令牌数据");
				}else{
					U.log(log, "已存在保存令牌数据");
				}
			}
			
			
			if(fg) {
				// 微信令牌token, jsApi接口临时票据
				String token = "", jsticket = "";
				
				if(wcg == null) {
					U.log(log, "不存在令牌和票据数据，则添加");
					
					token = U.Cq(WeiXinUtil.getAccessToken(wpd.getWxAppId(), wpd.getWxAppSecret()), "access_token");
					wcg = new WxGlobal();
					wcg.setAccessToken(token);
					wcg.setAddTime(new Date());
					wcg.setValidTime(7000L);
					
					if(StringUtils.isNotBlank(wcg.getAccessToken())) {
						jsticket = U.Cq(WeiXinUtil.getJSAPI(token), "ticket");
						wcg.setJsApiTicket(jsticket);
						wcg.setJsApiValidTime(7000L);
						wcg.setJsApiAddTime(new Date());
					}else {
						U.log(log, "第一次添加jsApiTicket失败，因为token获取为空");
					}
					wxGlobalDao.save(wcg);
					
					U.log(log, "添加令牌和票据数据-完成");
				}else {
					U.log(log, "已存在令牌和票据数据，则获取或更新");
				
					// 存在微信token令牌
					if(StringUtils.isNotEmpty(wcg.getAccessToken())){
						// 令牌token过期时间
						Date tokenPass = DateUtils.getPlusSecondsD(wcg.getAddTime(), -wcg.getValidTime());
						// 当前时间在token过期时间之后说明已过期，重新获取
						if(new Date().after(tokenPass)){ 
							token = U.Cq(WeiXinUtil.getAccessToken(wpd.getWxAppId(), wpd.getWxAppSecret()), "access_token");
							if(StringUtils.isEmpty(token)){
								U.log(log, "重新获取token失败");
							}else{
								wcg.setAccessToken(token);			// 更新token
								wcg.setAddTime(new Date()); 		// 更新获取token时间
								wxGlobalDao.update(wcg);
								
								U.log(log, "更新指令token数据-完成");
							}
						}else{// 未过期，直接获取
							token = wcg.getAccessToken();
						}
					}else{// 不存在微信token令牌
						token = U.Cq(WeiXinUtil.getAccessToken(wpd.getWxAppId(), wpd.getWxAppSecret()), "access_token");
						if(StringUtils.isEmpty(token)){
							U.log(log, "重新获取token失败，token为空");
						}else{
							wcg.setValidTime(7000L);		// 设置token令牌过期时间
							wcg.setAccessToken(token);		// 设置token
							wcg.setAddTime(new Date()); 	// 设置获取token时间
							wxGlobalDao.update(wcg);
							
							U.log(log, "更新指令token数据-完成");
						}
					}
					
					if(StringUtils.isNotBlank(token)){
						// 存在jsApi接口票据
						if(StringUtils.isNotEmpty(wcg.getJsApiTicket())){
							// jsApiTicket过期时间
							Date jsApiPass = DateUtils.getPlusSecondsD(wcg.getJsApiAddTime(), -wcg.getJsApiValidTime());
							// 当前时间在js过期时间之后说明已过期，重新获取
							if(new Date().after(jsApiPass)){ 
								jsticket = U.Cq(WeiXinUtil.getJSAPI(token), "ticket");
								wcg.setJsApiTicket(jsticket);
								wcg.setJsApiAddTime(new Date());
								wxGlobalDao.update(wcg);
								
								U.log(log, "更新jsApi接口数据-完成");
							}else{// 未过期，直接获取
								jsticket = wcg.getJsApiTicket();
							}
						}else{// 不存在jsApi接口票据，重新获取
							jsticket = U.Cq(WeiXinUtil.getJSAPI(token), "ticket");
							wcg.setJsApiValidTime(Long.valueOf(7000));
							wcg.setJsApiTicket(jsticket);
							wcg.setJsApiAddTime(new Date());
							wxGlobalDao.update(wcg);
							
							U.log(log, "更新（添加）jsApi接口数据-完成");
						}
					}
				}
				
				if(type == 1) {			// 获取：token
					res = token;
				}else if(type == 2) {	// 获取：jsApiTicket
					res = jsticket;
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return res;
	}
	
	
}
