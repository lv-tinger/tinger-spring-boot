package org.tinger.data.jdbc.handler;

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
    public String getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String result = resultSet.getString(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    public String getResult(ResultSet resultSet, String columnName) throws SQLException {
        String result = resultSet.getString(columnName);
        return resultSet.wasNull() ? null : result;
    }
}
