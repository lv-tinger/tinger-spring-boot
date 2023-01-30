package org.tinger.data.jdbc.resolver;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tinger on 2023-01-30
 */
public interface TingerResolver<T> {
    <T> T resolve(ResultSet set) throws Exception;
}
