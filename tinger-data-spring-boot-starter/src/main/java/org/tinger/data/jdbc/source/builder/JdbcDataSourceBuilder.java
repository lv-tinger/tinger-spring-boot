package org.tinger.data.jdbc.source.builder;


import org.tinger.data.jdbc.source.config.JdbcCommonConfig;
import org.tinger.data.jdbc.source.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

public interface JdbcDataSourceBuilder {
    DataSource build(JdbcDataSourceConfig sourceConfig, JdbcCommonConfig commonConfig);
}
