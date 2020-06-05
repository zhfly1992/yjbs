package com.fx.service.cus;


import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.cus.WalletList;

public interface WalletListService extends BaseService<WalletList, Long> {
	/**
	 * 根据条件获取交易列表
	 * @author xx
	 * @Date 20200426
	 * @param pageData 分页数据
	 * @param obj 查询类型
	 * @param cName 用户名
	 * @param find 订单号
	 * @param status 收入/支出
	 * @param tradeSt 交易时间
	 * @param tradeSt 交易时间
	 * @param like 订单号 86 76 96 97
	 */
	public Page<WalletList> getTradeList(Page<WalletList> pageData,Object obj[],String cName,
			String find,int status,String tradeST,String tradeET,String like);

	/**
	 * 获取钱包记录
	 * @author qfc
	 * @Date 20200426
	 * @param pageData 分页数据设置
	 * @param atype 金额类型
	 * @param status 金额状态
	 * @param loginName 登录用户名
	 * @param find 查找关键字
	 * @param startTime 查询开始时间
	 * @param endTime 查询结束时间
	 * @return 分页后的数据列表
	 */
	public Page<WalletList> getWalletRecord(Page<WalletList> pageData, String atype,
		String status, String loginName, String find, String startTime, String endTime);

	

	
	/****移动端====begin**************************/

	/**
	 * @author qfc
	 * @Date 20200426
	 * 查询-用户-钱包交易记录
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param uid 		用户id
	 * @param stime 	交易时间-开始
	 * @param etime 	交易时间-结束
	 * @param atype  	金额类型
	 * @return Page<T>	分页数据
	 */
	public Page<WalletList> findWalletList(ReqSrc reqsrc, String page, String rows, 
		String uid, String stime, String etime, String atype);
	
	/****移动端====end**************************/
}
