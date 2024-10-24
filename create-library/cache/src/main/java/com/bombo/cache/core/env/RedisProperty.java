package com.bombo.cache.core.env;

import java.util.List;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "spring.redis.host", havingValue = "true")
public class RedisProperty {

    private static final String REDIS_PREFIX = "redis://";

    @Value("${spring.redis.host:localhost}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.cluster.enable:false}")
    private boolean isClusterEnabled;
    @Value("${spring.redis.cluster.nodes:}")
    private List<String> clusterNodes;

    @Bean
    public RedissonClient redissonClient() {
        Config redissonConfig = new Config();
        if (isClusterEnabled) {
            redissonConfig.useClusterServers().addNodeAddress(clusterNodes.toArray(String[]::new));
            return Redisson.create(redissonConfig);
        }

        redissonConfig.useSingleServer()
                .setAddress(REDIS_PREFIX + host + ":" + port);

        return Redisson.create(redissonConfig);
    }
}
