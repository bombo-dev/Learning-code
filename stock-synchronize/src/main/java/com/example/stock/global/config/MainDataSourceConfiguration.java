package com.example.stock.global.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.stock.stock",
        entityManagerFactoryRef = "mainEntityManager",
        transactionManagerRef = "mainTransactionManager")
public class MainDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties mainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource mainDataSource() {
        return mainDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "mainEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManager(
            EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mainDataSource())
                .packages("com.example.stock.stock")
                .build();
    }

    @Bean(name = "mainTransactionManager")
    @Primary
    public PlatformTransactionManager mainTransactionManager(
            @Qualifier("mainEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean)
    {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }
}
