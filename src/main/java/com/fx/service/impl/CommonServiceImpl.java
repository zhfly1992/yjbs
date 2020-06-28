package com.fx.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.exception.GlobalException;
import com.fx.commons.utils.clazz.CarRouteRes;
import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.EncodeUtils;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.other.vcode.Captcha;
import com.fx.commons.utils.other.vcode.CreateImageCode;
import com.fx.commons.utils.other.vcode.GifCaptcha;
import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.CommonDao;
import com.fx.dao.back.FileManDao;
import com.fx.dao.finance.CarOilListDao;
import com.fx.dao.finance.CarRepairListDao;
import com.fx.dao.finance.StaffReimburseDao;
import com.fx.dao.order.RouteTradeListDao;
import com.fx.dao.order.StationListDao;
import com.fx.entity.back.FileMan;
import com.fx.entity.finance.CarOilList;
import com.fx.entity.finance.CarRepairList;
import com.fx.entity.finance.StaffReimburse;
import com.fx.entity.order.RouteTradeList;
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
	/** 文件管理-服务 */
	@Autowired
	private FileManDao fileManDao;
	/** 加油记账 */
	@Autowired
	private CarOilListDao carOilListDao;
	/** 维修记账 */
	@Autowired
	private CarRepairListDao carRepairListDao;
	/** 其他记账 */
	@Autowired
	private StaffReimburseDao staffReimburseDao;
	/** 行程收支记账 */
	@Autowired
	private RouteTradeListDao routeTradeListDao;
	
	
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
	
	@Override
	public Map<String, Object> addJzbxFile(ReqSrc reqsrc, String ftype, String lteamNo, String luname, 
		MultipartFile[] files, String uid, String fnames) {
		String logtxt = U.log(log, "添加-记账报销-文件", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			FileType _ftype = null;
			if(fg) {
				if(StringUtils.isBlank(ftype)) {
					fg = U.setPutFalse(map, "[文件类型]不能为空");
				}else {
					ftype = ftype.trim();
					if(!FV.isOfEnum(FileType.class, ftype)) {
						fg = U.setPutFalse(map, "[文件类型]格式错误");
					}else {
						_ftype = FileType.valueOf(ftype);
					}
					
					U.log(log, "[文件类型] ftype="+ftype);
				}
			}
			
			if(fg) {
				if(StringUtils.isBlank(lteamNo)) {
					fg = U.setPutFalse(map, "[登录车队编号]不能为空");
				}else {
					lteamNo = lteamNo.trim();
					
					U.log(log, "[登录车队编号] lteamNo="+lteamNo);
				}
			}
			
			if(fg) {
				if(StringUtils.isBlank(luname)) {
					fg = U.setPutFalse(map, "[登录用户名]不能为空");
				}else {
					luname = luname.trim();
					
					U.log(log, "[登录用户名] luname="+luname);
				}
			}
			
			// 保存-需要删除的文件id列表
			List<Object> delfids = new ArrayList<Object>();
			String oldJzImgUrls = "";
			if(fg && StringUtils.isNoneBlank(uid)) {// 修改才处理
				if(StringUtils.isBlank(fnames)) {
					U.log(log, "用户欲删除所有旧文件");
					
					// 获取需要删除的文件数据id数组字符串
					hql = "from FileMan where fdat = ?0 and ftype = ?1 order by id asc";
					List<FileMan> delfiles = fileManDao.findhqlList(hql, lteamNo+"="+luname+"="+uid, _ftype);
					for (FileMan fm : delfiles) {
						delfids.add(fm.getId());
					}
					
					U.log(log, "[需要删除"+delfiles.size()+"个"+_ftype.getKey()+"文件]");
				}else {
					U.log(log, "用户保留了一部分旧文件或者未删除旧文件");
					
					// 存储-未删除的文件url
					List<String> saveFmUrls = new ArrayList<String>();
					hql = "from FileMan where fname in(:v0) order by id asc";
					List<FileMan> savefiles = fileManDao.findListIns(hql, (Object[])fnames.split(","));
					for (FileMan fm : savefiles) {
						saveFmUrls.add(fm.getFolderName()+"/"+fm.getFname());
					}
					oldJzImgUrls = StringUtils.join(saveFmUrls.toArray(), ",");
					
					// 获取需要删除的文件数据id数组字符串
					hql = "from FileMan where fname not in(:v0) and fdat = :v1 and ftype = :v2 order by id asc";
					List<FileMan> delfiles = fileManDao.findListIns(hql, fnames.split(","), lteamNo+"="+luname+"="+uid, _ftype);
					for (FileMan fm : delfiles) {
						delfids.add(fm.getId());
					}
					
					U.log(log, "[需要删除"+delfiles.size()+"个"+_ftype.getKey()+"文件]");
				}
			}
			
			String jzImgUrls = "";
			if(fg) {
				if(StringUtils.isBlank(fnames) && files.length == 0) {
					fg = U.setPutFalse(map, "文件为空，请选择文件");
				}else {
					U u = new U();
					String pf = u.getClassPath();// 获取盘符
					
					// 存放文件根目录
					String rootPath = UtilFile.JZBX_FILE_PATH+UT.getYearMonthFolder(new Date());
					// 判断文件夹是否存在，不存在则创建
				    U.creatFolder(rootPath);					
					
				    List<String> fids = new ArrayList<String>();
				    List<String> urls = new ArrayList<String>();
				    
					for(int i = 0; i < files.length; i++) {
						MultipartFile file = files[i];
						
						// 创建新文件名并固定文件后缀名
						String fileName = U.getUUID()+".png";
						file.transferTo(new File(pf+":"+rootPath+"/"+fileName));
						
						// 添加上传数据记录
						FileMan fm = new FileMan();
						fm.setAtime(new Date());
						fm.setFid(UT.creatFileNum(fm.getAtime()));
						fm.setFname(fileName);
						fm.setFolderName(rootPath.replace(UtilFile.JZBX_FILE_PATH, "/jzbx"));
						fm.setReqsrc(reqsrc);
						fm.setFtype(_ftype);
						if(StringUtils.isNoneBlank(uid)) {
							fm.setFdat(lteamNo+"="+luname+"="+uid);// 车队编号,添加用户名,记账对id
						}
						fileManDao.save(fm);
						U.log(log, "添加-"+fm.getFtype().getKey()+"-成功");
						
						// 保存文件id
						fids.add(fm.getId()+"");
						
						// 保存图片地址
						urls.add(fm.getFolderName()+"/"+fm.getFname());
						
						U.log(log, "上传文件完成");
					}
					jzImgUrls = StringUtils.join(urls.toArray(), ",");
					
					// 将新添加的文件数据id传给前端
					map.put("fids", fids.size() > 0 ? StringUtils.join(fids.toArray(), ",") : "");
					
					U.setPut(map, 1, "上传文件成功");
				}
			}
			
			if(fg) {
				if(StringUtils.isNoneBlank(uid)) {
					U.log(log, "存在修改对象id，则需要更新对应类型的对象数据");
					
					if(!FV.isLong(uid)) {
						fg = U.setPutFalse(map, "[修改对象id]格斯错误");
					}else {
						// 处理最终保留的文件名数组字符串
						if(StringUtils.isBlank(jzImgUrls)) {
							// 不存在新添加的文件名数组字符串，则说明未新添加文件，即文件名数组字符串与原来一样
							jzImgUrls = oldJzImgUrls;
						}else {
							// 存在新添加的文件名数组字符串，则说明新添加了文件，即文件名数组字符串为原来的+新的
							if(StringUtils.isNotBlank(oldJzImgUrls)) {
								// 未全部删除旧文件，保留了一部分
								jzImgUrls = oldJzImgUrls+","+jzImgUrls;
							}
						}
						
						String dat = "";
						if(_ftype == FileType.JYJZ_IMG) {
							CarOilList jyjz = carOilListDao.findByField("id", Long.parseLong(uid));
							if(jyjz == null) {
								fg = U.logFalse(log, "[加油记账]不存在");
							}else {
								// 标识
								dat = jyjz.getUnitNum()+"="+jyjz.getOilDriver().getUname()+"="+jyjz.getId();
								if(fg) {
									// 修改加油记账记录的凭证url
									jyjz.setOilVoucherUrl(jzImgUrls);
									carOilListDao.update(jyjz);
									U.log(log, "更新-加油记账-凭证url-完成");
									
									
									// 对应员工记账
									StaffReimburse sr = staffReimburseDao.findByField("dat", dat);
									if(sr == null) {
										U.logFalse(log, "[对应员工记账]不存在");
									}else {
										sr.setReimVoucherUrl(jzImgUrls);
										staffReimburseDao.update(sr);
										U.log(log, "更新-员工记账-凭证url-完成");
										
										U.setPut(map, 1, "更新-凭证图片地址-完成");
									}
								}
							}
						}else if(_ftype == FileType.WXJZ_IMG) {
							CarRepairList wxjz = carRepairListDao.findByField("id", Long.parseLong(uid));
							if(wxjz == null) {
								fg = U.logFalse(log, "[维修记账]不存在");
							}else {
								// 标识
								dat = wxjz.getUnitNum()+"="+wxjz.getRepairDriver().getUname()+"="+wxjz.getId();
								if(fg) {
									// 修改维修记账记录的凭证url
									wxjz.setRepairVoucherUrl(jzImgUrls);
									carRepairListDao.update(wxjz);
									U.log(log, "更新-维修记账-凭证url-完成");
									
									// 对应员工记账
									StaffReimburse sr = staffReimburseDao.findByField("dat", dat);
									if(sr == null) {
										U.logFalse(log, "[对应员工记账]不存在");
									}else {
										sr.setReimVoucherUrl(jzImgUrls);
										staffReimburseDao.update(sr);
										U.log(log, "更新-员工记账-凭证url-完成");
										
										U.setPut(map, 1, "更新-凭证图片地址-完成");
									}
								}
							}
						}else if(_ftype == FileType.QTJZ_IMG) {
							StaffReimburse sr = staffReimburseDao.findByField("id", Long.parseLong(uid));
							if(sr == null) {
								fg = U.logFalse(log, "[对应员工记账]不存在");
							}else {
								if(fg) {
									sr.setReimVoucherUrl(jzImgUrls);
									staffReimburseDao.update(sr);
									U.log(log, "更新-员工记账-凭证url-完成");
									
									U.setPut(map, 1, "更新-凭证图片地址-完成");
								}
							}
						}else if(_ftype == FileType.XCSZ_IMG) {
							StaffReimburse sr = staffReimburseDao.findByField("id", Long.parseLong(uid));
							if(sr == null) {
								fg = U.logFalse(log, "[对应员工记账]不存在");
							}else {
								if(fg) {
									sr.setReimVoucherUrl(jzImgUrls);
									staffReimburseDao.update(sr);
									U.log(log, "更新-员工记账-凭证url-完成");
									
									// 修改对应行程收支
									RouteTradeList xcsz = sr.getOrderTrade();
									if(xcsz == null) {
										U.logFalse(log, "[行程收支]不存在");
									}else {
										// 修改加油记账记录的凭证url
										xcsz.setRouteVoucherUrl(jzImgUrls);
										routeTradeListDao.update(xcsz);
										U.log(log, "更新-行程收支-凭证url-完成");
									}
									
									U.setPut(map, 1, "更新-凭证图片地址-完成");
								}
							}
						}
						
						if(fg) {
							if(delfids.size() == 0) {
								U.log(log, "没有需要删除的文件");
							}else {
								// 删除加油记账对应凭证记录及图片
								fileManDao.delJzbxFiles(delfids.toArray());
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
