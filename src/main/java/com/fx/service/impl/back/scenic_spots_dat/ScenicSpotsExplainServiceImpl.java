package com.fx.service.impl.back.scenic_spots_dat;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.dao.back.scenic_spots_dat.ScenicSpotsExplainDao;
import com.fx.dao.back.scenic_spots_dat.ScenicSpotsPointDao;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsExplain;
import com.fx.entity.back.scenic_spots_dat.ScenicSpotsPoint;
import com.fx.entity.company.FileManage;
import com.fx.entity.cus.Customer;
import com.fx.service.back.scenic_spots_dat.ScenicSpotsExplainService;
import com.fx.service.company.FileService;

@Service
@Transactional
public class ScenicSpotsExplainServiceImpl extends BaseServiceImpl<ScenicSpotsExplain,Long> implements ScenicSpotsExplainService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 文件-服务 */
	@Autowired
	private FileService fileSer;
	/** 景点地点-数据源 */
	@Autowired
	private ScenicSpotsPointDao scenicSpotsPointDao;
	/** 景点说明-数据源 */
	@Autowired
	private ScenicSpotsExplainDao scenicSpotsExplainDao;
	@Override
	public ZBaseDaoImpl<ScenicSpotsExplain, Long> getDao() {
		return scenicSpotsExplainDao;
	}
	
	
	@Override
	public Map<String, Object> uploadScenicSpotsImg(ReqSrc reqsrc, HttpServletRequest request,
		HttpServletResponse response, MultipartHttpServletRequest freq, Customer lcus, 
		String filedName) {
		String logtxt = U.log(log, "上传-景点图片", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}
			
			if(fg){
				if(!freq.getFileNames().hasNext()){// 不存在文件
					fg = U.setPutFalse(map, "[图片]不能为空"); 
				}
			}
			
			if(fg) {
				map = fileSer.addFileMan(freq, UtilFile.JD_FILE_ROOT_DIR, lcus.getBaseUserId().getUname(), "3", "景点简介-图片", "scenicSpots-"+filedName);
				if(!"1".equals(map.get("code").toString())) {
					fg = U.setPutFalse(map, "上传图片失败");
				}else{
					@SuppressWarnings("unchecked")
					List<FileManage> fmlist = (List<FileManage>)map.get("fmlist");
					String src = fmlist.get(0).getfUrl()+"/"+fmlist.get(0).getfRelName();
					String title = fmlist.get(0).getfRemark();
					
					map.remove("fmlist");// 清除文件列表
					
					Map<String, String> dat = new HashMap<String, String>();
					dat.put("fid", fmlist.get(0).getId()+"");
					dat.put("src", src);
					dat.put("title", title);
					map.put("data", dat);
					
					U.setPut(map, 0, "上传图片成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> addScenicSpotsExplain(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String sspId, String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp) {
		String logtxt = U.log(log, "添加-景点说明", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
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
			if(fg) {
				if(StringUtils.isEmpty(sspId)) {
					fg = U.setPutFalse(map, "[景点地址对象id]不能为空");
				}else {
					sspId = sspId.trim();
					if(!FV.isLong(sspId)) {
						fg = U.setPutFalse(map, "[景点地址对象id]格式错误");
					}else {
						ssp = scenicSpotsPointDao.findByField("id", Long.parseLong(sspId));
						if(ssp == null) {
							fg = U.setPutFalse(map, "当前[景点]不存在");
						}if(ssp.getExplainId() != null) {
							fg = U.setPutFalse(map, "当前景点已存在景点说明，请勿重复添加");
						}
					}
					
					U.log(log, "[景点地址对象id] sspId="+sspId);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(openTimeExp)) {
					fg = U.setPutFalse(map, "[景点开放时间说明]不能为空");
				}else {
					openTimeExp = openTimeExp.trim();
					if(openTimeExp.length() > 100) {
						fg = U.setPutFalse(map, "[景点开放时间说明]最多100个字");
					}
					
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
			
			if(fg) {
				ScenicSpotsExplain o = new ScenicSpotsExplain();
				o.setOpenTimeExp(openTimeExp);
				o.setTicketExp(ticketExp);
				o.setNotice(notice);
				o.setOtherExp(otherExp);
				o.setDetailExp(detailExp);
				o.setAtime(new Date());
				// 张三-18888888888-添加=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-添加="+DateUtils.DateToStr(new Date())+";";
				o.setOperNote(operNote);
				scenicSpotsExplainDao.save(o);
				U.log(log, "添加-景点说明-完成");
				
				ssp.setExplainId(o);
				scenicSpotsPointDao.update(ssp);
				U.log(log, "修改-景点地址数据-景点详情-完成");
				
				U.setPut(map, 1, "添加成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updScenicSpotsExplain(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		Customer lcus, String sspId, String openTimeExp, String ticketExp, String notice, String otherExp, String detailExp) {
		String logtxt = U.log(log, "修改-景点说明", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(lcus == null) {
					fg = U.setPutFalse(map, "[登录用户]不能为空");
				}else {
					U.log(log, "[登录账号] luname="+lcus.getBaseUserId().getUname());
				}
			}
			
			ScenicSpotsExplain o = null;
			ScenicSpotsPoint ssp = null;
			if(fg) {
				if(StringUtils.isEmpty(sspId)) {
					fg = U.setPutFalse(map, "[景点地址对象id]不能为空");
				}else {
					sspId = sspId.trim();
					if(!FV.isLong(sspId)) {
						fg = U.setPutFalse(map, "[景点地址对象id]格式错误");
					}else {
						ssp = scenicSpotsPointDao.findByField("id", Long.parseLong(sspId));
						if(ssp == null) {
							fg = U.setPutFalse(map, "当前[景点]不存在");
						}else {
							if(ssp.getExplainId() == null) {
								fg = U.setPutFalse(map, "当前[景点说明]不存在");
							}else {
								o = scenicSpotsExplainDao.findByField("id", ssp.getExplainId().getId());
								if(o == null) {
									fg = U.setPutFalse(map, "获取[景点说明]失败");
								}
							}
						}
					}
					
					U.log(log, "[景点地址对象id] sspId="+sspId);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(openTimeExp)) {
					fg = U.setPutFalse(map, "[景点开放时间说明]不能为空");
				}else {
					openTimeExp = openTimeExp.trim();
					if(openTimeExp.length() > 100) {
						fg = U.setPutFalse(map, "[景点开放时间说明]最多100个字");
					}
					
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
			
			if(fg) {
				o.setOpenTimeExp(openTimeExp);
				o.setTicketExp(ticketExp);
				o.setNotice(notice);
				o.setOtherExp(otherExp);
				o.setDetailExp(detailExp);
				// 张三-18888888888-修改=2020-03-07 10:30:00; 
				String operNote = lcus.getBaseUserId().getRealName()+"-"+lcus.getBaseUserId().getUname()+"-修改="+DateUtils.DateToStr(new Date())+";";
				o.setOperNote(o.getOperNote()+operNote);
				scenicSpotsExplainDao.update(o);
				U.log(log, "修改-景点说明-完成");
				
				U.setPut(map, 1, "修改成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	
}
