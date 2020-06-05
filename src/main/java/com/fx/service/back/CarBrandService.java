package com.fx.service.back;

import java.util.Map;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.CarBrand;

public interface CarBrandService extends BaseService<CarBrand,Long> {

	/**
	 * 添加/修改车辆品牌
	 * @author xx
	 * @date 20200507
	 * @param cbId 修改/删除对象id
	 * @param cb 添加/修改/删除对象
	 * @return 1成功，-1异常
	 */
	public int operCarBrand(String cbId, CarBrand cb);
	/**
	 * 重置所有品牌排序
	 * @author xx
	 * @param cbType 车辆类型
	 * @date 20200507
	 */
	public int resetBrandOrder(int cbType);
	/**
	 * 热门品牌排序
	 * @author xx
	 * @date 20200507
	 * @param currType 品牌所属类型
	 * @param menuId 操作类型标识:topMove（向上移动）、downMove（向下移动）、moveTo（移动到？行）
	 * @param currOrder 当前行序号
	 */
	public int moveBrandOrder(int currType, int currOrder, int targetOrder, int sta);
	/**
	 * 获取-车辆品牌列表
	 * @author xx
	 * @date 20200507
	 * @param carType 0:大巴车 1:中巴车 2:商务车 3:越野车 4:轿车 5:其他
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findCarBrands(String carType);
	/**
	 * 
	 * @Description:获取-所有车辆品牌列表
	 * @returnmap{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> findAllCarBrands();

}
