package org.tinger.data.jdbc.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.ServiceLoaderUtils;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.jdbc.repository.JdbcNamespace;
import org.tinger.data.jdbc.repository.JdbcSingletRepository;
import org.tinger.data.jdbc.source.TingerJdbcDataSourceManager;
import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

import java.util.List;

@Component
public class SingleRepositoryBeanPostProcessor implements BeanPostProcessor, InitializingBean {
    private TingerJdbcDataSourceManager manager;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JdbcSingletRepository<?, ?> repository) {
            Class<?> type = bean.getClass();
            TingerDataTable datatable = type.getDeclaredAnnotation(TingerDataTable.class);
            TingerDatabase database = type.getDeclaredAnnotation(TingerDatabase.class);
            TingerDataSource datasource = type.getDeclaredAnnotation(TingerDataSource.class);
            List<TingerJdbcDataSourceWrapper> sourceWrappers = manager.singlet(datasource.value());
            TingerJdbcDataSourceWrapper masterWrapper = sourceWrappers.stream().filter(x -> (x.getPosition() & 1) == 1).findFirst().orElse(null);
            if (masterWrapper == null) {
                throw new RuntimeException();
            }
            JdbcNamespace masterNamespace = JdbcNamespace.builder()
                    .master(masterWrapper.getSource())
                    .database(database.value())
                    .datatable(datatable.value())
                    .build();
            repository.setNamespace(masterNamespace);
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
