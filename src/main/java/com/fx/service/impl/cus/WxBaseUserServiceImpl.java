package com.fx.service.impl.cus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.WxBaseUserDao;
import com.fx.entity.cus.WxBaseUser;
import com.fx.service.cus.WxBaseUserService;

@Service
@Transactional
public class WxBaseUserServiceImpl extends BaseServiceImpl<WxBaseUser, Long> implements WxBaseUserService {
	
	/** 微信用户基类-数据源 */
	@Autowired
	private WxBaseUserDao wxBaseUserDao;
	@Override
	public ZBaseDaoImpl<WxBaseUser, Long> getDao() {
		return wxBaseUserDao;
	}
	
	
	
}
