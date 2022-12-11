package org.tinger.serv.security.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.tinger.jdbc.core.JdbcEntity;

@Getter
@Setter
@SuperBuilder
public class Identity extends JdbcEntity<Long> {
    public Integer userType;
    public Integer status;
}