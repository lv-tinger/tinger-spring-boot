package org.tinger.data.jdbc.handler;

import org.tinger.common.serialize.JsonSerializer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JsonHandler<T> extends AbstractJdbcHandler<T> {

    private final JsonSerializer serializer = JsonSerializer.getInstance();
    private final Class<T> type;

    public JsonHandler(Class<T> type) {
        super(Types.NVARCHAR);
        this.type = type;
    }

    @Override
    protected void setNonParameter(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
        statement.setString(parameterIndex, serializer.toJson(parameterValue));
    }

    @Override
    public T getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String string = resultSet.getString(columnIndex);
        return resultSet.wasNull() ? null : serializer.fromJson(string, type);
    }

    @Override
    public T getResult(ResultSet resultSet, String columnName) throws SQLException {
        String string = resultSet.getString(columnName);
        return resultSet.wasNull() ? null : serializer.fromJson(string, type);
    }
}
