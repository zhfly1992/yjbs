package com.fx.service.finance;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.FeeCourse;



public interface FeeCourseService extends BaseService<FeeCourse, Long> {
	
	/**
	 *  查询单位科目列表
	 * @author xx
	 * @date 20200707
	 * @param pageData 分页数据
	 * @param unitNum 单位编号
	 * @param find 查询条件（科目名称，科目编码，科目简拼）
	 * @param courseType 收支状态
	 * @param courseStatus 使用状态
	 * @param courseCategory 科目类别
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}
	 */	
	public Map<String, Object> findFeeCourses(ReqSrc reqsrc, String page, String rows,String unitNum, String find,String courseType,
			String courseStatus,String courseCategory ,String sTime, String eTime);
	
	/**
	 * @Description:新增子级科目查询父级科目是否有交易记录或者关联银行
	 * @author xx
	 * @date 20200707
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param parentId 父级科目id
	 * @return map{code: 结果状态码, msg: 结果状态说明, fctIds:关联交易记录id，bankIds：关联银行记录id }
	 */
	public Map<String, Object> findCourseLink(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String parentId);
	
	/**
	 * @author xx
	 * @version 20200521
	 * @Description:下拉框获取科目列表
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param tips all全部 last最后一级
	 * @return map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findCourses(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String tips);
	
	/**
	 * 
	 * @Description:新增科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param feeCourse 科目对象
	 * @param unitNum 单位编号
	 * @param courseCategory 科目类别
	 * @param courseName 科目名称
	 * @param pinyinSimple 科目简拼
	 * @param courseType 收支状态
	 * @param courseStatus 启用状态
	 * @param level 科目层级
	 * @param parentId 父级id
	 * @param fctIds 父级关联的科目交易记录id
	 * @param bankIds 父级科目关联的银行记录id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> addFeeCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum,String courseCategory,String courseName,String pinyinSimple,String courseType,String courseStatus,
			String level,String parentId,String fctIds,String bankIds);
	/**
	 * 
	 * @Description:改变科目状态(可用/不可用)
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 科目id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> changeCourseStatus(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String id);
	
	
	/**
	 * 
	 * @Description:编辑科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param upId 修改记录id
	 * @param courseCategory 科目类别
	 * @param courseName 科目名称
	 * @param pinyinSimple 科目简拼
	 * @param courseType 收支状态
	 * @param courseStatus 启用状态
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> updateCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String upId,
			String courseCategory,String courseName,String pinyinSimple,String courseType,String courseStatus);
	
	/**
	 * 
	 * @Description:根据id获取科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 科目id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 * @author :zh 
	 * @version 2020年5月22日
	 */
	public Map<String, Object> getCourseById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String id);
	
	/**
	 * 
	 * @Description:删除科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 删除科目id
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> deleteCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String id);
	
	
	/**
	 * 
	 * @Description:获取最上层科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param unitNum 单位编号
	 * @return  map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Map<String, Object> findRootCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String unitNum);
	
	/**
	 * 
	 * @Description:根据parentID获取科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id 父级科目id
	 * @return map{code: 结果状态码, msg: 结果状态说明，data:数据列表}
	 * @author :zh
	 * @version 2020年5月23日
	 */
	public Map<String, Object> getCourseByParentId(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String id);
	
	
	/**
	 * 
	 * @Description:检验科目名字是否重复
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param courseName 科目名称
	 * @param courseId 科目id
	 * @param level 科目层级
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 * @author :zh
	 * @version 2020年6月8日
	 */
	public Map<String, Object> checkCourseName(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String courseName,String courseId,String level);

	/**
	 * 获取-指定单位的科目列表
	 * @param reqsrc 	请求来源
	 * @param request 	request
	 * @param response 	response
	 * @param lunitNum 	登录单位编号
	 * @param type 		科目收支类型 0-收入；1-支出
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public Map<String, Object> findFeeCourseList(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
		String lunitNum, String type);
}



