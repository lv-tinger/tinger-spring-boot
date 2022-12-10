package org.tinger.jdbc.repository;

import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;

import java.util.List;

/**
 * Created by tinger on 2022-10-16
 */
public interface DynamicRepository<T, K> {
    T select(Object shard, K id);

    T update(Object shard, T document);

    T create(Object shard, T document);

    T upsert(Object shard, T document);

    T delete(Object shard, T document);

    List<T> delete(Object shard, Criteria criteria);

    List<T> update(Object shard, Update update, Criteria criteria);

    List<T> select(Object shard, Criteria criteria);
}