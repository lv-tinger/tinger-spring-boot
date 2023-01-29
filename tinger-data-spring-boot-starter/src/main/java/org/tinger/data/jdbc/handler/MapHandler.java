package org.tinger.data.jdbc.handler;


import org.tinger.common.serialize.JsonSerializer;

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
    public Map<String, Object> getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String result = resultSet.getString(columnIndex);
        return resultSet.wasNull() ? null : serializer.fromJson(result);
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, String columnName) throws SQLException {
        String result = resultSet.getString(columnName);
        return resultSet.wasNull() ? null : serializer.fromJson(result);
    }
}