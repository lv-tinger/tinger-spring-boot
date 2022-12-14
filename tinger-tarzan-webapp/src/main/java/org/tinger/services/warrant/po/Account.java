package org.tinger.services.warrant.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.jdbc.anno.JdbcDataColumn;
import org.tinger.jdbc.anno.JdbcPrimaryKey;
import org.tinger.jdbc.core.JdbcEntity;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements JdbcEntity<Long> {
    @JdbcPrimaryKey
    private Long id;
    private String username;
    private String password;
    @JdbcDataColumn(name = "salt_code")
    private String saltCode;
}