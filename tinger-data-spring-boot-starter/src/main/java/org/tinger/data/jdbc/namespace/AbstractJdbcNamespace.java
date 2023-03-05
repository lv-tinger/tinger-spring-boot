package org.tinger.data.jdbc.namespace;

import javax.sql.DataSource;

public abstract class AbstractJdbcNamespace implements JdbcNamespace {
    private final String database;
    private final String datatable;
    protected final DataSource master;

    public AbstractJdbcNamespace(DataSource master, String database, String datatable) {
        this.master = master;
        this.database = database;
        this.datatable = datatable;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public String getDatatable() {
        return this.datatable;
    }

    @Override
    public DataSource getMaster() {
        return this.master;
    }
}
