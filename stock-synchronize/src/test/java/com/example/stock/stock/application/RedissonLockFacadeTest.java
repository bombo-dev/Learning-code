package com.example.stock.stock.application;

import com.example.stock.stock.domain.Stock;
import com.example.stock.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedissonLockFacadeTest {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonLockStockFacade lettuceLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void setUp() {
        stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    public void tearDown() {
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("동시에 100개의 요청")
    @Test
    void namedLockDecrease100() throws InterruptedException {
        // given
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int threadCount = 100;

        // 비동기로 요청을 쏠 수 있게 해주는 자바의 API
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // 다른 쓰레드에서 실행중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lettuceLockStockFacade.decrease(1L, 1L);
                } catch (Exception e) {
                    throw new RuntimeException();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        Stock stock = stockRepository.findById(1L).get();

        // then
        stopWatch.stop();
        log.info("걸린 시간 {} ms", stopWatch.getTotalTimeMillis());
        assertThat(stock.getQuantity()).isEqualTo(0);
    }
}
