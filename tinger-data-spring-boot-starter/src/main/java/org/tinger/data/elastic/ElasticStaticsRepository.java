package org.tinger.data.elastic;

import org.tinger.data.core.tsql.*;
import org.tinger.data.core.repo.TingerStaticsRepository;

import java.util.Collection;
import java.util.List;

public class ElasticStaticsRepository<K,T> implements TingerStaticsRepository<T,K> {

    @Override
    public T create(T document) {
        return null;
    }

    @Override
    public List<T> create(List<T> documents) {
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
    public T upsert(T document) {
        return null;
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        return null;
    }

    @Override
    public T delete(K id) {
        return null;
    }

    @Override
    public List<T> delete(List<K> ids) {
        return null;
    }

    @Override
    public List<T> select() {
        return null;
    }

    @Override
    public T select(K id) {
        return null;
    }

    @Override
    public List<T> select(Collection<K> ids) {
        return null;
    }

    @Override
    public List<T> select(Criteria criteria) {
        return null;
    }

    @Override
    public List<T> select(Criteria criteria, Ordered ordered, Limited limited) {
        return null;
    }
}
