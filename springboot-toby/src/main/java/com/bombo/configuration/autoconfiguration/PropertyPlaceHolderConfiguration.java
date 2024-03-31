package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.MyAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@MyAutoConfiguration
public class PropertyPlaceHolderConfiguration {

    @Bean
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
