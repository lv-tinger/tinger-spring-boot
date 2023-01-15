package org.tinger.data.jdbc.handler;


import java.math.BigDecimal;
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
    public Integer getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int result = resultSet.getInt(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    public Integer getResult(ResultSet resultSet, String columnName) throws SQLException {
        int result = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : result;
    }
}
