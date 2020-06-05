package com.fx.service.impl.company;

import java.util.Date;
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

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.CompanyGroupDao;
import com.fx.dao.company.StaffDao;
import com.fx.entity.company.CompanyGroup;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.Customer;
import com.fx.service.company.CompanyGroupService;

@Service
@Transactional
public class CompanyGroupServiceImpl extends BaseServiceImpl<CompanyGroup, Long> implements CompanyGroupService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 小组-数据源 */
	@Autowired
	private CompanyGroupDao cgDao;
	
	/** 员工-数据源 */
	@Autowired
	private StaffDao sfDao;
	@Override
	public ZBaseDaoImpl<CompanyGroup, Long> getDao() {
		return cgDao;
	}
	
	@Override
	public Map<String, Object> findCompanyGroupList(ReqSrc reqsrc, String page, String rows,String unitNum, String find) {
		String logtxt = U.log(log, "获取-小组-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "用户");
			/*****参数--验证--end******/
			
			if(fg) {
				if(StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				}else {
					find = find.trim();
					
					U.log(log, "[查询关键字] find="+find);
				}
			}
			
			if(fg){
				Page<CompanyGroup> pd = cgDao.findGroupList(reqsrc, page, rows,unitNum, find);
				U.setPageData(map, pd);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[]{});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}

	@Override
	public Map<String, Object> subGroupAdUp(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum, String id,String groupName, String linkPhone, String linkName) {
		String logtxt = U.log(log, "添加-单位小组", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			CompanyGroup cg=null;
			if(StringUtils.isNotBlank(id)) {
				cg=cgDao.findByField("id",Long.valueOf(id));
				if(cg==null) {
					fg = U.setPutFalse(map, "[修改数据]不存在");
				}
			}else {
				cg=new CompanyGroup();
				cg.setUnitNum(unitNum);
				cg.setAddTime(new Date());
			}
			if(fg) {
				if(StringUtils.isEmpty(unitNum)) {
					fg = U.setPutFalse(map, "[单位编号]不能为空");
				}
			}
			if(fg) {
				if(StringUtils.isEmpty(groupName)) {
					fg = U.setPutFalse(map, "[小组名称]不能为空");
				}
			}
			if(fg) {
				if(StringUtils.isEmpty(linkPhone)) {
					fg = U.setPutFalse(map, "[队长联系电话]不能为空");
				}
			}
			if(fg) {
				if(StringUtils.isEmpty(linkName)) {
					fg = U.setPutFalse(map, "[队长姓名]不能为空");
				}
			}
			
			if(fg){
				cg.setGroupName(groupName);
				cg.setLinkPhone(linkPhone);
				cg.setLinkName(linkName);
				if(StringUtils.isNotBlank(id)) {
					cgDao.update(cg);
					U.setPut(map, 1, "编辑成功");
				}else {
					cgDao.save(cg);
					U.setPut(map, 1, "添加成功");
				}
				
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}

	@Override
	public Map<String, Object> groupDelete(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "删除-单位小组", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			if (!jsonObject.containsKey("id")) {
				U.logFalse(log, "删除-单位小组-失败-参数不包含id");
				U.setPutFalse(map, "id参数不能为空");
				return map;
			}
			long id=jsonObject.getLong("id");
			CompanyGroup cg=cgDao.findByField("id", id);
			if(cg==null) {
				U.logFalse(log, "删除-单位小组-小组不存在");
				U.setPutFalse(map, 0, "删除失败-小组不存在");
				return map;
			}
			String hql="from Staff where unitNum=?0 and groupId.id=?1";
			List<Staff> staffList=sfDao.findhqlList(hql, cg.getUnitNum(),cg.getId());
			if(staffList.size()>0) {
				U.logFalse(log, "删除-单位小组-小组已有驾驶员不能删除");
				U.setPutFalse(map, 0, "删除失败-小组已有驾驶员不能删除");
				return map;
			}else {
				cgDao.delete(cg);
				U.log(log, "删除-单位小组-成功");
				U.setPut(map, 1, "删除成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> findGroupById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-小组-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("id")) {
				U.setPutFalse(map,  "id参数不为空");
				return map;
			}
			CompanyGroup group = cgDao.findByField("id", jsonObject.getLong("id"));
			if (group != null) {
				U.log(log, "通过id查找小组成功");
				map.put("data", group);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找小组失败");
				U.setPutFalse(map, 0, "查找失败");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}

	@Override
	public Map<String, Object> getGroupList(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum) {
		String logtxt = U.log(log, "查询-小组下拉框", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<CompanyGroup> cgList = cgDao.findListByField("unitNum", unitNum);
			map.put("data", cgList);
			
			// 字段过滤
			Map<String, Object> fmap = new HashMap<String, Object>();
			fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
			map.put(QC.FIT_FIELDS, fmap);

			U.setPut(map, 1, "请求数据成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> findGroupByName(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String groupName,String groupId) {
		String logtxt = U.log(log, "查询-小组-by 小组名称", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(groupName)) {
				U.setPutFalse(map,  "小组名称不能为空");
				return map;
			}
			if(StringUtils.isNotBlank(groupId)) {
				CompanyGroup group = cgDao.findByField("groupName", groupName);
				if (group != null && group.getId()!=Long.valueOf(groupId)) {
					U.setPut(map, 1, "小组名称已存在");
				} else {
					U.setPutFalse(map, 0, "小组名称不存在");
				}
			}else {
				CompanyGroup group = cgDao.findByField("groupName", groupName);
				if (group != null) {
					U.setPut(map, 1, "小组名称已存在");
				} else {
					U.setPutFalse(map, 0, "小组名称不存在");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
}
