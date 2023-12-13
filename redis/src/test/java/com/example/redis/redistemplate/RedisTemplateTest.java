package com.example.redis.redistemplate;

import com.example.redis.RedisIntegrationTest;
import com.example.redis.domain.redis.Fruits;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTemplateTest extends RedisIntegrationTest {

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

        // when
        Fruits value = (Fruits) stringObjectValueOperations.get(key);
        Set<String> keys = redisTemplate.keys(key);

        // then
        assertThat(keys.size()).isEqualTo(1);
        assertThat(value).usingRecursiveComparison().isEqualTo(banana);
    }

    @DisplayName("restTemplate key-value(list) 형식으로 저장한다.")
    @Test
    void saveListForOps() {
        // given
        ListOperations<String, Object> stringObjectListOperations = redisTemplate.opsForList();
        String KEY = "listKey";

        LocalDateTime serverTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, serverTime);
        Fruits banana = Fruits.createFruit("바나나", 15, serverTime);
        Fruits waterMellon = Fruits.createFruit("수박", 13, serverTime);

        stringObjectListOperations.leftPush(KEY, apple);
        stringObjectListOperations.rightPush(KEY, banana);
        stringObjectListOperations.leftPush(KEY, waterMellon);

        // when
        Fruits fruitA = (Fruits) stringObjectListOperations.leftPop(KEY);
        Fruits fruitB = (Fruits) stringObjectListOperations.leftPop(KEY);
        Fruits fruitC = (Fruits) stringObjectListOperations.leftPop(KEY);

        // then
        assertThat(fruitA).usingRecursiveComparison().isEqualTo(waterMellon);
        assertThat(fruitB).usingRecursiveComparison().isEqualTo(apple);
        assertThat(fruitC).usingRecursiveComparison().isEqualTo(banana);
    }

    @DisplayName("restTemplate key-value(list) 형식으로 저장 된 값이 없다면 null을 반환한다.")
    @Test
    void saveListForOpsWithEmpty() {
        // given
        ListOperations<String, Object> stringObjectListOperations = redisTemplate.opsForList();
        String KEY = "listKey";

        // when
        Object object = stringObjectListOperations.leftPop(KEY);

        // then
        assertThat(object).isNull();
    }

    @DisplayName("restTemplate key-value(Set) 형식으로 저장한다.")
    @Test
    void saveSetForOps() {
        // given
        SetOperations<String, Object> stringObjectSetOperations = redisTemplate.opsForSet();
        String KEY = "setKey";

        LocalDateTime serverTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, serverTime);
        Fruits apple2 = Fruits.createFruit("사과", 10, serverTime);
        Fruits banana = Fruits.createFruit("바나나", 15, serverTime);
        Fruits waterMellon = Fruits.createFruit("수박", 13, serverTime);

        stringObjectSetOperations.add(KEY, apple, banana, waterMellon, apple2);

        // when
        Set<Object> sets = stringObjectSetOperations.members(KEY);

        // then
        assertThat(sets.size()).isEqualTo(3);
        assertThat(stringObjectSetOperations.isMember(KEY, apple)).isTrue();

        // 객체 내부의 값이 같기때문에 true 이다.
        assertThat(stringObjectSetOperations.isMember(KEY, apple2)).isTrue();
        assertThat(stringObjectSetOperations.isMember(KEY, banana)).isTrue();
        assertThat(stringObjectSetOperations.isMember(KEY, waterMellon)).isTrue();
    }

    @DisplayName("restTemplate key-value(Hash) 형식으로 저장한다.")
    @Test
    void saveHashForOps() {
        // given
        HashOperations<String, String, Fruits> stringObjectHashOperations = redisTemplate.opsForHash();
        String KEY = "hashKey";

        LocalDateTime serverTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, serverTime);
        Fruits banana = Fruits.createFruit("바나나", 15, serverTime);
        Fruits waterMellon = Fruits.createFruit("수박", 13, serverTime);

        stringObjectHashOperations.put(KEY, "apple", apple);
        stringObjectHashOperations.put(KEY, "banana", banana);
        stringObjectHashOperations.put(KEY, "waterMellon", waterMellon);

        // when
        Set<String> keys = stringObjectHashOperations.keys(KEY);
        Object notFound = stringObjectHashOperations.get("Unknown", "null");

        Fruits findApple = stringObjectHashOperations.get(KEY, "apple");
        Fruits findBanana = stringObjectHashOperations.get(KEY, "banana");
        Fruits findWaterMellon = stringObjectHashOperations.get(KEY, "waterMellon");

        // then
        assertThat(keys.size()).isEqualTo(3);
        assertThat(notFound).isNull();
        assertThat(findApple).usingRecursiveComparison().isEqualTo(apple);
        assertThat(findBanana).usingRecursiveComparison().isEqualTo(banana);
        assertThat(findWaterMellon).usingRecursiveComparison().isEqualTo(waterMellon);
    }
}
