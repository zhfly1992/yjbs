package com.fx.service.impl.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.FeeCourseTradeFirstDao;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.FeeCourseTradeFirst;
import com.fx.service.finance.FeeCourseTradeFirstService;

@Service
@Transactional
public class FeeCourseTradeFirstServiceImpl extends BaseServiceImpl<FeeCourseTradeFirst,Long> implements FeeCourseTradeFirstService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private FeeCourseTradeFirstDao fctfDao;
	/***科目-服务***/
	@Autowired
	private FeeCourseDao	fcDao;
	
	@Override
	public ZBaseDaoImpl<FeeCourseTradeFirst, Long> getDao() {
		return fctfDao;
	}

	@Override
	public Map<String, Object> firstBalanceSet(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String courseId, String gathMoney, String payMoney, String balance,String setId) {
		String logtxt = U.log(log, "科目-期初余额设置", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			FeeCourseTradeFirst fctf=null;
			if(StringUtils.isNotBlank(setId)) {
				fctf=fctfDao.findByField("id", Long.valueOf(setId));
				if(fctf==null) {
					fg = U.setPutFalse(map, 0, "[期初记录]不存在");
				}
			}else {
				fctf=new FeeCourseTradeFirst();
				fctf.setAddTime(new Date());
			}
			FeeCourse fc=null;
			if (fg) {
				if (StringUtils.isBlank(courseId)) {
					fg = U.setPutFalse(map, 0, "科目id不能为空");
				}else {
					fc = fcDao.findByField("id", Long.parseLong(courseId));
					if (null == fc) {
						fg = U.setPutFalse(map, 0, "设置科目不存在");
					}else if(fc.getIsLastCourse()==0){
						fg = U.setPutFalse(map, 0, "该科目非末级科目，不能设置期初余额");
					}
				}
			}
			double _gathMoney = 0;
			if(fg) {
				if(StringUtils.isNotBlank(gathMoney)) {
					gathMoney = gathMoney.trim();
					if(!FV.isDouble(gathMoney)) {
						fg = U.setPutFalse(map, "[收入金额]格式错误");
					}else {
						_gathMoney = Double.valueOf(gathMoney);
					}
				}
			}
			double _payMoney = 0;
			if(fg) {
				if(StringUtils.isBlank(payMoney)) {
					payMoney = payMoney.trim();
					if(!FV.isDouble(payMoney)) {
						fg = U.setPutFalse(map, "[支出金额]格式错误");
					}else {
						_payMoney = Double.valueOf(payMoney);
					}
				}
			}
			double _balance = 0;
			if(fg) {
				if(StringUtils.isBlank(balance)) {
					fg = U.setPutFalse(map, "[期初余额]不能为空");
				}else {
					balance = balance.trim();
					if(!FV.isDouble(balance)) {
						fg = U.setPutFalse(map, "[期初余额]格式错误");
					}else {
						_balance = Double.valueOf(balance);
					}
					
				}
			}
			if(fg) {
				double oldBalance=fctf.getBalance();
				fctf.setUnitNum(fc.getUnitNum());
				fctf.setRemark("期初余额");
				fctf.setGathMoney(_gathMoney);
				fctf.setPayMoney(_payMoney);
				fctf.setBalance(Double.valueOf(_balance));
				fctfDao.save(fctf);
				//更新科目余额=科目余额-原来的期初余额+现在的期初余额
				fc.setBalance(MathUtils.add(MathUtils.sub(fc.getBalance(), oldBalance, 2), _balance, 2));
				fc.setFirstBalanceId(fctf);//关联期初记录
				fcDao.update(fc);
				U.setPut(map, 1, "设置成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
}
