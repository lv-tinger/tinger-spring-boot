package org.tinger.jdbc.scan;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcRepository;

@Component
public class JdbcRepositoryRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        JdbcRepositoryScanner jdbcRepositoryScanner = new JdbcRepositoryScanner(registry, false);
        jdbcRepositoryScanner.addIncludeFilter(new AnnotationTypeFilter(JdbcRepository.class));
        jdbcRepositoryScanner.doScan("org.tinger");
    }
}