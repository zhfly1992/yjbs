package com.fx.service.finance;


import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.FeeCourseTrade;

public interface FeeCourseTradeService extends BaseService<FeeCourseTrade, Long> {
	/**
	 *  获取-单位科目交易记录-分页列表
	 *  @author xx
	 *  @date 20200708
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find		查询关键字
	 * @param unitNum   单位编号
	 * @param voucherNum 凭证号
	 * @param courseName 科目名称
	 * @param courseId   科目id，多个逗号拼接
	 * @param uname     报销人uname
	 * @param plateNum  车牌号
	 * @param sTime     添加开始时间
	 * @param eTime		添加结束时间
	 * @return Page<T>	分页数据
	 */
	public Map<String, Object> findCourseTrades(ReqSrc reqsrc, String page, String rows,String unitNum, String voucherNum,
			String courseName,String courseId,String uname,String palteNum,String sTime, String eTime);
	
}
