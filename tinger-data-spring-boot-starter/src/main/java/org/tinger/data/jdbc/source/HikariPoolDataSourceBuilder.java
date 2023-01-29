package org.tinger.data.jdbc.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;
import org.tinger.data.jdbc.config.JdbcDataSourceConfig;

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
