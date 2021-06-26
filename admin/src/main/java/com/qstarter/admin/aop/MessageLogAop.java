package com.qstarter.admin.aop;

import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.admin.event.PublishCenter;
import com.qstarter.core.utils.RemoteIpHelper;
import com.qstarter.security.utils.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author peter
 * date: 2019-09-25 14:24
 **/
@Aspect
@Component
@Slf4j
public class MessageLogAop {

    @Pointcut("@annotation(com.qstarter.admin.annotations.MessageLog)")
    public void handleLog() {
    }

    @Around("handleLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //入参
        Object[] args = joinPoint.getArgs();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        MessageLog annotation = method.getAnnotation(MessageLog.class);

        Object result = joinPoint.proceed();

        PublishCenter.publishMessageLogHandleEvent(args,annotation,result, RemoteIpHelper.getClientIpAddress(ContextHolder.getRequest()),ContextHolder.getUserIdOrNull());
        return result;
    }
}