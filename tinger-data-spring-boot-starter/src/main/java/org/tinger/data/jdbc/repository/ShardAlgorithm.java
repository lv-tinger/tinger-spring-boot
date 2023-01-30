package org.tinger.data.jdbc.repository;

import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

@Data
public abstract class ShardAlgorithm {

    private List<DataSource> dataSources;
    private String database;
    private String datatable;

    public abstract DataSource source(Object object);

    public abstract String datatable(Object object);

    public abstract String database(Object object);
}
