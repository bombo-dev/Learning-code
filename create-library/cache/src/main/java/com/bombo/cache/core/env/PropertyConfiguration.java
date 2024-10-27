package com.bombo.cache.core.env;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RedisProperty.class})
public class PropertyConfiguration {
}
