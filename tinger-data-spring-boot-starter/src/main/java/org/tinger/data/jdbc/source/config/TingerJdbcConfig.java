package org.tinger.data.jdbc.source.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TingerJdbcConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 8385294295804429332L;
    private JdbcCommonConfig commonConfig;
    private List<SingletDataSourceConfig> singlets;
    private List<ClusterDataSourceConfig> clusters;
}
