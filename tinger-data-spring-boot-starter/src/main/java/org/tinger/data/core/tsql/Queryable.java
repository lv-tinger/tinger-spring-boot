package org.tinger.data.core.tsql;

import lombok.Builder;
import lombok.Data;

/**
 * Created by tinger on 2022-10-16
 */
@Builder
@Data
public class Queryable {
    private Criteria criteria;
    private Ordered ordered;
    private Limited limited;

    public Queryable() {
    }

    public Queryable(Criteria criteria) {
        this.criteria = criteria;
    }
}