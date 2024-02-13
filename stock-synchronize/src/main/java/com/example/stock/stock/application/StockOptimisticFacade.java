package com.example.stock.stock.application;

import org.springframework.stereotype.Component;

@Component
public class StockOptimisticFacade {

    private final StockService stockService;

    public StockOptimisticFacade(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(true) {
            try {
                stockService.decreaseStock(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
