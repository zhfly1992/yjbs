package com.fx.service.back;

import java.util.Date;

import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.back.FeastDayList;

public interface FeastDayListService extends BaseService<FeastDayList,Long> {

	/**
	 * 获取节假日分页列表数据
	 * @author xx
	 * @date 20200426
	 * @param pageData 分页数据条件设置
	 * @param find 查询关键字
	 * @return 分页手的列表数据
	 */
	public Page<FeastDayList> getFeastDayList(Page<FeastDayList> pageData, String find);

	/**
	 * 添加/修改节假日数据
	 * @author xx
	 * @date 20200426
	 * @param feastDay 添加/修改节假日对象
	 * @param fName 节假日名称/备注
	 * @param fTime 节假日时间，eg:2017-05-08
	 * @param fNote 节假备注
	 * @return 1成功，-1错误
	 */
	public int feastDayAdup(FeastDayList feastDay, String fName, Date fTime, String fNote);
	
}
