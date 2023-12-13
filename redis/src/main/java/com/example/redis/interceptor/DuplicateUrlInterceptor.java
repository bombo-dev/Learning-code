package com.example.redis.interceptor;

import com.example.redis.exception.DuplicateUrlException;
import com.example.redis.exception.ExceedUrlRequestException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DuplicateUrlInterceptor {

    private final static Integer MAX_REQUEST = 5;

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Integer> urlHash;

    public DuplicateUrlInterceptor(@Qualifier(value = "urlRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        urlHash = redisTemplate.opsForHash();
    }

    public void interceptDuplicateRequest(String id, String url) {
        try {
            if (urlHash.hasKey(id, url)) {
                Integer count = urlHash.get(id, url);
                urlHash.put(id, url, count + 1);

                if (count >= MAX_REQUEST) {
                    throw new ExceedUrlRequestException("반복된 요청이 짧은 시간 내에 반복되었습니다.");
                }
                throw new DuplicateUrlException("반복 요청이 들어왔습니다. 현재 요청 수 : " + (count + 1));
            } else {
                urlHash.put(id, url, 1);
            }
        } catch (ExceedUrlRequestException exception) {
            System.out.println("반복된 요청이 5회 발생하여 해당 계정을 블랙리스트에 등록합니다.");
        } finally {
            redisTemplate.expire(id, 2000, TimeUnit.MILLISECONDS);
        }
    }
}
