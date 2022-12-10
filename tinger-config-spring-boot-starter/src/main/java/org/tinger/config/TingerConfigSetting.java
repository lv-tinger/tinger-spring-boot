package org.tinger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("tinger")
public class TingerConfigSetting {
    private String appName;
    private String secretKey;
}