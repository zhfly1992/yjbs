package com.fx.service.impl.company;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusType;
import com.fx.commons.utils.enums.RegWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.StaffState;
import com.fx.commons.utils.enums.UState;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.company.CompanyVehicleDao;
import com.fx.dao.company.StaffDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.permi.Dept;
import com.fx.service.company.StaffService;

@Service
@Transactional
public class StaffServiceImpl extends BaseServiceImpl<Staff, Long> implements StaffService {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	/** 员工-数据源 */
	@Autowired
	private StaffDao	staffDao;
	
	@Autowired
	private CompanyCustomDao companyCustomDao;
	
	@Autowired
	private CompanyVehicleDao companyVehicleDao;



	@Override
	public ZBaseDaoImpl<Staff, Long> getDao() {
		return staffDao;
	}

	@Autowired
	private BaseUserDao			baseUserdao;




	@Override
	public Map<String, Object> findStaffList(ReqSrc reqsrc, String page, String rows, String unitNum, String find) {
		String logtxt = U.log(log, "获取-员工-分页列表", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			/***** 参数--验证--begin *****/
			if (fg)
				fg = U.valPageNo(map, page, rows, "用户");
			/***** 参数--验证--end ******/

			if (fg) {
				if (StringUtils.isEmpty(find)) {
					U.log(log, "[查询关键字]为空");
				} else {
					find = find.trim();

					U.log(log, "[查询关键字] find=" + find);
				}
			}

			if (fg) {
				Page<Staff> pd = staffDao.findStaffList(reqsrc, page, rows, unitNum, find);
				U.setPageData(map, pd);

				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);

				U.setPut(map, 1, "请求数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> subStaffAdup(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum, Staff staff) {

		return null;
	}



	/**
	 * @see com.fx.service.company.StaffService#subStaffAdd(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest, com.fx.entity.company.Staff)
	 */
	@Override
	public Map<String, Object> subStaffAdd(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,CompanyUser companyUser) {

		String logtxt = U.log(log, "新增-员工", reqsrc);
		Map<String, Object> map = new HashMap<>();
		boolean fg = true;
		
		//入职公司不为空，根据id获得公司信息
		String entryCompany = jsonObject.getString("entryCompany");
		if (!StringUtils.isBlank(entryCompany)) {
			CompanyCustom entryCom = companyCustomDao.findByField("id", Long.parseLong(entryCompany));
			jsonObject.put("entryCompany", entryCom);
		}

		//获取当前公司的公司名称，用于添加员工时员工为当前公司；
		String currentCompany = companyUser.getCompanyName();
		Staff staff = jsonObject.toJavaObject(Staff.class);
		
		try {
			if (fg) {
				if (StringUtils.isBlank(staff.getBaseUserId().getPhone())) {
					U.logFalse(log, "phone为空");
					fg = U.setPutFalse(map, 0, "phone为空");
				}
			}
			if (fg) {
				if (!FV.isPhone(staff.getBaseUserId().getPhone())) {
					U.logFalse(log, "phone格式不正确");
					fg = U.setPutFalse(map, 0, "phone格式不正确");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(staff.getUnitNum())) {
					U.logFalse(log, "unintNum为空");
					fg = U.setPutFalse(map, 0, "unintNum为空");
				}
			}


			String phone = staff.getBaseUserId().getPhone();
			String unitNum = staff.getUnitNum();
			String uname = null;
			BaseUser baseUser = null;
			if (fg) {
				// 检查是否存在基类
				baseUser = baseUserdao.findByPhone(phone);
				if (baseUser == null) {
					// 添加基类
					baseUser = addBaseUser(phone, staff.getBaseUserId().getRealName());
					if (baseUser == null) {
						U.logFalse(log, "公司-新增员工-新增基类失败");
						fg = U.setPutFalse(map, 0, "公司-新增员工-新增基类失败");
					}
				}
			}
			if (fg) {
				uname = baseUser.getUname();
				// 检查是否已经在companyCustom表中
				CompanyCustom checkIfExists = companyCustomDao.checkIfExists(reqsrc, unitNum, uname);
				if (checkIfExists == null) {
					CompanyCustom addCompanyCustom;
					if (staff.getEntryCompany() != null) {
						addCompanyCustom = addCompanyCustom(baseUser, unitNum, staff.getEntryCompany().getUnitName(),
								CusType.COMPANY, 0, "员工", jsonObject.getString("serviceMan"),
								jsonObject.getString("recomMan"));
					}
					else{
						addCompanyCustom = addCompanyCustom(baseUser, unitNum, currentCompany,
								CusType.COMPANY, 0, "员工", jsonObject.getString("serviceMan"),
								jsonObject.getString("recomMan"));
					}
		
					if (addCompanyCustom == null) {
						U.logFalse(log, "公司-新增员工-新增客户失败");
						fg = U.setPutFalse(map, 0, "公司-新增员工-新增客户失败");
					}
				} else if (checkIfExists.getIsDel() == 1) {
					// companyCustom表中已有记录，处于删除状态，重新添加
					boolean reAdd = companyCustomDao.reAdd(checkIfExists.getId());
					if (!reAdd) {
						U.logFalse(log, "公司-新增员工-新增客户失败");
						fg = U.setPutFalse(map, 0, "公司-新增员工-新增客户失败");
					}
				}
			}
			if (fg) {

				Staff checkIfExists = staffDao.checkIfExists(reqsrc, unitNum, uname);
				if (checkIfExists != null) {
					// isdel为 ，被删除了
					if (checkIfExists.getIsDel() == 1) {
						U.log(log, "新增-员工-该员工已存在，处于删除状态");
						boolean reAdd = staffDao.reAdd(checkIfExists.getId());
						if (reAdd) {
							U.log(log, "新增-员工-该员工已存在，重新添加成功");
							U.setPut(map, 1, "新增员工成功");
							fg = false;
						} else {
							U.logFalse(log, "新增-员工-该员工已存在，重新添加失败");
							fg = U.setPutFalse(map, 0, "新增员工失败,该员工已存在");
						}
					} else {
						// isdel为0，存在
						U.logFalse(log, "新增-员工-该员工已存在,且未处于删除状态");
						fg = U.setPutFalse(map, 0, "该员工已存在,且未处于删除状态");
					}
				}
			}
			if (fg) {
				staff.setAddTime(new Date());
				staff.setBaseUserId(baseUser);
				staffDao.save(staff);
				U.log(log, "公司-新增员工-成功");
				U.setPut(map, 1, "新增员工成功");
			}

			return map;
		} catch (Exception e) {

			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			return map;
		}
	}



	private BaseUser addBaseUser(String phone, String realName) {
		// 重新生成加密登录
		try {
			BaseUser baseUser = new BaseUser();
			PasswordHelper passHelper = new PasswordHelper();

			baseUser.setUname(UT.createUname());
			baseUser.setPhone(phone);
			baseUser.setRealName(realName);
			baseUser.setSalt(baseUser.getUname());
			baseUser.setLpass(passHelper.encryptPassword("", baseUser.getSalt()));
			baseUser.setRegWay(RegWay.PC_COMPANY);
			baseUser.setUstate(UState.NORMAL);
			baseUser.setAtime(new Date());
			baseUserdao.save(baseUser);
			U.log(log, "公司-新增员工-新增基类成功");
			return baseUser;
		} catch (Exception e) {
			U.log(log, "公司-新增员工-新增基类异常", e);
			e.printStackTrace();
			return null;
		}
	}



	private CompanyCustom addCompanyCustom(BaseUser baseUser, String unitNum, String unitName, CusType cusType,
			int isDepend, String cusRole, String serviceMan, String recomMan) {
		try {
			CompanyCustom companyCustom = new CompanyCustom();
			companyCustom.setBaseUserId(baseUser);
			companyCustom.setAddTime(new Date());
			companyCustom.setCusRole(cusRole);
			companyCustom.setCusType(cusType);
			companyCustom.setIsDepend(isDepend);
			companyCustom.setServiceMan(serviceMan);
			companyCustom.setRecomMan(recomMan);
			companyCustom.setUnitNum(unitNum);
			companyCustom.setUnitName(unitName);
			companyCustomDao.save(companyCustom);
			U.log(log, "公司-新增员工-新增客户表成功");
			return companyCustom;
		} catch (Exception e) {
			U.log(log, "公司-新增员工-新增客户表异常", e);
			e.printStackTrace();
			return null;
		}
	}



	/**
	 * @see com.fx.service.company.StaffService#subStaffUpdate(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Map<String, Object> subStaffUpdate(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {

		String logtxt = U.log(log, "编辑-员工", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;

		try {
			
			String entryCompany = jsonObject.getString("entryCompany");
			if (!StringUtils.isBlank(entryCompany)) {
				CompanyCustom entryCom = companyCustomDao.findByField("id", Long.parseLong(entryCompany));
				jsonObject.put("entryCompany", entryCom);
			}
			Staff staff = jsonObject.toJavaObject(Staff.class);

			if (fg) {

				// 获取原有staff，用于判断是否需要删除图片
				Staff originalStaff = staffDao.findByField("id", staff.getId());
				// 若是驾驶员，进行图片删除判断
				if (originalStaff.getIsDriver() == 1) {
					if (null != originalStaff.getIdCardBackImg() && null != staff.getIdCardBackImg()) {
						if (!originalStaff.getIdCardBackImg().equals(staff.getIdCardBackImg())) {
							LU.deletePic(originalStaff.getIdCardBackImg());
						}
					}

					if (null != originalStaff.getIdCardFrontImg() && null != staff.getIdCardFrontImg()) {
						if (!originalStaff.getIdCardFrontImg().equals(staff.getIdCardFrontImg())) {
							LU.deletePic(originalStaff.getIdCardFrontImg());
						}
					}

					if (null != originalStaff.getCertificateImg() && null != staff.getCertificateImg()) {
						if (!originalStaff.getCertificateImg().equals(staff.getCertificateImg())) {
							LU.deletePic(originalStaff.getCertificateImg());
						}
					}

					if (null != originalStaff.getDriveImg() && null != staff.getDriveImg()) {
						if (!originalStaff.getDriveImg().equals(staff.getDriveImg())) {
							LU.deletePic(originalStaff.getDriveImg());
						}
					}
				}

				// 防止a different object with the same identifier value was
				// already associated with the session
				staffDao.getCurrentSession().clear();
				staffDao.update(staff);

				U.log(log, "修改-员工-成功");
				U.setPut(map, 1, "修改成功");

			}
		} catch (Exception e) {

			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			U.setPutEx(map, log, e, "修改员工信息异常");
		}

		return map;
	}



	/**
	 * @see com.fx.service.company.StaffService#subStaffDelete(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Map<String, Object> subStaffDelete(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {

		String logtxt = U.log(log, "删除-员工", reqsrc);
		Map<String, Object> map = new HashMap<>();

		boolean fg = true;
		try {
			if (!jsonObject.containsKey("id")) {
				U.logFalse(log, "删除-员工-失败，传参中需包含id");
				fg = U.setPutFalse(map, 0, "传参中需包含id");
			}
			if (fg) {
				long id = jsonObject.getLong("id");
				boolean deleteById = staffDao.deleteById(id, reqsrc);
				if (deleteById) {
					U.log(log, "删除-员工-成功");
					U.setPut(map, 1, "删除成功");
				} else {
					U.logFalse(log, "删除-员工-失败");
					U.setPutFalse(map, 0, "删除失败");
				}

			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
			U.setPutEx(map, log, e, "删除员工信息异常");
		}

		return map;

	}



	@Override
	public Map<String, Object> findStaffById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-员工-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("id")) {
				U.setPut(map, 0, "id参数不为空");
				return map;
			}
			Staff staff = staffDao.findByField("id", jsonObject.getLong("id"));
			if (staff != null) {
				U.log(log, "通过id查找员工成功");
				map.put("data", staff);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Dept.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找员工失败");
				U.setPutFalse(map, 0, "查找失败");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}



	@Override
	public Map<String, Object> getAllStaff(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-员工姓名下拉框", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("unitNum")) {
				U.setPutFalse(map, 0, "需包含unitNum");
				return map;
			}
			String unitNum = jsonObject.getString("unitNum");
			List<BaseUser> result = new ArrayList<>();
			List<BaseUser> findhqlList = staffDao.findhqlList(
					"select BU from Staff S left join S.baseUserId as BU with S.unitNum = ?0 and S.isDel = 0 and S.staffState != ?1",
					unitNum, StaffState.LEAVE);
			if (findhqlList.size() > 0) {
				for (BaseUser baseUser : findhqlList) {
					if (baseUser != null) {
						result.add(baseUser);
					}
				}
			}
			map.put("data", result);

			// 字段过滤
			Map<String, Object> fmap = new HashMap<String, Object>();
			fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
			map.put(QC.FIT_FIELDS, fmap);

			U.setPut(map, 1, "请求数据成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> getDriverList(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String unitNum) {
		String logtxt = U.log(log, "查询-驾驶员下拉框", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.log(log, "查询-驾驶员下拉框-unitNum为空");
				U.setPutFalse(map, 0, "获取unitNum出错");
				return map;
			}
			//获取已经被设置为驾驶员的uname
			List<CompanyVehicle> companyVehicles = companyVehicleDao.findhqlList("from CompanyVehicle where unitNum = ?0 and baseUserId is not null", unitNum);
			Set<String> unameSet = new HashSet<>();
			for(CompanyVehicle cv:companyVehicles){
				unameSet.add(cv.getBaseUserId().getUname());
			}
			
			List<BaseUser> findhqlList = staffDao.findhqlList(
					"select BU from Staff S left join S.baseUserId as BU with S.unitNum = ?0 and S.isDriver = 1 and S.isDel = 0",
					unitNum);
			List<BaseUser> result = new ArrayList<BaseUser>();
			if (findhqlList.size() > 0) {
				for (BaseUser baseUser : findhqlList) {
					 //排除已绑定车辆的驾驶员
					if (baseUser != null && !unameSet.contains(baseUser.getUname())) {
						result.add(baseUser);
					}
				}
			}
			map.put("data", result);

			// 字段过滤
			Map<String, Object> fmap = new HashMap<String, Object>();
			fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
			map.put(QC.FIT_FIELDS, fmap);

			U.setPut(map, 1, "请求数据成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}

		return map;
	}



	@Override
	public Map<String, Object> checkPhoneBeforeAdd(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String phone) {
		String logtxt = U.log(log, "查询-手机号是否存在基础用户", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			if (StringUtils.isBlank(phone)) {
				U.log(log, "查询-手机号是否存在基础用户-传入手机号为空");
				U.setPutFalse(map, 0, "传入手机号为空");
			}
			else{
				BaseUser baseUser = baseUserdao.findByField("phone", phone);
				if (baseUser == null) {
					U.log(log, "查询-手机号是否存在基础用户-不存在该手机号");
					U.setPut(map, 1, "手机号不存在");
				}
				else{
					U.log(log, "查询-手机号是否存在基础用户-手机号存在");
					U.setPutFalse(map, 0, "该用户已存在");
					map.put("data", baseUser);
					Map<String, Object> fmap = new HashMap<String, Object>();
					fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
					map.put(QC.FIT_FIELDS, fmap);
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}



	@Override
	public Map<String, Object> staffLeave(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String id, String leaveInfo) {
		String logtxt = U.log(log, "后台-员工-离职", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		Staff staff = null;
		try {
			if (fg) {
				if (StringUtils.isBlank(id)) {
					U.log(log, "后台-员工-离职-传入员工id为空");
					fg = U.setPutFalse(map, 0, "传入id为空");
				}
			}
			if (fg) {
				if (StringUtils.isBlank(leaveInfo)) {
					U.log(log, "后台-员工-离职-传入离职信息为空");
					fg = U.setPutFalse(map, 0, "传入离职信息为空");
				}
			}
			if (fg) {
				staff = staffDao.findByField("id", Long.parseLong(id));
				if (staff == null) {
					U.log(log, "后台-员工-离职-查询不到员工信息，id:" + id);
					fg = U.setPutFalse(map, 0, "查询员工信息错误，检查传入id");
				}
			}
			if (fg) {
				if (staff.getStaffState() == StaffState.LEAVE) {
					U.log(log, "后台-员工-离职-该员工已离职");
					fg = U.setPutFalse(map, 0, "该员工已离职");
				}
			}
			if (fg) {
				staff.setLeaveInfo(leaveInfo);
				staff.setStaffState(StaffState.LEAVE);
				staffDao.update(staff);
				U.log(log, "后台-员工-离职-操作成功");
				U.setPut(map, 1, "离职成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		return map;
	}

}
