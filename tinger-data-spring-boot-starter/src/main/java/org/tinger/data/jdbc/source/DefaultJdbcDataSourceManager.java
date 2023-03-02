package org.tinger.data.jdbc.source;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.core.Initial;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.common.utils.ServiceLoaderUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.jdbc.source.builder.JdbcDataSourceBuilder;
import org.tinger.data.jdbc.source.config.*;
import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultJdbcDataSourceManager implements TingerJdbcDataSourceManager, Initial {
    private TingerJdbcConfig jdbcConfig;
    private JdbcDataSourceBuilder sourceBuilder;

    private final TingerMapBuffer<String, List<TingerJdbcDataSourceWrapper>> singleWrappers = new TingerMapBuffer<>();
    private final TingerMapBuffer<String, List<List<TingerJdbcDataSourceWrapper>>> clusterWrappers = new TingerMapBuffer<>();

    @Override
    public List<TingerJdbcDataSourceWrapper> singlet(String name) {
        return singleWrappers.get(name, () -> {
            SingletDataSourceConfig config = jdbcConfig.getSinglets().stream().filter(x -> StringUtils.equals(x.getName(), name)).findFirst().orElse(null);
            if (config == null) {
                throw new RuntimeException();
            }
            return build(config.getConfigs());
        });
    }

    @Override
    public List<List<TingerJdbcDataSourceWrapper>> cluster(String name) {
        return clusterWrappers.get(name, () -> {
            ClusterDataSourceConfig config = jdbcConfig.getClusters().stream().filter(x -> StringUtils.equals(x.getName(), name)).findFirst().orElse(null);
            if (config == null) {
                throw new RuntimeException();
            }
            return config.getConfigs().stream().map(this::build).collect(Collectors.toList());
        });
    }

    private List<TingerJdbcDataSourceWrapper> build(List<JdbcDataSourceConfig> configs) {
        if (CollectionUtils.isEmpty(configs)) {
            throw new RuntimeException();
        }
        return Collections.emptyList();
    }

    @Override
    public void init() {
        this.sourceBuilder = ServiceLoaderUtils.load(JdbcDataSourceBuilder.class);
        TingerJdbcConfigLoader loader = ServiceLoaderUtils.load(TingerJdbcConfigLoader.class);
        this.jdbcConfig = loader.load();
    }
}