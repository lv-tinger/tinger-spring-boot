package org.tinger.serv.apps.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class AppSetting implements JdbcEntity<String> {
    private String id;
    private String name;
    private String icon;
    private String slogan;
    private String appKey;
    private String appSecret;
    private String secretKey;
}
