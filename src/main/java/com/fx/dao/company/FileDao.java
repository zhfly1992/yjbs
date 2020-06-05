package com.fx.dao.company;

import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.entity.company.FileManage;

@Repository
public class FileDao extends ZBaseDaoImpl<FileManage, Long> {

}
