package com.fx.service.impl.finance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.other.excel.POIUtils;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.dao.finance.EtcListDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.finance.EtcList;
import com.fx.service.finance.EtcListService;
import com.fx.service.order.CarOrderService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class EtcListServiceImpl extends BaseServiceImpl<EtcList,Long> implements EtcListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
    private RedisUtil redis;
	@Autowired
	private EtcListDao etcDao;
	@Autowired
	private CarOrderService coSer;
	@Override
	public ZBaseDaoImpl<EtcList, Long> getDao() {
		return etcDao;
	}
	@Override
	public Map<String, Object> findEtcList(ReqSrc reqsrc,String page, String rows,String unitNum, 
			String orderNum, String sTime, String eTime,
			String plateNum,String driverName,String cardNo,String operMark) {
		String logtxt = U.log(log, "获取-etc记录-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "单位ETC记录");
			/*****参数--验证--end******/
			if(fg){
				Page<EtcList> pd = etcDao.findEtcList(reqsrc, page, rows, unitNum, orderNum, sTime, eTime, plateNum, driverName, cardNo, operMark);
				U.setPageData(map, pd);
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public Map<String, Object> importFeeEtc(ReqSrc reqsrc,HttpServletRequest request,MultipartFile file,String cardNo) {
		String logtxt = U.log(log, "单位-etc导入");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,
						file.getOriginalFilename().length());
				if(file.isEmpty() || suffix==null){
					fg=U.setPutFalse(map, "文件为空，请检查");
				}
				if(fg){
					if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
						fg=U.setPutFalse(map, "上传excel文件后缀必须是xls或者xlsx");
					}
				}
				if(fg){
					// 文件保存路径
					SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
					String filePath=UtilFile.COMPANYEXCEL+ "/" + dfs.format(new Date())+file.getOriginalFilename();
					U.creatFolder(UtilFile.COMPANYEXCEL);
					//String filePath = request.getSession().getServletContext().getRealPath("/")+ "contractModel\\"+dfs.format(new Date())+file.getOriginalFilename();
					// 转存文件
					file.transferTo(new File(filePath));
					POIUtils poiUtils = new POIUtils(); //POI读写Excel
					Map<String, Object> order = new HashMap<String, Object>();
					// 路径 文件名 工作薄下标 开始行数
					List<List<String>> list = poiUtils.getExcelData(new File(filePath), "etcRecord."+suffix, 0, 1);
					//循环遍历从excel中获取的数据，进行计算
					EtcList etc=null;
					List<String> feerow=null;
					for (int i = 0; i < list.size(); i++) {
						feerow = (List<String>) list.get(i);
						if(StringUtils.isNotBlank(feerow.get(8).toString())){
							if(!feerow.get(8).toString().trim().equals(cardNo)){
								fg=U.setPutFalse(map, "导入卡号和选择卡号不匹配【请确认卡号是否正确】");
							}
						}else{
							fg=U.setPutFalse(map, "文档中有空卡号【请确认卡号是否正确】");
						}
					}
					if(fg){
						String hql="from EtcList where plateNum=? and interStationTime=? and outStationTime=? and teamNo=?";
						for (int i = 0; i < list.size(); i++) {
							feerow = (List<String>) list.get(i); //获取每行数据，格式：wlx1,1000,2012-12-12 12:12:12（时间可以不填写）
							etc=etcDao.findObj(hql, feerow.get(1),DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(5)),
									DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(7)),LU.getLUnitNum(request, redis));
							if(etc==null){
								etc = new EtcList(); //创建导入数据对象
								etc.setUnitNum(LU.getLUnitNum(request, redis));
								etc.setCardNo(feerow.get(8));
								etc.setCardMan("");//20200219持卡人换成扣款卡号
								etc.setInterStationName(feerow.get(4));
							 	if(StringUtils.isNotEmpty(feerow.get(5))){ //如果用户有填写时间，则用填写时间，否则用当前时间
							 		etc.setInterStationTime(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(5)));
							 	}else{
							 		etc.setInterStationTime(null);
							 	}
							 	etc.setOutStationName(feerow.get(6));
							 	if(StringUtils.isNotEmpty(feerow.get(7))){ //如果用户有填写时间，则用填写时间，否则用当前时间
							 		etc.setOutStationTime(DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(7)));
							 	}else{
							 		etc.setOutStationTime(null);
							 	}
							 	etc.setEtcMoney(Double.valueOf(feerow.get(3)));
								etc.setPlateNum(feerow.get(1));
								if(StringUtils.isNotEmpty(feerow.get(5)) && 
										StringUtils.isNotEmpty(feerow.get(7)) && StringUtils.isNotEmpty(feerow.get(1))){
									order=coSer.findOrderByPlateTime(reqsrc, request, feerow.get(1), feerow.get(5), feerow.get(7));
									if(order.get("orderNum")!=null)etc.setOrderNum(order.get("orderNum").toString());
									if(order.get("driver")!=null)etc.setEtcDriver((BaseUser)order.get("driver"));
									if(order.get("routeDetail")!=null)etc.setRouteDetail(order.get("routeDetail").toString());
								}
								etc.setAddTime(new Date());
							 	etcDao.save(etc);
							}
						}
						U.setPut(map, 1, "导入成功");
					}
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> delEtc(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String delId) {
		String logtxt = U.log(log, "删除ETC记录", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				EtcList etc=null;
				if(fg){
					if(StringUtils.isEmpty(delId)){
						fg = U.setPutFalse(map, "[删除id]不能为空");
					}else {
						etc=etcDao.findByField("id", Long.valueOf(delId));
						if(etc==null) {
							fg = U.setPutFalse(map, "记录不存在");
						}else {
							if(etc.getIsCheck()==1) {
								fg = U.setPutFalse(map, "记录已审核，不能删除");
							}
						}
					}
				}
				if(fg){
					etcDao.delete(etc);
					U.setPut(map, 1, "操作成功");
				}
			}else{
				U.setPut(map, 0, QC.ERRORS_MSG);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
}
