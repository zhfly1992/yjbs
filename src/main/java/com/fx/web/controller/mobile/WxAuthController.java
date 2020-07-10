package com.fx.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.annotation.Log;
import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.EncodeUtils;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.wx.util.Sign;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.cus.CustomerService;
import com.fx.service.cus.WxBaseUserService;
import com.fx.service.wxdat.WxGlobalService;
import com.fx.service.wxdat.WxPublicDataService;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	/** 微信基类-服务 */
	@Autowired
	private WxBaseUserService wxBaseUserSer;
	
	
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
	 * eg: http://51ekc.cn/mb/wxAuthority?tno=xxx&wr=xxx&state=xxx
	 * @param tno 车队编号
	 * @param wr 微信公众号菜单进入用户角色[普通用户-CUS_PT(默认); 车队计调-TEAM_JD; 车队业务员-TEAM_YW; 后台用户-BACK_ADMIN;]
	 * @param state 跳转地址状态码：114-普通用户登录地址；否则-其他地址（例如分享地址）；
	 */
	@RequestMapping(value="wxAuthority")
	public void wxAuthority(HttpServletRequest request, HttpServletResponse response, String state, String tno, String wr){
		wxPublicDataSer.wxAuthority(request, response, state, tno, wr);
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
				redirectUrl = QC.PRO_URL + QC.ERROR_PAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "redirect:"+redirectUrl;
   	}
    
    @ApiOperation(
		value="获取-用户登录信息",
		notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 数据（uuid、登录用户手机号、登录用户角色、登录用户所在单位编号）}"
	)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@Log("获取-用户登录信息")
	@RequestMapping(value="getLUser", method=RequestMethod.POST)
	public void getLUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = customerSer.findLUser(ReqSrc.WX, response, request);
		
		Message.print(response, map);
	}
    
    @ApiOperation(
		value="绑定微信公众号",
		notes="返回map{code: 结果状态码, msg: 结果状态码说明}"
	)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@Log("绑定微信公众号")
	@RequestMapping(value="setMainWx", method=RequestMethod.POST)
	public void setMainWx(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject json) {
    	String smsCode = U.P(json, "smsCode");
    	
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = wxBaseUserSer.updMainWx(ReqSrc.WX, lunitNum, luname, smsCode);
		
		Message.print(response, map);
	}
	
}
