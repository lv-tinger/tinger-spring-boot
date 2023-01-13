package org.tinger.data.core.repo;

import org.tinger.data.core.queryable.Criteria;
import org.tinger.data.core.queryable.Update;

import java.util.List;
import java.util.Map;

public interface TingerDynamicRepository<T, K, S> {
    T select(S shard, K id);

    Map<K, T> select(S shard, List<K> id);

    T update(S shard, T document);

    List<T> update(S shard, List<T> documents);

    T create(S shard, T document);

    List<T> create(S shard, List<T> documents);

    T upsert(S shard, T document);

    T delete(S shard, T document);

    List<T> delete(S shard, List<T> document);

    List<T> delete(S shard, Criteria criteria);

    List<T> update(S shard, Update update, Criteria criteria);

    List<T> select(S shard, Criteria criteria);
}