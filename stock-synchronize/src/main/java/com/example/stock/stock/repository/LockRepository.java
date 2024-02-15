package com.example.stock.stock.repository;

public interface LockRepository {

    void getLock(String key, Long lockTime);
    void releaseLock(String key);
}
