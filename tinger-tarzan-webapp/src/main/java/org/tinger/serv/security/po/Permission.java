package org.tinger.serv.security.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.tinger.jdbc.core.JdbcEntity;

import java.util.Date;

@SuperBuilder
@Getter
@Setter
public class Permission extends JdbcEntity<String> {
    private Long userId;
    private String imei;
    private String driver;
    private Date loginTime;
    private Date expiryTime;

    public static String generateId(Long userId, String imei) {
        return userId + "_" + imei;
    }
}