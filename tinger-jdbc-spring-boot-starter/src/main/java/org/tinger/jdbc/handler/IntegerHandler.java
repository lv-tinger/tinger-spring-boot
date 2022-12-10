package org.tinger.jdbc.handler;


import org.tinger.jdbc.exception.JdbcHandlerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class IntegerHandler extends AbstractJdbcHandler<Integer> {
    public IntegerHandler() {
        super(Types.INTEGER);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setInt(parameterIndex, (Integer) parameterValue);
    }

    @Override
    public Integer getResult(ResultSet resultSet, int columnIndex) {
        try {
            int result = resultSet.getInt(columnIndex);
            return resultSet.wasNull() ? null : result;
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}
