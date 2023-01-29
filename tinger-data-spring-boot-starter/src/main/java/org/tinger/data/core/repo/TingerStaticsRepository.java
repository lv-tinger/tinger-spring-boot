package org.tinger.data.core.repo;

import org.tinger.data.core.tsql.*;

import java.util.Collection;
import java.util.List;

public interface TingerStaticsRepository<T, K> {
    T create(T document);

    List<T> create(List<T> documents);

    T update(T document);

    List<T> update(List<T> documents);

    T upsert(T document);

    List<T> update(Update update, Criteria criteria);

    T delete(K id);

    List<T> delete(List<K> ids);

    List<T> select();

    T select(K id);

    List<T> select(Collection<K> ids);

    List<T> select(Criteria criteria);

    List<T> select(Criteria criteria, Ordered ordered, Limited limited);
}