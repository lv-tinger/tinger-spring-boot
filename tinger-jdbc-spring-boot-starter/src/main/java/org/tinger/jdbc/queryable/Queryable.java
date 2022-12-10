package org.tinger.jdbc.queryable;

import lombok.Builder;
import lombok.Data;

/**
 * Created by tinger on 2022-10-16
 */
@Builder
@Data
public class Queryable {
    private Criteria criteria;
    private Ordered sort;
    private Integer limit;
    private Integer skip;
}