package com.fx.service.cus.permi;

import java.util.List;
import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.cus.permi.Menu;

public interface MenuService extends BaseService<Menu, Long> {

	/**
	 * 添加-菜单资源
	 * @param systype 	菜单所属系统
	 * @param menuName 	菜单名称
	 * @param pid 		菜单所属父级菜单id
	 * @param url 		菜单url
	 * @param perms 	菜单权限标识
	 * @param mtype 	菜单所属类型
	 * @param num 		菜单标识编号
	 * @return map{ code: 结果状态码, msg: 结果状态码说明 }
	 */
	public Map<String, Object> addMenu(String systype, String menuName, String pid, String url, String perms,
		String mtype, String num);
	
	/**
	 * 获取-用户资源-列表
	 * @param uname 用户名
	 * @return 用户资源列表
	 */
	public List<Menu> findUserPermissions(String uname);

	
	
}
