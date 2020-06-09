package com.fx.service.impl.cus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.clazz.WxUserInfo;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.RegWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.Sex;
import com.fx.commons.utils.enums.UState;
import com.fx.commons.utils.other.AddressUtil;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.IPUtil;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.other.WeiXinUtil;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.dao.cus.CustomerDao;
import com.fx.dao.cus.WxBaseUserDao;
import com.fx.dao.log.LoginLogDao;
import com.fx.dao.wxdat.WxPublicDataDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.log.LoginLog;
import com.fx.entity.wxdat.WxPublicData;
import com.fx.service.cus.CustomerService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 登录日志-数据源 */
	@Autowired
	private LoginLogDao loginLogDao;
	
	/** 用户基类-数据源 */
	@Autowired
	private BaseUserDao baseUserDao;
	
	/** 个人用户-数据源 */
	@Autowired
	private CustomerDao customerDao;
	
	/** 单位用户-数据源 */
	@Autowired
	private CompanyUserDao cuDao;
	
	@Autowired
    private RedisUtil redis;
	
	@Override
	public ZBaseDaoImpl<Customer, Long> getDao() {
		return customerDao;
	}
	
	/** 微信用户基类-服务 */
	@Autowired
	private WxBaseUserDao wxBaseUserDao;
	/** 微信公众号数据-服务 */
	@Autowired
	private WxPublicDataDao wxPublicDataDao;
	
	
	@Override
	public Map<String, Object> wxAuthCallBack(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response) {
		String logtxt = U.log(log, "用户-微信授权自动登录-回调方法");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			// 设置编码
			request.setCharacterEncoding("utf-8");
    		response.setCharacterEncoding("utf-8");
    		
    		String code = request.getParameter("code");		// 获取微信id所需code字符串
    		String state = request.getParameter("state");	// 跳转地址状态码
    		String ps = request.getParameter("ps");			// 参数map字符串
    		
    		U.log(log, "参数code:"+code);
    		U.log(log, "参数state="+state+", ps="+ps);
    		
    		String redirectUrl = QC.PRO_URL;// 跳转地址
    		List<String> plist = new ArrayList<String>();
    		
    		if(fg){
    			if(StringUtils.isEmpty(code)){
    				fg = U.setPutFalse(map, 401, "微信网页授权失败：未获取code");
    			}else{
    				code = code.trim();
    				
    				U.log(log, "微信网页授权返回参数：code="+code);
    			}
    		}
    		
    		if(fg){
    			if(StringUtils.isEmpty(state)){
    				fg = U.setPutFalse(map, 401, "微信网页授权失败：未获取state");
    			}else{
    				state = state.trim();
    				
    				U.log(log, "微信网页授权返回参数：state="+state);
    			}
    		}
    		
    		JsonNode jps = null;
    		if(fg) {
    			if(StringUtils.isEmpty(ps)) {
    				U.log(log, "没有传入参数");
    			}else {
    				ObjectMapper mapper = new ObjectMapper();
    				jps = mapper.readTree(ps);
    				
    				U.log(log, "所有参数 jps="+jps.toString());
    			}
    		}
    		
    		String teamNo = "";
    		if(fg){
    			teamNo = U.Cq(jps, "teamNo");
    			if(StringUtils.isEmpty(teamNo)){
    				fg = U.setPutFalse(map, "微信网页授权失败：未获取[车队编号]");
    			}else{
    				teamNo = teamNo.trim();
    				plist.add("tno="+teamNo);
    				
    				U.log(log, "用户公众号菜单传入参数-车队编号：teamNo="+teamNo);
    			}
    			
    			U.log(log, "用户公众号菜单传入参数-车队编号：teamNo="+teamNo);
    		}
    		
    		CusRole _wrole = null;
    		if(fg){
    			String wrole = U.Cq(jps, "lrole");
    			
    			if(StringUtils.isEmpty(wrole)){
    				fg = U.setPutFalse(map, "微信网页授权失败：未获取[用户角色]");
    			}else{
    				wrole = wrole.trim();
    				if(!FV.isOfEnum(CusRole.class, wrole)){
    					fg = U.setPutFalse(map, "微信网页授权失败：[用户角色]错误");
    				}else{
    					_wrole = CusRole.valueOf(wrole);
    					plist.add("r="+wrole);
    				}
    				
    				U.log(log, "用户公众号菜单传入参数-用户角色：wrole="+wrole);
    			}
    		}
    		
//			String openId = "o33IJwQa0RBFDlU31xPGqduBewCc";
//			plist.add("oid="+openId);
			
    		String openId = "";
    		JsonNode auth = null;
    		if(fg){
    			WxPublicData wpd = wxPublicDataDao.findByField("companyNum", teamNo);
    			if(wpd != null) {
    				auth = WeiXinUtil.getOpenId(code, wpd.getWxAppId(), wpd.getWxAppSecret());
    			}else {
    				auth = WeiXinUtil.getOpenId(code, QC.DEF_APPID, QC.DEF_SECRET);
    			}
    			
    			if(auth == null){
    				fg = U.setPutFalse(map, "微信网页授权失败：openId为空");
    			}else{
    				openId = U.Cq(auth, "openid");
    				plist.add("oid="+openId);
    				
    				U.log(log, "微信网页授权返回参数：openId="+openId);
    			}
    		}
    		
    		if(fg) {
        		if("114".equals(state)) {// 自动注册、登录
        			
        		}
    		}
    		
    		WxBaseUser lwxuser = null;
    		if(fg) {
    			lwxuser = wxBaseUserDao.findWxUser2(teamNo, openId);
    			if(lwxuser == null) {
    				U.log(log, "【用户未在此公众号上绑定，则根据角色跳转到对应登录页面】");
    				// 地址带上：当前用户微信id、当前车队编号、当前用户访问的角色
    				redirectUrl = Util.getRouteUrlByCusRole(_wrole, 0)+"?"+StringUtils.join(plist, "&");
    			}else {
    				U.log(log, "【用户已注册，则修改用户信息】");
    				
    				if(fg) {
    					MapRes mr = customerDao.valUserRole(_wrole, teamNo, lwxuser.getUname());
    					if(mr.getCode() <= 0) fg = U.setPutFalse(map, mr.getMsg());
    				}
    				
    				if(fg) {
    					long day = 7;
        				if(lwxuser.getLgTime() != null) day = DateUtils.getDaysOfTowDiffDate(new Date(), lwxuser.getLgTime());
        				
        				// 如果一周未登录/登录信息不存在，则需要更新微信用户信息
        				if(day >= 7) {
        					WxUserInfo wxu = UT.getWxUserInfo(U.Cq(auth, "access_token"), openId);
        					if(wxu != null) {
        						U.log(log, "用户已有"+day+"天没有登录，则更新用户基类信息");
        						
        						BaseUser bu = baseUserDao.findByUname(lwxuser.getUname());
        						if(bu != null) {
        							bu.setNickName(wxu.getNickname());
        							bu.setSex(Sex.valueOf(wxu.getSex()));
        							bu.setHeadImg(wxu.getHeadimgurl());
        							baseUserDao.update(bu);
        							U.log(log, "修改-用户（头像、昵称）信息-完成");
        						}
        					}
        				}
        				
        				// 根据微信id和车队编号-自动登录
        				map = customerDao.wxAutoLogin(request, response, null, _wrole.name(), teamNo, lwxuser);
            			MapRes mr = U.mapRes(map);
            			if(mr.getCode() > 0) {
            				plist.add("uuid="+map.get("uuid").toString());
            				
            				// 自动登录成功后，跳转到指定的主页
                			redirectUrl = Util.getRouteUrlByCusRole(_wrole, 1)+"?uuid="+map.get("uuid").toString(); //StringUtils.join(plist, "&");
            			}else {
            				fg = U.setPutFalse(map, "[自动登录]错误");
            			}
    				}
    				
    			}
    		}
    		
    		if(fg) {
    			// 最终跳转地址
    			map.put("redirectUrl", redirectUrl);
    			U.log(log, "跳转地址："+redirectUrl);
    			
    			U.setPut(map, 1, "授权成功");
    		}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findCusDetail(ReqSrc reqsrc, String idOrUnameOrPhone) {
		String logtxt = U.log(log, "获取-用户-详情", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			Customer cus = null;
			if(fg) {
				if(StringUtils.isEmpty(idOrUnameOrPhone)) {
					fg = U.setPutFalse(map, "[用户id/用户名/手机号]不能为空");
				}else {
					idOrUnameOrPhone = idOrUnameOrPhone.trim();
					if(FV.isLong(idOrUnameOrPhone)) {
						U.log(log, "传入参数是id："+idOrUnameOrPhone);
						cus = customerDao.findByField("id", Long.parseLong(idOrUnameOrPhone));
					}else {
						U.log(log, "传入参数是用户名/手机号："+idOrUnameOrPhone);
						
						String uname = baseUserDao.findUname(idOrUnameOrPhone);
						if(uname != null) cus = customerDao.findByField("baseUserId.uname", uname);
					}
					
					if(cus == null) {
						fg = U.setPutFalse(map, "该[用户]不存在");
					}
				}
			}
			
			if(fg) {
				map.put("data", cus);
				
				U.setPut(map, 1, "获取-用户详情-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findCusList(ReqSrc reqsrc, String page, String rows, String find) {
		String logtxt = U.log(log, "获取-用户-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "用户");
			/*****参数--验证--end******/
			
			if(fg) {
				if(StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				}else {
					find = find.trim();
					
					U.log(log, "[查询关键字] find="+find);
				}
			}
			
			if(fg){
				Page<Customer> pd = customerDao.findCusList(reqsrc, page, rows, find);
				U.setPageData(map, pd);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				
				U.setPut(map, 0, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Customer findByName(String uname) {
		String logtxt = U.log(log, "获取-用户");
		
		Customer obj = null;
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(uname)) {
					U.log(log, "[用户名]为空");
				}else {
					uname = uname.trim();
					
					U.log(log, "[用户名] uname="+uname);
				}
			}
			
			if(fg) {
				obj = customerDao.findByField("baseUserId.uname", uname);
				
				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return obj;
	}
	
	@Override
	public Map<String, Object> subCompanyRegister(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode) {
		String logtxt = U.log(log, "个人用户注册");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(lphone)) {
					fg = U.setPutFalse(map, "[登录手机号]不能为空");
				}else {
					lphone = lphone.trim();
					if(!FV.isPhone(lphone)) {
						fg = U.setPutFalse(map, "[登录手机号]格式错误");
					}
					
					U.log(log, "[登录手机号] lphone="+lphone);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lpass)) {
					fg = U.setPutFalse(map, "[登录密码]不能为空");
				}else {
					lpass = lpass.trim();
					if(!FV.isLPass(lpass)) {
						fg = U.setPutFalse(map, "[登录密码]格式错误");
					}
					
					U.log(log, "[登录密码] lpass="+lpass);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(imgCode)) {
					fg = U.setPutFalse(map, "[图片验证码]不能为空");
				}else {
					imgCode = imgCode.trim();
					if(imgCode.length() > QC.IMG_CODE_LEN) {
						fg = U.setPutFalse(map, "[图片验证码]长度为"+QC.IMG_CODE_LEN);
					}
					
					U.log(log, "[图片验证码] imgCode="+imgCode);
				}
			}
			
			if(fg) {
			     String imgCode_session = (String) request.getSession().getAttribute(QC.IMG_CODE);
			     if(StringUtils.isEmpty(imgCode_session)) {
			    	 fg = U.setPutFalse(map, "请先获取[图片验证码]");
			     }else {
			    	 imgCode_session = imgCode_session.trim();
			    	 if(!imgCode_session.equalsIgnoreCase(imgCode)) {
			    		 fg = U.setPutFalse(map, "输入的[图片验证码]错误");
			    	 }
			    	 
			    	 U.log(log, "session中的[图片验证码] imgCode_session="+imgCode_session);
			     }
			}
			
			BaseUser luser = null;
			if(fg) {
				luser = baseUserDao.findByField("phone", lphone);
				if(luser != null) {
					fg = U.setPutFalse(map, "用户["+lphone+"]已存在");
				}
			}
			
			if(fg) {
				// 清除session中保存的验证码（在此处清楚最合适）
	    		request.getSession().removeAttribute(QC.IMG_CODE);
				
	    		// 重新生成加密登录
	            PasswordHelper passHelper = new PasswordHelper();
	    		
	            luser = new BaseUser();
	            luser.setUname(UT.createUname());
	            luser.setPhone(lphone);
	            luser.setRealName("匿名");
	            luser.setSalt(luser.getUname());
	            luser.setLpass(passHelper.encryptPassword(lpass, luser.getSalt()));
	            luser.setRegWay(RegWay.PC_COMPANY);
	            luser.setUstate(UState.NORMAL);
	            luser.setAtime(new Date());
	            baseUserDao.save(luser);
	            U.log(log, "保存-用户基类-完成");
	            
	            Customer lcus = new Customer();
	            lcus.setBaseUserId(luser);
	            lcus.setPayPass(passHelper.encryptPassword(QC.DEF_PAY_PASS, luser.getSalt()));
	            lcus.setRecBaseUserId(null);
	            lcus.setRecId("0");
	            customerDao.save(lcus);
	            U.log(log, "保存-用户信息-完成");
	            
	            // 跳转地址
	            map.put("goUrl", "/page/company/login");
	            
	            U.setPut(map, 1, "注册成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}

	@Override
	public Map<String, Object> subCompanyLogin(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe) {
		String logtxt = U.log(log, "个人/单位用户登录:手机号="+lphone+",密码="+lpass+",记住="+remberMe);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*if(fg) {
				if(StringUtils.isEmpty(imgCode)) {
					fg = U.setPutFalse(map, "[验证码]不能为空");
				}else {
					imgCode = imgCode.trim();
					if(imgCode.length() > QC.IMG_CODE_LEN) {
						fg = U.setPutFalse(map, "[验证码]长度为"+QC.IMG_CODE_LEN);
					}else {
						 String imgCode_session = (String) request.getSession().getAttribute(QC.IMG_CODE);
					     if(StringUtils.isEmpty(imgCode_session)) {
					    	 fg = U.setPutFalse(map, "请先获取[验证码]");
					     }else {
					    	 imgCode_session = imgCode_session.trim();
					    	 if(!imgCode_session.equalsIgnoreCase(imgCode)) {
					    		 fg = U.setPutFalse(map, "输入的[验证码]错误");
					    	 }
					    	 U.log(log, "session中的[图片验证码] imgCode_session="+imgCode_session);
					     }
					}
				}
			}*/
			
			BaseUser luser = null;
			if(fg) {
				if(StringUtils.isEmpty(lphone)) {
					fg = U.setPutFalse(map, "[登录手机号]不能为空");
				}else {
					lphone = lphone.trim();
					if(!FV.isPhone(lphone)) {
						fg = U.setPutFalse(map, "[登录手机号]格式错误");
					}else {
						luser = baseUserDao.findByField("phone", lphone);
						if(luser == null) {
							fg = U.setPutFalse(map, "用户["+lphone+"]不存在");
						}else {
							U.log(log, "[登录手机号] lphone="+lphone);
						}
					}
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lpass)) {
					fg = U.setPutFalse(map, "[登录密码]不能为空");
				}else {
					lpass = lpass.trim();
					if(!FV.isLPass(lpass)) {
						fg = U.setPutFalse(map, "[登录密码]格式错误");
					}else {
						// 重新生成加密登录
			            PasswordHelper passHelper = new PasswordHelper();
						if(!passHelper.encryptPassword(lpass, luser.getSalt()).equals(luser.getLpass())) {
							fg = U.setPutFalse(map, "[登录密码]不正确");
						}else {
							U.log(log, "[登录密码] lpass="+lpass);
						}
					}
				}
			}
			
			boolean _remberMe = false;
			if(fg) {
				if(StringUtils.isEmpty(remberMe)) {
					U.log(log, "[记住账号]为空");
				}else {
					remberMe = remberMe.trim();
					if(!FV.isBoolean(remberMe)) {
						fg = U.setPutFalse(map, "[记住账号]格式错误");
					}else {
						_remberMe = Boolean.parseBoolean(remberMe);
					}
					
					U.log(log, "[记住账号] remberMe="+remberMe);
				}
			}
			
			if(fg) {
				// 清除session中保存的验证码（在此处清除最合适）
	    		//request.getSession().removeAttribute(QC.IMG_CODE);
				
				UsernamePasswordToken token = new UsernamePasswordToken(luser.getUname(), lpass);
				if(_remberMe == true) {
					token.setRememberMe(_remberMe);
				}
				
	            Subject subject = SecurityUtils.getSubject();
	            if(subject != null) subject.logout(); // 已登录，则先退出
	            
	            subject.login(token);
	            U.log(log, "token: "+token);
	            U.log(log, "是否登录==>"+subject.isAuthenticated());
	            
	            String uuid = subject.getSession().getId().toString();
	            
	            // 保存uuid给前端
	            map.put(QC.UUID, uuid);
	            
	            Map<String, Object> mapUser = new HashMap<String, Object>();//缓存登录用户信息map
	            
	            CompanyUser cu = cuDao.findByField("baseUserId.uname", luser.getUname());
	            if(cu != null){
	            	map.put("company", cu);
	            	// 字段过滤
					Map<String, Object> fmap = new HashMap<String, Object>();
					fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
					fmap.put(U.getAtJsonFilter(CompanyUser.class), new String[]{});
					map.put(QC.FIT_FIELDS, fmap);
					
					mapUser.put(QC.L_COMPANY, cu);
	            }else {
	            	map.put("company", "");
	            }
	            
	            // 记录登录日志
	            LoginLog llog = new LoginLog();
	            llog.setOperIp(IPUtil.getIpAddr(request));// 获取HTTP请求
	            llog.setUname(luser.getUname());
	            llog.setOperLocation(AddressUtil.getAddress(llog.getOperIp()));
	            llog.setAtime(new Date());
	            llog.setOperDevice(UT.getReqUA(request));
	            loginLogDao.save(llog);
	            U.log(log, "保存-登录日志-完成");
	            
	            // 跳转-主页
	            //map.put("goUrl", "/page/company/goMain");
	            
	            // 缓存登录用户信息
	            Customer lcus = customerDao.findByField("baseUserId.uname", luser.getUname());
				mapUser.put(QC.L_USER, lcus);
				mapUser.put(QC.L_TIME, DateUtils.DateToStr(new Date()));
				redis.set(uuid, mapUser);
				
				U.setPut(map, 1, "登录单位成功");
	            
				
				U.log(log, "登录uuid："+uuid);
//				Map<String, Object> res = (Map<String, Object>)redis.get(uuid);
//				U.log(log, res.get(QC.L_TIME).toString());
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		//相应的异常
		//DisabledAccountException（禁用的帐号）
		//LockedAccountException（锁定的帐号）
		//UnknownAccountException（错误的帐号）
		//ExcessiveAttemptsException（登录失败次数过多）
		//IncorrectCredentialsException （错误的凭证）
		//ExpiredCredentialsException（过期的凭证）
		
		return map;
	}
	
	@Override
	public Map<String, Object> findLCus(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request) {
		String logtxt = U.log(log, "获取-登录用户信息");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				BaseUser luser = (BaseUser) SecurityUtils.getSubject().getPrincipal();
				Customer lcus = findByName(luser.getUname());
				
				if(lcus == null) {
					fg = U.setPutFalse(map, "登录已过期，请重新登录");
				}else {
					map.put("data", lcus);
					
					// 字段过滤
					Map<String, Object> fmap = new HashMap<String, Object>();
					fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
					map.put(QC.FIT_FIELDS, fmap);
					
					U.setPut(map, 1, "获取登录用户信息成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> subDriverPassLogin(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, 
		CusRole role, String wxid, String teamNo, String lphone, String lpass, String remberMe) {
		String logtxt = U.log(log, "驾驶员用户-手机号密码登录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
    			if(role == null) {
    				fg = U.setPutFalse(map, "[登录用户角色]不能为空");
    			}else {
    				U.log(log, "[登录用户角色] role="+role.getKey());
    			}
    		}
			
			CompanyUser cu = null;
			if(fg){
				if(StringUtils.isEmpty(teamNo)){
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else{
					teamNo = teamNo.trim();
					cu = cuDao.findByField("unitNum", teamNo);
    				if(cu == null){
    					fg = U.setPutFalse(map, "当前车队不存在，请通过微信公众号菜单访问");
    				}
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			// 手机号密码登录：以手机号、密码为准，根据车队编号和手机号对应用户名查询用户是否绑定当前微信公众号,
			// 如未绑定，则新绑定，如已绑定，则更新当前登录微信id
			
			if(fg){
				if(StringUtils.isEmpty(wxid)){
					fg = U.setPutFalse(map, "微信授权失败，请通过微信公众号菜单访问");
				}else{
					wxid = wxid.trim();
					
					U.log(log, "[用户微信id] wxid="+wxid);
				}
			}
			
			BaseUser luser = null;
			if(fg) {
				if(StringUtils.isEmpty(lphone)) {
					fg = U.setPutFalse(map, "[登录手机号]不能为空");
				}else {
					lphone = lphone.trim();
					if(!FV.isPhone(lphone)) {
						fg = U.setPutFalse(map, "[登录手机号]格式错误");
					}else {
						luser = baseUserDao.findByField("phone", lphone);
						if(luser == null) {
							fg = U.setPutFalse(map, "用户["+lphone+"]未注册，请向车队索要账号");
						}else {
							U.log(log, "[登录手机号] lphone="+lphone);
						}
					}
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lpass)) {
					fg = U.setPutFalse(map, "[登录密码]不能为空");
				}else {
					lpass = lpass.trim();
					if(!FV.isLPass(lpass)) {
						fg = U.setPutFalse(map, "[登录密码]格式错误");
					}else {
						// 重新生成加密登录
			            PasswordHelper passHelper = new PasswordHelper();
						if(!passHelper.encryptPassword(lpass, luser.getSalt()).equals(luser.getLpass())) {
							fg = U.setPutFalse(map, "[登录密码]不正确");
						}else {
							U.log(log, "[登录密码] lpass="+lpass);
						}
					}
				}
			}
			
			boolean _remberMe = false;
			if(fg) {
				if(StringUtils.isEmpty(remberMe)) {
					U.log(log, "[记住账号]为空");
				}else {
					remberMe = remberMe.trim();
					if(!FV.isBoolean(remberMe)) {
						fg = U.setPutFalse(map, "[记住账号]格式错误");
					}else {
						_remberMe = Boolean.parseBoolean(remberMe);
					}
					
					U.log(log, "[记住账号] remberMe="+remberMe);
				}
			}
			
			Customer lcus = null;
			if(fg) {
				lcus = customerDao.findByField("baseUserId.uname", luser.getUname());
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户信息]不存在，请联系管理员");
				}
			}
			
			if(fg) {
				MapRes mr = customerDao.valUserRole(role, teamNo, luser.getUname());
				if(mr.getCode() <= 0) fg = U.setPutFalse(map, mr.getMsg());
			}
			
			if(fg) {
				map = customerDao.saveWxLoginDat(map, request, _remberMe, lcus, luser, cu, wxid, role);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findLDriverUser(String teamNo, String luname) {
		String logtxt = U.log(log, "获取-登录驾驶员用户信息");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean fg = true;
		
		try {
			CompanyUser lcomUser = null;
			if(fg) {
				if(StringUtils.isBlank(teamNo)) {
					fg = U.setPutFalse(map, "[登录车队编号]不能为空");
				}else {
					teamNo = teamNo.trim();
					lcomUser = cuDao.findByField("unitNum", teamNo);
					if(lcomUser == null) {
						fg = U.setPutFalse(map, "[登录车队]不存在");
					}
					
					U.log(log, "[登录车队编号] teamNo="+teamNo);
				}
			}
			
			BaseUser lbuser = null;
			if(fg) {
				if(StringUtils.isBlank(luname)) {
					fg = U.setPutFalse(map, "[登录用户名]不能为空");
				}else {
					luname = luname.trim();
					lbuser = baseUserDao.findByField("uname", luname);
					if(lbuser == null) {
						fg = U.setPutFalse(map, "登录用户基本信息异常，请联系管理员");
					}
					
					U.log(log, "[登录用户名] luname="+luname);
				}
			}
			
			WxBaseUser lwxuser = null;
			if(fg) {
				lwxuser = wxBaseUserDao.findWxUser(teamNo, lbuser.getUname());
				if(lwxuser == null) {
					fg = U.setPutFalse(map, "登录用户微信信息异常，请联系管理员");
				}else {
					U.log(log, "[登录用户微信id] wxid="+lwxuser.getWxid());
				}
			}
			
			if(fg) {
				Map<String, Object> luser = new HashMap<String, Object>();
				luser.put("uname", lbuser.getUname());
				luser.put("phone", lbuser.getPhone());
				luser.put("realName", lbuser.getRealName());
				luser.put("headImg", lbuser.getHeadImg() == null ? "" : lbuser.getHeadImg());
				luser.put("teamNo", lcomUser.getUnitNum());
				luser.put("teamName", lcomUser.getCompanyName());
				map.put("data", luser);
				
				U.setPut(map, 1, "获取-驾驶员登录信息-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findLUser(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request) {
		String logtxt = U.log(log, "获取-登录用户信息", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				WxBaseUser lwxuser = LU.getLWx(request, redis);
				CompanyUser lcomUser = LU.getLCompany(request, redis);
				
				Map<String, Object> luser = new HashMap<String, Object>();
				luser.put("wxid", lwxuser.getWxid());
				luser.put("role", lwxuser.getLgRole().name());
				luser.put("teamNo", lcomUser.getUnitNum());
				
				U.setPut(map, 1, "获取-登录信息-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
