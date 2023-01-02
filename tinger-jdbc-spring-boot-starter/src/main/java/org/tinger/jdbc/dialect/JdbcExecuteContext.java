package org.tinger.jdbc.dialect;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Limited;
import org.tinger.jdbc.queryable.Ordered;
import org.tinger.jdbc.queryable.Update;

import java.util.List;

@Data
@Builder
public class JdbcExecuteContext<T, ID> {
    private JdbcMetadata<T, ID> metadata;

    private T document;
    private ID primary;

    private Update update;
    private Criteria criteria;
    private Ordered ordered;
    private Limited limited;

    private String command;
    private List<JdbcHandler<?>> handlers;
    private List<Object> parameters;
    private Object shard;

    public String calculateDatabase() {
        return this.metadata.shardingMetadata()
                ? metadata.getShardCalculator().calculateDatabase(metadata.getDatabase(), shard)
                : metadata.getDatabase();
    }

    public String calculateDatatable() {
        return this.metadata.shardingMetadata()
                ? metadata.getShardCalculator().calculateDatatable(metadata.getDatatable(), shard)
                : metadata.getDatatable();
    }
}
