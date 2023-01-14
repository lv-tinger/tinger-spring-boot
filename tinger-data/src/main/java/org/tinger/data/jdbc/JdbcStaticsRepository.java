package org.tinger.data.jdbc;

import org.springframework.beans.factory.InitializingBean;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.queryable.Criteria;
import org.tinger.data.core.queryable.Queryable;
import org.tinger.data.core.queryable.Update;
import org.tinger.data.core.repo.TingerStaticsRepository;

import java.util.List;

public class JdbcStaticsRepository<T, K> implements TingerStaticsRepository<T, K> {
    private TingerMetadata metadata;

    @Override
    public T select(K id) {
        return null;
    }

    @Override
    public T update(T document) {
        return null;
    }

    @Override
    public List<T> update(List<T> documents) {
        return null;
    }

    @Override
    public T create(T document) {
        return null;
    }

    @Override
    public List<T> create(List<T> documents) {
        return null;
    }

    @Override
    public T upsert(T document) {
        return null;
    }

    @Override
    public T delete(T document) {
        return null;
    }

    @Override
    public List<T> delete(Criteria criteria) {
        return null;
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        return null;
    }

    @Override
    public List<T> select() {
        return null;
    }

    @Override
    public List<T> select(Criteria criteria) {
        return null;
    }

    @Override
    public List<T> select(Queryable queryable) {
        return null;
    }
}
