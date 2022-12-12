package org.tinger.redis.config;

import lombok.Data;

import java.util.List;

/**
 * Created by tinger on 2022-12-12
 */
@Data
public class RedisConfig {
    private String type;
    private String urls;
}
