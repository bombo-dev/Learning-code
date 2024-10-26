package com.bombo.stock.domain;

import java.util.Optional;

public interface StockRepository {

    Long save(Stock stock);

    Optional<Stock> findById(Long id);
}
