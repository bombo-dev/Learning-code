package com.bombo.cache.core.env;

import java.util.List;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(RedissonProperties.class)
public class RedissonConfiguration {

    private static final String REDIS_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config redissonConfig = new Config();
        if (redissonProperties.isClustered()) {
            List<String> clusterNodes = redissonProperties.getClusterNodes();
            redissonConfig.useClusterServers().addNodeAddress(clusterNodes.toArray(String[]::new));
            return Redisson.create(redissonConfig);
        }

        redissonConfig.useSingleServer()
                .setAddress(REDIS_PREFIX + redissonProperties.getHost() + ":" + redissonProperties.getPort());

        return Redisson.create(redissonConfig);
    }
}
