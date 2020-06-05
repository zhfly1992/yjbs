package com.fx.service.impl.cus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.BaseUserDao;
import com.fx.entity.cus.BaseUser;
import com.fx.service.cus.BaseUserService;

@Service
@Transactional
public class BaseUserServiceImpl extends BaseServiceImpl<BaseUser, Long> implements BaseUserService {
	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 用户基类-数据源 */
	@Autowired
	private BaseUserDao baseUserDao;
	@Override
	public ZBaseDaoImpl<BaseUser, Long> getDao() {
		return baseUserDao;
	}
	
	
	@Override
	public BaseUser findByUname(String uname) {
		return baseUserDao.findByUname(uname);
	}
	
	@Override
	public BaseUser findByPhone(String phone) {
		return baseUserDao.findByPhone(phone);
	}
	
}
