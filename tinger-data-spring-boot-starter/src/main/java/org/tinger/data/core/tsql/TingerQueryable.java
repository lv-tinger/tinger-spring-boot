package org.tinger.data.core.tsql;

import org.tinger.data.core.meta.TingerMetadata;

public abstract class TingerQueryable implements Queryable {

    private final String name;
    private final TingerMetadata<?> metadata;

    protected TingerQueryable(String name, TingerMetadata<?> metadata) {
        this.name = name;
        this.metadata = metadata;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public TingerMetadata<?> metadata() {
        return this.metadata;
    }
}
