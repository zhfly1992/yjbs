package com.fx.dao.cus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.entity.cus.BaseUser;

@Repository

public class BaseUserDao extends ZBaseDaoImpl<BaseUser, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());



	/**
	 * 获取-用户名
	 * 
	 * @param find
	 *            查询的手机号/用户名
	 * @return 用户名
	 */
	public String findUname(String find) {
		String logtxt = U.log(log, "获取用户名-通过用户名/手机号关键字");

		String uname = null;
		try {
			String hql = "from BaseUser where realName = ?0 or phone = ?1";
			BaseUser bu = findObj(hql, find, find);
			if (bu != null)
				uname = bu.getUname();
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return uname;
	}



	/**
	 * 获取-用户通过用户名
	 * 
	 * @param uname
	 *            用户名
	 * @return 用户对象
	 */
	public BaseUser findByUname(String uname) {
		String logtxt = U.log(log, "获取-用户通过用户名");

		BaseUser obj = null;
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(uname)) {
					fg = U.logFalse(log, "[用户名]为空");
				} else {
					uname = uname.trim();

					U.log(log, "[用户名] uname=" + uname);
				}
			}

			if (fg) {
				obj = findByField("uname", uname);

				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return obj;
	}



	/**
	 * 获取-用户通过手机号
	 * 
	 * @param phone
	 *            手机号
	 * @return 用户对象
	 */
	public BaseUser findByPhone(String phone) {
		String logtxt = U.log(log, "获取-用户通过手机号");

		BaseUser obj = null;
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(phone)) {
					fg = U.logFalse(log, "[手机号]为空");
				} else {
					phone = phone.trim();

					U.log(log, "[手机号] phone=" + phone);
				}
			}

			if (fg) {
				obj = findByField("phone", phone);

				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return obj;
	}



	/**
	 * 获取-用户名数组
	 * 
	 * @param find
	 *            查询关键字 用户名/手机号/真实姓名
	 * @return 用户名数组
	 */
	public Object[] findUnameArr(String find) {
		String logtxt = U.log(log, "获取用户名数组-通过用户名/手机号关键字");

		List<Object> unameArr = new ArrayList<Object>();
		try {
			String hql = "SELECT new BaseUser(uname) FROM BaseUser WHERE uname LIKE ?0 OR phone LIKE ?1 OR realName LIKE ?2";
			List<BaseUser> list = findObj(hql, "%" + find + "%", "%" + find + "%", "%" + find + "%");
			for (BaseUser bu : list) {
				unameArr.add(bu.getUname());
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return unameArr.toArray();
	}



/**
 * 
 * @Description:通过姓名或者手机号获取用户名 
 * @param find
 * @return
 * @author :zh
 * @version 2020年4月27日
 */
	public List<String> findUnameByUnameOrPhone(String find) {
		String logtxt = U.log(log, "获取用户名-通过姓名/手机号关键字");


		List<String> unameList = new ArrayList<>();
		try {
			String hql = "from BaseUser where realName = ?0 or phone = ?1";
			List<BaseUser> bu = findhqlList(hql, find, find);
			
			if (bu.size() != 0) {
				for(BaseUser baseUser:bu){
					unameList.add(baseUser.getUname());
				}
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return unameList;
	}

}
