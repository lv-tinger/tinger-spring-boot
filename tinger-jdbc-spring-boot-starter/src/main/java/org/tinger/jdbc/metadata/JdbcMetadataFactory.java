package org.tinger.jdbc.metadata;

import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.mysql.MysqlMetadataBuilder;

import java.util.HashMap;
import java.util.Map;

public class JdbcMetadataFactory {
    private final Map<JdbcDriver, JdbcMetadataBuilder> mapper = new HashMap<>();

    public JdbcMetadataFactory() {
        mapper.put(JdbcDriver.MYSQL, new MysqlMetadataBuilder());
    }

    private static final JdbcMetadataFactory factory = new JdbcMetadataFactory();

    public static JdbcMetadataFactory getInstance() {
        return factory;
    }

    public <T, K> JdbcMetadata<T, K> build(JdbcDriver driver, Class<T> metadataType, Class<?> repositoryType) {
        return mapper.get(driver).build(metadataType, repositoryType);
    }
}
