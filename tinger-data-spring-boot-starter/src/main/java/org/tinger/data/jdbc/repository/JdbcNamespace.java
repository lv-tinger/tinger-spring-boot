package org.tinger.data.jdbc.repository;

import lombok.Builder;
import lombok.Data;

import javax.sql.DataSource;

@Builder
@Data
public class JdbcNamespace {
    private DataSource source;
    private String database;
    private String datatable;
}