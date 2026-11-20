// .java
// Database Configuration
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Configures Database Connections
 */
@Configuration
public class DatabaseConfig {
    /**
     * Sales Database
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    DataSourceProperties salesDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    HikariDataSource salesDataSource(
            @Qualifier("salesDataSourceProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * Authentication Database (read-only)
     */
    @Bean
    @ConfigurationProperties("salesguru.auth.datasource")
    DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @ConfigurationProperties("salesguru.auth.datasource.hikari")
    HikariDataSource authDataSource(
            @Qualifier("authDataSourceProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
