package org.tinger.data.core.tsql;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Update;

/**
 * Created by tinger on 2023-02-02
 */
public interface Updatable {
    String name();

    TingerMetadata<?> update();

    Update set();

    Criteria where();
}
