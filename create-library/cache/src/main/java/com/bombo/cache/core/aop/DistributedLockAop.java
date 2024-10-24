package com.bombo.cache.core.aop;

import com.bombo.cache.core.annotation.DistributeLock;
import com.bombo.cache.core.converter.CustomSpELParser;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "spring.redis.host", havingValue = "true")
@Component
@Aspect
public class DistributedLockAop {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockAop.class);
    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    public DistributedLockAop(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(com.bombo.cache.core.annotation.DistributeLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributeLock distributeLockAnnotation = method.getAnnotation(DistributeLock.class);
        String key = LOCK_PREFIX + CustomSpELParser.getDynamicValue(methodSignature.getParameterNames(), joinPoint.getArgs(), distributeLockAnnotation.key());
        RLock lock = redissonClient.getLock(key);

        try {
            boolean isAcquiredLock = lock.tryLock(distributeLockAnnotation.waitTime(), distributeLockAnnotation.leaseTime(), distributeLockAnnotation.timeUnit());

            if (!isAcquiredLock) {
                return false;
            }

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            log.error("Lock acquisition attempt interrupted. Method : {}", method, e);
            throw new InterruptedException("Lock acquisition attempt interrupted.");
        } finally {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.warn("Redisson Lock Already UnLock. Method : {}, key : {}", method, key);
            }
        }
    }
}
