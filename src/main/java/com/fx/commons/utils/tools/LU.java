package com.fx.commons.utils.tools;


import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fx.commons.utils.other.CookieUtil;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WxBaseUser;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

/**
 * 登录用户信息-工具处理类
 */
public class LU extends BaseController {
	/** 日志记录 */
	public static Logger log = LogManager.getLogger(LU.class.getName());



	/**
	 * 获取-缓存中的登录用户map对象
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 * @return 登录用户map对象
	 */
	public  Map<String, Object> getLMap(HttpServletRequest request, RedisUtil redis) {
		String uuid = (String) request.getHeader("uuid"); // 请求获取uuid
		if (StringUtils.isEmpty(uuid)) {
			uuid = getUUID();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> loginKey = (Map<String, Object>) redis.get(uuid);

		return loginKey;
	}



	/**
	 * 获取-登录用户名
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static String getLUName(HttpServletRequest request, RedisUtil redis) {
		Customer cus = getLUSER(request, redis);
		if (cus != null) {
			return cus.getBaseUserId().getUname();
		}else {
			Staff staff=getLStaff(request, redis);
			if(staff!=null) return staff.getBaseUserId().getUname();
		}
		return null;
	}
	
	/**
	 * 获取-登录用户姓名
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static String getLRealName(HttpServletRequest request, RedisUtil redis) {
		Customer cus = getLUSER(request, redis);
		if (cus != null) {
			return cus.getBaseUserId().getRealName();
		}else {
			Staff staff=getLStaff(request, redis);
			if(staff!=null) return staff.getBaseUserId().getRealName();
		}
		return null;
	}




	/**
	 * 获取-登录用户基类信息
	 */
	public static BaseUser getLBuser() {
		return getCurrentUser();
	}



	/**
	 * 获取-登录单位信息
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static CompanyUser getLCompany(HttpServletRequest request, RedisUtil redis) {
		LU lu = new LU();
		Map<String, Object> loginKey = lu.getLMap(request, redis);
		if (loginKey != null) {
			return (CompanyUser) loginKey.get(QC.L_COMPANY);
		}
		return null;
	}



	/**
	 * 获取-登录用户信息
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static Customer getLUSER(HttpServletRequest request, RedisUtil redis) {
		LU lu = new LU();
		Map<String, Object> loginKey = lu.getLMap(request, redis);
		if (loginKey != null) {
			return (Customer) loginKey.get(QC.L_USER);
		}

		return null;
	}

	/**
	 * 获取-登录员工信息
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static Staff getLStaff(HttpServletRequest request, RedisUtil redis) {
		LU lu = new LU();
		Map<String, Object> loginKey = lu.getLMap(request, redis);
		if (loginKey != null) {
			return (Staff) loginKey.get(QC.L_STAFF);
		}

		return null;
	}

	/**
	 * 获取-登录单位编号
	 * 
	 * @param request
	 *            request
	 * @param redis
	 *            redis
	 */
	public static String getLUnitNum(HttpServletRequest request, RedisUtil redis) {
		return getLCompany(request, redis).getUnitNum();
	}



	/**
	 * 更换图片时删除原有图片
	 * 
	 * @param String
	 *            oldPic
	 * @author zh
	 * @date 2020-05-07
	 */
	public static boolean deletePic(String oldPic) {
		try {
			String[] split = oldPic.split("/");
			String dest = "C://upload/pic" + "/" + split[split.length - 1];
			File file = new File(dest);
			if (file.exists()) {
				file.delete();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * memcached获取登录用户微信绑定对象
	 * @return 用户微信绑定对象
	 */
	public static WxBaseUser getLWx(HttpServletRequest request ,RedisUtil redis){
		try {
			String uuid = request.getParameter(QC.UUID);
			if(StringUtils.isEmpty(uuid)){
				uuid = CookieUtil.getUUID(request, null);
			}
			U.log(log, "获取用户缓存信息：从request中获取uuid："+uuid);
			
			Object obj = redis.get(uuid);
			if(obj != null){
				U.log(log, "成功：获取用户缓存信息");
				U.log(log, U.toJsonStr(obj));
				
				@SuppressWarnings("unchecked")
				Map<String, Object> loginKey = (Map<String, Object>)obj;
				 
				return (WxBaseUser)loginKey.get(QC.L_WX);
			}else{
				U.log(log, "失败：获取用户缓存信息为空");
				return null;
			}
		} catch (Exception e) {
			U.log(log, "异常：获取用户缓存信息为空", e);
			e.printStackTrace();
			return null;
		}
	}

}
