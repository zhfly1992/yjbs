package com.fx.service.impl.back;

import java.util.Date;
import java.util.HashMap;
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

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.other.AddressUtil;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.IPUtil;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.back.SysUserDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.log.LoginLogDao;
import com.fx.entity.back.SysUser;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.log.LoginLog;
import com.fx.service.back.SysUserService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, Long> implements SysUserService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 管理员用户-数据源 */
	@Autowired
	private SysUserDao sysUserDao;
	@Override
	public ZBaseDaoImpl<SysUser, Long> getDao() {
		return sysUserDao;
	}
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	
	/** 登录日志-数据源 */
	@Autowired
	private LoginLogDao loginLogDao;
	
	
	@Override
	public Map<String, Object> passLogin(HttpServletResponse response, HttpServletRequest request, 
		String lphone, String lpass, String imgCode, String remberMe) {
		String logtxt = U.log(log, "管理员-用户密码登录");
		
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
//					fg = U.setPutFalse(map, "[图片验证码]不能为空");
				}else {
					imgCode = imgCode.trim();
					if(imgCode.length() > QC.IMG_CODE_LEN) {
						fg = U.setPutFalse(map, "[图片验证码]长度为"+QC.IMG_CODE_LEN);
					}
					
					U.log(log, "[图片验证码] imgCode="+imgCode);
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
			
//			if(fg) {
//			     String imgCode_session = (String) request.getSession().getAttribute(QC.IMG_CODE);
//			     if(StringUtils.isEmpty(imgCode_session)) {
//			    	 fg = U.setPutFalse(map, "请先获取[图片验证码]");
//			     }else {
//			    	 imgCode_session = imgCode_session.trim();
//			    	 if(!imgCode_session.equalsIgnoreCase(imgCode)) {
//			    		 fg = U.setPutFalse(map, "输入的[图片验证码]错误");
//			    	 }
//			    	 
//			    	 U.log(log, "session中的[图片验证码] imgCode_session="+imgCode_session);
//			     }
//			}
			
			SysUser backUser = null;
			BaseUser luser = null;
			if(fg) {
				luser = baseUserDao.findByPhone(lphone);
				if(luser == null) {
					fg = U.setPutFalse(map, "管理员基本信息["+lphone+"]不存在");
				}else {
					PasswordHelper passhelper = new PasswordHelper();
					if(!luser.getLpass().equals(passhelper.encryptPassword(lpass, luser.getUname()))) {
						fg = U.setPutFalse(map, "管理员[登录密码]不正确");
					}else{
						backUser = sysUserDao.findByField("baseUserId.uname", luser.getUname());
						if(backUser == null) {
							fg = U.setPutFalse(map, "管理员信息["+lphone+"]不存在");
						}else {
							U.log(log, "用户["+lphone+"-"+backUser.getRole().getText()+"]正在登录[后台管理系统]");
						}
					}
					
					U.log(log, "[登录用户名] uname="+luser.getUname());
				}
			}
			
			if(fg) {
				// 清除session中保存的验证码（在此处清楚最合适）
//	    		request.getSession().removeAttribute(QC.IMG_CODE);
	    		
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
	            
	            // 记录登录日志
	            LoginLog llog = new LoginLog();
	            llog.setOperIp(IPUtil.getIpAddr(request));// 获取HTTP请求
	            llog.setUname(luser.getUname());
	            llog.setOperLocation(AddressUtil.getAddress(llog.getOperIp()));
	            llog.setAtime(new Date());
	            llog.setOperDevice(UT.getReqUA(request));
	            loginLogDao.save(llog);
	            U.log(log, "保存-登录日志-完成");
	            
	            // 保存登录用户信息至缓存
	            Map<String, Object> mapUser = new HashMap<String, Object>();
				mapUser.put(QC.L_BACK_USER, backUser);
				mapUser.put(QC.L_ROLE, CusRole.BACK_ADMIN.name());
				mapUser.put(QC.L_TIME, DateUtils.DateToStr(new Date()));
				redis.set(uuid, mapUser);
	            
				// 保存uuid给前端
	            map.put(QC.UUID, uuid);
	            
	            U.setPut(map, 1, "登录后台成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findLSysUser(HttpServletResponse response, HttpServletRequest request) {
		String logtxt = U.log(log, "获取-管理员登录用户信息");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				BaseUser luser = (BaseUser) SecurityUtils.getSubject().getPrincipal();
				SysUser lsys = findByUname(luser.getUname());
				
				if(lsys == null) {
					fg = U.setPutFalse(map, "登录已过期，请重新登录");
				}else {
					Map<String, Object> res = new HashMap<String, Object>();
					res.put("uname", lsys.getBaseUserId().getUname());
					res.put("role", lsys.getRole().name());
					res.put("phone", lsys.getBaseUserId().getPhone());
					res.put("realName", lsys.getBaseUserId().getRealName());
					res.put("atime", lsys.getBaseUserId().getAtime());
					res.put("nickName", lsys.getNickName());
					res.put("headImg", lsys.getHeadImg());
					
					map.put("data", res);
					
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
	public SysUser findByUname(String uname) {
		String logtxt = U.log(log, "获取-管理员用户通过用户名");
		
		SysUser obj = null;
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(uname)) {
					fg = U.logFalse(log, "[用户名]为空");
				}else {
					uname = uname.trim();
					
					U.log(log, "[用户名] uname="+uname);
				}
			}
			
			if(fg) {
				obj = findByField("baseUserId.uname", uname);
				
				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return obj;
	}
	
}
