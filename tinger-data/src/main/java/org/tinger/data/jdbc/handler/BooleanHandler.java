package org.tinger.data.jdbc.handler;


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
    public Boolean getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        boolean result = resultSet.getBoolean(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    public Boolean getResult(ResultSet resultSet, String columnName) throws SQLException {
        boolean result = resultSet.getBoolean(columnName);
        return resultSet.wasNull() ? null : result;
    }
}
