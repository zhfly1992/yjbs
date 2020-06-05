package com.fx.dao.company;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.dao.cus.CustomerDao;
import com.fx.entity.company.MyCustomers;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;

@Repository
public class MyCustomersDao extends ZBaseDaoImpl<MyCustomers, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 单位用户-服务 */
	@Autowired
	private CompanyUserDao companyUserDao;
	/** 用户-服务 */
	@Autowired
	private CustomerDao customerDao;

	/**
	 * 获取-订单业务员姓名
	 * @param companyNum 	单位编号
	 * @param customerPhone 用户手机号
	 * @return 业务员姓名
	 */
	public String getOrderServeMan(String companyNum, String customerPhone) {
		String logtxt = U.log(log, "获取-订单业务员");
		
		String res = null;
		
		try {
			CompanyUser comUser = companyUserDao.findByField("unitNum", companyNum);
			if(comUser != null){
				// 获取车队管理员账号
				String hql = "from MyCustomers where myName = ? and cusPhone = ?";
				MyCustomers mc = findObj(hql, comUser.getManagerName(), customerPhone);
				if(mc != null && StringUtils.isNotBlank(mc.getServiceMan())){
					res = mc.getServiceMan();
					
					if(res.indexOf("-") == -1){// 没有姓名
						Customer cus = customerDao.findByField("baseUserId.uname", res.trim());
						if(cus != null){
							res = cus.getBaseUserId().getRealName();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.log(log, logtxt, e);
		}
		
		return res;
	}
	
}
