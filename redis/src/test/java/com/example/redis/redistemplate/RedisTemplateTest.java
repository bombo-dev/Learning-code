package com.example.redis.redistemplate;

import com.example.redis.domain.redis.Fruits;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTemplateTest {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @DisplayName("Resource를 사용하여 빈의 이름에 맞는 빈을 주입받는다.")
    @Test
    void resourceDependencyInjection() {
        // when & then
        assertThat(redisTemplate.getClass().getSimpleName()).isEqualTo("RedisTemplate");
        assertThat(stringRedisTemplate.getClass().getSimpleName()).isEqualTo("StringRedisTemplate");
    }

    @DisplayName("redisTemplate key-value 형식으로 기본형 타입 저장")
    @Test
    void saveValueOpsPrimitiveType() {
        // given
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();

        String key = "KEY";
        stringObjectValueOperations.set(key, 1);
        stringObjectValueOperations.set(key, 2);
        stringObjectValueOperations.set(key, 3);

        // when
        Integer value = (Integer) stringObjectValueOperations.get(key);
        Set<String> keys = redisTemplate.keys(key);

        // then
        assertThat(keys.size()).isEqualTo(1);
        assertThat(value).isEqualTo(3);
    }

    @DisplayName("redisTemplate key-value 형식으로 객체 타입 저장")
    @Test
    void saveValueOpsReferenceType() {
        // given
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        String key = "ReferenceKey";

        LocalDateTime serverTime = LocalDateTime.now();

        Fruits apple = Fruits.createFruit("사과", 10, serverTime);
        Fruits banana = Fruits.createFruit("바나나", 15, serverTime);

        stringObjectValueOperations.set(key, apple);
        stringObjectValueOperations.set(key, banana);
        Set<String> keys = redisTemplate.keys(key);

        // when
        Fruits value = (Fruits) stringObjectValueOperations.get(key);

        // then
        assertThat(keys.size()).isEqualTo(1);
        assertThat(value).usingRecursiveComparison().isEqualTo(banana);
    }
}
