package com.fx.commons.aspect;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fx.commons.exception.GlobalException;
import com.fx.commons.utils.other.HttpContextUtil;
import com.fx.commons.utils.other.IPUtil;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.log.SysLog;
import com.fx.service.log.SysLogService;

/**
 * 记录系统日志记录
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private SysLogService logSer;

    @Pointcut("@annotation(com.fx.commons.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new GlobalException(throwable.getMessage());
        }
        // 获取Request请求
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置IP地址
        String ip = IPUtil.getIpAddr(request);
        // 记录时间（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        BaseUser luser = (BaseUser)SecurityUtils.getSubject().getPrincipal();
        if(luser!=null) {
        	 SysLog slog = new SysLog();
             slog.setUname(luser.getUname());
             slog.setOperIp(ip);
             slog.setOperTime(time);
             slog.setAtime(new Date());
             logSer.saveLog(proceedingJoinPoint, slog);
        }
        
        return result;
    }
}
