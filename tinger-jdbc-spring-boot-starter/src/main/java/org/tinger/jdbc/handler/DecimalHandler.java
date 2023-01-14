package org.tinger.jdbc.handler;


import org.tinger.jdbc.exception.JdbcHandlerException;

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
    public BigDecimal getResult(ResultSet resultSet, int columnIndex) {
        try {
            BigDecimal result = resultSet.getBigDecimal(columnIndex);
            return resultSet.wasNull() ? null : result;
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}