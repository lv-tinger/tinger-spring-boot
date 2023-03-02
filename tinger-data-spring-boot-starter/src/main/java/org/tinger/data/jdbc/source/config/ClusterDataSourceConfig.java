package org.tinger.data.jdbc.source.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
public class ClusterDataSourceConfig extends AbstractDataSourceConfig {
    @Serial
    private static final long serialVersionUID = 1546464067752171293L;
    private List<List<JdbcDataSourceConfig>> configs;
}
