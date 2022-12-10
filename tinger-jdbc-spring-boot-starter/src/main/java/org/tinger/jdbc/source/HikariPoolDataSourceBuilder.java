package org.tinger.jdbc.source;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.config.JdbcDataSourceConfig;

import javax.sql.DataSource;

@Component
public class HikariPoolDataSourceBuilder implements JdbcDataSourceBuilder {
    @Override
    public DataSource build(JdbcDataSourceConfig config) {
        return null;
    }
}
