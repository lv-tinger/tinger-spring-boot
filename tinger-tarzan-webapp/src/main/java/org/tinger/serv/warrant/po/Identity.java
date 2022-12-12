package org.tinger.serv.warrant.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.jdbc.core.JdbcEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Identity implements JdbcEntity<Long> {
    private Long id;
    private Integer userType;
    private Integer status;
}