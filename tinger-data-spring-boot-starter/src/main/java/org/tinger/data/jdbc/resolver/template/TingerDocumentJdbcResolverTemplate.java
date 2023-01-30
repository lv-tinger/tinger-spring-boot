package org.tinger.data.jdbc.resolver.template;

import java.sql.ResultSet;

public interface TingerDocumentJdbcResolverTemplate<T> {
    T resolve(ResultSet resultSet) throws Exception;
}