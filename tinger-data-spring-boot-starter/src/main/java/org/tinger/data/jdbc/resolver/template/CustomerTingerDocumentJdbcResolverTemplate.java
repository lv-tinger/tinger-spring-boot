package org.tinger.data.jdbc.resolver.template;

public abstract class CustomerTingerDocumentJdbcResolverTemplate<T> implements TingerDocumentJdbcResolverTemplate<T> {
    public abstract Class<?> getType();
}