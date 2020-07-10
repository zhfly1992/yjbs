package com.fx.web.controller.common;

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
import com.fx.commons.utils.other.towcode.EncoderHandler;
import com.fx.commons.utils.tools.U;
import com.fx.service.CommonService;
import com.fx.service.back.CarBrandService;
import com.fx.service.back.CityListService;
import com.fx.service.back.CountyListService;
import com.fx.service.back.scenic_spots_dat.ScenicSpotsPointService;
import com.fx.service.cus.SmsRecordService;
import com.fx.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="公共模块[不验证登录]")
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {
	
	/** 公共-服务 */
	@Autowired
	private CommonService commonSer;
	/** 城市服务 */
	@Autowired
	private CityListService citySer;
	/** 车辆品牌服务 */
	@Autowired
	private CarBrandService cbSer;
	
	/** 景点地点-服务 */
	@Autowired
	private ScenicSpotsPointService scenicSpotsPointSer;
	/** 城市区/县-服务 */
	@Autowired
	private CountyListService countySer;
	/** 短信记录-服务 */
	@Autowired
	private SmsRecordService smsRecordSer;
	
	
	/**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     */
    @RequestMapping(value="unauth")
    public void unAuth(HttpServletResponse response) {
    	Message.print(response, new Message(401, "用户未登录"));
    }

    /**
     * 未授权，无权限，此处返回未授权状态信息由前端控制跳转页面
     */
    @RequestMapping(value="unauthorized")
    public void unauthorized(HttpServletResponse response) {
    	Message.print(response, new Message(400, "用户无权限"));
    }
  
    @Log("获取-手机短信-登录验证码")
	@ApiOperation(value="获取-手机短信-登录验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="lphone", 
			paramType="query", 
			dataType="String", 
			value="手机号码"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getPhoneCode", method=RequestMethod.POST)
	public void getPhoneCode(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json){
    	Map<String, Object> map = new HashMap<String, Object>();
		
		String lphone = U.P(json, "lphone");
		
		map = smsRecordSer.sendSmsCode(request, response, lphone);
		
		Message.print(response, map);
	}
    
    /**
	* 生成二维码
	 * 请求接口（get）：/common/getTwoCode
	*/
	@ApiOperation(value="获取-字符串二维码图片", notes="返回一个文件流")
	@RequestMapping(value = "getTwoCode", method = RequestMethod.GET)
	public void getQRCoder(HttpServletResponse response){
		String content = "http://121.36.52.250/yjbs-driver";
		EncoderHandler.createCode(content, response);
	}
	
	@ApiOperation(value="获取-图片验证码", notes="返回一个文件流")
	@RequestMapping(value="getImgCode", method=RequestMethod.GET)
    public void getImgCode(HttpServletResponse response, HttpServletRequest request) {
		commonSer.findImgCode(ReqSrc.COMMON, response, request, 2);
    }
	
	@ApiOperation(value="获取-图片验证码", notes="返回一个json对象")
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getImgCodeModile", method=RequestMethod.POST)
    public void getImgCodeModile(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = commonSer.findImgCodeMobile(ReqSrc.COMMON, response, request);
		
		Message.print(response, map);
    }
	
	@Log("获取-地图地点列表")
	@ApiOperation(value="获取-地图地点列表", notes="返回一个名为data的数组")
	@ApiImplicitParams({
		@ApiImplicitParam(
			required=true, 
			name="keywords", 
			paramType="query", 
			dataType="String", 
			value="地址关键字 eg：天府广场"
		),
		@ApiImplicitParam(
			required=true, 
			name="city", 
			paramType="query", 
			dataType="String",
			value="城市名称 ge：成都市"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getMapPointList", method=RequestMethod.POST)
	public void getMapPointList(HttpServletResponse response, HttpServletRequest request, 
		String keywords, String city) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = commonSer.findMapPointList(ReqSrc.COMMON, request, keywords, city);
		
		Message.print(response, map);
	}
	
	@Log("获取-系统站点列表")
	@ApiOperation(value="获取-系统站点列表", notes="返回一个名为data的数组")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="travelWay",  
			paramType="query", 
			dataType="String",
			value="出行类型 1-飞机；2-火车；3-汽车；"
		),
		@ApiImplicitParam(
			name="city",  
			paramType="query", 
			dataType="String",
			value="城市名称 ge：成都市"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getStationList", method=RequestMethod.POST)
	public void getStationList(HttpServletResponse response, HttpServletRequest request, 
		String travelWay, String city) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = commonSer.findStationList(ReqSrc.COMMON, request, travelWay, city);
		
		Message.print(response, map);
	}
	
	@Log("查询-航班号/车次号信息")
	@ApiOperation(value="查询-航班号/车次号信息", notes="返回一个名为data的数组")
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="num",  
			paramType="query", 
			dataType="String",
			value="航班号/车次号"
		),
		@ApiImplicitParam(
			name="date",  
			paramType="query", 
			dataType="String",
			value="查询日期 格式为：yyyy-MM-dd eg:2017-10-10"
		),
		@ApiImplicitParam(
			name="travelWay",  
			paramType="query", 
			dataType="String",
			value="出行类型 1-飞机；2-火车；3-汽车；"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="queryStationInfo", method=RequestMethod.POST)
	public void queryStationInfo(HttpServletResponse response, HttpServletRequest request, 
		String num, String date, String travelWay) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = commonSer.findStationInfo(ReqSrc.COMMON, request, num, date, travelWay);
		
		Message.print(response, map);
	}
	
	@Log("验证-指定城市站点（机场/火车站/车站）平台是否有开通服务")
	@ApiOperation(
		value="验证-指定城市站点（机场/火车站/车站）平台是否有开通服务", 
		notes="如果站点已开通，则返回terminal站点信息"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="travelWay",  
			paramType="query", 
			dataType="String",
			value="出行类型 1-飞机；2-火车；3-汽车；"
		),
		@ApiImplicitParam(
			name="city",  
			paramType="query", 
			dataType="String",
			value="城市名称 ge：成都市"
		),
		@ApiImplicitParam(
			name="terminal",  
			paramType="query", 
			dataType="String",
			value="T1、T2等"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="isOpenCityService", method=RequestMethod.POST)
	public void isOpenCityService(HttpServletResponse response, HttpServletRequest request, 
		String travelWay, String isShuttle, String city, String terminal) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = commonSer.isOpenCityService(ReqSrc.COMMON, request, travelWay, city, terminal);
		
		Message.print(response, map);
	}
	/**
	 * @Description:获取城市列表，用于下拉框（post）/common/getCitys
	 * @author xx
	 * @date 20200507
	 */
	@Log("获取城市列表")
	@ApiOperation(
		value="用于下拉框", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="provinceCode",  
			paramType="query", 
			dataType="String",
			value="省份编号,可为空,级联查询时才有值"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getCitys", method = RequestMethod.POST)
	public void getCitys(HttpServletResponse response, HttpServletRequest requestm,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String provinceCode=jsonObject.getString("provinceCode");//默认可为空，级联查询时才会有省份编号
		map =citySer.findCityList(provinceCode);
		Message.print(response, map);
	}
	
	/**
	 * @Description:根据车辆类型获取车牌品牌列表，用于下拉框（post）/common/getCarBrandsByCarType
	 * @author xx
	 * @date 20200507
	 */
	@Log("根据车辆类型获取车牌品牌列表")
	@ApiOperation(
		value="用于下拉框", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="carType",  
			paramType="query", 
			dataType="String",
			value="车辆类型：0:大巴车 1:中巴车 2:商务车 3:越野车 4:轿车"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getCarBrandsByCarType", method = RequestMethod.POST)
	public void getCarBrands(HttpServletResponse response, HttpServletRequest requestm,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String carType=jsonObject.getString("carType");//不可为空
		map =cbSer.findCarBrands(carType);
		Message.print(response, map);
	}
	
	
	/**
	 * @Description:根据城市id获取车牌前缀，用于下拉框（post）/common/getPlateNumShort
	 * @author zh
	 * @date 20200507
	 */
	@Log("根据城市id获取车牌前缀")
	@ApiOperation(
		value="用于下拉框", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="id",  
			paramType="query", 
			dataType="String",
			value="城市id"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getPlateNumShort", method = RequestMethod.POST)
	public void getPlateNumShortByCityId(HttpServletResponse response, HttpServletRequest requestm,@RequestBody JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		String id = jsonObject.getString("id");//不可为空
		map = citySer.getPlateNumShortById(id);
		Message.print(response, map);
	}
	
	
	/**
	 * @Description:获取所有车辆品牌（post）/common/getAllCarBrand
	 * @author zh
	 * @date 20200529
	 */
	@Log("获取所有车辆品牌")
	@ApiOperation(
		value="用于前端转换", 
		notes="返回一个名为data的数组"
	)
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value = "getAllCarBrand", method = RequestMethod.POST)
	public void getAllCarBrand(HttpServletResponse response, HttpServletRequest requestm){
		Map<String, Object> map = new HashMap<String, Object>();
		map = cbSer.findAllCarBrands();
		Message.print(response, map);
	}
	
	@ApiOperation(
		value="获取-城市-列表", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="province",  
			paramType="query", 
			dataType="String",
			value="省份名称或编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCityList", method=RequestMethod.POST)
  	public void getCityList(HttpServletRequest request, HttpServletResponse response, 
  		@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		String province = jsonObject.getString("province");
  		map = citySer.findCityList(province);
  		
  		Message.print(response, map);
  	}
	
	@ApiOperation(
		value="获取-城市区/县-列表", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="city",  
			paramType="query", 
			dataType="String",
			value="城市名称或编号"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCountyList", method=RequestMethod.POST)
  	public void getCountyList(HttpServletRequest request, HttpServletResponse response, 
  		@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		String city = jsonObject.getString("city");
  		map = countySer.findCountyList(city);
  		
  		Message.print(response, map);
  	}
	
	@ApiOperation(
		value="获取-景点城市区/县-列表", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="cityName",  
			paramType="query", 
			dataType="String",
			value="城市名称"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getJdCityOfCountyList", method=RequestMethod.POST)
  	public void getJdCityOfCountyList(HttpServletRequest request, HttpServletResponse response, 
  		@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		String cityName = jsonObject.getString("cityName");
  		map = scenicSpotsPointSer.findJdCityOfCountyList(cityName);
  		
  		Message.print(response, map);
  	}
	
	@ApiOperation(
		value="获取-区/县景点-列表", 
		notes="返回一个名为data的数组"
	)
	@ApiImplicitParams({
		@ApiImplicitParam(
			name="cityName",  
			paramType="query", 
			dataType="String",
			value="城市名称"
		),
		@ApiImplicitParam(
			name="countyName",  
			paramType="query", 
			dataType="String",
			value="区/县名称"
		)
	})
	@ApiResponses({
		@ApiResponse(code=1, message="msg"),
		@ApiResponse(code=0, message="msg"),
		@ApiResponse(code=-1, message="msg")
	})
	@RequestMapping(value="getCountyJdList", method=RequestMethod.POST)
  	public void getCountyJdList(HttpServletRequest request, HttpServletResponse response, 
  		@RequestBody JSONObject jsonObject){
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		String cityName = jsonObject.getString("cityName");
  		String countyName = jsonObject.getString("countyName");
  		map = scenicSpotsPointSer.findCountyJdList(cityName, countyName);
  		
  		Message.print(response, map);
  	}
	
}
