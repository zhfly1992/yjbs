package com.fx.service.impl.wxdat;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.wxdat.WxPublicDataDao;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.wxdat.WxPublicDataService;

@Service
@Transactional
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
	public void wxAuthority(HttpServletRequest request, HttpServletResponse response, 
		String state, String lteamNo, String lrole) {
		String logtxt = U.log(log, "用户微信登录、注册授权");
		
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			/*******参数、地址处理--begin*************/
			boolean fg = true;
			// 默认跳转地址
			String redirectUrl = QC.PRO_URL + "/mb/wxAuthCallBack";
			List<String> ps = new ArrayList<String>();
			
			String teamNo = "";// 车队编号
			if(fg) {
				if(StringUtils.isEmpty(lteamNo)){
					teamNo = QC.DEF_COMPANY_NUM;
				}else{
					teamNo = lteamNo.trim();
				}
				ps.add("teamNo="+teamNo);
				
				U.log(log, "teamNo="+teamNo);
			}
			
			String wrole = "";// 微信菜单进入用户角色
			if(fg) {
				if(StringUtils.isEmpty(lrole)){
					wrole = CusRole.PT_CUS.name();// 默认会员
				}else{
					wrole = lrole.trim();
				}
				ps.add("lrole="+wrole);
				
				U.log(log, "wrole="+wrole);
			}
			
			if("114".equals(state)){
				U.log(log, "登录/注册用户");
			}else {// 默认
				U.log(log, "默认：用户自动注册/登录");
				state = "120";
			}
			
			// 绑定参数,为了传入多个参数，则将所有参数封装到map对象中，并转成json字符串
			if(ps.size() > 0) {
				redirectUrl += "?"+StringUtils.join(ps.toArray(), "&");
			} 
			U.log(log, "跳转地址："+redirectUrl);
			
			// 这里要将你的授权回调地址处理一下，否则微信识别不了
			redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
			/*******参数、地址处理--end*************/
			
			String url = "";
			WxPublicData wpd = getWxPublicData(teamNo);
			if(wpd != null) {
				U.log(log, "存在-公众号数据");
				
				url = "https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri=" + redirectUrl + 
						"&appid=" + wpd.getWxAppId() + "&response_type=code&scope=snsapi_base&state="+state+"#wechat_redirect";
			}else {
				U.log(log, "不存在-公众号数据");
				
				url = "https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri=" + redirectUrl + 
						"&appid=" + QC.DEF_APPID + "&response_type=code&scope=snsapi_base&state="+state+"#wechat_redirect";
			}
			U.log(log, "授权地址："+url);
			// 简单获取openid的话参数response_type与scope与state参数固定写死即可
			StringBuffer sb = new StringBuffer(url);
			response.sendRedirect(sb.toString());// 这里请不要使用get请求，单纯的将页面跳转到该url即可
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
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
				wpd = wxPublicDataDao.findByField("companyNum", teamNo);
				if(wpd == null) {
					//fg = U.logFalse(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					U.log(log, "当前车队未设置微信公众号数据，则使用“客车帮”公众号数据");
					
					wpd = wxPublicDataDao.findByField("companyNum", QC.DEF_COMPANY_NUM);
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
