package org.tinger.data.jdbc.namespace;

import org.tinger.common.algorithm.round.RoundRobin;

import javax.sql.DataSource;

public class ReadWriteRoundRobinJdbcNamespace extends AbstractJdbcNamespace {
    private final RoundRobin<DataSource> slaver;

    public ReadWriteRoundRobinJdbcNamespace(DataSource master, RoundRobin<DataSource> slaver, String database, String datatable) {
        super(master, database, datatable);
        this.slaver = slaver;
    }

    public ReadWriteRoundRobinJdbcNamespace(DataSource master, RoundRobin<DataSource> slaver) {
        this(master, slaver, null, null);
    }

    @Override
    public DataSource getSlaver() {
        return this.slaver.get();
    }
}
