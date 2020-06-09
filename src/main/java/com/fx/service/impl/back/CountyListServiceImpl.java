package com.fx.service.impl.back;

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
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.CityListDao;
import com.fx.dao.back.CountyListDao;
import com.fx.entity.back.CityList;
import com.fx.entity.back.CountyList;
import com.fx.service.back.CountyListService;


@Service
@Transactional
public class CountyListServiceImpl extends BaseServiceImpl<CountyList, Long> implements CountyListService {
	/** 日志记录 */
	private Logger				log	= LogManager.getLogger(this.getClass());
	
	/** 城市-数据源 */
	@Autowired
	private CityListDao cityDao;
	/** 区/县-数据源 */
	@Autowired
	private CountyListDao countyListDao;
	
	@Override
	public ZBaseDaoImpl<CountyList, Long> getDao() {
		return countyListDao;
	}
	@Override
	public int updCountViews(CountyList c) {
		try {
			countyListDao.update(c);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	@Override
	public int resetCountyOrder(String cityCode) {
		try {
			List<CountyList> countys = countyListDao.findhqlList(" from CountyList where cityCode = ? order by countyCode asc", cityCode);
			for(int i = 0; i < countys.size(); i++){
				countys.get(i).setHotOrder(i+1);
				countyListDao.update(countys.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	@Override
	public int moveCountyOrder(String currCity, int currOrder, int targetOrder, int sta) {
		try {
			//首先获取当前序号、目标序号所在对象id，并保存起来
			long currId = 0, moveToId = 0;
			String hql = " from CountyList where cityCode = ? and hotOrder = ?";
			CountyList currCounty = countyListDao.findObj(hql, currCity, currOrder);
			currId = currCounty.getId();//保存当前区县id
			CountyList moveToCounty = countyListDao.findObj(hql, currCity, targetOrder);
			moveToId = moveToCounty.getId();//保存目标区县id
			if(sta == 1){
				//执行序号交换
				String updHql = "update CountyList set hotOrder = ? where id = ?";
				countyListDao.batchExecute(updHql, currOrder, Long.valueOf(moveToId));//更新目标对象的排序号
				countyListDao.batchExecute(updHql, targetOrder, Long.valueOf(currId));//更新当前对象的排序号
			}else{
				//1.当前序号大于目标序号
				if(currOrder > targetOrder){
					//从目标序号到当前序号前一个序号的对象的序号都加1. ?:目标 ?:当前
					hql = "update CountyList set hotOrder = (hotOrder + 1) where cityCode = ? and hotOrder >= ? and hotOrder < ?";
				}else if(currOrder < targetOrder){//2.当前序号小于目标序号
					//从目标行排序号到当前行排序号下一号都自减1
					//从目标后一个序号的对象到当前序号的对象的序号都减1. ?:目标 ?:当前
					hql = "update CountyList set hotOrder = (hotOrder - 1) where cityCode = ? and hotOrder <= ? and hotOrder > ?";
				}
				countyListDao.batchExecute(hql, currCity, targetOrder, currOrder);
				//再将当前行的排序号更改成目标行的排序号
				hql = "update CountyList set hotOrder = ? where id = ?";
				countyListDao.batchExecute(hql, targetOrder, Long.valueOf(currId));//更新目标对象的排序号
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	@Override
	public Map<String, Object> findCountyList(String city) {
		String logtxt = U.log(log, "获取-城市区/县列表");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(city)) {
					U.log(log, "[城市名称/编号]为空");
				}else {
					city = city.trim();
					
					U.log(log, "[城市名称/编号] city="+city);
				}
			}
			
			String cityCode = "";// 城市编号
			if(fg && StringUtils.isNotEmpty(city)) {
				CityList cl = cityDao.findByField("cityName", city);
				if(cl == null) {
					U.log(log, "[通过城市名称查询]对象为空");
					
					cl = cityDao.findByField("cityCode", city);
					if(cl == null) {
						U.log(log, "[通过城市编号查询]对象为空");
					}
				}
				
				if(cl == null) {
					fg = U.setPutFalse(map, "该[城市名称/编号]对应城市不存在");
				}else {
					cityCode = cl.getCityCode();
				}
				
				U.log(log, "[城市编号] cityCode="+cityCode);
			}
			
			if(fg) {
				StringBuilder sb = new StringBuilder("from CountyList where 1 = 1 ");
				List<Object> ps = new ArrayList<Object>();
				
				if(StringUtils.isNotBlank(cityCode)){
					sb.append("and cityCode = ?0 ");
					ps.add(cityCode);
				}
				sb.append("order by hotOrder asc");
				List<CountyList> list = countyListDao.findhqlList(sb.toString(), ps.toArray());
				
				map.put("data", list);
				
				U.setPut(map, 1, "获取列表成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
