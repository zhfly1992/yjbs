package com.fx.service.impl.wxdat;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.CookieUtil;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.CustomerDao;
import com.fx.dao.wxdat.WxIdDao;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.wxdat.WxId;
import com.fx.service.wxdat.WxIdService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class WxIdServiceImpl extends BaseServiceImpl<WxId,Long> implements WxIdService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
    private RedisUtil redis;
	@Autowired
	private WxIdDao wxIdDao;
	@Override
	public ZBaseDaoImpl<WxId, Long> getDao() {
		return wxIdDao;
	}
	/** 用户-服务 */
	@Autowired
	private CustomerDao cusDao;
	
	
	/*======移动端--begin==========*/
	@Override
	public WxId addWxId(Customer lcus, String teamNo, String openid) {
		String logtxt = U.log(log, "添加-微信绑定信息");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		WxId wd = null;
		
		try {
			/*if(fg){
				if(lcus == null){
					fg = U.setPutFalse(map, "[绑定用户]不能为空");
				}else if(StringUtils.isEmpty(lcus.getUid())){
					U.log(log, "uid为空，指还未绑定过微信信息");
				}
			}*/
			
			if(fg){
				if(StringUtils.isEmpty(openid)){
					openid = null;// 不存在openid，则赋值空
				}
			}
			
			/*if(fg){
				if(StringUtils.isEmpty(lcus.getUid())){
					String uid = U.getUniqueMbName();
					
					U.log(log, "[新生成uid] uid="+uid);
					
					lcus = cusDao.findByField("cName", lcus.getcName());
					lcus.setUid(uid);
					lcus.setOpenId(openid);
					cusDao.update(lcus);
					U.log(log, "更新用户uid、openid完成");
					
					// 为注册用户绑定微信id
					wd = new WxId();
					wd.setUid(uid);
					wd.setTeamNo(teamNo);
					wd.setWxid(openid);
					wd.setAtime(new Date());
					wxIdDao.save(wd);
					U.log(log, "为用户["+lcus.getPhone()+"]绑定微信id-成功");
				}else{
					hql = "from WxId where teamNo = ? and uid = ?";
					wd = wxIdDao.findObj(hql, teamNo, lcus.getUid());
					U.log(log, "获取-已绑定的微信信息对象");
				}
			}*/
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return wd;
	}
	
	@Override
	public List<WxId> getWxIdByUid(String uid) {
		U.log(log, "获取-用户["+uid+"]的微信id列表");
		boolean fg = true;
		String hql = "";
		
		List<WxId> wxids = new ArrayList<WxId>();
		try{
			if(fg){
				if(StringUtils.isEmpty(uid)){
					U.log(log, "用户id为空");
				}else{
					uid = uid.trim();
					hql = "from WxId where uid = ? order by atime asc";
					wxids = wxIdDao.findhqlList(hql, uid);
					if(wxids.size() == 0){
						U.log(log, "用户["+uid+"]没有绑定任何微信");
					}
				}
			}
		} catch (Exception e) {
			U.log(log, "获取-用户["+uid+"]的微信id列表", e);
			e.printStackTrace();
		}
		
		return wxids;
	}
	
	@Override
	public WxId isExist(String uid, String field) {
		U.log(log, "判断-用户["+uid+"]是否存在指定微信id/车队编号["+field+"]");
		WxId x = null;
		
		try{
			List<WxId> wxids = this.getWxIdByUid(uid);
			if(wxids.size() > 0){
				for(int i  = 0; i < wxids.size(); i++){
					if(wxids.get(i).getWxid().equals(field) || 
						wxids.get(i).getUnitNum().equals(field)){
						x = wxids.get(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			U.log(log, "判断-用户["+uid+"]是否存在指定微信id/车队编号["+field+"]", e);
			e.printStackTrace();
		}
		
		return x;
	}
	
	@Override
	public Map<String, Object> findWxId(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response) {
		String logtxt = U.log(log, "获取-登录用户微信绑定信息");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			String teamNo = "";
			if(fg){
				WxBaseUser lwx = LU.getLWx(request, redis);
				if(lwx == null){
					U.log(log, "登录缓存已失效");
					
					fg = U.setPutFalse(map, 0, "登录已过期，请重新登录");
				}else{
					lwx.getCompanyNum();
				}
			}
			
			String uid = "";
			/*if(fg){
				uid = LU.getLUid(request);
				hql = "from WxId where teamNo = ? and uid = ?"; 
				WxId wd = wxIdDao.findObj(hql, teamNo, uid);
				if(wd == null){
					U.log(log, "登录用户未绑定微信");
					
					U.setPut(map, 0, "登录用户未绑定微信");
				}else{
					map.put("data", wd);
					
					U.setPut(map, 1, "获取成功");
				}
			}*/
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updLuserLnglat(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String lname, String teamNo, String lnglat) {
		String logtxt = U.log(log, "更新-用户的地理位置坐标");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			String uid = null;
			/*if(fg){
				if(StringUtils.isEmpty(lname)){
					fg = U.setPutFalse(map, "[用户账号]不能为空");
				}else{
					lname = lname.trim();
					Customer cus = cusDao.findByField("cName", lname);
					if(cus == null){
						fg = U.setPutFalse(map, "[用户]不存在");
					}else{
						uid = cus.getUid();
					}
					
					U.log(log, "[用户账号] lname="+lname);
				}
			}*/
			
			WxId wx = null;
			if(fg){
				if(StringUtils.isEmpty(teamNo)){
					fg = U.setPutFalse(map, "[用户所属车队编号]不能为空");
				}else{
					teamNo = teamNo.trim();
					
					hql = "from WxId where teamNo = ? and uid = ?";
					wx = wxIdDao.findObj(hql, teamNo, uid);
					if(wx == null){
						fg = U.setPutFalse(map, "[用户]未绑定微信");
					}
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(lnglat)){
					fg = U.setPutFalse(map, "[地点坐标]不能为空");
				}else{
					U.log(log, "地点坐标：lnglat="+lnglat);
					
					lnglat = lnglat.trim();
					if(lnglat.indexOf("|") == -1){
						fg = U.setPutFalse(map, "[地点坐标]格式错误");
					}else{
						// 坐标经纬度保留小数点后6位
						String[] p = lnglat.split("\\|");
						lnglat = MathUtils.saveBit(p[0], 6) + "|" + MathUtils.saveBit(p[1], 6);
					}
				
					U.log(log, "地点坐标：lnglat="+lnglat);
				}
			}
			
			if(fg){
				if(!lnglat.equals(wx.getLgLonLat())){
					wx.setLgLonLat(lnglat);
					wxIdDao.update(wx);
					
					U.setPut(map, 1, "更新位置坐标成功");
				}else{
					U.setPut(map, 1, "位置坐标未变动");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updSetMainWx(ReqSrc reqsrc, HttpServletRequest request, 
		HttpServletResponse response, String teamNo, String phone, String smsCode) {
		String logtxt = U.log(log, "设置-当前微信为当前账号的主微信");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			String uuid = null;
			if(fg){
				uuid = U.getUUID(request);
				if(StringUtils.isEmpty(uuid)){
					uuid = CookieUtil.getUUID(request, null);
				}
				
				if(StringUtils.isEmpty(uuid)){
					fg = U.setPutFalse(map, "[登录uuid]不能为空");
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(teamNo)){
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else{
					teamNo = teamNo.trim();
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(phone)){
					fg = U.setPutFalse(map, "发送短信验证码手机号不能为空");
				}else{
					phone = phone.trim();
					if(!FV.isPhone(phone)){
						fg = U.setPutFalse(map, FV.match_phone_false_msg);
					}
					
					U.log(log, "发送短信验证码手机号：phone="+phone);
				}
			}
			
			if(fg){
				if(StringUtils.isNotEmpty(smsCode)){
					// 无论第一次绑定还是更换手机绑定，均获取新手机号短信验证码
					Item item = (Item)redis.get(phone);
					if(item == null){
						fg = U.setPutFalse(map, "请先获取手机动态验证码");
					}else{
						if(!item.getVal().toString().equals(smsCode)){
							fg = U.setPutFalse(map, "短信验证码错误");
						}
					}
					
					U.log(log, "短信验证码：smsCode="+smsCode);
				}else{
					fg = U.setPutFalse(map, "手机短信验证码不能为空");
				}
			}
			
			String uid = null;
			/*Customer lcus = null;
			if(fg){
				lcus = cusDao.findByField("cName", phone);
				if(lcus == null){
					fg = U.setPutFalse(map, "用户["+phone+"]不存在");
				}else{
					if(StringUtils.isEmpty(lcus.getUid())){
						fg = U.setPutFalse(map, "用户["+phone+"]还未首次登录");
					}else{
						uid = lcus.getUid();
					}
					
					U.log(log, "[用户id] uid="+uid);
				}
			}*/
			
			WxId wx = null;
			if(fg){
				hql = "from WxId where uid = ? and teamNo = ?";
				wx = wxIdDao.findObj(hql, uid, teamNo);
				if(wx == null){
					fg = U.setPutFalse(map, "用户["+phone+"]还未首次登录");
				}else{
					if(wx.getWxid().equals(wx.getLwxid())){
						U.log(log, "该账号已绑定当前微信，此处重新绑定");
						//fg = U.setPutFalse(map, "该账号已绑定当前微信，请勿重复操作");
					}
				}
			}
			
			if(fg){
				wx.setWxid(wx.getLwxid());// 将微信id变为当前登录微信id
				wx.setAtime(new Date());// 重新设置添加时间，则登录默认以最近时间为默认登录账号
				wxIdDao.update(wx);
				U.log(log, "更新-微信绑定信息-完成");
				
				/*lcus.setOpenId(wx.getLwxid());
				cusDao.update(lcus);*/
				U.log(log, "更新-用户信息-openid-完成");
				
				// 清空缓存保存的验证码
				redis.del(phone);
				
				U.setPut(map, 1, "绑定成功，需要重新登录");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/****移动端--end*************************/
	
}
