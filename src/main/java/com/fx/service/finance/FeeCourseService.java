package com.fx.service.finance;


import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.finance.FeeCourse;



public interface FeeCourseService extends BaseService<FeeCourse, Long> {
	/**
	 * @author xx
	 * @version 20200521
	 * @Description:下拉框获取科目列表
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @return map{code: 结果状态码, msg: 结果状态说明, data:数据列表 }
	 */
	public Map<String, Object> findCourses(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request);
	
	/**
	 * 
	 * @Description:新增科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param feeCourse
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> addFeeCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,FeeCourse feeCourse,String unitNum);
	/**
	 * 
	 * @Description:改变科目状态(可用/不可用)
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id
	 * @return
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
	 * @param feeCourse
	 * @return
	 * @author :zh
	 * @version 2020年5月22日
	 */
	public Map<String, Object> updateCourse(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,FeeCourse feeCourse);
	
	/**
	 * 
	 * @Description:根据id获取科目
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param id
	 * @return
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
	 * @param id
	 * @return
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
	 * @param unitNum
	 * @return
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
	 * @param id
	 * @return
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
	 * @param courseName
	 * @return
	 * @author :zh
	 * @version 2020年6月8日
	 */
	public Map<String, Object> checkCourseName(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,String courseName,String courseId);

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



