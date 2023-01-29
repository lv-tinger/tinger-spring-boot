package org.tinger.data.jdbc.resolver;

public abstract class CustomerTingerJdbcResolver<T> implements TingerJdbcResolver<T> {
    public abstract Class<?> getType();
}
