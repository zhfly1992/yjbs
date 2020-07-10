package com.fx.service.impl.company;

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
import com.fx.dao.company.CustomTypeDao;
import com.fx.entity.finance.BankTradeList;
import com.fx.entity.company.CustomType;
import com.fx.service.finance.BankTradeListService;
import com.fx.service.company.CustomTypeService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class CustomTypeServiceImpl extends BaseServiceImpl<CustomType,Long> implements CustomTypeService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private CustomTypeDao ctDao;
	/*******缓存服务*******/
	@Autowired
    private RedisUtil redis;
	/*******银行账服务*******/
	@Autowired
    private BankTradeListService btlSer;
	
	@Override
	public ZBaseDaoImpl<CustomType, Long> getDao() {
		return ctDao;
	}
	@Override
	public Map<String, Object> findCustomType(ReqSrc reqsrc, String page, String rows,String unitNum, String typeName,
			String sTime, String eTime) {
		String logtxt = U.log(log, "获取-客户类型-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "单位客户类型");
			/*****参数--验证--end******/
			
			if(fg){
				Page<CustomType> pd = ctDao.findCustomType(reqsrc, page, rows, unitNum, typeName, sTime, eTime);
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
	public Map<String, Object> adupCusType(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String typeName,String isSupplier) {
		String logtxt = U.log(log, (StringUtils.isNotBlank(updId))?"修改":"添加"+"-客户类型", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(StringUtils.isBlank(updId)) {//添加
					String hql="from CustomType where typeName = ?0 and unitNum = ?1";
					CustomType isExit = ctDao.findObj(hql,typeName, LU.getLUnitNum(request, redis));
					if (isExit != null) { // 已设置
						fg = U.setPutFalse(map, "该客户类型已存在，请修改");
					}
				}else {//修改
					String hql="from CustomType where typeName = ?0 and unitNum = ?1";
					CustomType isExit = ctDao.findObj(hql,typeName, LU.getLUnitNum(request, redis));
					if (isExit != null && isExit.getId()!=Long.valueOf(updId)) { // 已设置
						fg = U.setPutFalse(map, "该客户类型已存在，请修改");
					}
				}
				CustomType ct=null;
				if(fg){
					if(StringUtils.isNotBlank(updId)){
						ct=ctDao.findByField("id",Long.valueOf(updId));
						if(ct==null) {
							fg = U.setPutFalse(map, "[客户类型]不存在");
						}else {
							String hql="from CompanyCustom a where a.cusTypeId.id = ?0 and a.unitNum = ?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql,  ct.getId(),LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该客户类型已有客户存在，不能修改");
							}
						}
					}else{
						ct=new CustomType();
						ct.setUnitNum(LU.getLUnitNum(request, redis));
						ct.setAddTime(new Date());
					}
				}
				if(fg){
					ct.setTypeName(typeName);
					ct.setIsSupplier(Integer.parseInt(isSupplier));
					if(StringUtils.isNotBlank(updId)) {
						ctDao.update(ct);
					}else {
						ctDao.save(ct);
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
	public Map<String, Object> delCusType(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request, String delId) {
		String logtxt = U.log(log, "删除客户类型", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				CustomType ct=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						ct=ctDao.findByField("id", Long.valueOf(delId));
						if(ct==null) {
							fg = U.setPutFalse(map, "客户类型不存在");
						}else {
							String hql="from CompanyCustom a where a.cusTypeId.id = ?0 and a.unitNum = ?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql,  ct.getId(),LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该客户类型已有客户存在，不能修改");
							}
						}
					}
				}
				if(fg){
					ctDao.delete(ct);
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
	public Map<String, Object> cusTypeFindById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "查询-客户类型-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[查询id]不能为空");
				}
			}

			if (fg) {
				CustomType ct = ctDao.findByField("id", Long.valueOf(id));
				map.put("data", ct);
				U.setPut(map, 1, "查询成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> findCusTypes(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request) {
		String logtxt = U.log(log, "查询-单位客户类型列表-下拉框使用", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<CustomType> ctlist=ctDao.findListByField("unitNum", LU.getLUnitNum(request, redis));
			if(ctlist.size()>0){
				map.put("cusTypes", ctlist);
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
