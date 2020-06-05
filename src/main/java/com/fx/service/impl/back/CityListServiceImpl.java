package com.fx.service.impl.back;

import java.util.ArrayList;
import java.util.Collections;
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
import com.fx.dao.back.ProvinceListDao;
import com.fx.entity.back.CityList;
import com.fx.entity.back.ProvinceList;
import com.fx.service.back.CityListService;

@Service
@Transactional
public class CityListServiceImpl extends BaseServiceImpl<CityList, Long> implements CityListService {
	/** 日志记录 */
	private Logger			log	= LogManager.getLogger(this.getClass());

	/** 省份-服务 */
	@Autowired
	private ProvinceListDao	provinceListDao;
	/** 城市-服务 */
	@Autowired
	private CityListDao		cityListDao;



	@Override
	public ZBaseDaoImpl<CityList, Long> getDao() {
		return cityListDao;
	}



	@Override
	public List<CityList> getCurrList(String currCity) {
		// *****************************获取当前城市名称--begin*****************************//
		List<CityList> citys1 = new ArrayList<CityList>();
		if (StringUtils.isNotEmpty(currCity)) {
			// 获取城市列表、并设置默认为当前城市
			CityList cc = cityListDao.findByField("cityName", currCity);// 根据城市名称获取当前城市对象
			// 根据当前城市所属省份编号，获取当前省份所有城市（热门序号从小到大排序），并将当前城市排在第一位
			citys1 = cityListDao.findhqlList("from CityList where provinceCode = ? order by hotOrder asc",
					cc.getProvinceCode());
			for (int i = 0; i < citys1.size(); i++) {// 将当前城市排在列表中的第一个，为了方便页面上默认显示为当前城市
				if (currCity.equals(citys1.get(i).getCityName())) {
					Collections.swap(citys1, 0, i);
				}
			}
			// 查询除当前省份的所有城市，并以省份编号从小到大，城市热门序号从小到大排序
			List<CityList> citys2 = cityListDao.findhqlList(
					"from CityList where provinceCode <> ? order by provinceCode asc,hotOrder asc",
					cc.getProvinceCode());
			// 将当前省份的城市放在所有城市的最前面
			citys1.addAll(citys2);
		} else {
			// 查询所有城市列表，并以拼音字母、热门顺序排序
			citys1 = cityListDao.findhqlList("from CityList order by provinceCode asc,hotOrder asc");
		}
		// *****************************获取当前城市名称--begin*****************************//
		return citys1;
	}



	@Override
	public List<CityList> getCityListByHot() {
		// 查询除当前省份的所有城市，并以省份编号从小到大，城市热门序号从小到大排序
		String hql = "from CityList order by hotOrder asc,pinyinSimple asc";
		List<CityList> citys = cityListDao.findhqlList(hql);
		return citys;
	}



	@Override
	public int resetCityOrder(String proCode) {
		try {
			// 根据省份编号获取对应城市列表，并城市编号排序
			List<CityList> cityList = cityListDao
					.findhqlList(" from CityList where provinceCode = ? order by cityCode asc", proCode);
			// 遍历城市列表进行排序
			for (int j = 0; j < cityList.size(); j++) {
				cityList.get(j).setHotOrder(j + 1);
				cityListDao.update(cityList.get(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}



	@Override
	public int moveCityOrder(String currPro, int currOrder, int targetOrder, int sta) {
		try {
			// 首先获取当前序号、目标序号所在对象id，并保存起来
			long currId = 0, moveToId = 0;
			String hql = " from CityList where provinceCode = ? and hotOrder = ?";
			CityList currCity = cityListDao.findObj(hql, currPro, currOrder);
			currId = currCity.getId();// 保存当前城市id
			CityList moveToCity = cityListDao.findObj(hql, currPro, targetOrder);
			moveToId = moveToCity.getId();// 保存目标城市id
			if (sta == 1) {
				// 执行序号交换
				String updHql = "update CityList set hotOrder = ? where id = ?";
				cityListDao.batchExecute(updHql, currOrder, Long.valueOf(moveToId));// 更新目标对象的排序号
				cityListDao.batchExecute(updHql, targetOrder, Long.valueOf(currId));// 更新当前对象的排序号
			} else {
				// 1.当前序号大于目标序号
				if (currOrder > targetOrder) {
					// 从目标序号到当前序号前一个序号的对象的序号都加1. ?:目标 ?:当前
					hql = "update CityList set hotOrder = (hotOrder + 1) where provinceCode = ? and hotOrder >= ? and hotOrder < ?";
				} else if (currOrder < targetOrder) {// 2.当前序号小于目标序号
					// 从目标行排序号到当前行排序号下一号都自减1
					// 从目标后一个序号的对象到当前序号的对象的序号都减1. ?:目标 ?:当前
					hql = "update CityList set hotOrder = (hotOrder - 1) where provinceCode = ? and hotOrder <= ? and hotOrder > ?";
				}
				cityListDao.batchExecute(hql, currPro, targetOrder, currOrder);
				// 再将当前行的排序号更改成目标行的排序号
				hql = "update CityList set hotOrder = ? where id = ?";
				cityListDao.batchExecute(hql, targetOrder, Long.valueOf(currId));// 更新目标对象的排序号
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}



	@Override
	public String getCityNameByPlateNum(String plateNum) {
		String city = "";

		String[] plateNums = plateNum.split("");

		String short_proi = plateNums[0];// 获取车牌省份简写汉字
		String short_city = plateNums[1];// 获取车牌所属城市简写字母

		List<CityList> citys = cityListDao.findhqlList("from CityList where plateNumShort like ?", short_proi + "%");
		for (int i = 0; i < citys.size(); i++) {
			if (citys.get(i).getPlateNumShort().contains(short_city)) {
				city = citys.get(i).getCityName();
				break;
			}
		}

		return city;
	}



	@Override
	public Map<String, Object> findCityList(String province) {
		String logtxt = U.log(log, "获取-城市列表");

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(province)) {
					U.log(log, "[省份名称/编号]为空");
				} else {
					province = province.trim();

					U.log(log, "[省份名称/编号] province=" + province);
				}
			}

			String provinceCode = "";// 省份编号
			if (fg && StringUtils.isNotEmpty(province)) {
				ProvinceList pl = provinceListDao.findByField("provinceName", province);
				if (pl == null) {
					U.log(log, "[通过省份名称查询]对象为空");

					pl = provinceListDao.findByField("provinceCode", province);
					if (pl == null) {
						U.log(log, "[通过省份编号查询]对象为空");
					}
				}

				if (pl == null) {
					fg = U.setPutFalse(map, "该[省份名称/编号]对应省份不存在");
				} else {
					provinceCode = pl.getProvinceCode();
				}

				U.log(log, "[省份编号] provinceCode=" + provinceCode);
			}

			if (fg) {
				StringBuilder sb = new StringBuilder("from CityList where 1 = 1 ");
				List<Object> ps = new ArrayList<Object>();

				if (StringUtils.isNotBlank(provinceCode)) {
					sb.append("and provinceCode = ?0 ");
					ps.add(provinceCode);
				}
				sb.append("order by pinyinSimple asc");
				List<CityList> list = cityListDao.findhqlList(sb.toString(), ps.toArray());

				map.put("data", list);

				U.setPut(map, 1, "获取列表成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> getPlateNumShortById(String id) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-车牌前缀");

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "获取-车牌前缀-城市id为空");
				U.setPutFalse(map, 1, "城市id不为空");
				return map;
			}
			
			CityList city = cityListDao.findByField("id", Long.valueOf(id));
			if (city == null) {
				U.logFalse(log, "获取-车牌前缀-查询不到id对应的城市");
				U.setPutFalse(map, 1, "查询不到城市");
				return map;
			}
			String plateNumShort = city.getPlateNumShort();
			List<String> plateList = new ArrayList<>();
			//处理数据库中的特殊数据，eg:京ABCDEFGHJK
			if (plateNumShort.length() > 2) {
				char first = plateNumShort.charAt(0);
				for(int i=1;i<plateNumShort.length();i++){
					char second  = plateNumShort.charAt(i);
					String temp = String.valueOf(first) + String.valueOf(second);
					plateList.add(temp);
				}
			}
			else{
				plateList.add(plateNumShort);
			}
			map.put("data", plateList);
			U.log(log, "获取-车牌前缀-成功");
			U.setPut(map, 0, "success");
			return map;
			
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			return map;
		}

	}
}
