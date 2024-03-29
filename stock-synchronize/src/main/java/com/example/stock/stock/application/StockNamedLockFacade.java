package com.example.stock.stock.application;

import com.example.stock.stock.repository.LockRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StockNamedLockFacade {

    private static final Long LOCK_TIME = 3000L;

    private final LockRepository lockRepository;
    private final StockService stockService;

    public StockNamedLockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString(), LOCK_TIME);
            stockService.decreaseStock(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
