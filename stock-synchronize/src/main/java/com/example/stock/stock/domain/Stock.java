package com.example.stock.stock.domain;

import jakarta.persistence.*;

@Table(name = "stock")
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public Stock(){}

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("stock must not Negative");
        }

        this.quantity -= quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }
}
