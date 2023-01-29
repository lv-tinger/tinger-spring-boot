package org.tinger.data.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.data.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcRepositoryBeanPostProcessor implements BeanPostProcessor {

    private final TingerJdbcDataSource tingerJdbcDataSource;

    public JdbcRepositoryBeanPostProcessor(TingerJdbcDataSource tingerJdbcDataSource) {
        this.tingerJdbcDataSource = tingerJdbcDataSource;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof JdbcStaticsRepository<?, ?> repository) {
            DataSource dataSource = tingerJdbcDataSource.load(repository.getDatasourceName());
            repository.setDatasource(dataSource);
        } else if (bean instanceof JdbcDynamicRepository<?, ?, ?> repository) {
            DynamicAlgorithm<?> algorithm = repository.getAlgorithm();
            List<DataSource> dataSources = tingerJdbcDataSource.shard(algorithm.getDatasourceName());
            algorithm.setDataSources(dataSources);
        }
        return bean;
    }
}
