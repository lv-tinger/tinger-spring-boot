package org.tinger.data.jdbc.namespace;

import javax.sql.DataSource;

public interface JdbcNamespace {
    DataSource getMaster();
    DataSource getSlaver();
    String getDatabase();
    String getDatatable();
}