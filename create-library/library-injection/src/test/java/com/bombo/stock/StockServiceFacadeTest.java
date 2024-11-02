package com.bombo.stock;

import com.bombo.cache.core.aop.DistributedLockAop;
import com.bombo.stock.domain.Stock;
import com.bombo.stock.domain.StockInMemoryRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis")
@SpringBootTest
class StockServiceFacadeTest {

    @Autowired
    private StockInMemoryRepository stockInMemoryRepository;

    @Autowired
    private StockServiceFacade stockServiceFacade;

    @BeforeEach
    void setUp() {
        Stock stock = Stock.builder()
                .count(100L)
                .build();

        stockInMemoryRepository.save(stock);
    }

    @Test
    void decrease() throws InterruptedException {
        // given
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        AspectJProxyFactory factory = new AspectJProxyFactory(stockServiceFacade);
        factory.setProxyTargetClass(true);
        factory.addAspect(new DistributedLockAop(redissonClient));
        StockServiceFacade proxy = (StockServiceFacade) factory.getProxy();

        int numberOfThread = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThread);

        // when
        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                try {
                    proxy.decrease(1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        Stock stock = stockInMemoryRepository.findById(1L).get();

        // then
        Assertions.assertThat(stock.getCount()).isEqualTo(0L);
    }

    @AfterEach
    void tearDown() {
        stockInMemoryRepository.deleteAll();
    }
}
