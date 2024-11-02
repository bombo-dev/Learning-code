package com.bombo.stock.domain;

import lombok.Builder;

public class Stock {

    private Long id;
    private Long count;

    @Builder
    private Stock(Long id, Long count) {
        this.id = id;
        this.count = count;
    }

    public static Stock newInstance(Long id, Long count) {
        return Stock.builder()
                .id(id)
                .count(count)
                .build();
    }

    public static Stock injectId(Long id, Stock stock) {
        return Stock.builder()
                .id(id)
                .count(stock.getCount())
                .build();
    }

    public Long getId() {
        return id;
    }

    public Long getCount() {
        return count;
    }

    public void decrease() {
        if (count == 0) {
            throw new IllegalStateException("Stock is empty");
        }
        count--;
    }
}
