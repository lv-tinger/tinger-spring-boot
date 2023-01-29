package org.tinger.data.jdbc.source;


import org.tinger.data.jdbc.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

public interface JdbcDataSourceBuilder {
    DataSource build(JdbcDataSourceConfig config);
}
