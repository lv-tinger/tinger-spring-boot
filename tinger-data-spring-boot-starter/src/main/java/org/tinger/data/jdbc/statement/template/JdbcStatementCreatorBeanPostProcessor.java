package org.tinger.data.jdbc.statement.template;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.jdbc.statement.template.document.DocumentCreateStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.document.DocumentUpdateStatementCreatorTemplate;

@Component
public class JdbcStatementCreatorBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CustomerStatementCreatorTemplate creator) {
            if (StringUtils.equals(creator.getOperation(), CustomerStatementCreatorTemplate.CREATE)) {
                DocumentCreateStatementCreatorTemplate.register(creator.getType(), creator);
            } else if (StringUtils.equals(creator.getOperation(), CustomerStatementCreatorTemplate.UPDATE)) {
                DocumentUpdateStatementCreatorTemplate.register(creator.getType(), creator);
            } else {
                throw new RuntimeException();
            }
        }
        return bean;
    }
}