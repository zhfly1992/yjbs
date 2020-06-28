package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.CusType;
import com.fx.commons.utils.enums.OilWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.finance.CarOilList;

@Repository
public class CarOilListDao extends ZBaseDaoImpl<CarOilList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());

	/** 单位用户-服务 */
	@Autowired
	private CompanyCustomDao companyCustomDao;


	/**
	 * 
	 * @Description:获取车辆加油记录
	 * @param reqsrc
	 * @param unitNum
	 * @param plateNum 
	 * @param oilStation
	 * @param sTime
	 * @param eTime
	 * @param isCheck
	 * @param driver
	 * @param oilWay
	 * @param timeType 0添加时间  1加油时间
	 * @param page
	 * @param rows
	 * @return
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Page<CarOilList> findCoiList(ReqSrc reqsrc, String unitNum, String plateNum, String oilStation, String sTime,
			String eTime, String isCheck, String driver, String oilWay, String timeType, String page, String rows) {
		String logtxt = U.log(log, "获取-单位驾驶员加油-分页列表", reqsrc);

		Page<CarOilList> pd = new Page<CarOilList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {
				// 没有查询出数据
				filts.add(new Filtration(MatchType.ISNULL, "", "id"));
			}

			comps.add(new Compositor("id", CompositorType.DESC));

			List<Filtration> filtrations = new ArrayList<Filtration>();
			filtrations.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));// 当前车队
			if (StringUtils.isNotEmpty(plateNum)) {
				filtrations.add(new Filtration(MatchType.LIKE, plateNum, "plateNum"));// 车牌号
			}
			if (StringUtils.isNotEmpty(isCheck)) {
				filtrations.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));// 审核状态
			}
			if (StringUtils.isNotEmpty(oilStation)) {
				filtrations.add(new Filtration(MatchType.EQ, oilStation, "oilStation"));// 加油站
			}

			if (StringUtils.isNotEmpty(driver)) {
				filtrations.add(new Filtration(MatchType.EQ, driver, "oilDriver.uname"));// 驾驶员
			}
			if (StringUtils.isNotEmpty(oilWay)) {
				filtrations.add(new Filtration(MatchType.EQ, OilWay.valueOf(oilWay), "oilWay"));// 加油方式
			}
			if (StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)) { // 时间
				if ("0".equals(timeType)) {
					filtrations.add(new Filtration(MatchType.GE, DateUtils.strToDate("yyyy-MM-dd", sTime), "addTime"));
					filtrations.add(new Filtration(MatchType.LE,
							DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", eTime + " 23:59:59"), "addTime"));
				} else {
					filtrations.add(new Filtration(MatchType.GE, DateUtils.strToDate("yyyy-MM-dd", sTime), "oilDate"));
					filtrations.add(new Filtration(MatchType.LE,
							DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", eTime + " 23:59:59"), "oilDate"));
				}
			}

			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = findPageByOrders(pd); // 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		return pd;
	}
	
	/**
	 * 获取-加油记账-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @param unitNum 	单位编号
	 * @param uname 	记账用户账号
	 */
	public Page<CarOilList> findPageJyjzList(ReqSrc reqsrc, String page, String rows, String stime, String etime,
		String unitNum, String uname) {
		String logtxt = U.log(log, "获取-加油记账-分页列表", reqsrc);
		
		Page<CarOilList> pd = new Page<CarOilList>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			////////////////////--默认排序--//////////////////////////
			// 加油日期-倒序
			comps.add(new Compositor("oilDate", CompositorType.DESC));
			/////////////////// --条件--begin//////////////////////////
			// 指定查询[单位]
			filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
	
			// 指定查询加油用户
			filts.add(new Filtration(MatchType.EQ, uname, "oilDriver.uname"));
	
			// 查询-指定[加油日期]时间段
			if (StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)) {
				List<Filtration> fland = new ArrayList<Filtration>();
				fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "oilDate"));
				fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "oilDate"));
				filts.add(new Filtration(MatchType.AND, fland, ""));
			}
			/////////////////// --条件--end////////////////////////////
			
			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = findPageByOrders(pd); 								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}

	/**
	 * 获取-车辆加油数据
	 * @param unitNum 	单位编号
	 * @param driver 	指定驾驶员用户名
	 * @param type 		类型：0-加油方式；1-充值卡；2-加油站；3-维修站；
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findCarJzDat(String unitNum, String luname, int type) {
		String logtxt = U.log(log, "获取-车辆加油数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		
		try {
			if(type == 0) {
				List<Item> its = new ArrayList<Item>();
				
				OilWay[] ows = OilWay.values();
				for (int i = 0; i < ows.length - 1; i++) {
					if (ows[i] != OilWay.YGC_JY && ows[i] != OilWay.CZK_JY) {// 取消油罐车、暂无充值卡
						its.add(new Item(ows[i].name(), ows[i].getOilWayText(), i));
					}
				}

				map.put("data", its);
				
				U.setPut(map, 1, "获取[加油方式列表]成功"); 
			}else if(type == 1) {
//				hql = "from CompanyCustom where cusType = ?0 and isDel = ?1 order by oilType asc";
//				List<OilCardList> oilCard = oclSer.findhqlList(hql, CusType., 0);

				List<Item> its = new ArrayList<Item>();

//				for (OilCardList o : oilCard) {
//					its.add(new Item(o.getId() + "", o.getCardNo(), o.getBalance()));
//				}

				map.put("data", its);
				
				U.setPut(map, 1, "获取[充值卡列表]成功"); 
			}else if(type == 2) {
				hql = "from CompanyCustom where cusType = ?0 and isDel = ?1 order by id asc"; 
				List<CompanyCustom> comCuss = companyCustomDao.findhqlList(hql, CusType.OILSTATION, 0);
		  
				List<Item> its = new ArrayList<Item>();
				for(CompanyCustom o : comCuss){
					if(StringUtils.isNotBlank(o.getUnitName())) {// 存在名称
						its.add(new Item(o.getId()+"", o.getUnitName(), o.getAddress())); 
					}
				}
			  
				map.put("data", its);
			  
				U.setPut(map, 1, "获取[加油站列表]成功"); 
			}else if(type == 3) {
				hql = "from CompanyCustom where cusType = ?0 and isDel = ?1 order by id asc"; 
				List<CompanyCustom> comCuss = companyCustomDao.findhqlList(hql, CusType.REPAIR, 0);
		  
				List<Item> its = new ArrayList<Item>();
				for(CompanyCustom o : comCuss){ 
					if(StringUtils.isNotBlank(o.getUnitName())) {// 存在名称
						its.add(new Item(o.getId()+"", o.getUnitName(), o.getAddress()));
					}
				}
		  
				map.put("data", its);
				
				U.setPut(map, 1, "获取[维修站列表]成功"); 
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
}
