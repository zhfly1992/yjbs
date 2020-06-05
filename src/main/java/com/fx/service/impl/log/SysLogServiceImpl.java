package com.fx.service.impl.log;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.annotation.Log;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.other.AddressUtil;
import com.fx.dao.log.SysLogDao;
import com.fx.entity.log.SysLog;
import com.fx.service.log.SysLogService;

@Service
@Transactional
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, Long> implements SysLogService {
	
	/** 系统日志-数据源 */
	@Autowired
	private SysLogDao tbLogDao;
	@Override
	public ZBaseDaoImpl<SysLog, Long> getDao() {
		return tbLogDao;
	}
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
    public void deleteLogs(List<Long> ids) {
        //this.batchDelete(ids, "id", SysLog.class);
    }

    @Override
    public void saveLog(ProceedingJoinPoint proceedingJoinPoint, SysLog log) throws JsonProcessingException {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Log annotation = method.getAnnotation(Log.class);
        if (annotation != null) {
            // 注解上的描述
            log.setOperExp(annotation.value());
        }
        // 请求的类名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        // 请求方法名
        String methodName = signature.getName();
        log.setOperMethod(className + "." + methodName + "()");
        // 请求的方法参数
        Object[] args = proceedingJoinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer d = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = d.getParameterNames(method);
        if (args != null && parameterNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(parameterNames));
            log.setOperParams(params.toString());
        }
        log.setAtime(new Date());
        log.setOperLocation(AddressUtil.getAddress(log.getOperIp()));
        tbLogDao.save(log);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder handleParams(StringBuilder params, Object[] args, List<String> paramNames) throws JsonProcessingException {
    	for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List list = new ArrayList();
                List paramList = new ArrayList();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> clazz = args[i].getClass();
                    try {
                        clazz.getDeclaredMethod("toString", new Class[]{null});
                        //如果不抛出NoSuchMethodException异常，则存在ToString方法
                        params.append(" ").append(paramNames.get(i)).append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
	
}
