package org.tinger.data.jdbc.statement;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.StringUtils;

@Component
public class JdbcStatementCreatorBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CustomerStatementCreator creator) {
            if (StringUtils.equals(creator.getOperation(), CustomerStatementCreator.CREATE)) {
                DocumentCreateStatementCreator.register(creator.getType(), creator);
            } else if (StringUtils.equals(creator.getOperation(), CustomerStatementCreator.UPDATE)) {
                DocumentUpdateStatementCreator.register(creator.getType(), creator);
            } else {
                throw new RuntimeException();
            }
        }

        return bean;
    }
}