package com.example.studentmanagementfa22.logging;

import com.example.studentmanagementfa22.config.CustomSuccessHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger
            = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution(* com.example.studentmanagementfa22.service.*.add*(..))")
    public void before(JoinPoint joinPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.getName() + " added");
    }

    @Around("execution(* com.example.studentmanagementfa22.service.*.update*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object objectProceed = joinPoint.proceed();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.getName() + " edited " + objectProceed);
        return objectProceed;
    }
}
