package com.example.studentmanagementfa22.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger
            = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution(* com.example.studentmanagementfa22.service.*.add*(..))")
    public void afterAdd(JoinPoint joinPoint){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " added st");
    }

    @After("execution(* com.example.studentmanagementfa22.service.*.delete*(..))")
    public void afterDelete(JoinPoint joinPoint){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " deleted something");
    }

    @Around("execution(* com.example.studentmanagementfa22.service.*.update*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Object objectProceed = joinPoint.proceed();
        logger.info(currentUser + " updated " + objectProceed);
        return objectProceed;
    }

    @After("execution(* com.example.studentmanagementfa22.config.CustomSuccessHandler.*(..))")
    public void afterLogin(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " logged in");
    }

}
