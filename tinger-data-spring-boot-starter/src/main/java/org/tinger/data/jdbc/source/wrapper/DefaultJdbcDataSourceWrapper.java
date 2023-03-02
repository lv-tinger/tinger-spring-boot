package org.tinger.data.jdbc.source.wrapper;

import lombok.Builder;
import lombok.Getter;

import javax.sql.DataSource;

@Getter
@Builder
public class DefaultJdbcDataSourceWrapper implements TingerJdbcDataSourceWrapper {
    private DataSource source;
    private String name;
    private int weight;
    private int position;
}
