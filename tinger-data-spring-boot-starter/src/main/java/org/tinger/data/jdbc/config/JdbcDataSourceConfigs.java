package org.tinger.data.jdbc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "tinger.jdbc.config")
public class JdbcDataSourceConfigs implements Serializable {
    private Map<String, JdbcDataSourceConfig> databases;
    private Map<String, List<String>> sharding;
}