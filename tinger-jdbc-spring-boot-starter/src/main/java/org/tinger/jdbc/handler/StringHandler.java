package org.tinger.jdbc.handler;

import org.tinger.jdbc.exception.JdbcHandlerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class StringHandler extends AbstractJdbcHandler<String> {
    public StringHandler() {
        super(Types.NVARCHAR);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setString(parameterIndex, (String) parameterValue);
    }

    @Override
    public String getResult(ResultSet resultSet, int columnIndex) {
        try {
            String result = resultSet.getString(columnIndex);
            return resultSet.wasNull() ? null : result;
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}
