package org.tinger.data.jdbc.handler;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DecimalHandler extends AbstractJdbcHandler<BigDecimal> {
    public DecimalHandler() {
        super(Types.DECIMAL);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setBigDecimal(parameterIndex, (BigDecimal) parameterValue);
    }

    @Override
    public BigDecimal getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        BigDecimal result = resultSet.getBigDecimal(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    public BigDecimal getResult(ResultSet resultSet, String columnName) throws SQLException {
        BigDecimal result = resultSet.getBigDecimal(columnName);
        return resultSet.wasNull() ? null : result;
    }
}