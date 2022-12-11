package org.tinger.services.warrant.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.jdbc.core.JdbcEntity;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements JdbcEntity<String> {
    private String id;
    private Long userId;
    private String imei;
    private String driver;
    private Date loginTime;
    private Date expiryTime;

    public static String generateId(Long userId, String imei) {
        return userId + "_" + imei;
    }
}