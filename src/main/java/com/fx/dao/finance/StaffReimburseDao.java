package com.fx.dao.finance;

import java.util.ArrayList;
import java.util.List;

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
import com.fx.commons.utils.enums.JzType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.finance.CarOilList;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.StaffReimburse;

@Repository
public class StaffReimburseDao extends ZBaseDaoImpl<StaffReimburse, Long> {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());
	
	/** 单位车辆-服务 */
	@Autowired
	private CompanyVehicleDao companyVehicleDao;
	/** 车辆加油记账-服务 */
	@Autowired
	private CarOilListDao carOilListDao;
	/** 车辆维修记账-服务 */
	@Autowired
	private CarRepairListDao carRepairListDao;
	

	/**
	 * 获取-员工报账-分页列表
	 * 
	 * @param reqsrc 请求来源
	 * @param page 页码
	 * @param rows  页大小
	 * @param unitNum  单位编号
	 * @param uname    报销人账号
	 * @param deptId   业务部门id
	 * @param remark   摘要
	 * @param money  金额
	 * @param isCheck 状态
	 * @param addMark 添加标识
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return Page<T> 分页数据
	 */
	public Page<StaffReimburse> findStaffReimburse(ReqSrc reqsrc, String page, String rows, String unitNum, String uname,String plateNum,
			String deptId,String remark,String money,String isCheck,String voucherNo,String operMark,String sTime,String eTime) {
		String logtxt = U.log(log, "获取-员工报账-分页列表", reqsrc);

		Page<StaffReimburse> pd = new Page<StaffReimburse>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			} else {
				comps.add(new Compositor("addTime", CompositorType.DESC));
				filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));// 单位编号

				if (StringUtils.isNotBlank(uname)) {
					filts.add(new Filtration(MatchType.EQ, uname, "reimUserId.uname"));
				}
				if (StringUtils.isNotBlank(plateNum)) {
					filts.add(new Filtration(MatchType.EQ, uname, "plateNum"));
				}
				if (StringUtils.isNotBlank(deptId)) {
					filts.add(new Filtration(MatchType.EQ, deptId, "deptId.id"));
				}
				if (StringUtils.isNotBlank(remark)) {
					filts.add(new Filtration(MatchType.LIKE, remark, "remark"));
				}
				if (StringUtils.isNotBlank(money)) {
					filts.add(new Filtration(MatchType.EQ, Double.valueOf(money), "gathMoney","payMoney"));
				}
				if (StringUtils.isNotBlank(isCheck)) {
					filts.add(new Filtration(MatchType.EQ, Integer.parseInt(isCheck), "isCheck"));
				}else {//默认加载未审核数据
					filts.add(new Filtration(MatchType.EQ, 0, "isCheck"));
				}
				if (StringUtils.isNotBlank(voucherNo)) {
					filts.add(new Filtration(MatchType.EQ, voucherNo, "voucherNo"));
				}
				if (StringUtils.isNotBlank(operMark)) {
					filts.add(new Filtration(MatchType.EQ, operMark, "operMark"));
				}
				if(StringUtils.isNotEmpty(sTime) && StringUtils.isNotEmpty(eTime)){ //时间
					filts.add(new Filtration(MatchType.GE,DateUtils.strToDate("yyyy-MM-dd",sTime),"addTime"));
					filts.add(new Filtration(MatchType.LE,DateUtils.strToDate("yyyy-MM-dd HH:mm:ss",eTime+" 23:59:59"),"addTime"));
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
	 * 判断-指定记录公里数是否在其前后记录公里数之间
	 * @param obj 		记账对象（加油、维修、其他、行程收支）
	 * @param reimType 	报销类型：3加油报销  4维修报销
	 * @return 数组[最小值, 最大值, 下一条记录id]
	 */
	public Object[] getMinMaxKm(Object obj, int reimType) {
		String logtxt = U.log(log, "判断-指定记录公里数是否在其前后记录公里数之间");
		
		boolean fg = true;
		String hql = "";
		Object[] res = new Object[] {0, 0, 0};
		
		try {
			int index = -1;
			if(reimType == 3) {
				U.log(log, "加油报销");
				
				CarOilList o = (CarOilList)obj;
				
				// 查询记录的最小和最大里程（此条件控制了数据筛选量变少）
				double minMil = 0d, maxMil = 0d;
				CompanyVehicle car = null;
				if(fg) {
					car = companyVehicleDao.findCompanyCar(o.getUnitNum(), o.getPlateNum());
					if(car == null) {
						fg = U.logFalse(log, "【车辆"+o.getPlateNum()+"不存在，请联系管理员】");
					}else {
						// 按照要修改的公里数向前加车辆里程作为最大值，向后减车辆里程作为最小值
						minMil = MathUtils.sub(o.getCurrentKilo(), car.getMileage(), 2);
						maxMil = MathUtils.add(o.getCurrentKilo(), car.getMileage(), 2);
						
						U.log(log, "查询加油记账车辆公里数区间为：["+minMil+", "+maxMil+"]");
					}
				}
				
				List<CarOilList> list = new ArrayList<CarOilList>();
				if(fg) {
					// 获取车辆加油记账，公里数控制在区间内
					hql = "select new CarOilList(id, currentKilo) from CarOilList where unitNum = ?0 and plateNum = ?1 and (currentKilo >= ?2 and currentKilo <= ?3) order by id desc";
					list = carOilListDao.findhqlList(hql, o.getUnitNum(), o.getPlateNum(), minMil, maxMil);
					if(list.size() == 0) {
						fg = U.logFalse(log, "没有获取到驾驶员["+o.getOilDriver().getUname()+"]的加油记账列表");
					}
				}
				
				// 遍历列表，找到指定id数据所在列表的下标
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getId() == o.getId()) {
						index = i;
						break;
					}
				}
				
				if(fg) {
					if(index < 0) {
						fg = U.logFalse(log, "没有找到指定数据id["+o.getId()+"]所在列表的下标index");
					}else {
						// 找到指定id的下标，则获取该下标的前后两条数据
						double maxKm = 0d, minKm = 0d;// 最大里程数，最小里程数
						
						if(index == 0) {
							U.log(log, "不存在上一个下标的数据，则上限是该车辆的续航里程数");
							
							// 车辆最大续航里程 = 上一次记录的车辆总里程 + 车辆里程数
							maxKm = MathUtils.add(list.get(0).getCurrentKilo(), car.getMileage(), 2);
						}else {
							maxKm = list.get(index - 1).getCurrentKilo();
							res[2] = list.get(index - 1).getId();
						}
						
						if(index >= list.size() - 1) {
							U.log(log, "不存在下一个下标的数据，则下限是0");
							minKm = 0;
						}else {
							minKm = list.get(index + 1).getCurrentKilo();
						}
						U.log(log, "最大里程数为："+maxKm+", 最小里程数为："+minKm);
						
						res[0] = minKm;
						res[1] = maxKm;
					}
				}
			}else if(reimType == 4) {
				U.log(log, "维修报销");
				
				CarRepairList o = (CarRepairList)obj;
				
				// 查询记录的最小和最大里程（此条件控制了数据筛选量变少）
				double minMil = 0d, maxMil = 0d;
				CompanyVehicle car = null;
				if(fg) {
					car = companyVehicleDao.findCompanyCar(o.getUnitNum(), o.getPlateNum());
					if(car == null) {
						fg = U.logFalse(log, "【车辆"+o.getPlateNum()+"不存在，请联系管理员】");
					}else {
						// 按照要修改的公里数向前加车辆里程作为最大值，向后减车辆里程作为最小值
						minMil = MathUtils.sub(o.getCurrentKilo(), car.getMileage(), 2);
						maxMil = MathUtils.add(o.getCurrentKilo(), car.getMileage(), 2);
						
						U.log(log, "查询加油记账车辆公里数区间为：["+minMil+", "+maxMil+"]");
					}
				}
				
				List<CarRepairList> list = new ArrayList<CarRepairList>();
				if(fg) {
					// 获取驾驶员所有维修记账，按照车辆公里数倒序排序（因为一般修改都是修改最近的数据）
					hql = "select new CarRepairList(id, currentKilo) from CarRepairList where unitNum = ?0 and plateNum = ?1 and (currentKilo >= ?2 and currentKilo <= ?3) order by id desc";
					list = carRepairListDao.findhqlList(hql, o.getUnitNum(), o.getPlateNum(), minMil, maxMil);
					if(list.size() == 0) {
						fg = U.logFalse(log, "没有获取到驾驶员["+o.getRepairDriver().getUname()+"]的加油记账列表");
					}
				}
				
				// 遍历列表，找到指定id数据所在列表的下标
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getId() == o.getId()) {
						index = i;
						break;
					}
				}
				
				if(fg) {
					if(index < 0) {
						fg = U.logFalse(log, "没有找到指定数据id["+o.getId()+"]所在列表的下标index");
					}else {
						// 找到指定id的下标，则获取该下标的前后两条数据
						double maxKm = 0d, minKm = 0d;// 最大里程数，最小里程数
						
						if(index == 0) {
							U.log(log, "不存在上一个下标的数据，则上限是该车辆的续航里程数");
							
							// 车辆最大续航里程 = 上一次记录的车辆总里程 + 车辆里程数
							maxKm = MathUtils.add(list.get(0).getCurrentKilo(), car.getMileage(), 2);
						}else {
							maxKm = list.get(index - 1).getCurrentKilo();
							res[2] = list.get(index - 1).getId();
						}
						
						if(index >= list.size() - 1) {
							U.log(log, "不存在下一个下标的数据，则下限是0");
							minKm = 0;
						}else {
							minKm = list.get(index + 1).getCurrentKilo();
						}
						U.log(log, "最大里程数为："+maxKm+", 最小里程数为："+minKm);
						
						res[0] = minKm;
						res[1] = maxKm;
					}
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return res;
	}

	/**
	 * 获取-其他记账-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @param unitNum 	单位编号
	 * @param uname 	记账用户账号
	 */
	public Page<StaffReimburse> findPageQtjzList(ReqSrc reqsrc, String page, String rows, String stime, String etime,
		String unitNum, String uname) {
		String logtxt = U.log(log, "获取-其他记账-分页列表", reqsrc);
		
		Page<StaffReimburse> pd = new Page<StaffReimburse>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			////////////////////--默认排序--//////////////////////////
			// 记账日期-倒序
			comps.add(new Compositor("addTime", CompositorType.DESC));
			/////////////////// --条件--begin//////////////////////////
			// 指定查询[单位]
			filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
	
			// 指定查询记账用户
			filts.add(new Filtration(MatchType.EQ, uname, "reimUserId.uname"));
			
			// 指定查询记账类型
			filts.add(new Filtration(MatchType.EQ, JzType.QTJZ, "jzType"));
	
			// 查询-指定[记账日期]时间段
			if (StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)) {
				List<Filtration> fland = new ArrayList<Filtration>();
				fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "addTime"));
				fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "addTime"));
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
	 * 获取行程记账-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	开始时间
	 * @param etime 	结束时间
	 * @param unitNum 	单位编号
	 * @param uname 	记账用户账号
	 */
	public Page<StaffReimburse> findPageXcjzList(ReqSrc reqsrc, String page, String rows, String stime, String etime,
		String unitNum, String uname) {
		String logtxt = U.log(log, "获取-行程记账-分页列表", reqsrc);
		
		Page<StaffReimburse> pd = new Page<StaffReimburse>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			////////////////////--默认排序--//////////////////////////
			// 记账日期-倒序
			comps.add(new Compositor("addTime", CompositorType.DESC));
			/////////////////// --条件--begin//////////////////////////
			// 指定查询[单位]
			filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));
	
			// 指定查询记账用户
			filts.add(new Filtration(MatchType.EQ, uname, "reimUserId.uname"));
			
			// 指定查询记账类型
			filts.add(new Filtration(MatchType.EQ, JzType.XCSZ, "jzType"));
	
			// 查询-指定[记账日期]时间段
			if (StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)) {
				List<Filtration> fland = new ArrayList<Filtration>();
				fland.add(new Filtration(MatchType.GE, DateUtils.std_st(stime), "addTime"));
				fland.add(new Filtration(MatchType.LE, DateUtils.std_et(etime), "addTime"));
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

	
}
