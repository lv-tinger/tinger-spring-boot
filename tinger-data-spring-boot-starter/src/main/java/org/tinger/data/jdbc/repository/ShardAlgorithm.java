package org.tinger.data.jdbc.repository;

import org.tinger.data.jdbc.namespace.JdbcNamespace;

import javax.sql.DataSource;
import java.util.List;


public abstract class ShardAlgorithm<T> {
    protected List<JdbcNamespace> jdbcNamespaces;
    protected String database;
    protected String datatable;

    public abstract DataSource master(T object);

    public abstract DataSource slaver(T document);

    public abstract String datatable(T object);

    public abstract String database(T object);

    public ShardAlgorithm<T> build(List<JdbcNamespace> jdbcNamespaces, String database, String datatable) {
        this.jdbcNamespaces = jdbcNamespaces;
        this.datatable = datatable;
        this.database = database;
        return this;
    }
}
