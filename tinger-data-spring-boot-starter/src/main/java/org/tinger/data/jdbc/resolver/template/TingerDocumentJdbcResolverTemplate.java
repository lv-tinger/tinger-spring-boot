package org.tinger.data.jdbc.resolver.template;

import java.sql.ResultSet;
import java.util.List;

public interface TingerDocumentJdbcResolverTemplate<T> {
    T resolveOne(ResultSet resultSet) throws Exception;

    List<T> resolveList(ResultSet resultSet) throws Exception;
}