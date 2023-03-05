package org.tinger.data.jdbc.source.config;

import org.tinger.common.utils.JsonUtils;
import org.tinger.common.utils.ResourceUtils;

public class DefaultTingerJdbcConfigLoader implements TingerJdbcConfigLoader {
    @Override
    public TingerJdbcConfig load() {
        String content = ResourceUtils.readText("classpath://config/jdbc.config.json");
        TingerJdbcConfig config = JsonUtils.fromJson(content, TingerJdbcConfig.class);
        if (config == null) {
            throw new RuntimeException();
        }
        return config;
    }
}