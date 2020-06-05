package com.fx.dao.company;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.tools.U;
import com.fx.dao.cus.BaseUserDao;
import com.fx.entity.company.CompanyCustom;
import com.fx.entity.cus.BaseUser;

@Repository
public class CompanyCustomDao extends ZBaseDaoImpl<CompanyCustom, Long> {

	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	/** 用户基类-数据源 */
	@Autowired
	private BaseUserDao	buDao;



	/**
	 * 获取-单位客户-分页列表
	 * 
	 * @param reqsrc
	 *            请求来源
	 * @param page
	 *            页码
	 * @param rows
	 *            页大小
	 * @param find
	 *            查询关键字
	 * @param unitName
	 *            单位全称/简称
	 * @param serviceMan
	 *            业务员
	 * @param recomMan
	 *            推荐人
	 * @return Page<T> 分页数据
	 */

	public Page<CompanyCustom> findCompanyCusList(ReqSrc reqsrc, String page, String rows, String unitNum, String find,
			String unitName, String serviceMan, String recomMan) {
		String logtxt = U.log(log, "获取-单位客户-分页列表", reqsrc);

		Page<CompanyCustom> pd = new Page<CompanyCustom>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try {
			if (ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			} else {
				comps.add(new Compositor("addTime", CompositorType.DESC));
				filts.add(new Filtration(MatchType.EQ, unitNum, "unitNum"));// 单位编号
				filts.add(new Filtration(MatchType.EQ, 0, "isDel"));

				if (StringUtils.isNotBlank(find)) {
					List<String> uname = buDao.findUnameByUnameOrPhone(find);
					List<Filtration> unameFilt = new ArrayList<Filtration>();
					if (uname.size() > 0) {// 匹配用户名
						for (String name : uname) {
							unameFilt.add(new Filtration(MatchType.EQ, name, "baseUserId.uname"));
						}
						// filts.add(new Filtration(MatchType.EQ, uname,
						// "baseUserId.uname"));
						filts.add(new Filtration(MatchType.OR, unameFilt, "baseUserId.uname"));
					} else {
						filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
					}
				}
				if (StringUtils.isNotBlank(unitName)) {
					// 单位全称/简称
					filts.add(new Filtration(MatchType.LIKE, unitName, "unitName", "unitSimple"));
				}
				if (StringUtils.isNotBlank(serviceMan)) {
					// 业务员
					filts.add(new Filtration(MatchType.LIKE, serviceMan, "serviceMan"));
				}
				if (StringUtils.isNotBlank(recomMan)) {
					// 推荐人
					filts.add(new Filtration(MatchType.LIKE, recomMan, "recomMan"));
				}
			}

			/////////////////// --分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); // 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); // 排序条件
			pd.setFiltrations(filts); // 查询条件
			pd = findPageByOrders(pd); // 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}

		return pd;
	}



	/**
	 * 
	 * @Description:新增单位客户时查看是否已经存在
	 * @param reqsrc
	 * @param unim
	 * @param uname
	 * @return
	 */
	public CompanyCustom checkIfExists(ReqSrc reqsrc, String unim, String uname) {
		String logtxt = U.log(log, "新增-单位客户-校验是否添加过", reqsrc);
		try {

			Session currentSession = getCurrentSession();
			Query createQuery = currentSession.createQuery(
					"from CompanyCustom where base_user_id = '" + uname + "'and unit_num = '" + unim + "'");
			CompanyCustom companyCustom = (CompanyCustom) createQuery.uniqueResult();
			return companyCustom;

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return null;
		}

	}



	public boolean reAdd(Long id) {
		String logtxt = U.log(log, "新增-单位客户-将被删除的重新添加");
		try {
			Session session = getCurrentSession();
			// session.beginTransaction();
			session.createQuery("update CompanyCustom set is_del = 0 where id = " + id).executeUpdate();
			// session.getTransaction().commit();
			// session.close();
			return true;

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * 获取-订单业务员姓名
	 * 
	 * @param unitNum
	 *            单位编号
	 * @param uname
	 *            客户账号
	 * @return 业务员姓名
	 */
	public String getOrderServeMan(String unitNum, String uname) {
		String logtxt = U.log(log, "获取-订单业务员");

		String res = null;

		try {
			String hql = "from CompanyCustom where unitNum=?0 and baseUserId.uname=?1";
			CompanyCustom cpyCus = findObj(hql, unitNum, uname);
			if (cpyCus != null) {
				res = cpyCus.getServiceMan();
			}
		} catch (Exception e) {
			e.printStackTrace();
			U.log(log, logtxt, e);
		}

		return res;
	}



	/**
	 * 获取-所有客户基础信息
	 * 
	 * @param unitNum
	 *            单位编号
	 */
	public BaseUser[] getAllCompanyCustomBaseInfo(String unitNum) {
		String logtxt = U.log(log, "获取-所有客户信息");

	
		
		try {
			String hql = "from CompanyCustom where unitNum=?0 and isDel = 0";
			List<CompanyCustom> companyCustomList = findhqlList(hql, unitNum);
			if (companyCustomList.size() > 0) {
				BaseUser[] bUsers= new BaseUser[companyCustomList.size()];
				for(int i = 0; i< companyCustomList.size(); i++){
					bUsers[i] = companyCustomList.get(i).getBaseUserId();
				}
				return bUsers;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			U.log(log, logtxt, e);
			return null;
		}
	}

}
