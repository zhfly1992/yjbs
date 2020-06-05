package com.fx.service.impl.wxdat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.wxdat.YMessageDao;
import com.fx.entity.wxdat.YMessage;
import com.fx.service.wxdat.YMessageService;

@Service
public class YMessageServiceImpl extends BaseServiceImpl<YMessage,Long> implements YMessageService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private YMessageDao yMessDao;
	
	@Override
	public ZBaseDaoImpl<YMessage, Long> getDao() {
		return yMessDao;
	}
	
}
