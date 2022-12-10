package org.tinger.jdbc.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface JdbcHandler<T> {
    void setParameter(PreparedStatement statement, int parameterIndex, Object parameterValue);

    T getResult(ResultSet resultSet, int columnIndex);
}
