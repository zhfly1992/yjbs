package com.fx.service.impl.finance;

import java.util.ArrayList;
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

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.entity.finance.FeeCourse;
import com.fx.service.finance.FeeCourseService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class FeeCourseServiceImpl extends BaseServiceImpl<FeeCourse, Long> implements FeeCourseService {
	/** 日志记录 */
	private Logger			log	= LogManager.getLogger(this.getClass());
	@Autowired
	private RedisUtil		redis;
	@Autowired
	private FeeCourseDao	fcDao;



	@Override
	public ZBaseDaoImpl<FeeCourse, Long> getDao() {
		return fcDao;
	}



	@Override
	public Map<String, Object> findCourses(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request) {
		String logtxt = U.log(log, "查询-科目列表-用于下拉框", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String hql = "from FeeCourse where unitNum = ?0";
			List<FeeCourse> courselist = fcDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			if (courselist.size() > 0) {
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(FeeCourse.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				map.put("data", courselist);
			}
			U.setPut(map, 1, "查询成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}



	@Override
	public Map<String, Object> addFeeCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			FeeCourse feeCourse, String unitNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "科目-新增", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (fg) {
				if (StringUtils.isBlank(unitNum)) {
					U.logFalse(log, "新增-科目-失败，unitNum为空");
					fg = U.setPutFalse(map, 0, "获取unitNum失败");
				}
			}
			feeCourse.setUnitNum(unitNum);
			feeCourse.setAddTime(new Date());
			if (fg) {
				// 不是根层级
				if (feeCourse.getLevel() != 1) {
					// 无父层级id
					if (null == feeCourse.getParentId()) {
						U.logFalse(log, "新增-科目-失败，parentId为空");
						fg = U.setPutFalse(map, 0, "parentId不能为空");
					}
				}
			}
			if (fg) {
				// 自动生成科目编码
				if (feeCourse.getLevel() == 1) {
					// 科目为一级科目
					String hql = "from FeeCourse where level = ?0 and unitNum = ?1 order by courseNum desc";
					List<FeeCourse> top1FeeCourse = fcDao.findhqlList(hql, 1,unitNum);
					
					if (top1FeeCourse.size() == 0) {
						//尚未添加其他1级科目，则从1001开始
						feeCourse.setCourseNum("1001");
					}
					else{
						//现有最大科目编号+1
						feeCourse.setCourseNum(String.valueOf(Integer.parseInt(top1FeeCourse.get(0).getCourseNum()) + 1));
					}
				}
				else{
					String hql = "from FeeCourse where level = ?0 and parentId = ?1 and unitNum = ?2 order by courseNum desc";
					List<FeeCourse> temp = fcDao.findhqlList(hql, feeCourse.getLevel(),feeCourse.getParentId(),unitNum);
					if (temp.size() == 0) {
						//父科目下无该等级的科目
						//获取父科目的信息
						FeeCourse parentFee = fcDao.findByField("id",feeCourse.getParentId());
						//在父科目的科目编码后添加01作为新增添科目的编码
						feeCourse.setCourseNum(parentFee.getCourseNum() + "01");
					}
					else{
						//现有最大科目编号+1
						feeCourse.setCourseNum(String.valueOf(Integer.parseInt(temp.get(0).getCourseNum()) + 1));
					}
				}
			}
			if (fg) {
				fcDao.save(feeCourse);
				U.log(log, "新增-科目-成功");
				U.setPut(map, 1, "新增成功");
			}

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> changeCourseStatus(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id) {
		String logtxt = U.log(log, "科目-改变状态", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			FeeCourse feeCourse = null;
			if (fg) {
				if (StringUtils.isBlank(id)) {
					U.logFalse(log, "科目-改变状态-失败，id为空");
					fg = U.setPutFalse(map, 0, "id不能为空");
				}
			}
			if (fg) {
				feeCourse = fcDao.findByField("id", Long.parseLong(id));
				if (feeCourse == null) {
					U.logFalse(log, "科目-改变状态-失败，查找科目失败");
					fg = U.setPutFalse(map, 0, "查找科目失败，确认id是否有误");
				}
			}
			if (fg) {
				if (feeCourse.getCourseStatus().equals("0")) {
					feeCourse.setCourseStatus("1");
				} else {
					feeCourse.setCourseStatus("0");
				}
				fcDao.update(feeCourse);
				U.log(log, "科目-改变状态-成功");
				U.setPut(map, 1, "操作成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> updateCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			FeeCourse feeCourse) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "科目-编辑", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			fcDao.update(feeCourse);
			U.log(log, "科目-编辑-成功");
			U.setPut(map, 1, "编辑成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getCourseById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "查询-单个科目", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "科目-id查询单个科目-失败，id为空");
				U.setPutFalse(map, 0, "id不能为空");
			} else {
				FeeCourse feeCourse = fcDao.findByField("id", Long.parseLong(id));
				map.put("data", feeCourse);
				U.log(log, "科目-id查询单个科目-成功");
				U.setPut(map, 1, "查询成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> deleteCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id) {
		String logtxt = U.log(log, "科目-删除", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {

			List<Long> deleteIds = new ArrayList<Long>();
			FeeCourse feeCourse;
			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "科目-删除-失败，id为空");
				fg = U.setPutFalse(map, 0, "id不能为空");
			}

			if (fg) {
				feeCourse = fcDao.findByField("id", Long.parseLong(id));
				if (null == feeCourse) {
					U.logFalse(log, "科目-删除-失败，查找科目失败");
					fg = U.setPutFalse(map, 0, "查找科目失败，确认id是否有误");
				}
				if (fg) {
					ArrayList<Long> arrayList = new ArrayList<Long>();
					arrayList.add(Long.parseLong(id));
					getDeleteIds(arrayList, deleteIds);
					String hql = "delete from FeeCourse where id in ?0";
					fcDao.batchExecute(hql, deleteIds);
					U.log(log, "科目-删除-成功");
					U.setPut(map, 1, "删除成功");
				}

			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	private void getDeleteIds(List<Long> ids, List<Long> deleteIds) {

		for (Long id : ids) {
			List<FeeCourse> findListByField = fcDao.findListByField("parentId", id);
			if (findListByField.size() == 0) {
				// 查找子科目为空，该科目为最低一层
				deleteIds.add(id);
			} else {
				deleteIds.add(id);
				List<Long> ids2 = new ArrayList<>();
				for (FeeCourse feeCourse : findListByField) {
					ids2.add(feeCourse.getId());
				}
				getDeleteIds(ids2, deleteIds);
			}
		}
	}



	@Override
	public Map<String, Object> findRootCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "查询-科目列表-最上层科目", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.logFalse(log, "查询-科目列表-最上层科目-失败，unitNum为空");
				U.setPutFalse(map, 0, "获取unitNum失败");
			} else {
				String hql = "from FeeCourse where unitNum = ?0 and level = 1";
				List<FeeCourse> courselist = fcDao.findhqlList(hql, unitNum);
				if (courselist.size() > 0) {
					map.put("data", courselist);
				}
			}
			U.log(log, "查询-科目列表-最上层科目-成功");
			U.setPut(map, 1, "查询成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getCourseByParentId(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String parentId) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "查询-科目列表-根据parentId查询", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(parentId)) {
				U.logFalse(log, "查询-科目列表-根据parentId查询，parentId为空");
				U.setPutFalse(map, 0, "parentId为空");
			} else {
				String hql = "from FeeCourse where parentId = ?0";
				List<FeeCourse> courselist = fcDao.findhqlList(hql, Long.parseLong(parentId));
				if (courselist.size() > 0) {
					map.put("data", courselist);
				}
			}
			U.log(log, "查询-科目列表-根据parentId查询-成功");
			U.setPut(map, 1, "查询成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}

}
