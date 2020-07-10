package com.fx.service.impl.cus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.cus.WxBaseUserDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.WxBaseUser;
import com.fx.service.cus.WxBaseUserService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class WxBaseUserServiceImpl extends BaseServiceImpl<WxBaseUser, Long> implements WxBaseUserService {
	
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 微信用户基类-数据源 */
	@Autowired
	private WxBaseUserDao wxBaseUserDao;
	@Override
	public ZBaseDaoImpl<WxBaseUser, Long> getDao() {
		return wxBaseUserDao;
	}
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	
	
	@Override
	public Map<String, Object> updMainWx(ReqSrc reqsrc, String lunitNum, String luname, String smsCode) {
		String logtxt = U.log(log, "设置-当前微信为当前账号的主微信");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			WxBaseUser lwxuser = null;
			if(fg) {
				lwxuser = wxBaseUserDao.findWxUser(lunitNum, luname);
				if(lwxuser == null) {
					fg = U.setPutFalse(map, "抱歉，你还未绑定公众号，请从公众号菜单访问");
				}else {
					U.log(log, "已绑定公众号：wxid="+lwxuser.getWxid()+", lwxid="+lwxuser.getLgWxid());
				}
			}
			
			BaseUser buser = null;
			if(fg) {
				buser = baseUserDao.findByUname(luname);
				if(buser == null) {
					fg = U.setPutFalse(map, "当前用户基本信息不存在，请联系管理员");
				}else {
					U.log(log, "登录用户已绑定手机号="+buser.getPhone());
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(smsCode)){
					fg = U.setPutFalse(map, "手机短信验证码不能为空");
				}else{
					// 无论第一次绑定还是更换手机绑定，均获取新手机号短信验证码
					Item it = (Item)redis.get(buser.getPhone());
					if(it == null){
						fg = U.setPutFalse(map, "请先获取手机动态验证码");
					}else{
						if(new Date().getTime() - Long.parseLong(it.getOther().toString()) > QC.SMS_CODE_SAVE_TIME*60*1000) {
							fg = U.setPutFalse(map, "短信验证码已过期，请重新获取");
						}else if(!it.getVal().toString().equals(smsCode)){
							fg = U.setPutFalse(map, "短信验证码错误");
						}
					}
					
					U.log(log, "短信验证码：smsCode="+smsCode);
				}
			}
			
			if(fg){
				lwxuser.setWxid(lwxuser.getLgWxid());	// 将微信id变为当前登录微信id
				lwxuser.setAtime(new Date());			// 重新设置添加时间，则登录默认以最近时间为默认登录账号
				wxBaseUserDao.update(lwxuser);
				U.log(log, "更新-微信绑定信息-完成");
				
				// 清空缓存保存的验证码
				redis.del(buser.getPhone());
				
				// 清除当前登录用户信息
				SecurityUtils.getSubject().logout();
				
				U.setPut(map, 1, "绑定成功，需要重新登录");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
