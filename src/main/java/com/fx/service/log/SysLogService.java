package com.fx.service.log;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.entity.log.SysLog;

public interface SysLogService extends BaseService<SysLog, Long> {
	
    void deleteLogs(List<Long> ids);

    @Async
    void saveLog(ProceedingJoinPoint proceedingJoinPoint, SysLog log) throws JsonProcessingException;
	
}
