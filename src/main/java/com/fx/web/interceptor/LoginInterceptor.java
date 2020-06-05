//package com.fx.web.interceptor;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.fx.commons.utils.clazz.Message;
//import com.fx.commons.utils.tools.QC;
//import com.fx.commons.utils.tools.U;
//import com.fx.entity.cus.Customer;
//import com.fx.web.util.RedisUtil;
//
///**
// * 登录-拦截器
// *
// */
//public class LoginInterceptor implements HandlerInterceptor {
//	/** 日志记录 */
//	private final Logger log = LogManager.getLogger(LoginInterceptor.class.getName());
//	
//	/** 注入缓存-数据源 */
//	@Autowired
//    private RedisUtil redis;
//	
//	/**
//	 * 在请求被处理之前调用
//	 */
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
//		
//		try {
//			// 获取当前请求的路径
//			String url = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
//			
//			if(url.contains("/mb/")) {// 移动端请求
//				boolean fg = true;
//				String uuid = U.getUUID(request);
//		
//				Object lobj = null;
//				if (fg) {
//					if (StringUtils.isBlank(uuid)) {
//						fg = U.logFalse(log, "[uuid]为空");
//					} else {
//						lobj = redis.get(uuid);
//						if (lobj == null) {
//							fg = U.logFalse(log, "[缓存-登录对象]为空");
//						}
//					}
//				}
//		
//				if (fg) {
//					@SuppressWarnings("unchecked")
//					Map<String, Object> loginKey = (Map<String, Object>) lobj;
//		
//					Customer lcus = (Customer) loginKey.get(QC.L_USER);// 获取登录用户信息
//					if (lcus == null) {// 存在信息，则表示登录成功
//						fg = U.logFalse(log, "[缓存-登录-用户-对象]为空");
//					}
//				}
//				
//				if(!fg) {
//					Message.print(response, new Message(401, "登录已失效，请重新登录"));
//					return false;
//				}
//			}else{// pc端请求
//				if(!url.contains("/page/")) {
//					boolean fg = true;
//					String uuid = U.getUuidOfCookie(request);
//			
//					Object lobj = null;
//					if (fg) {
//						if (StringUtils.isBlank(uuid)) {
//							fg = U.logFalse(log, "[uuid]为空");
//						} else {
//							lobj = redis.get(uuid);
//							if (lobj == null) {
//								fg = U.logFalse(log, "[缓存-登录对象]为空");
//							}
//						}
//					}
//			
//					if (fg) {
//						@SuppressWarnings("unchecked")
//						Map<String, Object> loginKey = (Map<String, Object>) lobj;
//			
//						Customer lcus = (Customer) loginKey.get(QC.L_USER);// 获取登录用户信息
//						if (lcus == null) {// 存在信息，则表示登录成功
//							fg = U.logFalse(log, "[缓存-登录-用户-对象]为空");
//						}
//					}
//					
//					if(!fg) {
//						Message.print(response, new Message(401, "登录已失效，请重新登录"));
//						return false;
//					}
//				}else {
//					String gourl = "";
//					if(url.contains("/page/back/")) {
//						U.log(log, "进入[后台-登录]页面");
//						
//						gourl = "/page/back/login";
//					}else if(url.contains("/page/carteam/")) {
//						U.log(log, "进入[车队-登录]页面");
//						
//						gourl = "/page/carteam/login";
//					}else if(url.contains("/page/company/")) {
//						U.log(log, "进入[单位-登录]页面");
//						
//						gourl = "/page/company/login";
//					}else {
//						gourl = "/page/error";
//					}
//					
//					// 跳转对应系统的登录页面
//					response.sendRedirect(request.getContextPath() + gourl);
//					
//					return false;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return true;
//	}
//	
//	/**
//	 * 在请求被处理后，视图渲染之前调用
//	 */
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}
//	
//	/**
//	 * 在整个请求结束后调用
//	 */
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
//	
//}
