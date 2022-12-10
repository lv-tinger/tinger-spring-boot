package org.tinger.jdbc.metadata;

import lombok.Data;
import org.tinger.common.utils.StringUtils;
import org.tinger.jdbc.repository.ShardCalculator;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by tinger on 2022-10-18
 */
@Data
public class JdbcMetadata<T, K> {
    private Class<?> type;
    private Constructor<T> constructor;
    private JdbcProperty primaryKey;
    private List<JdbcProperty> properties;
    private String database;
    private String datatable;
    private String datasource;

    private ShardCalculator shardCalculator;

    public boolean shardingMetadata() {
        return shardCalculator != null;
    }

    public JdbcProperty getPropertyByName(String name) {
        return this.properties.stream().filter(x -> StringUtils.equals(x.getName(), name)).findFirst().orElse(null);
    }
}
