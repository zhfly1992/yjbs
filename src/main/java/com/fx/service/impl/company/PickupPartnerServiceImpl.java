package com.fx.service.impl.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.PickupPartnerDao;
import com.fx.entity.company.AreaRebateSet;
import com.fx.entity.company.PickupPartner;
import com.fx.service.company.AreaRebateSetService;
import com.fx.service.company.PickupPartnerService;

@Service
@Transactional
public class PickupPartnerServiceImpl extends BaseServiceImpl<PickupPartner,Long> implements PickupPartnerService {
	private Logger log = LogManager.getLogger(this.getClass());//日志记录
	
	@Autowired
	private PickupPartnerDao ppDao;
	@Override
	public ZBaseDaoImpl<PickupPartner, Long> getDao() {
		return ppDao;
	}
	
	@Autowired
	private AreaRebateSetService arsSer;
	//@Autowired
	//private CarOrderListDao colDao;
	
	@Override
	public int partnerAdup(String ppId, PickupPartner pp) {
		try {
			if(pp != null){
				if(StringUtils.isNotEmpty(ppId)){
					ppDao.update(pp);
				}else{
					ppDao.save(pp);
				}
			}else{
				PickupPartner delDis = ppDao.find(Long.parseLong(ppId));
				ppDao.delete(delDis);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	@Override
	public Page<PickupPartner> partnerListOfTeam(Page<PickupPartner> pageData,
			String teamNo,String find, String sTime, String eTime, String area) {
		//给pageData设置参数
  		Compositor compositor = new Compositor("addTime",CompositorType.DESC);
  		pageData.setCompositor(compositor);
  		List<Filtration> filtrations = new ArrayList<Filtration>();
  		/////////////////查询条件/////-s/////////////
  		filtrations.add(new Filtration(MatchType.EQ, teamNo, "teamNo"));//车队编号
  		
  		if(StringUtils.isNotBlank(area)){
  			List<AreaRebateSet> arss = arsSer.findAreaRebateSetList(teamNo, area);
  			List<Object> ids = new ArrayList<Object>();
  			for(int i = 0; i < arss.size(); i++){
  				ids.add(arss.get(i).getId());
  			}
  			
  			if(ids.size() > 0){
  				filtrations.add(new Filtration(MatchType.IN, ids.toArray(), "areaRebateSet.id"));
  			}else{
  				filtrations.add(new Filtration(MatchType.ISNULL, "", "partner"));
  			}
  		}
  		
  		if(StringUtils.isNotEmpty(find)){
  			filtrations.add(new Filtration(MatchType.LIKE, find, "partner"));//合作用户名
		}
  		if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
			filtrations.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
			filtrations.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
		}
  		/////////////////查询条件/////-e//////////////
  		pageData.setFiltrations(filtrations);
  		pageData = ppDao.findPage(pageData);
  		return pageData;
	}
	
	@Override
	public Map<String, Object> isMonthUser(String id) {
		log.info("判断订单["+id+"]用户是否是月结用户>>>>>>>>>>>>>>>>");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			/*CarOrderList col = colDao.findByField("id", Long.parseLong(id));
			if(col != null){
				// 只要下单用户[是月结账户]
				String hql = "from PickupPartner where partner = ? and teamNo = ? and settleWay = ?";
				PickupPartner pp = ppDao.findObj(hql, col.getMbName(), col.getTeamNo(), 1);
				if(pp != null){// 是月结账户 
					UTools.setPut(map, 1, "是月结用户");
					
					log.info("该订单所属用户[是月结用户]");
				}else{
					UTools.setPut(map, 0, "不是月结用户");
					
					log.info("该订单所属用户[不是是月结用户]");
				}
			}else{
				UTools.setPut(map, 0, "订单不存在");
				
				log.info("未找到指定id的订单");
			}*/
		} catch (Exception e) {
			log.info("异常：判断订单["+id+"]用户是否是月结用户", e);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public boolean isMonthUser(String uname, String teamNo) {
		String logtxt = U.log(log, "判断用户是否是月结用户");
		
		boolean fg = true;
		String hql = "";
		
		try {
			if(fg){
				if(StringUtils.isEmpty(uname)){
					fg = U.logFalse(log, "[用户名]不能为空");
				}else{
					uname = uname.trim();
					
					U.log(log, "[用户名] uname="+uname);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(teamNo)){
					fg = U.logFalse(log, "[车队编号]不能为空");
				}else{
					teamNo = teamNo.trim();
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			if(fg){
				hql = "from PickupPartner where partner = ? and teamNo = ? and settleWay = ?";
				PickupPartner pp = ppDao.findObj(hql, uname, teamNo, 1);
				if(pp == null){
					fg = U.logFalse(log, "用户["+uname+"]不是[月结用户]");
				}else{
					U.log(log, "用户["+uname+"]是[月结用户]");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return fg;
	}
	
}
