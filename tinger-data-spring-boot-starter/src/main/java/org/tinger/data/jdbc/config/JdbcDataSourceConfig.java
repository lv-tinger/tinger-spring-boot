package org.tinger.data.jdbc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdbcDataSourceConfig {
    private String driver;
    private String jdbcUrl;
    private String username;
    private String password;
    private List<String> alias;
}
