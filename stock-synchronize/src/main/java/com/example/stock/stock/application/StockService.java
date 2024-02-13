package com.example.stock.stock.application;

import com.example.stock.stock.domain.Stock;
import com.example.stock.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decreaseStock(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                Stock stock = stockRepository.findByIdWithPessimisticLock(id)
                        .orElseThrow();

                stock.decrease(quantity);
                stockRepository.save(stock);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

}
