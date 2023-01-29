package org.tinger.data.jdbc.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcHandler<T> {
    void setParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException;

    T getResult(ResultSet resultSet, int columnIndex) throws SQLException;
}