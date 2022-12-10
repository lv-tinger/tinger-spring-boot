package org.tinger.jdbc.repository;

import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by tinger on 2022-10-16
 */
public interface ShardingRepository<T, S, K> {
    T select(S shard, K id);

    T update(S shard, T document);

    T create(S shard, T document);

    T upsert(S shard, T document);

    T delete(S shard, T document);

    List<T> delete(S shard, Criteria criteria);

    List<T> update(S shard, Update update, Criteria criteria);

    List<T> select(S shard, Criteria criteria);

    DataSource calculateDatasource(S shard);

    String calculateDatabase(S shard);

    String calculateDatatable(S shard);
}