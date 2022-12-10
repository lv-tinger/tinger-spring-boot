package org.tinger.jdbc.handler;


import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.jdbc.exception.JdbcHandlerUnSupportException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public final class JdbcHandlerHolder {
    private static final JdbcHandlerHolder JDBC_HANDLER_HOLDER = new JdbcHandlerHolder();
    private final TingerMapBuffer<Class<?>, JdbcHandler<?>> handlerBuffer = new TingerMapBuffer<>();


    private JdbcHandlerHolder() {
        init();
    }

    public static JdbcHandlerHolder getInstance() {
        return JDBC_HANDLER_HOLDER;
    }

    private void init() {
        handlerBuffer.set(Integer.class, new IntegerHandler());
        handlerBuffer.set(Long.class, new LongHandler());
        handlerBuffer.set(String.class, new StringHandler());
        handlerBuffer.set(Boolean.class, new BooleanHandler());
        handlerBuffer.set(Date.class, new DateHandler());
        handlerBuffer.set(BigDecimal.class, new DecimalHandler());
    }

    @SuppressWarnings("unchecked")
    public <T> JdbcHandler<T> get(Class<T> type) {
        Objects.requireNonNull(type);
        JdbcHandler<?> handler = handlerBuffer.get(type);
        if (handler != null) {
            return (JdbcHandler<T>) handler;
        }

        throw new JdbcHandlerUnSupportException();
    }
}
