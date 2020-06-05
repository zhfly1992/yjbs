package com.fx.service.impl.back;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.CarBrandDao;
import com.fx.entity.back.CarBrand;
import com.fx.service.back.CarBrandService;

@Service
@Transactional
public class CarBrandServiceImpl extends BaseServiceImpl<CarBrand, Long> implements CarBrandService {

	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	@Autowired
	private CarBrandDao	carBrandDao;



	@Override
	public ZBaseDaoImpl<CarBrand, Long> getDao() {
		return carBrandDao;
	}



	@Override
	public int operCarBrand(String cbId, CarBrand cb) {
		try {
			if (cb != null) {
				if (StringUtils.isNotEmpty(cbId)) {
					carBrandDao.update(cb);
				} else {
					carBrandDao.save(cb);
				}
			} else {
				CarBrand delCb = carBrandDao.find(Long.parseLong(cbId));
				carBrandDao.delete(delCb);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}



	@Override
	public int resetBrandOrder(int cbType) {
		try {
			// 根据车辆类型获取对应品牌列表，并对品牌默认排序
			List<CarBrand> cbList = carBrandDao.findhqlList("from CarBrand where carType = ? order by id asc", cbType);
			// 遍历城市列表进行排序
			for (int j = 0; j < cbList.size(); j++) {
				cbList.get(j).setHotOrder(j + 1);
				carBrandDao.update(cbList.get(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}



	@Override
	public int moveBrandOrder(int currType, int currOrder, int targetOrder, int sta) {
		try {
			// 首先获取当前序号、目标序号所在对象id，并保存起来
			long currId = 0, moveToId = 0;
			String hql = " from CarBrand where carType = ? and hotOrder = ?";
			CarBrand currBrand = carBrandDao.findObj(hql, currType, currOrder);
			currId = currBrand.getId();// 保存当前品牌id
			CarBrand moveToBrand = carBrandDao.findObj(hql, currType, targetOrder);
			moveToId = moveToBrand.getId();// 保存目标城市id
			if (sta == 1) {
				// 执行序号交换
				String updHql = "update CarBrand set hotOrder = ? where id = ?";
				carBrandDao.batchExecute(updHql, currOrder, Long.valueOf(moveToId));// 更新目标对象的排序号
				carBrandDao.batchExecute(updHql, targetOrder, Long.valueOf(currId));// 更新当前对象的排序号
			} else {
				// 1.当前序号大于目标序号
				if (currOrder > targetOrder) {
					// 从目标序号到当前序号前一个序号的对象的序号都加1. ?:目标 ?:当前
					hql = "update CarBrand set hotOrder = (hotOrder + 1) where carType = ? and hotOrder >= ? and hotOrder < ?";
				} else if (currOrder < targetOrder) {// 2.当前序号小于目标序号
					// 从目标行排序号到当前行排序号下一号都自减1
					// 从目标后一个序号的对象到当前序号的对象的序号都减1. ?:目标 ?:当前
					hql = "update CarBrand set hotOrder = (hotOrder - 1) where carType = ? and hotOrder <= ? and hotOrder > ?";
				}
				carBrandDao.batchExecute(hql, currType, targetOrder, currOrder);
				// 再将当前行的排序号更改成目标行的排序号
				hql = "update CarBrand set hotOrder = ? where id = ?";
				carBrandDao.batchExecute(hql, targetOrder, Long.valueOf(currId));// 更新目标对象的排序号
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}



	@Override
	public Map<String, Object> findCarBrands(String carType) {
		String logtxt = U.log(log, "获取-车辆品牌列表");

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			if (fg) {
				if (StringUtils.isEmpty(carType)) {
					fg = U.setPutFalse(map, "车辆类型不能为空");
				}
			}
			if (fg) {
				String hql = "from CarBrand where carType=?0";
				List<CarBrand> list = carBrandDao.findhqlList(hql, Integer.parseInt(carType));
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
	public Map<String, Object> findAllCarBrands() {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "获取-所有车辆品牌列表");

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			String hql = "from CarBrand where id != ?0";
			List<CarBrand> list = carBrandDao.findhqlList(hql, Long.parseLong("0"));
			map.put("data", list);

			U.setPut(map, 1, "获取成功");

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}
}
