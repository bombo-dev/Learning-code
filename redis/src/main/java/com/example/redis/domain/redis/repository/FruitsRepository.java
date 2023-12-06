package com.example.redis.domain.redis.repository;

import com.example.redis.domain.redis.Fruits;
import org.springframework.data.repository.CrudRepository;

public interface FruitsRepository extends CrudRepository<Fruits, String> {
}
