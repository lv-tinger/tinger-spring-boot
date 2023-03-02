package org.tinger.data.jdbc.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

@Component
public class ClusterRepositoryBeanPostProcessor implements BeanPostProcessor {
    private final TingerJdbcDataSourceWrapper tingerJdbcDataSourceWrapper;

    public ClusterRepositoryBeanPostProcessor(TingerJdbcDataSourceWrapper tingerJdbcDataSourceWrapper) {
        this.tingerJdbcDataSourceWrapper = tingerJdbcDataSourceWrapper;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /*if (bean instanceof JdbcClusterRepository<?, ?> repository) {
            Class<?> type = bean.getClass();
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            TingerShardAlgorithm algorithm = type.getDeclaredAnnotation(TingerShardAlgorithm.class);
            ShardAlgorithm shardAlgorithm = (ShardAlgorithm) ClassUtils.newInstance(algorithm.value());
            List<DataSource> sources = tingerJdbcDataSource.shard(datasource.value());
            shardAlgorithm.setDatabase(database.value());
            shardAlgorithm.setDatatable(datatable.value());
            shardAlgorithm.setDataSources(sources);
            repository.setAlgorithm(shardAlgorithm);
        }*/
        return bean;
    }
}
