package com.fx.service.impl.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.MoneyTypeDao;
import com.fx.entity.finance.BankTradeList;
import com.fx.entity.finance.MoneyType;
import com.fx.service.finance.BankTradeListService;
import com.fx.service.finance.MoneyTypeService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class MoneyTypeServiceImpl extends BaseServiceImpl<MoneyType,Long> implements MoneyTypeService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private MoneyTypeDao mtDao;
	/*******缓存服务*******/
	@Autowired
    private RedisUtil redis;
	/*******银行账服务*******/
	@Autowired
    private BankTradeListService btlSer;
	
	@Override
	public ZBaseDaoImpl<MoneyType, Long> getDao() {
		return mtDao;
	}
	@Override
	public Map<String, Object> findMoneyType(ReqSrc reqsrc, String page, String rows,String unitNum, String typeName,
			String sTime, String eTime) {
		String logtxt = U.log(log, "获取-金额类型-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "单位金额类型");
			/*****参数--验证--end******/
			
			if(fg){
				Page<MoneyType> pd = mtDao.findMoneyType(reqsrc, page, rows, unitNum, typeName, sTime, eTime);
				U.setPageData(map, pd);
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> adupMtype(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String typeName) {
		String logtxt = U.log(log, (StringUtils.isNotBlank(updId))?"修改":"添加"+"-金额类型", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(StringUtils.isBlank(updId)) {//添加
					String hql="from MoneyType where typeName = ?0 and unitNum = ?1";
					MoneyType isExit = mtDao.findObj(hql,typeName, LU.getLUnitNum(request, redis));
					if (isExit != null) { // 已设置
						fg = U.setPutFalse(map, "该金额类型已存在，请修改");
					}
				}else {//修改
					String hql="from MoneyType where typeName = ?0 and unitNum = ?1";
					MoneyType isExit = mtDao.findObj(hql,typeName, LU.getLUnitNum(request, redis));
					if (isExit != null && isExit.getId()!=Long.valueOf(updId)) { // 已设置
						fg = U.setPutFalse(map, "该金额类型已存在，请修改");
					}
				}
				MoneyType mt=null;
				if(fg){
					if(StringUtils.isNotBlank(updId)){
						mt=mtDao.findByField("id",Long.valueOf(updId));
						if(mt==null) {
							fg = U.setPutFalse(map, "[金额类型]不存在");
						}else {
							String hql="from BankTradeList a where a.moneyTypeId.typeName = ?0 and a.unitNum = ?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql, typeName,LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该金额类型已有交易记录存在，不能修改");
							}
						}
					}else{
						mt=new MoneyType();
						mt.setUnitNum(LU.getLUnitNum(request, redis));
						mt.setAddTime(new Date());
					}
				}
				if(fg){
					mt.setTypeName(typeName);
					if(StringUtils.isNotBlank(updId)) {
						mtDao.update(mt);
					}else {
						mtDao.save(mt);
					}
					U.setPut(map, 1, "操作成功");
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> delMtype(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request, String delId) {
		String logtxt = U.log(log, "删除金额类型", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				MoneyType mt=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						mt=mtDao.findByField("id", Long.valueOf(delId));
						if(mt==null) {
							fg = U.setPutFalse(map, "金额类型不存在");
						}else {
							String hql="from BankTradeList a where a.moneyTypeId.typeName = ?0 and a.unitNum = ?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql,  mt.getTypeName(),LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该金额类型已有交易记录存在，不能修改");
							}
						}
					}
				}
				if(fg){
					mtDao.delete(mt);
					U.setPut(map, 1, "操作成功");
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> mtypeFindById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "查询-金额类型-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[查询id]不能为空");
				}
			}

			if (fg) {
				MoneyType mt = mtDao.findByField("id", Long.valueOf(id));
				map.put("data", mt);
				U.setPut(map, 1, "查询成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> findMtypes(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request) {
		String logtxt = U.log(log, "查询-单位金额类型列表-下拉框使用", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<MoneyType> mtlist=mtDao.findListByField("unitNum", LU.getLUnitNum(request, redis));
			if(mtlist.size()>0){
				map.put("mTypes", mtlist);
				U.setPut(map, 1, "查询成功");
			}else {
				U.setPut(map, 0, "无数据");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
}
