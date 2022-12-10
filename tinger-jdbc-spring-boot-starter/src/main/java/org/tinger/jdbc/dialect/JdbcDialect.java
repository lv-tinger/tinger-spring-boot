package org.tinger.jdbc.dialect;

import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.queryable.Criteria;

public interface JdbcDialect {
    <T, K> String buildCreateCommand(JdbcMetadata<T, K> metadata, String database, String datatable);

    <T, K> String buildUpdateCommand(JdbcMetadata<T, K> metadata, String database, String datatable);

    <T, K> String buildDeleteCommand(JdbcMetadata<T, K> metadata, String database, String datatable);

    <T, K> String buildSelectCommand(JdbcMetadata<T, K> metadata, String database, String datatable);

    <T, K> String buildUpdateCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria);

    <T, K> String buildDeleteCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria);

    <T, K> String buildSelectCommand(JdbcMetadata<T, K> metadata, String database, String datatable, Criteria criteria);
}
