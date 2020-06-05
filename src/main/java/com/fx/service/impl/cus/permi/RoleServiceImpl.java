package com.fx.service.impl.cus.permi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.permi.TbRoleDao;
import com.fx.entity.cus.permi.Role;
import com.fx.service.cus.permi.RoleService;

@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 用户角色-数据源 */
	@Autowired
	private TbRoleDao tbRoleDao;
	@Override
	public ZBaseDaoImpl<Role, Long> getDao() {
		return tbRoleDao;
	}
	
	
	@Override
	public List<Role> findUserRole(String uname) {
		String logtxt = U.log(log, "获取-用户角色-列表");
		
		List<Role> list = new ArrayList<Role>();
		String hql = "";
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
				hql = "FROM TbRole r "
						+ "LEFT JOIN TbUserRole ur ON (r.id = ur.roleId) "
						+ "LEFT JOIN TbUser u ON (u.id = ur.userId) "
						+ "LEFT JOIN tbRoleMenu rm ON (r.id = rm.roleId) "
						+ "LEFT JOIN TbMenu m ON (m.id = rm.menuId) "
						+ "WHERE u.uname = ? "
						+ "ORDER BY id asc";
				
				list = tbRoleDao.findhqlList(hql, uname);
				
				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return list;
	}
	
}
