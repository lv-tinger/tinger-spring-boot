package org.tinger.data.jdbc.handler;


import org.tinger.common.buffer.TingerMapBuffer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JdbcHandlerHolder {
    private static final JdbcHandlerHolder JDBC_HANDLER_HOLDER = new JdbcHandlerHolder();
    private final Map<Class<?>, JdbcHandler<?>> handlerBuffer = new HashMap<>();


    private JdbcHandlerHolder() {
        init();
    }

    public static JdbcHandlerHolder getInstance() {
        return JDBC_HANDLER_HOLDER;
    }

    private void init() {
        handlerBuffer.put(Integer.class, new IntegerHandler());
        handlerBuffer.put(Long.class, new LongHandler());
        handlerBuffer.put(String.class, new StringHandler());
        handlerBuffer.put(Boolean.class, new BooleanHandler());
        handlerBuffer.put(Date.class, new DateHandler());
        handlerBuffer.put(BigDecimal.class, new DecimalHandler());
        handlerBuffer.put(Map.class, new MapHandler());
    }

    @SuppressWarnings("unchecked")
    public <T> JdbcHandler<T> get(Class<T> type) {
        Objects.requireNonNull(type);
        JdbcHandler<?> handler = handlerBuffer.get(type);
        if (handler != null) {
            return (JdbcHandler<T>) handler;
        }

        throw new RuntimeException();
    }
}
