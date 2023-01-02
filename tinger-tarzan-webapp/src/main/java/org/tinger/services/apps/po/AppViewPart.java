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
public class AppViewPart implements JdbcEntity<String> {
    @JdbcPrimaryKey
    private String id;
    @JdbcDataColumn(name = "app_id")
    private String appId;
    private String type;
    private String title;
    private String summary;
    private String target;
}
