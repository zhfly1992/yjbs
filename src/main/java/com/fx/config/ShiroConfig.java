package com.fx.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fx.commons.utils.tools.QC;

/**
 * shiro配置
 */
@Configuration
public class ShiroConfig {
	
	/** 服务器ip */
	@Value("${spring.redis.host}")
    private String host;
	
    /** 服务器端口 */
	@Value("${spring.redis.port}")
    private String port;
	
    /** 超时重连时间（毫秒） */
	@Value("${spring.redis.timeout}")
    private String timeout;
	
    /** 登录密码 */
	@Value("${spring.redis.password}")
    private String password;
	
	/**
	 * Filter工厂，设置对应的过滤条件和跳转条件
	 * @param securityManager securityManager
	 * @return ShiroFilterFactoryBean
	 */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //用户未登录不进行跳转，而是自定义返回json数据
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
        filters.put("authc", new ShiroLoginFilter());//将自定义 的FormAuthenticationFilter注入shiroFilter中
        
        // 过滤器链定义映射
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        /*
         * anon:所有url都都可以匿名访问，authc:所有url都必须认证通过才可以访问;
         * 过滤链定义，从上向下顺序执行，authc 应放在 anon 下面
         * */
        
        /**swagger拦截配置--begin*/
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        /**swagger拦截配置--end***/
        
        /**页面拦截配置--begin*/
        filterChainDefinitionMap.put("/page/*/login", "anon");
        filterChainDefinitionMap.put("/page/*/register", "anon");
        filterChainDefinitionMap.put("/page/notfound", "anon");
        filterChainDefinitionMap.put("/page/error", "anon");
        /**页面拦截配置--end***/
        
        // 公共控制器接口
        filterChainDefinitionMap.put("/common/getImgCode", "anon");
        filterChainDefinitionMap.put("/common/getImgCodeModile", "anon");
        filterChainDefinitionMap.put("/common/getMapPointList", "anon");
        filterChainDefinitionMap.put("/common/getStationList", "anon");
        filterChainDefinitionMap.put("/common/queryStationInfo", "anon");
        filterChainDefinitionMap.put("/common/isOpenCityService", "anon");
        filterChainDefinitionMap.put("/common/getCitys", "anon");
        filterChainDefinitionMap.put("/common/getCarBrandsByCarType", "anon");
        filterChainDefinitionMap.put("/common/getPlateNumShort", "anon");
        
        // 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**", "anon")来配置匿名访问，必须配置到每个静态目录
        filterChainDefinitionMap.put("/back/**", "anon");
        filterChainDefinitionMap.put("/customer/**", "anon");
        filterChainDefinitionMap.put("/company/**", "anon");
        filterChainDefinitionMap.put("/common/**", "anon");
        
        // 微信授权文件
        filterChainDefinitionMap.put("/MP_verify_0K8o1Dm2b3zprV1Y.txt", "anon");
        

        /**********移动端-配置--begin************/
        
        // 微信自动登录授权
        filterChainDefinitionMap.put("/mb/wxAuthority", "anon");
        // 微信自动登录授权-回调方法
        filterChainDefinitionMap.put("/mb/wxAuthCallBack", "anon");
        // 提交-微信驾驶员-登录
        filterChainDefinitionMap.put("/mb/driver/passLogin", "anon");
        
        /**********移动端-配置--end************/
        
        
        
        // 提交-后台登录
        filterChainDefinitionMap.put("/back/cus/subLogin", "authc");
        // 提交-个人/单位登录
        filterChainDefinitionMap.put("/company/cus/subLogin", "authc");
        // 提交-个人/单位注册
        filterChainDefinitionMap.put("/company/cus/subRegister", "authc");
        // 所有url都必须认证通过才可以访问
        filterChainDefinitionMap.put("/**", "authc");
        

        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了, 位置放在 anon、authc下面
        filterChainDefinitionMap.put("*/*/logout", "logout");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        // 配器shirot认证登录页面地址，前后端分离中登录页面跳转应由前端路由控制，后台仅返回json数据, 对应LoginController中unauth请求
//        shiroFilterFactoryBean.setLoginUrl("/page/company/login");
//        shiroFilterFactoryBean.setLoginUrl("/common/unauth");

        // 登录成功后要跳转的链接, 此项目是前后端分离，故此行注释掉，登录成功之后返回用户基本信息及token给前端
        // shiroFilterFactoryBean.setSuccessUrl("/index");

        // 未授权界面, 对应LoginController中 403 请求
        shiroFilterFactoryBean.setUnauthorizedUrl("/common/unauthorized");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 将自己的验证方式加入容器
     * @param matcher 加密设置
     * @return realm
     */
    @Bean
    public Realm realm(HashedCredentialsMatcher matcher) {
        MyAuthRealm realm = new MyAuthRealm();
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager());
//        securityManager.setCacheManager(cacheManagers());
        securityManager.setRealm(realm(hashedCredentialsMatcher()));
        return securityManager;
    }

    /**
     * 设置“记住我”
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie rememberCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    /**
     * 凭证匹配器（由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        credentialsMatcher.setHashAlgorithmName(QC.ALGORITHNAME);
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        credentialsMatcher.setHashIterations(QC.HASHNUM);
        return credentialsMatcher;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解支持
     * 比如：@RequireRoles @RequireUsers
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 自定义sessionManager
     * @return SessionManager
     */
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO());
//        mySessionManager.setCacheManager(cacheManagers());
        mySessionManager.setSessionIdUrlRewritingEnabled(true);
        return mySessionManager;
    }

    /**
     * 使用shiro-redis配置
     * @return
     */
    public RedisManager redisManagers() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(Integer.parseInt(port));
        redisManager.setTimeout(Integer.parseInt(timeout));
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * redis实现缓存
     * @return
     */
    @Bean
    public RedisCacheManager cacheManagers() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManagers());
        return redisCacheManager;
    }

    /**
     * 使用Redis实现 shiro sessionDao
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManagers());
        return redisSessionDAO;
    }

}
