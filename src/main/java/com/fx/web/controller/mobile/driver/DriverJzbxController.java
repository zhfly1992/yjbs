package com.fx.web.controller.mobile.driver;

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
import com.fx.commons.utils.tools.U;
import com.fx.service.CommonService;
import com.fx.service.company.CompanyVehicleService;
import com.fx.service.finance.CarOilListService;
import com.fx.service.finance.CarRepairListService;
import com.fx.service.finance.FeeCourseService;
import com.fx.service.finance.StaffReimburseService;
import com.fx.service.order.CarOrderService;
import com.fx.service.order.MainCarOrderService;
import com.fx.service.order.RouteTradeListService;
import com.fx.web.util.RedisUtil;

/**
 * 驾驶员-记账报销-控制器
 */
@Controller
@RequestMapping("/mb/driver/jzbx")
public class DriverJzbxController {
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 公共-服务 */
	@Autowired
	private CommonService commonSer;
	/** 单位车辆-服务  */
	@Autowired
	private CompanyVehicleService companyVehicleSer;
	/** 车队操作员-服务 */
//	@Autowired
//	private OperatorListService operSer;
	/** 车辆维修报账-服务 */
	@Autowired
	private CarRepairListService wxjzSer;
	/** 加油记账-服务 */
	@Autowired
	private CarOilListService jyjzSer;
	/** 科目-服务 */
	@Autowired
	private FeeCourseService feeCourseSer;
	/** 驾驶员-行程收支-服务 */
	@Autowired
	private RouteTradeListService rtlSer;
	/** 员工记账-服务 */
	@Autowired
	private StaffReimburseService staffReimburseSer;
	/** 主订单-服务 */
	@Autowired
	private MainCarOrderService mainOrderSer;
	/** 子订单-服务 */
	@Autowired
	private CarOrderService carOrderSer;
	
	
	/*----------公共数据请求--------------------------------*/
	/**
	 * 获取-登录车队所有车辆列表
	 * 请求接口（post）：/mb/driver/jzbx/getLteamAllCarList
	 * @param type 查询类型：allCar-所有车辆；currCar-当前驾驶员所属车辆；
	 */
	@RequestMapping(value="getLteamAllCarList", method=RequestMethod.POST)
	public void getLteamAllCarList(HttpServletRequest request, HttpServletResponse response, 
		@RequestBody JSONObject json){
		String type = U.P(json, "type");

		Map<String, Object> map = new HashMap<String, Object>();
		
		String teamNo = LU.getLUnitNum(request, redis);
		String luname = "curr-car".equals(type) ? LU.getLUName(request, redis) : "";
		map = companyVehicleSer.findTeamAllCar(ReqSrc.WX, request, response, teamNo, luname);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-获取-加油方式/维修站/站点/充值卡
	 * 请求接口（post）：/mb/driver/jzbx/getCarOilRepair
	 * @param type 类型：0-加油方式；1-充值卡；2-加油站；3-维修站；
	 */
	@RequestMapping(value="getCarOilRepair", method=RequestMethod.POST)
	public void getCarOilRepair(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String type = U.P(json, "type");

		Map<String, Object> map = new HashMap<String, Object>();
		
		String teamNo = LU.getLUnitNum(request, redis);
		String luname = LU.getLUName(request, redis);
		map = jyjzSer.findCarJzDat(ReqSrc.WX, request, response, teamNo, luname, type);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-获取-上一次的公里数和最大续航里程
	 * 请求接口（post）：/mb/driver/jzbx/getPrevkmAndMaxkm
	 * @param plateNum 车牌号
	 * @param uid 修改加油记账id
	 * @param type 查询类型：1-加油记账；2-维修记账；
	 */
	@RequestMapping(value="getPrevkmAndMaxkm", method=RequestMethod.POST)
	public void getPrevkmAndMaxkm(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String plateNum = U.P(json, "plateNum");
		String uid = U.P(json, "uid");
		String type = U.P(json, "type");

		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String teamNo = LU.getLUnitNum(request, redis);
		map = jyjzSer.findPrevkmAndMaxkm(ReqSrc.WX, request, response, teamNo, luname, plateNum, uid, type);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-费用类型-列表
	 * 请求接口（post）：/mb/driver/jzbx/getFeeCourseList
	 * @param type 科目收支类型 0-收入；1-支出
	 */
	@RequestMapping(value="getFeeCourseList", method=RequestMethod.POST)
	public void getFeeCourseList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String type = U.P(json, "type");
		
		String lunitNum = LU.getLUnitNum(request, redis);
		map = feeCourseSer.findFeeCourseList(ReqSrc.WX, request, response, lunitNum, type);
		
		Message.print(response, map);
	}
	
	/**
	 * 添加-记账报销-文件
	 * 请求接口（post）：/mb/driver/jzbx/addJzbxFile
	 * @param files 	文件数组
	 * @param ftype 	文件所属类型 JYJZ_IMG=加油记账凭证；WXJZ_IMG=维修记账凭证;QTJZ_IMG=其他记账凭证;XCSZ_IMG=行程收支凭证;
	 * @param uid 		修改对象id
	 * @param fnames 	未修改的文件名数组字符串
	 */
	@RequestMapping(value="addJzbxFile", method=RequestMethod.POST)
	public void addJzbxFile(HttpServletResponse response, HttpServletRequest request, 
		@RequestParam("files")MultipartFile[] files, String ftype, String uid, String fnames){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lteamNo = LU.getLUnitNum(request, redis);
		map = commonSer.addJzbxFile(ReqSrc.WX, ftype, lteamNo, luname, files, uid, fnames);
		
		Message.print(response, map);
	}
	
	/*----------加油记账-----------------------*/
	/**
	 * 车队驾驶员-添加-加油记录
	 * 请求接口（post）：/mb/driver/jzbx/addJyjz
	 * @param flen 			上传文件个数
	 * @param plateNum 		车牌号
	 * @param currKm 		当前公里数
	 * @param jyWay 		加油方式 
	 * @param jyStation 	加油站名称
	 * @param jyCount 		加油数量
	 * @param jyMoney 		加油金额
	 * @param jyCard 		加油卡号
	 * @param jyDate 		加油日期
	 * @param jyRemark 		加油备注
	 */
	@RequestMapping(value="addJyjz", method=RequestMethod.POST)
	public void addJyjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String flen = U.P(json, "flen");
		String plateNum = U.P(json, "plateNum");
		String currKm = U.P(json, "currKm");
		String jyWay = U.P(json, "jyWay");
		String jyStation = U.P(json, "jyStation");
		String jyCount = U.P(json, "jyCount");
		String jyMoney = U.P(json, "jyMoney");
		String jyCard = U.P(json, "jyCard");
		String jyDate = U.P(json, "jyDate");
		String jyRemark = U.P(json, "jyRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = jyjzSer.addJyjz(ReqSrc.WX, request, response, lunitNum, luname, flen, plateNum, currKm, 
			jyWay, jyStation, jyCount, jyMoney, jyCard, jyDate, jyRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-修改-加油记录
	 * 请求接口（post）：/mb/driver/jzbx/updJyjz
	 * @param uid 			加油记账对象id
	 * @param flen 			上传文件个数
	 * @param currKm 		当前公里数
	 * @param jyWay 		加油方式 
	 * @param jyStation 	加油站名称
	 * @param jyCount 		加油数量
	 * @param jyMoney 		加油金额
	 * @param jyCard 		加油卡号
	 * @param jyDate 		加油日期
	 * @param jyRemark 		加油备注
	 */
	@RequestMapping(value="updJyjz", method=RequestMethod.POST)
	public void updJyjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String uid = U.P(json, "uid");
		String flen = U.P(json, "flen");
		String currKm = U.P(json, "currKm");
		String jyWay = U.P(json, "jyWay");
		String jyStation = U.P(json, "jyStation");
		String jyCount = U.P(json, "jyCount");
		String jyMoney = U.P(json, "jyMoney");
		String jyCard = U.P(json, "jyCard");
		String jyDate = U.P(json, "jyDate");
		String jyRemark = U.P(json, "jyRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = jyjzSer.updJyjz(ReqSrc.WX, request, response, lunitNum, luname, uid, flen, currKm, jyWay, 
			jyStation, jyCount, jyMoney, jyCard, jyDate, jyRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-删除-加油记录
	 * 请求接口（post）：/mb/driver/jzbx/delJyjz
	 * @param did 加油记录id
	 */
	@RequestMapping(value="delJyjz", method=RequestMethod.POST)
	public void delJyjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String did = U.P(json, "did");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = jyjzSer.delJyjz(ReqSrc.WX, request, response, lunitNum, luname, did);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-加油记账-详情
	 * 请求接口（post）：/mb/driver/jzbx/getJyjzDetail
	 * @param id 加油记录id
	 */
	@RequestMapping(value="getJyjzDetail", method=RequestMethod.POST)
	public void getJyjzDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String id = U.P(json, "id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		map = jyjzSer.findJyjzDetail(ReqSrc.WX, request, response, luname, id);
		
		Message.print(response, map);
	}
	
	/**
     * 获取-车队驾驶员-加油记账-列表
     * 请求接口（post）：/mb/driver/jzbx/getJyjzList
	 * @param page 		  页码
	 * @param rows 		  页大小
	 * @param stime 	  加油日期-开始
	 * @param etime 	  加油日期-结束
     */
	@RequestMapping(value="getJyjzList", method=RequestMethod.POST)
  	public void getJyjzList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String page = U.P(json, "page");
		String rows = U.P(json, "rows");
  		String stime = U.P(json, "stime");
  		String etime = U.P(json, "etime");
		
  		map = jyjzSer.findJyjzList(ReqSrc.WX, request, response, page, rows, stime, etime);
		
		Message.print(response, map);
  	}
	
	
	/*----------维修记账-----------------------*/
	/**
	 * 添加-维修记账
	 * 请求接口（post）：/mb/driver/jzbx/addWxjz
	 * @param flen  		上传文件个数
	 * @param plateNum 		车牌号
	 * @param currKm 		当前公里数
	 * @param wxPayWay 		支付方式：CASH_PAY-现金；JZ_PAY-记账；
	 * @param wxStation 	维修站名称
	 * @param wxMoney 		维修金额
	 * @param wxDate 		维修日期
	 * @param wxRemark 		维修备注
	 * @param updPicId 		修改后图片id
	 */
	@RequestMapping(value="addWxjz", method=RequestMethod.POST)
	public void addWxjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String flen = U.P(json, "flen");
		String plateNum = U.P(json, "plateNum");
		String currKm = U.P(json, "currKm");
		String wxPayWay = U.P(json, "wxPayWay");
		String wxStation = U.P(json, "wxStation");
		String wxMoney = U.P(json, "wxMoney");
		String wxDate = U.P(json, "wxDate");
		String wxRemark = U.P(json, "wxRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = wxjzSer.addWxjz(ReqSrc.WX, request, response, lunitNum, luname, flen, plateNum, currKm, wxPayWay, 
			wxStation, wxMoney, wxDate, wxRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 修改-维修记账
	 * 请求接口（post）：/mb/driver/jzbx/updRepair
	 * @param uid  			维修记录id
	 * @param flen  		上传文件个数
	 * @param currKm		车辆公里数
	 * @param wxPayWay 		支付方式：CASH_PAY-现金；JZ_PAY-记账；
	 * @param wxStation 	维修站名称
	 * @param wxMoney 		维修金额
	 * @param wxDate 		维修日期
	 * @param wxRemark 		维修备注
	 * @param imgIds		图片id数组字符串
	 */
	@RequestMapping(value="updWxjz", method=RequestMethod.POST)
	public void updWxjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String uid = U.P(json, "uid");
		String flen = U.P(json, "flen");
		String currKm = U.P(json, "currKm");
		String wxPayWay = U.P(json, "wxPayWay");
		String wxStation = U.P(json, "wxStation");
		String wxMoney = U.P(json, "wxMoney");
		String wxDate = U.P(json, "wxDate");
		String wxRemark = U.P(json, "wxRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = wxjzSer.updWxjz(ReqSrc.WX, request, response, lunitNum, luname, uid, flen, currKm, wxPayWay, 
			wxStation, wxMoney, wxDate, wxRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 删除-维修记账
	 * 请求接口（post）：/mb/driver/jzbx/delWxjz
	 * @param did 维修记录id
	 */
	@RequestMapping(value="delWxjz", method=RequestMethod.POST)
	public void delWxjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String did = U.P(json, "did");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = wxjzSer.delWxjz(ReqSrc.WX, request, response, lunitNum, luname, did);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-维修记账-详情
	 * 请求接口（post）：/mb/driver/jzbx/getWxjzDetail
	 * @param id 维修记录id
	 */
	@RequestMapping(value="getWxjzDetail", method=RequestMethod.POST)
	public void getWxjzDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String id = U.P(json, "id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		map = wxjzSer.findWxjzDetail(ReqSrc.WX, request, response, luname, id);
		
		Message.print(response, map);
	}
	
	/**
     * 获取-车队驾驶员-加油记账-列表
     * 请求接口（post）：/mb/driver/jzbx/getWxjzList
	 * @param page 		  页码
	 * @param rows 		  页大小
	 * @param stime 	  加油日期-开始
	 * @param etime 	  加油日期-结束
     */
	@RequestMapping(value="getWxjzList", method=RequestMethod.POST)
  	public void getWxjzList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String page = U.P(json, "page");
		String rows = U.P(json, "rows");
  		String stime = U.P(json, "stime");
  		String etime = U.P(json, "etime");
		
  		map = wxjzSer.findWxjzList(ReqSrc.WX, request, response, page, rows, stime, etime);
		
		Message.print(response, map);
  	}
	
	
	/*----------其他记账-----------------------*/
	/**
	 * 车队驾驶员-添加-其他记账
	 * 请求接口（post）：/mb/driver/jzbx/addQtjz
	 * @param flen  		上传文件的个数
	 * @param plateNum 		车牌号
	 * @param jzDate 		记账日期
	 * @param jzFeeCourseId	记账科目id（记账类型）
	 * @param jzMoney 		记账金额
	 * @param jzRemark 		记账备注
	 */
	@RequestMapping(value="addQtjz", method=RequestMethod.POST)
	public void addQtjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String flen = U.P(json, "flen");
		String plateNum = U.P(json, "plateNum");
		String jzDate = U.P(json, "jzDate");
		String jzFeeCourseId = U.P(json, "jzFeeCourseId");
		String jzMoney = U.P(json, "jzMoney");
		String jzRemark = U.P(json, "jzRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = staffReimburseSer.addQtjz(ReqSrc.WX, request, response, lunitNum, luname, flen, plateNum, jzDate, 
			jzFeeCourseId, jzMoney, jzRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 修改-其他记账
	 * 请求接口（post）：/mb/driver/jzbx/updQtjz
	 * @param uid  			其他记账id
	 * @param flen  		上传文件个数
	 * @param jzDate 		记账日期
	 * @param jzFeeCourseId	记账科目id（记账类型）
	 * @param jzMoney 		记账金额
	 * @param jzRemark 		记账备注
	 */
	@RequestMapping(value="updQtjz", method=RequestMethod.POST)
	public void updOtherjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String uid = U.P(json, "uid");
		String flen = U.P(json, "flen");
		String jzDate = U.P(json, "jzDate");
		String jzFeeCourseId = U.P(json, "jzFeeCourseId");
		String jzMoney = U.P(json, "jzMoney");
		String jzRemark = U.P(json, "jzRemark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = staffReimburseSer.updQtjz(ReqSrc.WX, request, response, lunitNum, luname, uid, flen, jzDate, 
			jzFeeCourseId, jzMoney, jzRemark);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-删除-其他记账
	 * 请求接口（post）：/mb/driver/jzbx/delQtjz
	 * @param did 其他记账id
	 */
	@RequestMapping(value="delQtjz", method=RequestMethod.POST)
	public void delQtjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String did = U.P(json, "did");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		map = staffReimburseSer.delQtjz(ReqSrc.WX, request, response, luname, did);
		
		Message.print(response, map);
	}
	
	/**
	 * 获取-其他记账-详情
	 * 请求接口（post）：/mb/driver/jzbx/getQtjzDetail
	 * @param id 记账记录id
	 */
	@RequestMapping(value="getQtjzDetail", method=RequestMethod.POST)
	public void getQtjzDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String id = U.P(json, "id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		map = staffReimburseSer.findQtjzDetail(ReqSrc.WX, request, response, luname, id);
		
		Message.print(response, map);
	}
	
	/**
     * 获取-车队驾驶员-其他记账-列表
     * 请求接口（post）：/mb/driver/jzbx/getQtjzList
	 * @param page 		  页码
	 * @param rows 		  页大小
	 * @param stime 	  记账日期-开始
	 * @param etime 	  记账日期-结束
     */
	@RequestMapping(value="getQtjzList", method=RequestMethod.POST)
  	public void getQtjzList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String page = U.P(json, "page");
		String rows = U.P(json, "rows");
  		String stime = U.P(json, "stime");
  		String etime = U.P(json, "etime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
  		map = staffReimburseSer.findQtjzList(ReqSrc.WX, request, response, page, rows, stime, etime);
		
		Message.print(response, map);
  	}
	
	
	/*----------订单行程收支-----------------------*/
	/**
     * 获取-行程收支-主订单-列表
     * 请求接口（post）：/mb/driver/jzbx/getXcjzMainOrderList
	 * @param page 		  页码
	 * @param rows 		  页大小
	 * @param stime 	  记账日期-开始
	 * @param etime 	  记账日期-结束
     */
	@RequestMapping(value="getXcjzMainOrderList", method=RequestMethod.POST)
  	public void getXcjzMainOrderList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String page = U.P(json, "page");
		String rows = U.P(json, "rows");
  		String stime = U.P(json, "stime"); 
  		String etime = U.P(json, "etime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String lunitNum = LU.getLUnitNum(request, redis);
		String luname = LU.getLUName(request, redis);
  		map = mainOrderSer.findXcjzMainOrderList(ReqSrc.WX, request, response, lunitNum, luname, page, rows, stime, etime, "2");
		
		Message.print(response, map);
  	}
	
	/**
     * 获取-行程收支-子订单-列表
     * 请求接口（post）：/mb/driver/jzbx/getXcjzOrderList
	 * @param mid 		  主订单id
     */
	@RequestMapping(value="getXcjzOrderList", method=RequestMethod.POST)
  	public void getXcjzOrderList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String mid = U.P(json, "mid");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String lunitNum = LU.getLUnitNum(request, redis);
		String luname = LU.getLUName(request, redis);
  		map = carOrderSer.findXcjzOrderList(ReqSrc.WX, lunitNum, luname, mid);
		
		Message.print(response, map);
  	}
	
	/**
	 * 添加-行程收支
	 * 请求API（post）/mb/driver/jzbx/addXcjz
	 * @param flen 			上传文件个数
	 * @param orderNum		派车-订单编号
	 * @param groupCash		团上现收
	 * @param groupRebate   团上返点
	 * @param routeRebate	行程加点
	 * @param singleFee		打单费
	 * @param washingFee	洗车费
	 * @param parkingFee	停车费
	 * @param roadFee		过路费
	 * @param livingFee 	生活费
	 * @param otherFee 		其他费
	 * @param waterFee      买水费
	 * @param stayFee       住宿费
	 * @param remark 		备注
	 */
	@RequestMapping(value="addXcjz", method=RequestMethod.POST)
	public void addXcjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String flen = U.P(json, "flen");
		String orderNum = U.P(json, "orderNum");
		String groupCash = U.P(json, "groupCash");
		String groupRebate = U.P(json, "groupRebate");
		String routeRebate = U.P(json, "routeRebate");
		String singleFee = U.P(json, "singleFee");
		String washingFee = U.P(json, "washingFee");
		String parkingFee = U.P(json, "parkingFee");
		String roadFee = U.P(json, "roadFee");
		String livingFee = U.P(json, "livingFee");
		String otherFee = U.P(json, "otherFee");
		String waterFee = U.P(json, "waterFee");
		String stayFee = U.P(json, "stayFee");
		String remark = U.P(json, "remark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = rtlSer.addXcjz(ReqSrc.WX, request, response, lunitNum, luname, flen, orderNum, groupCash, groupRebate, 
			routeRebate, singleFee, washingFee, parkingFee, roadFee, livingFee, otherFee, waterFee, stayFee, remark);
		
		Message.print(response, map);
	}
	
	/**
	 * 修改-行程收支
	 * 请求API（post）/mb/driver/jzbx/updXcjz
	 * @param uid			行程收支id
	 * @param flen 			上传文件个数
	 * @param groupCash		团上现收
	 * @param groupRebate   团上返点
	 * @param routeRebate	行程加点
	 * @param singleFee		打单费
	 * @param washingFee	洗车费
	 * @param parkingFee	停车费
	 * @param roadFee		过路费
	 * @param livingFee 	生活费
	 * @param otherFee 		其他费
	 * @param waterFee      买水费
	 * @param stayFee       住宿费
	 * @param remark 		备注
	 */
	@RequestMapping(value="updXcjz", method=RequestMethod.POST)
	public void updXcjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String uid = U.P(json, "uid");
		String flen = U.P(json, "flen");
		String groupCash = U.P(json, "groupCash");
		String groupRebate = U.P(json, "groupRebate");
		String routeRebate = U.P(json, "routeRebate");
		String singleFee = U.P(json, "singleFee");
		String washingFee = U.P(json, "washingFee");
		String parkingFee = U.P(json, "parkingFee");
		String roadFee = U.P(json, "roadFee");
		String livingFee = U.P(json, "livingFee");
		String otherFee = U.P(json, "otherFee");
		String waterFee = U.P(json, "waterFee");
		String stayFee = U.P(json, "stayFee");
		String remark = U.P(json, "remark");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = rtlSer.updXcjz(ReqSrc.WX, request, response, lunitNum, luname, uid, flen, groupCash, groupRebate, 
			routeRebate, singleFee, washingFee, parkingFee, roadFee, livingFee, otherFee, waterFee, stayFee, remark);
		
		Message.print(response, map);
	}
	
	/**
	 * 车队驾驶员-删除-行程收支
	 * 请求接口（post）：/mb/driver/jzbx/delXcjz
	 * @param did 行程收支对应员工记账id
	 */
	@RequestMapping(value="delXcjz", method=RequestMethod.POST)
	public void delXcjz(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String did = U.P(json, "did");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		map = staffReimburseSer.delXcjz(ReqSrc.WX, request, response, luname, did);
		
		Message.print(response, map);
	}
	
	/**
     * 获取-车队驾驶员-行程记账-列表
     * 请求接口（post）：/mb/driver/jzbx/getXcjzList
	 * @param orderNum 	 子订单编号
	 * @param page 		  页码
	 * @param rows 		  页大小
	 * @param stime 	  记账日期-开始
	 * @param etime 	  记账日期-结束
     */
	@RequestMapping(value="getXcjzList", method=RequestMethod.POST)
  	public void getXcjzList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String orderNum = U.P(json, "orderNum");
		String page = U.P(json, "page");
		String rows = U.P(json, "rows");
  		String stime = U.P(json, "stime");
  		String etime = U.P(json, "etime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
  		map = staffReimburseSer.findXcjzList(ReqSrc.WX, request, response, orderNum, page, rows, stime, etime);
		
		Message.print(response, map);
  	}
	
	/**
	 * 获取-行程记账-详情
	 * 请求API（post）/mb/driver/jzbx/getXcjzDetail
	 * @param id 行程记账id
	 */
	@RequestMapping(value="getXcjzDetail", method=RequestMethod.POST)
	public void getXcjzDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
		String id = U.P(json, "id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String luname = LU.getLUName(request, redis);
		String lunitNum = LU.getLUnitNum(request, redis);
		map = rtlSer.findXcjzDetail(ReqSrc.WX, lunitNum, luname, id);
		
		Message.print(response, map);
	}

	
}
