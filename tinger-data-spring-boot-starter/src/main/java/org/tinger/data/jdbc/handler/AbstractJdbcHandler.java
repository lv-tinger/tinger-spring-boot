package org.tinger.data.jdbc.handler;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractJdbcHandler<T> implements JdbcHandler<T> {
    protected int parameterType;

    protected AbstractJdbcHandler(int parameterType) {
        this.parameterType = parameterType;
    }

    public void setParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        if (parameterValue == null) {
            setNullParameter(statement, parameterIndex);
        } else {
            setNonParameter(statement, parameterIndex, parameterValue);
        }
    }

    protected abstract void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException;

    protected void setNullParameter(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setNull(parameterIndex, parameterType);
    }

    public abstract T getResult(ResultSet resultSet, int columnIndex) throws SQLException;

    public abstract T getResult(ResultSet resultSet, String columnName) throws SQLException;
}
