package com.example.redis.springdataredis;

import com.example.redis.RedisIntegrationTest;
import com.example.redis.domain.redis.Fruits;
import com.example.redis.domain.redis.repository.FruitsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringDataRedisTest extends RedisIntegrationTest {

    @Autowired
    private FruitsRepository fruitsRepository;

    @DisplayName("레디스에 저장한다.")
    @Test
    void saveRedis() {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime);

        // when
        fruitsRepository.save(apple);

        long count = fruitsRepository.count();

        // then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("레디스에서 저장된 값을 읽는다.")
    @Test
    void findFruit() {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime);
        Fruits banana = Fruits.createFruit("바나나", 15, saveTime);

        fruitsRepository.save(apple);
        fruitsRepository.save(banana);

        // when
        Fruits findBanana = fruitsRepository.findById(banana.getId()).get();

        // then
        assertThat(findBanana.getName()).isEqualTo(banana.getName());
        assertThat(findBanana.getStock()).isEqualTo(banana.getStock());
        assertThat(findBanana.getCreatedAt()).isEqualTo(saveTime);
    }

    @DisplayName("TTL을 지정했을 때 이후에 탐색이 되면 안된다.")
    @Test
    void findExceedTTLFruits() throws InterruptedException {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime);
        Fruits banana = Fruits.createFruit("바나나", 15, saveTime);

        fruitsRepository.save(apple);
        fruitsRepository.save(banana);
        
        // when
        Thread.sleep(5000);
        Optional<Fruits> findBanana = fruitsRepository.findById(banana.getId());

        // then
        assertThat(findBanana).isEmpty();
    }
}
