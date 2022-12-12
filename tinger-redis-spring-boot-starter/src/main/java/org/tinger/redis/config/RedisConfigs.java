package org.tinger.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by tinger on 2022-12-12
 */
@Data
@ConfigurationProperties(prefix = "tinger.redis.config")
public class RedisConfigs {
    private Map<String, RedisConfig> configs;
}
