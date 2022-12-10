package org.tinger.jdbc.metadata;

import lombok.Data;
import lombok.SneakyThrows;
import org.tinger.jdbc.handler.JdbcHandler;

import java.lang.reflect.Field;

/**
 * Created by tinger on 2022-10-18
 */
@Data
public class JdbcProperty {
    public static final int PRIMARY_KEY = 1;
    public static final int CREATE_TIME = 2;
    public static final int UPDATE_TIME = 4;
    public static final int STATUS = 8;
    public static final int VERSION = 16;
    public static final int DEFAULT = 32;
    private String name;
    private String column;
    private Class<?> type;
    private int attribute = 0;
    private JdbcHandler<?> handler;
    private Field field;

    @SneakyThrows
    public void setValue(Object object, Object propertyValue) {
        field.set(object, propertyValue);
    }

    @SneakyThrows
    public Object getValue(Object object) {
        return field.get(object);
    }

    public void setAttr(int value) {
        this.attribute = (this.attribute | value);
    }

    public boolean getAttr(int value) {
        return (this.attribute & value) == value;
    }

}
