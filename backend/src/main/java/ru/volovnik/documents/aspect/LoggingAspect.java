package ru.volovnik.documents.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("within(ru.volovnik.documents.controller.DocumentController)")
    public void pointcut() {
    }

    @After("pointcut()")
    public void logInfoMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        log.info("Called method DocumentController:" + name + "() with args: " + List.of(args));
    }
}
