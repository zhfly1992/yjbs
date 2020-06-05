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

import com.fx.entity.company.Staff;



@Repository
public class StaffDao extends ZBaseDaoImpl<Staff, Long> {
	/** 日志记录 */
	private Logger		log	= LogManager.getLogger(this.getClass());

	/** 用户基类-数据源 */
	@Autowired
	private BaseUserDao	buDao;



	/**
	 * 获取-员工-分页列表
	 * 
	 * @param reqsrc
	 *            请求来源
	 * @param page
	 *            页码
	 * @param rows
	 *            页大小
	 * @param find
	 *            查询关键字
	 * @return Page<T> 分页数据
	 */
	public Page<Staff> findStaffList(ReqSrc reqsrc, String page, String rows, String unitNum, String find) {
		String logtxt = U.log(log, "获取-员工-分页列表", reqsrc);

		Page<Staff> pd = new Page<Staff>();
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
						for(String name:uname){
							unameFilt.add(new Filtration(MatchType.EQ, name, "baseUserId.uname"));
						}
				//		filts.add(new Filtration(MatchType.EQ, uname, "baseUserId.uname"));
						filts.add(new Filtration(MatchType.OR,unameFilt,"baseUserId.uname"));
					}
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



//	public boolean update(Staff staff, ReqSrc reqsrc) {
//
//		String logtxt = U.log(log, "修改-员工-dao层", reqsrc);
//		try {
//			Session currentSession = getCurrentSession();
//			Transaction transaction = currentSession.getTransaction();
//			transaction.begin();
//			currentSession.update(staff);
//			transaction.commit();
//			currentSession.close();
//			return true;
//		} catch (Exception e) {
//			U.log(log, logtxt, e);
//			e.printStackTrace();
//			return false;
//		}
//
//	}



	public boolean deleteById(long id, ReqSrc reqsrc) {
		String logtxt = U.log(log, "删除-员工-dao层", reqsrc);
		try {
			Session session = getCurrentSession();
	//		Transaction transaction = session.getTransaction();
	//		transaction.begin();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("update Staff set is_del = 1 where id = :id");
			query.setParameter("id", id);
			query.executeUpdate();
	//		transaction.commit();
			return true;
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * 
	 * @Description:新增员工时查看是否已经存在
	 * @param reqsrc
	 * @param unim
	 * @param uname
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Staff checkIfExists(ReqSrc reqsrc, String unim, String uname) {
		String logtxt = U.log(log, "新增-员工-校验员工是否添加过", reqsrc);
		try {

			Session currentSession = getCurrentSession();
			Query createQuery = currentSession
					.createQuery("from Staff where base_user_id = '" + uname + "'and unit_num = '" + unim + "'");
			Staff staff = (Staff) createQuery.uniqueResult();
			return staff;

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return null;
		}
	}



	/**
	 * 
	 * @Description:新增员工时若是员工已存在表里，处于删除状态，重新添加
	 * @param id
	 * @return
	 */

	public boolean reAdd(Long id) {
		String logtxt = U.log(log, "新增-员工-将被删除的重新添加");
		try {
			Session session = getCurrentSession();
	//		session.beginTransaction();
			session.createQuery("update Staff set is_del = 0 where id = " + id).executeUpdate();
	//		session.getTransaction().commit();
	//		session.close();
			return true;

		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
			return false;
		}
	}
	
//	public List<String> getStaffName(String unitNum){
//		
//	}
}
