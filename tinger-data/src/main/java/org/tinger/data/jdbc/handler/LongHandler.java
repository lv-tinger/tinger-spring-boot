package org.tinger.data.jdbc.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class LongHandler extends AbstractJdbcHandler<Long> {
    public LongHandler() {
        super(Types.BIGINT);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setLong(parameterIndex, (Long) parameterValue);
    }

    @Override
    public Long getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        long result = resultSet.getLong(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    public Long getResult(ResultSet resultSet, String columnName) throws SQLException {
        long result = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : result;
    }
}
