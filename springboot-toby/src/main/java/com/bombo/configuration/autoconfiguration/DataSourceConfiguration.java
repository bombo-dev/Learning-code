package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.ConditionalMyOnClass;
import com.bombo.configuration.DataSourceProperties;
import com.bombo.configuration.EnableMyConfigurationProperties;
import com.bombo.configuration.MyAutoConfiguration;
import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@MyAutoConfiguration
@EnableMyConfigurationProperties(clazz = DataSourceProperties.class)
@ConditionalMyOnClass("org.springframework.jdbc.core.JdbcOperations")
public class DataSourceConfiguration {

    @Bean
    DataSource dataSource(DataSourceProperties property) throws ClassNotFoundException {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass((Class<? extends Driver>) Class.forName(property.getDriverClassName()));
        simpleDriverDataSource.setUrl(property.getUrl());
        simpleDriverDataSource.setUsername(property.getUsername());
        simpleDriverDataSource.setPassword(property.getPassword());
        return simpleDriverDataSource;
    }
}
