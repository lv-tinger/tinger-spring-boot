package org.tinger.redis.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tinger on 2022-12-12
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = {RedisConfigs.class})
public class RedisAutoConfig {
    @Getter
    private RedisConfigs redisConfigs;

    @Autowired
    public RedisAutoConfig(RedisConfigs redisConfigs) {
        this.redisConfigs = redisConfigs;
    }

    @Bean
    @ConditionalOnMissingBean(RedisSource.class)
    public RedisSource redisConfig() {
        RedisSource source = new RedisSource();
        source.setConfig(this.redisConfigs);
        return source;
    }

}
