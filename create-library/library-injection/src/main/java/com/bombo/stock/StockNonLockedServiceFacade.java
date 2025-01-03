package com.bombo.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockNonLockedServiceFacade implements StockServiceFacade {

    private final StockService stockService;

    public void decrease(Long stockId) {
        stockService.decreaseStock(stockId);
    }
}
