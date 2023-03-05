package org.tinger.data.jdbc.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.ClassUtils;
import org.tinger.common.utils.ServiceLoaderUtils;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.anno.TingerShardAlgorithm;
import org.tinger.data.jdbc.namespace.JdbcNamespace;
import org.tinger.data.jdbc.namespace.JdbcNamespaceBuilder;
import org.tinger.data.jdbc.repository.JdbcClusterRepository;
import org.tinger.data.jdbc.repository.JdbcSingletRepository;
import org.tinger.data.jdbc.repository.ShardAlgorithm;
import org.tinger.data.jdbc.source.TingerJdbcDataSourceManager;
import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

import java.util.List;

@Component
public class RepositoryBeanPostProcessor implements BeanPostProcessor, InitializingBean {
    private TingerJdbcDataSourceManager manager;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> type = bean.getClass();
        if (bean instanceof JdbcSingletRepository<?, ?> repository) {
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            List<TingerJdbcDataSourceWrapper> sourceWrappers = manager.singlet(datasource.value());
            JdbcNamespace namespace = JdbcNamespaceBuilder.build(sourceWrappers, database.value(), datatable.value());
            repository.setNamespace(namespace);
        } else if (bean instanceof JdbcClusterRepository<?, ?> repository) {
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            List<List<TingerJdbcDataSourceWrapper>> cluster = manager.cluster(datasource.value());
            List<JdbcNamespace> namespaces = cluster.stream().map(JdbcNamespaceBuilder::build).toList();
            TingerShardAlgorithm algorithm = type.getDeclaredAnnotation(TingerShardAlgorithm.class);
            ShardAlgorithm shardAlgorithm = (ShardAlgorithm) ClassUtils.newInstance(algorithm.value());
            shardAlgorithm.build(namespaces, database.value(), datatable.value());
            repository.setAlgorithm(shardAlgorithm);
        }
        return bean;
    }

    @Override
    public void afterPropertiesSet() {
        this.manager = ServiceLoaderUtils.load(TingerJdbcDataSourceManager.class);
        if (this.manager == null) {
            throw new RuntimeException();
        }
    }
}
