package com.fx.service.back;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.CarCompany;

public interface CarCompanyService extends BaseService<CarCompany,Long> {
	/**
	 * 添加/修改车辆生产厂家
	 * @author xx
	 * @date 20200507
	 * @param ccId 修改/删除对象id
	 * @param cc 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operCarCompany(String ccId, CarCompany cc);
}
