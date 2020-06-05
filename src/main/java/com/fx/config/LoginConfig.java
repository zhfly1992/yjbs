//package com.fx.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import com.fx.web.interceptor.LoginInterceptor;
//
///**
// * 配置-登录拦截器
// */
//@Configuration
//public class LoginConfig implements WebMvcConfigurer {
//	
//	/** 让拦截器执行的时候实例化拦截器Bean */
//	@Bean
//	public LoginInterceptor getLoginInterceptor() { return new LoginInterceptor(); }
//	
//	@Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册LoginInterceptor拦截器
//        InterceptorRegistration registration = registry.addInterceptor(getLoginInterceptor());
//        registration.addPathPatterns("/**");                      // 所有路径都被拦截
//        
//        // 不拦截的路径
//        List<String> excl2 = new ArrayList<String>();
//        excl2.add("/page/error");
//        excl2.add("/page/back/login");
//        excl2.add("/page/carteam/login");
//        excl2.add("/page/company/login");
//        
//        registration.excludePathPatterns(excl2);
//        
//        // 不拦截的静态资源
//        String[] excl1 = new String[] {"/back/**", "/carteam/**", "/company/**", "/commons/**"};
//        registration.excludePathPatterns(excl1);    
//    }
//	
//}
