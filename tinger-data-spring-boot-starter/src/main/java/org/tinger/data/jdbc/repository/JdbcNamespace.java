package org.tinger.data.jdbc.repository;

import lombok.Builder;
import lombok.Data;
import org.tinger.common.utils.CollectionUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Builder
public class JdbcNamespace {
    private DataSource master;
    private List<DataSource> salver;
    private String database;
    private String datatable;

    public String getDatabase() {
        return this.database;
    }

    public String getDatatable() {
        return this.datatable;
    }

    public DataSource getMaster() {
        return this.master;
    }

    public DataSource getSlaver() {
        if (CollectionUtils.isEmpty(this.salver)) {
            return this.master;
        }

        if (this.salver.size() == 1) {
            return this.salver.get(0);
        }

        return null;
    }
}