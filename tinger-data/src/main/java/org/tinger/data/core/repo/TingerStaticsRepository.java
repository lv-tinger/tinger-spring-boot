package org.tinger.data.core.repo;

import org.tinger.data.core.queryable.Criteria;
import org.tinger.data.core.queryable.Queryable;
import org.tinger.data.core.queryable.Update;

import java.util.List;

public interface TingerStaticsRepository<T, K> {
    T select(K id);

    T update(T document);

    List<T> update(List<T> documents);

    T create(T document);

    List<T> create(List<T> documents);

    T upsert(T document);

    T delete(T document);

    List<T> delete(Criteria criteria);

    List<T> update(Update update, Criteria criteria);

    List<T> select();

    List<T> select(Criteria criteria);

    List<T> select(Queryable queryable);
}