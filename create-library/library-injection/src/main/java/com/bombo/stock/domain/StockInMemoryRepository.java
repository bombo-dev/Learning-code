package com.bombo.stock.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Repository;

@Repository
public class StockInMemoryRepository implements StockRepository {

    private Map<Long, Stock> stocks = new HashMap<>();
    private AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public Long save(Stock stock) {
        if (isExists(stock)) {
            update(stock);
            return stock.getId();
        }
        insert(stock);
        return stock.getId();
    }

    private void insert(Stock stock) {
        Long id = (long) idGenerator.incrementAndGet();
        Stock.newInstance(id, stock.getCount());
        stocks.put(id, stock);
    }

    private boolean isExists(Stock stock) {
        return stocks.containsKey(stock.getId());
    }

    private void update(Stock stock) {
        stocks.put(stock.getId(), stock);
    }

    @Override
    public Optional<Stock> findById(Long id) {
        return Optional.ofNullable(stocks.get(id));
    }

    @Override
    public void deleteAll() {
        stocks.clear();
        idGenerator.set(0);
    }
}
