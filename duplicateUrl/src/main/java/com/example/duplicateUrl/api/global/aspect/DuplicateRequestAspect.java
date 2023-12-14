package com.example.duplicateUrl.api.global.aspect;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;

@RequiredArgsConstructor
@Configuration
@Aspect
public class DuplicateRequestAspect {

    @Resource(name = "redisTemplate")
    private final HashOperations<Long, String, Integer> hashOps;

    @Before("@annotation(com.example.duplicateUrl.core.global.config.Duplicate)")
    public void duplicateRequestCheck(JoinPoint joinPoint) {
        // 유저를 구분 할 수 있는 값이 무엇이 있을까? id가 없어도 말이다.
        // 가장 간단한 것은 HttpServletRequest에서 ip를 가져오는 것이다.
        // 다만 이렇게 사용한다면 controller 에서 해당 aop를 사용하기 위한 모든 메서드에는 HttpServletRequest가 있어야 한다.
    }
}
