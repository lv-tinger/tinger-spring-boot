package org.tinger.data.jdbc.source.wrapper;

import javax.sql.DataSource;

public interface TingerJdbcDataSourceWrapper {
    int getPosition();

    int getWeight();

    String getName();

    DataSource getSource();

    boolean isMaster();

    boolean isSlaver();
}