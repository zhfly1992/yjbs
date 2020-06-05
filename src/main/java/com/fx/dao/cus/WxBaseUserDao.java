package com.fx.dao.cus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.WxBaseUser;

@Repository
public class WxBaseUserDao extends ZBaseDaoImpl<WxBaseUser, Long> {
	
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 用户基类-服务 */
	@Autowired
	private BaseUserDao baseUserDao;
	
	
	/**
	 * 获取-微信基类用户信息
	 * @param teamNo 	车队编号
	 * @param uname 	用户名
	 */
	public WxBaseUser findWxUser(String teamNo, String uname) {
		String logtxt = U.log(log, "获取-微信用户信息");
		
		String hql = "";
		WxBaseUser wxUser = null;
		
		try {
			hql = "from WxBaseUser where uname = ?0 and companyNum = ?1";
			wxUser = findObj(hql, uname, teamNo, "LIMIT 1");
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return wxUser;
	}
	
	/**
	 * 获取-微信基类用户信息
	 * @param teamNo 	车队编号
	 * @param phone 	用户手机号
	 */
	public WxBaseUser findWxUser1(String teamNo, String phone) {
		String logtxt = U.log(log, "获取-微信用户信息");
		
		String hql = "";
		WxBaseUser wxUser = null;
		
		try {
			BaseUser buser = baseUserDao.findByField("phone", phone);
			if(buser != null) {
				hql = "from WxBaseUser where uname = ? and companyNum = ?";
				wxUser = findObj(hql, buser.getUname(), teamNo, "LIMIT 1");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return wxUser;
	}
	
	/**
	 * 获取-微信基类用户信息
	 * @param teamNo 	车队编号
	 * @param wxid 		用户微信id
	 */
	public WxBaseUser findWxUser2(String teamNo, String wxid) {
		String logtxt = U.log(log, "获取-微信用户信息");
		
		String hql = "";
		WxBaseUser wxUser = null;
		
		try {
			hql = "from WxBaseUser where wxid = ? and companyNum = ?";
			wxUser = findObj(hql, wxid, teamNo, "LIMIT 1");
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return wxUser;
	}
	
}
