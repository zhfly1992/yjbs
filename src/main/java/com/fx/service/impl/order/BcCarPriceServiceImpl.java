package com.fx.service.impl.order;

import java.util.ArrayList;
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
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.order.BcCarPriceDao;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.BcCarPrice;
import com.fx.service.company.TouristCharterPriceService;
import com.fx.service.order.BcCarPriceService;

@Service
@Transactional
public class BcCarPriceServiceImpl extends BaseServiceImpl<BcCarPrice, Long> implements BcCarPriceService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 旅游包车-临时车辆价格-数据源 */
	@Autowired
	private BcCarPriceDao bcCarPriceDao;
	@Override
	public ZBaseDaoImpl<BcCarPrice, Long> getDao() {
		return bcCarPriceDao;
	}
	
	@Autowired
	private TouristCharterPriceService toupSer;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findBcCarPriceList(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, String teamNo, Customer lcus, String dayNum) {
		String logtxt = U.log(log, "获取-旅游包车-临时车辆价格数据列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(teamNo)) {
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else {
					teamNo = teamNo.trim();
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			String luname = "";
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					luname = lcus.getBaseUserId().getUname();
					
					U.log(log, "[登录用户名] uname="+luname);
				}
			}
			
			int _dayNum = 0;
			if(fg) {
				if(StringUtils.isEmpty(dayNum)) {
					fg = U.setPutFalse(map, "[天数行程数量]不能为空");
				}else {
					if(!FV.isPosInteger(dayNum)) {
						fg = U.setPutFalse(map, "[天数行程数量]格式错误");
					}else {
						_dayNum = Integer.parseInt(dayNum);
					}
					
					U.log(log, "[天数行程数量] dayNum="+dayNum);
				}
			}
			
			if(fg) {
				// 清空本次订单多余行程的临时订单参数数据
				hql = "delete from BcOrderParam where companyNum = ? and uname = ? and routeNo > ?";
				int a = batchExecute(hql, teamNo, luname, _dayNum);
				U.log(log, "删除"+a+"条多余的旅游包车临时参数");
				
				// 清空本次订单临时车辆价格数据
				hql = "delete from BcCarPrice where uname = ?";
				int b = batchExecute(hql, luname);
				U.log(log, "删除"+b+"条临时车辆价格数据");
			}
			
			if(fg) {
				String mainOrderNum = UT.creatMainOrderNum(2);
				hql = "UPDATE BcOrderParam SET mainOrderNum = ? WHERE teamNo = ? and uname = ?";
				int c = batchExecute(hql, mainOrderNum, teamNo, luname);
				U.log(log, "更新"+c+"条旅游包车临时参数");
				if(c == 0) {
					fg = U.setPutFalse(map, "没有旅游包车临时参数数据，请重新选择行程");
				}else {
					List<BcCarPrice> list = new ArrayList<BcCarPrice>();
					// 调用-获取车辆价格数据列表方法
					Map<String, Object> lmap = toupSer.findTour(null, null, null, null, null, 
						null, null, null, null, null, null, mainOrderNum);
					if(lmap.get("price") != null) {
						list = (List<BcCarPrice>)lmap.get("price");
					}
					
					if(list.size() == 0) {
//						U.setPut(map, 0, "抱歉，没有查询到车辆，请重新选择行程");
						
						BcCarPrice bcp1 = new BcCarPrice(mainOrderNum, 7, 800, 780, "经济型 东风 商务车（测试数据）", "400=390;400=390;");
						bcCarPriceDao.save(bcp1);
						
						BcCarPrice bcp2 = new BcCarPrice(mainOrderNum, 25, 1800, 1780, "经济型 东风 中巴车（测试数据）", "900=890;900=890;");
						bcCarPriceDao.save(bcp2);
						
						BcCarPrice bcp3 = new BcCarPrice(mainOrderNum, 30, 2800, 2780, "经济型 金龙 大巴车（测试数据）", "1400=1390;1400=1390;");
						bcCarPriceDao.save(bcp3);
						
						BcCarPrice bcp4 = new BcCarPrice(mainOrderNum, 38, 3800, 3780, "经济型 金龙 大巴车（测试数据）", "1900=1890;1900=1890;");
						bcCarPriceDao.save(bcp4);
						
						BcCarPrice bcp5 = new BcCarPrice(mainOrderNum, 40, 3000, 2980, "经济型 金龙 大巴车（测试数据）", "1500=1490;1500=1490;");
						bcCarPriceDao.save(bcp5);
						
						BcCarPrice bcp6 = new BcCarPrice(mainOrderNum, 55, 4800, 4780, "经济型 金龙 大巴车（测试数据）", "2400=2390;2400=2390;");
						bcCarPriceDao.save(bcp6);
						
						list.add(bcp1);
						list.add(bcp2);
						list.add(bcp3);
						list.add(bcp4);
						list.add(bcp5);
						list.add(bcp6);
						
						map.put("list", list);
						
						U.setPut(map, 1, "获取-车辆价格数据列表-成功");
					}else {
						map.put("list", list);
						
						U.setPut(map, 1, "获取-车辆价格数据列表-成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
