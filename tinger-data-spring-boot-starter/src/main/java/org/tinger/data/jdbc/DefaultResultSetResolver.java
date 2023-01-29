package org.tinger.data.jdbc;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultResultSetResolver<T> implements ResultSetResolver<T> {

    private Constructor<T> constructor;

    private List<TingerProperty> properties;

    private List<JdbcHandler<?>> handlers;


    public DefaultResultSetResolver(TingerMetadata<T> metadata){
        this.constructor = metadata.getConstructor();

        int size = metadata.getProperties().size() + 1;

        this.properties = new ArrayList<>(size);
        this.handlers = new ArrayList<>(size);

        JdbcHandlerHolder handlerHolder = JdbcHandlerHolder.getInstance();

        this.properties.add(metadata.getPrimaryKey());
        this.handlers.add(handlerHolder.get(metadata.getPrimaryKey().getType()));
        for (TingerProperty property : metadata.getProperties()){
            this.properties.add(property);
            this.handlers.add(handlerHolder.get(property.getType()));
        }
    }

    @Override
    public T resolve(ResultSet resultSet) throws Exception {
        if (resultSet.next()) {
            T instance = constructor.newInstance();
            int length = properties.size();
            for (int i = 0; i < length; i++) {
                Object value = handlers.get(i).getResult(resultSet, i + 1);
                if (value == null) {
                    continue;
                }
                properties.get(i).setValue(instance, value);
            }
            return instance;
        }

        return null;
    }

    @Override
    public List<T> resolveList(ResultSet resultSet) throws Exception {
        LinkedList<T> list = new LinkedList<>();
        int length = this.properties.size();
        while (resultSet.next()) {
            T instance = constructor.newInstance();
            for (int i = 0; i < length; i++) {
                Object value = handlers.get(i).getResult(resultSet, i + 1);
                if (value == null) {
                    continue;
                }
                properties.get(i).setValue(instance, value);
            }
            list.add(instance);
        }
        return list;
    }
}
