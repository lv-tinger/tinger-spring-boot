package org.tinger.jdbc.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.springframework.stereotype.Component;
import org.tinger.jdbc.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

@Component
public class HikariPoolDataSourceBuilder implements JdbcDataSourceBuilder {
    @Override
    public DataSource build(JdbcDataSourceConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(config.getDriver());
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        return new HikariDataSource(hikariConfig);
    }
}
