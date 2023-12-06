package com.example.redis.global.config;

import com.example.redis.global.property.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@RequiredArgsConstructor
@EnableRedisRepositories(basePackages = "com.example.redis.domain.redis")
@Configuration
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();

        return new LettuceConnectionFactory(host, port);
    }
}
