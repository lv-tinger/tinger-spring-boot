package org.tinger.data.jdbc.statement.template.queryable;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Limited;
import org.tinger.data.core.tsql.Ordered;

/**
 * Created by tinger on 2023-02-02
 */
public interface Queryable {

    TingerMetadata<?> metadata();

    default Criteria where() {
        return null;
    }

    default Ordered order() {
        return null;
    }

    default Limited limit() {
        return null;
    }
}