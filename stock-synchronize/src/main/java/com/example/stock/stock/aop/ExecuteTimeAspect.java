package com.example.stock.stock.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class ExecuteTimeAspect {

    Logger log = LoggerFactory.getLogger(getClass());

    @Around("@annotation(com.example.stock.stock.aop.ExecuteTime)")
    public Object timer(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("{} 실행시간 : {} ms", joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());
        }
    }
}
