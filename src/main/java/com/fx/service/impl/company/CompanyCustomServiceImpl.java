package com.fx.service.impl.company;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.CusType;
import com.fx.commons.utils.enums.RegWay;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.enums.UState;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.CompanyCustomDao;
import com.fx.dao.cus.BaseUserDao;
import com.fx.dao.cus.CompanyUserDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.service.company.CompanyCustomService;

@Service
@Transactional
public class CompanyCustomServiceImpl<T> extends BaseServiceImpl<CompanyCustom, Long> implements CompanyCustomService {
	/** 日志记录 */
	private Logger				log	= LogManager.getLogger(this.getClass());

	/** 员工-数据源 */
	@Autowired
	private CompanyCustomDao	companyCusDao;
	@Autowired
	private BaseUserDao			baseUserdao;
	@Autowired
	private CompanyUserDao		companyUserDao;



	@Override
	public ZBaseDaoImpl<CompanyCustom, Long> getDao() {
		return companyCusDao;
	}



	@Override
	public Map<String, Object> findCompanyCusList(ReqSrc reqsrc, String page, String rows, String unitNum, String find,
			String unitName, String serviceMan, String recomMan) {
		String logtxt = U.log(log, "获取-单位客户-分页列表", reqsrc);

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
				if (StringUtils.isEmpty(unitName)) {
					U.log(log, "[查询单位名称]为空");
				} else {
					unitName = unitName.trim();

					U.log(log, "[查询单位名称] unitName=" + unitName);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(serviceMan)) {
					U.log(log, "[查询业务员]为空");
				} else {
					serviceMan = serviceMan.trim();

					U.log(log, "[查询业务员] serviceMan=" + serviceMan);
				}
			}
			if (fg) {
				if (StringUtils.isEmpty(recomMan)) {
					U.log(log, "[查询推荐人]为空");
				} else {
					recomMan = recomMan.trim();

					U.log(log, "[查询推荐人] recomMan=" + recomMan);
				}
			}

			if (fg) {
				Page<CompanyCustom> pd = companyCusDao.findCompanyCusList(reqsrc, page, rows, unitNum, find, unitName,
						serviceMan, recomMan);

				U.setPageData(map, pd);

				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
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
	public Map<String, Object> subCompanyCusAdup(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum, JSONObject jsonObject) {
		CompanyCustom companyCus = jsonObject.toJavaObject(CompanyCustom.class);
		// 判断是否是供应商
		if (companyCus.getIsSupply() == 0) {
			// 不是供应商，但是填写了供应商信息
			companyCus.setServiceContent(null);
			companyCus.setUnitSimple(null);
			companyCus.setBusinessNum(null);
			companyCus.setIdCard(null);
			// 有图片信息，则删除图片
			if (!StringUtils.isBlank(companyCus.getBusinessImg())) {
				LU.deletePic(companyCus.getBusinessImg());
			}
			if (!StringUtils.isBlank(companyCus.getIdCardBackImg())) {
				LU.deletePic(companyCus.getIdCardBackImg());
			}
			if (!StringUtils.isBlank(companyCus.getIdCardFrontImg())) {
				LU.deletePic(companyCus.getIdCardFrontImg());
			}
			companyCus.setBusinessImg(null);
			companyCus.setIdCardBackImg(null);
			companyCus.setIdCardFrontImg(null);
		}
		String opt = request.getHeader("opt");
		String logtxt = U.log(log, "添加-修改-客户", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		// unitNum = companyCus.getUnitNum();
		try {

			// 必填参数校验,先写这么多，功能完善在补充
			if (StringUtils.isBlank(companyCus.getUnitNum())) {
				U.logFalse(log, "unitNum为空");
				U.setPutFalse(map, 0, "unitNum为空");
				return map;
			}
			if (StringUtils.isBlank(companyCus.getUnitName())) {
				U.logFalse(log, "unitName为空");
				U.setPutFalse(map, 0, "unitName为空");
				return map;
			}
			if (StringUtils.isBlank(companyCus.getBaseUserId().getPhone())) {
				U.logFalse(log, "phone为空");
				U.setPutFalse(map, 0, "phone为空");
				return map;
			}
			if (!FV.isPhone(companyCus.getBaseUserId().getPhone())) {
				U.logFalse(log, "手机号码格式错误");
				U.setPutFalse(map, 0, "手机号码格式错误");
				return map;
			}

			// 获取传入手机号
			String phone = companyCus.getBaseUserId().getPhone();

			// 添加操作
			if (opt.equals("save")) {

				return addCusetom(reqsrc, unitNum, companyCus, map, phone);

			}
			// 修改操作
			else {

				// 若是更换了图片，需要将旧图片删除
				CompanyCustom originalCompusCus = companyCusDao.findByField("id", companyCus.getId());
				// 之前是供应商，现在还是供应商，需要判断是否图片有更改
				if (originalCompusCus.getIsSupply() == 1 && companyCus.getIsSupply() == 1) {
					if (null != originalCompusCus.getBusinessImg() && null != companyCus.getBusinessImg()) {
						if (!originalCompusCus.getBusinessImg().equals(companyCus.getBusinessImg())) {
							// 更换了图片，将原有图片删除
							LU.deletePic(originalCompusCus.getBusinessImg());
						}
					}

					if (null != originalCompusCus.getIdCardBackImg() && null != companyCus.getIdCardBackImg()) {
						if (!originalCompusCus.getIdCardBackImg().equals(companyCus.getIdCardBackImg())) {
							// 更换了图片，将原有图片删除
							LU.deletePic(originalCompusCus.getIdCardBackImg());
						}
					}

					if (null != originalCompusCus.getIdCardFrontImg() && null != companyCus.getIdCardFrontImg()) {
						if (!originalCompusCus.getIdCardFrontImg().equals(companyCus.getIdCardFrontImg())) {
							// 更换了图片，将原有图片删除
							LU.deletePic(originalCompusCus.getIdCardFrontImg());
						}
					}
				}
				// 之前是供应商，现在不是供应商，需要删除之前的图片
				if (originalCompusCus.getIsSupply() == 1 && companyCus.getIsSupply() == 0) {
					if (!StringUtils.isBlank(originalCompusCus.getBusinessImg())) {
						LU.deletePic(originalCompusCus.getBusinessImg());
					}
					if (!StringUtils.isBlank(originalCompusCus.getIdCardBackImg())) {
						LU.deletePic(originalCompusCus.getIdCardBackImg());
					}
					if (!StringUtils.isBlank(originalCompusCus.getIdCardFrontImg())) {
						LU.deletePic(originalCompusCus.getIdCardFrontImg());
					}
				}
				companyCusDao.getCurrentSession().clear();
				companyCusDao.update(companyCus);
				U.log(log, "编辑-公司客户-完成");
				U.setPut(map, 1, "编辑成功");
			}

			return map;
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			return map;
		}
	}



	/**
	 * @Description:
	 * @param reqsrc
	 * @param unitNum
	 * @param companyCus
	 * @param map
	 * @param phone
	 * @return
	 */

	private Map<String, Object> addCusetom(ReqSrc reqsrc, String unitNum, CompanyCustom companyCus,
			Map<String, Object> map, String phone) {
		BaseUser baseUser = null;
		// 添加时需要判断基类是否存在该手机号，不存在则在基类添加一条数据
		baseUser = baseUserdao.findByField("phone", phone);
		if (baseUser != null) {
			U.log(log, "用户[" + phone + "]已存在，仅需添加company_custom信息");

			// 验证客户是否已存在表中
			CompanyCustom checkIfExists = companyCusDao.checkIfExists(reqsrc, unitNum, baseUser.getUname());

			if (checkIfExists != null) {
				// 已经添加
				if (checkIfExists.getIsDel() != 0) {
					// 已被添加，被删除，将isdel置为0
					boolean reAdd = companyCusDao.reAdd(checkIfExists.getId());
					if (reAdd) {
						U.log(log, "保存-公司客户-完成" + "用户[" + phone + "]");
						U.setPut(map, 1, "添加成功");
						return map;
					} else {
						U.logFalse(log, "保存-公司客户-失败" + "用户[" + phone + "]");
						U.setPut(map, 0, "重新添加失败");
						return map;
					}
				} else {
					// 已添加，且没被删除
					U.logFalse(log, "保存-公司客户-失败,用户已存在" + "用户[" + phone + "]");
					U.setPut(map, 0, "用户已存在");
					return map;
				}
			}

			// 不存在则添加
			companyCus.setBaseUserId(baseUser);
			companyCus.setAddTime(new Date());
			// 查询该用户是否为company_user,若是，添加相应公司信息
			CompanyUser findByField = companyUserDao.findByField("baseUserId.uname", baseUser.getUname());
			if (findByField != null) {
				companyCus.setAddress(findByField.getCompanyAddr());
				companyCus.setAddressLonlat(findByField.getCompanyLnglat());
				companyCus.setUnitName(findByField.getCompanyName());
				U.log(log, "保存-公司客户-用户[" + phone + "]为公司用户—读取客户公司信息");
				companyCusDao.save(companyCus);
				U.log(log, "保存-公司客户-完成" + "用户[" + phone + "]");
				U.setPut(map, 1, "添加成功");
				return map;
			} else {
				U.log(log, "保存-公司客户-客户[" + phone + "]为个人用户");
				companyCusDao.save(companyCus);
				U.log(log, "保存-公司客户-完成" + "用户[" + phone + "]");
				U.setPut(map, 1, "添加成功");
				return map;
			}
		} else {
			U.log(log, "用户[" + phone + "]不存在，需添加company_custom信息和用户基类");

			// 重新生成加密登录
			PasswordHelper passHelper = new PasswordHelper();
			baseUser = new BaseUser();
			baseUser.setUname(UT.createUname());
			baseUser.setPhone(phone);
			baseUser.setRealName(companyCus.getBaseUserId().getRealName());
			baseUser.setSalt(baseUser.getUname());
			baseUser.setLpass(passHelper.encryptPassword("", baseUser.getSalt()));
			baseUser.setRegWay(RegWay.PC_COMPANY);
			baseUser.setUstate(UState.NORMAL);
			baseUser.setAtime(new Date());
			baseUserdao.save(baseUser);
			U.log(log, "保存-用户基类-完成");

			companyCus.setBaseUserId(baseUserdao.findByField("phone", phone));
			companyCus.setAddTime(new Date());
			companyCusDao.save(companyCus);
			U.log(log, "保存-公司客户-完成");
			U.setPut(map, 1, "添加成功");
			return map;
		}
	}



	/**
	 * @see com.fx.service.company.CompanyCustomService#deleteCompanyCus(com.fx.commons.utils.enums.ReqSrc,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.http.HttpServletRequest,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Map<String, Object> deleteCompanyCus(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject) {

		String logtxt = U.log(log, "删除-公司客户", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			if (!jsonObject.containsKey("id")) {
				U.logFalse(log, "删除-公司客户-失败-参数不包含id");
				U.setPutFalse(map, 0, "id参数不为空");
				return map;
			}
			long id = jsonObject.getLong("id");
			int res = companyCusDao.batchExecute("update CompanyCustom set is_del = 1 where id = ?0", id);
			if (res != 0) {
				U.log(log, "删除-公司客户-成功");
				U.setPut(map, 1, "删除成功");
				return map;
			} else {
				U.logFalse(log, "删除-公司客户-失败");
				U.setPutFalse(map, 0, "删除失败");
				return map;
			}
			// Session openSession = companyCusDao.openSession();
			// Transaction transaction = openSession.getTransaction();
			// transaction.begin();
			// @SuppressWarnings("rawtypes")
			// Query query = openSession.createQuery("update CompanyCustom set
			// is_del = 1 where id = :id");
			// query.setParameter("id", jsonObject.getLong("id"));
			// query.executeUpdate();
			// transaction.commit();
			// openSession.close();

		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			return map;
		}
	}



	@Override
	public Map<String, Object> findCompanyCusById(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {

		String logtxt = U.log(log, "查询-单位客户-by id", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("id")) {
				U.setPutFalse(map, 0, "id参数不为空");
				return map;
			}
			CompanyCustom companyCustom = companyCusDao.findByField("id", jsonObject.getLong("id"));
			if (companyCustom != null) {
				U.log(log, "通过id查找公司客户成功");
				map.put("data", companyCustom);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.setPut(map, 1, "查找成功");
			} else {
				U.logFalse(log, "通过id查找公司客户失败");
				U.setPutFalse(map, 0, "查找失败");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);

		}
		return map;
	}



	@Override
	public Map<String, Object> getCompanyCusIsDepend(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject) {
		String logtxt = U.log(log, "查询-单位客户-挂靠公司的客户", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!jsonObject.containsKey("unitNum")) {
				U.setPutFalse(map, 0, "传入参数需包含unitNum");
				return map;
			}
			Map<String, Object> query = new HashMap<>();
			query.put("unitNum", jsonObject.getString("unitNum"));
			query.put("isDepend", 1);
			List<Object> findList = companyCusDao.findList(
					"select CC.id,CC.unitName,CC.unitSimple from CompanyCustom CC where unitNum = :unitNum and isDepend =:isDepend and isDel = 0",
					query);

			List<Map<String, Object>> res = new ArrayList<>();
			for (Object object : findList) {

				if (object != null) {

					Object[] list = (Object[]) object;
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("id", list[0]);
					temp.put("unitName", list[1]);
					temp.put("unitSimple", list[2]);
					res.add(temp);
				}
			}
			map.put("data", res);
			U.setPut(map, 1, "查询-单位客户-挂靠公司的客户-成功");
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> checkPhoneExists(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String phone, String unitNum) {
		String logtxt = U.log(log, "新增-单位客户-查询手机号对应用户是否已存在", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(phone)) {
				U.logFalse(log, "新增-单位客户-查询手机号对应用户是否已存在-手机号为空");
				fg = U.setPutFalse(map, 0, "手机号为空");
			}
			if (fg) {
				if (StringUtils.isBlank(unitNum)) {
					U.logFalse(log, "新增-单位客户-查询手机号对应用户是否已存在-unitNum为空");
					fg = U.setPutFalse(map, 0, "unitNum为空");
				}
			}
			if (fg) {
				BaseUser baseUser = baseUserdao.findByField("phone", phone);
				if (baseUser == null) {
					U.log(log, "新增-单位客户-查询手机号对应用户是否已存在-不存在baseUser");
					U.setPut(map, 1, "手机号通过检验");
				} else {
					// 验证客户是否已存在表中
					CompanyCustom checkIfExists = companyCusDao.checkIfExists(reqsrc, unitNum, baseUser.getUname());
					if (checkIfExists == null) {
						U.log(log, "新增-单位客户-查询手机号对应用户是否已存在-不存在custom");
						U.setPut(map, 1, "手机号通过检验");
					} else {
						if (checkIfExists.getIsDel() == 1) {
							// 已删除，可以重新添加，通过检验
							U.log(log, "新增-单位客户-查询手机号对应用户是否已存在-存在custom,处于删除状态");
							U.setPut(map, 1, "手机号通过检验");
						} else {
							// 未删除，不通过检验
							U.logFalse(log, "新增-单位客户-查询手机号对应用户是否已存在-存在custom,不处于删除状态");
							U.setPutFalse(map, 0, "该客户已存在");
						}
					}
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> addPersonInCharge(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String id, String personInCharge) {
		String logtxt = U.log(log, "后台-单位客户-添加业务负责人", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(id)) {
				U.logFalse(log, "后台-单位客户-添加业务负责人-id为空");
				fg = U.setPutFalse(map, 0, "id为空");
			}
			if (fg) {
				if (StringUtils.isBlank(personInCharge)) {
					U.logFalse(log, "后台-单位客户-添加业务负责人-负责人为空");
					fg = U.setPutFalse(map, 0, "负责人为空");
				}
			}
			if (fg) {
				CompanyCustom companyCustom = companyCusDao.findByField("id", Long.parseLong(id));
				if (companyCustom == null) {
					U.logFalse(log, "后台-单位客户-添加业务负责人-无该客户信息");
					U.setPutFalse(map, 0, "查询不到客户信息，请确认id");
				} else {
					JSONArray jsonArray = JSONArray.parseArray(personInCharge);
					companyCustom.setPersonInCharge(jsonArray);
					companyCusDao.update(companyCustom);
					U.log(log, "后台-单位客户-添加业务负责人-success");
					U.setPut(map, 1, "添加成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getCustomBaseInfo(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum) {
		String logtxt = U.log(log, "获取-单位客户-所有客户基本信息", reqsrc);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.logFalse(log, "获取-单位客户-所有客户基本信息-unitNum为空");
				fg = U.setPutFalse(map, 0, "unitNum获取失败");
			}
			if (fg) {
				BaseUser[] baseInfos = companyCusDao.getAllCompanyCustomBaseInfo(unitNum);
				if (null == baseInfos) {
					U.logFalse(log, "获取-单位客户-所有客户基本信息-获取失败");
					U.setPutFalse(map, 0, "获取失败");
				} else {
					U.log(log, "获取-单位客户-所有客户基本信息-获取成功");
					map.put("data", baseInfos);
					// 字段过滤
					Map<String, Object> fmap = new HashMap<String, Object>();
					fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
					fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
					map.put(QC.FIT_FIELDS, fmap);
					U.setPut(map, 1, "获取成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}



	@Override
	public Map<String, Object> getCustomInfoByType(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, String unitNum, String cusType) {
		String logtxt = U.log(log, "获取-单位客户-指定类型的客户信息", reqsrc);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		try {
			if (StringUtils.isBlank(unitNum)) {
				U.logFalse(log, "获取-单位客户-指定类型的客户信息-获取失败-unitNum为空");
				fg = U.setPutFalse(map, 0, "获取失败");
			}
			if (fg) {
				if (StringUtils.isBlank(cusType)) {
					U.logFalse(log, "获取-单位客户-指定类型的客户信息-获取失败-cusType为空");
					fg = U.setPutFalse(map, 0, "获取失败");
				}
			}
			if (fg) {
				String hql = "from CompanyCustom where unitNum = ?0 and cusType = ?1 and isDel = 0";
				List<CompanyCustom> findhqlList = companyCusDao.findhqlList(hql, unitNum, CusType.valueOf(cusType));
				map.put("data", findhqlList);
				// 字段过滤
				Map<String, Object> fmap = new HashMap<String, Object>();
				fmap.put(U.getAtJsonFilter(BaseUser.class), new String[] {});
				fmap.put(U.getAtJsonFilter(Customer.class), new String[] {});
				map.put(QC.FIT_FIELDS, fmap);
				U.log(log, "获取-单位客户-指定类型的客户信息-获取成功");
				U.setPut(map, 1, "查询成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
		}
		return map;
	}
}
