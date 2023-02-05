package org.tinger.data.test.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.tinger.data.jdbc.config.JdbcDataSourceConfig;
import org.tinger.data.jdbc.config.JdbcDataSourceConfigs;
import org.tinger.data.jdbc.config.JdbcDataSources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class Config {
    @Bean
    public JdbcDataSources sources() {
        JdbcDataSources sources = new JdbcDataSources();
        sources.setConfig(configs());
        return sources;
    }

    private JdbcDataSourceConfigs configs() {
        JdbcDataSourceConfigs configs = new JdbcDataSourceConfigs();
        HashMap<String, JdbcDataSourceConfig> map = new HashMap<>();
        JdbcDataSourceConfig tinger = JdbcDataSourceConfig.builder()
                .jdbcUrl("jdbc:mysql//localhost:3306/tinger")
                .username("root")
                .password("123456")
                .driver("com.mysql.cj.jdbc.driver")
                .alias(Arrays.asList("test1", "test2"))
                .build();
        map.put("tinger", tinger);
        configs.setDatabases(map);
        HashMap<String, List<String>> map1 = new HashMap<>();
        map1.put("shard", Arrays.asList("test1", "test2"));
        configs.setSharding(map1);
        return configs;
    }


}
