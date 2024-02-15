package com.example.stock.global.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class LockDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.lock")
    public DataSourceProperties lockDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource lockDataSource() {
        return lockDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "lockTransactionManager")
    public JdbcTransactionManager lockTransactionManager(@Qualifier("lockDataSource") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
