package com.example.redis.springdataredis;

import com.example.redis.RedisIntegrationTest;
import com.example.redis.domain.redis.Fruits;
import com.example.redis.domain.redis.repository.FruitsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class SpringDataRedisTest extends RedisIntegrationTest {

    @Autowired
    private FruitsRepository fruitsRepository;

    @AfterEach
    public void down() {
        fruitsRepository.deleteAll();
    }

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

    // TTL 이슈
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
        Thread.sleep(2000);
        Optional<Fruits> findBanana = fruitsRepository.findById(banana.getId());
        long count = fruitsRepository.count();

        // then
        assertThat(findBanana).isEmpty();
        assertThat(count).isEqualTo(0);
    }

    @DisplayName("@Indexed를 지정하지 않는다면 보조 인덱스로 검색이 안된다.")
    @Test
    void findByStockWithNotIndex() {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime.plusHours(1));
        Fruits banana = Fruits.createFruit("바나나", 15, saveTime);
        Fruits pineApple = Fruits.createFruit("파인애플", 13, saveTime.minusDays(1));

        fruitsRepository.save(apple);
        fruitsRepository.save(banana);
        fruitsRepository.save(pineApple);

        // when
        Optional<Fruits> findFruits = fruitsRepository.findByStock(apple.getStock());

        // then
        assertThat(findFruits).isEmpty();
    }

    @DisplayName("@Indexed를 지정한다면 보조 인덱스로 검색이 된다.")
    @Test
    void findByStockWithIndex() {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime.plusHours(1));
        Fruits banana = Fruits.createFruit("바나나", 15, saveTime);
        Fruits pineApple = Fruits.createFruit("파인애플", 13, saveTime.minusDays(1));

        fruitsRepository.save(apple);
        fruitsRepository.save(banana);
        fruitsRepository.save(pineApple);

        // when
        Optional<Fruits> findFruits = fruitsRepository.findByName(apple.getName());

        // then
        assertThat(findFruits).isNotEmpty();
        assertThat(findFruits.get()).usingRecursiveComparison()
                .isEqualTo(apple);
    }
}
