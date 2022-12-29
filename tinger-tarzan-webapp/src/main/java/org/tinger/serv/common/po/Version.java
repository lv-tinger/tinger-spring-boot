package org.tinger.serv.common.po;

import lombok.Builder;
import lombok.Data;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
public class Version implements JdbcEntity<String> {
    private String id;
    private String key;
    private Integer version;
}
