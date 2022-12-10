package org.tinger.jdbc.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.jdbc.config.JdbcDataSourceConfig;
import org.tinger.jdbc.config.JdbcDataSources;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TingerJdbcDataSource {
    private final JdbcDataSources jdbcDataSources;

    private final JdbcDataSourceBuilder builder;

    private final TingerMapBuffer<String, DataSource> buffer = new TingerMapBuffer<>();

    @Autowired
    public TingerJdbcDataSource(JdbcDataSources jdbcDataSources, JdbcDataSourceBuilder builder) {
        this.jdbcDataSources = jdbcDataSources;
        this.builder = builder;
    }

    public DataSource load(String name) {
        return buffer.get(name, () -> {
            JdbcDataSourceConfig config = jdbcDataSources.getConfig().getDatabases().get(name);
            if (config == null) {
                config = jdbcDataSources.getConfig().getDatabases().values().stream()
                        .filter(x -> x.getAlias() != null)
                        .filter(x -> x.getAlias().contains(name))
                        .findFirst().orElse(null);
            }

            if (config == null) {
                throw new RuntimeException();
            }
            return builder.build(config);
        });
    }

    public List<DataSource> shard(String name) {
        List<String> names = jdbcDataSources.getConfig().getSharding().get(name);
        return names.stream().map(this::load).collect(Collectors.toList());
    }

}
