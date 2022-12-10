package org.tinger.jdbc.handler;

import org.tinger.jdbc.exception.JdbcHandlerException;

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
    public Long getResult(ResultSet resultSet, int columnIndex) {
        try {
            long result = resultSet.getLong(columnIndex);
            return resultSet.wasNull() ? null : result;
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}
