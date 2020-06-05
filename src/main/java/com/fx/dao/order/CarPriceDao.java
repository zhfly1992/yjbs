package com.fx.dao.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.entity.company.AreaRebateSet;
import com.fx.entity.company.PartnerCustomer;
import com.fx.entity.company.PickupAreaSet;
import com.fx.entity.company.PickupPartner;
import com.fx.entity.company.PriceStair;
import com.fx.entity.company.PublicDataSet;
import com.fx.entity.company.RouteSet;
import com.fx.entity.order.CarPrice;
import com.fx.entity.order.DiscountDetail;

@Repository
public class CarPriceDao extends ZBaseDaoImpl<CarPrice, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * 获取-单程接送-接送机车辆价格
	 * @param uname			登录用户名
	 * @param teamNo		登录用户所属车队编号
	 * @param isShuttle 	是否接送：0-接；1-送；
	 * @param selCity		选择地点城市名称：接机-目的地城市；送机-出发地城市；
	 * @param selCounty		选择地点城市所在区县名称：接机-目的地区县；送机-出发地区县；
	 * @param spoint		起点坐标：104.959373,30.578421
	 * @param epoint		终点坐标：103.959373,30.578421
	 * @param terminal 		固定站点（xx市 xxx）：[接送机：接机-上车点（即机场）名称；送机-下车点（即机场）名称；]/[火车/汽车站点：xx车站]
	 * @param gotime		出发时间
	 * @param carType		车辆类型：0-大巴, 1-中巴, 2-商务, 4-小车
	 * @param config		车辆配置：经济型、豪华型...
	 * @param seat			车辆座位数
	 * @param minDistance	区域中心点-->乘客上车点 => 地球直线距离
	 * @param ucp2dcp		乘客上车点-->乘客下车点=> 行程距离C
	 * @param mpAC			行程距离AC[接机：起点->终点->中心点; 送机：中心点->起点->终点]
	 * @param mpB			行程距离B[接机：中心点->起点; 送机：中心点->终点]
	 * @param finalArea		最终的区域对象
	 * @param route			最终的一条行程对象
	 * @param ps			符合行程的价格阶梯对象
	 * @param opid			发布订单参数对象id
	 * @param pp			车队合作账号对象
	 * @return map{code: 结果状态码, msg: 结果状态信息, data: 数据}
	 */
	public CarPrice findCarPrice(String uname, String teamNo, int isShuttle, String selCity, 
		String selCounty, String spoint, String epoint, String terminal, Date gotime, 
		int carType, String config, int seat, double minDistance, CarRouteRes ucp2dcp, 
		CarRouteRes mpAC, CarRouteRes mpB, PickupAreaSet finalArea, RouteSet route, PriceStair ps, 
		long opid, PickupPartner pp) {
		String logtxt = U.log(log, "获取-接送机车辆价格");
		
		CarPrice cp = new CarPrice();// 最终车辆价格对象
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			// 基础价格（即行程单价）
			double basePrice = ps.getPrice();
			U.log(log, "【基础价格："+basePrice+"】");
			// 行程总公里数, 行程总耗时
			double totalKm = 0d, totalMin = 0d; 
			if(fg){
				totalKm = MathUtils.div(ucp2dcp.getDistance(), 1000, 2);// 转换成公里
				totalMin = ucp2dcp.getTimeCons()/60;// 转换成分钟
				
				U.log(log, "订单行程距离："+totalKm+"千米, 耗时："+totalMin+"分钟");
			}
			
			double beyondRangeMoney = 0;// 超范围金额
			if(fg){
				if(minDistance <= finalArea.getDefAreaRound()){// 未超范围（注：地图选择点在区域范围内均不算超范围）
					U.log(log, "未超范围：区域中心点-->乘客地图选择点=>直线距离："+minDistance+"米，半径："+finalArea.getDefAreaRound()+"米");
				}else{// 已超范围
					double dis_C = totalKm * 1000;		// 行程距离
					double dis_AC = mpAC.getDistance();	// 行程距离AC
					double dis_B = mpB.getDistance();	// 行程距离B
					
					if(isShuttle == 0) {
						U.log(log, "超范围计算方式（AC-B>0 = 超范围）====【接机】");
						U.log(log, "乘客上车点-->乘客下车点 = 行程距离："+dis_C+"米");
						U.log(log, "乘客上车点-->乘客下车点-->区域中心点 =行程距离AC："+dis_AC+"米");
						U.log(log, "区域中心点-->乘客上车点 =行程距离B："+dis_B+"米");
					}else {
						U.log(log, "超范围计算方式（AC-B>0 = 超范围）====【送机】");
						U.log(log, "乘客上车点-->乘客下车点 = 行程距离："+dis_C+"米");
						U.log(log, "区域中心点-->乘客上车点-->乘客下车点 =行程距离AC："+dis_AC+"米");
						U.log(log, "区域中心点-->乘客下车点 =行程距离B："+dis_B+"米");
					}
					
					if(dis_AC > dis_B){// 表示区域中心点到上车点距离超范围，即指定点不在师傅的路线上
						double ar_dis = finalArea.getDefAreaRound();// 区域半径直线距离
						U.log(log, "区域半径"+ar_dis+"米");
						
						if(dis_B < ar_dis){// 如果行程距离B小于区域半径，则行程距离B=区域半径
							dis_B = ar_dis;
						}
						
						// AC-B>0 => 超范围
						double isBeyound = MathUtils.sub(dis_AC, dis_B, 2);
						if(isBeyound > 0){// 超范围
							double beyondKm = MathUtils.div(isBeyound, 1000, 2);// 超出公里数
							// 超范围计算：(C+B-A)*超范围单价 => 与最低加收金额比较： 大于 => 取计算值；小于 => 取最低加收金额值；
							double beyondMoney = MathUtils.mul(ps.getBeyondPrice(), beyondKm, 2);
							if(beyondMoney > ps.getBeyondMinaddMoney()){
								beyondRangeMoney = beyondMoney;
								
								U.log(log, "超范围金额 > 超范围最低加收金额，超范围金额=超范围金额");
							}else{
								beyondRangeMoney = ps.getBeyondMinaddMoney();
								
								U.log(log, "超范围金额 < 超范围最低加收金额，超范围金额=范围最低加收金额");
							}
							
							U.log(log, "超范围公里数："+beyondKm+"km，超范围价格："+ps.getBeyondPrice()+"，超范围最低加收金额："+ps.getBeyondMinaddMoney());
						}else{
							U.log(log, "AC-B<0 => 未超范围");
						}
					}else{
						U.log(log, "AC-B>0 => 指定点在师傅的路线上");
					}
				}
			}
			
			// 车价=行程价格+超范围价格（第一次计算车价）
			double sumMoney = 0d;
			if(fg){
				sumMoney = MathUtils.add(basePrice, beyondRangeMoney, 2);
				
				log.info("第1次计算车价（行程价格+超范围金额）:"+sumMoney);
			}
			
			// 计算同行优惠，应该使用行程单价*同行优惠比例，而不是（行程单价+超范围金额）*同行优惠比例；
			double preMoney = 0d; // 同行优惠金额
			PartnerCustomer pcus = null;
			if(fg){
				if(pp != null){// 存在车队合作优惠
					if(pp.getAreaRebateSet() == null){
						U.log(log, "未设置区域返点比例");
					}else{
						U.log(log, "设置了区域返点比例");
						
						double rebate = 0d;// 返点比例
						
						// 如果订单区域是用户的本地执行区域，那么则按照用户本地执行区域的返点比例
						// 如果不是在用户本地执行区域，则按照下单区域对应的执行区域的最小返点比例
						
						// 当前账号本身的执行区域
						String selfExeArea = pp.getAreaRebateSet().getArea();
						if(selfExeArea.contains(selCounty)){// 与订单区域一样
							rebate = pp.getAreaRebateSet().getRebate();
							
							U.log(log, "与订单区域一样");
						}else{
							// 当前账号其他区域执行方式
							int areaExeWay = pp.getAreaExeWay();
							
							if(areaExeWay == 1){// 1-按照账号所在区域执行；
								rebate = pp.getAreaRebateSet().getRebate();
							}else{// 2-按照订单所在区域执行；（订单所在区域最小返点比例）
								hql = "from AreaRebateSet where teamNo = ? and area like ? order by rebate asc";
								List<AreaRebateSet> arss = findhqlList(hql, pp.getAreaRebateSet().getTeamNo(), "%"+selCounty+"%");
								if(arss.size() > 0){
									rebate = arss.get(0).getRebate();// 最小返点比例
									U.log(log, "最小返点比例");
								}else{
									U.log(log, "订单所在区域【"+pp.getAreaRebateSet().getTeamNo()+"-"+selCounty+"】，未找到比例设置数据");
								}
							}
						}
						
						// 优惠金额
						preMoney = MathUtils.mul(basePrice, rebate/100, 2);
					}
				}else{
					U.log(log, "不存在车队合作优惠");
					
					pcus = findObj("from PartnerCustomer where account = ? and partnerLevel = ? and validTime <= ?", uname, 2, new Date());
					if(pcus != null){
						preMoney = MathUtils.mul(basePrice, route.getPeerPrefRatio()/100, 2);
					}else{
						U.log(log, "不是合作客户");
					}
				}
				
				U.log(log, "同行优惠金额："+preMoney);
			}
			
			// 计算加班金额
			double overMoney = 0;// 加班金额
			if(fg){
				if(StringUtils.isNotEmpty(ps.getOverTime()) && !"0".equals(ps.getOverTime())){
					if(UT.isIncludeDate(ps.getOverTime(), gotime)){// 预约时间是在加班时间之内
						if(pp != null || pcus != null){
							overMoney = ps.getPeerOverTimePrice();// 是同行，加班价格为同行加班价格
							
							U.log(log, "同行加班价格："+overMoney);
						}else{
							overMoney = ps.getOverTimePrice();// 不是同行，则按照一般加班价格
							
							U.log(log, "一般加班价格："+overMoney);
						}
					}
				}else{
					U.log(log, "没有设置加班时间段");
				}
			}
			
			// 计算最终优惠金额 = 同行优惠金额 + 优惠券金额 
			double finalCouponMoney = 0d;
			if(fg){
				finalCouponMoney = preMoney;
				
				U.log(log, "最终优惠金额："+finalCouponMoney);
			}
			
			// 车价
			double resMoney = 0d; 
			if(fg){
				// 最终车价 = 第一次车价 - 总优惠金额 + 加班金额 + 增加途经点金额
				resMoney = MathUtils.sub(sumMoney, finalCouponMoney, 2);
				
				resMoney = MathUtils.add(resMoney, overMoney, 2);
				
				if(resMoney <= 0){// 支付金额小于0
					U.log(log, "支付金额为"+resMoney+"，则支付金额为："+resMoney);
					
					resMoney = 0.01;// 当支付金额小于0时，则支付金额为0.01，因为0支付金额无意义。
				}
				
				if(fg){
					hql = "from PublicDataSet where setId = ? and type = ?";
					PublicDataSet pds = findObj(hql, teamNo, 10);
					if(pds != null){// 设置了假日价格
						U.log(log, "车队："+teamNo+", 设置了假日价格");
						
						resMoney = UT.getFeastdayPrice(pds, gotime, seat, resMoney);
					}else{// 未设置假日价格
						U.log(log, "车队："+teamNo+", 未设置了假日价格");
						
						//resMoney = UTools.getFeastdayPrice(null, gotime, seat, resMoney);
					}
					
					U.log(log, "最终车价："+resMoney);
				}
				
				U.log(log, "【车价："+resMoney+"】");
			}
			
			if(fg){
				DiscountDetail dd = new DiscountDetail();
				dd.setPeerMoney(preMoney);
				dd.setOverMoney(overMoney);
				dd.setBeyondRangeMoney(beyondRangeMoney);
				dd.setPayMoney(resMoney);
				cp.setDisJson(U.toJsonStr(dd));
				U.log(log, "优惠对象json化完成");
				
				cp.setOpid(opid);
				cp.setUname(uname);
				cp.setPsid(ps.getId());
				cp.setConfig(config);
				cp.setPrice(Math.ceil(resMoney));
				cp.setSeat(seat);
				cp.setCarType(carType);
				save(cp);
				
				U.setPut(map, 1, "获取车辆价格数据成功");
			}
			
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return cp;
	}
	
}
