package org.tinger.data.jdbc;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetResolver<T> {
    T resolve(ResultSet resultSet) throws Exception;
    List<T> resolveList(ResultSet resultSet) throws Exception;
}
