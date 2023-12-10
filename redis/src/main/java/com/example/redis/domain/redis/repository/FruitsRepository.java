package com.example.redis.domain.redis.repository;

import com.example.redis.domain.redis.Fruits;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FruitsRepository extends CrudRepository<Fruits, String> {

    Optional<Fruits> findByName(String name);

    Optional<Fruits> findByStock(Integer stock);
}
