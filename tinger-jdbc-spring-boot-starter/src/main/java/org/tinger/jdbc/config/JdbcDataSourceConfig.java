package org.tinger.jdbc.config;

import lombok.Data;

import java.util.List;

@Data
public class JdbcDataSourceConfig {
    private String driver;
    private String jdbcUrl;
    private String username;
    private String password;
    private List<String> alias;
}
