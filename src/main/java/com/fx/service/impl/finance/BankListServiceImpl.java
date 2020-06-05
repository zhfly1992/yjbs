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
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.BankListDao;
import com.fx.entity.finance.BankList;
import com.fx.entity.finance.BankTradeList;
import com.fx.service.finance.BankListService;
import com.fx.service.finance.BankTradeListService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class BankListServiceImpl extends BaseServiceImpl<BankList,Long> implements BankListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private BankListDao blDao;
	/*******缓存服务*******/
	@Autowired
    private RedisUtil redis;
	/*******银行账服务*******/
	@Autowired
    private BankTradeListService btlSer;
	
	@Override
	public ZBaseDaoImpl<BankList, Long> getDao() {
		return blDao;
	}
	@Override
	public Map<String, Object> findBankList(ReqSrc reqsrc, String page, String rows,String unitNum, String find,
			String sTime, String eTime,String isOpen) {
		String logtxt = U.log(log, "获取-用户-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "单位银行");
			/*****参数--验证--end******/
			
			if(fg) {
				if(StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				}else {
					find = find.trim();
					U.log(log, "[查询关键字] find="+find);
				}
			}
			
			if(fg){
				Page<BankList> pd = blDao.findBankList(reqsrc, page, rows, unitNum, find, sTime, eTime, isOpen);
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
	public Map<String, Object> adupBank(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String updId, String bankName, String cardNo, String cardName) {
		String logtxt = U.log(log, (StringUtils.isNotBlank(updId))?"修改":"添加"+"-银行", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String hql="from BankList where bankName = ?0 and cardNo = ?1 and unitNum = ?2";
				BankList isExit = blDao.findObj(hql,bankName,cardNo, LU.getLUnitNum(request, redis));
				if (isExit != null) { // 已设置
					fg = U.setPutFalse(map, "该银行已存在，请修改");
				}
				BankList bank=null;
				if(fg){
					if(StringUtils.isNotBlank(updId)){
						bank=blDao.findByField("id",Long.valueOf(updId));
						if(bank==null) {
							fg = U.setPutFalse(map, "[银行数据]不存在");
						}else {
							hql="from BankTradeList where myBankNum = ?0 and unitNum = ?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql, bank.getCardNo(),LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该银行已有交易记录存在，不能修改");
							}
						}
					}else{
						bank=new BankList();
						bank.setUnitNum(LU.getLUnitNum(request, redis));
						bank.setAddTime(new Date());
					}
				}
				if(fg){
					bank.setBankName(bankName);
					bank.setCardNo(cardNo);
					bank.setCardName(cardName);
					if(StringUtils.isNotBlank(updId)){
						bank.setOperNote(bank.getOperNote() +Util.getOperInfo(LU.getLUSER(request, redis).getBaseUserId().getRealName(), "修改"));
					}else {
						bank.setOperNote(LU.getLUSER(request, redis).getBaseUserId().getRealName()+"[添加]");
					}
					if(StringUtils.isNotBlank(updId)) {
						blDao.update(bank);
					}else {
						blDao.save(bank);
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
	public Map<String, Object> delBank(ReqSrc reqsrc,HttpServletResponse response, HttpServletRequest request, String delId) {
		String logtxt = U.log(log, "删除银行", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				BankList bank=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						bank=blDao.findByField("id", Long.valueOf(delId));
						if(bank==null) {
							fg = U.setPutFalse(map, "银行不存在");
						}else {
							String hql="from BankTradeList where myBankNum =?0 and unitNum =?1 order by id asc";
							BankTradeList btl=btlSer.findObj(hql, bank.getCardNo(),LU.getLUnitNum(request, redis),"LIMIT 1");
							if(btl!=null){
								fg = U.setPutFalse(map, "该银行已有交易记录存在，不能删除");
							}
						}
					}
				}
				if(fg){
					blDao.delete(bank);
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
	public Map<String, Object> bankFindById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "查询-银行信息-通过id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					fg = U.setPutFalse(map, "[查询id]不能为空");
				}
			}

			if (fg) {
				BankList bank = blDao.findByField("id", Long.valueOf(id));
				map.put("data", bank);
				U.setPut(map, 1, "查询成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> openAccount(ReqSrc reqsrc,
			HttpServletRequest request, String openAccount) {
		String logtxt = U.log(log, "银行账本启用", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(openAccount)){
						fg = U.setPutFalse(map, "[启用银行]不能为空");
					}else{
						openAccount = openAccount.trim();
						U.log(log, "启用银行："+openAccount);
					}
				}
				if(fg){
					String [] ids=openAccount.split(",");
					BankList bank=null;
					for (String each : ids) {
						bank=blDao.findByField("id", Long.valueOf(each));
						if(bank!=null) {
							bank.setIsOpen(1);
							blDao.update(bank);
						}
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
	public Map<String, Object> findBanks(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String isOpen) {
		String logtxt = U.log(log, "查询-单位银行列表-下拉框使用", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BankList> blist=null;
			String hql="from BankList where unitNum = ?0";
			if(StringUtils.isNotBlank(isOpen)) {
				hql="from BankList where unitNum = ?0 and isOpen=?1";
				blist=blDao.findhqlList(hql, LU.getLUnitNum(request, redis),Integer.parseInt(isOpen));
			}else {
				blist=blDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			}
			if(blist.size()>0){
				map.put("banks", blist);
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
