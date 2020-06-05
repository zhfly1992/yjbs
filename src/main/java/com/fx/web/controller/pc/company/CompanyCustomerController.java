package com.fx.web.controller.pc.company;



import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSONObject;
import com.fx.commons.annotation.Log;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.U;
import com.fx.service.company.CompanyCustomService;
import com.fx.service.company.CompanyGroupService;
import com.fx.service.company.StaffService;
import com.fx.service.cus.CustomerService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * 单位用户-控制器
 */
@Api(tags="电脑端-单位管理-客户模块")
@Controller
@RequestMapping("/company/cus")
public class CompanyCustomerController extends BaseController {

	/** 客户信息-服务 */
	@Autowired
	private CompanyCustomService	ccSer;
	/** 员工信息-服务 */
	@Autowired
	private StaffService			staSer;
	/** 小组信息-服务 */
	@Autowired
	private CompanyGroupService		cgSer;
	/** 用户信息-服务 */
	@Autowired
	private CustomerService			customerSer;

	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	/**
	 * 单位用户-登录 API（post）/company/cus/subLogin
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="单位用户登录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="lphone", 
			dataType="String",
			value="手机号"
		),
		@ApiImplicitParam(
			required=true, 
			name="lpass", 
			dataType="String",
			value="密码"
		),
		@ApiImplicitParam(
			name="remberMe", 
			dataType="String",
			value="是否记住 eg:true/false"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "subLogin", method = RequestMethod.POST)
	public void subLogin(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String lphone = jsonObject.getString("lphone");
		String lpass = jsonObject.getString("lpass");
		String remberMe = jsonObject.getString("remberMe");
		map = customerSer.subCompanyLogin(ReqSrc.PC_COMPANY, response, request, lphone, lpass,null, remberMe);

		Message.print(response, map);
	}



	/**
	 * 获取-登录用户信息 API（post）/company/cus/getLCompanyUser
	 */
	@Log("获取登录用户信息")
	@RequestMapping(value = "getLCompanyUser", method = RequestMethod.POST)
	public void getLCompanyUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = customerSer.findLCus(ReqSrc.PC_COMPANY, response, request);

		Message.print(response, map);
	}



	/**
	 * 获取-用户-分页列表 API（post）/company/cus/getCusList
	 * 
	 * @param find
	 *            查询关键字
	 */
	/*@RequestMapping(value = "getCusList", method = RequestMethod.POST)
	public void getCusList(HttpServletResponse response, HttpServletRequest request, String page, String rows,
			String find) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = customerSer.findCusList(ReqSrc.PC_COMPANY, page, rows, find);

		Message.print(response, map);
	}*/



	/**
	 * 添加、修改客户 API（post）/company/cus/subCompanyCusAdup
	 * 
	 * @RequestBody 封装对象：前台要用JSON.stringify(paramdata)，而且要带上请求头，后台才会自动封装
	 */
	@RequestMapping(value = "subCompanyCusAdup", method = RequestMethod.POST)
	public void subCompanyCusAdup(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = ccSer.subCompanyCusAdup(ReqSrc.PC_COMPANY, response, request, "8112010001", jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description:删除公司客户
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            包含主键id
	 */
	@RequestMapping(value = "deleteCompanyCus", method = RequestMethod.POST)
	public void deleteCompanyCus(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = ccSer.deleteCompanyCus(ReqSrc.PC_COMPANY, response, request, jsonObject);

		Message.print(response, map);

	}



	/**
	 * 获取客户列表 API（post）/company/cus/companyCusList
	 */
	@RequestMapping(value = "companyCusList", method = RequestMethod.POST)
	public void companyCusList(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String find = jsonObject.getString("find");
		String unitName = jsonObject.getString("unitName");
		String serviceMan =jsonObject.getString("serviceMan");
		String recomMan =jsonObject.getString("recomMan");

	    map = ccSer.findCompanyCusList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), find, unitName,serviceMan,recomMan);
	    

		Message.print(response, map);
	}



	/**
	 * 通过id获取客户（post）/company/cus/companyCusFindById jsonObject 必须包含id
	 */
	@RequestMapping(value = "companyCusFindById", method = RequestMethod.POST)
	public void getCompanyCusById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = ccSer.findCompanyCusById(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description:获取挂靠公司选择为是的客户部分记录，用于下拉框，返回id,unitName,unitSimple（post）/company/cus/getCompanyCusIsDepend
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
	@RequestMapping(value = "getCompanyCusIsDepend", method = RequestMethod.POST)
	public void getCompanyCusIsDepend(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = ccSer.getCompanyCusIsDepend(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 添加、修改小组 API（post）/company/cus/subGroupAdUp
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="添加/修改小组 ", notes="map{code: 结果状态码, msg: 结果状态说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="id", 
			dataType="String",
			value="修改记录id 修改时传入此参数"
		),
		@ApiImplicitParam(
			required=true,
			name="groupName", 
			dataType="String",
			value="小组名称"
		),
		@ApiImplicitParam(
			required=true, 
			name="linkPhone", 
			dataType="String",
			value="小组联系人电话 eg:15888888888"
		),
		@ApiImplicitParam(
			required=true, 
			name="linkName", 
			dataType="String",
			value="小组联系人姓名 eg:xx"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "subGroupAdUp", method = RequestMethod.POST)
	public void subGroupAdUp(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		String groupName = jsonObject.getString("groupName");
		String linkPhone = jsonObject.getString("linkPhone");
		String linkName = jsonObject.getString("linkName");
		map = cgSer.subGroupAdUp(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis),id, groupName, linkPhone, linkName);
		Message.print(response, map);
	}
	
	/**
	 * 通过id获取小组信息（post）/company/cus/findGroupById jsonObject 必须包含id
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="通过id获取小组信息", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="查询id "
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findGroupById", method = RequestMethod.POST)
	public void findGroupById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = cgSer.findGroupById(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	/**
	 * 通过小组名称查询是否存在（post）/company/cus/findGroupByName
	 *  @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="通过小组名称查询是否存在", notes="map{code: 结果状态码, msg: 结果状态说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="groupName", 
			dataType="String",
			value="查询小组名称"
		),
		@ApiImplicitParam(
			name="groupId", 
			dataType="String",
			value="小组id,修改时传入"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findGroupByName", method = RequestMethod.POST)
	public void findGroupByName(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		String groupName = jsonObject.getString("groupName");
		String groupId = jsonObject.getString("groupId");
		Map<String, Object> map = new HashMap<String, Object>();
		map = cgSer.findGroupByName(ReqSrc.PC_COMPANY, response, request, groupName,groupId);
		Message.print(response, map);
	}
	
	/**
	 * 删除小组 API（post）/company/cus/groupDelete
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="通过id删除小组", notes="map{code: 结果状态码, msg: 结果状态说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="删除id "
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "groupDelete", method = RequestMethod.POST)
	public void groupDelete(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = cgSer.groupDelete(ReqSrc.PC_COMPANY, response, request, jsonObject);

		Message.print(response, map);
	}
	
	/**
	 * 获取小组列表-分页 API（post）/company/cus/findCompanyGroupList
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="获取小组列表-分页", notes="返回map集合")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String", 
			value="页码 eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页显示行数 eg：20"
		),
		@ApiImplicitParam(
			name="find", 
			dataType="String",
			value="姓名或电话"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findCompanyGroupList", method = RequestMethod.POST)
	public void findCompanyGroupList(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String find = jsonObject.getString("find");
		map = cgSer.findCompanyGroupList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), find);

		Message.print(response, map);
	}
	/**
	 * @Description:获取小组列表，用于下拉框（post）/company/cus/getGroupList
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="获取小组列表，用于下拉框-不分页，不传参", notes="返回map集合")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getGroupList", method = RequestMethod.POST)
	public void getGroupList(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =cgSer.getGroupList(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}

	/**
	 * 添加员工 API（post）/company/cus/staffAdd
	 */
	@RequestMapping(value = "staffAdd", method = RequestMethod.POST)
	public void subCompanyStaffAdd(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = staSer.subStaffAdd(ReqSrc.PC_COMPANY, response, request, jsonObject);

		Message.print(response, map);
	}



	/**
	 * 修改员工 API（post）/company/cus/staffUpdate
	 */
	@RequestMapping(value = "staffUpdate", method = RequestMethod.POST)
	public void subCompanyStaffUpdate(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = staSer.subStaffUpdate(ReqSrc.PC_COMPANY, response, request, jsonObject);

		Message.print(response, map);
	}



	/**
	 * 删除员工 API（post）/company/cus/staffDelete
	 */
	@RequestMapping(value = "staffDelete", method = RequestMethod.POST)
	public void CompanyStaffDelete(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = staSer.subStaffDelete(ReqSrc.PC_COMPANY, response, request, jsonObject);

		Message.print(response, map);
	}


	/**
	 * 获取员工列表 API（post）/company/cus/findStaffList
	 */
	@RequestMapping(value = "findStaffList", method = RequestMethod.POST)
	public void findStaffList(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String find = jsonObject.getString("find");
		map = staSer.findStaffList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), find);
		Message.print(response, map);
	}



	/**
	 * 通过id获取员工（post）/company/cus/findStaffById jsonObject 必须包含id
	 */
	@RequestMapping(value = "findStaffById", method = RequestMethod.POST)
	public void getStaffById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = staSer.findStaffById(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 * @Description:获取员工信息，用于下拉框（post）/company/cus/getStaffNameList
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
	@RequestMapping(value = "getStaffNameList", method = RequestMethod.POST)
	public void getAllStaff(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = staSer.getAllStaff(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取驾驶员信息，用于下拉框（post）/company/cus/getDriverList
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
	@RequestMapping(value = "getDriverList", method = RequestMethod.POST)
	public void getDriverList(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = staSer.getDriverList(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:新增客户时检验手机号 （post）/company/cus/checkCustomPhone
	 * @param response
	 * @param request
	 * @param jsonObject {"phone":""}
	 * @author :zh
	 * @version 2020年5月13日
	 */
	@RequestMapping(value = "checkCustomPhone", method = RequestMethod.POST)
	public void checkCustomPhone(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String phone = jsonObject.getString("phone");
		map = ccSer.checkPhoneExists(ReqSrc.PC_COMPANY, response, request, phone, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	
	/**
	 * 
	 * @Description:添加业务负责人（post）/company/cus/addPersonInCharge
	 * @param response
	 * @param request
	 * @param jsonObject 
	 * @author :zh
	 * @version 2020年5月13日
	 */
	@RequestMapping(value = "addPersonInCharge", method = RequestMethod.POST)
	public void addPersonInCharge(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		String personInCharge = jsonObject.getJSONArray("personInCharge").toJSONString();
		map = ccSer.addPersonInCharge(ReqSrc.PC_COMPANY, response, request, id, personInCharge);
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取客户基本信息（post）/company/cus/getCustomBaseInfo
	 * @param response
	 * @param request
	 * @param jsonObject 
	 * @author :zh
	 * @version 2020年5月22日
	 */
	@RequestMapping(value = "getCustomBaseInfo", method = RequestMethod.POST)
	public void getCustomBaseInfo(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map = ccSer.getCustomBaseInfo(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取指定类型的客户信息 (post) /company/cus/getCustomInfoByType
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年5月24日
	 */
	@RequestMapping(value = "getCustomInfoByType", method = RequestMethod.POST)
	public void getCustomInfoByType(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String cusType = jsonObject.getString("cusType");
		map = ccSer.getCustomInfoByType(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis), cusType);
		Message.print(response, map);
	}



	/**
	 * 登出-系统 API（post）/company/cus/logout
	 */
	@Log("单位退出系统")
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public void logout(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		getSubject().logout();
		U.setPut(map, 1, "登出系统成功");

		Message.print(response, map);
	}

}
