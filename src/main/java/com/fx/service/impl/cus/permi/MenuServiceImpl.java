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
import com.fx.dao.cus.permi.TbMenuDao;
import com.fx.entity.cus.permi.Menu;
import com.fx.service.cus.permi.MenuService;

@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 资源-数据源 */
	@Autowired
	private TbMenuDao tbMenuDao;
	@Override
	public ZBaseDaoImpl<Menu, Long> getDao() {
		return tbMenuDao;
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
				list = tbMenuDao.findListByField("uname", uname);
				
				U.log(log, "获取成功");
			}
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return list;
	}
	
}
