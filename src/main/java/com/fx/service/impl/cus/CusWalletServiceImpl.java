package com.fx.service.impl.cus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.cus.CusWalletDao;
import com.fx.entity.cus.CusWallet;
import com.fx.service.cus.CusWalletService;

@Service
@Transactional
public class CusWalletServiceImpl extends BaseServiceImpl<CusWallet,Long> implements CusWalletService {
	private Logger log = LogManager.getLogger(this.getClass());//日志记录
	
	@Autowired
	private CusWalletDao cusWalletDao;
	
	@Override
	public ZBaseDaoImpl<CusWallet, Long> getDao() {
		return cusWalletDao;
	}
	
	
}
