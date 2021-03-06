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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.utils.clazz.Message;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.LU;
import com.fx.service.finance.BankListService;
import com.fx.service.finance.BankTradeListService;
import com.fx.service.finance.CarOilListService;
import com.fx.service.finance.CarRepairListService;
import com.fx.service.finance.EtcListService;
import com.fx.service.finance.FeeCourseService;
import com.fx.service.finance.FeeCourseTradeFirstService;
import com.fx.service.finance.FeeCourseTradeService;
import com.fx.service.finance.MoneyTypeService;
import com.fx.service.finance.ReimburseListService;
import com.fx.service.finance.StaffReimburseService;
import com.fx.service.order.CarOrderService;
import com.fx.service.order.MainCarOrderService;
import com.fx.web.controller.BaseController;
import com.fx.web.util.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="电脑端-单位管理-财务模块")
@Controller
@RequestMapping("/company/finance")
public class CompanyFinanceController extends BaseController {
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	/** 单位银行-服务 */
	@Autowired
	private BankListService bankSer;
	/** 单位银行日记账-服务 */
	@Autowired
	private BankTradeListService btlSer;
	/** 单位凭证-服务 */
	@Autowired
	private ReimburseListService reimSer;
	/** 科目-服务 */
	@Autowired
	private FeeCourseService fcSer;
	/** 科目交易记录-服务 */
	@Autowired
	private FeeCourseTradeService fctSer;
	/** 科目	期初设置-服务 */
	@Autowired
	private FeeCourseTradeFirstService fctfSer;
	/** 车辆加油记录-服务*/
	@Autowired
	private CarOilListService coSer;
	/** ETC记录-服务*/
	@Autowired
	private EtcListService etcSer;
	/** 车辆维修记录-服务*/
	@Autowired
	private CarRepairListService crSer;
	/** 子订单-服务*/
	@Autowired
	private CarOrderService carOrderService;
	/** 主订单-服务*/
	@Autowired
	private MainCarOrderService mainCarOrderService;
	
	/** 员工报账-服务*/
	@Autowired
	private StaffReimburseService srService;
	
	/** 金额类型-服务*/
	@Autowired
	private MoneyTypeService mtSer;
	
	
	/*****************************etc管理*****************start***********/
	/**
	 * 分页查询etc记录  API（post）/company/finance/findEtcList
	 * @author xx
	 * @date 20200525
	 */
	@ApiOperation(value="获取-单位ETC列表-分页",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			name="orderNum", 
			dataType="String",
			value="子订单号 eg:O120200616095455380"
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
		@ApiImplicitParam(
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川A88888"
		),
		@ApiImplicitParam(
				name="driverName", 
				dataType="String",
				value="驾驶员姓名或手机号 eg:张三"
			),
		@ApiImplicitParam(
				name="cardNo", 
				dataType="String",
				value="卡号 eg:6222400016207061"
			),
		@ApiImplicitParam(
				name="operMark", 
				dataType="String",
				value="操作编号 eg:CZ1590394669120"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findEtcList", method = RequestMethod.POST)
	public void findEtcList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String orderNum = jsonObject.getString("orderNum");
		String sTime = jsonObject.getString("sTime");
		String eTime =jsonObject.getString("eTime");
		String plateNum = jsonObject.getString("plateNum");
		String driverName = jsonObject.getString("driverName");
		String cardNo = jsonObject.getString("cardNo");
		String operMark = jsonObject.getString("operMark");
		Map<String, Object> map = new HashMap<String, Object>();
		map = etcSer.findEtcList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), orderNum, sTime, eTime, plateNum, driverName, cardNo, operMark);
		Message.print(response, map);
	}
	
	/**
	 * 单位ETC导入 API（post）/company/finance/importFeeEtc
	 * @author xx
	 * @date 20200525
	 */
	@ApiOperation(value="ETC导入 ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="etcFile",
			dataType="String",
			value="上传文件 eg：前端file的name为该参数名"
		),
		@ApiImplicitParam(
			required=true, 
			name="cardNo", 
			dataType="String",
			value="导入卡号 eg:6222400016207061"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="importFeeEtc", method=RequestMethod.POST) 
	 public void importFeeEtc(@RequestParam("etcFile") MultipartFile file,@RequestParam("cardNo")String cardNo,
			 HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
    	map=etcSer.importFeeEtc(ReqSrc.PC_COMPANY,request,file,cardNo);
    	Message.print(response, map);
	}
	 
	/**
	 * 单位删除ETC记录 API（post）/company/finance/delEtc
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="单位删除Etc记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
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
	@RequestMapping(value="delEtc", method=RequestMethod.POST)
	public void delEtc(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map = etcSer.delEtc(ReqSrc.PC_COMPANY, response, request, delId);
		
		Message.print(response, map);
	}
	/*****************************etc管理*****************end***********/
	
	
	/*****************************金额类型管理*****************start***********/
	/**
	 * 获取-金额类型列表-分页列表 API（post）/company/finance/findMoneyType
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="获取-金额类型列表-分页",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
	@RequestMapping(value="findMoneyType", method=RequestMethod.POST)
	public void findMoneyType(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String typeName = jsonObject.getString("typeName");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = mtSer.findMoneyType(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), typeName, sTime, eTime);
		Message.print(response, map);
	}
	
	/**
	 * @Description:银行账添加时获取金额类型列表，用于下拉框（post）/company/finance/findMtypes
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="银行账添加时获取金额类型列表，用于下拉框-不分页", notes="返回map{code: 结果状态码, msg: 结果状态码说明, mTypes:金额类型列表}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findMtypes", method = RequestMethod.POST)
	public void findMtypes(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =mtSer.findMtypes(ReqSrc.PC_COMPANY, response, request);
		Message.print(response, map);
	}
	
	/**
	 * 单位添加/修改金额类型 API（post）/company/finance/adupMtype
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="单位添加/修改金额类型",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
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
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="adupMtype", method=RequestMethod.POST)
	public void adupMtype(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String updId = jsonObject.getString("updId");
		String typeName = jsonObject.getString("typeName");
		map = mtSer.adupMtype(ReqSrc.PC_COMPANY, response, request, updId, typeName);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位删除金额类型 API（post）/company/finance/delMtype
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="单位删除金额类型",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
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
	@RequestMapping(value="delMtype", method=RequestMethod.POST)
	public void delMtype(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map = mtSer.delMtype(ReqSrc.PC_COMPANY, response, request, delId);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位通过id查询金额类型信息 API（post）/company/finance/mtypeFindById
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="单位通过id查询金额类型信息",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:金额类型数据}")
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
	@RequestMapping(value="mtypeFindById", method=RequestMethod.POST)
	public void mtypeFindById(HttpServletResponse response, HttpServletRequest request,  @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map = mtSer.mtypeFindById(ReqSrc.PC_COMPANY, response, request, id);
		
		Message.print(response, map);
	}
	/*****************************金额类型管理*****************end***********/
	
	/*****************************银行管理*****************start***********/
	/**
	 * 获取-单位银行列表-分页列表 API（post）/company/finance/findBankList
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="获取-单位银行列表-分页",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			value="银行名称/账户姓名/卡号"
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
		@ApiImplicitParam(
			name="isOpen", 
			dataType="String",
			value="是否启用 eg：0未启用 1已启用"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findBankList", method=RequestMethod.POST)
	public void findBankList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String find = jsonObject.getString("find");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		String isOpen = jsonObject.getString("isOpen");
		map = bankSer.findBankList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), find, sTime, eTime, isOpen);
		
		Message.print(response, map);
	}
	/**
	 * @Description:银行账添加时获取银行列表，用于下拉框（post）/company/finance/findBanks
	 * @author xx
	 * @date 20200603
	 */
	@ApiOperation(value="银行账添加时获取银行列表，用于下拉框-不分页", notes="返回map{code: 结果状态码, msg: 结果状态码说明, banks:银行列表}")
	@ApiImplicitParam(
			name="isOpen", 
			dataType="String",
			value="是否启用 ，可为空 eg：0未启用 1已启用"
		)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findBanks", method = RequestMethod.POST)
	public void findBanks(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		String isOpen = jsonObject.getString("isOpen");
		Map<String, Object> map = new HashMap<String, Object>();
		map =bankSer.findBanks(ReqSrc.PC_COMPANY, response, request,isOpen);
		Message.print(response, map);
	}
	
	/**
	 * @Description:根据id获取银行是否能修改（post）/company/finance/isAllowModify
	 * @author xx
	 * @date 20200603
	 */
	@ApiOperation(value="银行账添加时获取银行列表，用于下拉框-不分页", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParam(
			name="id", 
			dataType="String",
			value="银行id eg:1"
		)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "isAllowModify", method = RequestMethod.POST)
	public void isAllowModify(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		String id = jsonObject.getString("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map =bankSer.isAllowModify(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	
	/**
	 * 单位添加/修改银行 API（post）/company/finance/adupBank
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="单位添加/修改银行",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="updId", 
			dataType="String",
			value="修改记录id 修改时传入此参数"
		),
		@ApiImplicitParam(
			required=true, 
			name="bankName", 
			dataType="String", 
			value="账户名称 eg：飞翔一般户"
		),
		@ApiImplicitParam(
			required=true, 
			name="cardNo", 
			dataType="String",
			value="卡号/微信/支付宝手机号 eg:15699999999 "
		),
		@ApiImplicitParam(
			required=true, 
			name="cardName", 
			dataType="String",
			value="开户行 eg：成都农商银行西区支行"
		),
		@ApiImplicitParam(
				name="courseId", 
				dataType="String",
				value="关联科目id eg：2"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="adupBank", method=RequestMethod.POST)
	public void adupBank(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String updId = jsonObject.getString("updId");
		String bankName = jsonObject.getString("bankName");
		String cardNo = jsonObject.getString("cardNo");
		String cardName = jsonObject.getString("cardName");
		String courseId = jsonObject.getString("courseId");
		map = bankSer.adupBank(ReqSrc.PC_COMPANY, response, request, updId, bankName, cardNo, cardName,courseId);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位删除银行 API（post）/company/finance/delBank
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="单位删除银行",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
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
	@RequestMapping(value="delBank", method=RequestMethod.POST)
	public void delBank(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map = bankSer.delBank(ReqSrc.PC_COMPANY, response, request, delId);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位通过id查询银行 API（post）/company/finance/bankFindById
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="单位通过id查询银行",notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:银行数据}")
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
	@RequestMapping(value="bankFindById", method=RequestMethod.POST)
	public void bankFindById(HttpServletResponse response, HttpServletRequest request,  @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map = bankSer.bankFindById(ReqSrc.PC_COMPANY, response, request, id);
		
		Message.print(response, map);
	}
	
	/**
	 * 单位启用银行账本 API（post）/company/finance/openBankAccount
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="单位启用银行账本",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="openAccount", 
			dataType="String",
			value="启用银行 可以单个启用 eg：银行id,银行id"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="openBankAccount", method=RequestMethod.POST)
	public void openBankAccount(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openAccount = jsonObject.getString("openAccount");
		map = bankSer.openAccount(ReqSrc.PC_COMPANY, request, openAccount);
		
		Message.print(response, map);
	}
	/*****************************银行管理*****************end***********/
	
	/*****************************银行帐管理*****************start***********/
	
	/**
	 * 获取-银行日记账列表-分页 API（post）/company/finance/findBankTradeList
	 * @author xx
	 * @date 20200515
	 */
	@ApiOperation(value="获取-银行日记账列表-分页",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数,totalGath:累计收入 totalPay:累计支出 balance:余额}")
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
			name="timeType", 
			dataType="String",
			value="时间查询类型 eg:0 添加时间 1 交易时间"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="开始时间  eg:2020-04-11 08:44:56"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="结束时间  eg:2020-04-11 08:44:56"
		),
		@ApiImplicitParam(
			name="bankNo", 
			dataType="String",
			value="银行账号,多个逗号拼接 eg:8111001013600208432"
		),
		@ApiImplicitParam(
			name="transName", 
			dataType="String",
			value="对方户名 eg:成都市武侯区社会保险事业管理局"
		),
		@ApiImplicitParam(
				name="remark", 
				dataType="String",
				value="摘要 eg:利息收入 5.11-5.15号"
			),
		@ApiImplicitParam(
				name="status", 
				dataType="String",
				value="收支情况 eg:0收入 1支出"
			),
		@ApiImplicitParam(
				name="isCheck", 
				dataType="String",
				value="报销状态  eg:-1已报销完成  0未操作  1待审核"
			),
		@ApiImplicitParam(
				name="findMoney", 
				dataType="String",
				value="交易金额 eg:400.5"
			),
		@ApiImplicitParam(
				name="voucherNum", 
				dataType="String",
				value="凭证号 eg:U1586856470308V202006210001"
			),
		@ApiImplicitParam(
				name="operMark", 
				dataType="String",
				value="操作编号 eg:OM1589263660827"
			),
		@ApiImplicitParam(
				name="moneyType", 
				dataType="String",
				value="金额类型名称,多个逗号拼接 eg:驾驶员工资,返款"
			),
		@ApiImplicitParam(
				name="cusName", 
				dataType="String",
				value="客户名称 eg:单位名称/姓名,电话"
			),
		@ApiImplicitParam(
				name="openRole", 
				dataType="String",
				value="开放查询角色id eg:1"
			),
		@ApiImplicitParam(
				name="serviceName", 
				dataType="String",
				value="业务员uname,多个逗号拼接 eg:1,2"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findBankTradeList", method=RequestMethod.POST)
	public void findBankTradeList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String bankNo = jsonObject.getString("bankNo");
		String transName = jsonObject.getString("transName");
		String remark = jsonObject.getString("remark");
		String timeType = jsonObject.getString("timeType");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		String status = jsonObject.getString("status");
		String isCheck = jsonObject.getString("isCheck");
		String findMoney = jsonObject.getString("findMoney");
		String openRole = jsonObject.getString("openRole");
		String voucherNum = jsonObject.getString("voucherNum");
		String operMark = jsonObject.getString("operMark");
		String openSel = jsonObject.getString("openSel");
		String moneyType = jsonObject.getString("moneyType");
		String cusName = jsonObject.getString("cusName");
		String serviceName = jsonObject.getString("serviceName");
		map = btlSer.findBankTradeList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), bankNo, transName, remark, timeType, 
				sTime, eTime, status, isCheck, findMoney, openRole, voucherNum, operMark, openSel, moneyType, cusName, serviceName);
		
		Message.print(response, map);
	}
	/**
	 * @Description:银行账添加时获取对方户名列表和摘要列表，用于下拉框（post）/company/finance/findTransNamesAndRemarks
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="银行账添加时获取对方户名列表和摘要列表，用于下拉框-不分页，不传参", 
			notes="返回map{code: 结果状态码, msg: 结果状态码说明, transNames:对方户名列表,格式：对方户名/@对方账号, remarks:摘要列表}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findTransNamesAndRemarks", method = RequestMethod.POST)
	public void findTransNamesAndRemarks(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =btlSer.findTransNamesAndRemarks(ReqSrc.PC_COMPANY, response, request);
		Message.print(response, map);
	}
	
	/**
	 * 根据银行信息获取余额 API（post）/company/finance/findBalanceByBankInfo
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="银行账添加时通过银行信息获取余额", notes="返回map{code: 结果状态码, msg: 结果状态码说明, balance:余额}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="bankNo", 
			dataType="String",
			value="银行卡号 eg:18080083599"
		),
		@ApiImplicitParam(
			required=true, 
			name="bankName", 
			dataType="String",
			value="银行名称  eg:微信"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="findBalanceByBankInfo", method = RequestMethod.POST) 
	 public void findBalanceByBankInfo(HttpServletRequest request,HttpServletResponse response,
			 @RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = btlSer.findBalanceByBankInfo(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	 /**
	 * 单位添加银行账 API（post）/company/finance/addBtl
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="单位添加银行账",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true,
			name="myBank", 
			dataType="String",
			value="添加银行信息 eg:账户名称/卡号"
		),
		@ApiImplicitParam(
			required=true, 
			name="transName", 
			dataType="String", 
			value="对方户名 eg：四川省中国旅行社"
		),
		@ApiImplicitParam(
			name="transNum", 
			dataType="String",
			value="对方账号 eg:275***@qq.com "
		),
		@ApiImplicitParam(
			required=true, 
			name="tradeTime", 
			dataType="String",
			value="交易时间  eg:2020-04-11 08:44:56"
		),
		@ApiImplicitParam(
			required=true, 
			name="tradeStatus", 
			dataType="String",
			value="交易状态 eg:0收入 1支出"
		),
		@ApiImplicitParam(
			required=true, 
			name="tradeMoney", 
			dataType="String",
			value="交易金额 eg：400"
		),
		@ApiImplicitParam(
			required=true, 
			name="balance", 
			dataType="String",
			value="交易后余额 eg：800"
		),
		@ApiImplicitParam(
			name="remark", 
			dataType="String",
			value="摘要 eg：盛和塾学习后空吧"
		),
		@ApiImplicitParam(
			name="moneyType", 
			dataType="String",
			value="金额类型id eg：1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="addBtl", method = RequestMethod.POST) 
	 public void addBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String myBank = jsonObject.getString("myBank");
		String transName = jsonObject.getString("transName");
		String transNum = jsonObject.getString("transNum");
		String tradeTime = jsonObject.getString("tradeTime");
		String tradeStatus = jsonObject.getString("tradeStatus");
		String tradeMoney = jsonObject.getString("tradeMoney");
		String balance = jsonObject.getString("balance");
		String remark = jsonObject.getString("remark");
		String moneyType = jsonObject.getString("moneyType");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.addBtl(request, ReqSrc.PC_COMPANY, myBank, transName, transNum, tradeTime, 
    			tradeStatus, tradeMoney, balance, remark,moneyType);
    	Message.print(response, map);
	}
	
	/**
	 * 通过id获取银行账信息（post）/company/cus/findGroupById jsonObject 必须包含id
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="通过id获取银行账信息", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:查询数据结果}")
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
	@RequestMapping(value = "findBtlById", method = RequestMethod.POST)
	public void findBtlById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = btlSer.findBtlById(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	/**
	 * 银行账修改 API（post）/company/finance/modifyBtl
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="单位修改银行账，可修改的信息为参数的信息，其他不能修改",notes="返回map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="updId", 
			dataType="String",
			value="修改记录id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="moneyType", 
			dataType="String",
			value="金额类型id eg：1"
		),
		@ApiImplicitParam(
			name="remark", 
			dataType="String",
			value="摘要 eg：盛和塾学习后空吧"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="modifyBtl", method = RequestMethod.POST)
	public void modifyBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String updId = jsonObject.getString("updId");
		String moneyType = jsonObject.getString("moneyType");
		String remark = jsonObject.getString("remark");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.modifyBtl(request,ReqSrc.PC_COMPANY, updId, moneyType, remark);
    	Message.print(response, map);
	}
	/**
	 * 删除银行账提交 API（post）/company/finance/delBtl
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="删除银行账",notes="返回map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="delId", 
			dataType="String",
			value="删除记录id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="myBankNum", 
			dataType="String",
			value="银行账号 eg：18080083599"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="delBtl", method = RequestMethod.POST) 
	 public void delBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String delId = jsonObject.getString("delId");
		String myBankNum = jsonObject.getString("myBankNum");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.delBtl(request, ReqSrc.PC_COMPANY, delId,myBankNum);
    	Message.print(response, map);
	}
	
	/**
	 * 银行账撤销（暂不启用） API（post）/company/finance/cancelBtl
	 * @author xx
	 * @date 20200517
	 * @param cancelId 撤销id
	 */
	@ApiOperation(value="银行账撤销（暂不启用）",notes="返回map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="cancelId", 
			dataType="String",
			value="撤销记录id eg：1"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="cancelBtl", method = RequestMethod.POST)
	public void cancelBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String cancelId = jsonObject.getString("cancelId");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.cancelBtl(ReqSrc.PC_COMPANY, request, response, cancelId);
    	Message.print(response, map);
	}
	
	/**
	 * 开放银行账查询（暂不启用）   API（post）/company/finance/openSelBtl
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="开放银行账查询 ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="openBtlId", 
			dataType="String",
			value="开放记录id eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="openRole", 
			dataType="String",
			value="开放角色 eg：19/@业务总监,20/@销售部负责人,21/@客户经理,31/@报账员"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="openSelBtl", method = RequestMethod.POST)
	public void openSelBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String openBtlId = jsonObject.getString("openBtlId");
		String openRole = jsonObject.getString("openRole");
		Map<String, Object> map = new HashMap<String, Object>();
		map=btlSer.openSelBtl(request, ReqSrc.PC_COMPANY, openBtlId,openRole);
    	Message.print(response, map);
	}
	
	/**
	 * 银行账互转 API（post）/company/finance/transEachOther
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="银行账互转",notes="返回map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="transId", 
			dataType="String",
			value="互转记录id eg：1,2"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "transEachOther", method = RequestMethod.POST)
	public void transEachOther(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String transId = jsonObject.getString("transId");
		Map<String, Object> map = new HashMap<String, Object>();
		map=btlSer.transBtl(request, ReqSrc.PC_COMPANY, transId);
		Message.print(response, map);
	}
	
	/**
	 * 银行账锁定/解锁（暂不启用） API（post）/company/finance/lockBankTrade
	 * @author xx
	 * @date 20200517
	 */
	@ApiOperation(value="银行账锁定/解锁 ",notes="返回map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="lockId", 
			dataType="String",
			value="锁定记录id,多个逗号拼接 eg：1"
		),
		@ApiImplicitParam(
			required=true, 
			name="isLock", 
			dataType="String",
			value="操作状态 eg:0 解锁 1锁定"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="lockBankTrade", method = RequestMethod.POST) 
	 public void lockBankTrade(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String lockId = jsonObject.getString("lockId");
		String isLock = jsonObject.getString("isLock");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.lockBankTrade(request, ReqSrc.PC_COMPANY, lockId,isLock);
    	Message.print(response, map);
	}
	
	/**
	 * 银行账关联财务记账报销记录
	 * @author xx
	 * @date 20200523
	 */
	@ApiOperation(value="银行账关联财务记账凭证（凭证列表接口已核销）记录 ",
			notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="reimId", 
			dataType="String",
			value="凭证记录id eg：1,2,3"
		),
		@ApiImplicitParam(
			required=true, 
			name="btlId", 
			dataType="String",
			value="银行账记录id eg:1,2,3"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="linkReim", method=RequestMethod.POST) 
	 public void linkReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String reimId = jsonObject.getString("reimId");
		String btlId = jsonObject.getString("btlId");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.linkReim(request, ReqSrc.PC_COMPANY, reimId, btlId);
    	Message.print(response, map);
	}
	/**
	 * 银行账下账 API（post）/company/finance/downBtlMoney
	 * @author xx
	 * @date 20200523
	 */
	@ApiOperation(value="银行账下账 ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
				required=true, 
				name="btlId", 
				dataType="String",
				value="下账记录id"
			),
		@ApiImplicitParam(
			required=true, 
			name="money", 
			dataType="String",
			value="下账金额，获取原记录金额，不能修改"
		),
		@ApiImplicitParam(
				required=true, 
				name="companyCusId", 
				dataType="String",
				value="客户id"
			),
		@ApiImplicitParam(
				name="notice_uname", 
				dataType="String",
				value="通知人uname,读取员工中财务部人员，目前就读取员工列表，可为空"
			),
		@ApiImplicitParam(
				name="notice_note", 
				dataType="String",
				value="通知人备注，可为空"
			),
		@ApiImplicitParam(
				name="orderNum", 
				dataType="String",
				value="下账订单号，选择客户后弹出该客户未付款的订单，选择后逗号拼接该订单号，可为空"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "downBtlMoney", method = RequestMethod.POST)
	public void downBtlMoney(HttpServletResponse response,HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		String btlId = jsonObject.getString("btlId");
		String money = jsonObject.getString("money");
		String companyCusId = jsonObject.getString("companyCusId");
		String notice_uname = jsonObject.getString("notice_uname");
		String notice_note = jsonObject.getString("notice_note");
		String orderNum = jsonObject.getString("orderNum");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.downBtlMoney(ReqSrc.PC_COMPANY, response, request, btlId, money, companyCusId, notice_uname, notice_note, orderNum);
    	Message.print(response, map);
	}
	/**
	 *  银行账审核下账：通过 API（post）/company/finance/checkYesBtl
	 * @author xx
	 * @date 20200617
	 */
	@ApiOperation(value="银行账审核下账：通过  ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
				required=true, 
				name="createInfo", 
				dataType="String",
				value="参数格式：银行账记录id=科目id@银行账记录id=科目id"
			),
			@ApiImplicitParam(
					required=true,
				name="faceCourseInfo", 
				dataType="String",
				value="参数格式：对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="checkYesBtl", method=RequestMethod.POST) 
	 public void checkYesBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String createInfo = jsonObject.getString("createInfo");
		String faceCourseInfo = jsonObject.getString("faceCourseInfo");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.checkYesBtl(ReqSrc.PC_COMPANY, response, request, createInfo, faceCourseInfo);
    	Message.print(response, map);
	}
	
	/**
	 * 银行账生成凭证 API（post）/company/finance/createReimBtl
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="银行账生成凭证",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="createInfo", 
			dataType="String",
			value="参数格式：报销人uname=车牌号=银行账记录id=科目id@报销人uname=车牌号=银行账记录id=科目id"
		),
		@ApiImplicitParam(
				required=true,
			name="faceCourseInfo", 
			dataType="String",
			value="参数格式：报销人uname=车牌号=对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@报销人uname=车牌号=对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额"
		),
		@ApiImplicitParam(
				required=true, 
				name="gainTime", 
				dataType="String",
				value="记账时间"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "createReimBtl", method = RequestMethod.POST)
	public void createReimBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String createInfo = jsonObject.getString("createInfo");
		String faceCourseInfo = jsonObject.getString("faceCourseInfo");
		String gainTime = jsonObject.getString("gainTime");
		Map<String, Object> map = new HashMap<String, Object>();
		map=btlSer.createReim(ReqSrc.PC_COMPANY, response, request, createInfo, faceCourseInfo,gainTime);
		Message.print(response, map);
	}
	
	/**
	 *  银行账审核下账：不通过 API（post）/company/finance/checkNoBtl
	 * @author xx
	 * @date 20200615
	 */
	@ApiOperation(value="银行账审核下账：不通过 ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
				required=true, 
				name="btlId", 
				dataType="String",
				value="记录id"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="checkNoBtl", method=RequestMethod.POST) 
	 public void checkNoBtl(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject){
		String btlId = jsonObject.getString("btlId");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.checkNoBtl(ReqSrc.PC_COMPANY, response, request, btlId);
    	Message.print(response, map);
	}
	/**
	 *   银行账导入 API（post）/company/finance/importfeeBtl
	 * @author xx
	 * @date 20200520
	 */
	@ApiOperation(value="银行账导入 ",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="btlFile",
			dataType="String",
			value="上传文件 eg：前端file的name为该参数名"
		),
		@ApiImplicitParam(
			required=true, 
			name="tradeBank", 
			dataType="String",
			value="导入银行 eg:格式 账户名称/卡号"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	 @RequestMapping(value="importfeeBtl", method=RequestMethod.POST) 
	 public void importfeeBtl(@RequestParam("btlFile")MultipartFile file,@RequestParam("tradeBank")String tradeBank,
			 HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
    	map=btlSer.importfeeBtl(file, request, ReqSrc.PC_COMPANY,tradeBank);
    	Message.print(response, map);
	}
	/**
	 *   导出银行日记账excel  API（post）/company/finance/expBtlBankEx
	 * @author xx
	 * @date 20200520
	 */
	@ApiOperation(value="导出-银行日记账列表",notes="map{code: 结果状态码, msg: 结果状态说明 }")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="下载行数 eg：应为查询出记录的总条数"
		),
		@ApiImplicitParam(
			name="timeType", 
			dataType="String",
			value="时间查询类型 eg:0 添加时间 1 交易时间"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="开始时间"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="结束时间"
		),
		@ApiImplicitParam(
			name="bankNo", 
			dataType="String",
			value="账号，多条逗号拼接 eg:8111001013600208432"
		),
		@ApiImplicitParam(
			name="transName", 
			dataType="String",
			value="对方户名 eg:成都市武侯区社会保险事业管理局"
		),
		@ApiImplicitParam(
				name="remark", 
				dataType="String",
				value="摘要 eg:利息收入 5.11-5.15号"
			),
		@ApiImplicitParam(
				name="status", 
				dataType="String",
				value="收支情况 eg:0收入 1支出"
			),
		@ApiImplicitParam(
				name="isCheck", 
				dataType="String",
				value="报销状态  eg:-2已锁定 -1已报销完成  0未操作  1待审核 2已审核"
			),
		@ApiImplicitParam(
				name="findMoney", 
				dataType="String",
				value="交易金额 eg:400.5"
			),
		@ApiImplicitParam(
			name="openSel", 
			dataType="String",
			value="是否开放查询 eg:0未开放  1已开放"
		),
		@ApiImplicitParam(
			name="openRole", 
			dataType="String",
			value="开放查询角色 eg:19/@业务总监,20/@销售部负责人,21/@客户经理,31/@报账员"
		),
		@ApiImplicitParam(
				name="voucherNum", 
				dataType="String",
				value="凭证号 eg:X305-03"
			),
		@ApiImplicitParam(
				name="operMark", 
				dataType="String",
				value="操作编号 eg:OM1589263660827"
			),
		@ApiImplicitParam(
				name="moneyType", 
				dataType="String",
				value="金额类型 eg:驾驶员工资"
			),
		@ApiImplicitParam(
				name="cusName", 
				dataType="String",
				value="客户名称 eg:单位名称/姓名,电话"
			),
		@ApiImplicitParam(
				name="serviceName", 
				dataType="String",
				value="业务员姓名"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="expBtlBankEx", method=RequestMethod.POST)
	public void expBtlBankEx(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		//Map<String, Object> map = new HashMap<String, Object>();
    	btlSer.btlExport(request, response, ReqSrc.PC_COMPANY, jsonObject);
    	//Message.print(response, map);
	}
	/*****************************银行帐管理*****************end***********/
	
	
	/*********************************员工报账模块*******************start**************/
	/**
	 * 获取-单位员工报账列表-分页 API（post）/company/finance/findStaffReimList
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="获取-单位员工报账列表-分页",
			notes="map{code: 结果状态码, msg: 结果状态说明}")
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
			name="uname", 
			dataType="String",
			value="报销人账号"
		),
		@ApiImplicitParam(
			name="deptId", 
			dataType="String",
			value="部门id"
		),
		@ApiImplicitParam(
				name="remark", 
				dataType="String",
				value="摘要模糊查询"
			),
		@ApiImplicitParam(
				name="money", 
				dataType="String",
				value="报账金额"
			),
		@ApiImplicitParam(
			name="isCheck", 
			dataType="String",
			value="审核状态：-1已驳回 0 未审核 1已审核 2已生成凭证"
		),
		@ApiImplicitParam(
				name="voucherNo", 
				dataType="String",
				value="凭证号 eg：U1591541710277V202007060002"
			),
		@ApiImplicitParam(
				name="operMark", 
				dataType="String",
				value="操作标识 eg：CZ1580892579045"
			),
		@ApiImplicitParam(
				name="sTime", 
				dataType="String",
				value="添加开始时间"
			),
		@ApiImplicitParam(
				name="eTime", 
				dataType="String",
				value="添加结束时间"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	}) 
	@RequestMapping(value="findStaffReimList", method=RequestMethod.POST)
	public void findStaffReimList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String uname = jsonObject.getString("uname");
		String plateNum = jsonObject.getString("plateNum");
		String deptId = jsonObject.getString("deptId");
		String remark = jsonObject.getString("remark");
		String money = jsonObject.getString("money");
		String isCheck = jsonObject.getString("isCheck");
		String operMark = jsonObject.getString("operMark");
		String voucherNo = jsonObject.getString("voucherNo");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = srService.findStaffReimburse(request, ReqSrc.PC_COMPANY, page, rows, uname,plateNum, deptId, remark, money, 
				isCheck,voucherNo, operMark, sTime, eTime);
		Message.print(response, map);
	}
	
	/**
	 *  单位添加员工报账 API（post）/company/finance/addStaffReimburse
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="单位添加员工报账",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
				required=true, 
				name="staffReimInfo", 
				dataType="String",
				value="参数格式：报销人uname=车牌号=收入金额=支出金额=摘要=图片url(多张图片逗号拼接)@报销人uname=车牌号=收入金额=支出金额=摘要=图片url(多张图片逗号拼接),eg:0=100=测试=图片url"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "addStaffReimburse", method = RequestMethod.POST)
	public void addStaffReimburse(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String staffReimInfo = jsonObject.getString("staffReimInfo");
		map=srService.addStaffReimburse(ReqSrc.PC_COMPANY, request, response, staffReimInfo);
		Message.print(response, map);
	}
	
	/**
	 * 通过id获取员工报账信息（post）/company/cus/findStaffReimById jsonObject 必须包含id
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="通过id获取员工报账信息", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:数据列表}")
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
	@RequestMapping(value = "findStaffReimById", method = RequestMethod.POST)
	public void findStaffReimById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		String id = jsonObject.getString("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map = srService.findStaffReimById(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	
	/**
	 *  单位修改员工报账 API（post）/company/finance/modifyStaffReimburse
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="单位修改员工报账",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="updId", 
			dataType="String",
			value="修改记录id，修改时才有值"
		),
		@ApiImplicitParam(
				required=true, 
				name="uname", 
				dataType="String",
				value="报销人账号，从客户列表中选择"
			),
		@ApiImplicitParam(
				name="plateNum", 
				dataType="String",
				value="车牌号 eg:川S88888"
			),
		@ApiImplicitParam(
				required=true, 
				name="gathMoney", 
				dataType="String",
				value="收入金额，默认0"
			),
		@ApiImplicitParam(
				required=true, 
				name="payMoney", 
				dataType="String",
				value="支出金额，默认0"
			),
		@ApiImplicitParam(
				required=true, 
				name="remark", 
				dataType="String",
				value="摘要"
			),
		@ApiImplicitParam(
				name="voucherUrl", 
				dataType="String",
				value="图片url,多张图片逗号拼接"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "modifyStaffReimburse", method = RequestMethod.POST)
	public void modifyStaffReimburse(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String updId = jsonObject.getString("updId");
		String uname = jsonObject.getString("uname");
		String plateNum = jsonObject.getString("plateNum");
		String gathMoney = jsonObject.getString("gathMoney");
		String payMoney = jsonObject.getString("payMoney");
		String remark = jsonObject.getString("remark");
		String voucherUrl = jsonObject.getString("voucherUrl");
		map=srService.modifyStaffReimburse(ReqSrc.PC_COMPANY, request, response, updId, uname,plateNum, gathMoney, payMoney, remark, voucherUrl);
		Message.print(response, map);
	}
	
	/**
	 * 删除员工报账 API（post）/company/finance/delStaffReim
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="单位删除员工报账",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="delId", 
			dataType="String",
			value="删除记录id"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "delStaffReim", method = RequestMethod.POST)
	public void delStaffReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map=srService.delStaffReim(ReqSrc.PC_COMPANY, response, request, delId);
		Message.print(response, map);
	}
	/**
	 * 审核员工报账 API（post）/company/finance/checkStaffReimburse
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="审核员工报账(支持多条审核)",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="ids", 
			dataType="String",
			value="审核记录id,多条审核逗号拼接"
		),
		@ApiImplicitParam(
			name="note", 
			dataType="String",
			value="审核备注，可为空"
		),
		@ApiImplicitParam(
			name="alNoticeChoice", 
			dataType="String",
			value="审核后通知人账号，多个通知逗号拼接，可为空"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "checkStaffReimburse", method = RequestMethod.POST)
	public void checkReimburse(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String ids = jsonObject.getString("ids");
		String note = jsonObject.getString("note");
		String alNoticeChoice = jsonObject.getString("alNoticeChoice");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=srService.checkStaffReimburse(ReqSrc.PC_COMPANY, request, ids, note, alNoticeChoice);
    	Message.print(response, map);
	}
	/**
	 * 拒绝员工报账 API（post）/company/finance/checkRefuse
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="拒绝员工报账",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="rId", 
			dataType="String",
			value="拒绝记录id"
		),
		@ApiImplicitParam(
			name="reason", 
			dataType="String",
			value="拒绝原因，可为空"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "checkRefuse", method = RequestMethod.POST)
	public void checkRefuse(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String rId = jsonObject.getString("rId");
		String reason = jsonObject.getString("reason");
		Map<String, Object> map = new HashMap<String, Object>();
		map=srService.checkRefuse(ReqSrc.PC_COMPANY, request, response, rId, reason);
		Message.print(response, map);
	}
	
	/**
	 * 生成凭证 API（post）/company/finance/createReim
	 * @author xx
	 * @date 20200611
	 */
	@ApiOperation(value="生成凭证",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="createInfo", 
			dataType="String",
			value="参数格式：报账记录id=科目id@报账记录id=科目id"
		),
		@ApiImplicitParam(
				required=true,
			name="faceCourseInfo", 
			dataType="String",
			value="参数格式：对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额"
		),
		@ApiImplicitParam(
				required=true, 
				name="gainTime", 
				dataType="String",
				value="记账时间"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "createReim", method = RequestMethod.POST)
	public void createReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String createInfo = jsonObject.getString("createInfo");
		String faceCourseInfo = jsonObject.getString("faceCourseInfo");
		String gainTime = jsonObject.getString("gainTime");
		Map<String, Object> map = new HashMap<String, Object>();
		map=srService.createReim(ReqSrc.PC_COMPANY, request, response, createInfo, faceCourseInfo, gainTime);
		Message.print(response, map);
	}
	
	/*********************************员工报账模块*******************end**************/
	
	/*********************************凭证模块*******************start**************/
	/**
	 * 获取-单位凭证列表-分页 API（post）/company/finance/findReimList
	 * @author xx
	 * @date 20200520
	 */
	@ApiOperation(value="获取-单位凭证列表-分页",
			notes="map{code: 结果状态码, msg: 结果状态说明, totalGath:累计收入 totalPay:累计支出 balance:余额 singleFee:打单费"
					+ "washingFee:洗车费 parkingFee:停车费 roadFee:过路费 livingFee:生活费 otherFee:其他费 waterFee:买水费 stayFee:住宿费}")
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
			name="uname", 
			dataType="String",
			value="报销人账号"
		),
		@ApiImplicitParam(
			name="vouNum", 
			dataType="String",
			value="凭证编号"
		),
		@ApiImplicitParam(
			name="status", 
			dataType="String",
			value="0收入 1支出"
		),
		@ApiImplicitParam(
			name="isCheck", 
			dataType="String",
			value="审核状态：-1已驳回 0 未审核 1已审核 2已核销 3已关联"
		),
		@ApiImplicitParam(
				name="courseName", 
				dataType="String",
				value="科目名称，传多个逗号拼接"
			),
		@ApiImplicitParam(
				name="myBank", 
				dataType="String",
				value="银行名称 eg:周尚明建行"
			),
		@ApiImplicitParam(
				name="reimIsCar", 
				dataType="String",
				value="0公司开支 1车辆开支"
			),
		@ApiImplicitParam(
				name="reimPlateNum", 
				dataType="String",
				value="车牌号"
			),
		@ApiImplicitParam(
				name="reimZy", 
				dataType="String",
				value="摘要模糊查询"
			),
		@ApiImplicitParam(
				name="reimMoney", 
				dataType="String",
				value="凭证金额"
			),
		@ApiImplicitParam(
				name="operMark", 
				dataType="String",
				value="操作编号 eg：OM1580892579045"
			),
		@ApiImplicitParam(
				name="sTime", 
				dataType="String",
				value="添加开始时间"
			),
		@ApiImplicitParam(
				name="eTime", 
				dataType="String",
				value="添加结束时间"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	}) 
	@RequestMapping(value="findReimList", method=RequestMethod.POST)
	public void findReimList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String uname = jsonObject.getString("uname");
		String vouNum = jsonObject.getString("vouNum");
		String status = jsonObject.getString("status");
		String isCheck = jsonObject.getString("isCheck");
		String courseName = jsonObject.getString("courseName");
		String myBank = jsonObject.getString("myBank");
		String reimIsCar = jsonObject.getString("reimIsCar");
		String reimPlateNum = jsonObject.getString("reimPlateNum");
		String reimZy = jsonObject.getString("reimZy");
		String reimMoney = jsonObject.getString("reimMoney");
		String operMark = jsonObject.getString("operMark");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = reimSer.findReimList(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), uname, vouNum, status, isCheck, courseName, myBank, 
				reimIsCar, reimPlateNum, reimZy, reimMoney, operMark, sTime, eTime);
		
		Message.print(response, map);
	}
	
	/**
	 * 通过id获取凭证信息（post）/company/cus/findReimById jsonObject 必须包含id
	 * @author xx
	 * @date 20200505
	 */
	@ApiOperation(value="通过id获取凭证信息", notes="返回map{code: 结果状态码, msg: 结果状态码说明, data:数据列表}")
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
	@RequestMapping(value = "findReimById", method = RequestMethod.POST)
	public void findReimById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody JSONObject jsonObject) {
		String id = jsonObject.getString("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map = reimSer.findReimById(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	/**
	 *  修改财务记账凭证 API（post）/company/finance/modifyReim
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="修改凭证记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="updId", 
			dataType="String",
			value="修改记录id"
		),
		@ApiImplicitParam(
				required=true, 
				name="modifyFeeCourse", 
				dataType="String",
				value="参数格式：修改科目交易记录id=修改科目id@修改科目交易记录id=修改科目id"
			),
		@ApiImplicitParam(
				required=true, 
				name="gainTime", 
				dataType="String",
				value="记账时间"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "modifyReim", method = RequestMethod.POST)
	public void modifyReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String updId = jsonObject.getString("updId");
		String modifyFeeCourse = jsonObject.getString("modifyFeeCourse");
		String gainTime = jsonObject.getString("gainTime");
		map=reimSer.modifyReim(ReqSrc.PC_COMPANY, request, response, updId, modifyFeeCourse, gainTime);
		Message.print(response, map);
	}
	/**
	 * 删除财务记账凭证 API（post）/company/finance/delReim
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="单位删除凭证记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="delId", 
			dataType="String",
			value="删除记录id"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "delReim", method = RequestMethod.POST)
	public void delReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String delId = jsonObject.getString("delId");
		map=reimSer.delReim(ReqSrc.PC_COMPANY, response, request, delId);
		Message.print(response, map);
	}
	/**
	 * 核销凭证 API（post）/company/finance/confirmReim
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="核销凭证(支持多条核销)",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="ids", 
			dataType="String",
			value="核销记录id,多条核销逗号拼接"
		),
		@ApiImplicitParam(
			required=true, 
			name="money", 
			dataType="String",
			value="核销金额"
		),
		@ApiImplicitParam(
			name="note", 
			dataType="String",
			value="核销备注，可为空"
		),
		@ApiImplicitParam(
			required=true,
			name="myBankInfo", 
			dataType="String",
			value="我的银行信息格式：银行名称/银行账号"
		),
		@ApiImplicitParam(
			required=true,
			name="transInfo", 
			dataType="String",
			value="对方银行信息格式：对方户名/对方账号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "confirmReim", method = RequestMethod.POST)
	public void confirmReim(HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject jsonObject) {
		String ids = jsonObject.getString("ids");
		String money = jsonObject.getString("money");
		String note = jsonObject.getString("note");
		String myBankInfo = jsonObject.getString("myBankInfo");
		String transInfo = jsonObject.getString("transInfo");
		Map<String, Object> map = new HashMap<String, Object>();
    	map=reimSer.confirmReim(ReqSrc.PC_COMPANY, request, ids, money, note, myBankInfo, transInfo);
    	Message.print(response, map);
	}
	/**
	 * @Description:凭证添加时获取科目列表，用于下拉框（post）/company/finance/findCourses
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="获取科目列表，用于下拉框-不分页", 
			notes="返回map{code: 结果状态码, msg: 结果状态码说明, courses:科目列表}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="tips", 
			dataType="String",
			value="格式：【all】全部 【last】最后一级科目"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findCourses", method = RequestMethod.POST)
	public void findFeeCourse(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String tips = jsonObject.getString("tips");
		map =fcSer.findCourses(ReqSrc.PC_COMPANY, response, request,tips);
		Message.print(response, map);
	}
	/**
	 * @Description:获取员工报账摘要列表，用于下拉框（post）/company/finance/findStaffReimRemarks
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="获取员工报账摘要列表，用于下拉框-不分页，不传参", 
			notes="返回map{code: 结果状态码, msg: 结果状态码说明, remarks:摘要列表}")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "findStaffReimRemarks", method = RequestMethod.POST)
	public void findReimRemarks(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =srService.findStaffReimRemarks(ReqSrc.PC_COMPANY, response, request);
		Message.print(response, map);
	}
	/*********************************凭证模块*******************end**************/
	
	/*********************************科目模块*******************start**************/
	
	/**
	 * 获取-单位科目交易记录-分页列表 API（post）/company/finance/findCourseTrades
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="获取-单位科目交易记录-分页列表",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			name="voucherNum", 
			dataType="String",
			value="凭证号 eg:U1591541710277V202007060002"
		),
		@ApiImplicitParam(
			name="courseName", 
			dataType="String",
			value="科目名称 eg:库存现金"
		),
		@ApiImplicitParam(
			name="courseId", 
			dataType="String",
			value="科目id,多个逗号拼接 eg:1,2,3"
		),
		@ApiImplicitParam(
			name="uname", 
			dataType="String",
			value="报销人账号 eg:U1586856470308"
		),
		@ApiImplicitParam(
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川A88888"
		),
		@ApiImplicitParam(
			name="sTime", 
			dataType="String",
			value="添加开始时间 eg:2020-04-11 08:44:56"
		),
		@ApiImplicitParam(
			name="eTime", 
			dataType="String",
			value="添加结束时间 eg:2020-04-21 08:44:56"
		),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findCourseTrades", method=RequestMethod.POST)
	public void findCourseTrades(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String voucherNum = jsonObject.getString("voucherNum");
		String courseName = jsonObject.getString("courseName");
		String courseId = jsonObject.getString("courseId");
		String uname = jsonObject.getString("uname");
		String palteNum = jsonObject.getString("palteNum");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = fctSer.findCourseTrades(ReqSrc.PC_COMPANY, page, rows,LU.getLUnitNum(request, redis), voucherNum, 
				courseName, courseId, uname, palteNum, sTime, eTime);
		Message.print(response, map);
	}
	
	/**
	 * @Description:科目期初设置（post）/company/finance/firstBalanceSet
	 * @author xx
	 * @date 20200521
	 */
	@ApiOperation(value="科目期初余额设置", notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="courseId", 
			dataType="String",
			value="被设置的科目id eg:1"
		),
		@ApiImplicitParam(
				required=true, 
				name="balance", 
				dataType="String",
				value="期初余额 eg:100"
			),
		@ApiImplicitParam(
				name="gathMoney", 
				dataType="String",
				value="收入金额 eg:100"
			),
		@ApiImplicitParam(
				name="payMoney", 
				dataType="String",
				value="支出金额 eg:100"
			),
		@ApiImplicitParam(
				name="setId", 
				dataType="String",
				value="期初记录id 修改时传入 eg:1"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "firstBalanceSet", method = RequestMethod.POST)
	public void firstBalanceSet(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String courseId = jsonObject.getString("courseId");
		String gathMoney = jsonObject.getString("gathMoney");
		String payMoney = jsonObject.getString("payMoney");
		String balance = jsonObject.getString("balance");
		String setId = jsonObject.getString("setId");
		map =fctfSer.firstBalanceSet(ReqSrc.PC_COMPANY, request, response, courseId, gathMoney, payMoney, balance, setId);
		Message.print(response, map);
	}
	
	/**
	 * 获取-单位科目-分页列表 API（post）/company/finance/findFeeCourses
	 * @author xx
	 * @date 20200702
	 */
	@ApiOperation(value="获取-单位科目-分页列表",notes="map{code: 结果状态码, msg: 结果状态说明, data: 数据列表, count: 数据总条数}")
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
			value="科目名称、科目编码、科目简称，模糊搜索"
		),
		@ApiImplicitParam(
			name="courseType", 
			dataType="String",
			value="收支状态 0收入 1支出"
		),
		@ApiImplicitParam(
			name="courseStatus", 
			dataType="String",
			value="使用状态 0启用 1停用"
		),
		@ApiImplicitParam(
				name="courseCategory", 
				dataType="String",
				value="科目类别 eg: 资产类[PROPERTY] 负债类[DEBT] 权益类[LEGAL] 成本类[COST] 损益类[LOSSES]"
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
	@RequestMapping(value="findFeeCourses", method=RequestMethod.POST)
	public void findFeeCourses(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		String find = jsonObject.getString("find");
		String courseType = jsonObject.getString("courseType");
		String courseStatus = jsonObject.getString("courseStatus");
		String courseCategory = jsonObject.getString("courseCategory");
		String sTime = jsonObject.getString("sTime");
		String eTime = jsonObject.getString("eTime");
		map = fcSer.findFeeCourses(ReqSrc.PC_COMPANY, page, rows, LU.getLUnitNum(request, redis), find, 
				courseType, courseStatus, courseCategory, sTime, eTime);
		Message.print(response, map);
	}
	
	
	/**
	 * 根据parentId获取科目已关联记录 API（post）/company/finance/findCourseLink
	 * @author xx
	 * @date 20200707
	 */
	@ApiOperation(value="新增子级科目时根据parentId获取科目已关联记录",
			notes="返回map{code: 结果状态码, msg: 结果状态码说明, fctIds:关联交易记录id，bankIds：关联银行记录id}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="parentId", 
			dataType="String",
			value="当前新增科目的父级id eg:1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findCourseLink", method = RequestMethod.POST)
	public void findCourseLink(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String parentId = jsonObject.getString("parentId");
		map =fcSer.findCourseLink(ReqSrc.PC_COMPANY, response, request, parentId);
		Message.print(response, map);
	}
	
	
	/**
	 * 新增凭证科目API（post）/company/finance/addFeeCourse
	 * @author zh
	 * @date 20200522
	 */
	@ApiOperation(value="新增凭证科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="courseCategory", 
			dataType="String",
			value="科目类别 eg: 资产类[PROPERTY] 负债类[DEBT] 权益类[LEGAL] 成本类[COST] 损益类[LOSSES]"
		),
		@ApiImplicitParam(
			required=true, 
			name="courseName", 
			dataType="String",
			value="科目名称 eg:名称"
			),
		@ApiImplicitParam(
			required=true, 
			name="pinyinSimple", 
			dataType="String",
			value="科目简拼 eg:fy"
			),
		@ApiImplicitParam(
			required=true, 
			name="courseType", 
			dataType="int",
			value="收支  eg:0收 1支"
			),
		@ApiImplicitParam(
			required=true, 
			name="courseStatus", 
			dataType="String",
			value="状态 eg: 0可用 1不可用"
				),
		@ApiImplicitParam(
			required=true, 
			name="level", 
			dataType="int",
			value="层级 eg:科目层级，1为最高层级"
				),
		@ApiImplicitParam(
			required=true, 
			name="parentId", 
			dataType="Long",
			value="上层科目 eg:上层科目id,若是根层级则为null"
			),
		@ApiImplicitParam(
			name="fctIds", 
			dataType="Long",
			value="父级关联的科目交易记录id eg:1,2,3"
			),
		@ApiImplicitParam(
			name="bankIds", 
			dataType="Long",
			value="父级关联的银行记录id eg:1,2,3"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addFeeCourse", method = RequestMethod.POST)
	public void addFeeCourse(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String courseCategory = jsonObject.getString("courseCategory");
		String courseName = jsonObject.getString("courseName");
		String pinyinSimple = jsonObject.getString("pinyinSimple");
		String courseType = jsonObject.getString("courseType");
		String courseStatus = jsonObject.getString("courseStatus");
		String level = jsonObject.getString("level");
		String parentId = jsonObject.getString("parentId");
		String fctIds = jsonObject.getString("fctIds");
		String bankIds = jsonObject.getString("bankIds");
		map = fcSer.addFeeCourse(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis), courseCategory, courseName, 
				pinyinSimple, courseType, courseStatus, level, parentId, fctIds, bankIds);

		Message.print(response, map);
	}
	
	
	/**
	 * 改变科目状态(可用/不可用) API（post）/company/finance/changeCourseStatus
	 * @author zh
	 * @date 20200522
	 */
	@ApiOperation(value="改变科目状态(可用/不可用)",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="科目id eg:1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="changeCourseStatus", method = RequestMethod.POST)
	public void changeCourseStatus(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map = fcSer.changeCourseStatus(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	
	
	/**
	 * 编辑凭证科目API（post）/company/finance/updateCourse
	 * @author zh
	 * @date 20200522
	 */
	@ApiOperation(value="编辑凭证科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="upId", 
			dataType="String",
			value="科目id eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="courseName", 
			dataType="String",
			value="科目名称 eg:名称"
			),
		@ApiImplicitParam(
			required=true, 
			name="pinyinSimple", 
			dataType="String",
			value="科目简拼 eg:fy"
			),
		@ApiImplicitParam(
			required=true, 
			name="courseType", 
			dataType="int",
			value="收支  eg:0收 1支"
			),
		@ApiImplicitParam(
			required=true, 
			name="courseStatus", 
			dataType="String",
			value="状态 eg: 0可用 1不可用"
				),
		@ApiImplicitParam(
				required=true, 
				name="courseCategory", 
				dataType="String",
				value="科目类别 eg: 资产类[PROPERTY] 负债类[DEBT] 权益类[LEGAL] 成本类[COST] 损益类[LOSSES]"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="updateCourse", method = RequestMethod.POST)
	public void updateCourse(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String upId = jsonObject.getString("upId");
		String courseCategory = jsonObject.getString("courseCategory");
		String courseName = jsonObject.getString("courseName");
		String pinyinSimple = jsonObject.getString("pinyinSimple");
		String courseType = jsonObject.getString("courseType");
		String courseStatus = jsonObject.getString("courseStatus");
		map=fcSer.updateCourse(ReqSrc.PC_COMPANY, response, request, upId, courseCategory, courseName, pinyinSimple, courseType, courseStatus);
		Message.print(response, map);
	}
	
	/**
	 * id获取凭证科目API（post）/company/finance/getCourseById
	 * @author zh
	 * @date 20200522
	 */
	@ApiOperation(value="id获取凭证科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="科目id eg:1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCourseById", method = RequestMethod.POST)
	public void getCourseById(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map=fcSer.getCourseById(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	
	
	/**
	 * 删除科目 API（post）/company/finance/deleteCourse
	 * @author zh
	 * @date 20200522
	 */
	@ApiOperation(value="删除科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="科目id eg:1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="deleteCourse", method = RequestMethod.POST)
	public void deleteCourse(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");
		map = fcSer.deleteCourse(ReqSrc.PC_COMPANY, response, request, id);
		Message.print(response, map);
	}
	
	
	/**
	 * 获取最上层科目 API（post）/company/finance/findRootCourse
	 * @author zh
	 * @date 20200523
	 */
	@ApiOperation(value="获取最上层科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findRootCourse", method = RequestMethod.POST)
	public void findRootCourse(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map =fcSer.findRootCourse(ReqSrc.PC_COMPANY, response, request, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	
	/**
	 * 根据parentId获取科目 API（post）/company/finance/getCourseByParentId
	 * @author zh
	 * @date 20200523
	 */
	@ApiOperation(value="根据parentId获取科目",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="parentId", 
			dataType="String",
			value="科目parentId eg:1"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCourseByParentId", method = RequestMethod.POST)
	public void getCourseByParentId(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String parentId = jsonObject.getString("parentId");
		map =fcSer.getCourseByParentId(ReqSrc.PC_COMPANY, response, request, parentId);
		Message.print(response, map);
	}
	
	
	
	/**
	 * 检验科目名称是否可用 API（post）/company/finance/checkCourseName
	 * @author zh
	 * @date 20200608
	 */
	@ApiOperation(value="检验科目名称是否可用",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="courseName", 
			dataType="String",
			value="科目名称 eg:xxx"
		),
		@ApiImplicitParam(
				required=true, 
				name="level", 
				dataType="String",
				value="科目层级 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="courseId", 
			dataType="String",
			value="科目id,修改时传科目id,新增时传null"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="checkCourseName", method = RequestMethod.POST)
	public void checkCourseName(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String courseName = jsonObject.getString("courseName");
		String courseId = jsonObject.getString("courseId");
		String level = jsonObject.getString("level");
		map = fcSer.checkCourseName(ReqSrc.PC_COMPANY, response, request, courseName,courseId,level);
		Message.print(response, map);
	}
	
	
	/*********************************科目模块*******************end**************/
	
	/**
	 * 新增车辆加油记录  API（post）/company/finance/addCarOilForPC
	 * @author zh
	 * @date 20200523
	 */
	@ApiOperation(value="新增车辆加油记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川Axxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="lastKilo", 
			dataType="double",
			value="上一次公里数 eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="currentKilo", 
			dataType="double",
			value="当前公里数  eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilWay", 
			dataType="enum",
			value="加油方式(加油站，充值卡等) eg:XJ_JY,YP_JY,YGC_JY,CZK_JY"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilStation", 
			dataType="String",
			value="加油站名称  eg:xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilRise", 
			dataType="double",
			value="加油数量:升  eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilMoney", 
			dataType="double",
			value="加油金额:元  eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilWear", 
			dataType="double",
			value="油耗:升/百公里  eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilVoucherUrl", 
			dataType="String",
			value="凭证图片下载地址 eg:http://xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilRemark", 
			dataType="String",
			value="加油备注 eg:加油备注"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilDate", 
			dataType="String",
			value="加油日期 eg:2020-05-23"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilRealMoney", 
			dataType="double",
			value="实付金额  eg:0.00"
		),
		@ApiImplicitParam(
			required=true, 
			name="uname", 
			dataType="String",
			value="用户名  eg:U1586856470308"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="addCarOilForPC", method = RequestMethod.POST)
	public void addCarOilForPC(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map=coSer.addCarOil(ReqSrc.PC_COMPANY, request, response,LU.getLUnitNum(request, redis), jsonObject);
		Message.print(response, map);
	}
	
	
	/**
	 * 分页查询车辆加油记录  API（post）/company/finance/findCarOilList
	 * @author zh
	 * @date 20200523
	 */
	@ApiOperation(value="分页查询车辆加油记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川Axxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilWay", 
			dataType="String",
			value="加油方式 eg:XJ_JY"
		),
		@ApiImplicitParam(
			required=true, 
			name="oilStation", 
			dataType="String",
			value="加油站名称  eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="sTime", 
			dataType="String",
			value="开始时间 eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="eTime", 
			dataType="String",
			value="结束时间  eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="isCheck", 
			dataType="String",
			value="审核状态  eg:-1驳回 0 未审核 1已审核 2已复核 3已核销"
		),
		@ApiImplicitParam(
			required=true, 
			name="driver", 
			dataType="String",
			value="驾驶员uname  eg:xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="timeType", 
			dataType="String",
			value="时间类型  eg:0添加时间  1加油时间"
		),
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String",
			value="页数 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页记录数 eg:10"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findCarOilList", method = RequestMethod.POST)
	public void findCarOilList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String plateNum = jsonObject.getString("plateNum");
		String oilStation = jsonObject.getString("oilStation");
		String sTime = jsonObject.getString("sTime");
		String eTime =jsonObject.getString("eTime");
		String isCheck = jsonObject.getString("isCheck");
		String driver = jsonObject.getString("driver");
		String oilWay = jsonObject.getString("oilWay");
		String timeType = jsonObject.getString("timeType");
		String page = jsonObject.getString("page");
		String rows = jsonObject.getString("rows");
		map = coSer.findCoiList(ReqSrc.PC_COMPANY, LU.getLUnitNum(request, redis), plateNum, oilStation, sTime, eTime, isCheck, driver, oilWay, timeType, page, rows);
		Message.print(response, map);
	}
	
	
	/*********************************车辆加油记录模块*******************end**************/
	
	/**
	 * 分页查询车辆维修记录  API（post）/company/finance/findCarRepairList
	 * @author zh
	 * @date 20200523
	 */
	@ApiOperation(value="分页查询车辆维修记录",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川Axxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="cpaiStation", 
			dataType="String",
			value="维修站名称  eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="sTime", 
			dataType="String",
			value="开始时间 eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="eTime", 
			dataType="String",
			value="结束时间  eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="isCheck", 
			dataType="String",
			value="审核状态  eg:-1驳回 0 未审核 1已审核 2已复核 3已核销"
		),
		@ApiImplicitParam(
			required=true, 
			name="driver", 
			dataType="String",
			value="驾驶员uname  eg:xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String",
			value="页数 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页记录数 eg:10"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="findCarRepairList", method = RequestMethod.POST)
	public void findCarRepairList(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = crSer.getCarRepairList(ReqSrc.PC_COMPANY, request, response, jsonObject, "asd");
		Message.print(response, map);
	}
	
	/*********************************车辆维修记录模块*******************end**************/
	
	/**
	 * 业务付款列表  API（post）/company/finance/getCarOrderListForPayment
	 * @author zh
	 * @date 20200603
	 */
	@ApiOperation(value="业务付款列表",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川Axxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="orderNum", 
			dataType="String",
			value="订单号 eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="startTime", 
			dataType="String",
			value="开始时间 eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="endTime", 
			dataType="String",
			value="结束时间  eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="routeDetail", 
			dataType="String",
			value="行程详情  eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="driver", 
			dataType="String",
			value="驾驶员uname  eg:xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String",
			value="页数 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页记录数 eg:10"
		),
		@ApiImplicitParam(
			required=true, 
			name="dutyService", 
			dataType="String",
			value="用车方负责人 eg:xxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="compositor", 
			dataType="String",
			value="时间顺序 eg:DESC"
		),
		@ApiImplicitParam(
			required=true, 
			name="timeType", 
			dataType="String",
			value="搜索时间类型 eg:1.用车时间 2.下单时间"
		),
		@ApiImplicitParam(
			required=true, 
			name="suppCar", 
			dataType="String",
			value="供车方（收款方） eg:xxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="serviceMan", 
			dataType="String",
			value="业务员   eg:xxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="payStatus", 
			dataType="String",
			value="订单支付状态  eg:UNPAID,DEPOSIT_PAID,FULL_PAID"
		),
		@ApiImplicitParam(
			required=true, 
			name="customer", 
			dataType="String",
			value="客户（用车方）uname  eg:xxxxx"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCarOrderListForPayment", method = RequestMethod.POST)
	public void getCarOrderForPayment(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = carOrderService.getCarOrderListForPayment(ReqSrc.PC_COMPANY, response, request, jsonObject,LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	/**
	 * 单位业务付款API（post）/company/finance/servicePay
	 * @author xx
	 * @date 20200602
	 */
	@ApiOperation(value="单位业务付款",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
				required=true, 
				name="ids", 
				dataType="String",
				value="订单id,多个逗号拼接"
			),
		@ApiImplicitParam(
				required=true, 
				name="payMoney", 
				dataType="String",
				value="付款金额 eg:500"
			),
		@ApiImplicitParam(
				required=true, 
				name="payRemark", 
				dataType="String",
				value="付款摘要"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="servicePay", method = RequestMethod.POST)
	public void servicePay(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = jsonObject.getString("ids");
		String payMoney = jsonObject.getString("payMoney");
		String payRemark = jsonObject.getString("payRemark");
		map =carOrderService.servicePay(ReqSrc.PC_COMPANY, request, LU.getLUnitNum(request, redis), LU.getLStaff(request, redis), ids, payMoney, payRemark);
		Message.print(response, map);
	}
	
	
	/**
	 * 取消确认付款  API（post）/company/finance/cancelConfirmPayment
	 * @author zh
	 * @date 20200603
	 */
	@ApiOperation(value="取消确认付款",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="子订单id eg:xxxx"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="cancelConfirmPayment", method = RequestMethod.POST)
	public void cancelConfirmPayment(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = carOrderService.cancelConfirmPayment(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	
	/*********************************业务付款模块*******************end**************/
	
	/**
	 * 取消确认收款 API（post）/company/finance/cancelConfirmCollection
	 * @author zh
	 * @date 20200603
	 */
	@ApiOperation(value="取消确认收款",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="id", 
			dataType="String",
			value="主订单id eg:xxxx"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="cancelConfirmCollection", method = RequestMethod.POST)
	public void cancelConfirmCollection(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = mainCarOrderService.cancelConfirmCollection(ReqSrc.PC_COMPANY, response, request, jsonObject);
		Message.print(response, map);
	}
	
	
	
	/**
	 * 业务收款列表  API（post）/company/finance/getMainCarOrderForCollection
	 * @author zh
	 * @date 20200603
	 */
	@ApiOperation(value="业务收款列表",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="plateNum", 
			dataType="String",
			value="车牌号 eg:川Axxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="orderNum", 
			dataType="String",
			value="订单号 eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="startTime", 
			dataType="String",
			value="开始时间 eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="endTime", 
			dataType="String",
			value="结束时间  eg:yyyy-MM-dd"
		),
		@ApiImplicitParam(
			required=true, 
			name="routeDetail", 
			dataType="String",
			value="行程详情  eg:xxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="driver", 
			dataType="String",
			value="驾驶员uname  eg:xxxxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="page", 
			dataType="String",
			value="页数 eg:1"
		),
		@ApiImplicitParam(
			required=true, 
			name="rows", 
			dataType="String",
			value="每页记录数 eg:10"
		),
		@ApiImplicitParam(
			required=true, 
			name="dutyService", 
			dataType="String",
			value="用车方负责人 eg:xxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="compositor", 
			dataType="String",
			value="时间顺序 eg:DESC,ASC"
		),
		@ApiImplicitParam(
			required=true, 
			name="timeType", 
			dataType="String",
			value="搜索时间类型 eg:1.用车时间 2.下单时间"
		),
		@ApiImplicitParam(
			required=true, 
			name="businessType", 
			dataType="String",
			value="车辆是否自营 eg:MYSELF,NOTSELF"
		),
		@ApiImplicitParam(
			required=true, 
			name="serviceMan", 
			dataType="String",
			value="业务员   eg:xxx"
		),
		@ApiImplicitParam(
			required=true, 
			name="payStatus", 
			dataType="String",
			value="订单支付状态  eg:UNPAID(未付款 ),DEPOSIT_PAID(已付定金),FULL_PAID(全款已付)"
		),
		@ApiImplicitParam(
			required=true, 
			name="customer", 
			dataType="String",
			value="客户（用车方）uname  eg:xxxxx"
			)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getMainCarOrderForCollection", method = RequestMethod.POST)
	public void getMainCarOrderForCollection(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		map = mainCarOrderService.getMainCarOrderForCollection(ReqSrc.PC_COMPANY, response, request, jsonObject, LU.getLUnitNum(request, redis));
		Message.print(response, map);
	}
	
	/**
	 * 单位业务收款 API（post）/company/finance/serviceGath
	 * @author xx
	 * @date 20200602
	 */
	@ApiOperation(value="单位业务收款（多条收款必须是同一客户）",notes="返回map{code: 结果状态码, msg: 结果状态码说明}")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="gathType", 
			dataType="String",
			value="收款类型：0师傅团上现收 1业务员交车款 2客户自己付尾款"
		),
		@ApiImplicitParam(
			required=true, 
			name="createInfo", 
			dataType="String",
			value="参数格式：订单记录id=科目id@订单记录id=科目id"
		),
		@ApiImplicitParam(
				required=true,
			name="faceCourseInfo", 
			dataType="String",
			value="参数格式：对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额@对方科目id=对方科目摘要=对方科目借方金额=对方科目贷方金额"
		),
		@ApiImplicitParam(
				required=true, 
				name="gathMoney", 
				dataType="String",
				value="收款金额，单条订单收款时必传，默认为0 eg:500"
			),
		@ApiImplicitParam(
				name="routeDriver", 
				dataType="String",
				value="gathType=0时此参数必有值，师傅信息，默认为空"
			),
		@ApiImplicitParam(
				name="cusId", 
				dataType="String",
				value="客户id:gathType=2时此参数必有值，默认为空"
			),
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="serviceGath", method = RequestMethod.POST)
	public void serviceGath(HttpServletResponse response, HttpServletRequest request,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String gathType = jsonObject.getString("gathType");
		String createInfo = jsonObject.getString("createInfo");
		String gathMoney = jsonObject.getString("gathMoney");
		String routeDriver = jsonObject.getString("routeDriver");
		String faceCourseInfo = jsonObject.getString("faceCourseInfo");
		String cusId = jsonObject.getString("cusId");
		map =mainCarOrderService.serviceGath(ReqSrc.PC_COMPANY, request, response, LU.getLUnitNum(request, redis), gathType, routeDriver, cusId, 
				gathMoney, createInfo, faceCourseInfo, LU.getLUName(request, redis), LU.getLRealName(request, redis));
		Message.print(response, map);
	}
}
