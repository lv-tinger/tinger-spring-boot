package org.tinger.data.jdbc.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class DateHandler extends AbstractJdbcHandler<Date> {
    public DateHandler() {
        super(Types.BIGINT);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setLong(parameterIndex, ((Date) parameterValue).getTime());
    }

    @Override
    public Date getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        long result = resultSet.getLong(columnIndex);
        return resultSet.wasNull() ? null : new Date(result);
    }

    @Override
    public Date getResult(ResultSet resultSet, String columnName) throws SQLException {
        long result = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : new Date(result);
    }
}
