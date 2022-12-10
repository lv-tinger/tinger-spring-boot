package org.tinger.jdbc.metadata;

import org.tinger.jdbc.core.JdbcDriver;

public interface JdbcMetadataBuilder {
    JdbcDriver getDriver();

    <T, K> JdbcMetadata<T, K> build(Class<T> metadataType, Class<?> repositoryType);
}
