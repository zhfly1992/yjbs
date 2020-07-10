package com.fx.service.impl.finance;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.enums.CourseCategory;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.BankListDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.FeeCourseTradeDao;
import com.fx.entity.finance.BankList;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.FeeCourseTrade;
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
	
	/** 科目交易记录-服务 */
	@Autowired
	private FeeCourseTradeDao fctDao;
	
	/** 银行-服务 */
	@Autowired
	private BankListDao blDao;


	@Override
	public ZBaseDaoImpl<FeeCourse, Long> getDao() {
		return fcDao;
	}
	
	@Override
	public Map<String, Object> findFeeCourses(ReqSrc reqsrc, String page, String rows, String unitNum, String find,String courseType,
			String courseStatus,String courseCategory ,String sTime, String eTime) {
		String logtxt = U.log(log, "获取-单位科目-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "单位科目列表");
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
				Page<FeeCourse> pd = fcDao.findFeeCourseList(reqsrc, page, rows, unitNum, find, courseType, courseStatus, courseCategory, sTime, eTime);
				U.setPageData(map, pd);
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findCourseLink(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String parentId) {
		String logtxt = U.log(log, "查询-父级科目-是否有关联关系", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			FeeCourse fc=null;
			if (fg) {
				if (StringUtils.isBlank(parentId)) {
					fg = U.setPutFalse(map, 0, "父级科目id不能为空");
				}else {
					fc=fcDao.findByField("id", Long.valueOf(parentId));
					if(fc==null) {
						fg = U.setPutFalse(map, 0, "父级科目不存在");
					}
				}
			}
			if(fg) {
				List<FeeCourseTrade> fctlist=fctDao.findListByField("feeCourseId.id", fc.getId());
				if(fctlist.size()>0) {//父级科目已有交易记录
					List<Long> fctIds=new ArrayList<Long>();
					for (FeeCourseTrade fct : fctlist) {
						fctIds.add(fct.getId());
					}
					map.put("fctIds", fctIds);
				}else{
					map.put("fctIds", "");
				}
				List<BankList> banks=blDao.findListByField("feeCourseId.id", fc.getId());
				if(banks.size()>0) {//父级科目已关联银行
					List<Long> bankIds=new ArrayList<Long>();
					for (BankList bl : banks) {
						bankIds.add(bl.getId());
					}
					map.put("bankIds", bankIds);
				}else {
					map.put("bankIds", "");
				}
				U.setPut(map, 1, "查询成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}

	@Override
	public Map<String, Object> findCourses(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String tips) {
		String logtxt = U.log(log, "查询-科目列表-用于下拉框", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String hql = "from FeeCourse where unitNum = ?0 order by courseNum asc";//获取全部科目并按照科目编号分组排序
			if("last".equalsIgnoreCase(tips)) hql="from FeeCourse where unitNum = ?0 and isLastCourse=1 order by courseNum asc";//获取最后一级科目并按照科目编号排序
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
			String unitNum,String courseCategory,String courseName,String pinyinSimple,String courseType,String courseStatus,
			String level,String parentId,String fctIds,String bankIds) {
		// TODO Auto-generated method stub
		String logtxt = U.log(log, "科目-新增", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (fg) {
				if (StringUtils.isBlank(unitNum)) {
					fg = U.setPutFalse(map, 0, "获取unitNum失败");
				}
			}
			int _level=1;
			if (fg) {
				if (StringUtils.isBlank(level)) {
					fg = U.setPutFalse(map, 0, "科目层级不能为空");
				}else {
					_level=Integer.parseInt(level.trim());
				}
			}
			FeeCourse parentFee=null;//父级科目
			String courseNum="";//科目编号
			if (fg) {
				// 不是根层级
				if (_level != 1) {
					// 无父层级id
					if (StringUtils.isBlank(parentId)) {
						fg = U.setPutFalse(map, 0, "添加子级科目父级科目id不能为空");
					}else {
						//获取父科目的信息
						parentFee = fcDao.findByField("id",Long.valueOf(parentId));
						if(parentFee==null) {
							fg = U.setPutFalse(map, 0, "父级科目不存在");
						}else {
							String hql = "from FeeCourse where level = ?0 and parentCourseId.id = ?1 and unitNum = ?2 order by courseNum desc";
							List<FeeCourse> temp = fcDao.findhqlList(hql, _level,Long.valueOf(parentId),unitNum);
							if (temp.size() == 0) {//父科目下无该等级的科目
								//在父科目的科目编码后添加01作为新增添科目的编码
								courseNum=parentFee.getCourseNum() + "01";
							}
							else{//现有最大科目编号+1
								courseNum=String.valueOf(Integer.parseInt(temp.get(0).getCourseNum()) + 1);
							}
						}
					}
				}else {
					// 科目为一级科目
					String hql = "from FeeCourse where level = ?0 and unitNum = ?1 order by courseNum desc";
					List<FeeCourse> top1FeeCourse = fcDao.findhqlList(hql, 1,unitNum);
					
					if (top1FeeCourse.size() == 0) {
						//尚未添加其他1级科目，则从1001开始
						courseNum="1001";
					}else{
						//现有最大科目编号+1
						courseNum=String.valueOf(Integer.parseInt(top1FeeCourse.get(0).getCourseNum()) + 1);
					}
				}
			}
			if (fg) {
				FeeCourse feeCourse=new FeeCourse();
				feeCourse.setUnitNum(unitNum);
				feeCourse.setCourseNum(courseNum);
				feeCourse.setCourseCategory(CourseCategory.valueOf(courseCategory));
				feeCourse.setCourseName(courseName);
				feeCourse.setPinyinSimple(pinyinSimple);
				feeCourse.setCourseType(Integer.parseInt(courseType));
				feeCourse.setCourseStatus(Integer.parseInt(courseStatus));
				feeCourse.setLevel(_level);
				if(parentFee!=null)feeCourse.setParentCourseId(parentFee);
				feeCourse.setIsLastCourse(1);
				feeCourse.setAddTime(new Date());
				fcDao.save(feeCourse);
				if(parentFee!=null) {
					parentFee.setIsLastCourse(0);//父级科目设为非最后一级科目
					fcDao.update(parentFee);
					if(StringUtils.isNotBlank(fctIds) || StringUtils.isNotBlank(bankIds)) {//父级科目已有交易记录或已关联银行：需要自动生成同名的下级科目并关联这些记录
						FeeCourse sameParent=new FeeCourse();
						sameParent.setUnitNum(unitNum);
						sameParent.setCourseNum(String.valueOf(Integer.parseInt(courseNum)+1));
						sameParent.setCourseCategory(parentFee.getCourseCategory());
						sameParent.setCourseName(parentFee.getCourseName());
						sameParent.setPinyinSimple(parentFee.getPinyinSimple());
						sameParent.setCourseType(Integer.parseInt(courseType));
						sameParent.setCourseStatus(Integer.parseInt(courseStatus));
						sameParent.setLevel(parentFee.getLevel()+1);
						sameParent.setParentCourseId(parentFee);
						sameParent.setBalance(parentFee.getBalance());
						sameParent.setIsLastCourse(1);
						sameParent.setAddTime(new Date());
						fcDao.save(sameParent);
						if(StringUtils.isNotBlank(fctIds)) {
							List<FeeCourseTrade> fctlist=fctDao.findListByField("feeCourseId.id", parentFee.getId());
							if(fctlist.size()>0) {//更新父级科目关联的交易记录
								for (FeeCourseTrade fct : fctlist) {
									fct.setFeeCourseId(sameParent);
									fctDao.update(fct);
								}
							}
						}
						if(StringUtils.isNotBlank(bankIds)) {
							List<BankList> banks=blDao.findListByField("feeCourseId.id", parentFee.getId());
							if(banks.size()>0) {//更新父级科目关联的银行
								for (BankList bl : banks) {
									bl.setFeeCourseId(sameParent);
									blDao.update(bl);
								}
							}
						}
					}
				}
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
				if (feeCourse.getCourseStatus()==0) {
					feeCourse.setCourseStatus(1);
				} else {
					feeCourse.setCourseStatus(0);
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
	public Map<String, Object> updateCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String upId,
			String courseCategory,String courseName,String pinyinSimple,String courseType,String courseStatus) {
		String logtxt = U.log(log, "单位科目-修改", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			FeeCourse fc = null;
			if (StringUtils.isBlank(upId)) {
				fg=U.setPutFalse(map, 0, "修改记录id不能为空");
			} else {
				fc = fcDao.findByField("id", Long.parseLong(upId));
				if(fc==null) {
					fg=U.setPutFalse(map, 0, "修改记录不存在");
				}
			}
			if(fg) {
				fc.setCourseCategory(CourseCategory.valueOf(courseCategory));
				fc.setCourseName(courseName);
				fc.setPinyinSimple(pinyinSimple);
				fc.setCourseType(Integer.parseInt(courseType));
				fc.setCourseStatus(Integer.parseInt(courseStatus));
				fcDao.update(fc);
				U.setPut(map, 1, "修改成功");
			}
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
			FeeCourse feeCourse=null;
			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "科目-删除-失败，id为空");
				fg = U.setPutFalse(map, 0, "id不能为空");
			}

			if (fg) {
				feeCourse = fcDao.findByField("id", Long.parseLong(id));
				if (null == feeCourse) {
					U.logFalse(log, "科目-删除-失败，查找科目失败");
					fg = U.setPutFalse(map, 0, "查找科目失败，确认id是否有误");
				}else if(feeCourse.getIsLastCourse()==0){
					fg = U.setPutFalse(map, 0, "该科目非末级科目，不能删除");
				}
			}
			if(fg) {
				//判断被删除的科目是否被银行使用
				String hql = "from BankList where feeCourseId.id =?0";
				List <BankList> bkList=blDao.findhqlList(hql, feeCourse.getId());
				if (!bkList.isEmpty()) {
					Set<String> useCourseNames =  new HashSet<>();
					for(BankList bl:bkList){
						useCourseNames.add(bl.getFeeCourseId().getCourseName());
					}
					U.logFalse(log, "科目-删除-失败，科目被银行引用");
					U.setPutFalse(map, 0, "删除科目失败，该科目【" + useCourseNames.toString() +"】已被银行使用");
				}
			}
			if(fg) {
				//判断被删除的科目是否已有交易记录
				String hql = "from FeeCourseTrade where feeCourseId.id=?0";
				List <FeeCourseTrade> fctList=fctDao.findhqlList(hql, feeCourse.getId());
				if (!fctList.isEmpty()) {
					Set<String> useCourseNames =  new HashSet<>();
					for(FeeCourseTrade fct:fctList){
						useCourseNames.add(fct.getFeeCourseId().getCourseName());
					}
					U.logFalse(log, "科目-删除-失败，科目被交易记录引用");
					U.setPutFalse(map, 0, "删除科目失败，科目" + useCourseNames.toString() +"已被科目交易记录使用");
				}
			}
			if (fg) {
				/*List<Long> deleteIds = new ArrayList<Long>();
				ArrayList<Long> arrayList = new ArrayList<Long>();
				arrayList.add(Long.parseLong(id));
				getDeleteIds(arrayList, deleteIds);
				String hql = "delete from FeeCourse where id in ?0";
				fcDao.batchExecute(hql, deleteIds);*/
				
				List<FeeCourse> childs=fcDao.findListByField("parentCourseId.id", feeCourse.getParentCourseId().getId());
				if(childs.size()==1) {//只有一个子集就把父级设为最后一级
					feeCourse.getParentCourseId().setIsLastCourse(1);
					fcDao.update(feeCourse);
				}
				fcDao.delete(feeCourse);
				
				U.log(log, "科目-删除-成功");
				U.setPut(map, 1, "删除成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@SuppressWarnings("unused")
	private void getDeleteIds(List<Long> ids, List<Long> deleteIds) {

		for (Long id : ids) {
			List<FeeCourse> findListByField = fcDao.findListByField("parentCourseId.id", id);
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
				String hql = "from FeeCourse where parentCourseId.id = ?0";
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



	@Override
	public Map<String, Object> checkCourseName(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String courseName,String courseId,String level) {
		String logtxt = U.log(log, "科目-判断科目名称是否可用", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(courseName)) {
				U.logFalse(log, "科目-判断科目名称是否可用，courseName为空");
				U.setPutFalse(map, 0, "科目名称为空");
			} else {
				String hql="from FeeCourse where courseName=?0 and level=?1";
				FeeCourse findByField = fcDao.findObj(hql, courseName,Integer.parseInt(level));
				if (null == findByField) {
					U.log(log, "科目-判断科目名称是否可用-可用");
					U.setPut(map, 1, "名称可用");
				}else{
					if (courseId != null) {//courseId不为null说明是修改
							if (findByField.getId() == Long.parseLong(courseId)) {
								U.log(log, "科目-判断科目名称是否可用-可用");
								U.setPut(map, 1, "名称可用");
							}else{
								U.logFalse(log, "科目-判断科目名称是否-不可用");
								U.setPutFalse(map, 0, "科目名称重复，不可用");
							}
					}else {
						U.logFalse(log, "科目-判断科目名称是否-不可用");
						U.setPutFalse(map, 0, "科目名称重复，不可用");
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}
	
	@Override
	public Map<String, Object> findFeeCourseList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String lunitNum, String type) {
		String logtxt = U.log(log, "获取-指定单位的科目列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			int _type = -1;
			if(fg) {
				if(StringUtils.isBlank(type)) {
					fg = U.setPutFalse(map, "[科目收支类型]不能为空");
				}else {
					type = type.trim();
					if(!FV.isInteger(type)) {
						fg = U.setPutFalse(map, "[科目收支类型]格式错误");
					}else {
						_type = Integer.parseInt(type);
					}
					
					U.log(log, "[科目收支类型] type="+type);
				}
			}
			
			if(fg) {
				hql = "select new FeeCourse(id,courseName) from FeeCourse where unitNum = ?0 and courseStatus = ?1 and courseType = ?2 order by id asc";
				List<FeeCourse> fcs = fcDao.findhqlList(hql, lunitNum, "0", _type);
				
				List<Item> its = new ArrayList<Item>();
				for (FeeCourse fc : fcs) {
					its.add(new Item(fc.getId()+"", fc.getCourseName()));
				}
				
				map.put("data", its);
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}

}
