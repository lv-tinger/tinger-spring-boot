package org.tinger.data.jdbc.namespace;

import javax.sql.DataSource;

public class ReadWriteJdbcNamespace extends AbstractJdbcNamespace {
    private final DataSource slaver;

    public ReadWriteJdbcNamespace(DataSource master, DataSource slaver, String database, String datatable) {
        super(master, database, datatable);
        this.slaver = slaver;
    }

    public ReadWriteJdbcNamespace(DataSource master, DataSource slaver) {
        this(master, slaver, null, null);
    }

    @Override
    public DataSource getSlaver() {
        return this.slaver;
    }
}