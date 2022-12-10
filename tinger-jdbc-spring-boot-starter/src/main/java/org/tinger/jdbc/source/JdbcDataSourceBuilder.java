package org.tinger.jdbc.source;

import org.tinger.jdbc.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

public interface JdbcDataSourceBuilder {
    DataSource build(JdbcDataSourceConfig config);
}
