package org.tinger.serv.common.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class Config implements JdbcEntity<String> {
    private String id;
    private String key;
    private String content;
    private String title;
}
