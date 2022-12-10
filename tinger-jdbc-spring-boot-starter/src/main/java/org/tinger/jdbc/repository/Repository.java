package org.tinger.jdbc.repository;

import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;

import java.util.List;

/**
 * Created by tinger on 2022-10-15
 */
public interface Repository<T, K> {
    T select(K id);

    T update(T document);

    T create(T document);

    T upsert(T document);

    T delete(T document);

    List<T> delete(Criteria criteria);

    List<T> update(Update update, Criteria criteria);

    List<T> select();

    List<T> select(Criteria criteria);
}