package org.tinger.data.jdbc.resolver.template;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class JdbcResolverBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CustomerTingerDocumentJdbcResolverTemplate<?> resolver) {
            DocumentJdbcResolverTemplateHolder.register(resolver.getType(), resolver);
        }
        return bean;
    }
}