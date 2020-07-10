package com.fx.service.impl.finance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.OrderPayStatus;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.Util;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.other.excel.ExportUtil;
import com.fx.commons.utils.other.excel.POIUtils;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.finance.BankListDao;
import com.fx.dao.finance.BankTradeListDao;
import com.fx.dao.finance.FeeCourseDao;
import com.fx.dao.finance.MoneyTypeDao;
import com.fx.dao.finance.ReimburseListDao;
import com.fx.dao.order.MainCarOrderDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.Staff;
import com.fx.entity.finance.BankList;
import com.fx.entity.finance.BankTradeList;
import com.fx.entity.finance.FeeCourse;
import com.fx.entity.finance.FeeCourseTrade;
import com.fx.entity.finance.MoneyType;
import com.fx.entity.finance.ReimburseList;
import com.fx.entity.order.MainCarOrder;
import com.fx.service.finance.BankTradeListService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class BankTradeListServiceImpl extends BaseServiceImpl<BankTradeList,Long> implements BankTradeListService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
    private RedisUtil redis;
	@Override
	public ZBaseDaoImpl<BankTradeList, Long> getDao() {
		return btlDao;
	}
	@Autowired
	private BankTradeListDao btlDao;
	/**凭证服务**/
	@Autowired
	private ReimburseListDao reimDao;
	/**银行服务**/
	@Autowired
	private BankListDao blDao;
	/**科目服务**/
	@Autowired
	private FeeCourseDao fcDao;
	/**主订单服务**/
	@Autowired
	private MainCarOrderDao mcoDao;
	/**单位客户服务**/
	@Autowired
	private CompanyCustomDao ccDao;
	/**金额类型服务**/
	@Autowired
	private MoneyTypeDao mtDao;
	/**员工服务**/
	@Autowired
	private StaffDao staffDao;
	
	@Override
	public Map<String, Object> findBankTradeList(ReqSrc reqsrc, String page, String rows,String unitNum,String bankNo,String transName,
			String remark,String timeType,String sTime,String eTime,String status,String isReim,String findMoney,
			String openRole,String voucherNum,String operMark,String openSel,String moneyType,String cusName,String serviceName) {
		String logtxt = U.log(log, "获取-银行账-分页列表", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			/*****参数--验证--begin*****/
			if(fg) fg = U.valPageNo(map, page, rows, "银行账");
			/*****参数--验证--end******/
			if(fg){
				Page<BankTradeList> pd = btlDao.findBankTradeList(reqsrc, page, rows, unitNum, bankNo, transName, remark, timeType, sTime, eTime, status, isReim, 
						findMoney, openRole, voucherNum, operMark, openSel, moneyType, cusName, serviceName);
				double totalGath=0;//总收入
				double totalPay=0;//总支出
				// 解决懒加载问题
				for (BankTradeList btl : pd.getResult()) {
					totalGath+=btl.getTradeInMoney();
					totalPay+=btl.getTradeOutMoney();
				}
				
				U.setPageData(map, pd);
				
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(FeeCourse.class), new String[]{});
				map.put(QC.FIT_FIELDS, fmap);
				map.put("totalGath", totalGath);
				map.put("totalPay", totalPay);
				map.put("balance", MathUtils.sub(totalGath, totalPay, 2));
				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	@Override
	public Map<String, Object> lockBankTrade(HttpServletRequest request,ReqSrc reqsrc, String btlId,String isLock) {
		String logtxt = U.log(log, ("0".equals(isLock))?"解锁":"锁定"+"-银行账(已取消)", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){ //后台
				if(fg){
					if(StringUtils.isEmpty(btlId)){
						fg = U.setPutFalse(map, "[锁定id]不能为空");
					}else{
						btlId = btlId.trim();
						U.log(log, "银行账btlId="+btlId);
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				if(fg) {
					BankTradeList btl = null;
					String [] ids=btlId.split(",");
					for (String each : ids) {
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null){
							if("0".equals(isLock) && btl.getIsCheck()!=-2){//解锁
								fg = U.setPutFalse(map, "有记录非锁定状态，不能解锁");
								break;
							}else if("1".equals(isLock) && btl.getIsCheck()!=-1){//锁定
								fg = U.setPutFalse(map, "有记录非已完成报销状态，不能锁定");
								break;
							}
							btlist.add(btl);
						}
					}
				}
				if(fg){
					for (BankTradeList btl : btlist) {
						if("0".equals(isLock)){//解锁
							btl.setIsCheck(-1);
							btlDao.update(btl);
							U.log(log, "银行账"+btl.getId()+"解锁成功");
						}else{//锁定
							btl.setIsCheck(-2);
							btlDao.update(btl);
							U.log(log, "银行账"+btl.getId()+"锁定成功");
						}
						//后期如有需要再将报销记录锁定
					}
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
	@Override
	public Map<String, Object> linkReim(HttpServletRequest request,
			ReqSrc reqsrc, String reimId,String btlId) {
		String logtxt = U.log(log, "结账-银行账与财务记账报销，相当于财务核销(已取消)", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(reimId)){
						fg = U.setPutFalse(map, "[凭证记录]不能为空");
					}else{
						reimId = reimId.trim();
						U.log(log, "关联凭证记录Id="+reimId);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(btlId)){
						fg = U.setPutFalse(map, "[银行账id]不能为空");
					}else{
						btlId = btlId.trim();
						U.log(log, "银行账btlId="+btlId);
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				if(fg) {
					String [] btlIds=btlId.split(",");
					BankTradeList btl = null;
					for (String each : btlIds) {
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null){
							if(btl.getIsCheck()!=0) {
								fg = U.setPutFalse(map, "有记录已有其他操作，不能结账");
								break;
							}
							btlist.add(btl);
						}
					}
				}
				if(fg){
					String operMark=UT.creatOperMark();
					String [] reimIds=reimId.split(",");
					ReimburseList reim=null;
					for (int i=0;i<reimIds.length;i++) {
						reim=reimDao.findByField("id", Long.valueOf(reimIds[i]));
						if(reim!=null){
							reim.setOperMark(StringUtils.isBlank(reim.getOperMark())?operMark:reim.getOperMark()+","+operMark);
							reim.setIsCheck(3);
							reimDao.update(reim);
						}
					}
					for (BankTradeList btl:btlist) {
						btl.setIsCheck(-1);
						btl.setOperMark(operMark);
						btlDao.update(btl);
					}
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
	@Override
	public Map<String, Object> transBtl(HttpServletRequest request,
			ReqSrc reqsrc, String transId) {
		String logtxt = U.log(log, "银行账互转", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				BankTradeList btlIn = null;
				BankTradeList btlOut = null;
				if(fg){
					if(StringUtils.isEmpty(transId)){
						fg = U.setPutFalse(map, "[互转记录]不能为空");
					}else{
						String [] transIds=transId.split(",");
						if(transIds.length!=2){
							fg = U.setPutFalse(map, "请选择两条[银行账]记录");
						}else{
							U.log(log, "互转记录id="+transId);
							btlIn=btlDao.findByField("id", Long.valueOf(transIds[0]));
							if(btlIn==null){
								fg = U.setPutFalse(map, "第1条银行账记录不存在");
							}
							btlOut=btlDao.findByField("id", Long.valueOf(transIds[1]));
							if(btlIn==null){
								fg = U.setPutFalse(map, "第2条银行账记录不存在");
							}
						}
					}
				}
				if(fg){
					if(btlIn.getIsCheck()!=0 || btlOut.getIsCheck()!=0){
						fg = U.setPutFalse(map, "其中一条记录【已经操作】，不能参与此次互转");
					}
				}
				if(fg){
					String operMark=UT.creatOperMark();
					String voucherNo=reimDao.getVoucherNum(btlIn.getUnitNum(), LU.getLUName(request, redis));
					//互转银行账变为已报销
					btlIn.setIsCheck(-2);
					btlIn.setVoucherNumber(voucherNo);
					btlIn.setOperMark((StringUtils.isNotBlank(btlIn.getOperMark()))?btlIn.getOperMark():operMark);
					btlDao.update(btlIn);
					btlOut.setIsCheck(-2);
					btlOut.setVoucherNumber(voucherNo);
					btlOut.setOperMark((StringUtils.isNotBlank(btlOut.getOperMark()))?btlOut.getOperMark():operMark);
					btlDao.update(btlOut);
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
	
	@Override
	public Map<String, Object> openSelBtl(HttpServletRequest request,
			ReqSrc reqsrc, String openBtlId, String openRole) {
		String logtxt = U.log(log, "财务-开放银行账查询(已取消)", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(openBtlId)){
						fg = U.setPutFalse(map, "[开放记录]不能为空");
					}else{
						openBtlId = openBtlId.trim();
						U.log(log, "开放记录Id="+openBtlId);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(openRole)){
						fg = U.setPutFalse(map, "[开放角色]不能为空");
					}else{
						openRole = openRole.trim();
						U.log(log, "开放角色="+openRole);
					}
				}
				if(fg){
					String [] id=openBtlId.split(",");
					BankTradeList btl=null;
					for(String each :id){
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null){
							//btl.setOpenRole(openRole);
							btlDao.update(btl);
						}
					}
					/*String hql="from Staff where unitNum=? and baseUserId.uname=?";
					Staff oper=oplSer.findObj(hql, unitNum,LU.getLUName(request, redis));
					if(oper!=null){
						oper.setOpenRole(openRole);
						oplSer.update(oper);
					}*/
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
	@Override
	public Map<String, Object> cancelBtl(ReqSrc reqsrc,
			HttpServletRequest request, HttpServletResponse response,
			String cancelId) {
		String logtxt = U.log(log, "撤销银行日记账(已取消)", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				BankTradeList cancelBtl = null;// 银行账
				if(fg){
					if(StringUtils.isEmpty(cancelId)){
						fg = U.setPutFalse(map, "[撤销记录id]不能为空");
					}else{
						cancelId = cancelId.trim();
						if(!FV.isLong(cancelId)){
							fg = U.setPutFalse(map, "[撤销记录id]值格式错误");
						}else{
							cancelBtl = btlDao.findByField("id", Long.parseLong(cancelId));
							if(cancelBtl == null){
								fg = U.setPutFalse(map, "该银行账账记录不存在");
							}
						}
						
						U.log(log, "撤销记录id："+cancelId);
					}
				}
				if(fg){
					//新银行账更新为未核销状态，已核销金额清零(20191218)
					String hql="from BankTradeList where operMark like ?0";
					List<BankTradeList> btlist=btlDao.findhqlList(hql, "%"+cancelBtl.getOperMark()+"%");
					if(btlist.size()>0){
						for (BankTradeList btl:btlist) {
							btl.setIsCheck(0);
							btlDao.update(btl);
						}
					}
				}
				U.setPut(map, 1, "操作成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Object[] getOrderNum(String unitNum,String isCheck) {
		String hql="from BankTradeList where unitNum = ?0 and isCheck = ?1 order by id desc";
		List<BankTradeList> btList=btlDao.findhqlList(hql,unitNum,Integer.parseInt(isCheck));
		HashSet<String> hs = new HashSet<String>();
		String [] orderNums=null;
		for (BankTradeList each : btList) {
			if(StringUtils.isNotBlank(each.getOrderNum())){
				if(each.getOrderNum().contains(",")){
					orderNums=each.getOrderNum().split(",");
					for (String eachNum : orderNums) {
						hs.add(eachNum);
					}
				}else{
					hs.add(each.getOrderNum());
				}
			}
		}
		List<String> orderNum=new ArrayList<String>(hs);
		Object [] obj = (String[])orderNum.toArray(new String[orderNum.size()]);
		return obj;
	}
	@Override
	public Map<String, Object> findBtlById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-银行账-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("id")) {
				U.setPutFalse(map,  "id参数不为空");
				return map;
			}
			BankTradeList btl = btlDao.findByField("id", jsonObject.getLong("id"));
			if (btl != null) {
				U.log(log, "通过id查找银行账成功");
				map.put("data", btl);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(FeeCourse.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找银行账失败");
				U.setPutFalse(map, 0, "查找失败,银行账不存在");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	@Override
	public Map<String, Object> findBalanceByBankInfo(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-银行余额", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("bankNo")) {
				U.setPutFalse(map,  "银行卡号不能为空");
				return map;
			}
			if (!jsonObject.containsKey("bankName")) {
				U.setPutFalse(map,  "银行名称不能为空");
				return map;
			}
			String hql="from BankTradeList where myBankNum = ?0 and myBankName = ?1 and unitNum = ?2 order by id desc";
			BankTradeList btl=btlDao.findObj(hql, jsonObject.getString("bankNo").toString(),jsonObject.getString("bankName"),LU.getLUnitNum(request, redis),"LIMIT 1");
			if (btl != null) {
				U.log(log, "获取银行余额成功");
				map.put("balance", btl.getBalance());
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找银行余额失败");
				U.setPutFalse(map, 0, "查找失败,银行账不存在");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	@Override
	public Map<String, Object> findTransNamesAndRemarks(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request) {
		
		String logtxt = U.log(log, "查询-对方户名列表和摘要列表", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String hql="from BankTradeList where unitNum = ?0 group by transName,transNum order by id desc";
			List<BankTradeList> btlist=btlDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			if(btlist.size()>0){
				List<String> transNames=new ArrayList<String>();
				for (BankTradeList btl:btlist) {
					if(StringUtils.isNotBlank(btl.getTransNum())) {
						transNames.add(btl.getTransName()+"/@"+btl.getTransNum());
					}else {
						transNames.add(btl.getTransName()+"/@");
					}
					
				}
				map.put("transNames", transNames);
			}
			hql="from BankTradeList where unitNum = ?0 group by remark order by id desc";
			btlist=btlDao.findhqlList(hql, LU.getLUnitNum(request, redis));
			if(btlist.size()>0){
				List<String> remarks=new ArrayList<String>();
				for (BankTradeList btl:btlist) {
					remarks.add(btl.getRemark());
				}
				map.put("remarks", remarks);
			}
			U.setPut(map, 1, "查询成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}
	
	@Override
	public Map<String, Object> importfeeBtl(MultipartFile file,HttpServletRequest request,ReqSrc reqsrc,String tradeBank) {
		String logtxt = U.log(log, "银行账导入",reqsrc);
		
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
					int startRow=1;//默认取建行第一行
					int cardRow=2;//默认取建行卡号
					int myCardCell=1;//默认取建行我的卡号
					int startCell=4;//默认取建行表头
					int tdttCell=-1;//工行公账日期是标准格式
					int tdCell=1;//默认取建行日期
					int ttCell=2;//默认取建行时间
					int status=0;//农商需要二次判断收支
					int tdMoneyCell=3;//农商交易金额列
					int inCell=4;//默认取建行收入
					int outCell=3;//默认取建行支出
					int balCell=5;//默认取建行余额
					int rekCell=7;//默认取建行摘要
					int dfzhCell=8;//默认取建行对方账号
					int dfhmCell=9;//默认取建行对方户名
					int moneyType=11;//默认取建行金额类型
					if(file.getOriginalFilename().contains("工行")){
						if(file.getOriginalFilename().contains("公账")){
							startRow=1;
							cardRow=0;
							myCardCell=1;
							startCell=2;
							tdttCell=1;//标准日期
							inCell=10;
							outCell=11;
							balCell=9;
							rekCell=7;
							dfzhCell=0;
							dfhmCell=3;
							moneyType=12;
						}else{//个人
							startRow=2;
							cardRow=0;
							myCardCell=0;
							startCell=3;
							tdttCell=0;//拼接时分秒
							inCell=8;
							outCell=9;
							balCell=11;
							rekCell=1;
							dfzhCell=0;
							dfhmCell=12;
							moneyType=13;
						}
					}else if(file.getOriginalFilename().contains("民生")){
						startRow=1;
						cardRow=0;
						myCardCell=1;
						startCell=12;
						tdCell=0;
						ttCell=10;
						inCell=3;
						outCell=2;
						balCell=4;
						rekCell=6;
						dfzhCell=7;
						dfhmCell=8;
						moneyType=11;
					}else if(file.getOriginalFilename().contains("农商")){
						startRow=0;
						cardRow=0;
						myCardCell=1;
						startCell=2;
						tdttCell=1;
						status=2;
						balCell=4;
						rekCell=8;
						dfzhCell=6;
						dfhmCell=7;
						moneyType=10;
					}else if(file.getOriginalFilename().contains("微信")){
						startRow=1;
						cardRow=0;
						myCardCell=1;
						startCell=2;
						tdttCell=1;//标准日期
						inCell=10;
						outCell=11;
						balCell=9;
						rekCell=7;
						dfzhCell=0;
						dfhmCell=3;
						moneyType=12;
					}else if(file.getOriginalFilename().contains("中信")){
						startRow=4;
						cardRow=0;
						myCardCell=1;
						startCell=8;
						tdCell=0;
						ttCell=1;
						inCell=5;
						outCell=6;
						balCell=7;
						rekCell=8;
						dfzhCell=2;
						dfhmCell=3;
						moneyType=9;
					}else if(file.getOriginalFilename().contains("农行")){
						
					}else if(file.getOriginalFilename().contains("支付宝")){
						startRow=0;
						cardRow=1;
						myCardCell=1;
						startCell=1;
						tdCell=2;
						ttCell=3;
						inCell=5;
						outCell=4;
						balCell=6;
						rekCell=7;
						dfzhCell=8;
						dfhmCell=9;
						moneyType=10;
					}
					// 路径 文件名 工作薄下标 开始行数
					List<List<String>> list = POIUtils.getExcelData(new File(filePath), "btlRecord."+suffix, 0, startRow);
					List<String> feerow=(List<String>) list.get(cardRow); //获取银行账号
					if(StringUtils.isBlank(tradeBank) || !tradeBank.contains("/")) {
						fg=U.setPutFalse(map, "导入银行格式错误，正确格式为：银行名称/银行账号");
					}
					if(fg) {
						if(file.getOriginalFilename().contains("工行") && !file.getOriginalFilename().contains("公账")){
							if(!tradeBank.contains(feerow.get(myCardCell).trim().split(":")[1].trim())){//导入的银行账号和选择的银行账号不匹配
								fg=U.setPutFalse(map, "导入银行和选择银行不匹配【请确认是公账文档或个人文档】");
							}
						}else{
							if(!tradeBank.contains(feerow.get(myCardCell).trim())){//导入的银行账号和选择的银行账号不匹配
								fg=U.setPutFalse(map, "导入银行和选择银行不匹配【请确认是公账文档或个人文档】");
							}
						}
					}
					if(fg){
						feerow=(List<String>) list.get(startCell); //获取第1行信息
						if(feerow.size()<9){
							fg=U.setPutFalse(map, "文档格式或内容不匹配【请确认是公账文档或个人文档】");
						}
					}
					if(fg){
						String hql="from BankList where unitNum=?0 and cardNo=?1 and isOpen=1";
						BankList gpw=blDao.findObj(hql,LU.getLUnitNum(request, redis),tradeBank.split("/")[1]);
						if(gpw==null){
							fg=U.setPutFalse(map, "该银行账本未启用，请先启用");
						}
					}
					String myBankName=tradeBank.split("/")[0];
					String myBankNum=tradeBank.split("/")[1];
					Date tradeTime=null;
					String tradeHMS=null;
					String hql="from BankTradeList where myBankNum = ?0 and unitNum = ?1 order by id desc";
					BankTradeList lastBtl=btlDao.findObj(hql, myBankNum,LU.getLUnitNum(request, redis),"LIMIT 1");
					BankTradeList btl=null;
					/*hql="from BankTradeList where myBankNum = ?0" +
							" and transName = ?1 and transNum = ?2 and balance = ?3 and unitNum = ?4";*/
					double lastBalance=0;//上一条余额；
					MoneyType mt=null;//金额类型
					Map<Integer,MoneyType> mts=new HashMap<Integer,MoneyType>();
					if(fg){
						//循环遍历从excel中获取的数据，进行计算
						for (int i = startCell; i < list.size(); i++) {
							feerow = (List<String>) list.get(i); //获取每行数据
							if(StringUtils.isBlank(feerow.get(dfhmCell).trim())) {
								fg=U.setPutFalse(map, "本次导入不成功：对方户名为必填项，请检查后重试！");
								break;
							}
							if(StringUtils.isBlank(feerow.get(balCell).trim())) {
								fg=U.setPutFalse(map, "本次导入不成功：余额为必填项，请检查后重试！");
								break;
							}
							if(StringUtils.isBlank(feerow.get(moneyType).trim())) {
								fg=U.setPutFalse(map, "本次导入不成功：金额类型为必填项，请检查后重试！");
								break;
							}else {
								mt=mtDao.findByField("typeName", feerow.get(moneyType).trim());
								if(mt!=null) {
									mts.put(i, mt);
								}else {
									fg=U.setPutFalse(map, "本次导入不成功：金额类型【"+feerow.get(moneyType).trim()+"】在系统中不存在，请检查后重试！");
									break;
								}
							}
							if(tdttCell!=-1){//正常日期
								if(StringUtils.isBlank(feerow.get(tdttCell).trim())) {
									fg=U.setPutFalse(map, "本次导入不成功：金额类型为必填项，请检查后重试！");
									break;
								}
							}else{
								if(StringUtils.isBlank(feerow.get(ttCell).trim()) || StringUtils.isBlank(feerow.get(tdCell).trim())) {
									fg=U.setPutFalse(map, "本次导入不成功：交易时间为必填项，请检查后重试！");
									break;
								}
							}
							if(fg) {
								if(lastBtl!=null){
									if(i==startCell){
										lastBalance=lastBtl.getBalance();//系统中已存在最后一条记录的余额
									}else{
										lastBalance=Double.valueOf(((List<String>) list.get(i-1)).get(balCell).trim().replaceAll(",", ""));
									}
								}else{
									if(i>startCell){//获取上一条记录的余额
										lastBalance=Double.valueOf(((List<String>) list.get(i-1)).get(balCell).trim().replaceAll(",", ""));
									}
								}
								if(status!=0){//农商
									if(feerow.get(status).trim().contains("收入")){
										inCell=tdMoneyCell;
										outCell=-1;
									}else{
										outCell=tdMoneyCell;
										inCell=-1;
									}
								}
								//最后一条记录的余额+/-当前记录的金额=当前记录的余额
								if(inCell!=-1 && StringUtils.isNotBlank(feerow.get(inCell).trim()) && 
										Double.valueOf(feerow.get(inCell).trim().replaceAll(",", ""))>0){//收
									if(MathUtils.add(lastBalance, Double.valueOf(feerow.get(inCell).trim().replaceAll(",", "")), 2)!=
											Double.valueOf(feerow.get(balCell).trim().replaceAll(",", ""))){
										if(lastBtl!=null && i==startCell){
											fg=U.setPutFalse(map, "本次导入不成功：导入余额与系统余额数据有误差，有重复或遗漏的银行交易数据！请检查后重试！");
										}else{
											fg=U.setPutFalse(map, "本次导入不成功：有记录余额不正确！请检查后重试！");
										}
										break;
									}
								}else if(outCell!=-1 && StringUtils.isNotBlank(feerow.get(outCell).trim()) && 
										Double.valueOf(feerow.get(outCell).trim().replaceAll(",", ""))>0){//支出
									if(MathUtils.sub(lastBalance, Double.valueOf(feerow.get(outCell).trim().replaceAll(",", "")), 2)!=
											Double.valueOf(feerow.get(balCell).trim().replaceAll(",", ""))){
										if(lastBtl!=null && i==startCell){
											fg=U.setPutFalse(map, "本次导入不成功：导入余额与系统余额数据有误差，有重复或遗漏的银行交易数据！请检查后重试！");
										}else{
											fg=U.setPutFalse(map, "本次导入不成功：有记录余额不正确！请检查后重试！");
										}
										break;
									}
								}else{
									fg=U.setPutFalse(map, "本次导入不成功：导入数据中有收入和支出都为0或为空的交易记录，请检查后重试！");
									break;
								}
							}
						}
					}
					if(fg){
						String operMark=UT.creatOperMark();//操作标识号
						for (int i = startCell; i < list.size(); i++) {
							feerow = (List<String>) list.get(i); //获取每行数据
							if(status!=0){//农商
								if(feerow.get(status).trim().contains("收入")){
									inCell=tdMoneyCell;
									outCell=-1;
								}else{
									outCell=tdMoneyCell;
									inCell=-1;
								}
							}
							if(tdttCell!=-1){//正常日期
								if(feerow.get(tdttCell).trim().contains(":")){//工行公账标准时间
									tradeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(tdttCell).trim());
								}else{//农商/工行个人时间要拼接时分秒
									tradeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(tdttCell).trim()+
											DateUtils.getHHmmss(DateUtils.getPlusSecondsDate(DateUtils.yyyy_MM_dd_HH_mm_ss, new Date(), i)));
								}
							}else{
								if(feerow.get(ttCell).trim().contains(":")){
									tradeHMS=feerow.get(ttCell).trim();
								}else{
									tradeHMS=DateUtils.getHms(feerow.get(ttCell).trim());
								}
								if(feerow.get(tdCell).trim().contains("-")){
									tradeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, feerow.get(tdCell).trim()+" "+tradeHMS);
								}else{
									tradeTime=DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, DateUtils.getDate(feerow.get(tdCell).trim())+" "+tradeHMS);
								}
							}
							btl = new BankTradeList(); //创建导入数据对象
							btl.setUnitNum(LU.getLUnitNum(request, redis));
							btl.setMyBankName(myBankName);
							btl.setMyBankNum(myBankNum);
							btl.setTransName(feerow.get(dfhmCell).trim());
							if(dfzhCell!=0)btl.setTransNum(feerow.get(dfzhCell).trim());
							if(inCell!=-1 && StringUtils.isNotBlank(feerow.get(inCell).trim())){
								btl.setTradeInMoney(Double.valueOf(feerow.get(inCell).trim().replaceAll(",", "")));	
							}
							if(outCell!=-1 && StringUtils.isNotBlank(feerow.get(outCell).trim())){
								btl.setTradeOutMoney(Double.valueOf(feerow.get(outCell).trim().replaceAll(",", "")));
							}
							btl.setBalance(Double.valueOf(feerow.get(balCell).trim().replaceAll(",", "")));
							btl.setRemark(feerow.get(rekCell).trim());
							btl.setTradeTime(tradeTime);
							btl.setAddTime(new Date());
							btl.setOperNote(LU.getLRealName(request, redis)+"[导入]");
							btl.setOperMark(operMark);
							btl.setMoneyTypeId(mts.get(i));
						 	btlDao.save(btl);
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
	public Map<String, Object> btlExport(HttpServletRequest request,HttpServletResponse response, ReqSrc reqsrc, JSONObject jsonObject) {
		String logtxt = U.log(log, "银行账-下载excel", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
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
			if(fg){
				Page<BankTradeList> pd = btlDao.findBankTradeList(reqsrc, "1", rows, LU.getLUnitNum(request, redis), bankNo, transName, remark, timeType, sTime, eTime, 
						status, isCheck, findMoney, openRole, voucherNum, operMark, openSel, moneyType, cusName, serviceName);
				String contextTitle = "车队银行账记录";// excel表内容标题
				String [] headers = {"凭证号","金额类型","交易日期","交易时间","银行名称","对方户名","客户名称","收入","支出","余额","摘要","导入时间","操作记录","对方账号"};// 表列标题
				List<Object[]> dataList = new ArrayList<Object[]>();
				String voucherNo="",transNameEx="",customer="",remarkEx="",transNum="";
				if(pd.getResult().size()>0) {
					List<BankTradeList> expBtlBankD=pd.getResult();
					
					// 转换成数组格式数据
					for (int i = 0; i < expBtlBankD.size(); i++) {
						voucherNo="";transNameEx="";customer="";remarkEx="";transNum="";
						if (StringUtils.isNotEmpty(expBtlBankD.get(i).getTransName())) {
							transNameEx = expBtlBankD.get(i).getTransName();
						}
						if (StringUtils.isNotEmpty(expBtlBankD.get(i).getCusName())) {
							customer = expBtlBankD.get(i).getCusName().replace(",", "，");
						}
						if (StringUtils.isNotEmpty(expBtlBankD.get(i).getRemark())) {
							remarkEx = expBtlBankD.get(i).getRemark().replaceAll(",", "，").replace("&nbsp;", " ");
						}
						if (StringUtils.isNotEmpty(expBtlBankD.get(i).getTransNum())) {
							transNum = expBtlBankD.get(i).getTransNum();
						}
						Object[] element= {voucherNo, expBtlBankD.get(i).getMoneyTypeId().getTypeName(),
								DateUtils.DateToStr(DateUtils.yyyy_MM_dd, expBtlBankD.get(i).getTradeTime()),
								DateUtils.DateToStr(DateUtils.HH_mm_ss, expBtlBankD.get(i).getTradeTime()),
								expBtlBankD.get(i).getMyBankName(),transNameEx,customer,expBtlBankD.get(i).getTradeInMoney(),
								expBtlBankD.get(i).getTradeOutMoney(),expBtlBankD.get(i).getBalance(),remarkEx,expBtlBankD.get(i).getAddTime(),
								expBtlBankD.get(i).getOperNote(),transNum};
						dataList.add(element);
					}
				}
				String fileName = ExportUtil.getFileNameByTime(new Date(),LU.getLCompany(request, redis).getCompanyName() + "_");// 文件名重命名
				POIUtils.downToExcel(response, fileName,contextTitle, headers, dataList);
				U.setPut(map, 1, "下载成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> addBtl(HttpServletRequest request,
			ReqSrc reqsrc, String myBank, String transName, String transNum,
			String tradeTime, String tradeStatus, String tradeMoney,
			String balance, String remark,String moneyType) {
		String logtxt = U.log(log, "财务-添加银行账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String myBankName="";
				String myBankNum="";
				Date tradeDate=null;
				MoneyType mt=null;
				if(StringUtils.isNotEmpty(moneyType)) {
					mt=mtDao.findByField("id", Long.valueOf(moneyType));
					if(mt==null) {
						fg = U.setPutFalse(map, "[金额类型]不存在");
					}
				}
				if(fg){
					if(StringUtils.isEmpty(tradeTime)){
						fg = U.setPutFalse(map, "交易时间不能为空");
					}else{
						tradeDate = DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, tradeTime);
						U.log(log, "交易时间tradeTime="+tradeTime);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(myBank)){
						fg = U.setPutFalse(map, "[我的银行]不能为空");
					}else{
						if(!myBank.contains("/")) {
							fg = U.setPutFalse(map, "[我的银行]格式不正确");
						}
						if(fg) {
							myBankName=myBank.split("/")[0];
							myBankNum=myBank.split("/")[1];
							String hql="from BankTradeList where myBankNum = ?0 and unitNum = ?1 order by id desc";
							BankTradeList lastBtl=btlDao.findObj(hql, myBankNum,LU.getLUnitNum(request, redis),"LIMIT 1");
							if(lastBtl!=null){
								if("0".equals(tradeStatus)){//收
									if(MathUtils.add(lastBtl.getBalance(), Double.valueOf(tradeMoney), 2)!=Double.valueOf(balance)){
										fg=U.setPutFalse(map, "添加不成功：添加余额与系统余额数据有误差，请检查后重试！");
									}
								}else{
									if(MathUtils.sub(lastBtl.getBalance(), Double.valueOf(tradeMoney), 2)!=Double.valueOf(balance)){
										fg=U.setPutFalse(map, "添加不成功：添加余额与系统余额数据有误差，请检查后重试！");
									}
								}
								if(fg){
									if(tradeDate.getTime()<=lastBtl.getTradeTime().getTime()){
										fg=U.setPutFalse(map, "本次添加不成功：交易时间必须在上一条交易时间之后");
									}
								}
							}
						}
					}
				}
				if(fg){
					if(StringUtils.isEmpty(transName)){
						fg = U.setPutFalse(map, "对方户名不能为空");
					}else{
						transName = transName.trim();
						U.log(log, "对方户名transName="+transName);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(transNum)){
						//fg = U.setPutFalse(map, "对方账号不能为空");20220103改为选填
						U.log(log, "对方账号transNum为空");
					}else{
						transNum = transNum.trim();
						U.log(log, "对方账号transNum="+transNum);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(tradeMoney)){
						fg = U.setPutFalse(map, "交易金额不能为空");
					}else{
						tradeMoney = tradeMoney.trim();
						U.log(log, "交易金额tradeMoney="+tradeMoney);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(balance)){
						fg = U.setPutFalse(map, "余额不能为空");
					}else{
						balance = balance.trim();
						U.log(log, "余额balance="+balance);
					}
				}
				if(fg){
					if(StringUtils.isEmpty(remark)){
						//fg = U.setPutFalse(map, "摘要不能为空");20220103改为选填
						U.log(log, "摘要remark为空");
					}else{
						remark = remark.trim();
						U.log(log, "摘要remark="+remark);
					}
				}
				if(fg){
					String hql="from BankList where unitNum = ?0 and cardNo = ?1 and isOpen=1";
					BankList gpw=blDao.findObj(hql, LU.getLUnitNum(request, redis),myBankNum);
					if(gpw==null){
						fg=U.setPutFalse(map, "该银行账本未启用，请先启用");
					}
				}
				if(fg){
					String operMark=UT.creatOperMark();//操作标识号
					BankTradeList btl=new BankTradeList();
					btl.setUnitNum(LU.getLUnitNum(request, redis));
					btl.setMyBankName(myBankName);
					btl.setMyBankNum(myBankNum);
					btl.setTransName(transName);
					if(StringUtils.isNotBlank(transNum))btl.setTransNum(transNum);
					if("0".equals(tradeStatus)){
						btl.setTradeInMoney(Double.valueOf(tradeMoney));	
					}else{
						btl.setTradeOutMoney(Double.valueOf(tradeMoney));
					}
					if(Double.valueOf(tradeMoney)==0){//期初余额
						btl.setIsCheck(-1);//报销完成
					}
					btl.setBalance(Double.valueOf(balance));
					if(StringUtils.isNotBlank(remark))btl.setRemark(remark);
					btl.setTradeTime(tradeDate);
					if(mt!=null) {
						btl.setMoneyTypeId(mt);
					}
					btl.setAddTime(new Date());
					btl.setOperNote(LU.getLRealName(request, redis)+"[添加]");
					btl.setOperMark(operMark);
				 	btlDao.save(btl);
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
	
	@Override
	public Map<String, Object> modifyBtl(HttpServletRequest request,
			ReqSrc reqsrc, String updId, String moneyType, String remark) {
		String logtxt = U.log(log, "财务-修改银行账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				BankTradeList btl=null;
				if(fg){
					if(StringUtils.isEmpty(updId)){
						fg = U.setPutFalse(map, "[修改记录]不能为空");
					}else{
						updId = updId.trim();
						btl=btlDao.findByField("id", Long.valueOf(updId));
						if(btl==null){
							fg = U.setPutFalse(map, "[修改记录]不存在");	
						}else{
							U.log(log, "修改记录Id="+updId);
						}
					}
				}
				MoneyType mt=null;
				if(fg) {
					if(StringUtils.isEmpty(moneyType)){
						fg = U.setPutFalse(map, "[金额类型]不能为空");
					}else{
						moneyType = moneyType.trim();
						mt=mtDao.findByField("id", Long.valueOf(moneyType));
						if(mt==null){
							fg = U.setPutFalse(map, "[金额类型]不存在");	
						}
					}
				}
				if(fg){
					btl.setMoneyTypeId(mt);
					btl.setRemark(remark);
					btlDao.update(btl);
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
	
	@Override
	public Map<String, Object> delBtl(HttpServletRequest request,
			ReqSrc reqsrc, String ids,String myBankNum) {
		String logtxt = U.log(log, "财务-删除银行账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				BankTradeList btl=null;
				if(fg){
					if(StringUtils.isEmpty(ids)){
						fg = U.setPutFalse(map, "[删除记录]不能为空");
					}else{
						ids = ids.trim();
						U.log(log, "删除记录Id="+ids);
					}
				}
				if(fg){
					String hql="from BankTradeList where unitNum = ?0 and myBankNum = ?1 and id> ?2 order by id asc";
					String [] id=ids.split(",");//前台控制了只能选择一条
					for(String each :id){
						btl=btlDao.findObj(hql, LU.getLUnitNum(request, redis),myBankNum,Long.valueOf(each),"LIMIT 1");
						if(btl!=null){
							fg = U.setPutFalse(map, "请从最后一条银行账记录开始删除，并且选择的记录必须是连续的");
							break;
						}
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				if(fg) {
					String [] id=ids.split(",");
					for(String each :id){
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null) {
							if(btl.getIsCheck()!=0) {
								fg = U.setPutFalse(map, "有记录已操作，不能删除");
								break;
							}
							btlist.add(btl);
						}
					}
				}
				if(fg){
					for(BankTradeList del : btlist){
						btlDao.delete(del);
					}
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
	
	@Override
	public Map<String, Object> downBtlMoney(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String btlId, String money, String companyCusId, String notice_uname,
			String notice_note, String orderNum) {
		String logtxt = U.log(log, "银行账下账", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(btlId)){
						fg = U.setPutFalse(map, "[下账记录]不能为空");
					}
				}
				if(fg){
					if(StringUtils.isEmpty(money)){
						fg = U.setPutFalse(map, "[下账金额]不能为空");
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				if(fg) {
					BankTradeList btl=null;
					String [] btlIds=btlId.split(",");
					for (String each : btlIds) {
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null){
							if(btl.getIsCheck()!=0) {
								fg = U.setPutFalse(map, "有记录已有其他操作，不能下账");
								break;
							}
							btlist.add(btl);
						}
					}
				}
				if(fg){
					for (BankTradeList btl : btlist) {
						btl.setIsCheck(1);//待审核
						btl.setCompanyCusId(companyCusId);
						btl.setNoticeMan(notice_uname);
						btl.setNoticeRemark(notice_note);
						if(StringUtils.isNotBlank(orderNum))btl.setOrderNum(orderNum);
						btl.setOperNote(btl.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "下账：待审核"));
						btlDao.update(btl);
					}
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
	@Override
	public Map<String, Object> checkYesBtl(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String createInfo,String faceCourseInfo) {
		String logtxt = U.log(log, "银行账审核下账记录-通过", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String [] faceInfos=null;
				if(fg) {
					if(StringUtils.isEmpty(faceCourseInfo)){
						fg = U.setPutFalse(map, "[对方科目信息]不能为空");
					}else {
						faceInfos=faceCourseInfo.split("@");
						for (int i = 0; i < faceInfos.length; i++) {
							if(faceInfos[i].split("=").length!=4) {
								fg = U.setPutFalse(map, "第"+(i+1)+"个[对方科目信息]格式错误");
								break;
							}
						}
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				Map<Long, Object> fcmap = new HashMap<Long, Object>();
				if(fg){
					if(StringUtils.isEmpty(createInfo)){
						fg = U.setPutFalse(map, "[银行账记录]不能为空");
					}else {
						String [] infos=createInfo.split("@");
						for (int i = 0; i < infos.length; i++) {
							if(infos[i].split("=").length!=2) {
								fg = U.setPutFalse(map, "第"+(i+1)+"个[银行帐科目信息]格式错误");
								break;
							}
						}
						if(fg) {
							String [] ids=null;
							BankTradeList btl=null;
							FeeCourse fc=null;
							for (int i = 0; i < infos.length; i++) {
								ids=infos[i].split("=");
								btl=btlDao.findByField("id", Long.valueOf(ids[0]));
								if(btl.getIsCheck()!=1) {
									fg = U.setPutFalse(map, "有报账记录状态非【待审核】状态，操作失败");
									break;
								}
								btlist.add(btl);
								fc=fcDao.findByField("id", Long.valueOf(ids[1]));
								if(fc.getIsLastCourse()==0) {
									fg = U.setPutFalse(map, "科目【"+fc.getCourseName()+"】非末级科目，不能生成凭证");
									break;
								}
								fcmap.put(btl.getId(), fc);
							}
						}
					}
				}
				if(fg){
					double tradeMoney=0;//生成凭证总金额
					String operMark=UT.creatOperMark();//操作编号
					List<FeeCourseTrade> fctlist=new ArrayList<FeeCourseTrade>();
					FeeCourse fc=null;
					String voucherNum=reimDao.getVoucherNum(LU.getLUnitNum(request, redis),  LU.getLUName(request, redis));
					double downMoney=0;//本次可下账金额
					CompanyCustom cc=null;//报销客户
					for (BankTradeList btl : btlist) {
						if(btl.getIsCheck()==1){//待审核
							downMoney+=MathUtils.add(downMoney, MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2);
							fc=(FeeCourse)fcmap.get(btl.getId());
							if(fc.getCourseType()==0) {//收入
								tradeMoney+=MathUtils.add(tradeMoney, MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2);
							}else {//支出
								tradeMoney+=MathUtils.sub(tradeMoney, MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2);
							}
							cc=ccDao.findByField("id", Long.valueOf(btl.getCompanyCusId()));//下账客户为报销人
							FeeCourseTrade fct=new FeeCourseTrade();
							fct.setUnitNum(btl.getUnitNum());
							fct.setFeeCourseId(fc);
							fct.setRemark(btl.getRemark());
							fct.setGathMoney(btl.getTradeInMoney());
							fct.setPayMoney(btl.getTradeOutMoney());
							fct.setAddTime(new Date());
							fct.setBankTradeId(btl);
							if(cc!=null)fct.setReimUserId(cc.getBaseUserId());
							fctlist.add(fct);
							//更新对应科目余额
							fc.setBalance(MathUtils.add(fc.getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
							fcDao.update(fc);
							if(fc.getParentCourseId()!=null) {//更新父级科目余额
								FeeCourse parentOne=fc.getParentCourseId();
								fc.getParentCourseId().setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
								fcDao.update(parentOne);
								if(parentOne.getParentCourseId()!=null) {
									parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
									fcDao.update(parentOne.getParentCourseId());
								}
							}
							//更新银行账
							btl.setVoucherNumber(voucherNum);
							btl.setIsCheck(-1);
							btl.setOperMark(StringUtils.isBlank(btl.getOperMark())?operMark:btl.getOperMark()+","+operMark);
							btl.setOperNote(btl.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "下账审核通过"));
							if("预收账款".equals(fc.getCourseName()) && btl.getTradeInMoney()>0) {//增加客户预存款
								if(cc!=null) {
									cc.setPreMoney(MathUtils.add(cc.getPreMoney(),btl.getTradeInMoney(), 2));
									ccDao.update(cc);
								}
							}else if(StringUtils.isNotBlank(btl.getOrderNum())){//订单收款
								String [] orderNums=btl.getOrderNum().split(",");
								for (String eachNum : orderNums) {
									MainCarOrder mco=mcoDao.findByField("orderNum", eachNum);
									if(mco!=null){
										if(downMoney>0) {
											if(downMoney>=mco.getPrice()) {//金额够本次订单下账
												mco.setAlGathPrice(MathUtils.add(mco.getAlGathPrice(), mco.getPrice(), 2));
												downMoney=MathUtils.sub(downMoney, mco.getPrice(), 2);
											}else {//金额不够本次订单下账，只能下部分
												mco.setAlGathPrice(MathUtils.add(mco.getAlGathPrice(), downMoney, 2));
												downMoney=0;
											}
											if(mco.getAlGathPrice()>=mco.getPrice()) {//收款完成
												mco.setPayStatus(OrderPayStatus.FULL_PAID);
											}else {//已收部分
												mco.setPayStatus(OrderPayStatus.DEPOSIT_PAID);
											}
											mcoDao.update(mco);
										}
									}
								}
							}
							btlDao.update(btl);
							if(StringUtils.isNotBlank(btl.getNoticeMan())){//发送通知
								
							}
						}
					}
					//添加对方科目交易记录
					String [] faceIds=null;
					FeeCourse face=null;
					double faceGath=0;//对方科目借方金额
					double facePay=0;//对方科目贷方金额
					for (int i = 0; i < faceInfos.length; i++) {
						faceIds=faceInfos[i].split("=");
						face=fcDao.findByField("id", Long.valueOf(faceIds[0]));
						faceGath=Double.valueOf(faceIds[2]);
						facePay=Double.valueOf(faceIds[3]);
						FeeCourseTrade fct=new FeeCourseTrade();
						fct.setUnitNum(LU.getLUnitNum(request, redis));
						fct.setFeeCourseId(face);
						fct.setRemark(faceIds[1]);
						fct.setGathMoney(faceGath);
						fct.setPayMoney(facePay);
						fct.setAddTime(new Date());
						fctlist.add(fct);
						//更新对应科目余额
						face.setBalance(MathUtils.add(face.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
						fcDao.update(face);
						if(fc.getParentCourseId()!=null) {//更新父级科目余额
							FeeCourse parentOne=fc.getParentCourseId();
							fc.getParentCourseId().setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
							fcDao.update(parentOne);
							if(parentOne.getParentCourseId()!=null) {
								parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
								fcDao.update(parentOne.getParentCourseId());
							}
						}
					}
					//生成凭证
					ReimburseList obj=new ReimburseList();
					obj.setUnitNum(LU.getLUnitNum(request, redis));
					obj.setCourseTrades(fctlist);
					obj.setGainTime(new Date());
					obj.setVoucherNum(voucherNum);
					obj.setTotalMoney(tradeMoney);
					obj.setIsCheck(2);
					obj.setAddTime(new Date());
					obj.setReqsrc(reqsrc);
					obj.setOperMark(operMark);
					obj.setOperNote(LU.getLRealName(request, redis)+"[添加]");
					reimDao.save(obj);
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
	@Override
	public Map<String, Object> checkNoBtl(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String btlId) {
		String logtxt = U.log(log, "银行账审核下账记录-不通过", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				if(fg){
					if(StringUtils.isEmpty(btlId)){
						fg = U.setPutFalse(map, "[审核记录]不能为空");
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				if(fg) {
					String [] btlIds=btlId.split(",");
					BankTradeList btl=null;
					for (String each : btlIds) {
						btl=btlDao.findByField("id", Long.valueOf(each));
						if(btl!=null){
							if(btl.getIsCheck()!=1) {
								fg = U.setPutFalse(map, "有审核记录状态非待审核状态，不能审核");
								break;
							}
							btlist.add(btl);
						}
					}
				}
				if(fg){
					for (BankTradeList btl : btlist) {
						btl.setIsCheck(0);//未操作
						btl.setNoticeMan(null);
						btl.setNoticeRemark(null);
						btl.setOrderNum(null);
						btl.setOperNote(btl.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "下账审核不通过"));
						btlDao.update(btl);
					}
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
	@Override
	public Map<String, Object> createReim(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String createInfo, String faceCourseInfo,String gainTime) {
		String logtxt = U.log(log, "银行账直接生成凭证", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(ReqSrc.PC_COMPANY == reqsrc){
				String [] faceInfos=null;
				FeeCourse fc=null;
				if(fg) {
					if(StringUtils.isEmpty(faceCourseInfo)){
						fg = U.setPutFalse(map, "[对方科目信息]不能为空");
					}else {
						faceInfos=faceCourseInfo.split("@");
						for (int i = 0; i < faceInfos.length; i++) {
							if(faceInfos[i].split("=").length!=6) {
								fg = U.setPutFalse(map, "第"+(i+1)+"个[对方科目信息]格式错误");
								break;
							}
						}
					}
				}
				List<BankTradeList> btlist=new ArrayList<BankTradeList>();
				Map<Long, Object> fcmap = new HashMap<Long, Object>();//科目
				Map<Long, Staff> staffs = new HashMap<Long, Staff>();//员工
				Map<Long, String> pns = new HashMap<Long, String>();//车牌号
				Staff staff=null;
				if(fg){
					if(StringUtils.isEmpty(createInfo)){
						fg = U.setPutFalse(map, "[银行账记录]不能为空");
					}else {
						String [] infos=createInfo.split("@");
						for (int i = 0; i < infos.length; i++) {
							if(infos[i].split("=").length!=4) {
								fg = U.setPutFalse(map, "第"+(i+1)+"个[银行帐科目信息]格式错误");
								break;
							}
						}
						if(fg) {
							String [] ids=null;
							BankTradeList btl=null;
							for (int i = 0; i < infos.length; i++) {
								ids=infos[i].split("=");
								btl=btlDao.findByField("id", Long.valueOf(ids[2]));
								if(btl.getIsCheck()!=0) {
									fg = U.setPutFalse(map, "有银行帐记录非【未操作】状态，不能生成凭证");
									break;
								}
								btlist.add(btl);
								fc=fcDao.findByField("id", Long.valueOf(ids[3]));
								if(fc.getIsLastCourse()==0) {
									fg = U.setPutFalse(map, "科目【"+fc.getCourseName()+"】非末级科目，不能生成凭证");
									break;
								}
								fcmap.put(btl.getId(), fc);
								if(StringUtils.isNotBlank(ids[0])) {//员工
									staff=staffDao.findByField("id", Long.valueOf(ids[0]));
									if(staff!=null) {
										staffs.put(btl.getId(), staff);
									}else {
										fg = U.setPutFalse(map, "有报销人已不存在于系统，不能生成凭证");
										break;
									}
								}
								if(StringUtils.isNotBlank(ids[1])) {//车牌号
									pns.put(btl.getId(), ids[1]);
								}
							}
						}
					}
				}
				if(fg){
					double tradeMoney=0;//生成凭证总金额
					String operMark=UT.creatOperMark();//操作编号
					List<FeeCourseTrade> fctlist=new ArrayList<FeeCourseTrade>();
					String voucherNum=reimDao.getVoucherNum(LU.getLUnitNum(request, redis),  LU.getLUName(request, redis));
					for (BankTradeList btl : btlist) {
						fc=(FeeCourse)fcmap.get(btl.getId());
						if(fc.getCourseType()==0) {//收入
							tradeMoney+=MathUtils.add(tradeMoney, MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2);
						}else {//支出
							tradeMoney+=MathUtils.sub(tradeMoney, MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2);
						}
						FeeCourseTrade fct=new FeeCourseTrade();
						fct.setUnitNum(btl.getUnitNum());
						if(staffs.get(btl.getId())!=null) {
							fct.setReimUserId(staffs.get(btl.getId()).getBaseUserId());
						}
						if(pns.get(btl.getId())!=null)fct.setPlateNum(pns.get(btl.getId()).toString());
						fct.setFeeCourseId(fc);
						fct.setRemark(btl.getRemark());
						fct.setGathMoney(btl.getTradeInMoney());
						fct.setPayMoney(btl.getTradeOutMoney());
						fct.setAddTime(new Date());
						fct.setBankTradeId(btl);
						fct.setVoucherNum(voucherNum);
						fctlist.add(fct);
						//更新对应科目余额
						fc.setBalance(MathUtils.add(fc.getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
						fcDao.update(fc);
						if(fc.getParentCourseId()!=null) {//更新父级科目余额
							FeeCourse parentOne=fc.getParentCourseId();
							fc.getParentCourseId().setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
							fcDao.update(parentOne);
							if(parentOne.getParentCourseId()!=null) {
								parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(), MathUtils.sub(btl.getTradeInMoney(), btl.getTradeOutMoney(), 2), 2));
								fcDao.update(parentOne.getParentCourseId());
							}
						}
						//更新银行账
						btl.setVoucherNumber(voucherNum);
						btl.setIsCheck(-1);
						btl.setOperMark(StringUtils.isBlank(btl.getOperMark())?operMark:btl.getOperMark()+","+operMark);
						btl.setOperNote(btl.getOperNote()+Util.getOperInfo(LU.getLRealName(request, redis), "生成凭证"));
						btlDao.update(btl);
					}
					//添加对方科目交易记录
					String [] faceIds=null;
					double faceGath=0;//对方科目借方金额
					double facePay=0;//对方科目贷方金额
					FeeCourse face=null;
					for (int i = 0; i < faceInfos.length; i++) {
						faceIds=faceInfos[i].split("=");
						face=fcDao.findByField("id", Long.valueOf(faceIds[2]));//对方科目
						faceGath=Double.valueOf(faceIds[4]);
						facePay=Double.valueOf(faceIds[5]);
						FeeCourseTrade fct=new FeeCourseTrade();
						fct.setUnitNum(LU.getLUnitNum(request, redis));
						if(StringUtils.isNotBlank(faceIds[0])) {
							staff=staffDao.findByField("id", Long.valueOf(faceIds[0]));//报销人
							fct.setReimUserId(staff.getBaseUserId());
						}
						if(StringUtils.isNotBlank(faceIds[1]))fct.setPlateNum(faceIds[1]);
						fct.setFeeCourseId(face);
						if(StringUtils.isNotBlank(faceIds[3]))fct.setRemark(faceIds[3]);
						fct.setGathMoney(faceGath);
						fct.setPayMoney(facePay);
						fct.setAddTime(new Date());
						fct.setVoucherNum(voucherNum);
						fctlist.add(fct);
						//更新对应科目余额
						face.setBalance(MathUtils.add(face.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
						fcDao.update(face);
						if(fc.getParentCourseId()!=null) {//更新父级科目余额
							FeeCourse parentOne=fc.getParentCourseId();
							fc.getParentCourseId().setBalance(MathUtils.add(parentOne.getBalance(), MathUtils.sub(faceGath,facePay, 2), 2));
							fcDao.update(parentOne);
							if(parentOne.getParentCourseId()!=null) {
								parentOne.getParentCourseId().setBalance(MathUtils.add(parentOne.getParentCourseId().getBalance(),MathUtils.sub(faceGath,facePay, 2), 2));
								fcDao.update(parentOne.getParentCourseId());
							}
						}
					}
					//生成凭证
					ReimburseList obj=new ReimburseList();
					obj.setUnitNum(LU.getLUnitNum(request, redis));
					obj.setCourseTrades(fctlist);
					obj.setGainTime(DateUtils.strToDate(gainTime));
					obj.setVoucherNum(voucherNum);
					obj.setTotalMoney(tradeMoney);
					obj.setIsCheck(2);
					obj.setAddTime(new Date());
					obj.setReqsrc(reqsrc);
					obj.setOperMark(operMark);
					obj.setOperNote(LU.getLRealName(request, redis)+"[添加]");
					reimDao.save(obj);
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
