package com.bombo.cache.core.env;

import java.util.Collections;
import java.util.List;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "spring.redis", havingValue = "true")
@ConditionalOnBean({RedissonClient.class, RedisProperties.class})
@Component
public class RedissonProperties {

    public static final int DEFAULT_PORT = 6379;
    public static final String DEFAULT_HOST = "localhost";

    private final RedisProperties redisProperties;

    public RedissonProperties(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    public String getHost() {
        if (redisProperties.getHost() != null) {
            return redisProperties.getHost();
        }

        return DEFAULT_HOST;
    }

    public int getPort() {
        if (redisProperties.getPort() != 0) {
            return redisProperties.getPort();
        }

        return DEFAULT_PORT;
    }

    public boolean isClustered() {
        if (redisProperties.getCluster() != null) {
            return !redisProperties.getCluster().getNodes().isEmpty();
        }

        return false;
    }

    public List<String> getClusterNodes() {
        if (isClustered()) {
            return redisProperties.getCluster().getNodes();
        }

        return Collections.emptyList();
    }
}
