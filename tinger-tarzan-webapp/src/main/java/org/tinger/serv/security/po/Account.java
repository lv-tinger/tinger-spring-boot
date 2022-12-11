package org.tinger.serv.security.po;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.tinger.jdbc.anno.JdbcDataColumn;
import org.tinger.jdbc.core.JdbcEntity;

@Getter
@SuperBuilder
public class Account extends JdbcEntity<Long> {
    private String username;
    private String password;
    @JdbcDataColumn(name = "salt_code")
    private String saltCode;
}