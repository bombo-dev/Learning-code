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
class StockServiceTest {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StockOptimisticFacade stockOptimisticFacade;

    @Autowired
    private StockService stockService;

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

    @DisplayName("재고를 감소시킨다.")
    @Test
    void decrease() {
        // given
        stockService.decreaseStock(1L, 1L);

        // when
        Stock stock = stockRepository.findById(1L).get();

        // then
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @DisplayName("동시에 100개의 요청")
    @Test
    void decrease100() throws InterruptedException {
        // given
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int threadCount = 100;

        // 비동기로 요청을 쏠 수 있게 해주는 자바의 API
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // 다른 쓰레드에서 실행중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
        // 좀 더 조사해보자.
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseStock(1L, 1L);
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

    @DisplayName("동시에 100개의 요청 - 낙관적 락")
    @Test
    void decrease100ByOptimistic() throws InterruptedException {
        // given
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int threadCount = 100;

        // 비동기로 요청을 쏠 수 있게 해주는 자바의 API
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // 다른 쓰레드에서 실행중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
        // 좀 더 조사해보자.
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockOptimisticFacade.decrease(1L, 1L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
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