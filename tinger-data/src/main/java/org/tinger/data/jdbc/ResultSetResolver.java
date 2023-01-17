package org.tinger.data.jdbc;

import lombok.*;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by tinger on 2023-01-17
 */
@Data
@Builder
public class ResultSetResolver {
    private Constructor<?> constructor;
    private List<TingerProperty> properties;

    private List<JdbcHandler<?>> handlers;

    public Object resolve(ResultSet resultSet) throws Exception {
        Object instance = constructor.newInstance();
        int length = properties.size();
        for (int i = 0; i < length; i++) {
            Object result = handlers.get(i).getResult(resultSet, i + 1);
            if (result != null) {
                properties.get(i).setValue(instance, result);
            }
        }
        return instance;
    }
}
