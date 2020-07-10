package com.fx.commons.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.fx.commons.annotation.NoRepeatSubmit;
import com.fx.commons.utils.other.HttpContextUtil;
import com.fx.commons.utils.tools.U;
import com.fx.web.util.RedisUtil;

/**
 * 防止用户重复提交-aop解析注解
 */
@Aspect
@Component
public class NoRepeatSubmitAop {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
    
    /** 缓存-服务 */
    @Autowired
    private RedisUtil redis;
 
    @Around("execution(* com.fx..*Controller.*(..)) && @annotation(nrs)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit nrs){
    	String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();// 获取Request请求
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();// 获取Response请求
        String key = sessionId + "-" + request.getServletPath();
        try {
            if (redis.get(key) == null) {// 如果缓存中有这个url视为重复提交
                Object o = pjp.proceed();
                redis.set(key, 0, 2);// 2秒内多次操作算重复请求
                return o;
            } else {
                U.log(log, "您的操作太频繁，请稍后再试:"+key);
                
                U.write(request, response, 0, "您的操作太频繁，请稍后再试");
                return null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            U.log(log, "验证重复提交时出现未知异常："+key);
            U.write(request, response, -1, "验证重复提交时出现未知异常");
            return null;
        }
        
    }
}
