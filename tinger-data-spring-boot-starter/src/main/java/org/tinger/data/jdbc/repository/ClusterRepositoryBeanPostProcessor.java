package org.tinger.data.jdbc.repository;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ClusterRepositoryBeanPostProcessor implements BeanPostProcessor {
    private final TingerJdbcDataSource tingerJdbcDataSource;

    public ClusterRepositoryBeanPostProcessor(TingerJdbcDataSource tingerJdbcDataSource) {
        this.tingerJdbcDataSource = tingerJdbcDataSource;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JdbcClusterRepository<?, ?> repository) {
            Class<?> type = bean.getClass();
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            List<DataSource> sources = tingerJdbcDataSource.shard(datasource.value());
            ShardAlgorithm algorithm = ShardAlgorithm.builder().database(database.value()).datatable(datatable.value()).dataSources(sources).build();
            repository.setAlgorithm(algorithm);
        }
        return bean;
    }
}
