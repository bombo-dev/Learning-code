package com.bombo.stock;

import com.bombo.stock.domain.Stock;
import com.bombo.stock.domain.StockRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockBaseService implements StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decreaseStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(EntityNotFoundException::new);

        stock.decrease();
    }
}
