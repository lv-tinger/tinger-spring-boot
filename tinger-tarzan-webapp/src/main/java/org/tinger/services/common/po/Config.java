package org.tinger.services.common.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.jdbc.anno.JdbcPrimaryKey;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config implements JdbcEntity<String> {
    @JdbcPrimaryKey
    private String id;
    private String key;
    private String content;
    private String title;
}
