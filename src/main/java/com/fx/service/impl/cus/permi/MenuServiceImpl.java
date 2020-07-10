package com.fx.service.impl.cus.permi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.MenuType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.permi.MenuDao;
import com.fx.entity.cus.permi.Menu;
import com.fx.service.cus.permi.MenuService;

@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 资源-数据源 */
	@Autowired
	private MenuDao menuDao;
	@Override
	public ZBaseDaoImpl<Menu, Long> getDao() {
		return menuDao;
	}
	
	
	@Override
	public Map<String, Object> addMenu(String systype, String menuName, String pid, String url, String perms, 
		String mtype, String num) {
		String logtxt = U.log(log, "添加-菜单资源");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			ReqSrc _systype = null;
			if(fg) {
				if(StringUtils.isEmpty(systype)) {
					fg = U.setPutFalse(map, "[菜单所属系统]不能为空");
				}else {
					systype = systype.trim();
					if(!FV.isOfEnum(ReqSrc.class, systype)) {
						fg = U.setPutFalse(map, "[菜单所属系统]格式错误");
					}else {
						_systype = ReqSrc.valueOf(systype);
					}
				}
				
				U.log(log, "[菜单所属系统] systype="+systype);
			}
			
			if(fg) {
				if(StringUtils.isEmpty(menuName)) {
					fg = U.setPutFalse(map, "[菜单名称]不能为空");
				}else {
					menuName = menuName.trim();
					if(menuName.length() > 10) {
						fg = U.setPutFalse(map, "[菜单名称]最多10个字符");
					}
				}
				
				U.log(log, "[菜单名称] menuName="+menuName);
			}
			
			long _pid = 0L;
			Menu pmenu = null;
			if(fg){
				if(StringUtils.isEmpty(pid)){
					U.log(log, "[父级菜单id]为空");
				}else{
					pid = pid.trim();
					if(!FV.isLong(pid)){
						fg = U.setPutFalse(map, "[父级菜单id]格式错误");
					}else{
						pmenu = menuDao.findByField("id", Long.parseLong(pid));
						if(pmenu == null){
							fg = U.setPutFalse(map, "[父级菜单]不存在");
						}else {
							_pid = pmenu.getId();
						}
					}
					
					U.log(log, "[父级菜单id] pid="+pid);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(url)){
					U.log(log, "[菜单url]为空");
				}else{
					url = url.trim();
					if(url.length() > 255){
						fg = U.setPutFalse(map, "[菜单url]最多255个字符");
					}
					
					U.log(log, "[菜单url] url="+url);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(perms)){
					U.log(log, "[菜单权限标识]为空");
				}else{
					perms = perms.trim();
					if(perms.length() > 50){
						fg = U.setPutFalse(map, "[菜单权限标识]最多50个字符");
					}
					
					U.log(log, "[菜单权限标识] perms="+perms);
				}
			}
			
			MenuType _mtype = null;
			if(fg) {
				if(StringUtils.isEmpty(mtype)) {
					fg = U.setPutFalse(map, "[菜单类型]不能为空");
				}else {
					mtype = mtype.trim();
					if(!FV.isOfEnum(MenuType.class, mtype)) {
						fg = U.setPutFalse(map, "[菜单类型]格式错误");
					}else {
						_mtype = MenuType.valueOf(mtype);
					}
				}
				
				U.log(log, "[菜单类型] mtype="+mtype);
			}
			
			if(fg){
				if(StringUtils.isEmpty(num)){
					fg = U.setPutFalse(map, "[菜单编号]不能为空");
				}else{
					num = num.trim();
					
					U.log(log, "[菜单编号] num="+num);
				}
			}
			
			if(fg) {
				if(fg && StringUtils.isNotEmpty(url)){
					hql = "select count(id) from Menu where systype = ?0 and url = ?1";
					Object o = menuDao.findObj(hql, _systype, url);
					if(o != null && Integer.parseInt(o.toString()) > 0){
						fg = U.setPutFalse(map, "资源url["+url+"]已存在");
					}
				}
				
				if(fg && StringUtils.isNotEmpty(perms)){
					hql = "select count(id) from Menu where systype = ?0 and perms = ?1";
					Object o = menuDao.findObj(hql, _systype, perms);
					if(o != null && Integer.parseInt(o.toString()) > 0){
						fg = U.setPutFalse(map, "权限标识["+perms+"]已存在");
					}
				}
				
				if(fg && StringUtils.isNotEmpty(num)){
					hql = "select count(id) from Menu where systype = ?0 and num = ?1";
					Object o = menuDao.findObj(hql, _systype, num);
					if(o != null && Integer.parseInt(o.toString()) > 0){
						fg = U.setPutFalse(map, "资源编号["+num+"]已存在");
					}
				}
			}
			
			if(fg){
				if(fg){
					Menu o = new Menu();
					o.setSystype(_systype);
					o.setName(menuName);
					o.setPid(_pid);
					o.setUrl(url);
					o.setPerms(perms);
					o.setMtype(_mtype);
					o.setMicon(null);
					o.setNum(num);
					o.setOcState(1);
					o.setAtime(new Date());
					menuDao.save(o);
					
					U.setPut(map, 1, "添加菜单资源成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public List<Menu> findUserPermissions(String uname) {
		String logtxt = U.log(log, "获取-用户资源-列表");
		
		List<Menu> list = new ArrayList<Menu>();
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
				list = menuDao.findListByField("uname", uname);
				
				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return list;
	}
	
}
