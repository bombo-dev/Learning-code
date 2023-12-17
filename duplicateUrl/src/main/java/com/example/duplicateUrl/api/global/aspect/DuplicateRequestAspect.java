package com.example.duplicateUrl.api.global.aspect;

import com.example.duplicateUrl.core.global.exception.DuplicateRequestException;
import com.example.duplicateUrl.core.global.exception.ExceedRequestException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Aspect
public class DuplicateRequestAspect {

    private static final String BLACKLIST_KEY = "BLACKLIST";
    private static final int MAX_REQUEST = 5;

    @Resource(name = "redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, Integer> hashOps;

    @PostConstruct
    private void initTemplate() {
        hashOps = redisTemplate.opsForHash();
    }

    @Before("@annotation(com.example.duplicateUrl.core.global.config.Duplicate) && args(request, ..)")
    public void duplicateRequestCheck(JoinPoint joinPoint, HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR") != null ?
                request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();

        String uri = request.getRequestURI();

        if (hashOps.hasKey(ip, uri)) {
            int count = hashOps.get(ip, uri);
            hashOps.put(ip, uri, count + 1);

            if (count + 1 > MAX_REQUEST) {
                log.warn("중복 요청이 5회 이상 발생하였습니다. 요청 ip : {}, 요청 uri : {}", ip, uri);
                hashOps.put(BLACKLIST_KEY, ip, 0);
                redisTemplate.expire(ip, 10000, TimeUnit.MILLISECONDS);
                throw new ExceedRequestException("중복 요청이 5회 이상 발생되었습니다.");
            }

            throw new DuplicateRequestException("중복 요청이 발생했습니다 요청 수 : [%d]".formatted(count));
        } else {
            hashOps.put(ip, uri, 1);
        }

        redisTemplate.expire(ip, 2000, TimeUnit.MILLISECONDS);
    }
}
