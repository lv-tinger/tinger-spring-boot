package org.tinger.jdbc.mysql;

import org.tinger.common.utils.StringUtils;
import org.tinger.jdbc.dialect.JdbcDialect;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcProperty;
import org.tinger.jdbc.queryable.Criteria;

import java.util.LinkedList;
import java.util.List;

public class MysqlJdbcDialect implements JdbcDialect {
    @Override
    public <T, K> String buildCreateCommand(JdbcMetadata<T, K> metadata, T document, String database, String datatable) {
        List<String> propertyNames = new LinkedList<>();

        List<JdbcProperty> properties = metadata.getProperties();
        for (JdbcProperty property : properties) {
            Object value = property.getValue(document);
            if (value != null) {
                parameters.add(value);
                handlers.add(property.getHandler());
                propertyNames.add(property.getName());
            }
        }

        if (propertyNames.isEmpty()) {
            throw new RuntimeException();
        }

        String columns = StringUtils.join(propertyNames, ", ");
        String[] repeat = StringUtils.repeat("?", properties.size());
        String values = StringUtils.join(repeat, ", ");

        return StringUtils.format(CREATE_SQL, database, datatable, columns, values);
    }

    @Override
    public <T, K> String buildUpdateCommand(JdbcMetadata<T, K> metadata, String database, String datatable) {
        return null;
    }

    @Override
    public <T, K> String buildDeleteCommand(JdbcMetadata<T, K> metadata, String database, String datatable) {
        return null;
    }

    @Override
    public <T, K> String buildSelectCommand(JdbcMetadata<T, K> metadata, String database, String datatable) {
        return null;
    }

    @Override
    public <T, K> String buildUpdateCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria) {
        return null;
    }

    @Override
    public <T, K> String buildDeleteCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria) {
        return null;
    }

    @Override
    public <T, K> String buildSelectCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria) {
        return null;
    }
}
