package org.tinger.data.jdbc.source;

import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

import java.util.List;

public interface TingerJdbcDataSourceManager {
    List<TingerJdbcDataSourceWrapper> singlet(String name);

    List<List<TingerJdbcDataSourceWrapper>> cluster(String name);
}