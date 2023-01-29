package org.tinger.data.core.meta;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;

@Data
@Builder
public class TingerProperty {
    private String name;
    private Class<?> type;
    private String column;
    private Field field;
    //private JdbcHandler<?> handler;

    public Object getValue(Object object) throws IllegalAccessException {
        return field.get(object);
    }

    public void setValue(Object object, Object value) throws IllegalAccessException {
        if (value == null) {
            return;
        }

        field.set(object, value);
    }
}