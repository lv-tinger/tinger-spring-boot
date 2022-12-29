package org.tinger.serv.apps.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class AppAgreement implements JdbcEntity<String> {
    private String id;
    private String appId;
    private String key;
    private String content;
}