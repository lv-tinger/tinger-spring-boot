package org.tinger.data.core.repo;

import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Limited;
import org.tinger.data.core.tsql.Ordered;
import org.tinger.data.core.tsql.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TingerDynamicRepository<T, K, S> {
    T create(S shard, T document);

    List<T> create(S shard, List<T> documents);

    T update(S shard, T document);

    List<T> update(S shard, Update update, Criteria criteria);

    List<T> update(S shard, List<T> documents);

    T delete(S shard, K id);

    List<T> delete(S shard, Criteria criteria);

    List<T> delete(S shard, List<K> ids);

    T select(S shard, K id);

    Map<K, T> select(S shard, Collection<K> ids);

    List<T> select(S shard, Criteria criteria);

    List<T> select(S shard, Criteria criteria, Ordered ordered, Limited limited);
}