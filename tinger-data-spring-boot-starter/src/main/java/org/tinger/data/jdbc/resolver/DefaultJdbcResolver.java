package org.tinger.data.jdbc.resolver;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.ArrayUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class DefaultJdbcResolver<T> implements TingerJdbcResolver<T> {
    private static final TingerMapBuffer<Class<?>, TingerJdbcResolver<?>> BUFFER = new TingerMapBuffer<>();

    private Constructor<T> constructor;

    private Field[] properties;
    private JdbcHandler<?>[] jdbcHandlers;

    @Override
    public T resolve(ResultSet resultSet) throws Exception {
        T instance = constructor.newInstance();
        for (int i = 0; i < this.properties.length; i++) {
            Object value = jdbcHandlers[i].getResult(resultSet, i + 1);
            if (value != null) {
                properties[i].set(instance, value);
            }
        }
        return instance;
    }

    private TingerJdbcResolver<T> generate(TingerMetadata<T> metadata) {
        this.constructor = metadata.getConstructor();
        List<Field> fields = new LinkedList<>();
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        fields.add(metadata.getPrimaryKey().getField());
        handlers.add(JdbcHandlerHolder.getInstance().get(metadata.getPrimaryKey().getType()));
        fields.addAll(metadata.getProperties().stream().map(TingerProperty::getField).toList());
        handlers.addAll(metadata.getProperties().stream().map(x -> JdbcHandlerHolder.getInstance().get(x.getType())).toList());
        this.properties = ArrayUtils.toArray(fields, Field.class);
        this.jdbcHandlers = ArrayUtils.toArray(handlers, JdbcHandler.class);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <T> TingerJdbcResolver<T> build(TingerMetadata<T> metadata) {
        TingerJdbcResolver<?> resolver = BUFFER.get(metadata.getType(), () -> new DefaultJdbcResolver<T>().generate(metadata));
        return (TingerJdbcResolver<T>) resolver;
    }

    public static void register(Class<?> type, TingerJdbcResolver<?> resolver) {
        BUFFER.set(type, resolver);
    }
}
