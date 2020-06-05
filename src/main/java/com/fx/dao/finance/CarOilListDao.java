package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.utils.enums.OilWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.U;
import com.fx.entity.finance.CarOilList;

@Repository
public class CarOilListDao extends ZBaseDaoImpl<CarOilList, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());



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
}
