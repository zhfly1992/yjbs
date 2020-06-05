package com.fx.service.back;


import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.SpecialRoute;

public interface SpecialRouteService extends BaseService<SpecialRoute, Long> {
	/**
	 * 查询路线列表
	 * @author xx
	 * @date 20200426
	 * @param startArea 起点
	 * @param sTime,eTime 添加时间
	 */
	public Page<SpecialRoute> findRouteList(Page<SpecialRoute> pageData,String startArea,String sTime,String eTime);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20200426
	 * @param srId 修改/删除对象id
	 * @param sr 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operSr(String srId, SpecialRoute sr);
}
