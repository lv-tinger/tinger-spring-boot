package org.tinger.jdbc.handler;


import org.tinger.common.serialize.JsonSerializer;
import org.tinger.jdbc.exception.JdbcHandlerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

public class MapHandler extends AbstractJdbcHandler<Map<String, Object>> {
    private final JsonSerializer serializer = JsonSerializer.getInstance();

    public MapHandler() {
        super(Types.NVARCHAR);
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setString(parameterIndex, serializer.toJson(parameterValue));
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, int columnIndex) {
        try {
            String result = resultSet.getString(columnIndex);
            return resultSet.wasNull() ? null : serializer.fromJson(result);
        } catch (SQLException e) {
            throw new JdbcHandlerException(columnIndex, e);
        }
    }
}