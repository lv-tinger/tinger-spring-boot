package org.tinger.data.jdbc.resolver;

import java.sql.ResultSet;

/**
 * Created by tinger on 2023-01-30
 */
public interface TingerResolver<T> {
    T resolve(ResultSet set) throws Exception;
}