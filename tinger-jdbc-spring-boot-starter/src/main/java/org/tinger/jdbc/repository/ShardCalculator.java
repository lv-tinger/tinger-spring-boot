package org.tinger.jdbc.repository;

public interface ShardCalculator {
    String calculateDatatable(String database, Object value);

    String calculateDatabase(String database, Object value);

    int calculateDatasource(Object value);
}