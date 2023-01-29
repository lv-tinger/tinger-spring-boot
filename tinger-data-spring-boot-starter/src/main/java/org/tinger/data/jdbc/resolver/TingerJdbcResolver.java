package org.tinger.data.jdbc.resolver;

import java.sql.ResultSet;

public interface TingerJdbcResolver<T> {
    T resolve(ResultSet resultSet) throws Exception;
}
