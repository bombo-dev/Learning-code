package com.bombo.stock;

import com.bombo.cache.core.aop.DistributedLockAop;
import com.bombo.stock.domain.Stock;
import com.bombo.stock.domain.StockInMemoryRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;


import static org.assertj.core.api.Assertions.assertThat;

class StockServiceLockFacadeTest {

    private static final int THREAD_COUNT = 100;
    private static final int EXECUTE_COUNT = 300;

    private StockInMemoryRepository stockInMemoryRepository = new StockInMemoryRepository();

    @BeforeEach
    void setUp() {
        Stock stock = Stock.builder()
                .count(300L)
                .build();

        stockInMemoryRepository.save(stock);
    }

    @DisplayName("분산 락이 적용된 decrease 메서드 테스트")
    @Test
    void decreaseWithLock() throws InterruptedException {
        // given
        StockServiceFacade stockServiceLockFacade = new StockServiceLockFacade(new StockBaseService(stockInMemoryRepository));

        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        AspectJProxyFactory factory = new AspectJProxyFactory(stockServiceLockFacade);
        factory.setProxyTargetClass(true);
        factory.addAspect(new DistributedLockAop(redissonClient));
        StockServiceLockFacade proxy = factory.getProxy();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);

        // when
        for (int i = 0; i < EXECUTE_COUNT; i++) {
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
        assertThat(stock.getCount()).isEqualTo(0L);
    }

    @DisplayName("분산 락이 적용되지 않은 decrease 메서드 테스트")
    @Test
    void decreaseWithNonLock() throws InterruptedException {
        // given
        StockServiceFacade stockServiceLockFacade = new StockNonLockedServiceFacade(new StockBaseService(stockInMemoryRepository));

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);

        // when
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                stockServiceLockFacade.decrease(1L);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        Stock stock = stockInMemoryRepository.findById(1L).get();

        // then
        System.out.println("현재 남은 재고 수 : "  + stock.getCount());
        assertThat(stock.getCount()).isNotZero();
    }

    @AfterEach
    void tearDown() {
        stockInMemoryRepository.deleteAll();
    }
}
