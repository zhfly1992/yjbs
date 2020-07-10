package com.fx.service.impl.back;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.dao.back.CarCompanyDao;
import com.fx.entity.back.CarCompany;
import com.fx.service.back.CarCompanyService;

@Service
@Transactional
public class CarCompanyServiceImpl extends BaseServiceImpl<CarCompany,Long> implements CarCompanyService {

	@Autowired
	private CarCompanyDao carCompanyDao;
	@Override
	public ZBaseDaoImpl<CarCompany, Long> getDao() {
		return carCompanyDao;
	}
	@Override
	public int operCarCompany(String ccId, CarCompany cc) {
		try {
			if(cc != null){
				if(StringUtils.isNotEmpty(ccId)){
					carCompanyDao.update(cc);
				}else{
					carCompanyDao.save(cc);
				}
			}else{
				CarCompany delCc = carCompanyDao.find(Long.parseLong(ccId));
				carCompanyDao.delete(delCc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
