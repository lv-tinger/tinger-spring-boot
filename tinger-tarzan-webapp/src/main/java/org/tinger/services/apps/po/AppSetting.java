package org.tinger.services.apps.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.jdbc.anno.JdbcDataColumn;
import org.tinger.jdbc.anno.JdbcPrimaryKey;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSetting implements JdbcEntity<String> {
    @JdbcPrimaryKey
    private String id;
    private String name;
    private String icon;
    private String slogan;
    @JdbcDataColumn(name ="app_key")
    private String appKey;
    @JdbcDataColumn(name = "app_secret")
    private String appSecret;
    @JdbcDataColumn(name = "secret_key")
    private String secretKey;
}
