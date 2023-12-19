package com.example.duplicateUrl.api.global.interceptor;

import com.example.duplicateUrl.core.global.exception.ExceedRequestException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class DuplicateRequestInterceptor implements HandlerInterceptor {

    private static final String BLACKLIST_KEY = "BLACKLIST";

    @Resource(name = "redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, Integer> hashOps;

    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getHeader("X-FORWARDED-FOR") != null ?
                request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();

        log.info("hash hasKey : {}", hashOps.hasKey(BLACKLIST_KEY, ip));

        if (hashOps.hasKey(BLACKLIST_KEY, ip)) {
            hashOps.put(BLACKLIST_KEY, ip, 0);
            redisTemplate.expire(ip, 10000, TimeUnit.MILLISECONDS);
            throw new ExceedRequestException("비정상적인 요청이 감지되었습니다. 잠시 후에 다시 시도해주세요.");
        }

        return true;
    }
}
