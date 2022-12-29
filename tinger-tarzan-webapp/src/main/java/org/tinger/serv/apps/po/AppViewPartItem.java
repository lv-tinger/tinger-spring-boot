package org.tinger.serv.apps.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class AppViewPartItem implements JdbcEntity<String> {
    private String id;
    private String partId;
    private String title;
    private String summary;
    private String shining;
    private String graying;
    private String target;
    private Integer sorted;
}