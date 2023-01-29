package org.tinger.data.jdbc.repository;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;

@Component
public class SingleRepositoryBeanPostProcessor implements BeanPostProcessor {

    private final TingerJdbcDataSource tingerJdbcDataSource;

    public SingleRepositoryBeanPostProcessor(TingerJdbcDataSource tingerJdbcDataSource) {
        this.tingerJdbcDataSource = tingerJdbcDataSource;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JdbcSingletRepository<?, ?> repository) {
            Class<?> type = bean.getClass();
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            DataSource source = tingerJdbcDataSource.load(datasource.value());
            JdbcNamespace namespace = JdbcNamespace.builder().source(source).database(database.value()).datatable(datatable.value()).build();
            repository.setNamespace(namespace);
        }
        return bean;
    }
}
