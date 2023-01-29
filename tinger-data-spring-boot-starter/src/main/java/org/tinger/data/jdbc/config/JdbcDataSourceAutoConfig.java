package org.tinger.data.jdbc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = {JdbcDataSourceConfigs.class})
public class JdbcDataSourceAutoConfig {
    private final JdbcDataSourceConfigs config;

    @Autowired
    public JdbcDataSourceAutoConfig(JdbcDataSourceConfigs config) {
        this.config = config;
    }

    @Bean
    @ConditionalOnMissingBean(JdbcDataSources.class)
    public JdbcDataSources jdbcDataSources() {
        JdbcDataSources sources = new JdbcDataSources();
        sources.setConfig(config);
        return sources;
    }
}
