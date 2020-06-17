package com.fx.service.impl.back;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.back.FileManDao;
import com.fx.entity.back.FileMan;
import com.fx.service.back.FileManService;

@Service
@Transactional
public class FileManServiceImpl extends BaseServiceImpl<FileMan, Long> implements FileManService {

	/** 日志记录 */
	private Logger log	= LogManager.getLogger(this.getClass());

	@Autowired
	private FileManDao fileManDao;

	@Override
	public ZBaseDaoImpl<FileMan, Long> getDao() {
		return fileManDao;
	}

	
	
}
