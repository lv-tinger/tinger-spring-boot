package org.tinger.data.jdbc.namespace;

import javax.sql.DataSource;

public class SimpleJdbcNamespace extends AbstractJdbcNamespace {

    public SimpleJdbcNamespace(DataSource master) {
        this(master, null, null);
    }

    public SimpleJdbcNamespace(DataSource master, String database, String datatable) {
        super(master, database, datatable);
    }

    @Override
    public DataSource getSlaver() {
        return this.master;
    }
}