package com.fx.service.company;

import java.util.Map;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.company.PickupPartner;

public interface PickupPartnerService extends BaseService<PickupPartner,Long> {
	/**
	 * 获取优惠券设置列表
	 * @author xx
	 * @date 20200426
	 * @param pageData 页面数据列表
	 * @param teamNo 车队编号
	 * @param sTime 有效期开始时间
	 * @param eTime 有效期结束时间
	 * @param area 返点区域名称
	 */
	public Page<PickupPartner> partnerListOfTeam(Page<PickupPartner> pageData,
		String teamNo, String find, String sTime, String eTime, String area);
	/**
	 * 执行添加/修改合作用户数据
	 * @author xx
	 * @date 20200426
	 * @param ppId 修改/删除对象id
	 * @param pp 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int partnerAdup(String ppId, PickupPartner pp);
	
	/**
	 * 判断订单发布用户是否是月结用户
	 * @author qfc
	 * @date 20200426
	 * @param id 订单id
	 * @return map{}
	 */
	public Map<String, Object> isMonthUser(String id);
	
	/**
	 * 判断用户是否是月结用户
	 * @author qfc
	 * @date 20200426
	 * @param uname  用户名
	 * @param teamNo 车队编号
	 * @return true-是月结用户；false-不是月结用户；
	 */
	public boolean isMonthUser(String uname, String teamNo);
}
