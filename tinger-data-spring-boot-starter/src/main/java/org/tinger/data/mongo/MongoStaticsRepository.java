package org.tinger.data.mongo;

import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.repo.TingerStaticsRepository;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Limited;
import org.tinger.data.core.tsql.Ordered;
import org.tinger.data.core.tsql.Update;
import org.tinger.data.jdbc.TingerJdbcRepository;

import java.util.Collection;
import java.util.List;

public class MongoStaticsRepository<T, K> extends TingerJdbcRepository<T, K> implements TingerStaticsRepository<T, K> {

    private final String datatable;
    private final String database;
    private final String datasource;


    protected MongoStaticsRepository() {
        super();

        Class<?> repositoryType = this.getClass();
        TingerDataTable tableAnnotation = repositoryType.getDeclaredAnnotation(TingerDataTable.class);
        TingerDataSource sourceAnnotation = repositoryType.getDeclaredAnnotation(TingerDataSource.class);
        TingerDatabase baseAnnotation = repositoryType.getDeclaredAnnotation(TingerDatabase.class);

        this.datatable = tableAnnotation.value();
        this.datasource = sourceAnnotation.value();
        this.database = baseAnnotation.value();
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
