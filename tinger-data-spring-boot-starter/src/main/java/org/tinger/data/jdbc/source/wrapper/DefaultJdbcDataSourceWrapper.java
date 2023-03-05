package org.tinger.data.jdbc.source.wrapper;

import jakarta.servlet.http.PushBuilder;
import lombok.Builder;
import lombok.Getter;

import javax.sql.DataSource;

@Getter
@Builder
public class DefaultJdbcDataSourceWrapper implements TingerJdbcDataSourceWrapper {
    public static final int MASTER = 1;
    public static final int SLAVER = 2;
    private DataSource source;
    private String name;
    private int weight;
    private int position;

    @Override
    public boolean isMaster() {
        return (this.position & MASTER) == MASTER;
    }

    @Override
    public boolean isSlaver() {
        return (this.position & SLAVER) == SLAVER;
    }
}
