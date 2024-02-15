package com.example.stock.stock.application;

import com.example.stock.stock.domain.Stock;
import com.example.stock.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    public void decreaseStock(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow();

        stock.decrease(quantity);
        stockRepository.save(stock);
    }

}
