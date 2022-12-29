package org.tinger.serv.apps.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class AppViewPart implements JdbcEntity<String> {
    private String id;
    private String appId;
    private String type;
    private String title;
    private String summary;
    private String target;
}
