package com.fx.service.company;


import java.util.List;
import java.util.Map;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.company.AreaRebateSet;

public interface AreaRebateSetService extends BaseService<AreaRebateSet, Long> {
	
	
	/**
	 * 获取-区域返点设置列表
	 * @author qfc
	 * @date 20200426
	 * @param pageData 分页数据
	 * @param temaNo 所属车队
	 * @param find 查询条件[返点名称、区域名称]
	 * @return 分页后的数据列表
	 */
	public Page<AreaRebateSet> getAreaRebateSetList(Page<AreaRebateSet> pageData, 
		String temaNo, String find);

	/**
	 * 添加/修改-区域返点比例设置
	 * @author qfc
	 * @date 20200426
	 * @param uid 数据id（仅修改使用）
	 * @param teamNo 所属车队编号
	 * @param rebateName 返点名称
	 * @param area 所属区域
	 * @param rebate 返点比例
	 * @param overkmParam 超公里调节参数
	 * @return map{}
	 */
	public Map<String, Object> addAreaRebateSet(String uid, String teamNo, String rebateName, 
		String area, String rebate, String overkmParam);
	
	/**
	 * 删除-区域返点比例设置
	 * @author qfc
	 * @date 20200426
	 * @param did 删除对象id
	 * @return map{}
	 */
	public Map<String, Object> delAreaRebateSet(String did);

	/**
	 * 查询区域返点列表数据
	 * @author qfc
	 * @date 20200426
	 * @param teamNo 查询的车队编号
	 * @param area 查询的区域名称
	 */
	public List<AreaRebateSet> findAreaRebateSetList(String teamNo, String area);
}
