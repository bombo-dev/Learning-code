package com.bombo.cache.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(value = "spring.redis.host", havingValue = "true")
@Aspect
public class DistributedLockAop {

}
