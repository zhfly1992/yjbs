package com.fx.web.controller.mobile;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.EncodeUtils;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.wx.util.Sign;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.cus.CustomerService;
import com.fx.service.wxdat.WxGlobalService;
import com.fx.service.wxdat.WxPublicDataService;
import com.fx.web.util.RedisUtil;

/**
 * 微信授权-控制器
 */
@Controller
@RequestMapping("/mb")
public class WxAuthController {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Autowired
	private WxGlobalService wcgSer;
	/** 微信公众号数据-服务 */
	@Autowired
	private WxPublicDataService wxPublicDataSer;
	
	/** 个人用户-服务 */
	@Autowired
	private CustomerService customerSer;
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	
	/**
	 * 微信jsSdk授权url
	 * 请求接口（post-不登录）：/mb/wxAuthUrl
	 * @param url 授权页面的url（在哪个页面分享，就授权页面对应地址url）
	 */
	@RequestMapping(value="wxAuthUrl", method=RequestMethod.POST)
	public void wxAuthUrl(HttpServletResponse response, HttpServletRequest request, String url){
		U.log(log, "jsSdk授权地址："+url);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isNotBlank(url)){
				url = EncodeUtils.decode(url);
				U.log(log, "jsSdk授权地址解码后："+url);
			}else{
				url = "";
				U.log(log, "jsSdk授权地址：url为空");
			}
			
			String teamNo = LU.getLUnitNum(request, redis);
			if(StringUtils.isEmpty(teamNo)) teamNo = QC.DEF_COMPANY_NUM;
			String jsApiTicket = wcgSer.findTokenOrTicket(teamNo, 2);
			map = Sign.sign(jsApiTicket, url);
			
			// 设置appid
			WxPublicData wxpdat = wxPublicDataSer.getWxPublicData(teamNo);
			map.put("appId", wxpdat.getWxAppId());
			
			U.setPut(map, 1, "授权成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, "授权失败");
			e.printStackTrace();
		}
		
		U.log(log, "jsSdk授权参数结果："+U.toJsonStr(map));
		
		Message.print(response, map);
	}
	
	/**
	 * 微信静默授权
	 * 请求API（GET-不登录）/mb/wxAuthority
	 * eg: http://51ekc.com/mb/wxAuthority?tno=xxx&wr=xxx&state=xxx
	 * @param tno 车队编号
	 * @param wr 微信公众号菜单进入用户角色[普通用户-CUS_PT(默认); 车队计调-TEAM_JD; 车队业务员-TEAM_YW; 后台用户-BACK_ADMIN;]
	 * @param state 跳转地址状态码：114-普通用户登录地址；否则-其他地址（例如分享地址）；
	 */
	@RequestMapping(value="wxAuthority")
	public void wxAuthority(HttpServletRequest request, HttpServletResponse response, String state, String tno, String wr){
		String logtxt = U.log(log, "用户微信登录、注册授权");
		
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			/*******参数、地址处理--begin*************/
			boolean fg = true;
			// 默认跳转地址
			String redirectUrl = QC.PRO_URL + "/mb/wxAutCallBack";
			Map<String, String> ps = new HashMap<String, String>();
			
			String teamNo = "";// 车队编号
			if(fg) {
				if(StringUtils.isEmpty(tno)){
					teamNo = QC.DEF_COMPANY_NUM;
				}else{
					teamNo = tno.trim();
				}
				ps.put("teamNo", teamNo);
			}
			
			String wrole = "";// 微信菜单进入用户角色
			if(fg) {
				if(StringUtils.isEmpty(wr)){
					wrole = CusRole.PT_CUS.name();// 默认会员
				}else{
					wrole = wr.trim();
				}
				ps.put("lrole", wrole);
			}
			
			if("114".equals(state)){
				U.log(log, "登录/注册用户");
			}else {// 默认
				U.log(log, "默认：用户自动注册/登录");
				state = "120";
			}
			
			// 绑定参数,为了传入多个参数，则将所有参数封装到map对象中，并转成json字符串
			if(ps.size() > 0) redirectUrl += "?ps="+U.toJsonStr(ps);
			U.log(log, "跳转地址："+redirectUrl);
			
			// 这里要将你的授权回调地址处理一下，否则微信识别不了
			redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
			/*******参数、地址处理--end*************/
			
			String url = "";
			WxPublicData wpd = wxPublicDataSer.findByField("companyNum", teamNo);
			if(wpd != null) {
				url = "https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri=" + redirectUrl + 
						"&appid=" + wpd.getWxAppId() + "&response_type=code&scope=snsapi_base&state="+state+"#wechat_redirect";
			}else {
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
	
	/**
   	 * 微信-分享/支付-授权（微信自动回调方法）
   	 * 请求API（POST/GET-不登录）：/mb/wxAuthCallBack
   	 */
    @RequestMapping(value="wxAuthCallBack")
   	public String wxAuthCallBack(HttpServletRequest request, HttpServletResponse response){
    	String redirectUrl = "";
    	
    	try {
			Map<String, Object> map = customerSer.wxAuthCallBack(ReqSrc.WX, request, response);
			MapRes mr = U.mapRes(map);
			if(mr.getCode() > 0) {
				redirectUrl = map.get("redirectUrl").toString();
			}else {
				// 跳转到移动端错误页面
				redirectUrl = QC.PRO_URL + "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "redirect:"+redirectUrl;
   	}
	
}
