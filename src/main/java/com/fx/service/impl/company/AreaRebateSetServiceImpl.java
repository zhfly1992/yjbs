package com.fx.service.impl.company;

import java.util.ArrayList;
import java.util.Date;
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
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.AreaRebateSetDao;
import com.fx.entity.company.AreaRebateSet;
import com.fx.service.company.AreaRebateSetService;

@Service
@Transactional
public class AreaRebateSetServiceImpl extends BaseServiceImpl<AreaRebateSet,Long> implements AreaRebateSetService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private AreaRebateSetDao arsDao;
	@Override
	public ZBaseDaoImpl<AreaRebateSet, Long> getDao() {
		return arsDao;
	}
	
	@Override
	public List<AreaRebateSet> findAreaRebateSetList(String teamNo, String area) {
		return arsDao.findAreaRebateSetList(teamNo, area);
	}
	
	@Override
	public Map<String, Object> addAreaRebateSet(String uid, String teamNo, 
		String rebateName, String area, String rebate, String overkmParam){
		log.info(">>>>>>>>>>>>>>>>添加/修改区域返点比例设置--begin");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		String hql = "";
		
		try {
			if(fg){
				if(StringUtils.isBlank(area)){
					U.setPut(map, 0, "所属区域不能为空");
					fg = false;
				}else{
					area = area.trim();
					if(!area.contains("-")){
						U.setPut(map, 0, "所属区域错误");
						fg = false;
					}
				}
			}
			
			if(fg){
				if(StringUtils.isBlank(rebateName)){
					U.setPut(map, 0, "返点名称不能为空");
					fg = false;
				}else{
					rebateName = rebateName.trim();
				}
			}
			
			double _rebate = 0d;
			if(fg){
				if(StringUtils.isBlank(rebate)){
					U.setPut(map, 0, "返点比例不能为空");
					fg = false;
				}else{
					rebate = rebate.trim();
					if(MathUtils.isDoubel(rebate)){
						_rebate = Double.parseDouble(rebate);
						if(_rebate < 0 || _rebate > 100){
							U.setPut(map, 0, "返点比例取值为0~100之间");
							fg = false;
						}
					}else{
						U.setPut(map, 0, "返点比例格式错误，请输入数字");
						fg = false;
					}
				}
			}
			
			double _overkmParam = 0d;
			if(fg){
				if(StringUtils.isBlank(overkmParam)){
					U.setPut(map, 0, "超公里调节参数不能为空");
					fg = false;
				}else{
					overkmParam = overkmParam.trim();
					if(MathUtils.isDoubel(overkmParam)){
						_overkmParam = Double.parseDouble(overkmParam);
					}else{
						U.setPut(map, 0, "超公里调节参数格式错误，请输入数字");
						fg = false;
					}
				}
			}
			
			AreaRebateSet ars = null;
			if(fg){
				if(StringUtils.isNotBlank(uid)){// 修改
					// 判断除原修改对象以外的数据是否与新修改对象有重复
					hql = "from AreaRebateSet where id <> ? and area = ? and rebateName = ?";
					ars = arsDao.findObj(hql, Long.parseLong(uid), area, rebateName);
				}else{// 添加
					// 判断现有数据是否与新添加对象有重复
					hql = "from AreaRebateSet where area = ? and rebateName = ?";
					ars = arsDao.findObj(hql, area, rebateName);
				}
				
				// 同一区域可以有多个不同返点名称的返点，但是不能有相同返点
				if(ars != null){
					U.setPut(map, 0, "已存在【"+area+"-"+rebateName+"】的返点比例");
					fg = false;
				}else{
					// 执行添加/修改数据
					if(StringUtils.isNotBlank(uid)){// 修改
						ars = arsDao.findByField("id", Long.parseLong(uid));
						
						ars.setRebateName(rebateName);
						ars.setArea(area);
						ars.setRebate(_rebate);
						ars.setOverkmParam(_overkmParam);
						ars.setAtime(new Date());
						arsDao.update(ars);
						
						U.setPut(map, 1, "修改成功");
					}else{// 添加
						ars = new AreaRebateSet();
						
						ars.setTeamNo(teamNo);
						ars.setRebateName(rebateName);
						ars.setArea(area);
						ars.setRebate(_rebate);
						ars.setOverkmParam(_overkmParam);
						ars.setAtime(new Date());
						arsDao.save(ars);
						
						U.setPut(map, 1, "添加成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPut(map, -1, "异常：添加/修改区域返点比例设置");
			log.info(map.get("msg"), e);
			e.printStackTrace();
		}
		
		log.info(">>>>>>>>>>>>>>>>添加/修改区域返点比例设置--end--"+map.get("msg"));
		return map;
	}
	
	@Override
	public Map<String, Object> delAreaRebateSet(String did){
		log.info(">>>>>>>>>>>>>>>>删除区域返点比例设置--begin");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				if(StringUtils.isBlank(did)){
					U.setPut(map, 0, "传入参数错误");
					fg = false;
				}else{
					did = did.trim();
				}
			}
			
			if(fg){
				AreaRebateSet ars = arsDao.findByField("id", Long.parseLong(did));
				if(ars != null){
					arsDao.delete(ars);
					
					U.setPut(map, 1, "删除成功");
				}else{
					U.setPut(map, 0, "该数据已不存在");
					fg = false;
				}
			}
		} catch (Exception e) {
			U.setPut(map, -1, "异常：删除区域返点比例设置");
			log.info(map.get("msg"), e);
			e.printStackTrace();
		}
		
		log.info(">>>>>>>>>>>>>>>>删除区域返点比例设置--end--"+map.get("msg"));
		return map;
	}
	
	@Override
	public Page<AreaRebateSet> getAreaRebateSetList(Page<AreaRebateSet> pd, String teamNo, 
		String find) {
		try {
			List<Compositor> comps = new ArrayList<Compositor>();
			List<Filtration> filts = new ArrayList<Filtration>();
			
			//***************排序设置--begin***************//
			comps.add(new Compositor("area", CompositorType.ASC));
			comps.add(new Compositor("rebate", CompositorType.ASC));
			comps.add(new Compositor("rebateName", CompositorType.ASC));
			//***************排序设置--end****************//
			
			//***************查询条件--begin**************//
			if(StringUtils.isNotBlank(teamNo)){
				filts.add(new Filtration(MatchType.EQ, teamNo, "teamNo"));
			}
			
			if(StringUtils.isNotBlank(find)){
				filts.add(new Filtration(MatchType.LIKE, find, "rebateName", "area"));
			}
			//***************查询条件--end***************//
			
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = arsDao.findPageByOrders(pd);
			
			log.info("成功：获取区域返点设置列表");
		} catch (Exception e) {
			log.info("异常：获取区域返点设置列表", e);
			e.printStackTrace();
		}
		return pd;
	}
	
}
