package com.fx.service.impl.cus.permi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.permi.RoleDao;
import com.fx.entity.cus.permi.Role;
import com.fx.service.cus.permi.RoleService;

@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass().getName());

	/** 用户角色-数据源 */
	@Autowired
	private RoleDao	roleDao;



	@Override
	public ZBaseDaoImpl<Role, Long> getDao() {
		return roleDao;
	}



	@Override
	public List<Role> findUserRole(String uname) {
		String logtxt = U.log(log, "获取-用户角色-列表");

		List<Role> list = new ArrayList<Role>();
		String hql = "";
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(uname)) {
					U.log(log, "[用户名]为空");
				} else {
					uname = uname.trim();

					U.log(log, "[用户名] uname=" + uname);
				}
			}

			if (fg) {
				hql = "FROM TbRole r " + "LEFT JOIN TbUserRole ur ON (r.id = ur.roleId) "
						+ "LEFT JOIN TbUser u ON (u.id = ur.userId) " + "LEFT JOIN tbRoleMenu rm ON (r.id = rm.roleId) "
						+ "LEFT JOIN TbMenu m ON (m.id = rm.menuId) " + "WHERE u.uname = ? " + "ORDER BY id asc";

				list = roleDao.findhqlList(hql, uname);

				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}

		return list;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> getRoleByDeptId(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String deptId) {
		String logtxt = U.log(log, "后台-角色-根据部门获取角色", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(deptId)) {
				U.logFalse(log, "后台-角色-根据部门获取角色-失败-部门id为空");
				U.setPutFalse(map, 0, "部门id为空");
			} else {
				Session session = roleDao.openSession();
				Query createQuery = session.createQuery("from Role where deptId = " + Long.parseLong(deptId));
				List<Role> list = createQuery.list();
				List<Role> result = new ArrayList<>();
				for (Role role : list) {
					if (null != role) {
						result.add(role);
					}
				}
				map.put("data", result);
				U.log(log, "后台-角色-根据部门获取角色-成功");
				U.setPut(map, 1, "查询成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}

}
