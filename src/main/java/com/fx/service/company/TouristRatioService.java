package com.fx.service.company;


import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.company.TouristRatio;

public interface TouristRatioService extends BaseService<TouristRatio, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据
	 * @param find 查询条件
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 */
	public Page<TouristRatio> findRatioList(Page<TouristRatio> pageData,String find,String sTime,String eTime);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20200426
	 * @param trId 修改/删除对象id
	 * @param ratio 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operTr(String trId, TouristRatio ratio);
}
