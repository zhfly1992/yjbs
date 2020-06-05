package com.fx.service.finance;


import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.finance.OilCardList;

public interface OilCardListService extends BaseService<OilCardList, Long> {
	/**
	 * 查询设置列表
	 * @author xx
	 * @date 20190319
	 * @param pageData 分页数据
	 * @param teamNo 车队编号
	 * @param find 卡号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param oilType 油类型
	 * @param cName 使用人
	 */
	public Page<OilCardList> findOclList(Page<OilCardList> pageData,String teamNo,
			String find,String sTime,String eTime,String oilType,String cName);
	/**
	 * 添加/修改/删除设置
	 * @author xx
	 * @date 20190319
	 * @param updId 修改/删除对象id
	 * @param coi 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operOcl(String updId, OilCardList ocl);
}
