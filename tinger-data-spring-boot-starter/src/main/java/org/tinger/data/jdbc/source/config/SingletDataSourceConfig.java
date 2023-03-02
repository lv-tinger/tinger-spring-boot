package org.tinger.data.jdbc.source.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
public class SingletDataSourceConfig extends AbstractDataSourceConfig {
    @Serial
    private static final long serialVersionUID = -1169380355739371968L;
    private List<JdbcDataSourceConfig> configs;
}