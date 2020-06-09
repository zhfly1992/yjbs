package com.fx.dao.cus;

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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Compositor;
import com.fx.commons.hiberantedao.pagingcom.Compositor.CompositorType;
import com.fx.commons.hiberantedao.pagingcom.Filtration;
import com.fx.commons.hiberantedao.pagingcom.Filtration.MatchType;
import com.fx.commons.hiberantedao.pagingcom.Page;
import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.AddressUtil;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.IPUtil;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.company.StaffDao;
import com.fx.dao.log.LoginLogDao;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.cus.WxBaseUser;
import com.fx.entity.log.LoginLog;
import com.fx.web.util.RedisUtil;

@Repository
public class CustomerDao extends ZBaseDaoImpl<Customer, Long> {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	/** 登录日志-服务 */
	@Autowired
	private LoginLogDao loginLogDao;
	/** 用户基类-数据源 */
	@Autowired
	private BaseUserDao buDao;
	/** 单位-服务 */
	@Autowired
	private CompanyUserDao companyUserDao;
	/** 微信用户基类-服务 */
	@Autowired
	private WxBaseUserDao wxBaseUserDao;
	/** 员工-服务 */
	@Autowired
	private StaffDao staffDao;
	
	/**
	 * 获取-根用户基类
	 */
	public BaseUser getRootBuser() {
		String logtxt = U.log(log, "获取-根用户基类");
		
		BaseUser rootUser = null;
		try {
			String hql = "from Customer where recBaseUserId is null order by id asc";
			List<Customer> rootCuss = hqlListFirstMax(hql, 0, 1);
			if(rootCuss.size() > 0) rootUser = rootCuss.get(0).getBaseUserId();
		} catch (Exception e) {
			U.logEx(log, logtxt);
			e.printStackTrace();
		}
		
		return rootUser;
	}
	
	/**
	 * 保存微信登录数据
	 * @param map 		map
	 * @param request 	request
	 * @param remberMe 	是否保存数据至session中
	 * @param lcus 		登录个人用户对象
	 * @param luser 	登录用户基类
	 * @param comUser 	登录单位对象
	 * @param wxid 		登录用户微信id
	 * @param role 		登录用户角色
	 * @return map
	 */
	public Map<String, Object> saveWxLoginDat(Map<String, Object> map, HttpServletRequest request, boolean remberMe, 
		Customer lcus, BaseUser luser, CompanyUser comUser, String wxid, CusRole role) {
		UsernamePasswordToken token = new UsernamePasswordToken(luser.getUname(), luser.getLpass());
		token.setRememberMe(remberMe);
		
        Subject subject = SecurityUtils.getSubject();
        if(subject != null) subject.logout(); // 已登录，则先退出
        
        subject.login(token);
        U.log(log, "token: "+token);
        U.log(log, "是否登录==>"+subject.isAuthenticated());
        
        
        // 获取-用户微信基类对象
        WxBaseUser wxUser = wxBaseUserDao.findWxUser(comUser.getUnitNum(), luser.getUname());
        if(wxUser == null) {
        	U.log(log, "不存在-微信基类对象，即未绑定当前访问公众号");
        	
        	wxUser = new WxBaseUser();
        	wxUser.setUname(luser.getUname());
        	wxUser.setCompanyNum(comUser.getUnitNum());
        	wxUser.setWxid(wxid);
        	wxUser.setAtime(new Date());
        	wxUser.setLgWxid(wxid);
        	wxUser.setLgRole(role);
        	wxUser.setLgLngLat(null);
        	wxUser.setLgIp(IPUtil.getIpAddr(request));
        	wxUser.setLgTime(new Date());
        	wxBaseUserDao.save(wxUser);
        	U.log(log, "绑定当前公众号-完成");
        }else {
        	U.log(log, "存在-微信基类对象，即已绑定当前访问公众号");
        	
        	wxUser.setLgWxid(wxid);
        	wxUser.setLgRole(role);
        	wxUser.setLgLngLat(null);
        	wxUser.setLgIp(IPUtil.getIpAddr(request));
        	wxUser.setLgTime(new Date());
        	wxBaseUserDao.update(wxUser);
        	U.log(log, "更新-绑定当前公众号信息-完成");
        }
        
        // 记录登录日志
        LoginLog llog = new LoginLog();
        llog.setOperIp(IPUtil.getIpAddr(request));// 获取HTTP请求
        llog.setUname(luser.getUname());
        llog.setOperLocation(AddressUtil.getAddress(llog.getOperIp()));
        llog.setAtime(new Date());
        llog.setOperDevice(UT.getReqUA(request));
        loginLogDao.save(llog);
        U.log(log, "保存-登录日志-完成");
        
        // 缓存登录用户信息
        Map<String, Object> mapUser = new HashMap<String, Object>();// 缓存登录用户信息map
        String uuid = subject.getSession().getId().toString();
		mapUser.put(QC.L_USER, lcus);
		mapUser.put(QC.L_WX, wxUser);
		mapUser.put(QC.L_COMPANY, comUser);
		mapUser.put(QC.L_TIME, DateUtils.DateToStr(new Date()));
		redis.set(uuid, mapUser);
		
		// 传入前端
        map.put(QC.UUID, uuid);
		
		U.setPut(map, 1, "驾驶员-登录成功");
		
		return map;
	}
	
	/**
	 * 微信-用户自动登录
	 * @param request 	request
	 * @param response 	response
	 * @param recUname 	推荐人用户名
	 * @param role 		用户角色 
	 * @param teamNo	车队编号
	 * @param wxuser 	微信用户基类
	 * @return map{code: 结果状态码, msg: 结果状态码说明, uuid: 登录成功的uuid}
	 */
	public Map<String, Object> wxAutoLogin(HttpServletRequest request, HttpServletResponse response, 
		String recUname, String role, String teamNo, WxBaseUser wxuser) {
		String logtxt = U.log(log, "微信-用户自动登录");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			BaseUser recUser = null;
			if(fg) {
				if(StringUtils.isBlank(recUname)) {
					U.log(log, "[推荐人用户名]为空");
				}else {
					recUname = recUname.trim();
					recUser = buDao.findByField("uname", recUname);
					
					U.log(log, "[推荐人用户名] recUname="+recUname);
				}
				
				if(recUser == null) {
					U.log(log, "没有推荐人，则默认推荐人为根用户");
					
					recUser = getRootBuser();
				}
			}
			
			CusRole _role = null;
			if(fg) {
				if(StringUtils.isBlank(role)) {
					fg = U.setPutFalse(map, "[用户角色]不能为空");
				}else {
					role = role.trim();
					if(!FV.isOfEnum(CusRole.class, role)) {
						fg = U.setPutFalse(map, "[用户角色]格式错误");
					}else {
						_role = CusRole.valueOf(role);
					}
					
					U.log(log, "[用户角色] role="+role);
				}
			}
			
			CompanyUser comUser = null;
			if(fg) {
				if(StringUtils.isBlank(teamNo)) {
					fg = U.setPutFalse(map, "[车队编号]不能为空");
				}else {
					teamNo = teamNo.trim();
					comUser = companyUserDao.findByField("unitNum", teamNo);
					if(comUser == null) {
						fg = U.setPutFalse(map, "[车队]不存在");
					}
					
					U.log(log, "[车队编号] teamNo="+teamNo);
				}
			}
			
			Customer lcus = null;
			BaseUser luser = null;
			if(fg) {
				if(wxuser == null) {
					fg = U.setPutFalse(map, "[微信用户基类]不能为空");
				}else {
					U.log(log, "[微信用户微信id] wxid="+wxuser.getWxid());
					
					lcus = findByField("baseUserId.uname", wxuser.getUname());
					if(lcus == null) {
						fg = U.setPutFalse(map, "[登录用户个人信息]不存在");
					}else {
						luser = lcus.getBaseUserId();
					}
				}
			}
			
			if(fg) {
				map = saveWxLoginDat(map, request, false, lcus, luser, comUser, wxuser.getWxid(), _role);
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 获取-用户-分页列表
	 * @param reqsrc 	请求来源
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param find		查询关键字
	 * @return Page<T>	分页数据
	 */
	public Page<Customer> findCusList(ReqSrc reqsrc, String page, String rows, String find) {
		String logtxt = U.log(log, "获取-用户-分页列表", reqsrc);
		
		Page<Customer> pd = new Page<Customer>();
		List<Compositor> comps = new ArrayList<Compositor>();
		List<Filtration> filts = new ArrayList<Filtration>();
		try{
			if(ReqSrc.PC_BACK != reqsrc && ReqSrc.PC_COMPANY != reqsrc) {// 没有查询出数据
				filts.add(new Filtration(MatchType.EQ, null, "baseUserId.uname"));
			}else {
				comps.add(new Compositor("id", CompositorType.DESC));
				
				if(StringUtils.isNotBlank(find)) {
					String uname = buDao.findUname(find);
					if(uname != null) {// 匹配用户名
						filts.add(new Filtration(MatchType.EQ, uname, "baseUserId.uname"));
					}else {// 匹配其他属性
						//filts.add(new Filtration(MatchType.LIKE, find, "baseUserId.uname"));
					}
				}
			}
			
			///////////////////--分页设置--////////////////////////////
			pd.setPageNo(Integer.parseInt(page)); 					// 页码
			pd.getPagination().setPageSize(Integer.parseInt(rows)); // 页大小
			pd.setCompositors(comps); 								// 排序条件
			pd.setFiltrations(filts); 								// 查询条件
			pd = findPageByOrders(pd);								// 设置列表数据
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return pd;
	}

	/**
	 * 验证-用户是否拥有指定角色
	 * @param wrole 	用户角色
	 * @param teamNo 	车队编号
	 * @param uname 	用户名
	 * @return MapRes
	 */
	public MapRes valUserRole(CusRole wrole, String teamNo, String uname) {
		String logtxt = U.log(log, "验证-用户是否拥有指定角色");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg) {
				if(wrole == CusRole.TEAM_DRIVER) {
					U.setPut(map, 1, "用户是："+wrole.getKey());
					
//					hql = "select count(id) from Staff where unitNum = ?0 and baseUserId.uname = ?1 and isDriver = ?2";
//					Object obj = staffDao.findObj(hql, teamNo, uname, 1, "LIMIT 1");
//					if(obj != null && Integer.parseInt(obj.toString()) > 0) {
//						U.setPut(map, 1, "用户是："+wrole.getKey());
//					}else {
//						fg = U.setPutFalse(map, "您不是："+wrole.getKey());
//					}
				}else {
					fg = U.setPutFalse(map, "暂时没有该角色");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return U.mapRes(map);
	}
	
}
