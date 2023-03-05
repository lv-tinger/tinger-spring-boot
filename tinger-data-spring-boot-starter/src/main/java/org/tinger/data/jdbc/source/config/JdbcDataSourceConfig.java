package org.tinger.data.jdbc.source.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdbcDataSourceConfig {
    private String driver;
    private String jdbcUrl;
    private String username;
    private String password;
    private Integer position;
    private Integer weight;
}