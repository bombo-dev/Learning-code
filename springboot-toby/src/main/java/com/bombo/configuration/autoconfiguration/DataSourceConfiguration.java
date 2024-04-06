package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.ConditionalMyOnClass;
import com.bombo.configuration.DataSourceProperties;
import com.bombo.configuration.EnableMyConfigurationProperties;
import com.bombo.configuration.MyAutoConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@MyAutoConfiguration
@EnableMyConfigurationProperties(clazz = DataSourceProperties.class)
@ConditionalMyOnClass("org.springframework.jdbc.core.JdbcOperations")
public class DataSourceConfiguration {

    @ConditionalMyOnClass("com.zaxxer.hikari.HikariDataSource")
    @ConditionalOnMissingBean
    @Bean
    DataSource hikariDataSource(DataSourceProperties property) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(property.getDriverClassName());
        hikariDataSource.setJdbcUrl(property.getUrl());
        hikariDataSource.setUsername(property.getUsername());
        hikariDataSource.setPassword(property.getPassword());
        return hikariDataSource;
    }

    @ConditionalOnMissingBean
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
