package com.fx.commons.utils.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件常量-类
 */
@Component
@PropertySource(value = {"classpath:application.yml"})
public class ConfigPs {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 服务器jdbcip */
	@Value("${spring.datasource.url}")
    public String jdbcIp;
	
	/** 服务器jdbcip */
	public String getJdbcIp() { return jdbcIp; }
	/** 服务器jdbcip */
	public void setJdbcIp(String jdbcIp) { this.jdbcIp = jdbcIp; }
	
	
	/**
	 * 是否是正式服务器ip
	 * @return true-是；false-不是；
	 */
	public boolean isIPpass() {
		if(getJdbcIp().contains(QC.SQL_IP)) {
			U.log(log, "是正式服务器访问");
			
			return true;
		}else {
			U.log(log, "不是正式服务器访问");
			
			return false;
		}
	}
	
}
