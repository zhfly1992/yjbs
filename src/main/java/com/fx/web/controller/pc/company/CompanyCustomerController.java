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
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.U;
import com.fx.service.company.CompanyCustomService;
import com.fx.service.company.CompanyGroupService;
import com.fx.service.company.CustomTypeService;
import com.fx.service.company.StaffService;
import com.fx.service.cus.CustomerService;
import com.fx.service.cus.permi.RoleService;
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
	/** 角色-服务 */
	@Autowired
	private RoleService  ros;
	
	/** 客户类型-服务 */
	@Autowired
	private CustomTypeService  ctSer;


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
	@ApiOperation(value="获取登录用户信息",notes="返回map{ code: 结果状态码, msg: 结果状态码说明, data: 数据 }")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getLCompanyUser", method = RequestMethod.POST)
	public void getLCompanyUser(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = customerSer.findLCus(ReqSrc.PC_COMPANY, response, request);

		Message.print(response, map);
	}
	
	/*****************************客户类型管理*****************start***********/
	/**
	 * 获取-客户类型列表-分页列表 API（post）/company/cus/findCustomType
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="获取-客户类型列表-分页",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			name="typeName", 
			dataType="String",
			value="类型名称"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="添加开始时间 eg:2020-04-11 08:44:56"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="添加结束时间 eg:2020-04-11 08:44:56"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findCustomType", method=RequestMethod.POST)
	public void findCustomType(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String typeName = jsonObject.getString("typeName");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = ctSer.findCustomType(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), typeName, sTime, eTime);
		Message.print(response, map);
	}
	
	/**
	 * @Description:客户类型列表，用于下拉框（post）/company/cus/findCusTypes
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="客户类型列表，用于下拉框-不分页", notes="返回map{code: 结果状态码, msg: 结果状态码说明, cusTypes:金额类型列表}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findCusTypes", method = RequestMethod.POST)
	public void findMtypes(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =ctSer.findCusTypes(ReqSrc.PC_COMPANY, response, request);
		Message.print(response, map);
	}
	
	/**
	 * 添加/修改客户类型 API（post）/company/cus/adupCusType
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="添加/修改客户类型",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="updId", 
			dataType="String",
			value="修改记录id 修改时传入此参数"
		),
		@ApiImplicitParam(
			required=true, 
			name="typeName", 
			dataType="String", 
			value="类型名称 eg：驾驶员工资"
		),
		@ApiImplicitParam(
			required=true, 
			name="isSupplier", 
			dataType="String", 
			value="供应商 0否 1是 eg：0"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="adupCusType", method=RequestMethod.POST)
	public void adupMtype(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String updId = jsonObject.getString("updId");
		String typeName = jsonObject.getString("typeName");
		String isSupplier = jsonObject.getString("isSupplier");
		map = ctSer.adupCusType(ReqSrc.PC_COMPANY, response, request, updId, typeName, isSupplier);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位删除客户类型 API（post）/company/cus/delCusType
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="单位删除客户类型",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="delId", 
			dataType="String",
			value="删除记录id eg:1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="delCusType", method=RequestMethod.POST)
	public void delMtype(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map = ctSer.delCusType(ReqSrc.PC_COMPANY, response, request, delId);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位通过id查询客户类型信息 API（post）/company/cus/mtypeFindById
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="单位通过id查询客户类型信息",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:客户类型数据}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="查询记录id eg:1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="cusTypeFindById", method=RequestMethod.POST)
	public void mtypeFindById(HttpServletResponse response, HttpServletRequest request,  @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map = ctSer.cusTypeFindById(ReqSrc.PC_COMPANY, response, request, id);;
		
		Message.print(response, map);
	}
	/*****************************客户类型管理*****************end***********/

	/**
	 * 添加、修改客户 API（post）/company/cus/subCompanyCusAdup
	 * 
	 * @RequestBody 封装对象：前台要用JSON.stringify(paramdata)，而且要带上请求头，后台才会自动封装
	 */
	@ApiOperation(value="添加/修改客户",notes="返回map{code: 结果状态码, msg: 结果状态码说明}，接口说明为添加参数，修改参数参考查询出的客户信息的字段")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="unitNum", 
			dataType="String",
			value="公司编码  eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="unitName", 
			dataType="String", 
			value="客户公司名称 eg：客户公司名称"
		),
		@ApiImplicitParam(
			required=true, 
			name="cusType", 
			dataType="String", 
			value="客户类型  枚举"
		),
		@ApiImplicitParam(
			required=true, 
			name="isDepend", 
			dataType="int", 
			value="是否挂靠"
		),
		@ApiImplicitParam(
			required=true, 
			name="cusRole", 
			dataType="String", 
			value="职务"
		),
		@ApiImplicitParam(
			required=true, 
			name="serviceMan", 
			dataType="String", 
			value="业务员姓名"
		),
		@ApiImplicitParam(
			required=true, 
			name="recomMan", 
			dataType="String", 
			value="推荐人姓名"
		),
		@ApiImplicitParam(
			required=true, 
			name="baseUserId", 
			dataType="String", 
			value="{'phone':'13555555555','realName':'asd'"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "subCompanyCusAdup", method = RequestMethod.POST)
	public void subCompanyCusAdup(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = ccSer.subCompanyCusAdup(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis), jsonObject);
		Message.print(response, map);
	}



	/**
	 * 
	 *   删除单位的客户 API（post）/company/cus/deleteCompanyCus
	 */
	@ApiOperation(value="删除单位的客户",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="删除记录id"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="成功"),
		@ApiResponse(code=0, message="失败原因"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="获取客户列表",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:数据列表, count:总数}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="int",
			value="页码 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="int", 
			value="每页行数"
		),
		@ApiImplicitParam(
			required=false, 
			name="find", 
			dataType="String", 
			value="关键字"
		),
		@ApiImplicitParam(
			required=true, 
			name="unitNum", 
			dataType="String", 
			value="公司编码"
		),
		@ApiImplicitParam(
			required=false, 
			name="unitName", 
			dataType="String", 
			value="查询的公司名称"
		),
		@ApiImplicitParam(
			required=false, 
			name="serviceMan", 
			dataType="String", 
			value="业务员姓名"
		),
		@ApiImplicitParam(
			required=false, 
			name="recomMan", 
			dataType="String", 
			value="推荐人姓名"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="通过id获取客户",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 数据}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="客户id"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="获取挂靠公司选择为是的客户部分记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 数据列表}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="unitNum", 
			dataType="String",
			value="公司编码"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
			value="修改记录id 修改时传入此参数 eg:1"
		),
		@ApiImplicitParam(
			required=true,
			name="groupName", 
			dataType="String",
			value="小组名称 eg:一队"
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
			value="小组联系人姓名 eg:张三"
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
	@ApiOperation(value="通过id获取小组信息", notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="查询id eg:1"
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
			value="查询小组名称 eg:一队"
		),
		@ApiImplicitParam(
			name="groupId", 
			dataType="String",
			value="小组id,修改时传入 eg:1"
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
			value="删除id eg:1"
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
	@ApiOperation(value="获取小组列表-分页", notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			value="姓名或电话 eg:张三"
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
	@ApiOperation(value="获取小组列表，用于下拉框-不分页，不传参", notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表}")
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
	@ApiOperation(value="添加员工",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="unitNum", 
			dataType="String",
			value="公司编码  eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="entryTime", 
			dataType="String", 
			value="入职时间 eg：客户公司名称"
		),
		@ApiImplicitParam(
			required=true, 
			name="staffState", 
			dataType="String", 
			value="员工状态  枚举"
		),
		@ApiImplicitParam(
			required=true, 
			name="expireTime", 
			dataType="String", 
			value="试用截止/合同到期时间"
		),
		@ApiImplicitParam(
			required=false,
			name="entryCompany", 
			dataType="String", 
			value=" 入职公司"
		),
		@ApiImplicitParam(
			required=false, 
			name="socialUnit", 
			dataType="String", 
			value="社保单位"
		),
		@ApiImplicitParam(
			required=true, 
			name="idCard", 
			dataType="String", 
			value="身份证号"
		),
		@ApiImplicitParam(
			required=true, 
			name="birthdayTime", 
			dataType="String", 
			value="生日"
		),
		@ApiImplicitParam(
			required=false, 
			name="takeDriveTime", 
			dataType="String", 
			value="驾驶证领证时间"
			),
		@ApiImplicitParam(
			required=false, 
			name="isDriver", 
			dataType="String", 
			value="是否驾驶员，0不是，1是，默认0"
			),
		@ApiImplicitParam(
			required=false, 
			name="certificateNum", 
			dataType="String", 
			value="资格证号"
			),
		@ApiImplicitParam(
			required=false, 
			name="certificateType", 
			dataType="String", 
			value="资格证证件类型"
			),
		@ApiImplicitParam(
			required=false, 
			name="takeCertificateTime", 
			dataType="String", 
			value="资格证领证时间"
			),
		@ApiImplicitParam(
			required=false, 
			name="driveType", 
			dataType="String", 
			value="准驾车型"
			),
		@ApiImplicitParam(
			required=false, 
			name="idCardFrontImg", 
			dataType="String", 
			value="身份证正面url"
			),
		@ApiImplicitParam(
			required=false, 
			name="idCardBackImg", 
			dataType="String", 
			value="身份证反面url"
			),
		@ApiImplicitParam(
			required=false, 
			name="driveImg", 
			dataType="String", 
			value="驾驶证url"
			),
		@ApiImplicitParam(
			required=false, 
			name="certificateImg", 
			dataType="String", 
			value="资格证url"
			),
		@ApiImplicitParam(
			required=true, 
			name="serviceMan", 
			dataType="String", 
			value="业务员，用于添加客户，默认当前账号"
			),
		@ApiImplicitParam(
			required=true, 
			name="recomMan", 
			dataType="String", 
			value="推荐人，用于添加客户，默认当前账号"
			),
		@ApiImplicitParam(
			required=true, 
			name="sex", 
			dataType="String", 
			value="性别，MALE男，FEMALE女"
			),
		@ApiImplicitParam(
			required=true, 
			name="age", 
			dataType="int", 
			value="年龄"
			),
		@ApiImplicitParam(
			required=true, 
			name="education", 
			dataType="String", 
			value="学历，枚举"
			),
		@ApiImplicitParam(
			required=false, 
			name="address", 
			dataType="String", 
			value="地址"
			),
		@ApiImplicitParam(
			required=false, 
			name="simpleAddress", 
			dataType="String", 
			value="地址简称"
			),
		@ApiImplicitParam(
			required=false, 
			name="latitude", 
			dataType="String", 
			value="地址纬度"
			),
		@ApiImplicitParam(
			required=false, 
			name="longitude", 
			dataType="String", 
			value="地址经度"
			),
		@ApiImplicitParam(
			required=true, 
			name="deptId", 
			dataType="String", 
			value="部门信息，json格式   eg:{'id':'1','name':'后勤中心'}"
			),
		@ApiImplicitParam(
			required=true, 
			name="roleId", 
			dataType="String", 
			value="职务信息，json格式   eg:{'id':'1','name':'机务长'"
			),
		@ApiImplicitParam(
			required=true, 
			name="groupId", 
			dataType="String", 
			value="小组信息，json格式 ，包含小组信息 "
			),
		@ApiImplicitParam(
			required=true, 
			name="baseUserId", 
			dataType="String", 
			value="用户基本信息，json格式 ，eg:{'phone':'1243333','realName':'啦啦啦'}"
			),
		
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "staffAdd", method = RequestMethod.POST)
	public void subCompanyStaffAdd(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		map = staSer.subStaffAdd(ReqSrc.PC_COMPANY, response, request, jsonObject,LU.getLCompany(request, redis));

		Message.print(response, map);
	}



	/**
	 * 修改员工 API（post）/company/cus/staffUpdate
	 */
	@ApiOperation(value="修改员工信息", notes="map{code: 结果状态码, msg: 结果状态说明}，传参参考获取员工信息接口返回数据")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="删除员工 API", notes="map{code: 结果状态码, msg: 结果状态说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="int",
			value="删除id eg:1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="获取员工列表", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:数据列表, count:总数}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="int",
			value="页码 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="int",
			value="每页数量 eg:10"
		),
		@ApiImplicitParam(
			required=true, 
			name="find", 
			dataType="String",
			value="关键字搜索"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="通过id获取员工信息", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:员工信息}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="int",
			value="员工id eg:1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="下拉框-查询公司员工基础信息，不分页", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:员工信息}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="unitNum", 
			dataType="String",
			value="公司编码 eg:8112010001"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getStaffNameList", method = RequestMethod.POST)
	public void getAllStaff(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = staSer.getAllStaff(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	/**
	 * 
	 * @Description:获取驾驶员信息，用于下拉框, 排除已绑定车辆的驾驶员（post）/company/cus/getDriverList
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年4月22日
	 */
	@ApiOperation(value="获取驾驶员信息，用于下拉框, 排除已绑定车辆的驾驶员，无参数", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:驾驶员信息}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="新增客户时检验手机号", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="phone", 
			dataType="String",
			value="手机号 eg:18141312312"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	 * @Description:新增员工检验手机号 （post）/company/cus/checkStaffPhone
	 * @param response
	 * @param request
	 * @param jsonObject {"phone":""}
	 * @author :zh
	 * @version 2020年6月18日
	 */
	@ApiOperation(value="新增员工检验手机号", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="phone", 
			dataType="String",
			value="手机号 eg:18141312312"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "checkStaffPhone", method = RequestMethod.POST)
	public void checkPhoneBeforeAddStaff(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String phone = jsonObject.getString("phone");
		map = staSer.checkPhoneBeforeAdd(ReqSrc.PC_COMPANY, response, request, phone);
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
	@ApiOperation(value="添加业务负责人", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="客户id eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="personInCharge", 
			dataType="String",
			value="业务负责人 ,数组形式 eg:[{'companyName':'aaaa','name':'111','phone':'13555555'}]"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="获取客户基本信息", notes="无参数，返回map{code: 结果状态码, msg: 结果状态码说明, data:客户信息}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
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
	@ApiOperation(value="获取指定类型的客户信息", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 客户信息}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="cusType", 
			dataType="String",
			value="客户类型  eg:SCHOOL"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getCustomInfoByType", method = RequestMethod.POST)
	public void getCustomInfoByType(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String cusType = jsonObject.getString("cusType");
		map = ccSer.getCustomInfoByType(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis), cusType);
		Message.print(response, map);
	}
	
	
	
	/**
	 * 
	 * @Description:根据部门id获取角色 (post) /company/cus/getRoleByDeptId
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @author :zh
	 * @version 2020年5月24日
	 */
	@ApiOperation(value="根据部门id获取角色", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data: 角色信息}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="deptId", 
			dataType="String",
			value="部门id  eg:1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getRoleByDeptId", method = RequestMethod.POST)
	public void getRoleByDeptId(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String deptId = jsonObject.getString("deptId");
		map = ros.getRoleByDeptId(ReqSrc.PC_COMPANY, response, request, deptId);
		Message.print(response, map);
	}

	
	/**
 	 * 员工离职（post）/company/cus/staffLeave
 	 * @author zh
 	 * @date 20200627
 	 */
 	@ApiOperation(value="后台操作员工离职", notes="后台操作员工离职")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="员工id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="leaveInfo", 
			dataType="String", 
			value="离职信息 eg：'2020-06-27,备注'"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
 	@RequestMapping(value="staffLeave",method=RequestMethod.POST)
	public void staffLeave(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
 		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		String leaveInfo =jsonObject.getString("leaveInfo");
		map = staSer.staffLeave(ReqSrc.PC_COMPANY, response, request, id,leaveInfo);
		Message.print(response, map);
	}

	/**
	 * 登出-系统 API（post）/company/cus/logout
	 */
	@ApiOperation(value="单位退出系统", notes="map{code: 结果状态码, msg: 结果状态说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String", 
			value="员工id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="leaveInfo", 
			dataType="String", 
			value="离职信息 eg：'2020-06-27,备注'"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public void logout(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		getSubject().logout();
		U.setPut(map, 1, "登出系统成功");

		Message.print(response, map);
	}

}
