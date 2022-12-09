package com.example.studentmanagementfa22.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger
            = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning("execution(* com.example.studentmanagementfa22.service.*.add*(..))")
    public void afterAdd(JoinPoint joinPoint){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Object> object = Arrays.stream(joinPoint.getArgs()).toList();
        logger.info(currentUser + " added " + object.get(0));
    }

    @AfterReturning("execution(* com.example.studentmanagementfa22.service.*.delete*(..))")
    public void afterDelete(JoinPoint joinPoint){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        List<Object> object = Arrays.stream(joinPoint.getArgs()).toList();
        Integer deletedId = (Integer) object.get(0);
        if (methodName.contains("Teacher")){
            logger.info(currentUser + " deleted teacher with id = " + deletedId);
        } else if (methodName.contains("Subject")){
            logger.info(currentUser + " deleted subject with id = " + deletedId);
        }
    }

    @Around("execution(* com.example.studentmanagementfa22.service.*.update*(..))")
    public Object logObjectUpdated(ProceedingJoinPoint joinPoint) throws Throwable {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Object objectProceed = joinPoint.proceed();
        logger.info(currentUser + " updated " + objectProceed);
        return objectProceed;
    }

    @AfterReturning("execution(* com.example.studentmanagementfa22.config.security.filter.JwtAuthenticationFilter.*(..))")
    public void afterLogin(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " logged in");
    }

}
