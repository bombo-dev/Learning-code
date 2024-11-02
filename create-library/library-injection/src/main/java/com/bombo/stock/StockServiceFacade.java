package com.bombo.stock;

import com.bombo.cache.core.annotation.DistributeLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StockServiceFacade {

    private final StockService stockService;

    @DistributeLock(key = "#stockId")
    public void decrease(Long stockId) {
        stockService.decreaseStock(stockId);
    }
}
