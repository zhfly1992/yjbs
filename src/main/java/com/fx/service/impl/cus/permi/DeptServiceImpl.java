package com.fx.service.impl.cus.permi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.permi.TbDeptDao;
import com.fx.entity.cus.permi.Dept;
import com.fx.service.cus.permi.DeptService;

@Service
public class DeptServiceImpl extends BaseServiceImpl<Dept, Long> implements DeptService {
	
	/** 部门-数据源 */
	@Autowired
	private TbDeptDao tbDeptDao;
	@Override
	public ZBaseDaoImpl<Dept, Long> getDao() {
		return tbDeptDao;
	}
	
	
	
}
