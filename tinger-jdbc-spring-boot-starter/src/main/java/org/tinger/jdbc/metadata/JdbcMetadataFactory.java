package org.tinger.jdbc.metadata;

import org.tinger.common.utils.ClassUtils;
import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.repository.AbstractRepository;
import org.tinger.jdbc.mysql.MysqlMetadataBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JdbcMetadataFactory {
    private final Map<JdbcDriver, JdbcMetadataBuilder> mapper = new HashMap<>();

    private JdbcMetadataFactory() {
        mapper.put(JdbcDriver.MYSQL, new MysqlMetadataBuilder());
    }

    @SuppressWarnings("unchecked")
    public <T, K> JdbcMetadata<T, K> build(AbstractRepository<T, K> repository) {
        List<Class<?>> classes = ClassUtils.getGenericSuperclass(this);
        return mapper.get(repository.getDriver()).build((Class<T>) classes.get(0), repository.getClass());
    }

    private static final JdbcMetadataFactory factory = new JdbcMetadataFactory();

    public static JdbcMetadataFactory getInstance() {
        return factory;
    }
}
