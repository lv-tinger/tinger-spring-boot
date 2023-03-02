package org.tinger.data.jdbc.source.builder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.tinger.data.jdbc.source.config.JdbcCommonConfig;
import org.tinger.data.jdbc.source.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

public class HikariPoolDataSourceBuilder implements JdbcDataSourceBuilder {
    @Override
    public DataSource build(JdbcDataSourceConfig sourceConfig, JdbcCommonConfig commonConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(sourceConfig.getDriver());
        hikariConfig.setJdbcUrl(sourceConfig.getJdbcUrl());
        hikariConfig.setUsername(sourceConfig.getUsername());
        hikariConfig.setPassword(sourceConfig.getPassword());
        return new HikariDataSource(hikariConfig);
    }
}
