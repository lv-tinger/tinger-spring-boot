package org.tinger.data.jdbc.statement.template.queryable;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Update;

/**
 * Created by tinger on 2023-02-02
 */
public interface Updatable {
    TingerMetadata<?> update();

    Update set();

    Criteria where();
}
