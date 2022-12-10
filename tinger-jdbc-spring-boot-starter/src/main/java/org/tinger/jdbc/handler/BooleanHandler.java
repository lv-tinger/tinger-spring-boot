package org.tinger.jdbc.handler;


import org.tinger.jdbc.exception.JdbcHandlerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class BooleanHandler extends AbstractJdbcHandler<Boolean> {
    protected BooleanHandler() {
        super(Types.BOOLEAN);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setBoolean(parameterIndex, (Boolean) parameterValue);
    }

    @Override
    public Boolean getResult(ResultSet resultSet, int columnIndex) {
        try {
            boolean result = resultSet.getBoolean(columnIndex);
            return resultSet.wasNull() ? null : result;
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}
