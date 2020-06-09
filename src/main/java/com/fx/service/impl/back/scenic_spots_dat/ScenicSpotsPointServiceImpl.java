package com.fx.service.impl.back.scenic_spots_dat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.SortType;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.scenic_spots_dat.ScenicSpotsExplainDao;
import com.fx.dao.back.scenic_spots_dat.ScenicSpotsPointDao;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsExplain;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsPoint;
import com.fx.entity.company.FileManage;
import com.fx.entity.cus.Customer;
import com.fx.service.back.scenic_spots_dat.ScenicSpotsPointService;
import com.fx.service.company.FileService;

@Service
@Transactional
public class ScenicSpotsPointServiceImpl extends BaseServiceImpl<ScenicSpotsPoint,Long> implements ScenicSpotsPointService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 文件管理-服务 */
	@Autowired
	private FileService fileSer;
	/** 景点说明-数据源 */
	@Autowired
	private ScenicSpotsExplainDao scenicSpotsExplainDao;
	/** 景点地点-数据源 */
	@Autowired
	private ScenicSpotsPointDao scenicSpotsPointDao;
	@Override
	public ZBaseDaoImpl<ScenicSpotsPoint, Long> getDao() {
		return scenicSpotsPointDao;
	}
	
	
	@Override
	public Map<String, Object> addScenicSpotsPoint(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String city, String county, String lngLat, String mapAddr, String addrShort) {
		String logtxt = U.log(log, "添加-景点地点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(city)) {
					fg = U.setPutFalse(map, "[景点所属城市]不能为空");
				}else {
					city = city.trim();
					
					U.log(log, "[景点所属城市] city="+city);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(county)) {
					fg = U.setPutFalse(map, "[景点所属区/县]不能为空");
				}else {
					county = county.trim();
					
					U.log(log, "[景点所属区/县] county="+county);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lngLat)) {
					fg = U.setPutFalse(map, "[景点所在坐标]不能为空");
				}else {
					lngLat = lngLat.trim();
					if(lngLat.indexOf(",") == -1) {
						fg = U.setPutFalse(map, "[景点所在坐标]格式错误");
					}
					
					U.log(log, "[景点所在坐标] lngLat="+lngLat);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(mapAddr)) {
					fg = U.setPutFalse(map, "[景点在地址]不能为空");
				}else {
					mapAddr = mapAddr.trim();
					
					U.log(log, "[景点在地址] mapAddr="+mapAddr);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(addrShort)) {
					fg = U.setPutFalse(map, "[景点名称简称]不能为空");
				}else {
					addrShort = addrShort.trim();
					if(addrShort.length() > 5) {
						fg = U.setPutFalse(map, "[景点名称简称]最多4个字");
					}
					
					U.log(log, "[景点名称简称] addrShort="+addrShort);
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where mapAddr = ?";
				Object obj = scenicSpotsPointDao.findObj(hql, mapAddr);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在地址为["+mapAddr+"]的景点");
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where addrShort = ?";
				Object obj = scenicSpotsPointDao.findObj(hql, addrShort);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在简称为["+addrShort+"]的景点");
				}
			}
			
			if(fg) {
				ScenicSpotsPoint o = new ScenicSpotsPoint();
				o.setCity(city);
				o.setCounty(county);
				o.setLngLat(lngLat);
				o.setMapAddr(mapAddr);
				o.setAddrShort(addrShort);
				o.setExplainId(null);
				o.setAtime(new Date());
				// 张三-18888888888-添加=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-添加="+DateUtils.DateToStr(new Date())+";";
				o.setOperNote(operNote);
				scenicSpotsPointDao.save(o);
				
				map.put("id", o.getId());
				
				U.setPut(map, 1, "添加-景点地址数据-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updScenicSpotsPoint(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String id, String city, String county, String lngLat, String mapAddr, String addrShort) {
		String logtxt = U.log(log, "修改-景点地点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}
			
			ScenicSpotsPoint o = null;
			if(fg) {
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[修改景点id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[修改景点id]格式错误");
					}else {
						o = scenicSpotsPointDao.findByField("id", Long.parseLong(id));
						if(o == null) {
							fg = U.setPutFalse(map, "当前[修改景点]不存在");
						}
					}
					
					U.log(log, "[修改景点id] id="+id);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(city)) {
					fg = U.setPutFalse(map, "[景点所属城市]不能为空");
				}else {
					city = city.trim();
					
					U.log(log, "[景点所属城市] city="+city);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(county)) {
					fg = U.setPutFalse(map, "[景点所属区/县]不能为空");
				}else {
					county = county.trim();
					
					U.log(log, "[景点所属区/县] county="+county);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lngLat)) {
					fg = U.setPutFalse(map, "[景点所在坐标]不能为空");
				}else {
					lngLat = lngLat.trim();
					if(lngLat.indexOf(",") == -1) {
						fg = U.setPutFalse(map, "[景点所在坐标]格式错误");
					}
					
					U.log(log, "[景点所在坐标] lngLat="+lngLat);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(mapAddr)) {
					fg = U.setPutFalse(map, "[景点在地址]不能为空");
				}else {
					mapAddr = mapAddr.trim();
					
					U.log(log, "[景点在地址] mapAddr="+mapAddr);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(addrShort)) {
					fg = U.setPutFalse(map, "[景点名称简称]不能为空");
				}else {
					addrShort = addrShort.trim();
					if(addrShort.length() > 5) {
						fg = U.setPutFalse(map, "[景点名称简称]最多4个字");
					}
					
					U.log(log, "[景点名称简称] addrShort="+addrShort);
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where id <> ? and (mapAddr = ? or addrShort = ?)";
				Object obj = scenicSpotsPointDao.findObj(hql, o.getId(), mapAddr, addrShort);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在地址为["+mapAddr+"]或者简称为["+addrShort+"]的景点");
				}
			}
			
			if(fg) {
				o.setCity(city);
				o.setCounty(county);
				o.setLngLat(lngLat);
				o.setMapAddr(mapAddr);
				o.setAddrShort(addrShort);
				// 张三-18888888888-修改=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-修改="+DateUtils.DateToStr(new Date())+";";
				o.setOperNote(o.getOperNote()+operNote);
				scenicSpotsPointDao.update(o);
				
				map.put("id", o.getId());
				
				U.setPut(map, 1, "修改-景点地址数据-完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> addScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String city, String county, String lngLat, String mapAddr, String addrShort, String openTimeExp, 
		String ticketExp, String notice, String otherExp, String detailExp) {
		String logtxt = U.log(log, "添加-景点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(city)) {
					fg = U.setPutFalse(map, "[景点所属城市]不能为空");
				}else {
					city = city.trim();
					
					U.log(log, "[景点所属城市] city="+city);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(county)) {
					fg = U.setPutFalse(map, "[景点所属区/县]不能为空");
				}else {
					county = county.trim();
					
					U.log(log, "[景点所属区/县] county="+county);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lngLat)) {
					fg = U.setPutFalse(map, "[景点所在坐标]不能为空");
				}else {
					lngLat = lngLat.trim();
					if(lngLat.indexOf(",") == -1) {
						fg = U.setPutFalse(map, "[景点所在坐标]格式错误");
					}
					
					U.log(log, "[景点所在坐标] lngLat="+lngLat);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(mapAddr)) {
					fg = U.setPutFalse(map, "[景点在地址]不能为空");
				}else {
					mapAddr = mapAddr.trim();
					
					U.log(log, "[景点在地址] mapAddr="+mapAddr);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(addrShort)) {
					fg = U.setPutFalse(map, "[景点名称简称]不能为空");
				}else {
					addrShort = addrShort.trim();
					if(addrShort.length() > 5) {
						fg = U.setPutFalse(map, "[景点名称简称]最多4个字");
					}
					
					U.log(log, "[景点名称简称] addrShort="+addrShort);
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where mapAddr = ?";
				Object obj = scenicSpotsPointDao.findObj(hql, mapAddr);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在地址为["+mapAddr+"]的景点");
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where addrShort = ?";
				Object obj = scenicSpotsPointDao.findObj(hql, addrShort);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在简称为["+addrShort+"]的景点");
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(openTimeExp)) {
					fg = U.setPutFalse(map, "[景点开放时间说明]不能为空");
				}else {
					openTimeExp = openTimeExp.trim();
//					if(openTimeExp.length() > 100) {
//						fg = U.setPutFalse(map, "[景点开放时间说明]最多100个字");
//					}
					
					U.log(log, "[景点开放时间说明] openTimeExp="+openTimeExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(ticketExp)) {
					fg = U.setPutFalse(map, "[景点门票说明]不能为空");
				}else {
					ticketExp = ticketExp.trim();
					
					U.log(log, "[景点门票说明] ticketExp="+ticketExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(notice)) {
					fg = U.setPutFalse(map, "[景点公告]不能为空");
				}else {
					notice = notice.trim();
					
					U.log(log, "[景点公告] notice="+notice);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(otherExp)) {
					fg = U.setPutFalse(map, "[景点其他说明]不能为空");
				}else {
					otherExp = otherExp.trim();
					
					U.log(log, "[景点其他说明] otherExp="+otherExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(detailExp)) {
					fg = U.setPutFalse(map, "[景点详情说明]不能为空");
				}else {
					detailExp = detailExp.trim();
					
					U.log(log, "[景点详情说明] detailExp="+detailExp);
				}
			}
			
			List<Object> fidlist = new ArrayList<Object>();
			if(fg) {
				if(StringUtils.isNotBlank(detailExp)) {
					List<String> imgSrcs = Util.getImgStr(detailExp);
					
					for (String str : imgSrcs) {
						if(str.indexOf(UtilFile.JD_FILE_ROOT_DIR) != -1) {
							String fid = str.substring(str.lastIndexOf("_")+1, str.lastIndexOf("."));
							if(!fidlist.contains(fid)) {// 不包含
								fidlist.add(Long.parseLong(fid));
							}
						}
					}
				}
			}
			
			if(fg) {
				Date currTime = new Date();
				// 张三-18888888888-添加=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-添加="+DateUtils.DateToStr(currTime)+";";
				
				// 先添加[景点说明]数据
				ScenicSpotsExplain sse = new ScenicSpotsExplain();
				sse.setOpenTimeExp(openTimeExp);
				sse.setTicketExp(ticketExp);
				sse.setNotice(notice);
				sse.setOtherExp(otherExp);
				sse.setDetailExp(detailExp);
				sse.setAtime(currTime);
				sse.setOperNote(operNote);
				scenicSpotsExplainDao.save(sse);
				U.log(log, "添加-景点说明-完成");
				
				// 再添加[景点地址]数据
				ScenicSpotsPoint ssp = new ScenicSpotsPoint();
				ssp.setCity(city);
				ssp.setCounty(county);
				ssp.setLngLat(lngLat);
				ssp.setMapAddr(mapAddr);
				ssp.setAddrShort(addrShort);
				ssp.setExplainId(sse);
				ssp.setAtime(sse.getAtime());
				ssp.setOperNote(operNote);
				scenicSpotsPointDao.save(ssp);
				U.log(log, "添加-景点地址-完成");
				
				if(fg) {
					if(fidlist.size() == 0) {
						U.log(log, "[不存在]需要更新状态的文件对象");
					}else {
						U.log(log, "[存在]需要更新状态的文件对象");
						
						// 只更新当前用户刚刚临时上传的文件数据
						hql = "update FileManage set status = :v0,dataId = :v1 where id in(:v2) and fCName = :v3 and filedName like :v4 and status = :v5";
						int count1 = fileSer.batchExecuteIns(hql, 1, ssp.getId()+"", fidlist.toArray(), lcus.getBaseUserId().getUname(), "scenicSpots-%", 0);
						U.log(log, "更新景点图片文件"+count1+"个");
					}
					
					// 删除-不需要保存的图片
					hql = "from FileManage where fCName = ? and filedName like ? and status = ? order by fUpTime asc";
					List<FileManage> fmlist = fileSer.findhqlList(hql, lcus.getBaseUserId().getUname(), "scenicSpots-%", 0);
					fileSer.delFileList(fmlist, null);
					U.log(log, "删除-景点图片-完成");
				}
				
				U.setPut(map, 1, "添加完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String id, String city, String county, String lngLat, String mapAddr, String addrShort, 
		String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp) {
		String logtxt = U.log(log, "修改-景点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}

			ScenicSpotsPoint ssp = null;
			ScenicSpotsExplain sse = null;
			if(fg) {
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[景点地址对象id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[景点地址对象id]格式错误");
					}else {
						ssp = scenicSpotsPointDao.findByField("id", Long.parseLong(id));
						if(ssp == null) {
							fg = U.setPutFalse(map, "当前[景点]不存在");
						}else {
							if(ssp.getExplainId() == null) {
								fg = U.setPutFalse(map, "当前[景点说明]不存在");
							}else {
								sse = scenicSpotsExplainDao.findByField("id", ssp.getExplainId().getId());
								if(sse == null) {
									fg = U.setPutFalse(map, "获取[景点说明]失败");
								}
							}
						}
					}
					
					U.log(log, "[景点地址对象id] id="+id);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(city)) {
					fg = U.setPutFalse(map, "[景点所属城市]不能为空");
				}else {
					city = city.trim();
					
					U.log(log, "[景点所属城市] city="+city);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(county)) {
					fg = U.setPutFalse(map, "[景点所属区/县]不能为空");
				}else {
					county = county.trim();
					
					U.log(log, "[景点所属区/县] county="+county);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(lngLat)) {
					fg = U.setPutFalse(map, "[景点所在坐标]不能为空");
				}else {
					lngLat = lngLat.trim();
					if(lngLat.indexOf(",") == -1) {
						fg = U.setPutFalse(map, "[景点所在坐标]格式错误");
					}
					
					U.log(log, "[景点所在坐标] lngLat="+lngLat);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(mapAddr)) {
					fg = U.setPutFalse(map, "[景点在地址]不能为空");
				}else {
					mapAddr = mapAddr.trim();
					
					U.log(log, "[景点在地址] mapAddr="+mapAddr);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(addrShort)) {
					fg = U.setPutFalse(map, "[景点名称简称]不能为空");
				}else {
					addrShort = addrShort.trim();
					if(addrShort.length() > 5) {
						fg = U.setPutFalse(map, "[景点名称简称]最多4个字");
					}
					
					U.log(log, "[景点名称简称] addrShort="+addrShort);
				}
			}
			
			if(fg) {
				hql = "select count(id) from ScenicSpotsPoint where id <> ? and (mapAddr = ? or addrShort = ?)";
				Object obj = scenicSpotsPointDao.findObj(hql, ssp.getId(), mapAddr, addrShort);
				if(obj != null && Integer.parseInt(obj.toString()) > 0) {
					fg = U.setPutFalse(map, "已存在地址为["+mapAddr+"]或者简称为["+addrShort+"]的景点");
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(openTimeExp)) {
					fg = U.setPutFalse(map, "[景点开放时间说明]不能为空");
				}else {
					openTimeExp = openTimeExp.trim();
//					if(openTimeExp.length() > 100) {
//						fg = U.setPutFalse(map, "[景点开放时间说明]最多100个字");
//					}
					
					U.log(log, "[景点开放时间说明] openTimeExp="+openTimeExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(ticketExp)) {
					fg = U.setPutFalse(map, "[景点门票说明]不能为空");
				}else {
					ticketExp = ticketExp.trim();
					
					U.log(log, "[景点门票说明] ticketExp="+ticketExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(notice)) {
					fg = U.setPutFalse(map, "[景点公告]不能为空");
				}else {
					notice = notice.trim();
					
					U.log(log, "[景点公告] notice="+notice);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(otherExp)) {
					fg = U.setPutFalse(map, "[景点其他说明]不能为空");
				}else {
					otherExp = otherExp.trim();
					
					U.log(log, "[景点其他说明] otherExp="+otherExp);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(detailExp)) {
					fg = U.setPutFalse(map, "[景点详情说明]不能为空");
				}else {
					detailExp = detailExp.trim();
					
					U.log(log, "[景点详情说明] detailExp="+detailExp);
				}
			}
			
			List<Object> fidlist = new ArrayList<Object>();
			if(fg) {
				if(StringUtils.isNotBlank(detailExp)) {
					List<String> imgSrcs = Util.getImgStr(detailExp);
					
					for (String str : imgSrcs) {
						if(str.indexOf(UtilFile.JD_FILE_ROOT_DIR) != -1) {
							String fid = str.substring(str.lastIndexOf("_")+1, str.lastIndexOf("."));
							if(!fidlist.contains(fid)) {// 不包含
								fidlist.add(Long.parseLong(fid));
							}
						}
					}
				}
			}
			
			if(fg) {
				Date currTime = new Date();
				// 张三-18888888888-修改=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-修改="+DateUtils.DateToStr(currTime)+";";
				
				// 修改[景点地址]数据
				ssp.setCity(city);
				ssp.setCounty(county);
				ssp.setLngLat(lngLat);
				ssp.setMapAddr(mapAddr);
				ssp.setAddrShort(addrShort);
				ssp.setOperNote(ssp.getOperNote()+operNote);
				scenicSpotsPointDao.update(ssp);
				U.log(log, "修改-景点地址-完成");
				
				// 修改[景点说明]数据
				sse.setOpenTimeExp(openTimeExp);
				sse.setTicketExp(ticketExp);
				sse.setNotice(notice);
				sse.setOtherExp(otherExp);
				sse.setDetailExp(detailExp);
				sse.setOperNote(sse.getOperNote()+operNote);
				scenicSpotsExplainDao.update(sse);
				U.log(log, "修改-景点说明-完成");
				
				if(fg) {
					if(fidlist.size() == 0) {
						U.log(log, "[不存在]需要更新状态的文件对象");
					}else {
						U.log(log, "[存在]需要更新状态的文件对象");
						
						// 只更新当前用户刚刚临时上传的文件数据
						hql = "update FileManage set status = :v0,dataId = :v1 where id in(:v2) and fCName = :v3 and filedName like :v4 and status = :v5";
						int count1 = fileSer.batchExecuteIns(hql, 1, ssp.getId()+"", fidlist.toArray(), lcus.getBaseUserId().getUname(), "scenicSpots-%", 0);
						U.log(log, "更新文件"+count1+"个");
					}
					
					List<FileManage> fmlist = new ArrayList<FileManage>();
					if(fidlist.size() > 0) {
						// 删除-不需要保存的图片（或者之前添加的不需要的）
						hql = "from FileManage where (dataId = :v0 and id not in(:v1)) or (fCName = :v2 and filedName like :v3 and status = :v4) order by fUpTime asc";
						fmlist = fileSer.findListIns(hql, ssp.getId()+"", fidlist.toArray(), lcus.getBaseUserId().getUname(), "scenicSpots-%", 0);
					}else {
						// 删除-不需要保存的图片（或者之前添加的不需要的）
						hql = "from FileManage where dataId = :v0 or (fCName = :v1 and filedName like :v2 and status = :v3) order by fUpTime asc";
						fmlist = fileSer.findListIns(hql, ssp.getId()+"", lcus.getBaseUserId().getUname(), "scenicSpots-%", 0);
					}
					
					fileSer.delFileList(fmlist, null);
					U.log(log, "删除-景点图片-完成");
				}
				
				U.setPut(map, 1, "修改成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findScenicSpotsList(ReqSrc reqsrc, String page, String rows, String cityName, String countyName, 
		String find) {
		String logtxt = U.log(log, "获取-景点数据-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "景点");
			/*****参数--验证--end******/
			
			if(fg) {
				if(StringUtils.isEmpty(cityName)) {
					U.log(log, "[城市名称]为空");
				}else {
					cityName = cityName.trim();
					
					U.log(log, "[城市名称] cityName="+cityName);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(countyName)) {
					U.log(log, "[城市区/县名称]为空");
				}else {
					countyName = countyName.trim();
					
					U.log(log, "[城市区/县名称] countyName="+countyName);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				}else {
					find = find.trim();
					
					U.log(log, "[查询关键字] find="+find);
				}
			}
			
			if(fg){
				Page<ScenicSpotsPoint> pd = scenicSpotsPointDao.findScenicSpotsPointList(reqsrc, 
					page, rows, cityName, countyName, find);
				U.setPageData(map, pd);
				
				// 字段过滤
				
				U.setPut(map, 0, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String id) {
		String logtxt = U.log(log, "获取-景点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			ScenicSpotsPoint o = null;
			if(fg) {
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[景点数据id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[景点数据id]格式错误");
					}else {
						o = scenicSpotsPointDao.findByField("id", Long.parseLong(id));
						if(o == null) {
							fg = U.setPutFalse(map, "当前[景点数据]不存在");
						}
					}
					
					U.log(log, "[景点数据id] id="+id);
				}
			}
			
			if(fg) {
				map.put("data", o);
				
				U.setPut(map, 1, "获取景点数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delScenicSpotsDat(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String id) {
		String logtxt = U.log(log, "删除-景点数据", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			ScenicSpotsPoint o = null;
			if(fg) {
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[景点数据id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[景点数据id]格式错误");
					}else {
						o = scenicSpotsPointDao.findByField("id", Long.parseLong(id));
						if(o == null) {
							fg = U.setPutFalse(map, "当前[景点数据]不存在");
						}
					}
					
					U.log(log, "[景点数据id] id="+id);
				}
			}
			
			if(fg) {
				long sspId = o.getId();// 保存景点地点id
				long sseId = o.getExplainId() == null ? 0 : o.getExplainId().getId();// 保存景点说明id
				
				scenicSpotsPointDao.delete(o);
				U.log(log, "删除-景点地点数据-完成");
				
				if(sseId == 0){
					U.log(log, "[景点说明]已不存在");
				}else {
					ScenicSpotsExplain sse = scenicSpotsExplainDao.findByField("id", sseId);
					if(sse == null) {
						U.log(log, "景点说明不存在");
					}else {
						scenicSpotsExplainDao.delete(sse);
						U.log(log, "删除-景点说明数据-完成");
						
						// 删除图片
						hql = "delete from FileManage where filedName = ?";
						int count = fileSer.batchExecute(hql, "scenicSpots-detailExp-"+sspId);
						U.log(log, "删除图片"+count+"张");
					}
				}
				
				U.setPut(map, 1, "删除景点数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updScenicSpotsSort(ReqSrc reqsrc, String id, String sortType, String id2) {
		String logtxt = U.log(log, "更新-景点排序", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			ScenicSpotsPoint o = null;
			if(fg) {
				if(StringUtils.isEmpty(id)) {
					fg = U.setPutFalse(map, "[景点数据id]不能为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.setPutFalse(map, "[景点数据id]格式错误");
					}else {
						o = scenicSpotsPointDao.findByField("id", Long.parseLong(id));
						if(o == null) {
							fg = U.setPutFalse(map, "当前[景点数据]不存在");
						}
					}
					
					U.log(log, "[景点数据id] id="+id);
				}
			}
			
			SortType _sortType = null;
			if(fg) {
				if(StringUtils.isEmpty(sortType)) {
					fg = U.setPutFalse(map, "[排序类型]不能为空");
				}else {
					sortType = sortType.trim();
					if(!FV.isOfEnum(SortType.class, sortType)) {
						fg = U.setPutFalse(map, "[排序类型]格式错误");
					}else {
						_sortType = SortType.valueOf(sortType);
					}
					
					U.log(log, "[排序类型] sortType="+sortType);
				}
			}
			
			if(fg) {
				List<ScenicSpotsPoint> list = new ArrayList<ScenicSpotsPoint>();
				
				if(_sortType == SortType.INIT) {
					// 获取当前景点所属城市下的所有景点
					hql = "from ScenicSpotsPoint where city = ? order by atime asc";
					list = scenicSpotsPointDao.findhqlList(hql, o.getCity());
					
					if(list.size() <= 0) {
						U.setPut(map, 0, "["+o.getCity()+"]并没有景点数据");
					}else {
						for(int i = 0; i < list.size(); i++) {
							list.get(i).setSortNo(i+1);
							scenicSpotsPointDao.update(list.get(i));
						}
						
						U.setPut(map, 1, "初始化["+o.getCity()+"]的景点排序编号完成");
					}
				}else {
					// 交换序号的两个景点对象
					ScenicSpotsPoint ssp1 = null, ssp2 = null;
					
					if(_sortType == SortType.EXCH){// 两个景点交换序号
						ssp1 = o;
						
						if(fg) {
							if(StringUtils.isEmpty(id2)) {
								fg = U.setPutFalse(map, "[交换景点id]不能为空");
							}else {
								id2 = id2.trim();
								if(!FV.isLong(id2)) {
									fg = U.setPutFalse(map, "[交换景点id]格式错误");
								}else {
									ssp2 = scenicSpotsPointDao.findByField("id", Long.parseLong(id2));
									if(ssp2 == null) {
										fg = U.setPutFalse(map, "[交换景点]不存在，请刷新再试");
									}
								}
								
								U.log(log, "[交换景点id] id2="+id2);
							}
						}
					}else {// 向上/向下移动序号
						int sindex = -1, eindex = -1;
						if(fg) {
							// 获取当前景点所属城市下的所有景点
							hql = "from ScenicSpotsPoint where city = ? order by sortNo asc";
							list = scenicSpotsPointDao.findhqlList(hql, o.getCity());
							
							if(list.size() <= 0) {
								fg = U.setPutFalse(map, 0, "["+o.getCity()+"]并没有景点数据");
							}else if(list.size() == 1){
								fg = U.setPutFalse(map, 0, "["+o.getCity()+"]只有1条景点数据，无需排序");
							}else{
								for(int i = 0; i < list.size(); i++) {
									if(list.get(i).getId() == o.getId()) {// id一样
										sindex = i;
										break;
									}
								}
								
								if(sindex == -1) {
									fg = U.setPutFalse(map, "当前景点已不存在，请刷新再试");
								}
							}
						}
						
						if(fg) {
							if(_sortType == SortType.UP) {
								if(sindex - 1 >= 0) {
									U.log(log, "存在[上一个序]号数据，则上一个数据为交换景点");
									eindex = sindex - 1;
								}else {
									U.log(log, "不存在[上一个序号]数据，则最后一个数据为交换景点");
									eindex = list.size() - 1;
								}
							}else if(_sortType == SortType.DOWN) {
								if(sindex + 1 <= list.size() - 1) {
									U.log(log, "存在[下一个序]号数据，则下一个数据为交换景点");
									eindex = sindex + 1;
								}else {
									U.log(log, "不存在[下一个序号]数据，则第一个数据为交换景点");
									eindex = 0;
								}
							}
							
							ssp1 = list.get(sindex); 
							ssp2 = list.get(eindex);
							
							U.log(log, "两个交换景点分别是：ssp1="+ssp1.getAddrShort()+", ssp2="+ssp2.getAddrShort());
						}
					}
					
					if(fg) {
						if(ssp1.getSortNo() == ssp2.getSortNo()) {
							fg = U.setPutFalse(map, 0, "请先初始化排序["+o.getCity()+"]的景点");
						}else {
							int no1 = ssp1.getSortNo(), no2 = ssp2.getSortNo();
							
							ssp1.setSortNo(no2);
							scenicSpotsPointDao.update(ssp1);
							
							ssp2.setSortNo(no1);
							scenicSpotsPointDao.update(ssp1);
							
							U.setPut(map, 1, "更新景点排序号完成");
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
	
	@Override
	public Map<String, Object> findJdCityOfCountyList(String cityName) {
		String logtxt = U.log(log, "获取-景点城市区/县-列表");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(cityName)) {
					fg = U.setPutFalse(map, "[城市名称]不能为空");
				}else {
					cityName = cityName.trim();
					
					U.log(log, "[城市名称] cityName="+cityName);
				}
			}
			
			if(fg) {
				StringBuilder sb = new StringBuilder("from ScenicSpotsPoint where 1 = 1 ");
				List<Object> ps = new ArrayList<Object>();
				
				sb.append("and city like ?0 ");
				ps.add("%-"+cityName);
				
				sb.append("group by county ");
				
				sb.append("order by sortNo asc");
				List<ScenicSpotsPoint> list = scenicSpotsPointDao.findhqlList(sb.toString(), ps.toArray());
				
				map.put("data", list);
				
				// 字段过滤
				
				U.setPut(map, 1, "获取列表成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> findCountyJdList(String cityName, String countyName) {
		String logtxt = U.log(log, "获取-区/县景点-列表");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(cityName)) {
					fg = U.setPutFalse(map, "[城市名称]不能为空");
				}else {
					cityName = cityName.trim();
					
					U.log(log, "[城市名称] cityName="+cityName);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(countyName)) {
					fg = U.setPutFalse(map, "[区/县名称]不能为空");
				}else {
					countyName = countyName.trim();
					
					U.log(log, "[区/县名称] countyName="+countyName);
				}
			}
			
			if(fg) {
				StringBuilder sb = new StringBuilder("from ScenicSpotsPoint where 1 = 1 ");
				List<Object> ps = new ArrayList<Object>();
				
				sb.append("and city like ?0 ");
				ps.add("%-"+cityName);
				
				sb.append("and county = ?1 ");
				ps.add(countyName);
				
				sb.append("order by atime asc");
				List<ScenicSpotsPoint> list = scenicSpotsPointDao.findhqlList(sb.toString(), ps.toArray());
				
				map.put("data", list);
				
				// 字段过滤
				
				U.setPut(map, 1, "获取列表成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
