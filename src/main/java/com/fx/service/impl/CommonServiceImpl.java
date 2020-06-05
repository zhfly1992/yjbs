package com.fx.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.exception.GlobalException;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.EncodeUtils;
import com.fx.commons.utils.other.vcode.Captcha;
import com.fx.commons.utils.other.vcode.CreateImageCode;
import com.fx.commons.utils.other.vcode.GifCaptcha;
import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.CommonDao;
import com.fx.dao.order.StationListDao;
import com.fx.entity.order.StationList;
import com.fx.service.CommonService;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 配置文件常量-服务 */
	@Autowired
	private ConfigPs cps;
	/** 远程请求-服务 */
	@Autowired
	private RestTemplate restTemplate;
	/** 公共-服务 */
	@Autowired
	private CommonDao commonDao;
	/** 站点列表-服务 */
	@Autowired
	private StationListDao stationListDao;
	
	
	@Override
	public void findImgCode(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request, int type) {
		if(type == 2) {
			String logtxt = U.log(log, "获取-gif图片验证码", reqsrc);
			
			try {
				// 设置响应的类型格式为图片格式
	            response.setContentType("image/gif");
	            response.setHeader("Pragma", "No-cache");
	            response.setHeader("Cache-Control", "no-cache");
	            response.setHeader("Expires", "0");
	            
	            Captcha captcha = new GifCaptcha(146, 33, 4);
	            captcha.out(response.getOutputStream());
	            HttpSession session = request.getSession();
//	            session.removeAttribute(QC.IMG_CODE);
	            session.setAttribute(QC.IMG_CODE, captcha.text().toLowerCase());
	        } catch (Exception e) {
	        	U.logEx(log, logtxt);
	            e.printStackTrace();
	            throw new GlobalException(logtxt);
	        }
		}else {
			String logtxt = U.log(log, "获取png图片验证码");
			
			try {
				// 设置响应的类型格式为图片格式
				response.setContentType("image/png");
				// 禁止图像缓存。
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				
				CreateImageCode vCode = new CreateImageCode(100, 30, 4, 10);
				HttpSession session = request.getSession();
//				session.removeAttribute(QC.IMG_CODE);
	            session.setAttribute(QC.IMG_CODE, vCode.getCode().toUpperCase());
				vCode.write(response.getOutputStream());
			} catch (IOException e) {
				U.log(log, logtxt, e);
				e.printStackTrace();
	            throw new GlobalException(logtxt);
			}
		}
	}

	@Override
	public Map<String, Object> findImgCodeMobile(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logtxt = U.log(log, "获取-登录验证码", reqsrc);
		try {
            Captcha captcha = new GifCaptcha(146, 33, 4);
            HttpSession session = request.getSession();
            session.setAttribute(QC.IMG_CODE, captcha.text().toLowerCase());
            map.put(QC.IMG_CODE, captcha.text().toLowerCase());
        } catch (Exception e) {
        	U.logEx(log, logtxt);
            e.printStackTrace();
            throw new GlobalException(logtxt);
        }
		return map;
		
	}
	
	@Override
	public Map<String, Object> findMapPointList(ReqSrc reqsrc, HttpServletRequest request, String keywords, 
		String city) {
		String logtxt = U.log(log, "获取-地图地点列表（地图api）", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				if(StringUtils.isEmpty(city)){
					fg = U.setPutFalse(map, "[搜索城市]不能为空");
				}else{
					city = city.trim();
					if(city.length() > 20){
						fg = U.setPutFalse(map, "[搜索城市]长度最多20个字");
					}
					
					U.log(log, "[搜索城市] city="+city);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(keywords)){
					fg = U.setPutFalse(map, "[搜索关键字]不能为空");
				}else{
					keywords = keywords.trim();
					if(keywords.length() > 50){
						fg = U.setPutFalse(map, "[搜索关键字]长度最多50个字");
					}else{
						String en = EncodeUtils.getEncode(keywords);
						if(!"UTF-8".equals(en)){
							U.log(log, "即将转换编码");
							
							keywords = EncodeUtils.gbk2utf8(keywords);
						}
						
						U.log(log, "keywords的编码是："+en);
						String en2 = EncodeUtils.getEncode(keywords);
						U.log(log, "转换后keywords的编码是："+en2);
					}
					
					U.log(log, "[搜索关键字] keywords="+keywords);
				}
			}
			
			if(fg){
				// 如果jdbc地址包含正式服务器ip，则获取数据
				if(cps.isIPpass()){
					String params = "key="+QC.MAP_WEB_API_KEY+"&keywords=" + keywords + "&types=&city="+city+"&children=1&offset=20&page=1&extensions=base";
					String res = restTemplate.getForObject("https://restapi.amap.com/v3/place/text?"+params, String.class);
					U.log(log, "请求结果: "+res);
					
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jnode = mapper.readTree(res);
					
				    String status = U.Cq(jnode, "status");
					if(StringUtils.equals("1", status)){
						map.put("data", U.toJsonStr(jnode));
						
						U.setPut(map, 1, "获取成功");
						
						U.log(log, map.get("data").toString());
					}else{
						U.setPut(map, 0, "获取数据失败");
						
						map.put("data", U.toJsonStr(jnode));
						
						U.log(log, map.get("data").toString());
					}
				}else{
					// 获取文件种的json数据
					File file = UT.getFile(this.getClass(), "/config/data/poi_data.txt");
					if(file == null){
						fg = U.setPutFalse(map, "[本地json数据文件]不存在");
					}else{
						String jsonStr = FileUtils.readFileToString(file, "UTF-8");
						
						map.put("data", U.toJsonStr(jsonStr));
						
						U.setPut(map, 1, "获取成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findStationList(ReqSrc reqsrc, HttpServletRequest request, String travelWay,
		String city) {
		String logtxt = U.log(log, "获取-站点列表（机场站点、火车站等）", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			int _travelWay = 0;
			if(fg){
				if(StringUtils.isEmpty(travelWay)){
					U.log(log, "[出行方式]为空");
				}else{
					travelWay = travelWay.trim();
					if(!FV.isPosInteger(travelWay)){
						fg = U.setPutFalse(map, "[出行方式]格式错误");
					}else{
						_travelWay = Integer.parseInt(travelWay);
					}
					
					U.log(log, "[出行方式] travelWay="+travelWay);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(city)){
					U.log(log, "[城市名称]为空");
				}else{
					city = city.trim();
					
					U.log(log, "[城市名称] city="+city);
				}
			}
			
			if(fg){
				// hql拼接
				StringBuilder sb = new StringBuilder("from StationList where 1=1 ");
				List<Object> ps = new ArrayList<Object>();
				
				if(StringUtils.isNotBlank(city)){
					sb.append("and cityName like ?0 ");
					ps.add("%"+city+"%");
				}
				
				if(StringUtils.isNotBlank(travelWay)){
					sb.append("and type = ?1 ");
					ps.add(_travelWay);
				}
				sb.append("order by type asc");
				List<StationList> pls = stationListDao.findhqlList(sb.toString(), ps.toArray());
				map.put("data", pls);
				
				U.setPut(map, 1, "获取成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findStationInfo(ReqSrc reqsrc, HttpServletRequest request, String num, String date,
		String travelWay) {
		String logtxt = U.log(log, "查询-站点信息（航班号、车次号）", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			int _travelWay = 0;
			if(fg) {
				if(StringUtils.isEmpty(travelWay)) {
					_travelWay = UT.getTravelWayByNum(num);
					if(_travelWay == 0) {
						fg = U.setPutFalse(map, "[查询编号]输入有误，无法查询");
					}
				}else {
					if(!FV.isPosInteger(travelWay)) {
						fg = U.setPutFalse(map, "[出行方式]格式错误");
					}else {
						_travelWay = Integer.parseInt(travelWay);
					}
					
					U.log(log, "[出行方式] travelWay="+travelWay);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(num)){
					fg = U.setPutFalse(map, "[查询航班号/车次号]不能为空");
				}else{
					num = num.trim().toUpperCase();// 全部转大写
					
					U.log(log, "[查询航班号/车次号] num="+num);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(date)){
					fg = U.setPutFalse(map, "[查询航班日期]不能为空");
				}else{
					date = date.trim();
					if(!FV.isDate(date)){
						fg = U.setPutFalse(map, "[查询航班日期]格式错误");
					}
					
					U.log(log, "[查询航班日期] date="+date);
				}
			}
			
			if(fg){
				if(_travelWay == 1) {
					U.log(log, "【查询航班信息】");
					
					if(cps.isIPpass()){
						String params = "appkey="+QC.FLIGHT_SEARCH_API_KEY+"&flightNo=" + num + "&date=" + date;
						String res = restTemplate.getForObject("https://way.jd.com/apemesh/fdaq?"+params, String.class);
						U.log(log, "请求结果: "+res);
						
						ObjectMapper mapper = new ObjectMapper();
						JsonNode jnode = mapper.readTree(res);
						
						map = UT.getNumInfo(jnode, 1);
					}else{
						// 获取文件种的json数据
						String pathName = request.getSession().getServletContext().getRealPath("/data/flight_def_data.txt");
						File f = new File(pathName);
						if(!f.exists()){
							fg = U.setPutFalse(map, "[本地json数据文件]不存在");
						}else{
							String jsonStr = FileUtils.readFileToString(f, "UTF-8");
							
							ObjectMapper mapper = new ObjectMapper();
							
							map = UT.getNumInfo(mapper.readTree(jsonStr), 1);
						}
					}
				}else if(_travelWay == 2) {
					U.log(log, "【查询火车车次信息】");
					
					if(fg){
						if(cps.isIPpass()){
							String params = "appkey="+QC.FLIGHT_SEARCH_API_KEY+"&trainno=" + num;
							String res = restTemplate.getForObject("https://way.jd.com/jisuapi/line?"+params, String.class);
							U.log(log, "请求结果: "+res);
							
							ObjectMapper mapper = new ObjectMapper();
							JsonNode jnode = mapper.readTree(res);
							map = UT.getNumInfo(jnode, 2);
						}else{
							// 获取文件种的json数据
							String pathName = request.getSession().getServletContext().getRealPath("/data/train_def_data.txt");
							File f = new File(pathName);
							if(!f.exists()){
								fg = U.setPutFalse(map, "[本地json数据文件]不存在");
							}else{
								String jsonStr = FileUtils.readFileToString(f, "UTF-8");
								
								ObjectMapper mapper = new ObjectMapper();
								map = UT.getNumInfo(mapper.readTree(jsonStr), 2);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt, e);
			
			U.setPut(map, -1, "获取航班信息超时，请重试");
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> isOpenCityService(ReqSrc reqsrc, HttpServletRequest request, String travelWay,
		String city, String terminal) {
		String logtxt = U.log(log, "验证-指定城市站点（机场/火车站/车站）平台是否有开通服务", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			int _travelWay = 0;
			if(fg){
				if(StringUtils.isEmpty(travelWay)){
					fg = U.setPutFalse(map, "[出行方式]不能为空");
				}else{
					travelWay = travelWay.trim();
					if(!FV.isPosInteger(travelWay)){
						fg = U.setPutFalse(map, "[出行方式]格式错误");
					}else{
						_travelWay = Integer.parseInt(travelWay);
					}
					
					U.log(log, "[出行方式] travelWay="+travelWay);
				}
			}
			
			if(fg){
				if(StringUtils.isEmpty(city)){
					fg = U.setPutFalse(map, "[城市名称]不能为空");
				}else{
					city = city.trim();
					
					U.log(log, "[城市名称] city="+city);
				}
			}
			
			if(_travelWay == 1){// 接送机-航站楼
				if(fg){
					if(StringUtils.isEmpty(terminal)){
						U.log(log, "[航站楼]为空");
					}else{
						terminal = terminal.trim();
						
						U.log(log, "[航站楼] terminal="+terminal);
					}
				}
				
				if(fg){
					List<StationList> pls = new ArrayList<StationList>();
					if(StringUtils.isEmpty(terminal)){
						hql = "from StationList where cityName like ? and type = ? order by posCode asc";
						pls = stationListDao.findhqlList(hql, "%"+city+"%", _travelWay);
					}else{
						hql = "from StationList where cityName like ? and type = ? and nameNote like ? order by posCode asc";
						pls = stationListDao.findhqlList(hql, "%"+city+"%", _travelWay, "%"+terminal+"%");
					}
					
					if(pls.size() == 0){
						fg = U.setPutFalse(map, "抱歉，["+city+"]暂未提供该服务");
					}else {
						String selTerminal = "";
						if(StringUtils.isNotEmpty(terminal)) {
							if(pls.size() == 1) {
								selTerminal = pls.get(0).getCityName()+" "+pls.get(0).getName();
							}
						}
						
						map.put("terminal", selTerminal);
						
						U.setPut(map, 1, "获取成功");
					}
				}
			}
			
			if(_travelWay == 2){// 接送火车-站点
				if(fg){
					if(StringUtils.isEmpty(terminal)){
						fg = U.setPutFalse(map, "[站点]不能为空");
					}else{
						terminal = terminal.trim();
						
						U.log(log, "[站点] terminal="+terminal);
					}
				}
				
				if(fg){
					List<StationList> pls = new ArrayList<StationList>();
					hql = "from StationList where type = ? and nameNote like ? order by posCode asc";
					pls = stationListDao.findhqlList(hql, _travelWay, "%"+terminal+"%");
					
					if(pls.size() == 0){
						fg = U.setPutFalse(map, "抱歉，["+city+"]暂未提供该服务");
					}else {
						String selTerminal = "";
						if(StringUtils.isNotEmpty(terminal)) {
							selTerminal = pls.get(0).getCityName()+" "+pls.get(0).getName();
						}
						
						map.put("terminal", selTerminal);
						
						U.setPut(map, 1, "获取成功");
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public CarRouteRes queryCarRouteRes(String spoint, String epoint, String waypoints, String stg) {
		return commonDao.queryCarRouteRes(spoint, epoint, waypoints, stg);
	}
	
}
