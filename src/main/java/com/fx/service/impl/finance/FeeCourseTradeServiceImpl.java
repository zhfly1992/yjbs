package com.fx.service.impl.finance;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.FeeCourseTradeDao;
import com.fx.entity.finance.FeeCourseTrade;
import com.fx.service.finance.FeeCourseTradeService;

@Service
@Transactional
public class FeeCourseTradeServiceImpl extends BaseServiceImpl<FeeCourseTrade,Long> implements FeeCourseTradeService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	/** 科目交易记录服务 */
	@Autowired
	private FeeCourseTradeDao fctDao;
	
	@Override
	public ZBaseDaoImpl<FeeCourseTrade, Long> getDao() {
		return fctDao;
	}
	@Override
	public Map<String, Object> findCourseTrades(ReqSrc reqsrc, String page, String rows,String unitNum, String voucherNum,
			String courseName,String courseId,String uname,String palteNum,String sTime, String eTime) {
		String logtxt = U.log(log, "获取-科目交易记录-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "科目交易记录");
			/*****参数--验证--end******/
			
			if(fg){
				Page<FeeCourseTrade> pd = fctDao.findCourseTrades(reqsrc, page, rows, unitNum, voucherNum, courseName, courseId, uname, palteNum, sTime, eTime);
				U.setPageData(map, pd);
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
